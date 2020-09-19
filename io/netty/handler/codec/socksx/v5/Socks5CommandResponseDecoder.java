/*     */ package io.netty.handler.codec.socksx.v5;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.handler.codec.socksx.SocksVersion;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Socks5CommandResponseDecoder
/*     */   extends ReplayingDecoder<Socks5CommandResponseDecoder.State>
/*     */ {
/*     */   private final Socks5AddressDecoder addressDecoder;
/*     */   
/*     */   enum State
/*     */   {
/*  38 */     INIT,
/*  39 */     SUCCESS,
/*  40 */     FAILURE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socks5CommandResponseDecoder() {
/*  46 */     this(Socks5AddressDecoder.DEFAULT);
/*     */   }
/*     */   
/*     */   public Socks5CommandResponseDecoder(Socks5AddressDecoder addressDecoder) {
/*  50 */     super(State.INIT);
/*  51 */     if (addressDecoder == null) {
/*  52 */       throw new NullPointerException("addressDecoder");
/*     */     }
/*     */     
/*  55 */     this.addressDecoder = addressDecoder; } protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception { try {
/*     */       byte version; int readableBytes;
/*     */       Socks5CommandStatus status;
/*     */       Socks5AddressType addrType;
/*     */       String addr;
/*     */       int port;
/*  61 */       switch ((State)state()) {
/*     */         case INIT:
/*  63 */           version = in.readByte();
/*  64 */           if (version != SocksVersion.SOCKS5.byteValue()) {
/*  65 */             throw new DecoderException("unsupported version: " + version + " (expected: " + SocksVersion.SOCKS5
/*  66 */                 .byteValue() + ')');
/*     */           }
/*  68 */           status = Socks5CommandStatus.valueOf(in.readByte());
/*  69 */           in.skipBytes(1);
/*  70 */           addrType = Socks5AddressType.valueOf(in.readByte());
/*  71 */           addr = this.addressDecoder.decodeAddress(addrType, in);
/*  72 */           port = in.readUnsignedShort();
/*     */           
/*  74 */           out.add(new DefaultSocks5CommandResponse(status, addrType, addr, port));
/*  75 */           checkpoint(State.SUCCESS);
/*     */         
/*     */         case SUCCESS:
/*  78 */           readableBytes = actualReadableBytes();
/*  79 */           if (readableBytes > 0) {
/*  80 */             out.add(in.readRetainedSlice(readableBytes));
/*     */           }
/*     */           break;
/*     */         
/*     */         case FAILURE:
/*  85 */           in.skipBytes(actualReadableBytes());
/*     */           break;
/*     */       } 
/*     */     
/*  89 */     } catch (Exception e) {
/*  90 */       fail(out, e);
/*     */     }  }
/*     */   
/*     */   private void fail(List<Object> out, Exception cause) {
/*     */     DecoderException decoderException;
/*  95 */     if (!(cause instanceof DecoderException)) {
/*  96 */       decoderException = new DecoderException(cause);
/*     */     }
/*     */     
/*  99 */     checkpoint(State.FAILURE);
/*     */     
/* 101 */     Socks5Message m = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, Socks5AddressType.IPv4, null, 0);
/*     */     
/* 103 */     m.setDecoderResult(DecoderResult.failure((Throwable)decoderException));
/* 104 */     out.add(m);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */