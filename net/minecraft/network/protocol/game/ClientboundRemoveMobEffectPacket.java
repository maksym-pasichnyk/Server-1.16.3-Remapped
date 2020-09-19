/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundRemoveMobEffectPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int entityId;
/*    */   private MobEffect effect;
/*    */   
/*    */   public ClientboundRemoveMobEffectPacket() {}
/*    */   
/*    */   public ClientboundRemoveMobEffectPacket(int debug1, MobEffect debug2) {
/* 20 */     this.entityId = debug1;
/* 21 */     this.effect = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.entityId = debug1.readVarInt();
/* 27 */     this.effect = MobEffect.byId(debug1.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 32 */     debug1.writeVarInt(this.entityId);
/* 33 */     debug1.writeByte(MobEffect.getId(this.effect));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 38 */     debug1.handleRemoveMobEffect(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRemoveMobEffectPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */