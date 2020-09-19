/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class MegaPineFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(debug0 -> foliagePlacerParts(debug0).and((App)UniformInt.codec(0, 16, 8).fieldOf("crown_height").forGetter(())).apply((Applicative)debug0, MegaPineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final Codec<MegaPineFoliagePlacer> CODEC;
/*    */   private final UniformInt crownHeight;
/*    */   
/*    */   public MegaPineFoliagePlacer(UniformInt debug1, UniformInt debug2, UniformInt debug3) {
/* 24 */     super(debug1, debug2);
/* 25 */     this.crownHeight = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 30 */     return FoliagePlacerType.MEGA_PINE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 35 */     BlockPos debug11 = debug5.foliagePos();
/*    */     
/* 37 */     int debug12 = 0;
/* 38 */     for (int debug13 = debug11.getY() - debug6 + debug9; debug13 <= debug11.getY() + debug9; debug13++) {
/* 39 */       int debug16, debug14 = debug11.getY() - debug13;
/* 40 */       int debug15 = debug7 + debug5.radiusOffset() + Mth.floor(debug14 / debug6 * 3.5F);
/*    */       
/* 42 */       if (debug14 > 0 && debug15 == debug12 && (debug13 & 0x1) == 0) {
/* 43 */         debug16 = debug15 + 1;
/*    */       } else {
/* 45 */         debug16 = debug15;
/*    */       } 
/*    */       
/* 48 */       placeLeavesRow(debug1, debug2, debug3, new BlockPos(debug11.getX(), debug13, debug11.getZ()), debug16, debug8, 0, debug5.doubleTrunk(), debug10);
/* 49 */       debug12 = debug15;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 55 */     return this.crownHeight.sample(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 60 */     if (debug2 + debug4 >= 7) {
/* 61 */       return true;
/*    */     }
/* 63 */     return (debug2 * debug2 + debug4 * debug4 > debug5 * debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\MegaPineFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */