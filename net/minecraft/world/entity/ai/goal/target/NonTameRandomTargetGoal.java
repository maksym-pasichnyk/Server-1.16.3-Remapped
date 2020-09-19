/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ 
/*    */ public class NonTameRandomTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*    */   private final TamableAnimal tamableMob;
/*    */   
/*    */   public NonTameRandomTargetGoal(TamableAnimal debug1, Class<T> debug2, boolean debug3, @Nullable Predicate<LivingEntity> debug4) {
/* 13 */     super((Mob)debug1, debug2, 10, debug3, false, debug4);
/* 14 */     this.tamableMob = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 19 */     return (!this.tamableMob.isTame() && super.canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 24 */     if (this.targetConditions != null) {
/* 25 */       return this.targetConditions.test((LivingEntity)this.mob, this.target);
/*    */     }
/* 27 */     return super.canContinueToUse();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NonTameRandomTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */