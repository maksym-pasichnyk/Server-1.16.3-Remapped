/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OioByteStreamChannel
/*     */   extends AbstractOioByteChannel
/*     */ {
/*  37 */   private static final InputStream CLOSED_IN = new InputStream()
/*     */     {
/*     */       public int read() {
/*  40 */         return -1;
/*     */       }
/*     */     };
/*     */   
/*  44 */   private static final OutputStream CLOSED_OUT = new OutputStream()
/*     */     {
/*     */       public void write(int b) throws IOException {
/*  47 */         throw new ClosedChannelException();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream is;
/*     */ 
/*     */   
/*     */   private OutputStream os;
/*     */   
/*     */   private WritableByteChannel outChannel;
/*     */ 
/*     */   
/*     */   protected OioByteStreamChannel(Channel parent) {
/*  62 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void activate(InputStream is, OutputStream os) {
/*  69 */     if (this.is != null) {
/*  70 */       throw new IllegalStateException("input was set already");
/*     */     }
/*  72 */     if (this.os != null) {
/*  73 */       throw new IllegalStateException("output was set already");
/*     */     }
/*  75 */     if (is == null) {
/*  76 */       throw new NullPointerException("is");
/*     */     }
/*  78 */     if (os == null) {
/*  79 */       throw new NullPointerException("os");
/*     */     }
/*  81 */     this.is = is;
/*  82 */     this.os = os;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/*  87 */     InputStream is = this.is;
/*  88 */     if (is == null || is == CLOSED_IN) {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     OutputStream os = this.os;
/*  93 */     return (os != null && os != CLOSED_OUT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int available() {
/*     */     try {
/*  99 */       return this.is.available();
/* 100 */     } catch (IOException ignored) {
/* 101 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doReadBytes(ByteBuf buf) throws Exception {
/* 107 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 108 */     allocHandle.attemptedBytesRead(Math.max(1, Math.min(available(), buf.maxWritableBytes())));
/* 109 */     return buf.writeBytes(this.is, allocHandle.attemptedBytesRead());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWriteBytes(ByteBuf buf) throws Exception {
/* 114 */     OutputStream os = this.os;
/* 115 */     if (os == null) {
/* 116 */       throw new NotYetConnectedException();
/*     */     }
/* 118 */     buf.readBytes(os, buf.readableBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWriteFileRegion(FileRegion region) throws Exception {
/* 123 */     OutputStream os = this.os;
/* 124 */     if (os == null) {
/* 125 */       throw new NotYetConnectedException();
/*     */     }
/* 127 */     if (this.outChannel == null) {
/* 128 */       this.outChannel = Channels.newChannel(os);
/*     */     }
/*     */     
/* 131 */     long written = 0L;
/*     */     do {
/* 133 */       long localWritten = region.transferTo(this.outChannel, written);
/* 134 */       if (localWritten == -1L) {
/* 135 */         checkEOF(region);
/*     */         return;
/*     */       } 
/* 138 */       written += localWritten;
/*     */     }
/* 140 */     while (written < region.count());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkEOF(FileRegion region) throws IOException {
/* 147 */     if (region.transferred() < region.count()) {
/* 148 */       throw new EOFException("Expected to be able to write " + region.count() + " bytes, but only wrote " + region
/* 149 */           .transferred());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 155 */     InputStream is = this.is;
/* 156 */     OutputStream os = this.os;
/* 157 */     this.is = CLOSED_IN;
/* 158 */     this.os = CLOSED_OUT;
/*     */     
/*     */     try {
/* 161 */       if (is != null) {
/* 162 */         is.close();
/*     */       }
/*     */     } finally {
/* 165 */       if (os != null)
/* 166 */         os.close(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\oio\OioByteStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */