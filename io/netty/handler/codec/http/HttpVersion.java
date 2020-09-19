/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpVersion
/*     */   implements Comparable<HttpVersion>
/*     */ {
/*  32 */   private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
/*     */ 
/*     */   
/*     */   private static final String HTTP_1_0_STRING = "HTTP/1.0";
/*     */ 
/*     */   
/*     */   private static final String HTTP_1_1_STRING = "HTTP/1.1";
/*     */   
/*  40 */   public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);
/*     */   
/*     */   private final String protocolName;
/*     */   
/*     */   private final int majorVersion;
/*     */   private final int minorVersion;
/*     */   private final String text;
/*     */   private final boolean keepAliveDefault;
/*     */   private final byte[] bytes;
/*     */   
/*     */   public static HttpVersion valueOf(String text) {
/*  56 */     if (text == null) {
/*  57 */       throw new NullPointerException("text");
/*     */     }
/*     */     
/*  60 */     text = text.trim();
/*     */     
/*  62 */     if (text.isEmpty()) {
/*  63 */       throw new IllegalArgumentException("text is empty (possibly HTTP/0.9)");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     HttpVersion version = version0(text);
/*  75 */     if (version == null) {
/*  76 */       version = new HttpVersion(text, true);
/*     */     }
/*  78 */     return version;
/*     */   }
/*     */   
/*     */   private static HttpVersion version0(String text) {
/*  82 */     if ("HTTP/1.1".equals(text)) {
/*  83 */       return HTTP_1_1;
/*     */     }
/*  85 */     if ("HTTP/1.0".equals(text)) {
/*  86 */       return HTTP_1_0;
/*     */     }
/*  88 */     return null;
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
/*     */   public HttpVersion(String text, boolean keepAliveDefault) {
/* 110 */     if (text == null) {
/* 111 */       throw new NullPointerException("text");
/*     */     }
/*     */     
/* 114 */     text = text.trim().toUpperCase();
/* 115 */     if (text.isEmpty()) {
/* 116 */       throw new IllegalArgumentException("empty text");
/*     */     }
/*     */     
/* 119 */     Matcher m = VERSION_PATTERN.matcher(text);
/* 120 */     if (!m.matches()) {
/* 121 */       throw new IllegalArgumentException("invalid version format: " + text);
/*     */     }
/*     */     
/* 124 */     this.protocolName = m.group(1);
/* 125 */     this.majorVersion = Integer.parseInt(m.group(2));
/* 126 */     this.minorVersion = Integer.parseInt(m.group(3));
/* 127 */     this.text = this.protocolName + '/' + this.majorVersion + '.' + this.minorVersion;
/* 128 */     this.keepAliveDefault = keepAliveDefault;
/* 129 */     this.bytes = null;
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
/*     */   public HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault) {
/* 146 */     this(protocolName, majorVersion, minorVersion, keepAliveDefault, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault, boolean bytes) {
/* 152 */     if (protocolName == null) {
/* 153 */       throw new NullPointerException("protocolName");
/*     */     }
/*     */     
/* 156 */     protocolName = protocolName.trim().toUpperCase();
/* 157 */     if (protocolName.isEmpty()) {
/* 158 */       throw new IllegalArgumentException("empty protocolName");
/*     */     }
/*     */     
/* 161 */     for (int i = 0; i < protocolName.length(); i++) {
/* 162 */       if (Character.isISOControl(protocolName.charAt(i)) || 
/* 163 */         Character.isWhitespace(protocolName.charAt(i))) {
/* 164 */         throw new IllegalArgumentException("invalid character in protocolName");
/*     */       }
/*     */     } 
/*     */     
/* 168 */     if (majorVersion < 0) {
/* 169 */       throw new IllegalArgumentException("negative majorVersion");
/*     */     }
/* 171 */     if (minorVersion < 0) {
/* 172 */       throw new IllegalArgumentException("negative minorVersion");
/*     */     }
/*     */     
/* 175 */     this.protocolName = protocolName;
/* 176 */     this.majorVersion = majorVersion;
/* 177 */     this.minorVersion = minorVersion;
/* 178 */     this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
/* 179 */     this.keepAliveDefault = keepAliveDefault;
/*     */     
/* 181 */     if (bytes) {
/* 182 */       this.bytes = this.text.getBytes(CharsetUtil.US_ASCII);
/*     */     } else {
/* 184 */       this.bytes = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String protocolName() {
/* 192 */     return this.protocolName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int majorVersion() {
/* 199 */     return this.majorVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int minorVersion() {
/* 206 */     return this.minorVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String text() {
/* 213 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isKeepAliveDefault() {
/* 221 */     return this.keepAliveDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 229 */     return text();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 234 */     return (protocolName().hashCode() * 31 + majorVersion()) * 31 + 
/* 235 */       minorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 240 */     if (!(o instanceof HttpVersion)) {
/* 241 */       return false;
/*     */     }
/*     */     
/* 244 */     HttpVersion that = (HttpVersion)o;
/* 245 */     return (minorVersion() == that.minorVersion() && 
/* 246 */       majorVersion() == that.majorVersion() && 
/* 247 */       protocolName().equals(that.protocolName()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(HttpVersion o) {
/* 252 */     int v = protocolName().compareTo(o.protocolName());
/* 253 */     if (v != 0) {
/* 254 */       return v;
/*     */     }
/*     */     
/* 257 */     v = majorVersion() - o.majorVersion();
/* 258 */     if (v != 0) {
/* 259 */       return v;
/*     */     }
/*     */     
/* 262 */     return minorVersion() - o.minorVersion();
/*     */   }
/*     */   
/*     */   void encode(ByteBuf buf) {
/* 266 */     if (this.bytes == null) {
/* 267 */       buf.writeCharSequence(this.text, CharsetUtil.US_ASCII);
/*     */     } else {
/* 269 */       buf.writeBytes(this.bytes);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */