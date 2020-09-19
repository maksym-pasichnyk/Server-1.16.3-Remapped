/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class SpikeFeature extends Feature<SpikeConfiguration> {
/*  34 */   private static final LoadingCache<Long, List<EndSpike>> SPIKE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(new SpikeCacheLoader());
/*     */   
/*     */   public SpikeFeature(Codec<SpikeConfiguration> debug1) {
/*  37 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static List<EndSpike> getSpikesForLevel(WorldGenLevel debug0) {
/*  41 */     Random debug1 = new Random(debug0.getSeed());
/*  42 */     long debug2 = debug1.nextLong() & 0xFFFFL;
/*  43 */     return (List<EndSpike>)SPIKE_CACHE.getUnchecked(Long.valueOf(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, SpikeConfiguration debug5) {
/*  48 */     List<EndSpike> debug6 = debug5.getSpikes();
/*  49 */     if (debug6.isEmpty()) {
/*  50 */       debug6 = getSpikesForLevel(debug1);
/*     */     }
/*     */     
/*  53 */     for (EndSpike debug8 : debug6) {
/*  54 */       if (debug8.isCenterWithinChunk(debug4)) {
/*  55 */         placeSpike((ServerLevelAccessor)debug1, debug3, debug5, debug8);
/*     */       }
/*     */     } 
/*     */     
/*  59 */     return true;
/*     */   }
/*     */   
/*     */   private void placeSpike(ServerLevelAccessor debug1, Random debug2, SpikeConfiguration debug3, EndSpike debug4) {
/*  63 */     int debug5 = debug4.getRadius();
/*  64 */     for (BlockPos debug7 : BlockPos.betweenClosed(new BlockPos(debug4.getCenterX() - debug5, 0, debug4.getCenterZ() - debug5), new BlockPos(debug4.getCenterX() + debug5, debug4.getHeight() + 10, debug4.getCenterZ() + debug5))) {
/*  65 */       if (debug7.distSqr(debug4.getCenterX(), debug7.getY(), debug4.getCenterZ(), false) <= (debug5 * debug5 + 1) && debug7.getY() < debug4.getHeight()) {
/*  66 */         setBlock((LevelWriter)debug1, debug7, Blocks.OBSIDIAN.defaultBlockState()); continue;
/*  67 */       }  if (debug7.getY() > 65) {
/*  68 */         setBlock((LevelWriter)debug1, debug7, Blocks.AIR.defaultBlockState());
/*     */       }
/*     */     } 
/*     */     
/*  72 */     if (debug4.isGuarded()) {
/*  73 */       int i = -2;
/*  74 */       int debug7 = 2;
/*  75 */       int debug8 = 3;
/*     */       
/*  77 */       BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/*  78 */       for (int debug10 = -2; debug10 <= 2; debug10++) {
/*  79 */         for (int debug11 = -2; debug11 <= 2; debug11++) {
/*  80 */           for (int debug12 = 0; debug12 <= 3; debug12++) {
/*  81 */             boolean debug13 = (Mth.abs(debug10) == 2);
/*  82 */             boolean debug14 = (Mth.abs(debug11) == 2);
/*  83 */             boolean debug15 = (debug12 == 3);
/*     */             
/*  85 */             if (debug13 || debug14 || debug15) {
/*  86 */               boolean debug16 = (debug10 == -2 || debug10 == 2 || debug15);
/*  87 */               boolean debug17 = (debug11 == -2 || debug11 == 2 || debug15);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  93 */               BlockState debug18 = (BlockState)((BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf((debug16 && debug11 != -2)))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf((debug16 && debug11 != 2)))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf((debug17 && debug10 != -2)))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf((debug17 && debug10 != 2)));
/*     */               
/*  95 */               setBlock((LevelWriter)debug1, (BlockPos)debug9.set(debug4.getCenterX() + debug10, debug4.getHeight() + debug12, debug4.getCenterZ() + debug11), debug18);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     EndCrystal debug6 = (EndCrystal)EntityType.END_CRYSTAL.create((Level)debug1.getLevel());
/* 103 */     debug6.setBeamTarget(debug3.getCrystalBeamTarget());
/* 104 */     debug6.setInvulnerable(debug3.isCrystalInvulnerable());
/* 105 */     debug6.moveTo(debug4.getCenterX() + 0.5D, (debug4.getHeight() + 1), debug4.getCenterZ() + 0.5D, debug2.nextFloat() * 360.0F, 0.0F);
/* 106 */     debug1.addFreshEntity((Entity)debug6);
/* 107 */     setBlock((LevelWriter)debug1, new BlockPos(debug4.getCenterX(), debug4.getHeight(), debug4.getCenterZ()), Blocks.BEDROCK.defaultBlockState());
/*     */   }
/*     */   public static class EndSpike { public static final Codec<EndSpike> CODEC;
/*     */     static {
/* 111 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("centerX").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("centerZ").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("radius").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.INT.fieldOf("height").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.BOOL.fieldOf("guarded").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)debug0, EndSpike::new));
/*     */     }
/*     */ 
/*     */     
/*     */     private final int centerX;
/*     */     
/*     */     private final int centerZ;
/*     */     
/*     */     private final int radius;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final boolean guarded;
/*     */     private final AABB topBoundingBox;
/*     */     
/*     */     public EndSpike(int debug1, int debug2, int debug3, int debug4, boolean debug5) {
/* 127 */       this.centerX = debug1;
/* 128 */       this.centerZ = debug2;
/* 129 */       this.radius = debug3;
/* 130 */       this.height = debug4;
/* 131 */       this.guarded = debug5;
/*     */       
/* 133 */       this.topBoundingBox = new AABB((debug1 - debug3), 0.0D, (debug2 - debug3), (debug1 + debug3), 256.0D, (debug2 + debug3));
/*     */     }
/*     */     
/*     */     public boolean isCenterWithinChunk(BlockPos debug1) {
/* 137 */       return (debug1.getX() >> 4 == this.centerX >> 4 && debug1
/* 138 */         .getZ() >> 4 == this.centerZ >> 4);
/*     */     }
/*     */     
/*     */     public int getCenterX() {
/* 142 */       return this.centerX;
/*     */     }
/*     */     
/*     */     public int getCenterZ() {
/* 146 */       return this.centerZ;
/*     */     }
/*     */     
/*     */     public int getRadius() {
/* 150 */       return this.radius;
/*     */     }
/*     */     
/*     */     public int getHeight() {
/* 154 */       return this.height;
/*     */     }
/*     */     
/*     */     public boolean isGuarded() {
/* 158 */       return this.guarded;
/*     */     }
/*     */     
/*     */     public AABB getTopBoundingBox() {
/* 162 */       return this.topBoundingBox;
/*     */     } }
/*     */   
/*     */   static class SpikeCacheLoader extends CacheLoader<Long, List<EndSpike>> {
/*     */     private SpikeCacheLoader() {}
/*     */     
/*     */     public List<SpikeFeature.EndSpike> load(Long debug1) {
/* 169 */       List<Integer> debug2 = IntStream.range(0, 10).boxed().collect((Collector)Collectors.toList());
/* 170 */       Collections.shuffle(debug2, new Random(debug1.longValue()));
/*     */       
/* 172 */       List<SpikeFeature.EndSpike> debug3 = Lists.newArrayList();
/* 173 */       for (int debug4 = 0; debug4 < 10; debug4++) {
/* 174 */         int debug5 = Mth.floor(42.0D * Math.cos(2.0D * (-3.141592653589793D + 0.3141592653589793D * debug4)));
/* 175 */         int debug6 = Mth.floor(42.0D * Math.sin(2.0D * (-3.141592653589793D + 0.3141592653589793D * debug4)));
/* 176 */         int debug7 = ((Integer)debug2.get(debug4)).intValue();
/* 177 */         int debug8 = 2 + debug7 / 3;
/* 178 */         int debug9 = 76 + debug7 * 3;
/* 179 */         boolean debug10 = (debug7 == 1 || debug7 == 2);
/* 180 */         debug3.add(new SpikeFeature.EndSpike(debug5, debug6, debug8, debug9, debug10));
/*     */       } 
/* 182 */       return debug3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SpikeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */