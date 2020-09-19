package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;

public interface ServerLoginPacketListener extends PacketListener {
  void handleHello(ServerboundHelloPacket paramServerboundHelloPacket);
  
  void handleKey(ServerboundKeyPacket paramServerboundKeyPacket);
  
  void handleCustomQueryPacket(ServerboundCustomQueryPacket paramServerboundCustomQueryPacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ServerLoginPacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */