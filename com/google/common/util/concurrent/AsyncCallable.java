package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public interface AsyncCallable<V> {
  ListenableFuture<V> call() throws Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AsyncCallable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */