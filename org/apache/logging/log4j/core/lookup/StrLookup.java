package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup {
  public static final String CATEGORY = "Lookup";
  
  String lookup(String paramString);
  
  String lookup(LogEvent paramLogEvent, String paramString);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\lookup\StrLookup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */