/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class ApplyBonusCount extends LootItemConditionalFunction {
/*     */   static interface Formula {
/*     */     int calculateNewCount(Random param1Random, int param1Int1, int param1Int2);
/*     */     
/*     */     void serializeParams(JsonObject param1JsonObject, JsonSerializationContext param1JsonSerializationContext);
/*     */     
/*     */     ResourceLocation getType();
/*     */   }
/*     */   
/*     */   static interface FormulaDeserializer {
/*     */     ApplyBonusCount.Formula deserialize(JsonObject param1JsonObject, JsonDeserializationContext param1JsonDeserializationContext);
/*     */   }
/*     */   
/*     */   static final class BinomialWithBonusCount implements Formula {
/*  38 */     public static final ResourceLocation TYPE = new ResourceLocation("binomial_with_bonus_count");
/*     */     
/*     */     private final int extraRounds;
/*     */     private final float probability;
/*     */     
/*     */     public BinomialWithBonusCount(int debug1, float debug2) {
/*  44 */       this.extraRounds = debug1;
/*  45 */       this.probability = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public int calculateNewCount(Random debug1, int debug2, int debug3) {
/*  50 */       for (int debug4 = 0; debug4 < debug3 + this.extraRounds; debug4++) {
/*  51 */         if (debug1.nextFloat() < this.probability) {
/*  52 */           debug2++;
/*     */         }
/*     */       } 
/*  55 */       return debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeParams(JsonObject debug1, JsonSerializationContext debug2) {
/*  60 */       debug1.addProperty("extra", Integer.valueOf(this.extraRounds));
/*  61 */       debug1.addProperty("probability", Float.valueOf(this.probability));
/*     */     }
/*     */     
/*     */     public static ApplyBonusCount.Formula deserialize(JsonObject debug0, JsonDeserializationContext debug1) {
/*  65 */       int debug2 = GsonHelper.getAsInt(debug0, "extra");
/*  66 */       float debug3 = GsonHelper.getAsFloat(debug0, "probability");
/*  67 */       return new BinomialWithBonusCount(debug2, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getType() {
/*  72 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class UniformBonusCount implements Formula {
/*  77 */     public static final ResourceLocation TYPE = new ResourceLocation("uniform_bonus_count");
/*     */     
/*     */     private final int bonusMultiplier;
/*     */     
/*     */     public UniformBonusCount(int debug1) {
/*  82 */       this.bonusMultiplier = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int calculateNewCount(Random debug1, int debug2, int debug3) {
/*  87 */       return debug2 + debug1.nextInt(this.bonusMultiplier * debug3 + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeParams(JsonObject debug1, JsonSerializationContext debug2) {
/*  92 */       debug1.addProperty("bonusMultiplier", Integer.valueOf(this.bonusMultiplier));
/*     */     }
/*     */     
/*     */     public static ApplyBonusCount.Formula deserialize(JsonObject debug0, JsonDeserializationContext debug1) {
/*  96 */       int debug2 = GsonHelper.getAsInt(debug0, "bonusMultiplier");
/*  97 */       return new UniformBonusCount(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getType() {
/* 102 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class OreDrops implements Formula {
/* 107 */     public static final ResourceLocation TYPE = new ResourceLocation("ore_drops");
/*     */     private OreDrops() {}
/*     */     
/*     */     public int calculateNewCount(Random debug1, int debug2, int debug3) {
/* 111 */       if (debug3 > 0) {
/* 112 */         int debug4 = debug1.nextInt(debug3 + 2) - 1;
/* 113 */         if (debug4 < 0) {
/* 114 */           debug4 = 0;
/*     */         }
/* 116 */         return debug2 * (debug4 + 1);
/*     */       } 
/*     */       
/* 119 */       return debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeParams(JsonObject debug1, JsonSerializationContext debug2) {}
/*     */ 
/*     */     
/*     */     public static ApplyBonusCount.Formula deserialize(JsonObject debug0, JsonDeserializationContext debug1) {
/* 127 */       return new OreDrops();
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getType() {
/* 132 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/* 136 */   private static final Map<ResourceLocation, FormulaDeserializer> FORMULAS = Maps.newHashMap(); private final Enchantment enchantment;
/*     */   
/*     */   static {
/* 139 */     FORMULAS.put(BinomialWithBonusCount.TYPE, BinomialWithBonusCount::deserialize);
/* 140 */     FORMULAS.put(OreDrops.TYPE, OreDrops::deserialize);
/* 141 */     FORMULAS.put(UniformBonusCount.TYPE, UniformBonusCount::deserialize);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Formula formula;
/*     */ 
/*     */   
/*     */   private ApplyBonusCount(LootItemCondition[] debug1, Enchantment debug2, Formula debug3) {
/* 149 */     super(debug1);
/* 150 */     this.enchantment = debug2;
/* 151 */     this.formula = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/* 156 */     return LootItemFunctions.APPLY_BONUS;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 161 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.TOOL);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 166 */     ItemStack debug3 = (ItemStack)debug2.getParamOrNull(LootContextParams.TOOL);
/*     */     
/* 168 */     if (debug3 != null) {
/* 169 */       int debug4 = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, debug3);
/* 170 */       int debug5 = this.formula.calculateNewCount(debug2.getRandom(), debug1.getCount(), debug4);
/* 171 */       debug1.setCount(debug5);
/*     */     } 
/* 173 */     return debug1;
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addBonusBinomialDistributionCount(Enchantment debug0, float debug1, int debug2) {
/* 177 */     return simpleBuilder(debug3 -> new ApplyBonusCount(debug3, debug0, new BinomialWithBonusCount(debug1, debug2)));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addOreBonusCount(Enchantment debug0) {
/* 181 */     return simpleBuilder(debug1 -> new ApplyBonusCount(debug1, debug0, new OreDrops()));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Enchantment debug0) {
/* 185 */     return simpleBuilder(debug1 -> new ApplyBonusCount(debug1, debug0, new UniformBonusCount(1)));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Enchantment debug0, int debug1) {
/* 189 */     return simpleBuilder(debug2 -> new ApplyBonusCount(debug2, debug0, new UniformBonusCount(debug1)));
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<ApplyBonusCount> {
/*     */     public void serialize(JsonObject debug1, ApplyBonusCount debug2, JsonSerializationContext debug3) {
/* 195 */       super.serialize(debug1, debug2, debug3);
/*     */       
/* 197 */       debug1.addProperty("enchantment", Registry.ENCHANTMENT.getKey(debug2.enchantment).toString());
/* 198 */       debug1.addProperty("formula", debug2.formula.getType().toString());
/*     */       
/* 200 */       JsonObject debug4 = new JsonObject();
/* 201 */       debug2.formula.serializeParams(debug4, debug3);
/* 202 */       if (debug4.size() > 0) {
/* 203 */         debug1.add("parameters", (JsonElement)debug4);
/*     */       }
/*     */     }
/*     */     
/*     */     public ApplyBonusCount deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/*     */       ApplyBonusCount.Formula debug8;
/* 209 */       ResourceLocation debug4 = new ResourceLocation(GsonHelper.getAsString(debug1, "enchantment"));
/* 210 */       Enchantment debug5 = (Enchantment)Registry.ENCHANTMENT.getOptional(debug4).orElseThrow(() -> new JsonParseException("Invalid enchantment id: " + debug0));
/* 211 */       ResourceLocation debug6 = new ResourceLocation(GsonHelper.getAsString(debug1, "formula"));
/* 212 */       ApplyBonusCount.FormulaDeserializer debug7 = (ApplyBonusCount.FormulaDeserializer)ApplyBonusCount.FORMULAS.get(debug6);
/* 213 */       if (debug7 == null) {
/* 214 */         throw new JsonParseException("Invalid formula id: " + debug6);
/*     */       }
/*     */ 
/*     */       
/* 218 */       if (debug1.has("parameters")) {
/* 219 */         debug8 = debug7.deserialize(GsonHelper.getAsJsonObject(debug1, "parameters"), debug2);
/*     */       } else {
/* 221 */         debug8 = debug7.deserialize(new JsonObject(), debug2);
/*     */       } 
/*     */       
/* 224 */       return new ApplyBonusCount(debug3, debug5, debug8);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */