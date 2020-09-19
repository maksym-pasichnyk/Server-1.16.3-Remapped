package io.netty.util.concurrent;

public interface EventExecutorChooserFactory {
  EventExecutorChooser newChooser(EventExecutor[] paramArrayOfEventExecutor);
  
  public static interface EventExecutorChooser {
    EventExecutor next();
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\EventExecutorChooserFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */