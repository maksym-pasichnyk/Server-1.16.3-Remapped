/*    */ package joptsimple;
/*    */ 
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
/*    */ class UnconfiguredOptionException
/*    */   extends OptionException
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/*    */   
/*    */   UnconfiguredOptionException(String option) {
/* 41 */     this(Collections.singletonList(option));
/*    */   }
/*    */   
/*    */   UnconfiguredOptionException(List<String> options) {
/* 45 */     super(options);
/*    */   }
/*    */ 
/*    */   
/*    */   Object[] messageArguments() {
/* 50 */     return new Object[] { multipleOptionString() };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\joptsimple\UnconfiguredOptionException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */