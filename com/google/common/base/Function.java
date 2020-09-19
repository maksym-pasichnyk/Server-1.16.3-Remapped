package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.function.Function;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public interface Function<F, T> extends Function<F, T> {
  @Nullable
  @CanIgnoreReturnValue
  T apply(@Nullable F paramF);
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */