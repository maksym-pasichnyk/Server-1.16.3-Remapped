/*    */ package io.netty.handler.codec.http.websocketx.extensions.compression;
/*    */ 
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;
/*    */ import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;
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
/*    */ public class WebSocketServerCompressionHandler
/*    */   extends WebSocketServerExtensionHandler
/*    */ {
/*    */   public WebSocketServerCompressionHandler() {
/* 32 */     super(new WebSocketServerExtensionHandshaker[] { new PerMessageDeflateServerExtensionHandshaker(), new DeflateFrameServerExtensionHandshaker() });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\compression\WebSocketServerCompressionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */