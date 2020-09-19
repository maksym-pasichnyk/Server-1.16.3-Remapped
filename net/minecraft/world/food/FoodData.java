/*     */ package net.minecraft.world.food;
/*     */ 
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FoodData
/*     */ {
/*  21 */   private int foodLevel = 20;
/*  22 */   private int lastFoodLevel = 20;
/*  23 */   private float saturationLevel = 5.0F;
/*     */   private float exhaustionLevel;
/*     */   
/*     */   public void eat(int debug1, float debug2) {
/*  27 */     this.foodLevel = Math.min(debug1 + this.foodLevel, 20);
/*  28 */     this.saturationLevel = Math.min(this.saturationLevel + debug1 * debug2 * 2.0F, this.foodLevel);
/*     */   }
/*     */   private int tickTimer;
/*     */   public void eat(Item debug1, ItemStack debug2) {
/*  32 */     if (debug1.isEdible()) {
/*  33 */       FoodProperties debug3 = debug1.getFoodProperties();
/*  34 */       eat(debug3.getNutrition(), debug3.getSaturationModifier());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick(Player debug1) {
/*  39 */     Difficulty debug2 = debug1.level.getDifficulty();
/*     */     
/*  41 */     this.lastFoodLevel = this.foodLevel;
/*     */     
/*  43 */     if (this.exhaustionLevel > 4.0F) {
/*  44 */       this.exhaustionLevel -= 4.0F;
/*     */       
/*  46 */       if (this.saturationLevel > 0.0F) {
/*  47 */         this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
/*  48 */       } else if (debug2 != Difficulty.PEACEFUL) {
/*  49 */         this.foodLevel = Math.max(this.foodLevel - 1, 0);
/*     */       } 
/*     */     } 
/*     */     
/*  53 */     boolean debug3 = debug1.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
/*  54 */     if (debug3 && this.saturationLevel > 0.0F && debug1.isHurt() && this.foodLevel >= 20) {
/*  55 */       this.tickTimer++;
/*  56 */       if (this.tickTimer >= 10) {
/*  57 */         float debug4 = Math.min(this.saturationLevel, 6.0F);
/*  58 */         debug1.heal(debug4 / 6.0F);
/*  59 */         addExhaustion(debug4);
/*  60 */         this.tickTimer = 0;
/*     */       } 
/*  62 */     } else if (debug3 && this.foodLevel >= 18 && debug1.isHurt()) {
/*  63 */       this.tickTimer++;
/*  64 */       if (this.tickTimer >= 80) {
/*  65 */         debug1.heal(1.0F);
/*  66 */         addExhaustion(6.0F);
/*  67 */         this.tickTimer = 0;
/*     */       } 
/*  69 */     } else if (this.foodLevel <= 0) {
/*  70 */       this.tickTimer++;
/*  71 */       if (this.tickTimer >= 80) {
/*  72 */         if (debug1.getHealth() > 10.0F || debug2 == Difficulty.HARD || (debug1.getHealth() > 1.0F && debug2 == Difficulty.NORMAL)) {
/*  73 */           debug1.hurt(DamageSource.STARVE, 1.0F);
/*     */         }
/*  75 */         this.tickTimer = 0;
/*     */       } 
/*     */     } else {
/*  78 */       this.tickTimer = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  83 */     if (debug1.contains("foodLevel", 99)) {
/*  84 */       this.foodLevel = debug1.getInt("foodLevel");
/*  85 */       this.tickTimer = debug1.getInt("foodTickTimer");
/*  86 */       this.saturationLevel = debug1.getFloat("foodSaturationLevel");
/*  87 */       this.exhaustionLevel = debug1.getFloat("foodExhaustionLevel");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  92 */     debug1.putInt("foodLevel", this.foodLevel);
/*  93 */     debug1.putInt("foodTickTimer", this.tickTimer);
/*  94 */     debug1.putFloat("foodSaturationLevel", this.saturationLevel);
/*  95 */     debug1.putFloat("foodExhaustionLevel", this.exhaustionLevel);
/*     */   }
/*     */   
/*     */   public int getFoodLevel() {
/*  99 */     return this.foodLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean needsFood() {
/* 107 */     return (this.foodLevel < 20);
/*     */   }
/*     */   
/*     */   public void addExhaustion(float debug1) {
/* 111 */     this.exhaustionLevel = Math.min(this.exhaustionLevel + debug1, 40.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSaturationLevel() {
/* 119 */     return this.saturationLevel;
/*     */   }
/*     */   
/*     */   public void setFoodLevel(int debug1) {
/* 123 */     this.foodLevel = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\food\FoodData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */