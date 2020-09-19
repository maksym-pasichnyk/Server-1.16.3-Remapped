/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LowerCaseEnumTypeAdapterFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/*    */   @Nullable
/*    */   public <T> TypeAdapter<T> create(Gson debug1, TypeToken<T> debug2) {
/* 24 */     Class<T> debug3 = debug2.getRawType();
/* 25 */     if (!debug3.isEnum()) {
/* 26 */       return null;
/*    */     }
/*    */     
/* 29 */     final Map<String, T> lowercaseToConstant = Maps.newHashMap();
/* 30 */     for (T debug8 : debug3.getEnumConstants()) {
/* 31 */       debug4.put(toLowercase(debug8), debug8);
/*    */     }
/*    */     
/* 34 */     return new TypeAdapter<T>()
/*    */       {
/*    */         public void write(JsonWriter debug1, T debug2) throws IOException {
/* 37 */           if (debug2 == null) {
/* 38 */             debug1.nullValue();
/*    */           } else {
/* 40 */             debug1.value(LowerCaseEnumTypeAdapterFactory.this.toLowercase(debug2));
/*    */           } 
/*    */         }
/*    */ 
/*    */         
/*    */         @Nullable
/*    */         public T read(JsonReader debug1) throws IOException {
/* 47 */           if (debug1.peek() == JsonToken.NULL) {
/* 48 */             debug1.nextNull();
/* 49 */             return null;
/*    */           } 
/* 51 */           return (T)lowercaseToConstant.get(debug1.nextString());
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   private String toLowercase(Object debug1) {
/* 58 */     if (debug1 instanceof Enum) {
/* 59 */       return ((Enum)debug1).name().toLowerCase(Locale.ROOT);
/*    */     }
/* 61 */     return debug1.toString().toLowerCase(Locale.ROOT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\LowerCaseEnumTypeAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */