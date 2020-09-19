package net.minecraft.world;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuConstructor;

public interface MenuProvider extends MenuConstructor {
  Component getDisplayName();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\MenuProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */