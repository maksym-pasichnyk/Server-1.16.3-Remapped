/*     */ package io.netty.channel.kqueue;
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
/*     */ 
/*     */ public final class KQueueDomainSocketChannel
/*     */   extends AbstractKQueueStreamChannel
/*     */   implements DomainSocketChannel
/*     */ {
/*  35 */   private final KQueueDomainSocketChannelConfig config = new KQueueDomainSocketChannelConfig(this);
/*     */   
/*     */   private volatile DomainSocketAddress local;
/*     */   private volatile DomainSocketAddress remote;
/*     */   
/*     */   public KQueueDomainSocketChannel() {
/*  41 */     super((Channel)null, BsdSocket.newSocketDomain(), false);
/*     */   }
/*     */   
/*     */   public KQueueDomainSocketChannel(int fd) {
/*  45 */     this((Channel)null, new BsdSocket(fd));
/*     */   }
/*     */   
/*     */   KQueueDomainSocketChannel(Channel parent, BsdSocket fd) {
/*  49 */     super(parent, fd, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractKQueueChannel.AbstractKQueueUnsafe newUnsafe() {
/*  54 */     return new KQueueDomainUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected DomainSocketAddress localAddress0() {
/*  59 */     return this.local;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DomainSocketAddress remoteAddress0() {
/*  64 */     return this.remote;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/*  69 */     this.socket.bind(localAddress);
/*  70 */     this.local = (DomainSocketAddress)localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueDomainSocketChannelConfig config() {
/*  75 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/*  80 */     if (super.doConnect(remoteAddress, localAddress)) {
/*  81 */       this.local = (DomainSocketAddress)localAddress;
/*  82 */       this.remote = (DomainSocketAddress)remoteAddress;
/*  83 */       return true;
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketAddress remoteAddress() {
/*  90 */     return (DomainSocketAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public DomainSocketAddress localAddress() {
/*  95 */     return (DomainSocketAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
/* 100 */     Object msg = in.current();
/* 101 */     if (msg instanceof FileDescriptor && this.socket.sendFd(((FileDescriptor)msg).intValue()) > 0) {
/*     */       
/* 103 */       in.remove();
/* 104 */       return 1;
/*     */     } 
/* 106 */     return super.doWriteSingle(in);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object filterOutboundMessage(Object msg) {
/* 111 */     if (msg instanceof FileDescriptor) {
/* 112 */       return msg;
/*     */     }
/* 114 */     return super.filterOutboundMessage(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PeerCredentials peerCredentials() throws IOException {
/* 123 */     return this.socket.getPeerCredentials();
/*     */   }
/*     */   
/*     */   private final class KQueueDomainUnsafe
/*     */     extends AbstractKQueueStreamChannel.KQueueStreamUnsafe {
/*     */     void readReady(KQueueRecvByteAllocatorHandle allocHandle) {
/* 129 */       switch (KQueueDomainSocketChannel.this.config().getReadMode()) {
/*     */         case BYTES:
/* 131 */           super.readReady(allocHandle);
/*     */           return;
/*     */         case FILE_DESCRIPTORS:
/* 134 */           readReadyFd();
/*     */           return;
/*     */       } 
/* 137 */       throw new Error();
/*     */     }
/*     */     private KQueueDomainUnsafe() {}
/*     */     
/*     */     private void readReadyFd() {
/* 142 */       if (KQueueDomainSocketChannel.this.socket.isInputShutdown()) {
/* 143 */         clearReadFilter0();
/*     */         return;
/*     */       } 
/* 146 */       KQueueDomainSocketChannelConfig kQueueDomainSocketChannelConfig = KQueueDomainSocketChannel.this.config();
/* 147 */       KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/*     */       
/* 149 */       ChannelPipeline pipeline = KQueueDomainSocketChannel.this.pipeline();
/* 150 */       allocHandle.reset((ChannelConfig)kQueueDomainSocketChannelConfig);
/* 151 */       readReadyBefore();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*     */         do {
/* 158 */           int recvFd = KQueueDomainSocketChannel.this.socket.recvFd();
/* 159 */           switch (recvFd) {
/*     */             case 0:
/* 161 */               allocHandle.lastBytesRead(0);
/*     */               break;
/*     */             case -1:
/* 164 */               allocHandle.lastBytesRead(-1);
/* 165 */               close(voidPromise());
/*     */               return;
/*     */           } 
/* 168 */           allocHandle.lastBytesRead(1);
/* 169 */           allocHandle.incMessagesRead(1);
/* 170 */           this.readPending = false;
/* 171 */           pipeline.fireChannelRead(new FileDescriptor(recvFd));
/*     */         
/*     */         }
/* 174 */         while (allocHandle.continueReading());
/*     */         
/* 176 */         allocHandle.readComplete();
/* 177 */         pipeline.fireChannelReadComplete();
/* 178 */       } catch (Throwable t) {
/* 179 */         allocHandle.readComplete();
/* 180 */         pipeline.fireChannelReadComplete();
/* 181 */         pipeline.fireExceptionCaught(t);
/*     */       } finally {
/* 183 */         readReadyFinally((ChannelConfig)kQueueDomainSocketChannelConfig);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueDomainSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */