/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.unix.DomainSocketAddress;
/*     */ import io.netty.channel.unix.DomainSocketChannel;
/*     */ import io.netty.channel.unix.DomainSocketChannelConfig;
/*     */ import io.netty.channel.unix.DomainSocketReadMode;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.channel.unix.PeerCredentials;
/*     */ import java.io.IOException;
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
/*     */ public final class EpollDomainSocketChannel
/*     */   extends AbstractEpollStreamChannel
/*     */   implements DomainSocketChannel
/*     */ {
/*  34 */   private final EpollDomainSocketChannelConfig config = new EpollDomainSocketChannelConfig(this);
/*     */   
/*     */   private volatile DomainSocketAddress local;
/*     */   private volatile DomainSocketAddress remote;
/*     */   
/*     */   public EpollDomainSocketChannel() {
/*  40 */     super(LinuxSocket.newSocketDomain(), false);
/*     */   }
/*     */   
/*     */   EpollDomainSocketChannel(Channel parent, FileDescriptor fd) {
/*  44 */     super(parent, new LinuxSocket(fd.intValue()));
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannel(int fd) {
/*  48 */     super(fd);
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannel(Channel parent, LinuxSocket fd) {
/*  52 */     super(parent, fd);
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannel(int fd, boolean active) {
/*  56 */     super(new LinuxSocket(fd), active);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
/*  61 */     return new EpollDomainUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected DomainSocketAddress localAddress0() {
/*  66 */     return this.local;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DomainSocketAddress remoteAddress0() {
/*  71 */     return this.remote;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/*  76 */     this.socket.bind(localAddress);
/*  77 */     this.local = (DomainSocketAddress)localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollDomainSocketChannelConfig config() {
/*  82 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/*  87 */     if (super.doConnect(remoteAddress, localAddress)) {
/*  88 */       this.local = (DomainSocketAddress)localAddress;
/*  89 */       this.remote = (DomainSocketAddress)remoteAddress;
/*  90 */       return true;
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketAddress remoteAddress() {
/*  97 */     return (DomainSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketAddress localAddress() {
/* 102 */     return (DomainSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
/* 107 */     Object msg = in.current();
/* 108 */     if (msg instanceof FileDescriptor && this.socket.sendFd(((FileDescriptor)msg).intValue()) > 0) {
/*     */       
/* 110 */       in.remove();
/* 111 */       return 1;
/*     */     } 
/* 113 */     return super.doWriteSingle(in);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 118 */     if (msg instanceof FileDescriptor) {
/* 119 */       return msg;
/*     */     }
/* 121 */     return super.filterOutboundMessage(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PeerCredentials peerCredentials() throws IOException {
/* 130 */     return this.socket.getPeerCredentials();
/*     */   }
/*     */   
/*     */   private final class EpollDomainUnsafe
/*     */     extends AbstractEpollStreamChannel.EpollStreamUnsafe {
/*     */     void epollInReady() {
/* 136 */       switch (EpollDomainSocketChannel.this.config().getReadMode()) {
/*     */         case BYTES:
/* 138 */           super.epollInReady();
/*     */           return;
/*     */         case FILE_DESCRIPTORS:
/* 141 */           epollInReadFd();
/*     */           return;
/*     */       } 
/* 144 */       throw new Error();
/*     */     }
/*     */     private EpollDomainUnsafe() {}
/*     */     
/*     */     private void epollInReadFd() {
/* 149 */       if (EpollDomainSocketChannel.this.socket.isInputShutdown()) {
/* 150 */         clearEpollIn0();
/*     */         return;
/*     */       } 
/* 153 */       EpollDomainSocketChannelConfig epollDomainSocketChannelConfig = EpollDomainSocketChannel.this.config();
/* 154 */       EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/* 155 */       allocHandle.edgeTriggered(EpollDomainSocketChannel.this.isFlagSet(Native.EPOLLET));
/*     */       
/* 157 */       ChannelPipeline pipeline = EpollDomainSocketChannel.this.pipeline();
/* 158 */       allocHandle.reset((ChannelConfig)epollDomainSocketChannelConfig);
/* 159 */       epollInBefore();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*     */         do {
/* 166 */           allocHandle.lastBytesRead(EpollDomainSocketChannel.this.socket.recvFd());
/* 167 */           switch (allocHandle.lastBytesRead()) {
/*     */             case 0:
/*     */               break;
/*     */             case -1:
/* 171 */               close(voidPromise());
/*     */               return;
/*     */           } 
/* 174 */           allocHandle.incMessagesRead(1);
/* 175 */           this.readPending = false;
/* 176 */           pipeline.fireChannelRead(new FileDescriptor(allocHandle.lastBytesRead()));
/*     */         
/*     */         }
/* 179 */         while (allocHandle.continueReading());
/*     */         
/* 181 */         allocHandle.readComplete();
/* 182 */         pipeline.fireChannelReadComplete();
/* 183 */       } catch (Throwable t) {
/* 184 */         allocHandle.readComplete();
/* 185 */         pipeline.fireChannelReadComplete();
/* 186 */         pipeline.fireExceptionCaught(t);
/*     */       } finally {
/* 188 */         epollInFinally((ChannelConfig)epollDomainSocketChannelConfig);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */