/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.io.File;
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
/*     */ 
/*     */ public class DiskFileUpload
/*     */   extends AbstractDiskHttpData
/*     */   implements FileUpload
/*     */ {
/*     */   public static String baseDirectory;
/*     */   public static boolean deleteOnExitTemporaryFile = true;
/*     */   public static final String prefix = "FUp_";
/*     */   public static final String postfix = ".tmp";
/*     */   private String filename;
/*     */   private String contentType;
/*     */   private String contentTransferEncoding;
/*     */   
/*     */   public DiskFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
/*  47 */     super(name, charset, size);
/*  48 */     setFilename(filename);
/*  49 */     setContentType(contentType);
/*  50 */     setContentTransferEncoding(contentTransferEncoding);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType() {
/*  55 */     return InterfaceHttpData.HttpDataType.FileUpload;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/*  60 */     return this.filename;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFilename(String filename) {
/*  65 */     if (filename == null) {
/*  66 */       throw new NullPointerException("filename");
/*     */     }
/*  68 */     this.filename = filename;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  73 */     return FileUploadUtil.hashCode(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  78 */     return (o instanceof FileUpload && FileUploadUtil.equals(this, (FileUpload)o));
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(InterfaceHttpData o) {
/*  83 */     if (!(o instanceof FileUpload)) {
/*  84 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o
/*  85 */           .getHttpDataType());
/*     */     }
/*  87 */     return compareTo((FileUpload)o);
/*     */   }
/*     */   
/*     */   public int compareTo(FileUpload o) {
/*  91 */     return FileUploadUtil.compareTo(this, o);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentType(String contentType) {
/*  96 */     if (contentType == null) {
/*  97 */       throw new NullPointerException("contentType");
/*     */     }
/*  99 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 104 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentTransferEncoding() {
/* 109 */     return this.contentTransferEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentTransferEncoding(String contentTransferEncoding) {
/* 114 */     this.contentTransferEncoding = contentTransferEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     File file = null;
/*     */     try {
/* 121 */       file = getFile();
/* 122 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 126 */     return HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + 
/* 127 */       getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + this.filename + "\"\r\n" + HttpHeaderNames.CONTENT_TYPE + ": " + this.contentType + (
/*     */ 
/*     */       
/* 130 */       (getCharset() != null) ? ("; " + HttpHeaderValues.CHARSET + '=' + getCharset().name() + "\r\n") : "\r\n") + HttpHeaderNames.CONTENT_LENGTH + ": " + 
/* 131 */       length() + "\r\nCompleted: " + 
/* 132 */       isCompleted() + "\r\nIsInMemory: " + 
/* 133 */       isInMemory() + "\r\nRealFile: " + ((file != null) ? file
/* 134 */       .getAbsolutePath() : "null") + " DefaultDeleteAfter: " + deleteOnExitTemporaryFile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean deleteOnExit() {
/* 140 */     return deleteOnExitTemporaryFile;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getBaseDirectory() {
/* 145 */     return baseDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDiskFilename() {
/* 150 */     return "upload";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getPostfix() {
/* 155 */     return ".tmp";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/* 160 */     return "FUp_";
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload copy() {
/* 165 */     ByteBuf content = content();
/* 166 */     return replace((content != null) ? content.copy() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload duplicate() {
/* 171 */     ByteBuf content = content();
/* 172 */     return replace((content != null) ? content.duplicate() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload retainedDuplicate() {
/* 177 */     ByteBuf content = content();
/* 178 */     if (content != null) {
/* 179 */       content = content.retainedDuplicate();
/* 180 */       boolean success = false;
/*     */       try {
/* 182 */         FileUpload duplicate = replace(content);
/* 183 */         success = true;
/* 184 */         return duplicate;
/*     */       } finally {
/* 186 */         if (!success) {
/* 187 */           content.release();
/*     */         }
/*     */       } 
/*     */     } 
/* 191 */     return replace((ByteBuf)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileUpload replace(ByteBuf content) {
/* 198 */     DiskFileUpload upload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
/* 199 */     if (content != null) {
/*     */       try {
/* 201 */         upload.setContent(content);
/* 202 */       } catch (IOException e) {
/* 203 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 206 */     return upload;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload retain(int increment) {
/* 211 */     super.retain(increment);
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload retain() {
/* 217 */     super.retain();
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload touch() {
/* 223 */     super.touch();
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FileUpload touch(Object hint) {
/* 229 */     super.touch(hint);
/* 230 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\DiskFileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */