/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AvoidEntityGoal<T extends LivingEntity> extends Goal {
/*    */   protected final PathfinderMob mob;
/*    */   private final double walkSpeedModifier;
/*    */   private final double sprintSpeedModifier;
/*    */   protected T toAvoid;
/*    */   protected final float maxDist;
/*    */   protected Path path;
/*    */   protected final PathNavigation pathNav;
/*    */   protected final Class<T> avoidClass;
/*    */   protected final Predicate<LivingEntity> avoidPredicate;
/*    */   protected final Predicate<LivingEntity> predicateOnAvoidEntity;
/*    */   private final TargetingConditions avoidEntityTargeting;
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob debug1, Class<T> debug2, float debug3, double debug4, double debug6) {
/* 29 */     this(debug1, debug2, debug0 -> true, debug3, debug4, debug6, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
/*    */   }
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob debug1, Class<T> debug2, Predicate<LivingEntity> debug3, float debug4, double debug5, double debug7, Predicate<LivingEntity> debug9) {
/* 33 */     this.mob = debug1;
/* 34 */     this.avoidClass = debug2;
/* 35 */     this.avoidPredicate = debug3;
/* 36 */     this.maxDist = debug4;
/* 37 */     this.walkSpeedModifier = debug5;
/* 38 */     this.sprintSpeedModifier = debug7;
/* 39 */     this.predicateOnAvoidEntity = debug9;
/* 40 */     this.pathNav = debug1.getNavigation();
/* 41 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */     
/* 43 */     this.avoidEntityTargeting = (new TargetingConditions()).range(debug4).selector(debug9.and(debug3));
/*    */   }
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob debug1, Class<T> debug2, float debug3, double debug4, double debug6, Predicate<LivingEntity> debug8) {
/* 47 */     this(debug1, debug2, debug0 -> true, debug3, debug4, debug6, debug8);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 52 */     this.toAvoid = (T)this.mob.level.getNearestLoadedEntity(this.avoidClass, this.avoidEntityTargeting, (LivingEntity)this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.maxDist, 3.0D, this.maxDist));
/* 53 */     if (this.toAvoid == null) {
/* 54 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     Vec3 debug1 = RandomPos.getPosAvoid(this.mob, 16, 7, this.toAvoid.position());
/* 61 */     if (debug1 == null) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (this.toAvoid.distanceToSqr(debug1.x, debug1.y, debug1.z) < this.toAvoid.distanceToSqr((Entity)this.mob)) {
/* 65 */       return false;
/*    */     }
/* 67 */     this.path = this.pathNav.createPath(debug1.x, debug1.y, debug1.z, 0);
/* 68 */     return (this.path != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 73 */     return !this.pathNav.isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 78 */     this.pathNav.moveTo(this.path, this.walkSpeedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 83 */     this.toAvoid = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 88 */     if (this.mob.distanceToSqr((Entity)this.toAvoid) < 49.0D) {
/* 89 */       this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
/*    */     } else {
/* 91 */       this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\AvoidEntityGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */