/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ 
/*     */ public class GameProfileArgument implements ArgumentType<GameProfileArgument.Result> {
/*  26 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e" });
/*  27 */   public static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.player.unknown"));
/*     */   
/*     */   public static Collection<GameProfile> getGameProfiles(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  30 */     return ((Result)debug0.getArgument(debug1, Result.class)).getNames((CommandSourceStack)debug0.getSource());
/*     */   }
/*     */   
/*     */   public static GameProfileArgument gameProfile() {
/*  34 */     return new GameProfileArgument();
/*     */   }
/*     */ 
/*     */   
/*     */   public Result parse(StringReader debug1) throws CommandSyntaxException {
/*  39 */     if (debug1.canRead() && debug1.peek() == '@') {
/*  40 */       EntitySelectorParser entitySelectorParser = new EntitySelectorParser(debug1);
/*  41 */       EntitySelector entitySelector = entitySelectorParser.parse();
/*  42 */       if (entitySelector.includesEntities()) {
/*  43 */         throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.create();
/*     */       }
/*  45 */       return new SelectorResult(entitySelector);
/*     */     } 
/*     */     
/*  48 */     int debug2 = debug1.getCursor();
/*  49 */     while (debug1.canRead() && debug1.peek() != ' ') {
/*  50 */       debug1.skip();
/*     */     }
/*  52 */     String debug3 = debug1.getString().substring(debug2, debug1.getCursor());
/*  53 */     return debug1 -> {
/*     */         GameProfile debug2 = debug1.getServer().getProfileCache().get(debug0);
/*     */         if (debug2 == null) {
/*     */           throw ERROR_UNKNOWN_PLAYER.create();
/*     */         }
/*     */         return Collections.singleton(debug2);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SelectorResult
/*     */     implements Result
/*     */   {
/*     */     private final EntitySelector selector;
/*     */ 
/*     */     
/*     */     public SelectorResult(EntitySelector debug1) {
/*  71 */       this.selector = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<GameProfile> getNames(CommandSourceStack debug1) throws CommandSyntaxException {
/*  76 */       List<ServerPlayer> debug2 = this.selector.findPlayers(debug1);
/*  77 */       if (debug2.isEmpty()) {
/*  78 */         throw EntityArgument.NO_PLAYERS_FOUND.create();
/*     */       }
/*  80 */       List<GameProfile> debug3 = Lists.newArrayList();
/*  81 */       for (ServerPlayer debug5 : debug2) {
/*  82 */         debug3.add(debug5.getGameProfile());
/*     */       }
/*  84 */       return debug3;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/*  90 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/*  91 */       StringReader debug3 = new StringReader(debug2.getInput());
/*  92 */       debug3.setCursor(debug2.getStart());
/*  93 */       EntitySelectorParser debug4 = new EntitySelectorParser(debug3);
/*     */       try {
/*  95 */         debug4.parse();
/*  96 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/*  98 */       return debug4.fillSuggestions(debug2, debug1 -> SharedSuggestionProvider.suggest(((SharedSuggestionProvider)debug0.getSource()).getOnlinePlayerNames(), debug1));
/*     */     } 
/* 100 */     return Suggestions.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 106 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Result {
/*     */     Collection<GameProfile> getNames(CommandSourceStack param1CommandSourceStack) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\GameProfileArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */