/*    */ package net.minecraft.world.food;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FoodProperties
/*    */ {
/*    */   private final int nutrition;
/*    */   private final float saturationModifier;
/*    */   private final boolean isMeat;
/*    */   private final boolean canAlwaysEat;
/*    */   private final boolean fastFood;
/*    */   private final List<Pair<MobEffectInstance, Float>> effects;
/*    */   
/*    */   private FoodProperties(int debug1, float debug2, boolean debug3, boolean debug4, boolean debug5, List<Pair<MobEffectInstance, Float>> debug6) {
/* 20 */     this.nutrition = debug1;
/* 21 */     this.saturationModifier = debug2;
/* 22 */     this.isMeat = debug3;
/* 23 */     this.canAlwaysEat = debug4;
/* 24 */     this.fastFood = debug5;
/* 25 */     this.effects = debug6;
/*    */   }
/*    */   
/*    */   public int getNutrition() {
/* 29 */     return this.nutrition;
/*    */   }
/*    */   
/*    */   public float getSaturationModifier() {
/* 33 */     return this.saturationModifier;
/*    */   }
/*    */   
/*    */   public boolean isMeat() {
/* 37 */     return this.isMeat;
/*    */   }
/*    */   
/*    */   public boolean canAlwaysEat() {
/* 41 */     return this.canAlwaysEat;
/*    */   }
/*    */   
/*    */   public boolean isFastFood() {
/* 45 */     return this.fastFood;
/*    */   }
/*    */   
/*    */   public List<Pair<MobEffectInstance, Float>> getEffects() {
/* 49 */     return this.effects;
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */   {
/*    */     private int nutrition;
/*    */     private float saturationModifier;
/*    */     private boolean isMeat;
/*    */     private boolean canAlwaysEat;
/*    */     private boolean fastFood;
/* 59 */     private final List<Pair<MobEffectInstance, Float>> effects = Lists.newArrayList();
/*    */     
/*    */     public Builder nutrition(int debug1) {
/* 62 */       this.nutrition = debug1;
/* 63 */       return this;
/*    */     }
/*    */     
/*    */     public Builder saturationMod(float debug1) {
/* 67 */       this.saturationModifier = debug1;
/* 68 */       return this;
/*    */     }
/*    */     
/*    */     public Builder meat() {
/* 72 */       this.isMeat = true;
/* 73 */       return this;
/*    */     }
/*    */     
/*    */     public Builder alwaysEat() {
/* 77 */       this.canAlwaysEat = true;
/* 78 */       return this;
/*    */     }
/*    */     
/*    */     public Builder fast() {
/* 82 */       this.fastFood = true;
/* 83 */       return this;
/*    */     }
/*    */     
/*    */     public Builder effect(MobEffectInstance debug1, float debug2) {
/* 87 */       this.effects.add(Pair.of(debug1, Float.valueOf(debug2)));
/* 88 */       return this;
/*    */     }
/*    */     
/*    */     public FoodProperties build() {
/* 92 */       return new FoodProperties(this.nutrition, this.saturationModifier, this.isMeat, this.canAlwaysEat, this.fastFood, this.effects);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\food\FoodProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */