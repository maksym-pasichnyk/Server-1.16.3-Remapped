/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class NetherFossilFeature extends StructureFeature<NoneFeatureConfiguration> {
/*    */   public NetherFossilFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
/* 25 */     return FeatureStart::new;
/*    */   }
/*    */   
/*    */   public static class FeatureStart extends BeardedStructureStart<NoneFeatureConfiguration> {
/*    */     public FeatureStart(StructureFeature<NoneFeatureConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 30 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, NoneFeatureConfiguration debug7) {
/* 35 */       ChunkPos debug8 = new ChunkPos(debug4, debug5);
/* 36 */       int debug9 = debug8.getMinBlockX() + this.random.nextInt(16);
/* 37 */       int debug10 = debug8.getMinBlockZ() + this.random.nextInt(16);
/*    */       
/* 39 */       int debug11 = debug2.getSeaLevel();
/*    */       
/* 41 */       int debug12 = debug11 + this.random.nextInt(debug2.getGenDepth() - 2 - debug11);
/*    */       
/* 43 */       BlockGetter debug13 = debug2.getBaseColumn(debug9, debug10);
/*    */       
/* 45 */       BlockPos.MutableBlockPos debug14 = new BlockPos.MutableBlockPos(debug9, debug12, debug10);
/* 46 */       while (debug12 > debug11) {
/* 47 */         BlockState debug15 = debug13.getBlockState((BlockPos)debug14);
/*    */         
/* 49 */         debug14.move(Direction.DOWN);
/*    */         
/* 51 */         BlockState debug16 = debug13.getBlockState((BlockPos)debug14);
/* 52 */         if (debug15.isAir() && (debug16.is(Blocks.SOUL_SAND) || debug16.isFaceSturdy(debug13, (BlockPos)debug14, Direction.UP))) {
/*    */           break;
/*    */         }
/*    */         
/* 56 */         debug12--;
/*    */       } 
/*    */       
/* 59 */       if (debug12 <= debug11) {
/*    */         return;
/*    */       }
/*    */       
/* 63 */       NetherFossilPieces.addPieces(debug3, this.pieces, (Random)this.random, new BlockPos(debug9, debug12, debug10));
/* 64 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\NetherFossilFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */