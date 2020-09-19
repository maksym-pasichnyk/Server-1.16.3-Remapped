/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public class DefaultStompHeadersSubframe
/*    */   implements StompHeadersSubframe
/*    */ {
/*    */   protected final StompCommand command;
/* 26 */   protected DecoderResult decoderResult = DecoderResult.SUCCESS;
/*    */   protected final DefaultStompHeaders headers;
/*    */   
/*    */   public DefaultStompHeadersSubframe(StompCommand command) {
/* 30 */     this(command, null);
/*    */   }
/*    */   
/*    */   DefaultStompHeadersSubframe(StompCommand command, DefaultStompHeaders headers) {
/* 34 */     if (command == null) {
/* 35 */       throw new NullPointerException("command");
/*    */     }
/*    */     
/* 38 */     this.command = command;
/* 39 */     this.headers = (headers == null) ? new DefaultStompHeaders() : headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public StompCommand command() {
/* 44 */     return this.command;
/*    */   }
/*    */ 
/*    */   
/*    */   public StompHeaders headers() {
/* 49 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public DecoderResult decoderResult() {
/* 54 */     return this.decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDecoderResult(DecoderResult decoderResult) {
/* 59 */     this.decoderResult = decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return "StompFrame{command=" + this.command + ", headers=" + this.headers + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\DefaultStompHeadersSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */