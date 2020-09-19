/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Charsets
/*     */ {
/*     */   public static SortedMap<String, Charset> requiredCharsets() {
/*  76 */     TreeMap<String, Charset> m = new TreeMap<String, Charset>(String.CASE_INSENSITIVE_ORDER);
/*  77 */     m.put(ISO_8859_1.name(), ISO_8859_1);
/*  78 */     m.put(US_ASCII.name(), US_ASCII);
/*  79 */     m.put(UTF_16.name(), UTF_16);
/*  80 */     m.put(UTF_16BE.name(), UTF_16BE);
/*  81 */     m.put(UTF_16LE.name(), UTF_16LE);
/*  82 */     m.put(UTF_8.name(), UTF_8);
/*  83 */     return Collections.unmodifiableSortedMap(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Charset toCharset(Charset charset) {
/*  94 */     return (charset == null) ? Charset.defaultCharset() : charset;
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
/*     */   public static Charset toCharset(String charset) {
/* 107 */     return (charset == null) ? Charset.defaultCharset() : Charset.forName(charset);
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
/*     */   @Deprecated
/* 120 */   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 134 */   public static final Charset US_ASCII = Charset.forName("US-ASCII");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 149 */   public static final Charset UTF_16 = Charset.forName("UTF-16");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 163 */   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 177 */   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 191 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\Charsets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */