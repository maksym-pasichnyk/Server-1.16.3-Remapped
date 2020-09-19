package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
@GwtIncompatible
public interface Interner<E> {
  @CanIgnoreReturnValue
  E intern(E paramE);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Interner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */