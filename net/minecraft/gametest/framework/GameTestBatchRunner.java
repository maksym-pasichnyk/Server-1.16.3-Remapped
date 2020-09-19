/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GameTestBatchRunner {
/*  19 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final BlockPos firstTestNorthWestCorner;
/*     */   private final ServerLevel level;
/*     */   private final GameTestTicker testTicker;
/*     */   private final int testsPerRow;
/*  25 */   private final List<GameTestInfo> allTestInfos = Lists.newArrayList();
/*  26 */   private final Map<GameTestInfo, BlockPos> northWestCorners = Maps.newHashMap();
/*  27 */   private final List<Pair<GameTestBatch, Collection<GameTestInfo>>> batches = Lists.newArrayList();
/*     */   
/*     */   private MultipleTestTracker currentBatchTracker;
/*  30 */   private int currentBatchIndex = 0;
/*     */   private BlockPos.MutableBlockPos nextTestNorthWestCorner;
/*     */   
/*     */   public GameTestBatchRunner(Collection<GameTestBatch> debug1, BlockPos debug2, Rotation debug3, ServerLevel debug4, GameTestTicker debug5, int debug6) {
/*  34 */     this.nextTestNorthWestCorner = debug2.mutable();
/*  35 */     this.firstTestNorthWestCorner = debug2;
/*  36 */     this.level = debug4;
/*  37 */     this.testTicker = debug5;
/*  38 */     this.testsPerRow = debug6;
/*     */     
/*  40 */     debug1.forEach(debug3 -> {
/*     */           Collection<GameTestInfo> debug4 = Lists.newArrayList();
/*     */           Collection<TestFunction> debug5 = debug3.getTestFunctions();
/*     */           for (TestFunction debug7 : debug5) {
/*     */             GameTestInfo debug8 = new GameTestInfo(debug7, debug1, debug2);
/*     */             debug4.add(debug8);
/*     */             this.allTestInfos.add(debug8);
/*     */           } 
/*     */           this.batches.add(Pair.of(debug3, debug4));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<GameTestInfo> getTestInfos() {
/*  56 */     return this.allTestInfos;
/*     */   }
/*     */   
/*     */   public void start() {
/*  60 */     runBatch(0);
/*     */   }
/*     */   
/*     */   private void runBatch(int debug1) {
/*  64 */     this.currentBatchIndex = debug1;
/*  65 */     this.currentBatchTracker = new MultipleTestTracker();
/*     */     
/*  67 */     if (debug1 >= this.batches.size()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  72 */     Pair<GameTestBatch, Collection<GameTestInfo>> debug2 = this.batches.get(this.currentBatchIndex);
/*  73 */     GameTestBatch debug3 = (GameTestBatch)debug2.getFirst();
/*  74 */     Collection<GameTestInfo> debug4 = (Collection<GameTestInfo>)debug2.getSecond();
/*  75 */     createStructuresForBatch(debug4);
/*  76 */     debug3.runBeforeBatchFunction(this.level);
/*     */     
/*  78 */     String debug5 = debug3.getName();
/*     */     
/*  80 */     LOGGER.info("Running test batch '" + debug5 + "' (" + debug4.size() + " tests)...");
/*  81 */     debug4.forEach(debug1 -> {
/*     */           this.currentBatchTracker.addTestToTrack(debug1);
/*     */           this.currentBatchTracker.addListener(new GameTestListener()
/*     */               {
/*     */                 public void testStructureLoaded(GameTestInfo debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void testFailed(GameTestInfo debug1) {
/*  95 */                   GameTestBatchRunner.this.testCompleted(debug1);
/*     */                 }
/*     */               });
/*     */           BlockPos debug2 = this.northWestCorners.get(debug1);
/*     */           GameTestRunner.runTest(debug1, debug2, this.testTicker);
/*     */         });
/*     */   }
/*     */   
/*     */   private void testCompleted(GameTestInfo debug1) {
/* 104 */     if (this.currentBatchTracker.isDone()) {
/* 105 */       runBatch(this.currentBatchIndex + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void createStructuresForBatch(Collection<GameTestInfo> debug1) {
/* 110 */     int debug2 = 0;
/* 111 */     AABB debug3 = new AABB((BlockPos)this.nextTestNorthWestCorner);
/*     */     
/* 113 */     for (GameTestInfo debug5 : debug1) {
/* 114 */       BlockPos debug6 = new BlockPos((Vec3i)this.nextTestNorthWestCorner);
/* 115 */       StructureBlockEntity debug7 = StructureUtils.spawnStructure(debug5.getStructureName(), debug6, debug5.getRotation(), 2, this.level, true);
/* 116 */       AABB debug8 = StructureUtils.getStructureBounds(debug7);
/* 117 */       debug5.setStructureBlockPos(debug7.getBlockPos());
/* 118 */       this.northWestCorners.put(debug5, new BlockPos((Vec3i)this.nextTestNorthWestCorner));
/* 119 */       debug3 = debug3.minmax(debug8);
/*     */       
/* 121 */       this.nextTestNorthWestCorner.move((int)debug8.getXsize() + 5, 0, 0);
/*     */       
/* 123 */       if (debug2++ % this.testsPerRow == this.testsPerRow - 1) {
/*     */         
/* 125 */         this.nextTestNorthWestCorner.move(0, 0, (int)debug3.getZsize() + 6);
/* 126 */         this.nextTestNorthWestCorner.setX(this.firstTestNorthWestCorner.getX());
/* 127 */         debug3 = new AABB((BlockPos)this.nextTestNorthWestCorner);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestBatchRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */