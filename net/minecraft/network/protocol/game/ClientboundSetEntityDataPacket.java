/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ 
/*    */ public class ClientboundSetEntityDataPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private List<SynchedEntityData.DataItem<?>> packedItems;
/*    */   
/*    */   public ClientboundSetEntityDataPacket() {}
/*    */   
/*    */   public ClientboundSetEntityDataPacket(int debug1, SynchedEntityData debug2, boolean debug3) {
/* 19 */     this.id = debug1;
/* 20 */     if (debug3) {
/* 21 */       this.packedItems = debug2.getAll();
/* 22 */       debug2.clearDirty();
/*    */     } else {
/* 24 */       this.packedItems = debug2.packDirty();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 30 */     this.id = debug1.readVarInt();
/* 31 */     this.packedItems = SynchedEntityData.unpack(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 36 */     debug1.writeVarInt(this.id);
/* 37 */     SynchedEntityData.pack(this.packedItems, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 42 */     debug1.handleSetEntityData(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetEntityDataPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */