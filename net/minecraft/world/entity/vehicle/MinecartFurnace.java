/*     */ package net.minecraft.world.entity.vehicle;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FurnaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MinecartFurnace extends AbstractMinecart {
/*  29 */   private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(MinecartFurnace.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int fuel;
/*     */   public double xPush;
/*     */   public double zPush;
/*  34 */   private static final Ingredient INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.COAL, (ItemLike)Items.CHARCOAL });
/*     */   
/*     */   public MinecartFurnace(EntityType<? extends MinecartFurnace> debug1, Level debug2) {
/*  37 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public MinecartFurnace(Level debug1, double debug2, double debug4, double debug6) {
/*  41 */     super(EntityType.FURNACE_MINECART, debug1, debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractMinecart.Type getMinecartType() {
/*  46 */     return AbstractMinecart.Type.FURNACE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  51 */     super.defineSynchedData();
/*  52 */     this.entityData.define(DATA_ID_FUEL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  57 */     super.tick();
/*     */     
/*  59 */     if (!this.level.isClientSide()) {
/*  60 */       if (this.fuel > 0) {
/*  61 */         this.fuel--;
/*     */       }
/*  63 */       if (this.fuel <= 0) {
/*  64 */         this.xPush = 0.0D;
/*  65 */         this.zPush = 0.0D;
/*     */       } 
/*  67 */       setHasFuel((this.fuel > 0));
/*     */     } 
/*     */     
/*  70 */     if (hasFuel() && this.random.nextInt(4) == 0) {
/*  71 */       this.level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, getX(), getY() + 0.8D, getZ(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getMaxSpeed() {
/*  77 */     return 0.2D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(DamageSource debug1) {
/*  82 */     super.destroy(debug1);
/*     */     
/*  84 */     if (!debug1.isExplosion() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/*  85 */       spawnAtLocation((ItemLike)Blocks.FURNACE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void moveAlongTrack(BlockPos debug1, BlockState debug2) {
/*  92 */     double debug3 = 1.0E-4D;
/*  93 */     double debug5 = 0.001D;
/*     */     
/*  95 */     super.moveAlongTrack(debug1, debug2);
/*     */     
/*  97 */     Vec3 debug7 = getDeltaMovement();
/*     */     
/*  99 */     double debug8 = getHorizontalDistanceSqr(debug7);
/* 100 */     double debug10 = this.xPush * this.xPush + this.zPush * this.zPush;
/* 101 */     if (debug10 > 1.0E-4D && debug8 > 0.001D) {
/* 102 */       double debug12 = Mth.sqrt(debug8);
/* 103 */       double debug14 = Mth.sqrt(debug10);
/*     */ 
/*     */       
/* 106 */       this.xPush = debug7.x / debug12 * debug14;
/* 107 */       this.zPush = debug7.z / debug12 * debug14;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyNaturalSlowdown() {
/* 113 */     double debug1 = this.xPush * this.xPush + this.zPush * this.zPush;
/*     */     
/* 115 */     if (debug1 > 1.0E-7D) {
/* 116 */       debug1 = Mth.sqrt(debug1);
/* 117 */       this.xPush /= debug1;
/* 118 */       this.zPush /= debug1;
/* 119 */       setDeltaMovement(getDeltaMovement().multiply(0.8D, 0.0D, 0.8D).add(this.xPush, 0.0D, this.zPush));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 125 */       setDeltaMovement(getDeltaMovement().multiply(0.98D, 0.0D, 0.98D));
/*     */     } 
/*     */     
/* 128 */     super.applyNaturalSlowdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 133 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 134 */     if (INGREDIENT.test(debug3) && this.fuel + 3600 <= 32000) {
/* 135 */       if (!debug1.abilities.instabuild) {
/* 136 */         debug3.shrink(1);
/*     */       }
/* 138 */       this.fuel += 3600;
/*     */     } 
/*     */     
/* 141 */     if (this.fuel > 0) {
/* 142 */       this.xPush = getX() - debug1.getX();
/* 143 */       this.zPush = getZ() - debug1.getZ();
/*     */     } 
/*     */     
/* 146 */     return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 151 */     super.addAdditionalSaveData(debug1);
/* 152 */     debug1.putDouble("PushX", this.xPush);
/* 153 */     debug1.putDouble("PushZ", this.zPush);
/* 154 */     debug1.putShort("Fuel", (short)this.fuel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 159 */     super.readAdditionalSaveData(debug1);
/* 160 */     this.xPush = debug1.getDouble("PushX");
/* 161 */     this.zPush = debug1.getDouble("PushZ");
/* 162 */     this.fuel = debug1.getShort("Fuel");
/*     */   }
/*     */   
/*     */   protected boolean hasFuel() {
/* 166 */     return ((Boolean)this.entityData.get(DATA_ID_FUEL)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void setHasFuel(boolean debug1) {
/* 170 */     this.entityData.set(DATA_ID_FUEL, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/* 175 */     return (BlockState)((BlockState)Blocks.FURNACE.defaultBlockState().setValue((Property)FurnaceBlock.FACING, (Comparable)Direction.NORTH)).setValue((Property)FurnaceBlock.LIT, Boolean.valueOf(hasFuel()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\MinecartFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */