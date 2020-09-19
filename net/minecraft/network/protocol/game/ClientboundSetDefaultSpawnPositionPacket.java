/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetDefaultSpawnPositionPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private BlockPos pos;
/*    */   private float angle;
/*    */   
/*    */   public ClientboundSetDefaultSpawnPositionPacket() {}
/*    */   
/*    */   public ClientboundSetDefaultSpawnPositionPacket(BlockPos debug1, float debug2) {
/* 17 */     this.pos = debug1;
/* 18 */     this.angle = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.pos = debug1.readBlockPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 28 */     debug1.writeBlockPos(this.pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 33 */     debug1.handleSetSpawn(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetDefaultSpawnPositionPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */