/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.gson.JsonObject;
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
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class EntityArgument
/*     */   implements ArgumentType<EntitySelector> {
/*  29 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498" });
/*  30 */   public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_ENTITY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.toomany"));
/*  31 */   public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_PLAYER = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.player.toomany"));
/*  32 */   public static final SimpleCommandExceptionType ERROR_ONLY_PLAYERS_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.player.entities"));
/*  33 */   public static final SimpleCommandExceptionType NO_ENTITIES_FOUND = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.notfound.entity"));
/*  34 */   public static final SimpleCommandExceptionType NO_PLAYERS_FOUND = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.notfound.player"));
/*  35 */   public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.selector.not_allowed"));
/*     */ 
/*     */   
/*     */   private final boolean single;
/*     */   
/*     */   private final boolean playersOnly;
/*     */ 
/*     */   
/*     */   protected EntityArgument(boolean debug1, boolean debug2) {
/*  44 */     this.single = debug1;
/*  45 */     this.playersOnly = debug2;
/*     */   }
/*     */   
/*     */   public static EntityArgument entity() {
/*  49 */     return new EntityArgument(true, false);
/*     */   }
/*     */   
/*     */   public static Entity getEntity(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  53 */     return ((EntitySelector)debug0.getArgument(debug1, EntitySelector.class)).findSingleEntity((CommandSourceStack)debug0.getSource());
/*     */   }
/*     */   
/*     */   public static EntityArgument entities() {
/*  57 */     return new EntityArgument(false, false);
/*     */   }
/*     */   
/*     */   public static Collection<? extends Entity> getEntities(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  61 */     Collection<? extends Entity> debug2 = getOptionalEntities(debug0, debug1);
/*  62 */     if (debug2.isEmpty()) {
/*  63 */       throw NO_ENTITIES_FOUND.create();
/*     */     }
/*  65 */     return debug2;
/*     */   }
/*     */   
/*     */   public static Collection<? extends Entity> getOptionalEntities(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  69 */     return ((EntitySelector)debug0.getArgument(debug1, EntitySelector.class)).findEntities((CommandSourceStack)debug0.getSource());
/*     */   }
/*     */   
/*     */   public static Collection<ServerPlayer> getOptionalPlayers(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  73 */     return ((EntitySelector)debug0.getArgument(debug1, EntitySelector.class)).findPlayers((CommandSourceStack)debug0.getSource());
/*     */   }
/*     */   
/*     */   public static EntityArgument player() {
/*  77 */     return new EntityArgument(true, true);
/*     */   }
/*     */   
/*     */   public static ServerPlayer getPlayer(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  81 */     return ((EntitySelector)debug0.getArgument(debug1, EntitySelector.class)).findSinglePlayer((CommandSourceStack)debug0.getSource());
/*     */   }
/*     */   
/*     */   public static EntityArgument players() {
/*  85 */     return new EntityArgument(false, true);
/*     */   }
/*     */   
/*     */   public static Collection<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  89 */     List<ServerPlayer> debug2 = ((EntitySelector)debug0.getArgument(debug1, EntitySelector.class)).findPlayers((CommandSourceStack)debug0.getSource());
/*  90 */     if (debug2.isEmpty()) {
/*  91 */       throw NO_PLAYERS_FOUND.create();
/*     */     }
/*  93 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntitySelector parse(StringReader debug1) throws CommandSyntaxException {
/*  98 */     int debug2 = 0;
/*  99 */     EntitySelectorParser debug3 = new EntitySelectorParser(debug1);
/* 100 */     EntitySelector debug4 = debug3.parse();
/* 101 */     if (debug4.getMaxResults() > 1 && this.single) {
/* 102 */       if (this.playersOnly) {
/* 103 */         debug1.setCursor(0);
/* 104 */         throw ERROR_NOT_SINGLE_PLAYER.createWithContext(debug1);
/*     */       } 
/* 106 */       debug1.setCursor(0);
/* 107 */       throw ERROR_NOT_SINGLE_ENTITY.createWithContext(debug1);
/*     */     } 
/*     */     
/* 110 */     if (debug4.includesEntities() && this.playersOnly && !debug4.isSelfSelector()) {
/* 111 */       debug1.setCursor(0);
/* 112 */       throw ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(debug1);
/*     */     } 
/*     */     
/* 115 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 120 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 121 */       StringReader debug3 = new StringReader(debug2.getInput());
/* 122 */       debug3.setCursor(debug2.getStart());
/* 123 */       SharedSuggestionProvider debug4 = (SharedSuggestionProvider)debug1.getSource();
/* 124 */       EntitySelectorParser debug5 = new EntitySelectorParser(debug3, debug4.hasPermission(2));
/*     */       try {
/* 126 */         debug5.parse();
/* 127 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/* 129 */       return debug5.fillSuggestions(debug2, debug2 -> {
/*     */             Collection<String> debug3 = debug1.getOnlinePlayerNames();
/*     */             Iterable<String> debug4 = this.playersOnly ? debug3 : Iterables.concat(debug3, debug1.getSelectedEntities());
/*     */             SharedSuggestionProvider.suggest(debug4, debug2);
/*     */           });
/*     */     } 
/* 135 */     return Suggestions.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 141 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements ArgumentSerializer<EntityArgument> {
/*     */     public void serializeToNetwork(EntityArgument debug1, FriendlyByteBuf debug2) {
/* 147 */       byte debug3 = 0;
/* 148 */       if (debug1.single) {
/* 149 */         debug3 = (byte)(debug3 | 0x1);
/*     */       }
/* 151 */       if (debug1.playersOnly) {
/* 152 */         debug3 = (byte)(debug3 | 0x2);
/*     */       }
/* 154 */       debug2.writeByte(debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public EntityArgument deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 159 */       byte debug2 = debug1.readByte();
/* 160 */       return new EntityArgument(((debug2 & 0x1) != 0), ((debug2 & 0x2) != 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(EntityArgument debug1, JsonObject debug2) {
/* 165 */       debug2.addProperty("amount", debug1.single ? "single" : "multiple");
/* 166 */       debug2.addProperty("type", debug1.playersOnly ? "players" : "entities");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\EntityArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */