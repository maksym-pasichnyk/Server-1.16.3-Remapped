/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class EntityEquipmentPredicate {
/*  16 */   public static final EntityEquipmentPredicate ANY = new EntityEquipmentPredicate(ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
/*  17 */   public static final EntityEquipmentPredicate CAPTAIN = new EntityEquipmentPredicate(ItemPredicate.Builder.item().of((ItemLike)Items.WHITE_BANNER).hasNbt(Raid.getLeaderBannerInstance().getTag()).build(), ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
/*     */   
/*     */   private final ItemPredicate head;
/*     */   private final ItemPredicate chest;
/*     */   private final ItemPredicate legs;
/*     */   private final ItemPredicate feet;
/*     */   private final ItemPredicate mainhand;
/*     */   private final ItemPredicate offhand;
/*     */   
/*     */   public EntityEquipmentPredicate(ItemPredicate debug1, ItemPredicate debug2, ItemPredicate debug3, ItemPredicate debug4, ItemPredicate debug5, ItemPredicate debug6) {
/*  27 */     this.head = debug1;
/*  28 */     this.chest = debug2;
/*  29 */     this.legs = debug3;
/*  30 */     this.feet = debug4;
/*  31 */     this.mainhand = debug5;
/*  32 */     this.offhand = debug6;
/*     */   }
/*     */   
/*     */   public boolean matches(@Nullable Entity debug1) {
/*  36 */     if (this == ANY) {
/*  37 */       return true;
/*     */     }
/*  39 */     if (!(debug1 instanceof LivingEntity)) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     LivingEntity debug2 = (LivingEntity)debug1;
/*  44 */     if (!this.head.matches(debug2.getItemBySlot(EquipmentSlot.HEAD))) {
/*  45 */       return false;
/*     */     }
/*  47 */     if (!this.chest.matches(debug2.getItemBySlot(EquipmentSlot.CHEST))) {
/*  48 */       return false;
/*     */     }
/*  50 */     if (!this.legs.matches(debug2.getItemBySlot(EquipmentSlot.LEGS))) {
/*  51 */       return false;
/*     */     }
/*  53 */     if (!this.feet.matches(debug2.getItemBySlot(EquipmentSlot.FEET))) {
/*  54 */       return false;
/*     */     }
/*  56 */     if (!this.mainhand.matches(debug2.getItemBySlot(EquipmentSlot.MAINHAND))) {
/*  57 */       return false;
/*     */     }
/*  59 */     if (!this.offhand.matches(debug2.getItemBySlot(EquipmentSlot.OFFHAND))) {
/*  60 */       return false;
/*     */     }
/*     */     
/*  63 */     return true;
/*     */   }
/*     */   
/*     */   public static EntityEquipmentPredicate fromJson(@Nullable JsonElement debug0) {
/*  67 */     if (debug0 == null || debug0.isJsonNull()) {
/*  68 */       return ANY;
/*     */     }
/*     */     
/*  71 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "equipment");
/*  72 */     ItemPredicate debug2 = ItemPredicate.fromJson(debug1.get("head"));
/*  73 */     ItemPredicate debug3 = ItemPredicate.fromJson(debug1.get("chest"));
/*  74 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("legs"));
/*  75 */     ItemPredicate debug5 = ItemPredicate.fromJson(debug1.get("feet"));
/*  76 */     ItemPredicate debug6 = ItemPredicate.fromJson(debug1.get("mainhand"));
/*  77 */     ItemPredicate debug7 = ItemPredicate.fromJson(debug1.get("offhand"));
/*  78 */     return new EntityEquipmentPredicate(debug2, debug3, debug4, debug5, debug6, debug7);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/*  82 */     if (this == ANY) {
/*  83 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/*  86 */     JsonObject debug1 = new JsonObject();
/*  87 */     debug1.add("head", this.head.serializeToJson());
/*  88 */     debug1.add("chest", this.chest.serializeToJson());
/*  89 */     debug1.add("legs", this.legs.serializeToJson());
/*  90 */     debug1.add("feet", this.feet.serializeToJson());
/*  91 */     debug1.add("mainhand", this.mainhand.serializeToJson());
/*  92 */     debug1.add("offhand", this.offhand.serializeToJson());
/*  93 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  97 */     private ItemPredicate head = ItemPredicate.ANY;
/*  98 */     private ItemPredicate chest = ItemPredicate.ANY;
/*  99 */     private ItemPredicate legs = ItemPredicate.ANY;
/* 100 */     private ItemPredicate feet = ItemPredicate.ANY;
/* 101 */     private ItemPredicate mainhand = ItemPredicate.ANY;
/* 102 */     private ItemPredicate offhand = ItemPredicate.ANY;
/*     */     
/*     */     public static Builder equipment() {
/* 105 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder head(ItemPredicate debug1) {
/* 109 */       this.head = debug1;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Builder chest(ItemPredicate debug1) {
/* 114 */       this.chest = debug1;
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public Builder legs(ItemPredicate debug1) {
/* 119 */       this.legs = debug1;
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public Builder feet(ItemPredicate debug1) {
/* 124 */       this.feet = debug1;
/* 125 */       return this;
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
/*     */     public EntityEquipmentPredicate build() {
/* 139 */       return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.mainhand, this.offhand);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EntityEquipmentPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */