/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class CopyBlockState
/*     */   extends LootItemConditionalFunction {
/*     */   private final Block block;
/*     */   private final Set<Property<?>> properties;
/*     */   
/*     */   private CopyBlockState(LootItemCondition[] debug1, Block debug2, Set<Property<?>> debug3) {
/*  32 */     super(debug1);
/*  33 */     this.block = debug2;
/*  34 */     this.properties = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  39 */     return LootItemFunctions.COPY_STATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/*  44 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.BLOCK_STATE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack run(ItemStack debug1, LootContext debug2) {
/*  49 */     BlockState debug3 = (BlockState)debug2.getParamOrNull(LootContextParams.BLOCK_STATE);
/*  50 */     if (debug3 != null) {
/*  51 */       CompoundTag debug5, debug4 = debug1.getOrCreateTag();
/*     */       
/*  53 */       if (debug4.contains("BlockStateTag", 10)) {
/*  54 */         debug5 = debug4.getCompound("BlockStateTag");
/*     */       } else {
/*  56 */         debug5 = new CompoundTag();
/*  57 */         debug4.put("BlockStateTag", (Tag)debug5);
/*     */       } 
/*     */       
/*  60 */       this.properties.stream().filter(debug3::hasProperty).forEach(debug2 -> debug0.putString(debug2.getName(), serialize(debug1, debug2)));
/*     */     } 
/*     */     
/*  63 */     return debug1;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final Block block;
/*  68 */     private final Set<Property<?>> properties = Sets.newHashSet();
/*     */     
/*     */     private Builder(Block debug1) {
/*  71 */       this.block = debug1;
/*     */     }
/*     */     
/*     */     public Builder copy(Property<?> debug1) {
/*  75 */       if (!this.block.getStateDefinition().getProperties().contains(debug1)) {
/*  76 */         throw new IllegalStateException("Property " + debug1 + " is not present on block " + this.block);
/*     */       }
/*  78 */       this.properties.add(debug1);
/*  79 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  84 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  89 */       return new CopyBlockState(getConditions(), this.block, this.properties);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder copyState(Block debug0) {
/*  94 */     return new Builder(debug0);
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> String serialize(BlockState debug0, Property<T> debug1) {
/*  98 */     Comparable comparable = debug0.getValue(debug1);
/*  99 */     return debug1.getName(comparable);
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<CopyBlockState> {
/*     */     public void serialize(JsonObject debug1, CopyBlockState debug2, JsonSerializationContext debug3) {
/* 105 */       super.serialize(debug1, debug2, debug3);
/* 106 */       debug1.addProperty("block", Registry.BLOCK.getKey(debug2.block).toString());
/* 107 */       JsonArray debug4 = new JsonArray();
/* 108 */       debug2.properties.forEach(debug1 -> debug0.add(debug1.getName()));
/* 109 */       debug1.add("properties", (JsonElement)debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public CopyBlockState deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 114 */       ResourceLocation debug4 = new ResourceLocation(GsonHelper.getAsString(debug1, "block"));
/*     */       
/* 116 */       Block debug5 = (Block)Registry.BLOCK.getOptional(debug4).orElseThrow(() -> new IllegalArgumentException("Can't find block " + debug0));
/* 117 */       StateDefinition<Block, BlockState> debug6 = debug5.getStateDefinition();
/* 118 */       Set<Property<?>> debug7 = Sets.newHashSet();
/*     */       
/* 120 */       JsonArray debug8 = GsonHelper.getAsJsonArray(debug1, "properties", null);
/* 121 */       if (debug8 != null) {
/* 122 */         debug8.forEach(debug2 -> debug0.add(debug1.getProperty(GsonHelper.convertToString(debug2, "property"))));
/*     */       }
/* 124 */       return new CopyBlockState(debug3, debug5, debug7);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyBlockState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */