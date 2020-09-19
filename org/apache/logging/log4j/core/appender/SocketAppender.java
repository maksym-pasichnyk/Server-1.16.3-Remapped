/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
/*     */ import org.apache.logging.log4j.core.layout.SerializedLayout;
/*     */ import org.apache.logging.log4j.core.net.AbstractSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.net.DatagramSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.net.SocketOptions;
/*     */ import org.apache.logging.log4j.core.net.SslSocketManager;
/*     */ import org.apache.logging.log4j.core.net.TcpSocketManager;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "Socket", category = "Core", elementType = "appender", printObject = true)
/*     */ public class SocketAppender
/*     */   extends AbstractOutputStreamAppender<AbstractSocketManager>
/*     */ {
/*     */   private final Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static abstract class AbstractBuilder<B extends AbstractBuilder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute
/*     */     private int connectTimeoutMillis;
/*     */     @PluginBuilderAttribute
/*     */     @ValidHost
/*  75 */     private String host = "localhost";
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean immediateFail = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @ValidPort
/*     */     private int port;
/*     */     
/*     */     @PluginBuilderAttribute
/*  86 */     private Protocol protocol = Protocol.TCP;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @PluginAliases({"reconnectDelay", "reconnectionDelay", "delayMillis", "reconnectionDelayMillis"})
/*     */     private int reconnectDelayMillis;
/*     */     
/*     */     @PluginElement("SocketOptions")
/*     */     private SocketOptions socketOptions;
/*     */     
/*     */     @PluginElement("SslConfiguration")
/*     */     @PluginAliases({"SslConfig"})
/*     */     private SslConfiguration sslConfiguration;
/*     */ 
/*     */     
/*     */     public boolean getAdvertise() {
/* 101 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public int getConnectTimeoutMillis() {
/* 105 */       return this.connectTimeoutMillis;
/*     */     }
/*     */     
/*     */     public String getHost() {
/* 109 */       return this.host;
/*     */     }
/*     */     
/*     */     public int getPort() {
/* 113 */       return this.port;
/*     */     }
/*     */     
/*     */     public Protocol getProtocol() {
/* 117 */       return this.protocol;
/*     */     }
/*     */     
/*     */     public SslConfiguration getSslConfiguration() {
/* 121 */       return this.sslConfiguration;
/*     */     }
/*     */     
/*     */     public boolean getImmediateFail() {
/* 125 */       return this.immediateFail;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 129 */       this.advertise = advertise;
/* 130 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withConnectTimeoutMillis(int connectTimeoutMillis) {
/* 134 */       this.connectTimeoutMillis = connectTimeoutMillis;
/* 135 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withHost(String host) {
/* 139 */       this.host = host;
/* 140 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withImmediateFail(boolean immediateFail) {
/* 144 */       this.immediateFail = immediateFail;
/* 145 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withPort(int port) {
/* 149 */       this.port = port;
/* 150 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withProtocol(Protocol protocol) {
/* 154 */       this.protocol = protocol;
/* 155 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withReconnectDelayMillis(int reconnectDelayMillis) {
/* 159 */       this.reconnectDelayMillis = reconnectDelayMillis;
/* 160 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withSocketOptions(SocketOptions socketOptions) {
/* 164 */       this.socketOptions = socketOptions;
/* 165 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withSslConfiguration(SslConfiguration sslConfiguration) {
/* 169 */       this.sslConfiguration = sslConfiguration;
/* 170 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public int getReconnectDelayMillis() {
/* 174 */       return this.reconnectDelayMillis;
/*     */     }
/*     */     
/*     */     public SocketOptions getSocketOptions() {
/* 178 */       return this.socketOptions;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     extends AbstractBuilder<Builder>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SocketAppender>
/*     */   {
/*     */     public SocketAppender build() {
/*     */       SerializedLayout serializedLayout;
/* 196 */       boolean immediateFlush = isImmediateFlush();
/* 197 */       boolean bufferedIo = isBufferedIo();
/* 198 */       Layout<? extends Serializable> layout = getLayout();
/* 199 */       if (layout == null) {
/* 200 */         serializedLayout = SerializedLayout.createLayout();
/*     */       }
/*     */       
/* 203 */       String name = getName();
/* 204 */       if (name == null) {
/* 205 */         SocketAppender.LOGGER.error("No name provided for SocketAppender");
/* 206 */         return null;
/*     */       } 
/*     */       
/* 209 */       Protocol protocol = getProtocol();
/* 210 */       Protocol actualProtocol = (protocol != null) ? protocol : Protocol.TCP;
/* 211 */       if (actualProtocol == Protocol.UDP) {
/* 212 */         immediateFlush = true;
/*     */       }
/*     */       
/* 215 */       AbstractSocketManager manager = SocketAppender.createSocketManager(name, actualProtocol, getHost(), getPort(), getConnectTimeoutMillis(), getSslConfiguration(), getReconnectDelayMillis(), getImmediateFail(), (Layout<? extends Serializable>)serializedLayout, getBufferSize(), getSocketOptions());
/*     */ 
/*     */       
/* 218 */       return new SocketAppender(name, (Layout<? extends Serializable>)serializedLayout, getFilter(), manager, isIgnoreExceptions(), (!bufferedIo || immediateFlush), getAdvertise() ? getConfiguration().getAdvertiser() : null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 225 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/* 234 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/* 235 */     if (advertiser != null) {
/* 236 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 237 */       configuration.putAll(manager.getContentFormat());
/* 238 */       configuration.put("contentType", layout.getContentType());
/* 239 */       configuration.put("name", name);
/* 240 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 242 */       this.advertisement = null;
/*     */     } 
/* 244 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 249 */     setStopping();
/* 250 */     stop(timeout, timeUnit, false);
/* 251 */     if (this.advertiser != null) {
/* 252 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 254 */     setStopped();
/* 255 */     return true;
/*     */   }
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
/*     */   @Deprecated
/*     */   @PluginFactory
/*     */   public static SocketAppender createAppender(String host, int port, Protocol protocol, SslConfiguration sslConfig, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, String name, boolean immediateFlush, boolean ignoreExceptions, Layout<? extends Serializable> layout, Filter filter, boolean advertise, Configuration configuration) {
/* 314 */     return ((Builder)newBuilder().withAdvertise(advertise).setConfiguration(configuration).withConnectTimeoutMillis(connectTimeoutMillis).withFilter(filter)).withHost(host).withIgnoreExceptions(ignoreExceptions).withImmediateFail(immediateFail).withLayout(layout).withName(name).withPort(port).withProtocol(protocol).withReconnectDelayMillis(reconnectDelayMillis).withSslConfiguration(sslConfig).build();
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static SocketAppender createAppender(String host, String portNum, String protocolIn, SslConfiguration sslConfig, int connectTimeoutMillis, String delayMillis, String immediateFail, String name, String immediateFlush, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, Configuration config) {
/* 386 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 387 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 388 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 389 */     boolean fail = Booleans.parseBoolean(immediateFail, true);
/* 390 */     int reconnectDelayMillis = AbstractAppender.parseInt(delayMillis, 0);
/* 391 */     int port = AbstractAppender.parseInt(portNum, 0);
/* 392 */     Protocol p = (protocolIn == null) ? Protocol.UDP : Protocol.valueOf(protocolIn);
/* 393 */     return createAppender(host, port, p, sslConfig, connectTimeoutMillis, reconnectDelayMillis, fail, name, isFlush, ignoreExceptions, layout, filter, isAdvertise, config);
/*     */   }
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
/*     */   @Deprecated
/*     */   protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, int connectTimeoutMillis, SslConfiguration sslConfig, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 408 */     return createSocketManager(name, protocol, host, port, connectTimeoutMillis, sslConfig, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
/*     */   }
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
/*     */   protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, int connectTimeoutMillis, SslConfiguration sslConfig, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 421 */     if (protocol == Protocol.TCP && sslConfig != null)
/*     */     {
/* 423 */       protocol = Protocol.SSL;
/*     */     }
/* 425 */     if (protocol != Protocol.SSL && sslConfig != null) {
/* 426 */       LOGGER.info("Appender {} ignoring SSL configuration for {} protocol", name, protocol);
/*     */     }
/* 428 */     switch (protocol) {
/*     */       case TCP:
/* 430 */         return (AbstractSocketManager)TcpSocketManager.getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
/*     */       
/*     */       case UDP:
/* 433 */         return (AbstractSocketManager)DatagramSocketManager.getSocketManager(host, port, layout, bufferSize);
/*     */       case SSL:
/* 435 */         return (AbstractSocketManager)SslSocketManager.getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
/*     */     } 
/*     */     
/* 438 */     throw new IllegalArgumentException(protocol.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void directEncodeEvent(LogEvent event) {
/* 446 */     writeByteArrayToManager(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\SocketAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */