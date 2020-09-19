/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
/*    */ import net.minecraft.world.level.storage.loot.Serializer;
/*    */ 
/*    */ public class LootPoolEntries {
/*  9 */   public static final LootPoolEntryType EMPTY = register("empty", new EmptyLootItem.Serializer());
/* 10 */   public static final LootPoolEntryType ITEM = register("item", new LootItem.Serializer());
/* 11 */   public static final LootPoolEntryType REFERENCE = register("loot_table", new LootTableReference.Serializer());
/* 12 */   public static final LootPoolEntryType DYNAMIC = register("dynamic", new DynamicLoot.Serializer());
/* 13 */   public static final LootPoolEntryType TAG = register("tag", new TagEntry.Serializer());
/*    */   
/* 15 */   public static final LootPoolEntryType ALTERNATIVES = register("alternatives", CompositeEntryBase.createSerializer(AlternativesEntry::new));
/* 16 */   public static final LootPoolEntryType SEQUENCE = register("sequence", CompositeEntryBase.createSerializer(SequentialEntry::new));
/* 17 */   public static final LootPoolEntryType GROUP = register("group", CompositeEntryBase.createSerializer(EntryGroup::new));
/*    */   
/*    */   private static LootPoolEntryType register(String debug0, Serializer<? extends LootPoolEntryContainer> debug1) {
/* 20 */     return (LootPoolEntryType)Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(debug0), new LootPoolEntryType(debug1));
/*    */   }
/*    */   
/*    */   public static Object createGsonAdapter() {
/* 24 */     return GsonAdapterFactory.builder(Registry.LOOT_POOL_ENTRY_TYPE, "entry", "type", LootPoolEntryContainer::getType).build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntries.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */