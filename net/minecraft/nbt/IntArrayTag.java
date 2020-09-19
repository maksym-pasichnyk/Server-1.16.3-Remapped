/*     */ package net.minecraft.nbt;
/*     */ 
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
/*     */ public class IntArrayTag
/*     */   extends CollectionTag<IntTag>
/*     */ {
/*  23 */   public static final TagType<IntArrayTag> TYPE = new TagType<IntArrayTag>()
/*     */     {
/*     */       public IntArrayTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  26 */         debug3.accountBits(192L);
/*     */         
/*  28 */         int debug4 = debug1.readInt();
/*  29 */         debug3.accountBits(32L * debug4);
/*  30 */         int[] debug5 = new int[debug4];
/*  31 */         for (int debug6 = 0; debug6 < debug4; debug6++) {
/*  32 */           debug5[debug6] = debug1.readInt();
/*     */         }
/*  34 */         return new IntArrayTag(debug5);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  39 */         return "INT[]";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  44 */         return "TAG_Int_Array";
/*     */       }
/*     */     };
/*     */   
/*     */   private int[] data;
/*     */   
/*     */   public IntArrayTag(int[] debug1) {
/*  51 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public IntArrayTag(List<Integer> debug1) {
/*  55 */     this(toArray(debug1));
/*     */   }
/*     */   
/*     */   private static int[] toArray(List<Integer> debug0) {
/*  59 */     int[] debug1 = new int[debug0.size()];
/*  60 */     for (int debug2 = 0; debug2 < debug0.size(); debug2++) {
/*  61 */       Integer debug3 = debug0.get(debug2);
/*  62 */       debug1[debug2] = (debug3 == null) ? 0 : debug3.intValue();
/*     */     } 
/*     */     
/*  65 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  70 */     debug1.writeInt(this.data.length);
/*  71 */     for (int debug5 : this.data) {
/*  72 */       debug1.writeInt(debug5);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  78 */     return 11;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<IntArrayTag> getType() {
/*  83 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     StringBuilder debug1 = new StringBuilder("[I;");
/*  89 */     for (int debug2 = 0; debug2 < this.data.length; debug2++) {
/*  90 */       if (debug2 != 0) {
/*  91 */         debug1.append(',');
/*     */       }
/*  93 */       debug1.append(this.data[debug2]);
/*     */     } 
/*  95 */     return debug1.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public IntArrayTag copy() {
/* 100 */     int[] debug1 = new int[this.data.length];
/* 101 */     System.arraycopy(this.data, 0, debug1, 0, this.data.length);
/* 102 */     return new IntArrayTag(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 107 */     if (this == debug1) {
/* 108 */       return true;
/*     */     }
/*     */     
/* 111 */     return (debug1 instanceof IntArrayTag && Arrays.equals(this.data, ((IntArrayTag)debug1).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     return Arrays.hashCode(this.data);
/*     */   }
/*     */   
/*     */   public int[] getAsIntArray() {
/* 120 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 125 */     MutableComponent mutableComponent1 = (new TextComponent("I")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 126 */     MutableComponent debug4 = (new TextComponent("[")).append((Component)mutableComponent1).append(";");
/*     */     
/* 128 */     for (int debug5 = 0; debug5 < this.data.length; debug5++) {
/* 129 */       debug4.append(" ").append((Component)(new TextComponent(String.valueOf(this.data[debug5]))).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
/* 130 */       if (debug5 != this.data.length - 1) {
/* 131 */         debug4.append(",");
/*     */       }
/*     */     } 
/*     */     
/* 135 */     debug4.append("]");
/*     */     
/* 137 */     return (Component)debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 142 */     return this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag get(int debug1) {
/* 147 */     return IntTag.valueOf(this.data[debug1]);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag set(int debug1, IntTag debug2) {
/* 152 */     int debug3 = this.data[debug1];
/* 153 */     this.data[debug1] = debug2.getAsInt();
/* 154 */     return IntTag.valueOf(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int debug1, IntTag debug2) {
/* 159 */     this.data = ArrayUtils.add(this.data, debug1, debug2.getAsInt());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int debug1, Tag debug2) {
/* 164 */     if (debug2 instanceof NumericTag) {
/* 165 */       this.data[debug1] = ((NumericTag)debug2).getAsInt();
/* 166 */       return true;
/*     */     } 
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int debug1, Tag debug2) {
/* 173 */     if (debug2 instanceof NumericTag) {
/* 174 */       this.data = ArrayUtils.add(this.data, debug1, ((NumericTag)debug2).getAsInt());
/* 175 */       return true;
/*     */     } 
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag remove(int debug1) {
/* 182 */     int debug2 = this.data[debug1];
/* 183 */     this.data = ArrayUtils.remove(this.data, debug1);
/* 184 */     return IntTag.valueOf(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getElementType() {
/* 189 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 194 */     this.data = new int[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\IntArrayTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */