/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.ChannelOption;
/*    */ import io.netty.channel.unix.UnixChannelOption;
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
/*    */ public final class KQueueChannelOption<T>
/*    */   extends UnixChannelOption<T>
/*    */ {
/* 25 */   public static final ChannelOption<Integer> SO_SNDLOWAT = valueOf(KQueueChannelOption.class, "SO_SNDLOWAT");
/* 26 */   public static final ChannelOption<Boolean> TCP_NOPUSH = valueOf(KQueueChannelOption.class, "TCP_NOPUSH");
/*    */   
/* 28 */   public static final ChannelOption<AcceptFilter> SO_ACCEPTFILTER = valueOf(KQueueChannelOption.class, "SO_ACCEPTFILTER");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final ChannelOption<Boolean> RCV_ALLOC_TRANSPORT_PROVIDES_GUESS = valueOf(KQueueChannelOption.class, "RCV_ALLOC_TRANSPORT_PROVIDES_GUESS");
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */