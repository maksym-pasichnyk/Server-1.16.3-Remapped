/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.scores.Team;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EntitySelector
/*    */ {
/* 17 */   public static final Predicate<Entity> ENTITY_STILL_ALIVE = Entity::isAlive; public static final Predicate<Entity> ENTITY_NOT_BEING_RIDDEN; public static final Predicate<Entity> CONTAINER_ENTITY_SELECTOR; public static final Predicate<Entity> NO_CREATIVE_OR_SPECTATOR;
/* 18 */   public static final Predicate<LivingEntity> LIVING_ENTITY_STILL_ALIVE = LivingEntity::isAlive; public static final Predicate<Entity> ATTACK_ALLOWED; public static final Predicate<Entity> NO_SPECTATORS; static {
/* 19 */     ENTITY_NOT_BEING_RIDDEN = (debug0 -> (debug0.isAlive() && !debug0.isVehicle() && !debug0.isPassenger()));
/* 20 */     CONTAINER_ENTITY_SELECTOR = (debug0 -> (debug0 instanceof net.minecraft.world.Container && debug0.isAlive()));
/* 21 */     NO_CREATIVE_OR_SPECTATOR = (debug0 -> (!(debug0 instanceof Player) || (!debug0.isSpectator() && !((Player)debug0).isCreative())));
/* 22 */     ATTACK_ALLOWED = (debug0 -> (!(debug0 instanceof Player) || (!debug0.isSpectator() && !((Player)debug0).isCreative() && debug0.level.getDifficulty() != Difficulty.PEACEFUL)));
/* 23 */     NO_SPECTATORS = (debug0 -> !debug0.isSpectator());
/*    */   }
/*    */   
/*    */   public static class MobCanWearArmorEntitySelector implements Predicate<Entity> { private final ItemStack itemStack;
/*    */     
/*    */     public MobCanWearArmorEntitySelector(ItemStack debug1) {
/* 29 */       this.itemStack = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean test(@Nullable Entity debug1) {
/* 34 */       if (!debug1.isAlive()) {
/* 35 */         return false;
/*    */       }
/* 37 */       if (!(debug1 instanceof LivingEntity)) {
/* 38 */         return false;
/*    */       }
/* 40 */       LivingEntity debug2 = (LivingEntity)debug1;
/* 41 */       return debug2.canTakeItem(this.itemStack);
/*    */     } }
/*    */ 
/*    */   
/*    */   public static Predicate<Entity> withinDistance(double debug0, double debug2, double debug4, double debug6) {
/* 46 */     double debug8 = debug6 * debug6;
/* 47 */     return debug8 -> (debug8 != null && debug8.distanceToSqr(debug0, debug2, debug4) <= debug6);
/*    */   }
/*    */   
/*    */   public static Predicate<Entity> pushableBy(Entity debug0) {
/* 51 */     Team debug1 = debug0.getTeam();
/* 52 */     Team.CollisionRule debug2 = (debug1 == null) ? Team.CollisionRule.ALWAYS : debug1.getCollisionRule();
/* 53 */     if (debug2 == Team.CollisionRule.NEVER) {
/* 54 */       return (Predicate<Entity>)Predicates.alwaysFalse();
/*    */     }
/* 56 */     return NO_SPECTATORS.and(debug3 -> {
/*    */           if (!debug3.isPushable()) {
/*    */             return false;
/*    */           }
/*    */           if (debug0.level.isClientSide && (!(debug3 instanceof Player) || !((Player)debug3).isLocalPlayer())) {
/*    */             return false;
/*    */           }
/*    */           Team debug4 = debug3.getTeam();
/*    */           Team.CollisionRule debug5 = (debug4 == null) ? Team.CollisionRule.ALWAYS : debug4.getCollisionRule();
/*    */           if (debug5 == Team.CollisionRule.NEVER) {
/*    */             return false;
/*    */           }
/* 68 */           boolean debug6 = (debug1 != null && debug1.isAlliedTo(debug4));
/* 69 */           return ((debug2 == Team.CollisionRule.PUSH_OWN_TEAM || debug5 == Team.CollisionRule.PUSH_OWN_TEAM) && debug6) ? false : (
/*    */ 
/*    */             
/* 72 */             !((debug2 == Team.CollisionRule.PUSH_OTHER_TEAMS || debug5 == Team.CollisionRule.PUSH_OTHER_TEAMS) && !debug6));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Predicate<Entity> notRiding(Entity debug0) {
/* 80 */     return debug1 -> {
/*    */         while (debug1.isPassenger()) {
/*    */           debug1 = debug1.getVehicle();
/*    */           if (debug1 == debug0)
/*    */             return false; 
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\EntitySelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */