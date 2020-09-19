/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundSetEntityLinkPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int sourceId;
/*    */   private int destId;
/*    */   
/*    */   public ClientboundSetEntityLinkPacket() {}
/*    */   
/*    */   public ClientboundSetEntityLinkPacket(Entity debug1, @Nullable Entity debug2) {
/* 18 */     this.sourceId = debug1.getId();
/* 19 */     this.destId = (debug2 != null) ? debug2.getId() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.sourceId = debug1.readInt();
/* 25 */     this.destId = debug1.readInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 30 */     debug1.writeInt(this.sourceId);
/* 31 */     debug1.writeInt(this.destId);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 36 */     debug1.handleEntityLinkPacket(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetEntityLinkPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */