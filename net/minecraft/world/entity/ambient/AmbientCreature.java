/*    */ package net.minecraft.world.entity.ambient;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AmbientCreature extends Mob {
/*    */   protected AmbientCreature(EntityType<? extends AmbientCreature> debug1, Level debug2) {
/* 10 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeLeashed(Player debug1) {
/* 15 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ambient\AmbientCreature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */