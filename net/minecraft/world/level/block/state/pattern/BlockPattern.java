/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ public class BlockPattern
/*     */ {
/*     */   private final Predicate<BlockInWorld>[][][] pattern;
/*     */   private final int depth;
/*     */   private final int height;
/*     */   private final int width;
/*     */   
/*     */   public BlockPattern(Predicate<BlockInWorld>[][][] debug1) {
/*  22 */     this.pattern = debug1;
/*     */     
/*  24 */     this.depth = debug1.length;
/*     */     
/*  26 */     if (this.depth > 0) {
/*  27 */       this.height = (debug1[0]).length;
/*     */       
/*  29 */       if (this.height > 0) {
/*  30 */         this.width = (debug1[0][0]).length;
/*     */       } else {
/*  32 */         this.width = 0;
/*     */       } 
/*     */     } else {
/*  35 */       this.height = 0;
/*  36 */       this.width = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getDepth() {
/*  41 */     return this.depth;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  45 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  49 */     return this.width;
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
/*     */   @Nullable
/*     */   private BlockPatternMatch matches(BlockPos debug1, Direction debug2, Direction debug3, LoadingCache<BlockPos, BlockInWorld> debug4) {
/*  64 */     for (int debug5 = 0; debug5 < this.width; debug5++) {
/*  65 */       for (int debug6 = 0; debug6 < this.height; debug6++) {
/*  66 */         for (int debug7 = 0; debug7 < this.depth; debug7++) {
/*  67 */           if (!this.pattern[debug7][debug6][debug5].test(debug4.getUnchecked(translateAndRotate(debug1, debug2, debug3, debug5, debug6, debug7)))) {
/*  68 */             return null;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     return new BlockPatternMatch(debug1, debug2, debug3, debug4, this.width, this.height, this.depth);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPatternMatch find(LevelReader debug1, BlockPos debug2) {
/*  79 */     LoadingCache<BlockPos, BlockInWorld> debug3 = createLevelCache(debug1, false);
/*     */     
/*  81 */     int debug4 = Math.max(Math.max(this.width, this.height), this.depth);
/*     */     
/*  83 */     for (BlockPos debug6 : BlockPos.betweenClosed(debug2, debug2.offset(debug4 - 1, debug4 - 1, debug4 - 1))) {
/*  84 */       for (Direction debug10 : Direction.values()) {
/*  85 */         for (Direction debug14 : Direction.values()) {
/*  86 */           if (debug14 != debug10 && debug14 != debug10.getOpposite()) {
/*     */ 
/*     */ 
/*     */             
/*  90 */             BlockPatternMatch debug15 = matches(debug6, debug10, debug14, debug3);
/*  91 */             if (debug15 != null) {
/*  92 */               return debug15;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public static LoadingCache<BlockPos, BlockInWorld> createLevelCache(LevelReader debug0, boolean debug1) {
/* 102 */     return CacheBuilder.newBuilder().build(new BlockCacheLoader(debug0, debug1));
/*     */   }
/*     */   
/*     */   protected static BlockPos translateAndRotate(BlockPos debug0, Direction debug1, Direction debug2, int debug3, int debug4, int debug5) {
/* 106 */     if (debug1 == debug2 || debug1 == debug2.getOpposite()) {
/* 107 */       throw new IllegalArgumentException("Invalid forwards & up combination");
/*     */     }
/*     */     
/* 110 */     Vec3i debug6 = new Vec3i(debug1.getStepX(), debug1.getStepY(), debug1.getStepZ());
/* 111 */     Vec3i debug7 = new Vec3i(debug2.getStepX(), debug2.getStepY(), debug2.getStepZ());
/* 112 */     Vec3i debug8 = debug6.cross(debug7);
/*     */     
/* 114 */     return debug0.offset(debug7
/* 115 */         .getX() * -debug4 + debug8.getX() * debug3 + debug6.getX() * debug5, debug7
/* 116 */         .getY() * -debug4 + debug8.getY() * debug3 + debug6.getY() * debug5, debug7
/* 117 */         .getZ() * -debug4 + debug8.getZ() * debug3 + debug6.getZ() * debug5);
/*     */   }
/*     */   
/*     */   static class BlockCacheLoader
/*     */     extends CacheLoader<BlockPos, BlockInWorld> {
/*     */     private final LevelReader level;
/*     */     private final boolean loadChunks;
/*     */     
/*     */     public BlockCacheLoader(LevelReader debug1, boolean debug2) {
/* 126 */       this.level = debug1;
/* 127 */       this.loadChunks = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockInWorld load(BlockPos debug1) throws Exception {
/* 132 */       return new BlockInWorld(this.level, debug1, this.loadChunks);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockPatternMatch {
/*     */     private final BlockPos frontTopLeft;
/*     */     private final Direction forwards;
/*     */     private final Direction up;
/*     */     private final LoadingCache<BlockPos, BlockInWorld> cache;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private final int depth;
/*     */     
/*     */     public BlockPatternMatch(BlockPos debug1, Direction debug2, Direction debug3, LoadingCache<BlockPos, BlockInWorld> debug4, int debug5, int debug6, int debug7) {
/* 146 */       this.frontTopLeft = debug1;
/* 147 */       this.forwards = debug2;
/* 148 */       this.up = debug3;
/* 149 */       this.cache = debug4;
/* 150 */       this.width = debug5;
/* 151 */       this.height = debug6;
/* 152 */       this.depth = debug7;
/*     */     }
/*     */     
/*     */     public BlockPos getFrontTopLeft() {
/* 156 */       return this.frontTopLeft;
/*     */     }
/*     */     
/*     */     public Direction getForwards() {
/* 160 */       return this.forwards;
/*     */     }
/*     */     
/*     */     public Direction getUp() {
/* 164 */       return this.up;
/*     */     }
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
/*     */     public BlockInWorld getBlock(int debug1, int debug2, int debug3) {
/* 180 */       return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, getForwards(), getUp(), debug1, debug2, debug3));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 185 */       return MoreObjects.toStringHelper(this)
/* 186 */         .add("up", this.up)
/* 187 */         .add("forwards", this.forwards)
/* 188 */         .add("frontTopLeft", this.frontTopLeft)
/* 189 */         .toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */