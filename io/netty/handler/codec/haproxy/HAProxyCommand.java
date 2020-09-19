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
/*    */ public enum HAProxyCommand
/*    */ {
/* 26 */   LOCAL((byte)0),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   PROXY((byte)1);
/*    */ 
/*    */ 
/*    */   
/*    */   private static final byte COMMAND_MASK = 15;
/*    */ 
/*    */ 
/*    */   
/*    */   private final byte byteValue;
/*    */ 
/*    */ 
/*    */   
/*    */   HAProxyCommand(byte byteValue) {
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


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\haproxy\HAProxyCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */