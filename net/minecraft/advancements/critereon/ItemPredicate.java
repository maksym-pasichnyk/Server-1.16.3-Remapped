/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.SerializationTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.EnchantedBookItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class ItemPredicate
/*     */ {
/*  30 */   public static final ItemPredicate ANY = new ItemPredicate();
/*     */   
/*     */   @Nullable
/*     */   private final Tag<Item> tag;
/*     */   @Nullable
/*     */   private final Item item;
/*     */   private final MinMaxBounds.Ints count;
/*     */   private final MinMaxBounds.Ints durability;
/*     */   private final EnchantmentPredicate[] enchantments;
/*     */   private final EnchantmentPredicate[] storedEnchantments;
/*     */   @Nullable
/*     */   private final Potion potion;
/*     */   private final NbtPredicate nbt;
/*     */   
/*     */   public ItemPredicate() {
/*  45 */     this.tag = null;
/*  46 */     this.item = null;
/*  47 */     this.potion = null;
/*  48 */     this.count = MinMaxBounds.Ints.ANY;
/*  49 */     this.durability = MinMaxBounds.Ints.ANY;
/*  50 */     this.enchantments = EnchantmentPredicate.NONE;
/*  51 */     this.storedEnchantments = EnchantmentPredicate.NONE;
/*  52 */     this.nbt = NbtPredicate.ANY;
/*     */   }
/*     */   
/*     */   public ItemPredicate(@Nullable Tag<Item> debug1, @Nullable Item debug2, MinMaxBounds.Ints debug3, MinMaxBounds.Ints debug4, EnchantmentPredicate[] debug5, EnchantmentPredicate[] debug6, @Nullable Potion debug7, NbtPredicate debug8) {
/*  56 */     this.tag = debug1;
/*  57 */     this.item = debug2;
/*  58 */     this.count = debug3;
/*  59 */     this.durability = debug4;
/*  60 */     this.enchantments = debug5;
/*  61 */     this.storedEnchantments = debug6;
/*  62 */     this.potion = debug7;
/*  63 */     this.nbt = debug8;
/*     */   }
/*     */   
/*     */   public boolean matches(ItemStack debug1) {
/*  67 */     if (this == ANY) {
/*  68 */       return true;
/*     */     }
/*     */     
/*  71 */     if (this.tag != null && !this.tag.contains(debug1.getItem())) {
/*  72 */       return false;
/*     */     }
/*  74 */     if (this.item != null && debug1.getItem() != this.item) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (!this.count.matches(debug1.getCount())) {
/*  78 */       return false;
/*     */     }
/*  80 */     if (!this.durability.isAny() && !debug1.isDamageableItem()) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (!this.durability.matches(debug1.getMaxDamage() - debug1.getDamageValue())) {
/*  84 */       return false;
/*     */     }
/*  86 */     if (!this.nbt.matches(debug1)) {
/*  87 */       return false;
/*     */     }
/*  89 */     if (this.enchantments.length > 0) {
/*  90 */       Map<Enchantment, Integer> map = EnchantmentHelper.deserializeEnchantments(debug1.getEnchantmentTags());
/*  91 */       for (EnchantmentPredicate debug6 : this.enchantments) {
/*  92 */         if (!debug6.containedIn(map)) {
/*  93 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*  97 */     if (this.storedEnchantments.length > 0) {
/*  98 */       Map<Enchantment, Integer> map = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(debug1));
/*  99 */       for (EnchantmentPredicate debug6 : this.storedEnchantments) {
/* 100 */         if (!debug6.containedIn(map)) {
/* 101 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     Potion debug2 = PotionUtils.getPotion(debug1);
/* 107 */     if (this.potion != null && this.potion != debug2) {
/* 108 */       return false;
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public static ItemPredicate fromJson(@Nullable JsonElement debug0) {
/* 114 */     if (debug0 == null || debug0.isJsonNull()) {
/* 115 */       return ANY;
/*     */     }
/* 117 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "item");
/* 118 */     MinMaxBounds.Ints debug2 = MinMaxBounds.Ints.fromJson(debug1.get("count"));
/* 119 */     MinMaxBounds.Ints debug3 = MinMaxBounds.Ints.fromJson(debug1.get("durability"));
/* 120 */     if (debug1.has("data")) {
/* 121 */       throw new JsonParseException("Disallowed data tag found");
/*     */     }
/* 123 */     NbtPredicate debug4 = NbtPredicate.fromJson(debug1.get("nbt"));
/* 124 */     Item debug5 = null;
/* 125 */     if (debug1.has("item")) {
/* 126 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "item"));
/* 127 */       debug5 = (Item)Registry.ITEM.getOptional(resourceLocation).orElseThrow(() -> new JsonSyntaxException("Unknown item id '" + debug0 + "'"));
/*     */     } 
/* 129 */     Tag<Item> debug6 = null;
/* 130 */     if (debug1.has("tag")) {
/* 131 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "tag"));
/* 132 */       debug6 = SerializationTags.getInstance().getItems().getTag(resourceLocation);
/* 133 */       if (debug6 == null) {
/* 134 */         throw new JsonSyntaxException("Unknown item tag '" + resourceLocation + "'");
/*     */       }
/*     */     } 
/* 137 */     Potion debug7 = null;
/* 138 */     if (debug1.has("potion")) {
/* 139 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "potion"));
/* 140 */       debug7 = (Potion)Registry.POTION.getOptional(resourceLocation).orElseThrow(() -> new JsonSyntaxException("Unknown potion '" + debug0 + "'"));
/*     */     } 
/*     */     
/* 143 */     EnchantmentPredicate[] debug8 = EnchantmentPredicate.fromJsonArray(debug1.get("enchantments"));
/* 144 */     EnchantmentPredicate[] debug9 = EnchantmentPredicate.fromJsonArray(debug1.get("stored_enchantments"));
/* 145 */     return new ItemPredicate(debug6, debug5, debug2, debug3, debug8, debug9, debug7, debug4);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 149 */     if (this == ANY) {
/* 150 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 153 */     JsonObject debug1 = new JsonObject();
/*     */     
/* 155 */     if (this.item != null) {
/* 156 */       debug1.addProperty("item", Registry.ITEM.getKey(this.item).toString());
/*     */     }
/*     */     
/* 159 */     if (this.tag != null) {
/* 160 */       debug1.addProperty("tag", SerializationTags.getInstance().getItems().getIdOrThrow(this.tag).toString());
/*     */     }
/*     */     
/* 163 */     debug1.add("count", this.count.serializeToJson());
/* 164 */     debug1.add("durability", this.durability.serializeToJson());
/* 165 */     debug1.add("nbt", this.nbt.serializeToJson());
/*     */     
/* 167 */     if (this.enchantments.length > 0) {
/* 168 */       JsonArray debug2 = new JsonArray();
/* 169 */       for (EnchantmentPredicate debug6 : this.enchantments) {
/* 170 */         debug2.add(debug6.serializeToJson());
/*     */       }
/* 172 */       debug1.add("enchantments", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 175 */     if (this.storedEnchantments.length > 0) {
/* 176 */       JsonArray debug2 = new JsonArray();
/* 177 */       for (EnchantmentPredicate debug6 : this.storedEnchantments) {
/* 178 */         debug2.add(debug6.serializeToJson());
/*     */       }
/* 180 */       debug1.add("stored_enchantments", (JsonElement)debug2);
/*     */     } 
/*     */     
/* 183 */     if (this.potion != null) {
/* 184 */       debug1.addProperty("potion", Registry.POTION.getKey(this.potion).toString());
/*     */     }
/*     */     
/* 187 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static ItemPredicate[] fromJsonArray(@Nullable JsonElement debug0) {
/* 191 */     if (debug0 == null || debug0.isJsonNull()) {
/* 192 */       return new ItemPredicate[0];
/*     */     }
/*     */     
/* 195 */     JsonArray debug1 = GsonHelper.convertToJsonArray(debug0, "items");
/* 196 */     ItemPredicate[] debug2 = new ItemPredicate[debug1.size()];
/*     */     
/* 198 */     for (int debug3 = 0; debug3 < debug2.length; debug3++) {
/* 199 */       debug2[debug3] = fromJson(debug1.get(debug3));
/*     */     }
/*     */     
/* 202 */     return debug2;
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 206 */     private final List<EnchantmentPredicate> enchantments = Lists.newArrayList();
/* 207 */     private final List<EnchantmentPredicate> storedEnchantments = Lists.newArrayList();
/*     */     @Nullable
/*     */     private Item item;
/*     */     @Nullable
/*     */     private Tag<Item> tag;
/* 212 */     private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
/* 213 */     private MinMaxBounds.Ints durability = MinMaxBounds.Ints.ANY;
/*     */     @Nullable
/*     */     private Potion potion;
/* 216 */     private NbtPredicate nbt = NbtPredicate.ANY;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Builder item() {
/* 222 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder of(ItemLike debug1) {
/* 226 */       this.item = debug1.asItem();
/* 227 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(Tag<Item> debug1) {
/* 231 */       this.tag = debug1;
/* 232 */       return this;
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
/*     */     public Builder hasNbt(CompoundTag debug1) {
/* 251 */       this.nbt = new NbtPredicate(debug1);
/* 252 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hasEnchantment(EnchantmentPredicate debug1) {
/* 256 */       this.enchantments.add(debug1);
/* 257 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ItemPredicate build() {
/* 266 */       return new ItemPredicate(this.tag, this.item, this.count, this.durability, this.enchantments.<EnchantmentPredicate>toArray(EnchantmentPredicate.NONE), this.storedEnchantments.<EnchantmentPredicate>toArray(EnchantmentPredicate.NONE), this.potion, this.nbt);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ItemPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */