/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.IpBanList;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ import net.minecraft.server.players.StoredUserEntry;
/*    */ 
/*    */ public class BanIpCommands {
/* 28 */   public static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
/* 29 */   private static final SimpleCommandExceptionType ERROR_INVALID_IP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.banip.invalid"));
/* 30 */   private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.banip.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 33 */     debug0.register(
/* 34 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban-ip")
/* 35 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 36 */         .then((
/* 37 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)StringArgumentType.word())
/* 38 */           .executes(debug0 -> banIpOrName((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "target"), null)))
/* 39 */           .then(
/* 40 */             Commands.argument("reason", (ArgumentType)MessageArgument.message())
/* 41 */             .executes(debug0 -> banIpOrName((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "target"), MessageArgument.getMessage(debug0, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int banIpOrName(CommandSourceStack debug0, String debug1, @Nullable Component debug2) throws CommandSyntaxException {
/* 48 */     Matcher debug3 = IP_ADDRESS_PATTERN.matcher(debug1);
/* 49 */     if (debug3.matches()) {
/* 50 */       return banIp(debug0, debug1, debug2);
/*    */     }
/* 52 */     ServerPlayer debug4 = debug0.getServer().getPlayerList().getPlayerByName(debug1);
/* 53 */     if (debug4 != null) {
/* 54 */       return banIp(debug0, debug4.getIpAddress(), debug2);
/*    */     }
/*    */     
/* 57 */     throw ERROR_INVALID_IP.create();
/*    */   }
/*    */   
/*    */   private static int banIp(CommandSourceStack debug0, String debug1, @Nullable Component debug2) throws CommandSyntaxException {
/* 61 */     IpBanList debug3 = debug0.getServer().getPlayerList().getIpBans();
/* 62 */     if (debug3.isBanned(debug1)) {
/* 63 */       throw ERROR_ALREADY_BANNED.create();
/*    */     }
/* 65 */     List<ServerPlayer> debug4 = debug0.getServer().getPlayerList().getPlayersWithAddress(debug1);
/* 66 */     IpBanListEntry debug5 = new IpBanListEntry(debug1, null, debug0.getTextName(), null, (debug2 == null) ? null : debug2.getString());
/* 67 */     debug3.add((StoredUserEntry)debug5);
/*    */     
/* 69 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.banip.success", new Object[] { debug1, debug5.getReason() }), true);
/* 70 */     if (!debug4.isEmpty()) {
/* 71 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.banip.info", new Object[] { Integer.valueOf(debug4.size()), EntitySelector.joinNames(debug4) }), true);
/*    */     }
/*    */     
/* 74 */     for (ServerPlayer debug7 : debug4) {
/* 75 */       debug7.connection.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.ip_banned"));
/*    */     }
/*    */     
/* 78 */     return debug4.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\BanIpCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */