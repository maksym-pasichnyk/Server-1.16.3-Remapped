/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.security.Principal;
/*    */ import javax.net.ssl.X509ExtendedKeyManager;
/*    */ import javax.security.auth.x500.X500Principal;
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
/*    */ final class OpenSslExtendedKeyMaterialManager
/*    */   extends OpenSslKeyMaterialManager
/*    */ {
/*    */   private final X509ExtendedKeyManager keyManager;
/*    */   
/*    */   OpenSslExtendedKeyMaterialManager(X509ExtendedKeyManager keyManager, String password) {
/* 26 */     super(keyManager, password);
/* 27 */     this.keyManager = keyManager;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String chooseClientAlias(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) {
/* 33 */     return this.keyManager.chooseEngineClientAlias(keyTypes, (Principal[])issuer, engine);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String chooseServerAlias(ReferenceCountedOpenSslEngine engine, String type) {
/* 38 */     return this.keyManager.chooseEngineServerAlias(type, null, engine);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\OpenSslExtendedKeyMaterialManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */