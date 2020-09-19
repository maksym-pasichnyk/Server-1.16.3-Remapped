package org.apache.logging.log4j.core.pattern;

public interface PatternConverter {
  public static final String CATEGORY = "Converter";
  
  void format(Object paramObject, StringBuilder paramStringBuilder);
  
  String getName();
  
  String getStyleClass(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\PatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */