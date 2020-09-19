package io.netty.util;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface AsyncMapping<IN, OUT> {
  Future<OUT> map(IN paramIN, Promise<OUT> paramPromise);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\AsyncMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */