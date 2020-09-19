/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DefaultDatagramChannelConfig;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.util.Enumeration;
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
/*     */ class NioDatagramChannelConfig
/*     */   extends DefaultDatagramChannelConfig
/*     */ {
/*     */   private static final Object IP_MULTICAST_TTL;
/*     */   private static final Object IP_MULTICAST_IF;
/*     */   private static final Object IP_MULTICAST_LOOP;
/*     */   private static final Method GET_OPTION;
/*     */   private static final Method SET_OPTION;
/*     */   private final DatagramChannel javaChannel;
/*     */   
/*     */   static {
/*  43 */     ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
/*  44 */     Class<?> socketOptionType = null;
/*     */     try {
/*  46 */       socketOptionType = Class.forName("java.net.SocketOption", true, classLoader);
/*  47 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  50 */     Class<?> stdSocketOptionType = null;
/*     */     try {
/*  52 */       stdSocketOptionType = Class.forName("java.net.StandardSocketOptions", true, classLoader);
/*  53 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/*  57 */     Object ipMulticastTtl = null;
/*  58 */     Object ipMulticastIf = null;
/*  59 */     Object ipMulticastLoop = null;
/*  60 */     Method getOption = null;
/*  61 */     Method setOption = null;
/*  62 */     if (socketOptionType != null) {
/*     */       try {
/*  64 */         ipMulticastTtl = stdSocketOptionType.getDeclaredField("IP_MULTICAST_TTL").get(null);
/*  65 */       } catch (Exception e) {
/*  66 */         throw new Error("cannot locate the IP_MULTICAST_TTL field", e);
/*     */       } 
/*     */       
/*     */       try {
/*  70 */         ipMulticastIf = stdSocketOptionType.getDeclaredField("IP_MULTICAST_IF").get(null);
/*  71 */       } catch (Exception e) {
/*  72 */         throw new Error("cannot locate the IP_MULTICAST_IF field", e);
/*     */       } 
/*     */       
/*     */       try {
/*  76 */         ipMulticastLoop = stdSocketOptionType.getDeclaredField("IP_MULTICAST_LOOP").get(null);
/*  77 */       } catch (Exception e) {
/*  78 */         throw new Error("cannot locate the IP_MULTICAST_LOOP field", e);
/*     */       } 
/*     */       
/*  81 */       Class<?> networkChannelClass = null;
/*     */       try {
/*  83 */         networkChannelClass = Class.forName("java.nio.channels.NetworkChannel", true, classLoader);
/*  84 */       } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */       
/*  88 */       if (networkChannelClass == null) {
/*  89 */         getOption = null;
/*  90 */         setOption = null;
/*     */       } else {
/*     */         try {
/*  93 */           getOption = networkChannelClass.getDeclaredMethod("getOption", new Class[] { socketOptionType });
/*  94 */         } catch (Exception e) {
/*  95 */           throw new Error("cannot locate the getOption() method", e);
/*     */         } 
/*     */         
/*     */         try {
/*  99 */           setOption = networkChannelClass.getDeclaredMethod("setOption", new Class[] { socketOptionType, Object.class });
/* 100 */         } catch (Exception e) {
/* 101 */           throw new Error("cannot locate the setOption() method", e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     IP_MULTICAST_TTL = ipMulticastTtl;
/* 106 */     IP_MULTICAST_IF = ipMulticastIf;
/* 107 */     IP_MULTICAST_LOOP = ipMulticastLoop;
/* 108 */     GET_OPTION = getOption;
/* 109 */     SET_OPTION = setOption;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   NioDatagramChannelConfig(NioDatagramChannel channel, DatagramChannel javaChannel) {
/* 115 */     super(channel, javaChannel.socket());
/* 116 */     this.javaChannel = javaChannel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTimeToLive() {
/* 121 */     return ((Integer)getOption0(IP_MULTICAST_TTL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setTimeToLive(int ttl) {
/* 126 */     setOption0(IP_MULTICAST_TTL, Integer.valueOf(ttl));
/* 127 */     return (DatagramChannelConfig)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getInterface() {
/* 132 */     NetworkInterface inf = getNetworkInterface();
/* 133 */     if (inf != null) {
/* 134 */       Enumeration<InetAddress> addresses = SocketUtils.addressesFromNetworkInterface(inf);
/* 135 */       if (addresses.hasMoreElements()) {
/* 136 */         return addresses.nextElement();
/*     */       }
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setInterface(InetAddress interfaceAddress) {
/*     */     try {
/* 145 */       setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
/* 146 */     } catch (SocketException e) {
/* 147 */       throw new ChannelException(e);
/*     */     } 
/* 149 */     return (DatagramChannelConfig)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NetworkInterface getNetworkInterface() {
/* 154 */     return (NetworkInterface)getOption0(IP_MULTICAST_IF);
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
/* 159 */     setOption0(IP_MULTICAST_IF, networkInterface);
/* 160 */     return (DatagramChannelConfig)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLoopbackModeDisabled() {
/* 165 */     return ((Boolean)getOption0(IP_MULTICAST_LOOP)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
/* 170 */     setOption0(IP_MULTICAST_LOOP, Boolean.valueOf(loopbackModeDisabled));
/* 171 */     return (DatagramChannelConfig)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramChannelConfig setAutoRead(boolean autoRead) {
/* 176 */     super.setAutoRead(autoRead);
/* 177 */     return (DatagramChannelConfig)this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void autoReadCleared() {
/* 182 */     ((NioDatagramChannel)this.channel).clearReadPending0();
/*     */   }
/*     */   
/*     */   private Object getOption0(Object option) {
/* 186 */     if (GET_OPTION == null) {
/* 187 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     try {
/* 190 */       return GET_OPTION.invoke(this.javaChannel, new Object[] { option });
/* 191 */     } catch (Exception e) {
/* 192 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setOption0(Object option, Object value) {
/* 198 */     if (SET_OPTION == null) {
/* 199 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     try {
/* 202 */       SET_OPTION.invoke(this.javaChannel, new Object[] { option, value });
/* 203 */     } catch (Exception e) {
/* 204 */       throw new ChannelException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\nio\NioDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */