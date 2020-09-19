/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.BarrelBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BarrelBlockEntity extends RandomizableContainerBlockEntity {
/*  24 */   private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   private int openCount;
/*     */   
/*     */   private BarrelBlockEntity(BlockEntityType<?> debug1) {
/*  28 */     super(debug1);
/*     */   }
/*     */   
/*     */   public BarrelBlockEntity() {
/*  32 */     this(BlockEntityType.BARREL);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  37 */     super.save(debug1);
/*  38 */     if (!trySaveLootTable(debug1)) {
/*  39 */       ContainerHelper.saveAllItems(debug1, this.items);
/*     */     }
/*  41 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  46 */     super.load(debug1, debug2);
/*     */     
/*  48 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  49 */     if (!tryLoadLootTable(debug2)) {
/*  50 */       ContainerHelper.loadAllItems(debug2, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  56 */     return 27;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/*  61 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> debug1) {
/*  66 */     this.items = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/*  71 */     return (Component)new TranslatableComponent("container.barrel");
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/*  76 */     return (AbstractContainerMenu)ChestMenu.threeRows(debug1, debug2, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(Player debug1) {
/*  81 */     if (!debug1.isSpectator()) {
/*  82 */       if (this.openCount < 0) {
/*  83 */         this.openCount = 0;
/*     */       }
/*  85 */       this.openCount++;
/*     */       
/*  87 */       BlockState debug2 = getBlockState();
/*  88 */       boolean debug3 = ((Boolean)debug2.getValue((Property)BarrelBlock.OPEN)).booleanValue();
/*  89 */       if (!debug3) {
/*  90 */         playSound(debug2, SoundEvents.BARREL_OPEN);
/*  91 */         updateBlockState(debug2, true);
/*     */       } 
/*  93 */       scheduleRecheck();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scheduleRecheck() {
/*  98 */     this.level.getBlockTicks().scheduleTick(getBlockPos(), getBlockState().getBlock(), 5);
/*     */   }
/*     */   
/*     */   public void recheckOpen() {
/* 102 */     int debug1 = this.worldPosition.getX();
/* 103 */     int debug2 = this.worldPosition.getY();
/* 104 */     int debug3 = this.worldPosition.getZ();
/*     */     
/* 106 */     this.openCount = ChestBlockEntity.getOpenCount(this.level, this, debug1, debug2, debug3);
/* 107 */     if (this.openCount > 0) {
/* 108 */       scheduleRecheck();
/*     */     } else {
/* 110 */       BlockState debug4 = getBlockState();
/*     */       
/* 112 */       if (!debug4.is(Blocks.BARREL)) {
/* 113 */         setRemoved();
/*     */         
/*     */         return;
/*     */       } 
/* 117 */       boolean debug5 = ((Boolean)debug4.getValue((Property)BarrelBlock.OPEN)).booleanValue();
/* 118 */       if (debug5) {
/* 119 */         playSound(debug4, SoundEvents.BARREL_CLOSE);
/* 120 */         updateBlockState(debug4, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(Player debug1) {
/* 127 */     if (!debug1.isSpectator()) {
/* 128 */       this.openCount--;
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateBlockState(BlockState debug1, boolean debug2) {
/* 133 */     this.level.setBlock(getBlockPos(), (BlockState)debug1.setValue((Property)BarrelBlock.OPEN, Boolean.valueOf(debug2)), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   private void playSound(BlockState debug1, SoundEvent debug2) {
/* 138 */     Vec3i debug3 = ((Direction)debug1.getValue((Property)BarrelBlock.FACING)).getNormal();
/* 139 */     double debug4 = this.worldPosition.getX() + 0.5D + debug3.getX() / 2.0D;
/* 140 */     double debug6 = this.worldPosition.getY() + 0.5D + debug3.getY() / 2.0D;
/* 141 */     double debug8 = this.worldPosition.getZ() + 0.5D + debug3.getZ() / 2.0D;
/*     */     
/* 143 */     this.level.playSound(null, debug4, debug6, debug8, debug2, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BarrelBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */