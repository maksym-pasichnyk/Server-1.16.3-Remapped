/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.handler.codec.EmptyHeaders;
/*    */ import java.util.Iterator;
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
/*    */ public final class EmptyHttp2Headers
/*    */   extends EmptyHeaders<CharSequence, CharSequence, Http2Headers>
/*    */   implements Http2Headers
/*    */ {
/* 24 */   public static final EmptyHttp2Headers INSTANCE = new EmptyHttp2Headers();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EmptyHttp2Headers method(CharSequence method) {
/* 31 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EmptyHttp2Headers scheme(CharSequence status) {
/* 36 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EmptyHttp2Headers authority(CharSequence authority) {
/* 41 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EmptyHttp2Headers path(CharSequence path) {
/* 46 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EmptyHttp2Headers status(CharSequence status) {
/* 51 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence method() {
/* 56 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.METHOD.value());
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence scheme() {
/* 61 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.SCHEME.value());
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence authority() {
/* 66 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.AUTHORITY.value());
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence path() {
/* 71 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.PATH.value());
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence status() {
/* 76 */     return (CharSequence)get(Http2Headers.PseudoHeaderName.STATUS.value());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(CharSequence name, CharSequence value, boolean caseInsensitive) {
/* 81 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\EmptyHttp2Headers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */