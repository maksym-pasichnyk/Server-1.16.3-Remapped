package io.netty.resolver;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import java.io.Closeable;
import java.util.List;

public interface NameResolver<T> extends Closeable {
  Future<T> resolve(String paramString);
  
  Future<T> resolve(String paramString, Promise<T> paramPromise);
  
  Future<List<T>> resolveAll(String paramString);
  
  Future<List<T>> resolveAll(String paramString, Promise<List<T>> paramPromise);
  
  void close();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\NameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */