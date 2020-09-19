/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.MobType;
/*    */ import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.level.Level;
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
/*    */ public abstract class AbstractIllager
/*    */   extends Raider
/*    */ {
/*    */   protected AbstractIllager(EntityType<? extends AbstractIllager> debug1, Level debug2) {
/* 23 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void registerGoals() {
/* 28 */     super.registerGoals();
/*    */   }
/*    */ 
/*    */   
/*    */   public MobType getMobType() {
/* 33 */     return MobType.ILLAGER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public class RaiderOpenDoorGoal
/*    */     extends OpenDoorGoal
/*    */   {
/*    */     public RaiderOpenDoorGoal(Raider debug2) {
/* 42 */       super((Mob)debug2, false);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean canUse() {
/* 47 */       return (super.canUse() && AbstractIllager.this.hasActiveRaid());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\AbstractIllager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */