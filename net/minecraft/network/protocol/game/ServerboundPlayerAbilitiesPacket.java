/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.player.Abilities;
/*    */ 
/*    */ 
/*    */ public class ServerboundPlayerAbilitiesPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private boolean isFlying;
/*    */   
/*    */   public ServerboundPlayerAbilitiesPacket() {}
/*    */   
/*    */   public ServerboundPlayerAbilitiesPacket(Abilities debug1) {
/* 18 */     this.isFlying = debug1.flying;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 23 */     byte debug2 = debug1.readByte();
/* 24 */     this.isFlying = ((debug2 & 0x2) != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 29 */     byte debug2 = 0;
/* 30 */     if (this.isFlying) {
/* 31 */       debug2 = (byte)(debug2 | 0x2);
/*    */     }
/* 33 */     debug1.writeByte(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 38 */     debug1.handlePlayerAbilities(this);
/*    */   }
/*    */   
/*    */   public boolean isFlying() {
/* 42 */     return this.isFlying;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerAbilitiesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */