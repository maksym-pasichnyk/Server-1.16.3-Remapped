/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GsonAdapterFactory
/*     */ {
/*     */   public static class Builder<E, T extends SerializerType<E>>
/*     */   {
/*     */     private final Registry<T> registry;
/*     */     private final String elementName;
/*     */     private final String typeKey;
/*     */     private final Function<E, T> typeGetter;
/*     */     @Nullable
/*     */     private Pair<T, GsonAdapterFactory.DefaultSerializer<? extends E>> defaultType;
/*     */     
/*     */     private Builder(Registry<T> debug1, String debug2, String debug3, Function<E, T> debug4) {
/*  33 */       this.registry = debug1;
/*  34 */       this.elementName = debug2;
/*  35 */       this.typeKey = debug3;
/*  36 */       this.typeGetter = debug4;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object build() {
/*  45 */       return new GsonAdapterFactory.JsonAdapter<>(this.registry, this.elementName, this.typeKey, this.typeGetter, this.defaultType);
/*     */     }
/*     */   }
/*     */   
/*     */   public static <E, T extends SerializerType<E>> Builder<E, T> builder(Registry<T> debug0, String debug1, String debug2, Function<E, T> debug3) {
/*  50 */     return new Builder<>(debug0, debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   static class JsonAdapter<E, T extends SerializerType<E>>
/*     */     implements JsonDeserializer<E>, JsonSerializer<E>
/*     */   {
/*     */     private final Registry<T> registry;
/*     */     private final String elementName;
/*     */     private final String typeKey;
/*     */     private final Function<E, T> typeGetter;
/*     */     @Nullable
/*     */     private final Pair<T, GsonAdapterFactory.DefaultSerializer<? extends E>> defaultType;
/*     */     
/*     */     private JsonAdapter(Registry<T> debug1, String debug2, String debug3, Function<E, T> debug4, @Nullable Pair<T, GsonAdapterFactory.DefaultSerializer<? extends E>> debug5) {
/*  65 */       this.registry = debug1;
/*  66 */       this.elementName = debug2;
/*  67 */       this.typeKey = debug3;
/*  68 */       this.typeGetter = debug4;
/*  69 */       this.defaultType = debug5;
/*     */     }
/*     */ 
/*     */     
/*     */     public E deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/*  74 */       if (debug1.isJsonObject()) {
/*  75 */         JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, this.elementName);
/*  76 */         ResourceLocation debug5 = new ResourceLocation(GsonHelper.getAsString(debug4, this.typeKey));
/*     */         
/*  78 */         SerializerType<E> serializerType = (SerializerType)this.registry.get(debug5);
/*  79 */         if (serializerType == null) {
/*  80 */           throw new JsonSyntaxException("Unknown type '" + debug5 + "'");
/*     */         }
/*     */         
/*  83 */         return serializerType.getSerializer().deserialize(debug4, debug3);
/*     */       } 
/*     */       
/*  86 */       if (this.defaultType == null) {
/*  87 */         throw new UnsupportedOperationException("Object " + debug1 + " can't be deserialized");
/*     */       }
/*  89 */       return ((GsonAdapterFactory.DefaultSerializer<E>)this.defaultType.getSecond()).deserialize(debug1, debug3);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement serialize(E debug1, Type debug2, JsonSerializationContext debug3) {
/*  95 */       SerializerType<E> serializerType = (SerializerType)this.typeGetter.apply(debug1);
/*  96 */       if (this.defaultType != null && this.defaultType.getFirst() == serializerType) {
/*  97 */         return ((GsonAdapterFactory.DefaultSerializer<E>)this.defaultType.getSecond()).serialize(debug1, debug3);
/*     */       }
/*     */       
/* 100 */       if (serializerType == null) {
/* 101 */         throw new JsonSyntaxException("Unknown type: " + debug1);
/*     */       }
/*     */       
/* 104 */       JsonObject debug5 = new JsonObject();
/* 105 */       debug5.addProperty(this.typeKey, this.registry.getKey(serializerType).toString());
/* 106 */       serializerType.getSerializer().serialize(debug5, debug1, debug3);
/* 107 */       return (JsonElement)debug5;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface DefaultSerializer<T> {
/*     */     JsonElement serialize(T param1T, JsonSerializationContext param1JsonSerializationContext);
/*     */     
/*     */     T deserialize(JsonElement param1JsonElement, JsonDeserializationContext param1JsonDeserializationContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\GsonAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */