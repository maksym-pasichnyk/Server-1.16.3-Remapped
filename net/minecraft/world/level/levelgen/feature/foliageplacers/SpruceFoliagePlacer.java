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
/*    */ public class SpruceFoliagePlacer extends FoliagePlacer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(debug0 -> foliagePlacerParts(debug0).and((App)UniformInt.codec(0, 16, 8).fieldOf("trunk_height").forGetter(())).apply((Applicative)debug0, SpruceFoliagePlacer::new));
/*    */   }
/*    */   
/*    */   public static final Codec<SpruceFoliagePlacer> CODEC;
/*    */   private final UniformInt trunkHeight;
/*    */   
/*    */   public SpruceFoliagePlacer(UniformInt debug1, UniformInt debug2, UniformInt debug3) {
/* 22 */     super(debug1, debug2);
/* 23 */     this.trunkHeight = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 28 */     return FoliagePlacerType.SPRUCE_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 33 */     BlockPos debug11 = debug5.foliagePos();
/*    */     
/* 35 */     int debug12 = debug2.nextInt(2);
/* 36 */     int debug13 = 1;
/* 37 */     int debug14 = 0;
/*    */     
/* 39 */     for (int debug15 = debug9; debug15 >= -debug6; debug15--) {
/* 40 */       placeLeavesRow(debug1, debug2, debug3, debug11, debug12, debug8, debug15, debug5.doubleTrunk(), debug10);
/*    */       
/* 42 */       if (debug12 >= debug13) {
/* 43 */         debug12 = debug14;
/* 44 */         debug14 = 1;
/* 45 */         debug13 = Math.min(debug13 + 1, debug7 + debug5.radiusOffset());
/*    */       } else {
/* 47 */         debug12++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 55 */     return Math.max(4, debug2 - this.trunkHeight.sample(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 60 */     return (debug2 == debug5 && debug4 == debug5 && debug5 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\SpruceFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */