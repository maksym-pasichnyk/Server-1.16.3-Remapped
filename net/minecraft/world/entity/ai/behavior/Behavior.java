/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Behavior<E extends LivingEntity>
/*     */ {
/*     */   protected final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*  18 */   private Status status = Status.STOPPED;
/*     */   private long endTimestamp;
/*     */   private final int minDuration;
/*     */   private final int maxDuration;
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> debug1) {
/*  24 */     this(debug1, 60);
/*     */   }
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> debug1, int debug2) {
/*  28 */     this(debug1, debug2, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> debug1, int debug2, int debug3) {
/*  35 */     this.minDuration = debug2;
/*  36 */     this.maxDuration = debug3;
/*  37 */     this.entryCondition = debug1;
/*     */   }
/*     */   
/*     */   public Status getStatus() {
/*  41 */     return this.status;
/*     */   }
/*     */   
/*     */   public final boolean tryStart(ServerLevel debug1, E debug2, long debug3) {
/*  45 */     if (hasRequiredMemories(debug2) && checkExtraStartConditions(debug1, debug2)) {
/*  46 */       this.status = Status.RUNNING;
/*  47 */       int debug5 = this.minDuration + debug1.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/*  48 */       this.endTimestamp = debug3 + debug5;
/*  49 */       start(debug1, debug2, debug3);
/*  50 */       return true;
/*     */     } 
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, E debug2, long debug3) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tickOrStop(ServerLevel debug1, E debug2, long debug3) {
/*  62 */     if (!timedOut(debug3) && canStillUse(debug1, debug2, debug3)) {
/*  63 */       tick(debug1, debug2, debug3);
/*     */     } else {
/*  65 */       doStop(debug1, debug2, debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, E debug2, long debug3) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final void doStop(ServerLevel debug1, E debug2, long debug3) {
/*  76 */     this.status = Status.STOPPED;
/*  77 */     stop(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, E debug2, long debug3) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, E debug2, long debug3) {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean timedOut(long debug1) {
/* 102 */     return (debug1 > this.endTimestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   private boolean hasRequiredMemories(E debug1) {
/* 119 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> debug3 : this.entryCondition.entrySet()) {
/* 120 */       MemoryModuleType<?> debug4 = debug3.getKey();
/* 121 */       MemoryStatus debug5 = debug3.getValue();
/* 122 */       if (!debug1.getBrain().checkMemory(debug4, debug5)) {
/* 123 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   public enum Status {
/* 131 */     STOPPED,
/* 132 */     RUNNING;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Behavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */