package net.minecraft.world.level.storage.loot.functions;

public interface FunctionUserBuilder<T> {
  T apply(LootItemFunction.Builder paramBuilder);
  
  T unwrap();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\FunctionUserBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */