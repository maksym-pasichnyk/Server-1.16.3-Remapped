/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class DefaultHttp2PingFrame
/*    */   implements Http2PingFrame
/*    */ {
/*    */   private final long content;
/*    */   private final boolean ack;
/*    */   
/*    */   public DefaultHttp2PingFrame(long content) {
/* 32 */     this(content, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   DefaultHttp2PingFrame(long content, boolean ack) {
/* 39 */     this.content = content;
/* 40 */     this.ack = ack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean ack() {
/* 45 */     return this.ack;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 50 */     return "PING";
/*    */   }
/*    */ 
/*    */   
/*    */   public long content() {
/* 55 */     return this.content;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 60 */     if (!(o instanceof Http2PingFrame)) {
/* 61 */       return false;
/*    */     }
/* 63 */     Http2PingFrame other = (Http2PingFrame)o;
/* 64 */     return (this.ack == other.ack() && this.content == other.content());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     int hash = super.hashCode();
/* 70 */     hash = hash * 31 + (this.ack ? 1 : 0);
/* 71 */     return hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return StringUtil.simpleClassName(this) + "(content=" + this.content + ", ack=" + this.ack + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2PingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */