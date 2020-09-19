/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ 
/*     */ public enum Rotation
/*     */ {
/*  14 */   NONE(OctahedralGroup.IDENTITY),
/*  15 */   CLOCKWISE_90(OctahedralGroup.ROT_90_Y_NEG),
/*  16 */   CLOCKWISE_180(OctahedralGroup.ROT_180_FACE_XZ),
/*  17 */   COUNTERCLOCKWISE_90(OctahedralGroup.ROT_90_Y_POS);
/*     */   
/*     */   private final OctahedralGroup rotation;
/*     */ 
/*     */   
/*     */   Rotation(OctahedralGroup debug3) {
/*  23 */     this.rotation = debug3;
/*     */   }
/*     */   
/*     */   public Rotation getRotated(Rotation debug1) {
/*  27 */     switch (debug1) {
/*     */       case CLOCKWISE_180:
/*  29 */         switch (this) {
/*     */           case NONE:
/*  31 */             return CLOCKWISE_180;
/*     */           case CLOCKWISE_90:
/*  33 */             return COUNTERCLOCKWISE_90;
/*     */           case CLOCKWISE_180:
/*  35 */             return NONE;
/*     */           case COUNTERCLOCKWISE_90:
/*  37 */             return CLOCKWISE_90;
/*     */         } 
/*     */       case COUNTERCLOCKWISE_90:
/*  40 */         switch (this) {
/*     */           case NONE:
/*  42 */             return COUNTERCLOCKWISE_90;
/*     */           case CLOCKWISE_90:
/*  44 */             return NONE;
/*     */           case CLOCKWISE_180:
/*  46 */             return CLOCKWISE_90;
/*     */           case COUNTERCLOCKWISE_90:
/*  48 */             return CLOCKWISE_180;
/*     */         } 
/*     */       case CLOCKWISE_90:
/*  51 */         switch (this) {
/*     */           case NONE:
/*  53 */             return CLOCKWISE_90;
/*     */           case CLOCKWISE_90:
/*  55 */             return CLOCKWISE_180;
/*     */           case CLOCKWISE_180:
/*  57 */             return COUNTERCLOCKWISE_90;
/*     */           case COUNTERCLOCKWISE_90:
/*  59 */             return NONE;
/*     */         }  break;
/*     */     } 
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public OctahedralGroup rotation() {
/*  67 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Direction rotate(Direction debug1) {
/*  71 */     if (debug1.getAxis() == Direction.Axis.Y) {
/*  72 */       return debug1;
/*     */     }
/*  74 */     switch (this) {
/*     */       case CLOCKWISE_180:
/*  76 */         return debug1.getOpposite();
/*     */       case COUNTERCLOCKWISE_90:
/*  78 */         return debug1.getCounterClockWise();
/*     */       case CLOCKWISE_90:
/*  80 */         return debug1.getClockWise();
/*     */     } 
/*  82 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int rotate(int debug1, int debug2) {
/*  87 */     switch (this) {
/*     */       case CLOCKWISE_180:
/*  89 */         return (debug1 + debug2 / 2) % debug2;
/*     */       case COUNTERCLOCKWISE_90:
/*  91 */         return (debug1 + debug2 * 3 / 4) % debug2;
/*     */       case CLOCKWISE_90:
/*  93 */         return (debug1 + debug2 / 4) % debug2;
/*     */     } 
/*  95 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Rotation getRandom(Random debug0) {
/* 100 */     return (Rotation)Util.getRandom((Object[])values(), debug0);
/*     */   }
/*     */   
/*     */   public static List<Rotation> getShuffled(Random debug0) {
/* 104 */     List<Rotation> debug1 = Lists.newArrayList((Object[])values());
/* 105 */     Collections.shuffle(debug1, debug0);
/* 106 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\Rotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */