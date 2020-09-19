/*     */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.UniformInt;
/*     */ import net.minecraft.world.level.LevelSimulatedRW;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public abstract class FoliagePlacer {
/*  19 */   public static final Codec<FoliagePlacer> CODEC = Registry.FOLIAGE_PLACER_TYPES.dispatch(FoliagePlacer::type, FoliagePlacerType::codec);
/*     */   
/*     */   protected final UniformInt radius;
/*     */   protected final UniformInt offset;
/*     */   
/*     */   protected static <P extends FoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, UniformInt, UniformInt> foliagePlacerParts(RecordCodecBuilder.Instance<P> debug0) {
/*  25 */     return debug0.group(
/*  26 */         (App)UniformInt.codec(0, 8, 8).fieldOf("radius").forGetter(debug0 -> debug0.radius), 
/*  27 */         (App)UniformInt.codec(0, 8, 8).fieldOf("offset").forGetter(debug0 -> debug0.offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FoliagePlacer(UniformInt debug1, UniformInt debug2) {
/*  36 */     this.radius = debug1;
/*  37 */     this.offset = debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createFoliage(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, int debug4, FoliageAttachment debug5, int debug6, int debug7, Set<BlockPos> debug8, BoundingBox debug9) {
/*  43 */     createFoliage(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8, offset(debug2), debug9);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int foliageRadius(Random debug1, int debug2) {
/*  51 */     return this.radius.sample(debug1);
/*     */   }
/*     */   
/*     */   private int offset(Random debug1) {
/*  55 */     return this.offset.sample(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldSkipLocationSigned(Random debug1, int debug2, int debug3, int debug4, int debug5, boolean debug6) {
/*     */     int debug7;
/*     */     int debug8;
/*  63 */     if (debug6) {
/*  64 */       debug7 = Math.min(Math.abs(debug2), Math.abs(debug2 - 1));
/*  65 */       debug8 = Math.min(Math.abs(debug4), Math.abs(debug4 - 1));
/*     */     } else {
/*  67 */       debug7 = Math.abs(debug2);
/*  68 */       debug8 = Math.abs(debug4);
/*     */     } 
/*  70 */     return shouldSkipLocation(debug1, debug7, debug3, debug8, debug5, debug6);
/*     */   }
/*     */   
/*     */   protected void placeLeavesRow(LevelSimulatedRW debug1, Random debug2, TreeConfiguration debug3, BlockPos debug4, int debug5, Set<BlockPos> debug6, int debug7, boolean debug8, BoundingBox debug9) {
/*  74 */     int debug10 = debug8 ? 1 : 0;
/*  75 */     BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos();
/*  76 */     for (int debug12 = -debug5; debug12 <= debug5 + debug10; debug12++) {
/*  77 */       for (int debug13 = -debug5; debug13 <= debug5 + debug10; debug13++) {
/*  78 */         if (!shouldSkipLocationSigned(debug2, debug12, debug7, debug13, debug5, debug8)) {
/*     */ 
/*     */           
/*  81 */           debug11.setWithOffset((Vec3i)debug4, debug12, debug7, debug13);
/*  82 */           if (TreeFeature.validTreePos((LevelSimulatedReader)debug1, (BlockPos)debug11)) {
/*  83 */             debug1.setBlock((BlockPos)debug11, debug3.leavesProvider.getState(debug2, (BlockPos)debug11), 19);
/*  84 */             debug9.expand(new BoundingBox((Vec3i)debug11, (Vec3i)debug11));
/*  85 */             debug6.add(debug11.immutable());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   protected abstract FoliagePlacerType<?> type();
/*     */   protected abstract void createFoliage(LevelSimulatedRW paramLevelSimulatedRW, Random paramRandom, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, Set<BlockPos> paramSet, int paramInt4, BoundingBox paramBoundingBox);
/*     */   public abstract int foliageHeight(Random paramRandom, int paramInt, TreeConfiguration paramTreeConfiguration);
/*     */   protected abstract boolean shouldSkipLocation(Random paramRandom, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
/*     */   public static final class FoliageAttachment { private final BlockPos foliagePos;
/*     */     public FoliageAttachment(BlockPos debug1, int debug2, boolean debug3) {
/*  97 */       this.foliagePos = debug1;
/*  98 */       this.radiusOffset = debug2;
/*  99 */       this.doubleTrunk = debug3;
/*     */     }
/*     */     private final int radiusOffset; private final boolean doubleTrunk;
/*     */     public BlockPos foliagePos() {
/* 103 */       return this.foliagePos;
/*     */     }
/*     */     
/*     */     public int radiusOffset() {
/* 107 */       return this.radiusOffset;
/*     */     }
/*     */     
/*     */     public boolean doubleTrunk() {
/* 111 */       return this.doubleTrunk;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */