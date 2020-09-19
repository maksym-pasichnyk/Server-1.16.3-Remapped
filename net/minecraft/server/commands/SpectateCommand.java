/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class SpectateCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_NOT_SPECTATOR;
/* 24 */   private static final SimpleCommandExceptionType ERROR_SELF = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.spectate.self")); static {
/* 25 */     ERROR_NOT_SPECTATOR = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.spectate.not_spectator", new Object[] { debug0 }));
/*    */   }
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 28 */     debug0.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spectate")
/* 30 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 31 */         .executes(debug0 -> spectate((CommandSourceStack)debug0.getSource(), null, ((CommandSourceStack)debug0.getSource()).getPlayerOrException())))
/* 32 */         .then((
/* 33 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)EntityArgument.entity())
/* 34 */           .executes(debug0 -> spectate((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ((CommandSourceStack)debug0.getSource()).getPlayerOrException())))
/* 35 */           .then(
/* 36 */             Commands.argument("player", (ArgumentType)EntityArgument.player())
/* 37 */             .executes(debug0 -> spectate((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), EntityArgument.getPlayer(debug0, "player"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int spectate(CommandSourceStack debug0, @Nullable Entity debug1, ServerPlayer debug2) throws CommandSyntaxException {
/* 44 */     if (debug2 == debug1)
/* 45 */       throw ERROR_SELF.create(); 
/* 46 */     if (debug2.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
/* 47 */       throw ERROR_NOT_SPECTATOR.create(debug2.getDisplayName());
/*    */     }
/*    */     
/* 50 */     debug2.setCamera(debug1);
/* 51 */     if (debug1 != null) {
/* 52 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.spectate.success.started", new Object[] { debug1.getDisplayName() }), false);
/*    */     } else {
/* 54 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.spectate.success.stopped"), false);
/*    */     } 
/* 56 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SpectateCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */