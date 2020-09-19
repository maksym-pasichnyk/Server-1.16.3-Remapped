/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ public class TradeWithVillager
/*     */   extends Behavior<Villager>
/*     */ {
/*  26 */   private Set<Item> trades = (Set<Item>)ImmutableSet.of();
/*     */   
/*     */   public TradeWithVillager() {
/*  29 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/*  37 */     return BehaviorUtils.targetIsValid(debug2.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/*  42 */     return checkExtraStartConditions(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  47 */     Villager debug5 = debug2.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*  48 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)debug2, (LivingEntity)debug5, 0.5F);
/*     */     
/*  50 */     this.trades = figureOutWhatIAmWillingToTrade(debug2, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/*  55 */     Villager debug5 = debug2.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */     
/*  57 */     if (debug2.distanceToSqr((Entity)debug5) > 5.0D) {
/*     */       return;
/*     */     }
/*     */     
/*  61 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)debug2, (LivingEntity)debug5, 0.5F);
/*     */     
/*  63 */     debug2.gossip(debug1, debug5, debug3);
/*     */     
/*  65 */     if (debug2.hasExcessFood() && (debug2.getVillagerData().getProfession() == VillagerProfession.FARMER || debug5.wantsMoreFood())) {
/*  66 */       throwHalfStack(debug2, Villager.FOOD_POINTS.keySet(), (LivingEntity)debug5);
/*     */     }
/*     */     
/*  69 */     if (debug5.getVillagerData().getProfession() == VillagerProfession.FARMER && debug2.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getMaxStackSize() / 2) {
/*  70 */       throwHalfStack(debug2, (Set<Item>)ImmutableSet.of(Items.WHEAT), (LivingEntity)debug5);
/*     */     }
/*     */     
/*  73 */     if (!this.trades.isEmpty() && debug2.getInventory().hasAnyOf(this.trades)) {
/*  74 */       throwHalfStack(debug2, this.trades, (LivingEntity)debug5);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/*  80 */     debug2.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<Item> figureOutWhatIAmWillingToTrade(Villager debug0, Villager debug1) {
/*  86 */     ImmutableSet<Item> debug2 = debug1.getVillagerData().getProfession().getRequestedItems();
/*  87 */     ImmutableSet<Item> debug3 = debug0.getVillagerData().getProfession().getRequestedItems();
/*  88 */     return (Set<Item>)debug2.stream().filter(debug1 -> !debug0.contains(debug1)).collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void throwHalfStack(Villager debug0, Set<Item> debug1, LivingEntity debug2) {
/*  95 */     SimpleContainer debug3 = debug0.getInventory();
/*     */     
/*  97 */     ItemStack debug4 = ItemStack.EMPTY;
/*  98 */     for (int debug5 = 0; debug5 < debug3.getContainerSize(); debug5++) {
/*  99 */       ItemStack debug6 = debug3.getItem(debug5);
/* 100 */       if (!debug6.isEmpty()) {
/* 101 */         Item debug7 = debug6.getItem();
/* 102 */         if (debug1.contains(debug7)) {
/*     */           int debug8;
/* 104 */           if (debug6.getCount() > debug6.getMaxStackSize() / 2) {
/* 105 */             debug8 = debug6.getCount() / 2;
/* 106 */           } else if (debug6.getCount() > 24) {
/* 107 */             debug8 = debug6.getCount() - 24;
/*     */           } else {
/*     */             continue;
/*     */           } 
/* 111 */           debug6.shrink(debug8);
/* 112 */           debug4 = new ItemStack((ItemLike)debug7, debug8);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       continue;
/*     */     } 
/* 118 */     if (!debug4.isEmpty())
/* 119 */       BehaviorUtils.throwItem((LivingEntity)debug0, debug4, debug2.position()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TradeWithVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */