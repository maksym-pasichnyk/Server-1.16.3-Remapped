/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.advancements.critereon.DeserializationContext;
/*    */ import net.minecraft.advancements.critereon.SerializationContext;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class Criterion
/*    */ {
/*    */   private final CriterionTriggerInstance trigger;
/*    */   
/*    */   public Criterion(CriterionTriggerInstance debug1) {
/* 20 */     this.trigger = debug1;
/*    */   }
/*    */   
/*    */   public Criterion() {
/* 24 */     this.trigger = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(FriendlyByteBuf debug1) {}
/*    */   
/*    */   public static Criterion criterionFromJson(JsonObject debug0, DeserializationContext debug1) {
/* 31 */     ResourceLocation debug2 = new ResourceLocation(GsonHelper.getAsString(debug0, "trigger"));
/* 32 */     CriterionTrigger<?> debug3 = CriteriaTriggers.getCriterion(debug2);
/* 33 */     if (debug3 == null) {
/* 34 */       throw new JsonSyntaxException("Invalid criterion trigger: " + debug2);
/*    */     }
/* 36 */     CriterionTriggerInstance debug4 = (CriterionTriggerInstance)debug3.createInstance(GsonHelper.getAsJsonObject(debug0, "conditions", new JsonObject()), debug1);
/* 37 */     return new Criterion(debug4);
/*    */   }
/*    */   
/*    */   public static Criterion criterionFromNetwork(FriendlyByteBuf debug0) {
/* 41 */     return new Criterion();
/*    */   }
/*    */   
/*    */   public static Map<String, Criterion> criteriaFromJson(JsonObject debug0, DeserializationContext debug1) {
/* 45 */     Map<String, Criterion> debug2 = Maps.newHashMap();
/* 46 */     for (Map.Entry<String, JsonElement> debug4 : (Iterable<Map.Entry<String, JsonElement>>)debug0.entrySet()) {
/* 47 */       debug2.put(debug4.getKey(), criterionFromJson(GsonHelper.convertToJsonObject(debug4.getValue(), "criterion"), debug1));
/*    */     }
/* 49 */     return debug2;
/*    */   }
/*    */   
/*    */   public static Map<String, Criterion> criteriaFromNetwork(FriendlyByteBuf debug0) {
/* 53 */     Map<String, Criterion> debug1 = Maps.newHashMap();
/* 54 */     int debug2 = debug0.readVarInt();
/* 55 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 56 */       debug1.put(debug0.readUtf(32767), criterionFromNetwork(debug0));
/*    */     }
/* 58 */     return debug1;
/*    */   }
/*    */   
/*    */   public static void serializeToNetwork(Map<String, Criterion> debug0, FriendlyByteBuf debug1) {
/* 62 */     debug1.writeVarInt(debug0.size());
/* 63 */     for (Map.Entry<String, Criterion> debug3 : debug0.entrySet()) {
/* 64 */       debug1.writeUtf(debug3.getKey());
/* 65 */       ((Criterion)debug3.getValue()).serializeToNetwork(debug1);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public CriterionTriggerInstance getTrigger() {
/* 71 */     return this.trigger;
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 75 */     JsonObject debug1 = new JsonObject();
/* 76 */     debug1.addProperty("trigger", this.trigger.getCriterion().toString());
/* 77 */     JsonObject debug2 = this.trigger.serializeToJson(SerializationContext.INSTANCE);
/* 78 */     if (debug2.size() != 0) {
/* 79 */       debug1.add("conditions", (JsonElement)debug2);
/*    */     }
/* 81 */     return (JsonElement)debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\Criterion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */