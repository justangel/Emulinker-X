package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.Chat_Notification;
import org.emulinker.kaillera.controller.v086.protocol.Chat_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;

public abstract class Chat extends V086Message {
   public static final byte ID = 7;
   private String userName;
   private String message;
   private ByteBuffer body;

   public Chat(int messageNumber, String userName, String message) throws MessageFormatException {
      super(messageNumber);
      this.userName = userName;
      this.message = message;
   }

   public byte getID() {
      return (byte)7;
   }

   public abstract String getDescription();

   public String getUserName() {
      return this.userName;
   }

   public String getMessage() {
      return this.message;
   }

   public abstract String toString();

   public int getBodyLength() {
      return this.userName.length() + this.message.length() + 2;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      EmuUtil.writeString(buffer, this.message, 0, charset);
   }

   public static Chat parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 3) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 2) {
            throw new ParseException("Failed byte count validation!");
         } else {
            String message = EmuUtil.readString(buffer, 0, charset);
            return (Chat)(userName.length() == 0?new Chat_Request(messageNumber, message):new Chat_Notification(messageNumber, userName, message));
         }
      }
   }
}
