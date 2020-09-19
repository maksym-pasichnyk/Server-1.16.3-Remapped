/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Vec2
/*    */ {
/*  7 */   public static final Vec2 ZERO = new Vec2(0.0F, 0.0F);
/*  8 */   public static final Vec2 ONE = new Vec2(1.0F, 1.0F);
/*  9 */   public static final Vec2 UNIT_X = new Vec2(1.0F, 0.0F);
/* 10 */   public static final Vec2 NEG_UNIT_X = new Vec2(-1.0F, 0.0F);
/* 11 */   public static final Vec2 UNIT_Y = new Vec2(0.0F, 1.0F);
/* 12 */   public static final Vec2 NEG_UNIT_Y = new Vec2(0.0F, -1.0F);
/* 13 */   public static final Vec2 MAX = new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);
/* 14 */   public static final Vec2 MIN = new Vec2(Float.MIN_VALUE, Float.MIN_VALUE);
/*    */   
/*    */   public final float x;
/*    */   public final float y;
/*    */   
/*    */   public Vec2(float debug1, float debug2) {
/* 20 */     this.x = debug1;
/* 21 */     this.y = debug2;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Vec2 debug1) {
/* 41 */     return (this.x == debug1.x && this.y == debug1.y);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\Vec2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */