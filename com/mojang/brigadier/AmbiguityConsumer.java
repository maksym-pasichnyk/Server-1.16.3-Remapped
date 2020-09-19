package com.mojang.brigadier;

import com.mojang.brigadier.tree.CommandNode;
import java.util.Collection;

@FunctionalInterface
public interface AmbiguityConsumer<S> {
  void ambiguous(CommandNode<S> paramCommandNode1, CommandNode<S> paramCommandNode2, CommandNode<S> paramCommandNode3, Collection<String> paramCollection);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\AmbiguityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */