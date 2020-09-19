/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class ChestBlockEntity
/*     */   extends RandomizableContainerBlockEntity
/*     */   implements TickableBlockEntity {
/*  33 */   private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   
/*     */   protected float openness;
/*     */   protected float oOpenness;
/*     */   protected int openCount;
/*     */   private int tickInterval;
/*     */   
/*     */   protected ChestBlockEntity(BlockEntityType<?> debug1) {
/*  41 */     super(debug1);
/*     */   }
/*     */   
/*     */   public ChestBlockEntity() {
/*  45 */     this(BlockEntityType.CHEST);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  50 */     return 27;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/*  55 */     return (Component)new TranslatableComponent("container.chest");
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  60 */     super.load(debug1, debug2);
/*     */     
/*  62 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  63 */     if (!tryLoadLootTable(debug2)) {
/*  64 */       ContainerHelper.loadAllItems(debug2, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  70 */     super.save(debug1);
/*     */     
/*  72 */     if (!trySaveLootTable(debug1)) {
/*  73 */       ContainerHelper.saveAllItems(debug1, this.items);
/*     */     }
/*  75 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  80 */     int debug1 = this.worldPosition.getX();
/*  81 */     int debug2 = this.worldPosition.getY();
/*  82 */     int debug3 = this.worldPosition.getZ();
/*     */     
/*  84 */     this.tickInterval++;
/*     */     
/*  86 */     this.openCount = getOpenCount(this.level, this, this.tickInterval, debug1, debug2, debug3, this.openCount);
/*     */     
/*  88 */     this.oOpenness = this.openness;
/*     */     
/*  90 */     float debug4 = 0.1F;
/*  91 */     if (this.openCount > 0 && this.openness == 0.0F) {
/*  92 */       playSound(SoundEvents.CHEST_OPEN);
/*     */     }
/*  94 */     if ((this.openCount == 0 && this.openness > 0.0F) || (this.openCount > 0 && this.openness < 1.0F)) {
/*  95 */       float debug5 = this.openness;
/*  96 */       if (this.openCount > 0) {
/*  97 */         this.openness += 0.1F;
/*     */       } else {
/*  99 */         this.openness -= 0.1F;
/*     */       } 
/* 101 */       if (this.openness > 1.0F) {
/* 102 */         this.openness = 1.0F;
/*     */       }
/* 104 */       float debug6 = 0.5F;
/* 105 */       if (this.openness < 0.5F && debug5 >= 0.5F) {
/* 106 */         playSound(SoundEvents.CHEST_CLOSE);
/*     */       }
/* 108 */       if (this.openness < 0.0F) {
/* 109 */         this.openness = 0.0F;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getOpenCount(Level debug0, BaseContainerBlockEntity debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 115 */     if (!debug0.isClientSide && debug6 != 0 && (debug2 + debug3 + debug4 + debug5) % 200 == 0) {
/* 116 */       debug6 = getOpenCount(debug0, debug1, debug3, debug4, debug5);
/*     */     }
/*     */     
/* 119 */     return debug6;
/*     */   }
/*     */   
/*     */   public static int getOpenCount(Level debug0, BaseContainerBlockEntity debug1, int debug2, int debug3, int debug4) {
/* 123 */     int debug5 = 0;
/*     */     
/* 125 */     float debug6 = 5.0F;
/* 126 */     List<Player> debug7 = debug0.getEntitiesOfClass(Player.class, new AABB((debug2 - 5.0F), (debug3 - 5.0F), (debug4 - 5.0F), ((debug2 + 1) + 5.0F), ((debug3 + 1) + 5.0F), ((debug4 + 1) + 5.0F)));
/* 127 */     for (Player debug9 : debug7) {
/* 128 */       if (debug9.containerMenu instanceof ChestMenu) {
/* 129 */         Container debug10 = ((ChestMenu)debug9.containerMenu).getContainer();
/* 130 */         if (debug10 == debug1 || (debug10 instanceof CompoundContainer && ((CompoundContainer)debug10).contains(debug1))) {
/* 131 */           debug5++;
/*     */         }
/*     */       } 
/*     */     } 
/* 135 */     return debug5;
/*     */   }
/*     */   
/*     */   private void playSound(SoundEvent debug1) {
/* 139 */     ChestType debug2 = (ChestType)getBlockState().getValue((Property)ChestBlock.TYPE);
/* 140 */     if (debug2 == ChestType.LEFT) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 145 */     double debug3 = this.worldPosition.getX() + 0.5D;
/* 146 */     double debug5 = this.worldPosition.getY() + 0.5D;
/* 147 */     double debug7 = this.worldPosition.getZ() + 0.5D;
/*     */     
/* 149 */     if (debug2 == ChestType.RIGHT) {
/* 150 */       Direction debug9 = ChestBlock.getConnectedDirection(getBlockState());
/* 151 */       debug3 += debug9.getStepX() * 0.5D;
/* 152 */       debug7 += debug9.getStepZ() * 0.5D;
/*     */     } 
/*     */     
/* 155 */     this.level.playSound(null, debug3, debug5, debug7, debug1, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int debug1, int debug2) {
/* 160 */     if (debug1 == 1) {
/* 161 */       this.openCount = debug2;
/* 162 */       return true;
/*     */     } 
/* 164 */     return super.triggerEvent(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(Player debug1) {
/* 169 */     if (!debug1.isSpectator()) {
/* 170 */       if (this.openCount < 0) {
/* 171 */         this.openCount = 0;
/*     */       }
/* 173 */       this.openCount++;
/* 174 */       signalOpenCount();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(Player debug1) {
/* 180 */     if (!debug1.isSpectator()) {
/* 181 */       this.openCount--;
/* 182 */       signalOpenCount();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void signalOpenCount() {
/* 187 */     Block debug1 = getBlockState().getBlock();
/* 188 */     if (debug1 instanceof ChestBlock) {
/* 189 */       this.level.blockEvent(this.worldPosition, debug1, 1, this.openCount);
/* 190 */       this.level.updateNeighborsAt(this.worldPosition, debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/* 196 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> debug1) {
/* 201 */     this.items = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getOpenCount(BlockGetter debug0, BlockPos debug1) {
/* 210 */     BlockState debug2 = debug0.getBlockState(debug1);
/* 211 */     if (debug2.getBlock().isEntityBlock()) {
/* 212 */       BlockEntity debug3 = debug0.getBlockEntity(debug1);
/* 213 */       if (debug3 instanceof ChestBlockEntity) {
/* 214 */         return ((ChestBlockEntity)debug3).openCount;
/*     */       }
/*     */     } 
/* 217 */     return 0;
/*     */   }
/*     */   
/*     */   public static void swapContents(ChestBlockEntity debug0, ChestBlockEntity debug1) {
/* 221 */     NonNullList<ItemStack> debug2 = debug0.getItems();
/* 222 */     debug0.setItems(debug1.getItems());
/* 223 */     debug1.setItems(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 228 */     return (AbstractContainerMenu)ChestMenu.threeRows(debug1, debug2, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\ChestBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */