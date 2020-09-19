/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.concurrent.Immutable;
/*    */ import net.minecraft.util.Mth;
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
/*    */ @Immutable
/*    */ public class DifficultyInstance
/*    */ {
/*    */   private final Difficulty base;
/*    */   private final float effectiveDifficulty;
/*    */   
/*    */   public DifficultyInstance(Difficulty debug1, long debug2, long debug4, float debug6) {
/* 22 */     this.base = debug1;
/* 23 */     this.effectiveDifficulty = calculateDifficulty(debug1, debug2, debug4, debug6);
/*    */   }
/*    */   
/*    */   public Difficulty getDifficulty() {
/* 27 */     return this.base;
/*    */   }
/*    */   
/*    */   public float getEffectiveDifficulty() {
/* 31 */     return this.effectiveDifficulty;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isHarderThan(float debug1) {
/* 39 */     return (this.effectiveDifficulty > debug1);
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
/*    */   public float getSpecialMultiplier() {
/* 52 */     if (this.effectiveDifficulty < 2.0F) {
/* 53 */       return 0.0F;
/*    */     }
/* 55 */     if (this.effectiveDifficulty > 4.0F) {
/* 56 */       return 1.0F;
/*    */     }
/* 58 */     return (this.effectiveDifficulty - 2.0F) / 2.0F;
/*    */   }
/*    */   
/*    */   private float calculateDifficulty(Difficulty debug1, long debug2, long debug4, float debug6) {
/* 62 */     if (debug1 == Difficulty.PEACEFUL) {
/* 63 */       return 0.0F;
/*    */     }
/*    */     
/* 66 */     boolean debug7 = (debug1 == Difficulty.HARD);
/* 67 */     float debug8 = 0.75F;
/*    */ 
/*    */     
/* 70 */     float debug9 = Mth.clamp(((float)debug2 + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
/* 71 */     debug8 += debug9;
/*    */     
/* 73 */     float debug10 = 0.0F;
/*    */ 
/*    */     
/* 76 */     debug10 += Mth.clamp((float)debug4 / 3600000.0F, 0.0F, 1.0F) * (debug7 ? 1.0F : 0.75F);
/* 77 */     debug10 += Mth.clamp(debug6 * 0.25F, 0.0F, debug9);
/*    */     
/* 79 */     if (debug1 == Difficulty.EASY) {
/* 80 */       debug10 *= 0.5F;
/*    */     }
/* 82 */     debug8 += debug10;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 87 */     return debug1.getId() * debug8;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\DifficultyInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */