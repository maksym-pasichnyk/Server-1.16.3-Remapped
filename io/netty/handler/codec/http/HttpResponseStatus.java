/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.CharsetUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponseStatus
/*     */   implements Comparable<HttpResponseStatus>
/*     */ {
/*  37 */   public static final HttpResponseStatus CONTINUE = newStatus(100, "Continue");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final HttpResponseStatus SWITCHING_PROTOCOLS = newStatus(101, "Switching Protocols");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final HttpResponseStatus PROCESSING = newStatus(102, "Processing");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final HttpResponseStatus OK = newStatus(200, "OK");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final HttpResponseStatus CREATED = newStatus(201, "Created");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final HttpResponseStatus ACCEPTED = newStatus(202, "Accepted");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static final HttpResponseStatus NON_AUTHORITATIVE_INFORMATION = newStatus(203, "Non-Authoritative Information");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final HttpResponseStatus NO_CONTENT = newStatus(204, "No Content");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final HttpResponseStatus RESET_CONTENT = newStatus(205, "Reset Content");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final HttpResponseStatus PARTIAL_CONTENT = newStatus(206, "Partial Content");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final HttpResponseStatus MULTI_STATUS = newStatus(207, "Multi-Status");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static final HttpResponseStatus MULTIPLE_CHOICES = newStatus(300, "Multiple Choices");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final HttpResponseStatus MOVED_PERMANENTLY = newStatus(301, "Moved Permanently");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final HttpResponseStatus FOUND = newStatus(302, "Found");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static final HttpResponseStatus SEE_OTHER = newStatus(303, "See Other");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final HttpResponseStatus NOT_MODIFIED = newStatus(304, "Not Modified");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static final HttpResponseStatus USE_PROXY = newStatus(305, "Use Proxy");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final HttpResponseStatus TEMPORARY_REDIRECT = newStatus(307, "Temporary Redirect");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public static final HttpResponseStatus PERMANENT_REDIRECT = newStatus(308, "Permanent Redirect");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public static final HttpResponseStatus BAD_REQUEST = newStatus(400, "Bad Request");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static final HttpResponseStatus UNAUTHORIZED = newStatus(401, "Unauthorized");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static final HttpResponseStatus PAYMENT_REQUIRED = newStatus(402, "Payment Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static final HttpResponseStatus FORBIDDEN = newStatus(403, "Forbidden");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public static final HttpResponseStatus NOT_FOUND = newStatus(404, "Not Found");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public static final HttpResponseStatus METHOD_NOT_ALLOWED = newStatus(405, "Method Not Allowed");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public static final HttpResponseStatus NOT_ACCEPTABLE = newStatus(406, "Not Acceptable");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED = newStatus(407, "Proxy Authentication Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final HttpResponseStatus REQUEST_TIMEOUT = newStatus(408, "Request Timeout");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public static final HttpResponseStatus CONFLICT = newStatus(409, "Conflict");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static final HttpResponseStatus GONE = newStatus(410, "Gone");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   public static final HttpResponseStatus LENGTH_REQUIRED = newStatus(411, "Length Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public static final HttpResponseStatus PRECONDITION_FAILED = newStatus(412, "Precondition Failed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE = newStatus(413, "Request Entity Too Large");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   public static final HttpResponseStatus REQUEST_URI_TOO_LONG = newStatus(414, "Request-URI Too Long");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE = newStatus(415, "Unsupported Media Type");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   public static final HttpResponseStatus REQUESTED_RANGE_NOT_SATISFIABLE = newStatus(416, "Requested Range Not Satisfiable");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   public static final HttpResponseStatus EXPECTATION_FAILED = newStatus(417, "Expectation Failed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public static final HttpResponseStatus MISDIRECTED_REQUEST = newStatus(421, "Misdirected Request");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   public static final HttpResponseStatus UNPROCESSABLE_ENTITY = newStatus(422, "Unprocessable Entity");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public static final HttpResponseStatus LOCKED = newStatus(423, "Locked");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   public static final HttpResponseStatus FAILED_DEPENDENCY = newStatus(424, "Failed Dependency");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public static final HttpResponseStatus UNORDERED_COLLECTION = newStatus(425, "Unordered Collection");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   public static final HttpResponseStatus UPGRADE_REQUIRED = newStatus(426, "Upgrade Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public static final HttpResponseStatus PRECONDITION_REQUIRED = newStatus(428, "Precondition Required");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   public static final HttpResponseStatus TOO_MANY_REQUESTS = newStatus(429, "Too Many Requests");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   public static final HttpResponseStatus REQUEST_HEADER_FIELDS_TOO_LARGE = newStatus(431, "Request Header Fields Too Large");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 274 */   public static final HttpResponseStatus INTERNAL_SERVER_ERROR = newStatus(500, "Internal Server Error");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 279 */   public static final HttpResponseStatus NOT_IMPLEMENTED = newStatus(501, "Not Implemented");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   public static final HttpResponseStatus BAD_GATEWAY = newStatus(502, "Bad Gateway");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 289 */   public static final HttpResponseStatus SERVICE_UNAVAILABLE = newStatus(503, "Service Unavailable");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 294 */   public static final HttpResponseStatus GATEWAY_TIMEOUT = newStatus(504, "Gateway Timeout");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   public static final HttpResponseStatus HTTP_VERSION_NOT_SUPPORTED = newStatus(505, "HTTP Version Not Supported");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 305 */   public static final HttpResponseStatus VARIANT_ALSO_NEGOTIATES = newStatus(506, "Variant Also Negotiates");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   public static final HttpResponseStatus INSUFFICIENT_STORAGE = newStatus(507, "Insufficient Storage");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   public static final HttpResponseStatus NOT_EXTENDED = newStatus(510, "Not Extended");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   public static final HttpResponseStatus NETWORK_AUTHENTICATION_REQUIRED = newStatus(511, "Network Authentication Required");
/*     */   
/*     */   private static HttpResponseStatus newStatus(int statusCode, String reasonPhrase) {
/* 324 */     return new HttpResponseStatus(statusCode, reasonPhrase, true);
/*     */   }
/*     */   private final int code;
/*     */   private final AsciiString codeAsText;
/*     */   private HttpStatusClass codeClass;
/*     */   private final String reasonPhrase;
/*     */   private final byte[] bytes;
/*     */   
/*     */   public static HttpResponseStatus valueOf(int code) {
/* 333 */     HttpResponseStatus status = valueOf0(code);
/* 334 */     return (status != null) ? status : new HttpResponseStatus(code);
/*     */   }
/*     */   
/*     */   private static HttpResponseStatus valueOf0(int code) {
/* 338 */     switch (code) {
/*     */       case 100:
/* 340 */         return CONTINUE;
/*     */       case 101:
/* 342 */         return SWITCHING_PROTOCOLS;
/*     */       case 102:
/* 344 */         return PROCESSING;
/*     */       case 200:
/* 346 */         return OK;
/*     */       case 201:
/* 348 */         return CREATED;
/*     */       case 202:
/* 350 */         return ACCEPTED;
/*     */       case 203:
/* 352 */         return NON_AUTHORITATIVE_INFORMATION;
/*     */       case 204:
/* 354 */         return NO_CONTENT;
/*     */       case 205:
/* 356 */         return RESET_CONTENT;
/*     */       case 206:
/* 358 */         return PARTIAL_CONTENT;
/*     */       case 207:
/* 360 */         return MULTI_STATUS;
/*     */       case 300:
/* 362 */         return MULTIPLE_CHOICES;
/*     */       case 301:
/* 364 */         return MOVED_PERMANENTLY;
/*     */       case 302:
/* 366 */         return FOUND;
/*     */       case 303:
/* 368 */         return SEE_OTHER;
/*     */       case 304:
/* 370 */         return NOT_MODIFIED;
/*     */       case 305:
/* 372 */         return USE_PROXY;
/*     */       case 307:
/* 374 */         return TEMPORARY_REDIRECT;
/*     */       case 308:
/* 376 */         return PERMANENT_REDIRECT;
/*     */       case 400:
/* 378 */         return BAD_REQUEST;
/*     */       case 401:
/* 380 */         return UNAUTHORIZED;
/*     */       case 402:
/* 382 */         return PAYMENT_REQUIRED;
/*     */       case 403:
/* 384 */         return FORBIDDEN;
/*     */       case 404:
/* 386 */         return NOT_FOUND;
/*     */       case 405:
/* 388 */         return METHOD_NOT_ALLOWED;
/*     */       case 406:
/* 390 */         return NOT_ACCEPTABLE;
/*     */       case 407:
/* 392 */         return PROXY_AUTHENTICATION_REQUIRED;
/*     */       case 408:
/* 394 */         return REQUEST_TIMEOUT;
/*     */       case 409:
/* 396 */         return CONFLICT;
/*     */       case 410:
/* 398 */         return GONE;
/*     */       case 411:
/* 400 */         return LENGTH_REQUIRED;
/*     */       case 412:
/* 402 */         return PRECONDITION_FAILED;
/*     */       case 413:
/* 404 */         return REQUEST_ENTITY_TOO_LARGE;
/*     */       case 414:
/* 406 */         return REQUEST_URI_TOO_LONG;
/*     */       case 415:
/* 408 */         return UNSUPPORTED_MEDIA_TYPE;
/*     */       case 416:
/* 410 */         return REQUESTED_RANGE_NOT_SATISFIABLE;
/*     */       case 417:
/* 412 */         return EXPECTATION_FAILED;
/*     */       case 421:
/* 414 */         return MISDIRECTED_REQUEST;
/*     */       case 422:
/* 416 */         return UNPROCESSABLE_ENTITY;
/*     */       case 423:
/* 418 */         return LOCKED;
/*     */       case 424:
/* 420 */         return FAILED_DEPENDENCY;
/*     */       case 425:
/* 422 */         return UNORDERED_COLLECTION;
/*     */       case 426:
/* 424 */         return UPGRADE_REQUIRED;
/*     */       case 428:
/* 426 */         return PRECONDITION_REQUIRED;
/*     */       case 429:
/* 428 */         return TOO_MANY_REQUESTS;
/*     */       case 431:
/* 430 */         return REQUEST_HEADER_FIELDS_TOO_LARGE;
/*     */       case 500:
/* 432 */         return INTERNAL_SERVER_ERROR;
/*     */       case 501:
/* 434 */         return NOT_IMPLEMENTED;
/*     */       case 502:
/* 436 */         return BAD_GATEWAY;
/*     */       case 503:
/* 438 */         return SERVICE_UNAVAILABLE;
/*     */       case 504:
/* 440 */         return GATEWAY_TIMEOUT;
/*     */       case 505:
/* 442 */         return HTTP_VERSION_NOT_SUPPORTED;
/*     */       case 506:
/* 444 */         return VARIANT_ALSO_NEGOTIATES;
/*     */       case 507:
/* 446 */         return INSUFFICIENT_STORAGE;
/*     */       case 510:
/* 448 */         return NOT_EXTENDED;
/*     */       case 511:
/* 450 */         return NETWORK_AUTHENTICATION_REQUIRED;
/*     */     } 
/* 452 */     return null;
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
/*     */   public static HttpResponseStatus valueOf(int code, String reasonPhrase) {
/* 464 */     HttpResponseStatus responseStatus = valueOf0(code);
/* 465 */     return (responseStatus != null && responseStatus.reasonPhrase().contentEquals(reasonPhrase)) ? responseStatus : new HttpResponseStatus(code, reasonPhrase);
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
/*     */   public static HttpResponseStatus parseLine(CharSequence line) {
/* 479 */     return (line instanceof AsciiString) ? parseLine((AsciiString)line) : parseLine(line.toString());
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
/*     */   public static HttpResponseStatus parseLine(String line) {
/*     */     try {
/* 493 */       int space = line.indexOf(' ');
/* 494 */       return (space == -1) ? valueOf(Integer.parseInt(line)) : 
/* 495 */         valueOf(Integer.parseInt(line.substring(0, space)), line.substring(space + 1));
/* 496 */     } catch (Exception e) {
/* 497 */       throw new IllegalArgumentException("malformed status line: " + line, e);
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
/*     */   public static HttpResponseStatus parseLine(AsciiString line) {
/*     */     try {
/* 512 */       int space = line.forEachByte(ByteProcessor.FIND_ASCII_SPACE);
/* 513 */       return (space == -1) ? valueOf(line.parseInt()) : valueOf(line.parseInt(0, space), line.toString(space + 1));
/* 514 */     } catch (Exception e) {
/* 515 */       throw new IllegalArgumentException("malformed status line: " + line, e);
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
/*     */   private HttpResponseStatus(int code) {
/* 530 */     this(code, HttpStatusClass.valueOf(code).defaultReasonPhrase() + " (" + code + ')', false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpResponseStatus(int code, String reasonPhrase) {
/* 537 */     this(code, reasonPhrase, false);
/*     */   }
/*     */   
/*     */   private HttpResponseStatus(int code, String reasonPhrase, boolean bytes) {
/* 541 */     if (code < 0) {
/* 542 */       throw new IllegalArgumentException("code: " + code + " (expected: 0+)");
/*     */     }
/*     */ 
/*     */     
/* 546 */     if (reasonPhrase == null) {
/* 547 */       throw new NullPointerException("reasonPhrase");
/*     */     }
/*     */     
/* 550 */     for (int i = 0; i < reasonPhrase.length(); i++) {
/* 551 */       char c = reasonPhrase.charAt(i);
/*     */       
/* 553 */       switch (c) { case '\n':
/*     */         case '\r':
/* 555 */           throw new IllegalArgumentException("reasonPhrase contains one of the following prohibited characters: \\r\\n: " + reasonPhrase); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 561 */     this.code = code;
/* 562 */     String codeString = Integer.toString(code);
/* 563 */     this.codeAsText = new AsciiString(codeString);
/* 564 */     this.reasonPhrase = reasonPhrase;
/* 565 */     if (bytes) {
/* 566 */       this.bytes = (codeString + ' ' + reasonPhrase).getBytes(CharsetUtil.US_ASCII);
/*     */     } else {
/* 568 */       this.bytes = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int code() {
/* 576 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsciiString codeAsText() {
/* 583 */     return this.codeAsText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String reasonPhrase() {
/* 590 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpStatusClass codeClass() {
/* 597 */     HttpStatusClass type = this.codeClass;
/* 598 */     if (type == null) {
/* 599 */       this.codeClass = type = HttpStatusClass.valueOf(this.code);
/*     */     }
/* 601 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 606 */     return code();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 615 */     if (!(o instanceof HttpResponseStatus)) {
/* 616 */       return false;
/*     */     }
/*     */     
/* 619 */     return (code() == ((HttpResponseStatus)o).code());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(HttpResponseStatus o) {
/* 628 */     return code() - o.code();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 633 */     return (new StringBuilder(this.reasonPhrase.length() + 4))
/* 634 */       .append((CharSequence)this.codeAsText)
/* 635 */       .append(' ')
/* 636 */       .append(this.reasonPhrase)
/* 637 */       .toString();
/*     */   }
/*     */   
/*     */   void encode(ByteBuf buf) {
/* 641 */     if (this.bytes == null) {
/* 642 */       ByteBufUtil.copy(this.codeAsText, buf);
/* 643 */       buf.writeByte(32);
/* 644 */       buf.writeCharSequence(this.reasonPhrase, CharsetUtil.US_ASCII);
/*     */     } else {
/* 646 */       buf.writeBytes(this.bytes);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpResponseStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */