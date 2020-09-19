/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LongTag
/*     */   extends NumericTag
/*     */ {
/*     */   static class Cache
/*     */   {
/*  21 */     static final LongTag[] cache = new LongTag[1153];
/*     */     
/*     */     static {
/*  24 */       for (int debug0 = 0; debug0 < cache.length; debug0++)
/*  25 */         cache[debug0] = new LongTag((-128 + debug0)); 
/*     */     }
/*     */   }
/*     */   
/*  29 */   public static final TagType<LongTag> TYPE = new TagType<LongTag>()
/*     */     {
/*     */       public LongTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  32 */         debug3.accountBits(128L);
/*  33 */         return LongTag.valueOf(debug1.readLong());
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  38 */         return "LONG";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  43 */         return "TAG_Long";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  48 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final long data;
/*     */   
/*     */   private LongTag(long debug1) {
/*  55 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static LongTag valueOf(long debug0) {
/*  59 */     if (debug0 >= -128L && debug0 <= 1024L)
/*  60 */       return Cache.cache[(int)debug0 + 128]; 
/*  61 */     return new LongTag(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  66 */     debug1.writeLong(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  71 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<LongTag> getType() {
/*  76 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     return this.data + "L";
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag copy() {
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  91 */     if (this == debug1) {
/*  92 */       return true;
/*     */     }
/*     */     
/*  95 */     return (debug1 instanceof LongTag && this.data == ((LongTag)debug1).data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return (int)(this.data ^ this.data >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 105 */     MutableComponent mutableComponent = (new TextComponent("L")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 106 */     return (Component)(new TextComponent(String.valueOf(this.data))).append((Component)mutableComponent).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 111 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 116 */     return (int)(this.data & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 121 */     return (short)(int)(this.data & 0xFFFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 126 */     return (byte)(int)(this.data & 0xFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 131 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 136 */     return (float)this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 141 */     return Long.valueOf(this.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\LongTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */