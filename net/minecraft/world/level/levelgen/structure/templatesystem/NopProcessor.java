/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class NopProcessor
/*    */   extends StructureProcessor {
/* 10 */   public static final Codec<NopProcessor> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 12 */   public static final NopProcessor INSTANCE = new NopProcessor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 20 */     return debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 25 */     return StructureProcessorType.NOP;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\NopProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */