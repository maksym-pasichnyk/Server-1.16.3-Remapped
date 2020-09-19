/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FlyingPathNavigation extends PathNavigation {
/*     */   public FlyingPathNavigation(Mob debug1, Level debug2) {
/*  16 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathFinder createPathFinder(int debug1) {
/*  21 */     this.nodeEvaluator = (NodeEvaluator)new FlyNodeEvaluator();
/*  22 */     this.nodeEvaluator.setCanPassDoors(true);
/*  23 */     return new PathFinder(this.nodeEvaluator, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canUpdatePath() {
/*  28 */     return ((canFloat() && isInLiquid()) || !this.mob.isPassenger());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getTempMobPos() {
/*  33 */     return this.mob.position();
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createPath(Entity debug1, int debug2) {
/*  38 */     return createPath(debug1.blockPosition(), debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  43 */     this.tick++;
/*     */     
/*  45 */     if (this.hasDelayedRecomputation) {
/*  46 */       recomputePath();
/*     */     }
/*     */     
/*  49 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/*  53 */     if (canUpdatePath()) {
/*  54 */       followThePath();
/*  55 */     } else if (this.path != null && !this.path.isDone()) {
/*  56 */       Vec3 vec3 = this.path.getNextEntityPos((Entity)this.mob);
/*  57 */       if (Mth.floor(this.mob.getX()) == Mth.floor(vec3.x) && Mth.floor(this.mob.getY()) == Mth.floor(vec3.y) && Mth.floor(this.mob.getZ()) == Mth.floor(vec3.z)) {
/*  58 */         this.path.advance();
/*     */       }
/*     */     } 
/*     */     
/*  62 */     DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
/*     */     
/*  64 */     if (isDone()) {
/*     */       return;
/*     */     }
/*  67 */     Vec3 debug1 = this.path.getNextEntityPos((Entity)this.mob);
/*     */     
/*  69 */     this.mob.getMoveControl().setWantedPosition(debug1.x, debug1.y, debug1.z, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canMoveDirectly(Vec3 debug1, Vec3 debug2, int debug3, int debug4, int debug5) {
/*  74 */     int debug6 = Mth.floor(debug1.x);
/*  75 */     int debug7 = Mth.floor(debug1.y);
/*  76 */     int debug8 = Mth.floor(debug1.z);
/*     */     
/*  78 */     double debug9 = debug2.x - debug1.x;
/*  79 */     double debug11 = debug2.y - debug1.y;
/*  80 */     double debug13 = debug2.z - debug1.z;
/*  81 */     double debug15 = debug9 * debug9 + debug11 * debug11 + debug13 * debug13;
/*  82 */     if (debug15 < 1.0E-8D) {
/*  83 */       return false;
/*     */     }
/*     */     
/*  86 */     double debug17 = 1.0D / Math.sqrt(debug15);
/*  87 */     debug9 *= debug17;
/*  88 */     debug11 *= debug17;
/*  89 */     debug13 *= debug17;
/*     */     
/*  91 */     double debug19 = 1.0D / Math.abs(debug9);
/*  92 */     double debug21 = 1.0D / Math.abs(debug11);
/*  93 */     double debug23 = 1.0D / Math.abs(debug13);
/*     */     
/*  95 */     double debug25 = debug6 - debug1.x;
/*  96 */     double debug27 = debug7 - debug1.y;
/*  97 */     double debug29 = debug8 - debug1.z;
/*  98 */     if (debug9 >= 0.0D) {
/*  99 */       debug25++;
/*     */     }
/* 101 */     if (debug11 >= 0.0D) {
/* 102 */       debug27++;
/*     */     }
/* 104 */     if (debug13 >= 0.0D) {
/* 105 */       debug29++;
/*     */     }
/* 107 */     debug25 /= debug9;
/* 108 */     debug27 /= debug11;
/* 109 */     debug29 /= debug13;
/*     */     
/* 111 */     int debug31 = (debug9 < 0.0D) ? -1 : 1;
/* 112 */     int debug32 = (debug11 < 0.0D) ? -1 : 1;
/* 113 */     int debug33 = (debug13 < 0.0D) ? -1 : 1;
/* 114 */     int debug34 = Mth.floor(debug2.x);
/* 115 */     int debug35 = Mth.floor(debug2.y);
/* 116 */     int debug36 = Mth.floor(debug2.z);
/* 117 */     int debug37 = debug34 - debug6;
/* 118 */     int debug38 = debug35 - debug7;
/* 119 */     int debug39 = debug36 - debug8;
/*     */     
/* 121 */     while (debug37 * debug31 > 0 || debug38 * debug32 > 0 || debug39 * debug33 > 0) {
/* 122 */       if (debug25 < debug29 && debug25 <= debug27) {
/* 123 */         debug25 += debug19;
/* 124 */         debug6 += debug31;
/* 125 */         debug37 = debug34 - debug6; continue;
/* 126 */       }  if (debug27 < debug25 && debug27 <= debug29) {
/* 127 */         debug27 += debug21;
/* 128 */         debug7 += debug32;
/* 129 */         debug38 = debug35 - debug7; continue;
/*     */       } 
/* 131 */       debug29 += debug23;
/* 132 */       debug8 += debug33;
/* 133 */       debug39 = debug36 - debug8;
/*     */     } 
/*     */     
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public void setCanOpenDoors(boolean debug1) {
/* 140 */     this.nodeEvaluator.setCanOpenDoors(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanPassDoors(boolean debug1) {
/* 148 */     this.nodeEvaluator.setCanPassDoors(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStableDestination(BlockPos debug1) {
/* 157 */     return this.level.getBlockState(debug1).entityCanStandOn((BlockGetter)this.level, debug1, (Entity)this.mob);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\navigation\FlyingPathNavigation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */