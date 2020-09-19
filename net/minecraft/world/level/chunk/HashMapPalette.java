/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ 
/*     */ public class HashMapPalette<T>
/*     */   implements Palette<T> {
/*     */   private final IdMapper<T> registry;
/*     */   private final CrudeIncrementalIntIdentityHashBiMap<T> values;
/*     */   private final PaletteResize<T> resizeHandler;
/*     */   private final Function<CompoundTag, T> reader;
/*     */   private final Function<T, CompoundTag> writer;
/*     */   private final int bits;
/*     */   
/*     */   public HashMapPalette(IdMapper<T> debug1, int debug2, PaletteResize<T> debug3, Function<CompoundTag, T> debug4, Function<T, CompoundTag> debug5) {
/*  22 */     this.registry = debug1;
/*  23 */     this.bits = debug2;
/*  24 */     this.resizeHandler = debug3;
/*  25 */     this.reader = debug4;
/*  26 */     this.writer = debug5;
/*  27 */     this.values = new CrudeIncrementalIntIdentityHashBiMap(1 << debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int idFor(T debug1) {
/*  32 */     int debug2 = this.values.getId(debug1);
/*  33 */     if (debug2 == -1) {
/*  34 */       debug2 = this.values.add(debug1);
/*     */       
/*  36 */       if (debug2 >= 1 << this.bits) {
/*  37 */         debug2 = this.resizeHandler.onResize(this.bits + 1, debug1);
/*     */       }
/*     */     } 
/*  40 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean maybeHas(Predicate<T> debug1) {
/*  45 */     for (int debug2 = 0; debug2 < getSize(); debug2++) {
/*  46 */       if (debug1.test((T)this.values.byId(debug2))) {
/*  47 */         return true;
/*     */       }
/*     */     } 
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T valueFor(int debug1) {
/*  56 */     return (T)this.values.byId(debug1);
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
/*     */   public void write(FriendlyByteBuf debug1) {
/*  70 */     int debug2 = getSize();
/*  71 */     debug1.writeVarInt(debug2);
/*     */     
/*  73 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  74 */       debug1.writeVarInt(this.registry.getId(this.values.byId(debug3)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSerializedSize() {
/*  80 */     int debug1 = FriendlyByteBuf.getVarIntSize(getSize());
/*     */     
/*  82 */     for (int debug2 = 0; debug2 < getSize(); debug2++) {
/*  83 */       debug1 += FriendlyByteBuf.getVarIntSize(this.registry.getId(this.values.byId(debug2)));
/*     */     }
/*     */     
/*  86 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  91 */     return this.values.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(ListTag debug1) {
/*  96 */     this.values.clear();
/*  97 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/*  98 */       this.values.add(this.reader.apply(debug1.getCompound(debug2)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ListTag debug1) {
/* 103 */     for (int debug2 = 0; debug2 < getSize(); debug2++)
/* 104 */       debug1.add(this.writer.apply((T)this.values.byId(debug2))); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\HashMapPalette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */