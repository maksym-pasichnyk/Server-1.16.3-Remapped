/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.AbstractChannel;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelConfig;
/*    */ import io.netty.channel.socket.ServerSocketChannel;
/*    */ import io.netty.channel.socket.SocketChannel;
/*    */ import io.netty.channel.socket.SocketChannelConfig;
/*    */ import io.netty.util.concurrent.GlobalEventExecutor;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.concurrent.Executor;
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
/*    */ public final class KQueueSocketChannel
/*    */   extends AbstractKQueueStreamChannel
/*    */   implements SocketChannel
/*    */ {
/*    */   private final KQueueSocketChannelConfig config;
/*    */   
/*    */   public KQueueSocketChannel() {
/* 32 */     super((Channel)null, BsdSocket.newSocketStream(), false);
/* 33 */     this.config = new KQueueSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */   public KQueueSocketChannel(int fd) {
/* 37 */     super(new BsdSocket(fd));
/* 38 */     this.config = new KQueueSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */   KQueueSocketChannel(Channel parent, BsdSocket fd, InetSocketAddress remoteAddress) {
/* 42 */     super(parent, fd, remoteAddress);
/* 43 */     this.config = new KQueueSocketChannelConfig(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress remoteAddress() {
/* 48 */     return (InetSocketAddress)super.remoteAddress();
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress localAddress() {
/* 53 */     return (InetSocketAddress)super.localAddress();
/*    */   }
/*    */ 
/*    */   
/*    */   public KQueueSocketChannelConfig config() {
/* 58 */     return this.config;
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerSocketChannel parent() {
/* 63 */     return (ServerSocketChannel)super.parent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
/* 68 */     return new KQueueSocketChannelUnsafe();
/*    */   }
/*    */   
/*    */   private final class KQueueSocketChannelUnsafe
/*    */     extends AbstractKQueueStreamChannel.KQueueStreamUnsafe {
/*    */     private KQueueSocketChannelUnsafe() {}
/*    */     
/*    */     protected Executor prepareToClose() {
/*    */       try {
/* 77 */         if (KQueueSocketChannel.this.isOpen() && KQueueSocketChannel.this.config().getSoLinger() > 0) {
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 82 */           ((KQueueEventLoop)KQueueSocketChannel.this.eventLoop()).remove(KQueueSocketChannel.this);
/* 83 */           return (Executor)GlobalEventExecutor.INSTANCE;
/*    */         } 
/* 85 */       } catch (Throwable throwable) {}
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 90 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */