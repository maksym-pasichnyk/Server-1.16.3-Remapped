/*     */ package io.netty.channel.unix;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.PortUnreachableException;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Socket
/*     */   extends FileDescriptor
/*     */ {
/*  49 */   private static final ClosedChannelException SHUTDOWN_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "shutdown(..)");
/*     */   
/*  51 */   private static final ClosedChannelException SEND_TO_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "sendTo(..)");
/*     */ 
/*     */   
/*  54 */   private static final ClosedChannelException SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "sendToAddress(..)");
/*     */   
/*  56 */   private static final ClosedChannelException SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "sendToAddresses(..)");
/*  57 */   private static final Errors.NativeIoException SEND_TO_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(
/*  58 */       Errors.newConnectionResetException("syscall:sendto", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendTo(..)");
/*     */ 
/*     */   
/*  61 */   private static final Errors.NativeIoException SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:sendto", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendToAddress");
/*     */   
/*  63 */   private static final Errors.NativeIoException CONNECTION_RESET_EXCEPTION_SENDMSG = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(
/*  64 */       Errors.newConnectionResetException("syscall:sendmsg", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendToAddresses(..)");
/*     */ 
/*     */   
/*  67 */   private static final Errors.NativeIoException CONNECTION_RESET_SHUTDOWN_EXCEPTION = (Errors.NativeIoException)ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:shutdown", Errors.ERRNO_ECONNRESET_NEGATIVE), Socket.class, "shutdown");
/*     */ 
/*     */   
/*  70 */   private static final Errors.NativeConnectException FINISH_CONNECT_REFUSED_EXCEPTION = (Errors.NativeConnectException)ThrowableUtil.unknownStackTrace(new Errors.NativeConnectException("syscall:getsockopt", Errors.ERROR_ECONNREFUSED_NEGATIVE), Socket.class, "finishConnect(..)");
/*     */ 
/*     */   
/*  73 */   private static final Errors.NativeConnectException CONNECT_REFUSED_EXCEPTION = (Errors.NativeConnectException)ThrowableUtil.unknownStackTrace(new Errors.NativeConnectException("syscall:connect", Errors.ERROR_ECONNREFUSED_NEGATIVE), Socket.class, "connect(..)");
/*     */ 
/*     */   
/*  76 */   public static final int UDS_SUN_PATH_SIZE = LimitsStaticallyReferencedJniMethods.udsSunPathSize();
/*     */   
/*     */   public Socket(int fd) {
/*  79 */     super(fd);
/*     */   }
/*     */   
/*     */   public final void shutdown() throws IOException {
/*  83 */     shutdown(true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void shutdown(boolean read, boolean write) throws IOException {
/*     */     int oldState, newState;
/*     */     do {
/*  92 */       oldState = this.state;
/*  93 */       if (isClosed(oldState)) {
/*  94 */         throw new ClosedChannelException();
/*     */       }
/*  96 */       newState = oldState;
/*  97 */       if (read && !isInputShutdown(newState)) {
/*  98 */         newState = inputShutdown(newState);
/*     */       }
/* 100 */       if (write && !isOutputShutdown(newState)) {
/* 101 */         newState = outputShutdown(newState);
/*     */       }
/*     */ 
/*     */       
/* 105 */       if (newState == oldState) {
/*     */         return;
/*     */       }
/* 108 */     } while (!casState(oldState, newState));
/*     */ 
/*     */ 
/*     */     
/* 112 */     int res = shutdown(this.fd, read, write);
/* 113 */     if (res < 0) {
/* 114 */       Errors.ioResult("shutdown", res, CONNECTION_RESET_SHUTDOWN_EXCEPTION, SHUTDOWN_CLOSED_CHANNEL_EXCEPTION);
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isShutdown() {
/* 119 */     int state = this.state;
/* 120 */     return (isInputShutdown(state) && isOutputShutdown(state));
/*     */   }
/*     */   
/*     */   public final boolean isInputShutdown() {
/* 124 */     return isInputShutdown(this.state);
/*     */   }
/*     */   
/*     */   public final boolean isOutputShutdown() {
/* 128 */     return isOutputShutdown(this.state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int sendTo(ByteBuffer buf, int pos, int limit, InetAddress addr, int port) throws IOException {
/*     */     byte[] address;
/*     */     int scopeId;
/* 136 */     if (addr instanceof Inet6Address) {
/* 137 */       address = addr.getAddress();
/* 138 */       scopeId = ((Inet6Address)addr).getScopeId();
/*     */     } else {
/*     */       
/* 141 */       scopeId = 0;
/* 142 */       address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
/*     */     } 
/* 144 */     int res = sendTo(this.fd, buf, pos, limit, address, scopeId, port);
/* 145 */     if (res >= 0) {
/* 146 */       return res;
/*     */     }
/* 148 */     if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
/* 149 */       throw new PortUnreachableException("sendTo failed");
/*     */     }
/* 151 */     return Errors.ioResult("sendTo", res, SEND_TO_CONNECTION_RESET_EXCEPTION, SEND_TO_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int sendToAddress(long memoryAddress, int pos, int limit, InetAddress addr, int port) throws IOException {
/*     */     byte[] address;
/*     */     int scopeId;
/* 160 */     if (addr instanceof Inet6Address) {
/* 161 */       address = addr.getAddress();
/* 162 */       scopeId = ((Inet6Address)addr).getScopeId();
/*     */     } else {
/*     */       
/* 165 */       scopeId = 0;
/* 166 */       address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
/*     */     } 
/* 168 */     int res = sendToAddress(this.fd, memoryAddress, pos, limit, address, scopeId, port);
/* 169 */     if (res >= 0) {
/* 170 */       return res;
/*     */     }
/* 172 */     if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
/* 173 */       throw new PortUnreachableException("sendToAddress failed");
/*     */     }
/* 175 */     return Errors.ioResult("sendToAddress", res, SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION, SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int sendToAddresses(long memoryAddress, int length, InetAddress addr, int port) throws IOException {
/*     */     byte[] address;
/*     */     int scopeId;
/* 184 */     if (addr instanceof Inet6Address) {
/* 185 */       address = addr.getAddress();
/* 186 */       scopeId = ((Inet6Address)addr).getScopeId();
/*     */     } else {
/*     */       
/* 189 */       scopeId = 0;
/* 190 */       address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
/*     */     } 
/* 192 */     int res = sendToAddresses(this.fd, memoryAddress, length, address, scopeId, port);
/* 193 */     if (res >= 0) {
/* 194 */       return res;
/*     */     }
/*     */     
/* 197 */     if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
/* 198 */       throw new PortUnreachableException("sendToAddresses failed");
/*     */     }
/* 200 */     return Errors.ioResult("sendToAddresses", res, CONNECTION_RESET_EXCEPTION_SENDMSG, SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */ 
/*     */   
/*     */   public final DatagramSocketAddress recvFrom(ByteBuffer buf, int pos, int limit) throws IOException {
/* 205 */     return recvFrom(this.fd, buf, pos, limit);
/*     */   }
/*     */   
/*     */   public final DatagramSocketAddress recvFromAddress(long memoryAddress, int pos, int limit) throws IOException {
/* 209 */     return recvFromAddress(this.fd, memoryAddress, pos, limit);
/*     */   }
/*     */   
/*     */   public final int recvFd() throws IOException {
/* 213 */     int res = recvFd(this.fd);
/* 214 */     if (res > 0) {
/* 215 */       return res;
/*     */     }
/* 217 */     if (res == 0) {
/* 218 */       return -1;
/*     */     }
/*     */     
/* 221 */     if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE)
/*     */     {
/* 223 */       return 0;
/*     */     }
/* 225 */     throw Errors.newIOException("recvFd", res);
/*     */   }
/*     */   
/*     */   public final int sendFd(int fdToSend) throws IOException {
/* 229 */     int res = sendFd(this.fd, fdToSend);
/* 230 */     if (res >= 0) {
/* 231 */       return res;
/*     */     }
/* 233 */     if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE)
/*     */     {
/* 235 */       return -1;
/*     */     }
/* 237 */     throw Errors.newIOException("sendFd", res);
/*     */   }
/*     */   
/*     */   public final boolean connect(SocketAddress socketAddress) throws IOException {
/*     */     int res;
/* 242 */     if (socketAddress instanceof InetSocketAddress) {
/* 243 */       InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
/* 244 */       NativeInetAddress address = NativeInetAddress.newInstance(inetSocketAddress.getAddress());
/* 245 */       res = connect(this.fd, address.address, address.scopeId, inetSocketAddress.getPort());
/* 246 */     } else if (socketAddress instanceof DomainSocketAddress) {
/* 247 */       DomainSocketAddress unixDomainSocketAddress = (DomainSocketAddress)socketAddress;
/* 248 */       res = connectDomainSocket(this.fd, unixDomainSocketAddress.path().getBytes(CharsetUtil.UTF_8));
/*     */     } else {
/* 250 */       throw new Error("Unexpected SocketAddress implementation " + socketAddress);
/*     */     } 
/* 252 */     if (res < 0) {
/* 253 */       if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE)
/*     */       {
/* 255 */         return false;
/*     */       }
/* 257 */       Errors.throwConnectException("connect", CONNECT_REFUSED_EXCEPTION, res);
/*     */     } 
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean finishConnect() throws IOException {
/* 263 */     int res = finishConnect(this.fd);
/* 264 */     if (res < 0) {
/* 265 */       if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE)
/*     */       {
/* 267 */         return false;
/*     */       }
/* 269 */       Errors.throwConnectException("finishConnect", FINISH_CONNECT_REFUSED_EXCEPTION, res);
/*     */     } 
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   public final void disconnect() throws IOException {
/* 275 */     int res = disconnect(this.fd);
/* 276 */     if (res < 0) {
/* 277 */       Errors.throwConnectException("disconnect", FINISH_CONNECT_REFUSED_EXCEPTION, res);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void bind(SocketAddress socketAddress) throws IOException {
/* 282 */     if (socketAddress instanceof InetSocketAddress) {
/* 283 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 284 */       NativeInetAddress address = NativeInetAddress.newInstance(addr.getAddress());
/* 285 */       int res = bind(this.fd, address.address, address.scopeId, addr.getPort());
/* 286 */       if (res < 0) {
/* 287 */         throw Errors.newIOException("bind", res);
/*     */       }
/* 289 */     } else if (socketAddress instanceof DomainSocketAddress) {
/* 290 */       DomainSocketAddress addr = (DomainSocketAddress)socketAddress;
/* 291 */       int res = bindDomainSocket(this.fd, addr.path().getBytes(CharsetUtil.UTF_8));
/* 292 */       if (res < 0) {
/* 293 */         throw Errors.newIOException("bind", res);
/*     */       }
/*     */     } else {
/* 296 */       throw new Error("Unexpected SocketAddress implementation " + socketAddress);
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void listen(int backlog) throws IOException {
/* 301 */     int res = listen(this.fd, backlog);
/* 302 */     if (res < 0) {
/* 303 */       throw Errors.newIOException("listen", res);
/*     */     }
/*     */   }
/*     */   
/*     */   public final int accept(byte[] addr) throws IOException {
/* 308 */     int res = accept(this.fd, addr);
/* 309 */     if (res >= 0) {
/* 310 */       return res;
/*     */     }
/* 312 */     if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE)
/*     */     {
/* 314 */       return -1;
/*     */     }
/* 316 */     throw Errors.newIOException("accept", res);
/*     */   }
/*     */   
/*     */   public final InetSocketAddress remoteAddress() {
/* 320 */     byte[] addr = remoteAddress(this.fd);
/*     */ 
/*     */     
/* 323 */     return (addr == null) ? null : NativeInetAddress.address(addr, 0, addr.length);
/*     */   }
/*     */   
/*     */   public final InetSocketAddress localAddress() {
/* 327 */     byte[] addr = localAddress(this.fd);
/*     */ 
/*     */     
/* 330 */     return (addr == null) ? null : NativeInetAddress.address(addr, 0, addr.length);
/*     */   }
/*     */   
/*     */   public final int getReceiveBufferSize() throws IOException {
/* 334 */     return getReceiveBufferSize(this.fd);
/*     */   }
/*     */   
/*     */   public final int getSendBufferSize() throws IOException {
/* 338 */     return getSendBufferSize(this.fd);
/*     */   }
/*     */   
/*     */   public final boolean isKeepAlive() throws IOException {
/* 342 */     return (isKeepAlive(this.fd) != 0);
/*     */   }
/*     */   
/*     */   public final boolean isTcpNoDelay() throws IOException {
/* 346 */     return (isTcpNoDelay(this.fd) != 0);
/*     */   }
/*     */   
/*     */   public final boolean isReuseAddress() throws IOException {
/* 350 */     return (isReuseAddress(this.fd) != 0);
/*     */   }
/*     */   
/*     */   public final boolean isReusePort() throws IOException {
/* 354 */     return (isReusePort(this.fd) != 0);
/*     */   }
/*     */   
/*     */   public final boolean isBroadcast() throws IOException {
/* 358 */     return (isBroadcast(this.fd) != 0);
/*     */   }
/*     */   
/*     */   public final int getSoLinger() throws IOException {
/* 362 */     return getSoLinger(this.fd);
/*     */   }
/*     */   
/*     */   public final int getSoError() throws IOException {
/* 366 */     return getSoError(this.fd);
/*     */   }
/*     */   
/*     */   public final int getTrafficClass() throws IOException {
/* 370 */     return getTrafficClass(this.fd);
/*     */   }
/*     */   
/*     */   public final void setKeepAlive(boolean keepAlive) throws IOException {
/* 374 */     setKeepAlive(this.fd, keepAlive ? 1 : 0);
/*     */   }
/*     */   
/*     */   public final void setReceiveBufferSize(int receiveBufferSize) throws IOException {
/* 378 */     setReceiveBufferSize(this.fd, receiveBufferSize);
/*     */   }
/*     */   
/*     */   public final void setSendBufferSize(int sendBufferSize) throws IOException {
/* 382 */     setSendBufferSize(this.fd, sendBufferSize);
/*     */   }
/*     */   
/*     */   public final void setTcpNoDelay(boolean tcpNoDelay) throws IOException {
/* 386 */     setTcpNoDelay(this.fd, tcpNoDelay ? 1 : 0);
/*     */   }
/*     */   
/*     */   public final void setSoLinger(int soLinger) throws IOException {
/* 390 */     setSoLinger(this.fd, soLinger);
/*     */   }
/*     */   
/*     */   public final void setReuseAddress(boolean reuseAddress) throws IOException {
/* 394 */     setReuseAddress(this.fd, reuseAddress ? 1 : 0);
/*     */   }
/*     */   
/*     */   public final void setReusePort(boolean reusePort) throws IOException {
/* 398 */     setReusePort(this.fd, reusePort ? 1 : 0);
/*     */   }
/*     */   
/*     */   public final void setBroadcast(boolean broadcast) throws IOException {
/* 402 */     setBroadcast(this.fd, broadcast ? 1 : 0);
/*     */   }
/*     */   
/*     */   public final void setTrafficClass(int trafficClass) throws IOException {
/* 406 */     setTrafficClass(this.fd, trafficClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 411 */     return "Socket{fd=" + this.fd + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 416 */   private static final AtomicBoolean INITIALIZED = new AtomicBoolean();
/*     */   
/*     */   public static Socket newSocketStream() {
/* 419 */     return new Socket(newSocketStream0());
/*     */   }
/*     */   
/*     */   public static Socket newSocketDgram() {
/* 423 */     return new Socket(newSocketDgram0());
/*     */   }
/*     */   
/*     */   public static Socket newSocketDomain() {
/* 427 */     return new Socket(newSocketDomain0());
/*     */   }
/*     */   
/*     */   public static void initialize() {
/* 431 */     if (INITIALIZED.compareAndSet(false, true)) {
/* 432 */       initialize(NetUtil.isIpV4StackPreferred());
/*     */     }
/*     */   }
/*     */   
/*     */   protected static int newSocketStream0() {
/* 437 */     int res = newSocketStreamFd();
/* 438 */     if (res < 0) {
/* 439 */       throw new ChannelException(Errors.newIOException("newSocketStream", res));
/*     */     }
/* 441 */     return res;
/*     */   }
/*     */   
/*     */   protected static int newSocketDgram0() {
/* 445 */     int res = newSocketDgramFd();
/* 446 */     if (res < 0) {
/* 447 */       throw new ChannelException(Errors.newIOException("newSocketDgram", res));
/*     */     }
/* 449 */     return res;
/*     */   }
/*     */   
/*     */   protected static int newSocketDomain0() {
/* 453 */     int res = newSocketDomainFd();
/* 454 */     if (res < 0) {
/* 455 */       throw new ChannelException(Errors.newIOException("newSocketDomain", res));
/*     */     }
/* 457 */     return res;
/*     */   }
/*     */   
/*     */   private static native int shutdown(int paramInt, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   private static native int connect(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int connectDomainSocket(int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   private static native int finishConnect(int paramInt);
/*     */   
/*     */   private static native int disconnect(int paramInt);
/*     */   
/*     */   private static native int bind(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int bindDomainSocket(int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   private static native int listen(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int accept(int paramInt, byte[] paramArrayOfbyte);
/*     */   
/*     */   private static native byte[] remoteAddress(int paramInt);
/*     */   
/*     */   private static native byte[] localAddress(int paramInt);
/*     */   
/*     */   private static native int sendTo(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3, byte[] paramArrayOfbyte, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native int sendToAddress(int paramInt1, long paramLong, int paramInt2, int paramInt3, byte[] paramArrayOfbyte, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native int sendToAddresses(int paramInt1, long paramLong, int paramInt2, byte[] paramArrayOfbyte, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native DatagramSocketAddress recvFrom(int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3) throws IOException;
/*     */   
/*     */   private static native DatagramSocketAddress recvFromAddress(int paramInt1, long paramLong, int paramInt2, int paramInt3) throws IOException;
/*     */   
/*     */   private static native int recvFd(int paramInt);
/*     */   
/*     */   private static native int sendFd(int paramInt1, int paramInt2);
/*     */   
/*     */   private static native int newSocketStreamFd();
/*     */   
/*     */   private static native int newSocketDgramFd();
/*     */   
/*     */   private static native int newSocketDomainFd();
/*     */   
/*     */   private static native int isReuseAddress(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isReusePort(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getReceiveBufferSize(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getSendBufferSize(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isKeepAlive(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isTcpNoDelay(int paramInt) throws IOException;
/*     */   
/*     */   private static native int isBroadcast(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getSoLinger(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getSoError(int paramInt) throws IOException;
/*     */   
/*     */   private static native int getTrafficClass(int paramInt) throws IOException;
/*     */   
/*     */   private static native void setReuseAddress(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setReusePort(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setKeepAlive(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setReceiveBufferSize(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setSendBufferSize(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTcpNoDelay(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setSoLinger(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setBroadcast(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void setTrafficClass(int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   private static native void initialize(boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\Socket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */