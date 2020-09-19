/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.CropBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class UseBonemeal extends Behavior<Villager> {
/*     */   private long nextWorkCycleTime;
/*     */   private long lastBonemealingSession;
/*     */   private int timeWorkedSoFar;
/*  30 */   private Optional<BlockPos> cropPos = Optional.empty();
/*     */   
/*     */   public UseBonemeal() {
/*  33 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/*  41 */     if (debug2.tickCount % 10 != 0 || (this.lastBonemealingSession != 0L && this.lastBonemealingSession + 160L > debug2.tickCount)) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     if (debug2.getInventory().countItem(Items.BONE_MEAL) <= 0) {
/*  46 */       return false;
/*     */     }
/*  48 */     this.cropPos = pickNextTarget(debug1, debug2);
/*  49 */     return this.cropPos.isPresent();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/*  54 */     return (this.timeWorkedSoFar < 80 && this.cropPos.isPresent());
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> pickNextTarget(ServerLevel debug1, Villager debug2) {
/*  58 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/*  59 */     Optional<BlockPos> debug4 = Optional.empty();
/*  60 */     int debug5 = 0;
/*  61 */     for (int debug6 = -1; debug6 <= 1; debug6++) {
/*  62 */       for (int debug7 = -1; debug7 <= 1; debug7++) {
/*  63 */         for (int debug8 = -1; debug8 <= 1; debug8++) {
/*  64 */           debug3.setWithOffset((Vec3i)debug2.blockPosition(), debug6, debug7, debug8);
/*  65 */           if (validPos((BlockPos)debug3, debug1) && 
/*  66 */             debug1.random.nextInt(++debug5) == 0) {
/*  67 */             debug4 = Optional.of(debug3.immutable());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return debug4;
/*     */   }
/*     */   
/*     */   private boolean validPos(BlockPos debug1, ServerLevel debug2) {
/*  78 */     BlockState debug3 = debug2.getBlockState(debug1);
/*  79 */     Block debug4 = debug3.getBlock();
/*  80 */     return (debug4 instanceof CropBlock && !((CropBlock)debug4).isMaxAge(debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  85 */     setCurrentCropAsTarget(debug2);
/*     */     
/*  87 */     debug2.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.BONE_MEAL));
/*     */     
/*  89 */     this.nextWorkCycleTime = debug3;
/*  90 */     this.timeWorkedSoFar = 0;
/*     */   }
/*     */   
/*     */   private void setCurrentCropAsTarget(Villager debug1) {
/*  94 */     this.cropPos.ifPresent(debug1 -> {
/*     */           BlockPosTracker debug2 = new BlockPosTracker(debug1);
/*     */           debug0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, debug2);
/*     */           debug0.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug2, 0.5F, 1));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/* 103 */     debug2.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 104 */     this.lastBonemealingSession = debug2.tickCount;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 109 */     BlockPos debug5 = this.cropPos.get();
/* 110 */     if (debug3 < this.nextWorkCycleTime || !debug5.closerThan((Position)debug2.position(), 1.0D)) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     ItemStack debug6 = ItemStack.EMPTY;
/* 115 */     SimpleContainer debug7 = debug2.getInventory();
/* 116 */     int debug8 = debug7.getContainerSize();
/* 117 */     for (int debug9 = 0; debug9 < debug8; debug9++) {
/* 118 */       ItemStack debug10 = debug7.getItem(debug9);
/* 119 */       if (debug10.getItem() == Items.BONE_MEAL) {
/* 120 */         debug6 = debug10;
/*     */         break;
/*     */       } 
/*     */     } 
/* 124 */     if (!debug6.isEmpty() && BoneMealItem.growCrop(debug6, (Level)debug1, debug5)) {
/* 125 */       debug1.levelEvent(2005, debug5, 0);
/*     */       
/* 127 */       this.cropPos = pickNextTarget(debug1, debug2);
/* 128 */       setCurrentCropAsTarget(debug2);
/* 129 */       this.nextWorkCycleTime = debug3 + 40L;
/*     */     } 
/*     */     
/* 132 */     this.timeWorkedSoFar++;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\UseBonemeal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */