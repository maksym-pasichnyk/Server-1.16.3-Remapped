/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JumpOnBed
/*     */   extends Behavior<Mob>
/*     */ {
/*     */   private final float speedModifier;
/*     */   @Nullable
/*     */   private BlockPos targetBed;
/*     */   private int remainingTimeToReachBed;
/*     */   private int remainingJumps;
/*     */   private int remainingCooldownUntilNextJump;
/*     */   
/*     */   public JumpOnBed(float debug1) {
/*  35 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*     */ 
/*     */ 
/*     */     
/*  39 */     this.speedModifier = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Mob debug2) {
/*  44 */     return (debug2.isBaby() && nearBed(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Mob debug2, long debug3) {
/*  49 */     super.start(debug1, debug2, debug3);
/*     */     
/*  51 */     getNearestBed(debug2).ifPresent(debug3 -> {
/*     */           this.targetBed = debug3;
/*     */           this.remainingTimeToReachBed = 100;
/*     */           this.remainingJumps = 3 + debug1.random.nextInt(4);
/*     */           this.remainingCooldownUntilNextJump = 0;
/*     */           startWalkingTowardsBed(debug2, debug3);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Mob debug2, long debug3) {
/*  62 */     super.stop(debug1, debug2, debug3);
/*     */     
/*  64 */     this.targetBed = null;
/*  65 */     this.remainingTimeToReachBed = 0;
/*  66 */     this.remainingJumps = 0;
/*  67 */     this.remainingCooldownUntilNextJump = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Mob debug2, long debug3) {
/*  72 */     return (debug2.isBaby() && this.targetBed != null && 
/*     */       
/*  74 */       isBed(debug1, this.targetBed) && 
/*  75 */       !tiredOfWalking(debug1, debug2) && 
/*  76 */       !tiredOfJumping(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean timedOut(long debug1) {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Mob debug2, long debug3) {
/*  86 */     if (!onOrOverBed(debug1, debug2)) {
/*  87 */       this.remainingTimeToReachBed--;
/*     */       
/*     */       return;
/*     */     } 
/*  91 */     if (this.remainingCooldownUntilNextJump > 0) {
/*  92 */       this.remainingCooldownUntilNextJump--;
/*     */       
/*     */       return;
/*     */     } 
/*  96 */     if (onBedSurface(debug1, debug2)) {
/*  97 */       debug2.getJumpControl().jump();
/*  98 */       this.remainingJumps--;
/*  99 */       this.remainingCooldownUntilNextJump = 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startWalkingTowardsBed(Mob debug1, BlockPos debug2) {
/* 104 */     debug1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug2, this.speedModifier, 0));
/*     */   }
/*     */   
/*     */   private boolean nearBed(ServerLevel debug1, Mob debug2) {
/* 108 */     return (onOrOverBed(debug1, debug2) || getNearestBed(debug2).isPresent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean onOrOverBed(ServerLevel debug1, Mob debug2) {
/* 115 */     BlockPos debug3 = debug2.blockPosition();
/* 116 */     BlockPos debug4 = debug3.below();
/* 117 */     return (isBed(debug1, debug3) || isBed(debug1, debug4));
/*     */   }
/*     */   
/*     */   private boolean onBedSurface(ServerLevel debug1, Mob debug2) {
/* 121 */     return isBed(debug1, debug2.blockPosition());
/*     */   }
/*     */   
/*     */   private boolean isBed(ServerLevel debug1, BlockPos debug2) {
/* 125 */     return debug1.getBlockState(debug2).is((Tag)BlockTags.BEDS);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getNearestBed(Mob debug1) {
/* 129 */     return debug1.getBrain().getMemory(MemoryModuleType.NEAREST_BED);
/*     */   }
/*     */   
/*     */   private boolean tiredOfWalking(ServerLevel debug1, Mob debug2) {
/* 133 */     return (!onOrOverBed(debug1, debug2) && this.remainingTimeToReachBed <= 0);
/*     */   }
/*     */   
/*     */   private boolean tiredOfJumping(ServerLevel debug1, Mob debug2) {
/* 137 */     return (onOrOverBed(debug1, debug2) && this.remainingJumps <= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\JumpOnBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */