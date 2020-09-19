/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class DelegatingDecompressorFrameListener
/*     */   extends Http2FrameListenerDecorator
/*     */ {
/*     */   private final Http2Connection connection;
/*     */   private final boolean strict;
/*     */   private boolean flowControllerInitialized;
/*     */   private final Http2Connection.PropertyKey propertyKey;
/*     */   
/*     */   public DelegatingDecompressorFrameListener(Http2Connection connection, Http2FrameListener listener) {
/*  50 */     this(connection, listener, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public DelegatingDecompressorFrameListener(Http2Connection connection, Http2FrameListener listener, boolean strict) {
/*  55 */     super(listener);
/*  56 */     this.connection = connection;
/*  57 */     this.strict = strict;
/*     */     
/*  59 */     this.propertyKey = connection.newKey();
/*  60 */     connection.addListener(new Http2ConnectionAdapter()
/*     */         {
/*     */           public void onStreamRemoved(Http2Stream stream) {
/*  63 */             DelegatingDecompressorFrameListener.Http2Decompressor decompressor = DelegatingDecompressorFrameListener.this.decompressor(stream);
/*  64 */             if (decompressor != null) {
/*  65 */               DelegatingDecompressorFrameListener.cleanup(decompressor);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/*  74 */     Http2Stream stream = this.connection.stream(streamId);
/*  75 */     Http2Decompressor decompressor = decompressor(stream);
/*  76 */     if (decompressor == null)
/*     */     {
/*  78 */       return this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */     }
/*     */     
/*  81 */     EmbeddedChannel channel = decompressor.decompressor();
/*  82 */     int compressedBytes = data.readableBytes() + padding;
/*  83 */     decompressor.incrementCompressedBytes(compressedBytes);
/*     */     
/*     */     try {
/*  86 */       channel.writeInbound(new Object[] { data.retain() });
/*  87 */       ByteBuf buf = nextReadableBuf(channel);
/*  88 */       if (buf == null && endOfStream && channel.finish()) {
/*  89 */         buf = nextReadableBuf(channel);
/*     */       }
/*  91 */       if (buf == null) {
/*  92 */         if (endOfStream) {
/*  93 */           this.listener.onDataRead(ctx, streamId, Unpooled.EMPTY_BUFFER, padding, true);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  99 */         decompressor.incrementDecompressedBytes(compressedBytes);
/* 100 */         return compressedBytes;
/*     */       } 
/*     */       try {
/* 103 */         Http2LocalFlowController flowController = this.connection.local().flowController();
/* 104 */         decompressor.incrementDecompressedBytes(padding);
/*     */         while (true) {
/* 106 */           ByteBuf nextBuf = nextReadableBuf(channel);
/* 107 */           boolean decompressedEndOfStream = (nextBuf == null && endOfStream);
/* 108 */           if (decompressedEndOfStream && channel.finish()) {
/* 109 */             nextBuf = nextReadableBuf(channel);
/* 110 */             decompressedEndOfStream = (nextBuf == null);
/*     */           } 
/*     */           
/* 113 */           decompressor.incrementDecompressedBytes(buf.readableBytes());
/*     */ 
/*     */ 
/*     */           
/* 117 */           flowController.consumeBytes(stream, this.listener
/* 118 */               .onDataRead(ctx, streamId, buf, padding, decompressedEndOfStream));
/* 119 */           if (nextBuf == null) {
/*     */             break;
/*     */           }
/*     */           
/* 123 */           padding = 0;
/* 124 */           buf.release();
/* 125 */           buf = nextBuf;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 130 */         return 0;
/*     */       } finally {
/* 132 */         buf.release();
/*     */       } 
/* 134 */     } catch (Http2Exception e) {
/* 135 */       throw e;
/* 136 */     } catch (Throwable t) {
/* 137 */       throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Decompressor error detected while delegating data read on streamId %d", new Object[] {
/* 138 */             Integer.valueOf(stream.id())
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) throws Http2Exception {
/* 145 */     initDecompressor(ctx, streamId, headers, endStream);
/* 146 */     this.listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {
/* 152 */     initDecompressor(ctx, streamId, headers, endStream);
/* 153 */     this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
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
/*     */   protected EmbeddedChannel newContentDecompressor(ChannelHandlerContext ctx, CharSequence contentEncoding) throws Http2Exception {
/* 167 */     if (HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
/* 168 */       return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx
/* 169 */           .channel().config(), new ChannelHandler[] { (ChannelHandler)ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) });
/*     */     }
/* 171 */     if (HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
/* 172 */       ZlibWrapper wrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
/*     */       
/* 174 */       return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx
/* 175 */           .channel().config(), new ChannelHandler[] { (ChannelHandler)ZlibCodecFactory.newZlibDecoder(wrapper) });
/*     */     } 
/*     */     
/* 178 */     return null;
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
/*     */   protected CharSequence getTargetContentEncoding(CharSequence contentEncoding) throws Http2Exception {
/* 191 */     return (CharSequence)HttpHeaderValues.IDENTITY;
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
/*     */   private void initDecompressor(ChannelHandlerContext ctx, int streamId, Http2Headers headers, boolean endOfStream) throws Http2Exception {
/* 206 */     Http2Stream stream = this.connection.stream(streamId);
/* 207 */     if (stream == null) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     Http2Decompressor decompressor = decompressor(stream);
/* 212 */     if (decompressor == null && !endOfStream) {
/*     */       AsciiString asciiString;
/* 214 */       CharSequence contentEncoding = (CharSequence)headers.get(HttpHeaderNames.CONTENT_ENCODING);
/* 215 */       if (contentEncoding == null) {
/* 216 */         asciiString = HttpHeaderValues.IDENTITY;
/*     */       }
/* 218 */       EmbeddedChannel channel = newContentDecompressor(ctx, (CharSequence)asciiString);
/* 219 */       if (channel != null) {
/* 220 */         decompressor = new Http2Decompressor(channel);
/* 221 */         stream.setProperty(this.propertyKey, decompressor);
/*     */ 
/*     */         
/* 224 */         CharSequence targetContentEncoding = getTargetContentEncoding((CharSequence)asciiString);
/* 225 */         if (HttpHeaderValues.IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
/* 226 */           headers.remove(HttpHeaderNames.CONTENT_ENCODING);
/*     */         } else {
/* 228 */           headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 233 */     if (decompressor != null) {
/*     */ 
/*     */ 
/*     */       
/* 237 */       headers.remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */ 
/*     */ 
/*     */       
/* 241 */       if (!this.flowControllerInitialized) {
/* 242 */         this.flowControllerInitialized = true;
/* 243 */         this.connection.local().flowController(new ConsumedBytesConverter(this.connection.local().flowController()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   Http2Decompressor decompressor(Http2Stream stream) {
/* 249 */     return (stream == null) ? null : stream.<Http2Decompressor>getProperty(this.propertyKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void cleanup(Http2Decompressor decompressor) {
/* 258 */     decompressor.decompressor().finishAndReleaseAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf nextReadableBuf(EmbeddedChannel decompressor) {
/*     */     ByteBuf buf;
/*     */     while (true) {
/* 270 */       buf = (ByteBuf)decompressor.readInbound();
/* 271 */       if (buf == null) {
/* 272 */         return null;
/*     */       }
/* 274 */       if (!buf.isReadable()) {
/* 275 */         buf.release(); continue;
/*     */       }  break;
/*     */     } 
/* 278 */     return buf;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class ConsumedBytesConverter
/*     */     implements Http2LocalFlowController
/*     */   {
/*     */     private final Http2LocalFlowController flowController;
/*     */ 
/*     */     
/*     */     ConsumedBytesConverter(Http2LocalFlowController flowController) {
/* 289 */       this.flowController = (Http2LocalFlowController)ObjectUtil.checkNotNull(flowController, "flowController");
/*     */     }
/*     */ 
/*     */     
/*     */     public Http2LocalFlowController frameWriter(Http2FrameWriter frameWriter) {
/* 294 */       return this.flowController.frameWriter(frameWriter);
/*     */     }
/*     */ 
/*     */     
/*     */     public void channelHandlerContext(ChannelHandlerContext ctx) throws Http2Exception {
/* 299 */       this.flowController.channelHandlerContext(ctx);
/*     */     }
/*     */ 
/*     */     
/*     */     public void initialWindowSize(int newWindowSize) throws Http2Exception {
/* 304 */       this.flowController.initialWindowSize(newWindowSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public int initialWindowSize() {
/* 309 */       return this.flowController.initialWindowSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public int windowSize(Http2Stream stream) {
/* 314 */       return this.flowController.windowSize(stream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception {
/* 319 */       this.flowController.incrementWindowSize(stream, delta);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void receiveFlowControlledFrame(Http2Stream stream, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/* 325 */       this.flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean consumeBytes(Http2Stream stream, int numBytes) throws Http2Exception {
/* 330 */       DelegatingDecompressorFrameListener.Http2Decompressor decompressor = DelegatingDecompressorFrameListener.this.decompressor(stream);
/* 331 */       if (decompressor != null)
/*     */       {
/* 333 */         numBytes = decompressor.consumeBytes(stream.id(), numBytes);
/*     */       }
/*     */       try {
/* 336 */         return this.flowController.consumeBytes(stream, numBytes);
/* 337 */       } catch (Http2Exception e) {
/* 338 */         throw e;
/* 339 */       } catch (Throwable t) {
/*     */ 
/*     */         
/* 342 */         throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Error while returning bytes to flow control window", new Object[0]);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int unconsumedBytes(Http2Stream stream) {
/* 348 */       return this.flowController.unconsumedBytes(stream);
/*     */     }
/*     */ 
/*     */     
/*     */     public int initialWindowSize(Http2Stream stream) {
/* 353 */       return this.flowController.initialWindowSize(stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Http2Decompressor
/*     */   {
/*     */     private final EmbeddedChannel decompressor;
/*     */     
/*     */     private int compressed;
/*     */     private int decompressed;
/*     */     
/*     */     Http2Decompressor(EmbeddedChannel decompressor) {
/* 366 */       this.decompressor = decompressor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     EmbeddedChannel decompressor() {
/* 373 */       return this.decompressor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void incrementCompressedBytes(int delta) {
/* 380 */       assert delta >= 0;
/* 381 */       this.compressed += delta;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void incrementDecompressedBytes(int delta) {
/* 388 */       assert delta >= 0;
/* 389 */       this.decompressed += delta;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int consumeBytes(int streamId, int decompressedBytes) throws Http2Exception {
/* 401 */       if (decompressedBytes < 0) {
/* 402 */         throw new IllegalArgumentException("decompressedBytes must not be negative: " + decompressedBytes);
/*     */       }
/* 404 */       if (this.decompressed - decompressedBytes < 0)
/* 405 */         throw Http2Exception.streamError(streamId, Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d. decompressed: %d decompressedBytes: %d", new Object[] {
/*     */               
/* 407 */               Integer.valueOf(streamId), Integer.valueOf(this.decompressed), Integer.valueOf(decompressedBytes)
/*     */             }); 
/* 409 */       double consumedRatio = decompressedBytes / this.decompressed;
/* 410 */       int consumedCompressed = Math.min(this.compressed, (int)Math.ceil(this.compressed * consumedRatio));
/* 411 */       if (this.compressed - consumedCompressed < 0)
/* 412 */         throw Http2Exception.streamError(streamId, Http2Error.INTERNAL_ERROR, "overflow when converting decompressed bytes to compressed bytes for stream %d.decompressedBytes: %d decompressed: %d compressed: %d consumedCompressed: %d", new Object[] {
/*     */ 
/*     */               
/* 415 */               Integer.valueOf(streamId), Integer.valueOf(decompressedBytes), Integer.valueOf(this.decompressed), Integer.valueOf(this.compressed), Integer.valueOf(consumedCompressed)
/*     */             }); 
/* 417 */       this.decompressed -= decompressedBytes;
/* 418 */       this.compressed -= consumedCompressed;
/*     */       
/* 420 */       return consumedCompressed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DelegatingDecompressorFrameListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */