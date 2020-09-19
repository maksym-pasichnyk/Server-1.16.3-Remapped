/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.ResultConsumer;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.DimensionArgument;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.ObjectiveArgument;
/*     */ import net.minecraft.commands.arguments.RangeArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.commands.arguments.ScoreHolderArgument;
/*     */ import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.ByteTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.DoubleTag;
/*     */ import net.minecraft.nbt.FloatTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.LongTag;
/*     */ import net.minecraft.nbt.ShortTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.ServerScoreboard;
/*     */ import net.minecraft.server.bossevents.CustomBossEvent;
/*     */ import net.minecraft.server.commands.data.DataAccessor;
/*     */ import net.minecraft.server.commands.data.DataCommands;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.PredicateManager;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.Scoreboard;
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
/*     */ public class ExecuteCommand
/*     */ {
/*     */   private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
/*     */   
/*     */   static {
/*  97 */     ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.execute.blocks.toobig", new Object[] { debug0, debug1 }));
/*     */   }
/*  99 */   private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.execute.conditional.fail")); private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT; static {
/* 100 */     ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.execute.conditional.fail_count", new Object[] { debug0 }));
/*     */   }
/*     */   
/*     */   private static final BinaryOperator<ResultConsumer<CommandSourceStack>> CALLBACK_CHAINER = (debug0, debug1) -> ();
/*     */   private static final SuggestionProvider<CommandSourceStack> SUGGEST_PREDICATE;
/*     */   
/*     */   static {
/* 107 */     SUGGEST_PREDICATE = ((debug0, debug1) -> {
/*     */         PredicateManager debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getPredicateManager();
/*     */         return SharedSuggestionProvider.suggestResource(debug2.getKeys(), debug1);
/*     */       });
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
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 123 */     LiteralCommandNode<CommandSourceStack> debug1 = debug0.register((LiteralArgumentBuilder)Commands.literal("execute").requires(debug0 -> debug0.hasPermission(2)));
/*     */     
/* 125 */     debug0.register(
/* 126 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("execute")
/* 127 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 128 */         .then(
/* 129 */           Commands.literal("run")
/* 130 */           .redirect((CommandNode)debug0.getRoot())))
/*     */         
/* 132 */         .then(
/* 133 */           addConditionals((CommandNode<CommandSourceStack>)debug1, Commands.literal("if"), true)))
/*     */         
/* 135 */         .then(
/* 136 */           addConditionals((CommandNode<CommandSourceStack>)debug1, Commands.literal("unless"), false)))
/*     */         
/* 138 */         .then(
/* 139 */           Commands.literal("as")
/* 140 */           .then(
/* 141 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 142 */             .fork((CommandNode)debug1, debug0 -> {
/*     */                 List<CommandSourceStack> debug1 = Lists.newArrayList();
/*     */ 
/*     */                 
/*     */                 for (Entity debug3 : EntityArgument.getOptionalEntities(debug0, "targets")) {
/*     */                   debug1.add(((CommandSourceStack)debug0.getSource()).withEntity(debug3));
/*     */                 }
/*     */                 
/*     */                 return debug1;
/* 151 */               })))).then(
/* 152 */           Commands.literal("at")
/* 153 */           .then(
/* 154 */             Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 155 */             .fork((CommandNode)debug1, debug0 -> {
/*     */                 List<CommandSourceStack> debug1 = Lists.newArrayList();
/*     */ 
/*     */                 
/*     */                 for (Entity debug3 : EntityArgument.getOptionalEntities(debug0, "targets")) {
/*     */                   debug1.add(((CommandSourceStack)debug0.getSource()).withLevel((ServerLevel)debug3.level).withPosition(debug3.position()).withRotation(debug3.getRotationVector()));
/*     */                 }
/*     */                 
/*     */                 return debug1;
/* 164 */               })))).then((
/* 165 */           (LiteralArgumentBuilder)Commands.literal("store")
/* 166 */           .then(wrapStores(debug1, Commands.literal("result"), true)))
/* 167 */           .then(wrapStores(debug1, Commands.literal("success"), false))))
/*     */         
/* 169 */         .then((
/* 170 */           (LiteralArgumentBuilder)Commands.literal("positioned")
/* 171 */           .then(
/* 172 */             Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 173 */             .redirect((CommandNode)debug1, debug0 -> ((CommandSourceStack)debug0.getSource()).withPosition(Vec3Argument.getVec3(debug0, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET))))
/*     */           
/* 175 */           .then(
/* 176 */             Commands.literal("as")
/* 177 */             .then(
/* 178 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 179 */               .fork((CommandNode)debug1, debug0 -> {
/*     */                   List<CommandSourceStack> debug1 = Lists.newArrayList();
/*     */ 
/*     */                   
/*     */                   for (Entity debug3 : EntityArgument.getOptionalEntities(debug0, "targets")) {
/*     */                     debug1.add(((CommandSourceStack)debug0.getSource()).withPosition(debug3.position()));
/*     */                   }
/*     */ 
/*     */                   
/*     */                   return debug1;
/* 189 */                 }))))).then((
/* 190 */           (LiteralArgumentBuilder)Commands.literal("rotated")
/* 191 */           .then(
/* 192 */             Commands.argument("rot", (ArgumentType)RotationArgument.rotation())
/* 193 */             .redirect((CommandNode)debug1, debug0 -> ((CommandSourceStack)debug0.getSource()).withRotation(RotationArgument.getRotation(debug0, "rot").getRotation((CommandSourceStack)debug0.getSource())))))
/*     */           
/* 195 */           .then(
/* 196 */             Commands.literal("as")
/* 197 */             .then(
/* 198 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 199 */               .fork((CommandNode)debug1, debug0 -> {
/*     */                   List<CommandSourceStack> debug1 = Lists.newArrayList();
/*     */ 
/*     */                   
/*     */                   for (Entity debug3 : EntityArgument.getOptionalEntities(debug0, "targets")) {
/*     */                     debug1.add(((CommandSourceStack)debug0.getSource()).withRotation(debug3.getRotationVector()));
/*     */                   }
/*     */ 
/*     */                   
/*     */                   return debug1;
/* 209 */                 }))))).then((
/* 210 */           (LiteralArgumentBuilder)Commands.literal("facing")
/* 211 */           .then(
/* 212 */             Commands.literal("entity")
/* 213 */             .then(
/* 214 */               Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 215 */               .then(
/* 216 */                 Commands.argument("anchor", (ArgumentType)EntityAnchorArgument.anchor())
/* 217 */                 .fork((CommandNode)debug1, debug0 -> {
/*     */                     List<CommandSourceStack> debug1 = Lists.newArrayList();
/*     */ 
/*     */                     
/*     */                     EntityAnchorArgument.Anchor debug2 = EntityAnchorArgument.getAnchor(debug0, "anchor");
/*     */                     
/*     */                     for (Entity debug4 : EntityArgument.getOptionalEntities(debug0, "targets")) {
/*     */                       debug1.add(((CommandSourceStack)debug0.getSource()).facing(debug4, debug2));
/*     */                     }
/*     */                     
/*     */                     return debug1;
/* 228 */                   }))))).then(
/* 229 */             Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 230 */             .redirect((CommandNode)debug1, debug0 -> ((CommandSourceStack)debug0.getSource()).facing(Vec3Argument.getVec3(debug0, "pos"))))))
/*     */ 
/*     */         
/* 233 */         .then(
/* 234 */           Commands.literal("align")
/* 235 */           .then(
/* 236 */             Commands.argument("axes", (ArgumentType)SwizzleArgument.swizzle())
/* 237 */             .redirect((CommandNode)debug1, debug0 -> ((CommandSourceStack)debug0.getSource()).withPosition(((CommandSourceStack)debug0.getSource()).getPosition().align(SwizzleArgument.getSwizzle(debug0, "axes")))))))
/*     */ 
/*     */         
/* 240 */         .then(
/* 241 */           Commands.literal("anchored")
/* 242 */           .then(
/* 243 */             Commands.argument("anchor", (ArgumentType)EntityAnchorArgument.anchor())
/* 244 */             .redirect((CommandNode)debug1, debug0 -> ((CommandSourceStack)debug0.getSource()).withAnchor(EntityAnchorArgument.getAnchor(debug0, "anchor"))))))
/*     */ 
/*     */         
/* 247 */         .then(
/* 248 */           Commands.literal("in")
/* 249 */           .then(
/* 250 */             Commands.argument("dimension", (ArgumentType)DimensionArgument.dimension())
/* 251 */             .redirect((CommandNode)debug1, debug0 -> ((CommandSourceStack)debug0.getSource()).withLevel(DimensionArgument.getDimension(debug0, "dimension"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(LiteralCommandNode<CommandSourceStack> debug0, LiteralArgumentBuilder<CommandSourceStack> debug1, boolean debug2) {
/* 258 */     debug1.then(
/* 259 */         Commands.literal("score")
/* 260 */         .then(
/* 261 */           Commands.argument("targets", (ArgumentType)ScoreHolderArgument.scoreHolders())
/* 262 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 263 */           .then(
/* 264 */             Commands.argument("objective", (ArgumentType)ObjectiveArgument.objective())
/* 265 */             .redirect((CommandNode)debug0, debug1 -> storeValue((CommandSourceStack)debug1.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(debug1, "targets"), ObjectiveArgument.getObjective(debug1, "objective"), debug0)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     debug1.then(
/* 271 */         Commands.literal("bossbar")
/* 272 */         .then((
/* 273 */           (RequiredArgumentBuilder)Commands.argument("id", (ArgumentType)ResourceLocationArgument.id())
/* 274 */           .suggests(BossBarCommands.SUGGEST_BOSS_BAR)
/* 275 */           .then(
/* 276 */             Commands.literal("value")
/* 277 */             .redirect((CommandNode)debug0, debug1 -> storeValue((CommandSourceStack)debug1.getSource(), BossBarCommands.getBossBar(debug1), true, debug0))))
/*     */           
/* 279 */           .then(
/* 280 */             Commands.literal("max")
/* 281 */             .redirect((CommandNode)debug0, debug1 -> storeValue((CommandSourceStack)debug1.getSource(), BossBarCommands.getBossBar(debug1), false, debug0)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     for (Iterator<DataCommands.DataProvider> iterator = DataCommands.TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) { DataCommands.DataProvider debug4 = iterator.next();
/* 287 */       debug4.wrap((ArgumentBuilder)debug1, debug3 -> debug3.then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).then(Commands.literal("int").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)debug0, ())))).then(Commands.literal("float").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)debug0, ())))).then(Commands.literal("short").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)debug0, ())))).then(Commands.literal("long").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)debug0, ())))).then(Commands.literal("double").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)debug0, ())))).then(Commands.literal("byte").then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).redirect((CommandNode)debug0, ()))))); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     return (ArgumentBuilder)debug1;
/*     */   }
/*     */   
/*     */   private static CommandSourceStack storeValue(CommandSourceStack debug0, Collection<String> debug1, Objective debug2, boolean debug3) {
/* 345 */     ServerScoreboard serverScoreboard = debug0.getServer().getScoreboard();
/*     */     
/* 347 */     return debug0.withCallback((debug4, debug5, debug6) -> { for (String debug8 : debug0) { Score debug9 = debug1.getOrCreatePlayerScore(debug8, debug2); int debug10 = debug3 ? debug6 : (debug5 ? 1 : 0); debug9.setScore(debug10); }  }CALLBACK_CHAINER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CommandSourceStack storeValue(CommandSourceStack debug0, CustomBossEvent debug1, boolean debug2, boolean debug3) {
/* 357 */     return debug0.withCallback((debug3, debug4, debug5) -> { int debug6 = debug0 ? debug5 : (debug4 ? 1 : 0); if (debug1) { debug2.setValue(debug6); } else { debug2.setMax(debug6); }  }CALLBACK_CHAINER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CommandSourceStack storeData(CommandSourceStack debug0, DataAccessor debug1, NbtPathArgument.NbtPath debug2, IntFunction<Tag> debug3, boolean debug4) {
/* 368 */     return debug0.withCallback((debug4, debug5, debug6) -> {
/*     */           try {
/*     */             CompoundTag debug7 = debug0.getData();
/*     */             int debug8 = debug1 ? debug6 : (debug5 ? 1 : 0);
/*     */             debug2.set((Tag)debug7, ());
/*     */             debug0.setData(debug7);
/* 374 */           } catch (CommandSyntaxException commandSyntaxException) {}
/*     */         }CALLBACK_CHAINER);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(CommandNode<CommandSourceStack> debug0, LiteralArgumentBuilder<CommandSourceStack> debug1, boolean debug2) {
/* 380 */     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)debug1
/* 381 */       .then(
/* 382 */         Commands.literal("block")
/* 383 */         .then(
/* 384 */           Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 385 */           .then(
/* 386 */             addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("block", (ArgumentType)BlockPredicateArgument.blockPredicate()), debug2, debug0 -> BlockPredicateArgument.getBlockPredicate(debug0, "block").test(new BlockInWorld((LevelReader)((CommandSourceStack)debug0.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(debug0, "pos"), true)))))))
/*     */ 
/*     */ 
/*     */       
/* 390 */       .then(
/* 391 */         Commands.literal("score")
/* 392 */         .then(
/* 393 */           Commands.argument("target", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 394 */           .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 395 */           .then((
/* 396 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targetObjective", (ArgumentType)ObjectiveArgument.objective())
/* 397 */             .then(
/* 398 */               Commands.literal("=")
/* 399 */               .then(
/* 400 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 401 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 402 */                 .then(
/* 403 */                   addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), debug2, debug0 -> checkScore(debug0, Integer::equals))))))
/*     */ 
/*     */ 
/*     */             
/* 407 */             .then(
/* 408 */               Commands.literal("<")
/* 409 */               .then(
/* 410 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 411 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 412 */                 .then(
/* 413 */                   addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), debug2, debug0 -> checkScore(debug0, ()))))))
/*     */ 
/*     */ 
/*     */             
/* 417 */             .then(
/* 418 */               Commands.literal("<=")
/* 419 */               .then(
/* 420 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 421 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 422 */                 .then(
/* 423 */                   addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), debug2, debug0 -> checkScore(debug0, ()))))))
/*     */ 
/*     */ 
/*     */             
/* 427 */             .then(
/* 428 */               Commands.literal(">")
/* 429 */               .then(
/* 430 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 431 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 432 */                 .then(
/* 433 */                   addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), debug2, debug0 -> checkScore(debug0, ()))))))
/*     */ 
/*     */ 
/*     */             
/* 437 */             .then(
/* 438 */               Commands.literal(">=")
/* 439 */               .then(
/* 440 */                 Commands.argument("source", (ArgumentType)ScoreHolderArgument.scoreHolder())
/* 441 */                 .suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS)
/* 442 */                 .then(
/* 443 */                   addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("sourceObjective", (ArgumentType)ObjectiveArgument.objective()), debug2, debug0 -> checkScore(debug0, ()))))))
/*     */ 
/*     */ 
/*     */             
/* 447 */             .then(
/* 448 */               Commands.literal("matches")
/* 449 */               .then(
/* 450 */                 addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("range", (ArgumentType)RangeArgument.intRange()), debug2, debug0 -> checkScore(debug0, RangeArgument.Ints.getRange(debug0, "range")))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 456 */       .then(
/* 457 */         Commands.literal("blocks")
/* 458 */         .then(
/* 459 */           Commands.argument("start", (ArgumentType)BlockPosArgument.blockPos())
/* 460 */           .then(
/* 461 */             Commands.argument("end", (ArgumentType)BlockPosArgument.blockPos())
/* 462 */             .then((
/* 463 */               (RequiredArgumentBuilder)Commands.argument("destination", (ArgumentType)BlockPosArgument.blockPos())
/* 464 */               .then(
/* 465 */                 addIfBlocksConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("all"), debug2, false)))
/*     */               
/* 467 */               .then(
/* 468 */                 addIfBlocksConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("masked"), debug2, true)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 474 */       .then(
/* 475 */         Commands.literal("entity")
/* 476 */         .then((
/* 477 */           (RequiredArgumentBuilder)Commands.argument("entities", (ArgumentType)EntityArgument.entities())
/* 478 */           .fork(debug0, debug1 -> expect(debug1, debug0, !EntityArgument.getOptionalEntities(debug1, "entities").isEmpty())))
/* 479 */           .executes(createNumericConditionalHandler(debug2, debug0 -> EntityArgument.getOptionalEntities(debug0, "entities").size())))))
/*     */ 
/*     */ 
/*     */       
/* 483 */       .then(
/* 484 */         Commands.literal("predicate")
/* 485 */         .then(
/* 486 */           addConditional(debug0, (ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("predicate", (ArgumentType)ResourceLocationArgument.id()).suggests(SUGGEST_PREDICATE), debug2, debug0 -> checkCustomPredicate((CommandSourceStack)debug0.getSource(), ResourceLocationArgument.getPredicate(debug0, "predicate")))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 491 */     for (Iterator<DataCommands.DataProvider> iterator = DataCommands.SOURCE_PROVIDERS.iterator(); iterator.hasNext(); ) { DataCommands.DataProvider debug4 = iterator.next();
/* 492 */       debug1
/* 493 */         .then(debug4
/* 494 */           .wrap((ArgumentBuilder)Commands.literal("data"), debug3 -> debug3.then(((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).fork(debug0, ())).executes(createNumericConditionalHandler(debug1, ()))))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 504 */     return (ArgumentBuilder)debug1;
/*     */   }
/*     */   
/*     */   private static Command<CommandSourceStack> createNumericConditionalHandler(boolean debug0, CommandNumericPredicate debug1) {
/* 508 */     if (debug0) {
/* 509 */       return debug1 -> {
/*     */           int debug2 = debug0.test(debug1);
/*     */           
/*     */           if (debug2 > 0) {
/*     */             ((CommandSourceStack)debug1.getSource()).sendSuccess((Component)new TranslatableComponent("commands.execute.conditional.pass_count", new Object[] { Integer.valueOf(debug2) }), false);
/*     */             return debug2;
/*     */           } 
/*     */           throw ERROR_CONDITIONAL_FAILED.create();
/*     */         };
/*     */     }
/* 519 */     return debug1 -> {
/*     */         int debug2 = debug0.test(debug1);
/*     */         if (debug2 == 0) {
/*     */           ((CommandSourceStack)debug1.getSource()).sendSuccess((Component)new TranslatableComponent("commands.execute.conditional.pass"), false);
/*     */           return 1;
/*     */         } 
/*     */         throw ERROR_CONDITIONAL_FAILED_COUNT.create(Integer.valueOf(debug2));
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int checkMatchingData(DataAccessor debug0, NbtPathArgument.NbtPath debug1) throws CommandSyntaxException {
/* 532 */     return debug1.countMatching((Tag)debug0.getData());
/*     */   }
/*     */   
/*     */   private static boolean checkScore(CommandContext<CommandSourceStack> debug0, BiPredicate<Integer, Integer> debug1) throws CommandSyntaxException {
/* 536 */     String debug2 = ScoreHolderArgument.getName(debug0, "target");
/* 537 */     Objective debug3 = ObjectiveArgument.getObjective(debug0, "targetObjective");
/* 538 */     String debug4 = ScoreHolderArgument.getName(debug0, "source");
/* 539 */     Objective debug5 = ObjectiveArgument.getObjective(debug0, "sourceObjective");
/*     */     
/* 541 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)debug0.getSource()).getServer().getScoreboard();
/*     */     
/* 543 */     if (!serverScoreboard.hasPlayerScore(debug2, debug3) || !serverScoreboard.hasPlayerScore(debug4, debug5)) {
/* 544 */       return false;
/*     */     }
/*     */     
/* 547 */     Score debug7 = serverScoreboard.getOrCreatePlayerScore(debug2, debug3);
/* 548 */     Score debug8 = serverScoreboard.getOrCreatePlayerScore(debug4, debug5);
/* 549 */     return debug1.test(Integer.valueOf(debug7.getScore()), Integer.valueOf(debug8.getScore()));
/*     */   }
/*     */   
/*     */   private static boolean checkScore(CommandContext<CommandSourceStack> debug0, MinMaxBounds.Ints debug1) throws CommandSyntaxException {
/* 553 */     String debug2 = ScoreHolderArgument.getName(debug0, "target");
/* 554 */     Objective debug3 = ObjectiveArgument.getObjective(debug0, "targetObjective");
/*     */     
/* 556 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)debug0.getSource()).getServer().getScoreboard();
/*     */     
/* 558 */     if (!serverScoreboard.hasPlayerScore(debug2, debug3)) {
/* 559 */       return false;
/*     */     }
/*     */     
/* 562 */     return debug1.matches(serverScoreboard.getOrCreatePlayerScore(debug2, debug3).getScore());
/*     */   }
/*     */   
/*     */   private static boolean checkCustomPredicate(CommandSourceStack debug0, LootItemCondition debug1) {
/* 566 */     ServerLevel debug2 = debug0.getLevel();
/*     */ 
/*     */ 
/*     */     
/* 570 */     LootContext.Builder debug3 = (new LootContext.Builder(debug2)).withParameter(LootContextParams.ORIGIN, debug0.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, debug0.getEntity());
/*     */     
/* 572 */     return debug1.test(debug3.create(LootContextParamSets.COMMAND));
/*     */   }
/*     */   
/*     */   private static Collection<CommandSourceStack> expect(CommandContext<CommandSourceStack> debug0, boolean debug1, boolean debug2) {
/* 576 */     if (debug2 == debug1) {
/* 577 */       return Collections.singleton(debug0.getSource());
/*     */     }
/* 579 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> debug0, ArgumentBuilder<CommandSourceStack, ?> debug1, boolean debug2, CommandPredicate debug3) {
/* 584 */     return debug1
/* 585 */       .fork(debug0, debug2 -> expect(debug2, debug0, debug1.test(debug2)))
/* 586 */       .executes(debug2 -> {
/*     */           if (debug0 == debug1.test(debug2)) {
/*     */             ((CommandSourceStack)debug2.getSource()).sendSuccess((Component)new TranslatableComponent("commands.execute.conditional.pass"), false);
/*     */             return 1;
/*     */           } 
/*     */           throw ERROR_CONDITIONAL_FAILED.create();
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(CommandNode<CommandSourceStack> debug0, ArgumentBuilder<CommandSourceStack, ?> debug1, boolean debug2, boolean debug3) {
/* 597 */     return debug1
/* 598 */       .fork(debug0, debug2 -> expect(debug2, debug0, checkRegions(debug2, debug1).isPresent()))
/* 599 */       .executes(debug2 ? (debug1 -> checkIfRegions(debug1, debug0)) : (debug1 -> checkUnlessRegions(debug1, debug0)));
/*     */   }
/*     */   
/*     */   private static int checkIfRegions(CommandContext<CommandSourceStack> debug0, boolean debug1) throws CommandSyntaxException {
/* 603 */     OptionalInt debug2 = checkRegions(debug0, debug1);
/* 604 */     if (debug2.isPresent()) {
/* 605 */       ((CommandSourceStack)debug0.getSource()).sendSuccess((Component)new TranslatableComponent("commands.execute.conditional.pass_count", new Object[] { Integer.valueOf(debug2.getAsInt()) }), false);
/* 606 */       return debug2.getAsInt();
/*     */     } 
/* 608 */     throw ERROR_CONDITIONAL_FAILED.create();
/*     */   }
/*     */ 
/*     */   
/*     */   private static int checkUnlessRegions(CommandContext<CommandSourceStack> debug0, boolean debug1) throws CommandSyntaxException {
/* 613 */     OptionalInt debug2 = checkRegions(debug0, debug1);
/* 614 */     if (debug2.isPresent()) {
/* 615 */       throw ERROR_CONDITIONAL_FAILED_COUNT.create(Integer.valueOf(debug2.getAsInt()));
/*     */     }
/* 617 */     ((CommandSourceStack)debug0.getSource()).sendSuccess((Component)new TranslatableComponent("commands.execute.conditional.pass"), false);
/* 618 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static OptionalInt checkRegions(CommandContext<CommandSourceStack> debug0, boolean debug1) throws CommandSyntaxException {
/* 623 */     return checkRegions(((CommandSourceStack)debug0.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(debug0, "start"), BlockPosArgument.getLoadedBlockPos(debug0, "end"), BlockPosArgument.getLoadedBlockPos(debug0, "destination"), debug1);
/*     */   }
/*     */   
/*     */   private static OptionalInt checkRegions(ServerLevel debug0, BlockPos debug1, BlockPos debug2, BlockPos debug3, boolean debug4) throws CommandSyntaxException {
/* 627 */     BoundingBox debug5 = new BoundingBox((Vec3i)debug1, (Vec3i)debug2);
/* 628 */     BoundingBox debug6 = new BoundingBox((Vec3i)debug3, (Vec3i)debug3.offset(debug5.getLength()));
/* 629 */     BlockPos debug7 = new BlockPos(debug6.x0 - debug5.x0, debug6.y0 - debug5.y0, debug6.z0 - debug5.z0);
/* 630 */     int debug8 = debug5.getXSpan() * debug5.getYSpan() * debug5.getZSpan();
/*     */     
/* 632 */     if (debug8 > 32768) {
/* 633 */       throw ERROR_AREA_TOO_LARGE.create(Integer.valueOf(32768), Integer.valueOf(debug8));
/*     */     }
/*     */     
/* 636 */     int debug9 = 0;
/* 637 */     for (int debug10 = debug5.z0; debug10 <= debug5.z1; debug10++) {
/* 638 */       for (int debug11 = debug5.y0; debug11 <= debug5.y1; debug11++) {
/* 639 */         for (int debug12 = debug5.x0; debug12 <= debug5.x1; debug12++) {
/* 640 */           BlockPos debug13 = new BlockPos(debug12, debug11, debug10);
/* 641 */           BlockPos debug14 = debug13.offset((Vec3i)debug7);
/*     */           
/* 643 */           BlockState debug15 = debug0.getBlockState(debug13);
/* 644 */           if (!debug4 || !debug15.is(Blocks.AIR)) {
/*     */ 
/*     */ 
/*     */             
/* 648 */             if (debug15 != debug0.getBlockState(debug14)) {
/* 649 */               return OptionalInt.empty();
/*     */             }
/*     */             
/* 652 */             BlockEntity debug16 = debug0.getBlockEntity(debug13);
/* 653 */             BlockEntity debug17 = debug0.getBlockEntity(debug14);
/* 654 */             if (debug16 != null) {
/* 655 */               if (debug17 == null) {
/* 656 */                 return OptionalInt.empty();
/*     */               }
/* 658 */               CompoundTag debug18 = debug16.save(new CompoundTag());
/* 659 */               debug18.remove("x");
/* 660 */               debug18.remove("y");
/* 661 */               debug18.remove("z");
/*     */               
/* 663 */               CompoundTag debug19 = debug17.save(new CompoundTag());
/* 664 */               debug19.remove("x");
/* 665 */               debug19.remove("y");
/* 666 */               debug19.remove("z");
/*     */               
/* 668 */               if (!debug18.equals(debug19)) {
/* 669 */                 return OptionalInt.empty();
/*     */               }
/*     */             } 
/*     */             
/* 673 */             debug9++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 678 */     return OptionalInt.of(debug9);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface CommandNumericPredicate {
/*     */     int test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   static interface CommandPredicate {
/*     */     boolean test(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ExecuteCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */