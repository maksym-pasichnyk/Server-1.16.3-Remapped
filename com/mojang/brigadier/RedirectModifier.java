package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;

@FunctionalInterface
public interface RedirectModifier<S> {
  Collection<S> apply(CommandContext<S> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\RedirectModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */