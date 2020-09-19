/*    */ package net.minecraft.advancements;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public interface RequirementsStrategy {
/*    */   static {
/*  6 */     AND = (debug0 -> {
/*    */         String[][] debug1 = new String[debug0.size()][];
/*    */         int debug2 = 0;
/*    */         for (String debug4 : debug0) {
/*    */           (new String[1])[0] = debug4;
/*    */           debug1[debug2++] = new String[1];
/*    */         } 
/*    */         return debug1;
/*    */       });
/* 15 */     OR = (debug0 -> new String[][] { (String[])debug0.toArray((Object[])new String[0]) });
/*    */   }
/*    */   
/*    */   public static final RequirementsStrategy AND;
/*    */   public static final RequirementsStrategy OR;
/*    */   
/*    */   String[][] createRequirements(Collection<String> paramCollection);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\RequirementsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */