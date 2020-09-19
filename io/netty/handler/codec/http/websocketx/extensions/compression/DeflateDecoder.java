/*     */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketFrame;
/*     */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class DeflateDecoder
/*     */   extends WebSocketExtensionDecoder
/*     */ {
/*  40 */   static final byte[] FRAME_TAIL = new byte[] { 0, 0, -1, -1 };
/*     */ 
/*     */   
/*     */   private final boolean noContext;
/*     */ 
/*     */   
/*     */   private EmbeddedChannel decoder;
/*     */ 
/*     */ 
/*     */   
/*     */   public DeflateDecoder(boolean noContext) {
/*  51 */     this.noContext = noContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
/*     */     ContinuationWebSocketFrame continuationWebSocketFrame;
/*  60 */     if (this.decoder == null) {
/*  61 */       if (!(msg instanceof TextWebSocketFrame) && !(msg instanceof BinaryWebSocketFrame)) {
/*  62 */         throw new CodecException("unexpected initial frame type: " + msg.getClass().getName());
/*     */       }
/*  64 */       this.decoder = new EmbeddedChannel(new ChannelHandler[] { (ChannelHandler)ZlibCodecFactory.newZlibDecoder(ZlibWrapper.NONE) });
/*     */     } 
/*     */     
/*  67 */     boolean readable = msg.content().isReadable();
/*  68 */     this.decoder.writeInbound(new Object[] { msg.content().retain() });
/*  69 */     if (appendFrameTail(msg)) {
/*  70 */       this.decoder.writeInbound(new Object[] { Unpooled.wrappedBuffer(FRAME_TAIL) });
/*     */     }
/*     */     
/*  73 */     CompositeByteBuf compositeUncompressedContent = ctx.alloc().compositeBuffer();
/*     */     while (true) {
/*  75 */       ByteBuf partUncompressedContent = (ByteBuf)this.decoder.readInbound();
/*  76 */       if (partUncompressedContent == null) {
/*     */         break;
/*     */       }
/*  79 */       if (!partUncompressedContent.isReadable()) {
/*  80 */         partUncompressedContent.release();
/*     */         continue;
/*     */       } 
/*  83 */       compositeUncompressedContent.addComponent(true, partUncompressedContent);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     if (readable && compositeUncompressedContent.numComponents() <= 0) {
/*  88 */       compositeUncompressedContent.release();
/*  89 */       throw new CodecException("cannot read uncompressed buffer");
/*     */     } 
/*     */     
/*  92 */     if (msg.isFinalFragment() && this.noContext) {
/*  93 */       cleanup();
/*     */     }
/*     */ 
/*     */     
/*  97 */     if (msg instanceof TextWebSocketFrame) {
/*  98 */       TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg.isFinalFragment(), newRsv(msg), (ByteBuf)compositeUncompressedContent);
/*  99 */     } else if (msg instanceof BinaryWebSocketFrame) {
/* 100 */       BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(msg.isFinalFragment(), newRsv(msg), (ByteBuf)compositeUncompressedContent);
/* 101 */     } else if (msg instanceof ContinuationWebSocketFrame) {
/* 102 */       continuationWebSocketFrame = new ContinuationWebSocketFrame(msg.isFinalFragment(), newRsv(msg), (ByteBuf)compositeUncompressedContent);
/*     */     } else {
/*     */       
/* 105 */       throw new CodecException("unexpected frame type: " + msg.getClass().getName());
/*     */     } 
/* 107 */     out.add(continuationWebSocketFrame);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 112 */     cleanup();
/* 113 */     super.handlerRemoved(ctx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 118 */     cleanup();
/* 119 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 123 */     if (this.decoder != null) {
/*     */       
/* 125 */       if (this.decoder.finish()) {
/*     */         while (true) {
/* 127 */           ByteBuf buf = (ByteBuf)this.decoder.readOutbound();
/* 128 */           if (buf == null) {
/*     */             break;
/*     */           }
/*     */           
/* 132 */           buf.release();
/*     */         } 
/*     */       }
/* 135 */       this.decoder = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean appendFrameTail(WebSocketFrame paramWebSocketFrame);
/*     */   
/*     */   protected abstract int newRsv(WebSocketFrame paramWebSocketFrame);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\DeflateDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */