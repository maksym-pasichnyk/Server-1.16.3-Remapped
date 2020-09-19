/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.unix.Errors;
/*     */ import io.netty.channel.unix.NativeInetAddress;
/*     */ import io.netty.channel.unix.PeerCredentials;
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
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
/*     */ final class LinuxSocket
/*     */   extends Socket
/*     */ {
/*     */   private static final long MAX_UINT32_T = 4294967295L;
/*  39 */   private static final Errors.NativeIoException SENDFILE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendfile(...)", Errors.ERRNO_EPIPE_NEGATIVE);
/*  40 */   private static final ClosedChannelException SENDFILE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "sendfile(...)");
/*     */ 
/*     */   
/*     */   public LinuxSocket(int fd) {
/*  44 */     super(fd);
/*     */   }
/*     */   
/*     */   void setTcpDeferAccept(int deferAccept) throws IOException {
/*  48 */     setTcpDeferAccept(intValue(), deferAccept);
/*     */   }
/*     */   
/*     */   void setTcpQuickAck(boolean quickAck) throws IOException {
/*  52 */     setTcpQuickAck(intValue(), quickAck ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setTcpCork(boolean tcpCork) throws IOException {
/*  56 */     setTcpCork(intValue(), tcpCork ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setTcpNotSentLowAt(long tcpNotSentLowAt) throws IOException {
/*  60 */     if (tcpNotSentLowAt < 0L || tcpNotSentLowAt > 4294967295L) {
/*  61 */       throw new IllegalArgumentException("tcpNotSentLowAt must be a uint32_t");
/*     */     }
/*  63 */     setTcpNotSentLowAt(intValue(), (int)tcpNotSentLowAt);
/*     */   }
/*     */   
/*     */   void setTcpFastOpen(int tcpFastopenBacklog) throws IOException {
/*  67 */     setTcpFastOpen(intValue(), tcpFastopenBacklog);
/*     */   }
/*     */   
/*     */   void setTcpFastOpenConnect(boolean tcpFastOpenConnect) throws IOException {
/*  71 */     setTcpFastOpenConnect(intValue(), tcpFastOpenConnect ? 1 : 0);
/*     */   }
/*     */   
/*     */   boolean isTcpFastOpenConnect() throws IOException {
/*  75 */     return (isTcpFastOpenConnect(intValue()) != 0);
/*     */   }
/*     */   
/*     */   void setTcpKeepIdle(int seconds) throws IOException {
/*  79 */     setTcpKeepIdle(intValue(), seconds);
/*     */   }
/*     */   
/*     */   void setTcpKeepIntvl(int seconds) throws IOException {
/*  83 */     setTcpKeepIntvl(intValue(), seconds);
/*     */   }
/*     */   
/*     */   void setTcpKeepCnt(int probes) throws IOException {
/*  87 */     setTcpKeepCnt(intValue(), probes);
/*     */   }
/*     */   
/*     */   void setTcpUserTimeout(int milliseconds) throws IOException {
/*  91 */     setTcpUserTimeout(intValue(), milliseconds);
/*     */   }
/*     */   
/*     */   void setIpFreeBind(boolean enabled) throws IOException {
/*  95 */     setIpFreeBind(intValue(), enabled ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setIpTransparent(boolean enabled) throws IOException {
/*  99 */     setIpTransparent(intValue(), enabled ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setIpRecvOrigDestAddr(boolean enabled) throws IOException {
/* 103 */     setIpRecvOrigDestAddr(intValue(), enabled ? 1 : 0);
/*     */   }
/*     */   
/*     */   void getTcpInfo(EpollTcpInfo info) throws IOException {
/* 107 */     getTcpInfo(intValue(), info.info);
/*     */   }
/*     */   
/*     */   void setTcpMd5Sig(InetAddress address, byte[] key) throws IOException {
/* 111 */     NativeInetAddress a = NativeInetAddress.newInstance(address);
/* 112 */     setTcpMd5Sig(intValue(), a.address(), a.scopeId(), key);
/*     */   }
/*     */   
/*     */   boolean isTcpCork() throws IOException {
/* 116 */     return (isTcpCork(intValue()) != 0);
/*     */   }
/*     */   
/*     */   int getTcpDeferAccept() throws IOException {
/* 120 */     return getTcpDeferAccept(intValue());
/*     */   }
/*     */   
/*     */   boolean isTcpQuickAck() throws IOException {
/* 124 */     return (isTcpQuickAck(intValue()) != 0);
/*     */   }
/*     */   
/*     */   long getTcpNotSentLowAt() throws IOException {
/* 128 */     return getTcpNotSentLowAt(intValue()) & 0xFFFFFFFFL;
/*     */   }
/*     */   
/*     */   int getTcpKeepIdle() throws IOException {
/* 132 */     return getTcpKeepIdle(intValue());
/*     */   }
/*     */   
/*     */   int getTcpKeepIntvl() throws IOException {
/* 136 */     return getTcpKeepIntvl(intValue());
/*     */   }
/*     */   
/*     */   int getTcpKeepCnt() throws IOException {
/* 140 */     return getTcpKeepCnt(intValue());
/*     */   }
/*     */   
/*     */   int getTcpUserTimeout() throws IOException {
/* 144 */     return getTcpUserTimeout(intValue());
/*     */   }
/*     */   
/*     */   boolean isIpFreeBind() throws IOException {
/* 148 */     return (isIpFreeBind(intValue()) != 0);
/*     */   }
/*     */   
/*     */   boolean isIpTransparent() throws IOException {
/* 152 */     return (isIpTransparent(intValue()) != 0);
/*     */   }
/*     */   
/*     */   boolean isIpRecvOrigDestAddr() throws IOException {
/* 156 */     return (isIpRecvOrigDestAddr(intValue()) != 0);
/*     */   }
/*     */   
/*     */   PeerCredentials getPeerCredentials() throws IOException {
/* 160 */     return getPeerCredentials(intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   long sendFile(DefaultFileRegion src, long baseOffset, long offset, long length) throws IOException {
/* 166 */     src.open();
/*     */     
/* 168 */     long res = sendFile(intValue(), src, baseOffset, offset, length);
/* 169 */     if (res >= 0L) {
/* 170 */       return res;
/*     */     }
/* 172 */     return Errors.ioResult("sendfile", (int)res, SENDFILE_CONNECTION_RESET_EXCEPTION, SENDFILE_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */   
/*     */   public static LinuxSocket newSocketStream() {
/* 176 */     return new LinuxSocket(newSocketStream0());
/*     */   }
/*     */   
/*     */   public static LinuxSocket newSocketDgram() {
/* 180 */     return new LinuxSocket(newSocketDgram0());
/*     */   }
/*     */   
/*     */   public static LinuxSocket newSocketDomain() {
/* 184 */     return new LinuxSocket(newSocketDomain0());
/*     */   }
/*     */   
/*     */   private static native long sendFile(int paramInt, DefaultFileRegion paramDefaultFileRegion, long paramLong1, long paramLong2, long paramLong3) throws IOException;
/*     */   
/*     */   private static native int getTcpDeferAccept(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isTcpQuickAck(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isTcpCork(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTcpNotSentLowAt(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTcpKeepIdle(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTcpKeepIntvl(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTcpKeepCnt(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTcpUserTimeout(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isIpFreeBind(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isIpTransparent(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isIpRecvOrigDestAddr(int paramInt) throws IOException;
/*     */   
/*     */   private static native void getTcpInfo(int paramInt, long[] paramArrayOflong) throws IOException;
/*     */   
/*     */   private static native PeerCredentials getPeerCredentials(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isTcpFastOpenConnect(int paramInt) throws IOException;
/*     */   
/*     */   private static native void setTcpDeferAccept(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpQuickAck(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpCork(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpNotSentLowAt(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpFastOpen(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpFastOpenConnect(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpKeepIdle(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpKeepIntvl(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpKeepCnt(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpUserTimeout(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setIpFreeBind(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setIpTransparent(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setIpRecvOrigDestAddr(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpMd5Sig(int paramInt1, byte[] paramArrayOfbyte1, int paramInt2, byte[] paramArrayOfbyte2) throws IOException;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\LinuxSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */