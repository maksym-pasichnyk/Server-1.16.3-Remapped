/*    */ package net.minecraft.data.loot;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.ConstantIntValue;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*    */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
/*    */ 
/*    */ public class PiglinBarterLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
/*    */   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> debug1) {
/* 26 */     debug1.accept(BuiltInLootTables.PIGLIN_BARTERING, 
/* 27 */         LootTable.lootTable()
/* 28 */         .withPool(LootPool.lootPool()
/* 29 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*    */ 
/*    */           
/* 32 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment(Enchantments.SOUL_SPEED)))
/*    */ 
/*    */           
/* 35 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(8).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment(Enchantments.SOUL_SPEED)))
/* 36 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(8).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:fire_resistance")))))
/* 37 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).setWeight(8).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:fire_resistance")))))
/*    */ 
/*    */           
/* 40 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(10).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:water")))))
/* 41 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(10.0F, 36.0F))))
/* 42 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/*    */ 
/*    */           
/* 45 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 9.0F))))
/* 46 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.QUARTZ).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(5.0F, 12.0F))))
/*    */ 
/*    */           
/* 49 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OBSIDIAN).setWeight(40))
/* 50 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CRYING_OBSIDIAN).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 51 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).setWeight(40))
/* 52 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 53 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SOUL_SAND).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/* 54 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHER_BRICK).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/* 55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(6.0F, 12.0F))))
/* 56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GRAVEL).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(8.0F, 16.0F))))
/* 57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLACKSTONE).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(8.0F, 16.0F))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\PiglinBarterLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */