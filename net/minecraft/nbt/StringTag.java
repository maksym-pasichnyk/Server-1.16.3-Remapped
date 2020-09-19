/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringTag
/*     */   implements Tag
/*     */ {
/*  19 */   public static final TagType<StringTag> TYPE = new TagType<StringTag>()
/*     */     {
/*     */       public StringTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  22 */         debug3.accountBits(288L);
/*     */ 
/*     */         
/*  25 */         String debug4 = debug1.readUTF();
/*  26 */         debug3.accountBits((16 * debug4.length()));
/*  27 */         return StringTag.valueOf(debug4);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  32 */         return "STRING";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  37 */         return "TAG_String";
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isValue() {
/*  42 */         return true;
/*     */       }
/*     */     };
/*     */   
/*  46 */   private static final StringTag EMPTY = new StringTag("");
/*     */   
/*     */   private final String data;
/*     */   
/*     */   private StringTag(String debug1) {
/*  51 */     Objects.requireNonNull(debug1, "Null string not allowed");
/*  52 */     this.data = debug1;
/*     */   }
/*     */   
/*     */   public static StringTag valueOf(String debug0) {
/*  56 */     if (debug0.isEmpty()) {
/*  57 */       return EMPTY;
/*     */     }
/*  59 */     return new StringTag(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  64 */     debug1.writeUTF(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  69 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<StringTag> getType() {
/*  74 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  79 */     return quoteAndEscape(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public StringTag copy() {
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  94 */     if (this == debug1) {
/*  95 */       return true;
/*     */     }
/*     */     
/*  98 */     return (debug1 instanceof StringTag && Objects.equals(this.data, ((StringTag)debug1).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 103 */     return this.data.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAsString() {
/* 108 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 113 */     String debug3 = quoteAndEscape(this.data);
/* 114 */     String debug4 = debug3.substring(0, 1);
/* 115 */     MutableComponent mutableComponent = (new TextComponent(debug3.substring(1, debug3.length() - 1))).withStyle(SYNTAX_HIGHLIGHTING_STRING);
/* 116 */     return (Component)(new TextComponent(debug4)).append((Component)mutableComponent).append(debug4);
/*     */   }
/*     */   
/*     */   public static String quoteAndEscape(String debug0) {
/* 120 */     StringBuilder debug1 = new StringBuilder(" ");
/* 121 */     char debug2 = Character.MIN_VALUE;
/* 122 */     for (int debug3 = 0; debug3 < debug0.length(); debug3++) {
/* 123 */       char debug4 = debug0.charAt(debug3);
/* 124 */       if (debug4 == '\\') {
/* 125 */         debug1.append('\\');
/* 126 */       } else if (debug4 == '"' || debug4 == '\'') {
/* 127 */         if (debug2 == '\000') {
/* 128 */           debug2 = (debug4 == '"') ? '\'' : '"';
/*     */         }
/* 130 */         if (debug2 == debug4) {
/* 131 */           debug1.append('\\');
/*     */         }
/*     */       } 
/* 134 */       debug1.append(debug4);
/*     */     } 
/* 136 */     if (debug2 == '\000') {
/* 137 */       debug2 = '"';
/*     */     }
/*     */     
/* 140 */     debug1.setCharAt(0, debug2);
/* 141 */     debug1.append(debug2);
/* 142 */     return debug1.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\StringTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */