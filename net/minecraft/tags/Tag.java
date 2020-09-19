/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public interface Tag<T>
/*     */ {
/*     */   static <T> Codec<Tag<T>> codec(Supplier<TagCollection<T>> debug0) {
/*  24 */     return ResourceLocation.CODEC.flatXmap(debug1 -> (DataResult)Optional.ofNullable(((TagCollection)debug0.get()).getTag(debug1)).map(DataResult::success).orElseGet(()), debug1 -> (DataResult)Optional.<ResourceLocation>ofNullable(((TagCollection)debug0.get()).getId(debug1)).map(DataResult::success).orElseGet(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T getRandomElement(Random debug1) {
/*  35 */     List<T> debug2 = getValues();
/*  36 */     return debug2.get(debug1.nextInt(debug2.size()));
/*     */   }
/*     */   
/*     */   public static class BuilderEntry {
/*     */     private final Tag.Entry entry;
/*     */     private final String source;
/*     */     
/*     */     private BuilderEntry(Tag.Entry debug1, String debug2) {
/*  44 */       this.entry = debug1;
/*  45 */       this.source = debug2;
/*     */     }
/*     */     
/*     */     public Tag.Entry getEntry() {
/*  49 */       return this.entry;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/*  58 */       return this.entry.toString() + " (from " + this.source + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  63 */     private final List<Tag.BuilderEntry> entries = Lists.newArrayList();
/*     */     
/*     */     public static Builder tag() {
/*  66 */       return new Builder();
/*     */     }
/*     */     
/*     */     public Builder add(Tag.BuilderEntry debug1) {
/*  70 */       this.entries.add(debug1);
/*  71 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(Tag.Entry debug1, String debug2) {
/*  75 */       return add(new Tag.BuilderEntry(debug1, debug2));
/*     */     }
/*     */     
/*     */     public Builder addElement(ResourceLocation debug1, String debug2) {
/*  79 */       return add(new Tag.ElementEntry(debug1), debug2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addTag(ResourceLocation debug1, String debug2) {
/*  87 */       return add(new Tag.TagEntry(debug1), debug2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Optional<Tag<T>> build(Function<ResourceLocation, Tag<T>> debug1, Function<ResourceLocation, T> debug2) {
/*  95 */       ImmutableSet.Builder<T> debug3 = ImmutableSet.builder();
/*  96 */       for (Tag.BuilderEntry debug5 : this.entries) {
/*  97 */         if (!debug5.getEntry().build(debug1, debug2, debug3::add)) {
/*  98 */           return Optional.empty();
/*     */         }
/*     */       } 
/* 101 */       return Optional.of(Tag.fromSet((Set<T>)debug3.build()));
/*     */     }
/*     */     
/*     */     public Stream<Tag.BuilderEntry> getEntries() {
/* 105 */       return this.entries.stream();
/*     */     }
/*     */     
/*     */     public <T> Stream<Tag.BuilderEntry> getUnresolvedEntries(Function<ResourceLocation, Tag<T>> debug1, Function<ResourceLocation, T> debug2) {
/* 109 */       return getEntries().filter(debug2 -> !debug2.getEntry().build(debug0, debug1, ()));
/*     */     }
/*     */     
/*     */     public Builder addFromJson(JsonObject debug1, String debug2) {
/* 113 */       JsonArray debug3 = GsonHelper.getAsJsonArray(debug1, "values");
/*     */ 
/*     */       
/* 116 */       List<Tag.Entry> debug4 = Lists.newArrayList();
/* 117 */       for (JsonElement debug6 : debug3) {
/* 118 */         debug4.add(parseEntry(debug6));
/*     */       }
/*     */       
/* 121 */       if (GsonHelper.getAsBoolean(debug1, "replace", false)) {
/* 122 */         this.entries.clear();
/*     */       }
/*     */       
/* 125 */       debug4.forEach(debug2 -> this.entries.add(new Tag.BuilderEntry(debug2, debug1)));
/* 126 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private static Tag.Entry parseEntry(JsonElement debug0) {
/*     */       String debug1;
/*     */       boolean debug2;
/* 133 */       if (debug0.isJsonObject()) {
/* 134 */         JsonObject jsonObject = debug0.getAsJsonObject();
/* 135 */         debug1 = GsonHelper.getAsString(jsonObject, "id");
/* 136 */         debug2 = GsonHelper.getAsBoolean(jsonObject, "required", true);
/*     */       } else {
/* 138 */         debug1 = GsonHelper.convertToString(debug0, "id");
/* 139 */         debug2 = true;
/*     */       } 
/*     */       
/* 142 */       if (debug1.startsWith("#")) {
/* 143 */         ResourceLocation resourceLocation = new ResourceLocation(debug1.substring(1));
/* 144 */         return debug2 ? new Tag.TagEntry(resourceLocation) : new Tag.OptionalTagEntry(resourceLocation);
/*     */       } 
/* 146 */       ResourceLocation debug3 = new ResourceLocation(debug1);
/* 147 */       return debug2 ? new Tag.ElementEntry(debug3) : new Tag.OptionalElementEntry(debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonObject serializeToJson() {
/* 152 */       JsonObject debug1 = new JsonObject();
/* 153 */       JsonArray debug2 = new JsonArray();
/*     */       
/* 155 */       for (Tag.BuilderEntry debug4 : this.entries) {
/* 156 */         debug4.getEntry().serializeTo(debug2);
/*     */       }
/*     */       
/* 159 */       debug1.addProperty("replace", Boolean.valueOf(false));
/* 160 */       debug1.add("values", (JsonElement)debug2);
/*     */       
/* 162 */       return debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Entry {
/*     */     <T> boolean build(Function<ResourceLocation, Tag<T>> param1Function, Function<ResourceLocation, T> param1Function1, Consumer<T> param1Consumer);
/*     */     
/*     */     void serializeTo(JsonArray param1JsonArray);
/*     */   }
/*     */   
/*     */   public static class ElementEntry implements Entry {
/*     */     private final ResourceLocation id;
/*     */     
/*     */     public ElementEntry(ResourceLocation debug1) {
/* 176 */       this.id = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> boolean build(Function<ResourceLocation, Tag<T>> debug1, Function<ResourceLocation, T> debug2, Consumer<T> debug3) {
/* 181 */       T debug4 = debug2.apply(this.id);
/* 182 */       if (debug4 == null) {
/* 183 */         return false;
/*     */       }
/* 185 */       debug3.accept(debug4);
/* 186 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeTo(JsonArray debug1) {
/* 191 */       debug1.add(this.id.toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 196 */       return this.id.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OptionalElementEntry implements Entry {
/*     */     private final ResourceLocation id;
/*     */     
/*     */     public OptionalElementEntry(ResourceLocation debug1) {
/* 204 */       this.id = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> boolean build(Function<ResourceLocation, Tag<T>> debug1, Function<ResourceLocation, T> debug2, Consumer<T> debug3) {
/* 209 */       T debug4 = debug2.apply(this.id);
/* 210 */       if (debug4 != null) {
/* 211 */         debug3.accept(debug4);
/*     */       }
/* 213 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeTo(JsonArray debug1) {
/* 218 */       JsonObject debug2 = new JsonObject();
/* 219 */       debug2.addProperty("id", this.id.toString());
/* 220 */       debug2.addProperty("required", Boolean.valueOf(false));
/* 221 */       debug1.add((JsonElement)debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 226 */       return this.id.toString() + "?";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TagEntry implements Entry {
/*     */     private final ResourceLocation id;
/*     */     
/*     */     public TagEntry(ResourceLocation debug1) {
/* 234 */       this.id = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> boolean build(Function<ResourceLocation, Tag<T>> debug1, Function<ResourceLocation, T> debug2, Consumer<T> debug3) {
/* 239 */       Tag<T> debug4 = debug1.apply(this.id);
/* 240 */       if (debug4 == null) {
/* 241 */         return false;
/*     */       }
/* 243 */       debug4.getValues().forEach(debug3);
/* 244 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeTo(JsonArray debug1) {
/* 249 */       debug1.add("#" + this.id);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 254 */       return "#" + this.id;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OptionalTagEntry implements Entry {
/*     */     private final ResourceLocation id;
/*     */     
/*     */     public OptionalTagEntry(ResourceLocation debug1) {
/* 262 */       this.id = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> boolean build(Function<ResourceLocation, Tag<T>> debug1, Function<ResourceLocation, T> debug2, Consumer<T> debug3) {
/* 267 */       Tag<T> debug4 = debug1.apply(this.id);
/* 268 */       if (debug4 != null) {
/* 269 */         debug4.getValues().forEach(debug3);
/*     */       }
/* 271 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeTo(JsonArray debug1) {
/* 276 */       JsonObject debug2 = new JsonObject();
/* 277 */       debug2.addProperty("id", "#" + this.id);
/* 278 */       debug2.addProperty("required", Boolean.valueOf(false));
/* 279 */       debug1.add((JsonElement)debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 284 */       return "#" + this.id + "?";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Tag<T> fromSet(Set<T> debug0) {
/* 293 */     return SetTag.create(debug0);
/*     */   }
/*     */   
/*     */   boolean contains(T paramT);
/*     */   
/*     */   List<T> getValues();
/*     */   
/*     */   public static interface Named<T> extends Tag<T> {
/*     */     ResourceLocation getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */