package io.netty.util.internal;

import java.util.Queue;

public interface PriorityQueue<T> extends Queue<T> {
  boolean removeTyped(T paramT);
  
  boolean containsTyped(T paramT);
  
  void priorityChanged(T paramT);
  
  void clearIgnoringIndexes();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\PriorityQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */