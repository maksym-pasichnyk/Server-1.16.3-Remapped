/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GroundPathNavigation extends PathNavigation {
/*     */   public GroundPathNavigation(Mob debug1, Level debug2) {
/*  22 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathFinder createPathFinder(int debug1) {
/*  27 */     this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/*  28 */     this.nodeEvaluator.setCanPassDoors(true);
/*  29 */     return new PathFinder(this.nodeEvaluator, debug1);
/*     */   }
/*     */   private boolean avoidSun;
/*     */   
/*     */   protected boolean canUpdatePath() {
/*  34 */     return (this.mob.isOnGround() || isInLiquid() || this.mob.isPassenger());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getTempMobPos() {
/*  39 */     return new Vec3(this.mob.getX(), getSurfaceY(), this.mob.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createPath(BlockPos debug1, int debug2) {
/*  44 */     if (this.level.getBlockState(debug1).isAir()) {
/*  45 */       BlockPos debug3 = debug1.below();
/*  46 */       while (debug3.getY() > 0 && this.level.getBlockState(debug3).isAir()) {
/*  47 */         debug3 = debug3.below();
/*     */       }
/*     */       
/*  50 */       if (debug3.getY() > 0) {
/*  51 */         return super.createPath(debug3.above(), debug2);
/*     */       }
/*     */       
/*  54 */       while (debug3.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(debug3).isAir()) {
/*  55 */         debug3 = debug3.above();
/*     */       }
/*  57 */       debug1 = debug3;
/*     */     } 
/*     */     
/*  60 */     if (this.level.getBlockState(debug1).getMaterial().isSolid()) {
/*  61 */       BlockPos debug3 = debug1.above();
/*  62 */       while (debug3.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(debug3).getMaterial().isSolid()) {
/*  63 */         debug3 = debug3.above();
/*     */       }
/*  65 */       return super.createPath(debug3, debug2);
/*     */     } 
/*     */     
/*  68 */     return super.createPath(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Path createPath(Entity debug1, int debug2) {
/*  73 */     return createPath(debug1.blockPosition(), debug2);
/*     */   }
/*     */   
/*     */   private int getSurfaceY() {
/*  77 */     if (!this.mob.isInWater() || !canFloat()) {
/*  78 */       return Mth.floor(this.mob.getY() + 0.5D);
/*     */     }
/*     */ 
/*     */     
/*  82 */     int debug1 = Mth.floor(this.mob.getY());
/*  83 */     Block debug2 = this.level.getBlockState(new BlockPos(this.mob.getX(), debug1, this.mob.getZ())).getBlock();
/*  84 */     int debug3 = 0;
/*  85 */     while (debug2 == Blocks.WATER) {
/*  86 */       debug1++;
/*  87 */       debug2 = this.level.getBlockState(new BlockPos(this.mob.getX(), debug1, this.mob.getZ())).getBlock();
/*  88 */       if (++debug3 > 16) {
/*  89 */         return Mth.floor(this.mob.getY());
/*     */       }
/*     */     } 
/*  92 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void trimPath() {
/*  97 */     super.trimPath();
/*     */     
/*  99 */     if (this.avoidSun) {
/* 100 */       if (this.level.canSeeSky(new BlockPos(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()))) {
/*     */         return;
/*     */       }
/*     */       
/* 104 */       for (int debug1 = 0; debug1 < this.path.getNodeCount(); debug1++) {
/* 105 */         Node debug2 = this.path.getNode(debug1);
/* 106 */         if (this.level.canSeeSky(new BlockPos(debug2.x, debug2.y, debug2.z))) {
/* 107 */           this.path.truncateNodes(debug1);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canMoveDirectly(Vec3 debug1, Vec3 debug2, int debug3, int debug4, int debug5) {
/* 116 */     int debug6 = Mth.floor(debug1.x);
/* 117 */     int debug7 = Mth.floor(debug1.z);
/*     */     
/* 119 */     double debug8 = debug2.x - debug1.x;
/* 120 */     double debug10 = debug2.z - debug1.z;
/* 121 */     double debug12 = debug8 * debug8 + debug10 * debug10;
/* 122 */     if (debug12 < 1.0E-8D) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     double debug14 = 1.0D / Math.sqrt(debug12);
/* 127 */     debug8 *= debug14;
/* 128 */     debug10 *= debug14;
/*     */     
/* 130 */     debug3 += 2;
/* 131 */     debug5 += 2;
/* 132 */     if (!canWalkOn(debug6, Mth.floor(debug1.y), debug7, debug3, debug4, debug5, debug1, debug8, debug10)) {
/* 133 */       return false;
/*     */     }
/* 135 */     debug3 -= 2;
/* 136 */     debug5 -= 2;
/*     */     
/* 138 */     double debug16 = 1.0D / Math.abs(debug8);
/* 139 */     double debug18 = 1.0D / Math.abs(debug10);
/*     */     
/* 141 */     double debug20 = debug6 - debug1.x;
/* 142 */     double debug22 = debug7 - debug1.z;
/* 143 */     if (debug8 >= 0.0D) {
/* 144 */       debug20++;
/*     */     }
/* 146 */     if (debug10 >= 0.0D) {
/* 147 */       debug22++;
/*     */     }
/* 149 */     debug20 /= debug8;
/* 150 */     debug22 /= debug10;
/*     */     
/* 152 */     int debug24 = (debug8 < 0.0D) ? -1 : 1;
/* 153 */     int debug25 = (debug10 < 0.0D) ? -1 : 1;
/* 154 */     int debug26 = Mth.floor(debug2.x);
/* 155 */     int debug27 = Mth.floor(debug2.z);
/* 156 */     int debug28 = debug26 - debug6;
/* 157 */     int debug29 = debug27 - debug7;
/* 158 */     while (debug28 * debug24 > 0 || debug29 * debug25 > 0) {
/* 159 */       if (debug20 < debug22) {
/* 160 */         debug20 += debug16;
/* 161 */         debug6 += debug24;
/* 162 */         debug28 = debug26 - debug6;
/*     */       } else {
/* 164 */         debug22 += debug18;
/* 165 */         debug7 += debug25;
/* 166 */         debug29 = debug27 - debug7;
/*     */       } 
/*     */       
/* 169 */       if (!canWalkOn(debug6, Mth.floor(debug1.y), debug7, debug3, debug4, debug5, debug1, debug8, debug10)) {
/* 170 */         return false;
/*     */       }
/*     */     } 
/* 173 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canWalkOn(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6, Vec3 debug7, double debug8, double debug10) {
/* 177 */     int debug12 = debug1 - debug4 / 2;
/* 178 */     int debug13 = debug3 - debug6 / 2;
/*     */     
/* 180 */     if (!canWalkAbove(debug12, debug2, debug13, debug4, debug5, debug6, debug7, debug8, debug10)) {
/* 181 */       return false;
/*     */     }
/*     */     
/* 184 */     for (int debug14 = debug12; debug14 < debug12 + debug4; debug14++) {
/* 185 */       for (int debug15 = debug13; debug15 < debug13 + debug6; debug15++) {
/* 186 */         double debug16 = debug14 + 0.5D - debug7.x;
/* 187 */         double debug18 = debug15 + 0.5D - debug7.z;
/* 188 */         if (debug16 * debug8 + debug18 * debug10 >= 0.0D) {
/*     */ 
/*     */ 
/*     */           
/* 192 */           BlockPathTypes debug20 = this.nodeEvaluator.getBlockPathType((BlockGetter)this.level, debug14, debug2 - 1, debug15, this.mob, debug4, debug5, debug6, true, true);
/*     */           
/* 194 */           if (!hasValidPathType(debug20)) {
/* 195 */             return false;
/*     */           }
/*     */           
/* 198 */           debug20 = this.nodeEvaluator.getBlockPathType((BlockGetter)this.level, debug14, debug2, debug15, this.mob, debug4, debug5, debug6, true, true);
/* 199 */           float debug21 = this.mob.getPathfindingMalus(debug20);
/*     */           
/* 201 */           if (debug21 < 0.0F || debug21 >= 8.0F) {
/* 202 */             return false;
/*     */           }
/*     */           
/* 205 */           if (debug20 == BlockPathTypes.DAMAGE_FIRE || debug20 == BlockPathTypes.DANGER_FIRE || debug20 == BlockPathTypes.DAMAGE_OTHER) {
/* 206 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 211 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean hasValidPathType(BlockPathTypes debug1) {
/* 215 */     if (debug1 == BlockPathTypes.WATER) {
/* 216 */       return false;
/*     */     }
/*     */     
/* 219 */     if (debug1 == BlockPathTypes.LAVA) {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     if (debug1 == BlockPathTypes.OPEN) {
/* 224 */       return false;
/*     */     }
/*     */     
/* 227 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canWalkAbove(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6, Vec3 debug7, double debug8, double debug10) {
/* 231 */     for (BlockPos debug13 : BlockPos.betweenClosed(new BlockPos(debug1, debug2, debug3), new BlockPos(debug1 + debug4 - 1, debug2 + debug5 - 1, debug3 + debug6 - 1))) {
/* 232 */       double debug14 = debug13.getX() + 0.5D - debug7.x;
/* 233 */       double debug16 = debug13.getZ() + 0.5D - debug7.z;
/* 234 */       if (debug14 * debug8 + debug16 * debug10 < 0.0D) {
/*     */         continue;
/*     */       }
/* 237 */       if (!this.level.getBlockState(debug13).isPathfindable((BlockGetter)this.level, debug13, PathComputationType.LAND)) {
/* 238 */         return false;
/*     */       }
/*     */     } 
/* 241 */     return true;
/*     */   }
/*     */   
/*     */   public void setCanOpenDoors(boolean debug1) {
/* 245 */     this.nodeEvaluator.setCanOpenDoors(debug1);
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
/*     */   public boolean canOpenDoors() {
/* 257 */     return this.nodeEvaluator.canPassDoors();
/*     */   }
/*     */   
/*     */   public void setAvoidSun(boolean debug1) {
/* 261 */     this.avoidSun = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\navigation\GroundPathNavigation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */