/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class RegionFile
/*     */   implements AutoCloseable
/*     */ {
/*  86 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   private static final ByteBuffer PADDING_BUFFER = ByteBuffer.allocateDirect(1);
/*     */   
/*     */   private final FileChannel file;
/*     */   
/*     */   private final Path externalFileDir;
/*     */   
/*     */   private final RegionFileVersion version;
/*     */   
/* 103 */   private final ByteBuffer header = ByteBuffer.allocateDirect(8192);
/*     */   private final IntBuffer offsets;
/*     */   private final IntBuffer timestamps;
/*     */   @VisibleForTesting
/* 107 */   protected final RegionBitmap usedSectors = new RegionBitmap();
/*     */ 
/*     */   
/*     */   public RegionFile(File debug1, File debug2, boolean debug3) throws IOException {
/* 111 */     this(debug1.toPath(), debug2.toPath(), RegionFileVersion.VERSION_DEFLATE, debug3);
/*     */   }
/*     */   
/*     */   public RegionFile(Path debug1, Path debug2, RegionFileVersion debug3, boolean debug4) throws IOException {
/* 115 */     this.version = debug3;
/* 116 */     if (!Files.isDirectory(debug2, new java.nio.file.LinkOption[0])) {
/* 117 */       throw new IllegalArgumentException("Expected directory, got " + debug2.toAbsolutePath());
/*     */     }
/* 119 */     this.externalFileDir = debug2;
/* 120 */     this.offsets = this.header.asIntBuffer();
/* 121 */     this.offsets.limit(1024);
/* 122 */     this.header.position(4096);
/* 123 */     this.timestamps = this.header.asIntBuffer();
/*     */     
/* 125 */     if (debug4) {
/* 126 */       this.file = FileChannel.open(debug1, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC });
/*     */     } else {
/* 128 */       this.file = FileChannel.open(debug1, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE });
/*     */     } 
/*     */ 
/*     */     
/* 132 */     this.usedSectors.force(0, 2);
/*     */     
/* 134 */     this.header.position(0);
/* 135 */     int debug5 = this.file.read(this.header, 0L);
/* 136 */     if (debug5 != -1) {
/* 137 */       if (debug5 != 8192) {
/* 138 */         LOGGER.warn("Region file {} has truncated header: {}", debug1, Integer.valueOf(debug5));
/*     */       }
/*     */       
/* 141 */       long debug6 = Files.size(debug1);
/* 142 */       for (int debug8 = 0; debug8 < 1024; debug8++) {
/* 143 */         int debug9 = this.offsets.get(debug8);
/* 144 */         if (debug9 != 0) {
/* 145 */           int debug10 = getSectorNumber(debug9);
/* 146 */           int debug11 = getNumSectors(debug9);
/* 147 */           if (debug10 < 2) {
/* 148 */             LOGGER.warn("Region file {} has invalid sector at index: {}; sector {} overlaps with header", debug1, Integer.valueOf(debug8), Integer.valueOf(debug10));
/* 149 */             this.offsets.put(debug8, 0);
/* 150 */           } else if (debug11 == 0) {
/* 151 */             LOGGER.warn("Region file {} has an invalid sector at index: {}; size has to be > 0", debug1, Integer.valueOf(debug8));
/* 152 */             this.offsets.put(debug8, 0);
/* 153 */           } else if (debug10 * 4096L > debug6) {
/* 154 */             LOGGER.warn("Region file {} has an invalid sector at index: {}; sector {} is out of bounds", debug1, Integer.valueOf(debug8), Integer.valueOf(debug10));
/* 155 */             this.offsets.put(debug8, 0);
/*     */           } else {
/* 157 */             this.usedSectors.force(debug10, debug11);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Path getExternalChunkPath(ChunkPos debug1) {
/* 165 */     String debug2 = "c." + debug1.x + "." + debug1.z + ".mcc";
/* 166 */     return this.externalFileDir.resolve(debug2);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public synchronized DataInputStream getChunkDataInputStream(ChunkPos debug1) throws IOException {
/* 171 */     int debug2 = getOffset(debug1);
/* 172 */     if (debug2 == 0) {
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     int debug3 = getSectorNumber(debug2);
/* 177 */     int debug4 = getNumSectors(debug2);
/*     */     
/* 179 */     int debug5 = debug4 * 4096;
/* 180 */     ByteBuffer debug6 = ByteBuffer.allocate(debug5);
/* 181 */     this.file.read(debug6, (debug3 * 4096));
/* 182 */     debug6.flip();
/*     */     
/* 184 */     if (debug6.remaining() < 5) {
/* 185 */       LOGGER.error("Chunk {} header is truncated: expected {} but read {}", debug1, Integer.valueOf(debug5), Integer.valueOf(debug6.remaining()));
/* 186 */       return null;
/*     */     } 
/*     */     
/* 189 */     int debug7 = debug6.getInt();
/* 190 */     byte debug8 = debug6.get();
/*     */     
/* 192 */     if (debug7 == 0) {
/* 193 */       LOGGER.warn("Chunk {} is allocated, but stream is missing", debug1);
/* 194 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     int debug9 = debug7 - 1;
/*     */     
/* 200 */     if (isExternalStreamChunk(debug8)) {
/* 201 */       if (debug9 != 0) {
/* 202 */         LOGGER.warn("Chunk has both internal and external streams");
/*     */       }
/* 204 */       return createExternalChunkInputStream(debug1, getExternalChunkVersion(debug8));
/*     */     } 
/*     */     
/* 207 */     if (debug9 > debug6.remaining()) {
/* 208 */       LOGGER.error("Chunk {} stream is truncated: expected {} but read {}", debug1, Integer.valueOf(debug9), Integer.valueOf(debug6.remaining()));
/* 209 */       return null;
/*     */     } 
/*     */     
/* 212 */     if (debug9 < 0) {
/* 213 */       LOGGER.error("Declared size {} of chunk {} is negative", Integer.valueOf(debug7), debug1);
/* 214 */       return null;
/*     */     } 
/*     */     
/* 217 */     return createChunkInputStream(debug1, debug8, createStream(debug6, debug9));
/*     */   }
/*     */   
/*     */   private static boolean isExternalStreamChunk(byte debug0) {
/* 221 */     return ((debug0 & 0x80) != 0);
/*     */   }
/*     */   
/*     */   private static byte getExternalChunkVersion(byte debug0) {
/* 225 */     return (byte)(debug0 & 0xFFFFFF7F);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private DataInputStream createChunkInputStream(ChunkPos debug1, byte debug2, InputStream debug3) throws IOException {
/* 230 */     RegionFileVersion debug4 = RegionFileVersion.fromId(debug2);
/* 231 */     if (debug4 == null) {
/* 232 */       LOGGER.error("Chunk {} has invalid chunk stream version {}", debug1, Byte.valueOf(debug2));
/* 233 */       return null;
/*     */     } 
/* 235 */     return new DataInputStream(new BufferedInputStream(debug4.wrap(debug3)));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private DataInputStream createExternalChunkInputStream(ChunkPos debug1, byte debug2) throws IOException {
/* 240 */     Path debug3 = getExternalChunkPath(debug1);
/* 241 */     if (!Files.isRegularFile(debug3, new java.nio.file.LinkOption[0])) {
/* 242 */       LOGGER.error("External chunk path {} is not file", debug3);
/* 243 */       return null;
/*     */     } 
/*     */     
/* 246 */     return createChunkInputStream(debug1, debug2, Files.newInputStream(debug3, new OpenOption[0]));
/*     */   }
/*     */   
/*     */   private static ByteArrayInputStream createStream(ByteBuffer debug0, int debug1) {
/* 250 */     return new ByteArrayInputStream(debug0.array(), debug0.position(), debug1);
/*     */   }
/*     */   
/*     */   private int packSectorOffset(int debug1, int debug2) {
/* 254 */     return debug1 << 8 | debug2;
/*     */   }
/*     */   
/*     */   private static int getNumSectors(int debug0) {
/* 258 */     return debug0 & 0xFF;
/*     */   }
/*     */   
/*     */   private static int getSectorNumber(int debug0) {
/* 262 */     return debug0 >> 8 & 0xFFFFFF;
/*     */   }
/*     */   
/*     */   private static int sizeToSectors(int debug0) {
/* 266 */     return (debug0 + 4096 - 1) / 4096;
/*     */   }
/*     */   
/*     */   public boolean doesChunkExist(ChunkPos debug1) {
/* 270 */     int debug2 = getOffset(debug1);
/* 271 */     if (debug2 == 0) {
/* 272 */       return false;
/*     */     }
/*     */     
/* 275 */     int debug3 = getSectorNumber(debug2);
/* 276 */     int debug4 = getNumSectors(debug2);
/*     */     
/* 278 */     ByteBuffer debug5 = ByteBuffer.allocate(5);
/*     */     try {
/* 280 */       this.file.read(debug5, (debug3 * 4096));
/* 281 */       debug5.flip();
/* 282 */       if (debug5.remaining() != 5) {
/* 283 */         return false;
/*     */       }
/*     */       
/* 286 */       int debug6 = debug5.getInt();
/* 287 */       byte debug7 = debug5.get();
/* 288 */       if (isExternalStreamChunk(debug7)) {
/* 289 */         if (!RegionFileVersion.isValidVersion(getExternalChunkVersion(debug7))) {
/* 290 */           return false;
/*     */         }
/*     */         
/* 293 */         if (!Files.isRegularFile(getExternalChunkPath(debug1), new java.nio.file.LinkOption[0])) {
/* 294 */           return false;
/*     */         }
/*     */       } else {
/* 297 */         if (!RegionFileVersion.isValidVersion(debug7)) {
/* 298 */           return false;
/*     */         }
/*     */         
/* 301 */         if (debug6 == 0) {
/* 302 */           return false;
/*     */         }
/*     */         
/* 305 */         int debug8 = debug6 - 1;
/* 306 */         if (debug8 < 0 || debug8 > 4096 * debug4) {
/* 307 */           return false;
/*     */         }
/*     */       } 
/* 310 */     } catch (IOException debug6) {
/* 311 */       return false;
/*     */     } 
/*     */     
/* 314 */     return true;
/*     */   }
/*     */   
/*     */   public DataOutputStream getChunkDataOutputStream(ChunkPos debug1) throws IOException {
/* 318 */     return new DataOutputStream(new BufferedOutputStream(this.version.wrap(new ChunkBuffer(debug1))));
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/* 322 */     this.file.force(true);
/*     */   }
/*     */   
/*     */   static interface CommitOp {
/*     */     void run() throws IOException;
/*     */   }
/*     */   
/*     */   class ChunkBuffer extends ByteArrayOutputStream {
/*     */     private final ChunkPos pos;
/*     */     
/*     */     public ChunkBuffer(ChunkPos debug2) {
/* 333 */       super(8096);
/*     */ 
/*     */       
/* 336 */       write(0);
/* 337 */       write(0);
/* 338 */       write(0);
/* 339 */       write(0);
/*     */       
/* 341 */       write(RegionFile.this.version.getId());
/* 342 */       this.pos = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 347 */       ByteBuffer debug1 = ByteBuffer.wrap(this.buf, 0, this.count);
/*     */       
/* 349 */       debug1.putInt(0, this.count - 5 + 1);
/* 350 */       RegionFile.this.write(this.pos, debug1);
/*     */     } }
/*     */   protected synchronized void write(ChunkPos debug1, ByteBuffer debug2) throws IOException {
/*     */     int debug9;
/*     */     CommitOp debug10;
/* 355 */     int debug3 = getOffsetIndex(debug1);
/* 356 */     int debug4 = this.offsets.get(debug3);
/* 357 */     int debug5 = getSectorNumber(debug4);
/* 358 */     int debug6 = getNumSectors(debug4);
/*     */     
/* 360 */     int debug7 = debug2.remaining();
/* 361 */     int debug8 = sizeToSectors(debug7);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 366 */     if (debug8 >= 256) {
/* 367 */       Path path = getExternalChunkPath(debug1);
/* 368 */       LOGGER.warn("Saving oversized chunk {} ({} bytes} to external file {}", debug1, Integer.valueOf(debug7), path);
/* 369 */       debug8 = 1;
/* 370 */       debug9 = this.usedSectors.allocate(debug8);
/* 371 */       debug10 = writeToExternalFile(path, debug2);
/* 372 */       ByteBuffer debug12 = createExternalStub();
/* 373 */       this.file.write(debug12, (debug9 * 4096));
/*     */     } else {
/* 375 */       debug9 = this.usedSectors.allocate(debug8);
/* 376 */       debug10 = (() -> Files.deleteIfExists(getExternalChunkPath(debug1)));
/* 377 */       this.file.write(debug2, (debug9 * 4096));
/*     */     } 
/*     */     
/* 380 */     int debug11 = (int)(Util.getEpochMillis() / 1000L);
/* 381 */     this.offsets.put(debug3, packSectorOffset(debug9, debug8));
/* 382 */     this.timestamps.put(debug3, debug11);
/* 383 */     writeHeader();
/*     */     
/* 385 */     debug10.run();
/*     */     
/* 387 */     if (debug5 != 0) {
/* 388 */       this.usedSectors.free(debug5, debug6);
/*     */     }
/*     */   }
/*     */   
/*     */   private ByteBuffer createExternalStub() {
/* 393 */     ByteBuffer debug1 = ByteBuffer.allocate(5);
/* 394 */     debug1.putInt(1);
/* 395 */     debug1.put((byte)(this.version.getId() | 0x80));
/* 396 */     debug1.flip();
/* 397 */     return debug1;
/*     */   }
/*     */   
/*     */   private CommitOp writeToExternalFile(Path debug1, ByteBuffer debug2) throws IOException {
/* 401 */     Path debug3 = Files.createTempFile(this.externalFileDir, "tmp", null, (FileAttribute<?>[])new FileAttribute[0]);
/* 402 */     try (FileChannel debug4 = FileChannel.open(debug3, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE })) {
/* 403 */       debug2.position(5);
/* 404 */       debug4.write(debug2);
/*     */     } 
/* 406 */     return () -> Files.move(debug0, debug1, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */   }
/*     */   
/*     */   private void writeHeader() throws IOException {
/* 410 */     this.header.position(0);
/* 411 */     this.file.write(this.header, 0L);
/*     */   }
/*     */   
/*     */   private int getOffset(ChunkPos debug1) {
/* 415 */     return this.offsets.get(getOffsetIndex(debug1));
/*     */   }
/*     */   
/*     */   public boolean hasChunk(ChunkPos debug1) {
/* 419 */     return (getOffset(debug1) != 0);
/*     */   }
/*     */   
/*     */   private static int getOffsetIndex(ChunkPos debug0) {
/* 423 */     return debug0.getRegionLocalX() + debug0.getRegionLocalZ() * 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 429 */       padToFullSector();
/*     */     } finally {
/*     */       try {
/* 432 */         this.file.force(true);
/*     */       } finally {
/* 434 */         this.file.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void padToFullSector() throws IOException {
/* 442 */     int debug1 = (int)this.file.size();
/* 443 */     int debug2 = sizeToSectors(debug1) * 4096;
/* 444 */     if (debug1 != debug2) {
/* 445 */       ByteBuffer debug3 = PADDING_BUFFER.duplicate();
/* 446 */       debug3.position(0);
/* 447 */       this.file.write(debug3, (debug2 - 1));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */