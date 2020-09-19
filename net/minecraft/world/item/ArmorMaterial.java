package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.crafting.Ingredient;

public interface ArmorMaterial {
  int getDurabilityForSlot(EquipmentSlot paramEquipmentSlot);
  
  int getDefenseForSlot(EquipmentSlot paramEquipmentSlot);
  
  int getEnchantmentValue();
  
  SoundEvent getEquipSound();
  
  Ingredient getRepairIngredient();
  
  float getToughness();
  
  float getKnockbackResistance();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ArmorMaterial.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */