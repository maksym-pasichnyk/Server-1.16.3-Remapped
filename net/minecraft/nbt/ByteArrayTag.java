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
/*     */ public class ByteArrayTag
/*     */   extends CollectionTag<ByteTag>
/*     */ {
/*  23 */   public static final TagType<ByteArrayTag> TYPE = new TagType<ByteArrayTag>()
/*     */     {
/*     */       public ByteArrayTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  26 */         debug3.accountBits(192L);
/*  27 */         int debug4 = debug1.readInt();
/*  28 */         debug3.accountBits(8L * debug4);
/*  29 */         byte[] debug5 = new byte[debug4];
/*  30 */         debug1.readFully(debug5);
/*  31 */         return new ByteArrayTag(debug5);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  36 */         return "BYTE[]";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  41 */         return "TAG_Byte_Array";
/*     */       }
/*     */     };
/*     */   
/*     */   private byte[] data;
/*     */   
/*     */   public ByteArrayTag(byte[] debug1) {
/*  48 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public ByteArrayTag(List<Byte> debug1) {
/*  52 */     this(toArray(debug1));
/*     */   }
/*     */   
/*     */   private static byte[] toArray(List<Byte> debug0) {
/*  56 */     byte[] debug1 = new byte[debug0.size()];
/*  57 */     for (int debug2 = 0; debug2 < debug0.size(); debug2++) {
/*  58 */       Byte debug3 = debug0.get(debug2);
/*  59 */       debug1[debug2] = (debug3 == null) ? 0 : debug3.byteValue();
/*     */     } 
/*     */     
/*  62 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  67 */     debug1.writeInt(this.data.length);
/*  68 */     debug1.write(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  73 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ByteArrayTag> getType() {
/*  78 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     StringBuilder debug1 = new StringBuilder("[B;");
/*  84 */     for (int debug2 = 0; debug2 < this.data.length; debug2++) {
/*  85 */       if (debug2 != 0) {
/*  86 */         debug1.append(',');
/*     */       }
/*  88 */       debug1.append(this.data[debug2]).append('B');
/*     */     } 
/*  90 */     return debug1.append(']').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag copy() {
/*  95 */     byte[] debug1 = new byte[this.data.length];
/*  96 */     System.arraycopy(this.data, 0, debug1, 0, this.data.length);
/*  97 */     return new ByteArrayTag(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 102 */     if (this == debug1) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     return (debug1 instanceof ByteArrayTag && Arrays.equals(this.data, ((ByteArrayTag)debug1).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 111 */     return Arrays.hashCode(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 116 */     MutableComponent mutableComponent1 = (new TextComponent("B")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 117 */     MutableComponent debug4 = (new TextComponent("[")).append((Component)mutableComponent1).append(";");
/*     */     
/* 119 */     for (int debug5 = 0; debug5 < this.data.length; debug5++) {
/* 120 */       MutableComponent debug6 = (new TextComponent(String.valueOf(this.data[debug5]))).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */       
/* 122 */       debug4.append(" ").append((Component)debug6).append((Component)mutableComponent1);
/*     */       
/* 124 */       if (debug5 != this.data.length - 1) {
/* 125 */         debug4.append(",");
/*     */       }
/*     */     } 
/*     */     
/* 129 */     debug4.append("]");
/*     */     
/* 131 */     return (Component)debug4;
/*     */   }
/*     */   
/*     */   public byte[] getAsByteArray() {
/* 135 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 140 */     return this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag get(int debug1) {
/* 145 */     return ByteTag.valueOf(this.data[debug1]);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag set(int debug1, ByteTag debug2) {
/* 150 */     byte debug3 = this.data[debug1];
/* 151 */     this.data[debug1] = debug2.getAsByte();
/* 152 */     return ByteTag.valueOf(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int debug1, ByteTag debug2) {
/* 157 */     this.data = ArrayUtils.add(this.data, debug1, debug2.getAsByte());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int debug1, Tag debug2) {
/* 162 */     if (debug2 instanceof NumericTag) {
/* 163 */       this.data[debug1] = ((NumericTag)debug2).getAsByte();
/* 164 */       return true;
/*     */     } 
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int debug1, Tag debug2) {
/* 171 */     if (debug2 instanceof NumericTag) {
/* 172 */       this.data = ArrayUtils.add(this.data, debug1, ((NumericTag)debug2).getAsByte());
/* 173 */       return true;
/*     */     } 
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag remove(int debug1) {
/* 180 */     byte debug2 = this.data[debug1];
/* 181 */     this.data = ArrayUtils.remove(this.data, debug1);
/* 182 */     return ByteTag.valueOf(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getElementType() {
/* 187 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 192 */     this.data = new byte[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\ByteArrayTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */