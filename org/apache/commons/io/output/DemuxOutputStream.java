/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public class DemuxOutputStream
/*    */   extends OutputStream
/*    */ {
/* 28 */   private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal = new InheritableThreadLocal<OutputStream>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream bindStream(OutputStream output) {
/* 38 */     OutputStream stream = this.outputStreamThreadLocal.get();
/* 39 */     this.outputStreamThreadLocal.set(output);
/* 40 */     return stream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 51 */     OutputStream output = this.outputStreamThreadLocal.get();
/* 52 */     if (null != output) {
/* 53 */       output.close();
/*    */     }
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
/*    */   public void flush() throws IOException {
/* 66 */     OutputStream output = this.outputStreamThreadLocal.get();
/* 67 */     if (null != output) {
/* 68 */       output.flush();
/*    */     }
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
/*    */   public void write(int ch) throws IOException {
/* 83 */     OutputStream output = this.outputStreamThreadLocal.get();
/* 84 */     if (null != output)
/* 85 */       output.write(ch); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\output\DemuxOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */