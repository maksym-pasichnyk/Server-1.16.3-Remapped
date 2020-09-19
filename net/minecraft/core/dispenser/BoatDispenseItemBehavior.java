/*    */ package net.minecraft.core.dispenser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.BlockSource;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.vehicle.Boat;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ 
/*    */ public class BoatDispenseItemBehavior extends DefaultDispenseItemBehavior {
/* 14 */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*    */   private final Boat.Type type;
/*    */   
/*    */   public BoatDispenseItemBehavior(Boat.Type debug1) {
/* 18 */     this.type = debug1;
/*    */   }
/*    */   
/*    */   public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/*    */     double debug12;
/* 23 */     Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 24 */     ServerLevel serverLevel = debug1.getLevel();
/*    */ 
/*    */     
/* 27 */     double debug5 = debug1.x() + (debug3.getStepX() * 1.125F);
/* 28 */     double debug7 = debug1.y() + (debug3.getStepY() * 1.125F);
/* 29 */     double debug9 = debug1.z() + (debug3.getStepZ() * 1.125F);
/*    */     
/* 31 */     BlockPos debug11 = debug1.getPos().relative(debug3);
/*    */ 
/*    */     
/* 34 */     if (serverLevel.getFluidState(debug11).is((Tag)FluidTags.WATER)) {
/* 35 */       debug12 = 1.0D;
/* 36 */     } else if (serverLevel.getBlockState(debug11).isAir() && serverLevel.getFluidState(debug11.below()).is((Tag)FluidTags.WATER)) {
/* 37 */       debug12 = 0.0D;
/*    */     } else {
/* 39 */       return this.defaultDispenseItemBehavior.dispense(debug1, debug2);
/*    */     } 
/*    */     
/* 42 */     Boat debug14 = new Boat((Level)serverLevel, debug5, debug7 + debug12, debug9);
/* 43 */     debug14.setType(this.type);
/* 44 */     debug14.yRot = debug3.toYRot();
/* 45 */     serverLevel.addFreshEntity((Entity)debug14);
/*    */     
/* 47 */     debug2.shrink(1);
/* 48 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource debug1) {
/* 53 */     debug1.getLevel().levelEvent(1000, debug1.getPos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\BoatDispenseItemBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */