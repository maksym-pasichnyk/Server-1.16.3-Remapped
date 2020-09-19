/*    */ package net.minecraft.world.entity.schedule;
/*    */ 
/*    */ public class Keyframe {
/*    */   private final int timeStamp;
/*    */   private final float value;
/*    */   
/*    */   public Keyframe(int debug1, float debug2) {
/*  8 */     this.timeStamp = debug1;
/*  9 */     this.value = debug2;
/*    */   }
/*    */   
/*    */   public int getTimeStamp() {
/* 13 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 17 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\schedule\Keyframe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */