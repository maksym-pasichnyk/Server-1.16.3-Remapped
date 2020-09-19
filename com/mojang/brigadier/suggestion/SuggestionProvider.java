package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface SuggestionProvider<S> {
  CompletableFuture<Suggestions> getSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) throws CommandSyntaxException;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\suggestion\SuggestionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */