/*     */ package com.mojang.brigadier;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringReader
/*     */   implements ImmutableStringReader
/*     */ {
/*     */   private static final char SYNTAX_ESCAPE = '\\';
/*     */   private static final char SYNTAX_DOUBLE_QUOTE = '"';
/*     */   private static final char SYNTAX_SINGLE_QUOTE = '\'';
/*     */   private final String string;
/*     */   private int cursor;
/*     */   
/*     */   public StringReader(StringReader other) {
/*  17 */     this.string = other.string;
/*  18 */     this.cursor = other.cursor;
/*     */   }
/*     */   
/*     */   public StringReader(String string) {
/*  22 */     this.string = string;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/*  27 */     return this.string;
/*     */   }
/*     */   
/*     */   public void setCursor(int cursor) {
/*  31 */     this.cursor = cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingLength() {
/*  36 */     return this.string.length() - this.cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalLength() {
/*  41 */     return this.string.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCursor() {
/*  46 */     return this.cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRead() {
/*  51 */     return this.string.substring(0, this.cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRemaining() {
/*  56 */     return this.string.substring(this.cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(int length) {
/*  61 */     return (this.cursor + length <= this.string.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead() {
/*  66 */     return canRead(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public char peek() {
/*  71 */     return this.string.charAt(this.cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public char peek(int offset) {
/*  76 */     return this.string.charAt(this.cursor + offset);
/*     */   }
/*     */   
/*     */   public char read() {
/*  80 */     return this.string.charAt(this.cursor++);
/*     */   }
/*     */   
/*     */   public void skip() {
/*  84 */     this.cursor++;
/*     */   }
/*     */   
/*     */   public static boolean isAllowedNumber(char c) {
/*  88 */     return ((c >= '0' && c <= '9') || c == '.' || c == '-');
/*     */   }
/*     */   
/*     */   public static boolean isQuotedStringStart(char c) {
/*  92 */     return (c == '"' || c == '\'');
/*     */   }
/*     */   
/*     */   public void skipWhitespace() {
/*  96 */     while (canRead() && Character.isWhitespace(peek())) {
/*  97 */       skip();
/*     */     }
/*     */   }
/*     */   
/*     */   public int readInt() throws CommandSyntaxException {
/* 102 */     int start = this.cursor;
/* 103 */     while (canRead() && isAllowedNumber(peek())) {
/* 104 */       skip();
/*     */     }
/* 106 */     String number = this.string.substring(start, this.cursor);
/* 107 */     if (number.isEmpty()) {
/* 108 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
/*     */     }
/*     */     try {
/* 111 */       return Integer.parseInt(number);
/* 112 */     } catch (NumberFormatException ex) {
/* 113 */       this.cursor = start;
/* 114 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
/*     */     } 
/*     */   }
/*     */   
/*     */   public long readLong() throws CommandSyntaxException {
/* 119 */     int start = this.cursor;
/* 120 */     while (canRead() && isAllowedNumber(peek())) {
/* 121 */       skip();
/*     */     }
/* 123 */     String number = this.string.substring(start, this.cursor);
/* 124 */     if (number.isEmpty()) {
/* 125 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
/*     */     }
/*     */     try {
/* 128 */       return Long.parseLong(number);
/* 129 */     } catch (NumberFormatException ex) {
/* 130 */       this.cursor = start;
/* 131 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number);
/*     */     } 
/*     */   }
/*     */   
/*     */   public double readDouble() throws CommandSyntaxException {
/* 136 */     int start = this.cursor;
/* 137 */     while (canRead() && isAllowedNumber(peek())) {
/* 138 */       skip();
/*     */     }
/* 140 */     String number = this.string.substring(start, this.cursor);
/* 141 */     if (number.isEmpty()) {
/* 142 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
/*     */     }
/*     */     try {
/* 145 */       return Double.parseDouble(number);
/* 146 */     } catch (NumberFormatException ex) {
/* 147 */       this.cursor = start;
/* 148 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float readFloat() throws CommandSyntaxException {
/* 153 */     int start = this.cursor;
/* 154 */     while (canRead() && isAllowedNumber(peek())) {
/* 155 */       skip();
/*     */     }
/* 157 */     String number = this.string.substring(start, this.cursor);
/* 158 */     if (number.isEmpty()) {
/* 159 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
/*     */     }
/*     */     try {
/* 162 */       return Float.parseFloat(number);
/* 163 */     } catch (NumberFormatException ex) {
/* 164 */       this.cursor = start;
/* 165 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAllowedInUnquotedString(char c) {
/* 170 */     return ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_' || c == '-' || c == '.' || c == '+');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readUnquotedString() {
/* 178 */     int start = this.cursor;
/* 179 */     while (canRead() && isAllowedInUnquotedString(peek())) {
/* 180 */       skip();
/*     */     }
/* 182 */     return this.string.substring(start, this.cursor);
/*     */   }
/*     */   
/*     */   public String readQuotedString() throws CommandSyntaxException {
/* 186 */     if (!canRead()) {
/* 187 */       return "";
/*     */     }
/* 189 */     char next = peek();
/* 190 */     if (!isQuotedStringStart(next)) {
/* 191 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
/*     */     }
/* 193 */     skip();
/* 194 */     return readStringUntil(next);
/*     */   }
/*     */   
/*     */   public String readStringUntil(char terminator) throws CommandSyntaxException {
/* 198 */     StringBuilder result = new StringBuilder();
/* 199 */     boolean escaped = false;
/* 200 */     while (canRead()) {
/* 201 */       char c = read();
/* 202 */       if (escaped) {
/* 203 */         if (c == terminator || c == '\\') {
/* 204 */           result.append(c);
/* 205 */           escaped = false; continue;
/*     */         } 
/* 207 */         setCursor(getCursor() - 1);
/* 208 */         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
/*     */       } 
/* 210 */       if (c == '\\') {
/* 211 */         escaped = true; continue;
/* 212 */       }  if (c == terminator) {
/* 213 */         return result.toString();
/*     */       }
/* 215 */       result.append(c);
/*     */     } 
/*     */ 
/*     */     
/* 219 */     throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
/*     */   }
/*     */   
/*     */   public String readString() throws CommandSyntaxException {
/* 223 */     if (!canRead()) {
/* 224 */       return "";
/*     */     }
/* 226 */     char next = peek();
/* 227 */     if (isQuotedStringStart(next)) {
/* 228 */       skip();
/* 229 */       return readStringUntil(next);
/*     */     } 
/* 231 */     return readUnquotedString();
/*     */   }
/*     */   
/*     */   public boolean readBoolean() throws CommandSyntaxException {
/* 235 */     int start = this.cursor;
/* 236 */     String value = readString();
/* 237 */     if (value.isEmpty()) {
/* 238 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
/*     */     }
/*     */     
/* 241 */     if (value.equals("true"))
/* 242 */       return true; 
/* 243 */     if (value.equals("false")) {
/* 244 */       return false;
/*     */     }
/* 246 */     this.cursor = start;
/* 247 */     throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void expect(char c) throws CommandSyntaxException {
/* 252 */     if (!canRead() || peek() != c) {
/* 253 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
/*     */     }
/* 255 */     skip();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\StringReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */