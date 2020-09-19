/*    */ package io.netty.handler.codec.haproxy;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public final class HAProxySSLTLV
/*    */   extends HAProxyTLV
/*    */ {
/*    */   private final int verify;
/*    */   private final List<HAProxyTLV> tlvs;
/*    */   private final byte clientBitField;
/*    */   
/*    */   HAProxySSLTLV(int verify, byte clientBitField, List<HAProxyTLV> tlvs, ByteBuf rawContent) {
/* 44 */     super(HAProxyTLV.Type.PP2_TYPE_SSL, (byte)32, rawContent);
/*    */     
/* 46 */     this.verify = verify;
/* 47 */     this.tlvs = Collections.unmodifiableList(tlvs);
/* 48 */     this.clientBitField = clientBitField;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPP2ClientCertConn() {
/* 55 */     return ((this.clientBitField & 0x2) != 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPP2ClientSSL() {
/* 62 */     return ((this.clientBitField & 0x1) != 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPP2ClientCertSess() {
/* 69 */     return ((this.clientBitField & 0x4) != 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int verify() {
/* 76 */     return this.verify;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<HAProxyTLV> encapsulatedTLVs() {
/* 83 */     return this.tlvs;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\haproxy\HAProxySSLTLV.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */