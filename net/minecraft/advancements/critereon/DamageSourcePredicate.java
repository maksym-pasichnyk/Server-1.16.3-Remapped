/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DamageSourcePredicate
/*     */ {
/*  15 */   public static final DamageSourcePredicate ANY = Builder.damageType().build();
/*     */   
/*     */   private final Boolean isProjectile;
/*     */   private final Boolean isExplosion;
/*     */   private final Boolean bypassesArmor;
/*     */   private final Boolean bypassesInvulnerability;
/*     */   private final Boolean bypassesMagic;
/*     */   private final Boolean isFire;
/*     */   private final Boolean isMagic;
/*     */   private final Boolean isLightning;
/*     */   private final EntityPredicate directEntity;
/*     */   private final EntityPredicate sourceEntity;
/*     */   
/*     */   public DamageSourcePredicate(@Nullable Boolean debug1, @Nullable Boolean debug2, @Nullable Boolean debug3, @Nullable Boolean debug4, @Nullable Boolean debug5, @Nullable Boolean debug6, @Nullable Boolean debug7, @Nullable Boolean debug8, EntityPredicate debug9, EntityPredicate debug10) {
/*  29 */     this.isProjectile = debug1;
/*  30 */     this.isExplosion = debug2;
/*  31 */     this.bypassesArmor = debug3;
/*  32 */     this.bypassesInvulnerability = debug4;
/*  33 */     this.bypassesMagic = debug5;
/*  34 */     this.isFire = debug6;
/*  35 */     this.isMagic = debug7;
/*  36 */     this.isLightning = debug8;
/*  37 */     this.directEntity = debug9;
/*  38 */     this.sourceEntity = debug10;
/*     */   }
/*     */   
/*     */   public boolean matches(ServerPlayer debug1, DamageSource debug2) {
/*  42 */     return matches(debug1.getLevel(), debug1.position(), debug2);
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel debug1, Vec3 debug2, DamageSource debug3) {
/*  46 */     if (this == ANY) {
/*  47 */       return true;
/*     */     }
/*  49 */     if (this.isProjectile != null && this.isProjectile.booleanValue() != debug3.isProjectile()) {
/*  50 */       return false;
/*     */     }
/*  52 */     if (this.isExplosion != null && this.isExplosion.booleanValue() != debug3.isExplosion()) {
/*  53 */       return false;
/*     */     }
/*  55 */     if (this.bypassesArmor != null && this.bypassesArmor.booleanValue() != debug3.isBypassArmor()) {
/*  56 */       return false;
/*     */     }
/*  58 */     if (this.bypassesInvulnerability != null && this.bypassesInvulnerability.booleanValue() != debug3.isBypassInvul()) {
/*  59 */       return false;
/*     */     }
/*  61 */     if (this.bypassesMagic != null && this.bypassesMagic.booleanValue() != debug3.isBypassMagic()) {
/*  62 */       return false;
/*     */     }
/*  64 */     if (this.isFire != null && this.isFire.booleanValue() != debug3.isFire()) {
/*  65 */       return false;
/*     */     }
/*  67 */     if (this.isMagic != null && this.isMagic.booleanValue() != debug3.isMagic()) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (this.isLightning != null && this.isLightning.booleanValue() != ((debug3 == DamageSource.LIGHTNING_BOLT))) {
/*  71 */       return false;
/*     */     }
/*  73 */     if (!this.directEntity.matches(debug1, debug2, debug3.getDirectEntity())) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (!this.sourceEntity.matches(debug1, debug2, debug3.getEntity())) {
/*  77 */       return false;
/*     */     }
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public static DamageSourcePredicate fromJson(@Nullable JsonElement debug0) {
/*  83 */     if (debug0 == null || debug0.isJsonNull()) {
/*  84 */       return ANY;
/*     */     }
/*  86 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "damage type");
/*  87 */     Boolean debug2 = getOptionalBoolean(debug1, "is_projectile");
/*  88 */     Boolean debug3 = getOptionalBoolean(debug1, "is_explosion");
/*  89 */     Boolean debug4 = getOptionalBoolean(debug1, "bypasses_armor");
/*  90 */     Boolean debug5 = getOptionalBoolean(debug1, "bypasses_invulnerability");
/*  91 */     Boolean debug6 = getOptionalBoolean(debug1, "bypasses_magic");
/*  92 */     Boolean debug7 = getOptionalBoolean(debug1, "is_fire");
/*  93 */     Boolean debug8 = getOptionalBoolean(debug1, "is_magic");
/*  94 */     Boolean debug9 = getOptionalBoolean(debug1, "is_lightning");
/*  95 */     EntityPredicate debug10 = EntityPredicate.fromJson(debug1.get("direct_entity"));
/*  96 */     EntityPredicate debug11 = EntityPredicate.fromJson(debug1.get("source_entity"));
/*  97 */     return new DamageSourcePredicate(debug2, debug3, debug4, debug5, debug6, debug7, debug8, debug9, debug10, debug11);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Boolean getOptionalBoolean(JsonObject debug0, String debug1) {
/* 102 */     return debug0.has(debug1) ? Boolean.valueOf(GsonHelper.getAsBoolean(debug0, debug1)) : null;
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 106 */     if (this == ANY) {
/* 107 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 110 */     JsonObject debug1 = new JsonObject();
/*     */     
/* 112 */     addOptionally(debug1, "is_projectile", this.isProjectile);
/* 113 */     addOptionally(debug1, "is_explosion", this.isExplosion);
/* 114 */     addOptionally(debug1, "bypasses_armor", this.bypassesArmor);
/* 115 */     addOptionally(debug1, "bypasses_invulnerability", this.bypassesInvulnerability);
/* 116 */     addOptionally(debug1, "bypasses_magic", this.bypassesMagic);
/* 117 */     addOptionally(debug1, "is_fire", this.isFire);
/* 118 */     addOptionally(debug1, "is_magic", this.isMagic);
/* 119 */     addOptionally(debug1, "is_lightning", this.isLightning);
/* 120 */     debug1.add("direct_entity", this.directEntity.serializeToJson());
/* 121 */     debug1.add("source_entity", this.sourceEntity.serializeToJson());
/*     */     
/* 123 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   private void addOptionally(JsonObject debug1, String debug2, @Nullable Boolean debug3) {
/* 127 */     if (debug3 != null)
/* 128 */       debug1.addProperty(debug2, debug3); 
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private Boolean isProjectile;
/*     */     private Boolean isExplosion;
/*     */     private Boolean bypassesArmor;
/*     */     private Boolean bypassesInvulnerability;
/*     */     private Boolean bypassesMagic;
/*     */     private Boolean isFire;
/*     */     private Boolean isMagic;
/*     */     private Boolean isLightning;
/* 141 */     private EntityPredicate directEntity = EntityPredicate.ANY;
/* 142 */     private EntityPredicate sourceEntity = EntityPredicate.ANY;
/*     */     
/*     */     public static Builder damageType() {
/* 145 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder isProjectile(Boolean debug1) {
/* 149 */       this.isProjectile = debug1;
/* 150 */       return this;
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
/*     */     public Builder isLightning(Boolean debug1) {
/* 184 */       this.isLightning = debug1;
/* 185 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder direct(EntityPredicate.Builder debug1) {
/* 194 */       this.directEntity = debug1.build();
/* 195 */       return this;
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
/*     */     public DamageSourcePredicate build() {
/* 209 */       return new DamageSourcePredicate(this.isProjectile, this.isExplosion, this.bypassesArmor, this.bypassesInvulnerability, this.bypassesMagic, this.isFire, this.isMagic, this.isLightning, this.directEntity, this.sourceEntity);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\DamageSourcePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */