package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.ScheduledFuture;

@Beta
@GwtIncompatible
public interface ListenableScheduledFuture<V> extends ScheduledFuture<V>, ListenableFuture<V> {}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ListenableScheduledFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */