/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetTimePacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private long gameTime;
/*    */   private long dayTime;
/*    */   
/*    */   public ClientboundSetTimePacket() {}
/*    */   
/*    */   public ClientboundSetTimePacket(long debug1, long debug3, boolean debug5) {
/* 16 */     this.gameTime = debug1;
/* 17 */     this.dayTime = debug3;
/*    */     
/* 19 */     if (!debug5) {
/* 20 */       this.dayTime = -this.dayTime;
/* 21 */       if (this.dayTime == 0L) {
/* 22 */         this.dayTime = -1L;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.gameTime = debug1.readLong();
/* 30 */     this.dayTime = debug1.readLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 35 */     debug1.writeLong(this.gameTime);
/* 36 */     debug1.writeLong(this.dayTime);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 41 */     debug1.handleSetTime(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetTimePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */