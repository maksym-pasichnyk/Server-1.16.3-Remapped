/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.embedded.EmbeddedChannel;
/*     */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*     */ import io.netty.handler.codec.compression.ZlibWrapper;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.PromiseCombiner;
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
/*     */ public class CompressorHttp2ConnectionEncoder
/*     */   extends DecoratingHttp2ConnectionEncoder
/*     */ {
/*     */   public static final int DEFAULT_COMPRESSION_LEVEL = 6;
/*     */   public static final int DEFAULT_WINDOW_BITS = 15;
/*     */   public static final int DEFAULT_MEM_LEVEL = 8;
/*     */   private final int compressionLevel;
/*     */   private final int windowBits;
/*     */   private final int memLevel;
/*     */   private final Http2Connection.PropertyKey propertyKey;
/*     */   
/*     */   public CompressorHttp2ConnectionEncoder(Http2ConnectionEncoder delegate) {
/*  53 */     this(delegate, 6, 15, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompressorHttp2ConnectionEncoder(Http2ConnectionEncoder delegate, int compressionLevel, int windowBits, int memLevel) {
/*  58 */     super(delegate);
/*  59 */     if (compressionLevel < 0 || compressionLevel > 9) {
/*  60 */       throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
/*     */     }
/*  62 */     if (windowBits < 9 || windowBits > 15) {
/*  63 */       throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
/*     */     }
/*  65 */     if (memLevel < 1 || memLevel > 9) {
/*  66 */       throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
/*     */     }
/*  68 */     this.compressionLevel = compressionLevel;
/*  69 */     this.windowBits = windowBits;
/*  70 */     this.memLevel = memLevel;
/*     */     
/*  72 */     this.propertyKey = connection().newKey();
/*  73 */     connection().addListener(new Http2ConnectionAdapter()
/*     */         {
/*     */           public void onStreamRemoved(Http2Stream stream) {
/*  76 */             EmbeddedChannel compressor = stream.<EmbeddedChannel>getProperty(CompressorHttp2ConnectionEncoder.this.propertyKey);
/*  77 */             if (compressor != null) {
/*  78 */               CompressorHttp2ConnectionEncoder.this.cleanup(stream, compressor);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
/*  87 */     Http2Stream stream = connection().stream(streamId);
/*  88 */     EmbeddedChannel channel = (stream == null) ? null : stream.<EmbeddedChannel>getProperty(this.propertyKey);
/*  89 */     if (channel == null)
/*     */     {
/*  91 */       return super.writeData(ctx, streamId, data, padding, endOfStream, promise);
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  96 */       channel.writeOutbound(new Object[] { data });
/*  97 */       ByteBuf buf = nextReadableBuf(channel);
/*  98 */       if (buf == null) {
/*  99 */         if (endOfStream) {
/* 100 */           if (channel.finish()) {
/* 101 */             buf = nextReadableBuf(channel);
/*     */           }
/* 103 */           return super.writeData(ctx, streamId, (buf == null) ? Unpooled.EMPTY_BUFFER : buf, padding, true, promise);
/*     */         } 
/*     */ 
/*     */         
/* 107 */         promise.setSuccess();
/* 108 */         return (ChannelFuture)promise;
/*     */       } 
/*     */       
/* 111 */       PromiseCombiner combiner = new PromiseCombiner();
/*     */       while (true) {
/* 113 */         ByteBuf nextBuf = nextReadableBuf(channel);
/* 114 */         boolean compressedEndOfStream = (nextBuf == null && endOfStream);
/* 115 */         if (compressedEndOfStream && channel.finish()) {
/* 116 */           nextBuf = nextReadableBuf(channel);
/* 117 */           compressedEndOfStream = (nextBuf == null);
/*     */         } 
/*     */         
/* 120 */         ChannelPromise bufPromise = ctx.newPromise();
/* 121 */         combiner.add((Promise)bufPromise);
/* 122 */         super.writeData(ctx, streamId, buf, padding, compressedEndOfStream, bufPromise);
/* 123 */         if (nextBuf == null) {
/*     */           break;
/*     */         }
/*     */         
/* 127 */         padding = 0;
/* 128 */         buf = nextBuf;
/*     */       } 
/* 130 */       combiner.finish((Promise)promise);
/* 131 */     } catch (Throwable cause) {
/* 132 */       promise.tryFailure(cause);
/*     */     } finally {
/* 134 */       if (endOfStream) {
/* 135 */         cleanup(stream, channel);
/*     */       }
/*     */     } 
/* 138 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
/*     */     try {
/* 146 */       EmbeddedChannel compressor = newCompressor(ctx, headers, endStream);
/*     */ 
/*     */       
/* 149 */       ChannelFuture future = super.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
/*     */ 
/*     */       
/* 152 */       bindCompressorToStream(compressor, streamId);
/*     */       
/* 154 */       return future;
/* 155 */     } catch (Throwable e) {
/* 156 */       promise.tryFailure(e);
/*     */       
/* 158 */       return (ChannelFuture)promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
/*     */     try {
/* 167 */       EmbeddedChannel compressor = newCompressor(ctx, headers, endOfStream);
/*     */ 
/*     */       
/* 170 */       ChannelFuture future = super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
/*     */ 
/*     */ 
/*     */       
/* 174 */       bindCompressorToStream(compressor, streamId);
/*     */       
/* 176 */       return future;
/* 177 */     } catch (Throwable e) {
/* 178 */       promise.tryFailure(e);
/*     */       
/* 180 */       return (ChannelFuture)promise;
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
/*     */ 
/*     */   
/*     */   protected EmbeddedChannel newContentCompressor(ChannelHandlerContext ctx, CharSequence contentEncoding) throws Http2Exception {
/* 195 */     if (HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
/* 196 */       return newCompressionChannel(ctx, ZlibWrapper.GZIP);
/*     */     }
/* 198 */     if (HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
/* 199 */       return newCompressionChannel(ctx, ZlibWrapper.ZLIB);
/*     */     }
/*     */     
/* 202 */     return null;
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
/*     */   protected CharSequence getTargetContentEncoding(CharSequence contentEncoding) throws Http2Exception {
/* 214 */     return contentEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EmbeddedChannel newCompressionChannel(ChannelHandlerContext ctx, ZlibWrapper wrapper) {
/* 223 */     return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx
/* 224 */         .channel().config(), new ChannelHandler[] { (ChannelHandler)ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel) });
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
/*     */   private EmbeddedChannel newCompressor(ChannelHandlerContext ctx, Http2Headers headers, boolean endOfStream) throws Http2Exception {
/*     */     AsciiString asciiString;
/* 240 */     if (endOfStream) {
/* 241 */       return null;
/*     */     }
/*     */     
/* 244 */     CharSequence encoding = (CharSequence)headers.get(HttpHeaderNames.CONTENT_ENCODING);
/* 245 */     if (encoding == null) {
/* 246 */       asciiString = HttpHeaderValues.IDENTITY;
/*     */     }
/* 248 */     EmbeddedChannel compressor = newContentCompressor(ctx, (CharSequence)asciiString);
/* 249 */     if (compressor != null) {
/* 250 */       CharSequence targetContentEncoding = getTargetContentEncoding((CharSequence)asciiString);
/* 251 */       if (HttpHeaderValues.IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
/* 252 */         headers.remove(HttpHeaderNames.CONTENT_ENCODING);
/*     */       } else {
/* 254 */         headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 260 */       headers.remove(HttpHeaderNames.CONTENT_LENGTH);
/*     */     } 
/*     */     
/* 263 */     return compressor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindCompressorToStream(EmbeddedChannel compressor, int streamId) {
/* 272 */     if (compressor != null) {
/* 273 */       Http2Stream stream = connection().stream(streamId);
/* 274 */       if (stream != null) {
/* 275 */         stream.setProperty(this.propertyKey, compressor);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void cleanup(Http2Stream stream, EmbeddedChannel compressor) {
/* 287 */     if (compressor.finish()) {
/*     */       while (true) {
/* 289 */         ByteBuf buf = (ByteBuf)compressor.readOutbound();
/* 290 */         if (buf == null) {
/*     */           break;
/*     */         }
/*     */         
/* 294 */         buf.release();
/*     */       } 
/*     */     }
/* 297 */     stream.removeProperty(this.propertyKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf nextReadableBuf(EmbeddedChannel compressor) {
/*     */     ByteBuf buf;
/*     */     while (true) {
/* 308 */       buf = (ByteBuf)compressor.readOutbound();
/* 309 */       if (buf == null) {
/* 310 */         return null;
/*     */       }
/* 312 */       if (!buf.isReadable()) {
/* 313 */         buf.release(); continue;
/*     */       }  break;
/*     */     } 
/* 316 */     return buf;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\CompressorHttp2ConnectionEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */