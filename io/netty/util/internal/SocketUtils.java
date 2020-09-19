/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
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
/*     */ public final class SocketUtils
/*     */ {
/*     */   public static void connect(final Socket socket, final SocketAddress remoteAddress, final int timeout) throws IOException {
/*     */     try {
/*  51 */       AccessController.doPrivileged(new PrivilegedExceptionAction<Void>()
/*     */           {
/*     */             public Void run() throws IOException {
/*  54 */               socket.connect(remoteAddress, timeout);
/*  55 */               return null;
/*     */             }
/*     */           });
/*  58 */     } catch (PrivilegedActionException e) {
/*  59 */       throw (IOException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void bind(final Socket socket, final SocketAddress bindpoint) throws IOException {
/*     */     try {
/*  65 */       AccessController.doPrivileged(new PrivilegedExceptionAction<Void>()
/*     */           {
/*     */             public Void run() throws IOException {
/*  68 */               socket.bind(bindpoint);
/*  69 */               return null;
/*     */             }
/*     */           });
/*  72 */     } catch (PrivilegedActionException e) {
/*  73 */       throw (IOException)e.getCause();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean connect(final SocketChannel socketChannel, final SocketAddress remoteAddress) throws IOException {
/*     */     try {
/*  80 */       return ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedExceptionAction<Boolean>()
/*     */           {
/*     */             public Boolean run() throws IOException {
/*  83 */               return Boolean.valueOf(socketChannel.connect(remoteAddress));
/*     */             }
/*     */           })).booleanValue();
/*  86 */     } catch (PrivilegedActionException e) {
/*  87 */       throw (IOException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void bind(final SocketChannel socketChannel, final SocketAddress address) throws IOException {
/*     */     try {
/*  93 */       AccessController.doPrivileged(new PrivilegedExceptionAction<Void>()
/*     */           {
/*     */             public Void run() throws IOException {
/*  96 */               socketChannel.bind(address);
/*  97 */               return null;
/*     */             }
/*     */           });
/* 100 */     } catch (PrivilegedActionException e) {
/* 101 */       throw (IOException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static SocketChannel accept(final ServerSocketChannel serverSocketChannel) throws IOException {
/*     */     try {
/* 107 */       return AccessController.<SocketChannel>doPrivileged(new PrivilegedExceptionAction<SocketChannel>()
/*     */           {
/*     */             public SocketChannel run() throws IOException {
/* 110 */               return serverSocketChannel.accept();
/*     */             }
/*     */           });
/* 113 */     } catch (PrivilegedActionException e) {
/* 114 */       throw (IOException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void bind(final DatagramChannel networkChannel, final SocketAddress address) throws IOException {
/*     */     try {
/* 120 */       AccessController.doPrivileged(new PrivilegedExceptionAction<Void>()
/*     */           {
/*     */             public Void run() throws IOException {
/* 123 */               networkChannel.bind(address);
/* 124 */               return null;
/*     */             }
/*     */           });
/* 127 */     } catch (PrivilegedActionException e) {
/* 128 */       throw (IOException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static SocketAddress localSocketAddress(final ServerSocket socket) {
/* 133 */     return AccessController.<SocketAddress>doPrivileged(new PrivilegedAction<SocketAddress>()
/*     */         {
/*     */           public SocketAddress run() {
/* 136 */             return socket.getLocalSocketAddress();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static InetAddress addressByName(final String hostname) throws UnknownHostException {
/*     */     try {
/* 143 */       return AccessController.<InetAddress>doPrivileged(new PrivilegedExceptionAction<InetAddress>()
/*     */           {
/*     */             public InetAddress run() throws UnknownHostException {
/* 146 */               return InetAddress.getByName(hostname);
/*     */             }
/*     */           });
/* 149 */     } catch (PrivilegedActionException e) {
/* 150 */       throw (UnknownHostException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static InetAddress[] allAddressesByName(final String hostname) throws UnknownHostException {
/*     */     try {
/* 156 */       return AccessController.<InetAddress[]>doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<InetAddress[]>()
/*     */           {
/*     */             public InetAddress[] run() throws UnknownHostException {
/* 159 */               return InetAddress.getAllByName(hostname);
/*     */             }
/*     */           });
/* 162 */     } catch (PrivilegedActionException e) {
/* 163 */       throw (UnknownHostException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static InetSocketAddress socketAddress(final String hostname, final int port) {
/* 168 */     return AccessController.<InetSocketAddress>doPrivileged(new PrivilegedAction<InetSocketAddress>()
/*     */         {
/*     */           public InetSocketAddress run() {
/* 171 */             return new InetSocketAddress(hostname, port);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Enumeration<InetAddress> addressesFromNetworkInterface(final NetworkInterface intf) {
/* 177 */     return AccessController.<Enumeration<InetAddress>>doPrivileged(new PrivilegedAction<Enumeration<InetAddress>>()
/*     */         {
/*     */           public Enumeration<InetAddress> run() {
/* 180 */             return intf.getInetAddresses();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static InetAddress loopbackAddress() {
/* 186 */     return AccessController.<InetAddress>doPrivileged(new PrivilegedAction<InetAddress>()
/*     */         {
/*     */           public InetAddress run() {
/* 189 */             if (PlatformDependent.javaVersion() >= 7) {
/* 190 */               return InetAddress.getLoopbackAddress();
/*     */             }
/*     */             try {
/* 193 */               return InetAddress.getByName(null);
/* 194 */             } catch (UnknownHostException e) {
/* 195 */               throw new IllegalStateException(e);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static byte[] hardwareAddressFromNetworkInterface(final NetworkInterface intf) throws SocketException {
/*     */     try {
/* 203 */       return AccessController.<byte[]>doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<byte[]>()
/*     */           {
/*     */             public byte[] run() throws SocketException {
/* 206 */               return intf.getHardwareAddress();
/*     */             }
/*     */           });
/* 209 */     } catch (PrivilegedActionException e) {
/* 210 */       throw (SocketException)e.getCause();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\SocketUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */