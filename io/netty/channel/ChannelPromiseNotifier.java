/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import io.netty.util.concurrent.PromiseNotifier;
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
/*    */ public final class ChannelPromiseNotifier
/*    */   extends PromiseNotifier<Void, ChannelFuture>
/*    */   implements ChannelFutureListener
/*    */ {
/*    */   public ChannelPromiseNotifier(ChannelPromise... promises) {
/* 33 */     super((Promise[])promises);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChannelPromiseNotifier(boolean logNotifyFailure, ChannelPromise... promises) {
/* 43 */     super(logNotifyFailure, (Promise[])promises);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelPromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */