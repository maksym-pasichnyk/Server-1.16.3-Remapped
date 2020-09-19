/*    */ package net.minecraft.world.item;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Rotations;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.decoration.ArmorStand;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ArmorStandItem extends Item {
/*    */   public ArmorStandItem(Item.Properties debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 29 */     Direction debug2 = debug1.getClickedFace();
/* 30 */     if (debug2 == Direction.DOWN) {
/* 31 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 34 */     Level debug3 = debug1.getLevel();
/* 35 */     BlockPlaceContext debug4 = new BlockPlaceContext(debug1);
/* 36 */     BlockPos debug5 = debug4.getClickedPos();
/*    */     
/* 38 */     ItemStack debug6 = debug1.getItemInHand();
/* 39 */     Vec3 debug7 = Vec3.atBottomCenterOf((Vec3i)debug5);
/* 40 */     AABB debug8 = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(debug7.x(), debug7.y(), debug7.z());
/*    */     
/* 42 */     if (!debug3.noCollision(null, debug8, debug0 -> true) || !debug3.getEntities(null, debug8).isEmpty()) {
/* 43 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 46 */     if (debug3 instanceof ServerLevel) {
/* 47 */       ServerLevel debug9 = (ServerLevel)debug3;
/* 48 */       ArmorStand debug10 = (ArmorStand)EntityType.ARMOR_STAND.create(debug9, debug6.getTag(), null, debug1.getPlayer(), debug5, MobSpawnType.SPAWN_EGG, true, true);
/*    */       
/* 50 */       if (debug10 == null) {
/* 51 */         return InteractionResult.FAIL;
/*    */       }
/*    */       
/* 54 */       debug9.addFreshEntityWithPassengers((Entity)debug10);
/*    */       
/* 56 */       float debug11 = Mth.floor((Mth.wrapDegrees(debug1.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
/* 57 */       debug10.moveTo(debug10.getX(), debug10.getY(), debug10.getZ(), debug11, 0.0F);
/*    */       
/* 59 */       randomizePose(debug10, debug3.random);
/* 60 */       debug3.addFreshEntity((Entity)debug10);
/*    */       
/* 62 */       debug3.playSound(null, debug10.getX(), debug10.getY(), debug10.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
/*    */     } 
/*    */     
/* 65 */     debug6.shrink(1);
/* 66 */     return InteractionResult.sidedSuccess(debug3.isClientSide);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void randomizePose(ArmorStand debug1, Random debug2) {
/* 75 */     Rotations debug3 = debug1.getHeadPose();
/* 76 */     float debug5 = debug2.nextFloat() * 5.0F;
/* 77 */     float debug6 = debug2.nextFloat() * 20.0F - 10.0F;
/* 78 */     Rotations debug4 = new Rotations(debug3.getX() + debug5, debug3.getY() + debug6, debug3.getZ());
/* 79 */     debug1.setHeadPose(debug4);
/*    */     
/* 81 */     debug3 = debug1.getBodyPose();
/* 82 */     debug5 = debug2.nextFloat() * 10.0F - 5.0F;
/* 83 */     debug4 = new Rotations(debug3.getX(), debug3.getY() + debug5, debug3.getZ());
/* 84 */     debug1.setBodyPose(debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ArmorStandItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */