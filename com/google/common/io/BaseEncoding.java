/*      */ package com.google.common.io;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.Arrays;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class BaseEncoding
/*      */ {
/*      */   public static final class DecodingException
/*      */     extends IOException
/*      */   {
/*      */     DecodingException(String message) {
/*  131 */       super(message);
/*      */     }
/*      */     
/*      */     DecodingException(Throwable cause) {
/*  135 */       super(cause);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String encode(byte[] bytes) {
/*  143 */     return encode(bytes, 0, bytes.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String encode(byte[] bytes, int off, int len) {
/*  151 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  152 */     StringBuilder result = new StringBuilder(maxEncodedSize(len));
/*      */     try {
/*  154 */       encodeTo(result, bytes, off, len);
/*  155 */     } catch (IOException impossible) {
/*  156 */       throw new AssertionError(impossible);
/*      */     } 
/*  158 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public abstract OutputStream encodingStream(Writer paramWriter);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public final ByteSink encodingSink(final CharSink encodedSink) {
/*  174 */     Preconditions.checkNotNull(encodedSink);
/*  175 */     return new ByteSink()
/*      */       {
/*      */         public OutputStream openStream() throws IOException {
/*  178 */           return BaseEncoding.this.encodingStream(encodedSink.openStream());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] extract(byte[] result, int length) {
/*  186 */     if (length == result.length) {
/*  187 */       return result;
/*      */     }
/*  189 */     byte[] trunc = new byte[length];
/*  190 */     System.arraycopy(result, 0, trunc, 0, length);
/*  191 */     return trunc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean canDecode(CharSequence paramCharSequence);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] decode(CharSequence chars) {
/*      */     try {
/*  212 */       return decodeChecked(chars);
/*  213 */     } catch (DecodingException badInput) {
/*  214 */       throw new IllegalArgumentException(badInput);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final byte[] decodeChecked(CharSequence chars) throws DecodingException {
/*  226 */     chars = padding().trimTrailingFrom(chars);
/*  227 */     byte[] tmp = new byte[maxDecodedSize(chars.length())];
/*  228 */     int len = decodeTo(tmp, chars);
/*  229 */     return extract(tmp, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public abstract InputStream decodingStream(Reader paramReader);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public final ByteSource decodingSource(final CharSource encodedSource) {
/*  246 */     Preconditions.checkNotNull(encodedSource);
/*  247 */     return new ByteSource()
/*      */       {
/*      */         public InputStream openStream() throws IOException {
/*  250 */           return BaseEncoding.this.decodingStream(encodedSource.openStream());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  314 */   private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", 
/*      */       
/*  316 */       Character.valueOf('='));
/*      */ 
/*      */   
/*      */   abstract int maxEncodedSize(int paramInt);
/*      */ 
/*      */   
/*      */   abstract void encodeTo(Appendable paramAppendable, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
/*      */ 
/*      */   
/*      */   abstract int maxDecodedSize(int paramInt);
/*      */   
/*      */   abstract int decodeTo(byte[] paramArrayOfbyte, CharSequence paramCharSequence) throws DecodingException;
/*      */   
/*      */   abstract CharMatcher padding();
/*      */   
/*      */   public static BaseEncoding base64() {
/*  332 */     return BASE64;
/*      */   } public abstract BaseEncoding omitPadding(); public abstract BaseEncoding withPadChar(char paramChar); public abstract BaseEncoding withSeparator(String paramString, int paramInt); public abstract BaseEncoding upperCase();
/*      */   public abstract BaseEncoding lowerCase();
/*  335 */   private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", 
/*      */       
/*  337 */       Character.valueOf('='));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base64Url() {
/*  354 */     return BASE64_URL;
/*      */   }
/*      */   
/*  357 */   private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", 
/*  358 */       Character.valueOf('='));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base32() {
/*  373 */     return BASE32;
/*      */   }
/*      */   
/*  376 */   private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", 
/*  377 */       Character.valueOf('='));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base32Hex() {
/*  392 */     return BASE32_HEX;
/*      */   }
/*      */   
/*  395 */   private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base16() {
/*  411 */     return BASE16;
/*      */   }
/*      */   
/*      */   private static final class Alphabet
/*      */     extends CharMatcher {
/*      */     private final String name;
/*      */     private final char[] chars;
/*      */     final int mask;
/*      */     final int bitsPerChar;
/*      */     final int charsPerChunk;
/*      */     final int bytesPerChunk;
/*      */     private final byte[] decodabet;
/*      */     private final boolean[] validPadding;
/*      */     
/*      */     Alphabet(String name, char[] chars) {
/*  426 */       this.name = (String)Preconditions.checkNotNull(name);
/*  427 */       this.chars = (char[])Preconditions.checkNotNull(chars);
/*      */       try {
/*  429 */         this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
/*  430 */       } catch (ArithmeticException e) {
/*  431 */         throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  438 */       int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
/*      */       try {
/*  440 */         this.charsPerChunk = 8 / gcd;
/*  441 */         this.bytesPerChunk = this.bitsPerChar / gcd;
/*  442 */       } catch (ArithmeticException e) {
/*  443 */         throw new IllegalArgumentException("Illegal alphabet " + new String(chars), e);
/*      */       } 
/*      */       
/*  446 */       this.mask = chars.length - 1;
/*      */       
/*  448 */       byte[] decodabet = new byte[128];
/*  449 */       Arrays.fill(decodabet, (byte)-1);
/*  450 */       for (int i = 0; i < chars.length; i++) {
/*  451 */         char c = chars[i];
/*  452 */         Preconditions.checkArgument(CharMatcher.ascii().matches(c), "Non-ASCII character: %s", c);
/*  453 */         Preconditions.checkArgument((decodabet[c] == -1), "Duplicate character: %s", c);
/*  454 */         decodabet[c] = (byte)i;
/*      */       } 
/*  456 */       this.decodabet = decodabet;
/*      */       
/*  458 */       boolean[] validPadding = new boolean[this.charsPerChunk];
/*  459 */       for (int j = 0; j < this.bytesPerChunk; j++) {
/*  460 */         validPadding[IntMath.divide(j * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
/*      */       }
/*  462 */       this.validPadding = validPadding;
/*      */     }
/*      */     
/*      */     char encode(int bits) {
/*  466 */       return this.chars[bits];
/*      */     }
/*      */     
/*      */     boolean isValidPaddingStartPosition(int index) {
/*  470 */       return this.validPadding[index % this.charsPerChunk];
/*      */     }
/*      */     
/*      */     boolean canDecode(char ch) {
/*  474 */       return (ch <= '' && this.decodabet[ch] != -1);
/*      */     }
/*      */     
/*      */     int decode(char ch) throws BaseEncoding.DecodingException {
/*  478 */       if (ch > '' || this.decodabet[ch] == -1) {
/*  479 */         throw new BaseEncoding.DecodingException("Unrecognized character: " + (
/*      */             
/*  481 */             CharMatcher.invisible().matches(ch) ? ("0x" + Integer.toHexString(ch)) : Character.valueOf(ch)));
/*      */       }
/*  483 */       return this.decodabet[ch];
/*      */     }
/*      */     
/*      */     private boolean hasLowerCase() {
/*  487 */       for (char c : this.chars) {
/*  488 */         if (Ascii.isLowerCase(c)) {
/*  489 */           return true;
/*      */         }
/*      */       } 
/*  492 */       return false;
/*      */     }
/*      */     
/*      */     private boolean hasUpperCase() {
/*  496 */       for (char c : this.chars) {
/*  497 */         if (Ascii.isUpperCase(c)) {
/*  498 */           return true;
/*      */         }
/*      */       } 
/*  501 */       return false;
/*      */     }
/*      */     
/*      */     Alphabet upperCase() {
/*  505 */       if (!hasLowerCase()) {
/*  506 */         return this;
/*      */       }
/*  508 */       Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
/*  509 */       char[] upperCased = new char[this.chars.length];
/*  510 */       for (int i = 0; i < this.chars.length; i++) {
/*  511 */         upperCased[i] = Ascii.toUpperCase(this.chars[i]);
/*      */       }
/*  513 */       return new Alphabet(this.name + ".upperCase()", upperCased);
/*      */     }
/*      */ 
/*      */     
/*      */     Alphabet lowerCase() {
/*  518 */       if (!hasUpperCase()) {
/*  519 */         return this;
/*      */       }
/*  521 */       Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
/*  522 */       char[] lowerCased = new char[this.chars.length];
/*  523 */       for (int i = 0; i < this.chars.length; i++) {
/*  524 */         lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
/*      */       }
/*  526 */       return new Alphabet(this.name + ".lowerCase()", lowerCased);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/*  532 */       return (CharMatcher.ascii().matches(c) && this.decodabet[c] != -1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  537 */       return this.name;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object other) {
/*  542 */       if (other instanceof Alphabet) {
/*  543 */         Alphabet that = (Alphabet)other;
/*  544 */         return Arrays.equals(this.chars, that.chars);
/*      */       } 
/*  546 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  551 */       return Arrays.hashCode(this.chars);
/*      */     } }
/*      */   
/*      */   static class StandardBaseEncoding extends BaseEncoding {
/*      */     final BaseEncoding.Alphabet alphabet;
/*      */     @Nullable
/*      */     final Character paddingChar;
/*      */     private transient BaseEncoding upperCase;
/*      */     private transient BaseEncoding lowerCase;
/*      */     
/*      */     StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) {
/*  562 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
/*      */     }
/*      */     
/*      */     StandardBaseEncoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  566 */       this.alphabet = (BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet);
/*  567 */       Preconditions.checkArgument((paddingChar == null || 
/*  568 */           !alphabet.matches(paddingChar.charValue())), "Padding character %s was already in alphabet", paddingChar);
/*      */ 
/*      */       
/*  571 */       this.paddingChar = paddingChar;
/*      */     }
/*      */ 
/*      */     
/*      */     CharMatcher padding() {
/*  576 */       return (this.paddingChar == null) ? CharMatcher.none() : CharMatcher.is(this.paddingChar.charValue());
/*      */     }
/*      */ 
/*      */     
/*      */     int maxEncodedSize(int bytes) {
/*  581 */       return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(final Writer out) {
/*  587 */       Preconditions.checkNotNull(out);
/*  588 */       return new OutputStream() {
/*  589 */           int bitBuffer = 0;
/*  590 */           int bitBufferLength = 0;
/*  591 */           int writtenChars = 0;
/*      */ 
/*      */           
/*      */           public void write(int b) throws IOException {
/*  595 */             this.bitBuffer <<= 8;
/*  596 */             this.bitBuffer |= b & 0xFF;
/*  597 */             this.bitBufferLength += 8;
/*  598 */             while (this.bitBufferLength >= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar) {
/*  599 */               int charIndex = this.bitBuffer >> this.bitBufferLength - BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  600 */               out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  601 */               this.writtenChars++;
/*  602 */               this.bitBufferLength -= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void flush() throws IOException {
/*  608 */             out.flush();
/*      */           }
/*      */ 
/*      */           
/*      */           public void close() throws IOException {
/*  613 */             if (this.bitBufferLength > 0) {
/*  614 */               int charIndex = this.bitBuffer << BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  615 */               out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  616 */               this.writtenChars++;
/*  617 */               if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null) {
/*  618 */                 while (this.writtenChars % BaseEncoding.StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
/*  619 */                   out.write(BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue());
/*  620 */                   this.writtenChars++;
/*      */                 } 
/*      */               }
/*      */             } 
/*  624 */             out.close();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  631 */       Preconditions.checkNotNull(target);
/*  632 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length); int i;
/*  633 */       for (i = 0; i < len; i += this.alphabet.bytesPerChunk) {
/*  634 */         encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
/*      */       }
/*      */     }
/*      */     
/*      */     void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  639 */       Preconditions.checkNotNull(target);
/*  640 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  641 */       Preconditions.checkArgument((len <= this.alphabet.bytesPerChunk));
/*  642 */       long bitBuffer = 0L;
/*  643 */       for (int i = 0; i < len; i++) {
/*  644 */         bitBuffer |= (bytes[off + i] & 0xFF);
/*  645 */         bitBuffer <<= 8L;
/*      */       } 
/*      */       
/*  648 */       int bitOffset = (len + 1) * 8 - this.alphabet.bitsPerChar;
/*  649 */       int bitsProcessed = 0;
/*  650 */       while (bitsProcessed < len * 8) {
/*  651 */         int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & this.alphabet.mask;
/*  652 */         target.append(this.alphabet.encode(charIndex));
/*  653 */         bitsProcessed += this.alphabet.bitsPerChar;
/*      */       } 
/*  655 */       if (this.paddingChar != null) {
/*  656 */         while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
/*  657 */           target.append(this.paddingChar.charValue());
/*  658 */           bitsProcessed += this.alphabet.bitsPerChar;
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int maxDecodedSize(int chars) {
/*  665 */       return (int)((this.alphabet.bitsPerChar * chars + 7L) / 8L);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDecode(CharSequence chars) {
/*  670 */       chars = padding().trimTrailingFrom(chars);
/*  671 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  672 */         return false;
/*      */       }
/*  674 */       for (int i = 0; i < chars.length(); i++) {
/*  675 */         if (!this.alphabet.canDecode(chars.charAt(i))) {
/*  676 */           return false;
/*      */         }
/*      */       } 
/*  679 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  684 */       Preconditions.checkNotNull(target);
/*  685 */       chars = padding().trimTrailingFrom(chars);
/*  686 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  687 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  689 */       int bytesWritten = 0; int charIdx;
/*  690 */       for (charIdx = 0; charIdx < chars.length(); charIdx += this.alphabet.charsPerChunk) {
/*  691 */         long chunk = 0L;
/*  692 */         int charsProcessed = 0;
/*  693 */         for (int i = 0; i < this.alphabet.charsPerChunk; i++) {
/*  694 */           chunk <<= this.alphabet.bitsPerChar;
/*  695 */           if (charIdx + i < chars.length()) {
/*  696 */             chunk |= this.alphabet.decode(chars.charAt(charIdx + charsProcessed++));
/*      */           }
/*      */         } 
/*  699 */         int minOffset = this.alphabet.bytesPerChunk * 8 - charsProcessed * this.alphabet.bitsPerChar;
/*  700 */         for (int offset = (this.alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
/*  701 */           target[bytesWritten++] = (byte)(int)(chunk >>> offset & 0xFFL);
/*      */         }
/*      */       } 
/*  704 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(final Reader reader) {
/*  710 */       Preconditions.checkNotNull(reader);
/*  711 */       return new InputStream() {
/*  712 */           int bitBuffer = 0;
/*  713 */           int bitBufferLength = 0;
/*  714 */           int readChars = 0;
/*      */           boolean hitPadding = false;
/*  716 */           final CharMatcher paddingMatcher = BaseEncoding.StandardBaseEncoding.this.padding();
/*      */ 
/*      */           
/*      */           public int read() throws IOException {
/*      */             while (true) {
/*  721 */               int readChar = reader.read();
/*  722 */               if (readChar == -1) {
/*  723 */                 if (!this.hitPadding && !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
/*  724 */                   throw new BaseEncoding.DecodingException("Invalid input length " + this.readChars);
/*      */                 }
/*  726 */                 return -1;
/*      */               } 
/*  728 */               this.readChars++;
/*  729 */               char ch = (char)readChar;
/*  730 */               if (this.paddingMatcher.matches(ch)) {
/*  731 */                 if (!this.hitPadding && (this.readChars == 1 || 
/*  732 */                   !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
/*  733 */                   throw new BaseEncoding.DecodingException("Padding cannot start at index " + this.readChars);
/*      */                 }
/*  735 */                 this.hitPadding = true; continue;
/*  736 */               }  if (this.hitPadding) {
/*  737 */                 throw new BaseEncoding.DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars);
/*      */               }
/*      */               
/*  740 */               this.bitBuffer <<= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*  741 */               this.bitBuffer |= BaseEncoding.StandardBaseEncoding.this.alphabet.decode(ch);
/*  742 */               this.bitBufferLength += BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */               
/*  744 */               if (this.bitBufferLength >= 8) {
/*  745 */                 this.bitBufferLength -= 8;
/*  746 */                 return this.bitBuffer >> this.bitBufferLength & 0xFF;
/*      */               } 
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void close() throws IOException {
/*  754 */             reader.close();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding omitPadding() {
/*  761 */       return (this.paddingChar == null) ? this : newInstance(this.alphabet, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar) {
/*  766 */       if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar
/*  767 */         .charValue() == padChar)) {
/*  768 */         return this;
/*      */       }
/*  770 */       return newInstance(this.alphabet, Character.valueOf(padChar));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars) {
/*  776 */       Preconditions.checkArgument(
/*  777 */           padding().or(this.alphabet).matchesNoneOf(separator), "Separator (%s) cannot contain alphabet or padding characters", separator);
/*      */ 
/*      */       
/*  780 */       return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseEncoding upperCase() {
/*  788 */       BaseEncoding result = this.upperCase;
/*  789 */       if (result == null) {
/*  790 */         BaseEncoding.Alphabet upper = this.alphabet.upperCase();
/*      */         
/*  792 */         result = this.upperCase = (upper == this.alphabet) ? this : newInstance(upper, this.paddingChar);
/*      */       } 
/*  794 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding lowerCase() {
/*  799 */       BaseEncoding result = this.lowerCase;
/*  800 */       if (result == null) {
/*  801 */         BaseEncoding.Alphabet lower = this.alphabet.lowerCase();
/*      */         
/*  803 */         result = this.lowerCase = (lower == this.alphabet) ? this : newInstance(lower, this.paddingChar);
/*      */       } 
/*  805 */       return result;
/*      */     }
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  809 */       return new StandardBaseEncoding(alphabet, paddingChar);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  814 */       StringBuilder builder = new StringBuilder("BaseEncoding.");
/*  815 */       builder.append(this.alphabet.toString());
/*  816 */       if (8 % this.alphabet.bitsPerChar != 0) {
/*  817 */         if (this.paddingChar == null) {
/*  818 */           builder.append(".omitPadding()");
/*      */         } else {
/*  820 */           builder.append(".withPadChar('").append(this.paddingChar).append("')");
/*      */         } 
/*      */       }
/*  823 */       return builder.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object other) {
/*  828 */       if (other instanceof StandardBaseEncoding) {
/*  829 */         StandardBaseEncoding that = (StandardBaseEncoding)other;
/*  830 */         return (this.alphabet.equals(that.alphabet) && 
/*  831 */           Objects.equal(this.paddingChar, that.paddingChar));
/*      */       } 
/*  833 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  838 */       return this.alphabet.hashCode() ^ Objects.hashCode(new Object[] { this.paddingChar });
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base16Encoding extends StandardBaseEncoding {
/*  843 */     final char[] encoding = new char[512];
/*      */     
/*      */     Base16Encoding(String name, String alphabetChars) {
/*  846 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
/*      */     }
/*      */     
/*      */     private Base16Encoding(BaseEncoding.Alphabet alphabet) {
/*  850 */       super(alphabet, null);
/*  851 */       Preconditions.checkArgument((alphabet.chars.length == 16));
/*  852 */       for (int i = 0; i < 256; i++) {
/*  853 */         this.encoding[i] = alphabet.encode(i >>> 4);
/*  854 */         this.encoding[i | 0x100] = alphabet.encode(i & 0xF);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  860 */       Preconditions.checkNotNull(target);
/*  861 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  862 */       for (int i = 0; i < len; i++) {
/*  863 */         int b = bytes[off + i] & 0xFF;
/*  864 */         target.append(this.encoding[b]);
/*  865 */         target.append(this.encoding[b | 0x100]);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  871 */       Preconditions.checkNotNull(target);
/*  872 */       if (chars.length() % 2 == 1) {
/*  873 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  875 */       int bytesWritten = 0;
/*  876 */       for (int i = 0; i < chars.length(); i += 2) {
/*  877 */         int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
/*  878 */         target[bytesWritten++] = (byte)decoded;
/*      */       } 
/*  880 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  885 */       return new Base16Encoding(alphabet);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base64Encoding extends StandardBaseEncoding {
/*      */     Base64Encoding(String name, String alphabetChars, @Nullable Character paddingChar) {
/*  891 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
/*      */     }
/*      */     
/*      */     private Base64Encoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  895 */       super(alphabet, paddingChar);
/*  896 */       Preconditions.checkArgument((alphabet.chars.length == 64));
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  901 */       Preconditions.checkNotNull(target);
/*  902 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  903 */       int i = off;
/*  904 */       for (int remaining = len; remaining >= 3; remaining -= 3) {
/*  905 */         int chunk = (bytes[i++] & 0xFF) << 16 | (bytes[i++] & 0xFF) << 8 | bytes[i++] & 0xFF;
/*  906 */         target.append(this.alphabet.encode(chunk >>> 18));
/*  907 */         target.append(this.alphabet.encode(chunk >>> 12 & 0x3F));
/*  908 */         target.append(this.alphabet.encode(chunk >>> 6 & 0x3F));
/*  909 */         target.append(this.alphabet.encode(chunk & 0x3F));
/*      */       } 
/*  911 */       if (i < off + len) {
/*  912 */         encodeChunkTo(target, bytes, i, off + len - i);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  918 */       Preconditions.checkNotNull(target);
/*  919 */       chars = padding().trimTrailingFrom(chars);
/*  920 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  921 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  923 */       int bytesWritten = 0;
/*  924 */       for (int i = 0; i < chars.length(); ) {
/*  925 */         int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
/*  926 */         chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
/*  927 */         target[bytesWritten++] = (byte)(chunk >>> 16);
/*  928 */         if (i < chars.length()) {
/*  929 */           chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
/*  930 */           target[bytesWritten++] = (byte)(chunk >>> 8 & 0xFF);
/*  931 */           if (i < chars.length()) {
/*  932 */             chunk |= this.alphabet.decode(chars.charAt(i++));
/*  933 */             target[bytesWritten++] = (byte)(chunk & 0xFF);
/*      */           } 
/*      */         } 
/*      */       } 
/*  937 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  942 */       return new Base64Encoding(alphabet, paddingChar);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static Reader ignoringReader(final Reader delegate, final CharMatcher toIgnore) {
/*  948 */     Preconditions.checkNotNull(delegate);
/*  949 */     Preconditions.checkNotNull(toIgnore);
/*  950 */     return new Reader()
/*      */       {
/*      */         public int read() throws IOException {
/*      */           int readChar;
/*      */           do {
/*  955 */             readChar = delegate.read();
/*  956 */           } while (readChar != -1 && toIgnore.matches((char)readChar));
/*  957 */           return readChar;
/*      */         }
/*      */ 
/*      */         
/*      */         public int read(char[] cbuf, int off, int len) throws IOException {
/*  962 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() throws IOException {
/*  967 */           delegate.close();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static Appendable separatingAppendable(final Appendable delegate, final String separator, final int afterEveryChars) {
/*  974 */     Preconditions.checkNotNull(delegate);
/*  975 */     Preconditions.checkNotNull(separator);
/*  976 */     Preconditions.checkArgument((afterEveryChars > 0));
/*  977 */     return new Appendable() {
/*  978 */         int charsUntilSeparator = afterEveryChars;
/*      */ 
/*      */         
/*      */         public Appendable append(char c) throws IOException {
/*  982 */           if (this.charsUntilSeparator == 0) {
/*  983 */             delegate.append(separator);
/*  984 */             this.charsUntilSeparator = afterEveryChars;
/*      */           } 
/*  986 */           delegate.append(c);
/*  987 */           this.charsUntilSeparator--;
/*  988 */           return this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Appendable append(CharSequence chars, int off, int len) throws IOException {
/*  993 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public Appendable append(CharSequence chars) throws IOException {
/*  998 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars) {
/* 1007 */     final Appendable seperatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
/* 1008 */     return new Writer()
/*      */       {
/*      */         public void write(int c) throws IOException {
/* 1011 */           seperatingAppendable.append((char)c);
/*      */         }
/*      */ 
/*      */         
/*      */         public void write(char[] chars, int off, int len) throws IOException {
/* 1016 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public void flush() throws IOException {
/* 1021 */           delegate.flush();
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() throws IOException {
/* 1026 */           delegate.close();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static final class SeparatedBaseEncoding extends BaseEncoding {
/*      */     private final BaseEncoding delegate;
/*      */     private final String separator;
/*      */     private final int afterEveryChars;
/*      */     private final CharMatcher separatorChars;
/*      */     
/*      */     SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
/* 1038 */       this.delegate = (BaseEncoding)Preconditions.checkNotNull(delegate);
/* 1039 */       this.separator = (String)Preconditions.checkNotNull(separator);
/* 1040 */       this.afterEveryChars = afterEveryChars;
/* 1041 */       Preconditions.checkArgument((afterEveryChars > 0), "Cannot add a separator after every %s chars", afterEveryChars);
/*      */       
/* 1043 */       this.separatorChars = CharMatcher.anyOf(separator).precomputed();
/*      */     }
/*      */ 
/*      */     
/*      */     CharMatcher padding() {
/* 1048 */       return this.delegate.padding();
/*      */     }
/*      */ 
/*      */     
/*      */     int maxEncodedSize(int bytes) {
/* 1053 */       int unseparatedSize = this.delegate.maxEncodedSize(bytes);
/* 1054 */       return unseparatedSize + this.separator
/* 1055 */         .length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(Writer output) {
/* 1061 */       return this.delegate.encodingStream(separatingWriter(output, this.separator, this.afterEveryChars));
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/* 1066 */       this.delegate.encodeTo(separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
/*      */     }
/*      */ 
/*      */     
/*      */     int maxDecodedSize(int chars) {
/* 1071 */       return this.delegate.maxDecodedSize(chars);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDecode(CharSequence chars) {
/* 1076 */       return this.delegate.canDecode(this.separatorChars.removeFrom(chars));
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/* 1081 */       return this.delegate.decodeTo(target, this.separatorChars.removeFrom(chars));
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(Reader reader) {
/* 1087 */       return this.delegate.decodingStream(ignoringReader(reader, this.separatorChars));
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding omitPadding() {
/* 1092 */       return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar) {
/* 1097 */       return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars) {
/* 1102 */       throw new UnsupportedOperationException("Already have a separator");
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding upperCase() {
/* 1107 */       return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding lowerCase() {
/* 1112 */       return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1117 */       return this.delegate + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\BaseEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */