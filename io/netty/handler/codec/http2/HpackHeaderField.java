/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class HpackHeaderField
/*    */ {
/*    */   static final int HEADER_ENTRY_OVERHEAD = 32;
/*    */   final CharSequence name;
/*    */   final CharSequence value;
/*    */   
/*    */   static long sizeOf(CharSequence name, CharSequence value) {
/* 44 */     return (name.length() + value.length() + 32);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   HpackHeaderField(CharSequence name, CharSequence value) {
/* 52 */     this.name = (CharSequence)ObjectUtil.checkNotNull(name, "name");
/* 53 */     this.value = (CharSequence)ObjectUtil.checkNotNull(value, "value");
/*    */   }
/*    */   
/*    */   final int size() {
/* 57 */     return this.name.length() + this.value.length() + 32;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 63 */     return super.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object obj) {
/* 68 */     if (obj == this) {
/* 69 */       return true;
/*    */     }
/* 71 */     if (!(obj instanceof HpackHeaderField)) {
/* 72 */       return false;
/*    */     }
/* 74 */     HpackHeaderField other = (HpackHeaderField)obj;
/*    */     
/* 76 */     return ((HpackUtil.equalsConstantTime(this.name, other.name) & HpackUtil.equalsConstantTime(this.value, other.value)) != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return this.name + ": " + this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackHeaderField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */