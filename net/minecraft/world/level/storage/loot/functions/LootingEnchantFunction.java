/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class LootingEnchantFunction
/*     */   extends LootItemConditionalFunction
/*     */ {
/*     */   private final RandomValueBounds value;
/*     */   private final int limit;
/*     */   
/*     */   private LootingEnchantFunction(LootItemCondition[] debug1, RandomValueBounds debug2, int debug3) {
/*  26 */     super(debug1);
/*  27 */     this.value = debug2;
/*  28 */     this.limit = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  33 */     return LootItemFunctions.LOOTING_ENCHANT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/*  38 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.KILLER_ENTITY);
/*     */   }
/*     */   
/*     */   private boolean hasLimit() {
/*  42 */     return (this.limit > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*  47 */     Entity debug3 = (Entity)debug2.getParamOrNull(LootContextParams.KILLER_ENTITY);
/*     */     
/*  49 */     if (debug3 instanceof LivingEntity) {
/*  50 */       int debug4 = EnchantmentHelper.getMobLooting((LivingEntity)debug3);
/*  51 */       if (debug4 == 0) {
/*  52 */         return debug1;
/*     */       }
/*  54 */       float debug5 = debug4 * this.value.getFloat(debug2.getRandom());
/*  55 */       debug1.grow(Math.round(debug5));
/*     */       
/*  57 */       if (hasLimit() && debug1.getCount() > this.limit) {
/*  58 */         debug1.setCount(this.limit);
/*     */       }
/*     */     } 
/*     */     
/*  62 */     return debug1;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final RandomValueBounds count;
/*  67 */     private int limit = 0;
/*     */     
/*     */     public Builder(RandomValueBounds debug1) {
/*  70 */       this.count = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  75 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLimit(int debug1) {
/*  79 */       this.limit = debug1;
/*  80 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  85 */       return new LootingEnchantFunction(getConditions(), this.count, this.limit);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootingMultiplier(RandomValueBounds debug0) {
/*  90 */     return new Builder(debug0);
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<LootingEnchantFunction> {
/*     */     public void serialize(JsonObject debug1, LootingEnchantFunction debug2, JsonSerializationContext debug3) {
/*  96 */       super.serialize(debug1, debug2, debug3);
/*     */       
/*  98 */       debug1.add("count", debug3.serialize(debug2.value));
/*  99 */       if (debug2.hasLimit()) {
/* 100 */         debug1.add("limit", debug3.serialize(Integer.valueOf(debug2.limit)));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public LootingEnchantFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 106 */       int debug4 = GsonHelper.getAsInt(debug1, "limit", 0);
/* 107 */       return new LootingEnchantFunction(debug3, (RandomValueBounds)GsonHelper.getAsObject(debug1, "count", debug2, RandomValueBounds.class), debug4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootingEnchantFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */