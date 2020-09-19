/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockMatchTest extends RuleTest {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Registry.BLOCK.fieldOf("block").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)debug0, RandomBlockMatchTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<RandomBlockMatchTest> CODEC;
/*    */   private final Block block;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockMatchTest(Block debug1, float debug2) {
/* 21 */     this.block = debug1;
/* 22 */     this.probability = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState debug1, Random debug2) {
/* 27 */     return (debug1.is(this.block) && debug2.nextFloat() < this.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 32 */     return RuleTestType.RANDOM_BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\RandomBlockMatchTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */