/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class ClientboundPlayerLookAtPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private int entity;
/*    */   private EntityAnchorArgument.Anchor fromAnchor;
/*    */   private EntityAnchorArgument.Anchor toAnchor;
/*    */   private boolean atEntity;
/*    */   
/*    */   public ClientboundPlayerLookAtPacket() {}
/*    */   
/*    */   public ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor debug1, double debug2, double debug4, double debug6) {
/* 26 */     this.fromAnchor = debug1;
/* 27 */     this.x = debug2;
/* 28 */     this.y = debug4;
/* 29 */     this.z = debug6;
/*    */   }
/*    */   
/*    */   public ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor debug1, Entity debug2, EntityAnchorArgument.Anchor debug3) {
/* 33 */     this.fromAnchor = debug1;
/* 34 */     this.entity = debug2.getId();
/* 35 */     this.toAnchor = debug3;
/* 36 */     Vec3 debug4 = debug3.apply(debug2);
/* 37 */     this.x = debug4.x;
/* 38 */     this.y = debug4.y;
/* 39 */     this.z = debug4.z;
/* 40 */     this.atEntity = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 45 */     this.fromAnchor = (EntityAnchorArgument.Anchor)debug1.readEnum(EntityAnchorArgument.Anchor.class);
/* 46 */     this.x = debug1.readDouble();
/* 47 */     this.y = debug1.readDouble();
/* 48 */     this.z = debug1.readDouble();
/* 49 */     if (debug1.readBoolean()) {
/* 50 */       this.atEntity = true;
/* 51 */       this.entity = debug1.readVarInt();
/* 52 */       this.toAnchor = (EntityAnchorArgument.Anchor)debug1.readEnum(EntityAnchorArgument.Anchor.class);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 58 */     debug1.writeEnum((Enum)this.fromAnchor);
/* 59 */     debug1.writeDouble(this.x);
/* 60 */     debug1.writeDouble(this.y);
/* 61 */     debug1.writeDouble(this.z);
/* 62 */     debug1.writeBoolean(this.atEntity);
/* 63 */     if (this.atEntity) {
/* 64 */       debug1.writeVarInt(this.entity);
/* 65 */       debug1.writeEnum((Enum)this.toAnchor);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 71 */     debug1.handleLookAt(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerLookAtPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */