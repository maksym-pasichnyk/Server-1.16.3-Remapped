/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class EndCrystalItem
/*    */   extends Item {
/*    */   public EndCrystalItem(Item.Properties debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 24 */     Level debug2 = debug1.getLevel();
/* 25 */     BlockPos debug3 = debug1.getClickedPos();
/*    */     
/* 27 */     BlockState debug4 = debug2.getBlockState(debug3);
/* 28 */     if (!debug4.is(Blocks.OBSIDIAN) && !debug4.is(Blocks.BEDROCK)) {
/* 29 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 32 */     BlockPos debug5 = debug3.above();
/* 33 */     if (!debug2.isEmptyBlock(debug5)) {
/* 34 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 37 */     double debug6 = debug5.getX();
/* 38 */     double debug8 = debug5.getY();
/* 39 */     double debug10 = debug5.getZ();
/*    */     
/* 41 */     List<Entity> debug12 = debug2.getEntities(null, new AABB(debug6, debug8, debug10, debug6 + 1.0D, debug8 + 2.0D, debug10 + 1.0D));
/* 42 */     if (!debug12.isEmpty()) {
/* 43 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 46 */     if (debug2 instanceof ServerLevel) {
/* 47 */       EndCrystal debug13 = new EndCrystal(debug2, debug6 + 0.5D, debug8, debug10 + 0.5D);
/* 48 */       debug13.setShowBottom(false);
/* 49 */       debug2.addFreshEntity((Entity)debug13);
/*    */       
/* 51 */       EndDragonFight debug14 = ((ServerLevel)debug2).dragonFight();
/*    */       
/* 53 */       if (debug14 != null) {
/* 54 */         debug14.tryRespawn();
/*    */       }
/*    */     } 
/* 57 */     debug1.getItemInHand().shrink(1);
/* 58 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFoil(ItemStack debug1) {
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\EndCrystalItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */