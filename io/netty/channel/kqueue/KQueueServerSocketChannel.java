/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelConfig;
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.channel.socket.ServerSocketChannel;
/*    */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*    */ import io.netty.channel.unix.NativeInetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class KQueueServerSocketChannel
/*    */   extends AbstractKQueueServerChannel
/*    */   implements ServerSocketChannel
/*    */ {
/*    */   private final KQueueServerSocketChannelConfig config;
/*    */   
/*    */   public KQueueServerSocketChannel() {
/* 34 */     super(BsdSocket.newSocketStream(), false);
/* 35 */     this.config = new KQueueServerSocketChannelConfig(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public KQueueServerSocketChannel(int fd) {
/* 41 */     this(new BsdSocket(fd));
/*    */   }
/*    */   
/*    */   KQueueServerSocketChannel(BsdSocket fd) {
/* 45 */     super(fd);
/* 46 */     this.config = new KQueueServerSocketChannelConfig(this);
/*    */   }
/*    */   
/*    */   KQueueServerSocketChannel(BsdSocket fd, boolean active) {
/* 50 */     super(fd, active);
/* 51 */     this.config = new KQueueServerSocketChannelConfig(this);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isCompatible(EventLoop loop) {
/* 56 */     return loop instanceof KQueueEventLoop;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 61 */     super.doBind(localAddress);
/*    */ 
/*    */     
/* 64 */     this.socket.listen(this.config.getBacklog());
/* 65 */     this.active = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress remoteAddress() {
/* 70 */     return (InetSocketAddress)super.remoteAddress();
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress localAddress() {
/* 75 */     return (InetSocketAddress)super.localAddress();
/*    */   }
/*    */ 
/*    */   
/*    */   public KQueueServerSocketChannelConfig config() {
/* 80 */     return this.config;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Channel newChildChannel(int fd, byte[] address, int offset, int len) throws Exception {
/* 85 */     return (Channel)new KQueueSocketChannel((Channel)this, new BsdSocket(fd), NativeInetAddress.address(address, offset, len));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */