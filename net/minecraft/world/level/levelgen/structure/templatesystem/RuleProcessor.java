/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RuleProcessor extends StructureProcessor {
/*    */   public static final Codec<RuleProcessor> CODEC;
/*    */   
/*    */   static {
/* 15 */     CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(RuleProcessor::new, debug0 -> debug0.rules).codec();
/*    */   }
/*    */   private final ImmutableList<ProcessorRule> rules;
/*    */   
/*    */   public RuleProcessor(List<? extends ProcessorRule> debug1) {
/* 20 */     this.rules = ImmutableList.copyOf(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/* 26 */     Random debug7 = new Random(Mth.getSeed((Vec3i)debug5.pos));
/* 27 */     BlockState debug8 = debug1.getBlockState(debug5.pos);
/* 28 */     for (UnmodifiableIterator<ProcessorRule> unmodifiableIterator = this.rules.iterator(); unmodifiableIterator.hasNext(); ) { ProcessorRule debug10 = unmodifiableIterator.next();
/* 29 */       if (debug10.test(debug5.state, debug8, debug4.pos, debug5.pos, debug3, debug7)) {
/* 30 */         return new StructureTemplate.StructureBlockInfo(debug5.pos, debug10.getOutputState(), debug10.getOutputTag());
/*    */       } }
/*    */     
/* 33 */     return debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 38 */     return StructureProcessorType.RULE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RuleProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */