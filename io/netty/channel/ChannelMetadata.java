/*    */ package io.netty.channel;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ChannelMetadata
/*    */ {
/*    */   private final boolean hasDisconnect;
/*    */   private final int defaultMaxMessagesPerRead;
/*    */   
/*    */   public ChannelMetadata(boolean hasDisconnect) {
/* 36 */     this(hasDisconnect, 1);
/*    */   }
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
/*    */   public ChannelMetadata(boolean hasDisconnect, int defaultMaxMessagesPerRead) {
/* 49 */     if (defaultMaxMessagesPerRead <= 0) {
/* 50 */       throw new IllegalArgumentException("defaultMaxMessagesPerRead: " + defaultMaxMessagesPerRead + " (expected > 0)");
/*    */     }
/*    */     
/* 53 */     this.hasDisconnect = hasDisconnect;
/* 54 */     this.defaultMaxMessagesPerRead = defaultMaxMessagesPerRead;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasDisconnect() {
/* 63 */     return this.hasDisconnect;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int defaultMaxMessagesPerRead() {
/* 71 */     return this.defaultMaxMessagesPerRead;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */