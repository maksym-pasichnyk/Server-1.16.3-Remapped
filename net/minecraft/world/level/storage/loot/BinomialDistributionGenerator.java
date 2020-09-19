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
/*    */ import java.util.Random;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public final class BinomialDistributionGenerator
/*    */   implements RandomIntGenerator
/*    */ {
/*    */   private final int n;
/*    */   private final float p;
/*    */   
/*    */   public BinomialDistributionGenerator(int debug1, float debug2) {
/* 22 */     this.n = debug1;
/* 23 */     this.p = debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getInt(Random debug1) {
/* 30 */     int debug2 = 0;
/* 31 */     for (int debug3 = 0; debug3 < this.n; debug3++) {
/* 32 */       if (debug1.nextFloat() < this.p) {
/* 33 */         debug2++;
/*    */       }
/*    */     } 
/*    */     
/* 37 */     return debug2;
/*    */   }
/*    */   
/*    */   public static BinomialDistributionGenerator binomial(int debug0, float debug1) {
/* 41 */     return new BinomialDistributionGenerator(debug0, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getType() {
/* 46 */     return BINOMIAL;
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<BinomialDistributionGenerator>, JsonSerializer<BinomialDistributionGenerator> {
/*    */     public BinomialDistributionGenerator deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 52 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "value");
/* 53 */       int debug5 = GsonHelper.getAsInt(debug4, "n");
/* 54 */       float debug6 = GsonHelper.getAsFloat(debug4, "p");
/* 55 */       return new BinomialDistributionGenerator(debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonElement serialize(BinomialDistributionGenerator debug1, Type debug2, JsonSerializationContext debug3) {
/* 60 */       JsonObject debug4 = new JsonObject();
/* 61 */       debug4.addProperty("n", Integer.valueOf(debug1.n));
/* 62 */       debug4.addProperty("p", Float.valueOf(debug1.p));
/* 63 */       return (JsonElement)debug4;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\BinomialDistributionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */