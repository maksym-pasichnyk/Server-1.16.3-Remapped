/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.data.worldgen.Features;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.projectile.ThrownEnderpearl;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class TheEndGatewayBlockEntity
/*     */   extends TheEndPortalBlockEntity
/*     */   implements TickableBlockEntity
/*     */ {
/*  39 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   private long age;
/*     */   
/*     */   private int teleportCooldown;
/*     */   
/*     */   @Nullable
/*     */   private BlockPos exitPortal;
/*     */   
/*     */   private boolean exactTeleport;
/*     */ 
/*     */   
/*     */   public TheEndGatewayBlockEntity() {
/*  53 */     super(BlockEntityType.END_GATEWAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  58 */     super.save(debug1);
/*  59 */     debug1.putLong("Age", this.age);
/*  60 */     if (this.exitPortal != null) {
/*  61 */       debug1.put("ExitPortal", (Tag)NbtUtils.writeBlockPos(this.exitPortal));
/*     */     }
/*  63 */     if (this.exactTeleport) {
/*  64 */       debug1.putBoolean("ExactTeleport", this.exactTeleport);
/*     */     }
/*     */     
/*  67 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  72 */     super.load(debug1, debug2);
/*  73 */     this.age = debug2.getLong("Age");
/*  74 */     if (debug2.contains("ExitPortal", 10)) {
/*  75 */       this.exitPortal = NbtUtils.readBlockPos(debug2.getCompound("ExitPortal"));
/*     */     }
/*  77 */     this.exactTeleport = debug2.getBoolean("ExactTeleport");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  87 */     boolean debug1 = isSpawning();
/*  88 */     boolean debug2 = isCoolingDown();
/*  89 */     this.age++;
/*     */     
/*  91 */     if (debug2) {
/*  92 */       this.teleportCooldown--;
/*  93 */     } else if (!this.level.isClientSide) {
/*  94 */       List<Entity> debug3 = this.level.getEntitiesOfClass(Entity.class, new AABB(getBlockPos()), TheEndGatewayBlockEntity::canEntityTeleport);
/*  95 */       if (!debug3.isEmpty()) {
/*  96 */         teleportEntity(debug3.get(this.level.random.nextInt(debug3.size())));
/*     */       }
/*  98 */       if (this.age % 2400L == 0L) {
/*  99 */         triggerCooldown();
/*     */       }
/*     */     } 
/*     */     
/* 103 */     if (debug1 != isSpawning() || debug2 != isCoolingDown()) {
/* 104 */       setChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean canEntityTeleport(Entity debug0) {
/* 109 */     return (EntitySelector.NO_SPECTATORS.test(debug0) && !debug0.getRootVehicle().isOnPortalCooldown());
/*     */   }
/*     */   
/*     */   public boolean isSpawning() {
/* 113 */     return (this.age < 200L);
/*     */   }
/*     */   
/*     */   public boolean isCoolingDown() {
/* 117 */     return (this.teleportCooldown > 0);
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
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 131 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 8, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 136 */     return save(new CompoundTag());
/*     */   }
/*     */   
/*     */   public void triggerCooldown() {
/* 140 */     if (!this.level.isClientSide) {
/* 141 */       this.teleportCooldown = 40;
/* 142 */       this.level.blockEvent(getBlockPos(), getBlockState().getBlock(), 1, 0);
/* 143 */       setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int debug1, int debug2) {
/* 149 */     if (debug1 == 1) {
/* 150 */       this.teleportCooldown = 40;
/* 151 */       return true;
/*     */     } 
/*     */     
/* 154 */     return super.triggerEvent(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void teleportEntity(Entity debug1) {
/* 158 */     if (!(this.level instanceof ServerLevel) || isCoolingDown()) {
/*     */       return;
/*     */     }
/* 161 */     this.teleportCooldown = 100;
/*     */ 
/*     */     
/* 164 */     if (this.exitPortal == null && this.level.dimension() == Level.END) {
/* 165 */       findExitPortal((ServerLevel)this.level);
/*     */     }
/*     */     
/* 168 */     if (this.exitPortal != null) {
/* 169 */       Entity debug3; BlockPos debug2 = this.exactTeleport ? this.exitPortal : findExitPosition();
/*     */       
/* 171 */       if (debug1 instanceof ThrownEnderpearl) {
/* 172 */         Entity debug4 = ((ThrownEnderpearl)debug1).getOwner();
/* 173 */         if (debug4 instanceof ServerPlayer) {
/* 174 */           CriteriaTriggers.ENTER_BLOCK.trigger((ServerPlayer)debug4, this.level.getBlockState(getBlockPos()));
/*     */         }
/* 176 */         if (debug4 != null) {
/* 177 */           debug3 = debug4;
/* 178 */           debug1.remove();
/*     */         } else {
/* 180 */           debug3 = debug1;
/*     */         } 
/*     */       } else {
/* 183 */         debug3 = debug1.getRootVehicle();
/*     */       } 
/* 185 */       debug3.setPortalCooldown();
/* 186 */       debug3.teleportToWithTicket(debug2.getX() + 0.5D, debug2.getY(), debug2.getZ() + 0.5D);
/*     */     } 
/*     */     
/* 189 */     triggerCooldown();
/*     */   }
/*     */   
/*     */   private BlockPos findExitPosition() {
/* 193 */     BlockPos debug1 = findTallestBlock((BlockGetter)this.level, this.exitPortal.offset(0, 2, 0), 5, false);
/* 194 */     LOGGER.debug("Best exit position for portal at {} is {}", this.exitPortal, debug1);
/* 195 */     return debug1.above();
/*     */   }
/*     */   
/*     */   private void findExitPortal(ServerLevel debug1) {
/* 199 */     Vec3 debug2 = (new Vec3(getBlockPos().getX(), 0.0D, getBlockPos().getZ())).normalize();
/* 200 */     Vec3 debug3 = debug2.scale(1024.0D);
/*     */     
/* 202 */     int debug4 = 16;
/* 203 */     while (getChunk((Level)debug1, debug3).getHighestSectionPosition() > 0 && debug4-- > 0) {
/* 204 */       LOGGER.debug("Skipping backwards past nonempty chunk at {}", debug3);
/* 205 */       debug3 = debug3.add(debug2.scale(-16.0D));
/*     */     } 
/*     */     
/* 208 */     debug4 = 16;
/* 209 */     while (getChunk((Level)debug1, debug3).getHighestSectionPosition() == 0 && debug4-- > 0) {
/* 210 */       LOGGER.debug("Skipping forward past empty chunk at {}", debug3);
/* 211 */       debug3 = debug3.add(debug2.scale(16.0D));
/*     */     } 
/* 213 */     LOGGER.debug("Found chunk at {}", debug3);
/*     */     
/* 215 */     LevelChunk debug5 = getChunk((Level)debug1, debug3);
/*     */     
/* 217 */     this.exitPortal = findValidSpawnInChunk(debug5);
/*     */     
/* 219 */     if (this.exitPortal == null) {
/* 220 */       this.exitPortal = new BlockPos(debug3.x + 0.5D, 75.0D, debug3.z + 0.5D);
/* 221 */       LOGGER.debug("Failed to find suitable block, settling on {}", this.exitPortal);
/* 222 */       Features.END_ISLAND.place((WorldGenLevel)debug1, debug1.getChunkSource().getGenerator(), new Random(this.exitPortal.asLong()), this.exitPortal);
/*     */     } else {
/* 224 */       LOGGER.debug("Found block at {}", this.exitPortal);
/*     */     } 
/*     */     
/* 227 */     this.exitPortal = findTallestBlock((BlockGetter)debug1, this.exitPortal, 16, true);
/* 228 */     LOGGER.debug("Creating portal at {}", this.exitPortal);
/*     */     
/* 230 */     this.exitPortal = this.exitPortal.above(10);
/* 231 */     createExitPortal(debug1, this.exitPortal);
/* 232 */     setChanged();
/*     */   }
/*     */   
/*     */   private static BlockPos findTallestBlock(BlockGetter debug0, BlockPos debug1, int debug2, boolean debug3) {
/* 236 */     BlockPos debug4 = null;
/*     */     
/* 238 */     for (int debug5 = -debug2; debug5 <= debug2; debug5++) {
/* 239 */       for (int debug6 = -debug2; debug6 <= debug2; debug6++) {
/* 240 */         if (debug5 != 0 || debug6 != 0 || debug3)
/*     */         {
/*     */ 
/*     */           
/* 244 */           for (int debug7 = 255; debug7 > ((debug4 == null) ? 0 : debug4.getY()); debug7--) {
/* 245 */             BlockPos debug8 = new BlockPos(debug1.getX() + debug5, debug7, debug1.getZ() + debug6);
/* 246 */             BlockState debug9 = debug0.getBlockState(debug8);
/* 247 */             if (debug9.isCollisionShapeFullBlock(debug0, debug8) && (debug3 || !debug9.is(Blocks.BEDROCK))) {
/* 248 */               debug4 = debug8;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 255 */     return (debug4 == null) ? debug1 : debug4;
/*     */   }
/*     */   
/*     */   private static LevelChunk getChunk(Level debug0, Vec3 debug1) {
/* 259 */     return debug0.getChunk(Mth.floor(debug1.x / 16.0D), Mth.floor(debug1.z / 16.0D));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static BlockPos findValidSpawnInChunk(LevelChunk debug0) {
/* 264 */     ChunkPos debug1 = debug0.getPos();
/* 265 */     BlockPos debug2 = new BlockPos(debug1.getMinBlockX(), 30, debug1.getMinBlockZ());
/* 266 */     int debug3 = debug0.getHighestSectionPosition() + 16 - 1;
/* 267 */     BlockPos debug4 = new BlockPos(debug1.getMaxBlockX(), debug3, debug1.getMaxBlockZ());
/* 268 */     BlockPos debug5 = null;
/* 269 */     double debug6 = 0.0D;
/*     */ 
/*     */     
/* 272 */     for (BlockPos debug9 : BlockPos.betweenClosed(debug2, debug4)) {
/* 273 */       BlockState debug10 = debug0.getBlockState(debug9);
/*     */       
/* 275 */       BlockPos debug11 = debug9.above();
/* 276 */       BlockPos debug12 = debug9.above(2);
/* 277 */       if (debug10.is(Blocks.END_STONE) && !debug0.getBlockState(debug11).isCollisionShapeFullBlock((BlockGetter)debug0, debug11) && !debug0.getBlockState(debug12).isCollisionShapeFullBlock((BlockGetter)debug0, debug12)) {
/* 278 */         double debug13 = debug9.distSqr(0.0D, 0.0D, 0.0D, true);
/* 279 */         if (debug5 == null || debug13 < debug6) {
/* 280 */           debug5 = debug9;
/* 281 */           debug6 = debug13;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 286 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   private void createExitPortal(ServerLevel debug1, BlockPos debug2) {
/* 291 */     Feature.END_GATEWAY.configured((FeatureConfiguration)EndGatewayConfiguration.knownExit(getBlockPos(), false)).place((WorldGenLevel)debug1, debug1.getChunkSource().getGenerator(), new Random(), debug2);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExitPosition(BlockPos debug1, boolean debug2) {
/* 308 */     this.exactTeleport = debug2;
/* 309 */     this.exitPortal = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\TheEndGatewayBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */