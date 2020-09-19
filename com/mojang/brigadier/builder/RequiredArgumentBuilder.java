/*    */ package com.mojang.brigadier.builder;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ 
/*    */ 
/*    */ public class RequiredArgumentBuilder<S, T>
/*    */   extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>>
/*    */ {
/*    */   private final String name;
/*    */   private final ArgumentType<T> type;
/* 14 */   private SuggestionProvider<S> suggestionsProvider = null;
/*    */   
/*    */   private RequiredArgumentBuilder(String name, ArgumentType<T> type) {
/* 17 */     this.name = name;
/* 18 */     this.type = type;
/*    */   }
/*    */   
/*    */   public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
/* 22 */     return new RequiredArgumentBuilder<>(name, type);
/*    */   }
/*    */   
/*    */   public RequiredArgumentBuilder<S, T> suggests(SuggestionProvider<S> provider) {
/* 26 */     this.suggestionsProvider = provider;
/* 27 */     return getThis();
/*    */   }
/*    */   
/*    */   public SuggestionProvider<S> getSuggestionsProvider() {
/* 31 */     return this.suggestionsProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected RequiredArgumentBuilder<S, T> getThis() {
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public ArgumentType<T> getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */   
/*    */   public ArgumentCommandNode<S, T> build() {
/* 48 */     ArgumentCommandNode<S, T> result = new ArgumentCommandNode(getName(), getType(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider());
/*    */     
/* 50 */     for (CommandNode<S> argument : getArguments()) {
/* 51 */       result.addChild(argument);
/*    */     }
/*    */     
/* 54 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\builder\RequiredArgumentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */