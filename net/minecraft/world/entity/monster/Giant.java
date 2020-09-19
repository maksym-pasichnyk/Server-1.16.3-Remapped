/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class Giant extends Monster {
/*    */   public Giant(EntityType<? extends Giant> debug1, Level debug2) {
/* 14 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 19 */     return 10.440001F;
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 23 */     return Monster.createMonsterAttributes()
/* 24 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 25 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/* 26 */       .add(Attributes.ATTACK_DAMAGE, 50.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 31 */     return debug2.getBrightness(debug1) - 0.5F;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Giant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */