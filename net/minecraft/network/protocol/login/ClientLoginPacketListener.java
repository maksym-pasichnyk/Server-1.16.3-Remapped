package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;

public interface ClientLoginPacketListener extends PacketListener {
  void handleHello(ClientboundHelloPacket paramClientboundHelloPacket);
  
  void handleGameProfile(ClientboundGameProfilePacket paramClientboundGameProfilePacket);
  
  void handleDisconnect(ClientboundLoginDisconnectPacket paramClientboundLoginDisconnectPacket);
  
  void handleCompression(ClientboundLoginCompressionPacket paramClientboundLoginCompressionPacket);
  
  void handleCustomQuery(ClientboundCustomQueryPacket paramClientboundCustomQueryPacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ClientLoginPacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */