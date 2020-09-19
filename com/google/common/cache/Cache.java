package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;

@GwtCompatible
public interface Cache<K, V> {
  @Nullable
  V getIfPresent(Object paramObject);
  
  V get(K paramK, Callable<? extends V> paramCallable) throws ExecutionException;
  
  ImmutableMap<K, V> getAllPresent(Iterable<?> paramIterable);
  
  void put(K paramK, V paramV);
  
  void putAll(Map<? extends K, ? extends V> paramMap);
  
  void invalidate(Object paramObject);
  
  void invalidateAll(Iterable<?> paramIterable);
  
  void invalidateAll();
  
  long size();
  
  CacheStats stats();
  
  ConcurrentMap<K, V> asMap();
  
  void cleanUp();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\Cache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */