/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.handler.codec.DefaultHeaders;
/*    */ import io.netty.handler.codec.UnsupportedValueConverter;
/*    */ import io.netty.handler.codec.ValueConverter;
/*    */ import io.netty.util.AsciiString;
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
/*    */ public final class CharSequenceMap<V>
/*    */   extends DefaultHeaders<CharSequence, V, CharSequenceMap<V>>
/*    */ {
/*    */   public CharSequenceMap() {
/* 32 */     this(true);
/*    */   }
/*    */   
/*    */   public CharSequenceMap(boolean caseSensitive) {
/* 36 */     this(caseSensitive, (ValueConverter<V>)UnsupportedValueConverter.instance());
/*    */   }
/*    */   
/*    */   public CharSequenceMap(boolean caseSensitive, ValueConverter<V> valueConverter) {
/* 40 */     super(caseSensitive ? AsciiString.CASE_SENSITIVE_HASHER : AsciiString.CASE_INSENSITIVE_HASHER, valueConverter);
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequenceMap(boolean caseSensitive, ValueConverter<V> valueConverter, int arraySizeHint) {
/* 45 */     super(caseSensitive ? AsciiString.CASE_SENSITIVE_HASHER : AsciiString.CASE_INSENSITIVE_HASHER, valueConverter, DefaultHeaders.NameValidator.NOT_NULL, arraySizeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\CharSequenceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */