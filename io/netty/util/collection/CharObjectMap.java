package io.netty.util.collection;

import java.util.Map;

public interface CharObjectMap<V> extends Map<Character, V> {
  V get(char paramChar);
  
  V put(char paramChar, V paramV);
  
  V remove(char paramChar);
  
  Iterable<PrimitiveEntry<V>> entries();
  
  boolean containsKey(char paramChar);
  
  public static interface PrimitiveEntry<V> {
    char key();
    
    V value();
    
    void setValue(V param1V);
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\collection\CharObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */