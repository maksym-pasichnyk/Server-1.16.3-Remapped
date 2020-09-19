/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ 
/*    */ public class DedicatedServerSettings
/*    */ {
/*    */   private final Path source;
/*    */   private DedicatedServerProperties properties;
/*    */   
/*    */   public DedicatedServerSettings(RegistryAccess debug1, Path debug2) {
/* 13 */     this.source = debug2;
/* 14 */     this.properties = DedicatedServerProperties.fromFile(debug1, debug2);
/*    */   }
/*    */   
/*    */   public DedicatedServerProperties getProperties() {
/* 18 */     return this.properties;
/*    */   }
/*    */   
/*    */   public void forceSave() {
/* 22 */     this.properties.store(this.source);
/*    */   }
/*    */   
/*    */   public DedicatedServerSettings update(UnaryOperator<DedicatedServerProperties> debug1) {
/* 26 */     (this.properties = debug1.apply(this.properties)).store(this.source);
/* 27 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServerSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */