/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.players.BanListEntry;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class BanListCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 18 */     debug0.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("banlist")
/* 20 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 21 */         .executes(debug0 -> {
/*    */             PlayerList debug1 = ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList();
/*    */             
/*    */             return showList((CommandSourceStack)debug0.getSource(), Lists.newArrayList(Iterables.concat(debug1.getBans().getEntries(), debug1.getIpBans().getEntries())));
/* 25 */           })).then(
/* 26 */           Commands.literal("ips")
/* 27 */           .executes(debug0 -> showList((CommandSourceStack)debug0.getSource(), ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getIpBans().getEntries()))))
/*    */         
/* 29 */         .then(
/* 30 */           Commands.literal("players")
/* 31 */           .executes(debug0 -> showList((CommandSourceStack)debug0.getSource(), ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getBans().getEntries()))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int showList(CommandSourceStack debug0, Collection<? extends BanListEntry<?>> debug1) {
/* 37 */     if (debug1.isEmpty()) {
/* 38 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.banlist.none"), false);
/*    */     } else {
/* 40 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.banlist.list", new Object[] { Integer.valueOf(debug1.size()) }), false);
/* 41 */       for (BanListEntry<?> debug3 : debug1) {
/* 42 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.banlist.entry", new Object[] { debug3.getDisplayName(), debug3.getSource(), debug3.getReason() }), false);
/*    */       } 
/*    */     } 
/* 45 */     return debug1.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\BanListCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */