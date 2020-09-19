/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.StoredUserEntry;
/*    */ import net.minecraft.server.players.UserBanList;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ public class BanPlayerCommands {
/* 27 */   private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.ban.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 30 */     debug0.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban")
/* 32 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 33 */         .then((
/* 34 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 35 */           .executes(debug0 -> banPlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets"), null)))
/* 36 */           .then(
/* 37 */             Commands.argument("reason", (ArgumentType)MessageArgument.message())
/* 38 */             .executes(debug0 -> banPlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets"), MessageArgument.getMessage(debug0, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int banPlayers(CommandSourceStack debug0, Collection<GameProfile> debug1, @Nullable Component debug2) throws CommandSyntaxException {
/* 45 */     UserBanList debug3 = debug0.getServer().getPlayerList().getBans();
/* 46 */     int debug4 = 0;
/*    */     
/* 48 */     for (GameProfile debug6 : debug1) {
/* 49 */       if (!debug3.isBanned(debug6)) {
/* 50 */         UserBanListEntry debug7 = new UserBanListEntry(debug6, null, debug0.getTextName(), null, (debug2 == null) ? null : debug2.getString());
/* 51 */         debug3.add((StoredUserEntry)debug7);
/* 52 */         debug4++;
/* 53 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.ban.success", new Object[] { ComponentUtils.getDisplayName(debug6), debug7.getReason() }), true);
/*    */         
/* 55 */         ServerPlayer debug8 = debug0.getServer().getPlayerList().getPlayer(debug6.getId());
/* 56 */         if (debug8 != null) {
/* 57 */           debug8.connection.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.banned"));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     if (debug4 == 0) {
/* 63 */       throw ERROR_ALREADY_BANNED.create();
/*    */     }
/*    */     
/* 66 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\BanPlayerCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */