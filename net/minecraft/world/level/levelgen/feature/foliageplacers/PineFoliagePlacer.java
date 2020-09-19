/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class PineFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(debug0 -> foliagePlacerParts(debug0).and((App)UniformInt.codec(0, 16, 8).fieldOf("height").forGetter(())).apply((Applicative)debug0, PineFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final Codec<PineFoliagePlacer> CODEC;
/*    */   private final UniformInt height;
/*    */   
/*    */   public PineFoliagePlacer(UniformInt debug1, UniformInt debug2, UniformInt debug3) {
/* 22 */     super(debug1, debug2);
/* 23 */     this.height = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 28 */     return FoliagePlacerType.PINE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 33 */     int debug11 = 0;
/*    */     
/* 35 */     for (int debug12 = debug9; debug12 >= debug9 - debug6; debug12--) {
/* 36 */       placeLeavesRow(debug1, debug2, debug3, debug5.foliagePos(), debug11, debug8, debug12, debug5.doubleTrunk(), debug10);
/*    */       
/* 38 */       if (debug11 >= 1 && debug12 == debug9 - debug6 + 1) {
/* 39 */         debug11--;
/* 40 */       } else if (debug11 < debug7 + debug5.radiusOffset()) {
/* 41 */         debug11++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageRadius(Random debug1, int debug2) {
/* 48 */     return super.foliageRadius(debug1, debug2) + debug1.nextInt(debug2 + 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 53 */     return this.height.sample(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 58 */     return (debug2 == debug5 && debug4 == debug5 && debug5 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\PineFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */