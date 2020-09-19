/*     */ package io.netty.handler.codec.serialization;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectEncoderOutputStream
/*     */   extends OutputStream
/*     */   implements ObjectOutput
/*     */ {
/*     */   private final DataOutputStream out;
/*     */   private final int estimatedLength;
/*     */   
/*     */   public ObjectEncoderOutputStream(OutputStream out) {
/*  47 */     this(out, 512);
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
/*     */   public ObjectEncoderOutputStream(OutputStream out, int estimatedLength) {
/*  66 */     if (out == null) {
/*  67 */       throw new NullPointerException("out");
/*     */     }
/*  69 */     if (estimatedLength < 0) {
/*  70 */       throw new IllegalArgumentException("estimatedLength: " + estimatedLength);
/*     */     }
/*     */     
/*  73 */     if (out instanceof DataOutputStream) {
/*  74 */       this.out = (DataOutputStream)out;
/*     */     } else {
/*  76 */       this.out = new DataOutputStream(out);
/*     */     } 
/*  78 */     this.estimatedLength = estimatedLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeObject(Object obj) throws IOException {
/*  83 */     ByteBuf buf = Unpooled.buffer(this.estimatedLength);
/*     */     try {
/*  85 */       ObjectOutputStream oout = new CompactObjectOutputStream((OutputStream)new ByteBufOutputStream(buf));
/*     */       try {
/*  87 */         oout.writeObject(obj);
/*  88 */         oout.flush();
/*     */       } finally {
/*  90 */         oout.close();
/*     */       } 
/*     */       
/*  93 */       int objectSize = buf.readableBytes();
/*  94 */       writeInt(objectSize);
/*  95 */       buf.getBytes(0, this, objectSize);
/*     */     } finally {
/*  97 */       buf.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 103 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 108 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 113 */     this.out.flush();
/*     */   }
/*     */   
/*     */   public final int size() {
/* 117 */     return this.out.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 122 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 127 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeBoolean(boolean v) throws IOException {
/* 132 */     this.out.writeBoolean(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeByte(int v) throws IOException {
/* 137 */     this.out.writeByte(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeBytes(String s) throws IOException {
/* 142 */     this.out.writeBytes(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeChar(int v) throws IOException {
/* 147 */     this.out.writeChar(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeChars(String s) throws IOException {
/* 152 */     this.out.writeChars(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeDouble(double v) throws IOException {
/* 157 */     this.out.writeDouble(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeFloat(float v) throws IOException {
/* 162 */     this.out.writeFloat(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeInt(int v) throws IOException {
/* 167 */     this.out.writeInt(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeLong(long v) throws IOException {
/* 172 */     this.out.writeLong(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeShort(int v) throws IOException {
/* 177 */     this.out.writeShort(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeUTF(String str) throws IOException {
/* 182 */     this.out.writeUTF(str);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\serialization\ObjectEncoderOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */