/*   */ package net.minecraft.world.entity.ai.util;
/*   */ 
/*   */ import net.minecraft.world.entity.Mob;
/*   */ 
/*   */ 
/*   */ public class GoalUtils
/*   */ {
/*   */   public static boolean hasGroundPathNavigation(Mob debug0) {
/* 9 */     return debug0.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*   */   }
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\a\\util\GoalUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */