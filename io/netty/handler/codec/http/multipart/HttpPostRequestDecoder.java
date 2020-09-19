/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class HttpPostRequestDecoder
/*     */   implements InterfaceHttpPostRequestDecoder
/*     */ {
/*     */   static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
/*     */   private final InterfaceHttpPostRequestDecoder decoder;
/*     */   
/*     */   public HttpPostRequestDecoder(HttpRequest request) {
/*  52 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request) {
/*  68 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) {
/*  86 */     if (factory == null) {
/*  87 */       throw new NullPointerException("factory");
/*     */     }
/*  89 */     if (request == null) {
/*  90 */       throw new NullPointerException("request");
/*     */     }
/*  92 */     if (charset == null) {
/*  93 */       throw new NullPointerException("charset");
/*     */     }
/*     */     
/*  96 */     if (isMultipart(request)) {
/*  97 */       this.decoder = new HttpPostMultipartRequestDecoder(factory, request, charset);
/*     */     } else {
/*  99 */       this.decoder = new HttpPostStandardRequestDecoder(factory, request, charset);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected enum MultiPartStatus
/*     */   {
/* 134 */     NOTSTARTED, PREAMBLE, HEADERDELIMITER, DISPOSITION, FIELD, FILEUPLOAD, MIXEDPREAMBLE, MIXEDDELIMITER,
/* 135 */     MIXEDDISPOSITION, MIXEDFILEUPLOAD, MIXEDCLOSEDELIMITER, CLOSEDELIMITER, PREEPILOGUE, EPILOGUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMultipart(HttpRequest request) {
/* 143 */     if (request.headers().contains((CharSequence)HttpHeaderNames.CONTENT_TYPE)) {
/* 144 */       return (getMultipartDataBoundary(request.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE)) != null);
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String[] getMultipartDataBoundary(String contentType) {
/* 157 */     String[] headerContentType = splitHeaderContentType(contentType);
/* 158 */     String multiPartHeader = HttpHeaderValues.MULTIPART_FORM_DATA.toString();
/* 159 */     if (headerContentType[0].regionMatches(true, 0, multiPartHeader, 0, multiPartHeader.length())) {
/*     */       int mrank, crank;
/*     */       
/* 162 */       String boundaryHeader = HttpHeaderValues.BOUNDARY.toString();
/* 163 */       if (headerContentType[1].regionMatches(true, 0, boundaryHeader, 0, boundaryHeader.length())) {
/* 164 */         mrank = 1;
/* 165 */         crank = 2;
/* 166 */       } else if (headerContentType[2].regionMatches(true, 0, boundaryHeader, 0, boundaryHeader.length())) {
/* 167 */         mrank = 2;
/* 168 */         crank = 1;
/*     */       } else {
/* 170 */         return null;
/*     */       } 
/* 172 */       String boundary = StringUtil.substringAfter(headerContentType[mrank], '=');
/* 173 */       if (boundary == null) {
/* 174 */         throw new ErrorDataDecoderException("Needs a boundary value");
/*     */       }
/* 176 */       if (boundary.charAt(0) == '"') {
/* 177 */         String bound = boundary.trim();
/* 178 */         int index = bound.length() - 1;
/* 179 */         if (bound.charAt(index) == '"') {
/* 180 */           boundary = bound.substring(1, index);
/*     */         }
/*     */       } 
/* 183 */       String charsetHeader = HttpHeaderValues.CHARSET.toString();
/* 184 */       if (headerContentType[crank].regionMatches(true, 0, charsetHeader, 0, charsetHeader.length())) {
/* 185 */         String charset = StringUtil.substringAfter(headerContentType[crank], '=');
/* 186 */         if (charset != null) {
/* 187 */           return new String[] { "--" + boundary, charset };
/*     */         }
/*     */       } 
/* 190 */       return new String[] { "--" + boundary };
/*     */     } 
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMultipart() {
/* 197 */     return this.decoder.isMultipart();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDiscardThreshold(int discardThreshold) {
/* 202 */     this.decoder.setDiscardThreshold(discardThreshold);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDiscardThreshold() {
/* 207 */     return this.decoder.getDiscardThreshold();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<InterfaceHttpData> getBodyHttpDatas() {
/* 212 */     return this.decoder.getBodyHttpDatas();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<InterfaceHttpData> getBodyHttpDatas(String name) {
/* 217 */     return this.decoder.getBodyHttpDatas(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData getBodyHttpData(String name) {
/* 222 */     return this.decoder.getBodyHttpData(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpPostRequestDecoder offer(HttpContent content) {
/* 227 */     return this.decoder.offer(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 232 */     return this.decoder.hasNext();
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData next() {
/* 237 */     return this.decoder.next();
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData currentPartialHttpData() {
/* 242 */     return this.decoder.currentPartialHttpData();
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 247 */     this.decoder.destroy();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanFiles() {
/* 252 */     this.decoder.cleanFiles();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeHttpDataFromClean(InterfaceHttpData data) {
/* 257 */     this.decoder.removeHttpDataFromClean(data);
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
/*     */   private static String[] splitHeaderContentType(String sb) {
/* 272 */     int aStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
/* 273 */     int aEnd = sb.indexOf(';');
/* 274 */     if (aEnd == -1) {
/* 275 */       return new String[] { sb, "", "" };
/*     */     }
/* 277 */     int bStart = HttpPostBodyUtil.findNonWhitespace(sb, aEnd + 1);
/* 278 */     if (sb.charAt(aEnd - 1) == ' ') {
/* 279 */       aEnd--;
/*     */     }
/* 281 */     int bEnd = sb.indexOf(';', bStart);
/* 282 */     if (bEnd == -1) {
/* 283 */       bEnd = HttpPostBodyUtil.findEndOfString(sb);
/* 284 */       return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), "" };
/*     */     } 
/* 286 */     int cStart = HttpPostBodyUtil.findNonWhitespace(sb, bEnd + 1);
/* 287 */     if (sb.charAt(bEnd - 1) == ' ') {
/* 288 */       bEnd--;
/*     */     }
/* 290 */     int cEnd = HttpPostBodyUtil.findEndOfString(sb);
/* 291 */     return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), sb.substring(cStart, cEnd) };
/*     */   }
/*     */ 
/*     */   
/*     */   public static class NotEnoughDataDecoderException
/*     */     extends DecoderException
/*     */   {
/*     */     private static final long serialVersionUID = -7846841864603865638L;
/*     */ 
/*     */     
/*     */     public NotEnoughDataDecoderException() {}
/*     */ 
/*     */     
/*     */     public NotEnoughDataDecoderException(String msg) {
/* 305 */       super(msg);
/*     */     }
/*     */     
/*     */     public NotEnoughDataDecoderException(Throwable cause) {
/* 309 */       super(cause);
/*     */     }
/*     */     
/*     */     public NotEnoughDataDecoderException(String msg, Throwable cause) {
/* 313 */       super(msg, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class EndOfDataDecoderException
/*     */     extends DecoderException
/*     */   {
/*     */     private static final long serialVersionUID = 1336267941020800769L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ErrorDataDecoderException
/*     */     extends DecoderException
/*     */   {
/*     */     private static final long serialVersionUID = 5020247425493164465L;
/*     */ 
/*     */     
/*     */     public ErrorDataDecoderException() {}
/*     */     
/*     */     public ErrorDataDecoderException(String msg) {
/* 334 */       super(msg);
/*     */     }
/*     */     
/*     */     public ErrorDataDecoderException(Throwable cause) {
/* 338 */       super(cause);
/*     */     }
/*     */     
/*     */     public ErrorDataDecoderException(String msg, Throwable cause) {
/* 342 */       super(msg, cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */