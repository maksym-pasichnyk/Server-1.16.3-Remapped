/*    */ package io.netty.handler.codec.socksx.v4;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.handler.codec.ReplayingDecoder;
/*    */ import io.netty.util.NetUtil;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Socks4ClientDecoder
/*    */   extends ReplayingDecoder<Socks4ClientDecoder.State>
/*    */ {
/*    */   enum State
/*    */   {
/* 37 */     START,
/* 38 */     SUCCESS,
/* 39 */     FAILURE;
/*    */   }
/*    */   
/*    */   public Socks4ClientDecoder() {
/* 43 */     super(State.START);
/* 44 */     setSingleDecode(true); } protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception { try {
/*    */       int version;
/*    */       int readableBytes;
/*    */       Socks4CommandStatus status;
/*    */       int dstPort;
/*    */       String dstAddr;
/* 50 */       switch ((State)state()) {
/*    */         case START:
/* 52 */           version = in.readUnsignedByte();
/* 53 */           if (version != 0) {
/* 54 */             throw new DecoderException("unsupported reply version: " + version + " (expected: 0)");
/*    */           }
/*    */           
/* 57 */           status = Socks4CommandStatus.valueOf(in.readByte());
/* 58 */           dstPort = in.readUnsignedShort();
/* 59 */           dstAddr = NetUtil.intToIpAddress(in.readInt());
/*    */           
/* 61 */           out.add(new DefaultSocks4CommandResponse(status, dstAddr, dstPort));
/* 62 */           checkpoint(State.SUCCESS);
/*    */         
/*    */         case SUCCESS:
/* 65 */           readableBytes = actualReadableBytes();
/* 66 */           if (readableBytes > 0) {
/* 67 */             out.add(in.readRetainedSlice(readableBytes));
/*    */           }
/*    */           break;
/*    */         
/*    */         case FAILURE:
/* 72 */           in.skipBytes(actualReadableBytes());
/*    */           break;
/*    */       } 
/*    */     
/* 76 */     } catch (Exception e) {
/* 77 */       fail(out, e);
/*    */     }  }
/*    */   
/*    */   private void fail(List<Object> out, Exception cause) {
/*    */     DecoderException decoderException;
/* 82 */     if (!(cause instanceof DecoderException)) {
/* 83 */       decoderException = new DecoderException(cause);
/*    */     }
/*    */     
/* 86 */     Socks4CommandResponse m = new DefaultSocks4CommandResponse(Socks4CommandStatus.REJECTED_OR_FAILED);
/* 87 */     m.setDecoderResult(DecoderResult.failure((Throwable)decoderException));
/* 88 */     out.add(m);
/*    */     
/* 90 */     checkpoint(State.FAILURE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v4\Socks4ClientDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */