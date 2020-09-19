/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PalettedContainer<T>
/*     */   implements PaletteResize<T>
/*     */ {
/*     */   private final Palette<T> globalPalette;
/*     */   private final PaletteResize<T> dummyPaletteResize = (debug0, debug1) -> 0;
/*     */   private final IdMapper<T> registry;
/*     */   private final Function<CompoundTag, T> reader;
/*     */   private final Function<T, CompoundTag> writer;
/*     */   private final T defaultValue;
/*     */   protected BitStorage storage;
/*     */   private Palette<T> palette;
/*     */   private int bits;
/*  38 */   private final ReentrantLock lock = new ReentrantLock();
/*     */   
/*     */   public void acquire() {
/*  41 */     if (this.lock.isLocked() && !this.lock.isHeldByCurrentThread()) {
/*     */ 
/*     */       
/*  44 */       String debug1 = Thread.getAllStackTraces().keySet().stream().filter(Objects::nonNull).map(debug0 -> debug0.getName() + ": \n\tat " + (String)Arrays.<StackTraceElement>stream(debug0.getStackTrace()).map(Object::toString).collect(Collectors.joining("\n\tat "))).collect(Collectors.joining("\n"));
/*  45 */       CrashReport debug2 = new CrashReport("Writing into PalettedContainer from multiple threads", new IllegalStateException());
/*  46 */       CrashReportCategory debug3 = debug2.addCategory("Thread dumps");
/*  47 */       debug3.setDetail("Thread dumps", debug1);
/*  48 */       throw new ReportedException(debug2);
/*     */     } 
/*  50 */     this.lock.lock();
/*     */   }
/*     */   
/*     */   public void release() {
/*  54 */     this.lock.unlock();
/*     */   }
/*     */   
/*     */   public PalettedContainer(Palette<T> debug1, IdMapper<T> debug2, Function<CompoundTag, T> debug3, Function<T, CompoundTag> debug4, T debug5) {
/*  58 */     this.globalPalette = debug1;
/*  59 */     this.registry = debug2;
/*  60 */     this.reader = debug3;
/*  61 */     this.writer = debug4;
/*  62 */     this.defaultValue = debug5;
/*  63 */     setBits(4);
/*     */   }
/*     */   
/*     */   private static int getIndex(int debug0, int debug1, int debug2) {
/*  67 */     return debug1 << 8 | debug2 << 4 | debug0;
/*     */   }
/*     */   
/*     */   private void setBits(int debug1) {
/*  71 */     if (debug1 == this.bits) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     this.bits = debug1;
/*     */     
/*  77 */     if (this.bits <= 4) {
/*  78 */       this.bits = 4;
/*  79 */       this.palette = new LinearPalette<>(this.registry, this.bits, this, this.reader);
/*  80 */     } else if (this.bits < 9) {
/*  81 */       this.palette = new HashMapPalette<>(this.registry, this.bits, this, this.reader, this.writer);
/*     */     } else {
/*  83 */       this.palette = this.globalPalette;
/*  84 */       this.bits = Mth.ceillog2(this.registry.size());
/*     */     } 
/*  86 */     this.palette.idFor(this.defaultValue);
/*     */     
/*  88 */     this.storage = new BitStorage(this.bits, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public int onResize(int debug1, T debug2) {
/*  93 */     acquire();
/*  94 */     BitStorage debug3 = this.storage;
/*  95 */     Palette<T> debug4 = this.palette;
/*     */     
/*  97 */     setBits(debug1);
/*     */     int debug5;
/*  99 */     for (debug5 = 0; debug5 < debug3.getSize(); debug5++) {
/* 100 */       T debug6 = debug4.valueFor(debug3.get(debug5));
/* 101 */       if (debug6 != null) {
/* 102 */         set(debug5, debug6);
/*     */       }
/*     */     } 
/*     */     
/* 106 */     debug5 = this.palette.idFor(debug2);
/* 107 */     release();
/* 108 */     return debug5;
/*     */   }
/*     */   
/*     */   public T getAndSet(int debug1, int debug2, int debug3, T debug4) {
/* 112 */     acquire();
/* 113 */     T debug5 = getAndSet(getIndex(debug1, debug2, debug3), debug4);
/* 114 */     release();
/* 115 */     return debug5;
/*     */   }
/*     */   
/*     */   public T getAndSetUnchecked(int debug1, int debug2, int debug3, T debug4) {
/* 119 */     return getAndSet(getIndex(debug1, debug2, debug3), debug4);
/*     */   }
/*     */   
/*     */   protected T getAndSet(int debug1, T debug2) {
/* 123 */     int debug3 = this.palette.idFor(debug2);
/* 124 */     int debug4 = this.storage.getAndSet(debug1, debug3);
/* 125 */     T debug5 = this.palette.valueFor(debug4);
/* 126 */     return (debug5 == null) ? this.defaultValue : debug5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void set(int debug1, T debug2) {
/* 136 */     int debug3 = this.palette.idFor(debug2);
/* 137 */     this.storage.set(debug1, debug3);
/*     */   }
/*     */   
/*     */   public T get(int debug1, int debug2, int debug3) {
/* 141 */     return get(getIndex(debug1, debug2, debug3));
/*     */   }
/*     */   
/*     */   protected T get(int debug1) {
/* 145 */     T debug2 = this.palette.valueFor(this.storage.get(debug1));
/* 146 */     return (debug2 == null) ? this.defaultValue : debug2;
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
/*     */   public void write(FriendlyByteBuf debug1) {
/* 163 */     acquire();
/* 164 */     debug1.writeByte(this.bits);
/* 165 */     this.palette.write(debug1);
/* 166 */     debug1.writeLongArray(this.storage.getRaw());
/* 167 */     release();
/*     */   }
/*     */   
/*     */   public void read(ListTag debug1, long[] debug2) {
/* 171 */     acquire();
/*     */ 
/*     */     
/* 174 */     int debug3 = Math.max(4, Mth.ceillog2(debug1.size()));
/* 175 */     if (debug3 != this.bits) {
/* 176 */       setBits(debug3);
/*     */     }
/*     */     
/* 179 */     this.palette.read(debug1);
/*     */     
/* 181 */     int debug4 = debug2.length * 64 / 4096;
/* 182 */     if (this.palette == this.globalPalette) {
/* 183 */       Palette<T> debug5 = new HashMapPalette<>(this.registry, debug3, this.dummyPaletteResize, this.reader, this.writer);
/* 184 */       debug5.read(debug1);
/*     */       
/* 186 */       BitStorage debug6 = new BitStorage(debug3, 4096, debug2);
/* 187 */       for (int debug7 = 0; debug7 < 4096; debug7++) {
/* 188 */         this.storage.set(debug7, this.globalPalette.idFor(debug5.valueFor(debug6.get(debug7))));
/*     */       }
/*     */     }
/* 191 */     else if (debug4 == this.bits) {
/* 192 */       System.arraycopy(debug2, 0, this.storage.getRaw(), 0, debug2.length);
/*     */     } else {
/*     */       
/* 195 */       BitStorage debug5 = new BitStorage(debug4, 4096, debug2);
/* 196 */       for (int debug6 = 0; debug6 < 4096; debug6++) {
/* 197 */         this.storage.set(debug6, debug5.get(debug6));
/*     */       }
/*     */     } 
/*     */     
/* 201 */     release();
/*     */   }
/*     */   
/*     */   public void write(CompoundTag debug1, String debug2, String debug3) {
/* 205 */     acquire();
/* 206 */     HashMapPalette<T> debug4 = new HashMapPalette<>(this.registry, this.bits, this.dummyPaletteResize, this.reader, this.writer);
/*     */     
/* 208 */     T debug5 = this.defaultValue;
/* 209 */     int debug6 = debug4.idFor(this.defaultValue);
/*     */     
/* 211 */     int[] debug7 = new int[4096];
/* 212 */     for (int i = 0; i < 4096; i++) {
/* 213 */       T t = get(i);
/* 214 */       if (t != debug5) {
/* 215 */         debug5 = t;
/* 216 */         debug6 = debug4.idFor(t);
/*     */       } 
/* 218 */       debug7[i] = debug6;
/*     */     } 
/*     */     
/* 221 */     ListTag debug8 = new ListTag();
/* 222 */     debug4.write(debug8);
/* 223 */     debug1.put(debug2, (Tag)debug8);
/*     */     
/* 225 */     int debug9 = Math.max(4, Mth.ceillog2(debug8.size()));
/* 226 */     BitStorage debug10 = new BitStorage(debug9, 4096);
/* 227 */     for (int debug11 = 0; debug11 < debug7.length; debug11++) {
/* 228 */       debug10.set(debug11, debug7[debug11]);
/*     */     }
/* 230 */     debug1.putLongArray(debug3, debug10.getRaw());
/* 231 */     release();
/*     */   }
/*     */   
/*     */   public int getSerializedSize() {
/* 235 */     return 1 + this.palette.getSerializedSize() + FriendlyByteBuf.getVarIntSize(this.storage.getSize()) + (this.storage.getRaw()).length * 8;
/*     */   }
/*     */   
/*     */   public boolean maybeHas(Predicate<T> debug1) {
/* 239 */     return this.palette.maybeHas(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void count(CountConsumer<T> debug1) {
/* 248 */     Int2IntOpenHashMap int2IntOpenHashMap = new Int2IntOpenHashMap();
/* 249 */     this.storage.getAll(debug1 -> debug0.put(debug1, debug0.get(debug1) + 1));
/* 250 */     int2IntOpenHashMap.int2IntEntrySet().forEach(debug2 -> debug1.accept(this.palette.valueFor(debug2.getIntKey()), debug2.getIntValue()));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface CountConsumer<T> {
/*     */     void accept(T param1T, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\PalettedContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */