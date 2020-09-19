/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.ByteOrderMark;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlStreamReader
/*     */   extends Reader
/*     */ {
/*     */   private static final int BUFFER_SIZE = 4096;
/*     */   private static final String UTF_8 = "UTF-8";
/*     */   private static final String US_ASCII = "US-ASCII";
/*     */   private static final String UTF_16BE = "UTF-16BE";
/*     */   private static final String UTF_16LE = "UTF-16LE";
/*     */   private static final String UTF_32BE = "UTF-32BE";
/*     */   private static final String UTF_32LE = "UTF-32LE";
/*     */   private static final String UTF_16 = "UTF-16";
/*     */   private static final String UTF_32 = "UTF-32";
/*     */   private static final String EBCDIC = "CP1047";
/*  87 */   private static final ByteOrderMark[] BOMS = new ByteOrderMark[] { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[] { new ByteOrderMark("UTF-8", new int[] { 60, 63, 120, 109 }), new ByteOrderMark("UTF-16BE", new int[] { 0, 60, 0, 63 }), new ByteOrderMark("UTF-16LE", new int[] { 60, 0, 63, 0 }), new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109 }), new ByteOrderMark("UTF-32LE", new int[] { 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0 }), new ByteOrderMark("CP1047", new int[] { 76, 111, 167, 148 }) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Reader reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String defaultEncoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultEncoding() {
/* 122 */     return this.defaultEncoding;
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
/*     */   public XmlStreamReader(File file) throws IOException {
/* 138 */     this(new FileInputStream(file));
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
/*     */   public XmlStreamReader(InputStream is) throws IOException {
/* 153 */     this(is, true);
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
/*     */   public XmlStreamReader(InputStream is, boolean lenient) throws IOException {
/* 184 */     this(is, lenient, (String)null);
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
/*     */   public XmlStreamReader(InputStream is, boolean lenient, String defaultEncoding) throws IOException {
/* 217 */     this.defaultEncoding = defaultEncoding;
/* 218 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
/* 219 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 220 */     this.encoding = doRawStream(bom, pis, lenient);
/* 221 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   public XmlStreamReader(URL url) throws IOException {
/* 242 */     this(url.openConnection(), (String)null);
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
/*     */   public XmlStreamReader(URLConnection conn, String defaultEncoding) throws IOException {
/* 265 */     this.defaultEncoding = defaultEncoding;
/* 266 */     boolean lenient = true;
/* 267 */     String contentType = conn.getContentType();
/* 268 */     InputStream is = conn.getInputStream();
/* 269 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
/* 270 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 271 */     if (conn instanceof java.net.HttpURLConnection || contentType != null) {
/* 272 */       this.encoding = doHttpStream(bom, pis, contentType, true);
/*     */     } else {
/* 274 */       this.encoding = doRawStream(bom, pis, true);
/*     */     } 
/* 276 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   public XmlStreamReader(InputStream is, String httpContentType) throws IOException {
/* 298 */     this(is, httpContentType, true);
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
/*     */   public XmlStreamReader(InputStream is, String httpContentType, boolean lenient, String defaultEncoding) throws IOException {
/* 337 */     this.defaultEncoding = defaultEncoding;
/* 338 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(is, 4096), false, BOMS);
/* 339 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 340 */     this.encoding = doHttpStream(bom, pis, httpContentType, lenient);
/* 341 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   public XmlStreamReader(InputStream is, String httpContentType, boolean lenient) throws IOException {
/* 379 */     this(is, httpContentType, lenient, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 388 */     return this.encoding;
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
/*     */   public int read(char[] buf, int offset, int len) throws IOException {
/* 401 */     return this.reader.read(buf, offset, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 411 */     this.reader.close();
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
/*     */   private String doRawStream(BOMInputStream bom, BOMInputStream pis, boolean lenient) throws IOException {
/* 426 */     String bomEnc = bom.getBOMCharsetName();
/* 427 */     String xmlGuessEnc = pis.getBOMCharsetName();
/* 428 */     String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
/*     */     try {
/* 430 */       return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
/* 431 */     } catch (XmlStreamReaderException ex) {
/* 432 */       if (lenient) {
/* 433 */         return doLenientDetection(null, ex);
/*     */       }
/* 435 */       throw ex;
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
/*     */   private String doHttpStream(BOMInputStream bom, BOMInputStream pis, String httpContentType, boolean lenient) throws IOException {
/* 453 */     String bomEnc = bom.getBOMCharsetName();
/* 454 */     String xmlGuessEnc = pis.getBOMCharsetName();
/* 455 */     String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
/*     */     try {
/* 457 */       return calculateHttpEncoding(httpContentType, bomEnc, xmlGuessEnc, xmlEnc, lenient);
/*     */     }
/* 459 */     catch (XmlStreamReaderException ex) {
/* 460 */       if (lenient) {
/* 461 */         return doLenientDetection(httpContentType, ex);
/*     */       }
/* 463 */       throw ex;
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
/*     */   private String doLenientDetection(String httpContentType, XmlStreamReaderException ex) throws IOException {
/* 479 */     if (httpContentType != null && httpContentType.startsWith("text/html")) {
/* 480 */       httpContentType = httpContentType.substring("text/html".length());
/* 481 */       httpContentType = "text/xml" + httpContentType;
/*     */       try {
/* 483 */         return calculateHttpEncoding(httpContentType, ex.getBomEncoding(), ex.getXmlGuessEncoding(), ex.getXmlEncoding(), true);
/*     */       }
/* 485 */       catch (XmlStreamReaderException ex2) {
/* 486 */         ex = ex2;
/*     */       } 
/*     */     } 
/* 489 */     String encoding = ex.getXmlEncoding();
/* 490 */     if (encoding == null) {
/* 491 */       encoding = ex.getContentTypeEncoding();
/*     */     }
/* 493 */     if (encoding == null) {
/* 494 */       encoding = (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
/*     */     }
/* 496 */     return encoding;
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
/*     */   String calculateRawEncoding(String bomEnc, String xmlGuessEnc, String xmlEnc) throws IOException {
/* 512 */     if (bomEnc == null) {
/* 513 */       if (xmlGuessEnc == null || xmlEnc == null) {
/* 514 */         return (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
/*     */       }
/* 516 */       if (xmlEnc.equals("UTF-16") && (xmlGuessEnc.equals("UTF-16BE") || xmlGuessEnc.equals("UTF-16LE")))
/*     */       {
/* 518 */         return xmlGuessEnc;
/*     */       }
/* 520 */       return xmlEnc;
/*     */     } 
/*     */ 
/*     */     
/* 524 */     if (bomEnc.equals("UTF-8")) {
/* 525 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals("UTF-8")) {
/* 526 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 527 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 529 */       if (xmlEnc != null && !xmlEnc.equals("UTF-8")) {
/* 530 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 531 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 533 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 537 */     if (bomEnc.equals("UTF-16BE") || bomEnc.equals("UTF-16LE")) {
/* 538 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
/* 539 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 540 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 542 */       if (xmlEnc != null && !xmlEnc.equals("UTF-16") && !xmlEnc.equals(bomEnc)) {
/* 543 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 544 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 546 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 550 */     if (bomEnc.equals("UTF-32BE") || bomEnc.equals("UTF-32LE")) {
/* 551 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
/* 552 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 553 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 555 */       if (xmlEnc != null && !xmlEnc.equals("UTF-32") && !xmlEnc.equals(bomEnc)) {
/* 556 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 557 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 559 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 563 */     String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 564 */     throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
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
/*     */   String calculateHttpEncoding(String httpContentType, String bomEnc, String xmlGuessEnc, String xmlEnc, boolean lenient) throws IOException {
/* 585 */     if (lenient && xmlEnc != null) {
/* 586 */       return xmlEnc;
/*     */     }
/*     */ 
/*     */     
/* 590 */     String cTMime = getContentTypeMime(httpContentType);
/* 591 */     String cTEnc = getContentTypeEncoding(httpContentType);
/* 592 */     boolean appXml = isAppXml(cTMime);
/* 593 */     boolean textXml = isTextXml(cTMime);
/*     */ 
/*     */     
/* 596 */     if (!appXml && !textXml) {
/* 597 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 598 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */ 
/*     */     
/* 602 */     if (cTEnc == null) {
/* 603 */       if (appXml) {
/* 604 */         return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
/*     */       }
/* 606 */       return (this.defaultEncoding == null) ? "US-ASCII" : this.defaultEncoding;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 611 */     if (cTEnc.equals("UTF-16BE") || cTEnc.equals("UTF-16LE")) {
/* 612 */       if (bomEnc != null) {
/* 613 */         String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 614 */         throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 616 */       return cTEnc;
/*     */     } 
/*     */ 
/*     */     
/* 620 */     if (cTEnc.equals("UTF-16")) {
/* 621 */       if (bomEnc != null && bomEnc.startsWith("UTF-16")) {
/* 622 */         return bomEnc;
/*     */       }
/* 624 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 625 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */ 
/*     */     
/* 629 */     if (cTEnc.equals("UTF-32BE") || cTEnc.equals("UTF-32LE")) {
/* 630 */       if (bomEnc != null) {
/* 631 */         String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 632 */         throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 634 */       return cTEnc;
/*     */     } 
/*     */ 
/*     */     
/* 638 */     if (cTEnc.equals("UTF-32")) {
/* 639 */       if (bomEnc != null && bomEnc.startsWith("UTF-32")) {
/* 640 */         return bomEnc;
/*     */       }
/* 642 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 643 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */     
/* 646 */     return cTEnc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getContentTypeMime(String httpContentType) {
/* 656 */     String mime = null;
/* 657 */     if (httpContentType != null) {
/* 658 */       int i = httpContentType.indexOf(";");
/* 659 */       if (i >= 0) {
/* 660 */         mime = httpContentType.substring(0, i);
/*     */       } else {
/* 662 */         mime = httpContentType;
/*     */       } 
/* 664 */       mime = mime.trim();
/*     */     } 
/* 666 */     return mime;
/*     */   }
/*     */   
/* 669 */   private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getContentTypeEncoding(String httpContentType) {
/* 680 */     String encoding = null;
/* 681 */     if (httpContentType != null) {
/* 682 */       int i = httpContentType.indexOf(";");
/* 683 */       if (i > -1) {
/* 684 */         String postMime = httpContentType.substring(i + 1);
/* 685 */         Matcher m = CHARSET_PATTERN.matcher(postMime);
/* 686 */         encoding = m.find() ? m.group(1) : null;
/* 687 */         encoding = (encoding != null) ? encoding.toUpperCase(Locale.US) : null;
/*     */       } 
/*     */     } 
/* 690 */     return encoding;
/*     */   }
/*     */   
/* 693 */   public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
/*     */   
/*     */   private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
/*     */   
/*     */   private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
/*     */   
/*     */   private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
/*     */   
/*     */   private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
/*     */   
/*     */   private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
/*     */ 
/*     */   
/*     */   private static String getXmlProlog(InputStream is, String guessedEnc) throws IOException {
/* 707 */     String encoding = null;
/* 708 */     if (guessedEnc != null) {
/* 709 */       byte[] bytes = new byte[4096];
/* 710 */       is.mark(4096);
/* 711 */       int offset = 0;
/* 712 */       int max = 4096;
/* 713 */       int c = is.read(bytes, offset, max);
/* 714 */       int firstGT = -1;
/* 715 */       String xmlProlog = "";
/* 716 */       while (c != -1 && firstGT == -1 && offset < 4096) {
/* 717 */         offset += c;
/* 718 */         max -= c;
/* 719 */         c = is.read(bytes, offset, max);
/* 720 */         xmlProlog = new String(bytes, 0, offset, guessedEnc);
/* 721 */         firstGT = xmlProlog.indexOf('>');
/*     */       } 
/* 723 */       if (firstGT == -1) {
/* 724 */         if (c == -1) {
/* 725 */           throw new IOException("Unexpected end of XML stream");
/*     */         }
/* 727 */         throw new IOException("XML prolog or ROOT element not found on first " + offset + " bytes");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 732 */       int bytesRead = offset;
/* 733 */       if (bytesRead > 0) {
/* 734 */         is.reset();
/* 735 */         BufferedReader bReader = new BufferedReader(new StringReader(xmlProlog.substring(0, firstGT + 1)));
/*     */         
/* 737 */         StringBuffer prolog = new StringBuffer();
/* 738 */         String line = bReader.readLine();
/* 739 */         while (line != null) {
/* 740 */           prolog.append(line);
/* 741 */           line = bReader.readLine();
/*     */         } 
/* 743 */         Matcher m = ENCODING_PATTERN.matcher(prolog);
/* 744 */         if (m.find()) {
/* 745 */           encoding = m.group(1).toUpperCase();
/* 746 */           encoding = encoding.substring(1, encoding.length() - 1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 750 */     return encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isAppXml(String mime) {
/* 761 */     return (mime != null && (mime.equals("application/xml") || mime.equals("application/xml-dtd") || mime.equals("application/xml-external-parsed-entity") || (mime.startsWith("application/") && mime.endsWith("+xml"))));
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
/*     */   static boolean isTextXml(String mime) {
/* 776 */     return (mime != null && (mime.equals("text/xml") || mime.equals("text/xml-external-parsed-entity") || (mime.startsWith("text/") && mime.endsWith("+xml"))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\input\XmlStreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */