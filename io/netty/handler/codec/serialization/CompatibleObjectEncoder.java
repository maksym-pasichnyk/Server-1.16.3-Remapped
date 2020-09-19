/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufOutputStream;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompatibleObjectEncoder
/*    */   extends MessageToByteEncoder<Serializable>
/*    */ {
/*    */   private final int resetInterval;
/*    */   private int writtenObjects;
/*    */   
/*    */   public CompatibleObjectEncoder() {
/* 45 */     this(16);
/*    */   }
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
/*    */   public CompatibleObjectEncoder(int resetInterval) {
/* 58 */     if (resetInterval < 0) {
/* 59 */       throw new IllegalArgumentException("resetInterval: " + resetInterval);
/*    */     }
/*    */     
/* 62 */     this.resetInterval = resetInterval;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ObjectOutputStream newObjectOutputStream(OutputStream out) throws Exception {
/* 71 */     return new ObjectOutputStream(out);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
/* 76 */     ObjectOutputStream oos = newObjectOutputStream((OutputStream)new ByteBufOutputStream(out));
/*    */     try {
/* 78 */       if (this.resetInterval != 0) {
/*    */         
/* 80 */         this.writtenObjects++;
/* 81 */         if (this.writtenObjects % this.resetInterval == 0) {
/* 82 */           oos.reset();
/*    */         }
/*    */       } 
/*    */       
/* 86 */       oos.writeObject(msg);
/* 87 */       oos.flush();
/*    */     } finally {
/* 89 */       oos.close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\serialization\CompatibleObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */