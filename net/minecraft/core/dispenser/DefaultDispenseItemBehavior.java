/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.BlockSource;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DefaultDispenseItemBehavior implements DispenseItemBehavior {
/*    */   public final ItemStack dispense(BlockSource debug1, ItemStack debug2) {
/* 15 */     ItemStack debug3 = execute(debug1, debug2);
/*    */     
/* 17 */     playSound(debug1);
/* 18 */     playAnimation(debug1, (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/*    */     
/* 20 */     return debug3;
/*    */   }
/*    */   
/*    */   protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 24 */     Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 25 */     Position debug4 = DispenserBlock.getDispensePosition(debug1);
/*    */     
/* 27 */     ItemStack debug5 = debug2.split(1);
/*    */     
/* 29 */     spawnItem((Level)debug1.getLevel(), debug5, 6, debug3, debug4);
/*    */     
/* 31 */     return debug2;
/*    */   }
/*    */   
/*    */   public static void spawnItem(Level debug0, ItemStack debug1, int debug2, Direction debug3, Position debug4) {
/* 35 */     double debug5 = debug4.x();
/* 36 */     double debug7 = debug4.y();
/* 37 */     double debug9 = debug4.z();
/*    */     
/* 39 */     if (debug3.getAxis() == Direction.Axis.Y) {
/*    */       
/* 41 */       debug7 -= 0.125D;
/*    */     } else {
/*    */       
/* 44 */       debug7 -= 0.15625D;
/*    */     } 
/*    */     
/* 47 */     ItemEntity debug11 = new ItemEntity(debug0, debug5, debug7, debug9, debug1);
/*    */     
/* 49 */     double debug12 = debug0.random.nextDouble() * 0.1D + 0.2D;
/* 50 */     debug11.setDeltaMovement(debug0.random
/* 51 */         .nextGaussian() * 0.007499999832361937D * debug2 + debug3.getStepX() * debug12, debug0.random
/* 52 */         .nextGaussian() * 0.007499999832361937D * debug2 + 0.20000000298023224D, debug0.random
/* 53 */         .nextGaussian() * 0.007499999832361937D * debug2 + debug3.getStepZ() * debug12);
/*    */ 
/*    */     
/* 56 */     debug0.addFreshEntity((Entity)debug11);
/*    */   }
/*    */   
/*    */   protected void playSound(BlockSource debug1) {
/* 60 */     debug1.getLevel().levelEvent(1000, debug1.getPos(), 0);
/*    */   }
/*    */   
/*    */   protected void playAnimation(BlockSource debug1, Direction debug2) {
/* 64 */     debug1.getLevel().levelEvent(2000, debug1.getPos(), debug2.get3DDataValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\DefaultDispenseItemBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */