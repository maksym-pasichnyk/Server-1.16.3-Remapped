package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;

public interface ServerStatusPacketListener extends PacketListener {
  void handlePingRequest(ServerboundPingRequestPacket paramServerboundPingRequestPacket);
  
  void handleStatusRequest(ServerboundStatusRequestPacket paramServerboundStatusRequestPacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ServerStatusPacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */