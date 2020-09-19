/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.ComponentArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class TellRawCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 19 */     debug0.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw")
/* 21 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 22 */         .then(
/* 23 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 24 */           .then(
/* 25 */             Commands.argument("message", (ArgumentType)ComponentArgument.textComponent())
/* 26 */             .executes(debug0 -> {
/*    */                 int debug1 = 0;
/*    */                 for (ServerPlayer debug3 : EntityArgument.getPlayers(debug0, "targets")) {
/*    */                   debug3.sendMessage((Component)ComponentUtils.updateForEntity((CommandSourceStack)debug0.getSource(), ComponentArgument.getComponent(debug0, "message"), (Entity)debug3, 0), Util.NIL_UUID);
/*    */                   debug1++;
/*    */                 } 
/*    */                 return debug1;
/*    */               }))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TellRawCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */