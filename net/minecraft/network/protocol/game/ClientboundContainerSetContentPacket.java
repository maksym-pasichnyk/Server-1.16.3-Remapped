/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClientboundContainerSetContentPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int containerId;
/*    */   private List<ItemStack> items;
/*    */   
/*    */   public ClientboundContainerSetContentPacket() {}
/*    */   
/*    */   public ClientboundContainerSetContentPacket(int debug1, NonNullList<ItemStack> debug2) {
/* 20 */     this.containerId = debug1;
/* 21 */     this.items = (List<ItemStack>)NonNullList.withSize(debug2.size(), ItemStack.EMPTY);
/* 22 */     for (int debug3 = 0; debug3 < this.items.size(); debug3++) {
/* 23 */       this.items.set(debug3, ((ItemStack)debug2.get(debug3)).copy());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.containerId = debug1.readUnsignedByte();
/* 30 */     int debug2 = debug1.readShort();
/* 31 */     this.items = (List<ItemStack>)NonNullList.withSize(debug2, ItemStack.EMPTY);
/* 32 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 33 */       this.items.set(debug3, debug1.readItem());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 39 */     debug1.writeByte(this.containerId);
/* 40 */     debug1.writeShort(this.items.size());
/* 41 */     for (ItemStack debug3 : this.items) {
/* 42 */       debug1.writeItem(debug3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 48 */     debug1.handleContainerContent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetContentPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */