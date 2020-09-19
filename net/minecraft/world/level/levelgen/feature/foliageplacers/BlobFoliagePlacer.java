/*    */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.LevelSimulatedRW;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public class BlobFoliagePlacer extends FoliagePlacer {
/*    */   public static final Codec<BlobFoliagePlacer> CODEC;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(debug0 -> blobParts(debug0).apply((Applicative)debug0, BlobFoliagePlacer::new));
/*    */   } protected final int height;
/*    */   protected static <P extends BlobFoliagePlacer> Products.P3<RecordCodecBuilder.Mu<P>, UniformInt, UniformInt, Integer> blobParts(RecordCodecBuilder.Instance<P> debug0) {
/* 19 */     return foliagePlacerParts((RecordCodecBuilder.Instance)debug0).and(
/* 20 */         (App)Codec.intRange(0, 16).fieldOf("height").forGetter(debug0 -> Integer.valueOf(debug0.height)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlobFoliagePlacer(UniformInt debug1, UniformInt debug2, int debug3) {
/* 27 */     super(debug1, debug2);
/* 28 */     this.height = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FoliagePlacerType<?> type() {
/* 33 */     return FoliagePlacerType.BLOB_FOLIAGE_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliagePlacer.FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, int debug9, BoundingBox debug10) {
/* 38 */     for (int debug11 = debug9; debug11 >= debug9 - debug6; debug11--) {
/* 39 */       int debug12 = Math.max(debug7 + debug5.radiusOffset() - 1 - debug11 / 2, 0);
/* 40 */       placeLeavesRow(debug1, debug2, debug3, debug5.foliagePos(), debug12, debug8, debug11, debug5.doubleTrunk(), debug10);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int foliageHeight(Random debug1, int debug2, TreeConfiguration debug3) {
/* 46 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSkipLocation(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/* 51 */     return (debug2 == debug5 && debug4 == debug5 && (debug1.nextInt(2) == 0 || debug3 == 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\BlobFoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */