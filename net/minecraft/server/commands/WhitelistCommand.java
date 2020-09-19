/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.GameProfileArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.players.UserWhiteList;
/*     */ import net.minecraft.server.players.UserWhiteListEntry;
/*     */ 
/*     */ public class WhitelistCommand {
/*  24 */   private static final SimpleCommandExceptionType ERROR_ALREADY_ENABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.alreadyOn"));
/*  25 */   private static final SimpleCommandExceptionType ERROR_ALREADY_DISABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.alreadyOff"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_ALREADY_WHITELISTED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.add.failed"));
/*  27 */   private static final SimpleCommandExceptionType ERROR_NOT_WHITELISTED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.whitelist.remove.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  30 */     debug0.register(
/*  31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("whitelist")
/*  32 */         .requires(debug0 -> debug0.hasPermission(3)))
/*  33 */         .then(
/*  34 */           Commands.literal("on")
/*  35 */           .executes(debug0 -> enableWhitelist((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  37 */         .then(
/*  38 */           Commands.literal("off")
/*  39 */           .executes(debug0 -> disableWhitelist((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  41 */         .then(
/*  42 */           Commands.literal("list")
/*  43 */           .executes(debug0 -> showList((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  45 */         .then(
/*  46 */           Commands.literal("add")
/*  47 */           .then(
/*  48 */             Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/*  49 */             .suggests((debug0, debug1) -> {
/*     */                 PlayerList debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList();
/*     */                 
/*     */                 return SharedSuggestionProvider.suggest(debug2.getPlayers().stream().filter(()).map(()), debug1);
/*  53 */               }).executes(debug0 -> addPlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets"))))))
/*     */ 
/*     */         
/*  56 */         .then(
/*  57 */           Commands.literal("remove")
/*  58 */           .then(
/*  59 */             Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/*  60 */             .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getWhiteListNames(), debug1))
/*  61 */             .executes(debug0 -> removePlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets"))))))
/*     */ 
/*     */         
/*  64 */         .then(
/*  65 */           Commands.literal("reload")
/*  66 */           .executes(debug0 -> reload((CommandSourceStack)debug0.getSource()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int reload(CommandSourceStack debug0) {
/*  72 */     debug0.getServer().getPlayerList().reloadWhiteList();
/*  73 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.reloaded"), true);
/*  74 */     debug0.getServer().kickUnlistedPlayers(debug0);
/*  75 */     return 1;
/*     */   }
/*     */   
/*     */   private static int addPlayers(CommandSourceStack debug0, Collection<GameProfile> debug1) throws CommandSyntaxException {
/*  79 */     UserWhiteList debug2 = debug0.getServer().getPlayerList().getWhiteList();
/*  80 */     int debug3 = 0;
/*     */     
/*  82 */     for (GameProfile debug5 : debug1) {
/*  83 */       if (!debug2.isWhiteListed(debug5)) {
/*  84 */         UserWhiteListEntry debug6 = new UserWhiteListEntry(debug5);
/*  85 */         debug2.add((StoredUserEntry)debug6);
/*  86 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.add.success", new Object[] { ComponentUtils.getDisplayName(debug5) }), true);
/*  87 */         debug3++;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     if (debug3 == 0) {
/*  92 */       throw ERROR_ALREADY_WHITELISTED.create();
/*     */     }
/*     */     
/*  95 */     return debug3;
/*     */   }
/*     */   
/*     */   private static int removePlayers(CommandSourceStack debug0, Collection<GameProfile> debug1) throws CommandSyntaxException {
/*  99 */     UserWhiteList debug2 = debug0.getServer().getPlayerList().getWhiteList();
/* 100 */     int debug3 = 0;
/*     */     
/* 102 */     for (GameProfile debug5 : debug1) {
/* 103 */       if (debug2.isWhiteListed(debug5)) {
/* 104 */         UserWhiteListEntry debug6 = new UserWhiteListEntry(debug5);
/* 105 */         debug2.remove((StoredUserEntry)debug6);
/* 106 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.remove.success", new Object[] { ComponentUtils.getDisplayName(debug5) }), true);
/* 107 */         debug3++;
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (debug3 == 0) {
/* 112 */       throw ERROR_NOT_WHITELISTED.create();
/*     */     }
/*     */     
/* 115 */     debug0.getServer().kickUnlistedPlayers(debug0);
/* 116 */     return debug3;
/*     */   }
/*     */   
/*     */   private static int enableWhitelist(CommandSourceStack debug0) throws CommandSyntaxException {
/* 120 */     PlayerList debug1 = debug0.getServer().getPlayerList();
/* 121 */     if (debug1.isUsingWhitelist()) {
/* 122 */       throw ERROR_ALREADY_ENABLED.create();
/*     */     }
/* 124 */     debug1.setUsingWhiteList(true);
/* 125 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.enabled"), true);
/* 126 */     debug0.getServer().kickUnlistedPlayers(debug0);
/* 127 */     return 1;
/*     */   }
/*     */   
/*     */   private static int disableWhitelist(CommandSourceStack debug0) throws CommandSyntaxException {
/* 131 */     PlayerList debug1 = debug0.getServer().getPlayerList();
/* 132 */     if (!debug1.isUsingWhitelist()) {
/* 133 */       throw ERROR_ALREADY_DISABLED.create();
/*     */     }
/* 135 */     debug1.setUsingWhiteList(false);
/* 136 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.disabled"), true);
/* 137 */     return 1;
/*     */   }
/*     */   
/*     */   private static int showList(CommandSourceStack debug0) {
/* 141 */     String[] debug1 = debug0.getServer().getPlayerList().getWhiteListNames();
/* 142 */     if (debug1.length == 0) {
/* 143 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.none"), false);
/*     */     } else {
/* 145 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.whitelist.list", new Object[] { Integer.valueOf(debug1.length), String.join(", ", (CharSequence[])debug1) }), false);
/*     */     } 
/* 147 */     return debug1.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\WhitelistCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */