/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemBasedSteering
/*    */ {
/*    */   private final SynchedEntityData entityData;
/*    */   private final EntityDataAccessor<Integer> boostTimeAccessor;
/*    */   private final EntityDataAccessor<Boolean> hasSaddleAccessor;
/*    */   public boolean boosting;
/*    */   public int boostTime;
/*    */   public int boostTimeTotal;
/*    */   
/*    */   public ItemBasedSteering(SynchedEntityData debug1, EntityDataAccessor<Integer> debug2, EntityDataAccessor<Boolean> debug3) {
/* 23 */     this.entityData = debug1;
/* 24 */     this.boostTimeAccessor = debug2;
/* 25 */     this.hasSaddleAccessor = debug3;
/*    */   }
/*    */   
/*    */   public void onSynced() {
/* 29 */     this.boosting = true;
/* 30 */     this.boostTime = 0;
/* 31 */     this.boostTimeTotal = ((Integer)this.entityData.get(this.boostTimeAccessor)).intValue();
/*    */   }
/*    */   
/*    */   public boolean boost(Random debug1) {
/* 35 */     if (this.boosting) {
/* 36 */       return false;
/*    */     }
/* 38 */     this.boosting = true;
/* 39 */     this.boostTime = 0;
/* 40 */     this.boostTimeTotal = debug1.nextInt(841) + 140;
/* 41 */     this.entityData.set(this.boostTimeAccessor, Integer.valueOf(this.boostTimeTotal));
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 46 */     debug1.putBoolean("Saddle", hasSaddle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 51 */     setSaddle(debug1.getBoolean("Saddle"));
/*    */   }
/*    */   
/*    */   public void setSaddle(boolean debug1) {
/* 55 */     this.entityData.set(this.hasSaddleAccessor, Boolean.valueOf(debug1));
/*    */   }
/*    */   
/*    */   public boolean hasSaddle() {
/* 59 */     return ((Boolean)this.entityData.get(this.hasSaddleAccessor)).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ItemBasedSteering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */