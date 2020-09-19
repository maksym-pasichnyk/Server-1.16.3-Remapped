/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundPlayerInputPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private float xxa;
/*    */   private float zza;
/*    */   private boolean isJumping;
/*    */   private boolean isShiftKeyDown;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.xxa = debug1.readFloat();
/* 30 */     this.zza = debug1.readFloat();
/*    */     
/* 32 */     byte debug2 = debug1.readByte();
/* 33 */     this.isJumping = ((debug2 & 0x1) > 0);
/* 34 */     this.isShiftKeyDown = ((debug2 & 0x2) > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 39 */     debug1.writeFloat(this.xxa);
/* 40 */     debug1.writeFloat(this.zza);
/*    */     
/* 42 */     byte debug2 = 0;
/* 43 */     if (this.isJumping) {
/* 44 */       debug2 = (byte)(debug2 | 0x1);
/*    */     }
/* 46 */     if (this.isShiftKeyDown) {
/* 47 */       debug2 = (byte)(debug2 | 0x2);
/*    */     }
/* 49 */     debug1.writeByte(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 54 */     debug1.handlePlayerInput(this);
/*    */   }
/*    */   
/*    */   public float getXxa() {
/* 58 */     return this.xxa;
/*    */   }
/*    */   
/*    */   public float getZza() {
/* 62 */     return this.zza;
/*    */   }
/*    */   
/*    */   public boolean isJumping() {
/* 66 */     return this.isJumping;
/*    */   }
/*    */   
/*    */   public boolean isShiftKeyDown() {
/* 70 */     return this.isShiftKeyDown;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerInputPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */