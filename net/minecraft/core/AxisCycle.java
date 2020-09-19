/*    */ package net.minecraft.core;
/*    */ 
/*    */ public enum AxisCycle {
/*  4 */   NONE
/*    */   {
/*    */     public int cycle(int debug1, int debug2, int debug3, Direction.Axis debug4) {
/*  7 */       return debug4.choose(debug1, debug2, debug3);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis debug1) {
/* 17 */       return debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public AxisCycle inverse() {
/* 22 */       return this;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */   
/* 28 */   FORWARD
/*    */   {
/*    */     public int cycle(int debug1, int debug2, int debug3, Direction.Axis debug4) {
/* 31 */       return debug4.choose(debug3, debug1, debug2);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis debug1) {
/* 41 */       return AXIS_VALUES[Math.floorMod(debug1.ordinal() + 1, 3)];
/*    */     }
/*    */ 
/*    */     
/*    */     public AxisCycle inverse() {
/* 46 */       return BACKWARD;
/*    */     }
/*    */   },
/* 49 */   BACKWARD
/*    */   {
/*    */     public int cycle(int debug1, int debug2, int debug3, Direction.Axis debug4) {
/* 52 */       return debug4.choose(debug2, debug3, debug1);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis debug1) {
/* 62 */       return AXIS_VALUES[Math.floorMod(debug1.ordinal() - 1, 3)];
/*    */     }
/*    */ 
/*    */     
/*    */     public AxisCycle inverse() {
/* 67 */       return FORWARD;
/*    */     }
/*    */   };
/*    */   
/*    */   static {
/* 72 */     AXIS_VALUES = Direction.Axis.values();
/* 73 */     VALUES = values();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Direction.Axis[] AXIS_VALUES;
/*    */ 
/*    */ 
/*    */   
/*    */   public static final AxisCycle[] VALUES;
/*    */ 
/*    */ 
/*    */   
/*    */   public static AxisCycle between(Direction.Axis debug0, Direction.Axis debug1) {
/* 88 */     return VALUES[Math.floorMod(debug1.ordinal() - debug0.ordinal(), 3)];
/*    */   }
/*    */   
/*    */   public abstract int cycle(int paramInt1, int paramInt2, int paramInt3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract Direction.Axis cycle(Direction.Axis paramAxis);
/*    */   
/*    */   public abstract AxisCycle inverse();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\AxisCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */