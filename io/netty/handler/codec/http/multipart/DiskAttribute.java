/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpConstants;
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
/*     */ 
/*     */ public class DiskAttribute
/*     */   extends AbstractDiskHttpData
/*     */   implements Attribute
/*     */ {
/*     */   public static String baseDirectory;
/*     */   public static boolean deleteOnExitTemporaryFile = true;
/*     */   public static final String prefix = "Attr_";
/*     */   public static final String postfix = ".att";
/*     */   
/*     */   public DiskAttribute(String name) {
/*  43 */     this(name, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, long definedSize) {
/*  47 */     this(name, definedSize, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, Charset charset) {
/*  51 */     super(name, charset, 0L);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, long definedSize, Charset charset) {
/*  55 */     super(name, charset, definedSize);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, String value) throws IOException {
/*  59 */     this(name, value, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public DiskAttribute(String name, String value, Charset charset) throws IOException {
/*  63 */     super(name, charset, 0L);
/*  64 */     setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType() {
/*  69 */     return InterfaceHttpData.HttpDataType.Attribute;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() throws IOException {
/*  74 */     byte[] bytes = get();
/*  75 */     return new String(bytes, getCharset());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(String value) throws IOException {
/*  80 */     if (value == null) {
/*  81 */       throw new NullPointerException("value");
/*     */     }
/*  83 */     byte[] bytes = value.getBytes(getCharset());
/*  84 */     checkSize(bytes.length);
/*  85 */     ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
/*  86 */     if (this.definedSize > 0L) {
/*  87 */       this.definedSize = buffer.readableBytes();
/*     */     }
/*  89 */     setContent(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException {
/*  94 */     long newDefinedSize = this.size + buffer.readableBytes();
/*  95 */     checkSize(newDefinedSize);
/*  96 */     if (this.definedSize > 0L && this.definedSize < newDefinedSize) {
/*  97 */       this.definedSize = newDefinedSize;
/*     */     }
/*  99 */     super.addContent(buffer, last);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 109 */     if (!(o instanceof Attribute)) {
/* 110 */       return false;
/*     */     }
/* 112 */     Attribute attribute = (Attribute)o;
/* 113 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(InterfaceHttpData o) {
/* 118 */     if (!(o instanceof Attribute)) {
/* 119 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o
/* 120 */           .getHttpDataType());
/*     */     }
/* 122 */     return compareTo((Attribute)o);
/*     */   }
/*     */   
/*     */   public int compareTo(Attribute o) {
/* 126 */     return getName().compareToIgnoreCase(o.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 132 */       return getName() + '=' + getValue();
/* 133 */     } catch (IOException e) {
/* 134 */       return getName() + '=' + e;
/*     */     } 
/*     */   }
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
/* 150 */     return getName() + ".att";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getPostfix() {
/* 155 */     return ".att";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/* 160 */     return "Attr_";
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute copy() {
/* 165 */     ByteBuf content = content();
/* 166 */     return replace((content != null) ? content.copy() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute duplicate() {
/* 171 */     ByteBuf content = content();
/* 172 */     return replace((content != null) ? content.duplicate() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute retainedDuplicate() {
/* 177 */     ByteBuf content = content();
/* 178 */     if (content != null) {
/* 179 */       content = content.retainedDuplicate();
/* 180 */       boolean success = false;
/*     */       try {
/* 182 */         Attribute duplicate = replace(content);
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
/*     */   public Attribute replace(ByteBuf content) {
/* 197 */     DiskAttribute attr = new DiskAttribute(getName());
/* 198 */     attr.setCharset(getCharset());
/* 199 */     if (content != null) {
/*     */       try {
/* 201 */         attr.setContent(content);
/* 202 */       } catch (IOException e) {
/* 203 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 206 */     return attr;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute retain(int increment) {
/* 211 */     super.retain(increment);
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute retain() {
/* 217 */     super.retain();
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute touch() {
/* 223 */     super.touch();
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute touch(Object hint) {
/* 229 */     super.touch(hint);
/* 230 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\DiskAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */