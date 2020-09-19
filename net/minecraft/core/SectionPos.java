/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SectionPos
/*     */   extends Vec3i
/*     */ {
/*     */   private SectionPos(int debug1, int debug2, int debug3) {
/*  45 */     super(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public static SectionPos of(int debug0, int debug1, int debug2) {
/*  49 */     return new SectionPos(debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public static SectionPos of(BlockPos debug0) {
/*  53 */     return new SectionPos(blockToSectionCoord(debug0.getX()), blockToSectionCoord(debug0.getY()), blockToSectionCoord(debug0.getZ()));
/*     */   }
/*     */   
/*     */   public static SectionPos of(ChunkPos debug0, int debug1) {
/*  57 */     return new SectionPos(debug0.x, debug1, debug0.z);
/*     */   }
/*     */   
/*     */   public static SectionPos of(Entity debug0) {
/*  61 */     return new SectionPos(
/*  62 */         blockToSectionCoord(Mth.floor(debug0.getX())), 
/*  63 */         blockToSectionCoord(Mth.floor(debug0.getY())), 
/*  64 */         blockToSectionCoord(Mth.floor(debug0.getZ())));
/*     */   }
/*     */ 
/*     */   
/*     */   public static SectionPos of(long debug0) {
/*  69 */     return new SectionPos(x(debug0), y(debug0), z(debug0));
/*     */   }
/*     */   
/*     */   public static long offset(long debug0, Direction debug2) {
/*  73 */     return offset(debug0, debug2.getStepX(), debug2.getStepY(), debug2.getStepZ());
/*     */   }
/*     */   
/*     */   public static long offset(long debug0, int debug2, int debug3, int debug4) {
/*  77 */     return asLong(x(debug0) + debug2, y(debug0) + debug3, z(debug0) + debug4);
/*     */   }
/*     */   
/*     */   public static int blockToSectionCoord(int debug0) {
/*  81 */     return debug0 >> 4;
/*     */   }
/*     */   
/*     */   public static int sectionRelative(int debug0) {
/*  85 */     return debug0 & 0xF;
/*     */   }
/*     */   
/*     */   public static short sectionRelativePos(BlockPos debug0) {
/*  89 */     int debug1 = sectionRelative(debug0.getX());
/*  90 */     int debug2 = sectionRelative(debug0.getY());
/*  91 */     int debug3 = sectionRelative(debug0.getZ());
/*  92 */     return (short)(debug1 << 8 | debug3 << 4 | debug2 << 0);
/*     */   }
/*     */   
/*     */   public static int sectionRelativeX(short debug0) {
/*  96 */     return debug0 >>> 8 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sectionRelativeY(short debug0) {
/* 100 */     return debug0 >>> 0 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sectionRelativeZ(short debug0) {
/* 104 */     return debug0 >>> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public int relativeToBlockX(short debug1) {
/* 108 */     return minBlockX() + sectionRelativeX(debug1);
/*     */   }
/*     */   
/*     */   public int relativeToBlockY(short debug1) {
/* 112 */     return minBlockY() + sectionRelativeY(debug1);
/*     */   }
/*     */   
/*     */   public int relativeToBlockZ(short debug1) {
/* 116 */     return minBlockZ() + sectionRelativeZ(debug1);
/*     */   }
/*     */   
/*     */   public BlockPos relativeToBlockPos(short debug1) {
/* 120 */     return new BlockPos(relativeToBlockX(debug1), relativeToBlockY(debug1), relativeToBlockZ(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int sectionToBlockCoord(int debug0) {
/* 128 */     return debug0 << 4;
/*     */   }
/*     */   
/*     */   public static int x(long debug0) {
/* 132 */     return (int)(debug0 << 0L >> 42L);
/*     */   }
/*     */   
/*     */   public static int y(long debug0) {
/* 136 */     return (int)(debug0 << 44L >> 44L);
/*     */   }
/*     */   
/*     */   public static int z(long debug0) {
/* 140 */     return (int)(debug0 << 22L >> 42L);
/*     */   }
/*     */   
/*     */   public int x() {
/* 144 */     return getX();
/*     */   }
/*     */   
/*     */   public int y() {
/* 148 */     return getY();
/*     */   }
/*     */   
/*     */   public int z() {
/* 152 */     return getZ();
/*     */   }
/*     */   
/*     */   public int minBlockX() {
/* 156 */     return x() << 4;
/*     */   }
/*     */   
/*     */   public int minBlockY() {
/* 160 */     return y() << 4;
/*     */   }
/*     */   
/*     */   public int minBlockZ() {
/* 164 */     return z() << 4;
/*     */   }
/*     */   
/*     */   public int maxBlockX() {
/* 168 */     return (x() << 4) + 15;
/*     */   }
/*     */   
/*     */   public int maxBlockY() {
/* 172 */     return (y() << 4) + 15;
/*     */   }
/*     */   
/*     */   public int maxBlockZ() {
/* 176 */     return (z() << 4) + 15;
/*     */   }
/*     */   
/*     */   public static long blockToSection(long debug0) {
/* 180 */     return asLong(
/* 181 */         blockToSectionCoord(BlockPos.getX(debug0)), 
/* 182 */         blockToSectionCoord(BlockPos.getY(debug0)), 
/* 183 */         blockToSectionCoord(BlockPos.getZ(debug0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getZeroNode(long debug0) {
/* 189 */     return debug0 & 0xFFFFFFFFFFF00000L;
/*     */   }
/*     */   
/*     */   public BlockPos origin() {
/* 193 */     return new BlockPos(sectionToBlockCoord(x()), sectionToBlockCoord(y()), sectionToBlockCoord(z()));
/*     */   }
/*     */   
/*     */   public BlockPos center() {
/* 197 */     int debug1 = 8;
/* 198 */     return origin().offset(8, 8, 8);
/*     */   }
/*     */   
/*     */   public ChunkPos chunk() {
/* 202 */     return new ChunkPos(x(), z());
/*     */   }
/*     */   
/*     */   public static long asLong(int debug0, int debug1, int debug2) {
/* 206 */     long debug3 = 0L;
/* 207 */     debug3 |= (debug0 & 0x3FFFFFL) << 42L;
/* 208 */     debug3 |= (debug1 & 0xFFFFFL) << 0L;
/* 209 */     debug3 |= (debug2 & 0x3FFFFFL) << 20L;
/* 210 */     return debug3;
/*     */   }
/*     */   
/*     */   public long asLong() {
/* 214 */     return asLong(x(), y(), z());
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
/*     */   public Stream<BlockPos> blocksInside() {
/* 226 */     return BlockPos.betweenClosedStream(minBlockX(), minBlockY(), minBlockZ(), maxBlockX(), maxBlockY(), maxBlockZ());
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> cube(SectionPos debug0, int debug1) {
/* 230 */     int debug2 = debug0.x();
/* 231 */     int debug3 = debug0.y();
/* 232 */     int debug4 = debug0.z();
/* 233 */     return betweenClosedStream(debug2 - debug1, debug3 - debug1, debug4 - debug1, debug2 + debug1, debug3 + debug1, debug4 + debug1);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> aroundChunk(ChunkPos debug0, int debug1) {
/* 237 */     int debug2 = debug0.x;
/* 238 */     int debug3 = debug0.z;
/* 239 */     return betweenClosedStream(debug2 - debug1, 0, debug3 - debug1, debug2 + debug1, 15, debug3 + debug1);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> betweenClosedStream(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
/* 243 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<SectionPos>(((maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)), 64) {
/* 244 */           final Cursor3D cursor = new Cursor3D(minX, minY, minZ, maxX, maxY, maxZ);
/*     */ 
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super SectionPos> debug1) {
/* 248 */             if (this.cursor.advance()) {
/* 249 */               debug1.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
/* 250 */               return true;
/*     */             } 
/* 252 */             return false;
/*     */           }
/*     */         }false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\SectionPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */