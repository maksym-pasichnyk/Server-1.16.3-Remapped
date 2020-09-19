/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class NearestAttackableWitchTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*    */   private boolean canAttack;
/*    */   
/*    */   public NearestAttackableWitchTargetGoal(Raider debug1, Class<T> debug2, int debug3, boolean debug4, boolean debug5, @Nullable Predicate<LivingEntity> debug6) {
/* 13 */     super((Mob)debug1, debug2, debug3, debug4, debug5, debug6);
/* 14 */     this.canAttack = true;
/*    */   }
/*    */   
/*    */   public void setCanAttack(boolean debug1) {
/* 18 */     this.canAttack = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     return (this.canAttack && super.canUse());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NearestAttackableWitchTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */