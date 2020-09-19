/*    */ package io.netty.channel.unix;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.UnknownHostException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UnixChannelUtil
/*    */ {
/*    */   public static boolean isBufferCopyNeededForWrite(ByteBuf byteBuf) {
/* 37 */     return isBufferCopyNeededForWrite(byteBuf, Limits.IOV_MAX);
/*    */   }
/*    */   
/*    */   static boolean isBufferCopyNeededForWrite(ByteBuf byteBuf, int iovMax) {
/* 41 */     return (!byteBuf.hasMemoryAddress() && (!byteBuf.isDirect() || byteBuf.nioBufferCount() > iovMax));
/*    */   }
/*    */   
/*    */   public static InetSocketAddress computeRemoteAddr(InetSocketAddress remoteAddr, InetSocketAddress osRemoteAddr) {
/* 45 */     if (osRemoteAddr != null) {
/* 46 */       if (PlatformDependent.javaVersion() >= 7) {
/*    */         
/*    */         try {
/*    */ 
/*    */           
/* 51 */           return new InetSocketAddress(InetAddress.getByAddress(remoteAddr.getHostString(), osRemoteAddr
/* 52 */                 .getAddress().getAddress()), osRemoteAddr
/* 53 */               .getPort());
/* 54 */         } catch (UnknownHostException unknownHostException) {}
/*    */       }
/*    */ 
/*    */       
/* 58 */       return osRemoteAddr;
/*    */     } 
/* 60 */     return remoteAddr;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\UnixChannelUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */