/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.ShortBufferException;
/*    */ 
/*    */ public class CipherBase
/*    */ {
/*    */   private final Cipher cipher;
/* 11 */   private byte[] heapIn = new byte[0];
/* 12 */   private byte[] heapOut = new byte[0];
/*    */   
/*    */   protected CipherBase(Cipher debug1) {
/* 15 */     this.cipher = debug1;
/*    */   }
/*    */   
/*    */   private byte[] bufToByte(ByteBuf debug1) {
/* 19 */     int debug2 = debug1.readableBytes();
/* 20 */     if (this.heapIn.length < debug2) {
/* 21 */       this.heapIn = new byte[debug2];
/*    */     }
/* 23 */     debug1.readBytes(this.heapIn, 0, debug2);
/* 24 */     return this.heapIn;
/*    */   }
/*    */   
/*    */   protected ByteBuf decipher(ChannelHandlerContext debug1, ByteBuf debug2) throws ShortBufferException {
/* 28 */     int debug3 = debug2.readableBytes();
/* 29 */     byte[] debug4 = bufToByte(debug2);
/*    */     
/* 31 */     ByteBuf debug5 = debug1.alloc().heapBuffer(this.cipher.getOutputSize(debug3));
/* 32 */     debug5.writerIndex(this.cipher.update(debug4, 0, debug3, debug5.array(), debug5.arrayOffset()));
/*    */     
/* 34 */     return debug5;
/*    */   }
/*    */   
/*    */   protected void encipher(ByteBuf debug1, ByteBuf debug2) throws ShortBufferException {
/* 38 */     int debug3 = debug1.readableBytes();
/* 39 */     byte[] debug4 = bufToByte(debug1);
/*    */     
/* 41 */     int debug5 = this.cipher.getOutputSize(debug3);
/* 42 */     if (this.heapOut.length < debug5) {
/* 43 */       this.heapOut = new byte[debug5];
/*    */     }
/* 45 */     debug2.writeBytes(this.heapOut, 0, this.cipher.update(debug4, 0, debug3, this.heapOut));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\CipherBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */