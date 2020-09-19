/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ 
/*     */ public class GiveGiftToHero
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private static final Map<VillagerProfession, ResourceLocation> gifts;
/*     */   
/*     */   static {
/*  39 */     gifts = (Map<VillagerProfession, ResourceLocation>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put(VillagerProfession.ARMORER, BuiltInLootTables.ARMORER_GIFT);
/*     */           debug0.put(VillagerProfession.BUTCHER, BuiltInLootTables.BUTCHER_GIFT);
/*     */           debug0.put(VillagerProfession.CARTOGRAPHER, BuiltInLootTables.CARTOGRAPHER_GIFT);
/*     */           debug0.put(VillagerProfession.CLERIC, BuiltInLootTables.CLERIC_GIFT);
/*     */           debug0.put(VillagerProfession.FARMER, BuiltInLootTables.FARMER_GIFT);
/*     */           debug0.put(VillagerProfession.FISHERMAN, BuiltInLootTables.FISHERMAN_GIFT);
/*     */           debug0.put(VillagerProfession.FLETCHER, BuiltInLootTables.FLETCHER_GIFT);
/*     */           debug0.put(VillagerProfession.LEATHERWORKER, BuiltInLootTables.LEATHERWORKER_GIFT);
/*     */           debug0.put(VillagerProfession.LIBRARIAN, BuiltInLootTables.LIBRARIAN_GIFT);
/*     */           debug0.put(VillagerProfession.MASON, BuiltInLootTables.MASON_GIFT);
/*     */           debug0.put(VillagerProfession.SHEPHERD, BuiltInLootTables.SHEPHERD_GIFT);
/*     */           debug0.put(VillagerProfession.TOOLSMITH, BuiltInLootTables.TOOLSMITH_GIFT);
/*     */           debug0.put(VillagerProfession.WEAPONSMITH, BuiltInLootTables.WEAPONSMITH_GIFT);
/*     */         });
/*     */   }
/*     */   
/*  56 */   private int timeUntilNextGift = 600;
/*     */   private boolean giftGivenDuringThisRun;
/*     */   private long timeSinceStart;
/*     */   
/*     */   public GiveGiftToHero(int debug1) {
/*  61 */     super(
/*  62 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT), debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/*  74 */     if (!isHeroVisible(debug2)) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     if (this.timeUntilNextGift > 0) {
/*  79 */       this.timeUntilNextGift--;
/*  80 */       return false;
/*     */     } 
/*     */     
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  88 */     this.giftGivenDuringThisRun = false;
/*  89 */     this.timeSinceStart = debug3;
/*  90 */     Player debug5 = getNearestTargetableHero(debug2).get();
/*  91 */     debug2.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, debug5);
/*  92 */     BehaviorUtils.lookAtEntity((LivingEntity)debug2, (LivingEntity)debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/*  97 */     return (isHeroVisible(debug2) && !this.giftGivenDuringThisRun);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 102 */     Player debug5 = getNearestTargetableHero(debug2).get();
/* 103 */     BehaviorUtils.lookAtEntity((LivingEntity)debug2, (LivingEntity)debug5);
/*     */     
/* 105 */     if (isWithinThrowingDistance(debug2, debug5)) {
/* 106 */       if (debug3 - this.timeSinceStart > 20L) {
/* 107 */         throwGift(debug2, (LivingEntity)debug5);
/* 108 */         this.giftGivenDuringThisRun = true;
/*     */       } 
/*     */     } else {
/* 111 */       BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug2, (Entity)debug5, 0.5F, 5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/* 117 */     this.timeUntilNextGift = calculateTimeUntilNextGift(debug1);
/* 118 */     debug2.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/* 119 */     debug2.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 120 */     debug2.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*     */   }
/*     */   
/*     */   private void throwGift(Villager debug1, LivingEntity debug2) {
/* 124 */     List<ItemStack> debug3 = getItemToThrow(debug1);
/* 125 */     for (ItemStack debug5 : debug3) {
/* 126 */       BehaviorUtils.throwItem((LivingEntity)debug1, debug5, debug2.position());
/*     */     }
/*     */   }
/*     */   
/*     */   private List<ItemStack> getItemToThrow(Villager debug1) {
/* 131 */     if (debug1.isBaby()) {
/* 132 */       return (List<ItemStack>)ImmutableList.of(new ItemStack((ItemLike)Items.POPPY));
/*     */     }
/*     */     
/* 135 */     VillagerProfession debug2 = debug1.getVillagerData().getProfession();
/* 136 */     if (gifts.containsKey(debug2)) {
/* 137 */       LootTable debug3 = debug1.level.getServer().getLootTables().get(gifts.get(debug2));
/*     */ 
/*     */ 
/*     */       
/* 141 */       LootContext.Builder debug4 = (new LootContext.Builder((ServerLevel)debug1.level)).withParameter(LootContextParams.ORIGIN, debug1.position()).withParameter(LootContextParams.THIS_ENTITY, debug1).withRandom(debug1.getRandom());
/*     */       
/* 143 */       return debug3.getRandomItems(debug4.create(LootContextParamSets.GIFT));
/*     */     } 
/*     */     
/* 146 */     return (List<ItemStack>)ImmutableList.of(new ItemStack((ItemLike)Items.WHEAT_SEEDS));
/*     */   }
/*     */   
/*     */   private boolean isHeroVisible(Villager debug1) {
/* 150 */     return getNearestTargetableHero(debug1).isPresent();
/*     */   }
/*     */   
/*     */   private Optional<Player> getNearestTargetableHero(Villager debug1) {
/* 154 */     return debug1.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER)
/* 155 */       .filter(this::isHero);
/*     */   }
/*     */   
/*     */   private boolean isHero(Player debug1) {
/* 159 */     return debug1.hasEffect(MobEffects.HERO_OF_THE_VILLAGE);
/*     */   }
/*     */   
/*     */   private boolean isWithinThrowingDistance(Villager debug1, Player debug2) {
/* 163 */     BlockPos debug3 = debug2.blockPosition();
/* 164 */     BlockPos debug4 = debug1.blockPosition();
/* 165 */     return debug4.closerThan((Vec3i)debug3, 5.0D);
/*     */   }
/*     */   
/*     */   private static int calculateTimeUntilNextGift(ServerLevel debug0) {
/* 169 */     return 600 + debug0.random.nextInt(6001);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GiveGiftToHero.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */