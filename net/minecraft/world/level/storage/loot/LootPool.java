/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class LootPool
/*     */ {
/*     */   private final LootPoolEntryContainer[] entries;
/*     */   private final LootItemCondition[] conditions;
/*     */   private final Predicate<LootContext> compositeCondition;
/*     */   private final LootItemFunction[] functions;
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   private final RandomIntGenerator rolls;
/*     */   private final RandomValueBounds bonusRolls;
/*     */   
/*     */   private LootPool(LootPoolEntryContainer[] debug1, LootItemCondition[] debug2, LootItemFunction[] debug3, RandomIntGenerator debug4, RandomValueBounds debug5) {
/*  42 */     this.entries = debug1;
/*  43 */     this.conditions = debug2;
/*  44 */     this.compositeCondition = LootItemConditions.andConditions((Predicate[])debug2);
/*  45 */     this.functions = debug3;
/*  46 */     this.compositeFunction = LootItemFunctions.compose((BiFunction[])debug3);
/*  47 */     this.rolls = debug4;
/*  48 */     this.bonusRolls = debug5;
/*     */   }
/*     */   
/*     */   private void addRandomItem(Consumer<ItemStack> debug1, LootContext debug2) {
/*  52 */     Random debug3 = debug2.getRandom();
/*  53 */     List<LootPoolEntry> debug4 = Lists.newArrayList();
/*  54 */     MutableInt debug5 = new MutableInt();
/*  55 */     for (LootPoolEntryContainer debug9 : this.entries) {
/*  56 */       debug9.expand(debug2, debug3 -> {
/*     */             int debug4 = debug3.getWeight(debug0.getLuck());
/*     */             
/*     */             if (debug4 > 0) {
/*     */               debug1.add(debug3);
/*     */               debug2.add(debug4);
/*     */             } 
/*     */           });
/*     */     } 
/*  65 */     int debug6 = debug4.size();
/*  66 */     if (debug5.intValue() == 0 || debug6 == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     if (debug6 == 1) {
/*  71 */       ((LootPoolEntry)debug4.get(0)).createItemStack(debug1, debug2);
/*     */       
/*     */       return;
/*     */     } 
/*  75 */     int debug7 = debug3.nextInt(debug5.intValue());
/*  76 */     for (LootPoolEntry debug9 : debug4) {
/*  77 */       debug7 -= debug9.getWeight(debug2.getLuck());
/*  78 */       if (debug7 < 0) {
/*  79 */         debug9.createItemStack(debug1, debug2);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addRandomItems(Consumer<ItemStack> debug1, LootContext debug2) {
/*  86 */     if (!this.compositeCondition.test(debug2)) {
/*     */       return;
/*     */     }
/*     */     
/*  90 */     Consumer<ItemStack> debug3 = LootItemFunction.decorate(this.compositeFunction, debug1, debug2);
/*     */     
/*  92 */     Random debug4 = debug2.getRandom();
/*  93 */     int debug5 = this.rolls.getInt(debug4) + Mth.floor(this.bonusRolls.getFloat(debug4) * debug2.getLuck());
/*  94 */     for (int debug6 = 0; debug6 < debug5; debug6++)
/*  95 */       addRandomItem(debug3, debug2); 
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext debug1) {
/*     */     int debug2;
/* 100 */     for (debug2 = 0; debug2 < this.conditions.length; debug2++) {
/* 101 */       this.conditions[debug2].validate(debug1.forChild(".condition[" + debug2 + "]"));
/*     */     }
/*     */     
/* 104 */     for (debug2 = 0; debug2 < this.functions.length; debug2++) {
/* 105 */       this.functions[debug2].validate(debug1.forChild(".functions[" + debug2 + "]"));
/*     */     }
/*     */     
/* 108 */     for (debug2 = 0; debug2 < this.entries.length; debug2++)
/* 109 */       this.entries[debug2].validate(debug1.forChild(".entries[" + debug2 + "]")); 
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements FunctionUserBuilder<Builder>, ConditionUserBuilder<Builder> {
/* 114 */     private final List<LootPoolEntryContainer> entries = Lists.newArrayList();
/* 115 */     private final List<LootItemCondition> conditions = Lists.newArrayList();
/* 116 */     private final List<LootItemFunction> functions = Lists.newArrayList();
/* 117 */     private RandomIntGenerator rolls = new RandomValueBounds(1.0F);
/* 118 */     private RandomValueBounds bonusRolls = new RandomValueBounds(0.0F, 0.0F);
/*     */     
/*     */     public Builder setRolls(RandomIntGenerator debug1) {
/* 121 */       this.rolls = debug1;
/* 122 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder unwrap() {
/* 127 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(LootPoolEntryContainer.Builder<?> debug1) {
/* 136 */       this.entries.add(debug1.build());
/* 137 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder when(LootItemCondition.Builder debug1) {
/* 142 */       this.conditions.add(debug1.build());
/* 143 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder debug1) {
/* 148 */       this.functions.add(debug1.build());
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public LootPool build() {
/* 153 */       if (this.rolls == null) {
/* 154 */         throw new IllegalArgumentException("Rolls not set");
/*     */       }
/*     */       
/* 157 */       return new LootPool(this.entries.<LootPoolEntryContainer>toArray(new LootPoolEntryContainer[0]), this.conditions.<LootItemCondition>toArray(new LootItemCondition[0]), this.functions.<LootItemFunction>toArray(new LootItemFunction[0]), this.rolls, this.bonusRolls);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootPool() {
/* 162 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {
/*     */     public LootPool deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 168 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "loot pool");
/* 169 */       LootPoolEntryContainer[] debug5 = (LootPoolEntryContainer[])GsonHelper.getAsObject(debug4, "entries", debug3, LootPoolEntryContainer[].class);
/* 170 */       LootItemCondition[] debug6 = (LootItemCondition[])GsonHelper.getAsObject(debug4, "conditions", new LootItemCondition[0], debug3, LootItemCondition[].class);
/* 171 */       LootItemFunction[] debug7 = (LootItemFunction[])GsonHelper.getAsObject(debug4, "functions", new LootItemFunction[0], debug3, LootItemFunction[].class);
/* 172 */       RandomIntGenerator debug8 = RandomIntGenerators.deserialize(debug4.get("rolls"), debug3);
/* 173 */       RandomValueBounds debug9 = (RandomValueBounds)GsonHelper.getAsObject(debug4, "bonus_rolls", new RandomValueBounds(0.0F, 0.0F), debug3, RandomValueBounds.class);
/* 174 */       return new LootPool(debug5, debug6, debug7, debug8, debug9);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(LootPool debug1, Type debug2, JsonSerializationContext debug3) {
/* 179 */       JsonObject debug4 = new JsonObject();
/* 180 */       debug4.add("rolls", RandomIntGenerators.serialize(debug1.rolls, debug3));
/* 181 */       debug4.add("entries", debug3.serialize(debug1.entries));
/* 182 */       if (debug1.bonusRolls.getMin() != 0.0F && debug1.bonusRolls.getMax() != 0.0F) {
/* 183 */         debug4.add("bonus_rolls", debug3.serialize(debug1.bonusRolls));
/*     */       }
/* 185 */       if (!ArrayUtils.isEmpty((Object[])debug1.conditions)) {
/* 186 */         debug4.add("conditions", debug3.serialize(debug1.conditions));
/*     */       }
/* 188 */       if (!ArrayUtils.isEmpty((Object[])debug1.functions)) {
/* 189 */         debug4.add("functions", debug3.serialize(debug1.functions));
/*     */       }
/* 191 */       return (JsonElement)debug4;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\LootPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */