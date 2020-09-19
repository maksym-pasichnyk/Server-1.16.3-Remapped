/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.collection.CharObjectMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2FrameWriter
/*     */   implements Http2FrameWriter, Http2FrameSizePolicy, Http2FrameWriter.Configuration
/*     */ {
/*     */   private static final String STREAM_ID = "Stream ID";
/*     */   private static final String STREAM_DEPENDENCY = "Stream Dependency";
/*  80 */   private static final ByteBuf ZERO_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(255).writeZero(255)).asReadOnly();
/*     */   
/*     */   private final Http2HeadersEncoder headersEncoder;
/*     */   private int maxFrameSize;
/*     */   
/*     */   public DefaultHttp2FrameWriter() {
/*  86 */     this(new DefaultHttp2HeadersEncoder());
/*     */   }
/*     */   
/*     */   public DefaultHttp2FrameWriter(Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector) {
/*  90 */     this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector));
/*     */   }
/*     */   
/*     */   public DefaultHttp2FrameWriter(Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector, boolean ignoreMaxHeaderListSize) {
/*  94 */     this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector, ignoreMaxHeaderListSize));
/*     */   }
/*     */   
/*     */   public DefaultHttp2FrameWriter(Http2HeadersEncoder headersEncoder) {
/*  98 */     this.headersEncoder = headersEncoder;
/*  99 */     this.maxFrameSize = 16384;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration() {
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2HeadersEncoder.Configuration headersConfiguration() {
/* 109 */     return this.headersEncoder.configuration();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameSizePolicy frameSizePolicy() {
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void maxFrameSize(int max) throws Http2Exception {
/* 119 */     if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
/* 120 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", new Object[] { Integer.valueOf(max) });
/*     */     }
/* 122 */     this.maxFrameSize = max;
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxFrameSize() {
/* 127 */     return this.maxFrameSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise) {
/* 137 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/* 138 */     ByteBuf frameHeader = null;
/*     */     try {
/* 140 */       verifyStreamId(streamId, "Stream ID");
/* 141 */       Http2CodecUtil.verifyPadding(padding);
/*     */       
/* 143 */       int remainingData = data.readableBytes();
/* 144 */       Http2Flags flags = new Http2Flags();
/* 145 */       flags.endOfStream(false);
/* 146 */       flags.paddingPresent(false);
/*     */       
/* 148 */       if (remainingData > this.maxFrameSize) {
/* 149 */         frameHeader = ctx.alloc().buffer(9);
/* 150 */         Http2CodecUtil.writeFrameHeaderInternal(frameHeader, this.maxFrameSize, (byte)0, flags, streamId);
/*     */         
/*     */         do {
/* 153 */           ctx.write(frameHeader.retainedSlice(), promiseAggregator.newPromise());
/*     */ 
/*     */           
/* 156 */           ctx.write(data.readRetainedSlice(this.maxFrameSize), promiseAggregator.newPromise());
/*     */           
/* 158 */           remainingData -= this.maxFrameSize;
/*     */         }
/* 160 */         while (remainingData > this.maxFrameSize);
/*     */       } 
/*     */       
/* 163 */       if (padding == 0) {
/*     */         
/* 165 */         if (frameHeader != null) {
/* 166 */           frameHeader.release();
/* 167 */           frameHeader = null;
/*     */         } 
/* 169 */         ByteBuf frameHeader2 = ctx.alloc().buffer(9);
/* 170 */         flags.endOfStream(endStream);
/* 171 */         Http2CodecUtil.writeFrameHeaderInternal(frameHeader2, remainingData, (byte)0, flags, streamId);
/* 172 */         ctx.write(frameHeader2, promiseAggregator.newPromise());
/*     */ 
/*     */         
/* 175 */         ByteBuf lastFrame = data.readSlice(remainingData);
/* 176 */         data = null;
/* 177 */         ctx.write(lastFrame, promiseAggregator.newPromise());
/*     */       } else {
/* 179 */         if (remainingData != this.maxFrameSize) {
/* 180 */           if (frameHeader != null) {
/* 181 */             frameHeader.release();
/* 182 */             frameHeader = null;
/*     */           } 
/*     */         } else {
/* 185 */           remainingData -= this.maxFrameSize;
/*     */ 
/*     */           
/* 188 */           if (frameHeader == null) {
/* 189 */             lastFrame = ctx.alloc().buffer(9);
/* 190 */             Http2CodecUtil.writeFrameHeaderInternal(lastFrame, this.maxFrameSize, (byte)0, flags, streamId);
/*     */           } else {
/* 192 */             lastFrame = frameHeader.slice();
/* 193 */             frameHeader = null;
/*     */           } 
/* 195 */           ctx.write(lastFrame, promiseAggregator.newPromise());
/*     */ 
/*     */           
/* 198 */           ByteBuf lastFrame = data.readSlice(this.maxFrameSize);
/* 199 */           data = null;
/* 200 */           ctx.write(lastFrame, promiseAggregator.newPromise());
/*     */         } 
/*     */         
/*     */         do {
/* 204 */           int frameDataBytes = Math.min(remainingData, this.maxFrameSize);
/* 205 */           int framePaddingBytes = Math.min(padding, Math.max(0, this.maxFrameSize - 1 - frameDataBytes));
/*     */ 
/*     */           
/* 208 */           padding -= framePaddingBytes;
/* 209 */           remainingData -= frameDataBytes;
/*     */ 
/*     */           
/* 212 */           ByteBuf frameHeader2 = ctx.alloc().buffer(10);
/* 213 */           flags.endOfStream((endStream && remainingData == 0 && padding == 0));
/* 214 */           flags.paddingPresent((framePaddingBytes > 0));
/* 215 */           Http2CodecUtil.writeFrameHeaderInternal(frameHeader2, framePaddingBytes + frameDataBytes, (byte)0, flags, streamId);
/* 216 */           writePaddingLength(frameHeader2, framePaddingBytes);
/* 217 */           ctx.write(frameHeader2, promiseAggregator.newPromise());
/*     */ 
/*     */           
/* 220 */           if (frameDataBytes != 0) {
/* 221 */             if (remainingData == 0) {
/* 222 */               ByteBuf lastFrame = data.readSlice(frameDataBytes);
/* 223 */               data = null;
/* 224 */               ctx.write(lastFrame, promiseAggregator.newPromise());
/*     */             } else {
/* 226 */               ctx.write(data.readRetainedSlice(frameDataBytes), promiseAggregator.newPromise());
/*     */             } 
/*     */           }
/*     */           
/* 230 */           if (paddingBytes(framePaddingBytes) <= 0)
/* 231 */             continue;  ctx.write(ZERO_BUFFER.slice(0, paddingBytes(framePaddingBytes)), promiseAggregator
/* 232 */               .newPromise());
/*     */         }
/* 234 */         while (remainingData != 0 || padding != 0);
/*     */       } 
/* 236 */     } catch (Throwable cause) {
/* 237 */       if (frameHeader != null) {
/* 238 */         frameHeader.release();
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 243 */         if (data != null) {
/* 244 */           data.release();
/*     */         }
/*     */       } finally {
/* 247 */         promiseAggregator.setFailure(cause);
/* 248 */         promiseAggregator.doneAllocatingPromises();
/*     */       } 
/* 250 */       return (ChannelFuture)promiseAggregator;
/*     */     } 
/* 252 */     return (ChannelFuture)promiseAggregator.doneAllocatingPromises();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
/* 258 */     return writeHeadersInternal(ctx, streamId, headers, padding, endStream, false, 0, (short)0, false, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise) {
/* 266 */     return writeHeadersInternal(ctx, streamId, headers, padding, endStream, true, streamDependency, weight, exclusive, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
/*     */     try {
/* 274 */       verifyStreamId(streamId, "Stream ID");
/* 275 */       verifyStreamId(streamDependency, "Stream Dependency");
/* 276 */       verifyWeight(weight);
/*     */       
/* 278 */       ByteBuf buf = ctx.alloc().buffer(14);
/* 279 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 5, (byte)2, new Http2Flags(), streamId);
/* 280 */       buf.writeInt(exclusive ? (int)(0x80000000L | streamDependency) : streamDependency);
/*     */       
/* 282 */       buf.writeByte(weight - 1);
/* 283 */       return ctx.write(buf, promise);
/* 284 */     } catch (Throwable t) {
/* 285 */       return (ChannelFuture)promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/*     */     try {
/* 293 */       verifyStreamId(streamId, "Stream ID");
/* 294 */       verifyErrorCode(errorCode);
/*     */       
/* 296 */       ByteBuf buf = ctx.alloc().buffer(13);
/* 297 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)3, new Http2Flags(), streamId);
/* 298 */       buf.writeInt((int)errorCode);
/* 299 */       return ctx.write(buf, promise);
/* 300 */     } catch (Throwable t) {
/* 301 */       return (ChannelFuture)promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise) {
/*     */     try {
/* 309 */       ObjectUtil.checkNotNull(settings, "settings");
/* 310 */       int payloadLength = 6 * settings.size();
/* 311 */       ByteBuf buf = ctx.alloc().buffer(9 + settings.size() * 6);
/* 312 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)4, new Http2Flags(), 0);
/* 313 */       for (CharObjectMap.PrimitiveEntry<Long> entry : (Iterable<CharObjectMap.PrimitiveEntry<Long>>)settings.entries()) {
/* 314 */         buf.writeChar(entry.key());
/* 315 */         buf.writeInt(((Long)entry.value()).intValue());
/*     */       } 
/* 317 */       return ctx.write(buf, promise);
/* 318 */     } catch (Throwable t) {
/* 319 */       return (ChannelFuture)promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise) {
/*     */     try {
/* 326 */       ByteBuf buf = ctx.alloc().buffer(9);
/* 327 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 0, (byte)4, (new Http2Flags()).ack(true), 0);
/* 328 */       return ctx.write(buf, promise);
/* 329 */     } catch (Throwable t) {
/* 330 */       return (ChannelFuture)promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, long data, ChannelPromise promise) {
/* 336 */     Http2Flags flags = ack ? (new Http2Flags()).ack(true) : new Http2Flags();
/* 337 */     ByteBuf buf = ctx.alloc().buffer(17);
/*     */ 
/*     */     
/* 340 */     Http2CodecUtil.writeFrameHeaderInternal(buf, 8, (byte)6, flags, 0);
/* 341 */     buf.writeLong(data);
/* 342 */     return ctx.write(buf, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise) {
/* 348 */     ByteBuf headerBlock = null;
/*     */     
/* 350 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try {
/* 352 */       verifyStreamId(streamId, "Stream ID");
/* 353 */       verifyStreamId(promisedStreamId, "Promised Stream ID");
/* 354 */       Http2CodecUtil.verifyPadding(padding);
/*     */ 
/*     */       
/* 357 */       headerBlock = ctx.alloc().buffer();
/* 358 */       this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
/*     */ 
/*     */       
/* 361 */       Http2Flags flags = (new Http2Flags()).paddingPresent((padding > 0));
/*     */       
/* 363 */       int nonFragmentLength = 4 + padding;
/* 364 */       int maxFragmentLength = this.maxFrameSize - nonFragmentLength;
/* 365 */       ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
/*     */       
/* 367 */       flags.endOfHeaders(!headerBlock.isReadable());
/*     */       
/* 369 */       int payloadLength = fragment.readableBytes() + nonFragmentLength;
/* 370 */       ByteBuf buf = ctx.alloc().buffer(14);
/* 371 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)5, flags, streamId);
/* 372 */       writePaddingLength(buf, padding);
/*     */ 
/*     */       
/* 375 */       buf.writeInt(promisedStreamId);
/* 376 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */ 
/*     */       
/* 379 */       ctx.write(fragment, promiseAggregator.newPromise());
/*     */ 
/*     */       
/* 382 */       if (paddingBytes(padding) > 0) {
/* 383 */         ctx.write(ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
/*     */       }
/*     */       
/* 386 */       if (!flags.endOfHeaders()) {
/* 387 */         writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
/*     */       }
/* 389 */     } catch (Http2Exception e) {
/* 390 */       promiseAggregator.setFailure(e);
/* 391 */     } catch (Throwable t) {
/* 392 */       promiseAggregator.setFailure(t);
/* 393 */       promiseAggregator.doneAllocatingPromises();
/* 394 */       PlatformDependent.throwException(t);
/*     */     } finally {
/* 396 */       if (headerBlock != null) {
/* 397 */         headerBlock.release();
/*     */       }
/*     */     } 
/* 400 */     return (ChannelFuture)promiseAggregator.doneAllocatingPromises();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise) {
/* 407 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try {
/* 409 */       verifyStreamOrConnectionId(lastStreamId, "Last Stream ID");
/* 410 */       verifyErrorCode(errorCode);
/*     */       
/* 412 */       int payloadLength = 8 + debugData.readableBytes();
/* 413 */       ByteBuf buf = ctx.alloc().buffer(17);
/*     */ 
/*     */       
/* 416 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)7, new Http2Flags(), 0);
/* 417 */       buf.writeInt(lastStreamId);
/* 418 */       buf.writeInt((int)errorCode);
/* 419 */       ctx.write(buf, promiseAggregator.newPromise());
/* 420 */     } catch (Throwable t) {
/*     */       try {
/* 422 */         debugData.release();
/*     */       } finally {
/* 424 */         promiseAggregator.setFailure(t);
/* 425 */         promiseAggregator.doneAllocatingPromises();
/*     */       } 
/* 427 */       return (ChannelFuture)promiseAggregator;
/*     */     } 
/*     */     
/*     */     try {
/* 431 */       ctx.write(debugData, promiseAggregator.newPromise());
/* 432 */     } catch (Throwable t) {
/* 433 */       promiseAggregator.setFailure(t);
/*     */     } 
/* 435 */     return (ChannelFuture)promiseAggregator.doneAllocatingPromises();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise) {
/*     */     try {
/* 442 */       verifyStreamOrConnectionId(streamId, "Stream ID");
/* 443 */       verifyWindowSizeIncrement(windowSizeIncrement);
/*     */       
/* 445 */       ByteBuf buf = ctx.alloc().buffer(13);
/* 446 */       Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)8, new Http2Flags(), streamId);
/* 447 */       buf.writeInt(windowSizeIncrement);
/* 448 */       return ctx.write(buf, promise);
/* 449 */     } catch (Throwable t) {
/* 450 */       return (ChannelFuture)promise.setFailure(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise) {
/* 458 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try {
/* 460 */       verifyStreamOrConnectionId(streamId, "Stream ID");
/* 461 */       ByteBuf buf = ctx.alloc().buffer(9);
/*     */ 
/*     */       
/* 464 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payload.readableBytes(), frameType, flags, streamId);
/* 465 */       ctx.write(buf, promiseAggregator.newPromise());
/* 466 */     } catch (Throwable t) {
/*     */       try {
/* 468 */         payload.release();
/*     */       } finally {
/* 470 */         promiseAggregator.setFailure(t);
/* 471 */         promiseAggregator.doneAllocatingPromises();
/*     */       } 
/* 473 */       return (ChannelFuture)promiseAggregator;
/*     */     } 
/*     */     try {
/* 476 */       ctx.write(payload, promiseAggregator.newPromise());
/* 477 */     } catch (Throwable t) {
/* 478 */       promiseAggregator.setFailure(t);
/*     */     } 
/* 480 */     return (ChannelFuture)promiseAggregator.doneAllocatingPromises();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture writeHeadersInternal(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, boolean hasPriority, int streamDependency, short weight, boolean exclusive, ChannelPromise promise) {
/* 486 */     ByteBuf headerBlock = null;
/*     */     
/* 488 */     Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/*     */     try {
/* 490 */       verifyStreamId(streamId, "Stream ID");
/* 491 */       if (hasPriority) {
/* 492 */         verifyStreamOrConnectionId(streamDependency, "Stream Dependency");
/* 493 */         Http2CodecUtil.verifyPadding(padding);
/* 494 */         verifyWeight(weight);
/*     */       } 
/*     */ 
/*     */       
/* 498 */       headerBlock = ctx.alloc().buffer();
/* 499 */       this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
/*     */ 
/*     */       
/* 502 */       Http2Flags flags = (new Http2Flags()).endOfStream(endStream).priorityPresent(hasPriority).paddingPresent((padding > 0));
/*     */ 
/*     */       
/* 505 */       int nonFragmentBytes = padding + flags.getNumPriorityBytes();
/* 506 */       int maxFragmentLength = this.maxFrameSize - nonFragmentBytes;
/* 507 */       ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
/*     */ 
/*     */       
/* 510 */       flags.endOfHeaders(!headerBlock.isReadable());
/*     */       
/* 512 */       int payloadLength = fragment.readableBytes() + nonFragmentBytes;
/* 513 */       ByteBuf buf = ctx.alloc().buffer(15);
/* 514 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)1, flags, streamId);
/* 515 */       writePaddingLength(buf, padding);
/*     */       
/* 517 */       if (hasPriority) {
/* 518 */         buf.writeInt(exclusive ? (int)(0x80000000L | streamDependency) : streamDependency);
/*     */ 
/*     */         
/* 521 */         buf.writeByte(weight - 1);
/*     */       } 
/* 523 */       ctx.write(buf, promiseAggregator.newPromise());
/*     */ 
/*     */       
/* 526 */       ctx.write(fragment, promiseAggregator.newPromise());
/*     */ 
/*     */       
/* 529 */       if (paddingBytes(padding) > 0) {
/* 530 */         ctx.write(ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
/*     */       }
/*     */       
/* 533 */       if (!flags.endOfHeaders()) {
/* 534 */         writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
/*     */       }
/* 536 */     } catch (Http2Exception e) {
/* 537 */       promiseAggregator.setFailure(e);
/* 538 */     } catch (Throwable t) {
/* 539 */       promiseAggregator.setFailure(t);
/* 540 */       promiseAggregator.doneAllocatingPromises();
/* 541 */       PlatformDependent.throwException(t);
/*     */     } finally {
/* 543 */       if (headerBlock != null) {
/* 544 */         headerBlock.release();
/*     */       }
/*     */     } 
/* 547 */     return (ChannelFuture)promiseAggregator.doneAllocatingPromises();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture writeContinuationFrames(ChannelHandlerContext ctx, int streamId, ByteBuf headerBlock, int padding, Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
/* 555 */     Http2Flags flags = (new Http2Flags()).paddingPresent((padding > 0));
/* 556 */     int maxFragmentLength = this.maxFrameSize - padding;
/*     */     
/* 558 */     if (maxFragmentLength <= 0) {
/* 559 */       return (ChannelFuture)promiseAggregator.setFailure(new IllegalArgumentException("Padding [" + padding + "] is too large for max frame size [" + this.maxFrameSize + "]"));
/*     */     }
/*     */ 
/*     */     
/* 563 */     if (headerBlock.isReadable()) {
/*     */       
/* 565 */       int fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
/* 566 */       int payloadLength = fragmentReadableBytes + padding;
/* 567 */       ByteBuf buf = ctx.alloc().buffer(10);
/* 568 */       Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
/* 569 */       writePaddingLength(buf, padding);
/*     */       
/*     */       do {
/* 572 */         fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
/* 573 */         ByteBuf fragment = headerBlock.readRetainedSlice(fragmentReadableBytes);
/*     */         
/* 575 */         payloadLength = fragmentReadableBytes + padding;
/* 576 */         if (headerBlock.isReadable()) {
/* 577 */           ctx.write(buf.retain(), promiseAggregator.newPromise());
/*     */         } else {
/*     */           
/* 580 */           flags = flags.endOfHeaders(true);
/* 581 */           buf.release();
/* 582 */           buf = ctx.alloc().buffer(10);
/* 583 */           Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
/* 584 */           writePaddingLength(buf, padding);
/* 585 */           ctx.write(buf, promiseAggregator.newPromise());
/*     */         } 
/*     */         
/* 588 */         ctx.write(fragment, promiseAggregator.newPromise());
/*     */ 
/*     */         
/* 591 */         if (paddingBytes(padding) <= 0)
/* 592 */           continue;  ctx.write(ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
/*     */       }
/* 594 */       while (headerBlock.isReadable());
/*     */     } 
/* 596 */     return (ChannelFuture)promiseAggregator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int paddingBytes(int padding) {
/* 605 */     return padding - 1;
/*     */   }
/*     */   
/*     */   private static void writePaddingLength(ByteBuf buf, int padding) {
/* 609 */     if (padding > 0)
/*     */     {
/*     */       
/* 612 */       buf.writeByte(padding - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyStreamId(int streamId, String argumentName) {
/* 617 */     if (streamId <= 0) {
/* 618 */       throw new IllegalArgumentException(argumentName + " must be > 0");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyStreamOrConnectionId(int streamId, String argumentName) {
/* 623 */     if (streamId < 0) {
/* 624 */       throw new IllegalArgumentException(argumentName + " must be >= 0");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyWeight(short weight) {
/* 629 */     if (weight < 1 || weight > 256) {
/* 630 */       throw new IllegalArgumentException("Invalid weight: " + weight);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyErrorCode(long errorCode) {
/* 635 */     if (errorCode < 0L || errorCode > 4294967295L) {
/* 636 */       throw new IllegalArgumentException("Invalid errorCode: " + errorCode);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyWindowSizeIncrement(int windowSizeIncrement) {
/* 641 */     if (windowSizeIncrement < 0) {
/* 642 */       throw new IllegalArgumentException("WindowSizeIncrement must be >= 0");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void verifyPingPayload(ByteBuf data) {
/* 647 */     if (data == null || data.readableBytes() != 8)
/* 648 */       throw new IllegalArgumentException("Opaque data must be 8 bytes"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2FrameWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */