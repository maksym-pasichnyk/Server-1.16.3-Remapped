/*    */ package com.mojang.brigadier;
/*    */ 
/*    */ 
/*    */ public class LiteralMessage
/*    */   implements Message
/*    */ {
/*    */   private final String string;
/*    */   
/*    */   public LiteralMessage(String string) {
/* 10 */     this.string = string;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 15 */     return this.string;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return this.string;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\LiteralMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */