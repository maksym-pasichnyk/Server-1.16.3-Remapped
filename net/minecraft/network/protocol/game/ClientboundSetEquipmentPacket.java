/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClientboundSetEquipmentPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private int entity;
/*    */   private final List<Pair<EquipmentSlot, ItemStack>> slots;
/*    */   
/*    */   public ClientboundSetEquipmentPacket() {
/* 19 */     this.slots = Lists.newArrayList();
/*    */   }
/*    */   
/*    */   public ClientboundSetEquipmentPacket(int debug1, List<Pair<EquipmentSlot, ItemStack>> debug2) {
/* 23 */     this.entity = debug1;
/* 24 */     this.slots = debug2;
/*    */   }
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/*    */     int debug3;
/* 29 */     this.entity = debug1.readVarInt();
/* 30 */     EquipmentSlot[] debug2 = EquipmentSlot.values();
/*    */     
/*    */     do {
/* 33 */       debug3 = debug1.readByte();
/* 34 */       EquipmentSlot debug4 = debug2[debug3 & 0x7F];
/* 35 */       ItemStack debug5 = debug1.readItem();
/* 36 */       this.slots.add(Pair.of(debug4, debug5));
/* 37 */     } while ((debug3 & 0xFFFFFF80) != 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 45 */     debug1.writeVarInt(this.entity);
/*    */     
/* 47 */     int debug2 = this.slots.size();
/* 48 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 49 */       Pair<EquipmentSlot, ItemStack> debug4 = this.slots.get(debug3);
/* 50 */       EquipmentSlot debug5 = (EquipmentSlot)debug4.getFirst();
/* 51 */       boolean debug6 = (debug3 != debug2 - 1);
/* 52 */       int debug7 = debug5.ordinal();
/* 53 */       debug1.writeByte(debug6 ? (debug7 | 0xFFFFFF80) : debug7);
/* 54 */       debug1.writeItem((ItemStack)debug4.getSecond());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 60 */     debug1.handleSetEquipment(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetEquipmentPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */