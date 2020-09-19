/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BitStorageAlignFix
/*     */   extends DataFix
/*     */ {
/*     */   public BitStorageAlignFix(Schema debug1) {
/*  28 */     super(debug1, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  33 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/*  34 */     Type<?> debug2 = debug1.findFieldType("Level");
/*     */     
/*  36 */     OpticFinder<?> debug3 = DSL.fieldFinder("Level", debug2);
/*  37 */     OpticFinder<?> debug4 = debug3.type().findField("Sections");
/*     */     
/*  39 */     Type<?> debug5 = ((List.ListType)debug4.type()).getElement();
/*  40 */     OpticFinder<?> debug6 = DSL.typeFinder(debug5);
/*     */     
/*  42 */     Type<Pair<String, Dynamic<?>>> debug7 = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/*  43 */     OpticFinder<List<Pair<String, Dynamic<?>>>> debug8 = DSL.fieldFinder("Palette", (Type)DSL.list(debug7));
/*     */     
/*  45 */     return fixTypeEverywhereTyped("BitStorageAlignFix", debug1, getOutputSchema().getType(References.CHUNK), debug5 -> debug5.updateTyped(debug1, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Typed<?> updateHeightmaps(Typed<?> debug1) {
/*  53 */     return debug1.update(DSL.remainderFinder(), debug0 -> debug0.update("Heightmaps", ()));
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
/*     */   private static Typed<?> updateSections(OpticFinder<?> debug0, OpticFinder<?> debug1, OpticFinder<List<Pair<String, Dynamic<?>>>> debug2, Typed<?> debug3) {
/*  65 */     return debug3.updateTyped(debug0, debug2 -> debug2.updateTyped(debug0, ()));
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
/*     */   private static Dynamic<?> updateBitStorage(Dynamic<?> debug0, Dynamic<?> debug1, int debug2, int debug3) {
/*  81 */     long[] debug4 = debug1.asLongStream().toArray();
/*  82 */     long[] debug5 = addPadding(debug2, debug3, debug4);
/*  83 */     return debug0.createLongList(LongStream.of(debug5));
/*     */   }
/*     */   
/*     */   public static long[] addPadding(int debug0, int debug1, long[] debug2) {
/*  87 */     int debug3 = debug2.length;
/*  88 */     if (debug3 == 0) {
/*  89 */       return debug2;
/*     */     }
/*     */     
/*  92 */     long debug4 = (1L << debug1) - 1L;
/*  93 */     int debug6 = 64 / debug1;
/*  94 */     int debug7 = (debug0 + debug6 - 1) / debug6;
/*     */     
/*  96 */     long[] debug8 = new long[debug7];
/*     */     
/*  98 */     int debug9 = 0;
/*  99 */     int debug10 = 0;
/* 100 */     long debug11 = 0L;
/*     */     
/* 102 */     int debug13 = 0;
/* 103 */     long debug14 = debug2[0];
/* 104 */     long debug16 = (debug3 > 1) ? debug2[1] : 0L;
/*     */     
/* 106 */     for (int debug18 = 0; debug18 < debug0; debug18++) {
/* 107 */       long debug23; int debug19 = debug18 * debug1;
/* 108 */       int debug20 = debug19 >> 6;
/* 109 */       int debug21 = (debug18 + 1) * debug1 - 1 >> 6;
/* 110 */       int debug22 = debug19 ^ debug20 << 6;
/*     */       
/* 112 */       if (debug20 != debug13) {
/* 113 */         debug14 = debug16;
/* 114 */         debug16 = (debug20 + 1 < debug3) ? debug2[debug20 + 1] : 0L;
/* 115 */         debug13 = debug20;
/*     */       } 
/*     */ 
/*     */       
/* 119 */       if (debug20 == debug21) {
/* 120 */         debug23 = debug14 >>> debug22 & debug4;
/*     */       } else {
/* 122 */         int i = 64 - debug22;
/* 123 */         debug23 = (debug14 >>> debug22 | debug16 << i) & debug4;
/*     */       } 
/*     */       
/* 126 */       int debug25 = debug10 + debug1;
/* 127 */       if (debug25 >= 64) {
/* 128 */         debug8[debug9++] = debug11;
/* 129 */         debug11 = debug23;
/* 130 */         debug10 = debug1;
/*     */       } else {
/* 132 */         debug11 |= debug23 << debug10;
/* 133 */         debug10 = debug25;
/*     */       } 
/*     */     } 
/* 136 */     if (debug11 != 0L) {
/* 137 */       debug8[debug9] = debug11;
/*     */     }
/*     */     
/* 140 */     return debug8;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BitStorageAlignFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */