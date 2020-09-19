package net.minecraft.world.level.storage.loot.predicates;

public interface ConditionUserBuilder<T> {
  T when(LootItemCondition.Builder paramBuilder);
  
  T unwrap();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\ConditionUserBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */