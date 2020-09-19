/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.player.Abilities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundPlayerAbilitiesPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private boolean invulnerable;
/*    */   private boolean isFlying;
/*    */   private boolean canFly;
/*    */   private boolean instabuild;
/*    */   private float flyingSpeed;
/*    */   private float walkingSpeed;
/*    */   
/*    */   public ClientboundPlayerAbilitiesPacket() {}
/*    */   
/*    */   public ClientboundPlayerAbilitiesPacket(Abilities debug1) {
/* 27 */     this.invulnerable = debug1.invulnerable;
/* 28 */     this.isFlying = debug1.flying;
/* 29 */     this.canFly = debug1.mayfly;
/* 30 */     this.instabuild = debug1.instabuild;
/* 31 */     this.flyingSpeed = debug1.getFlyingSpeed();
/* 32 */     this.walkingSpeed = debug1.getWalkingSpeed();
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 37 */     byte debug2 = debug1.readByte();
/*    */     
/* 39 */     this.invulnerable = ((debug2 & 0x1) != 0);
/* 40 */     this.isFlying = ((debug2 & 0x2) != 0);
/* 41 */     this.canFly = ((debug2 & 0x4) != 0);
/* 42 */     this.instabuild = ((debug2 & 0x8) != 0);
/* 43 */     this.flyingSpeed = debug1.readFloat();
/* 44 */     this.walkingSpeed = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 49 */     byte debug2 = 0;
/*    */     
/* 51 */     if (this.invulnerable) {
/* 52 */       debug2 = (byte)(debug2 | 0x1);
/*    */     }
/* 54 */     if (this.isFlying) {
/* 55 */       debug2 = (byte)(debug2 | 0x2);
/*    */     }
/* 57 */     if (this.canFly) {
/* 58 */       debug2 = (byte)(debug2 | 0x4);
/*    */     }
/* 60 */     if (this.instabuild) {
/* 61 */       debug2 = (byte)(debug2 | 0x8);
/*    */     }
/*    */     
/* 64 */     debug1.writeByte(debug2);
/* 65 */     debug1.writeFloat(this.flyingSpeed);
/* 66 */     debug1.writeFloat(this.walkingSpeed);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 71 */     debug1.handlePlayerAbilities(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerAbilitiesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */