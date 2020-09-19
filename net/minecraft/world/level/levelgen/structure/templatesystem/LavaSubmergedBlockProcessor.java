/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LavaSubmergedBlockProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 16 */   public static final Codec<LavaSubmergedBlockProcessor> CODEC = Codec.unit(() -> INSTANCE);
/* 17 */   public static final LavaSubmergedBlockProcessor INSTANCE = new LavaSubmergedBlockProcessor();
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 22 */     BlockPos debug7 = debug5.pos;
/* 23 */     boolean debug8 = debug1.getBlockState(debug7).is(Blocks.LAVA);
/* 24 */     if (debug8 && !Block.isShapeFullBlock(debug5.state.getShape((BlockGetter)debug1, debug7))) {
/* 25 */       return new StructureTemplate.StructureBlockInfo(debug7, Blocks.LAVA.defaultBlockState(), debug5.nbt);
/*    */     }
/* 27 */     return debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 32 */     return StructureProcessorType.LAVA_SUBMERGED_BLOCK;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LavaSubmergedBlockProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */