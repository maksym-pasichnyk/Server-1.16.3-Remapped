package org.apache.commons.lang3.concurrent;

public interface CircuitBreaker<T> {
  boolean isOpen();
  
  boolean isClosed();
  
  boolean checkState();
  
  void close();
  
  void open();
  
  boolean incrementAndCheckState(T paramT);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\CircuitBreaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */