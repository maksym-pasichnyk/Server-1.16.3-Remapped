/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.EnchantedBookItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EnchantRandomlyFunction extends LootItemConditionalFunction {
/*  34 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final List<Enchantment> enchantments;
/*     */   
/*     */   private EnchantRandomlyFunction(LootItemCondition[] debug1, Collection<Enchantment> debug2) {
/*  39 */     super(debug1);
/*  40 */     this.enchantments = (List<Enchantment>)ImmutableList.copyOf(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  45 */     return LootItemFunctions.ENCHANT_RANDOMLY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*     */     Enchantment debug3;
/*  52 */     Random debug4 = debug2.getRandom();
/*  53 */     if (this.enchantments.isEmpty()) {
/*  54 */       boolean debug5 = (debug1.getItem() == Items.BOOK);
/*     */ 
/*     */ 
/*     */       
/*  58 */       List<Enchantment> debug6 = (List<Enchantment>)Registry.ENCHANTMENT.stream().filter(Enchantment::isDiscoverable).filter(debug2 -> (debug0 || debug2.canEnchant(debug1))).collect(Collectors.toList());
/*     */       
/*  60 */       if (debug6.isEmpty()) {
/*  61 */         LOGGER.warn("Couldn't find a compatible enchantment for {}", debug1);
/*  62 */         return debug1;
/*     */       } 
/*  64 */       debug3 = debug6.get(debug4.nextInt(debug6.size()));
/*     */     } else {
/*  66 */       debug3 = this.enchantments.get(debug4.nextInt(this.enchantments.size()));
/*     */     } 
/*     */     
/*  69 */     return enchantItem(debug1, debug3, debug4);
/*     */   }
/*     */   
/*     */   private static ItemStack enchantItem(ItemStack debug0, Enchantment debug1, Random debug2) {
/*  73 */     int debug3 = Mth.nextInt(debug2, debug1.getMinLevel(), debug1.getMaxLevel());
/*     */     
/*  75 */     if (debug0.getItem() == Items.BOOK) {
/*  76 */       debug0 = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/*  77 */       EnchantedBookItem.addEnchantment(debug0, new EnchantmentInstance(debug1, debug3));
/*     */     } else {
/*  79 */       debug0.enchant(debug1, debug3);
/*     */     } 
/*  81 */     return debug0;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  85 */     private final Set<Enchantment> enchantments = Sets.newHashSet();
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  89 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEnchantment(Enchantment debug1) {
/*  93 */       this.enchantments.add(debug1);
/*  94 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  99 */       return new EnchantRandomlyFunction(getConditions(), this.enchantments);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> randomApplicableEnchantment() {
/* 108 */     return simpleBuilder(debug0 -> new EnchantRandomlyFunction(debug0, (Collection<Enchantment>)ImmutableList.of()));
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<EnchantRandomlyFunction> {
/*     */     public void serialize(JsonObject debug1, EnchantRandomlyFunction debug2, JsonSerializationContext debug3) {
/* 114 */       super.serialize(debug1, debug2, debug3);
/*     */       
/* 116 */       if (!debug2.enchantments.isEmpty()) {
/* 117 */         JsonArray debug4 = new JsonArray();
/* 118 */         for (Enchantment debug6 : debug2.enchantments) {
/* 119 */           ResourceLocation debug7 = Registry.ENCHANTMENT.getKey(debug6);
/* 120 */           if (debug7 == null) {
/* 121 */             throw new IllegalArgumentException("Don't know how to serialize enchantment " + debug6);
/*     */           }
/* 123 */           debug4.add((JsonElement)new JsonPrimitive(debug7.toString()));
/*     */         } 
/* 125 */         debug1.add("enchantments", (JsonElement)debug4);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public EnchantRandomlyFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 131 */       List<Enchantment> debug4 = Lists.newArrayList();
/* 132 */       if (debug1.has("enchantments")) {
/* 133 */         JsonArray debug5 = GsonHelper.getAsJsonArray(debug1, "enchantments");
/* 134 */         for (JsonElement debug7 : debug5) {
/* 135 */           String debug8 = GsonHelper.convertToString(debug7, "enchantment");
/*     */           
/* 137 */           Enchantment debug9 = (Enchantment)Registry.ENCHANTMENT.getOptional(new ResourceLocation(debug8)).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + debug0 + "'"));
/* 138 */           debug4.add(debug9);
/*     */         } 
/*     */       } 
/* 141 */       return new EnchantRandomlyFunction(debug3, debug4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */