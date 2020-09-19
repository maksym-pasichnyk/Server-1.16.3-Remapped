/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ public interface BooleanOp {
/*    */   static {
/*  5 */     NOT_OR = ((debug0, debug1) -> (!debug0 && !debug1));
/*  6 */     ONLY_SECOND = ((debug0, debug1) -> (debug1 && !debug0));
/*  7 */     NOT_FIRST = ((debug0, debug1) -> !debug0);
/*  8 */     ONLY_FIRST = ((debug0, debug1) -> (debug0 && !debug1));
/*  9 */     NOT_SECOND = ((debug0, debug1) -> !debug1);
/* 10 */     NOT_SAME = ((debug0, debug1) -> (debug0 != debug1));
/* 11 */     NOT_AND = ((debug0, debug1) -> (!debug0 || !debug1));
/* 12 */     AND = ((debug0, debug1) -> (debug0 && debug1));
/* 13 */     SAME = ((debug0, debug1) -> (debug0 == debug1));
/* 14 */     SECOND = ((debug0, debug1) -> debug1);
/* 15 */     CAUSES = ((debug0, debug1) -> (!debug0 || debug1));
/* 16 */     FIRST = ((debug0, debug1) -> debug0);
/* 17 */     CAUSED_BY = ((debug0, debug1) -> (debug0 || !debug1));
/* 18 */     OR = ((debug0, debug1) -> (debug0 || debug1));
/*    */   }
/*    */   
/*    */   public static final BooleanOp FALSE = (debug0, debug1) -> false;
/*    */   public static final BooleanOp NOT_OR;
/*    */   public static final BooleanOp ONLY_SECOND;
/*    */   public static final BooleanOp NOT_FIRST;
/*    */   public static final BooleanOp ONLY_FIRST;
/*    */   public static final BooleanOp NOT_SECOND;
/*    */   public static final BooleanOp NOT_SAME;
/*    */   public static final BooleanOp NOT_AND;
/*    */   public static final BooleanOp AND;
/*    */   public static final BooleanOp SAME;
/*    */   public static final BooleanOp SECOND;
/*    */   public static final BooleanOp CAUSES;
/*    */   public static final BooleanOp FIRST;
/*    */   public static final BooleanOp CAUSED_BY;
/*    */   public static final BooleanOp OR;
/*    */   public static final BooleanOp TRUE = (debug0, debug1) -> true;
/*    */   
/*    */   boolean apply(boolean paramBoolean1, boolean paramBoolean2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\BooleanOp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */