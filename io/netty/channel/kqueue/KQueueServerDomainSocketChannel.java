/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelConfig;
/*    */ import io.netty.channel.unix.DomainSocketAddress;
/*    */ import io.netty.channel.unix.ServerDomainSocketChannel;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ public final class KQueueServerDomainSocketChannel
/*    */   extends AbstractKQueueServerChannel
/*    */   implements ServerDomainSocketChannel
/*    */ {
/* 33 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(KQueueServerDomainSocketChannel.class);
/*    */ 
/*    */   
/* 36 */   private final KQueueServerChannelConfig config = new KQueueServerChannelConfig(this);
/*    */   private volatile DomainSocketAddress local;
/*    */   
/*    */   public KQueueServerDomainSocketChannel() {
/* 40 */     super(BsdSocket.newSocketDomain(), false);
/*    */   }
/*    */   
/*    */   public KQueueServerDomainSocketChannel(int fd) {
/* 44 */     this(new BsdSocket(fd), false);
/*    */   }
/*    */   
/*    */   KQueueServerDomainSocketChannel(BsdSocket socket, boolean active) {
/* 48 */     super(socket, active);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Channel newChildChannel(int fd, byte[] addr, int offset, int len) throws Exception {
/* 53 */     return (Channel)new KQueueDomainSocketChannel((Channel)this, new BsdSocket(fd));
/*    */   }
/*    */ 
/*    */   
/*    */   protected DomainSocketAddress localAddress0() {
/* 58 */     return this.local;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 63 */     this.socket.bind(localAddress);
/* 64 */     this.socket.listen(this.config.getBacklog());
/* 65 */     this.local = (DomainSocketAddress)localAddress;
/* 66 */     this.active = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doClose() throws Exception {
/*    */     try {
/* 72 */       super.doClose();
/*    */     } finally {
/* 74 */       DomainSocketAddress local = this.local;
/* 75 */       if (local != null) {
/*    */         
/* 77 */         File socketFile = new File(local.path());
/* 78 */         boolean success = socketFile.delete();
/* 79 */         if (!success && logger.isDebugEnabled()) {
/* 80 */           logger.debug("Failed to delete a domain socket file: {}", local.path());
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public KQueueServerChannelConfig config() {
/* 88 */     return this.config;
/*    */   }
/*    */ 
/*    */   
/*    */   public DomainSocketAddress remoteAddress() {
/* 93 */     return (DomainSocketAddress)super.remoteAddress();
/*    */   }
/*    */ 
/*    */   
/*    */   public DomainSocketAddress localAddress() {
/* 98 */     return (DomainSocketAddress)super.localAddress();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueServerDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */