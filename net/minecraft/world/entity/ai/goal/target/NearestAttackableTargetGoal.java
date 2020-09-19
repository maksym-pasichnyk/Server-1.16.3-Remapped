/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class NearestAttackableTargetGoal<T extends LivingEntity>
/*    */   extends TargetGoal {
/*    */   protected final Class<T> targetType;
/*    */   protected final int randomInterval;
/*    */   protected LivingEntity target;
/*    */   protected TargetingConditions targetConditions;
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob debug1, Class<T> debug2, boolean debug3) {
/* 22 */     this(debug1, debug2, debug3, false);
/*    */   }
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob debug1, Class<T> debug2, boolean debug3, boolean debug4) {
/* 26 */     this(debug1, debug2, 10, debug3, debug4, (Predicate<LivingEntity>)null);
/*    */   }
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob debug1, Class<T> debug2, int debug3, boolean debug4, boolean debug5, @Nullable Predicate<LivingEntity> debug6) {
/* 30 */     super(debug1, debug4, debug5);
/* 31 */     this.targetType = debug2;
/* 32 */     this.randomInterval = debug3;
/* 33 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */     
/* 35 */     this.targetConditions = (new TargetingConditions()).range(getFollowDistance()).selector(debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 40 */     if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     findTarget();
/* 45 */     return (this.target != null);
/*    */   }
/*    */   
/*    */   protected AABB getTargetSearchArea(double debug1) {
/* 49 */     return this.mob.getBoundingBox().inflate(debug1, 4.0D, debug1);
/*    */   }
/*    */   
/*    */   protected void findTarget() {
/* 53 */     if (this.targetType == Player.class || this.targetType == ServerPlayer.class) {
/* 54 */       this.target = (LivingEntity)this.mob.level.getNearestPlayer(this.targetConditions, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*    */     } else {
/* 56 */       this.target = this.mob.level.getNearestLoadedEntity(this.targetType, this.targetConditions, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), getTargetSearchArea(getFollowDistance()));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 62 */     this.mob.setTarget(this.target);
/* 63 */     super.start();
/*    */   }
/*    */   
/*    */   public void setTarget(@Nullable LivingEntity debug1) {
/* 67 */     this.target = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NearestAttackableTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */