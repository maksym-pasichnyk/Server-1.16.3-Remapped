/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.WeighedRandom;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseSpawner
/*     */ {
/*  32 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  36 */   private int spawnDelay = 20;
/*  37 */   private final List<SpawnData> spawnPotentials = Lists.newArrayList();
/*  38 */   private SpawnData nextSpawnData = new SpawnData();
/*     */   private double spin;
/*     */   private double oSpin;
/*  41 */   private int minSpawnDelay = 200;
/*  42 */   private int maxSpawnDelay = 800;
/*  43 */   private int spawnCount = 4;
/*     */   @Nullable
/*     */   private Entity displayEntity;
/*  46 */   private int maxNearbyEntities = 6;
/*  47 */   private int requiredPlayerRange = 16;
/*  48 */   private int spawnRange = 4;
/*     */   
/*     */   @Nullable
/*     */   private ResourceLocation getEntityId() {
/*  52 */     String debug1 = this.nextSpawnData.getTag().getString("id");
/*     */     try {
/*  54 */       return StringUtil.isNullOrEmpty(debug1) ? null : new ResourceLocation(debug1);
/*  55 */     } catch (ResourceLocationException debug2) {
/*  56 */       BlockPos debug3 = getPos();
/*  57 */       LOGGER.warn("Invalid entity id '{}' at spawner {}:[{},{},{}]", debug1, getLevel().dimension().location(), Integer.valueOf(debug3.getX()), Integer.valueOf(debug3.getY()), Integer.valueOf(debug3.getZ()));
/*  58 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setEntityId(EntityType<?> debug1) {
/*  63 */     this.nextSpawnData.getTag().putString("id", Registry.ENTITY_TYPE.getKey(debug1).toString());
/*     */   }
/*     */   
/*     */   private boolean isNearPlayer() {
/*  67 */     BlockPos debug1 = getPos();
/*  68 */     return getLevel().hasNearbyAlivePlayer(debug1.getX() + 0.5D, debug1.getY() + 0.5D, debug1.getZ() + 0.5D, this.requiredPlayerRange);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  72 */     if (!isNearPlayer()) {
/*  73 */       this.oSpin = this.spin;
/*     */       
/*     */       return;
/*     */     } 
/*  77 */     Level debug1 = getLevel();
/*  78 */     BlockPos debug2 = getPos();
/*  79 */     if (!(debug1 instanceof ServerLevel)) {
/*  80 */       double debug3 = debug2.getX() + debug1.random.nextDouble();
/*  81 */       double debug5 = debug2.getY() + debug1.random.nextDouble();
/*  82 */       double debug7 = debug2.getZ() + debug1.random.nextDouble();
/*  83 */       debug1.addParticle((ParticleOptions)ParticleTypes.SMOKE, debug3, debug5, debug7, 0.0D, 0.0D, 0.0D);
/*  84 */       debug1.addParticle((ParticleOptions)ParticleTypes.FLAME, debug3, debug5, debug7, 0.0D, 0.0D, 0.0D);
/*     */       
/*  86 */       if (this.spawnDelay > 0) {
/*  87 */         this.spawnDelay--;
/*     */       }
/*  89 */       this.oSpin = this.spin;
/*  90 */       this.spin = (this.spin + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
/*     */     } else {
/*  92 */       if (this.spawnDelay == -1) {
/*  93 */         delay();
/*     */       }
/*     */       
/*  96 */       if (this.spawnDelay > 0) {
/*  97 */         this.spawnDelay--;
/*     */         
/*     */         return;
/*     */       } 
/* 101 */       boolean debug3 = false;
/*     */       
/* 103 */       for (int debug4 = 0; debug4 < this.spawnCount; debug4++) {
/* 104 */         CompoundTag debug5 = this.nextSpawnData.getTag();
/* 105 */         Optional<EntityType<?>> debug6 = EntityType.by(debug5);
/* 106 */         if (!debug6.isPresent()) {
/* 107 */           delay();
/*     */           
/*     */           return;
/*     */         } 
/* 111 */         ListTag debug7 = debug5.getList("Pos", 6);
/*     */         
/* 113 */         int debug8 = debug7.size();
/* 114 */         double debug9 = (debug8 >= 1) ? debug7.getDouble(0) : (debug2.getX() + (debug1.random.nextDouble() - debug1.random.nextDouble()) * this.spawnRange + 0.5D);
/* 115 */         double debug11 = (debug8 >= 2) ? debug7.getDouble(1) : (debug2.getY() + debug1.random.nextInt(3) - 1);
/* 116 */         double debug13 = (debug8 >= 3) ? debug7.getDouble(2) : (debug2.getZ() + (debug1.random.nextDouble() - debug1.random.nextDouble()) * this.spawnRange + 0.5D);
/*     */         
/* 118 */         if (!debug1.noCollision(((EntityType)debug6.get()).getAABB(debug9, debug11, debug13))) {
/*     */           continue;
/*     */         }
/* 121 */         ServerLevel debug15 = (ServerLevel)debug1;
/* 122 */         if (!SpawnPlacements.checkSpawnRules(debug6.get(), (ServerLevelAccessor)debug15, MobSpawnType.SPAWNER, new BlockPos(debug9, debug11, debug13), debug1.getRandom())) {
/*     */           continue;
/*     */         }
/*     */         
/* 126 */         Entity debug16 = EntityType.loadEntityRecursive(debug5, debug1, debug6 -> {
/*     */               debug6.moveTo(debug0, debug2, debug4, debug6.yRot, debug6.xRot);
/*     */               return debug6;
/*     */             });
/* 130 */         if (debug16 == null) {
/* 131 */           delay();
/*     */           
/*     */           return;
/*     */         } 
/* 135 */         int debug17 = debug1.getEntitiesOfClass(debug16.getClass(), (new AABB(debug2.getX(), debug2.getY(), debug2.getZ(), (debug2.getX() + 1), (debug2.getY() + 1), (debug2.getZ() + 1))).inflate(this.spawnRange)).size();
/* 136 */         if (debug17 >= this.maxNearbyEntities) {
/* 137 */           delay();
/*     */           
/*     */           return;
/*     */         } 
/* 141 */         debug16.moveTo(debug16.getX(), debug16.getY(), debug16.getZ(), debug1.random.nextFloat() * 360.0F, 0.0F);
/* 142 */         if (debug16 instanceof Mob) {
/* 143 */           Mob debug18 = (Mob)debug16;
/* 144 */           if (!debug18.checkSpawnRules(debug1, MobSpawnType.SPAWNER) || !debug18.checkSpawnObstruction(debug1)) {
/*     */             continue;
/*     */           }
/* 147 */           if (this.nextSpawnData.getTag().size() == 1 && this.nextSpawnData.getTag().contains("id", 8)) {
/* 148 */             ((Mob)debug16).finalizeSpawn((ServerLevelAccessor)debug15, debug1.getCurrentDifficultyAt(debug16.blockPosition()), MobSpawnType.SPAWNER, null, null);
/*     */           }
/*     */         } 
/*     */         
/* 152 */         if (!debug15.tryAddFreshEntityWithPassengers(debug16)) {
/* 153 */           delay();
/*     */           
/*     */           return;
/*     */         } 
/* 157 */         debug1.levelEvent(2004, debug2, 0);
/* 158 */         if (debug16 instanceof Mob) {
/* 159 */           ((Mob)debug16).spawnAnim();
/*     */         }
/* 161 */         debug3 = true;
/*     */         continue;
/*     */       } 
/* 164 */       if (debug3) {
/* 165 */         delay();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void delay() {
/* 171 */     if (this.maxSpawnDelay <= this.minSpawnDelay) {
/* 172 */       this.spawnDelay = this.minSpawnDelay;
/*     */     } else {
/* 174 */       this.spawnDelay = this.minSpawnDelay + (getLevel()).random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
/*     */     } 
/*     */     
/* 177 */     if (!this.spawnPotentials.isEmpty()) {
/* 178 */       setNextSpawnData((SpawnData)WeighedRandom.getRandomItem((getLevel()).random, this.spawnPotentials));
/*     */     }
/*     */     
/* 181 */     broadcastEvent(1);
/*     */   }
/*     */   
/*     */   public void load(CompoundTag debug1) {
/* 185 */     this.spawnDelay = debug1.getShort("Delay");
/*     */     
/* 187 */     this.spawnPotentials.clear();
/* 188 */     if (debug1.contains("SpawnPotentials", 9)) {
/* 189 */       ListTag debug2 = debug1.getList("SpawnPotentials", 10);
/*     */       
/* 191 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 192 */         this.spawnPotentials.add(new SpawnData(debug2.getCompound(debug3)));
/*     */       }
/*     */     } 
/*     */     
/* 196 */     if (debug1.contains("SpawnData", 10)) {
/* 197 */       setNextSpawnData(new SpawnData(1, debug1.getCompound("SpawnData")));
/* 198 */     } else if (!this.spawnPotentials.isEmpty()) {
/* 199 */       setNextSpawnData((SpawnData)WeighedRandom.getRandomItem((getLevel()).random, this.spawnPotentials));
/*     */     } 
/*     */     
/* 202 */     if (debug1.contains("MinSpawnDelay", 99)) {
/* 203 */       this.minSpawnDelay = debug1.getShort("MinSpawnDelay");
/* 204 */       this.maxSpawnDelay = debug1.getShort("MaxSpawnDelay");
/* 205 */       this.spawnCount = debug1.getShort("SpawnCount");
/*     */     } 
/*     */     
/* 208 */     if (debug1.contains("MaxNearbyEntities", 99)) {
/* 209 */       this.maxNearbyEntities = debug1.getShort("MaxNearbyEntities");
/* 210 */       this.requiredPlayerRange = debug1.getShort("RequiredPlayerRange");
/*     */     } 
/*     */     
/* 213 */     if (debug1.contains("SpawnRange", 99)) {
/* 214 */       this.spawnRange = debug1.getShort("SpawnRange");
/*     */     }
/*     */     
/* 217 */     if (getLevel() != null) {
/* 218 */       this.displayEntity = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 223 */     ResourceLocation debug2 = getEntityId();
/* 224 */     if (debug2 == null) {
/* 225 */       return debug1;
/*     */     }
/* 227 */     debug1.putShort("Delay", (short)this.spawnDelay);
/* 228 */     debug1.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
/* 229 */     debug1.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
/* 230 */     debug1.putShort("SpawnCount", (short)this.spawnCount);
/* 231 */     debug1.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
/* 232 */     debug1.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
/* 233 */     debug1.putShort("SpawnRange", (short)this.spawnRange);
/* 234 */     debug1.put("SpawnData", (Tag)this.nextSpawnData.getTag().copy());
/*     */     
/* 236 */     ListTag debug3 = new ListTag();
/*     */     
/* 238 */     if (this.spawnPotentials.isEmpty()) {
/* 239 */       debug3.add(this.nextSpawnData.save());
/*     */     } else {
/* 241 */       for (SpawnData debug5 : this.spawnPotentials) {
/* 242 */         debug3.add(debug5.save());
/*     */       }
/*     */     } 
/*     */     
/* 246 */     debug1.put("SpawnPotentials", (Tag)debug3);
/*     */     
/* 248 */     return debug1;
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
/*     */   public boolean onEventTriggered(int debug1) {
/* 265 */     if (debug1 == 1 && (getLevel()).isClientSide) {
/* 266 */       this.spawnDelay = this.minSpawnDelay;
/* 267 */       return true;
/*     */     } 
/* 269 */     return false;
/*     */   }
/*     */   
/*     */   public void setNextSpawnData(SpawnData debug1) {
/* 273 */     this.nextSpawnData = debug1;
/*     */   }
/*     */   
/*     */   public abstract void broadcastEvent(int paramInt);
/*     */   
/*     */   public abstract Level getLevel();
/*     */   
/*     */   public abstract BlockPos getPos();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\BaseSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */