/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class DefaultHttp2FrameReader
/*     */   implements Http2FrameReader, Http2FrameSizePolicy, Http2FrameReader.Configuration
/*     */ {
/*     */   private final Http2HeadersDecoder headersDecoder;
/*     */   private boolean readingHeaders = true;
/*     */   private boolean readError;
/*     */   private byte frameType;
/*     */   private int streamId;
/*     */   private Http2Flags flags;
/*     */   private int payloadLength;
/*     */   private HeadersContinuation headersContinuation;
/*     */   private int maxFrameSize;
/*     */   
/*     */   public DefaultHttp2FrameReader() {
/*  80 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttp2FrameReader(boolean validateHeaders) {
/*  89 */     this(new DefaultHttp2HeadersDecoder(validateHeaders));
/*     */   }
/*     */   
/*     */   public DefaultHttp2FrameReader(Http2HeadersDecoder headersDecoder) {
/*  93 */     this.headersDecoder = headersDecoder;
/*  94 */     this.maxFrameSize = 16384;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2HeadersDecoder.Configuration headersConfiguration() {
/*  99 */     return this.headersDecoder.configuration();
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameReader.Configuration configuration() {
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Http2FrameSizePolicy frameSizePolicy() {
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void maxFrameSize(int max) throws Http2Exception {
/* 114 */     if (!Http2CodecUtil.isMaxFrameSizeValid(max))
/* 115 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", new Object[] {
/* 116 */             Integer.valueOf(max)
/*     */           }); 
/* 118 */     this.maxFrameSize = max;
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxFrameSize() {
/* 123 */     return this.maxFrameSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 128 */     closeHeadersContinuation();
/*     */   }
/*     */   
/*     */   private void closeHeadersContinuation() {
/* 132 */     if (this.headersContinuation != null) {
/* 133 */       this.headersContinuation.close();
/* 134 */       this.headersContinuation = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFrame(ChannelHandlerContext ctx, ByteBuf input, Http2FrameListener listener) throws Http2Exception {
/* 141 */     if (this.readError) {
/* 142 */       input.skipBytes(input.readableBytes());
/*     */       return;
/*     */     } 
/*     */     try {
/*     */       do {
/* 147 */         if (this.readingHeaders) {
/* 148 */           processHeaderState(input);
/* 149 */           if (this.readingHeaders) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 160 */         processPayloadState(ctx, input, listener);
/* 161 */         if (!this.readingHeaders) {
/*     */           return;
/*     */         }
/*     */       }
/* 165 */       while (input.isReadable());
/* 166 */     } catch (Http2Exception e) {
/* 167 */       this.readError = !Http2Exception.isStreamError(e);
/* 168 */       throw e;
/* 169 */     } catch (RuntimeException e) {
/* 170 */       this.readError = true;
/* 171 */       throw e;
/* 172 */     } catch (Throwable cause) {
/* 173 */       this.readError = true;
/* 174 */       PlatformDependent.throwException(cause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processHeaderState(ByteBuf in) throws Http2Exception {
/* 179 */     if (in.readableBytes() < 9) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     this.payloadLength = in.readUnsignedMedium();
/* 186 */     if (this.payloadLength > this.maxFrameSize) {
/* 187 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length: %d exceeds maximum: %d", new Object[] { Integer.valueOf(this.payloadLength), 
/* 188 */             Integer.valueOf(this.maxFrameSize) });
/*     */     }
/* 190 */     this.frameType = in.readByte();
/* 191 */     this.flags = new Http2Flags(in.readUnsignedByte());
/* 192 */     this.streamId = Http2CodecUtil.readUnsignedInt(in);
/*     */ 
/*     */     
/* 195 */     this.readingHeaders = false;
/*     */     
/* 197 */     switch (this.frameType) {
/*     */       case 0:
/* 199 */         verifyDataFrame();
/*     */         return;
/*     */       case 1:
/* 202 */         verifyHeadersFrame();
/*     */         return;
/*     */       case 2:
/* 205 */         verifyPriorityFrame();
/*     */         return;
/*     */       case 3:
/* 208 */         verifyRstStreamFrame();
/*     */         return;
/*     */       case 4:
/* 211 */         verifySettingsFrame();
/*     */         return;
/*     */       case 5:
/* 214 */         verifyPushPromiseFrame();
/*     */         return;
/*     */       case 6:
/* 217 */         verifyPingFrame();
/*     */         return;
/*     */       case 7:
/* 220 */         verifyGoAwayFrame();
/*     */         return;
/*     */       case 8:
/* 223 */         verifyWindowUpdateFrame();
/*     */         return;
/*     */       case 9:
/* 226 */         verifyContinuationFrame();
/*     */         return;
/*     */     } 
/*     */     
/* 230 */     verifyUnknownFrame();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processPayloadState(ChannelHandlerContext ctx, ByteBuf in, Http2FrameListener listener) throws Http2Exception {
/* 237 */     if (in.readableBytes() < this.payloadLength) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 243 */     ByteBuf payload = in.readSlice(this.payloadLength);
/*     */ 
/*     */     
/* 246 */     this.readingHeaders = true;
/*     */ 
/*     */     
/* 249 */     switch (this.frameType) {
/*     */       case 0:
/* 251 */         readDataFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 1:
/* 254 */         readHeadersFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 2:
/* 257 */         readPriorityFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 3:
/* 260 */         readRstStreamFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 4:
/* 263 */         readSettingsFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 5:
/* 266 */         readPushPromiseFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 6:
/* 269 */         readPingFrame(ctx, payload.readLong(), listener);
/*     */         return;
/*     */       case 7:
/* 272 */         readGoAwayFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 8:
/* 275 */         readWindowUpdateFrame(ctx, payload, listener);
/*     */         return;
/*     */       case 9:
/* 278 */         readContinuationFrame(payload, listener);
/*     */         return;
/*     */     } 
/* 281 */     readUnknownFrame(ctx, payload, listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyDataFrame() throws Http2Exception {
/* 287 */     verifyAssociatedWithAStream();
/* 288 */     verifyNotProcessingHeaders();
/* 289 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 291 */     if (this.payloadLength < this.flags.getPaddingPresenceFieldLength())
/* 292 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", new Object[] {
/* 293 */             Integer.valueOf(this.payloadLength)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void verifyHeadersFrame() throws Http2Exception {
/* 298 */     verifyAssociatedWithAStream();
/* 299 */     verifyNotProcessingHeaders();
/* 300 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 302 */     int requiredLength = this.flags.getPaddingPresenceFieldLength() + this.flags.getNumPriorityBytes();
/* 303 */     if (this.payloadLength < requiredLength) {
/* 304 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length too small." + this.payloadLength, new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void verifyPriorityFrame() throws Http2Exception {
/* 310 */     verifyAssociatedWithAStream();
/* 311 */     verifyNotProcessingHeaders();
/*     */     
/* 313 */     if (this.payloadLength != 5)
/* 314 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", new Object[] {
/* 315 */             Integer.valueOf(this.payloadLength)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void verifyRstStreamFrame() throws Http2Exception {
/* 320 */     verifyAssociatedWithAStream();
/* 321 */     verifyNotProcessingHeaders();
/*     */     
/* 323 */     if (this.payloadLength != 4) {
/* 324 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifySettingsFrame() throws Http2Exception {
/* 329 */     verifyNotProcessingHeaders();
/* 330 */     verifyPayloadLength(this.payloadLength);
/* 331 */     if (this.streamId != 0) {
/* 332 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
/*     */     }
/* 334 */     if (this.flags.ack() && this.payloadLength > 0) {
/* 335 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Ack settings frame must have an empty payload.", new Object[0]);
/*     */     }
/* 337 */     if (this.payloadLength % 6 > 0) {
/* 338 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d invalid.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyPushPromiseFrame() throws Http2Exception {
/* 343 */     verifyNotProcessingHeaders();
/* 344 */     verifyPayloadLength(this.payloadLength);
/*     */ 
/*     */ 
/*     */     
/* 348 */     int minLength = this.flags.getPaddingPresenceFieldLength() + 4;
/* 349 */     if (this.payloadLength < minLength)
/* 350 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", new Object[] {
/* 351 */             Integer.valueOf(this.payloadLength)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void verifyPingFrame() throws Http2Exception {
/* 356 */     verifyNotProcessingHeaders();
/* 357 */     if (this.streamId != 0) {
/* 358 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
/*     */     }
/* 360 */     if (this.payloadLength != 8)
/* 361 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d incorrect size for ping.", new Object[] {
/* 362 */             Integer.valueOf(this.payloadLength)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void verifyGoAwayFrame() throws Http2Exception {
/* 367 */     verifyNotProcessingHeaders();
/* 368 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 370 */     if (this.streamId != 0) {
/* 371 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "A stream ID must be zero.", new Object[0]);
/*     */     }
/* 373 */     if (this.payloadLength < 8) {
/* 374 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyWindowUpdateFrame() throws Http2Exception {
/* 379 */     verifyNotProcessingHeaders();
/* 380 */     verifyStreamOrConnectionId(this.streamId, "Stream ID");
/*     */     
/* 382 */     if (this.payloadLength != 4) {
/* 383 */       throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid frame length %d.", new Object[] { Integer.valueOf(this.payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyContinuationFrame() throws Http2Exception {
/* 388 */     verifyAssociatedWithAStream();
/* 389 */     verifyPayloadLength(this.payloadLength);
/*     */     
/* 391 */     if (this.headersContinuation == null) {
/* 392 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received %s frame but not currently processing headers.", new Object[] {
/* 393 */             Byte.valueOf(this.frameType)
/*     */           });
/*     */     }
/* 396 */     if (this.streamId != this.headersContinuation.getStreamId()) {
/* 397 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Continuation stream ID does not match pending headers. Expected %d, but received %d.", new Object[] {
/* 398 */             Integer.valueOf(this.headersContinuation.getStreamId()), Integer.valueOf(this.streamId)
/*     */           });
/*     */     }
/* 401 */     if (this.payloadLength < this.flags.getPaddingPresenceFieldLength())
/* 402 */       throw Http2Exception.streamError(this.streamId, Http2Error.FRAME_SIZE_ERROR, "Frame length %d too small for padding.", new Object[] {
/* 403 */             Integer.valueOf(this.payloadLength)
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void verifyUnknownFrame() throws Http2Exception {
/* 408 */     verifyNotProcessingHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   private void readDataFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 413 */     int padding = readPadding(payload);
/* 414 */     verifyPadding(padding);
/*     */ 
/*     */ 
/*     */     
/* 418 */     int dataLength = lengthWithoutTrailingPadding(payload.readableBytes(), padding);
/*     */     
/* 420 */     ByteBuf data = payload.readSlice(dataLength);
/* 421 */     listener.onDataRead(ctx, this.streamId, data, padding, this.flags.endOfStream());
/* 422 */     payload.skipBytes(payload.readableBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readHeadersFrame(final ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 427 */     final int headersStreamId = this.streamId;
/* 428 */     final Http2Flags headersFlags = this.flags;
/* 429 */     final int padding = readPadding(payload);
/* 430 */     verifyPadding(padding);
/*     */ 
/*     */ 
/*     */     
/* 434 */     if (this.flags.priorityPresent()) {
/* 435 */       long word1 = payload.readUnsignedInt();
/* 436 */       final boolean exclusive = ((word1 & 0x80000000L) != 0L);
/* 437 */       final int streamDependency = (int)(word1 & 0x7FFFFFFFL);
/* 438 */       if (streamDependency == this.streamId) {
/* 439 */         throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "A stream cannot depend on itself.", new Object[0]);
/*     */       }
/* 441 */       final short weight = (short)(payload.readUnsignedByte() + 1);
/* 442 */       ByteBuf byteBuf = payload.readSlice(lengthWithoutTrailingPadding(payload.readableBytes(), padding));
/*     */ 
/*     */       
/* 445 */       this.headersContinuation = new HeadersContinuation()
/*     */         {
/*     */           public int getStreamId() {
/* 448 */             return headersStreamId;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void processFragment(boolean endOfHeaders, ByteBuf fragment, Http2FrameListener listener) throws Http2Exception {
/* 454 */             DefaultHttp2FrameReader.HeadersBlockBuilder hdrBlockBuilder = headersBlockBuilder();
/* 455 */             hdrBlockBuilder.addFragment(fragment, ctx.alloc(), endOfHeaders);
/* 456 */             if (endOfHeaders) {
/* 457 */               listener.onHeadersRead(ctx, headersStreamId, hdrBlockBuilder.headers(), streamDependency, weight, exclusive, padding, headersFlags
/* 458 */                   .endOfStream());
/*     */             }
/*     */           }
/*     */         };
/*     */ 
/*     */       
/* 464 */       this.headersContinuation.processFragment(this.flags.endOfHeaders(), byteBuf, listener);
/* 465 */       resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 471 */     this.headersContinuation = new HeadersContinuation()
/*     */       {
/*     */         public int getStreamId() {
/* 474 */           return headersStreamId;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void processFragment(boolean endOfHeaders, ByteBuf fragment, Http2FrameListener listener) throws Http2Exception {
/* 480 */           DefaultHttp2FrameReader.HeadersBlockBuilder hdrBlockBuilder = headersBlockBuilder();
/* 481 */           hdrBlockBuilder.addFragment(fragment, ctx.alloc(), endOfHeaders);
/* 482 */           if (endOfHeaders) {
/* 483 */             listener.onHeadersRead(ctx, headersStreamId, hdrBlockBuilder.headers(), padding, headersFlags
/* 484 */                 .endOfStream());
/*     */           }
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 490 */     ByteBuf fragment = payload.readSlice(lengthWithoutTrailingPadding(payload.readableBytes(), padding));
/* 491 */     this.headersContinuation.processFragment(this.flags.endOfHeaders(), fragment, listener);
/* 492 */     resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
/*     */   }
/*     */   
/*     */   private void resetHeadersContinuationIfEnd(boolean endOfHeaders) {
/* 496 */     if (endOfHeaders) {
/* 497 */       closeHeadersContinuation();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void readPriorityFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 503 */     long word1 = payload.readUnsignedInt();
/* 504 */     boolean exclusive = ((word1 & 0x80000000L) != 0L);
/* 505 */     int streamDependency = (int)(word1 & 0x7FFFFFFFL);
/* 506 */     if (streamDependency == this.streamId) {
/* 507 */       throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "A stream cannot depend on itself.", new Object[0]);
/*     */     }
/* 509 */     short weight = (short)(payload.readUnsignedByte() + 1);
/* 510 */     listener.onPriorityRead(ctx, this.streamId, streamDependency, weight, exclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readRstStreamFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 515 */     long errorCode = payload.readUnsignedInt();
/* 516 */     listener.onRstStreamRead(ctx, this.streamId, errorCode);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readSettingsFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 521 */     if (this.flags.ack()) {
/* 522 */       listener.onSettingsAckRead(ctx);
/*     */     } else {
/* 524 */       int numSettings = this.payloadLength / 6;
/* 525 */       Http2Settings settings = new Http2Settings();
/* 526 */       for (int index = 0; index < numSettings; index++) {
/* 527 */         char id = (char)payload.readUnsignedShort();
/* 528 */         long value = payload.readUnsignedInt();
/*     */         try {
/* 530 */           settings.put(id, Long.valueOf(value));
/* 531 */         } catch (IllegalArgumentException e) {
/* 532 */           switch (id) {
/*     */             case '\005':
/* 534 */               throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, e, e.getMessage(), new Object[0]);
/*     */             case '\004':
/* 536 */               throw Http2Exception.connectionError(Http2Error.FLOW_CONTROL_ERROR, e, e.getMessage(), new Object[0]);
/*     */           } 
/* 538 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, e, e.getMessage(), new Object[0]);
/*     */         } 
/*     */       } 
/*     */       
/* 542 */       listener.onSettingsRead(ctx, settings);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readPushPromiseFrame(final ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 548 */     final int pushPromiseStreamId = this.streamId;
/* 549 */     final int padding = readPadding(payload);
/* 550 */     verifyPadding(padding);
/* 551 */     final int promisedStreamId = Http2CodecUtil.readUnsignedInt(payload);
/*     */ 
/*     */     
/* 554 */     this.headersContinuation = new HeadersContinuation()
/*     */       {
/*     */         public int getStreamId() {
/* 557 */           return pushPromiseStreamId;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void processFragment(boolean endOfHeaders, ByteBuf fragment, Http2FrameListener listener) throws Http2Exception {
/* 563 */           headersBlockBuilder().addFragment(fragment, ctx.alloc(), endOfHeaders);
/* 564 */           if (endOfHeaders) {
/* 565 */             listener.onPushPromiseRead(ctx, pushPromiseStreamId, promisedStreamId, 
/* 566 */                 headersBlockBuilder().headers(), padding);
/*     */           }
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 572 */     ByteBuf fragment = payload.readSlice(lengthWithoutTrailingPadding(payload.readableBytes(), padding));
/* 573 */     this.headersContinuation.processFragment(this.flags.endOfHeaders(), fragment, listener);
/* 574 */     resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readPingFrame(ChannelHandlerContext ctx, long data, Http2FrameListener listener) throws Http2Exception {
/* 579 */     if (this.flags.ack()) {
/* 580 */       listener.onPingAckRead(ctx, data);
/*     */     } else {
/* 582 */       listener.onPingRead(ctx, data);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void readGoAwayFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 588 */     int lastStreamId = Http2CodecUtil.readUnsignedInt(payload);
/* 589 */     long errorCode = payload.readUnsignedInt();
/* 590 */     ByteBuf debugData = payload.readSlice(payload.readableBytes());
/* 591 */     listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readWindowUpdateFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 596 */     int windowSizeIncrement = Http2CodecUtil.readUnsignedInt(payload);
/* 597 */     if (windowSizeIncrement == 0)
/* 598 */       throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Received WINDOW_UPDATE with delta 0 for stream: %d", new Object[] {
/* 599 */             Integer.valueOf(this.streamId)
/*     */           }); 
/* 601 */     listener.onWindowUpdateRead(ctx, this.streamId, windowSizeIncrement);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readContinuationFrame(ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 607 */     ByteBuf continuationFragment = payload.readSlice(payload.readableBytes());
/* 608 */     this.headersContinuation.processFragment(this.flags.endOfHeaders(), continuationFragment, listener);
/*     */     
/* 610 */     resetHeadersContinuationIfEnd(this.flags.endOfHeaders());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readUnknownFrame(ChannelHandlerContext ctx, ByteBuf payload, Http2FrameListener listener) throws Http2Exception {
/* 615 */     payload = payload.readSlice(payload.readableBytes());
/* 616 */     listener.onUnknownFrame(ctx, this.frameType, this.streamId, this.flags, payload);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readPadding(ByteBuf payload) {
/* 624 */     if (!this.flags.paddingPresent()) {
/* 625 */       return 0;
/*     */     }
/* 627 */     return payload.readUnsignedByte() + 1;
/*     */   }
/*     */   
/*     */   private void verifyPadding(int padding) throws Http2Exception {
/* 631 */     int len = lengthWithoutTrailingPadding(this.payloadLength, padding);
/* 632 */     if (len < 0) {
/* 633 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Frame payload too small for padding.", new Object[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int lengthWithoutTrailingPadding(int readableBytes, int padding) {
/* 642 */     return (padding == 0) ? readableBytes : (readableBytes - padding - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class HeadersContinuation
/*     */   {
/* 653 */     private final DefaultHttp2FrameReader.HeadersBlockBuilder builder = new DefaultHttp2FrameReader.HeadersBlockBuilder();
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
/*     */     final DefaultHttp2FrameReader.HeadersBlockBuilder headersBlockBuilder() {
/* 671 */       return this.builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final void close() {
/* 678 */       this.builder.close();
/*     */     }
/*     */ 
/*     */     
/*     */     private HeadersContinuation() {}
/*     */     
/*     */     abstract int getStreamId();
/*     */     
/*     */     abstract void processFragment(boolean param1Boolean, ByteBuf param1ByteBuf, Http2FrameListener param1Http2FrameListener) throws Http2Exception;
/*     */   }
/*     */   
/*     */   protected class HeadersBlockBuilder
/*     */   {
/*     */     private ByteBuf headerBlock;
/*     */     
/*     */     private void headerSizeExceeded() throws Http2Exception {
/* 694 */       close();
/* 695 */       Http2CodecUtil.headerListSizeExceeded(DefaultHttp2FrameReader.this.headersDecoder.configuration().maxHeaderListSizeGoAway());
/*     */     }
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
/*     */     final void addFragment(ByteBuf fragment, ByteBufAllocator alloc, boolean endOfHeaders) throws Http2Exception {
/* 708 */       if (this.headerBlock == null) {
/* 709 */         if (fragment.readableBytes() > DefaultHttp2FrameReader.this.headersDecoder.configuration().maxHeaderListSizeGoAway()) {
/* 710 */           headerSizeExceeded();
/*     */         }
/* 712 */         if (endOfHeaders) {
/*     */ 
/*     */           
/* 715 */           this.headerBlock = fragment.retain();
/*     */         } else {
/* 717 */           this.headerBlock = alloc.buffer(fragment.readableBytes());
/* 718 */           this.headerBlock.writeBytes(fragment);
/*     */         } 
/*     */         return;
/*     */       } 
/* 722 */       if (DefaultHttp2FrameReader.this.headersDecoder.configuration().maxHeaderListSizeGoAway() - fragment.readableBytes() < this.headerBlock
/* 723 */         .readableBytes()) {
/* 724 */         headerSizeExceeded();
/*     */       }
/* 726 */       if (this.headerBlock.isWritable(fragment.readableBytes())) {
/*     */         
/* 728 */         this.headerBlock.writeBytes(fragment);
/*     */       } else {
/*     */         
/* 731 */         ByteBuf buf = alloc.buffer(this.headerBlock.readableBytes() + fragment.readableBytes());
/* 732 */         buf.writeBytes(this.headerBlock);
/* 733 */         buf.writeBytes(fragment);
/* 734 */         this.headerBlock.release();
/* 735 */         this.headerBlock = buf;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Http2Headers headers() throws Http2Exception {
/*     */       try {
/* 745 */         return DefaultHttp2FrameReader.this.headersDecoder.decodeHeaders(DefaultHttp2FrameReader.this.streamId, this.headerBlock);
/*     */       } finally {
/* 747 */         close();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void close() {
/* 755 */       if (this.headerBlock != null) {
/* 756 */         this.headerBlock.release();
/* 757 */         this.headerBlock = null;
/*     */       } 
/*     */ 
/*     */       
/* 761 */       DefaultHttp2FrameReader.this.headersContinuation = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyNotProcessingHeaders() throws Http2Exception {
/* 770 */     if (this.headersContinuation != null)
/* 771 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received frame of type %s while processing headers on stream %d.", new Object[] {
/* 772 */             Byte.valueOf(this.frameType), Integer.valueOf(this.headersContinuation.getStreamId())
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void verifyPayloadLength(int payloadLength) throws Http2Exception {
/* 777 */     if (payloadLength > this.maxFrameSize) {
/* 778 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Total payload length %d exceeds max frame length.", new Object[] { Integer.valueOf(payloadLength) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyAssociatedWithAStream() throws Http2Exception {
/* 783 */     if (this.streamId == 0) {
/* 784 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Frame of type %s must be associated with a stream.", new Object[] { Byte.valueOf(this.frameType) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void verifyStreamOrConnectionId(int streamId, String argumentName) throws Http2Exception {
/* 790 */     if (streamId < 0)
/* 791 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "%s must be >= 0", new Object[] { argumentName }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2FrameReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */