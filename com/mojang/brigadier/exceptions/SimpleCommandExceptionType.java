/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class SimpleCommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Message message;
/*    */   
/*    */   public SimpleCommandExceptionType(Message message) {
/* 13 */     this.message = message;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create() {
/* 17 */     return new CommandSyntaxException(this, this.message);
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader reader) {
/* 21 */     return new CommandSyntaxException(this, this.message, reader.getString(), reader.getCursor());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return this.message.getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\exceptions\SimpleCommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */