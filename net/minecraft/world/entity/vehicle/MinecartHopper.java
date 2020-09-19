/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.Hopper;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MinecartHopper
/*     */   extends AbstractMinecartContainer implements Hopper {
/*     */   private boolean enabled = true;
/*  24 */   private int cooldownTime = -1;
/*  25 */   private final BlockPos lastPosition = BlockPos.ZERO;
/*     */   
/*     */   public MinecartHopper(EntityType<? extends MinecartHopper> debug1, Level debug2) {
/*  28 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public MinecartHopper(Level debug1, double debug2, double debug4, double debug6) {
/*  32 */     super(EntityType.HOPPER_MINECART, debug2, debug4, debug6, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractMinecart.Type getMinecartType() {
/*  37 */     return AbstractMinecart.Type.HOPPER;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  42 */     return Blocks.HOPPER.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultDisplayOffset() {
/*  47 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  52 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(int debug1, int debug2, int debug3, boolean debug4) {
/*  57 */     boolean debug5 = !debug4;
/*     */     
/*  59 */     if (debug5 != isEnabled()) {
/*  60 */       setEnabled(debug5);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  65 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean debug1) {
/*  69 */     this.enabled = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  74 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelX() {
/*  79 */     return getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelY() {
/*  84 */     return getY() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelZ() {
/*  89 */     return getZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  94 */     super.tick();
/*     */     
/*  96 */     if (!this.level.isClientSide && isAlive() && isEnabled()) {
/*  97 */       BlockPos debug1 = blockPosition();
/*  98 */       if (debug1.equals(this.lastPosition)) {
/*  99 */         this.cooldownTime--;
/*     */       } else {
/* 101 */         setCooldown(0);
/*     */       } 
/*     */       
/* 104 */       if (!isOnCooldown()) {
/* 105 */         setCooldown(0);
/*     */         
/* 107 */         if (suckInItems()) {
/* 108 */           setCooldown(4);
/* 109 */           setChanged();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean suckInItems() {
/* 116 */     if (HopperBlockEntity.suckInItems(this)) {
/* 117 */       return true;
/*     */     }
/*     */     
/* 120 */     List<ItemEntity> debug1 = this.level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.25D, 0.0D, 0.25D), EntitySelector.ENTITY_STILL_ALIVE);
/*     */     
/* 122 */     if (!debug1.isEmpty()) {
/* 123 */       HopperBlockEntity.addItem(this, debug1.get(0));
/*     */     }
/*     */     
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(DamageSource debug1) {
/* 131 */     super.destroy(debug1);
/*     */     
/* 133 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 134 */       spawnAtLocation((ItemLike)Blocks.HOPPER);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 140 */     super.addAdditionalSaveData(debug1);
/* 141 */     debug1.putInt("TransferCooldown", this.cooldownTime);
/* 142 */     debug1.putBoolean("Enabled", this.enabled);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 147 */     super.readAdditionalSaveData(debug1);
/* 148 */     this.cooldownTime = debug1.getInt("TransferCooldown");
/* 149 */     this.enabled = debug1.contains("Enabled") ? debug1.getBoolean("Enabled") : true;
/*     */   }
/*     */   
/*     */   public void setCooldown(int debug1) {
/* 153 */     this.cooldownTime = debug1;
/*     */   }
/*     */   
/*     */   public boolean isOnCooldown() {
/* 157 */     return (this.cooldownTime > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 162 */     return (AbstractContainerMenu)new HopperMenu(debug1, debug2, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\MinecartHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */