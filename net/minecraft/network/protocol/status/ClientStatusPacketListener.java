package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;

public interface ClientStatusPacketListener extends PacketListener {
  void handleStatusResponse(ClientboundStatusResponsePacket paramClientboundStatusResponsePacket);
  
  void handlePongResponse(ClientboundPongResponsePacket paramClientboundPongResponsePacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ClientStatusPacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */