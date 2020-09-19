package net.minecraft.world.item;

import net.minecraft.world.item.crafting.Ingredient;

public interface Tier {
  int getUses();
  
  float getSpeed();
  
  float getAttackDamageBonus();
  
  int getLevel();
  
  int getEnchantmentValue();
  
  Ingredient getRepairIngredient();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\Tier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */