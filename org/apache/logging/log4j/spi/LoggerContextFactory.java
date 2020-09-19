package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory {
  LoggerContext getContext(String paramString, ClassLoader paramClassLoader, Object paramObject, boolean paramBoolean);
  
  LoggerContext getContext(String paramString1, ClassLoader paramClassLoader, Object paramObject, boolean paramBoolean, URI paramURI, String paramString2);
  
  void removeContext(LoggerContext paramLoggerContext);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\LoggerContextFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */