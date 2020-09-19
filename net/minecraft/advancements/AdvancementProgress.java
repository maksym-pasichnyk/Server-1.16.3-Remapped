/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public class AdvancementProgress
/*     */   implements Comparable<AdvancementProgress> {
/*  24 */   private final Map<String, CriterionProgress> criteria = Maps.newHashMap();
/*  25 */   private String[][] requirements = new String[0][];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(Map<String, Criterion> debug1, String[][] debug2) {
/*  31 */     Set<String> debug3 = debug1.keySet();
/*  32 */     this.criteria.entrySet().removeIf(debug1 -> !debug0.contains(debug1.getKey()));
/*  33 */     for (String debug5 : debug3) {
/*  34 */       if (!this.criteria.containsKey(debug5)) {
/*  35 */         this.criteria.put(debug5, new CriterionProgress());
/*     */       }
/*     */     } 
/*  38 */     this.requirements = debug2;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  42 */     if (this.requirements.length == 0) {
/*  43 */       return false;
/*     */     }
/*  45 */     for (String[] debug4 : this.requirements) {
/*  46 */       boolean debug5 = false;
/*  47 */       for (String debug9 : debug4) {
/*  48 */         CriterionProgress debug10 = getCriterion(debug9);
/*  49 */         if (debug10 != null && debug10.isDone()) {
/*  50 */           debug5 = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*  54 */       if (!debug5) {
/*  55 */         return false;
/*     */       }
/*     */     } 
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hasProgress() {
/*  62 */     for (CriterionProgress debug2 : this.criteria.values()) {
/*  63 */       if (debug2.isDone()) {
/*  64 */         return true;
/*     */       }
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public boolean grantProgress(String debug1) {
/*  71 */     CriterionProgress debug2 = this.criteria.get(debug1);
/*  72 */     if (debug2 != null && !debug2.isDone()) {
/*  73 */       debug2.grant();
/*  74 */       return true;
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   public boolean revokeProgress(String debug1) {
/*  80 */     CriterionProgress debug2 = this.criteria.get(debug1);
/*  81 */     if (debug2 != null && debug2.isDone()) {
/*  82 */       debug2.revoke();
/*  83 */       return true;
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     return "AdvancementProgress{criteria=" + this.criteria + ", requirements=" + 
/*     */       
/*  92 */       Arrays.deepToString((Object[])this.requirements) + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public void serializeToNetwork(FriendlyByteBuf debug1) {
/*  97 */     debug1.writeVarInt(this.criteria.size());
/*  98 */     for (Map.Entry<String, CriterionProgress> debug3 : this.criteria.entrySet()) {
/*  99 */       debug1.writeUtf(debug3.getKey());
/* 100 */       ((CriterionProgress)debug3.getValue()).serializeToNetwork(debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AdvancementProgress fromNetwork(FriendlyByteBuf debug0) {
/* 105 */     AdvancementProgress debug1 = new AdvancementProgress();
/* 106 */     int debug2 = debug0.readVarInt();
/* 107 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 108 */       debug1.criteria.put(debug0.readUtf(32767), CriterionProgress.fromNetwork(debug0));
/*     */     }
/* 110 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public CriterionProgress getCriterion(String debug1) {
/* 115 */     return this.criteria.get(debug1);
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
/*     */   public Iterable<String> getRemainingCriteria() {
/* 161 */     List<String> debug1 = Lists.newArrayList();
/* 162 */     for (Map.Entry<String, CriterionProgress> debug3 : this.criteria.entrySet()) {
/* 163 */       if (!((CriterionProgress)debug3.getValue()).isDone()) {
/* 164 */         debug1.add(debug3.getKey());
/*     */       }
/*     */     } 
/* 167 */     return debug1;
/*     */   }
/*     */   
/*     */   public Iterable<String> getCompletedCriteria() {
/* 171 */     List<String> debug1 = Lists.newArrayList();
/* 172 */     for (Map.Entry<String, CriterionProgress> debug3 : this.criteria.entrySet()) {
/* 173 */       if (((CriterionProgress)debug3.getValue()).isDone()) {
/* 174 */         debug1.add(debug3.getKey());
/*     */       }
/*     */     } 
/* 177 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Date getFirstProgressDate() {
/* 182 */     Date debug1 = null;
/*     */     
/* 184 */     for (CriterionProgress debug3 : this.criteria.values()) {
/* 185 */       if (debug3.isDone() && (debug1 == null || debug3.getObtained().before(debug1))) {
/* 186 */         debug1 = debug3.getObtained();
/*     */       }
/*     */     } 
/*     */     
/* 190 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(AdvancementProgress debug1) {
/* 195 */     Date debug2 = getFirstProgressDate();
/* 196 */     Date debug3 = debug1.getFirstProgressDate();
/*     */     
/* 198 */     if (debug2 == null && debug3 != null) {
/* 199 */       return 1;
/*     */     }
/* 201 */     if (debug2 != null && debug3 == null) {
/* 202 */       return -1;
/*     */     }
/* 204 */     if (debug2 == null && debug3 == null) {
/* 205 */       return 0;
/*     */     }
/*     */     
/* 208 */     return debug2.compareTo(debug3);
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {
/*     */     public JsonElement serialize(AdvancementProgress debug1, Type debug2, JsonSerializationContext debug3) {
/* 214 */       JsonObject debug4 = new JsonObject();
/* 215 */       JsonObject debug5 = new JsonObject();
/* 216 */       for (Map.Entry<String, CriterionProgress> debug7 : (Iterable<Map.Entry<String, CriterionProgress>>)debug1.criteria.entrySet()) {
/* 217 */         CriterionProgress debug8 = debug7.getValue();
/* 218 */         if (debug8.isDone()) {
/* 219 */           debug5.add(debug7.getKey(), debug8.serializeToJson());
/*     */         }
/*     */       } 
/* 222 */       if (!debug5.entrySet().isEmpty()) {
/* 223 */         debug4.add("criteria", (JsonElement)debug5);
/*     */       }
/* 225 */       debug4.addProperty("done", Boolean.valueOf(debug1.isDone()));
/* 226 */       return (JsonElement)debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public AdvancementProgress deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 231 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "advancement");
/* 232 */       JsonObject debug5 = GsonHelper.getAsJsonObject(debug4, "criteria", new JsonObject());
/* 233 */       AdvancementProgress debug6 = new AdvancementProgress();
/*     */       
/* 235 */       for (Map.Entry<String, JsonElement> debug8 : (Iterable<Map.Entry<String, JsonElement>>)debug5.entrySet()) {
/* 236 */         String debug9 = debug8.getKey();
/* 237 */         debug6.criteria.put(debug9, CriterionProgress.fromJson(GsonHelper.convertToString(debug8.getValue(), debug9)));
/*     */       } 
/*     */       
/* 240 */       return debug6;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\AdvancementProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */