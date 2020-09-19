package io.netty.handler.codec.smtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface SmtpContent extends ByteBufHolder {
  SmtpContent copy();
  
  SmtpContent duplicate();
  
  SmtpContent retainedDuplicate();
  
  SmtpContent replace(ByteBuf paramByteBuf);
  
  SmtpContent retain();
  
  SmtpContent retain(int paramInt);
  
  SmtpContent touch();
  
  SmtpContent touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */