/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class SetPlayerIdleTimeoutCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 15 */     debug0.register(
/* 16 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setidletimeout")
/* 17 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 18 */         .then(
/* 19 */           Commands.argument("minutes", (ArgumentType)IntegerArgumentType.integer(0))
/* 20 */           .executes(debug0 -> setIdleTimeout((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "minutes")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setIdleTimeout(CommandSourceStack debug0, int debug1) {
/* 26 */     debug0.getServer().setPlayerIdleTimeout(debug1);
/* 27 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.setidletimeout.success", new Object[] { Integer.valueOf(debug1) }), true);
/* 28 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SetPlayerIdleTimeoutCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */