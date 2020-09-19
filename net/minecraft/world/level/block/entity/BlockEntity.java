/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ public abstract class BlockEntity {
/*  19 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final BlockEntityType<?> type;
/*     */   @Nullable
/*     */   protected Level level;
/*  24 */   protected BlockPos worldPosition = BlockPos.ZERO;
/*     */   
/*     */   protected boolean remove;
/*     */   
/*     */   @Nullable
/*     */   private BlockState blockState;
/*     */   private boolean hasLoggedInvalidStateBefore;
/*     */   
/*     */   public BlockEntity(BlockEntityType<?> debug1) {
/*  33 */     this.type = debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Level getLevel() {
/*  38 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevelAndPosition(Level debug1, BlockPos debug2) {
/*  42 */     this.level = debug1;
/*  43 */     this.worldPosition = debug2.immutable();
/*     */   }
/*     */   
/*     */   public boolean hasLevel() {
/*  47 */     return (this.level != null);
/*     */   }
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  51 */     this.worldPosition = new BlockPos(debug2.getInt("x"), debug2.getInt("y"), debug2.getInt("z"));
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  55 */     return saveMetadata(debug1);
/*     */   }
/*     */   
/*     */   private CompoundTag saveMetadata(CompoundTag debug1) {
/*  59 */     ResourceLocation debug2 = BlockEntityType.getKey(getType());
/*  60 */     if (debug2 == null) {
/*  61 */       throw new RuntimeException(getClass() + " is missing a mapping! This is a bug!");
/*     */     }
/*  63 */     debug1.putString("id", debug2.toString());
/*  64 */     debug1.putInt("x", this.worldPosition.getX());
/*  65 */     debug1.putInt("y", this.worldPosition.getY());
/*  66 */     debug1.putInt("z", this.worldPosition.getZ());
/*     */     
/*  68 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static BlockEntity loadStatic(BlockState debug0, CompoundTag debug1) {
/*  73 */     String debug2 = debug1.getString("id");
/*     */     
/*  75 */     return Registry.BLOCK_ENTITY_TYPE.getOptional(new ResourceLocation(debug2))
/*  76 */       .map(debug1 -> {
/*     */           try {
/*     */             return debug1.create();
/*  79 */           } catch (Throwable debug2) {
/*     */             LOGGER.error("Failed to create block entity {}", debug0, debug2);
/*     */             
/*     */             return null;
/*     */           } 
/*  84 */         }).map(debug3 -> {
/*     */           try {
/*     */             debug3.load(debug0, debug1);
/*     */             return debug3;
/*  88 */           } catch (Throwable debug4) {
/*     */             LOGGER.error("Failed to load data for block entity {}", debug2, debug4);
/*     */             
/*     */             return null;
/*     */           } 
/*  93 */         }).orElseGet(() -> {
/*     */           LOGGER.warn("Skipping BlockEntity with id {}", debug0);
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   public void setChanged() {
/* 100 */     if (this.level != null) {
/* 101 */       this.blockState = this.level.getBlockState(this.worldPosition);
/* 102 */       this.level.blockEntityChanged(this.worldPosition, this);
/*     */       
/* 104 */       if (!this.blockState.isAir()) {
/* 105 */         this.level.updateNeighbourForOutputSignal(this.worldPosition, this.blockState.getBlock());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getBlockPos() {
/* 116 */     return this.worldPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getBlockState() {
/* 123 */     if (this.blockState == null) {
/* 124 */       this.blockState = this.level.getBlockState(this.worldPosition);
/*     */     }
/* 126 */     return this.blockState;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 135 */     return saveMetadata(new CompoundTag());
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 139 */     return this.remove;
/*     */   }
/*     */   
/*     */   public void setRemoved() {
/* 143 */     this.remove = true;
/*     */   }
/*     */   
/*     */   public void clearRemoved() {
/* 147 */     this.remove = false;
/*     */   }
/*     */   
/*     */   public boolean triggerEvent(int debug1, int debug2) {
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   public void clearCache() {
/* 155 */     this.blockState = null;
/*     */   }
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory debug1) {
/* 159 */     debug1.setDetail("Name", () -> Registry.BLOCK_ENTITY_TYPE.getKey(getType()) + " // " + getClass().getCanonicalName());
/*     */     
/* 161 */     if (this.level == null) {
/*     */       return;
/*     */     }
/*     */     
/* 165 */     CrashReportCategory.populateBlockDetails(debug1, this.worldPosition, getBlockState());
/*     */     
/* 167 */     CrashReportCategory.populateBlockDetails(debug1, this.worldPosition, this.level.getBlockState(this.worldPosition));
/*     */   }
/*     */   
/*     */   public void setPosition(BlockPos debug1) {
/* 171 */     this.worldPosition = debug1.immutable();
/*     */   }
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(Rotation debug1) {}
/*     */ 
/*     */   
/*     */   public void mirror(Mirror debug1) {}
/*     */   
/*     */   public BlockEntityType<?> getType() {
/* 185 */     return this.type;
/*     */   }
/*     */   
/*     */   public void logInvalidState() {
/* 189 */     if (this.hasLoggedInvalidStateBefore) {
/*     */       return;
/*     */     }
/*     */     
/* 193 */     this.hasLoggedInvalidStateBefore = true;
/* 194 */     LOGGER.warn("Block entity invalid: {} @ {}", new Supplier[] { () -> Registry.BLOCK_ENTITY_TYPE.getKey(getType()), this::getBlockPos });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */