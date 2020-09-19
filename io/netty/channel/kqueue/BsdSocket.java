/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.unix.Errors;
/*     */ import io.netty.channel.unix.PeerCredentials;
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ClosedChannelException;
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
/*     */ final class BsdSocket
/*     */   extends Socket
/*     */ {
/*     */   private static final Errors.NativeIoException SENDFILE_CONNECTION_RESET_EXCEPTION;
/*  37 */   private static final ClosedChannelException SENDFILE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "sendfile(..)");
/*     */ 
/*     */   
/*     */   private static final int APPLE_SND_LOW_AT_MAX = 131072;
/*     */   
/*     */   private static final int FREEBSD_SND_LOW_AT_MAX = 32768;
/*     */   
/*  44 */   static final int BSD_SND_LOW_AT_MAX = Math.min(131072, 32768);
/*     */   
/*     */   static {
/*  47 */     SENDFILE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendfile", Errors.ERRNO_EPIPE_NEGATIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   BsdSocket(int fd) {
/*  52 */     super(fd);
/*     */   }
/*     */   
/*     */   void setAcceptFilter(AcceptFilter acceptFilter) throws IOException {
/*  56 */     setAcceptFilter(intValue(), acceptFilter.filterName(), acceptFilter.filterArgs());
/*     */   }
/*     */   
/*     */   void setTcpNoPush(boolean tcpNoPush) throws IOException {
/*  60 */     setTcpNoPush(intValue(), tcpNoPush ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setSndLowAt(int lowAt) throws IOException {
/*  64 */     setSndLowAt(intValue(), lowAt);
/*     */   }
/*     */   
/*     */   boolean isTcpNoPush() throws IOException {
/*  68 */     return (getTcpNoPush(intValue()) != 0);
/*     */   }
/*     */   
/*     */   int getSndLowAt() throws IOException {
/*  72 */     return getSndLowAt(intValue());
/*     */   }
/*     */   
/*     */   AcceptFilter getAcceptFilter() throws IOException {
/*  76 */     String[] result = getAcceptFilter(intValue());
/*  77 */     return (result == null) ? AcceptFilter.PLATFORM_UNSUPPORTED : new AcceptFilter(result[0], result[1]);
/*     */   }
/*     */   
/*     */   PeerCredentials getPeerCredentials() throws IOException {
/*  81 */     return getPeerCredentials(intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   long sendFile(DefaultFileRegion src, long baseOffset, long offset, long length) throws IOException {
/*  87 */     src.open();
/*     */     
/*  89 */     long res = sendFile(intValue(), src, baseOffset, offset, length);
/*  90 */     if (res >= 0L) {
/*  91 */       return res;
/*     */     }
/*  93 */     return Errors.ioResult("sendfile", (int)res, SENDFILE_CONNECTION_RESET_EXCEPTION, SENDFILE_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */   
/*     */   public static BsdSocket newSocketStream() {
/*  97 */     return new BsdSocket(newSocketStream0());
/*     */   }
/*     */   
/*     */   public static BsdSocket newSocketDgram() {
/* 101 */     return new BsdSocket(newSocketDgram0());
/*     */   }
/*     */   
/*     */   public static BsdSocket newSocketDomain() {
/* 105 */     return new BsdSocket(newSocketDomain0());
/*     */   }
/*     */   
/*     */   private static native long sendFile(int paramInt, DefaultFileRegion paramDefaultFileRegion, long paramLong1, long paramLong2, long paramLong3) throws IOException;
/*     */   
/*     */   private static native String[] getAcceptFilter(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTcpNoPush(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getSndLowAt(int paramInt) throws IOException;
/*     */   
/*     */   private static native PeerCredentials getPeerCredentials(int paramInt) throws IOException;
/*     */   
/*     */   private static native void setAcceptFilter(int paramInt, String paramString1, String paramString2) throws IOException;
/*     */   
/*     */   private static native void setTcpNoPush(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setSndLowAt(int paramInt1, int paramInt2) throws IOException;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\BsdSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */