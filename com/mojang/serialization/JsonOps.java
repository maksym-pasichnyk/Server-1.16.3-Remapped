/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ public class JsonOps
/*     */   implements DynamicOps<JsonElement>
/*     */ {
/*  25 */   public static final JsonOps INSTANCE = new JsonOps(false);
/*  26 */   public static final JsonOps COMPRESSED = new JsonOps(true);
/*     */   
/*     */   private final boolean compressed;
/*     */   
/*     */   protected JsonOps(boolean compressed) {
/*  31 */     this.compressed = compressed;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement empty() {
/*  36 */     return (JsonElement)JsonNull.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, JsonElement input) {
/*  41 */     if (input instanceof JsonObject) {
/*  42 */       return (U)convertMap(outOps, input);
/*     */     }
/*  44 */     if (input instanceof JsonArray) {
/*  45 */       return (U)convertList(outOps, input);
/*     */     }
/*  47 */     if (input instanceof JsonNull) {
/*  48 */       return outOps.empty();
/*     */     }
/*  50 */     JsonPrimitive primitive = input.getAsJsonPrimitive();
/*  51 */     if (primitive.isString()) {
/*  52 */       return outOps.createString(primitive.getAsString());
/*     */     }
/*  54 */     if (primitive.isBoolean()) {
/*  55 */       return outOps.createBoolean(primitive.getAsBoolean());
/*     */     }
/*  57 */     BigDecimal value = primitive.getAsBigDecimal();
/*     */     try {
/*  59 */       long l = value.longValueExact();
/*  60 */       if ((byte)(int)l == l) {
/*  61 */         return outOps.createByte((byte)(int)l);
/*     */       }
/*  63 */       if ((short)(int)l == l) {
/*  64 */         return outOps.createShort((short)(int)l);
/*     */       }
/*  66 */       if ((int)l == l) {
/*  67 */         return outOps.createInt((int)l);
/*     */       }
/*  69 */       return outOps.createLong(l);
/*  70 */     } catch (ArithmeticException e) {
/*  71 */       double d = value.doubleValue();
/*  72 */       if ((float)d == d) {
/*  73 */         return outOps.createFloat((float)d);
/*     */       }
/*  75 */       return outOps.createDouble(d);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> getNumberValue(JsonElement input) {
/*  81 */     if (input instanceof JsonPrimitive) {
/*  82 */       if (input.getAsJsonPrimitive().isNumber())
/*  83 */         return DataResult.success(input.getAsNumber()); 
/*  84 */       if (input.getAsJsonPrimitive().isBoolean()) {
/*  85 */         return DataResult.success(Integer.valueOf(input.getAsBoolean() ? 1 : 0));
/*     */       }
/*  87 */       if (this.compressed && input.getAsJsonPrimitive().isString()) {
/*     */         try {
/*  89 */           return DataResult.success(Integer.valueOf(Integer.parseInt(input.getAsString())));
/*  90 */         } catch (NumberFormatException e) {
/*  91 */           return DataResult.error("Not a number: " + e + " " + input);
/*     */         } 
/*     */       }
/*     */     } 
/*  95 */     if (input instanceof JsonPrimitive && input.getAsJsonPrimitive().isBoolean()) {
/*  96 */       return DataResult.success(Integer.valueOf(input.getAsJsonPrimitive().getAsBoolean() ? 1 : 0));
/*     */     }
/*  98 */     return DataResult.error("Not a number: " + input);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createNumeric(Number i) {
/* 103 */     return (JsonElement)new JsonPrimitive(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(JsonElement input) {
/* 108 */     if (input instanceof JsonPrimitive) {
/* 109 */       if (input.getAsJsonPrimitive().isBoolean())
/* 110 */         return DataResult.success(Boolean.valueOf(input.getAsBoolean())); 
/* 111 */       if (input.getAsJsonPrimitive().isNumber()) {
/* 112 */         return DataResult.success(Boolean.valueOf((input.getAsNumber().byteValue() != 0)));
/*     */       }
/*     */     } 
/* 115 */     return DataResult.error("Not a boolean: " + input);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createBoolean(boolean value) {
/* 120 */     return (JsonElement)new JsonPrimitive(Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(JsonElement input) {
/* 125 */     if (input instanceof JsonPrimitive && (
/* 126 */       input.getAsJsonPrimitive().isString() || (input.getAsJsonPrimitive().isNumber() && this.compressed))) {
/* 127 */       return DataResult.success(input.getAsString());
/*     */     }
/*     */     
/* 130 */     return DataResult.error("Not a string: " + input);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createString(String value) {
/* 135 */     return (JsonElement)new JsonPrimitive(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToList(JsonElement list, JsonElement value) {
/* 140 */     if (!(list instanceof JsonArray) && list != empty()) {
/* 141 */       return DataResult.error("mergeToList called with not a list: " + list, list);
/*     */     }
/*     */     
/* 144 */     JsonArray result = new JsonArray();
/* 145 */     if (list != empty()) {
/* 146 */       result.addAll(list.getAsJsonArray());
/*     */     }
/* 148 */     result.add(value);
/* 149 */     return (DataResult)DataResult.success(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToList(JsonElement list, List<JsonElement> values) {
/* 154 */     if (!(list instanceof JsonArray) && list != empty()) {
/* 155 */       return DataResult.error("mergeToList called with not a list: " + list, list);
/*     */     }
/*     */     
/* 158 */     JsonArray result = new JsonArray();
/* 159 */     if (list != empty()) {
/* 160 */       result.addAll(list.getAsJsonArray());
/*     */     }
/* 162 */     values.forEach(result::add);
/* 163 */     return (DataResult)DataResult.success(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToMap(JsonElement map, JsonElement key, JsonElement value) {
/* 168 */     if (!(map instanceof JsonObject) && map != empty()) {
/* 169 */       return DataResult.error("mergeToMap called with not a map: " + map, map);
/*     */     }
/* 171 */     if (!(key instanceof JsonPrimitive) || (!key.getAsJsonPrimitive().isString() && !this.compressed)) {
/* 172 */       return DataResult.error("key is not a string: " + key, map);
/*     */     }
/*     */     
/* 175 */     JsonObject output = new JsonObject();
/* 176 */     if (map != empty()) {
/* 177 */       map.getAsJsonObject().entrySet().forEach(entry -> output.add((String)entry.getKey(), (JsonElement)entry.getValue()));
/*     */     }
/* 179 */     output.add(key.getAsString(), value);
/*     */     
/* 181 */     return (DataResult)DataResult.success(output);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> mergeToMap(JsonElement map, MapLike<JsonElement> values) {
/* 186 */     if (!(map instanceof JsonObject) && map != empty()) {
/* 187 */       return DataResult.error("mergeToMap called with not a map: " + map, map);
/*     */     }
/*     */     
/* 190 */     JsonObject output = new JsonObject();
/* 191 */     if (map != empty()) {
/* 192 */       map.getAsJsonObject().entrySet().forEach(entry -> output.add((String)entry.getKey(), (JsonElement)entry.getValue()));
/*     */     }
/*     */     
/* 195 */     List<JsonElement> missed = Lists.newArrayList();
/*     */     
/* 197 */     values.entries().forEach(entry -> {
/*     */           JsonElement key = (JsonElement)entry.getFirst();
/*     */           
/*     */           if (!(key instanceof JsonPrimitive) || (!key.getAsJsonPrimitive().isString() && !this.compressed)) {
/*     */             missed.add(key);
/*     */             return;
/*     */           } 
/*     */           output.add(key.getAsString(), (JsonElement)entry.getSecond());
/*     */         });
/* 206 */     if (!missed.isEmpty()) {
/* 207 */       return (DataResult)DataResult.error("some keys are not strings: " + missed, output);
/*     */     }
/*     */     
/* 210 */     return (DataResult)DataResult.success(output);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<JsonElement, JsonElement>>> getMapValues(JsonElement input) {
/* 215 */     if (!(input instanceof JsonObject)) {
/* 216 */       return DataResult.error("Not a JSON object: " + input);
/*     */     }
/* 218 */     return DataResult.success(input.getAsJsonObject().entrySet().stream().map(entry -> Pair.of(new JsonPrimitive((String)entry.getKey()), (entry.getValue() instanceof JsonNull) ? null : entry.getValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<JsonElement, JsonElement>>> getMapEntries(JsonElement input) {
/* 223 */     if (!(input instanceof JsonObject)) {
/* 224 */       return DataResult.error("Not a JSON object: " + input);
/*     */     }
/* 226 */     return DataResult.success(c -> {
/*     */           for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)input.getAsJsonObject().entrySet()) {
/*     */             c.accept(createString(entry.getKey()), (entry.getValue() instanceof JsonNull) ? null : entry.getValue());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<MapLike<JsonElement>> getMap(JsonElement input) {
/* 235 */     if (!(input instanceof JsonObject)) {
/* 236 */       return DataResult.error("Not a JSON object: " + input);
/*     */     }
/* 238 */     final JsonObject object = input.getAsJsonObject();
/* 239 */     return DataResult.success(new MapLike<JsonElement>()
/*     */         {
/*     */           @Nullable
/*     */           public JsonElement get(JsonElement key) {
/* 243 */             JsonElement element = object.get(key.getAsString());
/* 244 */             if (element instanceof JsonNull) {
/* 245 */               return null;
/*     */             }
/* 247 */             return element;
/*     */           }
/*     */ 
/*     */           
/*     */           @Nullable
/*     */           public JsonElement get(String key) {
/* 253 */             JsonElement element = object.get(key);
/* 254 */             if (element instanceof JsonNull) {
/* 255 */               return null;
/*     */             }
/* 257 */             return element;
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<Pair<JsonElement, JsonElement>> entries() {
/* 262 */             return object.entrySet().stream().map(e -> Pair.of(new JsonPrimitive((String)e.getKey()), e.getValue()));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 267 */             return "MapLike[" + object + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createMap(Stream<Pair<JsonElement, JsonElement>> map) {
/* 274 */     JsonObject result = new JsonObject();
/* 275 */     map.forEach(p -> result.add(((JsonElement)p.getFirst()).getAsString(), (JsonElement)p.getSecond()));
/* 276 */     return (JsonElement)result;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<JsonElement>> getStream(JsonElement input) {
/* 281 */     if (input instanceof JsonArray) {
/* 282 */       return DataResult.success(StreamSupport.stream(input.getAsJsonArray().spliterator(), false).map(e -> (e instanceof JsonNull) ? null : e));
/*     */     }
/* 284 */     return DataResult.error("Not a json array: " + input);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<JsonElement>>> getList(JsonElement input) {
/* 289 */     if (input instanceof JsonArray) {
/* 290 */       return DataResult.success(c -> {
/*     */             for (JsonElement element : input.getAsJsonArray()) {
/*     */               c.accept((element instanceof JsonNull) ? null : element);
/*     */             }
/*     */           });
/*     */     }
/* 296 */     return DataResult.error("Not a json array: " + input);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement createList(Stream<JsonElement> input) {
/* 301 */     JsonArray result = new JsonArray();
/* 302 */     input.forEach(result::add);
/* 303 */     return (JsonElement)result;
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement remove(JsonElement input, String key) {
/* 308 */     if (input instanceof JsonObject) {
/* 309 */       JsonObject result = new JsonObject();
/* 310 */       input.getAsJsonObject().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), key)).forEach(entry -> result.add((String)entry.getKey(), (JsonElement)entry.getValue()));
/* 311 */       return (JsonElement)result;
/*     */     } 
/* 313 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 318 */     return "JSON";
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<JsonElement> listBuilder() {
/* 323 */     return new ArrayBuilder();
/*     */   }
/*     */   
/*     */   private static final class ArrayBuilder implements ListBuilder<JsonElement> {
/* 327 */     private DataResult<JsonArray> builder = DataResult.success(new JsonArray(), Lifecycle.stable());
/*     */ 
/*     */     
/*     */     public DynamicOps<JsonElement> ops() {
/* 331 */       return JsonOps.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> add(JsonElement value) {
/* 336 */       this.builder = this.builder.map(b -> {
/*     */             b.add(value);
/*     */             return b;
/*     */           });
/* 340 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> add(DataResult<JsonElement> value) {
/* 345 */       this.builder = this.builder.apply2stable((b, element) -> { b.add(element); return b; }value);
/*     */ 
/*     */ 
/*     */       
/* 349 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> withErrorsFrom(DataResult<?> result) {
/* 354 */       this.builder = this.builder.flatMap(r -> result.map(()));
/* 355 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<JsonElement> mapError(UnaryOperator<String> onError) {
/* 360 */       this.builder = this.builder.mapError(onError);
/* 361 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataResult<JsonElement> build(JsonElement prefix) {
/* 366 */       DataResult<JsonElement> result = this.builder.flatMap(b -> {
/*     */             if (!(prefix instanceof JsonArray) && prefix != ops().empty()) {
/*     */               return DataResult.error("Cannot append a list to not a list: " + prefix, prefix);
/*     */             }
/*     */             
/*     */             JsonArray array = new JsonArray();
/*     */             
/*     */             if (prefix != ops().empty()) {
/*     */               array.addAll(prefix.getAsJsonArray());
/*     */             }
/*     */             array.addAll(b);
/*     */             return DataResult.success(array, Lifecycle.stable());
/*     */           });
/* 379 */       this.builder = DataResult.success(new JsonArray(), Lifecycle.stable());
/* 380 */       return result;
/*     */     }
/*     */     
/*     */     private ArrayBuilder() {} }
/*     */   
/*     */   public boolean compressMaps() {
/* 386 */     return this.compressed;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<JsonElement> mapBuilder() {
/* 391 */     return new JsonRecordBuilder();
/*     */   }
/*     */   
/*     */   private class JsonRecordBuilder extends RecordBuilder.AbstractStringBuilder<JsonElement, JsonObject> {
/*     */     protected JsonRecordBuilder() {
/* 396 */       super(JsonOps.this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject initBuilder() {
/* 401 */       return new JsonObject();
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject append(String key, JsonElement value, JsonObject builder) {
/* 406 */       builder.add(key, value);
/* 407 */       return builder;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<JsonElement> build(JsonObject builder, JsonElement prefix) {
/* 412 */       if (prefix == null || prefix instanceof JsonNull) {
/* 413 */         return (DataResult)DataResult.success(builder);
/*     */       }
/* 415 */       if (prefix instanceof JsonObject) {
/* 416 */         JsonObject result = new JsonObject();
/* 417 */         for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)prefix.getAsJsonObject().entrySet()) {
/* 418 */           result.add(entry.getKey(), entry.getValue());
/*     */         }
/* 420 */         for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)builder.entrySet()) {
/* 421 */           result.add(entry.getKey(), entry.getValue());
/*     */         }
/* 423 */         return (DataResult)DataResult.success(result);
/*     */       } 
/* 425 */       return DataResult.error("mergeToMap called with not a map: " + prefix, prefix);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\JsonOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */