/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LongArrayTag
/*     */   extends CollectionTag<LongTag>
/*     */ {
/*  24 */   public static final TagType<LongArrayTag> TYPE = new TagType<LongArrayTag>()
/*     */     {
/*     */       public LongArrayTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  27 */         debug3.accountBits(192L);
/*     */         
/*  29 */         int debug4 = debug1.readInt();
/*  30 */         debug3.accountBits(64L * debug4);
/*  31 */         long[] debug5 = new long[debug4];
/*  32 */         for (int debug6 = 0; debug6 < debug4; debug6++) {
/*  33 */           debug5[debug6] = debug1.readLong();
/*     */         }
/*  35 */         return new LongArrayTag(debug5);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  40 */         return "LONG[]";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  45 */         return "TAG_Long_Array";
/*     */       }
/*     */     };
/*     */   
/*     */   private long[] data;
/*     */   
/*     */   public LongArrayTag(long[] debug1) {
/*  52 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public LongArrayTag(LongSet debug1) {
/*  56 */     this.data = debug1.toLongArray();
/*     */   }
/*     */   
/*     */   public LongArrayTag(List<Long> debug1) {
/*  60 */     this(toArray(debug1));
/*     */   }
/*     */   
/*     */   private static long[] toArray(List<Long> debug0) {
/*  64 */     long[] debug1 = new long[debug0.size()];
/*  65 */     for (int debug2 = 0; debug2 < debug0.size(); debug2++) {
/*  66 */       Long debug3 = debug0.get(debug2);
/*  67 */       debug1[debug2] = (debug3 == null) ? 0L : debug3.longValue();
/*     */     } 
/*     */     
/*  70 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  75 */     debug1.writeInt(this.data.length);
/*  76 */     for (long debug5 : this.data) {
/*  77 */       debug1.writeLong(debug5);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  83 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<LongArrayTag> getType() {
/*  88 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     StringBuilder debug1 = new StringBuilder("[L;");
/*  94 */     for (int debug2 = 0; debug2 < this.data.length; debug2++) {
/*  95 */       if (debug2 != 0) {
/*  96 */         debug1.append(',');
/*     */       }
/*  98 */       debug1.append(this.data[debug2]).append('L');
/*     */     } 
/* 100 */     return debug1.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public LongArrayTag copy() {
/* 105 */     long[] debug1 = new long[this.data.length];
/* 106 */     System.arraycopy(this.data, 0, debug1, 0, this.data.length);
/* 107 */     return new LongArrayTag(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 112 */     if (this == debug1) {
/* 113 */       return true;
/*     */     }
/*     */     
/* 116 */     return (debug1 instanceof LongArrayTag && Arrays.equals(this.data, ((LongArrayTag)debug1).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 121 */     return Arrays.hashCode(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 126 */     MutableComponent mutableComponent1 = (new TextComponent("L")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 127 */     MutableComponent debug4 = (new TextComponent("[")).append((Component)mutableComponent1).append(";");
/*     */     
/* 129 */     for (int debug5 = 0; debug5 < this.data.length; debug5++) {
/* 130 */       MutableComponent debug6 = (new TextComponent(String.valueOf(this.data[debug5]))).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/* 131 */       debug4.append(" ").append((Component)debug6).append((Component)mutableComponent1);
/* 132 */       if (debug5 != this.data.length - 1) {
/* 133 */         debug4.append(",");
/*     */       }
/*     */     } 
/*     */     
/* 137 */     debug4.append("]");
/*     */     
/* 139 */     return (Component)debug4;
/*     */   }
/*     */   
/*     */   public long[] getAsLongArray() {
/* 143 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 148 */     return this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag get(int debug1) {
/* 153 */     return LongTag.valueOf(this.data[debug1]);
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag set(int debug1, LongTag debug2) {
/* 158 */     long debug3 = this.data[debug1];
/* 159 */     this.data[debug1] = debug2.getAsLong();
/* 160 */     return LongTag.valueOf(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int debug1, LongTag debug2) {
/* 165 */     this.data = ArrayUtils.add(this.data, debug1, debug2.getAsLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int debug1, Tag debug2) {
/* 170 */     if (debug2 instanceof NumericTag) {
/* 171 */       this.data[debug1] = ((NumericTag)debug2).getAsLong();
/* 172 */       return true;
/*     */     } 
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int debug1, Tag debug2) {
/* 179 */     if (debug2 instanceof NumericTag) {
/* 180 */       this.data = ArrayUtils.add(this.data, debug1, ((NumericTag)debug2).getAsLong());
/* 181 */       return true;
/*     */     } 
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag remove(int debug1) {
/* 188 */     long debug2 = this.data[debug1];
/* 189 */     this.data = ArrayUtils.remove(this.data, debug1);
/* 190 */     return LongTag.valueOf(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getElementType() {
/* 195 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 200 */     this.data = new long[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\LongArrayTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */