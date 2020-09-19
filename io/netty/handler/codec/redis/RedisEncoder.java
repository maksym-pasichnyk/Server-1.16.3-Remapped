/*     */ package io.netty.handler.codec.redis;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.CodecException;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class RedisEncoder
/*     */   extends MessageToMessageEncoder<RedisMessage>
/*     */ {
/*     */   private final RedisMessagePool messagePool;
/*     */   
/*     */   public RedisEncoder() {
/*  42 */     this(FixedRedisMessagePool.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisEncoder(RedisMessagePool messagePool) {
/*  50 */     this.messagePool = (RedisMessagePool)ObjectUtil.checkNotNull(messagePool, "messagePool");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, RedisMessage msg, List<Object> out) throws Exception {
/*     */     try {
/*  56 */       writeRedisMessage(ctx.alloc(), msg, out);
/*  57 */     } catch (CodecException e) {
/*  58 */       throw e;
/*  59 */     } catch (Exception e) {
/*  60 */       throw new CodecException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeRedisMessage(ByteBufAllocator allocator, RedisMessage msg, List<Object> out) {
/*  65 */     if (msg instanceof InlineCommandRedisMessage) {
/*  66 */       writeInlineCommandMessage(allocator, (InlineCommandRedisMessage)msg, out);
/*  67 */     } else if (msg instanceof SimpleStringRedisMessage) {
/*  68 */       writeSimpleStringMessage(allocator, (SimpleStringRedisMessage)msg, out);
/*  69 */     } else if (msg instanceof ErrorRedisMessage) {
/*  70 */       writeErrorMessage(allocator, (ErrorRedisMessage)msg, out);
/*  71 */     } else if (msg instanceof IntegerRedisMessage) {
/*  72 */       writeIntegerMessage(allocator, (IntegerRedisMessage)msg, out);
/*  73 */     } else if (msg instanceof FullBulkStringRedisMessage) {
/*  74 */       writeFullBulkStringMessage(allocator, (FullBulkStringRedisMessage)msg, out);
/*  75 */     } else if (msg instanceof BulkStringRedisContent) {
/*  76 */       writeBulkStringContent(allocator, (BulkStringRedisContent)msg, out);
/*  77 */     } else if (msg instanceof BulkStringHeaderRedisMessage) {
/*  78 */       writeBulkStringHeader(allocator, (BulkStringHeaderRedisMessage)msg, out);
/*  79 */     } else if (msg instanceof ArrayHeaderRedisMessage) {
/*  80 */       writeArrayHeader(allocator, (ArrayHeaderRedisMessage)msg, out);
/*  81 */     } else if (msg instanceof ArrayRedisMessage) {
/*  82 */       writeArrayMessage(allocator, (ArrayRedisMessage)msg, out);
/*     */     } else {
/*  84 */       throw new CodecException("unknown message type: " + msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeInlineCommandMessage(ByteBufAllocator allocator, InlineCommandRedisMessage msg, List<Object> out) {
/*  90 */     writeString(allocator, RedisMessageType.INLINE_COMMAND, msg.content(), out);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeSimpleStringMessage(ByteBufAllocator allocator, SimpleStringRedisMessage msg, List<Object> out) {
/*  95 */     writeString(allocator, RedisMessageType.SIMPLE_STRING, msg.content(), out);
/*     */   }
/*     */   
/*     */   private static void writeErrorMessage(ByteBufAllocator allocator, ErrorRedisMessage msg, List<Object> out) {
/*  99 */     writeString(allocator, RedisMessageType.ERROR, msg.content(), out);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeString(ByteBufAllocator allocator, RedisMessageType type, String content, List<Object> out) {
/* 104 */     ByteBuf buf = allocator.ioBuffer(type.length() + ByteBufUtil.utf8MaxBytes(content) + 2);
/*     */     
/* 106 */     type.writeTo(buf);
/* 107 */     ByteBufUtil.writeUtf8(buf, content);
/* 108 */     buf.writeShort(RedisConstants.EOL_SHORT);
/* 109 */     out.add(buf);
/*     */   }
/*     */   
/*     */   private void writeIntegerMessage(ByteBufAllocator allocator, IntegerRedisMessage msg, List<Object> out) {
/* 113 */     ByteBuf buf = allocator.ioBuffer(23);
/*     */     
/* 115 */     RedisMessageType.INTEGER.writeTo(buf);
/* 116 */     buf.writeBytes(numberToBytes(msg.value()));
/* 117 */     buf.writeShort(RedisConstants.EOL_SHORT);
/* 118 */     out.add(buf);
/*     */   }
/*     */   
/*     */   private void writeBulkStringHeader(ByteBufAllocator allocator, BulkStringHeaderRedisMessage msg, List<Object> out) {
/* 122 */     ByteBuf buf = allocator.ioBuffer(1 + (
/* 123 */         msg.isNull() ? 2 : 22));
/*     */     
/* 125 */     RedisMessageType.BULK_STRING.writeTo(buf);
/* 126 */     if (msg.isNull()) {
/* 127 */       buf.writeShort(RedisConstants.NULL_SHORT);
/*     */     } else {
/* 129 */       buf.writeBytes(numberToBytes(msg.bulkStringLength()));
/* 130 */       buf.writeShort(RedisConstants.EOL_SHORT);
/*     */     } 
/* 132 */     out.add(buf);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeBulkStringContent(ByteBufAllocator allocator, BulkStringRedisContent msg, List<Object> out) {
/* 137 */     out.add(msg.content().retain());
/* 138 */     if (msg instanceof LastBulkStringRedisContent) {
/* 139 */       out.add(allocator.ioBuffer(2).writeShort(RedisConstants.EOL_SHORT));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeFullBulkStringMessage(ByteBufAllocator allocator, FullBulkStringRedisMessage msg, List<Object> out) {
/* 145 */     if (msg.isNull()) {
/* 146 */       ByteBuf buf = allocator.ioBuffer(5);
/*     */       
/* 148 */       RedisMessageType.BULK_STRING.writeTo(buf);
/* 149 */       buf.writeShort(RedisConstants.NULL_SHORT);
/* 150 */       buf.writeShort(RedisConstants.EOL_SHORT);
/* 151 */       out.add(buf);
/*     */     } else {
/* 153 */       ByteBuf headerBuf = allocator.ioBuffer(23);
/*     */       
/* 155 */       RedisMessageType.BULK_STRING.writeTo(headerBuf);
/* 156 */       headerBuf.writeBytes(numberToBytes(msg.content().readableBytes()));
/* 157 */       headerBuf.writeShort(RedisConstants.EOL_SHORT);
/* 158 */       out.add(headerBuf);
/* 159 */       out.add(msg.content().retain());
/* 160 */       out.add(allocator.ioBuffer(2).writeShort(RedisConstants.EOL_SHORT));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeArrayHeader(ByteBufAllocator allocator, ArrayHeaderRedisMessage msg, List<Object> out) {
/* 168 */     writeArrayHeader(allocator, msg.isNull(), msg.length(), out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeArrayMessage(ByteBufAllocator allocator, ArrayRedisMessage msg, List<Object> out) {
/* 175 */     if (msg.isNull()) {
/* 176 */       writeArrayHeader(allocator, msg.isNull(), -1L, out);
/*     */     } else {
/* 178 */       writeArrayHeader(allocator, msg.isNull(), msg.children().size(), out);
/* 179 */       for (RedisMessage child : msg.children()) {
/* 180 */         writeRedisMessage(allocator, child, out);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeArrayHeader(ByteBufAllocator allocator, boolean isNull, long length, List<Object> out) {
/* 186 */     if (isNull) {
/* 187 */       ByteBuf buf = allocator.ioBuffer(5);
/*     */       
/* 189 */       RedisMessageType.ARRAY_HEADER.writeTo(buf);
/* 190 */       buf.writeShort(RedisConstants.NULL_SHORT);
/* 191 */       buf.writeShort(RedisConstants.EOL_SHORT);
/* 192 */       out.add(buf);
/*     */     } else {
/* 194 */       ByteBuf buf = allocator.ioBuffer(23);
/*     */       
/* 196 */       RedisMessageType.ARRAY_HEADER.writeTo(buf);
/* 197 */       buf.writeBytes(numberToBytes(length));
/* 198 */       buf.writeShort(RedisConstants.EOL_SHORT);
/* 199 */       out.add(buf);
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte[] numberToBytes(long value) {
/* 204 */     byte[] bytes = this.messagePool.getByteBufOfInteger(value);
/* 205 */     return (bytes != null) ? bytes : RedisCodecUtil.longToAsciiBytes(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */