/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundTagQueryPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int transactionId;
/*    */   @Nullable
/*    */   private CompoundTag tag;
/*    */   
/*    */   public ClientboundTagQueryPacket() {}
/*    */   
/*    */   public ClientboundTagQueryPacket(int debug1, @Nullable CompoundTag debug2) {
/* 20 */     this.transactionId = debug1;
/* 21 */     this.tag = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.transactionId = debug1.readVarInt();
/* 27 */     this.tag = debug1.readNbt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeVarInt(this.transactionId);
/* 33 */     debug1.writeNbt(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 38 */     debug1.handleTagQueryPacket(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSkippable() {
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTagQueryPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */