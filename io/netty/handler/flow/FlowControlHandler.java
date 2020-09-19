/*     */ package io.netty.handler.flow;
/*     */ 
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayDeque;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlowControlHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  68 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(FlowControlHandler.class);
/*     */   
/*     */   private final boolean releaseMessages;
/*     */   
/*     */   private RecyclableArrayDeque queue;
/*     */   
/*     */   private ChannelConfig config;
/*     */   
/*     */   private boolean shouldConsume;
/*     */   
/*     */   public FlowControlHandler() {
/*  79 */     this(true);
/*     */   }
/*     */   
/*     */   public FlowControlHandler(boolean releaseMessages) {
/*  83 */     this.releaseMessages = releaseMessages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isQueueEmpty() {
/*  91 */     return this.queue.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroy() {
/*  98 */     if (this.queue != null) {
/*     */       
/* 100 */       if (!this.queue.isEmpty()) {
/* 101 */         logger.trace("Non-empty queue: {}", this.queue);
/*     */         
/* 103 */         if (this.releaseMessages) {
/*     */           Object msg;
/* 105 */           while ((msg = this.queue.poll()) != null) {
/* 106 */             ReferenceCountUtil.safeRelease(msg);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 111 */       this.queue.recycle();
/* 112 */       this.queue = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 118 */     this.config = ctx.channel().config();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 123 */     destroy();
/* 124 */     ctx.fireChannelInactive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception {
/* 129 */     if (dequeue(ctx, 1) == 0) {
/*     */ 
/*     */ 
/*     */       
/* 133 */       this.shouldConsume = true;
/* 134 */       ctx.read();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 140 */     if (this.queue == null) {
/* 141 */       this.queue = RecyclableArrayDeque.newInstance();
/*     */     }
/*     */     
/* 144 */     this.queue.offer(msg);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     int minConsume = this.shouldConsume ? 1 : 0;
/* 150 */     this.shouldConsume = false;
/*     */     
/* 152 */     dequeue(ctx, minConsume);
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
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int dequeue(ChannelHandlerContext ctx, int minConsume) {
/* 175 */     if (this.queue != null) {
/*     */       
/* 177 */       int consumed = 0;
/*     */ 
/*     */       
/* 180 */       while (consumed < minConsume || this.config.isAutoRead()) {
/* 181 */         Object msg = this.queue.poll();
/* 182 */         if (msg == null) {
/*     */           break;
/*     */         }
/*     */         
/* 186 */         consumed++;
/* 187 */         ctx.fireChannelRead(msg);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 193 */       if (this.queue.isEmpty() && consumed > 0) {
/* 194 */         ctx.fireChannelReadComplete();
/*     */       }
/*     */       
/* 197 */       return consumed;
/*     */     } 
/*     */     
/* 200 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RecyclableArrayDeque
/*     */     extends ArrayDeque<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     private static final int DEFAULT_NUM_ELEMENTS = 2;
/*     */ 
/*     */ 
/*     */     
/* 215 */     private static final Recycler<RecyclableArrayDeque> RECYCLER = new Recycler<RecyclableArrayDeque>()
/*     */       {
/*     */         protected FlowControlHandler.RecyclableArrayDeque newObject(Recycler.Handle<FlowControlHandler.RecyclableArrayDeque> handle) {
/* 218 */           return new FlowControlHandler.RecyclableArrayDeque(2, handle);
/*     */         }
/*     */       };
/*     */     
/*     */     public static RecyclableArrayDeque newInstance() {
/* 223 */       return (RecyclableArrayDeque)RECYCLER.get();
/*     */     }
/*     */     
/*     */     private final Recycler.Handle<RecyclableArrayDeque> handle;
/*     */     
/*     */     private RecyclableArrayDeque(int numElements, Recycler.Handle<RecyclableArrayDeque> handle) {
/* 229 */       super(numElements);
/* 230 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     public void recycle() {
/* 234 */       clear();
/* 235 */       this.handle.recycle(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\flow\FlowControlHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */