/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.data.structures.NbtToSnbt;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ public class TestCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  71 */     debug0.register(
/*  72 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("test")
/*  73 */         .then(
/*  74 */           Commands.literal("runthis")
/*  75 */           .executes(debug0 -> runNearbyTest((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  77 */         .then(
/*  78 */           Commands.literal("runthese")
/*  79 */           .executes(debug0 -> runAllNearbyTests((CommandSourceStack)debug0.getSource()))))
/*     */         
/*  81 */         .then((
/*  82 */           (LiteralArgumentBuilder)Commands.literal("runfailed")
/*  83 */           .executes(debug0 -> runLastFailedTests((CommandSourceStack)debug0.getSource(), false, 0, 8)))
/*  84 */           .then(((RequiredArgumentBuilder)Commands.argument("onlyRequiredTests", (ArgumentType)BoolArgumentType.bool())
/*  85 */             .executes(debug0 -> runLastFailedTests((CommandSourceStack)debug0.getSource(), BoolArgumentType.getBool(debug0, "onlyRequiredTests"), 0, 8)))
/*  86 */             .then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", (ArgumentType)IntegerArgumentType.integer())
/*  87 */               .executes(debug0 -> runLastFailedTests((CommandSourceStack)debug0.getSource(), BoolArgumentType.getBool(debug0, "onlyRequiredTests"), IntegerArgumentType.getInteger(debug0, "rotationSteps"), 8)))
/*  88 */               .then(Commands.argument("testsPerRow", (ArgumentType)IntegerArgumentType.integer())
/*  89 */                 .executes(debug0 -> runLastFailedTests((CommandSourceStack)debug0.getSource(), BoolArgumentType.getBool(debug0, "onlyRequiredTests"), IntegerArgumentType.getInteger(debug0, "rotationSteps"), IntegerArgumentType.getInteger(debug0, "testsPerRow"))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  94 */         .then(
/*  95 */           Commands.literal("run")
/*  96 */           .then((
/*  97 */             (RequiredArgumentBuilder)Commands.argument("testName", TestFunctionArgument.testFunctionArgument())
/*  98 */             .executes(debug0 -> runTest((CommandSourceStack)debug0.getSource(), TestFunctionArgument.getTestFunction(debug0, "testName"), 0)))
/*  99 */             .then(Commands.argument("rotationSteps", (ArgumentType)IntegerArgumentType.integer())
/* 100 */               .executes(debug0 -> runTest((CommandSourceStack)debug0.getSource(), TestFunctionArgument.getTestFunction(debug0, "testName"), IntegerArgumentType.getInteger(debug0, "rotationSteps")))))))
/*     */ 
/*     */ 
/*     */         
/* 104 */         .then((
/* 105 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("runall")
/* 106 */           .executes(debug0 -> runAllTests((CommandSourceStack)debug0.getSource(), 0, 8)))
/* 107 */           .then((
/* 108 */             (RequiredArgumentBuilder)Commands.argument("testClassName", TestClassNameArgument.testClassName())
/* 109 */             .executes(debug0 -> runAllTestsInClass((CommandSourceStack)debug0.getSource(), TestClassNameArgument.getTestClassName(debug0, "testClassName"), 0, 8)))
/* 110 */             .then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", (ArgumentType)IntegerArgumentType.integer())
/* 111 */               .executes(debug0 -> runAllTestsInClass((CommandSourceStack)debug0.getSource(), TestClassNameArgument.getTestClassName(debug0, "testClassName"), IntegerArgumentType.getInteger(debug0, "rotationSteps"), 8)))
/* 112 */               .then(Commands.argument("testsPerRow", (ArgumentType)IntegerArgumentType.integer())
/* 113 */                 .executes(debug0 -> runAllTestsInClass((CommandSourceStack)debug0.getSource(), TestClassNameArgument.getTestClassName(debug0, "testClassName"), IntegerArgumentType.getInteger(debug0, "rotationSteps"), IntegerArgumentType.getInteger(debug0, "testsPerRow")))))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 118 */           .then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", (ArgumentType)IntegerArgumentType.integer())
/* 119 */             .executes(debug0 -> runAllTests((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "rotationSteps"), 8)))
/* 120 */             .then(Commands.argument("testsPerRow", (ArgumentType)IntegerArgumentType.integer())
/* 121 */               .executes(debug0 -> runAllTests((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "rotationSteps"), IntegerArgumentType.getInteger(debug0, "testsPerRow")))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 126 */         .then(
/* 127 */           Commands.literal("export")
/* 128 */           .then(
/* 129 */             Commands.argument("testName", (ArgumentType)StringArgumentType.word())
/* 130 */             .executes(debug0 -> exportTestStructure((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "testName"))))))
/*     */ 
/*     */         
/* 133 */         .then(
/* 134 */           Commands.literal("exportthis")
/* 135 */           .executes(debug0 -> exportNearestTestStructure((CommandSourceStack)debug0.getSource()))))
/*     */         
/* 137 */         .then(
/* 138 */           Commands.literal("import")
/* 139 */           .then(
/* 140 */             Commands.argument("testName", (ArgumentType)StringArgumentType.word())
/* 141 */             .executes(debug0 -> importTestStructure((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "testName"))))))
/*     */ 
/*     */         
/* 144 */         .then((
/* 145 */           (LiteralArgumentBuilder)Commands.literal("pos")
/* 146 */           .executes(debug0 -> showPos((CommandSourceStack)debug0.getSource(), "pos")))
/* 147 */           .then(
/* 148 */             Commands.argument("var", (ArgumentType)StringArgumentType.word())
/* 149 */             .executes(debug0 -> showPos((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "var"))))))
/*     */ 
/*     */         
/* 152 */         .then(
/* 153 */           Commands.literal("create")
/* 154 */           .then((
/* 155 */             (RequiredArgumentBuilder)Commands.argument("testName", (ArgumentType)StringArgumentType.word())
/* 156 */             .executes(debug0 -> createNewStructure((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "testName"), 5, 5, 5)))
/* 157 */             .then((
/* 158 */               (RequiredArgumentBuilder)Commands.argument("width", (ArgumentType)IntegerArgumentType.integer())
/* 159 */               .executes(debug0 -> createNewStructure((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "testName"), IntegerArgumentType.getInteger(debug0, "width"), IntegerArgumentType.getInteger(debug0, "width"), IntegerArgumentType.getInteger(debug0, "width"))))
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 164 */               .then(
/* 165 */                 Commands.argument("height", (ArgumentType)IntegerArgumentType.integer())
/* 166 */                 .then(
/* 167 */                   Commands.argument("depth", (ArgumentType)IntegerArgumentType.integer())
/* 168 */                   .executes(debug0 -> createNewStructure((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "testName"), IntegerArgumentType.getInteger(debug0, "width"), IntegerArgumentType.getInteger(debug0, "height"), IntegerArgumentType.getInteger(debug0, "depth")))))))))
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
/* 179 */         .then((
/* 180 */           (LiteralArgumentBuilder)Commands.literal("clearall")
/* 181 */           .executes(debug0 -> clearAllTests((CommandSourceStack)debug0.getSource(), 200)))
/* 182 */           .then(
/* 183 */             Commands.argument("radius", (ArgumentType)IntegerArgumentType.integer())
/* 184 */             .executes(debug0 -> clearAllTests((CommandSourceStack)debug0.getSource(), IntegerArgumentType.getInteger(debug0, "radius"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int createNewStructure(CommandSourceStack debug0, String debug1, int debug2, int debug3, int debug4) {
/* 191 */     if (debug2 > 48 || debug3 > 48 || debug4 > 48) {
/* 192 */       throw new IllegalArgumentException("The structure must be less than 48 blocks big in each axis");
/*     */     }
/*     */     
/* 195 */     ServerLevel debug5 = debug0.getLevel();
/* 196 */     BlockPos debug6 = new BlockPos(debug0.getPosition());
/* 197 */     BlockPos debug7 = new BlockPos(debug6.getX(), debug0.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, debug6).getY(), debug6.getZ() + 3);
/*     */     
/* 199 */     StructureUtils.createNewEmptyStructureBlock(debug1.toLowerCase(), debug7, new BlockPos(debug2, debug3, debug4), Rotation.NONE, debug5);
/*     */     
/* 201 */     for (int debug8 = 0; debug8 < debug2; debug8++) {
/* 202 */       for (int debug9 = 0; debug9 < debug4; debug9++) {
/* 203 */         BlockPos debug10 = new BlockPos(debug7.getX() + debug8, debug7.getY() + 1, debug7.getZ() + debug9);
/* 204 */         Block debug11 = Blocks.POLISHED_ANDESITE;
/* 205 */         BlockInput debug12 = new BlockInput(debug11.defaultBlockState(), Collections.EMPTY_SET, null);
/* 206 */         debug12.place(debug5, debug10, 2);
/*     */       } 
/*     */     } 
/*     */     
/* 210 */     StructureUtils.addCommandBlockAndButtonToStartTest(debug7, new BlockPos(1, 0, -1), Rotation.NONE, debug5);
/*     */     
/* 212 */     return 0;
/*     */   }
/*     */   
/*     */   private static int showPos(CommandSourceStack debug0, String debug1) throws CommandSyntaxException {
/* 216 */     BlockHitResult debug2 = (BlockHitResult)debug0.getPlayerOrException().pick(10.0D, 1.0F, false);
/*     */     
/* 218 */     BlockPos debug3 = debug2.getBlockPos();
/* 219 */     ServerLevel debug4 = debug0.getLevel();
/*     */     
/* 221 */     Optional<BlockPos> debug5 = StructureUtils.findStructureBlockContainingPos(debug3, 15, debug4);
/* 222 */     if (!debug5.isPresent())
/*     */     {
/* 224 */       debug5 = StructureUtils.findStructureBlockContainingPos(debug3, 200, debug4);
/*     */     }
/*     */     
/* 227 */     if (!debug5.isPresent()) {
/* 228 */       debug0.sendFailure((Component)new TextComponent("Can't find a structure block that contains the targeted pos " + debug3));
/* 229 */       return 0;
/*     */     } 
/* 231 */     StructureBlockEntity debug6 = (StructureBlockEntity)debug4.getBlockEntity(debug5.get());
/*     */     
/* 233 */     BlockPos debug7 = debug3.subtract((Vec3i)debug5.get());
/* 234 */     String debug8 = debug7.getX() + ", " + debug7.getY() + ", " + debug7.getZ();
/* 235 */     String debug9 = debug6.getStructurePath();
/*     */ 
/*     */     
/* 238 */     MutableComponent mutableComponent = (new TextComponent(debug8)).setStyle(Style.EMPTY
/* 239 */         .withBold(Boolean.valueOf(true))
/* 240 */         .withColor(ChatFormatting.GREEN)
/* 241 */         .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("Click to copy to clipboard")))
/* 242 */         .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "final BlockPos " + debug1 + " = new BlockPos(" + debug8 + ");")));
/*     */     
/* 244 */     debug0.sendSuccess((Component)(new TextComponent("Position relative to " + debug9 + ": ")).append((Component)mutableComponent), false);
/*     */     
/* 246 */     DebugPackets.sendGameTestAddMarker(debug4, new BlockPos((Vec3i)debug3), debug8, -2147418368, 10000);
/*     */     
/* 248 */     return 1;
/*     */   }
/*     */   
/*     */   private static int runNearbyTest(CommandSourceStack debug0) {
/* 252 */     BlockPos debug1 = new BlockPos(debug0.getPosition());
/* 253 */     ServerLevel debug2 = debug0.getLevel();
/* 254 */     BlockPos debug3 = StructureUtils.findNearestStructureBlock(debug1, 15, debug2);
/* 255 */     if (debug3 == null) {
/* 256 */       say(debug2, "Couldn't find any structure block within 15 radius", ChatFormatting.RED);
/* 257 */       return 0;
/*     */     } 
/*     */     
/* 260 */     GameTestRunner.clearMarkers(debug2);
/*     */     
/* 262 */     runTest(debug2, debug3, (MultipleTestTracker)null);
/*     */     
/* 264 */     return 1;
/*     */   }
/*     */   
/*     */   private static int runAllNearbyTests(CommandSourceStack debug0) {
/* 268 */     BlockPos debug1 = new BlockPos(debug0.getPosition());
/* 269 */     ServerLevel debug2 = debug0.getLevel();
/* 270 */     Collection<BlockPos> debug3 = StructureUtils.findStructureBlocks(debug1, 200, debug2);
/*     */     
/* 272 */     if (debug3.isEmpty()) {
/* 273 */       say(debug2, "Couldn't find any structure blocks within 200 block radius", ChatFormatting.RED);
/* 274 */       return 1;
/*     */     } 
/*     */     
/* 277 */     GameTestRunner.clearMarkers(debug2);
/*     */     
/* 279 */     say(debug0, "Running " + debug3.size() + " tests...");
/*     */     
/* 281 */     MultipleTestTracker debug4 = new MultipleTestTracker();
/* 282 */     debug3.forEach(debug2 -> runTest(debug0, debug2, debug1));
/*     */     
/* 284 */     return 1;
/*     */   }
/*     */   
/*     */   private static void runTest(ServerLevel debug0, BlockPos debug1, @Nullable MultipleTestTracker debug2) {
/* 288 */     StructureBlockEntity debug3 = (StructureBlockEntity)debug0.getBlockEntity(debug1);
/* 289 */     String debug4 = debug3.getStructurePath();
/* 290 */     TestFunction debug5 = GameTestRegistry.getTestFunction(debug4);
/* 291 */     GameTestInfo debug6 = new GameTestInfo(debug5, debug3.getRotation(), debug0);
/* 292 */     if (debug2 != null) {
/* 293 */       debug2.addTestToTrack(debug6);
/* 294 */       debug6.addListener(new TestSummaryDisplayer(debug0, debug2));
/*     */     } 
/* 296 */     runTestPreparation(debug5, debug0);
/* 297 */     AABB debug7 = StructureUtils.getStructureBounds(debug3);
/* 298 */     BlockPos debug8 = new BlockPos(debug7.minX, debug7.minY, debug7.minZ);
/* 299 */     GameTestRunner.runTest(debug6, debug8, GameTestTicker.singleton);
/*     */   }
/*     */   
/*     */   private static void showTestSummaryIfAllDone(ServerLevel debug0, MultipleTestTracker debug1) {
/* 303 */     if (debug1.isDone()) {
/* 304 */       say(debug0, "GameTest done! " + debug1.getTotalCount() + " tests were run", ChatFormatting.WHITE);
/* 305 */       if (debug1.hasFailedRequired()) {
/* 306 */         say(debug0, "" + debug1.getFailedRequiredCount() + " required tests failed :(", ChatFormatting.RED);
/*     */       } else {
/* 308 */         say(debug0, "All required tests passed :)", ChatFormatting.GREEN);
/*     */       } 
/* 310 */       if (debug1.hasFailedOptional()) {
/* 311 */         say(debug0, "" + debug1.getFailedOptionalCount() + " optional tests failed", ChatFormatting.GRAY);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int clearAllTests(CommandSourceStack debug0, int debug1) {
/* 317 */     ServerLevel debug2 = debug0.getLevel();
/* 318 */     GameTestRunner.clearMarkers(debug2);
/* 319 */     BlockPos debug3 = new BlockPos((debug0.getPosition()).x, debug0.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(debug0.getPosition())).getY(), (debug0.getPosition()).z);
/* 320 */     GameTestRunner.clearAllTests(debug2, debug3, GameTestTicker.singleton, Mth.clamp(debug1, 0, 1024));
/* 321 */     return 1;
/*     */   }
/*     */   
/*     */   private static int runTest(CommandSourceStack debug0, TestFunction debug1, int debug2) {
/* 325 */     ServerLevel debug3 = debug0.getLevel();
/* 326 */     BlockPos debug4 = new BlockPos(debug0.getPosition());
/* 327 */     int debug5 = debug0.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, debug4).getY();
/* 328 */     BlockPos debug6 = new BlockPos(debug4.getX(), debug5, debug4.getZ() + 3);
/* 329 */     GameTestRunner.clearMarkers(debug3);
/* 330 */     runTestPreparation(debug1, debug3);
/* 331 */     Rotation debug7 = StructureUtils.getRotationForRotationSteps(debug2);
/* 332 */     GameTestInfo debug8 = new GameTestInfo(debug1, debug7, debug3);
/* 333 */     GameTestRunner.runTest(debug8, debug6, GameTestTicker.singleton);
/* 334 */     return 1;
/*     */   }
/*     */   
/*     */   private static void runTestPreparation(TestFunction debug0, ServerLevel debug1) {
/* 338 */     Consumer<ServerLevel> debug2 = GameTestRegistry.getBeforeBatchFunction(debug0.getBatchName());
/* 339 */     if (debug2 != null) {
/* 340 */       debug2.accept(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   private static int runAllTests(CommandSourceStack debug0, int debug1, int debug2) {
/* 345 */     GameTestRunner.clearMarkers(debug0.getLevel());
/* 346 */     Collection<TestFunction> debug3 = GameTestRegistry.getAllTestFunctions();
/* 347 */     say(debug0, "Running all " + debug3.size() + " tests...");
/* 348 */     GameTestRegistry.forgetFailedTests();
/* 349 */     runTests(debug0, debug3, debug1, debug2);
/* 350 */     return 1;
/*     */   }
/*     */   
/*     */   private static int runAllTestsInClass(CommandSourceStack debug0, String debug1, int debug2, int debug3) {
/* 354 */     Collection<TestFunction> debug4 = GameTestRegistry.getTestFunctionsForClassName(debug1);
/* 355 */     GameTestRunner.clearMarkers(debug0.getLevel());
/* 356 */     say(debug0, "Running " + debug4.size() + " tests from " + debug1 + "...");
/* 357 */     GameTestRegistry.forgetFailedTests();
/* 358 */     runTests(debug0, debug4, debug2, debug3);
/* 359 */     return 1;
/*     */   }
/*     */   
/*     */   private static int runLastFailedTests(CommandSourceStack debug0, boolean debug1, int debug2, int debug3) {
/*     */     Collection<TestFunction> debug4;
/* 364 */     if (debug1) {
/* 365 */       debug4 = (Collection<TestFunction>)GameTestRegistry.getLastFailedTests().stream().filter(TestFunction::isRequired).collect(Collectors.toList());
/*     */     } else {
/* 367 */       debug4 = GameTestRegistry.getLastFailedTests();
/*     */     } 
/* 369 */     if (debug4.isEmpty()) {
/* 370 */       say(debug0, "No failed tests to rerun");
/* 371 */       return 0;
/*     */     } 
/* 373 */     GameTestRunner.clearMarkers(debug0.getLevel());
/* 374 */     say(debug0, "Rerunning " + debug4.size() + " failed tests (" + (debug1 ? "only required tests" : "including optional tests") + ")");
/* 375 */     runTests(debug0, debug4, debug2, debug3);
/* 376 */     return 1;
/*     */   }
/*     */   
/*     */   private static void runTests(CommandSourceStack debug0, Collection<TestFunction> debug1, int debug2, int debug3) {
/* 380 */     BlockPos debug4 = new BlockPos(debug0.getPosition());
/* 381 */     BlockPos debug5 = new BlockPos(debug4.getX(), debug0.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, debug4).getY(), debug4.getZ() + 3);
/* 382 */     ServerLevel debug6 = debug0.getLevel();
/* 383 */     Rotation debug7 = StructureUtils.getRotationForRotationSteps(debug2);
/* 384 */     Collection<GameTestInfo> debug8 = GameTestRunner.runTests(debug1, debug5, debug7, debug6, GameTestTicker.singleton, debug3);
/* 385 */     MultipleTestTracker debug9 = new MultipleTestTracker(debug8);
/* 386 */     debug9.addListener(new TestSummaryDisplayer(debug6, debug9));
/* 387 */     debug9.addFailureListener(debug0 -> GameTestRegistry.rememberFailedTest(debug0.getTestFunction()));
/*     */   }
/*     */   
/*     */   private static void say(CommandSourceStack debug0, String debug1) {
/* 391 */     debug0.sendSuccess((Component)new TextComponent(debug1), false);
/*     */   }
/*     */   
/*     */   private static int exportNearestTestStructure(CommandSourceStack debug0) {
/* 395 */     BlockPos debug1 = new BlockPos(debug0.getPosition());
/* 396 */     ServerLevel debug2 = debug0.getLevel();
/* 397 */     BlockPos debug3 = StructureUtils.findNearestStructureBlock(debug1, 15, debug2);
/* 398 */     if (debug3 == null) {
/* 399 */       say(debug2, "Couldn't find any structure block within 15 radius", ChatFormatting.RED);
/* 400 */       return 0;
/*     */     } 
/* 402 */     StructureBlockEntity debug4 = (StructureBlockEntity)debug2.getBlockEntity(debug3);
/* 403 */     String debug5 = debug4.getStructurePath();
/* 404 */     return exportTestStructure(debug0, debug5);
/*     */   }
/*     */   
/*     */   private static int exportTestStructure(CommandSourceStack debug0, String debug1) {
/* 408 */     Path debug2 = Paths.get(StructureUtils.testStructuresDir, new String[0]);
/*     */     
/* 410 */     ResourceLocation debug3 = new ResourceLocation("minecraft", debug1);
/* 411 */     Path debug4 = debug0.getLevel().getStructureManager().createPathToStructure(debug3, ".nbt");
/* 412 */     Path debug5 = NbtToSnbt.convertStructure(debug4, debug1, debug2);
/* 413 */     if (debug5 == null) {
/* 414 */       say(debug0, "Failed to export " + debug4);
/* 415 */       return 1;
/*     */     } 
/*     */     
/*     */     try {
/* 419 */       Files.createDirectories(debug5.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 420 */     } catch (IOException debug6) {
/* 421 */       say(debug0, "Could not create folder " + debug5.getParent());
/* 422 */       debug6.printStackTrace();
/* 423 */       return 1;
/*     */     } 
/*     */     
/* 426 */     say(debug0, "Exported " + debug1 + " to " + debug5.toAbsolutePath());
/* 427 */     return 0;
/*     */   }
/*     */   
/*     */   private static int importTestStructure(CommandSourceStack debug0, String debug1) {
/* 431 */     Path debug2 = Paths.get(StructureUtils.testStructuresDir, new String[] { debug1 + ".snbt" });
/*     */     
/* 433 */     ResourceLocation debug3 = new ResourceLocation("minecraft", debug1);
/* 434 */     Path debug4 = debug0.getLevel().getStructureManager().createPathToStructure(debug3, ".nbt");
/*     */     
/*     */     try {
/* 437 */       BufferedReader debug5 = Files.newBufferedReader(debug2);
/* 438 */       String debug6 = IOUtils.toString(debug5);
/* 439 */       Files.createDirectories(debug4.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 440 */       try (OutputStream debug7 = Files.newOutputStream(debug4, new java.nio.file.OpenOption[0])) {
/* 441 */         NbtIo.writeCompressed(TagParser.parseTag(debug6), debug7);
/*     */       } 
/* 443 */       say(debug0, "Imported to " + debug4.toAbsolutePath());
/* 444 */       return 0;
/* 445 */     } catch (IOException|CommandSyntaxException debug5) {
/* 446 */       System.err.println("Failed to load structure " + debug1);
/* 447 */       debug5.printStackTrace();
/* 448 */       return 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void say(ServerLevel debug0, String debug1, ChatFormatting debug2) {
/* 453 */     debug0.getPlayers(debug0 -> true).forEach(debug2 -> debug2.sendMessage((Component)new TextComponent(debug0 + debug1), Util.NIL_UUID));
/*     */   }
/*     */   
/*     */   static class TestSummaryDisplayer
/*     */     implements GameTestListener
/*     */   {
/*     */     private final ServerLevel level;
/*     */     private final MultipleTestTracker tracker;
/*     */     
/*     */     public TestSummaryDisplayer(ServerLevel debug1, MultipleTestTracker debug2) {
/* 463 */       this.level = debug1;
/* 464 */       this.tracker = debug2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void testStructureLoaded(GameTestInfo debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void testFailed(GameTestInfo debug1) {
/* 478 */       TestCommand.showTestSummaryIfAllDone(this.level, this.tracker);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\TestCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */