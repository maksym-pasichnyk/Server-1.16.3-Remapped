package com.mojang.datafixers.types.families;

import com.mojang.datafixers.RewriteResult;

public interface Algebra {
  RewriteResult<?, ?> apply(int paramInt);
  
  String toString(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\families\Algebra.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */