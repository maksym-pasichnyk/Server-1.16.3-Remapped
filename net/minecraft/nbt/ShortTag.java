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
/*     */ public class ShortTag
/*     */   extends NumericTag
/*     */ {
/*     */   static class Cache
/*     */   {
/*  21 */     static final ShortTag[] cache = new ShortTag[1153];
/*     */     
/*     */     static {
/*  24 */       for (int debug0 = 0; debug0 < cache.length; debug0++)
/*  25 */         cache[debug0] = new ShortTag((short)(-128 + debug0)); 
/*     */     }
/*     */   }
/*     */   
/*  29 */   public static final TagType<ShortTag> TYPE = new TagType<ShortTag>()
/*     */     {
/*     */       public ShortTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  32 */         debug3.accountBits(80L);
/*  33 */         return ShortTag.valueOf(debug1.readShort());
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  38 */         return "SHORT";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  43 */         return "TAG_Short";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  48 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final short data;
/*     */   
/*     */   private ShortTag(short debug1) {
/*  55 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static ShortTag valueOf(short debug0) {
/*  59 */     if (debug0 >= -128 && debug0 <= 1024)
/*  60 */       return Cache.cache[debug0 + 128]; 
/*  61 */     return new ShortTag(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  66 */     debug1.writeShort(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  71 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ShortTag> getType() {
/*  76 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     return this.data + "s";
/*     */   }
/*     */ 
/*     */   
/*     */   public ShortTag copy() {
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  91 */     if (this == debug1) {
/*  92 */       return true;
/*     */     }
/*     */     
/*  95 */     return (debug1 instanceof ShortTag && this.data == ((ShortTag)debug1).data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 105 */     MutableComponent mutableComponent = (new TextComponent("s")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
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
/* 116 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 121 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 126 */     return (byte)(this.data & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 131 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 136 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 141 */     return Short.valueOf(this.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\ShortTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */