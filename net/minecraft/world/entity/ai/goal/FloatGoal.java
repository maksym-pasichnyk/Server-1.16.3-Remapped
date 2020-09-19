/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class FloatGoal extends Goal {
/*    */   private final Mob mob;
/*    */   
/*    */   public FloatGoal(Mob debug1) {
/* 12 */     this.mob = debug1;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP));
/* 14 */     debug1.getNavigation().setCanFloat(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 19 */     return ((this.mob.isInWater() && this.mob.getFluidHeight((Tag)FluidTags.WATER) > this.mob.getFluidJumpThreshold()) || this.mob.isInLava());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 24 */     if (this.mob.getRandom().nextFloat() < 0.8F)
/* 25 */       this.mob.getJumpControl().jump(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FloatGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */