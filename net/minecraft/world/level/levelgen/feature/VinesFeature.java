/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VinesFeature extends Feature<NoneFeatureConfiguration> {
/* 17 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   
/*    */   public VinesFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 25 */     BlockPos.MutableBlockPos debug6 = debug4.mutable();
/* 26 */     for (int debug7 = 64; debug7 < 256; debug7++) {
/* 27 */       debug6.set((Vec3i)debug4);
/* 28 */       debug6.move(debug3.nextInt(4) - debug3.nextInt(4), 0, debug3.nextInt(4) - debug3.nextInt(4));
/* 29 */       debug6.setY(debug7);
/*    */       
/* 31 */       if (debug1.isEmptyBlock((BlockPos)debug6))
/*    */       {
/*    */ 
/*    */         
/* 35 */         for (Direction debug11 : DIRECTIONS) {
/* 36 */           if (debug11 != Direction.DOWN)
/*    */           {
/*    */ 
/*    */             
/* 40 */             if (VineBlock.isAcceptableNeighbour((BlockGetter)debug1, (BlockPos)debug6, debug11)) {
/* 41 */               debug1.setBlock((BlockPos)debug6, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.getPropertyForFace(debug11), Boolean.valueOf(true)), 2);
/*    */               break;
/*    */             }  } 
/*    */         }  } 
/*    */     } 
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\VinesFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */