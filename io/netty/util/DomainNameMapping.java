/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.IDN;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
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
/*     */ public class DomainNameMapping<V>
/*     */   implements Mapping<String, V>
/*     */ {
/*     */   final V defaultValue;
/*     */   private final Map<String, V> map;
/*     */   private final Map<String, V> unmodifiableMap;
/*     */   
/*     */   @Deprecated
/*     */   public DomainNameMapping(V defaultValue) {
/*  52 */     this(4, defaultValue);
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
/*     */   @Deprecated
/*     */   public DomainNameMapping(int initialCapacity, V defaultValue) {
/*  65 */     this(new LinkedHashMap<String, V>(initialCapacity), defaultValue);
/*     */   }
/*     */   
/*     */   DomainNameMapping(Map<String, V> map, V defaultValue) {
/*  69 */     this.defaultValue = (V)ObjectUtil.checkNotNull(defaultValue, "defaultValue");
/*  70 */     this.map = map;
/*  71 */     this.unmodifiableMap = (map != null) ? Collections.<String, V>unmodifiableMap(map) : null;
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
/*     */   @Deprecated
/*     */   public DomainNameMapping<V> add(String hostname, V output) {
/*  89 */     this.map.put(normalizeHostname((String)ObjectUtil.checkNotNull(hostname, "hostname")), (V)ObjectUtil.checkNotNull(output, "output"));
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean matches(String template, String hostName) {
/*  97 */     if (template.startsWith("*.")) {
/*  98 */       return (template.regionMatches(2, hostName, 0, hostName.length()) || 
/*  99 */         StringUtil.commonSuffixOfLength(hostName, template, template.length() - 1));
/*     */     }
/* 101 */     return template.equals(hostName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String normalizeHostname(String hostname) {
/* 108 */     if (needsNormalization(hostname)) {
/* 109 */       hostname = IDN.toASCII(hostname, 1);
/*     */     }
/* 111 */     return hostname.toLowerCase(Locale.US);
/*     */   }
/*     */   
/*     */   private static boolean needsNormalization(String hostname) {
/* 115 */     int length = hostname.length();
/* 116 */     for (int i = 0; i < length; i++) {
/* 117 */       int c = hostname.charAt(i);
/* 118 */       if (c > 127) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V map(String hostname) {
/* 127 */     if (hostname != null) {
/* 128 */       hostname = normalizeHostname(hostname);
/*     */       
/* 130 */       for (Map.Entry<String, V> entry : this.map.entrySet()) {
/* 131 */         if (matches(entry.getKey(), hostname)) {
/* 132 */           return entry.getValue();
/*     */         }
/*     */       } 
/*     */     } 
/* 136 */     return this.defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, V> asMap() {
/* 143 */     return this.unmodifiableMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 148 */     return StringUtil.simpleClassName(this) + "(default: " + this.defaultValue + ", map: " + this.map + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\DomainNameMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */