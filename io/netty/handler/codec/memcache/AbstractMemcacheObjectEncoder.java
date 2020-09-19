/*     */ package io.netty.handler.codec.memcache;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMemcacheObjectEncoder<M extends MemcacheMessage>
/*     */   extends MessageToMessageEncoder<Object>
/*     */ {
/*     */   private boolean expectingMoreContent;
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/*  42 */     if (msg instanceof MemcacheMessage) {
/*  43 */       if (this.expectingMoreContent) {
/*  44 */         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */       }
/*     */ 
/*     */       
/*  48 */       MemcacheMessage memcacheMessage = (MemcacheMessage)msg;
/*  49 */       out.add(encodeMessage(ctx, (M)memcacheMessage));
/*     */     } 
/*     */     
/*  52 */     if (msg instanceof MemcacheContent || msg instanceof ByteBuf || msg instanceof FileRegion) {
/*  53 */       int contentLength = contentLength(msg);
/*  54 */       if (contentLength > 0) {
/*  55 */         out.add(encodeAndRetain(msg));
/*     */       } else {
/*  57 */         out.add(Unpooled.EMPTY_BUFFER);
/*     */       } 
/*     */       
/*  60 */       this.expectingMoreContent = !(msg instanceof LastMemcacheContent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/*  66 */     return (msg instanceof MemcacheObject || msg instanceof ByteBuf || msg instanceof FileRegion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ByteBuf encodeMessage(ChannelHandlerContext paramChannelHandlerContext, M paramM);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int contentLength(Object msg) {
/*  85 */     if (msg instanceof MemcacheContent) {
/*  86 */       return ((MemcacheContent)msg).content().readableBytes();
/*     */     }
/*  88 */     if (msg instanceof ByteBuf) {
/*  89 */       return ((ByteBuf)msg).readableBytes();
/*     */     }
/*  91 */     if (msg instanceof FileRegion) {
/*  92 */       return (int)((FileRegion)msg).count();
/*     */     }
/*  94 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object encodeAndRetain(Object msg) {
/* 104 */     if (msg instanceof ByteBuf) {
/* 105 */       return ((ByteBuf)msg).retain();
/*     */     }
/* 107 */     if (msg instanceof MemcacheContent) {
/* 108 */       return ((MemcacheContent)msg).content().retain();
/*     */     }
/* 110 */     if (msg instanceof FileRegion) {
/* 111 */       return ((FileRegion)msg).retain();
/*     */     }
/* 113 */     throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\AbstractMemcacheObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */