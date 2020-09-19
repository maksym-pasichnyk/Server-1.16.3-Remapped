/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ByteToMessageDecoder
/*     */   extends ChannelInboundHandlerAdapter
/*     */ {
/*  75 */   public static final Cumulator MERGE_CUMULATOR = new Cumulator()
/*     */     {
/*     */       public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
/*     */         ByteBuf buffer;
/*  79 */         if (cumulation.writerIndex() > cumulation.maxCapacity() - in.readableBytes() || cumulation
/*  80 */           .refCnt() > 1 || cumulation.isReadOnly()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  88 */           buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
/*     */         } else {
/*  90 */           buffer = cumulation;
/*     */         } 
/*  92 */         buffer.writeBytes(in);
/*  93 */         in.release();
/*  94 */         return buffer;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final Cumulator COMPOSITE_CUMULATOR = new Cumulator()
/*     */     {
/*     */       public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
/*     */         CompositeByteBuf compositeByteBuf;
/* 107 */         if (cumulation.refCnt() > 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 114 */           ByteBuf buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
/* 115 */           buffer.writeBytes(in);
/* 116 */           in.release();
/*     */         } else {
/*     */           CompositeByteBuf composite;
/* 119 */           if (cumulation instanceof CompositeByteBuf) {
/* 120 */             composite = (CompositeByteBuf)cumulation;
/*     */           } else {
/* 122 */             composite = alloc.compositeBuffer(2147483647);
/* 123 */             composite.addComponent(true, cumulation);
/*     */           } 
/* 125 */           composite.addComponent(true, in);
/* 126 */           compositeByteBuf = composite;
/*     */         } 
/* 128 */         return (ByteBuf)compositeByteBuf;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static final byte STATE_INIT = 0;
/*     */   private static final byte STATE_CALLING_CHILD_DECODE = 1;
/*     */   private static final byte STATE_HANDLER_REMOVED_PENDING = 2;
/*     */   ByteBuf cumulation;
/* 137 */   private Cumulator cumulator = MERGE_CUMULATOR;
/*     */ 
/*     */   
/*     */   private boolean singleDecode;
/*     */ 
/*     */   
/*     */   private boolean decodeWasNull;
/*     */ 
/*     */   
/*     */   private boolean first;
/*     */ 
/*     */   
/* 149 */   private byte decodeState = 0;
/* 150 */   private int discardAfterReads = 16;
/*     */   private int numReads;
/*     */   
/*     */   protected ByteToMessageDecoder() {
/* 154 */     ensureNotSharable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSingleDecode(boolean singleDecode) {
/* 164 */     this.singleDecode = singleDecode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleDecode() {
/* 174 */     return this.singleDecode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCumulator(Cumulator cumulator) {
/* 181 */     if (cumulator == null) {
/* 182 */       throw new NullPointerException("cumulator");
/*     */     }
/* 184 */     this.cumulator = cumulator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDiscardAfterReads(int discardAfterReads) {
/* 192 */     if (discardAfterReads <= 0) {
/* 193 */       throw new IllegalArgumentException("discardAfterReads must be > 0");
/*     */     }
/* 195 */     this.discardAfterReads = discardAfterReads;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int actualReadableBytes() {
/* 205 */     return internalBuffer().readableBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuf internalBuffer() {
/* 214 */     if (this.cumulation != null) {
/* 215 */       return this.cumulation;
/*     */     }
/* 217 */     return Unpooled.EMPTY_BUFFER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 223 */     if (this.decodeState == 1) {
/* 224 */       this.decodeState = 2;
/*     */       return;
/*     */     } 
/* 227 */     ByteBuf buf = this.cumulation;
/* 228 */     if (buf != null) {
/*     */       
/* 230 */       this.cumulation = null;
/*     */       
/* 232 */       int readable = buf.readableBytes();
/* 233 */       if (readable > 0) {
/* 234 */         ByteBuf bytes = buf.readBytes(readable);
/* 235 */         buf.release();
/* 236 */         ctx.fireChannelRead(bytes);
/*     */       } else {
/* 238 */         buf.release();
/*     */       } 
/*     */       
/* 241 */       this.numReads = 0;
/* 242 */       ctx.fireChannelReadComplete();
/*     */     } 
/* 244 */     handlerRemoved0(ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 255 */     if (msg instanceof ByteBuf) {
/* 256 */       CodecOutputList out = CodecOutputList.newInstance();
/*     */       try {
/* 258 */         ByteBuf data = (ByteBuf)msg;
/* 259 */         this.first = (this.cumulation == null);
/* 260 */         if (this.first) {
/* 261 */           this.cumulation = data;
/*     */         } else {
/* 263 */           this.cumulation = this.cumulator.cumulate(ctx.alloc(), this.cumulation, data);
/*     */         } 
/* 265 */         callDecode(ctx, this.cumulation, out);
/* 266 */       } catch (DecoderException e) {
/* 267 */         throw e;
/* 268 */       } catch (Exception e) {
/* 269 */         throw new DecoderException(e);
/*     */       } finally {
/* 271 */         if (this.cumulation != null && !this.cumulation.isReadable()) {
/* 272 */           this.numReads = 0;
/* 273 */           this.cumulation.release();
/* 274 */           this.cumulation = null;
/* 275 */         } else if (++this.numReads >= this.discardAfterReads) {
/*     */ 
/*     */           
/* 278 */           this.numReads = 0;
/* 279 */           discardSomeReadBytes();
/*     */         } 
/*     */         
/* 282 */         int size = out.size();
/* 283 */         this.decodeWasNull = !out.insertSinceRecycled();
/* 284 */         fireChannelRead(ctx, out, size);
/* 285 */         out.recycle();
/*     */       } 
/*     */     } else {
/* 288 */       ctx.fireChannelRead(msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void fireChannelRead(ChannelHandlerContext ctx, List<Object> msgs, int numElements) {
/* 296 */     if (msgs instanceof CodecOutputList) {
/* 297 */       fireChannelRead(ctx, (CodecOutputList)msgs, numElements);
/*     */     } else {
/* 299 */       for (int i = 0; i < numElements; i++) {
/* 300 */         ctx.fireChannelRead(msgs.get(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void fireChannelRead(ChannelHandlerContext ctx, CodecOutputList msgs, int numElements) {
/* 309 */     for (int i = 0; i < numElements; i++) {
/* 310 */       ctx.fireChannelRead(msgs.getUnsafe(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 316 */     this.numReads = 0;
/* 317 */     discardSomeReadBytes();
/* 318 */     if (this.decodeWasNull) {
/* 319 */       this.decodeWasNull = false;
/* 320 */       if (!ctx.channel().config().isAutoRead()) {
/* 321 */         ctx.read();
/*     */       }
/*     */     } 
/* 324 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */   
/*     */   protected final void discardSomeReadBytes() {
/* 328 */     if (this.cumulation != null && !this.first && this.cumulation.refCnt() == 1)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 336 */       this.cumulation.discardSomeReadBytes();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 342 */     channelInputClosed(ctx, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 347 */     if (evt instanceof io.netty.channel.socket.ChannelInputShutdownEvent)
/*     */     {
/*     */ 
/*     */       
/* 351 */       channelInputClosed(ctx, false);
/*     */     }
/* 353 */     super.userEventTriggered(ctx, evt);
/*     */   }
/*     */   
/*     */   private void channelInputClosed(ChannelHandlerContext ctx, boolean callChannelInactive) throws Exception {
/* 357 */     CodecOutputList out = CodecOutputList.newInstance();
/*     */     try {
/* 359 */       channelInputClosed(ctx, out);
/* 360 */     } catch (DecoderException e) {
/* 361 */       throw e;
/* 362 */     } catch (Exception e) {
/* 363 */       throw new DecoderException(e);
/*     */     } finally {
/*     */       try {
/* 366 */         if (this.cumulation != null) {
/* 367 */           this.cumulation.release();
/* 368 */           this.cumulation = null;
/*     */         } 
/* 370 */         int size = out.size();
/* 371 */         fireChannelRead(ctx, out, size);
/* 372 */         if (size > 0)
/*     */         {
/* 374 */           ctx.fireChannelReadComplete();
/*     */         }
/* 376 */         if (callChannelInactive) {
/* 377 */           ctx.fireChannelInactive();
/*     */         }
/*     */       } finally {
/*     */         
/* 381 */         out.recycle();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void channelInputClosed(ChannelHandlerContext ctx, List<Object> out) throws Exception {
/* 391 */     if (this.cumulation != null) {
/* 392 */       callDecode(ctx, this.cumulation, out);
/* 393 */       decodeLast(ctx, this.cumulation, out);
/*     */     } else {
/* 395 */       decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
/*     */     try {
/* 409 */       while (in.isReadable()) {
/* 410 */         int outSize = out.size();
/*     */         
/* 412 */         if (outSize > 0) {
/* 413 */           fireChannelRead(ctx, out, outSize);
/* 414 */           out.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 421 */           if (ctx.isRemoved()) {
/*     */             break;
/*     */           }
/* 424 */           outSize = 0;
/*     */         } 
/*     */         
/* 427 */         int oldInputLength = in.readableBytes();
/* 428 */         decodeRemovalReentryProtection(ctx, in, out);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 434 */         if (ctx.isRemoved()) {
/*     */           break;
/*     */         }
/*     */         
/* 438 */         if (outSize == out.size()) {
/* 439 */           if (oldInputLength == in.readableBytes()) {
/*     */             break;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 446 */         if (oldInputLength == in.readableBytes()) {
/* 447 */           throw new DecoderException(
/* 448 */               StringUtil.simpleClassName(getClass()) + ".decode() did not read anything but decoded a message.");
/*     */         }
/*     */ 
/*     */         
/* 452 */         if (isSingleDecode()) {
/*     */           break;
/*     */         }
/*     */       } 
/* 456 */     } catch (DecoderException e) {
/* 457 */       throw e;
/* 458 */     } catch (Exception cause) {
/* 459 */       throw new DecoderException(cause);
/*     */     } 
/*     */   }
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
/*     */   protected abstract void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception;
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
/*     */   final void decodeRemovalReentryProtection(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/* 487 */     this.decodeState = 1;
/*     */     try {
/* 489 */       decode(ctx, in, out);
/*     */     } finally {
/* 491 */       boolean removePending = (this.decodeState == 2);
/* 492 */       this.decodeState = 0;
/* 493 */       if (removePending) {
/* 494 */         handlerRemoved(ctx);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/* 507 */     if (in.isReadable())
/*     */     {
/*     */       
/* 510 */       decodeRemovalReentryProtection(ctx, in, out);
/*     */     }
/*     */   }
/*     */   
/*     */   static ByteBuf expandCumulation(ByteBufAllocator alloc, ByteBuf cumulation, int readable) {
/* 515 */     ByteBuf oldCumulation = cumulation;
/* 516 */     cumulation = alloc.buffer(oldCumulation.readableBytes() + readable);
/* 517 */     cumulation.writeBytes(oldCumulation);
/* 518 */     oldCumulation.release();
/* 519 */     return cumulation;
/*     */   }
/*     */   
/*     */   public static interface Cumulator {
/*     */     ByteBuf cumulate(ByteBufAllocator param1ByteBufAllocator, ByteBuf param1ByteBuf1, ByteBuf param1ByteBuf2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\ByteToMessageDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */