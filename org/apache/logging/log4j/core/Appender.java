package org.apache.logging.log4j.core;

import java.io.Serializable;

public interface Appender extends LifeCycle {
  public static final String ELEMENT_TYPE = "appender";
  
  void append(LogEvent paramLogEvent);
  
  String getName();
  
  Layout<? extends Serializable> getLayout();
  
  boolean ignoreExceptions();
  
  ErrorHandler getHandler();
  
  void setHandler(ErrorHandler paramErrorHandler);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\Appender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */