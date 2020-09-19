/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import java.util.List;
/*    */ import java.util.zip.Inflater;
/*    */ 
/*    */ 
/*    */ public class CompressionDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   private final Inflater inflater;
/*    */   private int threshold;
/*    */   
/*    */   public CompressionDecoder(int debug1) {
/* 19 */     this.threshold = debug1;
/* 20 */     this.inflater = new Inflater();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext debug1, ByteBuf debug2, List<Object> debug3) throws Exception {
/* 25 */     if (debug2.readableBytes() == 0) {
/*    */       return;
/*    */     }
/*    */     
/* 29 */     FriendlyByteBuf debug4 = new FriendlyByteBuf(debug2);
/* 30 */     int debug5 = debug4.readVarInt();
/*    */     
/* 32 */     if (debug5 == 0)
/* 33 */     { debug3.add(debug4.readBytes(debug4.readableBytes())); }
/* 34 */     else { if (debug5 < this.threshold)
/* 35 */         throw new DecoderException("Badly compressed packet - size of " + debug5 + " is below server threshold of " + this.threshold); 
/* 36 */       if (debug5 > 2097152) {
/* 37 */         throw new DecoderException("Badly compressed packet - size of " + debug5 + " is larger than protocol maximum of " + 2097152);
/*    */       }
/* 39 */       byte[] debug6 = new byte[debug4.readableBytes()];
/* 40 */       debug4.readBytes(debug6);
/* 41 */       this.inflater.setInput(debug6);
/*    */       
/* 43 */       byte[] debug7 = new byte[debug5];
/* 44 */       this.inflater.inflate(debug7);
/* 45 */       debug3.add(Unpooled.wrappedBuffer(debug7));
/*    */       
/* 47 */       this.inflater.reset(); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setThreshold(int debug1) {
/* 56 */     this.threshold = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\CompressionDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */