/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public final class HttpUtil
/*     */ {
/*  35 */   private static final AsciiString CHARSET_EQUALS = AsciiString.of(HttpHeaderValues.CHARSET + "=");
/*  36 */   private static final AsciiString SEMICOLON = AsciiString.cached(";");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOriginForm(URI uri) {
/*  45 */     return (uri.getScheme() == null && uri.getSchemeSpecificPart() == null && uri
/*  46 */       .getHost() == null && uri.getAuthority() == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAsteriskForm(URI uri) {
/*  54 */     return ("*".equals(uri.getPath()) && uri
/*  55 */       .getScheme() == null && uri.getSchemeSpecificPart() == null && uri
/*  56 */       .getHost() == null && uri.getAuthority() == null && uri.getQuery() == null && uri
/*  57 */       .getFragment() == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isKeepAlive(HttpMessage message) {
/*  67 */     CharSequence connection = message.headers().get((CharSequence)HttpHeaderNames.CONNECTION);
/*  68 */     if (connection != null && HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
/*  69 */       return false;
/*     */     }
/*     */     
/*  72 */     if (message.protocolVersion().isKeepAliveDefault()) {
/*  73 */       return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
/*     */     }
/*  75 */     return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
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
/*     */   public static void setKeepAlive(HttpMessage message, boolean keepAlive) {
/* 100 */     setKeepAlive(message.headers(), message.protocolVersion(), keepAlive);
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
/*     */   public static void setKeepAlive(HttpHeaders h, HttpVersion httpVersion, boolean keepAlive) {
/* 123 */     if (httpVersion.isKeepAliveDefault()) {
/* 124 */       if (keepAlive) {
/* 125 */         h.remove((CharSequence)HttpHeaderNames.CONNECTION);
/*     */       } else {
/* 127 */         h.set((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
/*     */       }
/*     */     
/* 130 */     } else if (keepAlive) {
/* 131 */       h.set((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
/*     */     } else {
/* 133 */       h.remove((CharSequence)HttpHeaderNames.CONNECTION);
/*     */     } 
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
/*     */   public static long getContentLength(HttpMessage message) {
/* 151 */     String value = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
/* 152 */     if (value != null) {
/* 153 */       return Long.parseLong(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 158 */     long webSocketContentLength = getWebSocketContentLength(message);
/* 159 */     if (webSocketContentLength >= 0L) {
/* 160 */       return webSocketContentLength;
/*     */     }
/*     */ 
/*     */     
/* 164 */     throw new NumberFormatException("header not found: " + HttpHeaderNames.CONTENT_LENGTH);
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
/*     */   public static long getContentLength(HttpMessage message, long defaultValue) {
/* 178 */     String value = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
/* 179 */     if (value != null) {
/* 180 */       return Long.parseLong(value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     long webSocketContentLength = getWebSocketContentLength(message);
/* 186 */     if (webSocketContentLength >= 0L) {
/* 187 */       return webSocketContentLength;
/*     */     }
/*     */ 
/*     */     
/* 191 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getContentLength(HttpMessage message, int defaultValue) {
/* 201 */     return (int)Math.min(2147483647L, getContentLength(message, defaultValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getWebSocketContentLength(HttpMessage message) {
/* 210 */     HttpHeaders h = message.headers();
/* 211 */     if (message instanceof HttpRequest) {
/* 212 */       HttpRequest req = (HttpRequest)message;
/* 213 */       if (HttpMethod.GET.equals(req.method()) && h
/* 214 */         .contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY1) && h
/* 215 */         .contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
/* 216 */         return 8;
/*     */       }
/* 218 */     } else if (message instanceof HttpResponse) {
/* 219 */       HttpResponse res = (HttpResponse)message;
/* 220 */       if (res.status().code() == 101 && h
/* 221 */         .contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) && h
/* 222 */         .contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
/* 223 */         return 16;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 228 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setContentLength(HttpMessage message, long length) {
/* 235 */     message.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Long.valueOf(length));
/*     */   }
/*     */   
/*     */   public static boolean isContentLengthSet(HttpMessage m) {
/* 239 */     return m.headers().contains((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
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
/*     */   public static boolean is100ContinueExpected(HttpMessage message) {
/* 252 */     if (!isExpectHeaderValid(message)) {
/* 253 */       return false;
/*     */     }
/*     */     
/* 256 */     String expectValue = message.headers().get((CharSequence)HttpHeaderNames.EXPECT);
/*     */     
/* 258 */     return HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue);
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
/*     */   static boolean isUnsupportedExpectation(HttpMessage message) {
/* 270 */     if (!isExpectHeaderValid(message)) {
/* 271 */       return false;
/*     */     }
/*     */     
/* 274 */     String expectValue = message.headers().get((CharSequence)HttpHeaderNames.EXPECT);
/* 275 */     return (expectValue != null && !HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isExpectHeaderValid(HttpMessage message) {
/* 284 */     return (message instanceof HttpRequest && message
/* 285 */       .protocolVersion().compareTo(HttpVersion.HTTP_1_1) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void set100ContinueExpected(HttpMessage message, boolean expected) {
/* 296 */     if (expected) {
/* 297 */       message.headers().set((CharSequence)HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
/*     */     } else {
/* 299 */       message.headers().remove((CharSequence)HttpHeaderNames.EXPECT);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTransferEncodingChunked(HttpMessage message) {
/* 310 */     return message.headers().contains((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, (CharSequence)HttpHeaderValues.CHUNKED, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTransferEncodingChunked(HttpMessage m, boolean chunked) {
/* 321 */     if (chunked) {
/* 322 */       m.headers().set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
/* 323 */       m.headers().remove((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
/*     */     } else {
/* 325 */       List<String> encodings = m.headers().getAll((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
/* 326 */       if (encodings.isEmpty()) {
/*     */         return;
/*     */       }
/* 329 */       List<CharSequence> values = new ArrayList<CharSequence>((Collection)encodings);
/* 330 */       Iterator<CharSequence> valuesIt = values.iterator();
/* 331 */       while (valuesIt.hasNext()) {
/* 332 */         CharSequence value = valuesIt.next();
/* 333 */         if (HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(value)) {
/* 334 */           valuesIt.remove();
/*     */         }
/*     */       } 
/* 337 */       if (values.isEmpty()) {
/* 338 */         m.headers().remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
/*     */       } else {
/* 340 */         m.headers().set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, values);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Charset getCharset(HttpMessage message) {
/* 353 */     return getCharset(message, CharsetUtil.ISO_8859_1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Charset getCharset(CharSequence contentTypeValue) {
/* 364 */     if (contentTypeValue != null) {
/* 365 */       return getCharset(contentTypeValue, CharsetUtil.ISO_8859_1);
/*     */     }
/* 367 */     return CharsetUtil.ISO_8859_1;
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
/*     */   public static Charset getCharset(HttpMessage message, Charset defaultCharset) {
/* 380 */     CharSequence contentTypeValue = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE);
/* 381 */     if (contentTypeValue != null) {
/* 382 */       return getCharset(contentTypeValue, defaultCharset);
/*     */     }
/* 384 */     return defaultCharset;
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
/*     */   public static Charset getCharset(CharSequence contentTypeValue, Charset defaultCharset) {
/* 397 */     if (contentTypeValue != null) {
/* 398 */       CharSequence charsetCharSequence = getCharsetAsSequence(contentTypeValue);
/* 399 */       if (charsetCharSequence != null) {
/*     */         try {
/* 401 */           return Charset.forName(charsetCharSequence.toString());
/* 402 */         } catch (UnsupportedCharsetException ignored) {
/* 403 */           return defaultCharset;
/*     */         } 
/*     */       }
/* 406 */       return defaultCharset;
/*     */     } 
/*     */     
/* 409 */     return defaultCharset;
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
/*     */   @Deprecated
/*     */   public static CharSequence getCharsetAsString(HttpMessage message) {
/* 426 */     return getCharsetAsSequence(message);
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
/*     */   public static CharSequence getCharsetAsSequence(HttpMessage message) {
/* 439 */     CharSequence contentTypeValue = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE);
/* 440 */     if (contentTypeValue != null) {
/* 441 */       return getCharsetAsSequence(contentTypeValue);
/*     */     }
/* 443 */     return null;
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
/*     */   public static CharSequence getCharsetAsSequence(CharSequence contentTypeValue) {
/* 459 */     if (contentTypeValue == null) {
/* 460 */       throw new NullPointerException("contentTypeValue");
/*     */     }
/* 462 */     int indexOfCharset = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, (CharSequence)CHARSET_EQUALS, 0);
/* 463 */     if (indexOfCharset != -1) {
/* 464 */       int indexOfEncoding = indexOfCharset + CHARSET_EQUALS.length();
/* 465 */       if (indexOfEncoding < contentTypeValue.length()) {
/* 466 */         return contentTypeValue.subSequence(indexOfEncoding, contentTypeValue.length());
/*     */       }
/*     */     } 
/* 469 */     return null;
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
/*     */   public static CharSequence getMimeType(HttpMessage message) {
/* 484 */     CharSequence contentTypeValue = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE);
/* 485 */     if (contentTypeValue != null) {
/* 486 */       return getMimeType(contentTypeValue);
/*     */     }
/* 488 */     return null;
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
/*     */   public static CharSequence getMimeType(CharSequence contentTypeValue) {
/* 505 */     if (contentTypeValue == null) {
/* 506 */       throw new NullPointerException("contentTypeValue");
/*     */     }
/*     */     
/* 509 */     int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, (CharSequence)SEMICOLON, 0);
/* 510 */     if (indexOfSemicolon != -1) {
/* 511 */       return contentTypeValue.subSequence(0, indexOfSemicolon);
/*     */     }
/* 513 */     return (contentTypeValue.length() > 0) ? contentTypeValue : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatHostnameForHttp(InetSocketAddress addr) {
/* 524 */     String hostString = NetUtil.getHostname(addr);
/* 525 */     if (NetUtil.isValidIpV6Address(hostString)) {
/* 526 */       if (!addr.isUnresolved()) {
/* 527 */         hostString = NetUtil.toAddressString(addr.getAddress());
/*     */       }
/* 529 */       return "[" + hostString + "]";
/*     */     } 
/* 531 */     return hostString;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */