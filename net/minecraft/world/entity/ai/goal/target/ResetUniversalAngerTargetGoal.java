/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.NeutralMob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResetUniversalAngerTargetGoal<T extends Mob & NeutralMob>
/*    */   extends Goal
/*    */ {
/*    */   private final T mob;
/*    */   private final boolean alertOthersOfSameType;
/*    */   private int lastHurtByPlayerTimestamp;
/*    */   
/*    */   public ResetUniversalAngerTargetGoal(T debug1, boolean debug2) {
/* 32 */     this.mob = debug1;
/* 33 */     this.alertOthersOfSameType = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 38 */     return (((Mob)this.mob).level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && wasHurtByPlayer());
/*    */   }
/*    */   
/*    */   private boolean wasHurtByPlayer() {
/* 42 */     return (this.mob.getLastHurtByMob() != null && this.mob
/* 43 */       .getLastHurtByMob().getType() == EntityType.PLAYER && this.mob
/* 44 */       .getLastHurtByMobTimestamp() > this.lastHurtByPlayerTimestamp);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 49 */     this.lastHurtByPlayerTimestamp = this.mob.getLastHurtByMobTimestamp();
/* 50 */     ((NeutralMob)this.mob).forgetCurrentTargetAndRefreshUniversalAnger();
/* 51 */     if (this.alertOthersOfSameType) {
/* 52 */       getNearbyMobsOfSameType().stream()
/* 53 */         .filter(debug1 -> (debug1 != this.mob))
/* 54 */         .map(debug0 -> (NeutralMob)debug0)
/* 55 */         .forEach(NeutralMob::forgetCurrentTargetAndRefreshUniversalAnger);
/*    */     }
/* 57 */     super.start();
/*    */   }
/*    */   
/*    */   private List<Mob> getNearbyMobsOfSameType() {
/* 61 */     double debug1 = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 62 */     AABB debug3 = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(debug1, 10.0D, debug1);
/* 63 */     return ((Mob)this.mob).level.getLoadedEntitiesOfClass(this.mob.getClass(), debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\ResetUniversalAngerTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */