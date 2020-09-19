package net.minecraft.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MenuConstructor {
  @Nullable
  AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\MenuConstructor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */