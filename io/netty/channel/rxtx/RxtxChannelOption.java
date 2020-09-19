/*    */ package io.netty.channel.rxtx;
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
/*    */ @Deprecated
/*    */ public final class RxtxChannelOption<T>
/*    */   extends ChannelOption<T>
/*    */ {
/* 31 */   public static final ChannelOption<Integer> BAUD_RATE = valueOf(RxtxChannelOption.class, "BAUD_RATE");
/* 32 */   public static final ChannelOption<Boolean> DTR = valueOf(RxtxChannelOption.class, "DTR");
/* 33 */   public static final ChannelOption<Boolean> RTS = valueOf(RxtxChannelOption.class, "RTS");
/* 34 */   public static final ChannelOption<RxtxChannelConfig.Stopbits> STOP_BITS = valueOf(RxtxChannelOption.class, "STOP_BITS");
/* 35 */   public static final ChannelOption<RxtxChannelConfig.Databits> DATA_BITS = valueOf(RxtxChannelOption.class, "DATA_BITS");
/* 36 */   public static final ChannelOption<RxtxChannelConfig.Paritybit> PARITY_BIT = valueOf(RxtxChannelOption.class, "PARITY_BIT");
/* 37 */   public static final ChannelOption<Integer> WAIT_TIME = valueOf(RxtxChannelOption.class, "WAIT_TIME");
/* 38 */   public static final ChannelOption<Integer> READ_TIMEOUT = valueOf(RxtxChannelOption.class, "READ_TIMEOUT");
/*    */ 
/*    */   
/*    */   private RxtxChannelOption() {
/* 42 */     super(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\rxtx\RxtxChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */