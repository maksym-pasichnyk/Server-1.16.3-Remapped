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
/*     */ public class ByteTag
/*     */   extends NumericTag
/*     */ {
/*     */   static class Cache
/*     */   {
/*  19 */     private static final ByteTag[] cache = new ByteTag[256];
/*     */     
/*     */     static {
/*  22 */       for (int debug0 = 0; debug0 < cache.length; debug0++)
/*  23 */         cache[debug0] = new ByteTag((byte)(debug0 - 128)); 
/*     */     }
/*     */   }
/*     */   
/*  27 */   public static final TagType<ByteTag> TYPE = new TagType<ByteTag>()
/*     */     {
/*     */       public ByteTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  30 */         debug3.accountBits(72L);
/*  31 */         return ByteTag.valueOf(debug1.readByte());
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  36 */         return "BYTE";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  41 */         return "TAG_Byte";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  46 */         return true;
/*     */       }
/*     */     };
/*     */   
/*  50 */   public static final ByteTag ZERO = valueOf((byte)0);
/*  51 */   public static final ByteTag ONE = valueOf((byte)1);
/*     */   
/*     */   private final byte data;
/*     */   
/*     */   private ByteTag(byte debug1) {
/*  56 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static ByteTag valueOf(byte debug0) {
/*  60 */     return Cache.cache[128 + debug0];
/*     */   }
/*     */   
/*     */   public static ByteTag valueOf(boolean debug0) {
/*  64 */     return debug0 ? ONE : ZERO;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  69 */     debug1.writeByte(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  74 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ByteTag> getType() {
/*  79 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     return this.data + "b";
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag copy() {
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  94 */     if (this == debug1) {
/*  95 */       return true;
/*     */     }
/*     */     
/*  98 */     return (debug1 instanceof ByteTag && this.data == ((ByteTag)debug1).data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 108 */     MutableComponent mutableComponent = (new TextComponent("b")).withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 109 */     return (Component)(new TextComponent(String.valueOf(this.data))).append((Component)mutableComponent).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 114 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 119 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 124 */     return (short)this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 129 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 134 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 139 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 144 */     return Byte.valueOf(this.data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\ByteTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */