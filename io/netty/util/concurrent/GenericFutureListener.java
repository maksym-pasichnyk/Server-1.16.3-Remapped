package io.netty.util.concurrent;

import java.util.EventListener;

public interface GenericFutureListener<F extends Future<?>> extends EventListener {
  void operationComplete(F paramF) throws Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\GenericFutureListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */