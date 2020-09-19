package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Table<R, C, V> {
  boolean contains(@Nullable @CompatibleWith("R") Object paramObject1, @Nullable @CompatibleWith("C") Object paramObject2);
  
  boolean containsRow(@Nullable @CompatibleWith("R") Object paramObject);
  
  boolean containsColumn(@Nullable @CompatibleWith("C") Object paramObject);
  
  boolean containsValue(@Nullable @CompatibleWith("V") Object paramObject);
  
  V get(@Nullable @CompatibleWith("R") Object paramObject1, @Nullable @CompatibleWith("C") Object paramObject2);
  
  boolean isEmpty();
  
  int size();
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
  
  void clear();
  
  @Nullable
  @CanIgnoreReturnValue
  V put(R paramR, C paramC, V paramV);
  
  void putAll(Table<? extends R, ? extends C, ? extends V> paramTable);
  
  @Nullable
  @CanIgnoreReturnValue
  V remove(@Nullable @CompatibleWith("R") Object paramObject1, @Nullable @CompatibleWith("C") Object paramObject2);
  
  Map<C, V> row(R paramR);
  
  Map<R, V> column(C paramC);
  
  Set<Cell<R, C, V>> cellSet();
  
  Set<R> rowKeySet();
  
  Set<C> columnKeySet();
  
  Collection<V> values();
  
  Map<R, Map<C, V>> rowMap();
  
  Map<C, Map<R, V>> columnMap();
  
  public static interface Cell<R, C, V> {
    @Nullable
    R getRowKey();
    
    @Nullable
    C getColumnKey();
    
    @Nullable
    V getValue();
    
    boolean equals(@Nullable Object param1Object);
    
    int hashCode();
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Table.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */