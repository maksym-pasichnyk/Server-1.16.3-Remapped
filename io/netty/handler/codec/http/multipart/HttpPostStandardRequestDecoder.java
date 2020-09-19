/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.handler.codec.http.HttpContent;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.QueryStringDecoder;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class HttpPostStandardRequestDecoder
/*     */   implements InterfaceHttpPostRequestDecoder
/*     */ {
/*     */   private final HttpDataFactory factory;
/*     */   private final HttpRequest request;
/*     */   private final Charset charset;
/*     */   private boolean isLastChunk;
/*  71 */   private final List<InterfaceHttpData> bodyListHttpData = new ArrayList<InterfaceHttpData>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap<String, List<InterfaceHttpData>>(CaseIgnoringComparator.INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuf undecodedChunk;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int bodyListHttpDataRank;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private HttpPostRequestDecoder.MultiPartStatus currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
/*     */ 
/*     */   
/*     */   private Attribute currentAttribute;
/*     */ 
/*     */   
/*     */   private boolean destroyed;
/*     */ 
/*     */   
/* 101 */   private int discardThreshold = 10485760;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpPostStandardRequestDecoder(HttpRequest request) {
/* 114 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request) {
/* 130 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
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
/*     */   public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) {
/* 148 */     this.request = (HttpRequest)ObjectUtil.checkNotNull(request, "request");
/* 149 */     this.charset = (Charset)ObjectUtil.checkNotNull(charset, "charset");
/* 150 */     this.factory = (HttpDataFactory)ObjectUtil.checkNotNull(factory, "factory");
/* 151 */     if (request instanceof HttpContent) {
/*     */ 
/*     */       
/* 154 */       offer((HttpContent)request);
/*     */     } else {
/* 156 */       this.undecodedChunk = Unpooled.buffer();
/* 157 */       parseBody();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkDestroyed() {
/* 162 */     if (this.destroyed) {
/* 163 */       throw new IllegalStateException(HttpPostStandardRequestDecoder.class.getSimpleName() + " was destroyed already");
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
/*     */   public boolean isMultipart() {
/* 175 */     checkDestroyed();
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDiscardThreshold(int discardThreshold) {
/* 186 */     this.discardThreshold = ObjectUtil.checkPositiveOrZero(discardThreshold, "discardThreshold");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDiscardThreshold() {
/* 194 */     return this.discardThreshold;
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
/*     */   public List<InterfaceHttpData> getBodyHttpDatas() {
/* 209 */     checkDestroyed();
/*     */     
/* 211 */     if (!this.isLastChunk) {
/* 212 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 214 */     return this.bodyListHttpData;
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
/*     */   public List<InterfaceHttpData> getBodyHttpDatas(String name) {
/* 230 */     checkDestroyed();
/*     */     
/* 232 */     if (!this.isLastChunk) {
/* 233 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 235 */     return this.bodyMapHttpData.get(name);
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
/*     */   public InterfaceHttpData getBodyHttpData(String name) {
/* 252 */     checkDestroyed();
/*     */     
/* 254 */     if (!this.isLastChunk) {
/* 255 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*     */     }
/* 257 */     List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
/* 258 */     if (list != null) {
/* 259 */       return list.get(0);
/*     */     }
/* 261 */     return null;
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
/*     */   public HttpPostStandardRequestDecoder offer(HttpContent content) {
/* 275 */     checkDestroyed();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 280 */     ByteBuf buf = content.content();
/* 281 */     if (this.undecodedChunk == null) {
/* 282 */       this.undecodedChunk = buf.copy();
/*     */     } else {
/* 284 */       this.undecodedChunk.writeBytes(buf);
/*     */     } 
/* 286 */     if (content instanceof io.netty.handler.codec.http.LastHttpContent) {
/* 287 */       this.isLastChunk = true;
/*     */     }
/* 289 */     parseBody();
/* 290 */     if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
/* 291 */       this.undecodedChunk.discardReadBytes();
/*     */     }
/* 293 */     return this;
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
/*     */   public boolean hasNext() {
/* 308 */     checkDestroyed();
/*     */     
/* 310 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)
/*     */     {
/* 312 */       if (this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
/* 313 */         throw new HttpPostRequestDecoder.EndOfDataDecoderException();
/*     */       }
/*     */     }
/* 316 */     return (!this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size());
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
/*     */   public InterfaceHttpData next() {
/* 333 */     checkDestroyed();
/*     */     
/* 335 */     if (hasNext()) {
/* 336 */       return this.bodyListHttpData.get(this.bodyListHttpDataRank++);
/*     */     }
/* 338 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData currentPartialHttpData() {
/* 343 */     return this.currentAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseBody() {
/* 354 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
/* 355 */       if (this.isLastChunk) {
/* 356 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/*     */       }
/*     */       return;
/*     */     } 
/* 360 */     parseBodyAttributes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addHttpData(InterfaceHttpData data) {
/* 367 */     if (data == null) {
/*     */       return;
/*     */     }
/* 370 */     List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
/* 371 */     if (datas == null) {
/* 372 */       datas = new ArrayList<InterfaceHttpData>(1);
/* 373 */       this.bodyMapHttpData.put(data.getName(), datas);
/*     */     } 
/* 375 */     datas.add(data);
/* 376 */     this.bodyListHttpData.add(data);
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
/*     */   private void parseBodyAttributesStandard() {
/* 388 */     int firstpos = this.undecodedChunk.readerIndex();
/* 389 */     int currentpos = firstpos;
/*     */ 
/*     */     
/* 392 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
/* 393 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/*     */     }
/* 395 */     boolean contRead = true;
/*     */     try {
/* 397 */       while (this.undecodedChunk.isReadable() && contRead) {
/* 398 */         char read = (char)this.undecodedChunk.readUnsignedByte();
/* 399 */         currentpos++;
/* 400 */         switch (this.currentStatus) {
/*     */           case DISPOSITION:
/* 402 */             if (read == '=') {
/* 403 */               this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/* 404 */               int equalpos = currentpos - 1;
/* 405 */               String key = decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset);
/*     */               
/* 407 */               this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 408 */               firstpos = currentpos; continue;
/* 409 */             }  if (read == '&') {
/* 410 */               this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 411 */               int ampersandpos = currentpos - 1;
/* 412 */               String key = decodeAttribute(this.undecodedChunk
/* 413 */                   .toString(firstpos, ampersandpos - firstpos, this.charset), this.charset);
/* 414 */               this.currentAttribute = this.factory.createAttribute(this.request, key);
/* 415 */               this.currentAttribute.setValue("");
/* 416 */               addHttpData(this.currentAttribute);
/* 417 */               this.currentAttribute = null;
/* 418 */               firstpos = currentpos;
/* 419 */               contRead = true;
/*     */             } 
/*     */             continue;
/*     */           case FIELD:
/* 423 */             if (read == '&') {
/* 424 */               this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
/* 425 */               int ampersandpos = currentpos - 1;
/* 426 */               setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 427 */               firstpos = currentpos;
/* 428 */               contRead = true; continue;
/* 429 */             }  if (read == '\r') {
/* 430 */               if (this.undecodedChunk.isReadable()) {
/* 431 */                 read = (char)this.undecodedChunk.readUnsignedByte();
/* 432 */                 currentpos++;
/* 433 */                 if (read == '\n') {
/* 434 */                   this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 435 */                   int ampersandpos = currentpos - 2;
/* 436 */                   setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 437 */                   firstpos = currentpos;
/* 438 */                   contRead = false;
/*     */                   continue;
/*     */                 } 
/* 441 */                 throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
/*     */               } 
/*     */               
/* 444 */               currentpos--; continue;
/*     */             } 
/* 446 */             if (read == '\n') {
/* 447 */               this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
/* 448 */               int ampersandpos = currentpos - 1;
/* 449 */               setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 450 */               firstpos = currentpos;
/* 451 */               contRead = false;
/*     */             } 
/*     */             continue;
/*     */         } 
/*     */         
/* 456 */         contRead = false;
/*     */       } 
/*     */       
/* 459 */       if (this.isLastChunk && this.currentAttribute != null) {
/*     */         
/* 461 */         int ampersandpos = currentpos;
/* 462 */         if (ampersandpos > firstpos) {
/* 463 */           setFinalBuffer(this.undecodedChunk.copy(firstpos, ampersandpos - firstpos));
/* 464 */         } else if (!this.currentAttribute.isCompleted()) {
/* 465 */           setFinalBuffer(Unpooled.EMPTY_BUFFER);
/*     */         } 
/* 467 */         firstpos = currentpos;
/* 468 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/* 469 */       } else if (contRead && this.currentAttribute != null && this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
/*     */         
/* 471 */         this.currentAttribute.addContent(this.undecodedChunk.copy(firstpos, currentpos - firstpos), false);
/*     */         
/* 473 */         firstpos = currentpos;
/*     */       } 
/* 475 */       this.undecodedChunk.readerIndex(firstpos);
/* 476 */     } catch (ErrorDataDecoderException e) {
/*     */       
/* 478 */       this.undecodedChunk.readerIndex(firstpos);
/* 479 */       throw e;
/* 480 */     } catch (IOException e) {
/*     */       
/* 482 */       this.undecodedChunk.readerIndex(firstpos);
/* 483 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseBodyAttributes() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   4: invokevirtual hasArray : ()Z
/*     */     //   7: ifne -> 15
/*     */     //   10: aload_0
/*     */     //   11: invokespecial parseBodyAttributesStandard : ()V
/*     */     //   14: return
/*     */     //   15: new io/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadOptimize
/*     */     //   18: dup
/*     */     //   19: aload_0
/*     */     //   20: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   23: invokespecial <init> : (Lio/netty/buffer/ByteBuf;)V
/*     */     //   26: astore_1
/*     */     //   27: aload_0
/*     */     //   28: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   31: invokevirtual readerIndex : ()I
/*     */     //   34: istore_2
/*     */     //   35: iload_2
/*     */     //   36: istore_3
/*     */     //   37: aload_0
/*     */     //   38: getfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   41: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.NOTSTARTED : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   44: if_acmpne -> 54
/*     */     //   47: aload_0
/*     */     //   48: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   51: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   54: iconst_1
/*     */     //   55: istore #6
/*     */     //   57: aload_1
/*     */     //   58: getfield pos : I
/*     */     //   61: aload_1
/*     */     //   62: getfield limit : I
/*     */     //   65: if_icmpge -> 522
/*     */     //   68: aload_1
/*     */     //   69: getfield bytes : [B
/*     */     //   72: aload_1
/*     */     //   73: dup
/*     */     //   74: getfield pos : I
/*     */     //   77: dup_x1
/*     */     //   78: iconst_1
/*     */     //   79: iadd
/*     */     //   80: putfield pos : I
/*     */     //   83: baload
/*     */     //   84: sipush #255
/*     */     //   87: iand
/*     */     //   88: i2c
/*     */     //   89: istore #7
/*     */     //   91: iinc #3, 1
/*     */     //   94: getstatic io/netty/handler/codec/http/multipart/HttpPostStandardRequestDecoder$1.$SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus : [I
/*     */     //   97: aload_0
/*     */     //   98: getfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   101: invokevirtual ordinal : ()I
/*     */     //   104: iaload
/*     */     //   105: lookupswitch default -> 508, 1 -> 132, 2 -> 296
/*     */     //   132: iload #7
/*     */     //   134: bipush #61
/*     */     //   136: if_icmpne -> 200
/*     */     //   139: aload_0
/*     */     //   140: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FIELD : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   143: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   146: iload_3
/*     */     //   147: iconst_1
/*     */     //   148: isub
/*     */     //   149: istore #4
/*     */     //   151: aload_0
/*     */     //   152: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   155: iload_2
/*     */     //   156: iload #4
/*     */     //   158: iload_2
/*     */     //   159: isub
/*     */     //   160: aload_0
/*     */     //   161: getfield charset : Ljava/nio/charset/Charset;
/*     */     //   164: invokevirtual toString : (IILjava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   167: aload_0
/*     */     //   168: getfield charset : Ljava/nio/charset/Charset;
/*     */     //   171: invokestatic decodeAttribute : (Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   174: astore #8
/*     */     //   176: aload_0
/*     */     //   177: aload_0
/*     */     //   178: getfield factory : Lio/netty/handler/codec/http/multipart/HttpDataFactory;
/*     */     //   181: aload_0
/*     */     //   182: getfield request : Lio/netty/handler/codec/http/HttpRequest;
/*     */     //   185: aload #8
/*     */     //   187: invokeinterface createAttribute : (Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   192: putfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   195: iload_3
/*     */     //   196: istore_2
/*     */     //   197: goto -> 519
/*     */     //   200: iload #7
/*     */     //   202: bipush #38
/*     */     //   204: if_icmpne -> 519
/*     */     //   207: aload_0
/*     */     //   208: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   211: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   214: iload_3
/*     */     //   215: iconst_1
/*     */     //   216: isub
/*     */     //   217: istore #5
/*     */     //   219: aload_0
/*     */     //   220: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   223: iload_2
/*     */     //   224: iload #5
/*     */     //   226: iload_2
/*     */     //   227: isub
/*     */     //   228: aload_0
/*     */     //   229: getfield charset : Ljava/nio/charset/Charset;
/*     */     //   232: invokevirtual toString : (IILjava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   235: aload_0
/*     */     //   236: getfield charset : Ljava/nio/charset/Charset;
/*     */     //   239: invokestatic decodeAttribute : (Ljava/lang/String;Ljava/nio/charset/Charset;)Ljava/lang/String;
/*     */     //   242: astore #8
/*     */     //   244: aload_0
/*     */     //   245: aload_0
/*     */     //   246: getfield factory : Lio/netty/handler/codec/http/multipart/HttpDataFactory;
/*     */     //   249: aload_0
/*     */     //   250: getfield request : Lio/netty/handler/codec/http/HttpRequest;
/*     */     //   253: aload #8
/*     */     //   255: invokeinterface createAttribute : (Lio/netty/handler/codec/http/HttpRequest;Ljava/lang/String;)Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   260: putfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   263: aload_0
/*     */     //   264: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   267: ldc_w ''
/*     */     //   270: invokeinterface setValue : (Ljava/lang/String;)V
/*     */     //   275: aload_0
/*     */     //   276: aload_0
/*     */     //   277: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   280: invokevirtual addHttpData : (Lio/netty/handler/codec/http/multipart/InterfaceHttpData;)V
/*     */     //   283: aload_0
/*     */     //   284: aconst_null
/*     */     //   285: putfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   288: iload_3
/*     */     //   289: istore_2
/*     */     //   290: iconst_1
/*     */     //   291: istore #6
/*     */     //   293: goto -> 519
/*     */     //   296: iload #7
/*     */     //   298: bipush #38
/*     */     //   300: if_icmpne -> 339
/*     */     //   303: aload_0
/*     */     //   304: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.DISPOSITION : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   307: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   310: iload_3
/*     */     //   311: iconst_1
/*     */     //   312: isub
/*     */     //   313: istore #5
/*     */     //   315: aload_0
/*     */     //   316: aload_0
/*     */     //   317: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   320: iload_2
/*     */     //   321: iload #5
/*     */     //   323: iload_2
/*     */     //   324: isub
/*     */     //   325: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
/*     */     //   328: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
/*     */     //   331: iload_3
/*     */     //   332: istore_2
/*     */     //   333: iconst_1
/*     */     //   334: istore #6
/*     */     //   336: goto -> 519
/*     */     //   339: iload #7
/*     */     //   341: bipush #13
/*     */     //   343: if_icmpne -> 460
/*     */     //   346: aload_1
/*     */     //   347: getfield pos : I
/*     */     //   350: aload_1
/*     */     //   351: getfield limit : I
/*     */     //   354: if_icmpge -> 447
/*     */     //   357: aload_1
/*     */     //   358: getfield bytes : [B
/*     */     //   361: aload_1
/*     */     //   362: dup
/*     */     //   363: getfield pos : I
/*     */     //   366: dup_x1
/*     */     //   367: iconst_1
/*     */     //   368: iadd
/*     */     //   369: putfield pos : I
/*     */     //   372: baload
/*     */     //   373: sipush #255
/*     */     //   376: iand
/*     */     //   377: i2c
/*     */     //   378: istore #7
/*     */     //   380: iinc #3, 1
/*     */     //   383: iload #7
/*     */     //   385: bipush #10
/*     */     //   387: if_icmpne -> 431
/*     */     //   390: aload_0
/*     */     //   391: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.PREEPILOGUE : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   394: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   397: iload_3
/*     */     //   398: iconst_2
/*     */     //   399: isub
/*     */     //   400: istore #5
/*     */     //   402: aload_1
/*     */     //   403: iconst_0
/*     */     //   404: invokevirtual setReadPosition : (I)V
/*     */     //   407: aload_0
/*     */     //   408: aload_0
/*     */     //   409: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   412: iload_2
/*     */     //   413: iload #5
/*     */     //   415: iload_2
/*     */     //   416: isub
/*     */     //   417: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
/*     */     //   420: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
/*     */     //   423: iload_3
/*     */     //   424: istore_2
/*     */     //   425: iconst_0
/*     */     //   426: istore #6
/*     */     //   428: goto -> 522
/*     */     //   431: aload_1
/*     */     //   432: iconst_0
/*     */     //   433: invokevirtual setReadPosition : (I)V
/*     */     //   436: new io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
/*     */     //   439: dup
/*     */     //   440: ldc_w 'Bad end of line'
/*     */     //   443: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   446: athrow
/*     */     //   447: aload_1
/*     */     //   448: getfield limit : I
/*     */     //   451: ifle -> 519
/*     */     //   454: iinc #3, -1
/*     */     //   457: goto -> 519
/*     */     //   460: iload #7
/*     */     //   462: bipush #10
/*     */     //   464: if_icmpne -> 519
/*     */     //   467: aload_0
/*     */     //   468: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.PREEPILOGUE : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   471: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   474: iload_3
/*     */     //   475: iconst_1
/*     */     //   476: isub
/*     */     //   477: istore #5
/*     */     //   479: aload_1
/*     */     //   480: iconst_0
/*     */     //   481: invokevirtual setReadPosition : (I)V
/*     */     //   484: aload_0
/*     */     //   485: aload_0
/*     */     //   486: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   489: iload_2
/*     */     //   490: iload #5
/*     */     //   492: iload_2
/*     */     //   493: isub
/*     */     //   494: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
/*     */     //   497: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
/*     */     //   500: iload_3
/*     */     //   501: istore_2
/*     */     //   502: iconst_0
/*     */     //   503: istore #6
/*     */     //   505: goto -> 522
/*     */     //   508: aload_1
/*     */     //   509: iconst_0
/*     */     //   510: invokevirtual setReadPosition : (I)V
/*     */     //   513: iconst_0
/*     */     //   514: istore #6
/*     */     //   516: goto -> 522
/*     */     //   519: goto -> 57
/*     */     //   522: aload_0
/*     */     //   523: getfield isLastChunk : Z
/*     */     //   526: ifeq -> 595
/*     */     //   529: aload_0
/*     */     //   530: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   533: ifnull -> 595
/*     */     //   536: iload_3
/*     */     //   537: istore #5
/*     */     //   539: iload #5
/*     */     //   541: iload_2
/*     */     //   542: if_icmple -> 564
/*     */     //   545: aload_0
/*     */     //   546: aload_0
/*     */     //   547: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   550: iload_2
/*     */     //   551: iload #5
/*     */     //   553: iload_2
/*     */     //   554: isub
/*     */     //   555: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
/*     */     //   558: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
/*     */     //   561: goto -> 583
/*     */     //   564: aload_0
/*     */     //   565: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   568: invokeinterface isCompleted : ()Z
/*     */     //   573: ifne -> 583
/*     */     //   576: aload_0
/*     */     //   577: getstatic io/netty/buffer/Unpooled.EMPTY_BUFFER : Lio/netty/buffer/ByteBuf;
/*     */     //   580: invokespecial setFinalBuffer : (Lio/netty/buffer/ByteBuf;)V
/*     */     //   583: iload_3
/*     */     //   584: istore_2
/*     */     //   585: aload_0
/*     */     //   586: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.EPILOGUE : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   589: putfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   592: goto -> 640
/*     */     //   595: iload #6
/*     */     //   597: ifeq -> 640
/*     */     //   600: aload_0
/*     */     //   601: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   604: ifnull -> 640
/*     */     //   607: aload_0
/*     */     //   608: getfield currentStatus : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   611: getstatic io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus.FIELD : Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$MultiPartStatus;
/*     */     //   614: if_acmpne -> 640
/*     */     //   617: aload_0
/*     */     //   618: getfield currentAttribute : Lio/netty/handler/codec/http/multipart/Attribute;
/*     */     //   621: aload_0
/*     */     //   622: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   625: iload_2
/*     */     //   626: iload_3
/*     */     //   627: iload_2
/*     */     //   628: isub
/*     */     //   629: invokevirtual copy : (II)Lio/netty/buffer/ByteBuf;
/*     */     //   632: iconst_0
/*     */     //   633: invokeinterface addContent : (Lio/netty/buffer/ByteBuf;Z)V
/*     */     //   638: iload_3
/*     */     //   639: istore_2
/*     */     //   640: aload_0
/*     */     //   641: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   644: iload_2
/*     */     //   645: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
/*     */     //   648: pop
/*     */     //   649: goto -> 708
/*     */     //   652: astore #7
/*     */     //   654: aload_0
/*     */     //   655: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   658: iload_2
/*     */     //   659: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
/*     */     //   662: pop
/*     */     //   663: aload #7
/*     */     //   665: athrow
/*     */     //   666: astore #7
/*     */     //   668: aload_0
/*     */     //   669: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   672: iload_2
/*     */     //   673: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
/*     */     //   676: pop
/*     */     //   677: new io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
/*     */     //   680: dup
/*     */     //   681: aload #7
/*     */     //   683: invokespecial <init> : (Ljava/lang/Throwable;)V
/*     */     //   686: athrow
/*     */     //   687: astore #7
/*     */     //   689: aload_0
/*     */     //   690: getfield undecodedChunk : Lio/netty/buffer/ByteBuf;
/*     */     //   693: iload_2
/*     */     //   694: invokevirtual readerIndex : (I)Lio/netty/buffer/ByteBuf;
/*     */     //   697: pop
/*     */     //   698: new io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
/*     */     //   701: dup
/*     */     //   702: aload #7
/*     */     //   704: invokespecial <init> : (Ljava/lang/Throwable;)V
/*     */     //   707: athrow
/*     */     //   708: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #496	-> 0
/*     */     //   #497	-> 10
/*     */     //   #498	-> 14
/*     */     //   #500	-> 15
/*     */     //   #501	-> 27
/*     */     //   #502	-> 35
/*     */     //   #505	-> 37
/*     */     //   #506	-> 47
/*     */     //   #508	-> 54
/*     */     //   #510	-> 57
/*     */     //   #511	-> 68
/*     */     //   #512	-> 91
/*     */     //   #513	-> 94
/*     */     //   #515	-> 132
/*     */     //   #516	-> 139
/*     */     //   #517	-> 146
/*     */     //   #518	-> 151
/*     */     //   #520	-> 176
/*     */     //   #521	-> 195
/*     */     //   #522	-> 197
/*     */     //   #523	-> 207
/*     */     //   #524	-> 214
/*     */     //   #525	-> 219
/*     */     //   #526	-> 232
/*     */     //   #525	-> 239
/*     */     //   #527	-> 244
/*     */     //   #528	-> 263
/*     */     //   #529	-> 275
/*     */     //   #530	-> 283
/*     */     //   #531	-> 288
/*     */     //   #532	-> 290
/*     */     //   #533	-> 293
/*     */     //   #536	-> 296
/*     */     //   #537	-> 303
/*     */     //   #538	-> 310
/*     */     //   #539	-> 315
/*     */     //   #540	-> 331
/*     */     //   #541	-> 333
/*     */     //   #542	-> 339
/*     */     //   #543	-> 346
/*     */     //   #544	-> 357
/*     */     //   #545	-> 380
/*     */     //   #546	-> 383
/*     */     //   #547	-> 390
/*     */     //   #548	-> 397
/*     */     //   #549	-> 402
/*     */     //   #550	-> 407
/*     */     //   #551	-> 423
/*     */     //   #552	-> 425
/*     */     //   #553	-> 428
/*     */     //   #556	-> 431
/*     */     //   #557	-> 436
/*     */     //   #560	-> 447
/*     */     //   #561	-> 454
/*     */     //   #564	-> 460
/*     */     //   #565	-> 467
/*     */     //   #566	-> 474
/*     */     //   #567	-> 479
/*     */     //   #568	-> 484
/*     */     //   #569	-> 500
/*     */     //   #570	-> 502
/*     */     //   #571	-> 505
/*     */     //   #576	-> 508
/*     */     //   #577	-> 513
/*     */     //   #578	-> 516
/*     */     //   #580	-> 519
/*     */     //   #581	-> 522
/*     */     //   #583	-> 536
/*     */     //   #584	-> 539
/*     */     //   #585	-> 545
/*     */     //   #586	-> 564
/*     */     //   #587	-> 576
/*     */     //   #589	-> 583
/*     */     //   #590	-> 585
/*     */     //   #591	-> 595
/*     */     //   #593	-> 617
/*     */     //   #595	-> 638
/*     */     //   #597	-> 640
/*     */     //   #610	-> 649
/*     */     //   #598	-> 652
/*     */     //   #600	-> 654
/*     */     //   #601	-> 663
/*     */     //   #602	-> 666
/*     */     //   #604	-> 668
/*     */     //   #605	-> 677
/*     */     //   #606	-> 687
/*     */     //   #608	-> 689
/*     */     //   #609	-> 698
/*     */     //   #611	-> 708
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   176	21	8	key	Ljava/lang/String;
/*     */     //   151	49	4	equalpos	I
/*     */     //   244	49	8	key	Ljava/lang/String;
/*     */     //   219	77	5	ampersandpos	I
/*     */     //   315	24	5	ampersandpos	I
/*     */     //   402	29	5	ampersandpos	I
/*     */     //   479	29	5	ampersandpos	I
/*     */     //   91	428	7	read	C
/*     */     //   539	56	5	ampersandpos	I
/*     */     //   654	12	7	e	Lio/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException;
/*     */     //   668	19	7	e	Ljava/io/IOException;
/*     */     //   689	19	7	e	Ljava/lang/IllegalArgumentException;
/*     */     //   0	709	0	this	Lio/netty/handler/codec/http/multipart/HttpPostStandardRequestDecoder;
/*     */     //   27	682	1	sao	Lio/netty/handler/codec/http/multipart/HttpPostBodyUtil$SeekAheadOptimize;
/*     */     //   35	674	2	firstpos	I
/*     */     //   37	672	3	currentpos	I
/*     */     //   57	652	6	contRead	Z
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   57	649	652	io/netty/handler/codec/http/multipart/HttpPostRequestDecoder$ErrorDataDecoderException
/*     */     //   57	649	666	java/io/IOException
/*     */     //   57	649	687	java/lang/IllegalArgumentException
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setFinalBuffer(ByteBuf buffer) throws IOException {
/* 614 */     this.currentAttribute.addContent(buffer, true);
/* 615 */     String value = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
/* 616 */     this.currentAttribute.setValue(value);
/* 617 */     addHttpData(this.currentAttribute);
/* 618 */     this.currentAttribute = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String decodeAttribute(String s, Charset charset) {
/*     */     try {
/* 628 */       return QueryStringDecoder.decodeComponent(s, charset);
/* 629 */     } catch (IllegalArgumentException e) {
/* 630 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad string: '" + s + '\'', e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 641 */     cleanFiles();
/*     */     
/* 643 */     this.destroyed = true;
/*     */     
/* 645 */     if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
/* 646 */       this.undecodedChunk.release();
/* 647 */       this.undecodedChunk = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanFiles() {
/* 656 */     checkDestroyed();
/*     */     
/* 658 */     this.factory.cleanRequestHttpData(this.request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHttpDataFromClean(InterfaceHttpData data) {
/* 666 */     checkDestroyed();
/*     */     
/* 668 */     this.factory.removeHttpDataFromClean(this.request, data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\HttpPostStandardRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */