package org.apache.logging.log4j.core.appender.rolling;

public interface RolloverStrategy {
  RolloverDescription rollover(RollingFileManager paramRollingFileManager) throws SecurityException;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\RolloverStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */