/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Cursor3D;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CollisionSpliterator
/*     */   extends Spliterators.AbstractSpliterator<VoxelShape>
/*     */ {
/*     */   @Nullable
/*     */   private final Entity source;
/*     */   private final AABB box;
/*     */   private final CollisionContext context;
/*     */   private final Cursor3D cursor;
/*     */   private final BlockPos.MutableBlockPos pos;
/*     */   private final VoxelShape entityShape;
/*     */   private final CollisionGetter collisionGetter;
/*     */   private boolean needsBorderCheck;
/*     */   private final BiPredicate<BlockState, BlockPos> predicate;
/*     */   
/*     */   public CollisionSpliterator(CollisionGetter debug1, @Nullable Entity debug2, AABB debug3) {
/*  36 */     this(debug1, debug2, debug3, (debug0, debug1) -> true);
/*     */   }
/*     */   
/*     */   public CollisionSpliterator(CollisionGetter debug1, @Nullable Entity debug2, AABB debug3, BiPredicate<BlockState, BlockPos> debug4) {
/*  40 */     super(Long.MAX_VALUE, 1280);
/*  41 */     this.context = (debug2 == null) ? CollisionContext.empty() : CollisionContext.of(debug2);
/*  42 */     this.pos = new BlockPos.MutableBlockPos();
/*  43 */     this.entityShape = Shapes.create(debug3);
/*  44 */     this.collisionGetter = debug1;
/*  45 */     this.needsBorderCheck = (debug2 != null);
/*  46 */     this.source = debug2;
/*  47 */     this.box = debug3;
/*  48 */     this.predicate = debug4;
/*     */ 
/*     */     
/*  51 */     int debug5 = Mth.floor(debug3.minX - 1.0E-7D) - 1;
/*  52 */     int debug6 = Mth.floor(debug3.maxX + 1.0E-7D) + 1;
/*  53 */     int debug7 = Mth.floor(debug3.minY - 1.0E-7D) - 1;
/*  54 */     int debug8 = Mth.floor(debug3.maxY + 1.0E-7D) + 1;
/*  55 */     int debug9 = Mth.floor(debug3.minZ - 1.0E-7D) - 1;
/*  56 */     int debug10 = Mth.floor(debug3.maxZ + 1.0E-7D) + 1;
/*  57 */     this.cursor = new Cursor3D(debug5, debug7, debug9, debug6, debug8, debug10);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryAdvance(Consumer<? super VoxelShape> debug1) {
/*  62 */     return ((this.needsBorderCheck && worldBorderCheck(debug1)) || collisionCheck(debug1));
/*     */   }
/*     */   
/*     */   boolean collisionCheck(Consumer<? super VoxelShape> debug1) {
/*  66 */     while (this.cursor.advance()) {
/*  67 */       int debug2 = this.cursor.nextX();
/*  68 */       int debug3 = this.cursor.nextY();
/*  69 */       int debug4 = this.cursor.nextZ();
/*     */       
/*  71 */       int debug5 = this.cursor.getNextType();
/*     */       
/*  73 */       if (debug5 == 3) {
/*     */         continue;
/*     */       }
/*     */       
/*  77 */       BlockGetter debug6 = getChunk(debug2, debug4);
/*     */       
/*  79 */       if (debug6 == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  83 */       this.pos.set(debug2, debug3, debug4);
/*  84 */       BlockState debug7 = debug6.getBlockState((BlockPos)this.pos);
/*     */       
/*  86 */       if (!this.predicate.test(debug7, this.pos)) {
/*     */         continue;
/*     */       }
/*     */       
/*  90 */       if (debug5 == 1 && !debug7.hasLargeCollisionShape()) {
/*     */         continue;
/*     */       }
/*  93 */       if (debug5 == 2 && !debug7.is(Blocks.MOVING_PISTON)) {
/*     */         continue;
/*     */       }
/*     */       
/*  97 */       VoxelShape debug8 = debug7.getCollisionShape(this.collisionGetter, (BlockPos)this.pos, this.context);
/*     */       
/*  99 */       if (debug8 == Shapes.block()) {
/* 100 */         if (this.box.intersects(debug2, debug3, debug4, debug2 + 1.0D, debug3 + 1.0D, debug4 + 1.0D)) {
/* 101 */           debug1.accept(debug8.move(debug2, debug3, debug4));
/* 102 */           return true;
/*     */         }  continue;
/*     */       } 
/* 105 */       VoxelShape debug9 = debug8.move(debug2, debug3, debug4);
/* 106 */       if (Shapes.joinIsNotEmpty(debug9, this.entityShape, BooleanOp.AND)) {
/* 107 */         debug1.accept(debug9);
/* 108 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockGetter getChunk(int debug1, int debug2) {
/* 117 */     int debug3 = debug1 >> 4;
/* 118 */     int debug4 = debug2 >> 4;
/*     */     
/* 120 */     return this.collisionGetter.getChunkForCollisions(debug3, debug4);
/*     */   }
/*     */   
/*     */   boolean worldBorderCheck(Consumer<? super VoxelShape> debug1) {
/* 124 */     Objects.requireNonNull(this.source);
/* 125 */     this.needsBorderCheck = false;
/* 126 */     WorldBorder debug2 = this.collisionGetter.getWorldBorder();
/* 127 */     AABB debug3 = this.source.getBoundingBox();
/* 128 */     if (!isBoxFullyWithinWorldBorder(debug2, debug3)) {
/* 129 */       VoxelShape debug4 = debug2.getCollisionShape();
/* 130 */       if (!isOutsideBorder(debug4, debug3) && isCloseToBorder(debug4, debug3)) {
/* 131 */         debug1.accept(debug4);
/* 132 */         return true;
/*     */       } 
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isCloseToBorder(VoxelShape debug0, AABB debug1) {
/* 139 */     return Shapes.joinIsNotEmpty(debug0, Shapes.create(debug1.inflate(1.0E-7D)), BooleanOp.AND);
/*     */   }
/*     */   
/*     */   private static boolean isOutsideBorder(VoxelShape debug0, AABB debug1) {
/* 143 */     return Shapes.joinIsNotEmpty(debug0, Shapes.create(debug1.deflate(1.0E-7D)), BooleanOp.AND);
/*     */   }
/*     */   
/*     */   public static boolean isBoxFullyWithinWorldBorder(WorldBorder debug0, AABB debug1) {
/* 147 */     double debug2 = Mth.floor(debug0.getMinX());
/* 148 */     double debug4 = Mth.floor(debug0.getMinZ());
/*     */     
/* 150 */     double debug6 = Mth.ceil(debug0.getMaxX());
/* 151 */     double debug8 = Mth.ceil(debug0.getMaxZ());
/*     */     
/* 153 */     return (debug1.minX > debug2 && debug1.minX < debug6 && debug1.minZ > debug4 && debug1.minZ < debug8 && debug1.maxX > debug2 && debug1.maxX < debug6 && debug1.maxZ > debug4 && debug1.maxZ < debug8);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\CollisionSpliterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */