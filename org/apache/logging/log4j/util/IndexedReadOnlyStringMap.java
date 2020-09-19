package org.apache.logging.log4j.util;

public interface IndexedReadOnlyStringMap extends ReadOnlyStringMap {
  String getKeyAt(int paramInt);
  
  <V> V getValueAt(int paramInt);
  
  int indexOfKey(String paramString);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\IndexedReadOnlyStringMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */