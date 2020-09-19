package io.netty.util.collection;

import java.util.Map;

public interface ShortObjectMap<V> extends Map<Short, V> {
  V get(short paramShort);
  
  V put(short paramShort, V paramV);
  
  V remove(short paramShort);
  
  Iterable<PrimitiveEntry<V>> entries();
  
  boolean containsKey(short paramShort);
  
  public static interface PrimitiveEntry<V> {
    short key();
    
    V value();
    
    void setValue(V param1V);
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\collection\ShortObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */