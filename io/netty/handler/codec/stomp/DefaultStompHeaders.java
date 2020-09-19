/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.handler.codec.CharSequenceValueConverter;
/*    */ import io.netty.handler.codec.DefaultHeaders;
/*    */ import io.netty.handler.codec.HeadersUtils;
/*    */ import io.netty.handler.codec.ValueConverter;
/*    */ import io.netty.util.AsciiString;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class DefaultStompHeaders
/*    */   extends DefaultHeaders<CharSequence, CharSequence, StompHeaders>
/*    */   implements StompHeaders
/*    */ {
/*    */   public DefaultStompHeaders() {
/* 33 */     super(AsciiString.CASE_SENSITIVE_HASHER, (ValueConverter)CharSequenceValueConverter.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsString(CharSequence name) {
/* 38 */     return HeadersUtils.getAsString(this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> getAllAsString(CharSequence name) {
/* 43 */     return HeadersUtils.getAllAsString(this, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Map.Entry<String, String>> iteratorAsString() {
/* 48 */     return HeadersUtils.iteratorAsString((Iterable)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(CharSequence name, CharSequence value) {
/* 53 */     return contains(name, value, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
/* 58 */     return contains(name, value, ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultStompHeaders copy() {
/* 64 */     DefaultStompHeaders copyHeaders = new DefaultStompHeaders();
/* 65 */     copyHeaders.addImpl(this);
/* 66 */     return copyHeaders;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\DefaultStompHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */