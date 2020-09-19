/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ProcessorRule {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)RuleTest.CODEC.fieldOf("input_predicate").forGetter(()), (App)RuleTest.CODEC.fieldOf("location_predicate").forGetter(()), (App)PosRuleTest.CODEC.optionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(()), (App)BlockState.CODEC.fieldOf("output_state").forGetter(()), (App)CompoundTag.CODEC.optionalFieldOf("output_nbt").forGetter(())).apply((Applicative)debug0, ProcessorRule::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<ProcessorRule> CODEC;
/*    */ 
/*    */   
/*    */   private final RuleTest inputPredicate;
/*    */   
/*    */   private final RuleTest locPredicate;
/*    */   
/*    */   private final PosRuleTest posPredicate;
/*    */   
/*    */   private final BlockState outputState;
/*    */   
/*    */   @Nullable
/*    */   private final CompoundTag outputTag;
/*    */ 
/*    */   
/*    */   public ProcessorRule(RuleTest debug1, RuleTest debug2, BlockState debug3) {
/* 38 */     this(debug1, debug2, PosAlwaysTrueTest.INSTANCE, debug3, Optional.empty());
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest debug1, RuleTest debug2, PosRuleTest debug3, BlockState debug4) {
/* 42 */     this(debug1, debug2, debug3, debug4, Optional.empty());
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest debug1, RuleTest debug2, PosRuleTest debug3, BlockState debug4, Optional<CompoundTag> debug5) {
/* 46 */     this.inputPredicate = debug1;
/* 47 */     this.locPredicate = debug2;
/* 48 */     this.posPredicate = debug3;
/* 49 */     this.outputState = debug4;
/* 50 */     this.outputTag = debug5.orElse(null);
/*    */   }
/*    */   
/*    */   public boolean test(BlockState debug1, BlockState debug2, BlockPos debug3, BlockPos debug4, BlockPos debug5, Random debug6) {
/* 54 */     return (this.inputPredicate.test(debug1, debug6) && this.locPredicate.test(debug2, debug6) && this.posPredicate.test(debug3, debug4, debug5, debug6));
/*    */   }
/*    */   
/*    */   public BlockState getOutputState() {
/* 58 */     return this.outputState;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public CompoundTag getOutputTag() {
/* 63 */     return this.outputTag;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\ProcessorRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */