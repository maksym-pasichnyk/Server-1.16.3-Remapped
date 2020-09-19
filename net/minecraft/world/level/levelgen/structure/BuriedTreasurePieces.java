/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class BuriedTreasurePieces {
/*    */   public static class BuriedTreasurePiece
/*    */     extends StructurePiece {
/*    */     public BuriedTreasurePiece(BlockPos debug1) {
/* 23 */       super(StructurePieceType.BURIED_TREASURE_PIECE, 0);
/* 24 */       this.boundingBox = new BoundingBox(debug1.getX(), debug1.getY(), debug1.getZ(), debug1.getX(), debug1.getY(), debug1.getZ());
/*    */     }
/*    */     
/*    */     public BuriedTreasurePiece(StructureManager debug1, CompoundTag debug2) {
/* 28 */       super(StructurePieceType.BURIED_TREASURE_PIECE, debug2);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void addAdditionalSaveData(CompoundTag debug1) {}
/*    */ 
/*    */     
/*    */     public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/* 37 */       int debug8 = debug1.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.boundingBox.x0, this.boundingBox.z0);
/* 38 */       BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos(this.boundingBox.x0, debug8, this.boundingBox.z0);
/*    */       
/* 40 */       while (debug9.getY() > 0) {
/* 41 */         BlockState debug10 = debug1.getBlockState((BlockPos)debug9);
/* 42 */         BlockState debug11 = debug1.getBlockState(debug9.below());
/*    */         
/* 44 */         if (debug11 == Blocks.SANDSTONE.defaultBlockState() || debug11 == Blocks.STONE
/* 45 */           .defaultBlockState() || debug11 == Blocks.ANDESITE
/* 46 */           .defaultBlockState() || debug11 == Blocks.GRANITE
/* 47 */           .defaultBlockState() || debug11 == Blocks.DIORITE
/* 48 */           .defaultBlockState()) {
/*    */           
/* 50 */           BlockState debug12 = (debug10.isAir() || isLiquid(debug10)) ? Blocks.SAND.defaultBlockState() : debug10;
/*    */           
/* 52 */           for (Direction debug16 : Direction.values()) {
/* 53 */             BlockPos debug17 = debug9.relative(debug16);
/* 54 */             BlockState debug18 = debug1.getBlockState(debug17);
/*    */             
/* 56 */             if (debug18.isAir() || isLiquid(debug18)) {
/* 57 */               BlockPos debug19 = debug17.below();
/* 58 */               BlockState debug20 = debug1.getBlockState(debug19);
/*    */               
/* 60 */               if ((debug20.isAir() || isLiquid(debug20)) && debug16 != Direction.UP) {
/* 61 */                 debug1.setBlock(debug17, debug11, 3);
/*    */               } else {
/* 63 */                 debug1.setBlock(debug17, debug12, 3);
/*    */               } 
/*    */             } 
/*    */           } 
/* 67 */           this.boundingBox = new BoundingBox(debug9.getX(), debug9.getY(), debug9.getZ(), debug9.getX(), debug9.getY(), debug9.getZ());
/* 68 */           return createChest((ServerLevelAccessor)debug1, debug5, debug4, (BlockPos)debug9, BuiltInLootTables.BURIED_TREASURE, null);
/*    */         } 
/*    */         
/* 71 */         debug9.move(0, -1, 0);
/*    */       } 
/* 73 */       return false;
/*    */     }
/*    */     
/*    */     private boolean isLiquid(BlockState debug1) {
/* 77 */       return (debug1 == Blocks.WATER.defaultBlockState() || debug1 == Blocks.LAVA
/* 78 */         .defaultBlockState());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\BuriedTreasurePieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */