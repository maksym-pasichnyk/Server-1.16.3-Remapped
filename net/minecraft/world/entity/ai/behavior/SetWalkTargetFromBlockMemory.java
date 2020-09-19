/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetWalkTargetFromBlockMemory
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private final MemoryModuleType<GlobalPos> memoryType;
/*     */   private final float speedModifier;
/*     */   private final int closeEnoughDist;
/*     */   private final int tooFarDistance;
/*     */   private final int tooLongUnreachableDuration;
/*     */   
/*     */   public SetWalkTargetFromBlockMemory(MemoryModuleType<GlobalPos> debug1, float debug2, int debug3, int debug4, int debug5) {
/*  34 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, debug1, MemoryStatus.VALUE_PRESENT));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     this.memoryType = debug1;
/*  40 */     this.speedModifier = debug2;
/*  41 */     this.closeEnoughDist = debug3;
/*  42 */     this.tooFarDistance = debug4;
/*  43 */     this.tooLongUnreachableDuration = debug5;
/*     */   }
/*     */   
/*     */   private void dropPOI(Villager debug1, long debug2) {
/*  47 */     Brain<?> debug4 = debug1.getBrain();
/*     */     
/*  49 */     debug1.releasePoi(this.memoryType);
/*  50 */     debug4.eraseMemory(this.memoryType);
/*  51 */     debug4.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, Long.valueOf(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  56 */     Brain<?> debug5 = debug2.getBrain();
/*  57 */     debug5.getMemory(this.memoryType).ifPresent(debug6 -> {
/*     */           if (wrongDimension(debug1, debug6) || tiredOfTryingToFindTarget(debug1, debug2)) {
/*     */             dropPOI(debug2, debug3);
/*     */           } else if (tooFar(debug2, debug6)) {
/*     */             Vec3 debug7 = null;
/*     */             int debug8 = 0;
/*     */             int debug9 = 1000;
/*     */             while (debug8 < 1000 && (debug7 == null || tooFar(debug2, GlobalPos.of(debug1.dimension(), new BlockPos(debug7))))) {
/*     */               debug7 = RandomPos.getPosTowards((PathfinderMob)debug2, 15, 7, Vec3.atBottomCenterOf((Vec3i)debug6.pos()));
/*     */               debug8++;
/*     */             } 
/*     */             if (debug8 == 1000) {
/*     */               dropPOI(debug2, debug3);
/*     */               return;
/*     */             } 
/*     */             debug5.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug7, this.speedModifier, this.closeEnoughDist));
/*     */           } else if (!closeEnough(debug1, debug2, debug6)) {
/*     */             debug5.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug6.pos(), this.speedModifier, this.closeEnoughDist));
/*     */           } 
/*     */         });
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
/*     */   private boolean tiredOfTryingToFindTarget(ServerLevel debug1, Villager debug2) {
/*  88 */     Optional<Long> debug3 = debug2.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*  89 */     if (debug3.isPresent()) {
/*  90 */       return (debug1.getGameTime() - ((Long)debug3.get()).longValue() > this.tooLongUnreachableDuration);
/*     */     }
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   private boolean tooFar(Villager debug1, GlobalPos debug2) {
/*  96 */     return (debug2.pos().distManhattan((Vec3i)debug1.blockPosition()) > this.tooFarDistance);
/*     */   }
/*     */   
/*     */   private boolean wrongDimension(ServerLevel debug1, GlobalPos debug2) {
/* 100 */     return (debug2.dimension() != debug1.dimension());
/*     */   }
/*     */   
/*     */   private boolean closeEnough(ServerLevel debug1, Villager debug2, GlobalPos debug3) {
/* 104 */     return (debug3.dimension() == debug1.dimension() && debug3
/* 105 */       .pos().distManhattan((Vec3i)debug2.blockPosition()) <= this.closeEnoughDist);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromBlockMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */