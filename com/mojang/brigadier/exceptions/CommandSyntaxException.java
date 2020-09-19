/*    */ package com.mojang.brigadier.exceptions;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ 
/*    */ 
/*    */ public class CommandSyntaxException
/*    */   extends Exception
/*    */ {
/*    */   public static final int CONTEXT_AMOUNT = 10;
/*    */   public static boolean ENABLE_COMMAND_STACK_TRACES = true;
/* 11 */   public static BuiltInExceptionProvider BUILT_IN_EXCEPTIONS = new BuiltInExceptions();
/*    */   
/*    */   private final CommandExceptionType type;
/*    */   private final Message message;
/*    */   private final String input;
/*    */   private final int cursor;
/*    */   
/*    */   public CommandSyntaxException(CommandExceptionType type, Message message) {
/* 19 */     super(message.getString(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
/* 20 */     this.type = type;
/* 21 */     this.message = message;
/* 22 */     this.input = null;
/* 23 */     this.cursor = -1;
/*    */   }
/*    */   
/*    */   public CommandSyntaxException(CommandExceptionType type, Message message, String input, int cursor) {
/* 27 */     super(message.getString(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
/* 28 */     this.type = type;
/* 29 */     this.message = message;
/* 30 */     this.input = input;
/* 31 */     this.cursor = cursor;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 36 */     String message = this.message.getString();
/* 37 */     String context = getContext();
/* 38 */     if (context != null) {
/* 39 */       message = message + " at position " + this.cursor + ": " + context;
/*    */     }
/* 41 */     return message;
/*    */   }
/*    */   
/*    */   public Message getRawMessage() {
/* 45 */     return this.message;
/*    */   }
/*    */   
/*    */   public String getContext() {
/* 49 */     if (this.input == null || this.cursor < 0) {
/* 50 */       return null;
/*    */     }
/* 52 */     StringBuilder builder = new StringBuilder();
/* 53 */     int cursor = Math.min(this.input.length(), this.cursor);
/*    */     
/* 55 */     if (cursor > 10) {
/* 56 */       builder.append("...");
/*    */     }
/*    */     
/* 59 */     builder.append(this.input.substring(Math.max(0, cursor - 10), cursor));
/* 60 */     builder.append("<--[HERE]");
/*    */     
/* 62 */     return builder.toString();
/*    */   }
/*    */   
/*    */   public CommandExceptionType getType() {
/* 66 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getInput() {
/* 70 */     return this.input;
/*    */   }
/*    */   
/*    */   public int getCursor() {
/* 74 */     return this.cursor;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\exceptions\CommandSyntaxException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */