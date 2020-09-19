/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.WallTorchBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndPodiumFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 19 */   public static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO;
/*    */   
/*    */   private final boolean active;
/*    */   
/*    */   public EndPodiumFeature(boolean debug1) {
/* 24 */     super(NoneFeatureConfiguration.CODEC);
/* 25 */     this.active = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 30 */     for (BlockPos debug7 : BlockPos.betweenClosed(new BlockPos(debug4.getX() - 4, debug4.getY() - 1, debug4.getZ() - 4), new BlockPos(debug4.getX() + 4, debug4.getY() + 32, debug4.getZ() + 4))) {
/* 31 */       boolean debug8 = debug7.closerThan((Vec3i)debug4, 2.5D);
/*    */       
/* 33 */       if (debug8 || debug7.closerThan((Vec3i)debug4, 3.5D)) {
/* 34 */         if (debug7.getY() < debug4.getY()) {
/* 35 */           if (debug8) {
/*    */             
/* 37 */             setBlock((LevelWriter)debug1, debug7, Blocks.BEDROCK.defaultBlockState()); continue;
/* 38 */           }  if (debug7.getY() < debug4.getY())
/*    */           {
/* 40 */             setBlock((LevelWriter)debug1, debug7, Blocks.END_STONE.defaultBlockState()); }  continue;
/*    */         } 
/* 42 */         if (debug7.getY() > debug4.getY()) {
/*    */           
/* 44 */           setBlock((LevelWriter)debug1, debug7, Blocks.AIR.defaultBlockState()); continue;
/* 45 */         }  if (!debug8) {
/*    */           
/* 47 */           setBlock((LevelWriter)debug1, debug7, Blocks.BEDROCK.defaultBlockState()); continue;
/* 48 */         }  if (this.active) {
/*    */           
/* 50 */           setBlock((LevelWriter)debug1, new BlockPos((Vec3i)debug7), Blocks.END_PORTAL.defaultBlockState()); continue;
/*    */         } 
/* 52 */         setBlock((LevelWriter)debug1, new BlockPos((Vec3i)debug7), Blocks.AIR.defaultBlockState());
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 58 */     for (int i = 0; i < 4; i++) {
/* 59 */       setBlock((LevelWriter)debug1, debug4.above(i), Blocks.BEDROCK.defaultBlockState());
/*    */     }
/*    */     
/* 62 */     BlockPos debug6 = debug4.above(2);
/* 63 */     for (Direction debug8 : Direction.Plane.HORIZONTAL) {
/* 64 */       setBlock((LevelWriter)debug1, debug6.relative(debug8), (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)debug8));
/*    */     }
/*    */     
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndPodiumFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */