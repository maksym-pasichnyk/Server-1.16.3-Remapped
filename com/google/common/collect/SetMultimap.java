package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
  Set<V> get(@Nullable K paramK);
  
  @CanIgnoreReturnValue
  Set<V> removeAll(@Nullable Object paramObject);
  
  @CanIgnoreReturnValue
  Set<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  Set<Map.Entry<K, V>> entries();
  
  Map<K, Collection<V>> asMap();
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\SetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */