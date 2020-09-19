/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class ConsoleInput {
/*    */   public final String msg;
/*    */   public final CommandSourceStack source;
/*    */   
/*    */   public ConsoleInput(String debug1, CommandSourceStack debug2) {
/* 10 */     this.msg = debug1;
/* 11 */     this.source = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ConsoleInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */