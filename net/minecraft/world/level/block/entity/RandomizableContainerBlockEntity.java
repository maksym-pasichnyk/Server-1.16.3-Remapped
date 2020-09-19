/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public abstract class RandomizableContainerBlockEntity
/*     */   extends BaseContainerBlockEntity
/*     */ {
/*     */   @Nullable
/*     */   protected ResourceLocation lootTable;
/*     */   protected long lootTableSeed;
/*     */   
/*     */   protected RandomizableContainerBlockEntity(BlockEntityType<?> debug1) {
/*  35 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static void setLootTable(BlockGetter debug0, Random debug1, BlockPos debug2, ResourceLocation debug3) {
/*  39 */     BlockEntity debug4 = debug0.getBlockEntity(debug2);
/*  40 */     if (debug4 instanceof RandomizableContainerBlockEntity) {
/*  41 */       ((RandomizableContainerBlockEntity)debug4).setLootTable(debug3, debug1.nextLong());
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean tryLoadLootTable(CompoundTag debug1) {
/*  46 */     if (debug1.contains("LootTable", 8)) {
/*  47 */       this.lootTable = new ResourceLocation(debug1.getString("LootTable"));
/*  48 */       this.lootTableSeed = debug1.getLong("LootTableSeed");
/*  49 */       return true;
/*     */     } 
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean trySaveLootTable(CompoundTag debug1) {
/*  55 */     if (this.lootTable == null) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     debug1.putString("LootTable", this.lootTable.toString());
/*  60 */     if (this.lootTableSeed != 0L) {
/*  61 */       debug1.putLong("LootTableSeed", this.lootTableSeed);
/*     */     }
/*  63 */     return true;
/*     */   }
/*     */   
/*     */   public void unpackLootTable(@Nullable Player debug1) {
/*  67 */     if (this.lootTable != null && this.level.getServer() != null) {
/*  68 */       LootTable debug2 = this.level.getServer().getLootTables().get(this.lootTable);
/*  69 */       if (debug1 instanceof ServerPlayer) {
/*  70 */         CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)debug1, this.lootTable);
/*     */       }
/*  72 */       this.lootTable = null;
/*     */ 
/*     */       
/*  75 */       LootContext.Builder debug3 = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
/*     */       
/*  77 */       if (debug1 != null) {
/*  78 */         debug3.withLuck(debug1.getLuck()).withParameter(LootContextParams.THIS_ENTITY, debug1);
/*     */       }
/*     */       
/*  81 */       debug2.fill(this, debug3.create(LootContextParamSets.CHEST));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceLocation debug1, long debug2) {
/*  86 */     this.lootTable = debug1;
/*  87 */     this.lootTableSeed = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  92 */     unpackLootTable((Player)null);
/*  93 */     return getItems().stream().allMatch(ItemStack::isEmpty);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/*  98 */     unpackLootTable((Player)null);
/*  99 */     return (ItemStack)getItems().get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/* 104 */     unpackLootTable((Player)null);
/*     */     
/* 106 */     ItemStack debug3 = ContainerHelper.removeItem((List)getItems(), debug1, debug2);
/* 107 */     if (!debug3.isEmpty()) {
/* 108 */       setChanged();
/*     */     }
/* 110 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/* 115 */     unpackLootTable((Player)null);
/*     */     
/* 117 */     return ContainerHelper.takeItem((List)getItems(), debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 122 */     unpackLootTable((Player)null);
/* 123 */     getItems().set(debug1, debug2);
/* 124 */     if (debug2.getCount() > getMaxStackSize()) {
/* 125 */       debug2.setCount(getMaxStackSize());
/*     */     }
/* 127 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 132 */     if (this.level.getBlockEntity(this.worldPosition) != this) {
/* 133 */       return false;
/*     */     }
/* 135 */     if (debug1.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) > 64.0D) {
/* 136 */       return false;
/*     */     }
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 143 */     getItems().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract NonNullList<ItemStack> getItems();
/*     */   
/*     */   protected abstract void setItems(NonNullList<ItemStack> paramNonNullList);
/*     */   
/*     */   public boolean canOpen(Player debug1) {
/* 152 */     return (super.canOpen(debug1) && (this.lootTable == null || !debug1.isSpectator()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 158 */     if (canOpen(debug3)) {
/* 159 */       unpackLootTable(debug2.player);
/* 160 */       return createMenu(debug1, debug2);
/*     */     } 
/* 162 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\RandomizableContainerBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */