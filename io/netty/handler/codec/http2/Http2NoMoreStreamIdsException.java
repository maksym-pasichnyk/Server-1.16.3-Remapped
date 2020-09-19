/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Http2NoMoreStreamIdsException
/*    */   extends Http2Exception
/*    */ {
/*    */   private static final long serialVersionUID = -7756236161274851110L;
/*    */   private static final String ERROR_MESSAGE = "No more streams can be created on this connection";
/*    */   
/*    */   public Http2NoMoreStreamIdsException() {
/* 30 */     super(Http2Error.PROTOCOL_ERROR, "No more streams can be created on this connection", Http2Exception.ShutdownHint.GRACEFUL_SHUTDOWN);
/*    */   }
/*    */   
/*    */   public Http2NoMoreStreamIdsException(Throwable cause) {
/* 34 */     super(Http2Error.PROTOCOL_ERROR, "No more streams can be created on this connection", cause, Http2Exception.ShutdownHint.GRACEFUL_SHUTDOWN);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2NoMoreStreamIdsException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */