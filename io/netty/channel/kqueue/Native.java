/*     */ package io.netty.channel.kqueue;
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
/*     */ final class Native
/*     */ {
/*  51 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Native.class);
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  57 */       sizeofKEvent();
/*  58 */     } catch (UnsatisfiedLinkError ignore) {
/*     */       
/*  60 */       loadNativeLibrary();
/*     */     } 
/*  62 */     Socket.initialize();
/*     */   }
/*     */   
/*  65 */   static final short EV_ADD = KQueueStaticallyReferencedJniMethods.evAdd();
/*  66 */   static final short EV_ENABLE = KQueueStaticallyReferencedJniMethods.evEnable();
/*  67 */   static final short EV_DISABLE = KQueueStaticallyReferencedJniMethods.evDisable();
/*  68 */   static final short EV_DELETE = KQueueStaticallyReferencedJniMethods.evDelete();
/*  69 */   static final short EV_CLEAR = KQueueStaticallyReferencedJniMethods.evClear();
/*  70 */   static final short EV_ERROR = KQueueStaticallyReferencedJniMethods.evError();
/*  71 */   static final short EV_EOF = KQueueStaticallyReferencedJniMethods.evEOF();
/*     */   
/*  73 */   static final int NOTE_READCLOSED = KQueueStaticallyReferencedJniMethods.noteReadClosed();
/*  74 */   static final int NOTE_CONNRESET = KQueueStaticallyReferencedJniMethods.noteConnReset();
/*  75 */   static final int NOTE_DISCONNECTED = KQueueStaticallyReferencedJniMethods.noteDisconnected();
/*     */   
/*  77 */   static final int NOTE_RDHUP = NOTE_READCLOSED | NOTE_CONNRESET | NOTE_DISCONNECTED;
/*     */ 
/*     */   
/*  80 */   static final short EV_ADD_CLEAR_ENABLE = (short)(EV_ADD | EV_CLEAR | EV_ENABLE);
/*  81 */   static final short EV_DELETE_DISABLE = (short)(EV_DELETE | EV_DISABLE);
/*     */   
/*  83 */   static final short EVFILT_READ = KQueueStaticallyReferencedJniMethods.evfiltRead();
/*  84 */   static final short EVFILT_WRITE = KQueueStaticallyReferencedJniMethods.evfiltWrite();
/*  85 */   static final short EVFILT_USER = KQueueStaticallyReferencedJniMethods.evfiltUser();
/*  86 */   static final short EVFILT_SOCK = KQueueStaticallyReferencedJniMethods.evfiltSock();
/*     */   
/*     */   static FileDescriptor newKQueue() {
/*  89 */     return new FileDescriptor(kqueueCreate());
/*     */   }
/*     */ 
/*     */   
/*     */   static int keventWait(int kqueueFd, KQueueEventArray changeList, KQueueEventArray eventList, int tvSec, int tvNsec) throws IOException {
/*  94 */     int ready = keventWait(kqueueFd, changeList.memoryAddress(), changeList.size(), eventList
/*  95 */         .memoryAddress(), eventList.capacity(), tvSec, tvNsec);
/*  96 */     if (ready < 0) {
/*  97 */       throw Errors.newIOException("kevent", ready);
/*     */     }
/*  99 */     return ready;
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
/*     */   private static void loadNativeLibrary() {
/* 117 */     String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
/* 118 */     if (!name.startsWith("mac") && !name.contains("bsd") && !name.startsWith("darwin")) {
/* 119 */       throw new IllegalStateException("Only supported on BSD");
/*     */     }
/* 121 */     String staticLibName = "netty_transport_native_kqueue";
/* 122 */     String sharedLibName = staticLibName + '_' + PlatformDependent.normalizedArch();
/* 123 */     ClassLoader cl = PlatformDependent.getClassLoader(Native.class);
/*     */     try {
/* 125 */       NativeLibraryLoader.load(sharedLibName, cl);
/* 126 */     } catch (UnsatisfiedLinkError e1) {
/*     */       try {
/* 128 */         NativeLibraryLoader.load(staticLibName, cl);
/* 129 */         logger.debug("Failed to load {}", sharedLibName, e1);
/* 130 */       } catch (UnsatisfiedLinkError e2) {
/* 131 */         ThrowableUtil.addSuppressed(e1, e2);
/* 132 */         throw e1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static native int kqueueCreate();
/*     */   
/*     */   private static native int keventWait(int paramInt1, long paramLong1, int paramInt2, long paramLong2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   static native int keventTriggerUserEvent(int paramInt1, int paramInt2);
/*     */   
/*     */   static native int keventAddUserEvent(int paramInt1, int paramInt2);
/*     */   
/*     */   static native int sizeofKEvent();
/*     */   
/*     */   static native int offsetofKEventIdent();
/*     */   
/*     */   static native int offsetofKEventFlags();
/*     */   
/*     */   static native int offsetofKEventFFlags();
/*     */   
/*     */   static native int offsetofKEventFilter();
/*     */   
/*     */   static native int offsetofKeventData();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */