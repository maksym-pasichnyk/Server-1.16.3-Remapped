/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.channel.ChannelId;
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
/*    */ final class Http2StreamChannelId
/*    */   implements ChannelId
/*    */ {
/*    */   private static final long serialVersionUID = -6642338822166867585L;
/*    */   private final int id;
/*    */   private final ChannelId parentId;
/*    */   
/*    */   Http2StreamChannelId(ChannelId parentId, int id) {
/* 30 */     this.parentId = parentId;
/* 31 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String asShortText() {
/* 36 */     return this.parentId.asShortText() + '/' + this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String asLongText() {
/* 41 */     return this.parentId.asLongText() + '/' + this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(ChannelId o) {
/* 46 */     if (o instanceof Http2StreamChannelId) {
/* 47 */       Http2StreamChannelId otherId = (Http2StreamChannelId)o;
/* 48 */       int res = this.parentId.compareTo(otherId.parentId);
/* 49 */       if (res == 0) {
/* 50 */         return this.id - otherId.id;
/*    */       }
/* 52 */       return res;
/*    */     } 
/*    */     
/* 55 */     return this.parentId.compareTo(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     return this.id * 31 + this.parentId.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 65 */     if (!(obj instanceof Http2StreamChannelId)) {
/* 66 */       return false;
/*    */     }
/* 68 */     Http2StreamChannelId otherId = (Http2StreamChannelId)obj;
/* 69 */     return (this.id == otherId.id && this.parentId.equals(otherId.parentId));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return asShortText();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2StreamChannelId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */