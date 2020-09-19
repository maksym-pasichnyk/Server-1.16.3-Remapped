/*     */ package io.netty.channel.unix;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IovArray
/*     */   implements ChannelOutboundBuffer.MessageProcessor
/*     */ {
/*  55 */   private static final int ADDRESS_SIZE = PlatformDependent.addressSize();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final int IOV_SIZE = 2 * ADDRESS_SIZE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final int CAPACITY = Limits.IOV_MAX * IOV_SIZE;
/*     */   
/*     */   private final long memoryAddress;
/*     */   private int count;
/*     */   private long size;
/*  72 */   private long maxBytes = Limits.SSIZE_MAX;
/*     */   
/*     */   public IovArray() {
/*  75 */     this.memoryAddress = PlatformDependent.allocateMemory(CAPACITY);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  79 */     this.count = 0;
/*  80 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(ByteBuf buf) {
/*  91 */     if (this.count == Limits.IOV_MAX)
/*     */     {
/*  93 */       return false; } 
/*  94 */     if (buf.hasMemoryAddress() && buf.nioBufferCount() == 1) {
/*  95 */       int len = buf.readableBytes();
/*  96 */       return (len == 0 || add(buf.memoryAddress(), buf.readerIndex(), len));
/*     */     } 
/*  98 */     ByteBuffer[] buffers = buf.nioBuffers();
/*  99 */     for (ByteBuffer nioBuffer : buffers) {
/* 100 */       int len = nioBuffer.remaining();
/* 101 */       if (len != 0 && (!add(PlatformDependent.directBufferAddress(nioBuffer), nioBuffer.position(), len) || this.count == Limits.IOV_MAX)) {
/* 102 */         return false;
/*     */       }
/*     */     } 
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean add(long addr, int offset, int len) {
/* 110 */     long baseOffset = memoryAddress(this.count);
/* 111 */     long lengthOffset = baseOffset + ADDRESS_SIZE;
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (this.maxBytes - len < this.size && this.count > 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       return false;
/*     */     }
/* 124 */     this.size += len;
/* 125 */     this.count++;
/*     */     
/* 127 */     if (ADDRESS_SIZE == 8) {
/*     */       
/* 129 */       PlatformDependent.putLong(baseOffset, addr + offset);
/* 130 */       PlatformDependent.putLong(lengthOffset, len);
/*     */     } else {
/* 132 */       assert ADDRESS_SIZE == 4;
/* 133 */       PlatformDependent.putInt(baseOffset, (int)addr + offset);
/* 134 */       PlatformDependent.putInt(lengthOffset, len);
/*     */     } 
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int count() {
/* 143 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() {
/* 150 */     return this.size;
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
/*     */   public void maxBytes(long maxBytes) {
/* 164 */     this.maxBytes = Math.min(Limits.SSIZE_MAX, ObjectUtil.checkPositive(maxBytes, "maxBytes"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long maxBytes() {
/* 172 */     return this.maxBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long memoryAddress(int offset) {
/* 179 */     return this.memoryAddress + (IOV_SIZE * offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release() {
/* 186 */     PlatformDependent.freeMemory(this.memoryAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean processMessage(Object msg) throws Exception {
/* 191 */     return (msg instanceof ByteBuf && add((ByteBuf)msg));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\IovArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */