/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CropBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class HarvestFarmland
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   @Nullable
/*     */   private BlockPos aboveFarmlandPos;
/*     */   private long nextOkStartTime;
/*     */   private int timeWorkedSoFar;
/*  37 */   private final List<BlockPos> validFarmlandAroundVillager = Lists.newArrayList();
/*     */   
/*     */   public HarvestFarmland() {
/*  40 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/*  49 */     if (!debug1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/*  50 */       return false;
/*     */     }
/*     */     
/*  53 */     if (debug2.getVillagerData().getProfession() != VillagerProfession.FARMER) {
/*  54 */       return false;
/*     */     }
/*     */     
/*  57 */     BlockPos.MutableBlockPos debug3 = debug2.blockPosition().mutable();
/*     */     
/*  59 */     this.validFarmlandAroundVillager.clear();
/*  60 */     for (int debug4 = -1; debug4 <= 1; debug4++) {
/*  61 */       for (int debug5 = -1; debug5 <= 1; debug5++) {
/*  62 */         for (int debug6 = -1; debug6 <= 1; debug6++) {
/*  63 */           debug3.set(debug2.getX() + debug4, debug2.getY() + debug5, debug2.getZ() + debug6);
/*  64 */           if (validPos((BlockPos)debug3, debug1)) {
/*  65 */             this.validFarmlandAroundVillager.add(new BlockPos((Vec3i)debug3));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     this.aboveFarmlandPos = getValidFarmland(debug1);
/*  72 */     return (this.aboveFarmlandPos != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockPos getValidFarmland(ServerLevel debug1) {
/*  77 */     return this.validFarmlandAroundVillager.isEmpty() ? null : this.validFarmlandAroundVillager.get(debug1.getRandom().nextInt(this.validFarmlandAroundVillager.size()));
/*     */   }
/*     */   
/*     */   private boolean validPos(BlockPos debug1, ServerLevel debug2) {
/*  81 */     BlockState debug3 = debug2.getBlockState(debug1);
/*  82 */     Block debug4 = debug3.getBlock();
/*  83 */     Block debug5 = debug2.getBlockState(debug1.below()).getBlock();
/*  84 */     return ((debug4 instanceof CropBlock && ((CropBlock)debug4).isMaxAge(debug3)) || (debug3
/*  85 */       .isAir() && debug5 instanceof net.minecraft.world.level.block.FarmBlock));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  90 */     if (debug3 > this.nextOkStartTime && this.aboveFarmlandPos != null) {
/*  91 */       debug2.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
/*  92 */       debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/*  98 */     debug2.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*  99 */     debug2.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 100 */     this.timeWorkedSoFar = 0;
/* 101 */     this.nextOkStartTime = debug3 + 40L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 106 */     if (this.aboveFarmlandPos != null && !this.aboveFarmlandPos.closerThan((Position)debug2.position(), 1.0D)) {
/*     */       return;
/*     */     }
/*     */     
/* 110 */     if (this.aboveFarmlandPos != null && debug3 > this.nextOkStartTime) {
/* 111 */       BlockState debug5 = debug1.getBlockState(this.aboveFarmlandPos);
/* 112 */       Block debug6 = debug5.getBlock();
/* 113 */       Block debug7 = debug1.getBlockState(this.aboveFarmlandPos.below()).getBlock();
/*     */       
/* 115 */       if (debug6 instanceof CropBlock && ((CropBlock)debug6).isMaxAge(debug5)) {
/* 116 */         debug1.destroyBlock(this.aboveFarmlandPos, true, (Entity)debug2);
/*     */       }
/*     */       
/* 119 */       if (debug5.isAir() && debug7 instanceof net.minecraft.world.level.block.FarmBlock && debug2.hasFarmSeeds()) {
/* 120 */         SimpleContainer debug8 = debug2.getInventory();
/* 121 */         for (int debug9 = 0; debug9 < debug8.getContainerSize(); debug9++) {
/* 122 */           ItemStack debug10 = debug8.getItem(debug9);
/* 123 */           boolean debug11 = false;
/* 124 */           if (!debug10.isEmpty()) {
/* 125 */             if (debug10.getItem() == Items.WHEAT_SEEDS) {
/* 126 */               debug1.setBlock(this.aboveFarmlandPos, Blocks.WHEAT.defaultBlockState(), 3);
/* 127 */               debug11 = true;
/* 128 */             } else if (debug10.getItem() == Items.POTATO) {
/* 129 */               debug1.setBlock(this.aboveFarmlandPos, Blocks.POTATOES.defaultBlockState(), 3);
/* 130 */               debug11 = true;
/* 131 */             } else if (debug10.getItem() == Items.CARROT) {
/* 132 */               debug1.setBlock(this.aboveFarmlandPos, Blocks.CARROTS.defaultBlockState(), 3);
/* 133 */               debug11 = true;
/* 134 */             } else if (debug10.getItem() == Items.BEETROOT_SEEDS) {
/* 135 */               debug1.setBlock(this.aboveFarmlandPos, Blocks.BEETROOTS.defaultBlockState(), 3);
/* 136 */               debug11 = true;
/*     */             } 
/*     */           }
/* 139 */           if (debug11) {
/* 140 */             debug1.playSound(null, this.aboveFarmlandPos.getX(), this.aboveFarmlandPos.getY(), this.aboveFarmlandPos.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 141 */             debug10.shrink(1);
/* 142 */             if (debug10.isEmpty()) {
/* 143 */               debug8.setItem(debug9, ItemStack.EMPTY);
/*     */             }
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 150 */       if (debug6 instanceof CropBlock && !((CropBlock)debug6).isMaxAge(debug5)) {
/* 151 */         this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
/*     */         
/* 153 */         this.aboveFarmlandPos = getValidFarmland(debug1);
/* 154 */         if (this.aboveFarmlandPos != null) {
/* 155 */           this.nextOkStartTime = debug3 + 20L;
/* 156 */           debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
/* 157 */           debug2.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     this.timeWorkedSoFar++;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/* 167 */     return (this.timeWorkedSoFar < 200);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\HarvestFarmland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */