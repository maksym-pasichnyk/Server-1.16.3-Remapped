/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class CommandRuntimeException extends RuntimeException {
/*    */   private final Component message;
/*    */   
/*    */   public CommandRuntimeException(Component debug1) {
/* 10 */     super(debug1.getString(), null, CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES, CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES);
/* 11 */     this.message = debug1;
/*    */   }
/*    */   
/*    */   public Component getComponent() {
/* 15 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\CommandRuntimeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */