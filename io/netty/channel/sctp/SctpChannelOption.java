/*    */ package io.netty.channel.sctp;
/*    */ 
/*    */ import com.sun.nio.sctp.SctpStandardSocketOptions;
/*    */ import io.netty.channel.ChannelOption;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class SctpChannelOption<T>
/*    */   extends ChannelOption<T>
/*    */ {
/* 29 */   public static final ChannelOption<Boolean> SCTP_DISABLE_FRAGMENTS = valueOf(SctpChannelOption.class, "SCTP_DISABLE_FRAGMENTS");
/*    */   
/* 31 */   public static final ChannelOption<Boolean> SCTP_EXPLICIT_COMPLETE = valueOf(SctpChannelOption.class, "SCTP_EXPLICIT_COMPLETE");
/*    */   
/* 33 */   public static final ChannelOption<Integer> SCTP_FRAGMENT_INTERLEAVE = valueOf(SctpChannelOption.class, "SCTP_FRAGMENT_INTERLEAVE");
/*    */   
/* 35 */   public static final ChannelOption<SctpStandardSocketOptions.InitMaxStreams> SCTP_INIT_MAXSTREAMS = valueOf(SctpChannelOption.class, "SCTP_INIT_MAXSTREAMS");
/*    */ 
/*    */   
/* 38 */   public static final ChannelOption<Boolean> SCTP_NODELAY = valueOf(SctpChannelOption.class, "SCTP_NODELAY");
/*    */   
/* 40 */   public static final ChannelOption<SocketAddress> SCTP_PRIMARY_ADDR = valueOf(SctpChannelOption.class, "SCTP_PRIMARY_ADDR");
/*    */   
/* 42 */   public static final ChannelOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR = valueOf(SctpChannelOption.class, "SCTP_SET_PEER_PRIMARY_ADDR");
/*    */ 
/*    */   
/*    */   private SctpChannelOption() {
/* 46 */     super(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\SctpChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */