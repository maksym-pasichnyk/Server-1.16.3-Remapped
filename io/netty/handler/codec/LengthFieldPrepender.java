/*     */ package io.netty.handler.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.nio.ByteOrder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class LengthFieldPrepender
/*     */   extends MessageToMessageEncoder<ByteBuf>
/*     */ {
/*     */   private final ByteOrder byteOrder;
/*     */   private final int lengthFieldLength;
/*     */   private final boolean lengthIncludesLengthFieldLength;
/*     */   private final int lengthAdjustment;
/*     */   
/*     */   public LengthFieldPrepender(int lengthFieldLength) {
/*  71 */     this(lengthFieldLength, false);
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
/*     */   public LengthFieldPrepender(int lengthFieldLength, boolean lengthIncludesLengthFieldLength) {
/*  88 */     this(lengthFieldLength, 0, lengthIncludesLengthFieldLength);
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
/*     */   public LengthFieldPrepender(int lengthFieldLength, int lengthAdjustment) {
/* 103 */     this(lengthFieldLength, lengthAdjustment, false);
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
/*     */   public LengthFieldPrepender(int lengthFieldLength, int lengthAdjustment, boolean lengthIncludesLengthFieldLength) {
/* 122 */     this(ByteOrder.BIG_ENDIAN, lengthFieldLength, lengthAdjustment, lengthIncludesLengthFieldLength);
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
/*     */   public LengthFieldPrepender(ByteOrder byteOrder, int lengthFieldLength, int lengthAdjustment, boolean lengthIncludesLengthFieldLength) {
/* 144 */     if (lengthFieldLength != 1 && lengthFieldLength != 2 && lengthFieldLength != 3 && lengthFieldLength != 4 && lengthFieldLength != 8)
/*     */     {
/*     */       
/* 147 */       throw new IllegalArgumentException("lengthFieldLength must be either 1, 2, 3, 4, or 8: " + lengthFieldLength);
/*     */     }
/*     */ 
/*     */     
/* 151 */     ObjectUtil.checkNotNull(byteOrder, "byteOrder");
/*     */     
/* 153 */     this.byteOrder = byteOrder;
/* 154 */     this.lengthFieldLength = lengthFieldLength;
/* 155 */     this.lengthIncludesLengthFieldLength = lengthIncludesLengthFieldLength;
/* 156 */     this.lengthAdjustment = lengthAdjustment;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
/* 161 */     int length = msg.readableBytes() + this.lengthAdjustment;
/* 162 */     if (this.lengthIncludesLengthFieldLength) {
/* 163 */       length += this.lengthFieldLength;
/*     */     }
/*     */     
/* 166 */     if (length < 0) {
/* 167 */       throw new IllegalArgumentException("Adjusted frame length (" + length + ") is less than zero");
/*     */     }
/*     */ 
/*     */     
/* 171 */     switch (this.lengthFieldLength) {
/*     */       case 1:
/* 173 */         if (length >= 256) {
/* 174 */           throw new IllegalArgumentException("length does not fit into a byte: " + length);
/*     */         }
/*     */         
/* 177 */         out.add(ctx.alloc().buffer(1).order(this.byteOrder).writeByte((byte)length));
/*     */         break;
/*     */       case 2:
/* 180 */         if (length >= 65536) {
/* 181 */           throw new IllegalArgumentException("length does not fit into a short integer: " + length);
/*     */         }
/*     */         
/* 184 */         out.add(ctx.alloc().buffer(2).order(this.byteOrder).writeShort((short)length));
/*     */         break;
/*     */       case 3:
/* 187 */         if (length >= 16777216) {
/* 188 */           throw new IllegalArgumentException("length does not fit into a medium integer: " + length);
/*     */         }
/*     */         
/* 191 */         out.add(ctx.alloc().buffer(3).order(this.byteOrder).writeMedium(length));
/*     */         break;
/*     */       case 4:
/* 194 */         out.add(ctx.alloc().buffer(4).order(this.byteOrder).writeInt(length));
/*     */         break;
/*     */       case 8:
/* 197 */         out.add(ctx.alloc().buffer(8).order(this.byteOrder).writeLong(length));
/*     */         break;
/*     */       default:
/* 200 */         throw new Error("should not reach here");
/*     */     } 
/* 202 */     out.add(msg.retain());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\LengthFieldPrepender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */