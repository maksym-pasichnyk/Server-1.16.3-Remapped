/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class KickCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 21 */     debug0.register(
/* 22 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kick")
/* 23 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 24 */         .then((
/* 25 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 26 */           .executes(debug0 -> kickPlayers((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), (Component)new TranslatableComponent("multiplayer.disconnect.kicked"))))
/* 27 */           .then(
/* 28 */             Commands.argument("reason", (ArgumentType)MessageArgument.message())
/* 29 */             .executes(debug0 -> kickPlayers((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), MessageArgument.getMessage(debug0, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int kickPlayers(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Component debug2) {
/* 36 */     for (ServerPlayer debug4 : debug1) {
/* 37 */       debug4.connection.disconnect(debug2);
/* 38 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.kick.success", new Object[] { debug4.getDisplayName(), debug2 }), true);
/*    */     } 
/*    */     
/* 41 */     return debug1.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\KickCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */