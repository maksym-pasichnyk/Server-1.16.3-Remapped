/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.buffer.CompositeByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public final class CoalescingBufferQueue
/*    */   extends AbstractCoalescingBufferQueue
/*    */ {
/*    */   private final Channel channel;
/*    */   
/*    */   public CoalescingBufferQueue(Channel channel) {
/* 40 */     this(channel, 4);
/*    */   }
/*    */   
/*    */   public CoalescingBufferQueue(Channel channel, int initSize) {
/* 44 */     this(channel, initSize, false);
/*    */   }
/*    */   
/*    */   public CoalescingBufferQueue(Channel channel, int initSize, boolean updateWritability) {
/* 48 */     super(updateWritability ? channel : null, initSize);
/* 49 */     this.channel = (Channel)ObjectUtil.checkNotNull(channel, "channel");
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
/*    */   
/*    */   public ByteBuf remove(int bytes, ChannelPromise aggregatePromise) {
/* 63 */     return remove(this.channel.alloc(), bytes, aggregatePromise);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void releaseAndFailAll(Throwable cause) {
/* 70 */     releaseAndFailAll(this.channel, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ByteBuf compose(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf next) {
/* 75 */     if (cumulation instanceof CompositeByteBuf) {
/* 76 */       CompositeByteBuf composite = (CompositeByteBuf)cumulation;
/* 77 */       composite.addComponent(true, next);
/* 78 */       return (ByteBuf)composite;
/*    */     } 
/* 80 */     return composeIntoComposite(alloc, cumulation, next);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ByteBuf removeEmptyValue() {
/* 85 */     return Unpooled.EMPTY_BUFFER;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\CoalescingBufferQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */