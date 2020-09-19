/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ 
/*     */ public class TagParser {
/*  16 */   public static final SimpleCommandExceptionType ERROR_TRAILING_DATA = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.nbt.trailing"));
/*  17 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_KEY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.nbt.expected.key"));
/*  18 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_VALUE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.nbt.expected.value")); static {
/*  19 */     ERROR_INSERT_MIXED_LIST = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.nbt.list.mixed", new Object[] { debug0, debug1 }));
/*  20 */     ERROR_INSERT_MIXED_ARRAY = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.nbt.array.mixed", new Object[] { debug0, debug1 }));
/*  21 */     ERROR_INVALID_ARRAY = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.nbt.array.invalid", new Object[] { debug0 }));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Dynamic2CommandExceptionType ERROR_INSERT_MIXED_LIST;
/*     */   
/*     */   public static final Dynamic2CommandExceptionType ERROR_INSERT_MIXED_ARRAY;
/*     */   
/*     */   public static final DynamicCommandExceptionType ERROR_INVALID_ARRAY;
/*  30 */   private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
/*  31 */   private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
/*  32 */   private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
/*  33 */   private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
/*  34 */   private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
/*  35 */   private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
/*  36 */   private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
/*     */   
/*     */   private final StringReader reader;
/*     */   
/*     */   public static CompoundTag parseTag(String debug0) throws CommandSyntaxException {
/*  41 */     return (new TagParser(new StringReader(debug0))).readSingleStruct();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   CompoundTag readSingleStruct() throws CommandSyntaxException {
/*  46 */     CompoundTag debug1 = readStruct();
/*     */     
/*  48 */     this.reader.skipWhitespace();
/*     */     
/*  50 */     if (this.reader.canRead()) {
/*  51 */       throw ERROR_TRAILING_DATA.createWithContext(this.reader);
/*     */     }
/*  53 */     return debug1;
/*     */   }
/*     */   
/*     */   public TagParser(StringReader debug1) {
/*  57 */     this.reader = debug1;
/*     */   }
/*     */   
/*     */   protected String readKey() throws CommandSyntaxException {
/*  61 */     this.reader.skipWhitespace();
/*     */     
/*  63 */     if (!this.reader.canRead()) {
/*  64 */       throw ERROR_EXPECTED_KEY.createWithContext(this.reader);
/*     */     }
/*     */     
/*  67 */     return this.reader.readString();
/*     */   }
/*     */   
/*     */   protected Tag readTypedValue() throws CommandSyntaxException {
/*  71 */     this.reader.skipWhitespace();
/*  72 */     int debug1 = this.reader.getCursor();
/*     */     
/*  74 */     if (StringReader.isQuotedStringStart(this.reader.peek())) {
/*  75 */       return StringTag.valueOf(this.reader.readQuotedString());
/*     */     }
/*     */     
/*  78 */     String debug2 = this.reader.readUnquotedString();
/*  79 */     if (debug2.isEmpty()) {
/*  80 */       this.reader.setCursor(debug1);
/*  81 */       throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
/*     */     } 
/*  83 */     return type(debug2);
/*     */   }
/*     */   
/*     */   private Tag type(String debug1) {
/*     */     try {
/*  88 */       if (FLOAT_PATTERN.matcher(debug1).matches()) {
/*  89 */         return FloatTag.valueOf(Float.parseFloat(debug1.substring(0, debug1.length() - 1)));
/*     */       }
/*  91 */       if (BYTE_PATTERN.matcher(debug1).matches()) {
/*  92 */         return ByteTag.valueOf(Byte.parseByte(debug1.substring(0, debug1.length() - 1)));
/*     */       }
/*  94 */       if (LONG_PATTERN.matcher(debug1).matches()) {
/*  95 */         return LongTag.valueOf(Long.parseLong(debug1.substring(0, debug1.length() - 1)));
/*     */       }
/*  97 */       if (SHORT_PATTERN.matcher(debug1).matches()) {
/*  98 */         return ShortTag.valueOf(Short.parseShort(debug1.substring(0, debug1.length() - 1)));
/*     */       }
/* 100 */       if (INT_PATTERN.matcher(debug1).matches()) {
/* 101 */         return IntTag.valueOf(Integer.parseInt(debug1));
/*     */       }
/* 103 */       if (DOUBLE_PATTERN.matcher(debug1).matches()) {
/* 104 */         return DoubleTag.valueOf(Double.parseDouble(debug1.substring(0, debug1.length() - 1)));
/*     */       }
/* 106 */       if (DOUBLE_PATTERN_NOSUFFIX.matcher(debug1).matches()) {
/* 107 */         return DoubleTag.valueOf(Double.parseDouble(debug1));
/*     */       }
/* 109 */       if ("true".equalsIgnoreCase(debug1)) {
/* 110 */         return ByteTag.ONE;
/*     */       }
/* 112 */       if ("false".equalsIgnoreCase(debug1)) {
/* 113 */         return ByteTag.ZERO;
/*     */       }
/* 115 */     } catch (NumberFormatException numberFormatException) {}
/*     */ 
/*     */     
/* 118 */     return StringTag.valueOf(debug1);
/*     */   }
/*     */   
/*     */   public Tag readValue() throws CommandSyntaxException {
/* 122 */     this.reader.skipWhitespace();
/*     */     
/* 124 */     if (!this.reader.canRead()) {
/* 125 */       throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
/*     */     }
/*     */     
/* 128 */     char debug1 = this.reader.peek();
/* 129 */     if (debug1 == '{')
/* 130 */       return readStruct(); 
/* 131 */     if (debug1 == '[') {
/* 132 */       return readList();
/*     */     }
/* 134 */     return readTypedValue();
/*     */   }
/*     */   
/*     */   protected Tag readList() throws CommandSyntaxException {
/* 138 */     if (this.reader.canRead(3) && 
/* 139 */       !StringReader.isQuotedStringStart(this.reader.peek(1)) && this.reader.peek(2) == ';') {
/* 140 */       return readArrayTag();
/*     */     }
/*     */     
/* 143 */     return readListTag();
/*     */   }
/*     */   
/*     */   public CompoundTag readStruct() throws CommandSyntaxException {
/* 147 */     expect('{');
/*     */     
/* 149 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 151 */     this.reader.skipWhitespace();
/* 152 */     while (this.reader.canRead() && this.reader.peek() != '}') {
/* 153 */       int debug2 = this.reader.getCursor();
/* 154 */       String debug3 = readKey();
/* 155 */       if (debug3.isEmpty()) {
/* 156 */         this.reader.setCursor(debug2);
/* 157 */         throw ERROR_EXPECTED_KEY.createWithContext(this.reader);
/*     */       } 
/*     */       
/* 160 */       expect(':');
/*     */       
/* 162 */       debug1.put(debug3, readValue());
/*     */       
/* 164 */       if (!hasElementSeparator())
/*     */         break; 
/* 166 */       if (!this.reader.canRead()) {
/* 167 */         throw ERROR_EXPECTED_KEY.createWithContext(this.reader);
/*     */       }
/*     */     } 
/* 170 */     expect('}');
/*     */     
/* 172 */     return debug1;
/*     */   }
/*     */   
/*     */   private Tag readListTag() throws CommandSyntaxException {
/* 176 */     expect('[');
/*     */     
/* 178 */     this.reader.skipWhitespace();
/*     */     
/* 180 */     if (!this.reader.canRead()) {
/* 181 */       throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
/*     */     }
/*     */     
/* 184 */     ListTag debug1 = new ListTag();
/*     */     
/* 186 */     TagType<?> debug2 = null;
/* 187 */     while (this.reader.peek() != ']') {
/* 188 */       int debug3 = this.reader.getCursor();
/* 189 */       Tag debug4 = readValue();
/*     */       
/* 191 */       TagType<?> debug5 = debug4.getType();
/* 192 */       if (debug2 == null) {
/* 193 */         debug2 = debug5;
/* 194 */       } else if (debug5 != debug2) {
/* 195 */         this.reader.setCursor(debug3);
/* 196 */         throw ERROR_INSERT_MIXED_LIST.createWithContext(this.reader, debug5.getPrettyName(), debug2.getPrettyName());
/*     */       } 
/*     */       
/* 199 */       debug1.add(debug4);
/*     */       
/* 201 */       if (!hasElementSeparator())
/*     */         break; 
/* 203 */       if (!this.reader.canRead()) {
/* 204 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
/*     */       }
/*     */     } 
/* 207 */     expect(']');
/*     */     
/* 209 */     return debug1;
/*     */   }
/*     */   
/*     */   private Tag readArrayTag() throws CommandSyntaxException {
/* 213 */     expect('[');
/* 214 */     int debug1 = this.reader.getCursor();
/* 215 */     char debug2 = this.reader.read();
/* 216 */     this.reader.read();
/*     */     
/* 218 */     this.reader.skipWhitespace();
/*     */     
/* 220 */     if (!this.reader.canRead()) {
/* 221 */       throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
/*     */     }
/*     */     
/* 224 */     if (debug2 == 'B')
/* 225 */       return new ByteArrayTag(readArray(ByteArrayTag.TYPE, ByteTag.TYPE)); 
/* 226 */     if (debug2 == 'L')
/* 227 */       return new LongArrayTag(readArray(LongArrayTag.TYPE, LongTag.TYPE)); 
/* 228 */     if (debug2 == 'I') {
/* 229 */       return new IntArrayTag(readArray(IntArrayTag.TYPE, IntTag.TYPE));
/*     */     }
/* 231 */     this.reader.setCursor(debug1);
/* 232 */     throw ERROR_INVALID_ARRAY.createWithContext(this.reader, String.valueOf(debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends Number> List<T> readArray(TagType<?> debug1, TagType<?> debug2) throws CommandSyntaxException {
/* 238 */     List<T> debug3 = Lists.newArrayList();
/*     */     
/* 240 */     while (this.reader.peek() != ']') {
/* 241 */       int debug4 = this.reader.getCursor();
/* 242 */       Tag debug5 = readValue();
/*     */       
/* 244 */       TagType<?> debug6 = debug5.getType();
/* 245 */       if (debug6 != debug2) {
/* 246 */         this.reader.setCursor(debug4);
/* 247 */         throw ERROR_INSERT_MIXED_ARRAY.createWithContext(this.reader, debug6.getPrettyName(), debug1.getPrettyName());
/*     */       } 
/*     */       
/* 250 */       if (debug2 == ByteTag.TYPE) {
/* 251 */         debug3.add((T)Byte.valueOf(((NumericTag)debug5).getAsByte()));
/* 252 */       } else if (debug2 == LongTag.TYPE) {
/* 253 */         debug3.add((T)Long.valueOf(((NumericTag)debug5).getAsLong()));
/*     */       } else {
/* 255 */         debug3.add((T)Integer.valueOf(((NumericTag)debug5).getAsInt()));
/*     */       } 
/*     */       
/* 258 */       if (!hasElementSeparator())
/*     */         break; 
/* 260 */       if (!this.reader.canRead()) {
/* 261 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
/*     */       }
/*     */     } 
/* 264 */     expect(']');
/*     */     
/* 266 */     return debug3;
/*     */   }
/*     */   
/*     */   private boolean hasElementSeparator() {
/* 270 */     this.reader.skipWhitespace();
/* 271 */     if (this.reader.canRead() && this.reader.peek() == ',') {
/* 272 */       this.reader.skip();
/* 273 */       this.reader.skipWhitespace();
/* 274 */       return true;
/*     */     } 
/* 276 */     return false;
/*     */   }
/*     */   
/*     */   private void expect(char debug1) throws CommandSyntaxException {
/* 280 */     this.reader.skipWhitespace();
/*     */     
/* 282 */     this.reader.expect(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\TagParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */