/*     */ package io.netty.channel.rxtx;
/*     */ 
/*     */ import gnu.io.CommPort;
/*     */ import gnu.io.CommPortIdentifier;
/*     */ import gnu.io.SerialPort;
/*     */ import io.netty.channel.AbstractChannel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.oio.OioByteStreamChannel;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @Deprecated
/*     */ public class RxtxChannel
/*     */   extends OioByteStreamChannel
/*     */ {
/*  45 */   private static final RxtxDeviceAddress LOCAL_ADDRESS = new RxtxDeviceAddress("localhost");
/*     */   
/*     */   private final RxtxChannelConfig config;
/*     */   
/*     */   private boolean open = true;
/*     */   private RxtxDeviceAddress deviceAddress;
/*     */   private SerialPort serialPort;
/*     */   
/*     */   public RxtxChannel() {
/*  54 */     super(null);
/*     */     
/*  56 */     this.config = new DefaultRxtxChannelConfig(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxChannelConfig config() {
/*  61 */     return this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  66 */     return this.open;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe() {
/*  71 */     return new RxtxUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
/*  76 */     RxtxDeviceAddress remote = (RxtxDeviceAddress)remoteAddress;
/*  77 */     CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(remote.value());
/*  78 */     CommPort commPort = cpi.open(getClass().getName(), 1000);
/*  79 */     commPort.enableReceiveTimeout(((Integer)config().getOption(RxtxChannelOption.READ_TIMEOUT)).intValue());
/*  80 */     this.deviceAddress = remote;
/*     */     
/*  82 */     this.serialPort = (SerialPort)commPort;
/*     */   }
/*     */   
/*     */   protected void doInit() throws Exception {
/*  86 */     this.serialPort.setSerialPortParams(((Integer)
/*  87 */         config().getOption(RxtxChannelOption.BAUD_RATE)).intValue(), ((RxtxChannelConfig.Databits)
/*  88 */         config().getOption(RxtxChannelOption.DATA_BITS)).value(), ((RxtxChannelConfig.Stopbits)
/*  89 */         config().getOption(RxtxChannelOption.STOP_BITS)).value(), ((RxtxChannelConfig.Paritybit)
/*  90 */         config().getOption(RxtxChannelOption.PARITY_BIT)).value());
/*     */     
/*  92 */     this.serialPort.setDTR(((Boolean)config().getOption(RxtxChannelOption.DTR)).booleanValue());
/*  93 */     this.serialPort.setRTS(((Boolean)config().getOption(RxtxChannelOption.RTS)).booleanValue());
/*     */     
/*  95 */     activate(this.serialPort.getInputStream(), this.serialPort.getOutputStream());
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxDeviceAddress localAddress() {
/* 100 */     return (RxtxDeviceAddress)super.localAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public RxtxDeviceAddress remoteAddress() {
/* 105 */     return (RxtxDeviceAddress)super.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RxtxDeviceAddress localAddress0() {
/* 110 */     return LOCAL_ADDRESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RxtxDeviceAddress remoteAddress0() {
/* 115 */     return this.deviceAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDisconnect() throws Exception {
/* 125 */     doClose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doClose() throws Exception {
/* 130 */     this.open = false;
/*     */     try {
/* 132 */       super.doClose();
/*     */     } finally {
/* 134 */       if (this.serialPort != null) {
/* 135 */         this.serialPort.removeEventListener();
/* 136 */         this.serialPort.close();
/* 137 */         this.serialPort = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInputShutdown() {
/* 144 */     return !this.open;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ChannelFuture shutdownInput() {
/* 149 */     return newFailedFuture(new UnsupportedOperationException("shutdownInput"));
/*     */   }
/*     */   private final class RxtxUnsafe extends AbstractChannel.AbstractUnsafe { private RxtxUnsafe() {
/* 152 */       super((AbstractChannel)RxtxChannel.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, final ChannelPromise promise) {
/* 157 */       if (!promise.setUncancellable() || !ensureOpen(promise)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 162 */         final boolean wasActive = RxtxChannel.this.isActive();
/* 163 */         RxtxChannel.this.doConnect(remoteAddress, localAddress);
/*     */         
/* 165 */         int waitTime = ((Integer)RxtxChannel.this.config().getOption(RxtxChannelOption.WAIT_TIME)).intValue();
/* 166 */         if (waitTime > 0) {
/* 167 */           RxtxChannel.this.eventLoop().schedule(new Runnable()
/*     */               {
/*     */                 public void run() {
/*     */                   try {
/* 171 */                     RxtxChannel.this.doInit();
/* 172 */                     RxtxChannel.RxtxUnsafe.this.safeSetSuccess(promise);
/* 173 */                     if (!wasActive && RxtxChannel.this.isActive()) {
/* 174 */                       RxtxChannel.this.pipeline().fireChannelActive();
/*     */                     }
/* 176 */                   } catch (Throwable t) {
/* 177 */                     RxtxChannel.RxtxUnsafe.this.safeSetFailure(promise, t);
/* 178 */                     RxtxChannel.RxtxUnsafe.this.closeIfClosed();
/*     */                   } 
/*     */                 }
/*     */               }waitTime, TimeUnit.MILLISECONDS);
/*     */         } else {
/* 183 */           RxtxChannel.this.doInit();
/* 184 */           safeSetSuccess(promise);
/* 185 */           if (!wasActive && RxtxChannel.this.isActive()) {
/* 186 */             RxtxChannel.this.pipeline().fireChannelActive();
/*     */           }
/*     */         } 
/* 189 */       } catch (Throwable t) {
/* 190 */         safeSetFailure(promise, t);
/* 191 */         closeIfClosed();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\rxtx\RxtxChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */