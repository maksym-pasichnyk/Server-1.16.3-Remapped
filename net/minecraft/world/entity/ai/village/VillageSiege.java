/*     */ package net.minecraft.world.entity.ai.village;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class VillageSiege implements CustomSpawner {
/*  21 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private boolean hasSetupSiege;
/*  24 */   private State siegeState = State.SIEGE_DONE;
/*     */   private int zombiesToSpawn;
/*     */   private int nextSpawnTime;
/*     */   private int spawnX;
/*     */   private int spawnY;
/*     */   private int spawnZ;
/*     */   
/*     */   enum State {
/*  32 */     SIEGE_CAN_ACTIVATE,
/*  33 */     SIEGE_TONIGHT,
/*  34 */     SIEGE_DONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tick(ServerLevel debug1, boolean debug2, boolean debug3) {
/*  40 */     if (debug1.isDay() || !debug2) {
/*  41 */       this.siegeState = State.SIEGE_DONE;
/*  42 */       this.hasSetupSiege = false;
/*  43 */       return 0;
/*     */     } 
/*     */     
/*  46 */     float debug4 = debug1.getTimeOfDay(0.0F);
/*  47 */     if (debug4 == 0.5D) {
/*  48 */       this.siegeState = (debug1.random.nextInt(10) == 0) ? State.SIEGE_TONIGHT : State.SIEGE_DONE;
/*     */     }
/*     */     
/*  51 */     if (this.siegeState == State.SIEGE_DONE) {
/*  52 */       return 0;
/*     */     }
/*     */     
/*  55 */     if (!this.hasSetupSiege) {
/*  56 */       if (tryToSetupSiege(debug1)) {
/*  57 */         this.hasSetupSiege = true;
/*     */       } else {
/*  59 */         return 0;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  64 */     if (this.nextSpawnTime > 0) {
/*  65 */       this.nextSpawnTime--;
/*  66 */       return 0;
/*     */     } 
/*     */     
/*  69 */     this.nextSpawnTime = 2;
/*  70 */     if (this.zombiesToSpawn > 0) {
/*  71 */       trySpawn(debug1);
/*  72 */       this.zombiesToSpawn--;
/*     */     } else {
/*  74 */       this.siegeState = State.SIEGE_DONE;
/*     */     } 
/*     */     
/*  77 */     return 1;
/*     */   }
/*     */   
/*     */   private boolean tryToSetupSiege(ServerLevel debug1) {
/*  81 */     for (Player debug3 : debug1.players()) {
/*  82 */       if (!debug3.isSpectator()) {
/*  83 */         BlockPos debug4 = debug3.blockPosition();
/*  84 */         if (!debug1.isVillage(debug4) || debug1.getBiome(debug4).getBiomeCategory() == Biome.BiomeCategory.MUSHROOM) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  89 */         for (int debug5 = 0; debug5 < 10; debug5++) {
/*  90 */           float debug6 = debug1.random.nextFloat() * 6.2831855F;
/*  91 */           this.spawnX = debug4.getX() + Mth.floor(Mth.cos(debug6) * 32.0F);
/*  92 */           this.spawnY = debug4.getY();
/*  93 */           this.spawnZ = debug4.getZ() + Mth.floor(Mth.sin(debug6) * 32.0F);
/*     */           
/*  95 */           if (findRandomSpawnPos(debug1, new BlockPos(this.spawnX, this.spawnY, this.spawnZ)) != null) {
/*  96 */             this.nextSpawnTime = 0;
/*  97 */             this.zombiesToSpawn = 20;
/*     */             break;
/*     */           } 
/*     */         } 
/* 101 */         return true;
/*     */       } 
/*     */     } 
/* 104 */     return false;
/*     */   }
/*     */   private void trySpawn(ServerLevel debug1) {
/*     */     Zombie debug3;
/* 108 */     Vec3 debug2 = findRandomSpawnPos(debug1, new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
/* 109 */     if (debug2 == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 114 */       debug3 = new Zombie((Level)debug1);
/* 115 */       debug3.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug3.blockPosition()), MobSpawnType.EVENT, null, null);
/* 116 */     } catch (Exception debug4) {
/* 117 */       LOGGER.warn("Failed to create zombie for village siege at {}", debug2, debug4);
/*     */       return;
/*     */     } 
/* 120 */     debug3.moveTo(debug2.x, debug2.y, debug2.z, debug1.random.nextFloat() * 360.0F, 0.0F);
/* 121 */     debug1.addFreshEntityWithPassengers((Entity)debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Vec3 findRandomSpawnPos(ServerLevel debug1, BlockPos debug2) {
/* 127 */     for (int debug3 = 0; debug3 < 10; debug3++) {
/* 128 */       int debug4 = debug2.getX() + debug1.random.nextInt(16) - 8;
/* 129 */       int debug5 = debug2.getZ() + debug1.random.nextInt(16) - 8;
/* 130 */       int debug6 = debug1.getHeight(Heightmap.Types.WORLD_SURFACE, debug4, debug5);
/* 131 */       BlockPos debug7 = new BlockPos(debug4, debug6, debug5);
/*     */       
/* 133 */       if (debug1.isVillage(debug7))
/*     */       {
/*     */         
/* 136 */         if (Monster.checkMonsterSpawnRules(EntityType.ZOMBIE, (ServerLevelAccessor)debug1, MobSpawnType.EVENT, debug7, debug1.random))
/* 137 */           return Vec3.atBottomCenterOf((Vec3i)debug7); 
/*     */       }
/*     */     } 
/* 140 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\village\VillageSiege.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */