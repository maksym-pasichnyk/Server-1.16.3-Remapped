/*      */ package io.netty.handler.codec.http.multipart;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufHolder;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.channel.ChannelHandlerContext;
/*      */ import io.netty.handler.codec.DecoderResult;
/*      */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*      */ import io.netty.handler.codec.http.DefaultHttpContent;
/*      */ import io.netty.handler.codec.http.EmptyHttpHeaders;
/*      */ import io.netty.handler.codec.http.FullHttpMessage;
/*      */ import io.netty.handler.codec.http.FullHttpRequest;
/*      */ import io.netty.handler.codec.http.HttpConstants;
/*      */ import io.netty.handler.codec.http.HttpContent;
/*      */ import io.netty.handler.codec.http.HttpHeaderNames;
/*      */ import io.netty.handler.codec.http.HttpHeaderValues;
/*      */ import io.netty.handler.codec.http.HttpHeaders;
/*      */ import io.netty.handler.codec.http.HttpMessage;
/*      */ import io.netty.handler.codec.http.HttpMethod;
/*      */ import io.netty.handler.codec.http.HttpRequest;
/*      */ import io.netty.handler.codec.http.HttpUtil;
/*      */ import io.netty.handler.codec.http.HttpVersion;
/*      */ import io.netty.handler.codec.http.LastHttpContent;
/*      */ import io.netty.handler.stream.ChunkedInput;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URLEncoder;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
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
/*      */ public class HttpPostRequestEncoder
/*      */   implements ChunkedInput<HttpContent>
/*      */ {
/*      */   public enum EncoderMode
/*      */   {
/*   77 */     RFC1738,
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   82 */     RFC3986,
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
/*   93 */     HTML5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  100 */   private static final Map.Entry[] percentEncodings = new Map.Entry[] { new AbstractMap.SimpleImmutableEntry<Pattern, String>(
/*  101 */         Pattern.compile("\\*"), "%2A"), new AbstractMap.SimpleImmutableEntry<Pattern, String>(
/*  102 */         Pattern.compile("\\+"), "%20"), new AbstractMap.SimpleImmutableEntry<Pattern, String>(
/*  103 */         Pattern.compile("~"), "%7E") };
/*      */ 
/*      */   
/*      */   private final HttpDataFactory factory;
/*      */ 
/*      */   
/*      */   private final HttpRequest request;
/*      */ 
/*      */   
/*      */   private final Charset charset;
/*      */ 
/*      */   
/*      */   private boolean isChunked;
/*      */ 
/*      */   
/*      */   private final List<InterfaceHttpData> bodyListDatas;
/*      */ 
/*      */   
/*      */   final List<InterfaceHttpData> multipartHttpDatas;
/*      */ 
/*      */   
/*      */   private final boolean isMultipart;
/*      */ 
/*      */   
/*      */   String multipartDataBoundary;
/*      */ 
/*      */   
/*      */   String multipartMixedBoundary;
/*      */ 
/*      */   
/*      */   private boolean headerFinalized;
/*      */ 
/*      */   
/*      */   private final EncoderMode encoderMode;
/*      */ 
/*      */   
/*      */   private boolean isLastChunk;
/*      */ 
/*      */   
/*      */   private boolean isLastChunkSent;
/*      */ 
/*      */   
/*      */   private FileUpload currentFileUpload;
/*      */ 
/*      */   
/*      */   private boolean duringMixedMode;
/*      */ 
/*      */   
/*      */   private long globalBodySize;
/*      */ 
/*      */   
/*      */   private long globalProgress;
/*      */ 
/*      */   
/*      */   private ListIterator<InterfaceHttpData> iterator;
/*      */ 
/*      */   
/*      */   private ByteBuf currentBuffer;
/*      */ 
/*      */   
/*      */   private InterfaceHttpData currentData;
/*      */   
/*      */   private boolean isKey;
/*      */ 
/*      */   
/*      */   public HttpPostRequestEncoder(HttpRequest request, boolean multipart) throws ErrorDataEncoderException {
/*  169 */     this(new DefaultHttpDataFactory(16384L), request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
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
/*      */   public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart) throws ErrorDataEncoderException {
/*  188 */     this(factory, request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
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
/*      */   public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart, Charset charset, EncoderMode encoderMode) throws ErrorDataEncoderException {
/*  866 */     this.isKey = true; this.request = (HttpRequest)ObjectUtil.checkNotNull(request, "request"); this.charset = (Charset)ObjectUtil.checkNotNull(charset, "charset"); this.factory = (HttpDataFactory)ObjectUtil.checkNotNull(factory, "factory"); if (HttpMethod.TRACE.equals(request.method())) throw new ErrorDataEncoderException("Cannot create a Encoder if request is a TRACE");  this.bodyListDatas = new ArrayList<InterfaceHttpData>(); this.isLastChunk = false; this.isLastChunkSent = false; this.isMultipart = multipart; this.multipartHttpDatas = new ArrayList<InterfaceHttpData>(); this.encoderMode = encoderMode; if (this.isMultipart) initDataMultipart(); 
/*      */   } public void cleanFiles() { this.factory.cleanRequestHttpData(this.request); } public boolean isMultipart() { return this.isMultipart; }
/*      */   private void initDataMultipart() { this.multipartDataBoundary = getNewMultipartDelimiter(); }
/*      */   private void initMixedMultipart() { this.multipartMixedBoundary = getNewMultipartDelimiter(); }
/*      */   private static String getNewMultipartDelimiter() { return Long.toHexString(PlatformDependent.threadLocalRandom().nextLong()); }
/*      */   public List<InterfaceHttpData> getBodyListAttributes() { return this.bodyListDatas; }
/*      */   public void setBodyHttpDatas(List<InterfaceHttpData> datas) throws ErrorDataEncoderException { if (datas == null) throw new NullPointerException("datas");  this.globalBodySize = 0L; this.bodyListDatas.clear(); this.currentFileUpload = null; this.duringMixedMode = false; this.multipartHttpDatas.clear(); for (InterfaceHttpData data : datas) addBodyHttpData(data);  }
/*  873 */   private ByteBuf fillByteBuf() { int length = this.currentBuffer.readableBytes();
/*  874 */     if (length > 8096) {
/*  875 */       return this.currentBuffer.readRetainedSlice(8096);
/*      */     }
/*      */     
/*  878 */     ByteBuf slice = this.currentBuffer;
/*  879 */     this.currentBuffer = null;
/*  880 */     return slice; } public void addBodyAttribute(String name, String value) throws ErrorDataEncoderException { String svalue = (value != null) ? value : ""; Attribute data = this.factory.createAttribute(this.request, (String)ObjectUtil.checkNotNull(name, "name"), svalue); addBodyHttpData(data); }
/*      */   public void addBodyFileUpload(String name, File file, String contentType, boolean isText) throws ErrorDataEncoderException { addBodyFileUpload(name, file.getName(), file, contentType, isText); }
/*      */   public void addBodyFileUpload(String name, String filename, File file, String contentType, boolean isText) throws ErrorDataEncoderException { ObjectUtil.checkNotNull(name, "name"); ObjectUtil.checkNotNull(file, "file"); if (filename == null)
/*      */       filename = "";  String scontentType = contentType; String contentTransferEncoding = null; if (contentType == null)
/*      */       if (isText) { scontentType = "text/plain"; }
/*      */       else { scontentType = "application/octet-stream"; }
/*      */         if (!isText)
/*      */       contentTransferEncoding = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value();  FileUpload fileUpload = this.factory.createFileUpload(this.request, name, filename, scontentType, contentTransferEncoding, null, file.length()); try {
/*      */       fileUpload.setContent(file);
/*      */     } catch (IOException e) {
/*      */       throw new ErrorDataEncoderException(e);
/*      */     }  addBodyHttpData(fileUpload); }
/*      */   public void addBodyFileUploads(String name, File[] file, String[] contentType, boolean[] isText) throws ErrorDataEncoderException { if (file.length != contentType.length && file.length != isText.length)
/*      */       throw new IllegalArgumentException("Different array length");  for (int i = 0; i < file.length; i++)
/*      */       addBodyFileUpload(name, file[i], contentType[i], isText[i]);  }
/*  895 */   private HttpContent encodeNextChunkMultipart(int sizeleft) throws ErrorDataEncoderException { if (this.currentData == null) {
/*  896 */       return null;
/*      */     }
/*      */     
/*  899 */     if (this.currentData instanceof InternalAttribute) {
/*  900 */       buffer = ((InternalAttribute)this.currentData).toByteBuf();
/*  901 */       this.currentData = null;
/*      */     } else {
/*      */       try {
/*  904 */         buffer = ((HttpData)this.currentData).getChunk(sizeleft);
/*  905 */       } catch (IOException e) {
/*  906 */         throw new ErrorDataEncoderException(e);
/*      */       } 
/*  908 */       if (buffer.capacity() == 0) {
/*      */         
/*  910 */         this.currentData = null;
/*  911 */         return null;
/*      */       } 
/*      */     } 
/*  914 */     if (this.currentBuffer == null) {
/*  915 */       this.currentBuffer = buffer;
/*      */     } else {
/*  917 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
/*      */     } 
/*  919 */     if (this.currentBuffer.readableBytes() < 8096) {
/*  920 */       this.currentData = null;
/*  921 */       return null;
/*      */     } 
/*  923 */     ByteBuf buffer = fillByteBuf();
/*  924 */     return (HttpContent)new DefaultHttpContent(buffer); }
/*      */   public void addBodyHttpData(InterfaceHttpData data) throws ErrorDataEncoderException { if (this.headerFinalized)
/*      */       throw new ErrorDataEncoderException("Cannot add value once finalized");  this.bodyListDatas.add(ObjectUtil.checkNotNull(data, "data")); if (!this.isMultipart) { if (data instanceof Attribute) { Attribute attribute = (Attribute)data; try { String key = encodeAttribute(attribute.getName(), this.charset); String value = encodeAttribute(attribute.getValue(), this.charset); Attribute newattribute = this.factory.createAttribute(this.request, key, value); this.multipartHttpDatas.add(newattribute); this.globalBodySize += (newattribute.getName().length() + 1) + newattribute.length() + 1L; } catch (IOException e) { throw new ErrorDataEncoderException(e); }  } else if (data instanceof FileUpload) { FileUpload fileUpload = (FileUpload)data; String key = encodeAttribute(fileUpload.getName(), this.charset); String value = encodeAttribute(fileUpload.getFilename(), this.charset); Attribute newattribute = this.factory.createAttribute(this.request, key, value); this.multipartHttpDatas.add(newattribute); this.globalBodySize += (newattribute.getName().length() + 1) + newattribute.length() + 1L; }  return; }  if (data instanceof Attribute) { if (this.duringMixedMode) { InternalAttribute internalAttribute = new InternalAttribute(this.charset); internalAttribute.addValue("\r\n--" + this.multipartMixedBoundary + "--"); this.multipartHttpDatas.add(internalAttribute); this.multipartMixedBoundary = null; this.currentFileUpload = null; this.duringMixedMode = false; }  InternalAttribute internal = new InternalAttribute(this.charset); if (!this.multipartHttpDatas.isEmpty())
/*      */         internal.addValue("\r\n");  internal.addValue("--" + this.multipartDataBoundary + "\r\n"); Attribute attribute = (Attribute)data; internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + attribute.getName() + "\"\r\n"); internal.addValue(HttpHeaderNames.CONTENT_LENGTH + ": " + attribute.length() + "\r\n"); Charset localcharset = attribute.getCharset(); if (localcharset != null)
/*      */         internal.addValue(HttpHeaderNames.CONTENT_TYPE + ": " + "text/plain" + "; " + HttpHeaderValues.CHARSET + '=' + localcharset.name() + "\r\n");  internal.addValue("\r\n"); this.multipartHttpDatas.add(internal); this.multipartHttpDatas.add(data); this.globalBodySize += attribute.length() + internal.size(); } else if (data instanceof FileUpload) { boolean localMixed; FileUpload fileUpload = (FileUpload)data; InternalAttribute internal = new InternalAttribute(this.charset); if (!this.multipartHttpDatas.isEmpty())
/*      */         internal.addValue("\r\n");  if (this.duringMixedMode) { if (this.currentFileUpload != null && this.currentFileUpload.getName().equals(fileUpload.getName())) { localMixed = true; } else { internal.addValue("--" + this.multipartMixedBoundary + "--"); this.multipartHttpDatas.add(internal); this.multipartMixedBoundary = null; internal = new InternalAttribute(this.charset); internal.addValue("\r\n"); localMixed = false; this.currentFileUpload = fileUpload; this.duringMixedMode = false; }  } else if (this.encoderMode != EncoderMode.HTML5 && this.currentFileUpload != null && this.currentFileUpload.getName().equals(fileUpload.getName())) { initMixedMultipart(); InternalAttribute pastAttribute = (InternalAttribute)this.multipartHttpDatas.get(this.multipartHttpDatas.size() - 2); this.globalBodySize -= pastAttribute.size(); StringBuilder replacement = (new StringBuilder(139 + this.multipartDataBoundary.length() + this.multipartMixedBoundary.length() * 2 + fileUpload.getFilename().length() + fileUpload.getName().length())).append("--").append(this.multipartDataBoundary).append("\r\n").append((CharSequence)HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append((CharSequence)HttpHeaderValues.FORM_DATA).append("; ").append((CharSequence)HttpHeaderValues.NAME).append("=\"").append(fileUpload.getName()).append("\"\r\n").append((CharSequence)HttpHeaderNames.CONTENT_TYPE).append(": ").append((CharSequence)HttpHeaderValues.MULTIPART_MIXED).append("; ").append((CharSequence)HttpHeaderValues.BOUNDARY).append('=').append(this.multipartMixedBoundary).append("\r\n\r\n").append("--").append(this.multipartMixedBoundary).append("\r\n").append((CharSequence)HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append((CharSequence)HttpHeaderValues.ATTACHMENT); if (!fileUpload.getFilename().isEmpty())
/*      */           replacement.append("; ").append((CharSequence)HttpHeaderValues.FILENAME).append("=\"").append(fileUpload.getFilename()).append('"');  replacement.append("\r\n"); pastAttribute.setValue(replacement.toString(), 1); pastAttribute.setValue("", 2); this.globalBodySize += pastAttribute.size(); localMixed = true; this.duringMixedMode = true; } else { localMixed = false; this.currentFileUpload = fileUpload; this.duringMixedMode = false; }  if (localMixed) { internal.addValue("--" + this.multipartMixedBoundary + "\r\n"); if (fileUpload.getFilename().isEmpty()) { internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.ATTACHMENT + "\r\n"); } else { internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.ATTACHMENT + "; " + HttpHeaderValues.FILENAME + "=\"" + fileUpload.getFilename() + "\"\r\n"); }  }
/*      */       else { internal.addValue("--" + this.multipartDataBoundary + "\r\n"); if (fileUpload.getFilename().isEmpty()) { internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + fileUpload.getName() + "\"\r\n"); }
/*      */         else { internal.addValue(HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + fileUpload.getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + fileUpload.getFilename() + "\"\r\n"); }
/*      */          }
/*      */        internal.addValue(HttpHeaderNames.CONTENT_LENGTH + ": " + fileUpload.length() + "\r\n"); internal.addValue(HttpHeaderNames.CONTENT_TYPE + ": " + fileUpload.getContentType()); String contentTransferEncoding = fileUpload.getContentTransferEncoding(); if (contentTransferEncoding != null && contentTransferEncoding.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) { internal.addValue("\r\n" + HttpHeaderNames.CONTENT_TRANSFER_ENCODING + ": " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value() + "\r\n\r\n"); }
/*      */       else if (fileUpload.getCharset() != null) { internal.addValue("; " + HttpHeaderValues.CHARSET + '=' + fileUpload.getCharset().name() + "\r\n\r\n"); }
/*      */       else { internal.addValue("\r\n\r\n"); }
/*      */        this.multipartHttpDatas.add(internal); this.multipartHttpDatas.add(data); this.globalBodySize += fileUpload.length() + internal.size(); }
/*  938 */      } private HttpContent encodeNextChunkUrlEncoded(int sizeleft) throws ErrorDataEncoderException { if (this.currentData == null) {
/*  939 */       return null;
/*      */     }
/*  941 */     int size = sizeleft;
/*      */ 
/*      */ 
/*      */     
/*  945 */     if (this.isKey) {
/*  946 */       String key = this.currentData.getName();
/*  947 */       buffer = Unpooled.wrappedBuffer(key.getBytes());
/*  948 */       this.isKey = false;
/*  949 */       if (this.currentBuffer == null) {
/*  950 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, Unpooled.wrappedBuffer("=".getBytes()) });
/*      */       } else {
/*  952 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, Unpooled.wrappedBuffer("=".getBytes()) });
/*      */       } 
/*      */       
/*  955 */       size -= buffer.readableBytes() + 1;
/*  956 */       if (this.currentBuffer.readableBytes() >= 8096) {
/*  957 */         buffer = fillByteBuf();
/*  958 */         return (HttpContent)new DefaultHttpContent(buffer);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  964 */       buffer = ((HttpData)this.currentData).getChunk(size);
/*  965 */     } catch (IOException e) {
/*  966 */       throw new ErrorDataEncoderException(e);
/*      */     } 
/*      */ 
/*      */     
/*  970 */     ByteBuf delimiter = null;
/*  971 */     if (buffer.readableBytes() < size) {
/*  972 */       this.isKey = true;
/*  973 */       delimiter = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes()) : null;
/*      */     } 
/*      */ 
/*      */     
/*  977 */     if (buffer.capacity() == 0) {
/*  978 */       this.currentData = null;
/*  979 */       if (this.currentBuffer == null) {
/*  980 */         this.currentBuffer = delimiter;
/*      */       }
/*  982 */       else if (delimiter != null) {
/*  983 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, delimiter });
/*      */       } 
/*      */       
/*  986 */       if (this.currentBuffer.readableBytes() >= 8096) {
/*  987 */         buffer = fillByteBuf();
/*  988 */         return (HttpContent)new DefaultHttpContent(buffer);
/*      */       } 
/*  990 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  994 */     if (this.currentBuffer == null) {
/*  995 */       if (delimiter != null) {
/*  996 */         this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { buffer, delimiter });
/*      */       } else {
/*  998 */         this.currentBuffer = buffer;
/*      */       }
/*      */     
/* 1001 */     } else if (delimiter != null) {
/* 1002 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer, delimiter });
/*      */     } else {
/* 1004 */       this.currentBuffer = Unpooled.wrappedBuffer(new ByteBuf[] { this.currentBuffer, buffer });
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     if (this.currentBuffer.readableBytes() < 8096) {
/* 1010 */       this.currentData = null;
/* 1011 */       this.isKey = true;
/* 1012 */       return null;
/*      */     } 
/*      */     
/* 1015 */     ByteBuf buffer = fillByteBuf();
/* 1016 */     return (HttpContent)new DefaultHttpContent(buffer); }
/*      */   public HttpRequest finalizeRequest() throws ErrorDataEncoderException { if (!this.headerFinalized) { if (this.isMultipart) { InternalAttribute internal = new InternalAttribute(this.charset); if (this.duringMixedMode)
/*      */           internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");  internal.addValue("\r\n--" + this.multipartDataBoundary + "--\r\n"); this.multipartHttpDatas.add(internal); this.multipartMixedBoundary = null; this.currentFileUpload = null; this.duringMixedMode = false; this.globalBodySize += internal.size(); }  this.headerFinalized = true; } else { throw new ErrorDataEncoderException("Header already encoded"); }  HttpHeaders headers = this.request.headers(); List<String> contentTypes = headers.getAll((CharSequence)HttpHeaderNames.CONTENT_TYPE); List<String> transferEncoding = headers.getAll((CharSequence)HttpHeaderNames.TRANSFER_ENCODING); if (contentTypes != null) { headers.remove((CharSequence)HttpHeaderNames.CONTENT_TYPE); for (String contentType : contentTypes) { String lowercased = contentType.toLowerCase(); if (lowercased.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString()) || lowercased.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()))
/*      */           continue;  headers.add((CharSequence)HttpHeaderNames.CONTENT_TYPE, contentType); }  }  if (this.isMultipart) { String value = HttpHeaderValues.MULTIPART_FORM_DATA + "; " + HttpHeaderValues.BOUNDARY + '=' + this.multipartDataBoundary; headers.add((CharSequence)HttpHeaderNames.CONTENT_TYPE, value); } else { headers.add((CharSequence)HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED); }  long realSize = this.globalBodySize; if (!this.isMultipart)
/*      */       realSize--;  this.iterator = this.multipartHttpDatas.listIterator(); headers.set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, String.valueOf(realSize)); if (realSize > 8096L || this.isMultipart) { this.isChunked = true; if (transferEncoding != null) { headers.remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING); for (CharSequence v : transferEncoding) { if (HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(v))
/*      */             continue;  headers.add((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, v); }  }  HttpUtil.setTransferEncodingChunked((HttpMessage)this.request, true); return new WrappedHttpRequest(this.request); }  HttpContent chunk = nextChunk(); if (this.request instanceof FullHttpRequest) { FullHttpRequest fullRequest = (FullHttpRequest)this.request; ByteBuf chunkContent = chunk.content(); if (fullRequest.content() != chunkContent) { fullRequest.content().clear().writeBytes(chunkContent); chunkContent.release(); }  return (HttpRequest)fullRequest; }  return new WrappedFullHttpRequest(this.request, chunk); }
/*      */   public boolean isChunked() { return this.isChunked; }
/*      */   private String encodeAttribute(String s, Charset charset) throws ErrorDataEncoderException { if (s == null)
/*      */       return "";  try { String encoded = URLEncoder.encode(s, charset.name()); if (this.encoderMode == EncoderMode.RFC3986)
/*      */         for (Map.Entry<Pattern, String> entry : percentEncodings) { String replacement = entry.getValue(); encoded = ((Pattern)entry.getKey()).matcher(encoded).replaceAll(replacement); }
/*      */           return encoded; }
/*      */     catch (UnsupportedEncodingException e) { throw new ErrorDataEncoderException(charset.name(), e); }
/* 1028 */      } public void close() throws Exception {} @Deprecated public HttpContent readChunk(ChannelHandlerContext ctx) throws Exception { return readChunk(ctx.alloc()); }
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
/*      */   public HttpContent readChunk(ByteBufAllocator allocator) throws Exception {
/* 1041 */     if (this.isLastChunkSent) {
/* 1042 */       return null;
/*      */     }
/* 1044 */     HttpContent nextChunk = nextChunk();
/* 1045 */     this.globalProgress += nextChunk.content().readableBytes();
/* 1046 */     return nextChunk;
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
/*      */   private HttpContent nextChunk() throws ErrorDataEncoderException {
/* 1059 */     if (this.isLastChunk) {
/* 1060 */       this.isLastChunkSent = true;
/* 1061 */       return (HttpContent)LastHttpContent.EMPTY_LAST_CONTENT;
/*      */     } 
/*      */     
/* 1064 */     int size = calculateRemainingSize();
/* 1065 */     if (size <= 0) {
/*      */       
/* 1067 */       ByteBuf buffer = fillByteBuf();
/* 1068 */       return (HttpContent)new DefaultHttpContent(buffer);
/*      */     } 
/*      */     
/* 1071 */     if (this.currentData != null) {
/*      */       HttpContent chunk;
/*      */       
/* 1074 */       if (this.isMultipart) {
/* 1075 */         chunk = encodeNextChunkMultipart(size);
/*      */       } else {
/* 1077 */         chunk = encodeNextChunkUrlEncoded(size);
/*      */       } 
/* 1079 */       if (chunk != null)
/*      */       {
/* 1081 */         return chunk;
/*      */       }
/* 1083 */       size = calculateRemainingSize();
/*      */     } 
/* 1085 */     if (!this.iterator.hasNext()) {
/* 1086 */       return lastChunk();
/*      */     }
/* 1088 */     while (size > 0 && this.iterator.hasNext()) {
/* 1089 */       HttpContent chunk; this.currentData = this.iterator.next();
/*      */       
/* 1091 */       if (this.isMultipart) {
/* 1092 */         chunk = encodeNextChunkMultipart(size);
/*      */       } else {
/* 1094 */         chunk = encodeNextChunkUrlEncoded(size);
/*      */       } 
/* 1096 */       if (chunk == null) {
/*      */         
/* 1098 */         size = calculateRemainingSize();
/*      */         
/*      */         continue;
/*      */       } 
/* 1102 */       return chunk;
/*      */     } 
/*      */     
/* 1105 */     return lastChunk();
/*      */   }
/*      */   
/*      */   private int calculateRemainingSize() {
/* 1109 */     int size = 8096;
/* 1110 */     if (this.currentBuffer != null) {
/* 1111 */       size -= this.currentBuffer.readableBytes();
/*      */     }
/* 1113 */     return size;
/*      */   }
/*      */   
/*      */   private HttpContent lastChunk() {
/* 1117 */     this.isLastChunk = true;
/* 1118 */     if (this.currentBuffer == null) {
/* 1119 */       this.isLastChunkSent = true;
/*      */       
/* 1121 */       return (HttpContent)LastHttpContent.EMPTY_LAST_CONTENT;
/*      */     } 
/*      */     
/* 1124 */     ByteBuf buffer = this.currentBuffer;
/* 1125 */     this.currentBuffer = null;
/* 1126 */     return (HttpContent)new DefaultHttpContent(buffer);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEndOfInput() throws Exception {
/* 1131 */     return this.isLastChunkSent;
/*      */   }
/*      */ 
/*      */   
/*      */   public long length() {
/* 1136 */     return this.isMultipart ? this.globalBodySize : (this.globalBodySize - 1L);
/*      */   }
/*      */ 
/*      */   
/*      */   public long progress() {
/* 1141 */     return this.globalProgress;
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ErrorDataEncoderException
/*      */     extends Exception
/*      */   {
/*      */     private static final long serialVersionUID = 5020247425493164465L;
/*      */ 
/*      */     
/*      */     public ErrorDataEncoderException() {}
/*      */     
/*      */     public ErrorDataEncoderException(String msg) {
/* 1154 */       super(msg);
/*      */     }
/*      */     
/*      */     public ErrorDataEncoderException(Throwable cause) {
/* 1158 */       super(cause);
/*      */     }
/*      */     
/*      */     public ErrorDataEncoderException(String msg, Throwable cause) {
/* 1162 */       super(msg, cause);
/*      */     } }
/*      */   
/*      */   private static class WrappedHttpRequest implements HttpRequest {
/*      */     private final HttpRequest request;
/*      */     
/*      */     WrappedHttpRequest(HttpRequest request) {
/* 1169 */       this.request = request;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpRequest setProtocolVersion(HttpVersion version) {
/* 1174 */       this.request.setProtocolVersion(version);
/* 1175 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpRequest setMethod(HttpMethod method) {
/* 1180 */       this.request.setMethod(method);
/* 1181 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpRequest setUri(String uri) {
/* 1186 */       this.request.setUri(uri);
/* 1187 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpMethod getMethod() {
/* 1192 */       return this.request.method();
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpMethod method() {
/* 1197 */       return this.request.method();
/*      */     }
/*      */ 
/*      */     
/*      */     public String getUri() {
/* 1202 */       return this.request.uri();
/*      */     }
/*      */ 
/*      */     
/*      */     public String uri() {
/* 1207 */       return this.request.uri();
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpVersion getProtocolVersion() {
/* 1212 */       return this.request.protocolVersion();
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpVersion protocolVersion() {
/* 1217 */       return this.request.protocolVersion();
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpHeaders headers() {
/* 1222 */       return this.request.headers();
/*      */     }
/*      */ 
/*      */     
/*      */     public DecoderResult decoderResult() {
/* 1227 */       return this.request.decoderResult();
/*      */     }
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public DecoderResult getDecoderResult() {
/* 1233 */       return this.request.getDecoderResult();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setDecoderResult(DecoderResult result) {
/* 1238 */       this.request.setDecoderResult(result);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class WrappedFullHttpRequest extends WrappedHttpRequest implements FullHttpRequest {
/*      */     private final HttpContent content;
/*      */     
/*      */     private WrappedFullHttpRequest(HttpRequest request, HttpContent content) {
/* 1246 */       super(request);
/* 1247 */       this.content = content;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest setProtocolVersion(HttpVersion version) {
/* 1252 */       super.setProtocolVersion(version);
/* 1253 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest setMethod(HttpMethod method) {
/* 1258 */       super.setMethod(method);
/* 1259 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest setUri(String uri) {
/* 1264 */       super.setUri(uri);
/* 1265 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest copy() {
/* 1270 */       return replace(content().copy());
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest duplicate() {
/* 1275 */       return replace(content().duplicate());
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest retainedDuplicate() {
/* 1280 */       return replace(content().retainedDuplicate());
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest replace(ByteBuf content) {
/* 1285 */       DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content);
/* 1286 */       duplicate.headers().set(headers());
/* 1287 */       duplicate.trailingHeaders().set(trailingHeaders());
/* 1288 */       return (FullHttpRequest)duplicate;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest retain(int increment) {
/* 1293 */       this.content.retain(increment);
/* 1294 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest retain() {
/* 1299 */       this.content.retain();
/* 1300 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest touch() {
/* 1305 */       this.content.touch();
/* 1306 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public FullHttpRequest touch(Object hint) {
/* 1311 */       this.content.touch(hint);
/* 1312 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public ByteBuf content() {
/* 1317 */       return this.content.content();
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpHeaders trailingHeaders() {
/* 1322 */       if (this.content instanceof LastHttpContent) {
/* 1323 */         return ((LastHttpContent)this.content).trailingHeaders();
/*      */       }
/* 1325 */       return (HttpHeaders)EmptyHttpHeaders.INSTANCE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int refCnt() {
/* 1331 */       return this.content.refCnt();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean release() {
/* 1336 */       return this.content.release();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean release(int decrement) {
/* 1341 */       return this.content.release(decrement);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\HttpPostRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */