/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Item;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GsonHelper
/*     */ {
/*  27 */   private static final Gson GSON = (new GsonBuilder()).create();
/*     */   
/*     */   public static boolean isStringValue(JsonObject debug0, String debug1) {
/*  30 */     if (!isValidPrimitive(debug0, debug1)) {
/*  31 */       return false;
/*     */     }
/*  33 */     return debug0.getAsJsonPrimitive(debug1).isString();
/*     */   }
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
/*     */   public static boolean isNumberValue(JsonElement debug0) {
/*  51 */     if (!debug0.isJsonPrimitive()) {
/*  52 */       return false;
/*     */     }
/*  54 */     return debug0.getAsJsonPrimitive().isNumber();
/*     */   }
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
/*     */   public static boolean isArrayNode(JsonObject debug0, String debug1) {
/*  72 */     if (!isValidNode(debug0, debug1)) {
/*  73 */       return false;
/*     */     }
/*  75 */     return debug0.get(debug1).isJsonArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidPrimitive(JsonObject debug0, String debug1) {
/*  86 */     if (!isValidNode(debug0, debug1)) {
/*  87 */       return false;
/*     */     }
/*  89 */     return debug0.get(debug1).isJsonPrimitive();
/*     */   }
/*     */   
/*     */   public static boolean isValidNode(JsonObject debug0, String debug1) {
/*  93 */     if (debug0 == null) {
/*  94 */       return false;
/*     */     }
/*  96 */     return (debug0.get(debug1) != null);
/*     */   }
/*     */   
/*     */   public static String convertToString(JsonElement debug0, String debug1) {
/* 100 */     if (debug0.isJsonPrimitive()) {
/* 101 */       return debug0.getAsString();
/*     */     }
/* 103 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a string, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAsString(JsonObject debug0, String debug1) {
/* 108 */     if (debug0.has(debug1)) {
/* 109 */       return convertToString(debug0.get(debug1), debug1);
/*     */     }
/* 111 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a string");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAsString(JsonObject debug0, String debug1, String debug2) {
/* 116 */     if (debug0.has(debug1)) {
/* 117 */       return convertToString(debug0.get(debug1), debug1);
/*     */     }
/* 119 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Item convertToItem(JsonElement debug0, String debug1) {
/* 124 */     if (debug0.isJsonPrimitive()) {
/* 125 */       String debug2 = debug0.getAsString();
/* 126 */       return (Item)Registry.ITEM.getOptional(new ResourceLocation(debug2))
/* 127 */         .orElseThrow(() -> new JsonSyntaxException("Expected " + debug0 + " to be an item, was unknown string '" + debug1 + "'"));
/*     */     } 
/* 129 */     throw new JsonSyntaxException("Expected " + debug1 + " to be an item, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Item getAsItem(JsonObject debug0, String debug1) {
/* 134 */     if (debug0.has(debug1)) {
/* 135 */       return convertToItem(debug0.get(debug1), debug1);
/*     */     }
/* 137 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find an item");
/*     */   }
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
/*     */   public static boolean convertToBoolean(JsonElement debug0, String debug1) {
/* 150 */     if (debug0.isJsonPrimitive()) {
/* 151 */       return debug0.getAsBoolean();
/*     */     }
/* 153 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a Boolean, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAsBoolean(JsonObject debug0, String debug1) {
/* 158 */     if (debug0.has(debug1)) {
/* 159 */       return convertToBoolean(debug0.get(debug1), debug1);
/*     */     }
/* 161 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a Boolean");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAsBoolean(JsonObject debug0, String debug1, boolean debug2) {
/* 166 */     if (debug0.has(debug1)) {
/* 167 */       return convertToBoolean(debug0.get(debug1), debug1);
/*     */     }
/* 169 */     return debug2;
/*     */   }
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
/*     */   public static float convertToFloat(JsonElement debug0, String debug1) {
/* 198 */     if (debug0.isJsonPrimitive() && debug0.getAsJsonPrimitive().isNumber()) {
/* 199 */       return debug0.getAsFloat();
/*     */     }
/* 201 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a Float, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAsFloat(JsonObject debug0, String debug1) {
/* 206 */     if (debug0.has(debug1)) {
/* 207 */       return convertToFloat(debug0.get(debug1), debug1);
/*     */     }
/* 209 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a Float");
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAsFloat(JsonObject debug0, String debug1, float debug2) {
/* 214 */     if (debug0.has(debug1)) {
/* 215 */       return convertToFloat(debug0.get(debug1), debug1);
/*     */     }
/* 217 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long convertToLong(JsonElement debug0, String debug1) {
/* 222 */     if (debug0.isJsonPrimitive() && debug0.getAsJsonPrimitive().isNumber()) {
/* 223 */       return debug0.getAsLong();
/*     */     }
/* 225 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a Long, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getAsLong(JsonObject debug0, String debug1) {
/* 230 */     if (debug0.has(debug1)) {
/* 231 */       return convertToLong(debug0.get(debug1), debug1);
/*     */     }
/* 233 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a Long");
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getAsLong(JsonObject debug0, String debug1, long debug2) {
/* 238 */     if (debug0.has(debug1)) {
/* 239 */       return convertToLong(debug0.get(debug1), debug1);
/*     */     }
/* 241 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int convertToInt(JsonElement debug0, String debug1) {
/* 246 */     if (debug0.isJsonPrimitive() && debug0.getAsJsonPrimitive().isNumber()) {
/* 247 */       return debug0.getAsInt();
/*     */     }
/* 249 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a Int, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAsInt(JsonObject debug0, String debug1) {
/* 254 */     if (debug0.has(debug1)) {
/* 255 */       return convertToInt(debug0.get(debug1), debug1);
/*     */     }
/* 257 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a Int");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAsInt(JsonObject debug0, String debug1, int debug2) {
/* 262 */     if (debug0.has(debug1)) {
/* 263 */       return convertToInt(debug0.get(debug1), debug1);
/*     */     }
/* 265 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte convertToByte(JsonElement debug0, String debug1) {
/* 270 */     if (debug0.isJsonPrimitive() && debug0.getAsJsonPrimitive().isNumber()) {
/* 271 */       return debug0.getAsByte();
/*     */     }
/* 273 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a Byte, was " + getType(debug0));
/*     */   }
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
/*     */   public static byte getAsByte(JsonObject debug0, String debug1, byte debug2) {
/* 286 */     if (debug0.has(debug1)) {
/* 287 */       return convertToByte(debug0.get(debug1), debug1);
/*     */     }
/* 289 */     return debug2;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonObject convertToJsonObject(JsonElement debug0, String debug1) {
/* 390 */     if (debug0.isJsonObject()) {
/* 391 */       return debug0.getAsJsonObject();
/*     */     }
/* 393 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a JsonObject, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject getAsJsonObject(JsonObject debug0, String debug1) {
/* 398 */     if (debug0.has(debug1)) {
/* 399 */       return convertToJsonObject(debug0.get(debug1), debug1);
/*     */     }
/* 401 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a JsonObject");
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject getAsJsonObject(JsonObject debug0, String debug1, JsonObject debug2) {
/* 406 */     if (debug0.has(debug1)) {
/* 407 */       return convertToJsonObject(debug0.get(debug1), debug1);
/*     */     }
/* 409 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray convertToJsonArray(JsonElement debug0, String debug1) {
/* 414 */     if (debug0.isJsonArray()) {
/* 415 */       return debug0.getAsJsonArray();
/*     */     }
/* 417 */     throw new JsonSyntaxException("Expected " + debug1 + " to be a JsonArray, was " + getType(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray getAsJsonArray(JsonObject debug0, String debug1) {
/* 422 */     if (debug0.has(debug1)) {
/* 423 */       return convertToJsonArray(debug0.get(debug1), debug1);
/*     */     }
/* 425 */     throw new JsonSyntaxException("Missing " + debug1 + ", expected to find a JsonArray");
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static JsonArray getAsJsonArray(JsonObject debug0, String debug1, @Nullable JsonArray debug2) {
/* 431 */     if (debug0.has(debug1)) {
/* 432 */       return convertToJsonArray(debug0.get(debug1), debug1);
/*     */     }
/* 434 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T convertToObject(@Nullable JsonElement debug0, String debug1, JsonDeserializationContext debug2, Class<? extends T> debug3) {
/* 439 */     if (debug0 != null) {
/* 440 */       return (T)debug2.deserialize(debug0, debug3);
/*     */     }
/* 442 */     throw new JsonSyntaxException("Missing " + debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T getAsObject(JsonObject debug0, String debug1, JsonDeserializationContext debug2, Class<? extends T> debug3) {
/* 447 */     if (debug0.has(debug1)) {
/* 448 */       return convertToObject(debug0.get(debug1), debug1, debug2, debug3);
/*     */     }
/* 450 */     throw new JsonSyntaxException("Missing " + debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T getAsObject(JsonObject debug0, String debug1, T debug2, JsonDeserializationContext debug3, Class<? extends T> debug4) {
/* 455 */     if (debug0.has(debug1)) {
/* 456 */       return convertToObject(debug0.get(debug1), debug1, debug3, debug4);
/*     */     }
/* 458 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getType(JsonElement debug0) {
/* 463 */     String debug1 = StringUtils.abbreviateMiddle(String.valueOf(debug0), "...", 10);
/* 464 */     if (debug0 == null) {
/* 465 */       return "null (missing)";
/*     */     }
/* 467 */     if (debug0.isJsonNull()) {
/* 468 */       return "null (json)";
/*     */     }
/* 470 */     if (debug0.isJsonArray()) {
/* 471 */       return "an array (" + debug1 + ")";
/*     */     }
/* 473 */     if (debug0.isJsonObject()) {
/* 474 */       return "an object (" + debug1 + ")";
/*     */     }
/* 476 */     if (debug0.isJsonPrimitive()) {
/* 477 */       JsonPrimitive debug2 = debug0.getAsJsonPrimitive();
/* 478 */       if (debug2.isNumber()) {
/* 479 */         return "a number (" + debug1 + ")";
/*     */       }
/* 481 */       if (debug2.isBoolean()) {
/* 482 */         return "a boolean (" + debug1 + ")";
/*     */       }
/*     */     } 
/* 485 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static <T> T fromJson(Gson debug0, Reader debug1, Class<T> debug2, boolean debug3) {
/*     */     try {
/* 491 */       JsonReader debug4 = new JsonReader(debug1);
/* 492 */       debug4.setLenient(debug3);
/* 493 */       return (T)debug0.getAdapter(debug2).read(debug4);
/* 494 */     } catch (IOException debug4) {
/* 495 */       throw new JsonParseException(debug4);
/*     */     } 
/*     */   }
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
/*     */   @Nullable
/*     */   public static <T> T fromJson(Gson debug0, String debug1, Class<T> debug2, boolean debug3) {
/* 517 */     return fromJson(debug0, new StringReader(debug1), debug2, debug3);
/*     */   }
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
/*     */   @Nullable
/*     */   public static <T> T fromJson(Gson debug0, Reader debug1, Class<T> debug2) {
/* 532 */     return fromJson(debug0, debug1, debug2, false);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static <T> T fromJson(Gson debug0, String debug1, Class<T> debug2) {
/* 537 */     return fromJson(debug0, debug1, debug2, false);
/*     */   }
/*     */   
/*     */   public static JsonObject parse(String debug0, boolean debug1) {
/* 541 */     return parse(new StringReader(debug0), debug1);
/*     */   }
/*     */   
/*     */   public static JsonObject parse(Reader debug0, boolean debug1) {
/* 545 */     return fromJson(GSON, debug0, JsonObject.class, debug1);
/*     */   }
/*     */   
/*     */   public static JsonObject parse(String debug0) {
/* 549 */     return parse(debug0, false);
/*     */   }
/*     */   
/*     */   public static JsonObject parse(Reader debug0) {
/* 553 */     return parse(debug0, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\GsonHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */