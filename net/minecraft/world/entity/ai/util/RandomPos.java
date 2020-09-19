/*     */ package net.minecraft.world.entity.ai.util;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class RandomPos
/*     */ {
/*     */   @Nullable
/*     */   public static Vec3 getPos(PathfinderMob debug0, int debug1, int debug2) {
/*  41 */     return generateRandomPos(debug0, debug1, debug2, 0, null, true, 1.5707963705062866D, debug0::getWalkTargetValue, false, 0, 0, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getAirPos(PathfinderMob debug0, int debug1, int debug2, int debug3, @Nullable Vec3 debug4, double debug5) {
/*  49 */     return generateRandomPos(debug0, debug1, debug2, debug3, debug4, true, debug5, debug0::getWalkTargetValue, true, 0, 0, false);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getLandPos(PathfinderMob debug0, int debug1, int debug2) {
/*  54 */     return getLandPos(debug0, debug1, debug2, debug0::getWalkTargetValue);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getLandPos(PathfinderMob debug0, int debug1, int debug2, ToDoubleFunction<BlockPos> debug3) {
/*  59 */     return generateRandomPos(debug0, debug1, debug2, 0, null, false, 0.0D, debug3, true, 0, 0, true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getAboveLandPos(PathfinderMob debug0, int debug1, int debug2, Vec3 debug3, float debug4, int debug5, int debug6) {
/*  64 */     return generateRandomPos(debug0, debug1, debug2, 0, debug3, false, debug4, debug0::getWalkTargetValue, true, debug5, debug6, true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getLandPosTowards(PathfinderMob debug0, int debug1, int debug2, Vec3 debug3) {
/*  69 */     Vec3 debug4 = debug3.subtract(debug0.getX(), debug0.getY(), debug0.getZ());
/*  70 */     return generateRandomPos(debug0, debug1, debug2, 0, debug4, false, 1.5707963705062866D, debug0::getWalkTargetValue, true, 0, 0, true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getPosTowards(PathfinderMob debug0, int debug1, int debug2, Vec3 debug3) {
/*  75 */     Vec3 debug4 = debug3.subtract(debug0.getX(), debug0.getY(), debug0.getZ());
/*  76 */     return generateRandomPos(debug0, debug1, debug2, 0, debug4, true, 1.5707963705062866D, debug0::getWalkTargetValue, false, 0, 0, true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getPosTowards(PathfinderMob debug0, int debug1, int debug2, Vec3 debug3, double debug4) {
/*  81 */     Vec3 debug6 = debug3.subtract(debug0.getX(), debug0.getY(), debug0.getZ());
/*  82 */     return generateRandomPos(debug0, debug1, debug2, 0, debug6, true, debug4, debug0::getWalkTargetValue, false, 0, 0, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getAirPosTowards(PathfinderMob debug0, int debug1, int debug2, int debug3, Vec3 debug4, double debug5) {
/*  91 */     Vec3 debug7 = debug4.subtract(debug0.getX(), debug0.getY(), debug0.getZ());
/*     */     
/*  93 */     return generateRandomPos(debug0, debug1, debug2, debug3, debug7, false, debug5, debug0::getWalkTargetValue, true, 0, 0, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getPosAvoid(PathfinderMob debug0, int debug1, int debug2, Vec3 debug3) {
/*  99 */     Vec3 debug4 = debug0.position().subtract(debug3);
/* 100 */     return generateRandomPos(debug0, debug1, debug2, 0, debug4, true, 1.5707963705062866D, debug0::getWalkTargetValue, false, 0, 0, true);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Vec3 getLandPosAvoid(PathfinderMob debug0, int debug1, int debug2, Vec3 debug3) {
/* 105 */     Vec3 debug4 = debug0.position().subtract(debug3);
/* 106 */     return generateRandomPos(debug0, debug1, debug2, 0, debug4, false, 1.5707963705062866D, debug0::getWalkTargetValue, true, 0, 0, true);
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
/*     */   
/*     */   @Nullable
/*     */   private static Vec3 generateRandomPos(PathfinderMob debug0, int debug1, int debug2, int debug3, @Nullable Vec3 debug4, boolean debug5, double debug6, ToDoubleFunction<BlockPos> debug8, boolean debug9, int debug10, int debug11, boolean debug12) {
/*     */     boolean debug15;
/* 137 */     PathNavigation debug13 = debug0.getNavigation();
/* 138 */     Random debug14 = debug0.getRandom();
/*     */ 
/*     */     
/* 141 */     if (debug0.hasRestriction()) {
/* 142 */       debug15 = debug0.getRestrictCenter().closerThan((Position)debug0.position(), (debug0.getRestrictRadius() + debug1) + 1.0D);
/*     */     } else {
/* 144 */       debug15 = false;
/*     */     } 
/*     */     
/* 147 */     boolean debug16 = false;
/* 148 */     double debug17 = Double.NEGATIVE_INFINITY;
/*     */     
/* 150 */     BlockPos debug19 = debug0.blockPosition();
/*     */     
/* 152 */     for (int debug20 = 0; debug20 < 10; debug20++) {
/* 153 */       BlockPos debug21 = getRandomDelta(debug14, debug1, debug2, debug3, debug4, debug6);
/* 154 */       if (debug21 != null) {
/*     */ 
/*     */ 
/*     */         
/* 158 */         int debug22 = debug21.getX();
/* 159 */         int debug23 = debug21.getY();
/* 160 */         int debug24 = debug21.getZ();
/*     */         
/* 162 */         if (debug0.hasRestriction() && debug1 > 1) {
/* 163 */           BlockPos blockPos = debug0.getRestrictCenter();
/* 164 */           if (debug0.getX() > blockPos.getX()) {
/* 165 */             debug22 -= debug14.nextInt(debug1 / 2);
/*     */           } else {
/* 167 */             debug22 += debug14.nextInt(debug1 / 2);
/*     */           } 
/* 169 */           if (debug0.getZ() > blockPos.getZ()) {
/* 170 */             debug24 -= debug14.nextInt(debug1 / 2);
/*     */           } else {
/* 172 */             debug24 += debug14.nextInt(debug1 / 2);
/*     */           } 
/*     */         } 
/*     */         
/* 176 */         BlockPos debug25 = new BlockPos(debug22 + debug0.getX(), debug23 + debug0.getY(), debug24 + debug0.getZ());
/* 177 */         if (debug25.getY() >= 0 && debug25.getY() <= debug0.level.getMaxBuildHeight())
/*     */         {
/*     */ 
/*     */           
/* 181 */           if (!debug15 || debug0.isWithinRestriction(debug25))
/*     */           {
/*     */ 
/*     */             
/* 185 */             if (!debug12 || debug13.isStableDestination(debug25)) {
/*     */ 
/*     */ 
/*     */               
/* 189 */               if (debug9) {
/* 190 */                 debug25 = moveUpToAboveSolid(debug25, debug14.nextInt(debug10 + 1) + debug11, debug0.level.getMaxBuildHeight(), debug1 -> debug0.level.getBlockState(debug1).getMaterial().isSolid());
/*     */               }
/*     */               
/* 193 */               if (debug5 || !debug0.level.getFluidState(debug25).is((Tag)FluidTags.WATER))
/*     */               
/*     */               { 
/*     */                 
/* 197 */                 BlockPathTypes debug26 = WalkNodeEvaluator.getBlockPathTypeStatic((BlockGetter)debug0.level, debug25.mutable());
/* 198 */                 if (debug0.getPathfindingMalus(debug26) == 0.0F)
/*     */                 
/*     */                 { 
/*     */                   
/* 202 */                   double debug27 = debug8.applyAsDouble(debug25);
/* 203 */                   if (debug27 > debug17)
/* 204 */                   { debug17 = debug27;
/* 205 */                     debug19 = debug25;
/* 206 */                     debug16 = true; }  }  } 
/*     */             }  }  } 
/*     */       } 
/* 209 */     }  if (debug16) {
/* 210 */       return Vec3.atBottomCenterOf((Vec3i)debug19);
/*     */     }
/* 212 */     return null;
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
/*     */   @Nullable
/*     */   private static BlockPos getRandomDelta(Random debug0, int debug1, int debug2, int debug3, @Nullable Vec3 debug4, double debug5) {
/* 234 */     if (debug4 == null || debug5 >= Math.PI) {
/* 235 */       int i = debug0.nextInt(2 * debug1 + 1) - debug1;
/* 236 */       int debug8 = debug0.nextInt(2 * debug2 + 1) - debug2 + debug3;
/* 237 */       int j = debug0.nextInt(2 * debug1 + 1) - debug1;
/* 238 */       return new BlockPos(i, debug8, j);
/*     */     } 
/* 240 */     double debug7 = Mth.atan2(debug4.z, debug4.x) - 1.5707963705062866D;
/* 241 */     double debug9 = debug7 + (2.0F * debug0.nextFloat() - 1.0F) * debug5;
/* 242 */     double debug11 = Math.sqrt(debug0.nextDouble()) * Mth.SQRT_OF_TWO * debug1;
/* 243 */     double debug13 = -debug11 * Math.sin(debug9);
/* 244 */     double debug15 = debug11 * Math.cos(debug9);
/*     */     
/* 246 */     if (Math.abs(debug13) > debug1 || Math.abs(debug15) > debug1) {
/* 247 */       return null;
/*     */     }
/* 249 */     int debug17 = debug0.nextInt(2 * debug2 + 1) - debug2 + debug3;
/* 250 */     return new BlockPos(debug13, debug17, debug15);
/*     */   }
/*     */   
/*     */   static BlockPos moveUpToAboveSolid(BlockPos debug0, int debug1, int debug2, Predicate<BlockPos> debug3) {
/* 254 */     if (debug1 < 0) {
/* 255 */       throw new IllegalArgumentException("aboveSolidAmount was " + debug1 + ", expected >= 0");
/*     */     }
/* 257 */     if (debug3.test(debug0)) {
/*     */       
/* 259 */       BlockPos debug4 = debug0.above();
/* 260 */       while (debug4.getY() < debug2 && debug3.test(debug4)) {
/* 261 */         debug4 = debug4.above();
/*     */       }
/*     */       
/* 264 */       BlockPos debug5 = debug4;
/* 265 */       while (debug5.getY() < debug2 && debug5.getY() - debug4.getY() < debug1) {
/* 266 */         BlockPos debug6 = debug5.above();
/* 267 */         if (debug3.test(debug6)) {
/*     */           break;
/*     */         }
/* 270 */         debug5 = debug6;
/*     */       } 
/* 272 */       return debug5;
/*     */     } 
/* 274 */     return debug0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\a\\util\RandomPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */