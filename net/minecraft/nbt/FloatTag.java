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
/*     */ public class FloatTag
/*     */   extends NumericTag
/*     */ {
/*  19 */   public static final FloatTag ZERO = new FloatTag(0.0F);
/*     */   
/*  21 */   public static final TagType<FloatTag> TYPE = new TagType<FloatTag>()
/*     */     {
/*     */       public FloatTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  24 */         debug3.accountBits(96L);
/*  25 */         return FloatTag.valueOf(debug1.readFloat());
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  30 */         return "FLOAT";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  35 */         return "TAG_Float";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  40 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final float data;
/*     */   
/*     */   private FloatTag(float debug1) {
/*  47 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static FloatTag valueOf(float debug0) {
/*  51 */     if (debug0 == 0.0F) {
/*  52 */       return ZERO;
/*     */     }
/*  54 */     return new FloatTag(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  59 */     debug1.writeFloat(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  64 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<FloatTag> getType() {
/*  69 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  74 */     return this.data + "f";
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatTag copy() {
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  84 */     if (this == debug1) {
/*  85 */       return true;
/*     */     }
/*     */     
/*  88 */     return (debug1 instanceof FloatTag && this.data == ((FloatTag)debug1).data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     return Float.floatToIntBits(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/*  98 */     MutableComponent mutableComponent = (new TextComponent("f")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/*  99 */     return (Component)(new TextComponent(String.valueOf(this.data))).append((Component)mutableComponent).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 104 */     return (long)this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 109 */     return Mth.floor(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 114 */     return (short)(Mth.floor(this.data) & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 119 */     return (byte)(Mth.floor(this.data) & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 124 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 129 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 134 */     return Float.valueOf(this.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\FloatTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */