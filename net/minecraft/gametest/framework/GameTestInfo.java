/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
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
/*     */ public class GameTestInfo
/*     */ {
/*     */   private final TestFunction testFunction;
/*     */   @Nullable
/*     */   private BlockPos structureBlockPos;
/*     */   private final ServerLevel level;
/*  30 */   private final Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */   
/*     */   private final int timeoutTicks;
/*  33 */   private final Collection<GameTestSequence> sequences = Lists.newCopyOnWriteArrayList();
/*  34 */   private Object2LongMap<Runnable> runAtTickTimeMap = (Object2LongMap<Runnable>)new Object2LongOpenHashMap();
/*     */   
/*     */   private long startTick;
/*     */   private long tickCount;
/*     */   private boolean started = false;
/*  39 */   private final Stopwatch timer = Stopwatch.createUnstarted();
/*     */   
/*     */   private boolean done = false;
/*     */   
/*     */   private final Rotation rotation;
/*     */   @Nullable
/*     */   private Throwable error;
/*     */   
/*     */   public GameTestInfo(TestFunction debug1, Rotation debug2, ServerLevel debug3) {
/*  48 */     this.testFunction = debug1;
/*  49 */     this.level = debug3;
/*  50 */     this.timeoutTicks = debug1.getMaxTicks();
/*  51 */     this.rotation = debug1.getRotation().getRotated(debug2);
/*     */   }
/*     */   
/*     */   void setStructureBlockPos(BlockPos debug1) {
/*  55 */     this.structureBlockPos = debug1;
/*     */   }
/*     */   
/*     */   void startExecution() {
/*  59 */     this.startTick = this.level.getGameTime() + 1L + this.testFunction.getSetupTicks();
/*  60 */     this.timer.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  67 */     if (isDone()) {
/*     */       return;
/*     */     }
/*  70 */     this.tickCount = this.level.getGameTime() - this.startTick;
/*  71 */     if (this.tickCount < 0L) {
/*     */       return;
/*     */     }
/*  74 */     if (this.tickCount == 0L) {
/*  75 */       startTest();
/*     */     }
/*  77 */     ObjectIterator<Object2LongMap.Entry<Runnable>> debug1 = this.runAtTickTimeMap.object2LongEntrySet().iterator();
/*  78 */     while (debug1.hasNext()) {
/*  79 */       Object2LongMap.Entry<Runnable> debug2 = (Object2LongMap.Entry<Runnable>)debug1.next();
/*  80 */       if (debug2.getLongValue() <= this.tickCount) {
/*     */         try {
/*  82 */           ((Runnable)debug2.getKey()).run();
/*  83 */         } catch (Exception debug3) {
/*  84 */           fail(debug3);
/*     */         } 
/*  86 */         debug1.remove();
/*     */       } 
/*     */     } 
/*  89 */     if (this.tickCount > this.timeoutTicks) {
/*     */       
/*  91 */       if (this.sequences.isEmpty()) {
/*  92 */         fail(new GameTestTimeoutException("Didn't succeed or fail within " + this.testFunction.getMaxTicks() + " ticks"));
/*     */       } else {
/*  94 */         this.sequences.forEach(debug1 -> debug1.tickAndFailIfNotComplete(this.tickCount));
/*  95 */         if (this.error == null) {
/*  96 */           fail(new GameTestTimeoutException("No sequences finished"));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 101 */       this.sequences.forEach(debug1 -> debug1.tickAndContinue(this.tickCount));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startTest() {
/* 106 */     if (this.started) {
/* 107 */       throw new IllegalStateException("Test already started");
/*     */     }
/* 109 */     this.started = true;
/*     */     try {
/* 111 */       this.testFunction.run(new GameTestHelper(this));
/* 112 */     } catch (Exception debug1) {
/* 113 */       fail(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTestName() {
/* 122 */     return this.testFunction.getTestName();
/*     */   }
/*     */   
/*     */   public BlockPos getStructureBlockPos() {
/* 126 */     return this.structureBlockPos;
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
/*     */   public ServerLevel getLevel() {
/* 154 */     return this.level;
/*     */   }
/*     */   
/*     */   public boolean hasSucceeded() {
/* 158 */     return (this.done && this.error == null);
/*     */   }
/*     */   
/*     */   public boolean hasFailed() {
/* 162 */     return (this.error != null);
/*     */   }
/*     */   
/*     */   public boolean hasStarted() {
/* 166 */     return this.started;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 170 */     return this.done;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void finish() {
/* 178 */     if (!this.done) {
/* 179 */       this.done = true;
/* 180 */       this.timer.stop();
/*     */     } 
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
/*     */   public void fail(Throwable debug1) {
/* 193 */     finish();
/* 194 */     this.error = debug1;
/* 195 */     this.listeners.forEach(debug1 -> debug1.testFailed(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getError() {
/* 203 */     return this.error;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 208 */     return getTestName();
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener debug1) {
/* 212 */     this.listeners.add(debug1);
/*     */   }
/*     */   
/*     */   public void spawnStructure(BlockPos debug1, int debug2) {
/* 216 */     StructureBlockEntity debug3 = StructureUtils.spawnStructure(getStructureName(), debug1, getRotation(), debug2, this.level, false);
/* 217 */     setStructureBlockPos(debug3.getBlockPos());
/*     */     
/* 219 */     debug3.setStructureName(getTestName());
/* 220 */     StructureUtils.addCommandBlockAndButtonToStartTest(this.structureBlockPos, new BlockPos(1, 0, -1), getRotation(), this.level);
/*     */     
/* 222 */     this.listeners.forEach(debug1 -> debug1.testStructureLoaded(this));
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
/*     */   
/*     */   public boolean isRequired() {
/* 236 */     return this.testFunction.isRequired();
/*     */   }
/*     */   
/*     */   public boolean isOptional() {
/* 240 */     return !this.testFunction.isRequired();
/*     */   }
/*     */   
/*     */   public String getStructureName() {
/* 244 */     return this.testFunction.getStructureName();
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 248 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public TestFunction getTestFunction() {
/* 252 */     return this.testFunction;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */