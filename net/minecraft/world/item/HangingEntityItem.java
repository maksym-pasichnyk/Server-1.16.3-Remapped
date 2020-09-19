/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.decoration.ItemFrame;
/*    */ import net.minecraft.world.entity.decoration.Painting;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class HangingEntityItem
/*    */   extends Item {
/*    */   public HangingEntityItem(EntityType<? extends HangingEntity> debug1, Item.Properties debug2) {
/* 19 */     super(debug2);
/* 20 */     this.type = debug1;
/*    */   }
/*    */   private final EntityType<? extends HangingEntity> type;
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/*    */     ItemFrame itemFrame;
/* 25 */     BlockPos debug2 = debug1.getClickedPos();
/* 26 */     Direction debug3 = debug1.getClickedFace();
/*    */     
/* 28 */     BlockPos debug4 = debug2.relative(debug3);
/* 29 */     Player debug5 = debug1.getPlayer();
/* 30 */     ItemStack debug6 = debug1.getItemInHand();
/*    */     
/* 32 */     if (debug5 != null && !mayPlace(debug5, debug3, debug6, debug4)) {
/* 33 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 36 */     Level debug7 = debug1.getLevel();
/*    */     
/* 38 */     if (this.type == EntityType.PAINTING) {
/* 39 */       Painting painting = new Painting(debug7, debug4, debug3);
/* 40 */     } else if (this.type == EntityType.ITEM_FRAME) {
/* 41 */       itemFrame = new ItemFrame(debug7, debug4, debug3);
/*    */     } else {
/* 43 */       return InteractionResult.sidedSuccess(debug7.isClientSide);
/*    */     } 
/*    */     
/* 46 */     CompoundTag debug9 = debug6.getTag();
/* 47 */     if (debug9 != null) {
/* 48 */       EntityType.updateCustomEntityTag(debug7, debug5, (Entity)itemFrame, debug9);
/*    */     }
/*    */     
/* 51 */     if (itemFrame.survives()) {
/* 52 */       if (!debug7.isClientSide) {
/* 53 */         itemFrame.playPlacementSound();
/* 54 */         debug7.addFreshEntity((Entity)itemFrame);
/*    */       } 
/* 56 */       debug6.shrink(1);
/* 57 */       return InteractionResult.sidedSuccess(debug7.isClientSide);
/*    */     } 
/*    */     
/* 60 */     return InteractionResult.CONSUME;
/*    */   }
/*    */   
/*    */   protected boolean mayPlace(Player debug1, Direction debug2, ItemStack debug3, BlockPos debug4) {
/* 64 */     return (!debug2.getAxis().isVertical() && debug1.mayUseItemAt(debug4, debug2, debug3));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\HangingEntityItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */