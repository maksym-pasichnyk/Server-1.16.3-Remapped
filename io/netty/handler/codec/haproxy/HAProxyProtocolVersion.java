/*    */ package io.netty.handler.codec.haproxy;
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
/*    */ public enum HAProxyProtocolVersion
/*    */ {
/* 27 */   V1((byte)16),
/*    */ 
/*    */ 
/*    */   
/* 31 */   V2((byte)32);
/*    */ 
/*    */ 
/*    */   
/*    */   private static final byte VERSION_MASK = -16;
/*    */ 
/*    */ 
/*    */   
/*    */   private final byte byteValue;
/*    */ 
/*    */ 
/*    */   
/*    */   HAProxyProtocolVersion(byte byteValue) {
/* 44 */     this.byteValue = byteValue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte byteValue() {
/* 68 */     return this.byteValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\haproxy\HAProxyProtocolVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */