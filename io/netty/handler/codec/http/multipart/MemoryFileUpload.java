/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryFileUpload
/*     */   extends AbstractMemoryHttpData
/*     */   implements FileUpload
/*     */ {
/*     */   private String filename;
/*     */   private String contentType;
/*     */   private String contentTransferEncoding;
/*     */   
/*     */   public MemoryFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
/*  41 */     super(name, charset, size);
/*  42 */     setFilename(filename);
/*  43 */     setContentType(contentType);
/*  44 */     setContentTransferEncoding(contentTransferEncoding);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType() {
/*  49 */     return InterfaceHttpData.HttpDataType.FileUpload;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/*  54 */     return this.filename;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFilename(String filename) {
/*  59 */     if (filename == null) {
/*  60 */       throw new NullPointerException("filename");
/*     */     }
/*  62 */     this.filename = filename;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  67 */     return FileUploadUtil.hashCode(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  72 */     return (o instanceof FileUpload && FileUploadUtil.equals(this, (FileUpload)o));
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(InterfaceHttpData o) {
/*  77 */     if (!(o instanceof FileUpload)) {
/*  78 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o
/*  79 */           .getHttpDataType());
/*     */     }
/*  81 */     return compareTo((FileUpload)o);
/*     */   }
/*     */   
/*     */   public int compareTo(FileUpload o) {
/*  85 */     return FileUploadUtil.compareTo(this, o);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentType(String contentType) {
/*  90 */     if (contentType == null) {
/*  91 */       throw new NullPointerException("contentType");
/*     */     }
/*  93 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  98 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentTransferEncoding() {
/* 103 */     return this.contentTransferEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentTransferEncoding(String contentTransferEncoding) {
/* 108 */     this.contentTransferEncoding = contentTransferEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + 
/* 114 */       getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + this.filename + "\"\r\n" + HttpHeaderNames.CONTENT_TYPE + ": " + this.contentType + (
/*     */ 
/*     */       
/* 117 */       (getCharset() != null) ? ("; " + HttpHeaderValues.CHARSET + '=' + getCharset().name() + "\r\n") : "\r\n") + HttpHeaderNames.CONTENT_LENGTH + ": " + 
/* 118 */       length() + "\r\nCompleted: " + 
/* 119 */       isCompleted() + "\r\nIsInMemory: " + 
/* 120 */       isInMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload copy() {
/* 125 */     ByteBuf content = content();
/* 126 */     return replace((content != null) ? content.copy() : content);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload duplicate() {
/* 131 */     ByteBuf content = content();
/* 132 */     return replace((content != null) ? content.duplicate() : content);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload retainedDuplicate() {
/* 137 */     ByteBuf content = content();
/* 138 */     if (content != null) {
/* 139 */       content = content.retainedDuplicate();
/* 140 */       boolean success = false;
/*     */       try {
/* 142 */         FileUpload duplicate = replace(content);
/* 143 */         success = true;
/* 144 */         return duplicate;
/*     */       } finally {
/* 146 */         if (!success) {
/* 147 */           content.release();
/*     */         }
/*     */       } 
/*     */     } 
/* 151 */     return replace((ByteBuf)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileUpload replace(ByteBuf content) {
/* 158 */     MemoryFileUpload upload = new MemoryFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
/* 159 */     if (content != null) {
/*     */       try {
/* 161 */         upload.setContent(content);
/* 162 */         return upload;
/* 163 */       } catch (IOException e) {
/* 164 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 167 */     return upload;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload retain() {
/* 172 */     super.retain();
/* 173 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload retain(int increment) {
/* 178 */     super.retain(increment);
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload touch() {
/* 184 */     super.touch();
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload touch(Object hint) {
/* 190 */     super.touch(hint);
/* 191 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\MemoryFileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */