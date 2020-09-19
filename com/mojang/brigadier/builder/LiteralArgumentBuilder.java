/*    */ package com.mojang.brigadier.builder;
/*    */ 
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ 
/*    */ 
/*    */ public class LiteralArgumentBuilder<S>
/*    */   extends ArgumentBuilder<S, LiteralArgumentBuilder<S>>
/*    */ {
/*    */   private final String literal;
/*    */   
/*    */   protected LiteralArgumentBuilder(String literal) {
/* 13 */     this.literal = literal;
/*    */   }
/*    */   
/*    */   public static <S> LiteralArgumentBuilder<S> literal(String name) {
/* 17 */     return new LiteralArgumentBuilder<>(name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected LiteralArgumentBuilder<S> getThis() {
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public String getLiteral() {
/* 26 */     return this.literal;
/*    */   }
/*    */ 
/*    */   
/*    */   public LiteralCommandNode<S> build() {
/* 31 */     LiteralCommandNode<S> result = new LiteralCommandNode(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());
/*    */     
/* 33 */     for (CommandNode<S> argument : getArguments()) {
/* 34 */       result.addChild(argument);
/*    */     }
/*    */     
/* 37 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\builder\LiteralArgumentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */