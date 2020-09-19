/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class EntityFlagsPredicate
/*     */ {
/*  13 */   public static final EntityFlagsPredicate ANY = (new Builder()).build();
/*     */   
/*     */   @Nullable
/*     */   private final Boolean isOnFire;
/*     */   
/*     */   @Nullable
/*     */   private final Boolean isCrouching;
/*     */   
/*     */   @Nullable
/*     */   private final Boolean isSprinting;
/*     */   
/*     */   @Nullable
/*     */   private final Boolean isSwimming;
/*     */   
/*     */   @Nullable
/*     */   private final Boolean isBaby;
/*     */   
/*     */   public EntityFlagsPredicate(@Nullable Boolean debug1, @Nullable Boolean debug2, @Nullable Boolean debug3, @Nullable Boolean debug4, @Nullable Boolean debug5) {
/*  31 */     this.isOnFire = debug1;
/*  32 */     this.isCrouching = debug2;
/*  33 */     this.isSprinting = debug3;
/*  34 */     this.isSwimming = debug4;
/*  35 */     this.isBaby = debug5;
/*     */   }
/*     */   
/*     */   public boolean matches(Entity debug1) {
/*  39 */     if (this.isOnFire != null && debug1.isOnFire() != this.isOnFire.booleanValue()) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     if (this.isCrouching != null && debug1.isCrouching() != this.isCrouching.booleanValue()) {
/*  44 */       return false;
/*     */     }
/*     */     
/*  47 */     if (this.isSprinting != null && debug1.isSprinting() != this.isSprinting.booleanValue()) {
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     if (this.isSwimming != null && debug1.isSwimming() != this.isSwimming.booleanValue()) {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     if (this.isBaby != null && debug1 instanceof LivingEntity && ((LivingEntity)debug1).isBaby() != this.isBaby.booleanValue()) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Boolean getOptionalBoolean(JsonObject debug0, String debug1) {
/*  64 */     return debug0.has(debug1) ? Boolean.valueOf(GsonHelper.getAsBoolean(debug0, debug1)) : null;
/*     */   }
/*     */   
/*     */   public static EntityFlagsPredicate fromJson(@Nullable JsonElement debug0) {
/*  68 */     if (debug0 == null || debug0.isJsonNull()) {
/*  69 */       return ANY;
/*     */     }
/*     */     
/*  72 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "entity flags");
/*  73 */     Boolean debug2 = getOptionalBoolean(debug1, "is_on_fire");
/*     */     
/*  75 */     Boolean debug3 = getOptionalBoolean(debug1, "is_sneaking");
/*  76 */     Boolean debug4 = getOptionalBoolean(debug1, "is_sprinting");
/*  77 */     Boolean debug5 = getOptionalBoolean(debug1, "is_swimming");
/*  78 */     Boolean debug6 = getOptionalBoolean(debug1, "is_baby");
/*     */     
/*  80 */     return new EntityFlagsPredicate(debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private void addOptionalBoolean(JsonObject debug1, String debug2, @Nullable Boolean debug3) {
/*  84 */     if (debug3 != null) {
/*  85 */       debug1.addProperty(debug2, debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/*  90 */     if (this == ANY) {
/*  91 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/*  94 */     JsonObject debug1 = new JsonObject();
/*  95 */     addOptionalBoolean(debug1, "is_on_fire", this.isOnFire);
/*     */     
/*  97 */     addOptionalBoolean(debug1, "is_sneaking", this.isCrouching);
/*  98 */     addOptionalBoolean(debug1, "is_sprinting", this.isSprinting);
/*  99 */     addOptionalBoolean(debug1, "is_swimming", this.isSwimming);
/* 100 */     addOptionalBoolean(debug1, "is_baby", this.isBaby);
/* 101 */     return (JsonElement)debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     @Nullable
/*     */     private Boolean isOnFire;
/*     */     
/*     */     @Nullable
/*     */     private Boolean isCrouching;
/*     */     
/*     */     @Nullable
/*     */     private Boolean isSprinting;
/*     */     @Nullable
/*     */     private Boolean isSwimming;
/*     */     @Nullable
/*     */     private Boolean isBaby;
/*     */     
/*     */     public static Builder flags() {
/* 121 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder setOnFire(@Nullable Boolean debug1) {
/* 125 */       this.isOnFire = debug1;
/* 126 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setIsBaby(@Nullable Boolean debug1) {
/* 145 */       this.isBaby = debug1;
/* 146 */       return this;
/*     */     }
/*     */     
/*     */     public EntityFlagsPredicate build() {
/* 150 */       return new EntityFlagsPredicate(this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isBaby);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EntityFlagsPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */