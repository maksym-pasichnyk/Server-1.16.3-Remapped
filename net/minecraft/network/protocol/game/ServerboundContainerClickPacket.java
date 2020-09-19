/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.inventory.ClickType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ServerboundContainerClickPacket implements Packet<ServerGamePacketListener> {
/*    */   private int containerId;
/*    */   private int slotNum;
/*    */   private int buttonNum;
/*    */   private short uid;
/* 15 */   private ItemStack itemStack = ItemStack.EMPTY;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ClickType clickType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 32 */     debug1.handleContainerClick(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 37 */     this.containerId = debug1.readByte();
/* 38 */     this.slotNum = debug1.readShort();
/* 39 */     this.buttonNum = debug1.readByte();
/* 40 */     this.uid = debug1.readShort();
/* 41 */     this.clickType = (ClickType)debug1.readEnum(ClickType.class);
/*    */     
/* 43 */     this.itemStack = debug1.readItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 48 */     debug1.writeByte(this.containerId);
/* 49 */     debug1.writeShort(this.slotNum);
/* 50 */     debug1.writeByte(this.buttonNum);
/* 51 */     debug1.writeShort(this.uid);
/* 52 */     debug1.writeEnum((Enum)this.clickType);
/*    */     
/* 54 */     debug1.writeItem(this.itemStack);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 58 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int getSlotNum() {
/* 62 */     return this.slotNum;
/*    */   }
/*    */   
/*    */   public int getButtonNum() {
/* 66 */     return this.buttonNum;
/*    */   }
/*    */   
/*    */   public short getUid() {
/* 70 */     return this.uid;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 74 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public ClickType getClickType() {
/* 78 */     return this.clickType;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerClickPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */