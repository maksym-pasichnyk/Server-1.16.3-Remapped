/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class LinearPosTest extends PosRuleTest {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.FLOAT.fieldOf("max_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.fieldOf("min_dist").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("max_dist").orElse(Integer.valueOf(0)).forGetter(())).apply((Applicative)debug0, LinearPosTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<LinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   
/*    */   public LinearPosTest(float debug1, float debug2, int debug3, int debug4) {
/* 24 */     if (debug3 >= debug4) {
/* 25 */       throw new IllegalArgumentException("Invalid range: [" + debug3 + "," + debug4 + "]");
/*    */     }
/*    */     
/* 28 */     this.minChance = debug1;
/* 29 */     this.maxChance = debug2;
/* 30 */     this.minDist = debug3;
/* 31 */     this.maxDist = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos debug1, BlockPos debug2, BlockPos debug3, Random debug4) {
/* 36 */     int debug5 = debug2.distManhattan((Vec3i)debug3);
/*    */     
/* 38 */     float debug6 = debug4.nextFloat();
/* 39 */     return (debug6 <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp(debug5, this.minDist, this.maxDist)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 44 */     return PosRuleTestType.LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LinearPosTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */