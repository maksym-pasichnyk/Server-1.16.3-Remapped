/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.handler.codec.UnsupportedValueConverter;
/*     */ import io.netty.handler.codec.ValueConverter;
/*     */ import io.netty.util.AsciiString;
/*     */ import java.util.Arrays;
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
/*     */ final class HpackStaticTable
/*     */ {
/*  46 */   private static final List<HpackHeaderField> STATIC_TABLE = Arrays.asList(new HpackHeaderField[] { 
/*  47 */         newEmptyHeaderField(":authority"), 
/*  48 */         newHeaderField(":method", "GET"), 
/*  49 */         newHeaderField(":method", "POST"), 
/*  50 */         newHeaderField(":path", "/"), 
/*  51 */         newHeaderField(":path", "/index.html"), 
/*  52 */         newHeaderField(":scheme", "http"), 
/*  53 */         newHeaderField(":scheme", "https"), 
/*  54 */         newHeaderField(":status", "200"), 
/*  55 */         newHeaderField(":status", "204"), 
/*  56 */         newHeaderField(":status", "206"), 
/*  57 */         newHeaderField(":status", "304"), 
/*  58 */         newHeaderField(":status", "400"), 
/*  59 */         newHeaderField(":status", "404"), 
/*  60 */         newHeaderField(":status", "500"), 
/*  61 */         newEmptyHeaderField("accept-charset"), 
/*  62 */         newHeaderField("accept-encoding", "gzip, deflate"), 
/*  63 */         newEmptyHeaderField("accept-language"), 
/*  64 */         newEmptyHeaderField("accept-ranges"), 
/*  65 */         newEmptyHeaderField("accept"), 
/*  66 */         newEmptyHeaderField("access-control-allow-origin"), 
/*  67 */         newEmptyHeaderField("age"), 
/*  68 */         newEmptyHeaderField("allow"), 
/*  69 */         newEmptyHeaderField("authorization"), 
/*  70 */         newEmptyHeaderField("cache-control"), 
/*  71 */         newEmptyHeaderField("content-disposition"), 
/*  72 */         newEmptyHeaderField("content-encoding"), 
/*  73 */         newEmptyHeaderField("content-language"), 
/*  74 */         newEmptyHeaderField("content-length"), 
/*  75 */         newEmptyHeaderField("content-location"), 
/*  76 */         newEmptyHeaderField("content-range"), 
/*  77 */         newEmptyHeaderField("content-type"), 
/*  78 */         newEmptyHeaderField("cookie"), 
/*  79 */         newEmptyHeaderField("date"), 
/*  80 */         newEmptyHeaderField("etag"), 
/*  81 */         newEmptyHeaderField("expect"), 
/*  82 */         newEmptyHeaderField("expires"), 
/*  83 */         newEmptyHeaderField("from"), 
/*  84 */         newEmptyHeaderField("host"), 
/*  85 */         newEmptyHeaderField("if-match"), 
/*  86 */         newEmptyHeaderField("if-modified-since"), 
/*  87 */         newEmptyHeaderField("if-none-match"), 
/*  88 */         newEmptyHeaderField("if-range"), 
/*  89 */         newEmptyHeaderField("if-unmodified-since"), 
/*  90 */         newEmptyHeaderField("last-modified"), 
/*  91 */         newEmptyHeaderField("link"), 
/*  92 */         newEmptyHeaderField("location"), 
/*  93 */         newEmptyHeaderField("max-forwards"), 
/*  94 */         newEmptyHeaderField("proxy-authenticate"), 
/*  95 */         newEmptyHeaderField("proxy-authorization"), 
/*  96 */         newEmptyHeaderField("range"), 
/*  97 */         newEmptyHeaderField("referer"), 
/*  98 */         newEmptyHeaderField("refresh"), 
/*  99 */         newEmptyHeaderField("retry-after"), 
/* 100 */         newEmptyHeaderField("server"), 
/* 101 */         newEmptyHeaderField("set-cookie"), 
/* 102 */         newEmptyHeaderField("strict-transport-security"), 
/* 103 */         newEmptyHeaderField("transfer-encoding"), 
/* 104 */         newEmptyHeaderField("user-agent"), 
/* 105 */         newEmptyHeaderField("vary"), 
/* 106 */         newEmptyHeaderField("via"), 
/* 107 */         newEmptyHeaderField("www-authenticate") });
/*     */ 
/*     */   
/*     */   private static HpackHeaderField newEmptyHeaderField(String name) {
/* 111 */     return new HpackHeaderField((CharSequence)AsciiString.cached(name), (CharSequence)AsciiString.EMPTY_STRING);
/*     */   }
/*     */   
/*     */   private static HpackHeaderField newHeaderField(String name, String value) {
/* 115 */     return new HpackHeaderField((CharSequence)AsciiString.cached(name), (CharSequence)AsciiString.cached(value));
/*     */   }
/*     */   
/* 118 */   private static final CharSequenceMap<Integer> STATIC_INDEX_BY_NAME = createMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   static final int length = STATIC_TABLE.size();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HpackHeaderField getEntry(int index) {
/* 129 */     return STATIC_TABLE.get(index - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getIndex(CharSequence name) {
/* 137 */     Integer index = (Integer)STATIC_INDEX_BY_NAME.get(name);
/* 138 */     if (index == null) {
/* 139 */       return -1;
/*     */     }
/* 141 */     return index.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getIndex(CharSequence name, CharSequence value) {
/* 149 */     int index = getIndex(name);
/* 150 */     if (index == -1) {
/* 151 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 155 */     while (index <= length) {
/* 156 */       HpackHeaderField entry = getEntry(index);
/* 157 */       if (HpackUtil.equalsConstantTime(name, entry.name) == 0) {
/*     */         break;
/*     */       }
/* 160 */       if (HpackUtil.equalsConstantTime(value, entry.value) != 0) {
/* 161 */         return index;
/*     */       }
/* 163 */       index++;
/*     */     } 
/*     */     
/* 166 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static CharSequenceMap<Integer> createMap() {
/* 171 */     int length = STATIC_TABLE.size();
/*     */ 
/*     */     
/* 174 */     CharSequenceMap<Integer> ret = new CharSequenceMap<Integer>(true, (ValueConverter<Integer>)UnsupportedValueConverter.instance(), length);
/*     */ 
/*     */     
/* 177 */     for (int index = length; index > 0; index--) {
/* 178 */       HpackHeaderField entry = getEntry(index);
/* 179 */       CharSequence name = entry.name;
/* 180 */       ret.set(name, Integer.valueOf(index));
/*     */     } 
/* 182 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackStaticTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */