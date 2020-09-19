package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V> {
  List<V> get(@Nullable K paramK);
  
  @CanIgnoreReturnValue
  List<V> removeAll(@Nullable Object paramObject);
  
  @CanIgnoreReturnValue
  List<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  Map<K, Collection<V>> asMap();
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */