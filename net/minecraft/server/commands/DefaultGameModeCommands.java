/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class DefaultGameModeCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 16 */     LiteralArgumentBuilder<CommandSourceStack> debug1 = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("defaultgamemode").requires(debug0 -> debug0.hasPermission(2));
/*    */     
/* 18 */     for (GameType debug5 : GameType.values()) {
/* 19 */       if (debug5 != GameType.NOT_SET) {
/* 20 */         debug1.then(Commands.literal(debug5.getName()).executes(debug1 -> setMode((CommandSourceStack)debug1.getSource(), debug0)));
/*    */       }
/*    */     } 
/*    */     
/* 24 */     debug0.register(debug1);
/*    */   }
/*    */   
/*    */   private static int setMode(CommandSourceStack debug0, GameType debug1) {
/* 28 */     int debug2 = 0;
/* 29 */     MinecraftServer debug3 = debug0.getServer();
/* 30 */     debug3.setDefaultGameType(debug1);
/*    */     
/* 32 */     if (debug3.getForceGameType()) {
/* 33 */       for (ServerPlayer debug5 : debug3.getPlayerList().getPlayers()) {
/* 34 */         if (debug5.gameMode.getGameModeForPlayer() != debug1) {
/* 35 */           debug5.setGameMode(debug1);
/* 36 */           debug2++;
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 41 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.defaultgamemode.success", new Object[] { debug1.getDisplayName() }), true);
/*    */     
/* 43 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\DefaultGameModeCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */