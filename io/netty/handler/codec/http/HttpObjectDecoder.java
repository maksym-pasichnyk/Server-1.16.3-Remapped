/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.PrematureChannelClosureException;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.AppendableCharSequence;
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
/*     */ public abstract class HttpObjectDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static final String EMPTY_VALUE = "";
/*     */   private final int maxChunkSize;
/*     */   private final boolean chunkedSupported;
/*     */   protected final boolean validateHeaders;
/*     */   private final HeaderParser headerParser;
/*     */   private final LineParser lineParser;
/*     */   private HttpMessage message;
/*     */   private long chunkSize;
/* 113 */   private long contentLength = Long.MIN_VALUE;
/*     */ 
/*     */   
/*     */   private volatile boolean resetRequested;
/*     */   
/*     */   private CharSequence name;
/*     */   
/*     */   private CharSequence value;
/*     */   
/*     */   private LastHttpContent trailer;
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 127 */     SKIP_CONTROL_CHARS,
/* 128 */     READ_INITIAL,
/* 129 */     READ_HEADER,
/* 130 */     READ_VARIABLE_LENGTH_CONTENT,
/* 131 */     READ_FIXED_LENGTH_CONTENT,
/* 132 */     READ_CHUNK_SIZE,
/* 133 */     READ_CHUNKED_CONTENT,
/* 134 */     READ_CHUNK_DELIMITER,
/* 135 */     READ_CHUNK_FOOTER,
/* 136 */     BAD_MESSAGE,
/* 137 */     UPGRADED;
/*     */   }
/*     */   
/* 140 */   private State currentState = State.SKIP_CONTROL_CHARS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpObjectDecoder() {
/* 148 */     this(4096, 8192, 8192, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported) {
/* 156 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders) {
/* 165 */     this(maxInitialLineLength, maxHeaderSize, maxChunkSize, chunkedSupported, validateHeaders, 128);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize) {
/* 171 */     if (maxInitialLineLength <= 0) {
/* 172 */       throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + maxInitialLineLength);
/*     */     }
/*     */ 
/*     */     
/* 176 */     if (maxHeaderSize <= 0) {
/* 177 */       throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + maxHeaderSize);
/*     */     }
/*     */ 
/*     */     
/* 181 */     if (maxChunkSize <= 0) {
/* 182 */       throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
/*     */     }
/*     */ 
/*     */     
/* 186 */     AppendableCharSequence seq = new AppendableCharSequence(initialBufferSize);
/* 187 */     this.lineParser = new LineParser(seq, maxInitialLineLength);
/* 188 */     this.headerParser = new HeaderParser(seq, maxHeaderSize);
/* 189 */     this.maxChunkSize = maxChunkSize;
/* 190 */     this.chunkedSupported = chunkedSupported;
/* 191 */     this.validateHeaders = validateHeaders; } protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception { int i; int readLimit; int toRead; int wIdx; int readableBytes;
/*     */     int j;
/*     */     HttpContent chunk;
/*     */     int rIdx;
/*     */     ByteBuf content;
/* 196 */     if (this.resetRequested) {
/* 197 */       resetNow();
/*     */     }
/*     */     
/* 200 */     switch (this.currentState) {
/*     */       case SKIP_CONTROL_CHARS:
/* 202 */         if (!skipControlCharacters(buffer)) {
/*     */           return;
/*     */         }
/* 205 */         this.currentState = State.READ_INITIAL;
/*     */       case READ_INITIAL:
/*     */         try {
/* 208 */           AppendableCharSequence line = this.lineParser.parse(buffer);
/* 209 */           if (line == null) {
/*     */             return;
/*     */           }
/* 212 */           String[] initialLine = splitInitialLine(line);
/* 213 */           if (initialLine.length < 3) {
/*     */             
/* 215 */             this.currentState = State.SKIP_CONTROL_CHARS;
/*     */             
/*     */             return;
/*     */           } 
/* 219 */           this.message = createMessage(initialLine);
/* 220 */           this.currentState = State.READ_HEADER;
/*     */         }
/* 222 */         catch (Exception e) {
/* 223 */           out.add(invalidMessage(buffer, e)); return;
/*     */         } 
/*     */       case READ_HEADER:
/*     */         try {
/* 227 */           State nextState = readHeaders(buffer);
/* 228 */           if (nextState == null) {
/*     */             return;
/*     */           }
/* 231 */           this.currentState = nextState;
/* 232 */           switch (nextState) {
/*     */ 
/*     */             
/*     */             case SKIP_CONTROL_CHARS:
/* 236 */               out.add(this.message);
/* 237 */               out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/* 238 */               resetNow();
/*     */               return;
/*     */             case READ_CHUNK_SIZE:
/* 241 */               if (!this.chunkedSupported) {
/* 242 */                 throw new IllegalArgumentException("Chunked messages not supported");
/*     */               }
/*     */               
/* 245 */               out.add(this.message);
/*     */               return;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 254 */           long contentLength = contentLength();
/* 255 */           if (contentLength == 0L || (contentLength == -1L && isDecodingRequest())) {
/* 256 */             out.add(this.message);
/* 257 */             out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/* 258 */             resetNow();
/*     */             
/*     */             return;
/*     */           } 
/* 262 */           assert nextState == State.READ_FIXED_LENGTH_CONTENT || nextState == State.READ_VARIABLE_LENGTH_CONTENT;
/*     */ 
/*     */           
/* 265 */           out.add(this.message);
/*     */           
/* 267 */           if (nextState == State.READ_FIXED_LENGTH_CONTENT)
/*     */           {
/* 269 */             this.chunkSize = contentLength;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/* 275 */         } catch (Exception e) {
/* 276 */           out.add(invalidMessage(buffer, e));
/*     */           return;
/*     */         } 
/*     */       
/*     */       case READ_VARIABLE_LENGTH_CONTENT:
/* 281 */         i = Math.min(buffer.readableBytes(), this.maxChunkSize);
/* 282 */         if (i > 0) {
/* 283 */           ByteBuf byteBuf = buffer.readRetainedSlice(i);
/* 284 */           out.add(new DefaultHttpContent(byteBuf));
/*     */         } 
/*     */         return;
/*     */       
/*     */       case READ_FIXED_LENGTH_CONTENT:
/* 289 */         readLimit = buffer.readableBytes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 297 */         if (readLimit == 0) {
/*     */           return;
/*     */         }
/*     */         
/* 301 */         j = Math.min(readLimit, this.maxChunkSize);
/* 302 */         if (j > this.chunkSize) {
/* 303 */           j = (int)this.chunkSize;
/*     */         }
/* 305 */         content = buffer.readRetainedSlice(j);
/* 306 */         this.chunkSize -= j;
/*     */         
/* 308 */         if (this.chunkSize == 0L) {
/*     */           
/* 310 */           out.add(new DefaultLastHttpContent(content, this.validateHeaders));
/* 311 */           resetNow();
/*     */         } else {
/* 313 */           out.add(new DefaultHttpContent(content));
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case READ_CHUNK_SIZE:
/*     */         try {
/* 322 */           AppendableCharSequence line = this.lineParser.parse(buffer);
/* 323 */           if (line == null) {
/*     */             return;
/*     */           }
/* 326 */           int chunkSize = getChunkSize(line.toString());
/* 327 */           this.chunkSize = chunkSize;
/* 328 */           if (chunkSize == 0) {
/* 329 */             this.currentState = State.READ_CHUNK_FOOTER;
/*     */             return;
/*     */           } 
/* 332 */           this.currentState = State.READ_CHUNKED_CONTENT;
/*     */         }
/* 334 */         catch (Exception e) {
/* 335 */           out.add(invalidChunk(buffer, e));
/*     */           return;
/*     */         } 
/*     */       case READ_CHUNKED_CONTENT:
/* 339 */         assert this.chunkSize <= 2147483647L;
/* 340 */         toRead = Math.min((int)this.chunkSize, this.maxChunkSize);
/* 341 */         toRead = Math.min(toRead, buffer.readableBytes());
/* 342 */         if (toRead == 0) {
/*     */           return;
/*     */         }
/* 345 */         chunk = new DefaultHttpContent(buffer.readRetainedSlice(toRead));
/* 346 */         this.chunkSize -= toRead;
/*     */         
/* 348 */         out.add(chunk);
/*     */         
/* 350 */         if (this.chunkSize != 0L) {
/*     */           return;
/*     */         }
/* 353 */         this.currentState = State.READ_CHUNK_DELIMITER;
/*     */ 
/*     */       
/*     */       case READ_CHUNK_DELIMITER:
/* 357 */         wIdx = buffer.writerIndex();
/* 358 */         rIdx = buffer.readerIndex();
/* 359 */         while (wIdx > rIdx) {
/* 360 */           byte next = buffer.getByte(rIdx++);
/* 361 */           if (next == 10) {
/* 362 */             this.currentState = State.READ_CHUNK_SIZE;
/*     */             break;
/*     */           } 
/*     */         } 
/* 366 */         buffer.readerIndex(rIdx);
/*     */         return;
/*     */       case READ_CHUNK_FOOTER:
/*     */         try {
/* 370 */           LastHttpContent trailer = readTrailingHeaders(buffer);
/* 371 */           if (trailer == null) {
/*     */             return;
/*     */           }
/* 374 */           out.add(trailer);
/* 375 */           resetNow();
/*     */           return;
/* 377 */         } catch (Exception e) {
/* 378 */           out.add(invalidChunk(buffer, e));
/*     */           return;
/*     */         } 
/*     */       
/*     */       case BAD_MESSAGE:
/* 383 */         buffer.skipBytes(buffer.readableBytes());
/*     */         break;
/*     */       
/*     */       case UPGRADED:
/* 387 */         readableBytes = buffer.readableBytes();
/* 388 */         if (readableBytes > 0)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 393 */           out.add(buffer.readBytes(readableBytes));
/*     */         }
/*     */         break;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/* 402 */     super.decodeLast(ctx, in, out);
/*     */     
/* 404 */     if (this.resetRequested)
/*     */     {
/*     */       
/* 407 */       resetNow();
/*     */     }
/*     */     
/* 410 */     if (this.message != null) {
/* 411 */       boolean prematureClosure, chunked = HttpUtil.isTransferEncodingChunked(this.message);
/* 412 */       if (this.currentState == State.READ_VARIABLE_LENGTH_CONTENT && !in.isReadable() && !chunked) {
/*     */         
/* 414 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/* 415 */         resetNow();
/*     */         
/*     */         return;
/*     */       } 
/* 419 */       if (this.currentState == State.READ_HEADER) {
/*     */ 
/*     */         
/* 422 */         out.add(invalidMessage(Unpooled.EMPTY_BUFFER, (Exception)new PrematureChannelClosureException("Connection closed before received headers")));
/*     */         
/* 424 */         resetNow();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 430 */       if (isDecodingRequest() || chunked) {
/*     */         
/* 432 */         prematureClosure = true;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 437 */         prematureClosure = (contentLength() > 0L);
/*     */       } 
/*     */       
/* 440 */       if (!prematureClosure) {
/* 441 */         out.add(LastHttpContent.EMPTY_LAST_CONTENT);
/*     */       }
/* 443 */       resetNow();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 449 */     if (evt instanceof HttpExpectationFailedEvent) {
/* 450 */       switch (this.currentState) {
/*     */         case READ_CHUNK_SIZE:
/*     */         case READ_VARIABLE_LENGTH_CONTENT:
/*     */         case READ_FIXED_LENGTH_CONTENT:
/* 454 */           reset();
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     }
/* 460 */     super.userEventTriggered(ctx, evt);
/*     */   }
/*     */   
/*     */   protected boolean isContentAlwaysEmpty(HttpMessage msg) {
/* 464 */     if (msg instanceof HttpResponse) {
/* 465 */       HttpResponse res = (HttpResponse)msg;
/* 466 */       int code = res.status().code();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 473 */       if (code >= 100 && code < 200)
/*     */       {
/* 475 */         return (code != 101 || res.headers().contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_ACCEPT) || 
/* 476 */           !res.headers().contains((CharSequence)HttpHeaderNames.UPGRADE, (CharSequence)HttpHeaderValues.WEBSOCKET, true));
/*     */       }
/*     */       
/* 479 */       switch (code) { case 204:
/*     */         case 304:
/* 481 */           return true; }
/*     */     
/*     */     } 
/* 484 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSwitchingToNonHttp1Protocol(HttpResponse msg) {
/* 492 */     if (msg.status().code() != HttpResponseStatus.SWITCHING_PROTOCOLS.code()) {
/* 493 */       return false;
/*     */     }
/* 495 */     String newProtocol = msg.headers().get((CharSequence)HttpHeaderNames.UPGRADE);
/* 496 */     return (newProtocol == null || (
/* 497 */       !newProtocol.contains(HttpVersion.HTTP_1_0.text()) && 
/* 498 */       !newProtocol.contains(HttpVersion.HTTP_1_1.text())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 506 */     this.resetRequested = true;
/*     */   }
/*     */   
/*     */   private void resetNow() {
/* 510 */     HttpMessage message = this.message;
/* 511 */     this.message = null;
/* 512 */     this.name = null;
/* 513 */     this.value = null;
/* 514 */     this.contentLength = Long.MIN_VALUE;
/* 515 */     this.lineParser.reset();
/* 516 */     this.headerParser.reset();
/* 517 */     this.trailer = null;
/* 518 */     if (!isDecodingRequest()) {
/* 519 */       HttpResponse res = (HttpResponse)message;
/* 520 */       if (res != null && isSwitchingToNonHttp1Protocol(res)) {
/* 521 */         this.currentState = State.UPGRADED;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 526 */     this.resetRequested = false;
/* 527 */     this.currentState = State.SKIP_CONTROL_CHARS;
/*     */   }
/*     */   
/*     */   private HttpMessage invalidMessage(ByteBuf in, Exception cause) {
/* 531 */     this.currentState = State.BAD_MESSAGE;
/*     */ 
/*     */ 
/*     */     
/* 535 */     in.skipBytes(in.readableBytes());
/*     */     
/* 537 */     if (this.message == null) {
/* 538 */       this.message = createInvalidMessage();
/*     */     }
/* 540 */     this.message.setDecoderResult(DecoderResult.failure(cause));
/*     */     
/* 542 */     HttpMessage ret = this.message;
/* 543 */     this.message = null;
/* 544 */     return ret;
/*     */   }
/*     */   
/*     */   private HttpContent invalidChunk(ByteBuf in, Exception cause) {
/* 548 */     this.currentState = State.BAD_MESSAGE;
/*     */ 
/*     */ 
/*     */     
/* 552 */     in.skipBytes(in.readableBytes());
/*     */     
/* 554 */     HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
/* 555 */     chunk.setDecoderResult(DecoderResult.failure(cause));
/* 556 */     this.message = null;
/* 557 */     this.trailer = null;
/* 558 */     return chunk;
/*     */   }
/*     */   
/*     */   private static boolean skipControlCharacters(ByteBuf buffer) {
/* 562 */     boolean skiped = false;
/* 563 */     int wIdx = buffer.writerIndex();
/* 564 */     int rIdx = buffer.readerIndex();
/* 565 */     while (wIdx > rIdx) {
/* 566 */       int c = buffer.getUnsignedByte(rIdx++);
/* 567 */       if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
/* 568 */         rIdx--;
/* 569 */         skiped = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 573 */     buffer.readerIndex(rIdx);
/* 574 */     return skiped;
/*     */   }
/*     */   private State readHeaders(ByteBuf buffer) {
/*     */     State nextState;
/* 578 */     HttpMessage message = this.message;
/* 579 */     HttpHeaders headers = message.headers();
/*     */     
/* 581 */     AppendableCharSequence line = this.headerParser.parse(buffer);
/* 582 */     if (line == null) {
/* 583 */       return null;
/*     */     }
/* 585 */     if (line.length() > 0) {
/*     */       do {
/* 587 */         char firstChar = line.charAt(0);
/* 588 */         if (this.name != null && (firstChar == ' ' || firstChar == '\t')) {
/*     */ 
/*     */           
/* 591 */           String trimmedLine = line.toString().trim();
/* 592 */           String valueStr = String.valueOf(this.value);
/* 593 */           this.value = valueStr + ' ' + trimmedLine;
/*     */         } else {
/* 595 */           if (this.name != null) {
/* 596 */             headers.add(this.name, this.value);
/*     */           }
/* 598 */           splitHeader(line);
/*     */         } 
/*     */         
/* 601 */         line = this.headerParser.parse(buffer);
/* 602 */         if (line == null) {
/* 603 */           return null;
/*     */         }
/* 605 */       } while (line.length() > 0);
/*     */     }
/*     */ 
/*     */     
/* 609 */     if (this.name != null) {
/* 610 */       headers.add(this.name, this.value);
/*     */     }
/*     */     
/* 613 */     this.name = null;
/* 614 */     this.value = null;
/*     */ 
/*     */ 
/*     */     
/* 618 */     if (isContentAlwaysEmpty(message)) {
/* 619 */       HttpUtil.setTransferEncodingChunked(message, false);
/* 620 */       nextState = State.SKIP_CONTROL_CHARS;
/* 621 */     } else if (HttpUtil.isTransferEncodingChunked(message)) {
/* 622 */       nextState = State.READ_CHUNK_SIZE;
/* 623 */     } else if (contentLength() >= 0L) {
/* 624 */       nextState = State.READ_FIXED_LENGTH_CONTENT;
/*     */     } else {
/* 626 */       nextState = State.READ_VARIABLE_LENGTH_CONTENT;
/*     */     } 
/* 628 */     return nextState;
/*     */   }
/*     */   
/*     */   private long contentLength() {
/* 632 */     if (this.contentLength == Long.MIN_VALUE) {
/* 633 */       this.contentLength = HttpUtil.getContentLength(this.message, -1L);
/*     */     }
/* 635 */     return this.contentLength;
/*     */   }
/*     */   
/*     */   private LastHttpContent readTrailingHeaders(ByteBuf buffer) {
/* 639 */     AppendableCharSequence line = this.headerParser.parse(buffer);
/* 640 */     if (line == null) {
/* 641 */       return null;
/*     */     }
/* 643 */     CharSequence lastHeader = null;
/* 644 */     if (line.length() > 0) {
/* 645 */       LastHttpContent trailer = this.trailer;
/* 646 */       if (trailer == null) {
/* 647 */         trailer = this.trailer = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
/*     */       }
/*     */       do {
/* 650 */         char firstChar = line.charAt(0);
/* 651 */         if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
/* 652 */           List<String> current = trailer.trailingHeaders().getAll(lastHeader);
/* 653 */           if (!current.isEmpty()) {
/* 654 */             int lastPos = current.size() - 1;
/*     */ 
/*     */             
/* 657 */             String lineTrimmed = line.toString().trim();
/* 658 */             String currentLastPos = current.get(lastPos);
/* 659 */             current.set(lastPos, currentLastPos + lineTrimmed);
/*     */           } 
/*     */         } else {
/* 662 */           splitHeader(line);
/* 663 */           CharSequence headerName = this.name;
/* 664 */           if (!HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(headerName) && 
/* 665 */             !HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(headerName) && 
/* 666 */             !HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(headerName)) {
/* 667 */             trailer.trailingHeaders().add(headerName, this.value);
/*     */           }
/* 669 */           lastHeader = this.name;
/*     */           
/* 671 */           this.name = null;
/* 672 */           this.value = null;
/*     */         } 
/*     */         
/* 675 */         line = this.headerParser.parse(buffer);
/* 676 */         if (line == null) {
/* 677 */           return null;
/*     */         }
/* 679 */       } while (line.length() > 0);
/*     */       
/* 681 */       this.trailer = null;
/* 682 */       return trailer;
/*     */     } 
/*     */     
/* 685 */     return LastHttpContent.EMPTY_LAST_CONTENT;
/*     */   }
/*     */   protected abstract boolean isDecodingRequest();
/*     */   protected abstract HttpMessage createMessage(String[] paramArrayOfString) throws Exception;
/*     */   
/*     */   protected abstract HttpMessage createInvalidMessage();
/*     */   
/*     */   private static int getChunkSize(String hex) {
/* 693 */     hex = hex.trim();
/* 694 */     for (int i = 0; i < hex.length(); i++) {
/* 695 */       char c = hex.charAt(i);
/* 696 */       if (c == ';' || Character.isWhitespace(c) || Character.isISOControl(c)) {
/* 697 */         hex = hex.substring(0, i);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 702 */     return Integer.parseInt(hex, 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] splitInitialLine(AppendableCharSequence sb) {
/* 713 */     int aStart = findNonWhitespace(sb, 0);
/* 714 */     int aEnd = findWhitespace(sb, aStart);
/*     */     
/* 716 */     int bStart = findNonWhitespace(sb, aEnd);
/* 717 */     int bEnd = findWhitespace(sb, bStart);
/*     */     
/* 719 */     int cStart = findNonWhitespace(sb, bEnd);
/* 720 */     int cEnd = findEndOfString(sb);
/*     */     
/* 722 */     return new String[] { sb
/* 723 */         .subStringUnsafe(aStart, aEnd), sb
/* 724 */         .subStringUnsafe(bStart, bEnd), (cStart < cEnd) ? sb
/* 725 */         .subStringUnsafe(cStart, cEnd) : "" };
/*     */   }
/*     */   
/*     */   private void splitHeader(AppendableCharSequence sb) {
/* 729 */     int length = sb.length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 736 */     int nameStart = findNonWhitespace(sb, 0); int nameEnd;
/* 737 */     for (nameEnd = nameStart; nameEnd < length; nameEnd++) {
/* 738 */       char ch = sb.charAt(nameEnd);
/* 739 */       if (ch == ':' || Character.isWhitespace(ch)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     int colonEnd;
/* 744 */     for (colonEnd = nameEnd; colonEnd < length; colonEnd++) {
/* 745 */       if (sb.charAt(colonEnd) == ':') {
/* 746 */         colonEnd++;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 751 */     this.name = sb.subStringUnsafe(nameStart, nameEnd);
/* 752 */     int valueStart = findNonWhitespace(sb, colonEnd);
/* 753 */     if (valueStart == length) {
/* 754 */       this.value = "";
/*     */     } else {
/* 756 */       int valueEnd = findEndOfString(sb);
/* 757 */       this.value = sb.subStringUnsafe(valueStart, valueEnd);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int findNonWhitespace(AppendableCharSequence sb, int offset) {
/* 762 */     for (int result = offset; result < sb.length(); result++) {
/* 763 */       if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
/* 764 */         return result;
/*     */       }
/*     */     } 
/* 767 */     return sb.length();
/*     */   }
/*     */   
/*     */   private static int findWhitespace(AppendableCharSequence sb, int offset) {
/* 771 */     for (int result = offset; result < sb.length(); result++) {
/* 772 */       if (Character.isWhitespace(sb.charAtUnsafe(result))) {
/* 773 */         return result;
/*     */       }
/*     */     } 
/* 776 */     return sb.length();
/*     */   }
/*     */   
/*     */   private static int findEndOfString(AppendableCharSequence sb) {
/* 780 */     for (int result = sb.length() - 1; result > 0; result--) {
/* 781 */       if (!Character.isWhitespace(sb.charAtUnsafe(result))) {
/* 782 */         return result + 1;
/*     */       }
/*     */     } 
/* 785 */     return 0;
/*     */   }
/*     */   
/*     */   private static class HeaderParser implements ByteProcessor {
/*     */     private final AppendableCharSequence seq;
/*     */     private final int maxLength;
/*     */     private int size;
/*     */     
/*     */     HeaderParser(AppendableCharSequence seq, int maxLength) {
/* 794 */       this.seq = seq;
/* 795 */       this.maxLength = maxLength;
/*     */     }
/*     */     
/*     */     public AppendableCharSequence parse(ByteBuf buffer) {
/* 799 */       int oldSize = this.size;
/* 800 */       this.seq.reset();
/* 801 */       int i = buffer.forEachByte(this);
/* 802 */       if (i == -1) {
/* 803 */         this.size = oldSize;
/* 804 */         return null;
/*     */       } 
/* 806 */       buffer.readerIndex(i + 1);
/* 807 */       return this.seq;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 811 */       this.size = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean process(byte value) throws Exception {
/* 816 */       char nextByte = (char)(value & 0xFF);
/* 817 */       if (nextByte == '\r') {
/* 818 */         return true;
/*     */       }
/* 820 */       if (nextByte == '\n') {
/* 821 */         return false;
/*     */       }
/*     */       
/* 824 */       if (++this.size > this.maxLength)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 829 */         throw newException(this.maxLength);
/*     */       }
/*     */       
/* 832 */       this.seq.append(nextByte);
/* 833 */       return true;
/*     */     }
/*     */     
/*     */     protected TooLongFrameException newException(int maxLength) {
/* 837 */       return new TooLongFrameException("HTTP header is larger than " + maxLength + " bytes.");
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LineParser
/*     */     extends HeaderParser {
/*     */     LineParser(AppendableCharSequence seq, int maxLength) {
/* 844 */       super(seq, maxLength);
/*     */     }
/*     */ 
/*     */     
/*     */     public AppendableCharSequence parse(ByteBuf buffer) {
/* 849 */       reset();
/* 850 */       return super.parse(buffer);
/*     */     }
/*     */ 
/*     */     
/*     */     protected TooLongFrameException newException(int maxLength) {
/* 855 */       return new TooLongFrameException("An HTTP line is larger than " + maxLength + " bytes.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */