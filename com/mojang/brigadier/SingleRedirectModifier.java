package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface SingleRedirectModifier<S> {
  S apply(CommandContext<S> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\SingleRedirectModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */