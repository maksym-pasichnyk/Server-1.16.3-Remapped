/*    */ package com.mojang.brigadier.tree;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RootCommandNode<S>
/*    */   extends CommandNode<S>
/*    */ {
/*    */   public RootCommandNode() {
/* 20 */     super(null, c -> true, null, s -> Collections.singleton(s.getSource()), false);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 25 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUsageText() {
/* 30 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {}
/*    */ 
/*    */   
/*    */   public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 39 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidInput(String input) {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 49 */     if (this == o) return true; 
/* 50 */     if (!(o instanceof RootCommandNode)) return false; 
/* 51 */     return super.equals(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public ArgumentBuilder<S, ?> createBuilder() {
/* 56 */     throw new IllegalStateException("Cannot convert root into a builder");
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getSortedKey() {
/* 61 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 66 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "<root>";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\tree\RootCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */