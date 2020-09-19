/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.animal.AbstractFish;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FishBucketItem
/*    */   extends BucketItem
/*    */ {
/*    */   private final EntityType<?> type;
/*    */   
/*    */   public FishBucketItem(EntityType<?> debug1, Fluid debug2, Item.Properties debug3) {
/* 30 */     super(debug2, debug3);
/* 31 */     this.type = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkExtraContent(Level debug1, ItemStack debug2, BlockPos debug3) {
/* 36 */     if (debug1 instanceof ServerLevel) {
/* 37 */       spawn((ServerLevel)debug1, debug2, debug3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playEmptySound(@Nullable Player debug1, LevelAccessor debug2, BlockPos debug3) {
/* 43 */     debug2.playSound(debug1, debug3, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   private void spawn(ServerLevel debug1, ItemStack debug2, BlockPos debug3) {
/* 47 */     Entity debug4 = this.type.spawn(debug1, debug2, null, debug3, MobSpawnType.BUCKET, true, false);
/*    */     
/* 49 */     if (debug4 != null)
/* 50 */       ((AbstractFish)debug4).setFromBucket(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\FishBucketItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */