/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.lang.reflect.Type;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class BlockEntitySignTextStrictJsonFix
/*     */   extends NamedEntityFix {
/*     */   public BlockEntitySignTextStrictJsonFix(Schema debug1, boolean debug2) {
/*  24 */     super(debug1, debug2, "BlockEntitySignTextStrictJsonFix", References.BLOCK_ENTITY, "Sign");
/*     */   }
/*     */   
/*  27 */   public static final Gson GSON = (new GsonBuilder())
/*  28 */     .registerTypeAdapter(Component.class, new JsonDeserializer<Component>()
/*     */       {
/*     */         public MutableComponent deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/*  31 */           if (debug1.isJsonPrimitive())
/*     */           {
/*  33 */             return (MutableComponent)new TextComponent(debug1.getAsString()); } 
/*  34 */           if (debug1.isJsonArray()) {
/*     */             
/*  36 */             JsonArray debug4 = debug1.getAsJsonArray();
/*  37 */             MutableComponent debug5 = null;
/*     */             
/*  39 */             for (JsonElement debug7 : debug4) {
/*  40 */               MutableComponent debug8 = deserialize(debug7, debug7.getClass(), debug3);
/*  41 */               if (debug5 == null) {
/*  42 */                 debug5 = debug8; continue;
/*     */               } 
/*  44 */               debug5.append((Component)debug8);
/*     */             } 
/*     */ 
/*     */             
/*  48 */             return debug5;
/*     */           } 
/*  50 */           throw new JsonParseException("Don't know how to turn " + debug1 + " into a Component");
/*     */         }
/*  54 */       }).create();
/*     */   private Dynamic<?> updateLine(Dynamic<?> debug1, String debug2) {
/*     */     TextComponent textComponent;
/*  57 */     String debug3 = debug1.get(debug2).asString("");
/*     */     
/*  59 */     Component debug4 = null;
/*  60 */     if ("null".equals(debug3) || StringUtils.isEmpty(debug3)) {
/*  61 */       debug4 = TextComponent.EMPTY;
/*  62 */     } else if ((debug3
/*  63 */       .charAt(0) == '"' && debug3.charAt(debug3.length() - 1) == '"') || (debug3
/*  64 */       .charAt(0) == '{' && debug3.charAt(debug3.length() - 1) == '}')) {
/*     */       MutableComponent mutableComponent;
/*     */       try {
/*  67 */         debug4 = (Component)GsonHelper.fromJson(GSON, debug3, Component.class, true);
/*  68 */         if (debug4 == null) {
/*  69 */           debug4 = TextComponent.EMPTY;
/*     */         }
/*  71 */       } catch (JsonParseException jsonParseException) {}
/*     */ 
/*     */       
/*  74 */       if (debug4 == null) {
/*     */         try {
/*  76 */           mutableComponent = Component.Serializer.fromJson(debug3);
/*  77 */         } catch (JsonParseException jsonParseException) {}
/*     */       }
/*     */ 
/*     */       
/*  81 */       if (mutableComponent == null) {
/*     */         try {
/*  83 */           mutableComponent = Component.Serializer.fromJsonLenient(debug3);
/*  84 */         } catch (JsonParseException jsonParseException) {}
/*     */       }
/*     */ 
/*     */       
/*  88 */       if (mutableComponent == null) {
/*  89 */         textComponent = new TextComponent(debug3);
/*     */       }
/*     */     } else {
/*  92 */       textComponent = new TextComponent(debug3);
/*     */     } 
/*     */     
/*  95 */     return debug1.set(debug2, debug1.createString(Component.Serializer.toJson((Component)textComponent)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Typed<?> fix(Typed<?> debug1) {
/* 100 */     return debug1.update(DSL.remainderFinder(), debug1 -> {
/*     */           debug1 = updateLine(debug1, "Text1");
/*     */           debug1 = updateLine(debug1, "Text2");
/*     */           debug1 = updateLine(debug1, "Text3");
/*     */           return updateLine(debug1, "Text4");
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntitySignTextStrictJsonFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */