/*    */ package io.netty.channel.udt;
/*    */ 
/*    */ import io.netty.channel.ChannelOption;
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
/*    */ @Deprecated
/*    */ public final class UdtChannelOption<T>
/*    */   extends ChannelOption<T>
/*    */ {
/* 33 */   public static final ChannelOption<Integer> PROTOCOL_RECEIVE_BUFFER_SIZE = valueOf(UdtChannelOption.class, "PROTOCOL_RECEIVE_BUFFER_SIZE");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static final ChannelOption<Integer> PROTOCOL_SEND_BUFFER_SIZE = valueOf(UdtChannelOption.class, "PROTOCOL_SEND_BUFFER_SIZE");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final ChannelOption<Integer> SYSTEM_RECEIVE_BUFFER_SIZE = valueOf(UdtChannelOption.class, "SYSTEM_RECEIVE_BUFFER_SIZE");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static final ChannelOption<Integer> SYSTEM_SEND_BUFFER_SIZE = valueOf(UdtChannelOption.class, "SYSTEM_SEND_BUFFER_SIZE");
/*    */ 
/*    */   
/*    */   private UdtChannelOption() {
/* 55 */     super(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\UdtChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */