/*    */ package io.netty.channel.unix;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.WritableByteChannel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SocketWritableByteChannel
/*    */   implements WritableByteChannel
/*    */ {
/*    */   private final FileDescriptor fd;
/*    */   
/*    */   protected SocketWritableByteChannel(FileDescriptor fd) {
/* 26 */     this.fd = (FileDescriptor)ObjectUtil.checkNotNull(fd, "fd");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final int write(ByteBuffer src) throws IOException {
/* 32 */     int written, position = src.position();
/* 33 */     int limit = src.limit();
/* 34 */     if (src.isDirect()) {
/* 35 */       written = this.fd.write(src, position, src.limit());
/*    */     } else {
/* 37 */       int readableBytes = limit - position;
/* 38 */       ByteBuf buffer = null;
/*    */       try {
/* 40 */         if (readableBytes == 0) {
/* 41 */           buffer = Unpooled.EMPTY_BUFFER;
/*    */         } else {
/* 43 */           ByteBufAllocator alloc = alloc();
/* 44 */           if (alloc.isDirectBufferPooled()) {
/* 45 */             buffer = alloc.directBuffer(readableBytes);
/*    */           } else {
/* 47 */             buffer = ByteBufUtil.threadLocalDirectBuffer();
/* 48 */             if (buffer == null) {
/* 49 */               buffer = Unpooled.directBuffer(readableBytes);
/*    */             }
/*    */           } 
/*    */         } 
/* 53 */         buffer.writeBytes(src.duplicate());
/* 54 */         ByteBuffer nioBuffer = buffer.internalNioBuffer(buffer.readerIndex(), readableBytes);
/* 55 */         written = this.fd.write(nioBuffer, nioBuffer.position(), nioBuffer.limit());
/*    */       } finally {
/* 57 */         if (buffer != null) {
/* 58 */           buffer.release();
/*    */         }
/*    */       } 
/*    */     } 
/* 62 */     if (written > 0) {
/* 63 */       src.position(position + written);
/*    */     }
/* 65 */     return written;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isOpen() {
/* 70 */     return this.fd.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void close() throws IOException {
/* 75 */     this.fd.close();
/*    */   }
/*    */   
/*    */   protected abstract ByteBufAllocator alloc();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\SocketWritableByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */