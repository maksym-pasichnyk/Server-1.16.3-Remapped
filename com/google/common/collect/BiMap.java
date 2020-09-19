package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
  @Nullable
  @CanIgnoreReturnValue
  V put(@Nullable K paramK, @Nullable V paramV);
  
  @Nullable
  @CanIgnoreReturnValue
  V forcePut(@Nullable K paramK, @Nullable V paramV);
  
  void putAll(Map<? extends K, ? extends V> paramMap);
  
  Set<V> values();
  
  BiMap<V, K> inverse();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\BiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */