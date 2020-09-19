/*     */ package io.netty.channel.udt.nio;
/*     */ 
/*     */ import com.barchart.udt.SocketUDT;
/*     */ import com.barchart.udt.TypeUDT;
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import com.barchart.udt.nio.KindUDT;
/*     */ import com.barchart.udt.nio.RendezvousChannelUDT;
/*     */ import com.barchart.udt.nio.SelectorProviderUDT;
/*     */ import com.barchart.udt.nio.ServerSocketChannelUDT;
/*     */ import com.barchart.udt.nio.SocketChannelUDT;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.udt.UdtChannel;
/*     */ import io.netty.channel.udt.UdtServerChannel;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.spi.SelectorProvider;
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
/*     */ @Deprecated
/*     */ public final class NioUdtProvider<T extends UdtChannel>
/*     */   implements ChannelFactory<T>
/*     */ {
/*  51 */   public static final ChannelFactory<UdtServerChannel> BYTE_ACCEPTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.ACCEPTOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static final ChannelFactory<UdtChannel> BYTE_CONNECTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.CONNECTOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static final SelectorProvider BYTE_PROVIDER = (SelectorProvider)SelectorProviderUDT.STREAM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final ChannelFactory<UdtChannel> BYTE_RENDEZVOUS = new NioUdtProvider(TypeUDT.STREAM, KindUDT.RENDEZVOUS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final ChannelFactory<UdtServerChannel> MESSAGE_ACCEPTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.ACCEPTOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static final ChannelFactory<UdtChannel> MESSAGE_CONNECTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.CONNECTOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final SelectorProvider MESSAGE_PROVIDER = (SelectorProvider)SelectorProviderUDT.DATAGRAM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static final ChannelFactory<UdtChannel> MESSAGE_RENDEZVOUS = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.RENDEZVOUS);
/*     */ 
/*     */   
/*     */   private final KindUDT kind;
/*     */ 
/*     */   
/*     */   private final TypeUDT type;
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChannelUDT channelUDT(Channel channel) {
/* 109 */     if (channel instanceof NioUdtByteAcceptorChannel) {
/* 110 */       return (ChannelUDT)((NioUdtByteAcceptorChannel)channel).javaChannel();
/*     */     }
/* 112 */     if (channel instanceof NioUdtByteRendezvousChannel) {
/* 113 */       return (ChannelUDT)((NioUdtByteRendezvousChannel)channel).javaChannel();
/*     */     }
/* 115 */     if (channel instanceof NioUdtByteConnectorChannel) {
/* 116 */       return (ChannelUDT)((NioUdtByteConnectorChannel)channel).javaChannel();
/*     */     }
/*     */ 
/*     */     
/* 120 */     if (channel instanceof NioUdtMessageAcceptorChannel) {
/* 121 */       return (ChannelUDT)((NioUdtMessageAcceptorChannel)channel).javaChannel();
/*     */     }
/* 123 */     if (channel instanceof NioUdtMessageRendezvousChannel) {
/* 124 */       return (ChannelUDT)((NioUdtMessageRendezvousChannel)channel).javaChannel();
/*     */     }
/* 126 */     if (channel instanceof NioUdtMessageConnectorChannel) {
/* 127 */       return (ChannelUDT)((NioUdtMessageConnectorChannel)channel).javaChannel();
/*     */     }
/*     */     
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ServerSocketChannelUDT newAcceptorChannelUDT(TypeUDT type) {
/*     */     try {
/* 139 */       return SelectorProviderUDT.from(type).openServerSocketChannel();
/* 140 */     } catch (IOException e) {
/* 141 */       throw new ChannelException("failed to open a server socket channel", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static SocketChannelUDT newConnectorChannelUDT(TypeUDT type) {
/*     */     try {
/* 150 */       return SelectorProviderUDT.from(type).openSocketChannel();
/* 151 */     } catch (IOException e) {
/* 152 */       throw new ChannelException("failed to open a socket channel", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static RendezvousChannelUDT newRendezvousChannelUDT(TypeUDT type) {
/*     */     try {
/* 162 */       return SelectorProviderUDT.from(type).openRendezvousChannel();
/* 163 */     } catch (IOException e) {
/* 164 */       throw new ChannelException("failed to open a rendezvous channel", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SocketUDT socketUDT(Channel channel) {
/* 175 */     ChannelUDT channelUDT = channelUDT(channel);
/* 176 */     if (channelUDT == null) {
/* 177 */       return null;
/*     */     }
/* 179 */     return channelUDT.socketUDT();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NioUdtProvider(TypeUDT type, KindUDT kind) {
/* 190 */     this.type = type;
/* 191 */     this.kind = kind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KindUDT kind() {
/* 198 */     return this.kind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T newChannel() {
/* 208 */     switch (this.kind) {
/*     */       case ACCEPTOR:
/* 210 */         switch (this.type) {
/*     */           case ACCEPTOR:
/* 212 */             return (T)new NioUdtMessageAcceptorChannel();
/*     */           case CONNECTOR:
/* 214 */             return (T)new NioUdtByteAcceptorChannel();
/*     */         } 
/* 216 */         throw new IllegalStateException("wrong type=" + this.type);
/*     */       
/*     */       case CONNECTOR:
/* 219 */         switch (this.type) {
/*     */           case ACCEPTOR:
/* 221 */             return (T)new NioUdtMessageConnectorChannel();
/*     */           case CONNECTOR:
/* 223 */             return (T)new NioUdtByteConnectorChannel();
/*     */         } 
/* 225 */         throw new IllegalStateException("wrong type=" + this.type);
/*     */       
/*     */       case RENDEZVOUS:
/* 228 */         switch (this.type) {
/*     */           case ACCEPTOR:
/* 230 */             return (T)new NioUdtMessageRendezvousChannel();
/*     */           case CONNECTOR:
/* 232 */             return (T)new NioUdtByteRendezvousChannel();
/*     */         } 
/* 234 */         throw new IllegalStateException("wrong type=" + this.type);
/*     */     } 
/*     */     
/* 237 */     throw new IllegalStateException("wrong kind=" + this.kind);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUDT type() {
/* 245 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\nio\NioUdtProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */