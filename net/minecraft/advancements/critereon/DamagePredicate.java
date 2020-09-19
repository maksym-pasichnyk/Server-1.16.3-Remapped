/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ 
/*     */ public class DamagePredicate
/*     */ {
/*  13 */   public static final DamagePredicate ANY = Builder.damageInstance().build();
/*     */   
/*     */   private final MinMaxBounds.Floats dealtDamage;
/*     */   private final MinMaxBounds.Floats takenDamage;
/*     */   private final EntityPredicate sourceEntity;
/*     */   private final Boolean blocked;
/*     */   private final DamageSourcePredicate type;
/*     */   
/*     */   public DamagePredicate() {
/*  22 */     this.dealtDamage = MinMaxBounds.Floats.ANY;
/*  23 */     this.takenDamage = MinMaxBounds.Floats.ANY;
/*  24 */     this.sourceEntity = EntityPredicate.ANY;
/*  25 */     this.blocked = null;
/*  26 */     this.type = DamageSourcePredicate.ANY;
/*     */   }
/*     */   
/*     */   public DamagePredicate(MinMaxBounds.Floats debug1, MinMaxBounds.Floats debug2, EntityPredicate debug3, @Nullable Boolean debug4, DamageSourcePredicate debug5) {
/*  30 */     this.dealtDamage = debug1;
/*  31 */     this.takenDamage = debug2;
/*  32 */     this.sourceEntity = debug3;
/*  33 */     this.blocked = debug4;
/*  34 */     this.type = debug5;
/*     */   }
/*     */   
/*     */   public boolean matches(ServerPlayer debug1, DamageSource debug2, float debug3, float debug4, boolean debug5) {
/*  38 */     if (this == ANY) {
/*  39 */       return true;
/*     */     }
/*  41 */     if (!this.dealtDamage.matches(debug3)) {
/*  42 */       return false;
/*     */     }
/*  44 */     if (!this.takenDamage.matches(debug4)) {
/*  45 */       return false;
/*     */     }
/*  47 */     if (!this.sourceEntity.matches(debug1, debug2.getEntity())) {
/*  48 */       return false;
/*     */     }
/*  50 */     if (this.blocked != null && this.blocked.booleanValue() != debug5) {
/*  51 */       return false;
/*     */     }
/*  53 */     if (!this.type.matches(debug1, debug2)) {
/*  54 */       return false;
/*     */     }
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   public static DamagePredicate fromJson(@Nullable JsonElement debug0) {
/*  60 */     if (debug0 == null || debug0.isJsonNull()) {
/*  61 */       return ANY;
/*     */     }
/*  63 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "damage");
/*  64 */     MinMaxBounds.Floats debug2 = MinMaxBounds.Floats.fromJson(debug1.get("dealt"));
/*  65 */     MinMaxBounds.Floats debug3 = MinMaxBounds.Floats.fromJson(debug1.get("taken"));
/*  66 */     Boolean debug4 = debug1.has("blocked") ? Boolean.valueOf(GsonHelper.getAsBoolean(debug1, "blocked")) : null;
/*  67 */     EntityPredicate debug5 = EntityPredicate.fromJson(debug1.get("source_entity"));
/*  68 */     DamageSourcePredicate debug6 = DamageSourcePredicate.fromJson(debug1.get("type"));
/*  69 */     return new DamagePredicate(debug2, debug3, debug5, debug4, debug6);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/*  73 */     if (this == ANY) {
/*  74 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/*  77 */     JsonObject debug1 = new JsonObject();
/*     */     
/*  79 */     debug1.add("dealt", this.dealtDamage.serializeToJson());
/*  80 */     debug1.add("taken", this.takenDamage.serializeToJson());
/*  81 */     debug1.add("source_entity", this.sourceEntity.serializeToJson());
/*  82 */     debug1.add("type", this.type.serializeToJson());
/*     */     
/*  84 */     if (this.blocked != null) {
/*  85 */       debug1.addProperty("blocked", this.blocked);
/*     */     }
/*     */     
/*  88 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  92 */     private MinMaxBounds.Floats dealtDamage = MinMaxBounds.Floats.ANY;
/*  93 */     private MinMaxBounds.Floats takenDamage = MinMaxBounds.Floats.ANY;
/*  94 */     private EntityPredicate sourceEntity = EntityPredicate.ANY;
/*     */     private Boolean blocked;
/*  96 */     private DamageSourcePredicate type = DamageSourcePredicate.ANY;
/*     */     
/*     */     public static Builder damageInstance() {
/*  99 */       return new Builder();
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
/*     */     public Builder blocked(Boolean debug1) {
/* 118 */       this.blocked = debug1;
/* 119 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder type(DamageSourcePredicate.Builder debug1) {
/* 128 */       this.type = debug1.build();
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public DamagePredicate build() {
/* 133 */       return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\DamagePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */