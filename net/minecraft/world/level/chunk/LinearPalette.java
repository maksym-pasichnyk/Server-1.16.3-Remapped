/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public class LinearPalette<T>
/*     */   implements Palette<T>
/*     */ {
/*     */   private final IdMapper<T> registry;
/*     */   private final T[] values;
/*     */   private final PaletteResize<T> resizeHandler;
/*     */   private final Function<CompoundTag, T> reader;
/*     */   private final int bits;
/*     */   private int size;
/*     */   
/*     */   public LinearPalette(IdMapper<T> debug1, int debug2, PaletteResize<T> debug3, Function<CompoundTag, T> debug4) {
/*  22 */     this.registry = debug1;
/*  23 */     this.values = (T[])new Object[1 << debug2];
/*  24 */     this.bits = debug2;
/*  25 */     this.resizeHandler = debug3;
/*  26 */     this.reader = debug4;
/*     */   }
/*     */   
/*     */   public int idFor(T debug1) {
/*     */     int debug2;
/*  31 */     for (debug2 = 0; debug2 < this.size; debug2++) {
/*  32 */       if (this.values[debug2] == debug1) {
/*  33 */         return debug2;
/*     */       }
/*     */     } 
/*     */     
/*  37 */     debug2 = this.size;
/*  38 */     if (debug2 < this.values.length) {
/*  39 */       this.values[debug2] = debug1;
/*  40 */       this.size++;
/*  41 */       return debug2;
/*     */     } 
/*     */     
/*  44 */     return this.resizeHandler.onResize(this.bits + 1, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean maybeHas(Predicate<T> debug1) {
/*  49 */     for (int debug2 = 0; debug2 < this.size; debug2++) {
/*  50 */       if (debug1.test(this.values[debug2])) {
/*  51 */         return true;
/*     */       }
/*     */     } 
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T valueFor(int debug1) {
/*  60 */     if (debug1 >= 0 && debug1 < this.size) {
/*  61 */       return this.values[debug1];
/*     */     }
/*  63 */     return null;
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
/*     */   public void write(FriendlyByteBuf debug1) {
/*  76 */     debug1.writeVarInt(this.size);
/*  77 */     for (int debug2 = 0; debug2 < this.size; debug2++) {
/*  78 */       debug1.writeVarInt(this.registry.getId(this.values[debug2]));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSerializedSize() {
/*  84 */     int debug1 = FriendlyByteBuf.getVarIntSize(getSize());
/*     */     
/*  86 */     for (int debug2 = 0; debug2 < getSize(); debug2++) {
/*  87 */       debug1 += FriendlyByteBuf.getVarIntSize(this.registry.getId(this.values[debug2]));
/*     */     }
/*     */     
/*  90 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  95 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(ListTag debug1) {
/* 100 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 101 */       this.values[debug2] = this.reader.apply(debug1.getCompound(debug2));
/*     */     }
/* 103 */     this.size = debug1.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\LinearPalette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */