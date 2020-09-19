/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShowTradesToPlayer
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   @Nullable
/*     */   private ItemStack playerItemStack;
/*  27 */   private final List<ItemStack> displayItems = Lists.newArrayList();
/*     */   private int cycleCounter;
/*     */   private int displayIndex;
/*     */   private int lookTime;
/*     */   
/*     */   public ShowTradesToPlayer(int debug1, int debug2) {
/*  33 */     super(
/*  34 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT), debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/*  44 */     Brain<?> debug3 = debug2.getBrain();
/*  45 */     if (!debug3.getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
/*  46 */       return false;
/*     */     }
/*     */     
/*  49 */     LivingEntity debug4 = debug3.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  50 */     return (debug4.getType() == EntityType.PLAYER && debug2
/*  51 */       .isAlive() && debug4
/*  52 */       .isAlive() && 
/*  53 */       !debug2.isBaby() && debug2
/*  54 */       .distanceToSqr((Entity)debug4) <= 17.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/*  59 */     return (checkExtraStartConditions(debug1, debug2) && this.lookTime > 0 && debug2
/*     */       
/*  61 */       .getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  66 */     super.start(debug1, debug2, debug3);
/*  67 */     lookAtTarget(debug2);
/*     */     
/*  69 */     this.cycleCounter = 0;
/*  70 */     this.displayIndex = 0;
/*  71 */     this.lookTime = 40;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(ServerLevel debug1, Villager debug2, long debug3) {
/*  76 */     LivingEntity debug5 = lookAtTarget(debug2);
/*     */     
/*  78 */     findItemsToDisplay(debug5, debug2);
/*  79 */     if (!this.displayItems.isEmpty()) {
/*  80 */       displayCyclingItems(debug2);
/*     */     } else {
/*  82 */       debug2.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*  83 */       this.lookTime = Math.min(this.lookTime, 40);
/*     */     } 
/*     */     
/*  86 */     this.lookTime--;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(ServerLevel debug1, Villager debug2, long debug3) {
/*  91 */     super.stop(debug1, debug2, debug3);
/*  92 */     debug2.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*     */     
/*  94 */     debug2.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*  95 */     this.playerItemStack = null;
/*     */   }
/*     */   
/*     */   private void findItemsToDisplay(LivingEntity debug1, Villager debug2) {
/*  99 */     boolean debug3 = false;
/* 100 */     ItemStack debug4 = debug1.getMainHandItem();
/* 101 */     if (this.playerItemStack == null || !ItemStack.isSame(this.playerItemStack, debug4)) {
/* 102 */       this.playerItemStack = debug4;
/* 103 */       debug3 = true;
/* 104 */       this.displayItems.clear();
/*     */     } 
/*     */     
/* 107 */     if (debug3 && !this.playerItemStack.isEmpty()) {
/* 108 */       updateDisplayItems(debug2);
/* 109 */       if (!this.displayItems.isEmpty()) {
/* 110 */         this.lookTime = 900;
/* 111 */         displayFirstItem(debug2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void displayFirstItem(Villager debug1) {
/* 117 */     debug1.setItemSlot(EquipmentSlot.MAINHAND, this.displayItems.get(0));
/*     */   }
/*     */   
/*     */   private void updateDisplayItems(Villager debug1) {
/* 121 */     for (MerchantOffer debug3 : debug1.getOffers()) {
/* 122 */       if (!debug3.isOutOfStock() && playerItemStackMatchesCostOfOffer(debug3)) {
/* 123 */         this.displayItems.add(debug3.getResult());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean playerItemStackMatchesCostOfOffer(MerchantOffer debug1) {
/* 129 */     return (ItemStack.isSame(this.playerItemStack, debug1.getCostA()) || ItemStack.isSame(this.playerItemStack, debug1.getCostB()));
/*     */   }
/*     */   
/*     */   private LivingEntity lookAtTarget(Villager debug1) {
/* 133 */     Brain<?> debug2 = debug1.getBrain();
/*     */     
/* 135 */     LivingEntity debug3 = debug2.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */     
/* 137 */     debug2.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)debug3, true));
/* 138 */     return debug3;
/*     */   }
/*     */   
/*     */   private void displayCyclingItems(Villager debug1) {
/* 142 */     if (this.displayItems.size() >= 2 && ++this.cycleCounter >= 40) {
/* 143 */       this.displayIndex++;
/* 144 */       this.cycleCounter = 0;
/* 145 */       if (this.displayIndex > this.displayItems.size() - 1) {
/* 146 */         this.displayIndex = 0;
/*     */       }
/* 148 */       debug1.setItemSlot(EquipmentSlot.MAINHAND, this.displayItems.get(this.displayIndex));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ShowTradesToPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */