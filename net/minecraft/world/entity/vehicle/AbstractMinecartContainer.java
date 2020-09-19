/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public abstract class AbstractMinecartContainer
/*     */   extends AbstractMinecart
/*     */   implements Container, MenuProvider {
/*  35 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(36, ItemStack.EMPTY);
/*     */   private boolean dropEquipment = true;
/*     */   @Nullable
/*     */   private ResourceLocation lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*     */   protected AbstractMinecartContainer(EntityType<?> debug1, Level debug2) {
/*  42 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected AbstractMinecartContainer(EntityType<?> debug1, double debug2, double debug4, double debug6, Level debug8) {
/*  46 */     super(debug1, debug8, debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(DamageSource debug1) {
/*  51 */     super.destroy(debug1);
/*     */     
/*  53 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/*  54 */       Containers.dropContents(this.level, this, this);
/*     */       
/*  56 */       if (!this.level.isClientSide) {
/*  57 */         Entity debug2 = debug1.getDirectEntity();
/*  58 */         if (debug2 != null && debug2.getType() == EntityType.PLAYER) {
/*  59 */           PiglinAi.angerNearbyPiglins((Player)debug2, true);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  67 */     for (ItemStack debug2 : this.itemStacks) {
/*  68 */       if (!debug2.isEmpty()) {
/*  69 */         return false;
/*     */       }
/*     */     } 
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/*  77 */     unpackLootTable((Player)null);
/*  78 */     return (ItemStack)this.itemStacks.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/*  83 */     unpackLootTable((Player)null);
/*     */     
/*  85 */     return ContainerHelper.removeItem((List)this.itemStacks, debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/*  90 */     unpackLootTable((Player)null);
/*  91 */     ItemStack debug2 = (ItemStack)this.itemStacks.get(debug1);
/*  92 */     if (debug2.isEmpty()) {
/*  93 */       return ItemStack.EMPTY;
/*     */     }
/*  95 */     this.itemStacks.set(debug1, ItemStack.EMPTY);
/*  96 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 101 */     unpackLootTable((Player)null);
/* 102 */     this.itemStacks.set(debug1, debug2);
/* 103 */     if (!debug2.isEmpty() && debug2.getCount() > getMaxStackSize()) {
/* 104 */       debug2.setCount(getMaxStackSize());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 110 */     if (debug1 >= 0 && debug1 < getContainerSize()) {
/* 111 */       setItem(debug1, debug2);
/* 112 */       return true;
/*     */     } 
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 124 */     if (this.removed) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (debug1.distanceToSqr(this) > 64.0D) {
/* 128 */       return false;
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity changeDimension(ServerLevel debug1) {
/* 136 */     this.dropEquipment = false;
/* 137 */     return super.changeDimension(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 142 */     if (!this.level.isClientSide && this.dropEquipment) {
/* 143 */       Containers.dropContents(this.level, this, this);
/*     */     }
/*     */     
/* 146 */     super.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 156 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 158 */     if (this.lootTable != null) {
/* 159 */       debug1.putString("LootTable", this.lootTable.toString());
/* 160 */       if (this.lootTableSeed != 0L) {
/* 161 */         debug1.putLong("LootTableSeed", this.lootTableSeed);
/*     */       }
/*     */     } else {
/* 164 */       ContainerHelper.saveAllItems(debug1, this.itemStacks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 170 */     super.readAdditionalSaveData(debug1);
/* 171 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*     */     
/* 173 */     if (debug1.contains("LootTable", 8)) {
/* 174 */       this.lootTable = new ResourceLocation(debug1.getString("LootTable"));
/* 175 */       this.lootTableSeed = debug1.getLong("LootTableSeed");
/*     */     } else {
/* 177 */       ContainerHelper.loadAllItems(debug1, this.itemStacks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 183 */     debug1.openMenu(this);
/* 184 */     if (!debug1.level.isClientSide) {
/* 185 */       PiglinAi.angerNearbyPiglins(debug1, true);
/* 186 */       return InteractionResult.CONSUME;
/*     */     } 
/* 188 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyNaturalSlowdown() {
/* 193 */     float debug1 = 0.98F;
/*     */     
/* 195 */     if (this.lootTable == null) {
/* 196 */       int debug2 = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer(this);
/* 197 */       debug1 += debug2 * 0.001F;
/*     */     } 
/*     */     
/* 200 */     setDeltaMovement(getDeltaMovement().multiply(debug1, 0.0D, debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unpackLootTable(@Nullable Player debug1) {
/* 208 */     if (this.lootTable != null && this.level.getServer() != null) {
/* 209 */       LootTable debug2 = this.level.getServer().getLootTables().get(this.lootTable);
/* 210 */       if (debug1 instanceof ServerPlayer) {
/* 211 */         CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)debug1, this.lootTable);
/*     */       }
/* 213 */       this.lootTable = null;
/*     */ 
/*     */ 
/*     */       
/* 217 */       LootContext.Builder debug3 = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, position()).withOptionalRandomSeed(this.lootTableSeed);
/*     */       
/* 219 */       if (debug1 != null) {
/* 220 */         debug3.withLuck(debug1.getLuck()).withParameter(LootContextParams.THIS_ENTITY, debug1);
/*     */       }
/* 222 */       debug2.fill(this, debug3.create(LootContextParamSets.CHEST));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 228 */     unpackLootTable((Player)null);
/* 229 */     this.itemStacks.clear();
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceLocation debug1, long debug2) {
/* 233 */     this.lootTable = debug1;
/* 234 */     this.lootTableSeed = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 240 */     if (this.lootTable == null || !debug3.isSpectator()) {
/* 241 */       unpackLootTable(debug2.player);
/* 242 */       return createMenu(debug1, debug2);
/*     */     } 
/* 244 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\AbstractMinecartContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */