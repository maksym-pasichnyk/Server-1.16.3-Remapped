/*     */ package io.netty.handler.codec.stomp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.internal.AppendableCharSequence;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StompSubframeDecoder
/*     */   extends ReplayingDecoder<StompSubframeDecoder.State>
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 8132;
/*     */   private static final int DEFAULT_MAX_LINE_LENGTH = 1024;
/*     */   private final int maxLineLength;
/*     */   private final int maxChunkSize;
/*     */   private final boolean validateHeaders;
/*     */   private int alreadyReadChunkSize;
/*     */   private LastStompContentSubframe lastContent;
/*     */   
/*     */   enum State
/*     */   {
/*  64 */     SKIP_CONTROL_CHARACTERS,
/*  65 */     READ_HEADERS,
/*  66 */     READ_CONTENT,
/*  67 */     FINALIZE_FRAME_READ,
/*  68 */     BAD_FRAME,
/*  69 */     INVALID_CHUNK;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private long contentLength = -1L;
/*     */   
/*     */   public StompSubframeDecoder() {
/*  80 */     this(1024, 8132);
/*     */   }
/*     */   
/*     */   public StompSubframeDecoder(boolean validateHeaders) {
/*  84 */     this(1024, 8132, validateHeaders);
/*     */   }
/*     */   
/*     */   public StompSubframeDecoder(int maxLineLength, int maxChunkSize) {
/*  88 */     this(maxLineLength, maxChunkSize, false);
/*     */   }
/*     */   
/*     */   public StompSubframeDecoder(int maxLineLength, int maxChunkSize, boolean validateHeaders) {
/*  92 */     super(State.SKIP_CONTROL_CHARACTERS);
/*  93 */     if (maxLineLength <= 0) {
/*  94 */       throw new IllegalArgumentException("maxLineLength must be a positive integer: " + maxLineLength);
/*     */     }
/*     */ 
/*     */     
/*  98 */     if (maxChunkSize <= 0) {
/*  99 */       throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + maxChunkSize);
/*     */     }
/*     */ 
/*     */     
/* 103 */     this.maxChunkSize = maxChunkSize;
/* 104 */     this.maxLineLength = maxLineLength;
/* 105 */     this.validateHeaders = validateHeaders;
/*     */   }
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */     StompCommand command;
/*     */     StompHeadersSubframe frame;
/* 110 */     switch ((State)state()) {
/*     */       case SKIP_CONTROL_CHARACTERS:
/* 112 */         skipControlCharacters(in);
/* 113 */         checkpoint(State.READ_HEADERS);
/*     */       
/*     */       case READ_HEADERS:
/* 116 */         command = StompCommand.UNKNOWN;
/* 117 */         frame = null;
/*     */         try {
/* 119 */           command = readCommand(in);
/* 120 */           frame = new DefaultStompHeadersSubframe(command);
/* 121 */           checkpoint(readHeaders(in, frame.headers()));
/* 122 */           out.add(frame);
/* 123 */         } catch (Exception e) {
/* 124 */           if (frame == null) {
/* 125 */             frame = new DefaultStompHeadersSubframe(command);
/*     */           }
/* 127 */           frame.setDecoderResult(DecoderResult.failure(e));
/* 128 */           out.add(frame);
/* 129 */           checkpoint(State.BAD_FRAME);
/*     */           return;
/*     */         } 
/*     */         break;
/*     */       case BAD_FRAME:
/* 134 */         in.skipBytes(actualReadableBytes()); return;
/*     */     } 
/*     */     try {
/*     */       int toRead;
/* 138 */       switch ((State)state()) {
/*     */         case READ_CONTENT:
/* 140 */           toRead = in.readableBytes();
/* 141 */           if (toRead == 0) {
/*     */             return;
/*     */           }
/* 144 */           if (toRead > this.maxChunkSize) {
/* 145 */             toRead = this.maxChunkSize;
/*     */           }
/* 147 */           if (this.contentLength >= 0L) {
/* 148 */             int remainingLength = (int)(this.contentLength - this.alreadyReadChunkSize);
/* 149 */             if (toRead > remainingLength) {
/* 150 */               toRead = remainingLength;
/*     */             }
/* 152 */             ByteBuf chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
/* 153 */             if ((this.alreadyReadChunkSize += toRead) >= this.contentLength) {
/* 154 */               this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
/* 155 */               checkpoint(State.FINALIZE_FRAME_READ);
/*     */             } else {
/* 157 */               out.add(new DefaultStompContentSubframe(chunkBuffer));
/*     */               return;
/*     */             } 
/*     */           } else {
/* 161 */             int nulIndex = ByteBufUtil.indexOf(in, in.readerIndex(), in.writerIndex(), (byte)0);
/* 162 */             if (nulIndex == in.readerIndex()) {
/* 163 */               checkpoint(State.FINALIZE_FRAME_READ);
/*     */             } else {
/* 165 */               if (nulIndex > 0) {
/* 166 */                 toRead = nulIndex - in.readerIndex();
/*     */               } else {
/* 168 */                 toRead = in.writerIndex() - in.readerIndex();
/*     */               } 
/* 170 */               ByteBuf chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
/* 171 */               this.alreadyReadChunkSize += toRead;
/* 172 */               if (nulIndex > 0) {
/* 173 */                 this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
/* 174 */                 checkpoint(State.FINALIZE_FRAME_READ);
/*     */               } else {
/* 176 */                 out.add(new DefaultStompContentSubframe(chunkBuffer));
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         
/*     */         case FINALIZE_FRAME_READ:
/* 183 */           skipNullCharacter(in);
/* 184 */           if (this.lastContent == null) {
/* 185 */             this.lastContent = LastStompContentSubframe.EMPTY_LAST_CONTENT;
/*     */           }
/* 187 */           out.add(this.lastContent);
/* 188 */           resetDecoder(); break;
/*     */       } 
/* 190 */     } catch (Exception e) {
/* 191 */       StompContentSubframe errorContent = new DefaultLastStompContentSubframe(Unpooled.EMPTY_BUFFER);
/* 192 */       errorContent.setDecoderResult(DecoderResult.failure(e));
/* 193 */       out.add(errorContent);
/* 194 */       checkpoint(State.BAD_FRAME);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StompCommand readCommand(ByteBuf in) {
/* 199 */     String commandStr = readLine(in, 16);
/* 200 */     StompCommand command = null;
/*     */     try {
/* 202 */       command = StompCommand.valueOf(commandStr);
/* 203 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */     
/* 206 */     if (command == null) {
/* 207 */       commandStr = commandStr.toUpperCase(Locale.US);
/*     */       try {
/* 209 */         command = StompCommand.valueOf(commandStr);
/* 210 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     } 
/*     */ 
/*     */     
/* 214 */     if (command == null) {
/* 215 */       throw new DecoderException("failed to read command from channel");
/*     */     }
/* 217 */     return command;
/*     */   }
/*     */   
/*     */   private State readHeaders(ByteBuf buffer, StompHeaders headers) {
/* 221 */     AppendableCharSequence buf = new AppendableCharSequence(128);
/*     */     while (true) {
/* 223 */       boolean headerRead = readHeader(headers, buf, buffer);
/* 224 */       if (!headerRead) {
/* 225 */         if (headers.contains(StompHeaders.CONTENT_LENGTH)) {
/* 226 */           this.contentLength = getContentLength(headers, 0L);
/* 227 */           if (this.contentLength == 0L) {
/* 228 */             return State.FINALIZE_FRAME_READ;
/*     */           }
/*     */         } 
/* 231 */         return State.READ_CONTENT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static long getContentLength(StompHeaders headers, long defaultValue) {
/* 237 */     long contentLength = headers.getLong(StompHeaders.CONTENT_LENGTH, defaultValue);
/* 238 */     if (contentLength < 0L) {
/* 239 */       throw new DecoderException(StompHeaders.CONTENT_LENGTH + " must be non-negative");
/*     */     }
/* 241 */     return contentLength;
/*     */   }
/*     */   
/*     */   private static void skipNullCharacter(ByteBuf buffer) {
/* 245 */     byte b = buffer.readByte();
/* 246 */     if (b != 0) {
/* 247 */       throw new IllegalStateException("unexpected byte in buffer " + b + " while expecting NULL byte");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void skipControlCharacters(ByteBuf buffer) {
/*     */     while (true) {
/* 254 */       byte b = buffer.readByte();
/* 255 */       if (b != 13 && b != 10) {
/* 256 */         buffer.readerIndex(buffer.readerIndex() - 1);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String readLine(ByteBuf buffer, int initialBufferSize) {
/* 263 */     AppendableCharSequence buf = new AppendableCharSequence(initialBufferSize);
/* 264 */     int lineLength = 0;
/*     */     while (true) {
/* 266 */       byte nextByte = buffer.readByte();
/* 267 */       if (nextByte == 13)
/*     */         continue; 
/* 269 */       if (nextByte == 10) {
/* 270 */         return buf.toString();
/*     */       }
/* 272 */       if (lineLength >= this.maxLineLength) {
/* 273 */         invalidLineLength();
/*     */       }
/* 275 */       lineLength++;
/* 276 */       buf.append((char)nextByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean readHeader(StompHeaders headers, AppendableCharSequence buf, ByteBuf buffer) {
/* 282 */     buf.reset();
/* 283 */     int lineLength = 0;
/* 284 */     String key = null;
/* 285 */     boolean valid = false;
/*     */     while (true) {
/* 287 */       byte nextByte = buffer.readByte();
/*     */       
/* 289 */       if (nextByte == 58 && key == null) {
/* 290 */         key = buf.toString();
/* 291 */         valid = true;
/* 292 */         buf.reset(); continue;
/* 293 */       }  if (nextByte == 13)
/*     */         continue; 
/* 295 */       if (nextByte == 10) {
/* 296 */         if (key == null && lineLength == 0)
/* 297 */           return false; 
/* 298 */         if (valid) {
/* 299 */           headers.add(key, buf.toString());
/* 300 */         } else if (this.validateHeaders) {
/* 301 */           invalidHeader(key, buf.toString());
/*     */         } 
/* 303 */         return true;
/*     */       } 
/* 305 */       if (lineLength >= this.maxLineLength) {
/* 306 */         invalidLineLength();
/*     */       }
/* 308 */       if (nextByte == 58 && key != null) {
/* 309 */         valid = false;
/*     */       }
/* 311 */       lineLength++;
/* 312 */       buf.append((char)nextByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void invalidHeader(String key, String value) {
/* 318 */     String line = (key != null) ? (key + ":" + value) : value;
/* 319 */     throw new IllegalArgumentException("a header value or name contains a prohibited character ':', " + line);
/*     */   }
/*     */ 
/*     */   
/*     */   private void invalidLineLength() {
/* 324 */     throw new TooLongFrameException("An STOMP line is larger than " + this.maxLineLength + " bytes.");
/*     */   }
/*     */   
/*     */   private void resetDecoder() {
/* 328 */     checkpoint(State.SKIP_CONTROL_CHARACTERS);
/* 329 */     this.contentLength = -1L;
/* 330 */     this.alreadyReadChunkSize = 0;
/* 331 */     this.lastContent = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\StompSubframeDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */