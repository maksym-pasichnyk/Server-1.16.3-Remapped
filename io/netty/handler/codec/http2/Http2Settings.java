/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.collection.CharObjectHashMap;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Http2Settings
/*     */   extends CharObjectHashMap<Long>
/*     */ {
/*     */   private static final int DEFAULT_CAPACITY = 13;
/*  52 */   private static final Long FALSE = Long.valueOf(0L);
/*  53 */   private static final Long TRUE = Long.valueOf(1L);
/*     */   
/*     */   public Http2Settings() {
/*  56 */     this(13);
/*     */   }
/*     */   
/*     */   public Http2Settings(int initialCapacity, float loadFactor) {
/*  60 */     super(initialCapacity, loadFactor);
/*     */   }
/*     */   
/*     */   public Http2Settings(int initialCapacity) {
/*  64 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long put(char key, Long value) {
/*  75 */     verifyStandardSetting(key, value);
/*  76 */     return (Long)super.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long headerTableSize() {
/*  83 */     return (Long)get('\001');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings headerTableSize(long value) {
/*  92 */     put('\001', Long.valueOf(value));
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean pushEnabled() {
/* 100 */     Long value = (Long)get('\002');
/* 101 */     if (value == null) {
/* 102 */       return null;
/*     */     }
/* 104 */     return Boolean.valueOf(TRUE.equals(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings pushEnabled(boolean enabled) {
/* 111 */     put('\002', enabled ? TRUE : FALSE);
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long maxConcurrentStreams() {
/* 119 */     return (Long)get('\003');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings maxConcurrentStreams(long value) {
/* 128 */     put('\003', Long.valueOf(value));
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer initialWindowSize() {
/* 136 */     return getIntValue('\004');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings initialWindowSize(int value) {
/* 145 */     put('\004', Long.valueOf(value));
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer maxFrameSize() {
/* 153 */     return getIntValue('\005');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings maxFrameSize(int value) {
/* 162 */     put('\005', Long.valueOf(value));
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long maxHeaderListSize() {
/* 170 */     return (Long)get('\006');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings maxHeaderListSize(long value) {
/* 179 */     put('\006', Long.valueOf(value));
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2Settings copyFrom(Http2Settings settings) {
/* 187 */     clear();
/* 188 */     putAll((Map)settings);
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getIntValue(char key) {
/* 198 */     Long value = (Long)get(key);
/* 199 */     if (value == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     return Integer.valueOf(value.intValue());
/*     */   }
/*     */   
/*     */   private static void verifyStandardSetting(int key, Long value) {
/* 206 */     ObjectUtil.checkNotNull(value, "value");
/* 207 */     switch (key) {
/*     */       case 1:
/* 209 */         if (value.longValue() < 0L || value.longValue() > 4294967295L) {
/* 210 */           throw new IllegalArgumentException("Setting HEADER_TABLE_SIZE is invalid: " + value);
/*     */         }
/*     */         break;
/*     */       case 2:
/* 214 */         if (value.longValue() != 0L && value.longValue() != 1L) {
/* 215 */           throw new IllegalArgumentException("Setting ENABLE_PUSH is invalid: " + value);
/*     */         }
/*     */         break;
/*     */       case 3:
/* 219 */         if (value.longValue() < 0L || value.longValue() > 4294967295L) {
/* 220 */           throw new IllegalArgumentException("Setting MAX_CONCURRENT_STREAMS is invalid: " + value);
/*     */         }
/*     */         break;
/*     */       
/*     */       case 4:
/* 225 */         if (value.longValue() < 0L || value.longValue() > 2147483647L) {
/* 226 */           throw new IllegalArgumentException("Setting INITIAL_WINDOW_SIZE is invalid: " + value);
/*     */         }
/*     */         break;
/*     */       
/*     */       case 5:
/* 231 */         if (!Http2CodecUtil.isMaxFrameSizeValid(value.intValue())) {
/* 232 */           throw new IllegalArgumentException("Setting MAX_FRAME_SIZE is invalid: " + value);
/*     */         }
/*     */         break;
/*     */       case 6:
/* 236 */         if (value.longValue() < 0L || value.longValue() > 4294967295L) {
/* 237 */           throw new IllegalArgumentException("Setting MAX_HEADER_LIST_SIZE is invalid: " + value);
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String keyToString(char key) {
/* 248 */     switch (key) {
/*     */       case '\001':
/* 250 */         return "HEADER_TABLE_SIZE";
/*     */       case '\002':
/* 252 */         return "ENABLE_PUSH";
/*     */       case '\003':
/* 254 */         return "MAX_CONCURRENT_STREAMS";
/*     */       case '\004':
/* 256 */         return "INITIAL_WINDOW_SIZE";
/*     */       case '\005':
/* 258 */         return "MAX_FRAME_SIZE";
/*     */       case '\006':
/* 260 */         return "MAX_HEADER_LIST_SIZE";
/*     */     } 
/*     */     
/* 263 */     return super.keyToString(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Http2Settings defaultSettings() {
/* 268 */     return (new Http2Settings()).maxHeaderListSize(8192L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Settings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */