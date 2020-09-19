/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public class CaveSpider
/*    */   extends Spider
/*    */ {
/*    */   public CaveSpider(EntityType<? extends CaveSpider> debug1, Level debug2) {
/* 25 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createCaveSpider() {
/* 29 */     return Spider.createAttributes()
/* 30 */       .add(Attributes.MAX_HEALTH, 12.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doHurtTarget(Entity debug1) {
/* 35 */     if (super.doHurtTarget(debug1)) {
/* 36 */       if (debug1 instanceof LivingEntity) {
/* 37 */         int debug2 = 0;
/* 38 */         if (this.level.getDifficulty() == Difficulty.NORMAL) {
/* 39 */           debug2 = 7;
/* 40 */         } else if (this.level.getDifficulty() == Difficulty.HARD) {
/* 41 */           debug2 = 15;
/*    */         } 
/*    */         
/* 44 */         if (debug2 > 0) {
/* 45 */           ((LivingEntity)debug1).addEffect(new MobEffectInstance(MobEffects.POISON, debug2 * 20, 0));
/*    */         }
/*    */       } 
/*    */       
/* 49 */       return true;
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 58 */     return debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 63 */     return 0.45F;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\CaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */