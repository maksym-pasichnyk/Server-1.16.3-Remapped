/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import org.apache.commons.lang3.text.translate.AggregateTranslator;
/*     */ import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
/*     */ import org.apache.commons.lang3.text.translate.EntityArrays;
/*     */ import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
/*     */ import org.apache.commons.lang3.text.translate.LookupTranslator;
/*     */ import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
/*     */ import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
/*     */ import org.apache.commons.lang3.text.translate.OctalUnescaper;
/*     */ import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
/*     */ import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringEscapeUtils
/*     */ {
/*  53 */   public static final CharSequenceTranslator ESCAPE_JAVA = (new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\"
/*     */ 
/*     */         
/*     */         }
/*     */       
/*  58 */       })).with(new CharSequenceTranslator[] {
/*  59 */         (CharSequenceTranslator)new LookupTranslator((CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE())
/*  60 */       }).with(new CharSequenceTranslator[] {
/*  61 */         (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127)
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "'", "\\'" }, { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), (CharSequenceTranslator)new LookupTranslator(
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  82 */           (CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), 
/*  83 */         (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static final CharSequenceTranslator ESCAPE_JSON = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\"", "\\\"" }, { "\\", "\\\\" }, { "/", "\\/" } }), (CharSequenceTranslator)new LookupTranslator(
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 103 */           (CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), 
/* 104 */         (CharSequenceTranslator)JavaUnicodeEscaper.outsideOf(32, 127) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 118 */   public static final CharSequenceTranslator ESCAPE_XML = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 120 */           (CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 121 */           (CharSequence[][])EntityArrays.APOS_ESCAPE()) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public static final CharSequenceTranslator ESCAPE_XML10 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 135 */           (CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 136 */           (CharSequence[][])EntityArrays.APOS_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\000", "" }, { "\001", "" }, { "\002", "" }, { "\003", "" }, { "\004", "" }, { "\005", "" }, { "\006", "" }, { "\007", "" }, { "\b", "" }, { "\013", "" }, { "\f", "" }, { "\016", "" }, { "\017", "" }, { "\020", "" }, { "\021", "" }, { "\022", "" }, { "\023", "" }, { "\024", "" }, { "\025", "" }, { "\026", "" }, { "\027", "" }, { "\030", "" }, { "\031", "" }, { "\032", "" }, { "\033", "" }, { "\034", "" }, { "\035", "" }, { "\036", "" }, { "\037", "" }, { "￾", "" }, { "￿", ""
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 171 */           }), (CharSequenceTranslator)NumericEntityEscaper.between(127, 132), 
/* 172 */         (CharSequenceTranslator)NumericEntityEscaper.between(134, 159), (CharSequenceTranslator)new UnicodeUnpairedSurrogateRemover() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   public static final CharSequenceTranslator ESCAPE_XML11 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 187 */           (CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 188 */           (CharSequence[][])EntityArrays.APOS_ESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\000", "" }, { "\013", "&#11;" }, { "\f", "&#12;" }, { "￾", "" }, { "￿", ""
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */           
/* 197 */           }), (CharSequenceTranslator)NumericEntityEscaper.between(1, 8), 
/* 198 */         (CharSequenceTranslator)NumericEntityEscaper.between(14, 31), 
/* 199 */         (CharSequenceTranslator)NumericEntityEscaper.between(127, 132), 
/* 200 */         (CharSequenceTranslator)NumericEntityEscaper.between(134, 159), (CharSequenceTranslator)new UnicodeUnpairedSurrogateRemover() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   public static final CharSequenceTranslator ESCAPE_HTML3 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 215 */           (CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 216 */           (CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public static final CharSequenceTranslator ESCAPE_HTML4 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 230 */           (CharSequence[][])EntityArrays.BASIC_ESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 231 */           (CharSequence[][])EntityArrays.ISO8859_1_ESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 232 */           (CharSequence[][])EntityArrays.HTML40_EXTENDED_ESCAPE()) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public static final CharSequenceTranslator ESCAPE_CSV = new CsvEscaper();
/*     */ 
/*     */   
/*     */   static class CsvEscaper
/*     */     extends CharSequenceTranslator
/*     */   {
/*     */     private static final char CSV_DELIMITER = ',';
/*     */     
/*     */     private static final char CSV_QUOTE = '"';
/* 253 */     private static final String CSV_QUOTE_STR = String.valueOf('"');
/* 254 */     private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '"', '\r', '\n' };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 260 */       if (index != 0) {
/* 261 */         throw new IllegalStateException("CsvEscaper should never reach the [1] index");
/*     */       }
/*     */       
/* 264 */       if (StringUtils.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
/* 265 */         out.write(input.toString());
/*     */       } else {
/* 267 */         out.write(34);
/* 268 */         out.write(StringUtils.replace(input.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR + CSV_QUOTE_STR));
/* 269 */         out.write(34);
/*     */       } 
/* 271 */       return Character.codePointCount(input, 0, input.length());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 287 */   public static final CharSequenceTranslator UNESCAPE_JAVA = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new OctalUnescaper(), (CharSequenceTranslator)new UnicodeUnescaper(), (CharSequenceTranslator)new LookupTranslator(
/*     */ 
/*     */ 
/*     */           
/* 291 */           (CharSequence[][])EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator((CharSequence[][])new String[][] { { "\\\\", "\\" }, { "\\\"", "\"" }, { "\\'", "'" }, { "\\", "" } }) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public static final CharSequenceTranslator UNESCAPE_HTML3 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 334 */           (CharSequence[][])EntityArrays.BASIC_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 335 */           (CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   public static final CharSequenceTranslator UNESCAPE_HTML4 = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 350 */           (CharSequence[][])EntityArrays.BASIC_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 351 */           (CharSequence[][])EntityArrays.ISO8859_1_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 352 */           (CharSequence[][])EntityArrays.HTML40_EXTENDED_UNESCAPE()), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 365 */   public static final CharSequenceTranslator UNESCAPE_XML = (CharSequenceTranslator)new AggregateTranslator(new CharSequenceTranslator[] { (CharSequenceTranslator)new LookupTranslator(
/*     */           
/* 367 */           (CharSequence[][])EntityArrays.BASIC_UNESCAPE()), (CharSequenceTranslator)new LookupTranslator(
/* 368 */           (CharSequence[][])EntityArrays.APOS_UNESCAPE()), (CharSequenceTranslator)new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0]) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 381 */   public static final CharSequenceTranslator UNESCAPE_CSV = new CsvUnescaper();
/*     */   
/*     */   static class CsvUnescaper
/*     */     extends CharSequenceTranslator {
/*     */     private static final char CSV_DELIMITER = ',';
/*     */     private static final char CSV_QUOTE = '"';
/* 387 */     private static final String CSV_QUOTE_STR = String.valueOf('"');
/* 388 */     private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '"', '\r', '\n' };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 394 */       if (index != 0) {
/* 395 */         throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
/*     */       }
/*     */       
/* 398 */       if (input.charAt(0) != '"' || input.charAt(input.length() - 1) != '"') {
/* 399 */         out.write(input.toString());
/* 400 */         return Character.codePointCount(input, 0, input.length());
/*     */       } 
/*     */ 
/*     */       
/* 404 */       String quoteless = input.subSequence(1, input.length() - 1).toString();
/*     */       
/* 406 */       if (StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS)) {
/*     */         
/* 408 */         out.write(StringUtils.replace(quoteless, CSV_QUOTE_STR + CSV_QUOTE_STR, CSV_QUOTE_STR));
/*     */       } else {
/* 410 */         out.write(input.toString());
/*     */       } 
/* 412 */       return Character.codePointCount(input, 0, input.length());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String escapeJava(String input) {
/* 455 */     return ESCAPE_JAVA.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String escapeEcmaScript(String input) {
/* 483 */     return ESCAPE_ECMASCRIPT.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String escapeJson(String input) {
/* 511 */     return ESCAPE_JSON.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeJava(String input) {
/* 524 */     return UNESCAPE_JAVA.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeEcmaScript(String input) {
/* 541 */     return UNESCAPE_ECMASCRIPT.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeJson(String input) {
/* 558 */     return UNESCAPE_JSON.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String escapeHtml4(String input) {
/* 591 */     return ESCAPE_HTML4.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String escapeHtml3(String input) {
/* 604 */     return ESCAPE_HTML3.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeHtml4(String input) {
/* 626 */     return UNESCAPE_HTML4.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeHtml3(String input) {
/* 640 */     return UNESCAPE_HTML3.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final String escapeXml(String input) {
/* 666 */     return ESCAPE_XML.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeXml10(String input) {
/* 698 */     return ESCAPE_XML10.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeXml11(String input) {
/* 728 */     return ESCAPE_XML11.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeXml(String input) {
/* 750 */     return UNESCAPE_XML.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String escapeCsv(String input) {
/* 776 */     return ESCAPE_CSV.translate(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String unescapeCsv(String input) {
/* 801 */     return UNESCAPE_CSV.translate(input);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\StringEscapeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */