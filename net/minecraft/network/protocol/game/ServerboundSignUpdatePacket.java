/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
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
/*    */ public class ServerboundSignUpdatePacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private String[] lines;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.pos = debug1.readBlockPos();
/* 25 */     this.lines = new String[4];
/* 26 */     for (int debug2 = 0; debug2 < 4; debug2++) {
/* 27 */       this.lines[debug2] = debug1.readUtf(384);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 33 */     debug1.writeBlockPos(this.pos);
/* 34 */     for (int debug2 = 0; debug2 < 4; debug2++) {
/* 35 */       debug1.writeUtf(this.lines[debug2]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 41 */     debug1.handleSignUpdate(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 45 */     return this.pos;
/*    */   }
/*    */   
/*    */   public String[] getLines() {
/* 49 */     return this.lines;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSignUpdatePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */