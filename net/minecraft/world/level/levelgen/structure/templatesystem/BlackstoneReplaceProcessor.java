/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SlabBlock;
/*    */ import net.minecraft.world.level.block.StairBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlackstoneReplaceProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 20 */   public static final Codec<BlackstoneReplaceProcessor> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final BlackstoneReplaceProcessor INSTANCE = new BlackstoneReplaceProcessor();
/*    */   
/*    */   private final Map<Block, Block> replacements;
/*    */   
/*    */   private BlackstoneReplaceProcessor() {
/* 27 */     this.replacements = (Map<Block, Block>)Util.make(Maps.newHashMap(), debug0 -> {
/*    */           debug0.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
/*    */           debug0.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
/*    */           debug0.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
/*    */           debug0.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           debug0.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           debug0.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           debug0.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           debug0.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
/*    */           debug0.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           debug0.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           debug0.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           debug0.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           debug0.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           debug0.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           debug0.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           debug0.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           debug0.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           debug0.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           debug0.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           debug0.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           debug0.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
/*    */           debug0.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*    */           debug0.put(Blocks.IRON_BARS, Blocks.CHAIN);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 67 */     Block debug7 = this.replacements.get(debug5.state.getBlock());
/* 68 */     if (debug7 == null) {
/* 69 */       return debug5;
/*    */     }
/* 71 */     BlockState debug8 = debug5.state;
/* 72 */     BlockState debug9 = debug7.defaultBlockState();
/* 73 */     if (debug8.hasProperty((Property)StairBlock.FACING)) {
/* 74 */       debug9 = (BlockState)debug9.setValue((Property)StairBlock.FACING, debug8.getValue((Property)StairBlock.FACING));
/*    */     }
/* 76 */     if (debug8.hasProperty((Property)StairBlock.HALF)) {
/* 77 */       debug9 = (BlockState)debug9.setValue((Property)StairBlock.HALF, debug8.getValue((Property)StairBlock.HALF));
/*    */     }
/* 79 */     if (debug8.hasProperty((Property)SlabBlock.TYPE)) {
/* 80 */       debug9 = (BlockState)debug9.setValue((Property)SlabBlock.TYPE, debug8.getValue((Property)SlabBlock.TYPE));
/*    */     }
/* 82 */     return new StructureTemplate.StructureBlockInfo(debug5.pos, debug9, debug5.nbt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 87 */     return StructureProcessorType.BLACKSTONE_REPLACE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlackstoneReplaceProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */