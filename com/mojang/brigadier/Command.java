package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface Command<S> {
  public static final int SINGLE_SUCCESS = 1;
  
  int run(CommandContext<S> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */