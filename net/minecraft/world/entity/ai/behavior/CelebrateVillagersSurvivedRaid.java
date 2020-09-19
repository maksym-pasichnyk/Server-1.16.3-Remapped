/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.FireworkRocketItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class CelebrateVillagersSurvivedRaid extends Behavior<Villager> {
/*    */   public CelebrateVillagersSurvivedRaid(int debug1, int debug2) {
/* 27 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), debug1, debug2);
/*    */   }
/*    */   @Nullable
/*    */   private Raid currentRaid;
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 32 */     BlockPos debug3 = debug2.blockPosition();
/* 33 */     this.currentRaid = debug1.getRaidAt(debug3);
/* 34 */     return (this.currentRaid != null && this.currentRaid.isVictory() && MoveToSkySeeingSpot.hasNoBlocksAbove(debug1, (LivingEntity)debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/* 39 */     return (this.currentRaid != null && !this.currentRaid.isStopped());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/* 44 */     this.currentRaid = null;
/* 45 */     debug2.getBrain().updateActivityFromSchedule(debug1.getDayTime(), debug1.getGameTime());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 50 */     Random debug5 = debug2.getRandom();
/*    */     
/* 52 */     if (debug5.nextInt(100) == 0) {
/* 53 */       debug2.playCelebrateSound();
/*    */     }
/*    */     
/* 56 */     if (debug5.nextInt(200) == 0 && MoveToSkySeeingSpot.hasNoBlocksAbove(debug1, (LivingEntity)debug2, debug2.blockPosition())) {
/* 57 */       DyeColor debug6 = (DyeColor)Util.getRandom((Object[])DyeColor.values(), debug5);
/* 58 */       int debug7 = debug5.nextInt(3);
/* 59 */       ItemStack debug8 = getFirework(debug6, debug7);
/*    */       
/* 61 */       FireworkRocketEntity debug9 = new FireworkRocketEntity(debug2.level, (Entity)debug2, debug2.getX(), debug2.getEyeY(), debug2.getZ(), debug8);
/* 62 */       debug2.level.addFreshEntity((Entity)debug9);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private ItemStack getFirework(DyeColor debug1, int debug2) {
/* 68 */     ItemStack debug3 = new ItemStack((ItemLike)Items.FIREWORK_ROCKET, 1);
/*    */     
/* 70 */     ItemStack debug4 = new ItemStack((ItemLike)Items.FIREWORK_STAR);
/* 71 */     CompoundTag debug5 = debug4.getOrCreateTagElement("Explosion");
/*    */     
/* 73 */     List<Integer> debug6 = Lists.newArrayList();
/* 74 */     debug6.add(Integer.valueOf(debug1.getFireworkColor()));
/*    */     
/* 76 */     debug5.putIntArray("Colors", debug6);
/* 77 */     debug5.putByte("Type", (byte)FireworkRocketItem.Shape.BURST.getId());
/*    */     
/* 79 */     CompoundTag debug7 = debug3.getOrCreateTagElement("Fireworks");
/* 80 */     ListTag debug8 = new ListTag();
/*    */     
/* 82 */     CompoundTag debug9 = debug4.getTagElement("Explosion");
/* 83 */     if (debug9 != null) {
/* 84 */       debug8.add(debug9);
/*    */     }
/*    */     
/* 87 */     debug7.putByte("Flight", (byte)debug2);
/* 88 */     if (!debug8.isEmpty()) {
/* 89 */       debug7.put("Explosions", (Tag)debug8);
/*    */     }
/*    */     
/* 92 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CelebrateVillagersSurvivedRaid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */