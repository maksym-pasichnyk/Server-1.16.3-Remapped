/*    */ package net.minecraft.core.dispenser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.BlockSource;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ShulkerBoxDispenseBehavior extends OptionalDispenseItemBehavior {
/*    */   protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 15 */     setSuccess(false);
/*    */     
/* 17 */     Item debug3 = debug2.getItem();
/* 18 */     if (debug3 instanceof BlockItem) {
/* 19 */       Direction debug4 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 20 */       BlockPos debug5 = debug1.getPos().relative(debug4);
/*    */       
/* 22 */       Direction debug6 = debug1.getLevel().isEmptyBlock(debug5.below()) ? debug4 : Direction.UP;
/* 23 */       setSuccess(((BlockItem)debug3).place((BlockPlaceContext)new DirectionalPlaceContext((Level)debug1.getLevel(), debug5, debug4, debug2, debug6)).consumesAction());
/*    */     } 
/* 25 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\ShulkerBoxDispenseBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */