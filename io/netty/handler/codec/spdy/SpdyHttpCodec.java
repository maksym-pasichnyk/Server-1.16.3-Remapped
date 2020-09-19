/*    */ package io.netty.handler.codec.spdy;
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
/*    */ public final class SpdyHttpCodec
/*    */   extends CombinedChannelDuplexHandler<SpdyHttpDecoder, SpdyHttpEncoder>
/*    */ {
/*    */   public SpdyHttpCodec(SpdyVersion version, int maxContentLength) {
/* 28 */     super((ChannelInboundHandler)new SpdyHttpDecoder(version, maxContentLength), (ChannelOutboundHandler)new SpdyHttpEncoder(version));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpdyHttpCodec(SpdyVersion version, int maxContentLength, boolean validateHttpHeaders) {
/* 35 */     super((ChannelInboundHandler)new SpdyHttpDecoder(version, maxContentLength, validateHttpHeaders), (ChannelOutboundHandler)new SpdyHttpEncoder(version));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdyHttpCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */