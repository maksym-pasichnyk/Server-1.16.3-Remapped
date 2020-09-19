/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
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
/*    */ final class TcpMd5Util
/*    */ {
/*    */   static Collection<InetAddress> newTcpMd5Sigs(AbstractEpollChannel channel, Collection<InetAddress> current, Map<InetAddress, byte[]> newKeys) throws IOException {
/* 32 */     ObjectUtil.checkNotNull(channel, "channel");
/* 33 */     ObjectUtil.checkNotNull(current, "current");
/* 34 */     ObjectUtil.checkNotNull(newKeys, "newKeys");
/*    */ 
/*    */     
/* 37 */     for (Map.Entry<InetAddress, byte[]> e : newKeys.entrySet()) {
/* 38 */       byte[] key = e.getValue();
/* 39 */       if (e.getKey() == null) {
/* 40 */         throw new IllegalArgumentException("newKeys contains an entry with null address: " + newKeys);
/*    */       }
/* 42 */       if (key == null) {
/* 43 */         throw new NullPointerException("newKeys[" + e.getKey() + ']');
/*    */       }
/* 45 */       if (key.length == 0) {
/* 46 */         throw new IllegalArgumentException("newKeys[" + e.getKey() + "] has an empty key.");
/*    */       }
/* 48 */       if (key.length > Native.TCP_MD5SIG_MAXKEYLEN) {
/* 49 */         throw new IllegalArgumentException("newKeys[" + e.getKey() + "] has a key with invalid length; should not exceed the maximum length (" + Native.TCP_MD5SIG_MAXKEYLEN + ')');
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 56 */     for (InetAddress addr : current) {
/* 57 */       if (!newKeys.containsKey(addr)) {
/* 58 */         channel.socket.setTcpMd5Sig(addr, null);
/*    */       }
/*    */     } 
/*    */     
/* 62 */     if (newKeys.isEmpty()) {
/* 63 */       return Collections.emptySet();
/*    */     }
/*    */ 
/*    */     
/* 67 */     Collection<InetAddress> addresses = new ArrayList<InetAddress>(newKeys.size());
/* 68 */     for (Map.Entry<InetAddress, byte[]> e : newKeys.entrySet()) {
/* 69 */       channel.socket.setTcpMd5Sig(e.getKey(), e.getValue());
/* 70 */       addresses.add(e.getKey());
/*    */     } 
/*    */     
/* 73 */     return addresses;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\TcpMd5Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */