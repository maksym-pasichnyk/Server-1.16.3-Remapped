/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ShulkerBoxMenu;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShulkerBoxBlockEntity
/*     */   extends RandomizableContainerBlockEntity
/*     */   implements WorldlyContainer, TickableBlockEntity
/*     */ {
/*  44 */   private static final int[] SLOTS = IntStream.range(0, 27).toArray();
/*     */   
/*  46 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   private int openCount;
/*  48 */   private AnimationStatus animationStatus = AnimationStatus.CLOSED;
/*     */   private float progress;
/*     */   private float progressOld;
/*     */   @Nullable
/*     */   private DyeColor color;
/*     */   private boolean loadColorFromBlock;
/*     */   
/*     */   public ShulkerBoxBlockEntity(@Nullable DyeColor debug1) {
/*  56 */     super(BlockEntityType.SHULKER_BOX);
/*  57 */     this.color = debug1;
/*     */   }
/*     */   
/*     */   public ShulkerBoxBlockEntity() {
/*  61 */     this((DyeColor)null);
/*  62 */     this.loadColorFromBlock = true;
/*     */   }
/*     */   
/*     */   public enum AnimationStatus {
/*  66 */     CLOSED,
/*  67 */     OPENING,
/*  68 */     OPENED,
/*  69 */     CLOSING;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  74 */     updateAnimation();
/*  75 */     if (this.animationStatus == AnimationStatus.OPENING || this.animationStatus == AnimationStatus.CLOSING) {
/*  76 */       moveCollidedEntities();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateAnimation() {
/*  81 */     this.progressOld = this.progress;
/*  82 */     switch (this.animationStatus) {
/*     */       case X:
/*  84 */         this.progress = 0.0F;
/*     */         break;
/*     */       case Y:
/*  87 */         this.progress += 0.1F;
/*  88 */         if (this.progress >= 1.0F) {
/*  89 */           moveCollidedEntities();
/*  90 */           this.animationStatus = AnimationStatus.OPENED;
/*  91 */           this.progress = 1.0F;
/*  92 */           doNeighborUpdates();
/*     */         } 
/*     */         break;
/*     */       case Z:
/*  96 */         this.progress -= 0.1F;
/*  97 */         if (this.progress <= 0.0F) {
/*  98 */           this.animationStatus = AnimationStatus.CLOSED;
/*  99 */           this.progress = 0.0F;
/* 100 */           doNeighborUpdates();
/*     */         } 
/*     */         break;
/*     */       case null:
/* 104 */         this.progress = 1.0F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public AnimationStatus getAnimationStatus() {
/* 110 */     return this.animationStatus;
/*     */   }
/*     */   
/*     */   public AABB getBoundingBox(BlockState debug1) {
/* 114 */     return getBoundingBox((Direction)debug1.getValue((Property)ShulkerBoxBlock.FACING));
/*     */   }
/*     */   
/*     */   public AABB getBoundingBox(Direction debug1) {
/* 118 */     float debug2 = getProgress(1.0F);
/* 119 */     return Shapes.block().bounds().expandTowards((0.5F * debug2 * debug1
/* 120 */         .getStepX()), (0.5F * debug2 * debug1
/* 121 */         .getStepY()), (0.5F * debug2 * debug1
/* 122 */         .getStepZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   private AABB getTopBoundingBox(Direction debug1) {
/* 127 */     Direction debug2 = debug1.getOpposite();
/* 128 */     return getBoundingBox(debug1).contract(debug2
/* 129 */         .getStepX(), debug2
/* 130 */         .getStepY(), debug2
/* 131 */         .getStepZ());
/*     */   }
/*     */ 
/*     */   
/*     */   private void moveCollidedEntities() {
/* 136 */     BlockState debug1 = this.level.getBlockState(getBlockPos());
/* 137 */     if (!(debug1.getBlock() instanceof ShulkerBoxBlock)) {
/*     */       return;
/*     */     }
/*     */     
/* 141 */     Direction debug2 = (Direction)debug1.getValue((Property)ShulkerBoxBlock.FACING);
/* 142 */     AABB debug3 = getTopBoundingBox(debug2).move(this.worldPosition);
/*     */     
/* 144 */     List<Entity> debug4 = this.level.getEntities(null, debug3);
/* 145 */     if (debug4.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 149 */     for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/* 150 */       Entity debug6 = debug4.get(debug5);
/* 151 */       if (debug6.getPistonPushReaction() != PushReaction.IGNORE) {
/*     */ 
/*     */ 
/*     */         
/* 155 */         double debug7 = 0.0D;
/* 156 */         double debug9 = 0.0D;
/* 157 */         double debug11 = 0.0D;
/* 158 */         AABB debug13 = debug6.getBoundingBox();
/*     */         
/* 160 */         switch (debug2.getAxis()) {
/*     */           case X:
/* 162 */             if (debug2.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
/* 163 */               debug7 = debug3.maxX - debug13.minX;
/*     */             } else {
/* 165 */               debug7 = debug13.maxX - debug3.minX;
/*     */             } 
/* 167 */             debug7 += 0.01D;
/*     */             break;
/*     */           case Y:
/* 170 */             if (debug2.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
/* 171 */               debug9 = debug3.maxY - debug13.minY;
/*     */             } else {
/* 173 */               debug9 = debug13.maxY - debug3.minY;
/*     */             } 
/* 175 */             debug9 += 0.01D;
/*     */             break;
/*     */           case Z:
/* 178 */             if (debug2.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
/* 179 */               debug11 = debug3.maxZ - debug13.minZ;
/*     */             } else {
/* 181 */               debug11 = debug13.maxZ - debug3.minZ;
/*     */             } 
/* 183 */             debug11 += 0.01D;
/*     */             break;
/*     */         } 
/*     */         
/* 187 */         debug6.move(MoverType.SHULKER_BOX, new Vec3(debug7 * debug2.getStepX(), debug9 * debug2.getStepY(), debug11 * debug2.getStepZ()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getContainerSize() {
/* 193 */     return this.itemStacks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int debug1, int debug2) {
/* 198 */     if (debug1 == 1) {
/* 199 */       this.openCount = debug2;
/* 200 */       if (debug2 == 0) {
/* 201 */         this.animationStatus = AnimationStatus.CLOSING;
/* 202 */         doNeighborUpdates();
/*     */       } 
/* 204 */       if (debug2 == 1) {
/* 205 */         this.animationStatus = AnimationStatus.OPENING;
/* 206 */         doNeighborUpdates();
/*     */       } 
/* 208 */       return true;
/*     */     } 
/*     */     
/* 211 */     return super.triggerEvent(debug1, debug2);
/*     */   }
/*     */   
/*     */   private void doNeighborUpdates() {
/* 215 */     getBlockState().updateNeighbourShapes((LevelAccessor)getLevel(), getBlockPos(), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(Player debug1) {
/* 220 */     if (!debug1.isSpectator()) {
/* 221 */       if (this.openCount < 0) {
/* 222 */         this.openCount = 0;
/*     */       }
/* 224 */       this.openCount++;
/* 225 */       this.level.blockEvent(this.worldPosition, getBlockState().getBlock(), 1, this.openCount);
/* 226 */       if (this.openCount == 1) {
/* 227 */         this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(Player debug1) {
/* 234 */     if (!debug1.isSpectator()) {
/* 235 */       this.openCount--;
/* 236 */       this.level.blockEvent(this.worldPosition, getBlockState().getBlock(), 1, this.openCount);
/* 237 */       if (this.openCount <= 0) {
/* 238 */         this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/* 245 */     return (Component)new TranslatableComponent("container.shulkerBox");
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 250 */     super.load(debug1, debug2);
/* 251 */     loadFromTag(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 256 */     super.save(debug1);
/* 257 */     return saveToTag(debug1);
/*     */   }
/*     */   
/*     */   public void loadFromTag(CompoundTag debug1) {
/* 261 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 262 */     if (!tryLoadLootTable(debug1) && 
/* 263 */       debug1.contains("Items", 9)) {
/* 264 */       ContainerHelper.loadAllItems(debug1, this.itemStacks);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag saveToTag(CompoundTag debug1) {
/* 270 */     if (!trySaveLootTable(debug1)) {
/* 271 */       ContainerHelper.saveAllItems(debug1, this.itemStacks, false);
/*     */     }
/* 273 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/* 278 */     return this.itemStacks;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> debug1) {
/* 283 */     this.itemStacks = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(Direction debug1) {
/* 288 */     return SLOTS;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 293 */     return !(Block.byItem(debug2.getItem()) instanceof ShulkerBoxBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemThroughFace(int debug1, ItemStack debug2, Direction debug3) {
/* 298 */     return true;
/*     */   }
/*     */   
/*     */   public float getProgress(float debug1) {
/* 302 */     return Mth.lerp(debug1, this.progressOld, this.progress);
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
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 316 */     return (AbstractContainerMenu)new ShulkerBoxMenu(debug1, debug2, this);
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 320 */     return (this.animationStatus == AnimationStatus.CLOSED);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\ShulkerBoxBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */