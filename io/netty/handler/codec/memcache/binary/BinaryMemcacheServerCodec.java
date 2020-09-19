/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.channel.ChannelInboundHandler;
/*    */ import io.netty.channel.ChannelOutboundHandler;
/*    */ import io.netty.channel.CombinedChannelDuplexHandler;
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
/*    */ public class BinaryMemcacheServerCodec
/*    */   extends CombinedChannelDuplexHandler<BinaryMemcacheRequestDecoder, BinaryMemcacheResponseEncoder>
/*    */ {
/*    */   public BinaryMemcacheServerCodec() {
/* 33 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheServerCodec(int decodeChunkSize) {
/* 37 */     super((ChannelInboundHandler)new BinaryMemcacheRequestDecoder(decodeChunkSize), (ChannelOutboundHandler)new BinaryMemcacheResponseEncoder());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheServerCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */