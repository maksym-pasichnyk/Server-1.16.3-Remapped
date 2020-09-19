/*    */ package net.minecraft.core;
/*    */ 
/*    */ import net.minecraft.nbt.FloatTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ 
/*    */ public class Rotations
/*    */ {
/*    */   protected final float x;
/*    */   protected final float y;
/*    */   protected final float z;
/*    */   
/*    */   public Rotations(float debug1, float debug2, float debug3) {
/* 13 */     this.x = (Float.isInfinite(debug1) || Float.isNaN(debug1)) ? 0.0F : (debug1 % 360.0F);
/* 14 */     this.y = (Float.isInfinite(debug2) || Float.isNaN(debug2)) ? 0.0F : (debug2 % 360.0F);
/* 15 */     this.z = (Float.isInfinite(debug3) || Float.isNaN(debug3)) ? 0.0F : (debug3 % 360.0F);
/*    */   }
/*    */   
/*    */   public Rotations(ListTag debug1) {
/* 19 */     this(debug1.getFloat(0), debug1.getFloat(1), debug1.getFloat(2));
/*    */   }
/*    */   
/*    */   public ListTag save() {
/* 23 */     ListTag debug1 = new ListTag();
/* 24 */     debug1.add(FloatTag.valueOf(this.x));
/* 25 */     debug1.add(FloatTag.valueOf(this.y));
/* 26 */     debug1.add(FloatTag.valueOf(this.z));
/* 27 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 32 */     if (!(debug1 instanceof Rotations)) {
/* 33 */       return false;
/*    */     }
/* 35 */     Rotations debug2 = (Rotations)debug1;
/* 36 */     return (this.x == debug2.x && this.y == debug2.y && this.z == debug2.z);
/*    */   }
/*    */   
/*    */   public float getX() {
/* 40 */     return this.x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 44 */     return this.y;
/*    */   }
/*    */   
/*    */   public float getZ() {
/* 48 */     return this.z;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\Rotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */