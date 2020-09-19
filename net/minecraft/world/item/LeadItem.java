/*    */ package net.minecraft.world.item;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class LeadItem extends Item {
/*    */   public LeadItem(Item.Properties debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 23 */     Level debug2 = debug1.getLevel();
/* 24 */     BlockPos debug3 = debug1.getClickedPos();
/*    */     
/* 26 */     Block debug4 = debug2.getBlockState(debug3).getBlock();
/* 27 */     if (debug4.is((Tag)BlockTags.FENCES)) {
/* 28 */       Player debug5 = debug1.getPlayer();
/* 29 */       if (!debug2.isClientSide && debug5 != null) {
/* 30 */         bindPlayerMobs(debug5, debug2, debug3);
/*    */       }
/* 32 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */     } 
/*    */     
/* 35 */     return InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static InteractionResult bindPlayerMobs(Player debug0, Level debug1, BlockPos debug2) {
/* 39 */     LeashFenceKnotEntity debug3 = null;
/*    */ 
/*    */     
/* 42 */     boolean debug4 = false;
/* 43 */     double debug5 = 7.0D;
/* 44 */     int debug7 = debug2.getX();
/* 45 */     int debug8 = debug2.getY();
/* 46 */     int debug9 = debug2.getZ();
/*    */     
/* 48 */     List<Mob> debug10 = debug1.getEntitiesOfClass(Mob.class, new AABB(debug7 - 7.0D, debug8 - 7.0D, debug9 - 7.0D, debug7 + 7.0D, debug8 + 7.0D, debug9 + 7.0D));
/* 49 */     for (Mob debug12 : debug10) {
/* 50 */       if (debug12.getLeashHolder() == debug0) {
/* 51 */         if (debug3 == null) {
/* 52 */           debug3 = LeashFenceKnotEntity.getOrCreateKnot(debug1, debug2);
/*    */         }
/* 54 */         debug12.setLeashedTo((Entity)debug3, true);
/* 55 */         debug4 = true;
/*    */       } 
/*    */     } 
/* 58 */     return debug4 ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\LeadItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */