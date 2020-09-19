/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.LowerCaseEnumTypeAdapterFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Component
/*     */   extends Message, FormattedText
/*     */ {
/*     */   default String getString() {
/*  39 */     return super.getString();
/*     */   }
/*     */   
/*     */   default String getString(int debug1) {
/*  43 */     StringBuilder debug2 = new StringBuilder();
/*  44 */     visit(debug2 -> {
/*     */           int debug3 = debug0 - debug1.length();
/*     */           if (debug3 <= 0) {
/*     */             return STOP_ITERATION;
/*     */           }
/*     */           debug1.append((debug2.length() <= debug3) ? debug2 : debug2.substring(0, debug3));
/*     */           return Optional.empty();
/*     */         });
/*  52 */     return debug2.toString();
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
/*     */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> debug1) {
/*  96 */     Optional<T> debug2 = visitSelf(debug1);
/*  97 */     if (debug2.isPresent()) {
/*  98 */       return debug2;
/*     */     }
/*     */     
/* 101 */     for (Component debug4 : getSiblings()) {
/* 102 */       Optional<T> debug5 = debug4.visit(debug1);
/* 103 */       if (debug5.isPresent()) {
/* 104 */         return debug5;
/*     */       }
/*     */     } 
/*     */     
/* 108 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visitSelf(FormattedText.ContentConsumer<T> debug1) {
/* 116 */     return debug1.accept(getContents());
/*     */   }
/*     */   Style getStyle();
/*     */   
/*     */   String getContents();
/*     */   
/*     */   List<Component> getSiblings();
/*     */   
/*     */   MutableComponent plainCopy();
/*     */   
/*     */   MutableComponent copy();
/*     */   
/*     */   public static class Serializer implements JsonDeserializer<MutableComponent>, JsonSerializer<Component> { private static final Gson GSON;
/*     */     
/*     */     static {
/* 131 */       GSON = (Gson)Util.make(() -> {
/*     */             GsonBuilder debug0 = new GsonBuilder();
/*     */             
/*     */             debug0.disableHtmlEscaping();
/*     */             debug0.registerTypeHierarchyAdapter(Component.class, new Serializer());
/*     */             debug0.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
/*     */             debug0.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/*     */             return debug0.create();
/*     */           });
/* 140 */       JSON_READER_POS = (Field)Util.make(() -> {
/*     */             try {
/*     */               new JsonReader(new StringReader(""));
/*     */               Field debug0 = JsonReader.class.getDeclaredField("pos");
/*     */               debug0.setAccessible(true);
/*     */               return debug0;
/* 146 */             } catch (NoSuchFieldException debug0) {
/*     */               throw new IllegalStateException("Couldn't get field 'pos' for JsonReader", debug0);
/*     */             } 
/*     */           });
/*     */       
/* 151 */       JSON_READER_LINESTART = (Field)Util.make(() -> {
/*     */             try {
/*     */               new JsonReader(new StringReader(""));
/*     */               Field debug0 = JsonReader.class.getDeclaredField("lineStart");
/*     */               debug0.setAccessible(true);
/*     */               return debug0;
/* 157 */             } catch (NoSuchFieldException debug0) {
/*     */               throw new IllegalStateException("Couldn't get field 'lineStart' for JsonReader", debug0);
/*     */             } 
/*     */           });
/*     */     }
/*     */     private static final Field JSON_READER_POS; private static final Field JSON_READER_LINESTART;
/*     */     public MutableComponent deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 164 */       if (debug1.isJsonPrimitive())
/*     */       {
/* 166 */         return new TextComponent(debug1.getAsString()); } 
/* 167 */       if (debug1.isJsonObject()) {
/* 168 */         MutableComponent debug5; JsonObject debug4 = debug1.getAsJsonObject();
/*     */ 
/*     */         
/* 171 */         if (debug4.has("text")) {
/* 172 */           debug5 = new TextComponent(GsonHelper.getAsString(debug4, "text"));
/* 173 */         } else if (debug4.has("translate")) {
/* 174 */           String debug6 = GsonHelper.getAsString(debug4, "translate");
/*     */           
/* 176 */           if (debug4.has("with")) {
/* 177 */             JsonArray debug7 = GsonHelper.getAsJsonArray(debug4, "with");
/* 178 */             Object[] debug8 = new Object[debug7.size()];
/*     */             
/* 180 */             for (int debug9 = 0; debug9 < debug8.length; debug9++) {
/* 181 */               debug8[debug9] = deserialize(debug7.get(debug9), debug2, debug3);
/*     */               
/* 183 */               if (debug8[debug9] instanceof TextComponent) {
/* 184 */                 TextComponent debug10 = (TextComponent)debug8[debug9];
/* 185 */                 if (debug10.getStyle().isEmpty() && debug10.getSiblings().isEmpty()) {
/* 186 */                   debug8[debug9] = debug10.getText();
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 191 */             debug5 = new TranslatableComponent(debug6, debug8);
/*     */           } else {
/* 193 */             debug5 = new TranslatableComponent(debug6);
/*     */           } 
/* 195 */         } else if (debug4.has("score")) {
/* 196 */           JsonObject debug6 = GsonHelper.getAsJsonObject(debug4, "score");
/* 197 */           if (debug6.has("name") && debug6.has("objective")) {
/* 198 */             debug5 = new ScoreComponent(GsonHelper.getAsString(debug6, "name"), GsonHelper.getAsString(debug6, "objective"));
/*     */           } else {
/* 200 */             throw new JsonParseException("A score component needs a least a name and an objective");
/*     */           } 
/* 202 */         } else if (debug4.has("selector")) {
/* 203 */           debug5 = new SelectorComponent(GsonHelper.getAsString(debug4, "selector"));
/* 204 */         } else if (debug4.has("keybind")) {
/* 205 */           debug5 = new KeybindComponent(GsonHelper.getAsString(debug4, "keybind"));
/* 206 */         } else if (debug4.has("nbt")) {
/* 207 */           String debug6 = GsonHelper.getAsString(debug4, "nbt");
/* 208 */           boolean debug7 = GsonHelper.getAsBoolean(debug4, "interpret", false);
/* 209 */           if (debug4.has("block")) {
/* 210 */             debug5 = new NbtComponent.BlockNbtComponent(debug6, debug7, GsonHelper.getAsString(debug4, "block"));
/* 211 */           } else if (debug4.has("entity")) {
/* 212 */             debug5 = new NbtComponent.EntityNbtComponent(debug6, debug7, GsonHelper.getAsString(debug4, "entity"));
/* 213 */           } else if (debug4.has("storage")) {
/* 214 */             debug5 = new NbtComponent.StorageNbtComponent(debug6, debug7, new ResourceLocation(GsonHelper.getAsString(debug4, "storage")));
/*     */           } else {
/* 216 */             throw new JsonParseException("Don't know how to turn " + debug1 + " into a Component");
/*     */           } 
/*     */         } else {
/* 219 */           throw new JsonParseException("Don't know how to turn " + debug1 + " into a Component");
/*     */         } 
/*     */         
/* 222 */         if (debug4.has("extra")) {
/* 223 */           JsonArray debug6 = GsonHelper.getAsJsonArray(debug4, "extra");
/*     */           
/* 225 */           if (debug6.size() > 0) {
/* 226 */             for (int debug7 = 0; debug7 < debug6.size(); debug7++) {
/* 227 */               debug5.append(deserialize(debug6.get(debug7), debug2, debug3));
/*     */             }
/*     */           } else {
/* 230 */             throw new JsonParseException("Unexpected empty array of components");
/*     */           } 
/*     */         } 
/*     */         
/* 234 */         debug5.setStyle((Style)debug3.deserialize(debug1, Style.class));
/*     */         
/* 236 */         return debug5;
/* 237 */       }  if (debug1.isJsonArray()) {
/*     */         
/* 239 */         JsonArray debug4 = debug1.getAsJsonArray();
/* 240 */         MutableComponent debug5 = null;
/*     */         
/* 242 */         for (JsonElement debug7 : debug4) {
/* 243 */           MutableComponent debug8 = deserialize(debug7, debug7.getClass(), debug3);
/* 244 */           if (debug5 == null) {
/* 245 */             debug5 = debug8; continue;
/*     */           } 
/* 247 */           debug5.append(debug8);
/*     */         } 
/*     */ 
/*     */         
/* 251 */         return debug5;
/*     */       } 
/* 253 */       throw new JsonParseException("Don't know how to turn " + debug1 + " into a Component");
/*     */     }
/*     */ 
/*     */     
/*     */     private void serializeStyle(Style debug1, JsonObject debug2, JsonSerializationContext debug3) {
/* 258 */       JsonElement debug4 = debug3.serialize(debug1);
/*     */       
/* 260 */       if (debug4.isJsonObject()) {
/* 261 */         JsonObject debug5 = (JsonObject)debug4;
/* 262 */         for (Map.Entry<String, JsonElement> debug7 : (Iterable<Map.Entry<String, JsonElement>>)debug5.entrySet()) {
/* 263 */           debug2.add(debug7.getKey(), debug7.getValue());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(Component debug1, Type debug2, JsonSerializationContext debug3) {
/* 270 */       JsonObject debug4 = new JsonObject();
/*     */       
/* 272 */       if (!debug1.getStyle().isEmpty()) {
/* 273 */         serializeStyle(debug1.getStyle(), debug4, debug3);
/*     */       }
/*     */       
/* 276 */       if (!debug1.getSiblings().isEmpty()) {
/* 277 */         JsonArray debug5 = new JsonArray();
/*     */         
/* 279 */         for (Component debug7 : debug1.getSiblings()) {
/* 280 */           debug5.add(serialize(debug7, debug7.getClass(), debug3));
/*     */         }
/*     */         
/* 283 */         debug4.add("extra", (JsonElement)debug5);
/*     */       } 
/*     */       
/* 286 */       if (debug1 instanceof TextComponent) {
/* 287 */         debug4.addProperty("text", ((TextComponent)debug1).getText());
/* 288 */       } else if (debug1 instanceof TranslatableComponent) {
/* 289 */         TranslatableComponent debug5 = (TranslatableComponent)debug1;
/* 290 */         debug4.addProperty("translate", debug5.getKey());
/*     */         
/* 292 */         if (debug5.getArgs() != null && (debug5.getArgs()).length > 0) {
/* 293 */           JsonArray debug6 = new JsonArray();
/*     */           
/* 295 */           for (Object debug10 : debug5.getArgs()) {
/* 296 */             if (debug10 instanceof Component) {
/* 297 */               debug6.add(serialize((Component)debug10, debug10.getClass(), debug3));
/*     */             } else {
/* 299 */               debug6.add((JsonElement)new JsonPrimitive(String.valueOf(debug10)));
/*     */             } 
/*     */           } 
/*     */           
/* 303 */           debug4.add("with", (JsonElement)debug6);
/*     */         } 
/* 305 */       } else if (debug1 instanceof ScoreComponent) {
/* 306 */         ScoreComponent debug5 = (ScoreComponent)debug1;
/* 307 */         JsonObject debug6 = new JsonObject();
/* 308 */         debug6.addProperty("name", debug5.getName());
/* 309 */         debug6.addProperty("objective", debug5.getObjective());
/* 310 */         debug4.add("score", (JsonElement)debug6);
/* 311 */       } else if (debug1 instanceof SelectorComponent) {
/* 312 */         SelectorComponent debug5 = (SelectorComponent)debug1;
/* 313 */         debug4.addProperty("selector", debug5.getPattern());
/* 314 */       } else if (debug1 instanceof KeybindComponent) {
/* 315 */         KeybindComponent debug5 = (KeybindComponent)debug1;
/* 316 */         debug4.addProperty("keybind", debug5.getName());
/* 317 */       } else if (debug1 instanceof NbtComponent) {
/* 318 */         NbtComponent debug5 = (NbtComponent)debug1;
/* 319 */         debug4.addProperty("nbt", debug5.getNbtPath());
/* 320 */         debug4.addProperty("interpret", Boolean.valueOf(debug5.isInterpreting()));
/* 321 */         if (debug1 instanceof NbtComponent.BlockNbtComponent) {
/* 322 */           NbtComponent.BlockNbtComponent debug6 = (NbtComponent.BlockNbtComponent)debug1;
/* 323 */           debug4.addProperty("block", debug6.getPos());
/* 324 */         } else if (debug1 instanceof NbtComponent.EntityNbtComponent) {
/* 325 */           NbtComponent.EntityNbtComponent debug6 = (NbtComponent.EntityNbtComponent)debug1;
/* 326 */           debug4.addProperty("entity", debug6.getSelector());
/* 327 */         } else if (debug1 instanceof NbtComponent.StorageNbtComponent) {
/* 328 */           NbtComponent.StorageNbtComponent debug6 = (NbtComponent.StorageNbtComponent)debug1;
/* 329 */           debug4.addProperty("storage", debug6.getId().toString());
/*     */         } else {
/* 331 */           throw new IllegalArgumentException("Don't know how to serialize " + debug1 + " as a Component");
/*     */         } 
/*     */       } else {
/* 334 */         throw new IllegalArgumentException("Don't know how to serialize " + debug1 + " as a Component");
/*     */       } 
/*     */       
/* 337 */       return (JsonElement)debug4;
/*     */     }
/*     */     
/*     */     public static String toJson(Component debug0) {
/* 341 */       return GSON.toJson(debug0);
/*     */     }
/*     */     
/*     */     public static JsonElement toJsonTree(Component debug0) {
/* 345 */       return GSON.toJsonTree(debug0);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static MutableComponent fromJson(String debug0) {
/* 350 */       return (MutableComponent)GsonHelper.fromJson(GSON, debug0, MutableComponent.class, false);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static MutableComponent fromJson(JsonElement debug0) {
/* 355 */       return (MutableComponent)GSON.fromJson(debug0, MutableComponent.class);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static MutableComponent fromJsonLenient(String debug0) {
/* 360 */       return (MutableComponent)GsonHelper.fromJson(GSON, debug0, MutableComponent.class, true);
/*     */     }
/*     */     
/*     */     public static MutableComponent fromJson(StringReader debug0) {
/*     */       try {
/* 365 */         JsonReader debug1 = new JsonReader(new StringReader(debug0.getRemaining()));
/* 366 */         debug1.setLenient(false);
/* 367 */         MutableComponent debug2 = (MutableComponent)GSON.getAdapter(MutableComponent.class).read(debug1);
/* 368 */         debug0.setCursor(debug0.getCursor() + getPos(debug1));
/* 369 */         return debug2;
/* 370 */       } catch (IOException|StackOverflowError debug1) {
/* 371 */         throw new JsonParseException(debug1);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static int getPos(JsonReader debug0) {
/*     */       try {
/* 377 */         return JSON_READER_POS.getInt(debug0) - JSON_READER_LINESTART.getInt(debug0) + 1;
/* 378 */       } catch (IllegalAccessException debug1) {
/* 379 */         throw new IllegalStateException("Couldn't read position of JsonReader", debug1);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */