/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.util.zip.Deflater;
/*    */ 
/*    */ public class CompressionEncoder
/*    */   extends MessageToByteEncoder<ByteBuf> {
/* 10 */   private final byte[] encodeBuf = new byte[8192];
/*    */   private final Deflater deflater;
/*    */   private int threshold;
/*    */   
/*    */   public CompressionEncoder(int debug1) {
/* 15 */     this.threshold = debug1;
/* 16 */     this.deflater = new Deflater();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext debug1, ByteBuf debug2, ByteBuf debug3) throws Exception {
/* 21 */     int debug4 = debug2.readableBytes();
/* 22 */     FriendlyByteBuf debug5 = new FriendlyByteBuf(debug3);
/*    */     
/* 24 */     if (debug4 < this.threshold) {
/* 25 */       debug5.writeVarInt(0);
/* 26 */       debug5.writeBytes(debug2);
/*    */     } else {
/* 28 */       byte[] debug6 = new byte[debug4];
/* 29 */       debug2.readBytes(debug6);
/*    */       
/* 31 */       debug5.writeVarInt(debug6.length);
/*    */       
/* 33 */       this.deflater.setInput(debug6, 0, debug4);
/* 34 */       this.deflater.finish();
/* 35 */       while (!this.deflater.finished()) {
/* 36 */         int debug7 = this.deflater.deflate(this.encodeBuf);
/* 37 */         debug5.writeBytes(this.encodeBuf, 0, debug7);
/*    */       } 
/* 39 */       this.deflater.reset();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setThreshold(int debug1) {
/* 48 */     this.threshold = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\CompressionEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */