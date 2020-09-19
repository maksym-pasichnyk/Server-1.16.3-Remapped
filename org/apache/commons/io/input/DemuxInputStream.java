/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class DemuxInputStream
/*    */   extends InputStream
/*    */ {
/* 33 */   private final InheritableThreadLocal<InputStream> m_streams = new InheritableThreadLocal<InputStream>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream bindStream(InputStream input) {
/* 43 */     InputStream oldValue = this.m_streams.get();
/* 44 */     this.m_streams.set(input);
/* 45 */     return oldValue;
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
/*    */   public void close() throws IOException {
/* 57 */     InputStream input = this.m_streams.get();
/* 58 */     if (null != input)
/*    */     {
/* 60 */       input.close();
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
/*    */   public int read() throws IOException {
/* 74 */     InputStream input = this.m_streams.get();
/* 75 */     if (null != input)
/*    */     {
/* 77 */       return input.read();
/*    */     }
/*    */ 
/*    */     
/* 81 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\DemuxInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */