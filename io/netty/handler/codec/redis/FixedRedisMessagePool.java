/*     */ package io.netty.handler.codec.redis;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.collection.LongObjectHashMap;
/*     */ import io.netty.util.collection.LongObjectMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FixedRedisMessagePool
/*     */   implements RedisMessagePool
/*     */ {
/*  34 */   private static final String[] DEFAULT_SIMPLE_STRINGS = new String[] { "OK", "PONG", "QUEUED" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final String[] DEFAULT_ERRORS = new String[] { "ERR", "ERR index out of range", "ERR no such key", "ERR source and destination objects are the same", "ERR syntax error", "BUSY Redis is busy running a script. You can only call SCRIPT KILL or SHUTDOWN NOSAVE.", "BUSYKEY Target key name already exists.", "EXECABORT Transaction discarded because of previous errors.", "LOADING Redis is loading the dataset in memory", "MASTERDOWN Link with MASTER is down and slave-serve-stale-data is set to 'no'.", "MISCONF Redis is configured to save RDB snapshots, but is currently not able to persist on disk. Commands that may modify the data set are disabled. Please check Redis logs for details about the error.", "NOAUTH Authentication required.", "NOREPLICAS Not enough good slaves to write.", "NOSCRIPT No matching script. Please use EVAL.", "OOM command not allowed when used memory > 'maxmemory'.", "READONLY You can't write against a read only slave.", "WRONGTYPE Operation against a key holding the wrong kind of value" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MIN_CACHED_INTEGER_NUMBER = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MAX_CACHED_INTEGER_NUMBER = 128L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SIZE_CACHED_INTEGER_NUMBER = 129;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final FixedRedisMessagePool INSTANCE = new FixedRedisMessagePool();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private final Map<ByteBuf, SimpleStringRedisMessage> byteBufToSimpleStrings = new HashMap<ByteBuf, SimpleStringRedisMessage>(DEFAULT_SIMPLE_STRINGS.length, 1.0F);
/*  87 */   private final Map<String, SimpleStringRedisMessage> stringToSimpleStrings = new HashMap<String, SimpleStringRedisMessage>(DEFAULT_SIMPLE_STRINGS.length, 1.0F); private final Map<ByteBuf, ErrorRedisMessage> byteBufToErrors; private final Map<String, ErrorRedisMessage> stringToErrors; private FixedRedisMessagePool() {
/*  88 */     for (String message : DEFAULT_SIMPLE_STRINGS) {
/*  89 */       ByteBuf key = Unpooled.unmodifiableBuffer(
/*  90 */           Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8))));
/*  91 */       SimpleStringRedisMessage cached = new SimpleStringRedisMessage(message);
/*  92 */       this.byteBufToSimpleStrings.put(key, cached);
/*  93 */       this.stringToSimpleStrings.put(message, cached);
/*     */     } 
/*     */     
/*  96 */     this.byteBufToErrors = new HashMap<ByteBuf, ErrorRedisMessage>(DEFAULT_ERRORS.length, 1.0F);
/*  97 */     this.stringToErrors = new HashMap<String, ErrorRedisMessage>(DEFAULT_ERRORS.length, 1.0F);
/*  98 */     for (String message : DEFAULT_ERRORS) {
/*  99 */       ByteBuf key = Unpooled.unmodifiableBuffer(
/* 100 */           Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8))));
/* 101 */       ErrorRedisMessage cached = new ErrorRedisMessage(message);
/* 102 */       this.byteBufToErrors.put(key, cached);
/* 103 */       this.stringToErrors.put(message, cached);
/*     */     } 
/*     */     
/* 106 */     this.byteBufToIntegers = new HashMap<ByteBuf, IntegerRedisMessage>(129, 1.0F);
/* 107 */     this.longToIntegers = (LongObjectMap<IntegerRedisMessage>)new LongObjectHashMap(129, 1.0F);
/* 108 */     this.longToByteBufs = (LongObjectMap<byte[]>)new LongObjectHashMap(129, 1.0F);
/* 109 */     for (long value = -1L; value < 128L; value++) {
/* 110 */       byte[] keyBytes = RedisCodecUtil.longToAsciiBytes(value);
/* 111 */       ByteBuf keyByteBuf = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(
/* 112 */             Unpooled.wrappedBuffer(keyBytes)));
/* 113 */       IntegerRedisMessage cached = new IntegerRedisMessage(value);
/* 114 */       this.byteBufToIntegers.put(keyByteBuf, cached);
/* 115 */       this.longToIntegers.put(value, cached);
/* 116 */       this.longToByteBufs.put(value, keyBytes);
/*     */     } 
/*     */   }
/*     */   private final Map<ByteBuf, IntegerRedisMessage> byteBufToIntegers; private final LongObjectMap<IntegerRedisMessage> longToIntegers; private final LongObjectMap<byte[]> longToByteBufs;
/*     */   
/*     */   public SimpleStringRedisMessage getSimpleString(String content) {
/* 122 */     return this.stringToSimpleStrings.get(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleStringRedisMessage getSimpleString(ByteBuf content) {
/* 127 */     return this.byteBufToSimpleStrings.get(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public ErrorRedisMessage getError(String content) {
/* 132 */     return this.stringToErrors.get(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public ErrorRedisMessage getError(ByteBuf content) {
/* 137 */     return this.byteBufToErrors.get(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerRedisMessage getInteger(long value) {
/* 142 */     return (IntegerRedisMessage)this.longToIntegers.get(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerRedisMessage getInteger(ByteBuf content) {
/* 147 */     return this.byteBufToIntegers.get(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getByteBufOfInteger(long value) {
/* 152 */     return (byte[])this.longToByteBufs.get(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\FixedRedisMessagePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */