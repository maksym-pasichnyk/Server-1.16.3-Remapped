/*    */ package io.netty.channel.unix;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class DomainSocketAddress
/*    */   extends SocketAddress
/*    */ {
/*    */   private static final long serialVersionUID = -6934618000832236893L;
/*    */   private final String socketPath;
/*    */   
/*    */   public DomainSocketAddress(String socketPath) {
/* 30 */     if (socketPath == null) {
/* 31 */       throw new NullPointerException("socketPath");
/*    */     }
/* 33 */     this.socketPath = socketPath;
/*    */   }
/*    */   
/*    */   public DomainSocketAddress(File file) {
/* 37 */     this(file.getPath());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String path() {
/* 44 */     return this.socketPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return path();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 54 */     if (this == o) {
/* 55 */       return true;
/*    */     }
/* 57 */     if (!(o instanceof DomainSocketAddress)) {
/* 58 */       return false;
/*    */     }
/*    */     
/* 61 */     return ((DomainSocketAddress)o).socketPath.equals(this.socketPath);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.socketPath.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\DomainSocketAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */