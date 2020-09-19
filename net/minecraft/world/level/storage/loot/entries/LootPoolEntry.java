package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface LootPoolEntry {
  int getWeight(float paramFloat);
  
  void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */