/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ public class SaveDataDirtyRunnable implements Runnable {
/*    */   private final SavedData savedData;
/*    */   
/*    */   public SaveDataDirtyRunnable(SavedData debug1) {
/*  7 */     this.savedData = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 12 */     this.savedData.setDirty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\SaveDataDirtyRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */