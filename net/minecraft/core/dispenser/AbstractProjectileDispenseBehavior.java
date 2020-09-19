/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.BlockSource;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class AbstractProjectileDispenseBehavior extends DefaultDispenseItemBehavior {
/*    */   public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 16 */     ServerLevel serverLevel = debug1.getLevel();
/* 17 */     Position debug4 = DispenserBlock.getDispensePosition(debug1);
/* 18 */     Direction debug5 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/*    */     
/* 20 */     Projectile debug6 = getProjectile((Level)serverLevel, debug4, debug2);
/* 21 */     debug6.shoot(debug5.getStepX(), (debug5.getStepY() + 0.1F), debug5.getStepZ(), getPower(), getUncertainty());
/* 22 */     serverLevel.addFreshEntity((Entity)debug6);
/*    */     
/* 24 */     debug2.shrink(1);
/*    */     
/* 26 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource debug1) {
/* 31 */     debug1.getLevel().levelEvent(1002, debug1.getPos(), 0);
/*    */   }
/*    */   
/*    */   protected abstract Projectile getProjectile(Level paramLevel, Position paramPosition, ItemStack paramItemStack);
/*    */   
/*    */   protected float getUncertainty() {
/* 37 */     return 6.0F;
/*    */   }
/*    */   
/*    */   protected float getPower() {
/* 41 */     return 1.1F;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\AbstractProjectileDispenseBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */