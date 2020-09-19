/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.BlockSourceImpl;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DropperBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DropperBlock extends DispenserBlock {
/* 18 */   private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = (DispenseItemBehavior)new DefaultDispenseItemBehavior();
/*    */   
/*    */   public DropperBlock(BlockBehaviour.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected DispenseItemBehavior getDispenseMethod(ItemStack debug1) {
/* 26 */     return DISPENSE_BEHAVIOUR;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 31 */     return (BlockEntity)new DropperBlockEntity();
/*    */   }
/*    */   
/*    */   protected void dispenseFrom(ServerLevel debug1, BlockPos debug2) {
/*    */     ItemStack debug9;
/* 36 */     BlockSourceImpl debug3 = new BlockSourceImpl(debug1, debug2);
/* 37 */     DispenserBlockEntity debug4 = (DispenserBlockEntity)debug3.getEntity();
/*    */     
/* 39 */     int debug5 = debug4.getRandomSlot();
/* 40 */     if (debug5 < 0) {
/* 41 */       debug1.levelEvent(1001, debug2, 0);
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     ItemStack debug6 = debug4.getItem(debug5);
/* 46 */     if (debug6.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 50 */     Direction debug7 = (Direction)debug1.getBlockState(debug2).getValue((Property)FACING);
/* 51 */     Container debug8 = HopperBlockEntity.getContainerAt((Level)debug1, debug2.relative(debug7));
/*    */ 
/*    */     
/* 54 */     if (debug8 == null) {
/* 55 */       debug9 = DISPENSE_BEHAVIOUR.dispense((BlockSource)debug3, debug6);
/*    */     } else {
/* 57 */       debug9 = HopperBlockEntity.addItem((Container)debug4, debug8, debug6.copy().split(1), debug7.getOpposite());
/*    */       
/* 59 */       if (debug9.isEmpty()) {
/* 60 */         debug9 = debug6.copy();
/* 61 */         debug9.shrink(1);
/*    */       } else {
/*    */         
/* 64 */         debug9 = debug6.copy();
/*    */       } 
/*    */     } 
/*    */     
/* 68 */     debug4.setItem(debug5, debug9);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DropperBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */