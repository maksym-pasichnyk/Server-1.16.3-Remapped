/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ 
/*    */ public class ClientboundAddExperienceOrbPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int id;
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private int value;
/*    */   
/*    */   public ClientboundAddExperienceOrbPacket() {}
/*    */   
/*    */   public ClientboundAddExperienceOrbPacket(ExperienceOrb debug1) {
/* 21 */     this.id = debug1.getId();
/* 22 */     this.x = debug1.getX();
/* 23 */     this.y = debug1.getY();
/* 24 */     this.z = debug1.getZ();
/* 25 */     this.value = debug1.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 30 */     this.id = debug1.readVarInt();
/* 31 */     this.x = debug1.readDouble();
/* 32 */     this.y = debug1.readDouble();
/* 33 */     this.z = debug1.readDouble();
/* 34 */     this.value = debug1.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 39 */     debug1.writeVarInt(this.id);
/* 40 */     debug1.writeDouble(this.x);
/* 41 */     debug1.writeDouble(this.y);
/* 42 */     debug1.writeDouble(this.z);
/* 43 */     debug1.writeShort(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 48 */     debug1.handleAddExperienceOrb(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAddExperienceOrbPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */