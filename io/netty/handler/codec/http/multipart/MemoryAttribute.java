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
/*     */ public class MemoryAttribute
/*     */   extends AbstractMemoryHttpData
/*     */   implements Attribute
/*     */ {
/*     */   public MemoryAttribute(String name) {
/*  33 */     this(name, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, long definedSize) {
/*  37 */     this(name, definedSize, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, Charset charset) {
/*  41 */     super(name, charset, 0L);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, long definedSize, Charset charset) {
/*  45 */     super(name, charset, definedSize);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, String value) throws IOException {
/*  49 */     this(name, value, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MemoryAttribute(String name, String value, Charset charset) throws IOException {
/*  53 */     super(name, charset, 0L);
/*  54 */     setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType() {
/*  59 */     return InterfaceHttpData.HttpDataType.Attribute;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  64 */     return getByteBuf().toString(getCharset());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(String value) throws IOException {
/*  69 */     if (value == null) {
/*  70 */       throw new NullPointerException("value");
/*     */     }
/*  72 */     byte[] bytes = value.getBytes(getCharset());
/*  73 */     checkSize(bytes.length);
/*  74 */     ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
/*  75 */     if (this.definedSize > 0L) {
/*  76 */       this.definedSize = buffer.readableBytes();
/*     */     }
/*  78 */     setContent(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException {
/*  83 */     int localsize = buffer.readableBytes();
/*  84 */     checkSize(this.size + localsize);
/*  85 */     if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
/*  86 */       this.definedSize = this.size + localsize;
/*     */     }
/*  88 */     super.addContent(buffer, last);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  98 */     if (!(o instanceof Attribute)) {
/*  99 */       return false;
/*     */     }
/* 101 */     Attribute attribute = (Attribute)o;
/* 102 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(InterfaceHttpData other) {
/* 107 */     if (!(other instanceof Attribute)) {
/* 108 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + other
/* 109 */           .getHttpDataType());
/*     */     }
/* 111 */     return compareTo((Attribute)other);
/*     */   }
/*     */   
/*     */   public int compareTo(Attribute o) {
/* 115 */     return getName().compareToIgnoreCase(o.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return getName() + '=' + getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute copy() {
/* 125 */     ByteBuf content = content();
/* 126 */     return replace((content != null) ? content.copy() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute duplicate() {
/* 131 */     ByteBuf content = content();
/* 132 */     return replace((content != null) ? content.duplicate() : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute retainedDuplicate() {
/* 137 */     ByteBuf content = content();
/* 138 */     if (content != null) {
/* 139 */       content = content.retainedDuplicate();
/* 140 */       boolean success = false;
/*     */       try {
/* 142 */         Attribute duplicate = replace(content);
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
/*     */   public Attribute replace(ByteBuf content) {
/* 157 */     MemoryAttribute attr = new MemoryAttribute(getName());
/* 158 */     attr.setCharset(getCharset());
/* 159 */     if (content != null) {
/*     */       try {
/* 161 */         attr.setContent(content);
/* 162 */       } catch (IOException e) {
/* 163 */         throw new ChannelException(e);
/*     */       } 
/*     */     }
/* 166 */     return attr;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute retain() {
/* 171 */     super.retain();
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute retain(int increment) {
/* 177 */     super.retain(increment);
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute touch() {
/* 183 */     super.touch();
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute touch(Object hint) {
/* 189 */     super.touch(hint);
/* 190 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\MemoryAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */