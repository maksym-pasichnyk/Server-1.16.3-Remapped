package net.minecraft.network;

import net.minecraft.network.chat.Component;

public interface PacketListener {
  void onDisconnect(Component paramComponent);
  
  Connection getConnection();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\PacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */