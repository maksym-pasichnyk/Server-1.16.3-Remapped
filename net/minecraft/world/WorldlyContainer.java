package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public interface WorldlyContainer extends Container {
  int[] getSlotsForFace(Direction paramDirection);
  
  boolean canPlaceItemThroughFace(int paramInt, ItemStack paramItemStack, @Nullable Direction paramDirection);
  
  boolean canTakeItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\WorldlyContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */