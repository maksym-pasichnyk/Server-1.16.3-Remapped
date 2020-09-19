/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class Http2Exception
/*     */   extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -6941186345430164209L;
/*     */   private final Http2Error error;
/*     */   private final ShutdownHint shutdownHint;
/*     */   
/*     */   public Http2Exception(Http2Error error) {
/*  37 */     this(error, ShutdownHint.HARD_SHUTDOWN);
/*     */   }
/*     */   
/*     */   public Http2Exception(Http2Error error, ShutdownHint shutdownHint) {
/*  41 */     this.error = (Http2Error)ObjectUtil.checkNotNull(error, "error");
/*  42 */     this.shutdownHint = (ShutdownHint)ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
/*     */   }
/*     */   
/*     */   public Http2Exception(Http2Error error, String message) {
/*  46 */     this(error, message, ShutdownHint.HARD_SHUTDOWN);
/*     */   }
/*     */   
/*     */   public Http2Exception(Http2Error error, String message, ShutdownHint shutdownHint) {
/*  50 */     super(message);
/*  51 */     this.error = (Http2Error)ObjectUtil.checkNotNull(error, "error");
/*  52 */     this.shutdownHint = (ShutdownHint)ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
/*     */   }
/*     */   
/*     */   public Http2Exception(Http2Error error, String message, Throwable cause) {
/*  56 */     this(error, message, cause, ShutdownHint.HARD_SHUTDOWN);
/*     */   }
/*     */   
/*     */   public Http2Exception(Http2Error error, String message, Throwable cause, ShutdownHint shutdownHint) {
/*  60 */     super(message, cause);
/*  61 */     this.error = (Http2Error)ObjectUtil.checkNotNull(error, "error");
/*  62 */     this.shutdownHint = (ShutdownHint)ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
/*     */   }
/*     */   
/*     */   public Http2Error error() {
/*  66 */     return this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShutdownHint shutdownHint() {
/*  73 */     return this.shutdownHint;
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
/*     */   public static Http2Exception connectionError(Http2Error error, String fmt, Object... args) {
/*  85 */     return new Http2Exception(error, String.format(fmt, args));
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
/*     */   public static Http2Exception connectionError(Http2Error error, Throwable cause, String fmt, Object... args) {
/*  99 */     return new Http2Exception(error, String.format(fmt, args), cause);
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
/*     */   public static Http2Exception closedStreamError(Http2Error error, String fmt, Object... args) {
/* 111 */     return new ClosedStreamCreationException(error, String.format(fmt, args));
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
/*     */   public static Http2Exception streamError(int id, Http2Error error, String fmt, Object... args) {
/* 127 */     return (0 == id) ? 
/* 128 */       connectionError(error, fmt, args) : new StreamException(id, error, 
/* 129 */         String.format(fmt, args));
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
/*     */   public static Http2Exception streamError(int id, Http2Error error, Throwable cause, String fmt, Object... args) {
/* 147 */     return (0 == id) ? 
/* 148 */       connectionError(error, cause, fmt, args) : new StreamException(id, error, 
/* 149 */         String.format(fmt, args), cause);
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
/*     */   
/*     */   public static Http2Exception headerListSizeError(int id, Http2Error error, boolean onDecode, String fmt, Object... args) {
/* 169 */     return (0 == id) ? 
/* 170 */       connectionError(error, fmt, args) : new HeaderListSizeException(id, error, 
/* 171 */         String.format(fmt, args), onDecode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStreamError(Http2Exception e) {
/* 181 */     return e instanceof StreamException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int streamId(Http2Exception e) {
/* 191 */     return isStreamError(e) ? ((StreamException)e).streamId() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum ShutdownHint
/*     */   {
/* 201 */     NO_SHUTDOWN,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     GRACEFUL_SHUTDOWN,
/*     */ 
/*     */ 
/*     */     
/* 210 */     HARD_SHUTDOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ClosedStreamCreationException
/*     */     extends Http2Exception
/*     */   {
/*     */     private static final long serialVersionUID = -6746542974372246206L;
/*     */     
/*     */     public ClosedStreamCreationException(Http2Error error) {
/* 220 */       super(error);
/*     */     }
/*     */     
/*     */     public ClosedStreamCreationException(Http2Error error, String message) {
/* 224 */       super(error, message);
/*     */     }
/*     */     
/*     */     public ClosedStreamCreationException(Http2Error error, String message, Throwable cause) {
/* 228 */       super(error, message, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class StreamException
/*     */     extends Http2Exception
/*     */   {
/*     */     private static final long serialVersionUID = 602472544416984384L;
/*     */     private final int streamId;
/*     */     
/*     */     StreamException(int streamId, Http2Error error, String message) {
/* 240 */       super(error, message, Http2Exception.ShutdownHint.NO_SHUTDOWN);
/* 241 */       this.streamId = streamId;
/*     */     }
/*     */     
/*     */     StreamException(int streamId, Http2Error error, String message, Throwable cause) {
/* 245 */       super(error, message, cause, Http2Exception.ShutdownHint.NO_SHUTDOWN);
/* 246 */       this.streamId = streamId;
/*     */     }
/*     */     
/*     */     public int streamId() {
/* 250 */       return this.streamId;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class HeaderListSizeException
/*     */     extends StreamException {
/*     */     private static final long serialVersionUID = -8807603212183882637L;
/*     */     private final boolean decode;
/*     */     
/*     */     HeaderListSizeException(int streamId, Http2Error error, String message, boolean decode) {
/* 260 */       super(streamId, error, message);
/* 261 */       this.decode = decode;
/*     */     }
/*     */     
/*     */     public boolean duringDecode() {
/* 265 */       return this.decode;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CompositeStreamException
/*     */     extends Http2Exception
/*     */     implements Iterable<StreamException>
/*     */   {
/*     */     private static final long serialVersionUID = 7091134858213711015L;
/*     */     private final List<Http2Exception.StreamException> exceptions;
/*     */     
/*     */     public CompositeStreamException(Http2Error error, int initialCapacity) {
/* 277 */       super(error, Http2Exception.ShutdownHint.NO_SHUTDOWN);
/* 278 */       this.exceptions = new ArrayList<Http2Exception.StreamException>(initialCapacity);
/*     */     }
/*     */     
/*     */     public void add(Http2Exception.StreamException e) {
/* 282 */       this.exceptions.add(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Http2Exception.StreamException> iterator() {
/* 287 */       return this.exceptions.iterator();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Exception.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */