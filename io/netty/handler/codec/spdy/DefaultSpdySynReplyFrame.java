/*    */ package io.netty.handler.codec.spdy;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public class DefaultSpdySynReplyFrame
/*    */   extends DefaultSpdyHeadersFrame
/*    */   implements SpdySynReplyFrame
/*    */ {
/*    */   public DefaultSpdySynReplyFrame(int streamId) {
/* 32 */     super(streamId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultSpdySynReplyFrame(int streamId, boolean validateHeaders) {
/* 42 */     super(streamId, validateHeaders);
/*    */   }
/*    */ 
/*    */   
/*    */   public SpdySynReplyFrame setStreamId(int streamId) {
/* 47 */     super.setStreamId(streamId);
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpdySynReplyFrame setLast(boolean last) {
/* 53 */     super.setLast(last);
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpdySynReplyFrame setInvalid() {
/* 59 */     super.setInvalid();
/* 60 */     return this;
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
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     StringBuilder buf = (new StringBuilder()).append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
/* 76 */     appendHeaders(buf);
/*    */ 
/*    */     
/* 79 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 80 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\DefaultSpdySynReplyFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */