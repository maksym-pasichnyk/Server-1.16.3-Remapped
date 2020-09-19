/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public class TriggerCommand {
/*  30 */   private static final SimpleCommandExceptionType ERROR_NOT_PRIMED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.trigger.failed.unprimed"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_INVALID_OBJECTIVE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.trigger.failed.invalid"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  34 */     debug0.register(
/*  35 */         (LiteralArgumentBuilder)Commands.literal("trigger")
/*  36 */         .then((
/*  37 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/*  38 */           .suggests((debug0, debug1) -> suggestObjectives((CommandSourceStack)debug0.getSource(), debug1))
/*  39 */           .executes(debug0 -> simpleTrigger((CommandSourceStack)debug0.getSource(), getScore(((CommandSourceStack)debug0.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(debug0, "objective")))))
/*  40 */           .then(
/*  41 */             Commands.literal("add")
/*  42 */             .then(
/*  43 */               Commands.argument("value", (ArgumentType)IntegerArgumentType.integer())
/*  44 */               .executes(debug0 -> addValue((CommandSourceStack)debug0.getSource(), getScore(((CommandSourceStack)debug0.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(debug0, "objective")), IntegerArgumentType.getInteger(debug0, "value"))))))
/*     */ 
/*     */           
/*  47 */           .then(
/*  48 */             Commands.literal("set")
/*  49 */             .then(
/*  50 */               Commands.argument("value", (ArgumentType)IntegerArgumentType.integer())
/*  51 */               .executes(debug0 -> setValue((CommandSourceStack)debug0.getSource(), getScore(((CommandSourceStack)debug0.getSource()).getPlayerOrException(), ObjectiveArgument.getObjective(debug0, "objective")), IntegerArgumentType.getInteger(debug0, "value")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Suggestions> suggestObjectives(CommandSourceStack debug0, SuggestionsBuilder debug1) {
/*  59 */     Entity debug2 = debug0.getEntity();
/*  60 */     List<String> debug3 = Lists.newArrayList();
/*     */     
/*  62 */     if (debug2 != null) {
/*  63 */       ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*  64 */       String debug5 = debug2.getScoreboardName();
/*     */       
/*  66 */       for (Objective debug7 : serverScoreboard.getObjectives()) {
/*  67 */         if (debug7.getCriteria() == ObjectiveCriteria.TRIGGER && serverScoreboard.hasPlayerScore(debug5, debug7)) {
/*  68 */           Score debug8 = serverScoreboard.getOrCreatePlayerScore(debug5, debug7);
/*  69 */           if (!debug8.isLocked()) {
/*  70 */             debug3.add(debug7.getName());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  76 */     return SharedSuggestionProvider.suggest(debug3, debug1);
/*     */   }
/*     */   
/*     */   private static int addValue(CommandSourceStack debug0, Score debug1, int debug2) {
/*  80 */     debug1.add(debug2);
/*  81 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.trigger.add.success", new Object[] { debug1.getObjective().getFormattedDisplayName(), Integer.valueOf(debug2) }), true);
/*  82 */     return debug1.getScore();
/*     */   }
/*     */   
/*     */   private static int setValue(CommandSourceStack debug0, Score debug1, int debug2) {
/*  86 */     debug1.setScore(debug2);
/*  87 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.trigger.set.success", new Object[] { debug1.getObjective().getFormattedDisplayName(), Integer.valueOf(debug2) }), true);
/*  88 */     return debug2;
/*     */   }
/*     */   
/*     */   private static int simpleTrigger(CommandSourceStack debug0, Score debug1) {
/*  92 */     debug1.add(1);
/*  93 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.trigger.simple.success", new Object[] { debug1.getObjective().getFormattedDisplayName() }), true);
/*  94 */     return debug1.getScore();
/*     */   }
/*     */   
/*     */   private static Score getScore(ServerPlayer debug0, Objective debug1) throws CommandSyntaxException {
/*  98 */     if (debug1.getCriteria() != ObjectiveCriteria.TRIGGER) {
/*  99 */       throw ERROR_INVALID_OBJECTIVE.create();
/*     */     }
/* 101 */     Scoreboard debug2 = debug0.getScoreboard();
/* 102 */     String debug3 = debug0.getScoreboardName();
/* 103 */     if (!debug2.hasPlayerScore(debug3, debug1)) {
/* 104 */       throw ERROR_NOT_PRIMED.create();
/*     */     }
/* 106 */     Score debug4 = debug2.getOrCreatePlayerScore(debug3, debug1);
/* 107 */     if (debug4.isLocked()) {
/* 108 */       throw ERROR_NOT_PRIMED.create();
/*     */     }
/* 110 */     debug4.setLocked(true);
/* 111 */     return debug4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TriggerCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */