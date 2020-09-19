/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DoubleTag
/*     */   extends NumericTag
/*     */ {
/*  19 */   public static final DoubleTag ZERO = new DoubleTag(0.0D);
/*     */   
/*  21 */   public static final TagType<DoubleTag> TYPE = new TagType<DoubleTag>()
/*     */     {
/*     */       public DoubleTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  24 */         debug3.accountBits(128L);
/*  25 */         return DoubleTag.valueOf(debug1.readDouble());
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  30 */         return "DOUBLE";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  35 */         return "TAG_Double";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  40 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final double data;
/*     */   
/*     */   private DoubleTag(double debug1) {
/*  47 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static DoubleTag valueOf(double debug0) {
/*  51 */     if (debug0 == 0.0D) {
/*  52 */       return ZERO;
/*     */     }
/*  54 */     return new DoubleTag(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  59 */     debug1.writeDouble(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  64 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<DoubleTag> getType() {
/*  69 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  74 */     return this.data + "d";
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleTag copy() {
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  84 */     if (this == debug1) {
/*  85 */       return true;
/*     */     }
/*     */     
/*  88 */     return (debug1 instanceof DoubleTag && this.data == ((DoubleTag)debug1).data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     long debug1 = Double.doubleToLongBits(this.data);
/*  94 */     return (int)(debug1 ^ debug1 >>> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/*  99 */     MutableComponent mutableComponent = (new TextComponent("d")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 100 */     return (Component)(new TextComponent(String.valueOf(this.data))).append((Component)mutableComponent).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 105 */     return (long)Math.floor(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 110 */     return Mth.floor(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 115 */     return (short)(Mth.floor(this.data) & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 120 */     return (byte)(Mth.floor(this.data) & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 125 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 130 */     return (float)this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 135 */     return Double.valueOf(this.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\DoubleTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */