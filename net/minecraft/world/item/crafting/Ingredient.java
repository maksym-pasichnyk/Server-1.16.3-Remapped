/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntComparators;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.SerializationTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public final class Ingredient implements Predicate<ItemStack> {
/*  33 */   public static final Ingredient EMPTY = new Ingredient(Stream.empty());
/*     */   
/*     */   private final Value[] values;
/*     */   private ItemStack[] itemStacks;
/*     */   private IntList stackingIds;
/*     */   
/*     */   private Ingredient(Stream<? extends Value> debug1) {
/*  40 */     this.values = debug1.<Value>toArray(debug0 -> new Value[debug0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dissolve() {
/*  49 */     if (this.itemStacks == null) {
/*  50 */       this.itemStacks = (ItemStack[])Arrays.<Value>stream(this.values).flatMap(debug0 -> debug0.getItems().stream()).distinct().toArray(debug0 -> new ItemStack[debug0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean test(@Nullable ItemStack debug1) {
/*  56 */     if (debug1 == null) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     dissolve();
/*     */     
/*  62 */     if (this.itemStacks.length == 0) {
/*  63 */       return debug1.isEmpty();
/*     */     }
/*     */     
/*  66 */     for (ItemStack debug5 : this.itemStacks) {
/*  67 */       if (debug5.getItem() == debug1.getItem()) {
/*  68 */         return true;
/*     */       }
/*     */     } 
/*  71 */     return false;
/*     */   }
/*     */   
/*     */   public IntList getStackingIds() {
/*  75 */     if (this.stackingIds == null) {
/*  76 */       dissolve();
/*  77 */       this.stackingIds = (IntList)new IntArrayList(this.itemStacks.length);
/*  78 */       for (ItemStack debug4 : this.itemStacks) {
/*  79 */         this.stackingIds.add(StackedContents.getStackingIndex(debug4));
/*     */       }
/*  81 */       this.stackingIds.sort((Comparator)IntComparators.NATURAL_COMPARATOR);
/*     */     } 
/*     */     
/*  84 */     return this.stackingIds;
/*     */   }
/*     */   
/*     */   public void toNetwork(FriendlyByteBuf debug1) {
/*  88 */     dissolve();
/*  89 */     debug1.writeVarInt(this.itemStacks.length);
/*  90 */     for (int debug2 = 0; debug2 < this.itemStacks.length; debug2++) {
/*  91 */       debug1.writeItem(this.itemStacks[debug2]);
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonElement toJson() {
/*  96 */     if (this.values.length == 1) {
/*  97 */       return (JsonElement)this.values[0].serialize();
/*     */     }
/*  99 */     JsonArray debug1 = new JsonArray();
/* 100 */     for (Value debug5 : this.values) {
/* 101 */       debug1.add((JsonElement)debug5.serialize());
/*     */     }
/* 103 */     return (JsonElement)debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 108 */     return (this.values.length == 0 && (this.itemStacks == null || this.itemStacks.length == 0) && (this.stackingIds == null || this.stackingIds.isEmpty()));
/*     */   }
/*     */   
/*     */   private static Ingredient fromValues(Stream<? extends Value> debug0) {
/* 112 */     Ingredient debug1 = new Ingredient(debug0);
/*     */     
/* 114 */     return (debug1.values.length == 0) ? EMPTY : debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ingredient of(ItemLike... debug0) {
/* 122 */     return of(Arrays.<ItemLike>stream(debug0).map(ItemStack::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ingredient of(Stream<ItemStack> debug0) {
/* 130 */     return fromValues(debug0.filter(debug0 -> !debug0.isEmpty()).map(debug0 -> new ItemValue(debug0)));
/*     */   }
/*     */   
/*     */   public static Ingredient of(Tag<Item> debug0) {
/* 134 */     return fromValues(Stream.of(new TagValue(debug0)));
/*     */   }
/*     */   
/*     */   public static Ingredient fromNetwork(FriendlyByteBuf debug0) {
/* 138 */     int debug1 = debug0.readVarInt();
/* 139 */     return fromValues(Stream.<Value>generate(() -> new ItemValue(debug0.readItem())).limit(debug1));
/*     */   }
/*     */   
/*     */   public static Ingredient fromJson(@Nullable JsonElement debug0) {
/* 143 */     if (debug0 == null || debug0.isJsonNull()) {
/* 144 */       throw new JsonSyntaxException("Item cannot be null");
/*     */     }
/* 146 */     if (debug0.isJsonObject())
/* 147 */       return fromValues(Stream.of(valueFromJson(debug0.getAsJsonObject()))); 
/* 148 */     if (debug0.isJsonArray()) {
/* 149 */       JsonArray debug1 = debug0.getAsJsonArray();
/* 150 */       if (debug1.size() == 0) {
/* 151 */         throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
/*     */       }
/* 153 */       return fromValues(StreamSupport.stream(debug1.spliterator(), false).map(debug0 -> valueFromJson(GsonHelper.convertToJsonObject(debug0, "item"))));
/*     */     } 
/* 155 */     throw new JsonSyntaxException("Expected item to be object or array of objects");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Value valueFromJson(JsonObject debug0) {
/* 160 */     if (debug0.has("item") && debug0.has("tag")) {
/* 161 */       throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
/*     */     }
/* 163 */     if (debug0.has("item")) {
/* 164 */       ResourceLocation debug1 = new ResourceLocation(GsonHelper.getAsString(debug0, "item"));
/* 165 */       Item debug2 = (Item)Registry.ITEM.getOptional(debug1).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + debug0 + "'"));
/* 166 */       return new ItemValue(new ItemStack((ItemLike)debug2));
/* 167 */     }  if (debug0.has("tag")) {
/* 168 */       ResourceLocation debug1 = new ResourceLocation(GsonHelper.getAsString(debug0, "tag"));
/* 169 */       Tag<Item> debug2 = SerializationTags.getInstance().getItems().getTag(debug1);
/* 170 */       if (debug2 == null) {
/* 171 */         throw new JsonSyntaxException("Unknown item tag '" + debug1 + "'");
/*     */       }
/* 173 */       return new TagValue(debug2);
/*     */     } 
/* 175 */     throw new JsonParseException("An ingredient entry needs either a tag or an item");
/*     */   }
/*     */   
/*     */   static interface Value {
/*     */     Collection<ItemStack> getItems();
/*     */     
/*     */     JsonObject serialize();
/*     */   }
/*     */   
/*     */   static class ItemValue
/*     */     implements Value {
/*     */     private final ItemStack item;
/*     */     
/*     */     private ItemValue(ItemStack debug1) {
/* 189 */       this.item = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<ItemStack> getItems() {
/* 194 */       return Collections.singleton(this.item);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonObject serialize() {
/* 199 */       JsonObject debug1 = new JsonObject();
/* 200 */       debug1.addProperty("item", Registry.ITEM.getKey(this.item.getItem()).toString());
/* 201 */       return debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   static class TagValue implements Value {
/*     */     private final Tag<Item> tag;
/*     */     
/*     */     private TagValue(Tag<Item> debug1) {
/* 209 */       this.tag = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<ItemStack> getItems() {
/* 214 */       List<ItemStack> debug1 = Lists.newArrayList();
/* 215 */       for (Item debug3 : this.tag.getValues()) {
/* 216 */         debug1.add(new ItemStack((ItemLike)debug3));
/*     */       }
/* 218 */       return debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonObject serialize() {
/* 223 */       JsonObject debug1 = new JsonObject();
/* 224 */       debug1.addProperty("tag", SerializationTags.getInstance().getItems().getIdOrThrow(this.tag).toString());
/* 225 */       return debug1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\Ingredient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */