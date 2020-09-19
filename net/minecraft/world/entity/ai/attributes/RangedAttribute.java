/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class RangedAttribute extends Attribute {
/*    */   private final double minValue;
/*    */   private final double maxValue;
/*    */   
/*    */   public RangedAttribute(String debug1, double debug2, double debug4, double debug6) {
/* 10 */     super(debug1, debug2);
/* 11 */     this.minValue = debug4;
/* 12 */     this.maxValue = debug6;
/*    */     
/* 14 */     if (debug4 > debug6) {
/* 15 */       throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
/*    */     }
/* 17 */     if (debug2 < debug4) {
/* 18 */       throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
/*    */     }
/* 20 */     if (debug2 > debug6) {
/* 21 */       throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double sanitizeValue(double debug1) {
/* 35 */     debug1 = Mth.clamp(debug1, this.minValue, this.maxValue);
/*    */     
/* 37 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\attributes\RangedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */