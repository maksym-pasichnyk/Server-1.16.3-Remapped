/*    */ package net.minecraft.world.entity.animal.horse;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LightningBolt;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.monster.Skeleton;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public class SkeletonTrapGoal extends Goal {
/*    */   public SkeletonTrapGoal(SkeletonHorse debug1) {
/* 21 */     this.horse = debug1;
/*    */   }
/*    */   private final SkeletonHorse horse;
/*    */   
/*    */   public boolean canUse() {
/* 26 */     return this.horse.level.hasNearbyAlivePlayer(this.horse.getX(), this.horse.getY(), this.horse.getZ(), 10.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 31 */     ServerLevel debug1 = (ServerLevel)this.horse.level;
/* 32 */     DifficultyInstance debug2 = debug1.getCurrentDifficultyAt(this.horse.blockPosition());
/* 33 */     this.horse.setTrap(false);
/* 34 */     this.horse.setTamed(true);
/* 35 */     this.horse.setAge(0);
/* 36 */     LightningBolt debug3 = (LightningBolt)EntityType.LIGHTNING_BOLT.create((Level)debug1);
/* 37 */     debug3.moveTo(this.horse.getX(), this.horse.getY(), this.horse.getZ());
/* 38 */     debug3.setVisualOnly(true);
/* 39 */     debug1.addFreshEntity((Entity)debug3);
/* 40 */     Skeleton debug4 = createSkeleton(debug2, this.horse);
/* 41 */     debug4.startRiding((Entity)this.horse);
/*    */     
/* 43 */     debug1.addFreshEntityWithPassengers((Entity)debug4);
/*    */     
/* 45 */     for (int debug5 = 0; debug5 < 3; debug5++) {
/* 46 */       AbstractHorse debug6 = createHorse(debug2);
/* 47 */       Skeleton debug7 = createSkeleton(debug2, debug6);
/* 48 */       debug7.startRiding((Entity)debug6);
/* 49 */       debug6.push(this.horse.getRandom().nextGaussian() * 0.5D, 0.0D, this.horse.getRandom().nextGaussian() * 0.5D);
/* 50 */       debug1.addFreshEntityWithPassengers((Entity)debug6);
/*    */     } 
/*    */   }
/*    */   
/*    */   private AbstractHorse createHorse(DifficultyInstance debug1) {
/* 55 */     SkeletonHorse debug2 = (SkeletonHorse)EntityType.SKELETON_HORSE.create(this.horse.level);
/* 56 */     debug2.finalizeSpawn((ServerLevelAccessor)this.horse.level, debug1, MobSpawnType.TRIGGERED, (SpawnGroupData)null, (CompoundTag)null);
/* 57 */     debug2.setPos(this.horse.getX(), this.horse.getY(), this.horse.getZ());
/* 58 */     debug2.invulnerableTime = 60;
/* 59 */     debug2.setPersistenceRequired();
/* 60 */     debug2.setTamed(true);
/* 61 */     debug2.setAge(0);
/* 62 */     return debug2;
/*    */   }
/*    */   
/*    */   private Skeleton createSkeleton(DifficultyInstance debug1, AbstractHorse debug2) {
/* 66 */     Skeleton debug3 = (Skeleton)EntityType.SKELETON.create(debug2.level);
/* 67 */     debug3.finalizeSpawn((ServerLevelAccessor)debug2.level, debug1, MobSpawnType.TRIGGERED, null, null);
/* 68 */     debug3.setPos(debug2.getX(), debug2.getY(), debug2.getZ());
/* 69 */     debug3.invulnerableTime = 60;
/* 70 */     debug3.setPersistenceRequired();
/*    */     
/* 72 */     if (debug3.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 73 */       debug3.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)Items.IRON_HELMET));
/*    */     }
/*    */     
/* 76 */     debug3.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(debug3.getRandom(), disenchant(debug3.getMainHandItem()), (int)(5.0F + debug1.getSpecialMultiplier() * debug3.getRandom().nextInt(18)), false));
/* 77 */     debug3.setItemSlot(EquipmentSlot.HEAD, EnchantmentHelper.enchantItem(debug3.getRandom(), disenchant(debug3.getItemBySlot(EquipmentSlot.HEAD)), (int)(5.0F + debug1.getSpecialMultiplier() * debug3.getRandom().nextInt(18)), false));
/*    */     
/* 79 */     return debug3;
/*    */   }
/*    */   
/*    */   private ItemStack disenchant(ItemStack debug1) {
/* 83 */     debug1.removeTagKey("Enchantments");
/* 84 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\SkeletonTrapGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */