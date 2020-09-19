/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ArgumentType<T>
/*    */ {
/*    */   T parse(StringReader paramStringReader) throws CommandSyntaxException;
/*    */   
/*    */   default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 20 */     return Suggestions.empty();
/*    */   }
/*    */   
/*    */   default Collection<String> getExamples() {
/* 24 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\ArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */