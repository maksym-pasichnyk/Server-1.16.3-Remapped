/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class BlockRotProcessor extends StructureProcessor {
/*    */   public static final Codec<BlockRotProcessor> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = Codec.FLOAT.fieldOf("integrity").orElse(Float.valueOf(1.0F)).xmap(BlockRotProcessor::new, debug0 -> Float.valueOf(debug0.integrity)).codec();
/*    */   }
/*    */   private final float integrity;
/*    */   
/*    */   public BlockRotProcessor(float debug1) {
/* 16 */     this.integrity = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 22 */     Random debug7 = debug6.getRandom(debug5.pos);
/*    */     
/* 24 */     if (this.integrity >= 1.0F || debug7.nextFloat() <= this.integrity) {
/* 25 */       return debug5;
/*    */     }
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 32 */     return StructureProcessorType.BLOCK_ROT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockRotProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */