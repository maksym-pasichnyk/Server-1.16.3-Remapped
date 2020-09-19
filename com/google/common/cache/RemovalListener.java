package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface RemovalListener<K, V> {
  void onRemoval(RemovalNotification<K, V> paramRemovalNotification);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\RemovalListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */