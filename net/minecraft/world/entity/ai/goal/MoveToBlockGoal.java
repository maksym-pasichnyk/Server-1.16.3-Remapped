/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MoveToBlockGoal
/*     */   extends Goal
/*     */ {
/*     */   protected final PathfinderMob mob;
/*     */   public final double speedModifier;
/*     */   protected int nextStartTick;
/*     */   protected int tryTicks;
/*     */   private int maxStayTicks;
/*  20 */   protected BlockPos blockPos = BlockPos.ZERO;
/*     */   
/*     */   private boolean reachedTarget;
/*     */   private final int searchRange;
/*     */   private final int verticalSearchRange;
/*     */   protected int verticalSearchStart;
/*     */   
/*     */   public MoveToBlockGoal(PathfinderMob debug1, double debug2, int debug4) {
/*  28 */     this(debug1, debug2, debug4, 1);
/*     */   }
/*     */   
/*     */   public MoveToBlockGoal(PathfinderMob debug1, double debug2, int debug4, int debug5) {
/*  32 */     this.mob = debug1;
/*  33 */     this.speedModifier = debug2;
/*  34 */     this.searchRange = debug4;
/*  35 */     this.verticalSearchStart = 0;
/*  36 */     this.verticalSearchRange = debug5;
/*  37 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  42 */     if (this.nextStartTick > 0) {
/*  43 */       this.nextStartTick--;
/*  44 */       return false;
/*     */     } 
/*  46 */     this.nextStartTick = nextStartTick(this.mob);
/*  47 */     return findNearestBlock();
/*     */   }
/*     */   
/*     */   protected int nextStartTick(PathfinderMob debug1) {
/*  51 */     return 200 + debug1.getRandom().nextInt(200);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  56 */     return (this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.mob.level, this.blockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  61 */     moveMobToBlock();
/*  62 */     this.tryTicks = 0;
/*  63 */     this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(1200) + 1200) + 1200;
/*     */   }
/*     */   
/*     */   protected void moveMobToBlock() {
/*  67 */     this.mob.getNavigation().moveTo(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, this.speedModifier);
/*     */   }
/*     */   
/*     */   public double acceptedDistance() {
/*  71 */     return 1.0D;
/*     */   }
/*     */   
/*     */   protected BlockPos getMoveToTarget() {
/*  75 */     return this.blockPos.above();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  80 */     BlockPos debug1 = getMoveToTarget();
/*  81 */     if (!debug1.closerThan((Position)this.mob.position(), acceptedDistance())) {
/*  82 */       this.reachedTarget = false;
/*  83 */       this.tryTicks++;
/*  84 */       if (shouldRecalculatePath()) {
/*  85 */         this.mob.getNavigation().moveTo(debug1.getX() + 0.5D, debug1.getY(), debug1.getZ() + 0.5D, this.speedModifier);
/*     */       }
/*     */     } else {
/*  88 */       this.reachedTarget = true;
/*  89 */       this.tryTicks--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldRecalculatePath() {
/*  94 */     return (this.tryTicks % 40 == 0);
/*     */   }
/*     */   
/*     */   protected boolean isReachedTarget() {
/*  98 */     return this.reachedTarget;
/*     */   }
/*     */   
/*     */   protected boolean findNearestBlock() {
/* 102 */     int debug1 = this.searchRange;
/* 103 */     int debug2 = this.verticalSearchRange;
/* 104 */     BlockPos debug3 = this.mob.blockPosition();
/*     */     
/* 106 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos(); int debug5;
/* 107 */     for (debug5 = this.verticalSearchStart; debug5 <= debug2; debug5 = (debug5 > 0) ? -debug5 : (1 - debug5)) {
/* 108 */       for (int debug6 = 0; debug6 < debug1; debug6++) {
/* 109 */         int debug7; for (debug7 = 0; debug7 <= debug6; debug7 = (debug7 > 0) ? -debug7 : (1 - debug7)) {
/*     */           
/* 111 */           int debug8 = (debug7 < debug6 && debug7 > -debug6) ? debug6 : 0;
/* 112 */           for (; debug8 <= debug6; debug8 = (debug8 > 0) ? -debug8 : (1 - debug8)) {
/* 113 */             debug4.setWithOffset((Vec3i)debug3, debug7, debug5 - 1, debug8);
/* 114 */             if (this.mob.isWithinRestriction((BlockPos)debug4) && isValidTarget((LevelReader)this.mob.level, (BlockPos)debug4)) {
/* 115 */               this.blockPos = (BlockPos)debug4;
/* 116 */               return true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract boolean isValidTarget(LevelReader paramLevelReader, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveToBlockGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */