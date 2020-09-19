package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public interface AsyncFunction<I, O> {
  ListenableFuture<O> apply(@Nullable I paramI) throws Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AsyncFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */