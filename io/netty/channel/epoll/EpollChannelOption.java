/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.channel.ChannelOption;
/*    */ import io.netty.channel.unix.UnixChannelOption;
/*    */ import java.net.InetAddress;
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
/*    */ public final class EpollChannelOption<T>
/*    */   extends UnixChannelOption<T>
/*    */ {
/* 25 */   public static final ChannelOption<Boolean> TCP_CORK = valueOf(EpollChannelOption.class, "TCP_CORK");
/* 26 */   public static final ChannelOption<Long> TCP_NOTSENT_LOWAT = valueOf(EpollChannelOption.class, "TCP_NOTSENT_LOWAT");
/* 27 */   public static final ChannelOption<Integer> TCP_KEEPIDLE = valueOf(EpollChannelOption.class, "TCP_KEEPIDLE");
/* 28 */   public static final ChannelOption<Integer> TCP_KEEPINTVL = valueOf(EpollChannelOption.class, "TCP_KEEPINTVL");
/* 29 */   public static final ChannelOption<Integer> TCP_KEEPCNT = valueOf(EpollChannelOption.class, "TCP_KEEPCNT");
/*    */   
/* 31 */   public static final ChannelOption<Integer> TCP_USER_TIMEOUT = valueOf(EpollChannelOption.class, "TCP_USER_TIMEOUT");
/* 32 */   public static final ChannelOption<Boolean> IP_FREEBIND = valueOf("IP_FREEBIND");
/* 33 */   public static final ChannelOption<Boolean> IP_TRANSPARENT = valueOf("IP_TRANSPARENT");
/* 34 */   public static final ChannelOption<Boolean> IP_RECVORIGDSTADDR = valueOf("IP_RECVORIGDSTADDR");
/* 35 */   public static final ChannelOption<Integer> TCP_FASTOPEN = valueOf(EpollChannelOption.class, "TCP_FASTOPEN");
/*    */   
/* 37 */   public static final ChannelOption<Boolean> TCP_FASTOPEN_CONNECT = valueOf(EpollChannelOption.class, "TCP_FASTOPEN_CONNECT");
/*    */   
/* 39 */   public static final ChannelOption<Integer> TCP_DEFER_ACCEPT = ChannelOption.valueOf(EpollChannelOption.class, "TCP_DEFER_ACCEPT");
/* 40 */   public static final ChannelOption<Boolean> TCP_QUICKACK = valueOf(EpollChannelOption.class, "TCP_QUICKACK");
/*    */ 
/*    */   
/* 43 */   public static final ChannelOption<EpollMode> EPOLL_MODE = ChannelOption.valueOf(EpollChannelOption.class, "EPOLL_MODE");
/*    */   
/* 45 */   public static final ChannelOption<Map<InetAddress, byte[]>> TCP_MD5SIG = valueOf("TCP_MD5SIG");
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */