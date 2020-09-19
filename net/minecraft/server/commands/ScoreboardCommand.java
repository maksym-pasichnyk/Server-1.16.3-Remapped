/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
/*     */ import net.minecraft.commands.arguments.OperationArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.ScoreboardSlotArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScoreboardCommand
/*     */ {
/*  54 */   private static final SimpleCommandExceptionType ERROR_OBJECTIVE_ALREADY_EXISTS = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.scoreboard.objectives.add.duplicate"));
/*  55 */   private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_EMPTY = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.scoreboard.objectives.display.alreadyEmpty"));
/*  56 */   private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_SET = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.scoreboard.objectives.display.alreadySet"));
/*  57 */   private static final SimpleCommandExceptionType ERROR_TRIGGER_ALREADY_ENABLED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.scoreboard.players.enable.failed")); private static final Dynamic2CommandExceptionType ERROR_NO_VALUE;
/*  58 */   private static final SimpleCommandExceptionType ERROR_NOT_TRIGGER = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.scoreboard.players.enable.invalid")); static {
/*  59 */     ERROR_NO_VALUE = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.scoreboard.players.get.null", new Object[] { debug0, debug1 }));
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  62 */     debug0.register(
/*  63 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("scoreboard")
/*  64 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  65 */         .then((
/*  66 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("objectives")
/*  67 */           .then(
/*  68 */             Commands.literal("list")
/*  69 */             .executes(debug0 -> listObjectives((CommandSourceStack)debug0.getSource()))))
/*     */           
/*  71 */           .then(
/*  72 */             Commands.literal("add")
/*  73 */             .then(
/*  74 */               Commands.argument("objective", (ArgumentType)StringArgumentType.word())
/*  75 */               .then((
/*  76 */                 (RequiredArgumentBuilder)Commands.argument("criteria", (ArgumentType)ObjectiveCriteriaArgument.criteria())
/*  77 */                 .executes(debug0 -> addObjective((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "objective"), ObjectiveCriteriaArgument.getCriteria(debug0, "criteria"), (Component)new TextComponent(StringArgumentType.getString(debug0, "objective")))))
/*  78 */                 .then(
/*  79 */                   Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent())
/*  80 */                   .executes(debug0 -> addObjective((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "objective"), ObjectiveCriteriaArgument.getCriteria(debug0, "criteria"), ComponentArgument.getComponent(debug0, "displayName"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  85 */           .then(
/*  86 */             Commands.literal("modify")
/*  87 */             .then((
/*  88 */               (RequiredArgumentBuilder)Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/*  89 */               .then(
/*  90 */                 Commands.literal("displayname")
/*  91 */                 .then(
/*  92 */                   Commands.argument("displayName", (ArgumentType)ComponentArgument.textComponent())
/*  93 */                   .executes(debug0 -> setDisplayName((CommandSourceStack)debug0.getSource(), ObjectiveArgument.getObjective(debug0, "objective"), ComponentArgument.getComponent(debug0, "displayName"))))))
/*     */               
/*  95 */               .then((ArgumentBuilder)createRenderTypeModify()))))
/*     */ 
/*     */           
/*  98 */           .then(
/*  99 */             Commands.literal("remove")
/* 100 */             .then(
/* 101 */               Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 102 */               .executes(debug0 -> removeObjective((CommandSourceStack)debug0.getSource(), ObjectiveArgument.getObjective(debug0, "objective"))))))
/*     */ 
/*     */           
/* 105 */           .then(
/* 106 */             Commands.literal("setdisplay")
/* 107 */             .then((
/* 108 */               (RequiredArgumentBuilder)Commands.argument("slot", (ArgumentType)ScoreboardSlotArgument.displaySlot())
/* 109 */               .executes(debug0 -> clearDisplaySlot((CommandSourceStack)debug0.getSource(), ScoreboardSlotArgument.getDisplaySlot(debug0, "slot"))))
/* 110 */               .then(
/* 111 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 112 */                 .executes(debug0 -> setDisplaySlot((CommandSourceStack)debug0.getSource(), ScoreboardSlotArgument.getDisplaySlot(debug0, "slot"), ObjectiveArgument.getObjective(debug0, "objective"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 117 */         .then((
/* 118 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("players")
/* 119 */           .then((
/* 120 */             (LiteralArgumentBuilder)Commands.literal("list")
/* 121 */             .executes(debug0 -> listTrackedPlayers((CommandSourceStack)debug0.getSource())))
/* 122 */             .then(
/* 123 */               Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 124 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 125 */               .executes(debug0 -> listTrackedPlayerScores((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getName(debug0, "target"))))))
/*     */ 
/*     */           
/* 128 */           .then(
/* 129 */             Commands.literal("set")
/* 130 */             .then(
/* 131 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 132 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 133 */               .then(
/* 134 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 135 */                 .then(
/* 136 */                   Commands.argument("score", (ArgumentType)IntegerArgumentType.integer())
/* 137 */                   .executes(debug0 -> setScore((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), ObjectiveArgument.getWritableObjective(debug0, "objective"), IntegerArgumentType.getInteger(debug0, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           .then(
/* 143 */             Commands.literal("get")
/* 144 */             .then(
/* 145 */               Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 146 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 147 */               .then(
/* 148 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 149 */                 .executes(debug0 -> getScore((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getName(debug0, "target"), ObjectiveArgument.getObjective(debug0, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 153 */           .then(
/* 154 */             Commands.literal("add")
/* 155 */             .then(
/* 156 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 157 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 158 */               .then(
/* 159 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 160 */                 .then(
/* 161 */                   Commands.argument("score", (ArgumentType)IntegerArgumentType.integer(0))
/* 162 */                   .executes(debug0 -> addScore((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), ObjectiveArgument.getWritableObjective(debug0, "objective"), IntegerArgumentType.getInteger(debug0, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 167 */           .then(
/* 168 */             Commands.literal("remove")
/* 169 */             .then(
/* 170 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 171 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 172 */               .then(
/* 173 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 174 */                 .then(
/* 175 */                   Commands.argument("score", (ArgumentType)IntegerArgumentType.integer(0))
/* 176 */                   .executes(debug0 -> removeScore((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), ObjectiveArgument.getWritableObjective(debug0, "objective"), IntegerArgumentType.getInteger(debug0, "score"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 181 */           .then(
/* 182 */             Commands.literal("reset")
/* 183 */             .then((
/* 184 */               (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 185 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 186 */               .executes(debug0 -> resetScores((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"))))
/* 187 */               .then(
/* 188 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 189 */                 .executes(debug0 -> resetScore((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), ObjectiveArgument.getObjective(debug0, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 193 */           .then(
/* 194 */             Commands.literal("enable")
/* 195 */             .then(
/* 196 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 197 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 198 */               .then(
/* 199 */                 Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 200 */                 .suggests((debug0, debug1) -> suggestTriggers((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), debug1))
/* 201 */                 .executes(debug0 -> enableTrigger((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), ObjectiveArgument.getObjective(debug0, "objective")))))))
/*     */ 
/*     */ 
/*     */           
/* 205 */           .then(
/* 206 */             Commands.literal("operation")
/* 207 */             .then(
/* 208 */               Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 209 */               .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 210 */               .then(
/* 211 */                 Commands.argument("targetObjective", (ArgumentType)ObjectiveArgument.objective())
/* 212 */                 .then(
/* 213 */                   Commands.argument("operation", (ArgumentType)OperationArgument.operation())
/* 214 */                   .then(
/* 215 */                     Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 216 */                     .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 217 */                     .then(
/* 218 */                       Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective())
/* 219 */                       .executes(debug0 -> performOperation((CommandSourceStack)debug0.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "targets"), ObjectiveArgument.getWritableObjective(debug0, "targetObjective"), OperationArgument.getOperation(debug0, "operation"), ScoreHolderArgument.getNamesWithDefaultWildcard(debug0, "source"), ObjectiveArgument.getObjective(debug0, "sourceObjective")))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LiteralArgumentBuilder<CommandSourceStack> createRenderTypeModify() {
/* 231 */     LiteralArgumentBuilder<CommandSourceStack> debug0 = Commands.literal("rendertype");
/*     */     
/* 233 */     for (ObjectiveCriteria.RenderType debug4 : ObjectiveCriteria.RenderType.values()) {
/* 234 */       debug0.then(Commands.literal(debug4.getId())
/* 235 */           .executes(debug1 -> setRenderType((CommandSourceStack)debug1.getSource(), ObjectiveArgument.getObjective(debug1, "objective"), debug0)));
/*     */     }
/*     */     
/* 238 */     return debug0;
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Suggestions> suggestTriggers(CommandSourceStack debug0, Collection<String> debug1, SuggestionsBuilder debug2) {
/* 242 */     List<String> debug3 = Lists.newArrayList();
/* 243 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 245 */     for (Objective debug6 : serverScoreboard.getObjectives()) {
/* 246 */       if (debug6.getCriteria() == ObjectiveCriteria.TRIGGER) {
/* 247 */         boolean debug7 = false;
/* 248 */         for (String debug9 : debug1) {
/* 249 */           if (!serverScoreboard.hasPlayerScore(debug9, debug6) || serverScoreboard.getOrCreatePlayerScore(debug9, debug6).isLocked()) {
/* 250 */             debug7 = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 254 */         if (debug7) {
/* 255 */           debug3.add(debug6.getName());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     return SharedSuggestionProvider.suggest(debug3, debug2);
/*     */   }
/*     */   
/*     */   private static int getScore(CommandSourceStack debug0, String debug1, Objective debug2) throws CommandSyntaxException {
/* 264 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 265 */     if (!serverScoreboard.hasPlayerScore(debug1, debug2)) {
/* 266 */       throw ERROR_NO_VALUE.create(debug2.getName(), debug1);
/*     */     }
/*     */     
/* 269 */     Score debug4 = serverScoreboard.getOrCreatePlayerScore(debug1, debug2);
/* 270 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.get.success", new Object[] { debug1, Integer.valueOf(debug4.getScore()), debug2.getFormattedDisplayName() }), false);
/*     */     
/* 272 */     return debug4.getScore();
/*     */   }
/*     */   
/*     */   private static int performOperation(CommandSourceStack debug0, Collection<String> debug1, Objective debug2, OperationArgument.Operation debug3, Collection<String> debug4, Objective debug5) throws CommandSyntaxException {
/* 276 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 277 */     int debug7 = 0;
/*     */     
/* 279 */     for (String debug9 : debug1) {
/* 280 */       Score debug10 = serverScoreboard.getOrCreatePlayerScore(debug9, debug2);
/* 281 */       for (String debug12 : debug4) {
/* 282 */         Score debug13 = serverScoreboard.getOrCreatePlayerScore(debug12, debug5);
/* 283 */         debug3.apply(debug10, debug13);
/*     */       } 
/* 285 */       debug7 += debug10.getScore();
/*     */     } 
/*     */     
/* 288 */     if (debug1.size() == 1) {
/* 289 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.operation.success.single", new Object[] { debug2.getFormattedDisplayName(), debug1.iterator().next(), Integer.valueOf(debug7) }), true);
/*     */     } else {
/* 291 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.operation.success.multiple", new Object[] { debug2.getFormattedDisplayName(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 294 */     return debug7;
/*     */   }
/*     */   
/*     */   private static int enableTrigger(CommandSourceStack debug0, Collection<String> debug1, Objective debug2) throws CommandSyntaxException {
/* 298 */     if (debug2.getCriteria() != ObjectiveCriteria.TRIGGER) {
/* 299 */       throw ERROR_NOT_TRIGGER.create();
/*     */     }
/* 301 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 303 */     int debug4 = 0;
/*     */     
/* 305 */     for (String debug6 : debug1) {
/* 306 */       Score debug7 = serverScoreboard.getOrCreatePlayerScore(debug6, debug2);
/* 307 */       if (debug7.isLocked()) {
/* 308 */         debug7.setLocked(false);
/* 309 */         debug4++;
/*     */       } 
/*     */     } 
/*     */     
/* 313 */     if (debug4 == 0) {
/* 314 */       throw ERROR_TRIGGER_ALREADY_ENABLED.create();
/*     */     }
/*     */     
/* 317 */     if (debug1.size() == 1) {
/* 318 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.enable.success.single", new Object[] { debug2.getFormattedDisplayName(), debug1.iterator().next() }), true);
/*     */     } else {
/* 320 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.enable.success.multiple", new Object[] { debug2.getFormattedDisplayName(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 323 */     return debug4;
/*     */   }
/*     */   
/*     */   private static int resetScores(CommandSourceStack debug0, Collection<String> debug1) {
/* 327 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 329 */     for (String debug4 : debug1) {
/* 330 */       serverScoreboard.resetPlayerScore(debug4, null);
/*     */     }
/*     */     
/* 333 */     if (debug1.size() == 1) {
/* 334 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.reset.all.single", new Object[] { debug1.iterator().next() }), true);
/*     */     } else {
/* 336 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.reset.all.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 339 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int resetScore(CommandSourceStack debug0, Collection<String> debug1, Objective debug2) {
/* 343 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 345 */     for (String debug5 : debug1) {
/* 346 */       serverScoreboard.resetPlayerScore(debug5, debug2);
/*     */     }
/*     */     
/* 349 */     if (debug1.size() == 1) {
/* 350 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.reset.specific.single", new Object[] { debug2.getFormattedDisplayName(), debug1.iterator().next() }), true);
/*     */     } else {
/* 352 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.reset.specific.multiple", new Object[] { debug2.getFormattedDisplayName(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 355 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int setScore(CommandSourceStack debug0, Collection<String> debug1, Objective debug2, int debug3) {
/* 359 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 361 */     for (String debug6 : debug1) {
/* 362 */       Score debug7 = serverScoreboard.getOrCreatePlayerScore(debug6, debug2);
/* 363 */       debug7.setScore(debug3);
/*     */     } 
/*     */     
/* 366 */     if (debug1.size() == 1) {
/* 367 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.set.success.single", new Object[] { debug2.getFormattedDisplayName(), debug1.iterator().next(), Integer.valueOf(debug3) }), true);
/*     */     } else {
/* 369 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.set.success.multiple", new Object[] { debug2.getFormattedDisplayName(), Integer.valueOf(debug1.size()), Integer.valueOf(debug3) }), true);
/*     */     } 
/*     */     
/* 372 */     return debug3 * debug1.size();
/*     */   }
/*     */   
/*     */   private static int addScore(CommandSourceStack debug0, Collection<String> debug1, Objective debug2, int debug3) {
/* 376 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 377 */     int debug5 = 0;
/*     */     
/* 379 */     for (String debug7 : debug1) {
/* 380 */       Score debug8 = serverScoreboard.getOrCreatePlayerScore(debug7, debug2);
/* 381 */       debug8.setScore(debug8.getScore() + debug3);
/* 382 */       debug5 += debug8.getScore();
/*     */     } 
/*     */     
/* 385 */     if (debug1.size() == 1) {
/* 386 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.add.success.single", new Object[] { Integer.valueOf(debug3), debug2.getFormattedDisplayName(), debug1.iterator().next(), Integer.valueOf(debug5) }), true);
/*     */     } else {
/* 388 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.add.success.multiple", new Object[] { Integer.valueOf(debug3), debug2.getFormattedDisplayName(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 391 */     return debug5;
/*     */   }
/*     */   
/*     */   private static int removeScore(CommandSourceStack debug0, Collection<String> debug1, Objective debug2, int debug3) {
/* 395 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 396 */     int debug5 = 0;
/*     */     
/* 398 */     for (String debug7 : debug1) {
/* 399 */       Score debug8 = serverScoreboard.getOrCreatePlayerScore(debug7, debug2);
/* 400 */       debug8.setScore(debug8.getScore() - debug3);
/* 401 */       debug5 += debug8.getScore();
/*     */     } 
/*     */     
/* 404 */     if (debug1.size() == 1) {
/* 405 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.remove.success.single", new Object[] { Integer.valueOf(debug3), debug2.getFormattedDisplayName(), debug1.iterator().next(), Integer.valueOf(debug5) }), true);
/*     */     } else {
/* 407 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.remove.success.multiple", new Object[] { Integer.valueOf(debug3), debug2.getFormattedDisplayName(), Integer.valueOf(debug1.size()) }), true);
/*     */     } 
/*     */     
/* 410 */     return debug5;
/*     */   }
/*     */   
/*     */   private static int listTrackedPlayers(CommandSourceStack debug0) {
/* 414 */     Collection<String> debug1 = debug0.getServer().getScoreboard().getTrackedPlayers();
/*     */     
/* 416 */     if (debug1.isEmpty()) {
/* 417 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.list.empty"), false);
/*     */     } else {
/* 419 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.list.success", new Object[] { Integer.valueOf(debug1.size()), ComponentUtils.formatList(debug1) }), false);
/*     */     } 
/*     */     
/* 422 */     return debug1.size();
/*     */   }
/*     */   
/*     */   private static int listTrackedPlayerScores(CommandSourceStack debug0, String debug1) {
/* 426 */     Map<Objective, Score> debug2 = debug0.getServer().getScoreboard().getPlayerScores(debug1);
/*     */     
/* 428 */     if (debug2.isEmpty()) {
/* 429 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.list.entity.empty", new Object[] { debug1 }), false);
/*     */     } else {
/* 431 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.list.entity.success", new Object[] { debug1, Integer.valueOf(debug2.size()) }), false);
/* 432 */       for (Map.Entry<Objective, Score> debug4 : debug2.entrySet()) {
/* 433 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.players.list.entity.entry", new Object[] { ((Objective)debug4.getKey()).getFormattedDisplayName(), Integer.valueOf(((Score)debug4.getValue()).getScore()) }), false);
/*     */       } 
/*     */     } 
/*     */     
/* 437 */     return debug2.size();
/*     */   }
/*     */   
/*     */   private static int clearDisplaySlot(CommandSourceStack debug0, int debug1) throws CommandSyntaxException {
/* 441 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 443 */     if (serverScoreboard.getDisplayObjective(debug1) == null) {
/* 444 */       throw ERROR_DISPLAY_SLOT_ALREADY_EMPTY.create();
/*     */     }
/*     */     
/* 447 */     serverScoreboard.setDisplayObjective(debug1, null);
/* 448 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.display.cleared", new Object[] { Scoreboard.getDisplaySlotNames()[debug1] }), true);
/*     */     
/* 450 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplaySlot(CommandSourceStack debug0, int debug1, Objective debug2) throws CommandSyntaxException {
/* 454 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 456 */     if (serverScoreboard.getDisplayObjective(debug1) == debug2) {
/* 457 */       throw ERROR_DISPLAY_SLOT_ALREADY_SET.create();
/*     */     }
/*     */     
/* 460 */     serverScoreboard.setDisplayObjective(debug1, debug2);
/* 461 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.display.set", new Object[] { Scoreboard.getDisplaySlotNames()[debug1], debug2.getDisplayName() }), true);
/*     */     
/* 463 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setDisplayName(CommandSourceStack debug0, Objective debug1, Component debug2) {
/* 467 */     if (!debug1.getDisplayName().equals(debug2)) {
/* 468 */       debug1.setDisplayName(debug2);
/* 469 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.modify.displayname", new Object[] { debug1.getName(), debug1.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 472 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setRenderType(CommandSourceStack debug0, Objective debug1, ObjectiveCriteria.RenderType debug2) {
/* 476 */     if (debug1.getRenderType() != debug2) {
/* 477 */       debug1.setRenderType(debug2);
/* 478 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.modify.rendertype", new Object[] { debug1.getFormattedDisplayName() }), true);
/*     */     } 
/*     */     
/* 481 */     return 0;
/*     */   }
/*     */   
/*     */   private static int removeObjective(CommandSourceStack debug0, Objective debug1) {
/* 485 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/* 486 */     serverScoreboard.removeObjective(debug1);
/* 487 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.remove.success", new Object[] { debug1.getFormattedDisplayName() }), true);
/* 488 */     return serverScoreboard.getObjectives().size();
/*     */   }
/*     */   
/*     */   private static int addObjective(CommandSourceStack debug0, String debug1, ObjectiveCriteria debug2, Component debug3) throws CommandSyntaxException {
/* 492 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 494 */     if (serverScoreboard.getObjective(debug1) != null) {
/* 495 */       throw ERROR_OBJECTIVE_ALREADY_EXISTS.create();
/*     */     }
/* 497 */     if (debug1.length() > 16) {
/* 498 */       throw ObjectiveArgument.ERROR_OBJECTIVE_NAME_TOO_LONG.create(Integer.valueOf(16));
/*     */     }
/*     */     
/* 501 */     serverScoreboard.addObjective(debug1, debug2, debug3, debug2.getDefaultRenderType());
/* 502 */     Objective debug5 = serverScoreboard.getObjective(debug1);
/*     */     
/* 504 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.add.success", new Object[] { debug5.getFormattedDisplayName() }), true);
/*     */     
/* 506 */     return serverScoreboard.getObjectives().size();
/*     */   }
/*     */   
/*     */   private static int listObjectives(CommandSourceStack debug0) {
/* 510 */     Collection<Objective> debug1 = debug0.getServer().getScoreboard().getObjectives();
/*     */     
/* 512 */     if (debug1.isEmpty()) {
/* 513 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.list.empty"), false);
/*     */     } else {
/* 515 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.scoreboard.objectives.list.success", new Object[] { Integer.valueOf(debug1.size()), ComponentUtils.formatList(debug1, Objective::getFormattedDisplayName) }), false);
/*     */     } 
/*     */     
/* 518 */     return debug1.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ScoreboardCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */