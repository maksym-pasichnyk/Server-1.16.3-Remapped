/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerContents extends LootItemConditionalFunction {
/*    */   private final List<LootPoolEntryContainer> entries;
/*    */   
/*    */   private SetContainerContents(LootItemCondition[] debug1, List<LootPoolEntryContainer> debug2) {
/* 27 */     super(debug1);
/* 28 */     this.entries = (List<LootPoolEntryContainer>)ImmutableList.copyOf(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 33 */     return LootItemFunctions.SET_CONTENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 38 */     if (debug1.isEmpty()) {
/* 39 */       return debug1;
/*    */     }
/*    */     
/* 42 */     NonNullList<ItemStack> debug3 = NonNullList.create();
/* 43 */     this.entries.forEach(debug2 -> debug2.expand(debug0, ()));
/*    */     
/* 45 */     CompoundTag debug4 = new CompoundTag();
/* 46 */     ContainerHelper.saveAllItems(debug4, debug3);
/* 47 */     CompoundTag debug5 = debug1.getOrCreateTag();
/* 48 */     debug5.put("BlockEntityTag", (Tag)debug4.merge(debug5.getCompound("BlockEntityTag")));
/* 49 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 54 */     super.validate(debug1);
/*    */     
/* 56 */     for (int debug2 = 0; debug2 < this.entries.size(); debug2++)
/* 57 */       ((LootPoolEntryContainer)this.entries.get(debug2)).validate(debug1.forChild(".entry[" + debug2 + "]")); 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootItemConditionalFunction.Builder<Builder> {
/* 62 */     private final List<LootPoolEntryContainer> entries = Lists.newArrayList();
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEntry(LootPoolEntryContainer.Builder<?> debug1) {
/* 70 */       this.entries.add(debug1.build());
/* 71 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 76 */       return new SetContainerContents(getConditions(), this.entries);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder setContents() {
/* 81 */     return new Builder();
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<SetContainerContents> {
/*    */     public void serialize(JsonObject debug1, SetContainerContents debug2, JsonSerializationContext debug3) {
/* 87 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 89 */       debug1.add("entries", debug3.serialize(debug2.entries));
/*    */     }
/*    */ 
/*    */     
/*    */     public SetContainerContents deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 94 */       LootPoolEntryContainer[] debug4 = (LootPoolEntryContainer[])GsonHelper.getAsObject(debug1, "entries", debug2, LootPoolEntryContainer[].class);
/* 95 */       return new SetContainerContents(debug3, Arrays.asList(debug4));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetContainerContents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */