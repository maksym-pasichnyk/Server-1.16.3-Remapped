/*     */ package io.netty.buffer;
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
/*     */ final class PoolChunk<T>
/*     */   implements PoolChunkMetric
/*     */ {
/*     */   private static final int INTEGER_SIZE_MINUS_ONE = 31;
/*     */   final PoolArena<T> arena;
/*     */   final T memory;
/*     */   final boolean unpooled;
/*     */   final int offset;
/*     */   private final byte[] memoryMap;
/*     */   private final byte[] depthMap;
/*     */   private final PoolSubpage<T>[] subpages;
/*     */   private final int subpageOverflowMask;
/*     */   private final int pageSize;
/*     */   private final int pageShifts;
/*     */   private final int maxOrder;
/*     */   private final int chunkSize;
/*     */   private final int log2ChunkSize;
/*     */   private final int maxSubpageAllocs;
/*     */   private final byte unusable;
/*     */   private int freeBytes;
/*     */   PoolChunkList<T> parent;
/*     */   PoolChunk<T> prev;
/*     */   PoolChunk<T> next;
/*     */   
/*     */   PoolChunk(PoolArena<T> arena, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize, int offset) {
/* 136 */     this.unpooled = false;
/* 137 */     this.arena = arena;
/* 138 */     this.memory = memory;
/* 139 */     this.pageSize = pageSize;
/* 140 */     this.pageShifts = pageShifts;
/* 141 */     this.maxOrder = maxOrder;
/* 142 */     this.chunkSize = chunkSize;
/* 143 */     this.offset = offset;
/* 144 */     this.unusable = (byte)(maxOrder + 1);
/* 145 */     this.log2ChunkSize = log2(chunkSize);
/* 146 */     this.subpageOverflowMask = pageSize - 1 ^ 0xFFFFFFFF;
/* 147 */     this.freeBytes = chunkSize;
/*     */     
/* 149 */     assert maxOrder < 30 : "maxOrder should be < 30, but is: " + maxOrder;
/* 150 */     this.maxSubpageAllocs = 1 << maxOrder;
/*     */ 
/*     */     
/* 153 */     this.memoryMap = new byte[this.maxSubpageAllocs << 1];
/* 154 */     this.depthMap = new byte[this.memoryMap.length];
/* 155 */     int memoryMapIndex = 1;
/* 156 */     for (int d = 0; d <= maxOrder; d++) {
/* 157 */       int depth = 1 << d;
/* 158 */       for (int p = 0; p < depth; p++) {
/*     */         
/* 160 */         this.memoryMap[memoryMapIndex] = (byte)d;
/* 161 */         this.depthMap[memoryMapIndex] = (byte)d;
/* 162 */         memoryMapIndex++;
/*     */       } 
/*     */     } 
/*     */     
/* 166 */     this.subpages = newSubpageArray(this.maxSubpageAllocs);
/*     */   }
/*     */ 
/*     */   
/*     */   PoolChunk(PoolArena<T> arena, T memory, int size, int offset) {
/* 171 */     this.unpooled = true;
/* 172 */     this.arena = arena;
/* 173 */     this.memory = memory;
/* 174 */     this.offset = offset;
/* 175 */     this.memoryMap = null;
/* 176 */     this.depthMap = null;
/* 177 */     this.subpages = null;
/* 178 */     this.subpageOverflowMask = 0;
/* 179 */     this.pageSize = 0;
/* 180 */     this.pageShifts = 0;
/* 181 */     this.maxOrder = 0;
/* 182 */     this.unusable = (byte)(this.maxOrder + 1);
/* 183 */     this.chunkSize = size;
/* 184 */     this.log2ChunkSize = log2(this.chunkSize);
/* 185 */     this.maxSubpageAllocs = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private PoolSubpage<T>[] newSubpageArray(int size) {
/* 190 */     return (PoolSubpage<T>[])new PoolSubpage[size];
/*     */   }
/*     */ 
/*     */   
/*     */   public int usage() {
/*     */     int freeBytes;
/* 196 */     synchronized (this.arena) {
/* 197 */       freeBytes = this.freeBytes;
/*     */     } 
/* 199 */     return usage(freeBytes);
/*     */   }
/*     */   
/*     */   private int usage(int freeBytes) {
/* 203 */     if (freeBytes == 0) {
/* 204 */       return 100;
/*     */     }
/*     */     
/* 207 */     int freePercentage = (int)(freeBytes * 100L / this.chunkSize);
/* 208 */     if (freePercentage == 0) {
/* 209 */       return 99;
/*     */     }
/* 211 */     return 100 - freePercentage;
/*     */   }
/*     */   
/*     */   long allocate(int normCapacity) {
/* 215 */     if ((normCapacity & this.subpageOverflowMask) != 0) {
/* 216 */       return allocateRun(normCapacity);
/*     */     }
/* 218 */     return allocateSubpage(normCapacity);
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
/*     */   private void updateParentsAlloc(int id) {
/* 231 */     while (id > 1) {
/* 232 */       int parentId = id >>> 1;
/* 233 */       byte val1 = value(id);
/* 234 */       byte val2 = value(id ^ 0x1);
/* 235 */       byte val = (val1 < val2) ? val1 : val2;
/* 236 */       setValue(parentId, val);
/* 237 */       id = parentId;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateParentsFree(int id) {
/* 249 */     int logChild = depth(id) + 1;
/* 250 */     while (id > 1) {
/* 251 */       int parentId = id >>> 1;
/* 252 */       byte val1 = value(id);
/* 253 */       byte val2 = value(id ^ 0x1);
/* 254 */       logChild--;
/*     */       
/* 256 */       if (val1 == logChild && val2 == logChild) {
/* 257 */         setValue(parentId, (byte)(logChild - 1));
/*     */       } else {
/* 259 */         byte val = (val1 < val2) ? val1 : val2;
/* 260 */         setValue(parentId, val);
/*     */       } 
/*     */       
/* 263 */       id = parentId;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int allocateNode(int d) {
/* 275 */     int id = 1;
/* 276 */     int initial = -(1 << d);
/* 277 */     byte val = value(id);
/* 278 */     if (val > d) {
/* 279 */       return -1;
/*     */     }
/* 281 */     while (val < d || (id & initial) == 0) {
/* 282 */       id <<= 1;
/* 283 */       val = value(id);
/* 284 */       if (val > d) {
/* 285 */         id ^= 0x1;
/* 286 */         val = value(id);
/*     */       } 
/*     */     } 
/* 289 */     byte value = value(id);
/* 290 */     assert value == d && (id & initial) == 1 << d : String.format("val = %d, id & initial = %d, d = %d", new Object[] {
/* 291 */           Byte.valueOf(value), Integer.valueOf(id & initial), Integer.valueOf(d) });
/* 292 */     setValue(id, this.unusable);
/* 293 */     updateParentsAlloc(id);
/* 294 */     return id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long allocateRun(int normCapacity) {
/* 304 */     int d = this.maxOrder - log2(normCapacity) - this.pageShifts;
/* 305 */     int id = allocateNode(d);
/* 306 */     if (id < 0) {
/* 307 */       return id;
/*     */     }
/* 309 */     this.freeBytes -= runLength(id);
/* 310 */     return id;
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
/*     */   private long allocateSubpage(int normCapacity) {
/* 323 */     PoolSubpage<T> head = this.arena.findSubpagePoolHead(normCapacity);
/* 324 */     synchronized (head) {
/* 325 */       int d = this.maxOrder;
/* 326 */       int id = allocateNode(d);
/* 327 */       if (id < 0) {
/* 328 */         return id;
/*     */       }
/*     */       
/* 331 */       PoolSubpage<T>[] subpages = this.subpages;
/* 332 */       int pageSize = this.pageSize;
/*     */       
/* 334 */       this.freeBytes -= pageSize;
/*     */       
/* 336 */       int subpageIdx = subpageIdx(id);
/* 337 */       PoolSubpage<T> subpage = subpages[subpageIdx];
/* 338 */       if (subpage == null) {
/* 339 */         subpage = new PoolSubpage<T>(head, this, id, runOffset(id), pageSize, normCapacity);
/* 340 */         subpages[subpageIdx] = subpage;
/*     */       } else {
/* 342 */         subpage.init(head, normCapacity);
/*     */       } 
/* 344 */       return subpage.allocate();
/*     */     } 
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
/*     */   void free(long handle) {
/* 357 */     int memoryMapIdx = memoryMapIdx(handle);
/* 358 */     int bitmapIdx = bitmapIdx(handle);
/*     */     
/* 360 */     if (bitmapIdx != 0) {
/* 361 */       PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
/* 362 */       assert subpage != null && subpage.doNotDestroy;
/*     */ 
/*     */ 
/*     */       
/* 366 */       PoolSubpage<T> head = this.arena.findSubpagePoolHead(subpage.elemSize);
/* 367 */       synchronized (head) {
/* 368 */         if (subpage.free(head, bitmapIdx & 0x3FFFFFFF)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/* 373 */     this.freeBytes += runLength(memoryMapIdx);
/* 374 */     setValue(memoryMapIdx, depth(memoryMapIdx));
/* 375 */     updateParentsFree(memoryMapIdx);
/*     */   }
/*     */   
/*     */   void initBuf(PooledByteBuf<T> buf, long handle, int reqCapacity) {
/* 379 */     int memoryMapIdx = memoryMapIdx(handle);
/* 380 */     int bitmapIdx = bitmapIdx(handle);
/* 381 */     if (bitmapIdx == 0) {
/* 382 */       byte val = value(memoryMapIdx);
/* 383 */       assert val == this.unusable : String.valueOf(val);
/* 384 */       buf.init(this, handle, runOffset(memoryMapIdx) + this.offset, reqCapacity, runLength(memoryMapIdx), this.arena.parent
/* 385 */           .threadCache());
/*     */     } else {
/* 387 */       initBufWithSubpage(buf, handle, bitmapIdx, reqCapacity);
/*     */     } 
/*     */   }
/*     */   
/*     */   void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int reqCapacity) {
/* 392 */     initBufWithSubpage(buf, handle, bitmapIdx(handle), reqCapacity);
/*     */   }
/*     */   
/*     */   private void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int bitmapIdx, int reqCapacity) {
/* 396 */     assert bitmapIdx != 0;
/*     */     
/* 398 */     int memoryMapIdx = memoryMapIdx(handle);
/*     */     
/* 400 */     PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
/* 401 */     assert subpage.doNotDestroy;
/* 402 */     assert reqCapacity <= subpage.elemSize;
/*     */     
/* 404 */     buf.init(this, handle, 
/*     */         
/* 406 */         runOffset(memoryMapIdx) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize + this.offset, reqCapacity, subpage.elemSize, this.arena.parent
/* 407 */         .threadCache());
/*     */   }
/*     */   
/*     */   private byte value(int id) {
/* 411 */     return this.memoryMap[id];
/*     */   }
/*     */   
/*     */   private void setValue(int id, byte val) {
/* 415 */     this.memoryMap[id] = val;
/*     */   }
/*     */   
/*     */   private byte depth(int id) {
/* 419 */     return this.depthMap[id];
/*     */   }
/*     */ 
/*     */   
/*     */   private static int log2(int val) {
/* 424 */     return 31 - Integer.numberOfLeadingZeros(val);
/*     */   }
/*     */ 
/*     */   
/*     */   private int runLength(int id) {
/* 429 */     return 1 << this.log2ChunkSize - depth(id);
/*     */   }
/*     */ 
/*     */   
/*     */   private int runOffset(int id) {
/* 434 */     int shift = id ^ 1 << depth(id);
/* 435 */     return shift * runLength(id);
/*     */   }
/*     */   
/*     */   private int subpageIdx(int memoryMapIdx) {
/* 439 */     return memoryMapIdx ^ this.maxSubpageAllocs;
/*     */   }
/*     */   
/*     */   private static int memoryMapIdx(long handle) {
/* 443 */     return (int)handle;
/*     */   }
/*     */   
/*     */   private static int bitmapIdx(long handle) {
/* 447 */     return (int)(handle >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   public int chunkSize() {
/* 452 */     return this.chunkSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int freeBytes() {
/* 457 */     synchronized (this.arena) {
/* 458 */       return this.freeBytes;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     int freeBytes;
/* 465 */     synchronized (this.arena) {
/* 466 */       freeBytes = this.freeBytes;
/*     */     } 
/*     */     
/* 469 */     return "Chunk(" + 
/*     */       
/* 471 */       Integer.toHexString(System.identityHashCode(this)) + ": " + 
/*     */       
/* 473 */       usage(freeBytes) + "%, " + (
/* 474 */       this.chunkSize - freeBytes) + 
/* 475 */       '/' + 
/* 476 */       this.chunkSize + 
/* 477 */       ')';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void destroy() {
/* 483 */     this.arena.destroyChunk(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolChunk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */