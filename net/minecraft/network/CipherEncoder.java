/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherEncoder
/*    */   extends MessageToByteEncoder<ByteBuf> {
/*    */   private final CipherBase cipher;
/*    */   
/*    */   public CipherEncoder(Cipher debug1) {
/* 13 */     this.cipher = new CipherBase(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext debug1, ByteBuf debug2, ByteBuf debug3) throws Exception {
/* 18 */     this.cipher.encipher(debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\CipherEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */