/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public class JigsawReplacementProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 17 */   public static final Codec<JigsawReplacementProcessor> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 19 */   public static final JigsawReplacementProcessor INSTANCE = new JigsawReplacementProcessor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 27 */     BlockState debug7 = debug5.state;
/* 28 */     if (debug7.is(Blocks.JIGSAW)) {
/*    */ 
/*    */ 
/*    */       
/* 32 */       String debug8 = debug5.nbt.getString("final_state");
/* 33 */       BlockStateParser debug9 = new BlockStateParser(new StringReader(debug8), false);
/*    */       try {
/* 35 */         debug9.parse(true);
/* 36 */       } catch (CommandSyntaxException debug10) {
/* 37 */         throw new RuntimeException(debug10);
/*    */       } 
/* 39 */       if (debug9.getState().is(Blocks.STRUCTURE_VOID)) {
/* 40 */         return null;
/*    */       }
/* 42 */       return new StructureTemplate.StructureBlockInfo(debug5.pos, debug9.getState(), null);
/*    */     } 
/*    */     return debug5;
/*    */   }
/*    */   protected StructureProcessorType<?> getType() {
/* 47 */     return StructureProcessorType.JIGSAW_REPLACEMENT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\JigsawReplacementProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */