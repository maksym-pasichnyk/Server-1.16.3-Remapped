/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.function.IntUnaryOperator;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class IntLimiter
/*    */   implements IntUnaryOperator
/*    */ {
/*    */   private final Integer min;
/*    */   private final Integer max;
/*    */   private final IntUnaryOperator op;
/*    */   
/*    */   private IntLimiter(@Nullable Integer debug1, @Nullable Integer debug2) {
/* 24 */     this.min = debug1;
/* 25 */     this.max = debug2;
/*    */     
/* 27 */     if (debug1 == null) {
/* 28 */       if (debug2 == null) {
/* 29 */         this.op = (debug0 -> debug0);
/*    */       } else {
/* 31 */         int debug3 = debug2.intValue();
/* 32 */         this.op = (debug1 -> Math.min(debug0, debug1));
/*    */       } 
/*    */     } else {
/* 35 */       int debug3 = debug1.intValue();
/* 36 */       if (debug2 == null) {
/* 37 */         this.op = (debug1 -> Math.max(debug0, debug1));
/*    */       } else {
/* 39 */         int debug4 = debug2.intValue();
/* 40 */         this.op = (debug2 -> Mth.clamp(debug2, debug0, debug1));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static IntLimiter clamp(int debug0, int debug1) {
/* 46 */     return new IntLimiter(Integer.valueOf(debug0), Integer.valueOf(debug1));
/*    */   }
/*    */   
/*    */   public static IntLimiter lowerBound(int debug0) {
/* 50 */     return new IntLimiter(Integer.valueOf(debug0), null);
/*    */   }
/*    */   
/*    */   public static IntLimiter upperBound(int debug0) {
/* 54 */     return new IntLimiter(null, Integer.valueOf(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public int applyAsInt(int debug1) {
/* 59 */     return this.op.applyAsInt(debug1);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<IntLimiter>, JsonSerializer<IntLimiter> {
/*    */     public IntLimiter deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 65 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "value");
/* 66 */       Integer debug5 = debug4.has("min") ? Integer.valueOf(GsonHelper.getAsInt(debug4, "min")) : null;
/* 67 */       Integer debug6 = debug4.has("max") ? Integer.valueOf(GsonHelper.getAsInt(debug4, "max")) : null;
/* 68 */       return new IntLimiter(debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonElement serialize(IntLimiter debug1, Type debug2, JsonSerializationContext debug3) {
/* 73 */       JsonObject debug4 = new JsonObject();
/* 74 */       if (debug1.max != null) {
/* 75 */         debug4.addProperty("max", debug1.max);
/*    */       }
/*    */       
/* 78 */       if (debug1.min != null) {
/* 79 */         debug4.addProperty("min", debug1.min);
/*    */       }
/*    */       
/* 82 */       return (JsonElement)debug4;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\IntLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */