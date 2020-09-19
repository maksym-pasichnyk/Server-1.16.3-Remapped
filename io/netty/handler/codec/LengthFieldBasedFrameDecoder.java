/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.nio.ByteOrder;
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
/*     */ 
/*     */ public class LengthFieldBasedFrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private final ByteOrder byteOrder;
/*     */   private final int maxFrameLength;
/*     */   private final int lengthFieldOffset;
/*     */   private final int lengthFieldLength;
/*     */   private final int lengthFieldEndOffset;
/*     */   private final int lengthAdjustment;
/*     */   private final int initialBytesToStrip;
/*     */   private final boolean failFast;
/*     */   private boolean discardingTooLongFrame;
/*     */   private long tooLongFrameLength;
/*     */   private long bytesToDiscard;
/*     */   
/*     */   public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
/* 213 */     this(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
/* 236 */     this(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
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
/*     */   public LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
/* 268 */     this(ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
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
/*     */   public LengthFieldBasedFrameDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
/* 301 */     if (byteOrder == null) {
/* 302 */       throw new NullPointerException("byteOrder");
/*     */     }
/*     */     
/* 305 */     if (maxFrameLength <= 0) {
/* 306 */       throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + maxFrameLength);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 311 */     if (lengthFieldOffset < 0) {
/* 312 */       throw new IllegalArgumentException("lengthFieldOffset must be a non-negative integer: " + lengthFieldOffset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 317 */     if (initialBytesToStrip < 0) {
/* 318 */       throw new IllegalArgumentException("initialBytesToStrip must be a non-negative integer: " + initialBytesToStrip);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (lengthFieldOffset > maxFrameLength - lengthFieldLength) {
/* 324 */       throw new IllegalArgumentException("maxFrameLength (" + maxFrameLength + ") must be equal to or greater than lengthFieldOffset (" + lengthFieldOffset + ") + lengthFieldLength (" + lengthFieldLength + ").");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     this.byteOrder = byteOrder;
/* 332 */     this.maxFrameLength = maxFrameLength;
/* 333 */     this.lengthFieldOffset = lengthFieldOffset;
/* 334 */     this.lengthFieldLength = lengthFieldLength;
/* 335 */     this.lengthAdjustment = lengthAdjustment;
/* 336 */     this.lengthFieldEndOffset = lengthFieldOffset + lengthFieldLength;
/* 337 */     this.initialBytesToStrip = initialBytesToStrip;
/* 338 */     this.failFast = failFast;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/* 343 */     Object decoded = decode(ctx, in);
/* 344 */     if (decoded != null) {
/* 345 */       out.add(decoded);
/*     */     }
/*     */   }
/*     */   
/*     */   private void discardingTooLongFrame(ByteBuf in) {
/* 350 */     long bytesToDiscard = this.bytesToDiscard;
/* 351 */     int localBytesToDiscard = (int)Math.min(bytesToDiscard, in.readableBytes());
/* 352 */     in.skipBytes(localBytesToDiscard);
/* 353 */     bytesToDiscard -= localBytesToDiscard;
/* 354 */     this.bytesToDiscard = bytesToDiscard;
/*     */     
/* 356 */     failIfNecessary(false);
/*     */   }
/*     */   
/*     */   private static void failOnNegativeLengthField(ByteBuf in, long frameLength, int lengthFieldEndOffset) {
/* 360 */     in.skipBytes(lengthFieldEndOffset);
/* 361 */     throw new CorruptedFrameException("negative pre-adjustment length field: " + frameLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void failOnFrameLengthLessThanLengthFieldEndOffset(ByteBuf in, long frameLength, int lengthFieldEndOffset) {
/* 368 */     in.skipBytes(lengthFieldEndOffset);
/* 369 */     throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less than lengthFieldEndOffset: " + lengthFieldEndOffset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void exceededFrameLength(ByteBuf in, long frameLength) {
/* 375 */     long discard = frameLength - in.readableBytes();
/* 376 */     this.tooLongFrameLength = frameLength;
/*     */     
/* 378 */     if (discard < 0L) {
/*     */       
/* 380 */       in.skipBytes((int)frameLength);
/*     */     } else {
/*     */       
/* 383 */       this.discardingTooLongFrame = true;
/* 384 */       this.bytesToDiscard = discard;
/* 385 */       in.skipBytes(in.readableBytes());
/*     */     } 
/* 387 */     failIfNecessary(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void failOnFrameLengthLessThanInitialBytesToStrip(ByteBuf in, long frameLength, int initialBytesToStrip) {
/* 393 */     in.skipBytes((int)frameLength);
/* 394 */     throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less than initialBytesToStrip: " + initialBytesToStrip);
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
/*     */   protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
/* 408 */     if (this.discardingTooLongFrame) {
/* 409 */       discardingTooLongFrame(in);
/*     */     }
/*     */     
/* 412 */     if (in.readableBytes() < this.lengthFieldEndOffset) {
/* 413 */       return null;
/*     */     }
/*     */     
/* 416 */     int actualLengthFieldOffset = in.readerIndex() + this.lengthFieldOffset;
/* 417 */     long frameLength = getUnadjustedFrameLength(in, actualLengthFieldOffset, this.lengthFieldLength, this.byteOrder);
/*     */     
/* 419 */     if (frameLength < 0L) {
/* 420 */       failOnNegativeLengthField(in, frameLength, this.lengthFieldEndOffset);
/*     */     }
/*     */     
/* 423 */     frameLength += (this.lengthAdjustment + this.lengthFieldEndOffset);
/*     */     
/* 425 */     if (frameLength < this.lengthFieldEndOffset) {
/* 426 */       failOnFrameLengthLessThanLengthFieldEndOffset(in, frameLength, this.lengthFieldEndOffset);
/*     */     }
/*     */     
/* 429 */     if (frameLength > this.maxFrameLength) {
/* 430 */       exceededFrameLength(in, frameLength);
/* 431 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 435 */     int frameLengthInt = (int)frameLength;
/* 436 */     if (in.readableBytes() < frameLengthInt) {
/* 437 */       return null;
/*     */     }
/*     */     
/* 440 */     if (this.initialBytesToStrip > frameLengthInt) {
/* 441 */       failOnFrameLengthLessThanInitialBytesToStrip(in, frameLength, this.initialBytesToStrip);
/*     */     }
/* 443 */     in.skipBytes(this.initialBytesToStrip);
/*     */ 
/*     */     
/* 446 */     int readerIndex = in.readerIndex();
/* 447 */     int actualFrameLength = frameLengthInt - this.initialBytesToStrip;
/* 448 */     ByteBuf frame = extractFrame(ctx, in, readerIndex, actualFrameLength);
/* 449 */     in.readerIndex(readerIndex + actualFrameLength);
/* 450 */     return frame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
/*     */     long frameLength;
/* 462 */     buf = buf.order(order);
/*     */     
/* 464 */     switch (length) {
/*     */       case 1:
/* 466 */         frameLength = buf.getUnsignedByte(offset);
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
/* 484 */         return frameLength;case 2: frameLength = buf.getUnsignedShort(offset); return frameLength;case 3: frameLength = buf.getUnsignedMedium(offset); return frameLength;case 4: frameLength = buf.getUnsignedInt(offset); return frameLength;case 8: frameLength = buf.getLong(offset); return frameLength;
/*     */     } 
/*     */     throw new DecoderException("unsupported lengthFieldLength: " + this.lengthFieldLength + " (expected: 1, 2, 3, 4, or 8)");
/*     */   } private void failIfNecessary(boolean firstDetectionOfTooLongFrame) {
/* 488 */     if (this.bytesToDiscard == 0L) {
/*     */ 
/*     */       
/* 491 */       long tooLongFrameLength = this.tooLongFrameLength;
/* 492 */       this.tooLongFrameLength = 0L;
/* 493 */       this.discardingTooLongFrame = false;
/* 494 */       if (!this.failFast || firstDetectionOfTooLongFrame) {
/* 495 */         fail(tooLongFrameLength);
/*     */       
/*     */       }
/*     */     }
/* 499 */     else if (this.failFast && firstDetectionOfTooLongFrame) {
/* 500 */       fail(this.tooLongFrameLength);
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
/*     */   
/*     */   protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
/* 517 */     return buffer.retainedSlice(index, length);
/*     */   }
/*     */   
/*     */   private void fail(long frameLength) {
/* 521 */     if (frameLength > 0L) {
/* 522 */       throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
/*     */     }
/*     */ 
/*     */     
/* 526 */     throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + " - discarding");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\LengthFieldBasedFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */