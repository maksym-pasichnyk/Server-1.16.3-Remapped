/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.unix.Errors;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.util.internal.NativeLibraryLoader;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.Locale;
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
/*     */ public final class Native
/*     */ {
/*  52 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Native.class);
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  58 */       offsetofEpollData();
/*  59 */     } catch (UnsatisfiedLinkError ignore) {
/*     */       
/*  61 */       loadNativeLibrary();
/*     */     } 
/*  63 */     Socket.initialize();
/*     */   }
/*     */ 
/*     */   
/*  67 */   public static final int EPOLLIN = NativeStaticallyReferencedJniMethods.epollin();
/*  68 */   public static final int EPOLLOUT = NativeStaticallyReferencedJniMethods.epollout();
/*  69 */   public static final int EPOLLRDHUP = NativeStaticallyReferencedJniMethods.epollrdhup();
/*  70 */   public static final int EPOLLET = NativeStaticallyReferencedJniMethods.epollet();
/*  71 */   public static final int EPOLLERR = NativeStaticallyReferencedJniMethods.epollerr();
/*     */   
/*  73 */   public static final boolean IS_SUPPORTING_SENDMMSG = NativeStaticallyReferencedJniMethods.isSupportingSendmmsg();
/*  74 */   public static final boolean IS_SUPPORTING_TCP_FASTOPEN = NativeStaticallyReferencedJniMethods.isSupportingTcpFastopen();
/*  75 */   public static final int TCP_MD5SIG_MAXKEYLEN = NativeStaticallyReferencedJniMethods.tcpMd5SigMaxKeyLen();
/*  76 */   public static final String KERNEL_VERSION = NativeStaticallyReferencedJniMethods.kernelVersion();
/*     */   
/*     */   private static final Errors.NativeIoException SENDMMSG_CONNECTION_RESET_EXCEPTION;
/*     */   private static final Errors.NativeIoException SPLICE_CONNECTION_RESET_EXCEPTION;
/*  80 */   private static final ClosedChannelException SENDMMSG_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "sendmmsg(...)");
/*     */   
/*  82 */   private static final ClosedChannelException SPLICE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "splice(...)");
/*     */ 
/*     */   
/*     */   static {
/*  86 */     SENDMMSG_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendmmsg(...)", Errors.ERRNO_EPIPE_NEGATIVE);
/*     */     
/*  88 */     SPLICE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:splice(...)", Errors.ERRNO_EPIPE_NEGATIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static FileDescriptor newEventFd() {
/*  93 */     return new FileDescriptor(eventFd());
/*     */   }
/*     */   
/*     */   public static FileDescriptor newTimerFd() {
/*  97 */     return new FileDescriptor(timerFd());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileDescriptor newEpollCreate() {
/* 107 */     return new FileDescriptor(epollCreate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int epollWait(FileDescriptor epollFd, EpollEventArray events, FileDescriptor timerFd, int timeoutSec, int timeoutNs) throws IOException {
/* 114 */     int ready = epollWait0(epollFd.intValue(), events.memoryAddress(), events.length(), timerFd.intValue(), timeoutSec, timeoutNs);
/*     */     
/* 116 */     if (ready < 0) {
/* 117 */       throw Errors.newIOException("epoll_wait", ready);
/*     */     }
/* 119 */     return ready;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void epollCtlAdd(int efd, int fd, int flags) throws IOException {
/* 124 */     int res = epollCtlAdd0(efd, fd, flags);
/* 125 */     if (res < 0) {
/* 126 */       throw Errors.newIOException("epoll_ctl", res);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void epollCtlMod(int efd, int fd, int flags) throws IOException {
/* 132 */     int res = epollCtlMod0(efd, fd, flags);
/* 133 */     if (res < 0) {
/* 134 */       throw Errors.newIOException("epoll_ctl", res);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void epollCtlDel(int efd, int fd) throws IOException {
/* 140 */     int res = epollCtlDel0(efd, fd);
/* 141 */     if (res < 0) {
/* 142 */       throw Errors.newIOException("epoll_ctl", res);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int splice(int fd, long offIn, int fdOut, long offOut, long len) throws IOException {
/* 149 */     int res = splice0(fd, offIn, fdOut, offOut, len);
/* 150 */     if (res >= 0) {
/* 151 */       return res;
/*     */     }
/* 153 */     return Errors.ioResult("splice", res, SPLICE_CONNECTION_RESET_EXCEPTION, SPLICE_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int sendmmsg(int fd, NativeDatagramPacketArray.NativeDatagramPacket[] msgs, int offset, int len) throws IOException {
/* 160 */     int res = sendmmsg0(fd, msgs, offset, len);
/* 161 */     if (res >= 0) {
/* 162 */       return res;
/*     */     }
/* 164 */     return Errors.ioResult("sendmmsg", res, SENDMMSG_CONNECTION_RESET_EXCEPTION, SENDMMSG_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadNativeLibrary() {
/* 175 */     String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
/* 176 */     if (!name.startsWith("linux")) {
/* 177 */       throw new IllegalStateException("Only supported on Linux");
/*     */     }
/* 179 */     String staticLibName = "netty_transport_native_epoll";
/* 180 */     String sharedLibName = staticLibName + '_' + PlatformDependent.normalizedArch();
/* 181 */     ClassLoader cl = PlatformDependent.getClassLoader(Native.class);
/*     */     try {
/* 183 */       NativeLibraryLoader.load(sharedLibName, cl);
/* 184 */     } catch (UnsatisfiedLinkError e1) {
/*     */       try {
/* 186 */         NativeLibraryLoader.load(staticLibName, cl);
/* 187 */         logger.debug("Failed to load {}", sharedLibName, e1);
/* 188 */       } catch (UnsatisfiedLinkError e2) {
/* 189 */         ThrowableUtil.addSuppressed(e1, e2);
/* 190 */         throw e1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static native int eventFd();
/*     */   
/*     */   private static native int timerFd();
/*     */   
/*     */   public static native void eventFdWrite(int paramInt, long paramLong);
/*     */   
/*     */   public static native void eventFdRead(int paramInt);
/*     */   
/*     */   static native void timerFdRead(int paramInt);
/*     */   
/*     */   private static native int epollCreate();
/*     */   
/*     */   private static native int epollWait0(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native int epollCtlAdd0(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int epollCtlMod0(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int epollCtlDel0(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int splice0(int paramInt1, long paramLong1, int paramInt2, long paramLong2, long paramLong3);
/*     */   
/*     */   private static native int sendmmsg0(int paramInt1, NativeDatagramPacketArray.NativeDatagramPacket[] paramArrayOfNativeDatagramPacket, int paramInt2, int paramInt3);
/*     */   
/*     */   public static native int sizeofEpollEvent();
/*     */   
/*     */   public static native int offsetofEpollData();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */