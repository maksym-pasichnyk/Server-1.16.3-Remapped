/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.AbstractConstant;
/*     */ import io.netty.util.Constant;
/*     */ import io.netty.util.ConstantPool;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
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
/*     */ public class ChannelOption<T>
/*     */   extends AbstractConstant<ChannelOption<T>>
/*     */ {
/*  35 */   private static final ConstantPool<ChannelOption<Object>> pool = new ConstantPool<ChannelOption<Object>>()
/*     */     {
/*     */       protected ChannelOption<Object> newConstant(int id, String name) {
/*  38 */         return new ChannelOption(id, name);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> ChannelOption<T> valueOf(String name) {
/*  47 */     return (ChannelOption<T>)pool.valueOf(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> ChannelOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
/*  55 */     return (ChannelOption<T>)pool.valueOf(firstNameComponent, secondNameComponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean exists(String name) {
/*  62 */     return pool.exists(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> ChannelOption<T> newInstance(String name) {
/*  71 */     return (ChannelOption<T>)pool.newInstance(name);
/*     */   }
/*     */   
/*  74 */   public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");
/*  75 */   public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
/*  76 */   public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");
/*     */   
/*  78 */   public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  83 */   public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
/*  84 */   public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  89 */   public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  94 */   public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
/*     */   
/*  96 */   public static final ChannelOption<WriteBufferWaterMark> WRITE_BUFFER_WATER_MARK = valueOf("WRITE_BUFFER_WATER_MARK");
/*     */   
/*  98 */   public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
/*  99 */   public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 108 */   public static final ChannelOption<Boolean> AUTO_CLOSE = valueOf("AUTO_CLOSE");
/*     */   
/* 110 */   public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
/* 111 */   public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
/* 112 */   public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
/* 113 */   public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
/* 114 */   public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
/* 115 */   public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
/* 116 */   public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
/* 117 */   public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");
/*     */   
/* 119 */   public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");
/* 120 */   public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
/* 121 */   public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
/* 122 */   public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
/* 123 */   public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");
/*     */   
/* 125 */   public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");
/*     */ 
/*     */   
/*     */   @Deprecated
/* 129 */   public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
/*     */ 
/*     */   
/* 132 */   public static final ChannelOption<Boolean> SINGLE_EVENTEXECUTOR_PER_GROUP = valueOf("SINGLE_EVENTEXECUTOR_PER_GROUP");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelOption(int id, String name) {
/* 138 */     super(id, name);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected ChannelOption(String name) {
/* 143 */     this(pool.nextId(), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(T value) {
/* 151 */     if (value == null)
/* 152 */       throw new NullPointerException("value"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */