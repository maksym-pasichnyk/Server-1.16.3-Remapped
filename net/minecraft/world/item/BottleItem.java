/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.alchemy.PotionUtils;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class BottleItem
/*    */   extends Item {
/*    */   public BottleItem(Item.Properties debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 29 */     List<AreaEffectCloud> debug4 = debug1.getEntitiesOfClass(AreaEffectCloud.class, debug2.getBoundingBox().inflate(2.0D), debug0 -> (debug0 != null && debug0.isAlive() && debug0.getOwner() instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon));
/*    */     
/* 31 */     ItemStack debug5 = debug2.getItemInHand(debug3);
/*    */     
/* 33 */     if (!debug4.isEmpty()) {
/* 34 */       AreaEffectCloud debug6 = debug4.get(0);
/* 35 */       debug6.setRadius(debug6.getRadius() - 0.5F);
/*    */       
/* 37 */       debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 38 */       return InteractionResultHolder.sidedSuccess(turnBottleIntoItem(debug5, debug2, new ItemStack(Items.DRAGON_BREATH)), debug1.isClientSide());
/*    */     } 
/*    */     
/* 41 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(debug1, debug2, ClipContext.Fluid.SOURCE_ONLY);
/* 42 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 43 */       return InteractionResultHolder.pass(debug5);
/*    */     }
/*    */     
/* 46 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/* 47 */       BlockPos debug7 = blockHitResult.getBlockPos();
/*    */       
/* 49 */       if (!debug1.mayInteract(debug2, debug7)) {
/* 50 */         return InteractionResultHolder.pass(debug5);
/*    */       }
/* 52 */       if (debug1.getFluidState(debug7).is((Tag)FluidTags.WATER)) {
/* 53 */         debug1.playSound(debug2, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 54 */         return InteractionResultHolder.sidedSuccess(turnBottleIntoItem(debug5, debug2, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)), debug1.isClientSide());
/*    */       } 
/*    */     } 
/*    */     
/* 58 */     return InteractionResultHolder.pass(debug5);
/*    */   }
/*    */   
/*    */   protected ItemStack turnBottleIntoItem(ItemStack debug1, Player debug2, ItemStack debug3) {
/* 62 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 63 */     return ItemUtils.createFilledResult(debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BottleItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */