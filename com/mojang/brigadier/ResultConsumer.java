package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;

@FunctionalInterface
public interface ResultConsumer<S> {
  void onCommandComplete(CommandContext<S> paramCommandContext, boolean paramBoolean, int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\ResultConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */