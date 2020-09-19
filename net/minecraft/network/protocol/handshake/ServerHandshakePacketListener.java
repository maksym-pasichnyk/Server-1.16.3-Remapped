package net.minecraft.network.protocol.handshake;

import net.minecraft.network.PacketListener;

public interface ServerHandshakePacketListener extends PacketListener {
  void handleIntention(ClientIntentionPacket paramClientIntentionPacket);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\handshake\ServerHandshakePacketListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */