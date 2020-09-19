/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class AxisAlignedLinearPosTest extends PosRuleTest {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.FLOAT.fieldOf("max_chance").orElse(Float.valueOf(0.0F)).forGetter(()), (App)Codec.INT.fieldOf("min_dist").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("max_dist").orElse(Integer.valueOf(0)).forGetter(()), (App)Direction.Axis.CODEC.fieldOf("axis").orElse(Direction.Axis.Y).forGetter(())).apply((Applicative)debug0, AxisAlignedLinearPosTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<AxisAlignedLinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   private final Direction.Axis axis;
/*    */   
/*    */   public AxisAlignedLinearPosTest(float debug1, float debug2, int debug3, int debug4, Direction.Axis debug5) {
/* 27 */     if (debug3 >= debug4) {
/* 28 */       throw new IllegalArgumentException("Invalid range: [" + debug3 + "," + debug4 + "]");
/*    */     }
/* 30 */     this.minChance = debug1;
/* 31 */     this.maxChance = debug2;
/* 32 */     this.minDist = debug3;
/* 33 */     this.maxDist = debug4;
/* 34 */     this.axis = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos debug1, BlockPos debug2, BlockPos debug3, Random debug4) {
/* 39 */     Direction debug5 = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
/* 40 */     float debug6 = Math.abs((debug2.getX() - debug3.getX()) * debug5.getStepX());
/* 41 */     float debug7 = Math.abs((debug2.getY() - debug3.getY()) * debug5.getStepY());
/* 42 */     float debug8 = Math.abs((debug2.getZ() - debug3.getZ()) * debug5.getStepZ());
/* 43 */     int debug9 = (int)(debug6 + debug7 + debug8);
/*    */     
/* 45 */     float debug10 = debug4.nextFloat();
/* 46 */     return (debug10 <= Mth.clampedLerp(this.minChance, this.maxChance, Mth.inverseLerp(debug9, this.minDist, this.maxDist)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 51 */     return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AxisAlignedLinearPosTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */