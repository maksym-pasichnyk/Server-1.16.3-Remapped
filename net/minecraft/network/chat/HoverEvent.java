/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoverEvent
/*     */ {
/*  29 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Action<?> action;
/*     */   private final Object value;
/*     */   
/*     */   public <T> HoverEvent(Action<T> debug1, T debug2) {
/*  35 */     this.action = debug1;
/*  36 */     this.value = debug2;
/*     */   }
/*     */   
/*     */   public Action<?> getAction() {
/*  40 */     return this.action;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public <T> T getValue(Action<T> debug1) {
/*  45 */     if (this.action == debug1) {
/*  46 */       return debug1.cast(this.value);
/*     */     }
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  53 */     if (this == debug1) {
/*  54 */       return true;
/*     */     }
/*  56 */     if (debug1 == null || getClass() != debug1.getClass()) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     HoverEvent debug2 = (HoverEvent)debug1;
/*     */     
/*  62 */     return (this.action == debug2.action && Objects.equals(this.value, debug2.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  75 */     int debug1 = this.action.hashCode();
/*  76 */     debug1 = 31 * debug1 + ((this.value != null) ? this.value.hashCode() : 0);
/*  77 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static HoverEvent deserialize(JsonObject debug0) {
/*  82 */     String debug1 = GsonHelper.getAsString(debug0, "action", null);
/*  83 */     if (debug1 == null) {
/*  84 */       return null;
/*     */     }
/*     */     
/*  87 */     Action<?> debug2 = Action.getByName(debug1);
/*  88 */     if (debug2 == null) {
/*  89 */       return null;
/*     */     }
/*     */     
/*  92 */     JsonElement debug3 = debug0.get("contents");
/*  93 */     if (debug3 != null) {
/*  94 */       return debug2.deserialize(debug3);
/*     */     }
/*     */     
/*  97 */     Component debug4 = Component.Serializer.fromJson(debug0.get("value"));
/*  98 */     if (debug4 != null) {
/*  99 */       return debug2.deserializeFromLegacy(debug4);
/*     */     }
/*     */     
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public JsonObject serialize() {
/* 106 */     JsonObject debug1 = new JsonObject();
/* 107 */     debug1.addProperty("action", this.action.getName());
/* 108 */     debug1.add("contents", this.action.serializeArg(this.value));
/* 109 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class EntityTooltipInfo
/*     */   {
/*     */     public final EntityType<?> type;
/*     */     
/*     */     public final UUID id;
/*     */     @Nullable
/*     */     public final Component name;
/*     */     
/*     */     public EntityTooltipInfo(EntityType<?> debug1, UUID debug2, @Nullable Component debug3) {
/* 122 */       this.type = debug1;
/* 123 */       this.id = debug2;
/* 124 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static EntityTooltipInfo create(JsonElement debug0) {
/* 129 */       if (!debug0.isJsonObject()) {
/* 130 */         return null;
/*     */       }
/* 132 */       JsonObject debug1 = debug0.getAsJsonObject();
/* 133 */       EntityType<?> debug2 = (EntityType)Registry.ENTITY_TYPE.get(new ResourceLocation(GsonHelper.getAsString(debug1, "type")));
/* 134 */       UUID debug3 = UUID.fromString(GsonHelper.getAsString(debug1, "id"));
/* 135 */       Component debug4 = Component.Serializer.fromJson(debug1.get("name"));
/* 136 */       return new EntityTooltipInfo(debug2, debug3, debug4);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static EntityTooltipInfo create(Component debug0) {
/*     */       try {
/* 142 */         CompoundTag debug1 = TagParser.parseTag(debug0.getString());
/* 143 */         Component debug2 = Component.Serializer.fromJson(debug1.getString("name"));
/* 144 */         EntityType<?> debug3 = (EntityType)Registry.ENTITY_TYPE.get(new ResourceLocation(debug1.getString("type")));
/* 145 */         UUID debug4 = UUID.fromString(debug1.getString("id"));
/* 146 */         return new EntityTooltipInfo(debug3, debug4, debug2);
/* 147 */       } catch (JsonSyntaxException|CommandSyntaxException debug1) {
/* 148 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public JsonElement serialize() {
/* 153 */       JsonObject debug1 = new JsonObject();
/* 154 */       debug1.addProperty("type", Registry.ENTITY_TYPE.getKey(this.type).toString());
/* 155 */       debug1.addProperty("id", this.id.toString());
/* 156 */       if (this.name != null) {
/* 157 */         debug1.add("name", Component.Serializer.toJsonTree(this.name));
/*     */       }
/* 159 */       return (JsonElement)debug1;
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
/*     */     public boolean equals(Object debug1) {
/* 176 */       if (this == debug1) return true; 
/* 177 */       if (debug1 == null || getClass() != debug1.getClass()) return false;
/*     */       
/* 179 */       EntityTooltipInfo debug2 = (EntityTooltipInfo)debug1;
/* 180 */       return (this.type.equals(debug2.type) && this.id.equals(debug2.id) && Objects.equals(this.name, debug2.name));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 185 */       int debug1 = this.type.hashCode();
/* 186 */       debug1 = 31 * debug1 + this.id.hashCode();
/* 187 */       debug1 = 31 * debug1 + ((this.name != null) ? this.name.hashCode() : 0);
/* 188 */       return debug1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ItemStackInfo
/*     */   {
/*     */     private final Item item;
/*     */     
/*     */     private final int count;
/*     */     
/*     */     @Nullable
/*     */     private final CompoundTag tag;
/*     */     
/*     */     ItemStackInfo(Item debug1, int debug2, @Nullable CompoundTag debug3) {
/* 203 */       this.item = debug1;
/* 204 */       this.count = debug2;
/* 205 */       this.tag = debug3;
/*     */     }
/*     */     
/*     */     public ItemStackInfo(ItemStack debug1) {
/* 209 */       this(debug1.getItem(), debug1.getCount(), (debug1.getTag() != null) ? debug1.getTag().copy() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 214 */       if (this == debug1) return true; 
/* 215 */       if (debug1 == null || getClass() != debug1.getClass()) return false;
/*     */       
/* 217 */       ItemStackInfo debug2 = (ItemStackInfo)debug1;
/* 218 */       return (this.count == debug2.count && this.item.equals(debug2.item) && Objects.equals(this.tag, debug2.tag));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 223 */       int debug1 = this.item.hashCode();
/* 224 */       debug1 = 31 * debug1 + this.count;
/* 225 */       debug1 = 31 * debug1 + ((this.tag != null) ? this.tag.hashCode() : 0);
/* 226 */       return debug1;
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
/*     */     private static ItemStackInfo create(JsonElement debug0) {
/* 240 */       if (debug0.isJsonPrimitive()) {
/* 241 */         return new ItemStackInfo((Item)Registry.ITEM.get(new ResourceLocation(debug0.getAsString())), 1, null);
/*     */       }
/*     */       
/* 244 */       JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "item");
/* 245 */       Item debug2 = (Item)Registry.ITEM.get(new ResourceLocation(GsonHelper.getAsString(debug1, "id")));
/* 246 */       int debug3 = GsonHelper.getAsInt(debug1, "count", 1);
/* 247 */       if (debug1.has("tag")) {
/* 248 */         String debug4 = GsonHelper.getAsString(debug1, "tag");
/*     */         try {
/* 250 */           CompoundTag debug5 = TagParser.parseTag(debug4);
/* 251 */           return new ItemStackInfo(debug2, debug3, debug5);
/* 252 */         } catch (CommandSyntaxException debug5) {
/* 253 */           HoverEvent.LOGGER.warn("Failed to parse tag: {}", debug4, debug5);
/*     */         } 
/*     */       } 
/*     */       
/* 257 */       return new ItemStackInfo(debug2, debug3, null);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static ItemStackInfo create(Component debug0) {
/*     */       try {
/* 263 */         CompoundTag debug1 = TagParser.parseTag(debug0.getString());
/* 264 */         return new ItemStackInfo(ItemStack.of(debug1));
/* 265 */       } catch (CommandSyntaxException debug1) {
/* 266 */         HoverEvent.LOGGER.warn("Failed to parse item tag: {}", debug0, debug1);
/* 267 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private JsonElement serialize() {
/* 272 */       JsonObject debug1 = new JsonObject();
/* 273 */       debug1.addProperty("id", Registry.ITEM.getKey(this.item).toString());
/* 274 */       if (this.count != 1) {
/* 275 */         debug1.addProperty("count", Integer.valueOf(this.count));
/*     */       }
/* 277 */       if (this.tag != null) {
/* 278 */         debug1.addProperty("tag", this.tag.toString());
/*     */       }
/* 280 */       return (JsonElement)debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Action<T> {
/* 285 */     public static final Action<Component> SHOW_TEXT = new Action("show_text", true, Component.Serializer::fromJson, Component.Serializer::toJsonTree, 
/*     */ 
/*     */         
/* 288 */         (Function)Function.identity());
/*     */     static {
/* 290 */       SHOW_ITEM = new Action("show_item", true, debug0 -> HoverEvent.ItemStackInfo.create(debug0), debug0 -> ((HoverEvent.ItemStackInfo)debug0).serialize(), debug0 -> HoverEvent.ItemStackInfo.create(debug0));
/*     */     }
/*     */ 
/*     */     
/*     */     public static final Action<HoverEvent.ItemStackInfo> SHOW_ITEM;
/* 295 */     public static final Action<HoverEvent.EntityTooltipInfo> SHOW_ENTITY = new Action("show_entity", true, HoverEvent.EntityTooltipInfo::create, HoverEvent.EntityTooltipInfo::serialize, HoverEvent.EntityTooltipInfo::create);
/*     */     private static final Map<String, Action> LOOKUP;
/*     */     private final String name;
/*     */     private final boolean allowFromServer;
/*     */     
/*     */     static {
/* 301 */       LOOKUP = (Map<String, Action>)Stream.<Action>of(new Action[] { SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY }).collect(ImmutableMap.toImmutableMap(Action::getName, debug0 -> debug0));
/*     */     }
/*     */ 
/*     */     
/*     */     private final Function<JsonElement, T> argDeserializer;
/*     */     private final Function<T, JsonElement> argSerializer;
/*     */     private final Function<Component, T> legacyArgDeserializer;
/*     */     
/*     */     public Action(String debug1, boolean debug2, Function<JsonElement, T> debug3, Function<T, JsonElement> debug4, Function<Component, T> debug5) {
/* 310 */       this.name = debug1;
/* 311 */       this.allowFromServer = debug2;
/* 312 */       this.argDeserializer = debug3;
/* 313 */       this.argSerializer = debug4;
/* 314 */       this.legacyArgDeserializer = debug5;
/*     */     }
/*     */     
/*     */     public boolean isAllowedFromServer() {
/* 318 */       return this.allowFromServer;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 322 */       return this.name;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static Action getByName(String debug0) {
/* 327 */       return LOOKUP.get(debug0);
/*     */     }
/*     */ 
/*     */     
/*     */     private T cast(Object debug1) {
/* 332 */       return (T)debug1;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public HoverEvent deserialize(JsonElement debug1) {
/* 337 */       T debug2 = this.argDeserializer.apply(debug1);
/* 338 */       if (debug2 == null) {
/* 339 */         return null;
/*     */       }
/* 341 */       return new HoverEvent(this, debug2);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public HoverEvent deserializeFromLegacy(Component debug1) {
/* 346 */       T debug2 = this.legacyArgDeserializer.apply(debug1);
/* 347 */       if (debug2 == null) {
/* 348 */         return null;
/*     */       }
/* 350 */       return new HoverEvent(this, debug2);
/*     */     }
/*     */     
/*     */     public JsonElement serializeArg(Object debug1) {
/* 354 */       return this.argSerializer.apply(cast(debug1));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 359 */       return "<action " + this.name + ">";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\HoverEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */