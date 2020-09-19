/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.vehicle.Boat;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BoatItem extends Item {
/* 20 */   private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
/*    */   
/*    */   private final Boat.Type type;
/*    */   
/*    */   public BoatItem(Boat.Type debug1, Item.Properties debug2) {
/* 25 */     super(debug2);
/* 26 */     this.type = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 31 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/*    */     
/* 33 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(debug1, debug2, ClipContext.Fluid.ANY);
/* 34 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 35 */       return InteractionResultHolder.pass(debug4);
/*    */     }
/*    */ 
/*    */     
/* 39 */     Vec3 debug6 = debug2.getViewVector(1.0F);
/* 40 */     double debug7 = 5.0D;
/* 41 */     List<Entity> debug9 = debug1.getEntities((Entity)debug2, debug2.getBoundingBox().expandTowards(debug6.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
/* 42 */     if (!debug9.isEmpty()) {
/* 43 */       Vec3 debug10 = debug2.getEyePosition(1.0F);
/* 44 */       for (Entity debug12 : debug9) {
/* 45 */         AABB debug13 = debug12.getBoundingBox().inflate(debug12.getPickRadius());
/* 46 */         if (debug13.contains(debug10)) {
/* 47 */           return InteractionResultHolder.pass(debug4);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/* 53 */       Boat debug10 = new Boat(debug1, (blockHitResult.getLocation()).x, (blockHitResult.getLocation()).y, (blockHitResult.getLocation()).z);
/* 54 */       debug10.setType(this.type);
/* 55 */       debug10.yRot = debug2.yRot;
/* 56 */       if (!debug1.noCollision((Entity)debug10, debug10.getBoundingBox().inflate(-0.1D))) {
/* 57 */         return InteractionResultHolder.fail(debug4);
/*    */       }
/* 59 */       if (!debug1.isClientSide) {
/* 60 */         debug1.addFreshEntity((Entity)debug10);
/* 61 */         if (!debug2.abilities.instabuild) {
/* 62 */           debug4.shrink(1);
/*    */         }
/*    */       } 
/* 65 */       debug2.awardStat(Stats.ITEM_USED.get(this));
/* 66 */       return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */     } 
/*    */     
/* 69 */     return InteractionResultHolder.pass(debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BoatItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */