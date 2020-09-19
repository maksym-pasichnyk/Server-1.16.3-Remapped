/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class DynamicNCommandExceptionType
/*    */   implements CommandExceptionType
/*    */ {
/*    */   private final Function function;
/*    */   
/*    */   public DynamicNCommandExceptionType(Function function) {
/* 13 */     this.function = function;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException create(Object a, Object... args) {
/* 17 */     return new CommandSyntaxException(this, this.function.apply(args));
/*    */   }
/*    */   
/*    */   public CommandSyntaxException createWithContext(ImmutableStringReader reader, Object... args) {
/* 21 */     return new CommandSyntaxException(this, this.function.apply(args), reader.getString(), reader.getCursor());
/*    */   }
/*    */   
/*    */   public static interface Function {
/*    */     Message apply(Object[] param1ArrayOfObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\exceptions\DynamicNCommandExceptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */