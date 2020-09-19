package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext {
  Object getExternalContext();
  
  ExtendedLogger getLogger(String paramString);
  
  ExtendedLogger getLogger(String paramString, MessageFactory paramMessageFactory);
  
  boolean hasLogger(String paramString);
  
  boolean hasLogger(String paramString, MessageFactory paramMessageFactory);
  
  boolean hasLogger(String paramString, Class<? extends MessageFactory> paramClass);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\LoggerContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */