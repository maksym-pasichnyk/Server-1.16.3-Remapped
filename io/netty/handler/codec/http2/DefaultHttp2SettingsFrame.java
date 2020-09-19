/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class DefaultHttp2SettingsFrame
/*    */   implements Http2SettingsFrame
/*    */ {
/*    */   private final Http2Settings settings;
/*    */   
/*    */   public DefaultHttp2SettingsFrame(Http2Settings settings) {
/* 32 */     this.settings = (Http2Settings)ObjectUtil.checkNotNull(settings, "settings");
/*    */   }
/*    */ 
/*    */   
/*    */   public Http2Settings settings() {
/* 37 */     return this.settings;
/*    */   }
/*    */ 
/*    */   
/*    */   public String name() {
/* 42 */     return "SETTINGS";
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return StringUtil.simpleClassName(this) + "(settings=" + this.settings + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2SettingsFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */