/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InteractGoal
/*    */   extends LookAtPlayerGoal
/*    */ {
/*    */   public InteractGoal(Mob debug1, Class<? extends LivingEntity> debug2, float debug3, float debug4) {
/* 15 */     super(debug1, debug2, debug3, debug4);
/* 16 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\InteractGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */