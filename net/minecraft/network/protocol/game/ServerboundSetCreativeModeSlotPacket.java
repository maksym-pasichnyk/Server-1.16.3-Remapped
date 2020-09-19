/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ServerboundSetCreativeModeSlotPacket implements Packet<ServerGamePacketListener> {
/*    */   private int slotNum;
/* 11 */   private ItemStack itemStack = ItemStack.EMPTY;
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
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 23 */     debug1.handleSetCreativeModeSlot(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     this.slotNum = debug1.readShort();
/* 29 */     this.itemStack = debug1.readItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 34 */     debug1.writeShort(this.slotNum);
/* 35 */     debug1.writeItem(this.itemStack);
/*    */   }
/*    */   
/*    */   public int getSlotNum() {
/* 39 */     return this.slotNum;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 43 */     return this.itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCreativeModeSlotPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */