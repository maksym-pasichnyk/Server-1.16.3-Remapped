/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.channel.unix.IovArray;
/*     */ import io.netty.channel.unix.Limits;
/*     */ import io.netty.channel.unix.NativeInetAddress;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
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
/*     */ final class NativeDatagramPacketArray
/*     */   implements ChannelOutboundBuffer.MessageProcessor
/*     */ {
/*  35 */   private static final FastThreadLocal<NativeDatagramPacketArray> ARRAY = new FastThreadLocal<NativeDatagramPacketArray>()
/*     */     {
/*     */       protected NativeDatagramPacketArray initialValue() throws Exception
/*     */       {
/*  39 */         return new NativeDatagramPacketArray();
/*     */       }
/*     */ 
/*     */       
/*     */       protected void onRemoval(NativeDatagramPacketArray value) throws Exception {
/*  44 */         NativeDatagramPacketArray.NativeDatagramPacket[] packetsArray = value.packets;
/*     */         
/*  46 */         for (NativeDatagramPacketArray.NativeDatagramPacket datagramPacket : packetsArray) {
/*  47 */           datagramPacket.release();
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  53 */   private final NativeDatagramPacket[] packets = new NativeDatagramPacket[Limits.UIO_MAX_IOV];
/*     */   private int count;
/*     */   
/*     */   private NativeDatagramPacketArray() {
/*  57 */     for (int i = 0; i < this.packets.length; i++) {
/*  58 */       this.packets[i] = new NativeDatagramPacket();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean add(DatagramPacket packet) {
/*  67 */     if (this.count == this.packets.length) {
/*  68 */       return false;
/*     */     }
/*  70 */     ByteBuf content = (ByteBuf)packet.content();
/*  71 */     int len = content.readableBytes();
/*  72 */     if (len == 0) {
/*  73 */       return true;
/*     */     }
/*  75 */     NativeDatagramPacket p = this.packets[this.count];
/*  76 */     InetSocketAddress recipient = (InetSocketAddress)packet.recipient();
/*  77 */     if (!p.init(content, recipient)) {
/*  78 */       return false;
/*     */     }
/*     */     
/*  81 */     this.count++;
/*  82 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean processMessage(Object msg) throws Exception {
/*  87 */     return (msg instanceof DatagramPacket && add((DatagramPacket)msg));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int count() {
/*  94 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NativeDatagramPacket[] packets() {
/* 101 */     return this.packets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static NativeDatagramPacketArray getInstance(ChannelOutboundBuffer buffer) throws Exception {
/* 109 */     NativeDatagramPacketArray array = (NativeDatagramPacketArray)ARRAY.get();
/* 110 */     array.count = 0;
/* 111 */     buffer.forEachFlushedMessage(array);
/* 112 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class NativeDatagramPacket
/*     */   {
/* 123 */     private final IovArray array = new IovArray();
/*     */     
/*     */     private long memoryAddress;
/*     */     
/*     */     private int count;
/*     */     
/*     */     private byte[] addr;
/*     */     private int scopeId;
/*     */     private int port;
/*     */     
/*     */     private void release() {
/* 134 */       this.array.release();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean init(ByteBuf buf, InetSocketAddress recipient) {
/* 141 */       this.array.clear();
/* 142 */       if (!this.array.add(buf)) {
/* 143 */         return false;
/*     */       }
/*     */       
/* 146 */       this.memoryAddress = this.array.memoryAddress(0);
/* 147 */       this.count = this.array.count();
/*     */       
/* 149 */       InetAddress address = recipient.getAddress();
/* 150 */       if (address instanceof Inet6Address) {
/* 151 */         this.addr = address.getAddress();
/* 152 */         this.scopeId = ((Inet6Address)address).getScopeId();
/*     */       } else {
/* 154 */         this.addr = NativeInetAddress.ipv4MappedIpv6Address(address.getAddress());
/* 155 */         this.scopeId = 0;
/*     */       } 
/* 157 */       this.port = recipient.getPort();
/* 158 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\NativeDatagramPacketArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */