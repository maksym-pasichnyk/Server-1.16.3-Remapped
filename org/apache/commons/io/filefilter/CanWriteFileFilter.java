/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
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
/*    */ public class CanWriteFileFilter
/*    */   extends AbstractFileFilter
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5132005214688990379L;
/* 60 */   public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();
/*    */ 
/*    */   
/* 63 */   public static final IOFileFilter CANNOT_WRITE = new NotFileFilter(CAN_WRITE);
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
/*    */   public boolean accept(File file) {
/* 80 */     return file.canWrite();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\CanWriteFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */