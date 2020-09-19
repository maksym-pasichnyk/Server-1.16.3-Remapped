/*      */ package io.netty.handler.codec.http;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufUtil;
/*      */ import io.netty.handler.codec.DateFormatter;
/*      */ import io.netty.handler.codec.HeadersUtils;
/*      */ import io.netty.util.AsciiString;
/*      */ import io.netty.util.CharsetUtil;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import java.text.ParseException;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class HttpHeaders
/*      */   implements Iterable<Map.Entry<String, String>>
/*      */ {
/*      */   @Deprecated
/*   53 */   public static final HttpHeaders EMPTY_HEADERS = EmptyHttpHeaders.instance();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static final class Names
/*      */   {
/*      */     public static final String ACCEPT = "Accept";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCEPT_CHARSET = "Accept-Charset";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCEPT_ENCODING = "Accept-Encoding";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCEPT_LANGUAGE = "Accept-Language";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCEPT_RANGES = "Accept-Ranges";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCEPT_PATCH = "Accept-Patch";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String AGE = "Age";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ALLOW = "Allow";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String AUTHORIZATION = "Authorization";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CACHE_CONTROL = "Cache-Control";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONNECTION = "Connection";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_BASE = "Content-Base";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_ENCODING = "Content-Encoding";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_LANGUAGE = "Content-Language";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_LENGTH = "Content-Length";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_LOCATION = "Content-Location";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_MD5 = "Content-MD5";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_RANGE = "Content-Range";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTENT_TYPE = "Content-Type";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String COOKIE = "Cookie";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String DATE = "Date";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ETAG = "ETag";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String EXPECT = "Expect";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String EXPIRES = "Expires";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String FROM = "From";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String HOST = "Host";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String IF_MATCH = "If-Match";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String IF_NONE_MATCH = "If-None-Match";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String IF_RANGE = "If-Range";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String LAST_MODIFIED = "Last-Modified";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String LOCATION = "Location";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String MAX_FORWARDS = "Max-Forwards";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ORIGIN = "Origin";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String PRAGMA = "Pragma";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String RANGE = "Range";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String REFERER = "Referer";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String RETRY_AFTER = "Retry-After";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_KEY1 = "Sec-WebSocket-Key1";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_KEY2 = "Sec-WebSocket-Key2";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_LOCATION = "Sec-WebSocket-Location";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_ORIGIN = "Sec-WebSocket-Origin";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SERVER = "Server";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SET_COOKIE = "Set-Cookie";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String SET_COOKIE2 = "Set-Cookie2";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String TE = "TE";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String TRAILER = "Trailer";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String TRANSFER_ENCODING = "Transfer-Encoding";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String UPGRADE = "Upgrade";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String USER_AGENT = "User-Agent";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String VARY = "Vary";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String VIA = "Via";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String WARNING = "Warning";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String WEBSOCKET_LOCATION = "WebSocket-Location";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String WEBSOCKET_ORIGIN = "WebSocket-Origin";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String WEBSOCKET_PROTOCOL = "WebSocket-Protocol";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static final class Values
/*      */   {
/*      */     public static final String APPLICATION_JSON = "application/json";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String BASE64 = "base64";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String BINARY = "binary";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String BOUNDARY = "boundary";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String BYTES = "bytes";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CHARSET = "charset";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CHUNKED = "chunked";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CLOSE = "close";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String COMPRESS = "compress";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String CONTINUE = "100-continue";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String DEFLATE = "deflate";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String GZIP = "gzip";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String GZIP_DEFLATE = "gzip,deflate";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String IDENTITY = "identity";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String KEEP_ALIVE = "keep-alive";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String MAX_AGE = "max-age";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String MAX_STALE = "max-stale";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String MIN_FRESH = "min-fresh";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String MULTIPART_FORM_DATA = "multipart/form-data";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String MUST_REVALIDATE = "must-revalidate";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String NO_CACHE = "no-cache";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String NO_STORE = "no-store";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String NO_TRANSFORM = "no-transform";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String NONE = "none";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String ONLY_IF_CACHED = "only-if-cached";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String PRIVATE = "private";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String PROXY_REVALIDATE = "proxy-revalidate";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String PUBLIC = "public";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String QUOTED_PRINTABLE = "quoted-printable";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String S_MAXAGE = "s-maxage";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String TRAILERS = "trailers";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String UPGRADE = "Upgrade";
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String WEBSOCKET = "WebSocket";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isKeepAlive(HttpMessage message) {
/*  518 */     return HttpUtil.isKeepAlive(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setKeepAlive(HttpMessage message, boolean keepAlive) {
/*  544 */     HttpUtil.setKeepAlive(message, keepAlive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getHeader(HttpMessage message, String name) {
/*  552 */     return message.headers().get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getHeader(HttpMessage message, CharSequence name) {
/*  566 */     return message.headers().get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getHeader(HttpMessage message, String name, String defaultValue) {
/*  576 */     return message.headers().get(name, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getHeader(HttpMessage message, CharSequence name, String defaultValue) {
/*  591 */     return message.headers().get(name, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setHeader(HttpMessage message, String name, Object value) {
/*  601 */     message.headers().set(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setHeader(HttpMessage message, CharSequence name, Object value) {
/*  616 */     message.headers().set(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setHeader(HttpMessage message, String name, Iterable<?> values) {
/*  626 */     message.headers().set(name, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setHeader(HttpMessage message, CharSequence name, Iterable<?> values) {
/*  647 */     message.headers().set(name, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addHeader(HttpMessage message, String name, Object value) {
/*  657 */     message.headers().add(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addHeader(HttpMessage message, CharSequence name, Object value) {
/*  671 */     message.headers().add(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void removeHeader(HttpMessage message, String name) {
/*  681 */     message.headers().remove(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void removeHeader(HttpMessage message, CharSequence name) {
/*  691 */     message.headers().remove(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void clearHeaders(HttpMessage message) {
/*  701 */     message.headers().clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static int getIntHeader(HttpMessage message, String name) {
/*  711 */     return getIntHeader(message, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static int getIntHeader(HttpMessage message, CharSequence name) {
/*  727 */     String value = message.headers().get(name);
/*  728 */     if (value == null) {
/*  729 */       throw new NumberFormatException("header not found: " + name);
/*      */     }
/*  731 */     return Integer.parseInt(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static int getIntHeader(HttpMessage message, String name, int defaultValue) {
/*  741 */     return message.headers().getInt(name, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static int getIntHeader(HttpMessage message, CharSequence name, int defaultValue) {
/*  756 */     return message.headers().getInt(name, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setIntHeader(HttpMessage message, String name, int value) {
/*  766 */     message.headers().setInt(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setIntHeader(HttpMessage message, CharSequence name, int value) {
/*  777 */     message.headers().setInt(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setIntHeader(HttpMessage message, String name, Iterable<Integer> values) {
/*  787 */     message.headers().set(name, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setIntHeader(HttpMessage message, CharSequence name, Iterable<Integer> values) {
/*  798 */     message.headers().set(name, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addIntHeader(HttpMessage message, String name, int value) {
/*  808 */     message.headers().add(name, Integer.valueOf(value));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addIntHeader(HttpMessage message, CharSequence name, int value) {
/*  818 */     message.headers().addInt(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Date getDateHeader(HttpMessage message, String name) throws ParseException {
/*  828 */     return getDateHeader(message, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Date getDateHeader(HttpMessage message, CharSequence name) throws ParseException {
/*  844 */     String value = message.headers().get(name);
/*  845 */     if (value == null) {
/*  846 */       throw new ParseException("header not found: " + name, 0);
/*      */     }
/*  848 */     Date date = DateFormatter.parseHttpDate(value);
/*  849 */     if (date == null) {
/*  850 */       throw new ParseException("header can't be parsed into a Date: " + value, 0);
/*      */     }
/*  852 */     return date;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Date getDateHeader(HttpMessage message, String name, Date defaultValue) {
/*  862 */     return getDateHeader(message, name, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Date getDateHeader(HttpMessage message, CharSequence name, Date defaultValue) {
/*  877 */     String value = getHeader(message, name);
/*  878 */     Date date = DateFormatter.parseHttpDate(value);
/*  879 */     return (date != null) ? date : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setDateHeader(HttpMessage message, String name, Date value) {
/*  889 */     setDateHeader(message, name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setDateHeader(HttpMessage message, CharSequence name, Date value) {
/*  902 */     if (value != null) {
/*  903 */       message.headers().set(name, DateFormatter.format(value));
/*      */     } else {
/*  905 */       message.headers().set(name, (Iterable<?>)null);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setDateHeader(HttpMessage message, String name, Iterable<Date> values) {
/*  916 */     message.headers().set(name, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setDateHeader(HttpMessage message, CharSequence name, Iterable<Date> values) {
/*  929 */     message.headers().set(name, values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addDateHeader(HttpMessage message, String name, Date value) {
/*  939 */     message.headers().add(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void addDateHeader(HttpMessage message, CharSequence name, Date value) {
/*  951 */     message.headers().add(name, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static long getContentLength(HttpMessage message) {
/*  970 */     return HttpUtil.getContentLength(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static long getContentLength(HttpMessage message, long defaultValue) {
/*  987 */     return HttpUtil.getContentLength(message, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setContentLength(HttpMessage message, long length) {
/*  995 */     HttpUtil.setContentLength(message, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getHost(HttpMessage message) {
/* 1005 */     return message.headers().get((CharSequence)HttpHeaderNames.HOST);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getHost(HttpMessage message, String defaultValue) {
/* 1016 */     return message.headers().get((CharSequence)HttpHeaderNames.HOST, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setHost(HttpMessage message, String value) {
/* 1026 */     message.headers().set((CharSequence)HttpHeaderNames.HOST, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setHost(HttpMessage message, CharSequence value) {
/* 1036 */     message.headers().set((CharSequence)HttpHeaderNames.HOST, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Date getDate(HttpMessage message) throws ParseException {
/* 1049 */     return getDateHeader(message, (CharSequence)HttpHeaderNames.DATE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static Date getDate(HttpMessage message, Date defaultValue) {
/* 1061 */     return getDateHeader(message, (CharSequence)HttpHeaderNames.DATE, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setDate(HttpMessage message, Date value) {
/* 1071 */     message.headers().set((CharSequence)HttpHeaderNames.DATE, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean is100ContinueExpected(HttpMessage message) {
/* 1082 */     return HttpUtil.is100ContinueExpected(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void set100ContinueExpected(HttpMessage message) {
/* 1094 */     HttpUtil.set100ContinueExpected(message, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void set100ContinueExpected(HttpMessage message, boolean set) {
/* 1108 */     HttpUtil.set100ContinueExpected(message, set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isTransferEncodingChunked(HttpMessage message) {
/* 1121 */     return HttpUtil.isTransferEncodingChunked(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void removeTransferEncodingChunked(HttpMessage m) {
/* 1129 */     HttpUtil.setTransferEncodingChunked(m, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setTransferEncodingChunked(HttpMessage m) {
/* 1137 */     HttpUtil.setTransferEncodingChunked(m, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isContentLengthSet(HttpMessage m) {
/* 1145 */     return HttpUtil.isContentLengthSet(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean equalsIgnoreCase(CharSequence name1, CharSequence name2) {
/* 1153 */     return AsciiString.contentEqualsIgnoreCase(name1, name2);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static void encodeAscii(CharSequence seq, ByteBuf buf) {
/* 1158 */     if (seq instanceof AsciiString) {
/* 1159 */       ByteBufUtil.copy((AsciiString)seq, 0, buf, seq.length());
/*      */     } else {
/* 1161 */       buf.writeCharSequence(seq, CharsetUtil.US_ASCII);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharSequence newEntity(String name) {
/* 1173 */     return (CharSequence)new AsciiString(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String get(String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String get(CharSequence name) {
/* 1192 */     return get(name.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String get(CharSequence name, String defaultValue) {
/* 1203 */     String value = get(name);
/* 1204 */     if (value == null) {
/* 1205 */       return defaultValue;
/*      */     }
/* 1207 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Integer getInt(CharSequence paramCharSequence);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int getInt(CharSequence paramCharSequence, int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Short getShort(CharSequence paramCharSequence);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract short getShort(CharSequence paramCharSequence, short paramShort);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Long getTimeMillis(CharSequence paramCharSequence);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract long getTimeMillis(CharSequence paramCharSequence, long paramLong);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract List<String> getAll(String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getAll(CharSequence name) {
/* 1287 */     return getAll(name.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract List<Map.Entry<String, String>> entries();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean contains(String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public abstract Iterator<Map.Entry<String, String>> iterator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<String> valueStringIterator(CharSequence name) {
/* 1322 */     return getAll(name).iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<? extends CharSequence> valueCharSequenceIterator(CharSequence name) {
/* 1331 */     return (Iterator)valueStringIterator(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(CharSequence name) {
/* 1341 */     return contains(name.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isEmpty();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract int size();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract Set<String> names();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders add(String paramString, Object paramObject);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders add(CharSequence name, Object value) {
/* 1380 */     return add(name.toString(), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders add(String paramString, Iterable<?> paramIterable);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders add(CharSequence name, Iterable<?> values) {
/* 1406 */     return add(name.toString(), values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders add(HttpHeaders headers) {
/* 1415 */     if (headers == null) {
/* 1416 */       throw new NullPointerException("headers");
/*      */     }
/* 1418 */     for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)headers) {
/* 1419 */       add(e.getKey(), e.getValue());
/*      */     }
/* 1421 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders addInt(CharSequence paramCharSequence, int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders addShort(CharSequence paramCharSequence, short paramShort);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders set(String paramString, Object paramObject);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders set(CharSequence name, Object value) {
/* 1459 */     return set(name.toString(), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders set(String paramString, Iterable<?> paramIterable);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders set(CharSequence name, Iterable<?> values) {
/* 1487 */     return set(name.toString(), values);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders set(HttpHeaders headers) {
/* 1496 */     ObjectUtil.checkNotNull(headers, "headers");
/*      */     
/* 1498 */     clear();
/*      */     
/* 1500 */     if (headers.isEmpty()) {
/* 1501 */       return this;
/*      */     }
/*      */     
/* 1504 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)headers) {
/* 1505 */       add(entry.getKey(), entry.getValue());
/*      */     }
/* 1507 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders setAll(HttpHeaders headers) {
/* 1517 */     ObjectUtil.checkNotNull(headers, "headers");
/*      */     
/* 1519 */     if (headers.isEmpty()) {
/* 1520 */       return this;
/*      */     }
/*      */     
/* 1523 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)headers) {
/* 1524 */       set(entry.getKey(), entry.getValue());
/*      */     }
/* 1526 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders setInt(CharSequence paramCharSequence, int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders setShort(CharSequence paramCharSequence, short paramShort);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders remove(String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders remove(CharSequence name) {
/* 1557 */     return remove(name.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract HttpHeaders clear();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(String name, String value, boolean ignoreCase) {
/* 1571 */     Iterator<String> valueIterator = valueStringIterator(name);
/* 1572 */     if (ignoreCase) {
/* 1573 */       while (valueIterator.hasNext()) {
/* 1574 */         if (((String)valueIterator.next()).equalsIgnoreCase(value)) {
/* 1575 */           return true;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1579 */       while (valueIterator.hasNext()) {
/* 1580 */         if (((String)valueIterator.next()).equals(value)) {
/* 1581 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 1585 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsValue(CharSequence name, CharSequence value, boolean ignoreCase) {
/* 1599 */     Iterator<? extends CharSequence> itr = valueCharSequenceIterator(name);
/* 1600 */     while (itr.hasNext()) {
/* 1601 */       if (containsCommaSeparatedTrimmed(itr.next(), value, ignoreCase)) {
/* 1602 */         return true;
/*      */       }
/*      */     } 
/* 1605 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean containsCommaSeparatedTrimmed(CharSequence rawNext, CharSequence expected, boolean ignoreCase) {
/* 1610 */     int begin = 0;
/*      */     
/* 1612 */     if (ignoreCase) {
/* 1613 */       int end; if ((end = AsciiString.indexOf(rawNext, ',', begin)) == -1) {
/* 1614 */         if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(rawNext), expected)) {
/* 1615 */           return true;
/*      */         }
/*      */       } else {
/*      */         do {
/* 1619 */           if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(rawNext.subSequence(begin, end)), expected)) {
/* 1620 */             return true;
/*      */           }
/* 1622 */           begin = end + 1;
/* 1623 */         } while ((end = AsciiString.indexOf(rawNext, ',', begin)) != -1);
/*      */         
/* 1625 */         if (begin < rawNext.length() && 
/* 1626 */           AsciiString.contentEqualsIgnoreCase(AsciiString.trim(rawNext.subSequence(begin, rawNext.length())), expected)) {
/* 1627 */           return true;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       int end;
/* 1632 */       if ((end = AsciiString.indexOf(rawNext, ',', begin)) == -1) {
/* 1633 */         if (AsciiString.contentEquals(AsciiString.trim(rawNext), expected)) {
/* 1634 */           return true;
/*      */         }
/*      */       } else {
/*      */         do {
/* 1638 */           if (AsciiString.contentEquals(AsciiString.trim(rawNext.subSequence(begin, end)), expected)) {
/* 1639 */             return true;
/*      */           }
/* 1641 */           begin = end + 1;
/* 1642 */         } while ((end = AsciiString.indexOf(rawNext, ',', begin)) != -1);
/*      */         
/* 1644 */         if (begin < rawNext.length() && 
/* 1645 */           AsciiString.contentEquals(AsciiString.trim(rawNext.subSequence(begin, rawNext.length())), expected)) {
/* 1646 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1651 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getAsString(CharSequence name) {
/* 1660 */     return get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final List<String> getAllAsString(CharSequence name) {
/* 1669 */     return getAll(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Iterator<Map.Entry<String, String>> iteratorAsString() {
/* 1676 */     return iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
/* 1689 */     return contains(name.toString(), value.toString(), ignoreCase);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1694 */     return HeadersUtils.toString(getClass(), iteratorCharSequence(), size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpHeaders copy() {
/* 1701 */     return (new DefaultHttpHeaders()).set(this);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */