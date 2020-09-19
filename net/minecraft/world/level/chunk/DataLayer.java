/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataLayer
/*     */ {
/*     */   @Nullable
/*     */   protected byte[] data;
/*     */   
/*     */   public DataLayer() {}
/*     */   
/*     */   public DataLayer(byte[] debug1) {
/*  18 */     this.data = debug1;
/*     */     
/*  20 */     if (debug1.length != 2048) {
/*  21 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + debug1.length));
/*     */     }
/*     */   }
/*     */   
/*     */   protected DataLayer(int debug1) {
/*  26 */     this.data = new byte[debug1];
/*     */   }
/*     */   
/*     */   public int get(int debug1, int debug2, int debug3) {
/*  30 */     return get(getIndex(debug1, debug2, debug3));
/*     */   }
/*     */   
/*     */   public void set(int debug1, int debug2, int debug3, int debug4) {
/*  34 */     set(getIndex(debug1, debug2, debug3), debug4);
/*     */   }
/*     */   
/*     */   protected int getIndex(int debug1, int debug2, int debug3) {
/*  38 */     return debug2 << 8 | debug3 << 4 | debug1;
/*     */   }
/*     */   
/*     */   private int get(int debug1) {
/*  42 */     if (this.data == null) {
/*  43 */       return 0;
/*     */     }
/*  45 */     int debug2 = getPosition(debug1);
/*     */     
/*  47 */     if (isFirst(debug1)) {
/*  48 */       return this.data[debug2] & 0xF;
/*     */     }
/*  50 */     return this.data[debug2] >> 4 & 0xF;
/*     */   }
/*     */ 
/*     */   
/*     */   private void set(int debug1, int debug2) {
/*  55 */     if (this.data == null) {
/*  56 */       this.data = new byte[2048];
/*     */     }
/*  58 */     int debug3 = getPosition(debug1);
/*     */     
/*  60 */     if (isFirst(debug1)) {
/*  61 */       this.data[debug3] = (byte)(this.data[debug3] & 0xF0 | debug2 & 0xF);
/*     */     } else {
/*  63 */       this.data[debug3] = (byte)(this.data[debug3] & 0xF | (debug2 & 0xF) << 4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isFirst(int debug1) {
/*  68 */     return ((debug1 & 0x1) == 0);
/*     */   }
/*     */   
/*     */   private int getPosition(int debug1) {
/*  72 */     return debug1 >> 1;
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/*  76 */     if (this.data == null) {
/*  77 */       this.data = new byte[2048];
/*     */     }
/*  79 */     return this.data;
/*     */   }
/*     */   
/*     */   public DataLayer copy() {
/*  83 */     if (this.data == null) {
/*  84 */       return new DataLayer();
/*     */     }
/*  86 */     return new DataLayer((byte[])this.data.clone());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     StringBuilder debug1 = new StringBuilder();
/*  92 */     for (int debug2 = 0; debug2 < 4096; debug2++) {
/*  93 */       debug1.append(Integer.toHexString(get(debug2)));
/*  94 */       if ((debug2 & 0xF) == 15) {
/*  95 */         debug1.append("\n");
/*     */       }
/*  97 */       if ((debug2 & 0xFF) == 255) {
/*  98 */         debug1.append("\n");
/*     */       }
/*     */     } 
/* 101 */     return debug1.toString();
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
/*     */   public boolean isEmpty() {
/* 117 */     return (this.data == null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\DataLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */