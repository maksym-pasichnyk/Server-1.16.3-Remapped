/*    */ package net.minecraft.world.entity.animal;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*    */ 
/*    */ public abstract class WaterAnimal extends PathfinderMob {
/*    */   protected WaterAnimal(EntityType<? extends WaterAnimal> debug1, Level debug2) {
/* 14 */     super(debug1, debug2);
/*    */     
/* 16 */     setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBreatheUnderwater() {
/* 21 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public MobType getMobType() {
/* 26 */     return MobType.WATER;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 31 */     return debug1.isUnobstructed((Entity)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 36 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getExperienceReward(Player debug1) {
/* 41 */     return 1 + this.level.random.nextInt(3);
/*    */   }
/*    */   
/*    */   protected void handleAirSupply(int debug1) {
/* 45 */     if (isAlive() && !isInWaterOrBubble()) {
/* 46 */       setAirSupply(debug1 - 1);
/* 47 */       if (getAirSupply() == -20) {
/* 48 */         setAirSupply(0);
/* 49 */         hurt(DamageSource.DROWN, 2.0F);
/*    */       } 
/*    */     } else {
/* 52 */       setAirSupply(300);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void baseTick() {
/* 58 */     int debug1 = getAirSupply();
/* 59 */     super.baseTick();
/* 60 */     handleAirSupply(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPushedByFluid() {
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeLeashed(Player debug1) {
/* 71 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\WaterAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */