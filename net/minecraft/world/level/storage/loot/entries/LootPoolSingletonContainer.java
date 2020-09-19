/*     */ package net.minecraft.world.level.storage.loot.entries;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LootPoolSingletonContainer
/*     */   extends LootPoolEntryContainer
/*     */ {
/*     */   protected final int weight;
/*     */   protected final int quality;
/*     */   protected final LootItemFunction[] functions;
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   private final LootPoolEntry entry;
/*     */   
/*     */   protected LootPoolSingletonContainer(int debug1, int debug2, LootItemCondition[] debug3, LootItemFunction[] debug4) {
/*  33 */     super(debug3);
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
/*  56 */     this.entry = new EntryBase()
/*     */       {
/*     */         public void createItemStack(Consumer<ItemStack> debug1, LootContext debug2) {
/*  59 */           LootPoolSingletonContainer.this.createItemStack(LootItemFunction.decorate(LootPoolSingletonContainer.this.compositeFunction, debug1, debug2), debug2); }
/*     */       };
/*     */     this.weight = debug1;
/*     */     this.quality = debug2;
/*     */     this.functions = debug4;
/*     */     this.compositeFunction = LootItemFunctions.compose((BiFunction[])debug4);
/*     */   } public void validate(ValidationContext debug1) { super.validate(debug1);
/*     */     for (int debug2 = 0; debug2 < this.functions.length; debug2++)
/*  67 */       this.functions[debug2].validate(debug1.forChild(".functions[" + debug2 + "]"));  } public boolean expand(LootContext debug1, Consumer<LootPoolEntry> debug2) { if (canRun(debug1)) {
/*  68 */       debug2.accept(this.entry);
/*  69 */       return true;
/*     */     } 
/*     */     
/*  72 */     return false; }
/*     */    public abstract class EntryBase implements LootPoolEntry {
/*     */     public int getWeight(float debug1) {
/*     */       return Math.max(Mth.floor(LootPoolSingletonContainer.this.weight + LootPoolSingletonContainer.this.quality * debug1), 0);
/*  76 */     } } public static abstract class Builder<T extends Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T> { protected int weight = 1;
/*  77 */     protected int quality = 0;
/*     */     
/*  79 */     private final List<LootItemFunction> functions = Lists.newArrayList();
/*     */ 
/*     */     
/*     */     public T apply(LootItemFunction.Builder debug1) {
/*  83 */       this.functions.add(debug1.build());
/*  84 */       return getThis();
/*     */     }
/*     */     
/*     */     protected LootItemFunction[] getFunctions() {
/*  88 */       return this.functions.<LootItemFunction>toArray(new LootItemFunction[0]);
/*     */     }
/*     */     
/*     */     public T setWeight(int debug1) {
/*  92 */       this.weight = debug1;
/*  93 */       return getThis();
/*     */     }
/*     */     
/*     */     public T setQuality(int debug1) {
/*  97 */       this.quality = debug1;
/*  98 */       return getThis();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class DummyBuilder
/*     */     extends Builder<DummyBuilder>
/*     */   {
/*     */     private final LootPoolSingletonContainer.EntryConstructor constructor;
/*     */ 
/*     */     
/*     */     public DummyBuilder(LootPoolSingletonContainer.EntryConstructor debug1) {
/* 111 */       this.constructor = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DummyBuilder getThis() {
/* 116 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootPoolEntryContainer build() {
/* 121 */       return this.constructor.build(this.weight, this.quality, getConditions(), getFunctions());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder<?> simpleBuilder(EntryConstructor debug0) {
/* 126 */     return new DummyBuilder(debug0);
/*     */   }
/*     */   
/*     */   protected abstract void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
/*     */   
/*     */   public static abstract class Serializer<T extends LootPoolSingletonContainer> extends LootPoolEntryContainer.Serializer<T> { public void serializeCustom(JsonObject debug1, T debug2, JsonSerializationContext debug3) {
/* 132 */       if (((LootPoolSingletonContainer)debug2).weight != 1) {
/* 133 */         debug1.addProperty("weight", Integer.valueOf(((LootPoolSingletonContainer)debug2).weight));
/*     */       }
/*     */       
/* 136 */       if (((LootPoolSingletonContainer)debug2).quality != 0) {
/* 137 */         debug1.addProperty("quality", Integer.valueOf(((LootPoolSingletonContainer)debug2).quality));
/*     */       }
/*     */       
/* 140 */       if (!ArrayUtils.isEmpty((Object[])((LootPoolSingletonContainer)debug2).functions)) {
/* 141 */         debug1.add("functions", debug3.serialize(((LootPoolSingletonContainer)debug2).functions));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public final T deserializeCustom(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 147 */       int debug4 = GsonHelper.getAsInt(debug1, "weight", 1);
/* 148 */       int debug5 = GsonHelper.getAsInt(debug1, "quality", 0);
/* 149 */       LootItemFunction[] debug6 = (LootItemFunction[])GsonHelper.getAsObject(debug1, "functions", new LootItemFunction[0], debug2, LootItemFunction[].class);
/*     */       
/* 151 */       return deserialize(debug1, debug2, debug4, debug5, debug3, debug6);
/*     */     }
/*     */     
/*     */     protected abstract T deserialize(JsonObject param1JsonObject, JsonDeserializationContext param1JsonDeserializationContext, int param1Int1, int param1Int2, LootItemCondition[] param1ArrayOfLootItemCondition, LootItemFunction[] param1ArrayOfLootItemFunction); }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface EntryConstructor {
/*     */     LootPoolSingletonContainer build(int param1Int1, int param1Int2, LootItemCondition[] param1ArrayOfLootItemCondition, LootItemFunction[] param1ArrayOfLootItemFunction);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */