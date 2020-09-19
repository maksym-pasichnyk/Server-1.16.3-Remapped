/*     */ package net.minecraft.world.entity.animal.horse;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public abstract class AbstractChestedHorse extends AbstractHorse {
/*  22 */   private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(AbstractChestedHorse.class, EntityDataSerializers.BOOLEAN);
/*     */ 
/*     */   
/*     */   protected AbstractChestedHorse(EntityType<? extends AbstractChestedHorse> debug1, Level debug2) {
/*  26 */     super((EntityType)debug1, debug2);
/*     */     
/*  28 */     this.canGallop = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes() {
/*  33 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue(generateRandomMaxHealth());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  38 */     super.defineSynchedData();
/*     */     
/*  40 */     this.entityData.define(DATA_ID_CHEST, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createBaseChestedHorseAttributes() {
/*  44 */     return createBaseHorseAttributes()
/*  45 */       .add(Attributes.MOVEMENT_SPEED, 0.17499999701976776D)
/*  46 */       .add(Attributes.JUMP_STRENGTH, 0.5D);
/*     */   }
/*     */   
/*     */   public boolean hasChest() {
/*  50 */     return ((Boolean)this.entityData.get(DATA_ID_CHEST)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setChest(boolean debug1) {
/*  54 */     this.entityData.set(DATA_ID_CHEST, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getInventorySize() {
/*  59 */     if (hasChest()) {
/*  60 */       return 17;
/*     */     }
/*  62 */     return super.getInventorySize();
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/*  67 */     return super.getPassengersRidingOffset() - 0.25D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropEquipment() {
/*  72 */     super.dropEquipment();
/*  73 */     if (hasChest()) {
/*  74 */       if (!this.level.isClientSide) {
/*  75 */         spawnAtLocation((ItemLike)Blocks.CHEST);
/*     */       }
/*  77 */       setChest(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  83 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  85 */     debug1.putBoolean("ChestedHorse", hasChest());
/*  86 */     if (hasChest()) {
/*  87 */       ListTag debug2 = new ListTag();
/*     */       
/*  89 */       for (int debug3 = 2; debug3 < this.inventory.getContainerSize(); debug3++) {
/*  90 */         ItemStack debug4 = this.inventory.getItem(debug3);
/*     */         
/*  92 */         if (!debug4.isEmpty()) {
/*  93 */           CompoundTag debug5 = new CompoundTag();
/*     */           
/*  95 */           debug5.putByte("Slot", (byte)debug3);
/*     */           
/*  97 */           debug4.save(debug5);
/*  98 */           debug2.add(debug5);
/*     */         } 
/*     */       } 
/* 101 */       debug1.put("Items", (Tag)debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 107 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 109 */     setChest(debug1.getBoolean("ChestedHorse"));
/*     */     
/* 111 */     if (hasChest()) {
/* 112 */       ListTag debug2 = debug1.getList("Items", 10);
/* 113 */       createInventory();
/*     */       
/* 115 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 116 */         CompoundTag debug4 = debug2.getCompound(debug3);
/* 117 */         int debug5 = debug4.getByte("Slot") & 0xFF;
/*     */         
/* 119 */         if (debug5 >= 2 && debug5 < this.inventory.getContainerSize()) {
/* 120 */           this.inventory.setItem(debug5, ItemStack.of(debug4));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     updateContainerEquipment();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 130 */     if (debug1 == 499) {
/* 131 */       if (hasChest() && debug2.isEmpty()) {
/* 132 */         setChest(false);
/* 133 */         createInventory();
/* 134 */         return true;
/*     */       } 
/* 136 */       if (!hasChest() && debug2.getItem() == Blocks.CHEST.asItem()) {
/* 137 */         setChest(true);
/* 138 */         createInventory();
/* 139 */         return true;
/*     */       } 
/*     */     } 
/* 142 */     return super.setSlot(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 147 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*     */     
/* 149 */     if (!isBaby()) {
/* 150 */       if (isTamed() && debug1.isSecondaryUseActive()) {
/* 151 */         openInventory(debug1);
/* 152 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */       
/* 155 */       if (isVehicle()) {
/* 156 */         return super.mobInteract(debug1, debug2);
/*     */       }
/*     */     } 
/*     */     
/* 160 */     if (!debug3.isEmpty()) {
/* 161 */       if (isFood(debug3)) {
/* 162 */         return fedFood(debug1, debug3);
/*     */       }
/*     */       
/* 165 */       if (!isTamed()) {
/* 166 */         makeMad();
/* 167 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */       
/* 170 */       if (!hasChest() && debug3.getItem() == Blocks.CHEST.asItem()) {
/* 171 */         setChest(true);
/* 172 */         playChestEquipsSound();
/* 173 */         if (!debug1.abilities.instabuild) {
/* 174 */           debug3.shrink(1);
/*     */         }
/* 176 */         createInventory();
/* 177 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */       
/* 180 */       if (!isBaby() && !isSaddled() && debug3.getItem() == Items.SADDLE) {
/* 181 */         openInventory(debug1);
/* 182 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */     } 
/* 185 */     if (isBaby()) {
/* 186 */       return super.mobInteract(debug1, debug2);
/*     */     }
/*     */     
/* 189 */     doPlayerRide(debug1);
/*     */     
/* 191 */     return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */   }
/*     */   
/*     */   protected void playChestEquipsSound() {
/* 195 */     playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*     */   }
/*     */   
/*     */   public int getInventoryColumns() {
/* 199 */     return 5;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\AbstractChestedHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */