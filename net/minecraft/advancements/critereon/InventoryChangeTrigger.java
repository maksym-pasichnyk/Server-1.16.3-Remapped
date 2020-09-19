/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class InventoryChangeTrigger
/*     */   extends SimpleCriterionTrigger<InventoryChangeTrigger.TriggerInstance> {
/*  16 */   private static final ResourceLocation ID = new ResourceLocation("inventory_changed");
/*     */ 
/*     */   
/*     */   public ResourceLocation getId() {
/*  20 */     return ID;
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/*  25 */     JsonObject debug4 = GsonHelper.getAsJsonObject(debug1, "slots", new JsonObject());
/*  26 */     MinMaxBounds.Ints debug5 = MinMaxBounds.Ints.fromJson(debug4.get("occupied"));
/*  27 */     MinMaxBounds.Ints debug6 = MinMaxBounds.Ints.fromJson(debug4.get("full"));
/*  28 */     MinMaxBounds.Ints debug7 = MinMaxBounds.Ints.fromJson(debug4.get("empty"));
/*  29 */     ItemPredicate[] debug8 = ItemPredicate.fromJsonArray(debug1.get("items"));
/*  30 */     return new TriggerInstance(debug2, debug5, debug6, debug7, debug8);
/*     */   }
/*     */   
/*     */   public void trigger(ServerPlayer debug1, Inventory debug2, ItemStack debug3) {
/*  34 */     int debug4 = 0;
/*  35 */     int debug5 = 0;
/*  36 */     int debug6 = 0;
/*     */     
/*  38 */     for (int debug7 = 0; debug7 < debug2.getContainerSize(); debug7++) {
/*  39 */       ItemStack debug8 = debug2.getItem(debug7);
/*  40 */       if (debug8.isEmpty()) {
/*  41 */         debug5++;
/*     */       } else {
/*  43 */         debug6++;
/*  44 */         if (debug8.getCount() >= debug8.getMaxStackSize()) {
/*  45 */           debug4++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     trigger(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private void trigger(ServerPlayer debug1, Inventory debug2, ItemStack debug3, int debug4, int debug5, int debug6) {
/*  54 */     trigger(debug1, debug5 -> debug5.matches(debug0, debug1, debug2, debug3, debug4));
/*     */   }
/*     */   
/*     */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*     */     private final MinMaxBounds.Ints slotsOccupied;
/*     */     private final MinMaxBounds.Ints slotsFull;
/*     */     private final MinMaxBounds.Ints slotsEmpty;
/*     */     private final ItemPredicate[] predicates;
/*     */     
/*     */     public TriggerInstance(EntityPredicate.Composite debug1, MinMaxBounds.Ints debug2, MinMaxBounds.Ints debug3, MinMaxBounds.Ints debug4, ItemPredicate[] debug5) {
/*  64 */       super(InventoryChangeTrigger.ID, debug1);
/*  65 */       this.slotsOccupied = debug2;
/*  66 */       this.slotsFull = debug3;
/*  67 */       this.slotsEmpty = debug4;
/*  68 */       this.predicates = debug5;
/*     */     }
/*     */     
/*     */     public static TriggerInstance hasItems(ItemPredicate... debug0) {
/*  72 */       return new TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, debug0);
/*     */     }
/*     */     
/*     */     public static TriggerInstance hasItems(ItemLike... debug0) {
/*  76 */       ItemPredicate[] debug1 = new ItemPredicate[debug0.length];
/*  77 */       for (int debug2 = 0; debug2 < debug0.length; debug2++) {
/*  78 */         debug1[debug2] = new ItemPredicate(null, debug0[debug2].asItem(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY);
/*     */       }
/*  80 */       return hasItems(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonObject serializeToJson(SerializationContext debug1) {
/*  85 */       JsonObject debug2 = super.serializeToJson(debug1);
/*     */       
/*  87 */       if (!this.slotsOccupied.isAny() || !this.slotsFull.isAny() || !this.slotsEmpty.isAny()) {
/*  88 */         JsonObject debug3 = new JsonObject();
/*  89 */         debug3.add("occupied", this.slotsOccupied.serializeToJson());
/*  90 */         debug3.add("full", this.slotsFull.serializeToJson());
/*  91 */         debug3.add("empty", this.slotsEmpty.serializeToJson());
/*  92 */         debug2.add("slots", (JsonElement)debug3);
/*     */       } 
/*     */       
/*  95 */       if (this.predicates.length > 0) {
/*  96 */         JsonArray debug3 = new JsonArray();
/*  97 */         for (ItemPredicate debug7 : this.predicates) {
/*  98 */           debug3.add(debug7.serializeToJson());
/*     */         }
/* 100 */         debug2.add("items", (JsonElement)debug3);
/*     */       } 
/*     */       
/* 103 */       return debug2;
/*     */     }
/*     */     
/*     */     public boolean matches(Inventory debug1, ItemStack debug2, int debug3, int debug4, int debug5) {
/* 107 */       if (!this.slotsFull.matches(debug3)) {
/* 108 */         return false;
/*     */       }
/* 110 */       if (!this.slotsEmpty.matches(debug4)) {
/* 111 */         return false;
/*     */       }
/* 113 */       if (!this.slotsOccupied.matches(debug5)) {
/* 114 */         return false;
/*     */       }
/*     */       
/* 117 */       int debug6 = this.predicates.length;
/* 118 */       if (debug6 == 0) {
/* 119 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 124 */       if (debug6 == 1) {
/* 125 */         return (!debug2.isEmpty() && this.predicates[0].matches(debug2));
/*     */       }
/*     */       
/* 128 */       ObjectArrayList objectArrayList = new ObjectArrayList((Object[])this.predicates);
/* 129 */       int debug8 = debug1.getContainerSize();
/* 130 */       for (int debug9 = 0; debug9 < debug8; debug9++) {
/* 131 */         if (objectArrayList.isEmpty()) {
/* 132 */           return true;
/*     */         }
/*     */         
/* 135 */         ItemStack debug10 = debug1.getItem(debug9);
/* 136 */         if (!debug10.isEmpty()) {
/* 137 */           objectArrayList.removeIf(debug1 -> debug1.matches(debug0));
/*     */         }
/*     */       } 
/* 140 */       return objectArrayList.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\InventoryChangeTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */