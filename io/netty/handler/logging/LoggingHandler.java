/*     */ package io.netty.handler.logging;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogLevel;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
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
/*     */ @Sharable
/*     */ public class LoggingHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  43 */   private static final LogLevel DEFAULT_LEVEL = LogLevel.DEBUG;
/*     */ 
/*     */   
/*     */   protected final InternalLogger logger;
/*     */ 
/*     */   
/*     */   protected final InternalLogLevel internalLevel;
/*     */   
/*     */   private final LogLevel level;
/*     */ 
/*     */   
/*     */   public LoggingHandler() {
/*  55 */     this(DEFAULT_LEVEL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingHandler(LogLevel level) {
/*  65 */     if (level == null) {
/*  66 */       throw new NullPointerException("level");
/*     */     }
/*     */     
/*  69 */     this.logger = InternalLoggerFactory.getInstance(getClass());
/*  70 */     this.level = level;
/*  71 */     this.internalLevel = level.toInternalLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingHandler(Class<?> clazz) {
/*  81 */     this(clazz, DEFAULT_LEVEL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingHandler(Class<?> clazz, LogLevel level) {
/*  91 */     if (clazz == null) {
/*  92 */       throw new NullPointerException("clazz");
/*     */     }
/*  94 */     if (level == null) {
/*  95 */       throw new NullPointerException("level");
/*     */     }
/*     */     
/*  98 */     this.logger = InternalLoggerFactory.getInstance(clazz);
/*  99 */     this.level = level;
/* 100 */     this.internalLevel = level.toInternalLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingHandler(String name) {
/* 109 */     this(name, DEFAULT_LEVEL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingHandler(String name, LogLevel level) {
/* 119 */     if (name == null) {
/* 120 */       throw new NullPointerException("name");
/*     */     }
/* 122 */     if (level == null) {
/* 123 */       throw new NullPointerException("level");
/*     */     }
/*     */     
/* 126 */     this.logger = InternalLoggerFactory.getInstance(name);
/* 127 */     this.level = level;
/* 128 */     this.internalLevel = level.toInternalLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogLevel level() {
/* 135 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
/* 140 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 141 */       this.logger.log(this.internalLevel, format(ctx, "REGISTERED"));
/*     */     }
/* 143 */     ctx.fireChannelRegistered();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
/* 148 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 149 */       this.logger.log(this.internalLevel, format(ctx, "UNREGISTERED"));
/*     */     }
/* 151 */     ctx.fireChannelUnregistered();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 156 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 157 */       this.logger.log(this.internalLevel, format(ctx, "ACTIVE"));
/*     */     }
/* 159 */     ctx.fireChannelActive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 164 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 165 */       this.logger.log(this.internalLevel, format(ctx, "INACTIVE"));
/*     */     }
/* 167 */     ctx.fireChannelInactive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 172 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 173 */       this.logger.log(this.internalLevel, format(ctx, "EXCEPTION", cause), cause);
/*     */     }
/* 175 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 180 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 181 */       this.logger.log(this.internalLevel, format(ctx, "USER_EVENT", evt));
/*     */     }
/* 183 */     ctx.fireUserEventTriggered(evt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 188 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 189 */       this.logger.log(this.internalLevel, format(ctx, "BIND", localAddress));
/*     */     }
/* 191 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 198 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 199 */       this.logger.log(this.internalLevel, format(ctx, "CONNECT", remoteAddress, localAddress));
/*     */     }
/* 201 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 206 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 207 */       this.logger.log(this.internalLevel, format(ctx, "DISCONNECT"));
/*     */     }
/* 209 */     ctx.disconnect(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 214 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 215 */       this.logger.log(this.internalLevel, format(ctx, "CLOSE"));
/*     */     }
/* 217 */     ctx.close(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 222 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 223 */       this.logger.log(this.internalLevel, format(ctx, "DEREGISTER"));
/*     */     }
/* 225 */     ctx.deregister(promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 230 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 231 */       this.logger.log(this.internalLevel, format(ctx, "READ COMPLETE"));
/*     */     }
/* 233 */     ctx.fireChannelReadComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 238 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 239 */       this.logger.log(this.internalLevel, format(ctx, "READ", msg));
/*     */     }
/* 241 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 246 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 247 */       this.logger.log(this.internalLevel, format(ctx, "WRITE", msg));
/*     */     }
/* 249 */     ctx.write(msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/* 254 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 255 */       this.logger.log(this.internalLevel, format(ctx, "WRITABILITY CHANGED"));
/*     */     }
/* 257 */     ctx.fireChannelWritabilityChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception {
/* 262 */     if (this.logger.isEnabled(this.internalLevel)) {
/* 263 */       this.logger.log(this.internalLevel, format(ctx, "FLUSH"));
/*     */     }
/* 265 */     ctx.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String format(ChannelHandlerContext ctx, String eventName) {
/* 274 */     String chStr = ctx.channel().toString();
/* 275 */     return (new StringBuilder(chStr.length() + 1 + eventName.length()))
/* 276 */       .append(chStr)
/* 277 */       .append(' ')
/* 278 */       .append(eventName)
/* 279 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String format(ChannelHandlerContext ctx, String eventName, Object arg) {
/* 289 */     if (arg instanceof ByteBuf)
/* 290 */       return formatByteBuf(ctx, eventName, (ByteBuf)arg); 
/* 291 */     if (arg instanceof ByteBufHolder) {
/* 292 */       return formatByteBufHolder(ctx, eventName, (ByteBufHolder)arg);
/*     */     }
/* 294 */     return formatSimple(ctx, eventName, arg);
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
/*     */   protected String format(ChannelHandlerContext ctx, String eventName, Object firstArg, Object secondArg) {
/* 307 */     if (secondArg == null) {
/* 308 */       return formatSimple(ctx, eventName, firstArg);
/*     */     }
/*     */     
/* 311 */     String chStr = ctx.channel().toString();
/* 312 */     String arg1Str = String.valueOf(firstArg);
/* 313 */     String arg2Str = secondArg.toString();
/*     */     
/* 315 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + arg1Str.length() + 2 + arg2Str.length());
/* 316 */     buf.append(chStr).append(' ').append(eventName).append(": ").append(arg1Str).append(", ").append(arg2Str);
/* 317 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatByteBuf(ChannelHandlerContext ctx, String eventName, ByteBuf msg) {
/* 324 */     String chStr = ctx.channel().toString();
/* 325 */     int length = msg.readableBytes();
/* 326 */     if (length == 0) {
/* 327 */       StringBuilder stringBuilder = new StringBuilder(chStr.length() + 1 + eventName.length() + 4);
/* 328 */       stringBuilder.append(chStr).append(' ').append(eventName).append(": 0B");
/* 329 */       return stringBuilder.toString();
/*     */     } 
/* 331 */     int rows = length / 16 + ((length % 15 == 0) ? 0 : 1) + 4;
/* 332 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + 10 + 1 + 2 + rows * 80);
/*     */     
/* 334 */     buf.append(chStr).append(' ').append(eventName).append(": ").append(length).append('B').append(StringUtil.NEWLINE);
/* 335 */     ByteBufUtil.appendPrettyHexDump(buf, msg);
/*     */     
/* 337 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatByteBufHolder(ChannelHandlerContext ctx, String eventName, ByteBufHolder msg) {
/* 345 */     String chStr = ctx.channel().toString();
/* 346 */     String msgStr = msg.toString();
/* 347 */     ByteBuf content = msg.content();
/* 348 */     int length = content.readableBytes();
/* 349 */     if (length == 0) {
/* 350 */       StringBuilder stringBuilder = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + msgStr.length() + 4);
/* 351 */       stringBuilder.append(chStr).append(' ').append(eventName).append(", ").append(msgStr).append(", 0B");
/* 352 */       return stringBuilder.toString();
/*     */     } 
/* 354 */     int rows = length / 16 + ((length % 15 == 0) ? 0 : 1) + 4;
/*     */     
/* 356 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + msgStr.length() + 2 + 10 + 1 + 2 + rows * 80);
/*     */     
/* 358 */     buf.append(chStr).append(' ').append(eventName).append(": ")
/* 359 */       .append(msgStr).append(", ").append(length).append('B').append(StringUtil.NEWLINE);
/* 360 */     ByteBufUtil.appendPrettyHexDump(buf, content);
/*     */     
/* 362 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatSimple(ChannelHandlerContext ctx, String eventName, Object msg) {
/* 370 */     String chStr = ctx.channel().toString();
/* 371 */     String msgStr = String.valueOf(msg);
/* 372 */     StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 2 + msgStr.length());
/* 373 */     return buf.append(chStr).append(' ').append(eventName).append(": ").append(msgStr).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\logging\LoggingHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */