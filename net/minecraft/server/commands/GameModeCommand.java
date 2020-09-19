/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameModeCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 27 */     LiteralArgumentBuilder<CommandSourceStack> debug1 = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("gamemode").requires(debug0 -> debug0.hasPermission(2));
/*    */     
/* 29 */     for (GameType debug5 : GameType.values()) {
/* 30 */       if (debug5 != GameType.NOT_SET) {
/* 31 */         debug1.then((
/* 32 */             (LiteralArgumentBuilder)Commands.literal(debug5.getName())
/* 33 */             .executes(debug1 -> setMode(debug1, Collections.singleton(((CommandSourceStack)debug1.getSource()).getPlayerOrException()), debug0)))
/* 34 */             .then(
/* 35 */               Commands.argument("target", (ArgumentType)EntityArgument.players())
/* 36 */               .executes(debug1 -> setMode(debug1, EntityArgument.getPlayers(debug1, "target"), debug0))));
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 42 */     debug0.register(debug1);
/*    */   }
/*    */   
/*    */   private static void logGamemodeChange(CommandSourceStack debug0, ServerPlayer debug1, GameType debug2) {
/* 46 */     TranslatableComponent translatableComponent = new TranslatableComponent("gameMode." + debug2.getName());
/* 47 */     if (debug0.getEntity() == debug1) {
/* 48 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.gamemode.success.self", new Object[] { translatableComponent }), true);
/*    */     } else {
/* 50 */       if (debug0.getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
/* 51 */         debug1.sendMessage((Component)new TranslatableComponent("gameMode.changed", new Object[] { translatableComponent }), Util.NIL_UUID);
/*    */       }
/*    */       
/* 54 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.gamemode.success.other", new Object[] { debug1.getDisplayName(), translatableComponent }), true);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int setMode(CommandContext<CommandSourceStack> debug0, Collection<ServerPlayer> debug1, GameType debug2) {
/* 59 */     int debug3 = 0;
/* 60 */     for (ServerPlayer debug5 : debug1) {
/* 61 */       if (debug5.gameMode.getGameModeForPlayer() != debug2) {
/* 62 */         debug5.setGameMode(debug2);
/* 63 */         logGamemodeChange((CommandSourceStack)debug0.getSource(), debug5, debug2);
/* 64 */         debug3++;
/*    */       } 
/*    */     } 
/* 67 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\GameModeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */