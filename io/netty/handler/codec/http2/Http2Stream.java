/*    */ package io.netty.handler.codec.http2;public interface Http2Stream { int id();
/*    */   State state();
/*    */   Http2Stream open(boolean paramBoolean) throws Http2Exception;
/*    */   Http2Stream close();
/*    */   Http2Stream closeLocalSide();
/*    */   Http2Stream closeRemoteSide();
/*    */   boolean isResetSent();
/*    */   Http2Stream resetSent();
/*    */   <V> V setProperty(Http2Connection.PropertyKey paramPropertyKey, V paramV);
/*    */   <V> V getProperty(Http2Connection.PropertyKey paramPropertyKey);
/*    */   
/*    */   <V> V removeProperty(Http2Connection.PropertyKey paramPropertyKey);
/*    */   
/*    */   Http2Stream headersSent(boolean paramBoolean);
/*    */   
/*    */   boolean isHeadersSent();
/*    */   
/*    */   boolean isTrailersSent();
/*    */   
/*    */   Http2Stream headersReceived(boolean paramBoolean);
/*    */   
/*    */   boolean isHeadersReceived();
/*    */   
/*    */   boolean isTrailersReceived();
/*    */   
/*    */   Http2Stream pushPromiseSent();
/*    */   
/*    */   boolean isPushPromiseSent();
/*    */   
/* 30 */   public enum State { IDLE(false, false),
/* 31 */     RESERVED_LOCAL(false, false),
/* 32 */     RESERVED_REMOTE(false, false),
/* 33 */     OPEN(true, true),
/* 34 */     HALF_CLOSED_LOCAL(false, true),
/* 35 */     HALF_CLOSED_REMOTE(true, false),
/* 36 */     CLOSED(false, false);
/*    */     
/*    */     private final boolean localSideOpen;
/*    */     private final boolean remoteSideOpen;
/*    */     
/*    */     State(boolean localSideOpen, boolean remoteSideOpen) {
/* 42 */       this.localSideOpen = localSideOpen;
/* 43 */       this.remoteSideOpen = remoteSideOpen;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean localSideOpen() {
/* 51 */       return this.localSideOpen;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean remoteSideOpen() {
/* 59 */       return this.remoteSideOpen;
/*    */     } }
/*    */    }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Stream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */