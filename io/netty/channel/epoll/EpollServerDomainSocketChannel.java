/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.unix.DomainSocketAddress;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.ServerDomainSocketChannel;
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.net.SocketAddress;
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
/*     */ public final class EpollServerDomainSocketChannel
/*     */   extends AbstractEpollServerChannel
/*     */   implements ServerDomainSocketChannel
/*     */ {
/*  32 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EpollServerDomainSocketChannel.class);
/*     */ 
/*     */   
/*  35 */   private final EpollServerChannelConfig config = new EpollServerChannelConfig(this);
/*     */   private volatile DomainSocketAddress local;
/*     */   
/*     */   public EpollServerDomainSocketChannel() {
/*  39 */     super(LinuxSocket.newSocketDomain(), false);
/*     */   }
/*     */   
/*     */   public EpollServerDomainSocketChannel(int fd) {
/*  43 */     super(fd);
/*     */   }
/*     */   
/*     */   EpollServerDomainSocketChannel(LinuxSocket fd) {
/*  47 */     super(fd);
/*     */   }
/*     */   
/*     */   EpollServerDomainSocketChannel(LinuxSocket fd, boolean active) {
/*  51 */     super(fd, active);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Channel newChildChannel(int fd, byte[] addr, int offset, int len) throws Exception {
/*  56 */     return (Channel)new EpollDomainSocketChannel((Channel)this, (FileDescriptor)new Socket(fd));
/*     */   }
/*     */ 
/*     */   
/*     */   protected DomainSocketAddress localAddress0() {
/*  61 */     return this.local;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/*  66 */     this.socket.bind(localAddress);
/*  67 */     this.socket.listen(this.config.getBacklog());
/*  68 */     this.local = (DomainSocketAddress)localAddress;
/*  69 */     this.active = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/*     */     try {
/*  75 */       super.doClose();
/*     */     } finally {
/*  77 */       DomainSocketAddress local = this.local;
/*  78 */       if (local != null) {
/*     */         
/*  80 */         File socketFile = new File(local.path());
/*  81 */         boolean success = socketFile.delete();
/*  82 */         if (!success && logger.isDebugEnabled()) {
/*  83 */           logger.debug("Failed to delete a domain socket file: {}", local.path());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerChannelConfig config() {
/*  91 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketAddress remoteAddress() {
/*  96 */     return (DomainSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketAddress localAddress() {
/* 101 */     return (DomainSocketAddress)super.localAddress();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollServerDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */