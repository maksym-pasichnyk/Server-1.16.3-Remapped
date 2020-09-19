package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface PathCondition {
  void beforeFileTreeWalk();
  
  boolean accept(Path paramPath1, Path paramPath2, BasicFileAttributes paramBasicFileAttributes);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\PathCondition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */