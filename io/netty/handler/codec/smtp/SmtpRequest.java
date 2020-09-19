package io.netty.handler.codec.smtp;

import java.util.List;

public interface SmtpRequest {
  SmtpCommand command();
  
  List<CharSequence> parameters();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */