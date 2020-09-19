package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.LogEvent;

public interface PurgePolicy {
  void purge();
  
  void update(String paramString, LogEvent paramLogEvent);
  
  void initialize(RoutingAppender paramRoutingAppender);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\routing\PurgePolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */