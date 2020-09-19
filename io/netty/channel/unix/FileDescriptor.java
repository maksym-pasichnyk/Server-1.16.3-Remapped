/*     */ package io.netty.channel.unix;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ public class FileDescriptor
/*     */ {
/*  37 */   private static final ClosedChannelException WRITE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), FileDescriptor.class, "write(..)");
/*     */ 
/*     */   
/*  40 */   private static final ClosedChannelException WRITE_ADDRESS_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), FileDescriptor.class, "writeAddress(..)");
/*  41 */   private static final ClosedChannelException WRITEV_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), FileDescriptor.class, "writev(..)");
/*     */ 
/*     */   
/*  44 */   private static final ClosedChannelException WRITEV_ADDRESSES_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), FileDescriptor.class, "writevAddresses(..)");
/*  45 */   private static final ClosedChannelException READ_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), FileDescriptor.class, "read(..)");
/*     */   
/*  47 */   private static final ClosedChannelException READ_ADDRESS_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), FileDescriptor.class, "readAddress(..)");
/*     */   
/*  49 */   private static final Errors.NativeIoException WRITE_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(
/*  50 */       Errors.newConnectionResetException("syscall:write", Errors.ERRNO_EPIPE_NEGATIVE), FileDescriptor.class, "write(..)");
/*     */ 
/*     */   
/*  53 */   private static final Errors.NativeIoException WRITE_ADDRESS_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:write", Errors.ERRNO_EPIPE_NEGATIVE), FileDescriptor.class, "writeAddress(..)");
/*     */   
/*  55 */   private static final Errors.NativeIoException WRITEV_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(
/*  56 */       Errors.newConnectionResetException("syscall:writev", Errors.ERRNO_EPIPE_NEGATIVE), FileDescriptor.class, "writev(..)");
/*     */ 
/*     */   
/*  59 */   private static final Errors.NativeIoException WRITEV_ADDRESSES_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:writev", Errors.ERRNO_EPIPE_NEGATIVE), FileDescriptor.class, "writeAddresses(..)");
/*     */   
/*  61 */   private static final Errors.NativeIoException READ_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(
/*  62 */       Errors.newConnectionResetException("syscall:read", Errors.ERRNO_ECONNRESET_NEGATIVE), FileDescriptor.class, "read(..)");
/*     */ 
/*     */   
/*  65 */   private static final Errors.NativeIoException READ_ADDRESS_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:read", Errors.ERRNO_ECONNRESET_NEGATIVE), FileDescriptor.class, "readAddress(..)");
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final AtomicIntegerFieldUpdater<FileDescriptor> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(FileDescriptor.class, "state");
/*     */   
/*     */   private static final int STATE_CLOSED_MASK = 1;
/*     */   
/*     */   private static final int STATE_INPUT_SHUTDOWN_MASK = 2;
/*     */   
/*     */   private static final int STATE_OUTPUT_SHUTDOWN_MASK = 4;
/*     */   
/*     */   private static final int STATE_ALL_MASK = 7;
/*     */   
/*     */   volatile int state;
/*     */   
/*     */   final int fd;
/*     */ 
/*     */   
/*     */   public FileDescriptor(int fd) {
/*  85 */     if (fd < 0) {
/*  86 */       throw new IllegalArgumentException("fd must be >= 0");
/*     */     }
/*  88 */     this.fd = fd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int intValue() {
/*  95 */     return this.fd;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     int state;
/*     */     do {
/* 103 */       state = this.state;
/* 104 */       if (isClosed(state)) {
/*     */         return;
/*     */       }
/*     */     }
/* 108 */     while (!casState(state, state | 0x7));
/*     */ 
/*     */ 
/*     */     
/* 112 */     int res = close(this.fd);
/* 113 */     if (res < 0) {
/* 114 */       throw Errors.newIOException("close", res);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 122 */     return !isClosed(this.state);
/*     */   }
/*     */   
/*     */   public final int write(ByteBuffer buf, int pos, int limit) throws IOException {
/* 126 */     int res = write(this.fd, buf, pos, limit);
/* 127 */     if (res >= 0) {
/* 128 */       return res;
/*     */     }
/* 130 */     return Errors.ioResult("write", res, WRITE_CONNECTION_RESET_EXCEPTION, WRITE_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */   
/*     */   public final int writeAddress(long address, int pos, int limit) throws IOException {
/* 134 */     int res = writeAddress(this.fd, address, pos, limit);
/* 135 */     if (res >= 0) {
/* 136 */       return res;
/*     */     }
/* 138 */     return Errors.ioResult("writeAddress", res, WRITE_ADDRESS_CONNECTION_RESET_EXCEPTION, WRITE_ADDRESS_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long writev(ByteBuffer[] buffers, int offset, int length, long maxBytesToWrite) throws IOException {
/* 143 */     long res = writev(this.fd, buffers, offset, Math.min(Limits.IOV_MAX, length), maxBytesToWrite);
/* 144 */     if (res >= 0L) {
/* 145 */       return res;
/*     */     }
/* 147 */     return Errors.ioResult("writev", (int)res, WRITEV_CONNECTION_RESET_EXCEPTION, WRITEV_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */   
/*     */   public final long writevAddresses(long memoryAddress, int length) throws IOException {
/* 151 */     long res = writevAddresses(this.fd, memoryAddress, length);
/* 152 */     if (res >= 0L) {
/* 153 */       return res;
/*     */     }
/* 155 */     return Errors.ioResult("writevAddresses", (int)res, WRITEV_ADDRESSES_CONNECTION_RESET_EXCEPTION, WRITEV_ADDRESSES_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int read(ByteBuffer buf, int pos, int limit) throws IOException {
/* 160 */     int res = read(this.fd, buf, pos, limit);
/* 161 */     if (res > 0) {
/* 162 */       return res;
/*     */     }
/* 164 */     if (res == 0) {
/* 165 */       return -1;
/*     */     }
/* 167 */     return Errors.ioResult("read", res, READ_CONNECTION_RESET_EXCEPTION, READ_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */   
/*     */   public final int readAddress(long address, int pos, int limit) throws IOException {
/* 171 */     int res = readAddress(this.fd, address, pos, limit);
/* 172 */     if (res > 0) {
/* 173 */       return res;
/*     */     }
/* 175 */     if (res == 0) {
/* 176 */       return -1;
/*     */     }
/* 178 */     return Errors.ioResult("readAddress", res, READ_ADDRESS_CONNECTION_RESET_EXCEPTION, READ_ADDRESS_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return "FileDescriptor{fd=" + this.fd + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 191 */     if (this == o) {
/* 192 */       return true;
/*     */     }
/* 194 */     if (!(o instanceof FileDescriptor)) {
/* 195 */       return false;
/*     */     }
/*     */     
/* 198 */     return (this.fd == ((FileDescriptor)o).fd);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 203 */     return this.fd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileDescriptor from(String path) throws IOException {
/* 210 */     ObjectUtil.checkNotNull(path, "path");
/* 211 */     int res = open(path);
/* 212 */     if (res < 0) {
/* 213 */       throw Errors.newIOException("open", res);
/*     */     }
/* 215 */     return new FileDescriptor(res);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileDescriptor from(File file) throws IOException {
/* 222 */     return from(((File)ObjectUtil.checkNotNull(file, "file")).getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileDescriptor[] pipe() throws IOException {
/* 229 */     long res = newPipe();
/* 230 */     if (res < 0L) {
/* 231 */       throw Errors.newIOException("newPipe", (int)res);
/*     */     }
/* 233 */     return new FileDescriptor[] { new FileDescriptor((int)(res >>> 32L)), new FileDescriptor((int)res) };
/*     */   }
/*     */   
/*     */   final boolean casState(int expected, int update) {
/* 237 */     return stateUpdater.compareAndSet(this, expected, update);
/*     */   }
/*     */   
/*     */   static boolean isClosed(int state) {
/* 241 */     return ((state & 0x1) != 0);
/*     */   }
/*     */   
/*     */   static boolean isInputShutdown(int state) {
/* 245 */     return ((state & 0x2) != 0);
/*     */   }
/*     */   
/*     */   static boolean isOutputShutdown(int state) {
/* 249 */     return ((state & 0x4) != 0);
/*     */   }
/*     */   
/*     */   static int inputShutdown(int state) {
/* 253 */     return state | 0x2;
/*     */   }
/*     */   
/*     */   static int outputShutdown(int state) {
/* 257 */     return state | 0x4;
/*     */   }
/*     */   
/*     */   private static native int open(String paramString);
/*     */   
/*     */   private static native int close(int paramInt);
/*     */   
/*     */   private static native int write(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int writeAddress(int paramInt1, long paramLong, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native long writev(int paramInt1, ByteBuffer[] paramArrayOfByteBuffer, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   private static native long writevAddresses(int paramInt1, long paramLong, int paramInt2);
/*     */   
/*     */   private static native int read(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int readAddress(int paramInt1, long paramLong, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native long newPipe();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\FileDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */