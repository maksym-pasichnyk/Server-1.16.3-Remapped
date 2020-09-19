/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  31 */   private static final int CDATA_END_LEN = "]]>".length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeHtmlTags(String input) {
/*  49 */     if (Strings.isEmpty(input) || (input.indexOf('"') == -1 && input.indexOf('&') == -1 && input.indexOf('<') == -1 && input.indexOf('>') == -1))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  54 */       return input;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*     */     
/*  62 */     int len = input.length();
/*  63 */     for (int i = 0; i < len; i++) {
/*  64 */       char ch = input.charAt(i);
/*  65 */       if (ch > '>') {
/*  66 */         buf.append(ch);
/*  67 */       } else if (ch == '<') {
/*  68 */         buf.append("&lt;");
/*  69 */       } else if (ch == '>') {
/*  70 */         buf.append("&gt;");
/*  71 */       } else if (ch == '&') {
/*  72 */         buf.append("&amp;");
/*  73 */       } else if (ch == '"') {
/*  74 */         buf.append("&quot;");
/*     */       } else {
/*  76 */         buf.append(ch);
/*     */       } 
/*     */     } 
/*  79 */     return buf.toString();
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
/*     */   public static void appendEscapingCData(StringBuilder buf, String str) {
/*  92 */     if (str != null) {
/*  93 */       int end = str.indexOf("]]>");
/*  94 */       if (end < 0) {
/*  95 */         buf.append(str);
/*     */       } else {
/*  97 */         int start = 0;
/*  98 */         while (end > -1) {
/*  99 */           buf.append(str.substring(start, end));
/* 100 */           buf.append("]]>]]&gt;<![CDATA[");
/* 101 */           start = end + CDATA_END_LEN;
/* 102 */           if (start < str.length()) {
/* 103 */             end = str.indexOf("]]>", start);
/*     */             continue;
/*     */           } 
/*     */           return;
/*     */         } 
/* 108 */         buf.append(str.substring(start));
/*     */       } 
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
/*     */   public static String escapeJsonControlCharacters(String input) {
/* 126 */     if (Strings.isEmpty(input) || (input.indexOf('"') == -1 && input.indexOf('\\') == -1 && input.indexOf('/') == -1 && input.indexOf('\b') == -1 && input.indexOf('\f') == -1 && input.indexOf('\n') == -1 && input.indexOf('\r') == -1 && input.indexOf('\t') == -1))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       return input;
/*     */     }
/*     */     
/* 138 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*     */     
/* 140 */     int len = input.length();
/* 141 */     for (int i = 0; i < len; i++) {
/* 142 */       char ch = input.charAt(i);
/* 143 */       String escBs = "\\";
/* 144 */       switch (ch) {
/*     */         case '"':
/* 146 */           buf.append("\\");
/* 147 */           buf.append(ch);
/*     */           break;
/*     */         case '\\':
/* 150 */           buf.append("\\");
/* 151 */           buf.append(ch);
/*     */           break;
/*     */         case '/':
/* 154 */           buf.append("\\");
/* 155 */           buf.append(ch);
/*     */           break;
/*     */         case '\b':
/* 158 */           buf.append("\\");
/* 159 */           buf.append('b');
/*     */           break;
/*     */         case '\f':
/* 162 */           buf.append("\\");
/* 163 */           buf.append('f');
/*     */           break;
/*     */         case '\n':
/* 166 */           buf.append("\\");
/* 167 */           buf.append('n');
/*     */           break;
/*     */         case '\r':
/* 170 */           buf.append("\\");
/* 171 */           buf.append('r');
/*     */           break;
/*     */         case '\t':
/* 174 */           buf.append("\\");
/* 175 */           buf.append('t');
/*     */           break;
/*     */         default:
/* 178 */           buf.append(ch); break;
/*     */       } 
/*     */     } 
/* 181 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\Transform.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */