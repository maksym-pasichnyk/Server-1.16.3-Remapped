/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Transform;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "HtmlLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class HtmlLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";
/*     */   private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
/*  59 */   private static final String REGEXP = Strings.LINE_SEPARATOR.equals("\n") ? "\n" : (Strings.LINE_SEPARATOR + "|\n");
/*     */   
/*     */   private static final String DEFAULT_TITLE = "Log4j Log Messages";
/*     */   private static final String DEFAULT_CONTENT_TYPE = "text/html";
/*  63 */   private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
/*     */   
/*     */   private final boolean locationInfo;
/*     */   
/*     */   private final String title;
/*     */   private final String contentType;
/*     */   private final String font;
/*     */   private final String fontSize;
/*     */   private final String headerSize;
/*     */   
/*     */   public enum FontSize
/*     */   {
/*  75 */     SMALLER("smaller"), XXSMALL("xx-small"), XSMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE("large"),
/*  76 */     XLARGE("x-large"), XXLARGE("xx-large"), LARGER("larger");
/*     */     
/*     */     private final String size;
/*     */     
/*     */     FontSize(String size) {
/*  81 */       this.size = size;
/*     */     }
/*     */     
/*     */     public String getFontSize() {
/*  85 */       return this.size;
/*     */     }
/*     */     
/*     */     public static FontSize getFontSize(String size) {
/*  89 */       for (FontSize fontSize : values()) {
/*  90 */         if (fontSize.size.equals(size)) {
/*  91 */           return fontSize;
/*     */         }
/*     */       } 
/*  94 */       return SMALL;
/*     */     }
/*     */     
/*     */     public FontSize larger() {
/*  98 */       return (ordinal() < XXLARGE.ordinal()) ? values()[ordinal() + 1] : this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private HtmlLayout(boolean locationInfo, String title, String contentType, Charset charset, String font, String fontSize, String headerSize) {
/* 104 */     super(charset);
/* 105 */     this.locationInfo = locationInfo;
/* 106 */     this.title = title;
/* 107 */     this.contentType = addCharsetToContentType(contentType);
/* 108 */     this.font = font;
/* 109 */     this.fontSize = fontSize;
/* 110 */     this.headerSize = headerSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 117 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocationInfo() {
/* 124 */     return this.locationInfo;
/*     */   }
/*     */   
/*     */   private String addCharsetToContentType(String contentType) {
/* 128 */     if (contentType == null) {
/* 129 */       return "text/html; charset=" + getCharset();
/*     */     }
/* 131 */     return contentType.contains("charset") ? contentType : (contentType + "; charset=" + getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 142 */     StringBuilder sbuf = getStringBuilder();
/*     */     
/* 144 */     sbuf.append(Strings.LINE_SEPARATOR).append("<tr>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 146 */     sbuf.append("<td>");
/* 147 */     sbuf.append(event.getTimeMillis() - this.jvmStartTime);
/* 148 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 150 */     String escapedThread = Transform.escapeHtmlTags(event.getThreadName());
/* 151 */     sbuf.append("<td title=\"").append(escapedThread).append(" thread\">");
/* 152 */     sbuf.append(escapedThread);
/* 153 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 155 */     sbuf.append("<td title=\"Level\">");
/* 156 */     if (event.getLevel().equals(Level.DEBUG)) {
/* 157 */       sbuf.append("<font color=\"#339933\">");
/* 158 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/* 159 */       sbuf.append("</font>");
/* 160 */     } else if (event.getLevel().isMoreSpecificThan(Level.WARN)) {
/* 161 */       sbuf.append("<font color=\"#993300\"><strong>");
/* 162 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/* 163 */       sbuf.append("</strong></font>");
/*     */     } else {
/* 165 */       sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
/*     */     } 
/* 167 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 169 */     String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
/* 170 */     if (escapedLogger.isEmpty()) {
/* 171 */       escapedLogger = "root";
/*     */     }
/* 173 */     sbuf.append("<td title=\"").append(escapedLogger).append(" logger\">");
/* 174 */     sbuf.append(escapedLogger);
/* 175 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 177 */     if (this.locationInfo) {
/* 178 */       StackTraceElement element = event.getSource();
/* 179 */       sbuf.append("<td>");
/* 180 */       sbuf.append(Transform.escapeHtmlTags(element.getFileName()));
/* 181 */       sbuf.append(':');
/* 182 */       sbuf.append(element.getLineNumber());
/* 183 */       sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 186 */     sbuf.append("<td title=\"Message\">");
/* 187 */     sbuf.append(Transform.escapeHtmlTags(event.getMessage().getFormattedMessage()).replaceAll(REGEXP, "<br />"));
/* 188 */     sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
/* 189 */     sbuf.append("</tr>").append(Strings.LINE_SEPARATOR);
/*     */     
/* 191 */     if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
/* 192 */       sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
/* 193 */       sbuf.append(";\" colspan=\"6\" ");
/* 194 */       sbuf.append("title=\"Nested Diagnostic Context\">");
/* 195 */       sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
/* 196 */       sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 199 */     if (event.getContextData() != null && !event.getContextData().isEmpty()) {
/* 200 */       sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
/* 201 */       sbuf.append(";\" colspan=\"6\" ");
/* 202 */       sbuf.append("title=\"Mapped Diagnostic Context\">");
/* 203 */       sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextData().toMap().toString()));
/* 204 */       sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 207 */     Throwable throwable = event.getThrown();
/* 208 */     if (throwable != null) {
/* 209 */       sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
/* 210 */       sbuf.append(";\" colspan=\"6\">");
/* 211 */       appendThrowableAsHtml(throwable, sbuf);
/* 212 */       sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */     
/* 215 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 223 */     return this.contentType;
/*     */   }
/*     */   
/*     */   private void appendThrowableAsHtml(Throwable throwable, StringBuilder sbuf) {
/* 227 */     StringWriter sw = new StringWriter();
/* 228 */     PrintWriter pw = new PrintWriter(sw);
/*     */     try {
/* 230 */       throwable.printStackTrace(pw);
/* 231 */     } catch (RuntimeException runtimeException) {}
/*     */ 
/*     */     
/* 234 */     pw.flush();
/* 235 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/* 236 */     ArrayList<String> lines = new ArrayList<>();
/*     */     try {
/* 238 */       String line = reader.readLine();
/* 239 */       while (line != null) {
/* 240 */         lines.add(line);
/* 241 */         line = reader.readLine();
/*     */       } 
/* 243 */     } catch (IOException ex) {
/* 244 */       if (ex instanceof java.io.InterruptedIOException) {
/* 245 */         Thread.currentThread().interrupt();
/*     */       }
/* 247 */       lines.add(ex.toString());
/*     */     } 
/* 249 */     boolean first = true;
/* 250 */     for (String line : lines) {
/* 251 */       if (!first) {
/* 252 */         sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
/*     */       } else {
/* 254 */         first = false;
/*     */       } 
/* 256 */       sbuf.append(Transform.escapeHtmlTags(line));
/* 257 */       sbuf.append(Strings.LINE_SEPARATOR);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StringBuilder appendLs(StringBuilder sbuilder, String s) {
/* 262 */     sbuilder.append(s).append(Strings.LINE_SEPARATOR);
/* 263 */     return sbuilder;
/*     */   }
/*     */   
/*     */   private StringBuilder append(StringBuilder sbuilder, String s) {
/* 267 */     sbuilder.append(s);
/* 268 */     return sbuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 277 */     StringBuilder sbuf = new StringBuilder();
/* 278 */     append(sbuf, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
/* 279 */     appendLs(sbuf, "\"http://www.w3.org/TR/html4/loose.dtd\">");
/* 280 */     appendLs(sbuf, "<html>");
/* 281 */     appendLs(sbuf, "<head>");
/* 282 */     append(sbuf, "<meta charset=\"");
/* 283 */     append(sbuf, getCharset().toString());
/* 284 */     appendLs(sbuf, "\"/>");
/* 285 */     append(sbuf, "<title>").append(this.title);
/* 286 */     appendLs(sbuf, "</title>");
/* 287 */     appendLs(sbuf, "<style type=\"text/css\">");
/* 288 */     appendLs(sbuf, "<!--");
/* 289 */     append(sbuf, "body, table {font-family:").append(this.font).append("; font-size: ");
/* 290 */     appendLs(sbuf, this.headerSize).append(";}");
/* 291 */     appendLs(sbuf, "th {background: #336699; color: #FFFFFF; text-align: left;}");
/* 292 */     appendLs(sbuf, "-->");
/* 293 */     appendLs(sbuf, "</style>");
/* 294 */     appendLs(sbuf, "</head>");
/* 295 */     appendLs(sbuf, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">");
/* 296 */     appendLs(sbuf, "<hr size=\"1\" noshade=\"noshade\">");
/* 297 */     appendLs(sbuf, "Log session start time " + new Date() + "<br>");
/* 298 */     appendLs(sbuf, "<br>");
/* 299 */     appendLs(sbuf, "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
/*     */     
/* 301 */     appendLs(sbuf, "<tr>");
/* 302 */     appendLs(sbuf, "<th>Time</th>");
/* 303 */     appendLs(sbuf, "<th>Thread</th>");
/* 304 */     appendLs(sbuf, "<th>Level</th>");
/* 305 */     appendLs(sbuf, "<th>Logger</th>");
/* 306 */     if (this.locationInfo) {
/* 307 */       appendLs(sbuf, "<th>File:Line</th>");
/*     */     }
/* 309 */     appendLs(sbuf, "<th>Message</th>");
/* 310 */     appendLs(sbuf, "</tr>");
/* 311 */     return sbuf.toString().getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 320 */     StringBuilder sbuf = new StringBuilder();
/* 321 */     appendLs(sbuf, "</table>");
/* 322 */     appendLs(sbuf, "<br>");
/* 323 */     appendLs(sbuf, "</body></html>");
/* 324 */     return getBytes(sbuf.toString());
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
/*     */   @PluginFactory
/*     */   public static HtmlLayout createLayout(@PluginAttribute("locationInfo") boolean locationInfo, @PluginAttribute(value = "title", defaultString = "Log4j Log Messages") String title, @PluginAttribute("contentType") String contentType, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("fontSize") String fontSize, @PluginAttribute(value = "fontName", defaultString = "arial,sans-serif") String font) {
/* 345 */     FontSize fs = FontSize.getFontSize(fontSize);
/* 346 */     fontSize = fs.getFontSize();
/* 347 */     String headerSize = fs.larger().getFontSize();
/* 348 */     if (contentType == null) {
/* 349 */       contentType = "text/html; charset=" + charset;
/*     */     }
/* 351 */     return new HtmlLayout(locationInfo, title, contentType, charset, font, fontSize, headerSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HtmlLayout createDefaultLayout() {
/* 360 */     return newBuilder().build();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 365 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<HtmlLayout> {
/*     */     @PluginBuilderAttribute
/*     */     private boolean locationInfo = false;
/*     */     @PluginBuilderAttribute
/* 373 */     private String title = "Log4j Log Messages";
/*     */     
/*     */     @PluginBuilderAttribute
/* 376 */     private String contentType = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 379 */     private Charset charset = StandardCharsets.UTF_8;
/*     */     
/*     */     @PluginBuilderAttribute
/* 382 */     private HtmlLayout.FontSize fontSize = HtmlLayout.FontSize.SMALL;
/*     */     
/*     */     @PluginBuilderAttribute
/* 385 */     private String fontName = "arial,sans-serif";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withLocationInfo(boolean locationInfo) {
/* 392 */       this.locationInfo = locationInfo;
/* 393 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withTitle(String title) {
/* 397 */       this.title = title;
/* 398 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withContentType(String contentType) {
/* 402 */       this.contentType = contentType;
/* 403 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withCharset(Charset charset) {
/* 407 */       this.charset = charset;
/* 408 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFontSize(HtmlLayout.FontSize fontSize) {
/* 412 */       this.fontSize = fontSize;
/* 413 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withFontName(String fontName) {
/* 417 */       this.fontName = fontName;
/* 418 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HtmlLayout build() {
/* 424 */       if (this.contentType == null) {
/* 425 */         this.contentType = "text/html; charset=" + this.charset;
/*     */       }
/* 427 */       return new HtmlLayout(this.locationInfo, this.title, this.contentType, this.charset, this.fontName, this.fontSize.getFontSize(), this.fontSize.larger().getFontSize());
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\HtmlLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */