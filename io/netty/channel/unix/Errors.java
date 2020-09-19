/*     */ package io.netty.channel.unix;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.NotYetConnectedException;
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
/*     */ public final class Errors
/*     */ {
/*  37 */   public static final int ERRNO_ENOENT_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoENOENT();
/*  38 */   public static final int ERRNO_ENOTCONN_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoENOTCONN();
/*  39 */   public static final int ERRNO_EBADF_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEBADF();
/*  40 */   public static final int ERRNO_EPIPE_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEPIPE();
/*  41 */   public static final int ERRNO_ECONNRESET_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoECONNRESET();
/*  42 */   public static final int ERRNO_EAGAIN_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEAGAIN();
/*  43 */   public static final int ERRNO_EWOULDBLOCK_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEWOULDBLOCK();
/*  44 */   public static final int ERRNO_EINPROGRESS_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errnoEINPROGRESS();
/*  45 */   public static final int ERROR_ECONNREFUSED_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorECONNREFUSED();
/*  46 */   public static final int ERROR_EISCONN_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorEISCONN();
/*  47 */   public static final int ERROR_EALREADY_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorEALREADY();
/*  48 */   public static final int ERROR_ENETUNREACH_NEGATIVE = -ErrorsStaticallyReferencedJniMethods.errorENETUNREACH();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final String[] ERRORS = new String[512];
/*     */ 
/*     */   
/*     */   public static final class NativeIoException
/*     */     extends IOException
/*     */   {
/*     */     private static final long serialVersionUID = 8222160204268655526L;
/*     */     
/*     */     public NativeIoException(String method, int expectedErr) {
/*  66 */       super(method + "(..) failed: " + Errors.ERRORS[-expectedErr]);
/*  67 */       this.expectedErr = expectedErr;
/*     */     }
/*     */     private final int expectedErr;
/*     */     public int expectedErr() {
/*  71 */       return this.expectedErr;
/*     */     } }
/*     */   
/*     */   static final class NativeConnectException extends ConnectException {
/*     */     private static final long serialVersionUID = -5532328671712318161L;
/*     */     private final int expectedErr;
/*     */     
/*     */     NativeConnectException(String method, int expectedErr) {
/*  79 */       super(method + "(..) failed: " + Errors.ERRORS[-expectedErr]);
/*  80 */       this.expectedErr = expectedErr;
/*     */     }
/*     */     
/*     */     int expectedErr() {
/*  84 */       return this.expectedErr;
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  89 */     for (int i = 0; i < ERRORS.length; i++)
/*     */     {
/*  91 */       ERRORS[i] = ErrorsStaticallyReferencedJniMethods.strError(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static void throwConnectException(String method, NativeConnectException refusedCause, int err) throws IOException {
/*  97 */     if (err == refusedCause.expectedErr()) {
/*  98 */       throw refusedCause;
/*     */     }
/* 100 */     if (err == ERROR_EALREADY_NEGATIVE) {
/* 101 */       throw new ConnectionPendingException();
/*     */     }
/* 103 */     if (err == ERROR_ENETUNREACH_NEGATIVE) {
/* 104 */       throw new NoRouteToHostException();
/*     */     }
/* 106 */     if (err == ERROR_EISCONN_NEGATIVE) {
/* 107 */       throw new AlreadyConnectedException();
/*     */     }
/* 109 */     if (err == ERRNO_ENOENT_NEGATIVE) {
/* 110 */       throw new FileNotFoundException();
/*     */     }
/* 112 */     throw new ConnectException(method + "(..) failed: " + ERRORS[-err]);
/*     */   }
/*     */   
/*     */   public static NativeIoException newConnectionResetException(String method, int errnoNegative) {
/* 116 */     NativeIoException exception = newIOException(method, errnoNegative);
/* 117 */     exception.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/* 118 */     return exception;
/*     */   }
/*     */   
/*     */   public static NativeIoException newIOException(String method, int err) {
/* 122 */     return new NativeIoException(method, err);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ioResult(String method, int err, NativeIoException resetCause, ClosedChannelException closedCause) throws IOException {
/* 128 */     if (err == ERRNO_EAGAIN_NEGATIVE || err == ERRNO_EWOULDBLOCK_NEGATIVE) {
/* 129 */       return 0;
/*     */     }
/* 131 */     if (err == resetCause.expectedErr()) {
/* 132 */       throw resetCause;
/*     */     }
/* 134 */     if (err == ERRNO_EBADF_NEGATIVE) {
/* 135 */       throw closedCause;
/*     */     }
/* 137 */     if (err == ERRNO_ENOTCONN_NEGATIVE) {
/* 138 */       throw new NotYetConnectedException();
/*     */     }
/* 140 */     if (err == ERRNO_ENOENT_NEGATIVE) {
/* 141 */       throw new FileNotFoundException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 146 */     throw newIOException(method, err);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\Errors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */