/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public class ClientboundCooldownPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private Item item;
/*    */   private int duration;
/*    */   
/*    */   public ClientboundCooldownPacket() {}
/*    */   
/*    */   public ClientboundCooldownPacket(Item debug1, int debug2) {
/* 17 */     this.item = debug1;
/* 18 */     this.duration = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     this.item = Item.byId(debug1.readVarInt());
/* 24 */     this.duration = debug1.readVarInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 29 */     debug1.writeVarInt(Item.getId(this.item));
/* 30 */     debug1.writeVarInt(this.duration);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 35 */     debug1.handleItemCooldown(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCooldownPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */