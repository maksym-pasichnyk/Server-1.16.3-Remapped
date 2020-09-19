/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ListPlayersCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 19 */     debug0.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list")
/* 21 */         .executes(debug0 -> listPlayers((CommandSourceStack)debug0.getSource())))
/* 22 */         .then(
/* 23 */           Commands.literal("uuids")
/* 24 */           .executes(debug0 -> listPlayersWithUuids((CommandSourceStack)debug0.getSource()))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int listPlayers(CommandSourceStack debug0) {
/* 30 */     return format(debug0, Player::getDisplayName);
/*    */   }
/*    */   
/*    */   private static int listPlayersWithUuids(CommandSourceStack debug0) {
/* 34 */     return format(debug0, debug0 -> new TranslatableComponent("commands.list.nameAndId", new Object[] { debug0.getName(), debug0.getGameProfile().getId() }));
/*    */   }
/*    */   
/*    */   private static int format(CommandSourceStack debug0, Function<ServerPlayer, Component> debug1) {
/* 38 */     PlayerList debug2 = debug0.getServer().getPlayerList();
/* 39 */     List<ServerPlayer> debug3 = debug2.getPlayers();
/* 40 */     MutableComponent mutableComponent = ComponentUtils.formatList(debug3, debug1);
/* 41 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.list.players", new Object[] { Integer.valueOf(debug3.size()), Integer.valueOf(debug2.getMaxPlayers()), mutableComponent }), false);
/* 42 */     return debug3.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ListPlayersCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */