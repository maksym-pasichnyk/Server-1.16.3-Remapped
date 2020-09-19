/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.pathfinder.Path;
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
/*     */ public class AcquirePoi
/*     */   extends Behavior<PathfinderMob>
/*     */ {
/*     */   private final PoiType poiType;
/*     */   private final MemoryModuleType<GlobalPos> memoryToAcquire;
/*     */   private final boolean onlyIfAdult;
/*     */   private final Optional<Byte> onPoiAcquisitionEvent;
/*     */   private long nextScheduledStart;
/*  42 */   private final Long2ObjectMap<JitteredLinearRetry> batchCache = (Long2ObjectMap<JitteredLinearRetry>)new Long2ObjectOpenHashMap();
/*     */   
/*     */   public AcquirePoi(PoiType debug1, MemoryModuleType<GlobalPos> debug2, MemoryModuleType<GlobalPos> debug3, boolean debug4, Optional<Byte> debug5) {
/*  45 */     super((Map<MemoryModuleType<?>, MemoryStatus>)constructEntryConditionMap(debug2, debug3));
/*  46 */     this.poiType = debug1;
/*  47 */     this.memoryToAcquire = debug3;
/*  48 */     this.onlyIfAdult = debug4;
/*  49 */     this.onPoiAcquisitionEvent = debug5;
/*     */   }
/*     */   
/*     */   public AcquirePoi(PoiType debug1, MemoryModuleType<GlobalPos> debug2, boolean debug3, Optional<Byte> debug4) {
/*  53 */     this(debug1, debug2, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   private static ImmutableMap<MemoryModuleType<?>, MemoryStatus> constructEntryConditionMap(MemoryModuleType<GlobalPos> debug0, MemoryModuleType<GlobalPos> debug1) {
/*  57 */     ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> debug2 = ImmutableMap.builder();
/*  58 */     debug2.put(debug0, MemoryStatus.VALUE_ABSENT);
/*  59 */     if (debug1 != debug0) {
/*  60 */       debug2.put(debug1, MemoryStatus.VALUE_ABSENT);
/*     */     }
/*  62 */     return debug2.build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, PathfinderMob debug2) {
/*  67 */     if (this.onlyIfAdult && debug2.isBaby()) {
/*  68 */       return false;
/*     */     }
/*     */     
/*  71 */     if (this.nextScheduledStart == 0L) {
/*  72 */       this.nextScheduledStart = debug2.level.getGameTime() + debug1.random.nextInt(20);
/*  73 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     return (debug1.getGameTime() >= this.nextScheduledStart);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/*  84 */     this.nextScheduledStart = debug3 + 20L + debug1.getRandom().nextInt(20);
/*     */     
/*  86 */     PoiManager debug5 = debug1.getPoiManager();
/*     */     
/*  88 */     this.batchCache.long2ObjectEntrySet().removeIf(debug2 -> !((JitteredLinearRetry)debug2.getValue()).isStillValid(debug0));
/*     */     
/*  90 */     Predicate<BlockPos> debug6 = debug3 -> {
/*     */         JitteredLinearRetry debug4 = (JitteredLinearRetry)this.batchCache.get(debug3.asLong());
/*     */         
/*     */         if (debug4 == null) {
/*     */           return true;
/*     */         }
/*     */         
/*     */         if (!debug4.shouldRetry(debug1)) {
/*     */           return false;
/*     */         }
/*     */         
/*     */         debug4.markAttempt(debug1);
/*     */         
/*     */         return true;
/*     */       };
/*     */     
/* 106 */     Set<BlockPos> debug7 = (Set<BlockPos>)debug5.findAllClosestFirst(this.poiType.getPredicate(), debug6, debug2.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
/*     */     
/* 108 */     Path debug8 = debug2.getNavigation().createPath(debug7, this.poiType.getValidRange());
/*     */     
/* 110 */     if (debug8 != null && debug8.canReach()) {
/* 111 */       BlockPos debug9 = debug8.getTarget();
/* 112 */       debug5.getType(debug9).ifPresent(debug5 -> {
/*     */             debug1.take(this.poiType.getPredicate(), (), debug2, 1);
/*     */             debug3.getBrain().setMemory(this.memoryToAcquire, GlobalPos.of(debug4.dimension(), debug2));
/*     */             this.onPoiAcquisitionEvent.ifPresent(());
/*     */             this.batchCache.clear();
/*     */             DebugPackets.sendPoiTicketCountPacket(debug4, debug2);
/*     */           });
/*     */     } else {
/* 120 */       for (BlockPos debug10 : debug7) {
/* 121 */         this.batchCache.computeIfAbsent(debug10.asLong(), debug3 -> new JitteredLinearRetry(debug0.level.random, debug1));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class JitteredLinearRetry
/*     */   {
/*     */     private final Random random;
/*     */     
/*     */     private long previousAttemptTimestamp;
/*     */     
/*     */     private long nextScheduledAttemptTimestamp;
/*     */     
/*     */     private int currentDelay;
/*     */     
/*     */     JitteredLinearRetry(Random debug1, long debug2) {
/* 138 */       this.random = debug1;
/* 139 */       markAttempt(debug2);
/*     */     }
/*     */     
/*     */     public void markAttempt(long debug1) {
/* 143 */       this.previousAttemptTimestamp = debug1;
/* 144 */       int debug3 = this.currentDelay + this.random.nextInt(40) + 40;
/* 145 */       this.currentDelay = Math.min(debug3, 400);
/* 146 */       this.nextScheduledAttemptTimestamp = debug1 + this.currentDelay;
/*     */     }
/*     */     
/*     */     public boolean isStillValid(long debug1) {
/* 150 */       return (debug1 - this.previousAttemptTimestamp < 400L);
/*     */     }
/*     */     
/*     */     public boolean shouldRetry(long debug1) {
/* 154 */       return (debug1 >= this.nextScheduledAttemptTimestamp);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + '}';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */