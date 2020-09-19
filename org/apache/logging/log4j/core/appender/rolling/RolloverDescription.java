package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.action.Action;

public interface RolloverDescription {
  String getActiveFileName();
  
  boolean getAppend();
  
  Action getSynchronous();
  
  Action getAsynchronous();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\RolloverDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */