/*     */ package net.minecraft.world.entity.item;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.DirectionalPlaceContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FallingBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FallingBlockEntity extends Entity {
/*  44 */   private BlockState blockState = Blocks.SAND.defaultBlockState();
/*     */   public int time;
/*     */   public boolean dropItem = true;
/*     */   private boolean cancelDrop;
/*     */   private boolean hurtEntities;
/*  49 */   private int fallDamageMax = 40;
/*  50 */   private float fallDamageAmount = 2.0F;
/*     */   
/*     */   public CompoundTag blockData;
/*  53 */   protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
/*     */   
/*     */   public FallingBlockEntity(EntityType<? extends FallingBlockEntity> debug1, Level debug2) {
/*  56 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public FallingBlockEntity(Level debug1, double debug2, double debug4, double debug6, BlockState debug8) {
/*  60 */     this(EntityType.FALLING_BLOCK, debug1);
/*  61 */     this.blockState = debug8;
/*  62 */     this.blocksBuilding = true;
/*     */     
/*  64 */     setPos(debug2, debug4 + ((1.0F - getBbHeight()) / 2.0F), debug6);
/*     */     
/*  66 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/*  68 */     this.xo = debug2;
/*  69 */     this.yo = debug4;
/*  70 */     this.zo = debug6;
/*     */     
/*  72 */     setStartPos(blockPosition());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public void setStartPos(BlockPos debug1) {
/*  81 */     this.entityData.set(DATA_START_POS, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  95 */     this.entityData.define(DATA_START_POS, BlockPos.ZERO);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 100 */     return !this.removed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 105 */     if (this.blockState.isAir()) {
/* 106 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/* 110 */     Block debug1 = this.blockState.getBlock();
/* 111 */     if (this.time++ == 0) {
/*     */       
/* 113 */       BlockPos debug2 = blockPosition();
/* 114 */       if (this.level.getBlockState(debug2).is(debug1)) {
/* 115 */         this.level.removeBlock(debug2, false);
/* 116 */       } else if (!this.level.isClientSide) {
/* 117 */         remove();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 122 */     if (!isNoGravity()) {
/* 123 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
/*     */     }
/* 125 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 127 */     if (!this.level.isClientSide) {
/* 128 */       BlockPos debug2 = blockPosition();
/*     */       
/* 130 */       boolean debug3 = this.blockState.getBlock() instanceof net.minecraft.world.level.block.ConcretePowderBlock;
/* 131 */       boolean debug4 = (debug3 && this.level.getFluidState(debug2).is((Tag)FluidTags.WATER));
/* 132 */       double debug5 = getDeltaMovement().lengthSqr();
/*     */       
/* 134 */       if (debug3 && debug5 > 1.0D) {
/*     */ 
/*     */         
/* 137 */         BlockHitResult debug7 = this.level.clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
/* 138 */         if (debug7.getType() != HitResult.Type.MISS && this.level.getFluidState(debug7.getBlockPos()).is((Tag)FluidTags.WATER)) {
/*     */           
/* 140 */           debug2 = debug7.getBlockPos();
/* 141 */           debug4 = true;
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       if (this.onGround || debug4) {
/* 146 */         BlockState debug7 = this.level.getBlockState(debug2);
/*     */ 
/*     */         
/* 149 */         setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/*     */         
/* 151 */         if (!debug7.is(Blocks.MOVING_PISTON)) {
/* 152 */           remove();
/*     */           
/* 154 */           if (!this.cancelDrop) {
/* 155 */             boolean debug8 = debug7.canBeReplaced((BlockPlaceContext)new DirectionalPlaceContext(this.level, debug2, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
/*     */             
/* 157 */             boolean debug9 = (FallingBlock.isFree(this.level.getBlockState(debug2.below())) && (!debug3 || !debug4));
/* 158 */             boolean debug10 = (this.blockState.canSurvive((LevelReader)this.level, debug2) && !debug9);
/* 159 */             if (debug8 && debug10) {
/* 160 */               if (this.blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && this.level.getFluidState(debug2).getType() == Fluids.WATER) {
/* 161 */                 this.blockState = (BlockState)this.blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */               }
/* 163 */               if (this.level.setBlock(debug2, this.blockState, 3)) {
/* 164 */                 if (debug1 instanceof FallingBlock) {
/* 165 */                   ((FallingBlock)debug1).onLand(this.level, debug2, this.blockState, debug7, this);
/*     */                 }
/* 167 */                 if (this.blockData != null && debug1 instanceof net.minecraft.world.level.block.EntityBlock) {
/* 168 */                   BlockEntity debug11 = this.level.getBlockEntity(debug2);
/*     */                   
/* 170 */                   if (debug11 != null) {
/* 171 */                     CompoundTag debug12 = debug11.save(new CompoundTag());
/* 172 */                     for (String debug14 : this.blockData.getAllKeys()) {
/* 173 */                       Tag debug15 = this.blockData.get(debug14);
/* 174 */                       if ("x".equals(debug14) || "y".equals(debug14) || "z".equals(debug14)) {
/*     */                         continue;
/*     */                       }
/* 177 */                       debug12.put(debug14, debug15.copy());
/*     */                     } 
/* 179 */                     debug11.load(this.blockState, debug12);
/* 180 */                     debug11.setChanged();
/*     */                   } 
/*     */                 } 
/* 183 */               } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 184 */                 spawnAtLocation((ItemLike)debug1);
/*     */               } 
/* 186 */             } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 187 */               spawnAtLocation((ItemLike)debug1);
/*     */             } 
/* 189 */           } else if (debug1 instanceof FallingBlock) {
/* 190 */             ((FallingBlock)debug1).onBroken(this.level, debug2, this);
/*     */           } 
/*     */         } 
/* 193 */       } else if (!this.level.isClientSide && ((this.time > 100 && (debug2.getY() < 1 || debug2.getY() > 256)) || this.time > 600)) {
/*     */         
/* 195 */         if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 196 */           spawnAtLocation((ItemLike)debug1);
/*     */         }
/* 198 */         remove();
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     setDeltaMovement(getDeltaMovement().scale(0.98D));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 207 */     if (this.hurtEntities) {
/* 208 */       int debug3 = Mth.ceil(debug1 - 1.0F);
/* 209 */       if (debug3 > 0) {
/* 210 */         List<Entity> debug4 = Lists.newArrayList(this.level.getEntities(this, getBoundingBox()));
/* 211 */         boolean debug5 = this.blockState.is((Tag)BlockTags.ANVIL);
/* 212 */         DamageSource debug6 = debug5 ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
/*     */         
/* 214 */         for (Entity debug8 : debug4) {
/* 215 */           debug8.hurt(debug6, Math.min(Mth.floor(debug3 * this.fallDamageAmount), this.fallDamageMax));
/*     */         }
/*     */         
/* 218 */         if (debug5 && this.random.nextFloat() < 0.05000000074505806D + debug3 * 0.05D) {
/* 219 */           BlockState debug7 = AnvilBlock.damage(this.blockState);
/* 220 */           if (debug7 == null) {
/* 221 */             this.cancelDrop = true;
/*     */           } else {
/* 223 */             this.blockState = debug7;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 233 */     debug1.put("BlockState", (Tag)NbtUtils.writeBlockState(this.blockState));
/* 234 */     debug1.putInt("Time", this.time);
/* 235 */     debug1.putBoolean("DropItem", this.dropItem);
/* 236 */     debug1.putBoolean("HurtEntities", this.hurtEntities);
/* 237 */     debug1.putFloat("FallHurtAmount", this.fallDamageAmount);
/* 238 */     debug1.putInt("FallHurtMax", this.fallDamageMax);
/* 239 */     if (this.blockData != null) {
/* 240 */       debug1.put("TileEntityData", (Tag)this.blockData);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 246 */     this.blockState = NbtUtils.readBlockState(debug1.getCompound("BlockState"));
/*     */     
/* 248 */     this.time = debug1.getInt("Time");
/*     */     
/* 250 */     if (debug1.contains("HurtEntities", 99)) {
/* 251 */       this.hurtEntities = debug1.getBoolean("HurtEntities");
/* 252 */       this.fallDamageAmount = debug1.getFloat("FallHurtAmount");
/* 253 */       this.fallDamageMax = debug1.getInt("FallHurtMax");
/* 254 */     } else if (this.blockState.is((Tag)BlockTags.ANVIL)) {
/* 255 */       this.hurtEntities = true;
/*     */     } 
/*     */     
/* 258 */     if (debug1.contains("DropItem", 99)) {
/* 259 */       this.dropItem = debug1.getBoolean("DropItem");
/*     */     }
/*     */     
/* 262 */     if (debug1.contains("TileEntityData", 10)) {
/* 263 */       this.blockData = debug1.getCompound("TileEntityData");
/*     */     }
/*     */     
/* 266 */     if (this.blockState.isAir()) {
/* 267 */       this.blockState = Blocks.SAND.defaultBlockState();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHurtsEntities(boolean debug1) {
/* 276 */     this.hurtEntities = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory debug1) {
/* 286 */     super.fillCrashReportCategory(debug1);
/* 287 */     debug1.setDetail("Immitating BlockState", this.blockState.toString());
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 291 */     return this.blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 296 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 301 */     return (Packet<?>)new ClientboundAddEntityPacket(this, Block.getId(getBlockState()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\item\FallingBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */