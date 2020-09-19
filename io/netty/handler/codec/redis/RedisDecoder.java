/*     */ package io.netty.handler.codec.redis;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ public final class RedisDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*  37 */   private final ToPositiveLongProcessor toPositiveLongProcessor = new ToPositiveLongProcessor();
/*     */   
/*     */   private final boolean decodeInlineCommands;
/*     */   
/*     */   private final int maxInlineMessageLength;
/*     */   
/*     */   private final RedisMessagePool messagePool;
/*  44 */   private State state = State.DECODE_TYPE;
/*     */   private RedisMessageType type;
/*     */   private int remainingBulkLength;
/*     */   
/*     */   private enum State {
/*  49 */     DECODE_TYPE,
/*  50 */     DECODE_INLINE,
/*  51 */     DECODE_LENGTH,
/*  52 */     DECODE_BULK_STRING_EOL,
/*  53 */     DECODE_BULK_STRING_CONTENT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisDecoder() {
/*  61 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisDecoder(boolean decodeInlineCommands) {
/*  69 */     this(65536, FixedRedisMessagePool.INSTANCE, decodeInlineCommands);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisDecoder(int maxInlineMessageLength, RedisMessagePool messagePool) {
/*  78 */     this(maxInlineMessageLength, messagePool, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisDecoder(int maxInlineMessageLength, RedisMessagePool messagePool, boolean decodeInlineCommands) {
/*  88 */     if (maxInlineMessageLength <= 0 || maxInlineMessageLength > 536870912) {
/*  89 */       throw new RedisCodecException("maxInlineMessageLength: " + maxInlineMessageLength + " (expected: <= " + 536870912 + ")");
/*     */     }
/*     */     
/*  92 */     this.maxInlineMessageLength = maxInlineMessageLength;
/*  93 */     this.messagePool = messagePool;
/*  94 */     this.decodeInlineCommands = decodeInlineCommands;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*     */     try {
/*     */       while (true) {
/* 101 */         switch (this.state) {
/*     */           case ARRAY_HEADER:
/* 103 */             if (!decodeType(in)) {
/*     */               return;
/*     */             }
/*     */             continue;
/*     */           case BULK_STRING:
/* 108 */             if (!decodeInline(in, out)) {
/*     */               return;
/*     */             }
/*     */             continue;
/*     */           case INLINE_COMMAND:
/* 113 */             if (!decodeLength(in, out)) {
/*     */               return;
/*     */             }
/*     */             continue;
/*     */           case SIMPLE_STRING:
/* 118 */             if (!decodeBulkStringEndOfLine(in, out)) {
/*     */               return;
/*     */             }
/*     */             continue;
/*     */           case ERROR:
/* 123 */             if (!decodeBulkStringContent(in, out))
/*     */               return;  continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 128 */       throw new RedisCodecException("Unknown state: " + this.state);
/*     */     
/*     */     }
/* 131 */     catch (RedisCodecException e) {
/* 132 */       resetDecoder();
/* 133 */       throw e;
/* 134 */     } catch (Exception e) {
/* 135 */       resetDecoder();
/* 136 */       throw new RedisCodecException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetDecoder() {
/* 141 */     this.state = State.DECODE_TYPE;
/* 142 */     this.remainingBulkLength = 0;
/*     */   }
/*     */   
/*     */   private boolean decodeType(ByteBuf in) throws Exception {
/* 146 */     if (!in.isReadable()) {
/* 147 */       return false;
/*     */     }
/*     */     
/* 150 */     this.type = RedisMessageType.readFrom(in, this.decodeInlineCommands);
/* 151 */     this.state = this.type.isInline() ? State.DECODE_INLINE : State.DECODE_LENGTH;
/* 152 */     return true;
/*     */   }
/*     */   
/*     */   private boolean decodeInline(ByteBuf in, List<Object> out) throws Exception {
/* 156 */     ByteBuf lineBytes = readLine(in);
/* 157 */     if (lineBytes == null) {
/* 158 */       if (in.readableBytes() > this.maxInlineMessageLength) {
/* 159 */         throw new RedisCodecException("length: " + in.readableBytes() + " (expected: <= " + this.maxInlineMessageLength + ")");
/*     */       }
/*     */       
/* 162 */       return false;
/*     */     } 
/* 164 */     out.add(newInlineRedisMessage(this.type, lineBytes));
/* 165 */     resetDecoder();
/* 166 */     return true;
/*     */   }
/*     */   
/*     */   private boolean decodeLength(ByteBuf in, List<Object> out) throws Exception {
/* 170 */     ByteBuf lineByteBuf = readLine(in);
/* 171 */     if (lineByteBuf == null) {
/* 172 */       return false;
/*     */     }
/* 174 */     long length = parseRedisNumber(lineByteBuf);
/* 175 */     if (length < -1L) {
/* 176 */       throw new RedisCodecException("length: " + length + " (expected: >= " + -1 + ")");
/*     */     }
/* 178 */     switch (this.type) {
/*     */       case ARRAY_HEADER:
/* 180 */         out.add(new ArrayHeaderRedisMessage(length));
/* 181 */         resetDecoder();
/* 182 */         return true;
/*     */       case BULK_STRING:
/* 184 */         if (length > 536870912L) {
/* 185 */           throw new RedisCodecException("length: " + length + " (expected: <= " + 536870912 + ")");
/*     */         }
/*     */         
/* 188 */         this.remainingBulkLength = (int)length;
/* 189 */         return decodeBulkString(in, out);
/*     */     } 
/* 191 */     throw new RedisCodecException("bad type: " + this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean decodeBulkString(ByteBuf in, List<Object> out) throws Exception {
/* 196 */     switch (this.remainingBulkLength) {
/*     */       case -1:
/* 198 */         out.add(FullBulkStringRedisMessage.NULL_INSTANCE);
/* 199 */         resetDecoder();
/* 200 */         return true;
/*     */       case 0:
/* 202 */         this.state = State.DECODE_BULK_STRING_EOL;
/* 203 */         return decodeBulkStringEndOfLine(in, out);
/*     */     } 
/* 205 */     out.add(new BulkStringHeaderRedisMessage(this.remainingBulkLength));
/* 206 */     this.state = State.DECODE_BULK_STRING_CONTENT;
/* 207 */     return decodeBulkStringContent(in, out);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean decodeBulkStringEndOfLine(ByteBuf in, List<Object> out) throws Exception {
/* 213 */     if (in.readableBytes() < 2) {
/* 214 */       return false;
/*     */     }
/* 216 */     readEndOfLine(in);
/* 217 */     out.add(FullBulkStringRedisMessage.EMPTY_INSTANCE);
/* 218 */     resetDecoder();
/* 219 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean decodeBulkStringContent(ByteBuf in, List<Object> out) throws Exception {
/* 224 */     int readableBytes = in.readableBytes();
/* 225 */     if (readableBytes == 0 || (this.remainingBulkLength == 0 && readableBytes < 2)) {
/* 226 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 230 */     if (readableBytes >= this.remainingBulkLength + 2) {
/* 231 */       ByteBuf content = in.readSlice(this.remainingBulkLength);
/* 232 */       readEndOfLine(in);
/*     */       
/* 234 */       out.add(new DefaultLastBulkStringRedisContent(content.retain()));
/* 235 */       resetDecoder();
/* 236 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 240 */     int toRead = Math.min(this.remainingBulkLength, readableBytes);
/* 241 */     this.remainingBulkLength -= toRead;
/* 242 */     out.add(new DefaultBulkStringRedisContent(in.readSlice(toRead).retain()));
/* 243 */     return true;
/*     */   }
/*     */   
/*     */   private static void readEndOfLine(ByteBuf in) {
/* 247 */     short delim = in.readShort();
/* 248 */     if (RedisConstants.EOL_SHORT == delim) {
/*     */       return;
/*     */     }
/* 251 */     byte[] bytes = RedisCodecUtil.shortToBytes(delim);
/* 252 */     throw new RedisCodecException("delimiter: [" + bytes[0] + "," + bytes[1] + "] (expected: \\r\\n)"); } private RedisMessage newInlineRedisMessage(RedisMessageType messageType, ByteBuf content) {
/*     */     SimpleStringRedisMessage simpleStringRedisMessage;
/*     */     ErrorRedisMessage errorRedisMessage;
/*     */     IntegerRedisMessage cached;
/* 256 */     switch (messageType) {
/*     */       case INLINE_COMMAND:
/* 258 */         return new InlineCommandRedisMessage(content.toString(CharsetUtil.UTF_8));
/*     */       case SIMPLE_STRING:
/* 260 */         simpleStringRedisMessage = this.messagePool.getSimpleString(content);
/* 261 */         return (simpleStringRedisMessage != null) ? simpleStringRedisMessage : new SimpleStringRedisMessage(content.toString(CharsetUtil.UTF_8));
/*     */       
/*     */       case ERROR:
/* 264 */         errorRedisMessage = this.messagePool.getError(content);
/* 265 */         return (errorRedisMessage != null) ? errorRedisMessage : new ErrorRedisMessage(content.toString(CharsetUtil.UTF_8));
/*     */       
/*     */       case INTEGER:
/* 268 */         cached = this.messagePool.getInteger(content);
/* 269 */         return (cached != null) ? cached : new IntegerRedisMessage(parseRedisNumber(content));
/*     */     } 
/*     */     
/* 272 */     throw new RedisCodecException("bad type: " + messageType);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuf readLine(ByteBuf in) {
/* 277 */     if (!in.isReadable(2)) {
/* 278 */       return null;
/*     */     }
/* 280 */     int lfIndex = in.forEachByte(ByteProcessor.FIND_LF);
/* 281 */     if (lfIndex < 0) {
/* 282 */       return null;
/*     */     }
/* 284 */     ByteBuf data = in.readSlice(lfIndex - in.readerIndex() - 1);
/* 285 */     readEndOfLine(in);
/* 286 */     return data;
/*     */   }
/*     */   
/*     */   private long parseRedisNumber(ByteBuf byteBuf) {
/* 290 */     int readableBytes = byteBuf.readableBytes();
/* 291 */     boolean negative = (readableBytes > 0 && byteBuf.getByte(byteBuf.readerIndex()) == 45);
/* 292 */     int extraOneByteForNegative = negative ? 1 : 0;
/* 293 */     if (readableBytes <= extraOneByteForNegative) {
/* 294 */       throw new RedisCodecException("no number to parse: " + byteBuf.toString(CharsetUtil.US_ASCII));
/*     */     }
/* 296 */     if (readableBytes > 19 + extraOneByteForNegative) {
/* 297 */       throw new RedisCodecException("too many characters to be a valid RESP Integer: " + byteBuf
/* 298 */           .toString(CharsetUtil.US_ASCII));
/*     */     }
/* 300 */     if (negative) {
/* 301 */       return -parsePositiveNumber(byteBuf.skipBytes(extraOneByteForNegative));
/*     */     }
/* 303 */     return parsePositiveNumber(byteBuf);
/*     */   }
/*     */   
/*     */   private long parsePositiveNumber(ByteBuf byteBuf) {
/* 307 */     this.toPositiveLongProcessor.reset();
/* 308 */     byteBuf.forEachByte(this.toPositiveLongProcessor);
/* 309 */     return this.toPositiveLongProcessor.content();
/*     */   }
/*     */   
/*     */   private static final class ToPositiveLongProcessor implements ByteProcessor { private long result;
/*     */     
/*     */     private ToPositiveLongProcessor() {}
/*     */     
/*     */     public boolean process(byte value) throws Exception {
/* 317 */       if (value < 48 || value > 57) {
/* 318 */         throw new RedisCodecException("bad byte in number: " + value);
/*     */       }
/* 320 */       this.result = this.result * 10L + (value - 48);
/* 321 */       return true;
/*     */     }
/*     */     
/*     */     public long content() {
/* 325 */       return this.result;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 329 */       this.result = 0L;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */