package org.apache.logging.log4j.core;

public interface ErrorHandler {
  void error(String paramString);
  
  void error(String paramString, Throwable paramThrowable);
  
  void error(String paramString, LogEvent paramLogEvent, Throwable paramThrowable);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\ErrorHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */