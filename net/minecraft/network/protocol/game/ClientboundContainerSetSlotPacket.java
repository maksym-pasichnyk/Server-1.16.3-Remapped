/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundContainerSetSlotPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private int slot;
/* 16 */   private ItemStack itemStack = ItemStack.EMPTY;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundContainerSetSlotPacket(int debug1, int debug2, ItemStack debug3) {
/* 22 */     this.containerId = debug1;
/* 23 */     this.slot = debug2;
/* 24 */     this.itemStack = debug3.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 29 */     debug1.handleContainerSetSlot(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 34 */     this.containerId = debug1.readByte();
/* 35 */     this.slot = debug1.readShort();
/* 36 */     this.itemStack = debug1.readItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 41 */     debug1.writeByte(this.containerId);
/* 42 */     debug1.writeShort(this.slot);
/* 43 */     debug1.writeItem(this.itemStack);
/*    */   }
/*    */   
/*    */   public ClientboundContainerSetSlotPacket() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetSlotPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */