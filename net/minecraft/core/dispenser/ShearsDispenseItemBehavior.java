/*    */ package net.minecraft.core.dispenser;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.BlockSource;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Shearable;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BeehiveBlock;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class ShearsDispenseItemBehavior extends OptionalDispenseItemBehavior {
/*    */   protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 25 */     ServerLevel serverLevel = debug1.getLevel();
/* 26 */     if (!serverLevel.isClientSide()) {
/* 27 */       BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/*    */       
/* 29 */       setSuccess((tryShearBeehive(serverLevel, debug4) || tryShearLivingEntity(serverLevel, debug4)));
/* 30 */       if (isSuccess() && debug2.hurt(1, serverLevel.getRandom(), null)) {
/* 31 */         debug2.setCount(0);
/*    */       }
/*    */     } 
/* 34 */     return debug2;
/*    */   }
/*    */   
/*    */   private static boolean tryShearBeehive(ServerLevel debug0, BlockPos debug1) {
/* 38 */     BlockState debug2 = debug0.getBlockState(debug1);
/* 39 */     if (debug2.is((Tag)BlockTags.BEEHIVES)) {
/* 40 */       int debug3 = ((Integer)debug2.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue();
/*    */       
/* 42 */       if (debug3 >= 5) {
/* 43 */         debug0.playSound(null, debug1, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */         
/* 45 */         BeehiveBlock.dropHoneycomb((Level)debug0, debug1);
/* 46 */         ((BeehiveBlock)debug2.getBlock()).releaseBeesAndResetHoneyLevel((Level)debug0, debug2, debug1, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 47 */         return true;
/*    */       } 
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean tryShearLivingEntity(ServerLevel debug0, BlockPos debug1) {
/* 54 */     List<LivingEntity> debug2 = debug0.getEntitiesOfClass(LivingEntity.class, new AABB(debug1), EntitySelector.NO_SPECTATORS);
/* 55 */     for (LivingEntity debug4 : debug2) {
/* 56 */       if (debug4 instanceof Shearable) {
/* 57 */         Shearable debug5 = (Shearable)debug4;
/*    */         
/* 59 */         if (debug5.readyForShearing()) {
/* 60 */           debug5.shear(SoundSource.BLOCKS);
/* 61 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/* 65 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\ShearsDispenseItemBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */