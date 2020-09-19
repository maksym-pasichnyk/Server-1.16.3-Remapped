package io.netty.util.concurrent;

public interface ThreadProperties {
  Thread.State state();
  
  int priority();
  
  boolean isInterrupted();
  
  boolean isDaemon();
  
  String name();
  
  long id();
  
  StackTraceElement[] stackTrace();
  
  boolean isAlive();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\ThreadProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */