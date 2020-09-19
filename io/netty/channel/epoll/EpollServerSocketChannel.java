/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.channel.unix.NativeInetAddress;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
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
/*     */ public final class EpollServerSocketChannel
/*     */   extends AbstractEpollServerChannel
/*     */   implements ServerSocketChannel
/*     */ {
/*     */   private final EpollServerSocketChannelConfig config;
/*  40 */   private volatile Collection<InetAddress> tcpMd5SigAddresses = Collections.emptyList();
/*     */   
/*     */   public EpollServerSocketChannel() {
/*  43 */     super(LinuxSocket.newSocketStream(), false);
/*  44 */     this.config = new EpollServerSocketChannelConfig(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollServerSocketChannel(int fd) {
/*  50 */     this(new LinuxSocket(fd));
/*     */   }
/*     */   
/*     */   EpollServerSocketChannel(LinuxSocket fd) {
/*  54 */     super(fd);
/*  55 */     this.config = new EpollServerSocketChannelConfig(this);
/*     */   }
/*     */   
/*     */   EpollServerSocketChannel(LinuxSocket fd, boolean active) {
/*  59 */     super(fd, active);
/*  60 */     this.config = new EpollServerSocketChannelConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCompatible(EventLoop loop) {
/*  65 */     return loop instanceof EpollEventLoop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/*  70 */     super.doBind(localAddress);
/*  71 */     if (Native.IS_SUPPORTING_TCP_FASTOPEN && this.config.getTcpFastopen() > 0) {
/*  72 */       this.socket.setTcpFastOpen(this.config.getTcpFastopen());
/*     */     }
/*  74 */     this.socket.listen(this.config.getBacklog());
/*  75 */     this.active = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress remoteAddress() {
/*  80 */     return (InetSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress localAddress() {
/*  85 */     return (InetSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollServerSocketChannelConfig config() {
/*  90 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Channel newChildChannel(int fd, byte[] address, int offset, int len) throws Exception {
/*  95 */     return (Channel)new EpollSocketChannel((Channel)this, new LinuxSocket(fd), NativeInetAddress.address(address, offset, len));
/*     */   }
/*     */   
/*     */   Collection<InetAddress> tcpMd5SigAddresses() {
/*  99 */     return this.tcpMd5SigAddresses;
/*     */   }
/*     */   
/*     */   void setTcpMd5Sig(Map<InetAddress, byte[]> keys) throws IOException {
/* 103 */     this.tcpMd5SigAddresses = TcpMd5Util.newTcpMd5Sigs(this, this.tcpMd5SigAddresses, keys);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */