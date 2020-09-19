/*    */ package com.mojang.brigadier.suggestion;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.StringRange;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuggestionsBuilder
/*    */ {
/*    */   private final String input;
/*    */   private final int start;
/*    */   private final String remaining;
/* 17 */   private final List<Suggestion> result = new ArrayList<>();
/*    */   
/*    */   public SuggestionsBuilder(String input, int start) {
/* 20 */     this.input = input;
/* 21 */     this.start = start;
/* 22 */     this.remaining = input.substring(start);
/*    */   }
/*    */   
/*    */   public String getInput() {
/* 26 */     return this.input;
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 30 */     return this.start;
/*    */   }
/*    */   
/*    */   public String getRemaining() {
/* 34 */     return this.remaining;
/*    */   }
/*    */   
/*    */   public Suggestions build() {
/* 38 */     return Suggestions.create(this.input, this.result);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Suggestions> buildFuture() {
/* 42 */     return CompletableFuture.completedFuture(build());
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(String text) {
/* 46 */     if (text.equals(this.remaining)) {
/* 47 */       return this;
/*    */     }
/* 49 */     this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), text));
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(String text, Message tooltip) {
/* 54 */     if (text.equals(this.remaining)) {
/* 55 */       return this;
/*    */     }
/* 57 */     this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), text, tooltip));
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(int value) {
/* 62 */     this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), value));
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder suggest(int value, Message tooltip) {
/* 67 */     this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), value, tooltip));
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder add(SuggestionsBuilder other) {
/* 72 */     this.result.addAll(other.result);
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder createOffset(int start) {
/* 77 */     return new SuggestionsBuilder(this.input, start);
/*    */   }
/*    */   
/*    */   public SuggestionsBuilder restart() {
/* 81 */     return new SuggestionsBuilder(this.input, this.start);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\suggestion\SuggestionsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */