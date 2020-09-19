package net.minecraft.world.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface ContainerListener {
  void refreshContainer(AbstractContainerMenu paramAbstractContainerMenu, NonNullList<ItemStack> paramNonNullList);
  
  void slotChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt, ItemStack paramItemStack);
  
  void setContainerData(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ContainerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */