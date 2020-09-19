/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*    */ 
/*    */ public class ReplaceBlobsFeature extends Feature<ReplaceSphereConfiguration> {
/*    */   public ReplaceBlobsFeature(Codec<ReplaceSphereConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, ReplaceSphereConfiguration debug5) {
/* 23 */     Block debug6 = debug5.targetState.getBlock();
/* 24 */     BlockPos debug7 = findTarget((LevelAccessor)debug1, debug4.mutable().clamp(Direction.Axis.Y, 1, debug1.getMaxBuildHeight() - 1), debug6);
/* 25 */     if (debug7 == null) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     int debug8 = debug5.radius().sample(debug3);
/*    */     
/* 31 */     boolean debug9 = false;
/* 32 */     for (BlockPos debug11 : BlockPos.withinManhattan(debug7, debug8, debug8, debug8)) {
/* 33 */       if (debug11.distManhattan((Vec3i)debug7) > debug8) {
/*    */         break;
/*    */       }
/*    */ 
/*    */       
/* 38 */       BlockState debug12 = debug1.getBlockState(debug11);
/* 39 */       if (debug12.is(debug6)) {
/* 40 */         setBlock((LevelWriter)debug1, debug11, debug5.replaceState);
/* 41 */         debug9 = true;
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     return debug9;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private static BlockPos findTarget(LevelAccessor debug0, BlockPos.MutableBlockPos debug1, Block debug2) {
/* 50 */     while (debug1.getY() > 1) {
/* 51 */       BlockState debug3 = debug0.getBlockState((BlockPos)debug1);
/* 52 */       if (debug3.is(debug2)) {
/* 53 */         return (BlockPos)debug1;
/*    */       }
/*    */       
/* 56 */       debug1.move(Direction.DOWN);
/*    */     } 
/* 58 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ReplaceBlobsFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */