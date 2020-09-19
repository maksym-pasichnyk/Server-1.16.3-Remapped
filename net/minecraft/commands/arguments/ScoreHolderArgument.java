/*     */ package net.minecraft.commands.arguments;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.commands.synchronization.ArgumentSerializer;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class ScoreHolderArgument implements ArgumentType<ScoreHolderArgument.Result> {
/*     */   static {
/*  27 */     SUGGEST_SCORE_HOLDERS = ((debug0, debug1) -> {
/*     */         StringReader debug2 = new StringReader(debug1.getInput());
/*     */         debug2.setCursor(debug1.getStart());
/*     */         EntitySelectorParser debug3 = new EntitySelectorParser(debug2);
/*     */         try {
/*     */           debug3.parse();
/*  33 */         } catch (CommandSyntaxException commandSyntaxException) {}
/*     */         return debug3.fillSuggestions(debug1, ());
/*     */       });
/*     */   }
/*     */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_SCORE_HOLDERS;
/*  38 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "Player", "0123", "*", "@e" });
/*  39 */   private static final SimpleCommandExceptionType ERROR_NO_RESULTS = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.scoreHolder.empty"));
/*     */ 
/*     */   
/*     */   private final boolean multiple;
/*     */ 
/*     */   
/*     */   public ScoreHolderArgument(boolean debug1) {
/*  46 */     this.multiple = debug1;
/*     */   }
/*     */   
/*     */   public static String getName(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  50 */     return getNames(debug0, debug1).iterator().next();
/*     */   }
/*     */   
/*     */   public static Collection<String> getNames(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  54 */     return getNames(debug0, debug1, Collections::emptyList);
/*     */   }
/*     */   
/*     */   public static Collection<String> getNamesWithDefaultWildcard(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/*  58 */     return getNames(debug0, debug1, ((CommandSourceStack)debug0.getSource()).getServer().getScoreboard()::getTrackedPlayers);
/*     */   }
/*     */   
/*     */   public static Collection<String> getNames(CommandContext<CommandSourceStack> debug0, String debug1, Supplier<Collection<String>> debug2) throws CommandSyntaxException {
/*  62 */     Collection<String> debug3 = ((Result)debug0.getArgument(debug1, Result.class)).getNames((CommandSourceStack)debug0.getSource(), debug2);
/*  63 */     if (debug3.isEmpty()) {
/*  64 */       throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */     }
/*  66 */     return debug3;
/*     */   }
/*     */   
/*     */   public static ScoreHolderArgument scoreHolder() {
/*  70 */     return new ScoreHolderArgument(false);
/*     */   }
/*     */   
/*     */   public static ScoreHolderArgument scoreHolders() {
/*  74 */     return new ScoreHolderArgument(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Result parse(StringReader debug1) throws CommandSyntaxException {
/*  79 */     if (debug1.canRead() && debug1.peek() == '@') {
/*  80 */       EntitySelectorParser entitySelectorParser = new EntitySelectorParser(debug1);
/*  81 */       EntitySelector entitySelector = entitySelectorParser.parse();
/*  82 */       if (!this.multiple && entitySelector.getMaxResults() > 1) {
/*  83 */         throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
/*     */       }
/*  85 */       return new SelectorResult(entitySelector);
/*     */     } 
/*  87 */     int debug2 = debug1.getCursor();
/*  88 */     while (debug1.canRead() && debug1.peek() != ' ') {
/*  89 */       debug1.skip();
/*     */     }
/*  91 */     String debug3 = debug1.getString().substring(debug2, debug1.getCursor());
/*  92 */     if (debug3.equals("*")) {
/*  93 */       return (debug0, debug1) -> {
/*     */           Collection<String> debug2 = debug1.get();
/*     */           if (debug2.isEmpty()) {
/*     */             throw ERROR_NO_RESULTS.create();
/*     */           }
/*     */           return debug2;
/*     */         };
/*     */     }
/* 101 */     Collection<String> debug4 = Collections.singleton(debug3);
/* 102 */     return (debug1, debug2) -> debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 107 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Result {
/*     */     Collection<String> getNames(CommandSourceStack param1CommandSourceStack, Supplier<Collection<String>> param1Supplier) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   public static class SelectorResult implements Result {
/*     */     private final EntitySelector selector;
/*     */     
/*     */     public SelectorResult(EntitySelector debug1) {
/* 119 */       this.selector = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<String> getNames(CommandSourceStack debug1, Supplier<Collection<String>> debug2) throws CommandSyntaxException {
/* 124 */       List<? extends Entity> debug3 = this.selector.findEntities(debug1);
/* 125 */       if (debug3.isEmpty()) {
/* 126 */         throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */       }
/* 128 */       List<String> debug4 = Lists.newArrayList();
/* 129 */       for (Entity debug6 : debug3) {
/* 130 */         debug4.add(debug6.getScoreboardName());
/*     */       }
/* 132 */       return debug4;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements ArgumentSerializer<ScoreHolderArgument> {
/*     */     public void serializeToNetwork(ScoreHolderArgument debug1, FriendlyByteBuf debug2) {
/* 139 */       byte debug3 = 0;
/* 140 */       if (debug1.multiple) {
/* 141 */         debug3 = (byte)(debug3 | 0x1);
/*     */       }
/* 143 */       debug2.writeByte(debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public ScoreHolderArgument deserializeFromNetwork(FriendlyByteBuf debug1) {
/* 148 */       byte debug2 = debug1.readByte();
/* 149 */       boolean debug3 = ((debug2 & 0x1) != 0);
/* 150 */       return new ScoreHolderArgument(debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeToJson(ScoreHolderArgument debug1, JsonObject debug2) {
/* 155 */       debug2.addProperty("amount", debug1.multiple ? "multiple" : "single");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ScoreHolderArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */