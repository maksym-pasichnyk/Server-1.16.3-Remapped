/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class RandomIntGenerators
/*    */ {
/* 15 */   private static final Map<ResourceLocation, Class<? extends RandomIntGenerator>> GENERATORS = Maps.newHashMap();
/*    */   
/*    */   static {
/* 18 */     GENERATORS.put(RandomIntGenerator.UNIFORM, RandomValueBounds.class);
/* 19 */     GENERATORS.put(RandomIntGenerator.BINOMIAL, BinomialDistributionGenerator.class);
/* 20 */     GENERATORS.put(RandomIntGenerator.CONSTANT, ConstantIntValue.class);
/*    */   }
/*    */   
/*    */   public static RandomIntGenerator deserialize(JsonElement debug0, JsonDeserializationContext debug1) throws JsonParseException {
/* 24 */     if (debug0.isJsonPrimitive()) {
/* 25 */       return (RandomIntGenerator)debug1.deserialize(debug0, ConstantIntValue.class);
/*    */     }
/*    */     
/* 28 */     JsonObject debug2 = debug0.getAsJsonObject();
/* 29 */     String debug3 = GsonHelper.getAsString(debug2, "type", RandomIntGenerator.UNIFORM.toString());
/*    */     
/* 31 */     Class<? extends RandomIntGenerator> debug4 = GENERATORS.get(new ResourceLocation(debug3));
/* 32 */     if (debug4 == null) {
/* 33 */       throw new JsonParseException("Unknown generator: " + debug3);
/*    */     }
/*    */     
/* 36 */     return (RandomIntGenerator)debug1.deserialize((JsonElement)debug2, debug4);
/*    */   }
/*    */   
/*    */   public static JsonElement serialize(RandomIntGenerator debug0, JsonSerializationContext debug1) {
/* 40 */     JsonElement debug2 = debug1.serialize(debug0);
/* 41 */     if (debug2.isJsonObject()) {
/* 42 */       debug2.getAsJsonObject().addProperty("type", debug0.getType().toString());
/*    */     }
/*    */     
/* 45 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\RandomIntGenerators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */