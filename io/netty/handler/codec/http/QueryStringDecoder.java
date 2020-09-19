/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryStringDecoder
/*     */ {
/*     */   private static final int DEFAULT_MAX_PARAMS = 1024;
/*     */   private final Charset charset;
/*     */   private final String uri;
/*     */   private final int maxParams;
/*     */   private int pathEndIdx;
/*     */   private String path;
/*     */   private Map<String, List<String>> params;
/*     */   
/*     */   public QueryStringDecoder(String uri) {
/*  80 */     this(uri, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(String uri, boolean hasPath) {
/*  88 */     this(uri, HttpConstants.DEFAULT_CHARSET, hasPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(String uri, Charset charset) {
/*  96 */     this(uri, charset, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(String uri, Charset charset, boolean hasPath) {
/* 104 */     this(uri, charset, hasPath, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(String uri, Charset charset, boolean hasPath, int maxParams) {
/* 112 */     this.uri = (String)ObjectUtil.checkNotNull(uri, "uri");
/* 113 */     this.charset = (Charset)ObjectUtil.checkNotNull(charset, "charset");
/* 114 */     this.maxParams = ObjectUtil.checkPositive(maxParams, "maxParams");
/*     */ 
/*     */     
/* 117 */     this.pathEndIdx = hasPath ? -1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(URI uri) {
/* 125 */     this(uri, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(URI uri, Charset charset) {
/* 133 */     this(uri, charset, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QueryStringDecoder(URI uri, Charset charset, int maxParams) {
/* 141 */     String rawPath = uri.getRawPath();
/* 142 */     if (rawPath == null) {
/* 143 */       rawPath = "";
/*     */     }
/* 145 */     String rawQuery = uri.getRawQuery();
/*     */     
/* 147 */     this.uri = (rawQuery == null) ? rawPath : (rawPath + '?' + rawQuery);
/* 148 */     this.charset = (Charset)ObjectUtil.checkNotNull(charset, "charset");
/* 149 */     this.maxParams = ObjectUtil.checkPositive(maxParams, "maxParams");
/* 150 */     this.pathEndIdx = rawPath.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 155 */     return uri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String uri() {
/* 162 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String path() {
/* 169 */     if (this.path == null) {
/* 170 */       this.path = decodeComponent(this.uri, 0, pathEndIdx(), this.charset, true);
/*     */     }
/* 172 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<String>> parameters() {
/* 179 */     if (this.params == null) {
/* 180 */       this.params = decodeParams(this.uri, pathEndIdx(), this.charset, this.maxParams);
/*     */     }
/* 182 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String rawPath() {
/* 189 */     return this.uri.substring(0, pathEndIdx());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String rawQuery() {
/* 196 */     int start = pathEndIdx() + 1;
/* 197 */     return (start < this.uri.length()) ? this.uri.substring(start) : "";
/*     */   }
/*     */   
/*     */   private int pathEndIdx() {
/* 201 */     if (this.pathEndIdx == -1) {
/* 202 */       this.pathEndIdx = findPathEndIndex(this.uri);
/*     */     }
/* 204 */     return this.pathEndIdx;
/*     */   }
/*     */   
/*     */   private static Map<String, List<String>> decodeParams(String s, int from, Charset charset, int paramsLimit) {
/* 208 */     int len = s.length();
/* 209 */     if (from >= len) {
/* 210 */       return Collections.emptyMap();
/*     */     }
/* 212 */     if (s.charAt(from) == '?') {
/* 213 */       from++;
/*     */     }
/* 215 */     Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
/* 216 */     int nameStart = from;
/* 217 */     int valueStart = -1;
/*     */     
/*     */     int i;
/* 220 */     for (i = from; i < len; i++) {
/* 221 */       switch (s.charAt(i)) {
/*     */         case '=':
/* 223 */           if (nameStart == i) {
/* 224 */             nameStart = i + 1; break;
/* 225 */           }  if (valueStart < nameStart) {
/* 226 */             valueStart = i + 1;
/*     */           }
/*     */           break;
/*     */         
/*     */         case '&':
/*     */         case ';':
/* 232 */           paramsLimit--;
/* 233 */           if (addParam(s, nameStart, valueStart, i, params, charset) && paramsLimit == 0) {
/* 234 */             return params;
/*     */           }
/*     */           
/* 237 */           nameStart = i + 1;
/*     */           break;
/*     */         
/*     */         case '#':
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 245 */     addParam(s, nameStart, valueStart, i, params, charset);
/* 246 */     return params;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean addParam(String s, int nameStart, int valueStart, int valueEnd, Map<String, List<String>> params, Charset charset) {
/* 251 */     if (nameStart >= valueEnd) {
/* 252 */       return false;
/*     */     }
/* 254 */     if (valueStart <= nameStart) {
/* 255 */       valueStart = valueEnd + 1;
/*     */     }
/* 257 */     String name = decodeComponent(s, nameStart, valueStart - 1, charset, false);
/* 258 */     String value = decodeComponent(s, valueStart, valueEnd, charset, false);
/* 259 */     List<String> values = params.get(name);
/* 260 */     if (values == null) {
/* 261 */       values = new ArrayList<String>(1);
/* 262 */       params.put(name, values);
/*     */     } 
/* 264 */     values.add(value);
/* 265 */     return true;
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
/*     */   public static String decodeComponent(String s) {
/* 280 */     return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String decodeComponent(String s, Charset charset) {
/* 306 */     if (s == null) {
/* 307 */       return "";
/*     */     }
/* 309 */     return decodeComponent(s, 0, s.length(), charset, false);
/*     */   }
/*     */   
/*     */   private static String decodeComponent(String s, int from, int toExcluded, Charset charset, boolean isPath) {
/* 313 */     int len = toExcluded - from;
/* 314 */     if (len <= 0) {
/* 315 */       return "";
/*     */     }
/* 317 */     int firstEscaped = -1;
/* 318 */     for (int i = from; i < toExcluded; i++) {
/* 319 */       char c = s.charAt(i);
/* 320 */       if (c == '%' || (c == '+' && !isPath)) {
/* 321 */         firstEscaped = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 325 */     if (firstEscaped == -1) {
/* 326 */       return s.substring(from, toExcluded);
/*     */     }
/*     */     
/* 329 */     CharsetDecoder decoder = CharsetUtil.decoder(charset);
/*     */ 
/*     */     
/* 332 */     int decodedCapacity = (toExcluded - firstEscaped) / 3;
/* 333 */     ByteBuffer byteBuf = ByteBuffer.allocate(decodedCapacity);
/* 334 */     CharBuffer charBuf = CharBuffer.allocate(decodedCapacity);
/*     */     
/* 336 */     StringBuilder strBuf = new StringBuilder(len);
/* 337 */     strBuf.append(s, from, firstEscaped);
/*     */     
/* 339 */     for (int j = firstEscaped; j < toExcluded; j++) {
/* 340 */       char c = s.charAt(j);
/* 341 */       if (c != '%') {
/* 342 */         strBuf.append((c != '+' || isPath) ? c : 32);
/*     */       }
/*     */       else {
/*     */         
/* 346 */         byteBuf.clear();
/*     */         do {
/* 348 */           if (j + 3 > toExcluded) {
/* 349 */             throw new IllegalArgumentException("unterminated escape sequence at index " + j + " of: " + s);
/*     */           }
/* 351 */           byteBuf.put(StringUtil.decodeHexByte(s, j + 1));
/* 352 */           j += 3;
/* 353 */         } while (j < toExcluded && s.charAt(j) == '%');
/* 354 */         j--;
/*     */         
/* 356 */         byteBuf.flip();
/* 357 */         charBuf.clear();
/* 358 */         CoderResult result = decoder.reset().decode(byteBuf, charBuf, true);
/*     */         try {
/* 360 */           if (!result.isUnderflow()) {
/* 361 */             result.throwException();
/*     */           }
/* 363 */           result = decoder.flush(charBuf);
/* 364 */           if (!result.isUnderflow()) {
/* 365 */             result.throwException();
/*     */           }
/* 367 */         } catch (CharacterCodingException ex) {
/* 368 */           throw new IllegalStateException(ex);
/*     */         } 
/* 370 */         strBuf.append(charBuf.flip());
/*     */       } 
/* 372 */     }  return strBuf.toString();
/*     */   }
/*     */   
/*     */   private static int findPathEndIndex(String uri) {
/* 376 */     int len = uri.length();
/* 377 */     for (int i = 0; i < len; i++) {
/* 378 */       char c = uri.charAt(i);
/* 379 */       if (c == '?' || c == '#') {
/* 380 */         return i;
/*     */       }
/*     */     } 
/* 383 */     return len;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\QueryStringDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */