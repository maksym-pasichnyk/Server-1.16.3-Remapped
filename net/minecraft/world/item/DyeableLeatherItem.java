/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DyeableLeatherItem
/*     */ {
/*     */   default boolean hasCustomColor(ItemStack debug1) {
/*  14 */     CompoundTag debug2 = debug1.getTagElement("display");
/*  15 */     return (debug2 != null && debug2.contains("color", 99));
/*     */   }
/*     */   
/*     */   default int getColor(ItemStack debug1) {
/*  19 */     CompoundTag debug2 = debug1.getTagElement("display");
/*  20 */     if (debug2 != null && debug2.contains("color", 99)) {
/*  21 */       return debug2.getInt("color");
/*     */     }
/*  23 */     return 10511680;
/*     */   }
/*     */   
/*     */   default void clearColor(ItemStack debug1) {
/*  27 */     CompoundTag debug2 = debug1.getTagElement("display");
/*  28 */     if (debug2 != null && debug2.contains("color")) {
/*  29 */       debug2.remove("color");
/*     */     }
/*     */   }
/*     */   
/*     */   default void setColor(ItemStack debug1, int debug2) {
/*  34 */     debug1.getOrCreateTagElement("display").putInt("color", debug2);
/*     */   }
/*     */   
/*     */   static ItemStack dyeArmor(ItemStack debug0, List<DyeItem> debug1) {
/*  38 */     ItemStack debug2 = ItemStack.EMPTY;
/*  39 */     int[] debug3 = new int[3];
/*  40 */     int debug4 = 0;
/*  41 */     int debug5 = 0;
/*  42 */     DyeableLeatherItem debug6 = null;
/*     */     
/*  44 */     Item debug7 = debug0.getItem();
/*  45 */     if (debug7 instanceof DyeableLeatherItem) {
/*  46 */       debug6 = (DyeableLeatherItem)debug7;
/*     */       
/*  48 */       debug2 = debug0.copy();
/*  49 */       debug2.setCount(1);
/*     */ 
/*     */       
/*  52 */       if (debug6.hasCustomColor(debug0)) {
/*  53 */         int i = debug6.getColor(debug2);
/*  54 */         float f1 = (i >> 16 & 0xFF) / 255.0F;
/*  55 */         float f2 = (i >> 8 & 0xFF) / 255.0F;
/*  56 */         float f3 = (i & 0xFF) / 255.0F;
/*     */         
/*  58 */         debug4 = (int)(debug4 + Math.max(f1, Math.max(f2, f3)) * 255.0F);
/*     */         
/*  60 */         debug3[0] = (int)(debug3[0] + f1 * 255.0F);
/*  61 */         debug3[1] = (int)(debug3[1] + f2 * 255.0F);
/*  62 */         debug3[2] = (int)(debug3[2] + f3 * 255.0F);
/*  63 */         debug5++;
/*     */       } 
/*     */ 
/*     */       
/*  67 */       for (DyeItem dyeItem : debug1) {
/*  68 */         float[] arrayOfFloat = dyeItem.getDyeColor().getTextureDiffuseColors();
/*  69 */         int i = (int)(arrayOfFloat[0] * 255.0F);
/*  70 */         int j = (int)(arrayOfFloat[1] * 255.0F);
/*  71 */         int k = (int)(arrayOfFloat[2] * 255.0F);
/*     */         
/*  73 */         debug4 += Math.max(i, Math.max(j, k));
/*     */         
/*  75 */         debug3[0] = debug3[0] + i;
/*  76 */         debug3[1] = debug3[1] + j;
/*  77 */         debug3[2] = debug3[2] + k;
/*  78 */         debug5++;
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     if (debug6 == null) {
/*  83 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/*  86 */     int debug8 = debug3[0] / debug5;
/*  87 */     int debug9 = debug3[1] / debug5;
/*  88 */     int debug10 = debug3[2] / debug5;
/*     */     
/*  90 */     float debug11 = debug4 / debug5;
/*  91 */     float debug12 = Math.max(debug8, Math.max(debug9, debug10));
/*     */     
/*  93 */     debug8 = (int)(debug8 * debug11 / debug12);
/*  94 */     debug9 = (int)(debug9 * debug11 / debug12);
/*  95 */     debug10 = (int)(debug10 * debug11 / debug12);
/*     */     
/*  97 */     int debug13 = debug8;
/*  98 */     debug13 = (debug13 << 8) + debug9;
/*  99 */     debug13 = (debug13 << 8) + debug10;
/*     */     
/* 101 */     debug6.setColor(debug2, debug13);
/* 102 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\DyeableLeatherItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */