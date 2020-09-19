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
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class LootTable
/*     */ {
/*  34 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  36 */   public static final LootTable EMPTY = new LootTable(LootContextParamSets.EMPTY, new LootPool[0], new LootItemFunction[0]);
/*  37 */   public static final LootContextParamSet DEFAULT_PARAM_SET = LootContextParamSets.ALL_PARAMS;
/*     */   
/*     */   private final LootContextParamSet paramSet;
/*     */   
/*     */   private final LootPool[] pools;
/*     */   
/*     */   private final LootItemFunction[] functions;
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   
/*     */   private LootTable(LootContextParamSet debug1, LootPool[] debug2, LootItemFunction[] debug3) {
/*  47 */     this.paramSet = debug1;
/*  48 */     this.pools = debug2;
/*  49 */     this.functions = debug3;
/*  50 */     this.compositeFunction = LootItemFunctions.compose((BiFunction[])debug3);
/*     */   }
/*     */   
/*     */   public static Consumer<ItemStack> createStackSplitter(Consumer<ItemStack> debug0) {
/*  54 */     return debug1 -> {
/*     */         if (debug1.getCount() < debug1.getMaxStackSize()) {
/*     */           debug0.accept(debug1);
/*     */         } else {
/*     */           int debug2 = debug1.getCount();
/*     */           while (debug2 > 0) {
/*     */             ItemStack debug3 = debug1.copy();
/*     */             debug3.setCount(Math.min(debug1.getMaxStackSize(), debug2));
/*     */             debug2 -= debug3.getCount();
/*     */             debug0.accept(debug3);
/*     */           } 
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public void getRandomItemsRaw(LootContext debug1, Consumer<ItemStack> debug2) {
/*  70 */     if (debug1.addVisitedTable(this)) {
/*  71 */       Consumer<ItemStack> debug3 = LootItemFunction.decorate(this.compositeFunction, debug2, debug1);
/*  72 */       for (LootPool debug7 : this.pools) {
/*  73 */         debug7.addRandomItems(debug3, debug1);
/*     */       }
/*  75 */       debug1.removeVisitedTable(this);
/*     */     } else {
/*  77 */       LOGGER.warn("Detected infinite loop in loot tables");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootContext debug1, Consumer<ItemStack> debug2) {
/*  82 */     getRandomItemsRaw(debug1, createStackSplitter(debug2));
/*     */   }
/*     */   
/*     */   public List<ItemStack> getRandomItems(LootContext debug1) {
/*  86 */     List<ItemStack> debug2 = Lists.newArrayList();
/*  87 */     getRandomItems(debug1, debug2::add);
/*  88 */     return debug2;
/*     */   }
/*     */   
/*     */   public LootContextParamSet getParamSet() {
/*  92 */     return this.paramSet;
/*     */   }
/*     */   public void validate(ValidationContext debug1) {
/*     */     int debug2;
/*  96 */     for (debug2 = 0; debug2 < this.pools.length; debug2++) {
/*  97 */       this.pools[debug2].validate(debug1.forChild(".pools[" + debug2 + "]"));
/*     */     }
/*     */     
/* 100 */     for (debug2 = 0; debug2 < this.functions.length; debug2++) {
/* 101 */       this.functions[debug2].validate(debug1.forChild(".functions[" + debug2 + "]"));
/*     */     }
/*     */   }
/*     */   
/*     */   public void fill(Container debug1, LootContext debug2) {
/* 106 */     List<ItemStack> debug3 = getRandomItems(debug2);
/* 107 */     Random debug4 = debug2.getRandom();
/* 108 */     List<Integer> debug5 = getAvailableSlots(debug1, debug4);
/* 109 */     shuffleAndSplitItems(debug3, debug5.size(), debug4);
/* 110 */     for (ItemStack debug7 : debug3) {
/* 111 */       if (debug5.isEmpty()) {
/* 112 */         LOGGER.warn("Tried to over-fill a container");
/*     */         
/*     */         return;
/*     */       } 
/* 116 */       if (debug7.isEmpty()) {
/* 117 */         debug1.setItem(((Integer)debug5.remove(debug5.size() - 1)).intValue(), ItemStack.EMPTY); continue;
/*     */       } 
/* 119 */       debug1.setItem(((Integer)debug5.remove(debug5.size() - 1)).intValue(), debug7);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void shuffleAndSplitItems(List<ItemStack> debug1, int debug2, Random debug3) {
/* 125 */     List<ItemStack> debug4 = Lists.newArrayList();
/* 126 */     for (Iterator<ItemStack> debug5 = debug1.iterator(); debug5.hasNext(); ) {
/* 127 */       ItemStack debug6 = debug5.next();
/* 128 */       if (debug6.isEmpty()) {
/* 129 */         debug5.remove(); continue;
/* 130 */       }  if (debug6.getCount() > 1) {
/* 131 */         debug4.add(debug6);
/* 132 */         debug5.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 136 */     while (debug2 - debug1.size() - debug4.size() > 0 && !debug4.isEmpty()) {
/* 137 */       ItemStack itemStack1 = debug4.remove(Mth.nextInt(debug3, 0, debug4.size() - 1));
/* 138 */       int debug6 = Mth.nextInt(debug3, 1, itemStack1.getCount() / 2);
/* 139 */       ItemStack debug7 = itemStack1.split(debug6);
/*     */       
/* 141 */       if (itemStack1.getCount() > 1 && debug3.nextBoolean()) {
/* 142 */         debug4.add(itemStack1);
/*     */       } else {
/* 144 */         debug1.add(itemStack1);
/*     */       } 
/*     */       
/* 147 */       if (debug7.getCount() > 1 && debug3.nextBoolean()) {
/* 148 */         debug4.add(debug7); continue;
/*     */       } 
/* 150 */       debug1.add(debug7);
/*     */     } 
/*     */ 
/*     */     
/* 154 */     debug1.addAll(debug4);
/*     */     
/* 156 */     Collections.shuffle(debug1, debug3);
/*     */   }
/*     */   
/*     */   private List<Integer> getAvailableSlots(Container debug1, Random debug2) {
/* 160 */     List<Integer> debug3 = Lists.newArrayList();
/*     */     
/* 162 */     for (int debug4 = 0; debug4 < debug1.getContainerSize(); debug4++) {
/* 163 */       if (debug1.getItem(debug4).isEmpty()) {
/* 164 */         debug3.add(Integer.valueOf(debug4));
/*     */       }
/*     */     } 
/*     */     
/* 168 */     Collections.shuffle(debug3, debug2);
/* 169 */     return debug3;
/*     */   }
/*     */   
/*     */   public static class Builder implements FunctionUserBuilder<Builder> {
/* 173 */     private final List<LootPool> pools = Lists.newArrayList();
/*     */     
/* 175 */     private final List<LootItemFunction> functions = Lists.newArrayList();
/*     */     
/* 177 */     private LootContextParamSet paramSet = LootTable.DEFAULT_PARAM_SET;
/*     */     
/*     */     public Builder withPool(LootPool.Builder debug1) {
/* 180 */       this.pools.add(debug1.build());
/* 181 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setParamSet(LootContextParamSet debug1) {
/* 185 */       this.paramSet = debug1;
/* 186 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder debug1) {
/* 191 */       this.functions.add(debug1.build());
/* 192 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder unwrap() {
/* 197 */       return this;
/*     */     }
/*     */     
/*     */     public LootTable build() {
/* 201 */       return new LootTable(this.paramSet, this.pools.<LootPool>toArray(new LootPool[0]), this.functions.<LootItemFunction>toArray(new LootItemFunction[0]));
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootTable() {
/* 206 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {
/*     */     public LootTable deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 212 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "loot table");
/* 213 */       LootPool[] debug5 = (LootPool[])GsonHelper.getAsObject(debug4, "pools", new LootPool[0], debug3, LootPool[].class);
/*     */       
/* 215 */       LootContextParamSet debug6 = null;
/*     */       
/* 217 */       if (debug4.has("type")) {
/* 218 */         String str = GsonHelper.getAsString(debug4, "type");
/* 219 */         debug6 = LootContextParamSets.get(new ResourceLocation(str));
/*     */       } 
/*     */       
/* 222 */       LootItemFunction[] debug7 = (LootItemFunction[])GsonHelper.getAsObject(debug4, "functions", new LootItemFunction[0], debug3, LootItemFunction[].class);
/* 223 */       return new LootTable((debug6 != null) ? debug6 : LootContextParamSets.ALL_PARAMS, debug5, debug7);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(LootTable debug1, Type debug2, JsonSerializationContext debug3) {
/* 228 */       JsonObject debug4 = new JsonObject();
/* 229 */       if (debug1.paramSet != LootTable.DEFAULT_PARAM_SET) {
/* 230 */         ResourceLocation debug5 = LootContextParamSets.getKey(debug1.paramSet);
/* 231 */         if (debug5 != null) {
/* 232 */           debug4.addProperty("type", debug5.toString());
/*     */         } else {
/* 234 */           LootTable.LOGGER.warn("Failed to find id for param set " + debug1.paramSet);
/*     */         } 
/*     */       } 
/*     */       
/* 238 */       if (debug1.pools.length > 0) {
/* 239 */         debug4.add("pools", debug3.serialize(debug1.pools));
/*     */       }
/*     */       
/* 242 */       if (!ArrayUtils.isEmpty((Object[])debug1.functions)) {
/* 243 */         debug4.add("functions", debug3.serialize(debug1.functions));
/*     */       }
/*     */       
/* 246 */       return (JsonElement)debug4;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\LootTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */