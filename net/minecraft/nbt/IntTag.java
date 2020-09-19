/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntTag
/*     */   extends NumericTag
/*     */ {
/*     */   static class Cache
/*     */   {
/*  21 */     static final IntTag[] cache = new IntTag[1153];
/*     */     
/*     */     static {
/*  24 */       for (int debug0 = 0; debug0 < cache.length; debug0++)
/*  25 */         cache[debug0] = new IntTag(-128 + debug0); 
/*     */     }
/*     */   }
/*     */   
/*  29 */   public static final TagType<IntTag> TYPE = new TagType<IntTag>()
/*     */     {
/*     */       public IntTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  32 */         debug3.accountBits(96L);
/*  33 */         return IntTag.valueOf(debug1.readInt());
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  38 */         return "INT";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  43 */         return "TAG_Int";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  48 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final int data;
/*     */   
/*     */   private IntTag(int debug1) {
/*  55 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static IntTag valueOf(int debug0) {
/*  59 */     if (debug0 >= -128 && debug0 <= 1024)
/*  60 */       return Cache.cache[debug0 + 128]; 
/*  61 */     return new IntTag(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  66 */     debug1.writeInt(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  71 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<IntTag> getType() {
/*  76 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     return String.valueOf(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag copy() {
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  91 */     if (this == debug1) {
/*  92 */       return true;
/*     */     }
/*     */     
/*  95 */     return (debug1 instanceof IntTag && this.data == ((IntTag)debug1).data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 105 */     return (Component)(new TextComponent(String.valueOf(this.data))).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 110 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 115 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 120 */     return (short)(this.data & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 125 */     return (byte)(this.data & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 130 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 135 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 140 */     return Integer.valueOf(this.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\IntTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */