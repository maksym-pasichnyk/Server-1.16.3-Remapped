/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TitleCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  27 */     debug0.register(
/*  28 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("title")
/*  29 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  30 */         .then((
/*  31 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  32 */           .then(
/*  33 */             Commands.literal("clear")
/*  34 */             .executes(debug0 -> clearTitle((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets")))))
/*     */           
/*  36 */           .then(
/*  37 */             Commands.literal("reset")
/*  38 */             .executes(debug0 -> resetTitle((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets")))))
/*     */           
/*  40 */           .then(
/*  41 */             Commands.literal("title")
/*  42 */             .then(
/*  43 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent())
/*  44 */               .executes(debug0 -> showTitle((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ComponentArgument.getComponent(debug0, "title"), ClientboundSetTitlesPacket.Type.TITLE)))))
/*     */ 
/*     */           
/*  47 */           .then(
/*  48 */             Commands.literal("subtitle")
/*  49 */             .then(
/*  50 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent())
/*  51 */               .executes(debug0 -> showTitle((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ComponentArgument.getComponent(debug0, "title"), ClientboundSetTitlesPacket.Type.SUBTITLE)))))
/*     */ 
/*     */           
/*  54 */           .then(
/*  55 */             Commands.literal("actionbar")
/*  56 */             .then(
/*  57 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent())
/*  58 */               .executes(debug0 -> showTitle((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), ComponentArgument.getComponent(debug0, "title"), ClientboundSetTitlesPacket.Type.ACTIONBAR)))))
/*     */ 
/*     */           
/*  61 */           .then(
/*  62 */             Commands.literal("times")
/*  63 */             .then(
/*  64 */               Commands.argument("fadeIn", (ArgumentType)IntegerArgumentType.integer(0))
/*  65 */               .then(
/*  66 */                 Commands.argument("stay", (ArgumentType)IntegerArgumentType.integer(0))
/*  67 */                 .then(
/*  68 */                   Commands.argument("fadeOut", (ArgumentType)IntegerArgumentType.integer(0))
/*  69 */                   .executes(debug0 -> setTimes((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), IntegerArgumentType.getInteger(debug0, "fadeIn"), IntegerArgumentType.getInteger(debug0, "stay"), IntegerArgumentType.getInteger(debug0, "fadeOut")))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int clearTitle(CommandSourceStack debug0, Collection<ServerPlayer> debug1) {
/*  79 */     ClientboundSetTitlesPacket debug2 = new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.CLEAR, null);
/*  80 */     for (ServerPlayer debug4 : debug1) {
/*  81 */       debug4.connection.send((Packet)debug2);
/*     */     }
/*     */     
/*  84 */     if (debug1.size() == 1) {
/*  85 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.cleared.single", new Object[] { ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  87 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.cleared.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/*  90 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int resetTitle(CommandSourceStack debug0, Collection<ServerPlayer> debug1) {
/*  94 */     ClientboundSetTitlesPacket debug2 = new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.RESET, null);
/*  95 */     for (ServerPlayer debug4 : debug1) {
/*  96 */       debug4.connection.send((Packet)debug2);
/*     */     }
/*     */     
/*  99 */     if (debug1.size() == 1) {
/* 100 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.reset.single", new Object[] { ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 102 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.reset.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 105 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int showTitle(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Component debug2, ClientboundSetTitlesPacket.Type debug3) throws CommandSyntaxException {
/* 109 */     for (ServerPlayer debug5 : debug1) {
/* 110 */       debug5.connection.send((Packet)new ClientboundSetTitlesPacket(debug3, (Component)ComponentUtils.updateForEntity(debug0, debug2, (Entity)debug5, 0)));
/*     */     }
/*     */     
/* 113 */     if (debug1.size() == 1) {
/* 114 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.show." + debug3.name().toLowerCase(Locale.ROOT) + ".single", new Object[] { ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 116 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.show." + debug3.name().toLowerCase(Locale.ROOT) + ".multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 119 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int setTimes(CommandSourceStack debug0, Collection<ServerPlayer> debug1, int debug2, int debug3, int debug4) {
/* 123 */     ClientboundSetTitlesPacket debug5 = new ClientboundSetTitlesPacket(debug2, debug3, debug4);
/* 124 */     for (ServerPlayer debug7 : debug1) {
/* 125 */       debug7.connection.send((Packet)debug5);
/*     */     }
/*     */     
/* 128 */     if (debug1.size() == 1) {
/* 129 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.times.single", new Object[] { ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 131 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.title.times.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 134 */     return debug1.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TitleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */