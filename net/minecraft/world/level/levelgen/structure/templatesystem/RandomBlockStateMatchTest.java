/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockStateMatchTest extends RuleTest {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("block_state").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)debug0, RandomBlockStateMatchTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RandomBlockStateMatchTest> CODEC;
/*    */   private final BlockState blockState;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockStateMatchTest(BlockState debug1, float debug2) {
/* 19 */     this.blockState = debug1;
/* 20 */     this.probability = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState debug1, Random debug2) {
/* 25 */     return (debug1 == this.blockState && debug2.nextFloat() < this.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 30 */     return RuleTestType.RANDOM_BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockStateMatchTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */