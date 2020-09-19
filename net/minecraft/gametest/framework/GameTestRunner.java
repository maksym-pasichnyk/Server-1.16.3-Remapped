/*     */ package net.minecraft.gametest.framework;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Streams;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LecternBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class GameTestRunner {
/*  40 */   public static TestReporter TEST_REPORTER = new LogTestReporter();
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
/*     */   public static void runTest(GameTestInfo debug0, BlockPos debug1, GameTestTicker debug2) {
/*  52 */     debug0.startExecution();
/*  53 */     debug2.add(debug0);
/*     */     
/*  55 */     debug0.addListener(new GameTestListener()
/*     */         {
/*     */           public void testStructureLoaded(GameTestInfo debug1) {
/*  58 */             GameTestRunner.spawnBeacon(debug1, Blocks.LIGHT_GRAY_STAINED_GLASS);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void testFailed(GameTestInfo debug1) {
/*  69 */             GameTestRunner.spawnBeacon(debug1, debug1.isRequired() ? Blocks.RED_STAINED_GLASS : Blocks.ORANGE_STAINED_GLASS);
/*  70 */             GameTestRunner.spawnLectern(debug1, Util.describeError(debug1.getError()));
/*  71 */             GameTestRunner.visualizeFailedTest(debug1);
/*     */           }
/*     */         });
/*  74 */     debug0.spawnStructure(debug1, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<GameTestInfo> runTestBatches(Collection<GameTestBatch> debug0, BlockPos debug1, Rotation debug2, ServerLevel debug3, GameTestTicker debug4, int debug5) {
/*  83 */     GameTestBatchRunner debug6 = new GameTestBatchRunner(debug0, debug1, debug2, debug3, debug4, debug5);
/*  84 */     debug6.start();
/*  85 */     return debug6.getTestInfos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<GameTestInfo> runTests(Collection<TestFunction> debug0, BlockPos debug1, Rotation debug2, ServerLevel debug3, GameTestTicker debug4, int debug5) {
/*  92 */     return runTestBatches(groupTestsIntoBatches(debug0), debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public static Collection<GameTestBatch> groupTestsIntoBatches(Collection<TestFunction> debug0) {
/*  96 */     Map<String, Collection<TestFunction>> debug1 = Maps.newHashMap();
/*     */ 
/*     */     
/*  99 */     debug0.forEach(debug1 -> {
/*     */           String debug2 = debug1.getBatchName();
/*     */           
/*     */           Collection<TestFunction> debug3 = debug0.computeIfAbsent(debug2, ());
/*     */           
/*     */           debug3.add(debug1);
/*     */         });
/* 106 */     return (Collection<GameTestBatch>)debug1.keySet().stream().flatMap(debug1 -> {
/*     */           Collection<TestFunction> debug2 = (Collection<TestFunction>)debug0.get(debug1);
/*     */           
/*     */           Consumer<ServerLevel> debug3 = GameTestRegistry.getBeforeBatchFunction(debug1);
/*     */           MutableInt debug4 = new MutableInt();
/*     */           return Streams.stream(Iterables.partition(debug2, 100)).map(());
/* 112 */         }).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   private static void visualizeFailedTest(GameTestInfo debug0) {
/* 116 */     Throwable debug1 = debug0.getError();
/* 117 */     String debug2 = (debug0.isRequired() ? "" : "(optional) ") + debug0.getTestName() + " failed! " + Util.describeError(debug1);
/*     */     
/* 119 */     say(debug0.getLevel(), debug0.isRequired() ? ChatFormatting.RED : ChatFormatting.YELLOW, debug2);
/*     */     
/* 121 */     if (debug1 instanceof GameTestAssertPosException) {
/* 122 */       GameTestAssertPosException debug3 = (GameTestAssertPosException)debug1;
/* 123 */       showRedBox(debug0.getLevel(), debug3.getAbsolutePos(), debug3.getMessageToShowAtBlock());
/*     */     } 
/*     */     
/* 126 */     TEST_REPORTER.onTestFailed(debug0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void spawnBeacon(GameTestInfo debug0, Block debug1) {
/* 136 */     ServerLevel debug2 = debug0.getLevel();
/* 137 */     BlockPos debug3 = debug0.getStructureBlockPos();
/* 138 */     BlockPos debug4 = new BlockPos(-1, -1, -1);
/* 139 */     BlockPos debug5 = StructureTemplate.transform(debug3.offset((Vec3i)debug4), Mirror.NONE, debug0.getRotation(), debug3);
/* 140 */     debug2.setBlockAndUpdate(debug5, Blocks.BEACON.defaultBlockState().rotate(debug0.getRotation()));
/*     */     
/* 142 */     BlockPos debug6 = debug5.offset(0, 1, 0);
/* 143 */     debug2.setBlockAndUpdate(debug6, debug1.defaultBlockState());
/*     */     
/* 145 */     for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 146 */       for (int debug8 = -1; debug8 <= 1; debug8++) {
/* 147 */         BlockPos debug9 = debug5.offset(debug7, -1, debug8);
/* 148 */         debug2.setBlockAndUpdate(debug9, Blocks.IRON_BLOCK.defaultBlockState());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void spawnLectern(GameTestInfo debug0, String debug1) {
/* 154 */     ServerLevel debug2 = debug0.getLevel();
/* 155 */     BlockPos debug3 = debug0.getStructureBlockPos();
/* 156 */     BlockPos debug4 = new BlockPos(-1, 1, -1);
/* 157 */     BlockPos debug5 = StructureTemplate.transform(debug3.offset((Vec3i)debug4), Mirror.NONE, debug0.getRotation(), debug3);
/*     */     
/* 159 */     debug2.setBlockAndUpdate(debug5, Blocks.LECTERN.defaultBlockState().rotate(debug0.getRotation()));
/*     */     
/* 161 */     BlockState debug6 = debug2.getBlockState(debug5);
/*     */     
/* 163 */     ItemStack debug7 = createBook(debug0.getTestName(), debug0.isRequired(), debug1);
/*     */     
/* 165 */     LecternBlock.tryPlaceBook((Level)debug2, debug5, debug6, debug7);
/*     */   }
/*     */   
/*     */   private static ItemStack createBook(String debug0, boolean debug1, String debug2) {
/* 169 */     ItemStack debug3 = new ItemStack((ItemLike)Items.WRITABLE_BOOK);
/* 170 */     ListTag debug4 = new ListTag();
/*     */     
/* 172 */     StringBuffer debug5 = new StringBuffer();
/* 173 */     Arrays.<String>stream(debug0.split("\\.")).forEach(debug1 -> debug0.append(debug1).append('\n'));
/*     */ 
/*     */     
/* 176 */     if (!debug1) {
/* 177 */       debug5.append("(optional)\n");
/*     */     }
/*     */     
/* 180 */     debug5.append("-------------------\n");
/*     */     
/* 182 */     debug4.add(StringTag.valueOf(debug5.toString() + debug2));
/* 183 */     debug3.addTagElement("pages", (Tag)debug4);
/* 184 */     return debug3;
/*     */   }
/*     */   
/*     */   private static void say(ServerLevel debug0, ChatFormatting debug1, String debug2) {
/* 188 */     debug0.getPlayers(debug0 -> true).forEach(debug2 -> debug2.sendMessage((Component)(new TextComponent(debug0)).withStyle(debug1), Util.NIL_UUID));
/*     */   }
/*     */   
/*     */   public static void clearMarkers(ServerLevel debug0) {
/* 192 */     DebugPackets.sendGameTestClearPacket(debug0);
/*     */   }
/*     */   
/*     */   private static void showRedBox(ServerLevel debug0, BlockPos debug1, String debug2) {
/* 196 */     DebugPackets.sendGameTestAddMarker(debug0, debug1, debug2, -2130771968, 2147483647);
/*     */   }
/*     */   
/*     */   public static void clearAllTests(ServerLevel debug0, BlockPos debug1, GameTestTicker debug2, int debug3) {
/* 200 */     debug2.clear();
/* 201 */     BlockPos debug4 = debug1.offset(-debug3, 0, -debug3);
/* 202 */     BlockPos debug5 = debug1.offset(debug3, 0, debug3);
/* 203 */     BlockPos.betweenClosedStream(debug4, debug5)
/* 204 */       .filter(debug1 -> debug0.getBlockState(debug1).is(Blocks.STRUCTURE_BLOCK))
/* 205 */       .forEach(debug1 -> {
/*     */           StructureBlockEntity debug2 = (StructureBlockEntity)debug0.getBlockEntity(debug1);
/*     */           BlockPos debug3 = debug2.getBlockPos();
/*     */           BoundingBox debug4 = StructureUtils.getStructureBoundingBox(debug2);
/*     */           StructureUtils.clearSpaceForStructure(debug4, debug3.getY(), debug0);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */