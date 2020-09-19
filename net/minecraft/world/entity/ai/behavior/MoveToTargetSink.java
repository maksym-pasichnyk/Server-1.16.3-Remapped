/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MoveToTargetSink
/*     */   extends Behavior<Mob>
/*     */ {
/*     */   private int remainingCooldown;
/*     */   @Nullable
/*     */   private Path path;
/*     */   @Nullable
/*     */   private BlockPos lastTargetPos;
/*     */   private float speedModifier;
/*     */   
/*     */   public MoveToTargetSink() {
/*  39 */     this(150, 250);
/*     */   }
/*     */   
/*     */   public MoveToTargetSink(int debug1, int debug2) {
/*  43 */     super(
/*  44 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT), debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Mob debug2) {
/*  55 */     if (this.remainingCooldown > 0) {
/*  56 */       this.remainingCooldown--;
/*  57 */       return false;
/*     */     } 
/*     */     
/*  60 */     Brain<?> debug3 = debug2.getBrain();
/*  61 */     WalkTarget debug4 = debug3.getMemory(MemoryModuleType.WALK_TARGET).get();
/*     */     
/*  63 */     boolean debug5 = reachedTarget(debug2, debug4);
/*  64 */     if (!debug5 && tryComputePath(debug2, debug4, debug1.getGameTime())) {
/*  65 */       this.lastTargetPos = debug4.getTarget().currentBlockPosition();
/*  66 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  71 */     debug3.eraseMemory(MemoryModuleType.WALK_TARGET);
/*  72 */     if (debug5) {
/*  73 */       debug3.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*     */     }
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Mob debug2, long debug3) {
/*  80 */     if (this.path == null || this.lastTargetPos == null) {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     Optional<WalkTarget> debug5 = debug2.getBrain().getMemory(MemoryModuleType.WALK_TARGET);
/*  85 */     PathNavigation debug6 = debug2.getNavigation();
/*  86 */     return (!debug6.isDone() && debug5.isPresent() && !reachedTarget(debug2, debug5.get()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Mob debug2, long debug3) {
/*  91 */     if (debug2.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET) && !reachedTarget(debug2, debug2.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get()) && debug2.getNavigation().isStuck())
/*     */     {
/*  93 */       this.remainingCooldown = debug1.getRandom().nextInt(40);
/*     */     }
/*     */     
/*  96 */     debug2.getNavigation().stop();
/*  97 */     debug2.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  98 */     debug2.getBrain().eraseMemory(MemoryModuleType.PATH);
/*  99 */     this.path = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Mob debug2, long debug3) {
/* 104 */     debug2.getBrain().setMemory(MemoryModuleType.PATH, this.path);
/* 105 */     debug2.getNavigation().moveTo(this.path, this.speedModifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Mob debug2, long debug3) {
/* 111 */     Path debug5 = debug2.getNavigation().getPath();
/* 112 */     Brain<?> debug6 = debug2.getBrain();
/* 113 */     if (this.path != debug5) {
/* 114 */       this.path = debug5;
/* 115 */       debug6.setMemory(MemoryModuleType.PATH, debug5);
/*     */     } 
/*     */     
/* 118 */     if (debug5 == null || this.lastTargetPos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     WalkTarget debug7 = debug6.getMemory(MemoryModuleType.WALK_TARGET).get();
/*     */     
/* 124 */     if (debug7.getTarget().currentBlockPosition().distSqr((Vec3i)this.lastTargetPos) > 4.0D && 
/* 125 */       tryComputePath(debug2, debug7, debug1.getGameTime())) {
/* 126 */       this.lastTargetPos = debug7.getTarget().currentBlockPosition();
/* 127 */       start(debug1, debug2, debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean tryComputePath(Mob debug1, WalkTarget debug2, long debug3) {
/* 134 */     BlockPos debug5 = debug2.getTarget().currentBlockPosition();
/* 135 */     this.path = debug1.getNavigation().createPath(debug5, 0);
/* 136 */     this.speedModifier = debug2.getSpeedModifier();
/*     */     
/* 138 */     Brain<?> debug6 = debug1.getBrain();
/* 139 */     if (reachedTarget(debug1, debug2)) {
/* 140 */       debug6.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*     */     } else {
/* 142 */       boolean debug7 = (this.path != null && this.path.canReach());
/* 143 */       if (debug7) {
/* 144 */         debug6.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 145 */       } else if (!debug6.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
/* 146 */         debug6.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, Long.valueOf(debug3));
/*     */       } 
/*     */       
/* 149 */       if (this.path != null) {
/* 150 */         return true;
/*     */       }
/*     */       
/* 153 */       Vec3 debug8 = RandomPos.getPosTowards((PathfinderMob)debug1, 10, 7, Vec3.atBottomCenterOf((Vec3i)debug5));
/* 154 */       if (debug8 != null) {
/* 155 */         this.path = debug1.getNavigation().createPath(debug8.x, debug8.y, debug8.z, 0);
/* 156 */         return (this.path != null);
/*     */       } 
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */   
/*     */   private boolean reachedTarget(Mob debug1, WalkTarget debug2) {
/* 163 */     return (debug2.getTarget().currentBlockPosition().distManhattan((Vec3i)debug1.blockPosition()) <= debug2.getCloseEnoughDist());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\MoveToTargetSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */