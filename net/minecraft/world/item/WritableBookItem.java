/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.LecternBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WritableBookItem
/*    */   extends Item
/*    */ {
/*    */   public WritableBookItem(Item.Properties debug1) {
/* 22 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 27 */     Level debug2 = debug1.getLevel();
/* 28 */     BlockPos debug3 = debug1.getClickedPos();
/* 29 */     BlockState debug4 = debug2.getBlockState(debug3);
/*    */     
/* 31 */     if (debug4.is(Blocks.LECTERN)) {
/* 32 */       return LecternBlock.tryPlaceBook(debug2, debug3, debug4, debug1.getItemInHand()) ? InteractionResult.sidedSuccess(debug2.isClientSide) : InteractionResult.PASS;
/*    */     }
/*    */     
/* 35 */     return InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 40 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 41 */     debug2.openItemGui(debug4, debug3);
/* 42 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 43 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */   
/*    */   public static boolean makeSureTagIsValid(@Nullable CompoundTag debug0) {
/* 47 */     if (debug0 == null) {
/* 48 */       return false;
/*    */     }
/* 50 */     if (!debug0.contains("pages", 9)) {
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     ListTag debug1 = debug0.getList("pages", 8);
/* 55 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 56 */       String debug3 = debug1.getString(debug2);
/*    */       
/* 58 */       if (debug3.length() > 32767) {
/* 59 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\WritableBookItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */