/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundUpdateMobEffectPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int entityId;
/*    */   private byte effectId;
/*    */   private byte effectAmplifier;
/*    */   private int effectDurationTicks;
/*    */   private byte flags;
/*    */   
/*    */   public ClientboundUpdateMobEffectPacket() {}
/*    */   
/*    */   public ClientboundUpdateMobEffectPacket(int debug1, MobEffectInstance debug2) {
/* 28 */     this.entityId = debug1;
/* 29 */     this.effectId = (byte)(MobEffect.getId(debug2.getEffect()) & 0xFF);
/* 30 */     this.effectAmplifier = (byte)(debug2.getAmplifier() & 0xFF);
/* 31 */     if (debug2.getDuration() > 32767) {
/* 32 */       this.effectDurationTicks = 32767;
/*    */     } else {
/* 34 */       this.effectDurationTicks = debug2.getDuration();
/*    */     } 
/* 36 */     this.flags = 0;
/*    */     
/* 38 */     if (debug2.isAmbient()) {
/* 39 */       this.flags = (byte)(this.flags | 0x1);
/*    */     }
/* 41 */     if (debug2.isVisible()) {
/* 42 */       this.flags = (byte)(this.flags | 0x2);
/*    */     }
/* 44 */     if (debug2.showIcon()) {
/* 45 */       this.flags = (byte)(this.flags | 0x4);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 51 */     this.entityId = debug1.readVarInt();
/* 52 */     this.effectId = debug1.readByte();
/* 53 */     this.effectAmplifier = debug1.readByte();
/* 54 */     this.effectDurationTicks = debug1.readVarInt();
/* 55 */     this.flags = debug1.readByte();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 60 */     debug1.writeVarInt(this.entityId);
/* 61 */     debug1.writeByte(this.effectId);
/* 62 */     debug1.writeByte(this.effectAmplifier);
/* 63 */     debug1.writeVarInt(this.effectDurationTicks);
/* 64 */     debug1.writeByte(this.flags);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 73 */     debug1.handleUpdateMobEffect(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateMobEffectPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */