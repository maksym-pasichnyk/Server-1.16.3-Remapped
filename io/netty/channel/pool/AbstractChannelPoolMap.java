/*    */ package io.netty.channel.pool;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import io.netty.util.internal.ReadOnlyIterator;
/*    */ import java.io.Closeable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ public abstract class AbstractChannelPoolMap<K, P extends ChannelPool>
/*    */   implements ChannelPoolMap<K, P>, Iterable<Map.Entry<K, P>>, Closeable
/*    */ {
/* 34 */   private final ConcurrentMap<K, P> map = PlatformDependent.newConcurrentHashMap();
/*    */ 
/*    */   
/*    */   public final P get(K key) {
/* 38 */     ChannelPool channelPool = (ChannelPool)this.map.get(ObjectUtil.checkNotNull(key, "key"));
/* 39 */     if (channelPool == null) {
/* 40 */       channelPool = (ChannelPool)newPool(key);
/* 41 */       ChannelPool channelPool1 = (ChannelPool)this.map.putIfAbsent(key, (P)channelPool);
/* 42 */       if (channelPool1 != null) {
/*    */         
/* 44 */         channelPool.close();
/* 45 */         channelPool = channelPool1;
/*    */       } 
/*    */     } 
/* 48 */     return (P)channelPool;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean remove(K key) {
/* 57 */     ChannelPool channelPool = (ChannelPool)this.map.remove(ObjectUtil.checkNotNull(key, "key"));
/* 58 */     if (channelPool != null) {
/* 59 */       channelPool.close();
/* 60 */       return true;
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Iterator<Map.Entry<K, P>> iterator() {
/* 67 */     return (Iterator<Map.Entry<K, P>>)new ReadOnlyIterator(this.map.entrySet().iterator());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final int size() {
/* 74 */     return this.map.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isEmpty() {
/* 81 */     return this.map.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean contains(K key) {
/* 86 */     return this.map.containsKey(ObjectUtil.checkNotNull(key, "key"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract P newPool(K paramK);
/*    */ 
/*    */ 
/*    */   
/*    */   public final void close() {
/* 96 */     for (K key : this.map.keySet())
/* 97 */       remove(key); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\pool\AbstractChannelPoolMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */