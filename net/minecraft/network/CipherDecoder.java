/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.List;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherDecoder
/*    */   extends MessageToMessageDecoder<ByteBuf> {
/*    */   private final CipherBase cipher;
/*    */   
/*    */   public CipherDecoder(Cipher debug1) {
/* 14 */     this.cipher = new CipherBase(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext debug1, ByteBuf debug2, List<Object> debug3) throws Exception {
/* 19 */     debug3.add(this.cipher.decipher(debug1, debug2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\CipherDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */