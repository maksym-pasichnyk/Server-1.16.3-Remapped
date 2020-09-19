/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class StopCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 12 */     debug0.register(
/* 13 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stop")
/* 14 */         .requires(debug0 -> debug0.hasPermission(4)))
/* 15 */         .executes(debug0 -> {
/*    */             ((CommandSourceStack)debug0.getSource()).sendSuccess((Component)new TranslatableComponent("commands.stop.stopping"), true);
/*    */             ((CommandSourceStack)debug0.getSource()).getServer().halt(false);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\StopCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */