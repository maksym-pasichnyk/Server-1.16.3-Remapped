/*      */ package io.netty.handler.codec.http.multipart;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.handler.codec.http.HttpConstants;
/*      */ import io.netty.handler.codec.http.HttpContent;
/*      */ import io.netty.handler.codec.http.HttpHeaderNames;
/*      */ import io.netty.handler.codec.http.HttpHeaderValues;
/*      */ import io.netty.handler.codec.http.HttpRequest;
/*      */ import io.netty.handler.codec.http.QueryStringDecoder;
/*      */ import io.netty.util.CharsetUtil;
/*      */ import io.netty.util.internal.InternalThreadLocalMap;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import java.io.IOException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
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
/*      */ public class HttpPostMultipartRequestDecoder
/*      */   implements InterfaceHttpPostRequestDecoder
/*      */ {
/*      */   private final HttpDataFactory factory;
/*      */   private final HttpRequest request;
/*      */   private Charset charset;
/*      */   private boolean isLastChunk;
/*   78 */   private final List<InterfaceHttpData> bodyListHttpData = new ArrayList<InterfaceHttpData>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   83 */   private final Map<String, List<InterfaceHttpData>> bodyMapHttpData = new TreeMap<String, List<InterfaceHttpData>>(CaseIgnoringComparator.INSTANCE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteBuf undecodedChunk;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int bodyListHttpDataRank;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String multipartDataBoundary;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String multipartMixedBoundary;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   private HttpPostRequestDecoder.MultiPartStatus currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<CharSequence, Attribute> currentFieldAttributes;
/*      */ 
/*      */ 
/*      */   
/*      */   private FileUpload currentFileUpload;
/*      */ 
/*      */ 
/*      */   
/*      */   private Attribute currentAttribute;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean destroyed;
/*      */ 
/*      */   
/*  129 */   private int discardThreshold = 10485760;
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
/*      */   public HttpPostMultipartRequestDecoder(HttpRequest request) {
/*  142 */     this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
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
/*      */   public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request) {
/*  158 */     this(factory, request, HttpConstants.DEFAULT_CHARSET);
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
/*      */   public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) {
/*  176 */     this.request = (HttpRequest)ObjectUtil.checkNotNull(request, "request");
/*  177 */     this.charset = (Charset)ObjectUtil.checkNotNull(charset, "charset");
/*  178 */     this.factory = (HttpDataFactory)ObjectUtil.checkNotNull(factory, "factory");
/*      */ 
/*      */     
/*  181 */     setMultipart(this.request.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE));
/*  182 */     if (request instanceof HttpContent) {
/*      */ 
/*      */       
/*  185 */       offer((HttpContent)request);
/*      */     } else {
/*  187 */       this.undecodedChunk = Unpooled.buffer();
/*  188 */       parseBody();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setMultipart(String contentType) {
/*  196 */     String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentType);
/*  197 */     if (dataBoundary != null) {
/*  198 */       this.multipartDataBoundary = dataBoundary[0];
/*  199 */       if (dataBoundary.length > 1 && dataBoundary[1] != null) {
/*  200 */         this.charset = Charset.forName(dataBoundary[1]);
/*      */       }
/*      */     } else {
/*  203 */       this.multipartDataBoundary = null;
/*      */     } 
/*  205 */     this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
/*      */   }
/*      */   
/*      */   private void checkDestroyed() {
/*  209 */     if (this.destroyed) {
/*  210 */       throw new IllegalStateException(HttpPostMultipartRequestDecoder.class.getSimpleName() + " was destroyed already");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMultipart() {
/*  222 */     checkDestroyed();
/*  223 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDiscardThreshold(int discardThreshold) {
/*  233 */     this.discardThreshold = ObjectUtil.checkPositiveOrZero(discardThreshold, "discardThreshold");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDiscardThreshold() {
/*  241 */     return this.discardThreshold;
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
/*      */   public List<InterfaceHttpData> getBodyHttpDatas() {
/*  256 */     checkDestroyed();
/*      */     
/*  258 */     if (!this.isLastChunk) {
/*  259 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */     }
/*  261 */     return this.bodyListHttpData;
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
/*      */   public List<InterfaceHttpData> getBodyHttpDatas(String name) {
/*  277 */     checkDestroyed();
/*      */     
/*  279 */     if (!this.isLastChunk) {
/*  280 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */     }
/*  282 */     return this.bodyMapHttpData.get(name);
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
/*      */   public InterfaceHttpData getBodyHttpData(String name) {
/*  299 */     checkDestroyed();
/*      */     
/*  301 */     if (!this.isLastChunk) {
/*  302 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */     }
/*  304 */     List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
/*  305 */     if (list != null) {
/*  306 */       return list.get(0);
/*      */     }
/*  308 */     return null;
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
/*      */   public HttpPostMultipartRequestDecoder offer(HttpContent content) {
/*  322 */     checkDestroyed();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  327 */     ByteBuf buf = content.content();
/*  328 */     if (this.undecodedChunk == null) {
/*  329 */       this.undecodedChunk = buf.copy();
/*      */     } else {
/*  331 */       this.undecodedChunk.writeBytes(buf);
/*      */     } 
/*  333 */     if (content instanceof io.netty.handler.codec.http.LastHttpContent) {
/*  334 */       this.isLastChunk = true;
/*      */     }
/*  336 */     parseBody();
/*  337 */     if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
/*  338 */       this.undecodedChunk.discardReadBytes();
/*      */     }
/*  340 */     return this;
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
/*      */   public boolean hasNext() {
/*  355 */     checkDestroyed();
/*      */     
/*  357 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE)
/*      */     {
/*  359 */       if (this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
/*  360 */         throw new HttpPostRequestDecoder.EndOfDataDecoderException();
/*      */       }
/*      */     }
/*  363 */     return (!this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size());
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
/*      */   public InterfaceHttpData next() {
/*  380 */     checkDestroyed();
/*      */     
/*  382 */     if (hasNext()) {
/*  383 */       return this.bodyListHttpData.get(this.bodyListHttpDataRank++);
/*      */     }
/*  385 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public InterfaceHttpData currentPartialHttpData() {
/*  390 */     if (this.currentFileUpload != null) {
/*  391 */       return this.currentFileUpload;
/*      */     }
/*  393 */     return this.currentAttribute;
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
/*      */   private void parseBody() {
/*  405 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
/*  406 */       if (this.isLastChunk) {
/*  407 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
/*      */       }
/*      */       return;
/*      */     } 
/*  411 */     parseBodyMultipart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addHttpData(InterfaceHttpData data) {
/*  418 */     if (data == null) {
/*      */       return;
/*      */     }
/*  421 */     List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
/*  422 */     if (datas == null) {
/*  423 */       datas = new ArrayList<InterfaceHttpData>(1);
/*  424 */       this.bodyMapHttpData.put(data.getName(), datas);
/*      */     } 
/*  426 */     datas.add(data);
/*  427 */     this.bodyListHttpData.add(data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseBodyMultipart() {
/*  438 */     if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  442 */     InterfaceHttpData data = decodeMultipart(this.currentStatus);
/*  443 */     while (data != null) {
/*  444 */       addHttpData(data);
/*  445 */       if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
/*      */         break;
/*      */       }
/*  448 */       data = decodeMultipart(this.currentStatus);
/*      */     } 
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
/*      */   private InterfaceHttpData decodeMultipart(HttpPostRequestDecoder.MultiPartStatus state) {
/*      */     Charset localCharset;
/*      */     Attribute charsetAttribute;
/*      */     Attribute nameAttribute;
/*      */     Attribute finalAttribute;
/*  469 */     switch (state) {
/*      */       case NOTSTARTED:
/*  471 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
/*      */       
/*      */       case PREAMBLE:
/*  474 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
/*      */       
/*      */       case HEADERDELIMITER:
/*  477 */         return findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
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
/*      */       case DISPOSITION:
/*  490 */         return findMultipartDisposition();
/*      */ 
/*      */       
/*      */       case FIELD:
/*  494 */         localCharset = null;
/*  495 */         charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
/*  496 */         if (charsetAttribute != null) {
/*      */           try {
/*  498 */             localCharset = Charset.forName(charsetAttribute.getValue());
/*  499 */           } catch (IOException e) {
/*  500 */             throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  501 */           } catch (UnsupportedCharsetException e) {
/*  502 */             throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */           } 
/*      */         }
/*  505 */         nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
/*  506 */         if (this.currentAttribute == null) {
/*      */           long size;
/*  508 */           Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
/*      */           
/*      */           try {
/*  511 */             size = (lengthAttribute != null) ? Long.parseLong(lengthAttribute
/*  512 */                 .getValue()) : 0L;
/*  513 */           } catch (IOException e) {
/*  514 */             throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  515 */           } catch (NumberFormatException ignored) {
/*  516 */             size = 0L;
/*      */           } 
/*      */           try {
/*  519 */             if (size > 0L) {
/*  520 */               this.currentAttribute = this.factory.createAttribute(this.request, 
/*  521 */                   cleanString(nameAttribute.getValue()), size);
/*      */             } else {
/*  523 */               this.currentAttribute = this.factory.createAttribute(this.request, 
/*  524 */                   cleanString(nameAttribute.getValue()));
/*      */             } 
/*  526 */           } catch (NullPointerException e) {
/*  527 */             throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  528 */           } catch (IllegalArgumentException e) {
/*  529 */             throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  530 */           } catch (IOException e) {
/*  531 */             throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */           } 
/*  533 */           if (localCharset != null) {
/*  534 */             this.currentAttribute.setCharset(localCharset);
/*      */           }
/*      */         } 
/*      */         
/*  538 */         if (!loadDataMultipart(this.undecodedChunk, this.multipartDataBoundary, this.currentAttribute))
/*      */         {
/*  540 */           return null;
/*      */         }
/*  542 */         finalAttribute = this.currentAttribute;
/*  543 */         this.currentAttribute = null;
/*  544 */         this.currentFieldAttributes = null;
/*      */         
/*  546 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
/*  547 */         return finalAttribute;
/*      */ 
/*      */       
/*      */       case FILEUPLOAD:
/*  551 */         return getFileUpload(this.multipartDataBoundary);
/*      */ 
/*      */ 
/*      */       
/*      */       case MIXEDDELIMITER:
/*  556 */         return findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
/*      */ 
/*      */       
/*      */       case MIXEDDISPOSITION:
/*  560 */         return findMultipartDisposition();
/*      */ 
/*      */       
/*      */       case MIXEDFILEUPLOAD:
/*  564 */         return getFileUpload(this.multipartMixedBoundary);
/*      */       
/*      */       case PREEPILOGUE:
/*  567 */         return null;
/*      */       case EPILOGUE:
/*  569 */         return null;
/*      */     } 
/*  571 */     throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn't reach here.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void skipControlCharacters(ByteBuf undecodedChunk) {
/*  581 */     if (!undecodedChunk.hasArray()) {
/*      */       try {
/*  583 */         skipControlCharactersStandard(undecodedChunk);
/*  584 */       } catch (IndexOutOfBoundsException e1) {
/*  585 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e1);
/*      */       } 
/*      */       return;
/*      */     } 
/*  589 */     HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
/*  590 */     while (sao.pos < sao.limit) {
/*  591 */       char c = (char)(sao.bytes[sao.pos++] & 0xFF);
/*  592 */       if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
/*  593 */         sao.setReadPosition(1);
/*      */         return;
/*      */       } 
/*      */     } 
/*  597 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
/*      */   }
/*      */   
/*      */   private static void skipControlCharactersStandard(ByteBuf undecodedChunk) {
/*      */     while (true) {
/*  602 */       char c = (char)undecodedChunk.readUnsignedByte();
/*  603 */       if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
/*  604 */         undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
/*      */         return;
/*      */       } 
/*      */     } 
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
/*      */   private InterfaceHttpData findMultipartDelimiter(String delimiter, HttpPostRequestDecoder.MultiPartStatus dispositionStatus, HttpPostRequestDecoder.MultiPartStatus closeDelimiterStatus) {
/*      */     String newline;
/*  625 */     int readerIndex = this.undecodedChunk.readerIndex();
/*      */     try {
/*  627 */       skipControlCharacters(this.undecodedChunk);
/*  628 */     } catch (NotEnoughDataDecoderException ignored) {
/*  629 */       this.undecodedChunk.readerIndex(readerIndex);
/*  630 */       return null;
/*      */     } 
/*  632 */     skipOneLine();
/*      */     
/*      */     try {
/*  635 */       newline = readDelimiter(this.undecodedChunk, delimiter);
/*  636 */     } catch (NotEnoughDataDecoderException ignored) {
/*  637 */       this.undecodedChunk.readerIndex(readerIndex);
/*  638 */       return null;
/*      */     } 
/*  640 */     if (newline.equals(delimiter)) {
/*  641 */       this.currentStatus = dispositionStatus;
/*  642 */       return decodeMultipart(dispositionStatus);
/*      */     } 
/*  644 */     if (newline.equals(delimiter + "--")) {
/*      */       
/*  646 */       this.currentStatus = closeDelimiterStatus;
/*  647 */       if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER) {
/*      */ 
/*      */         
/*  650 */         this.currentFieldAttributes = null;
/*  651 */         return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
/*      */       } 
/*  653 */       return null;
/*      */     } 
/*  655 */     this.undecodedChunk.readerIndex(readerIndex);
/*  656 */     throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InterfaceHttpData findMultipartDisposition() {
/*  666 */     int readerIndex = this.undecodedChunk.readerIndex();
/*  667 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  668 */       this.currentFieldAttributes = new TreeMap<CharSequence, Attribute>(CaseIgnoringComparator.INSTANCE);
/*      */     }
/*      */     
/*  671 */     while (!skipOneLine()) {
/*      */       String newline;
/*      */       try {
/*  674 */         skipControlCharacters(this.undecodedChunk);
/*  675 */         newline = readLine(this.undecodedChunk, this.charset);
/*  676 */       } catch (NotEnoughDataDecoderException ignored) {
/*  677 */         this.undecodedChunk.readerIndex(readerIndex);
/*  678 */         return null;
/*      */       } 
/*  680 */       String[] contents = splitMultipartHeader(newline);
/*  681 */       if (HttpHeaderNames.CONTENT_DISPOSITION.contentEqualsIgnoreCase(contents[0])) {
/*      */         boolean checkSecondArg;
/*  683 */         if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  684 */           checkSecondArg = HttpHeaderValues.FORM_DATA.contentEqualsIgnoreCase(contents[1]);
/*      */         } else {
/*      */           
/*  687 */           checkSecondArg = (HttpHeaderValues.ATTACHMENT.contentEqualsIgnoreCase(contents[1]) || HttpHeaderValues.FILE.contentEqualsIgnoreCase(contents[1]));
/*      */         } 
/*  689 */         if (checkSecondArg)
/*      */         {
/*  691 */           for (int i = 2; i < contents.length; i++) {
/*  692 */             Attribute attribute; String[] values = contents[i].split("=", 2);
/*      */             
/*      */             try {
/*  695 */               attribute = getContentDispositionAttribute(values);
/*  696 */             } catch (NullPointerException e) {
/*  697 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  698 */             } catch (IllegalArgumentException e) {
/*  699 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             } 
/*  701 */             this.currentFieldAttributes.put(attribute.getName(), attribute);
/*      */           }  }  continue;
/*      */       } 
/*  704 */       if (HttpHeaderNames.CONTENT_TRANSFER_ENCODING.contentEqualsIgnoreCase(contents[0])) {
/*      */         Attribute attribute;
/*      */         try {
/*  707 */           attribute = this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString(), 
/*  708 */               cleanString(contents[1]));
/*  709 */         } catch (NullPointerException e) {
/*  710 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  711 */         } catch (IllegalArgumentException e) {
/*  712 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         } 
/*      */         
/*  715 */         this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_TRANSFER_ENCODING, attribute); continue;
/*  716 */       }  if (HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(contents[0])) {
/*      */         Attribute attribute;
/*      */         try {
/*  719 */           attribute = this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_LENGTH.toString(), 
/*  720 */               cleanString(contents[1]));
/*  721 */         } catch (NullPointerException e) {
/*  722 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  723 */         } catch (IllegalArgumentException e) {
/*  724 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */         } 
/*      */         
/*  727 */         this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_LENGTH, attribute); continue;
/*  728 */       }  if (HttpHeaderNames.CONTENT_TYPE.contentEqualsIgnoreCase(contents[0])) {
/*      */         
/*  730 */         if (HttpHeaderValues.MULTIPART_MIXED.contentEqualsIgnoreCase(contents[1])) {
/*  731 */           if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  732 */             String values = StringUtil.substringAfter(contents[2], '=');
/*  733 */             this.multipartMixedBoundary = "--" + values;
/*  734 */             this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
/*  735 */             return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER);
/*      */           } 
/*  737 */           throw new HttpPostRequestDecoder.ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
/*      */         } 
/*      */         
/*  740 */         for (int i = 1; i < contents.length; i++) {
/*  741 */           String charsetHeader = HttpHeaderValues.CHARSET.toString();
/*  742 */           if (contents[i].regionMatches(true, 0, charsetHeader, 0, charsetHeader.length())) {
/*  743 */             Attribute attribute; String values = StringUtil.substringAfter(contents[i], '=');
/*      */             
/*      */             try {
/*  746 */               attribute = this.factory.createAttribute(this.request, charsetHeader, cleanString(values));
/*  747 */             } catch (NullPointerException e) {
/*  748 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  749 */             } catch (IllegalArgumentException e) {
/*  750 */               throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */             } 
/*  752 */             this.currentFieldAttributes.put(HttpHeaderValues.CHARSET, attribute);
/*      */           } else {
/*      */             Attribute attribute;
/*      */             try {
/*  756 */               attribute = this.factory.createAttribute(this.request, 
/*  757 */                   cleanString(contents[0]), contents[i]);
/*  758 */             } catch (NullPointerException e) {
/*  759 */               Attribute attribute1; throw new HttpPostRequestDecoder.ErrorDataDecoderException(attribute1);
/*  760 */             } catch (IllegalArgumentException e) {
/*  761 */               Attribute attribute1; throw new HttpPostRequestDecoder.ErrorDataDecoderException(attribute1);
/*      */             } 
/*  763 */             this.currentFieldAttributes.put(attribute.getName(), attribute);
/*      */           } 
/*      */         } 
/*      */         continue;
/*      */       } 
/*  768 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException("Unknown Params: " + newline);
/*      */     } 
/*      */ 
/*      */     
/*  772 */     Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
/*  773 */     if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
/*  774 */       if (filenameAttribute != null) {
/*      */         
/*  776 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD;
/*      */         
/*  778 */         return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD);
/*      */       } 
/*      */       
/*  781 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
/*      */       
/*  783 */       return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FIELD);
/*      */     } 
/*      */     
/*  786 */     if (filenameAttribute != null) {
/*      */       
/*  788 */       this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD;
/*      */       
/*  790 */       return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD);
/*      */     } 
/*      */     
/*  793 */     throw new HttpPostRequestDecoder.ErrorDataDecoderException("Filename not found");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  798 */   private static final String FILENAME_ENCODED = HttpHeaderValues.FILENAME.toString() + '*';
/*      */   
/*      */   private Attribute getContentDispositionAttribute(String... values) {
/*  801 */     String name = cleanString(values[0]);
/*  802 */     String value = values[1];
/*      */ 
/*      */     
/*  805 */     if (HttpHeaderValues.FILENAME.contentEquals(name)) {
/*      */       
/*  807 */       int last = value.length() - 1;
/*  808 */       if (last > 0 && value
/*  809 */         .charAt(0) == '"' && value
/*  810 */         .charAt(last) == '"') {
/*  811 */         value = value.substring(1, last);
/*      */       }
/*  813 */     } else if (FILENAME_ENCODED.equals(name)) {
/*      */       try {
/*  815 */         name = HttpHeaderValues.FILENAME.toString();
/*  816 */         String[] split = value.split("'", 3);
/*  817 */         value = QueryStringDecoder.decodeComponent(split[2], Charset.forName(split[0]));
/*  818 */       } catch (ArrayIndexOutOfBoundsException e) {
/*  819 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  820 */       } catch (UnsupportedCharsetException e) {
/*  821 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } 
/*      */     } else {
/*      */       
/*  825 */       value = cleanString(value);
/*      */     } 
/*  827 */     return this.factory.createAttribute(this.request, name, value);
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
/*      */   protected InterfaceHttpData getFileUpload(String delimiter) {
/*  841 */     Attribute encoding = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
/*  842 */     Charset localCharset = this.charset;
/*      */     
/*  844 */     HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
/*  845 */     if (encoding != null) {
/*      */       String code;
/*      */       try {
/*  848 */         code = encoding.getValue().toLowerCase();
/*  849 */       } catch (IOException e) {
/*  850 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } 
/*  852 */       if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
/*  853 */         localCharset = CharsetUtil.US_ASCII;
/*  854 */       } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
/*  855 */         localCharset = CharsetUtil.ISO_8859_1;
/*  856 */         mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
/*  857 */       } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
/*      */         
/*  859 */         mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
/*      */       } else {
/*  861 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + code);
/*      */       } 
/*      */     } 
/*  864 */     Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
/*  865 */     if (charsetAttribute != null) {
/*      */       try {
/*  867 */         localCharset = Charset.forName(charsetAttribute.getValue());
/*  868 */       } catch (IOException e) {
/*  869 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  870 */       } catch (UnsupportedCharsetException e) {
/*  871 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } 
/*      */     }
/*  874 */     if (this.currentFileUpload == null) {
/*  875 */       long size; Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
/*  876 */       Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
/*  877 */       Attribute contentTypeAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TYPE);
/*  878 */       Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
/*      */       
/*      */       try {
/*  881 */         size = (lengthAttribute != null) ? Long.parseLong(lengthAttribute.getValue()) : 0L;
/*  882 */       } catch (IOException e) {
/*  883 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  884 */       } catch (NumberFormatException ignored) {
/*  885 */         size = 0L;
/*      */       } 
/*      */       try {
/*      */         String contentType;
/*  889 */         if (contentTypeAttribute != null) {
/*  890 */           contentType = contentTypeAttribute.getValue();
/*      */         } else {
/*  892 */           contentType = "application/octet-stream";
/*      */         } 
/*  894 */         this.currentFileUpload = this.factory.createFileUpload(this.request, 
/*  895 */             cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentType, mechanism
/*  896 */             .value(), localCharset, size);
/*      */       }
/*  898 */       catch (NullPointerException e) {
/*  899 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  900 */       } catch (IllegalArgumentException e) {
/*  901 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*  902 */       } catch (IOException e) {
/*  903 */         throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */       } 
/*      */     } 
/*      */     
/*  907 */     if (!loadDataMultipart(this.undecodedChunk, delimiter, this.currentFileUpload))
/*      */     {
/*  909 */       return null;
/*      */     }
/*  911 */     if (this.currentFileUpload.isCompleted()) {
/*      */       
/*  913 */       if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
/*  914 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
/*  915 */         this.currentFieldAttributes = null;
/*      */       } else {
/*  917 */         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
/*  918 */         cleanMixedAttributes();
/*      */       } 
/*  920 */       FileUpload fileUpload = this.currentFileUpload;
/*  921 */       this.currentFileUpload = null;
/*  922 */       return fileUpload;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  927 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  936 */     checkDestroyed();
/*  937 */     cleanFiles();
/*  938 */     this.destroyed = true;
/*      */     
/*  940 */     if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
/*  941 */       this.undecodedChunk.release();
/*  942 */       this.undecodedChunk = null;
/*      */     } 
/*      */ 
/*      */     
/*  946 */     for (int i = this.bodyListHttpDataRank; i < this.bodyListHttpData.size(); i++) {
/*  947 */       ((InterfaceHttpData)this.bodyListHttpData.get(i)).release();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cleanFiles() {
/*  956 */     checkDestroyed();
/*      */     
/*  958 */     this.factory.cleanRequestHttpData(this.request);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeHttpDataFromClean(InterfaceHttpData data) {
/*  966 */     checkDestroyed();
/*      */     
/*  968 */     this.factory.removeHttpDataFromClean(this.request, data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void cleanMixedAttributes() {
/*  976 */     this.currentFieldAttributes.remove(HttpHeaderValues.CHARSET);
/*  977 */     this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_LENGTH);
/*  978 */     this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
/*  979 */     this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TYPE);
/*  980 */     this.currentFieldAttributes.remove(HttpHeaderValues.FILENAME);
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
/*      */   private static String readLineStandard(ByteBuf undecodedChunk, Charset charset) {
/*  992 */     int readerIndex = undecodedChunk.readerIndex();
/*      */     try {
/*  994 */       ByteBuf line = Unpooled.buffer(64);
/*      */       
/*  996 */       while (undecodedChunk.isReadable()) {
/*  997 */         byte nextByte = undecodedChunk.readByte();
/*  998 */         if (nextByte == 13) {
/*      */           
/* 1000 */           nextByte = undecodedChunk.getByte(undecodedChunk.readerIndex());
/* 1001 */           if (nextByte == 10) {
/*      */             
/* 1003 */             undecodedChunk.readByte();
/* 1004 */             return line.toString(charset);
/*      */           } 
/*      */           
/* 1007 */           line.writeByte(13); continue;
/*      */         } 
/* 1009 */         if (nextByte == 10) {
/* 1010 */           return line.toString(charset);
/*      */         }
/* 1012 */         line.writeByte(nextByte);
/*      */       }
/*      */     
/* 1015 */     } catch (IndexOutOfBoundsException e) {
/* 1016 */       undecodedChunk.readerIndex(readerIndex);
/* 1017 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     } 
/* 1019 */     undecodedChunk.readerIndex(readerIndex);
/* 1020 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
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
/*      */   private static String readLine(ByteBuf undecodedChunk, Charset charset) {
/* 1032 */     if (!undecodedChunk.hasArray()) {
/* 1033 */       return readLineStandard(undecodedChunk, charset);
/*      */     }
/* 1035 */     HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
/* 1036 */     int readerIndex = undecodedChunk.readerIndex();
/*      */     try {
/* 1038 */       ByteBuf line = Unpooled.buffer(64);
/*      */       
/* 1040 */       while (sao.pos < sao.limit) {
/* 1041 */         byte nextByte = sao.bytes[sao.pos++];
/* 1042 */         if (nextByte == 13) {
/* 1043 */           if (sao.pos < sao.limit) {
/* 1044 */             nextByte = sao.bytes[sao.pos++];
/* 1045 */             if (nextByte == 10) {
/* 1046 */               sao.setReadPosition(0);
/* 1047 */               return line.toString(charset);
/*      */             } 
/*      */             
/* 1050 */             sao.pos--;
/* 1051 */             line.writeByte(13);
/*      */             continue;
/*      */           } 
/* 1054 */           line.writeByte(nextByte); continue;
/*      */         } 
/* 1056 */         if (nextByte == 10) {
/* 1057 */           sao.setReadPosition(0);
/* 1058 */           return line.toString(charset);
/*      */         } 
/* 1060 */         line.writeByte(nextByte);
/*      */       }
/*      */     
/* 1063 */     } catch (IndexOutOfBoundsException e) {
/* 1064 */       undecodedChunk.readerIndex(readerIndex);
/* 1065 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     } 
/* 1067 */     undecodedChunk.readerIndex(readerIndex);
/* 1068 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
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
/*      */   private static String readDelimiterStandard(ByteBuf undecodedChunk, String delimiter) {
/* 1087 */     int readerIndex = undecodedChunk.readerIndex();
/*      */     try {
/* 1089 */       StringBuilder sb = new StringBuilder(64);
/* 1090 */       int delimiterPos = 0;
/* 1091 */       int len = delimiter.length();
/* 1092 */       while (undecodedChunk.isReadable() && delimiterPos < len) {
/* 1093 */         byte nextByte = undecodedChunk.readByte();
/* 1094 */         if (nextByte == delimiter.charAt(delimiterPos)) {
/* 1095 */           delimiterPos++;
/* 1096 */           sb.append((char)nextByte);
/*      */           continue;
/*      */         } 
/* 1099 */         undecodedChunk.readerIndex(readerIndex);
/* 1100 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */       } 
/*      */ 
/*      */       
/* 1104 */       if (undecodedChunk.isReadable()) {
/* 1105 */         byte nextByte = undecodedChunk.readByte();
/*      */         
/* 1107 */         if (nextByte == 13) {
/* 1108 */           nextByte = undecodedChunk.readByte();
/* 1109 */           if (nextByte == 10) {
/* 1110 */             return sb.toString();
/*      */           }
/*      */ 
/*      */           
/* 1114 */           undecodedChunk.readerIndex(readerIndex);
/* 1115 */           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */         } 
/* 1117 */         if (nextByte == 10)
/* 1118 */           return sb.toString(); 
/* 1119 */         if (nextByte == 45) {
/* 1120 */           sb.append('-');
/*      */           
/* 1122 */           nextByte = undecodedChunk.readByte();
/* 1123 */           if (nextByte == 45) {
/* 1124 */             sb.append('-');
/*      */             
/* 1126 */             if (undecodedChunk.isReadable()) {
/* 1127 */               nextByte = undecodedChunk.readByte();
/* 1128 */               if (nextByte == 13) {
/* 1129 */                 nextByte = undecodedChunk.readByte();
/* 1130 */                 if (nextByte == 10) {
/* 1131 */                   return sb.toString();
/*      */                 }
/*      */ 
/*      */                 
/* 1135 */                 undecodedChunk.readerIndex(readerIndex);
/* 1136 */                 throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */               } 
/* 1138 */               if (nextByte == 10) {
/* 1139 */                 return sb.toString();
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 1144 */               undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
/* 1145 */               return sb.toString();
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1152 */             return sb.toString();
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } 
/* 1158 */     } catch (IndexOutOfBoundsException e) {
/* 1159 */       undecodedChunk.readerIndex(readerIndex);
/* 1160 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     } 
/* 1162 */     undecodedChunk.readerIndex(readerIndex);
/* 1163 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
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
/*      */   private static String readDelimiter(ByteBuf undecodedChunk, String delimiter) {
/* 1181 */     if (!undecodedChunk.hasArray()) {
/* 1182 */       return readDelimiterStandard(undecodedChunk, delimiter);
/*      */     }
/* 1184 */     HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
/* 1185 */     int readerIndex = undecodedChunk.readerIndex();
/* 1186 */     int delimiterPos = 0;
/* 1187 */     int len = delimiter.length();
/*      */     try {
/* 1189 */       StringBuilder sb = new StringBuilder(64);
/*      */       
/* 1191 */       while (sao.pos < sao.limit && delimiterPos < len) {
/* 1192 */         byte nextByte = sao.bytes[sao.pos++];
/* 1193 */         if (nextByte == delimiter.charAt(delimiterPos)) {
/* 1194 */           delimiterPos++;
/* 1195 */           sb.append((char)nextByte);
/*      */           continue;
/*      */         } 
/* 1198 */         undecodedChunk.readerIndex(readerIndex);
/* 1199 */         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */       } 
/*      */ 
/*      */       
/* 1203 */       if (sao.pos < sao.limit) {
/* 1204 */         byte nextByte = sao.bytes[sao.pos++];
/* 1205 */         if (nextByte == 13) {
/*      */           
/* 1207 */           if (sao.pos < sao.limit) {
/* 1208 */             nextByte = sao.bytes[sao.pos++];
/* 1209 */             if (nextByte == 10) {
/* 1210 */               sao.setReadPosition(0);
/* 1211 */               return sb.toString();
/*      */             } 
/*      */ 
/*      */             
/* 1215 */             undecodedChunk.readerIndex(readerIndex);
/* 1216 */             throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 1221 */           undecodedChunk.readerIndex(readerIndex);
/* 1222 */           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */         } 
/* 1224 */         if (nextByte == 10) {
/*      */ 
/*      */           
/* 1227 */           sao.setReadPosition(0);
/* 1228 */           return sb.toString();
/* 1229 */         }  if (nextByte == 45) {
/* 1230 */           sb.append('-');
/*      */           
/* 1232 */           if (sao.pos < sao.limit) {
/* 1233 */             nextByte = sao.bytes[sao.pos++];
/* 1234 */             if (nextByte == 45) {
/* 1235 */               sb.append('-');
/*      */               
/* 1237 */               if (sao.pos < sao.limit) {
/* 1238 */                 nextByte = sao.bytes[sao.pos++];
/* 1239 */                 if (nextByte == 13) {
/* 1240 */                   if (sao.pos < sao.limit) {
/* 1241 */                     nextByte = sao.bytes[sao.pos++];
/* 1242 */                     if (nextByte == 10) {
/* 1243 */                       sao.setReadPosition(0);
/* 1244 */                       return sb.toString();
/*      */                     } 
/*      */ 
/*      */                     
/* 1248 */                     undecodedChunk.readerIndex(readerIndex);
/* 1249 */                     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */                   } 
/*      */ 
/*      */ 
/*      */                   
/* 1254 */                   undecodedChunk.readerIndex(readerIndex);
/* 1255 */                   throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */                 } 
/* 1257 */                 if (nextByte == 10) {
/* 1258 */                   sao.setReadPosition(0);
/* 1259 */                   return sb.toString();
/*      */                 } 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1265 */                 sao.setReadPosition(1);
/* 1266 */                 return sb.toString();
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1273 */               sao.setReadPosition(0);
/* 1274 */               return sb.toString();
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       } 
/* 1282 */     } catch (IndexOutOfBoundsException e) {
/* 1283 */       undecodedChunk.readerIndex(readerIndex);
/* 1284 */       throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
/*      */     } 
/* 1286 */     undecodedChunk.readerIndex(readerIndex);
/* 1287 */     throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean loadDataMultipartStandard(ByteBuf undecodedChunk, String delimiter, HttpData httpData) {
/* 1297 */     int startReaderIndex = undecodedChunk.readerIndex();
/* 1298 */     int delimeterLength = delimiter.length();
/* 1299 */     int index = 0;
/* 1300 */     int lastPosition = startReaderIndex;
/* 1301 */     byte prevByte = 10;
/* 1302 */     boolean delimiterFound = false;
/* 1303 */     while (undecodedChunk.isReadable()) {
/* 1304 */       byte nextByte = undecodedChunk.readByte();
/*      */       
/* 1306 */       if (prevByte == 10 && nextByte == delimiter.codePointAt(index)) {
/* 1307 */         index++;
/* 1308 */         if (delimeterLength == index) {
/* 1309 */           delimiterFound = true;
/*      */           break;
/*      */         } 
/*      */         continue;
/*      */       } 
/* 1314 */       lastPosition = undecodedChunk.readerIndex();
/* 1315 */       if (nextByte == 10) {
/* 1316 */         index = 0;
/* 1317 */         lastPosition -= (prevByte == 13) ? 2 : 1;
/*      */       } 
/* 1319 */       prevByte = nextByte;
/*      */     } 
/* 1321 */     if (prevByte == 13) {
/* 1322 */       lastPosition--;
/*      */     }
/* 1324 */     ByteBuf content = undecodedChunk.copy(startReaderIndex, lastPosition - startReaderIndex);
/*      */     try {
/* 1326 */       httpData.addContent(content, delimiterFound);
/* 1327 */     } catch (IOException e) {
/* 1328 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */     } 
/* 1330 */     undecodedChunk.readerIndex(lastPosition);
/* 1331 */     return delimiterFound;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean loadDataMultipart(ByteBuf undecodedChunk, String delimiter, HttpData httpData) {
/* 1341 */     if (!undecodedChunk.hasArray()) {
/* 1342 */       return loadDataMultipartStandard(undecodedChunk, delimiter, httpData);
/*      */     }
/* 1344 */     HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
/* 1345 */     int startReaderIndex = undecodedChunk.readerIndex();
/* 1346 */     int delimeterLength = delimiter.length();
/* 1347 */     int index = 0;
/* 1348 */     int lastRealPos = sao.pos;
/* 1349 */     byte prevByte = 10;
/* 1350 */     boolean delimiterFound = false;
/* 1351 */     while (sao.pos < sao.limit) {
/* 1352 */       byte nextByte = sao.bytes[sao.pos++];
/*      */       
/* 1354 */       if (prevByte == 10 && nextByte == delimiter.codePointAt(index)) {
/* 1355 */         index++;
/* 1356 */         if (delimeterLength == index) {
/* 1357 */           delimiterFound = true;
/*      */           break;
/*      */         } 
/*      */         continue;
/*      */       } 
/* 1362 */       lastRealPos = sao.pos;
/* 1363 */       if (nextByte == 10) {
/* 1364 */         index = 0;
/* 1365 */         lastRealPos -= (prevByte == 13) ? 2 : 1;
/*      */       } 
/* 1367 */       prevByte = nextByte;
/*      */     } 
/* 1369 */     if (prevByte == 13) {
/* 1370 */       lastRealPos--;
/*      */     }
/* 1372 */     int lastPosition = sao.getReadPosition(lastRealPos);
/* 1373 */     ByteBuf content = undecodedChunk.copy(startReaderIndex, lastPosition - startReaderIndex);
/*      */     try {
/* 1375 */       httpData.addContent(content, delimiterFound);
/* 1376 */     } catch (IOException e) {
/* 1377 */       throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
/*      */     } 
/* 1379 */     undecodedChunk.readerIndex(lastPosition);
/* 1380 */     return delimiterFound;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String cleanString(String field) {
/* 1389 */     int size = field.length();
/* 1390 */     StringBuilder sb = new StringBuilder(size);
/* 1391 */     for (int i = 0; i < size; i++) {
/* 1392 */       char nextChar = field.charAt(i);
/* 1393 */       switch (nextChar) {
/*      */         case '\t':
/*      */         case ',':
/*      */         case ':':
/*      */         case ';':
/*      */         case '=':
/* 1399 */           sb.append(' ');
/*      */           break;
/*      */         
/*      */         case '"':
/*      */           break;
/*      */         default:
/* 1405 */           sb.append(nextChar);
/*      */           break;
/*      */       } 
/*      */     } 
/* 1409 */     return sb.toString().trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipOneLine() {
/* 1418 */     if (!this.undecodedChunk.isReadable()) {
/* 1419 */       return false;
/*      */     }
/* 1421 */     byte nextByte = this.undecodedChunk.readByte();
/* 1422 */     if (nextByte == 13) {
/* 1423 */       if (!this.undecodedChunk.isReadable()) {
/* 1424 */         this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/* 1425 */         return false;
/*      */       } 
/* 1427 */       nextByte = this.undecodedChunk.readByte();
/* 1428 */       if (nextByte == 10) {
/* 1429 */         return true;
/*      */       }
/* 1431 */       this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
/* 1432 */       return false;
/*      */     } 
/* 1434 */     if (nextByte == 10) {
/* 1435 */       return true;
/*      */     }
/* 1437 */     this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
/* 1438 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] splitMultipartHeader(String sb) {
/*      */     String[] values;
/* 1448 */     ArrayList<String> headers = new ArrayList<String>(1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1454 */     int nameStart = HttpPostBodyUtil.findNonWhitespace(sb, 0); int nameEnd;
/* 1455 */     for (nameEnd = nameStart; nameEnd < sb.length(); nameEnd++) {
/* 1456 */       char ch = sb.charAt(nameEnd);
/* 1457 */       if (ch == ':' || Character.isWhitespace(ch))
/*      */         break; 
/*      */     } 
/*      */     int colonEnd;
/* 1461 */     for (colonEnd = nameEnd; colonEnd < sb.length(); colonEnd++) {
/* 1462 */       if (sb.charAt(colonEnd) == ':') {
/* 1463 */         colonEnd++;
/*      */         break;
/*      */       } 
/*      */     } 
/* 1467 */     int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
/* 1468 */     int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
/* 1469 */     headers.add(sb.substring(nameStart, nameEnd));
/* 1470 */     String svalue = (valueStart >= valueEnd) ? "" : sb.substring(valueStart, valueEnd);
/*      */     
/* 1472 */     if (svalue.indexOf(';') >= 0) {
/* 1473 */       values = splitMultipartHeaderValues(svalue);
/*      */     } else {
/* 1475 */       values = svalue.split(",");
/*      */     } 
/* 1477 */     for (String value : values) {
/* 1478 */       headers.add(value.trim());
/*      */     }
/* 1480 */     String[] array = new String[headers.size()];
/* 1481 */     for (int i = 0; i < headers.size(); i++) {
/* 1482 */       array[i] = headers.get(i);
/*      */     }
/* 1484 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] splitMultipartHeaderValues(String svalue) {
/* 1492 */     List<String> values = InternalThreadLocalMap.get().arrayList(1);
/* 1493 */     boolean inQuote = false;
/* 1494 */     boolean escapeNext = false;
/* 1495 */     int start = 0;
/* 1496 */     for (int i = 0; i < svalue.length(); i++) {
/* 1497 */       char c = svalue.charAt(i);
/* 1498 */       if (inQuote) {
/* 1499 */         if (escapeNext) {
/* 1500 */           escapeNext = false;
/*      */         }
/* 1502 */         else if (c == '\\') {
/* 1503 */           escapeNext = true;
/* 1504 */         } else if (c == '"') {
/* 1505 */           inQuote = false;
/*      */         }
/*      */       
/*      */       }
/* 1509 */       else if (c == '"') {
/* 1510 */         inQuote = true;
/* 1511 */       } else if (c == ';') {
/* 1512 */         values.add(svalue.substring(start, i));
/* 1513 */         start = i + 1;
/*      */       } 
/*      */     } 
/*      */     
/* 1517 */     values.add(svalue.substring(start));
/* 1518 */     return values.<String>toArray(new String[values.size()]);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\HttpPostMultipartRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */