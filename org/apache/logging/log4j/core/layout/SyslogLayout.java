/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Priority;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SyslogLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class SyslogLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractStringLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SyslogLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private Facility facility;
/*     */     @PluginBuilderAttribute("newLine")
/*     */     private boolean includeNewLine;
/*     */     @PluginBuilderAttribute("newLineEscape")
/*     */     private String escapeNL;
/*     */     
/*     */     public Builder() {
/*  65 */       this.facility = Facility.LOCAL0;
/*     */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SyslogLayout build() {
/*  76 */       return new SyslogLayout(this.facility, this.includeNewLine, this.escapeNL, getCharset());
/*     */     }
/*     */     
/*     */     public Facility getFacility() {
/*  80 */       return this.facility;
/*     */     }
/*     */     
/*     */     public boolean isIncludeNewLine() {
/*  84 */       return this.includeNewLine;
/*     */     }
/*     */     
/*     */     public String getEscapeNL() {
/*  88 */       return this.escapeNL;
/*     */     }
/*     */     
/*     */     public B setFacility(Facility facility) {
/*  92 */       this.facility = facility;
/*  93 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setIncludeNewLine(boolean includeNewLine) {
/*  97 */       this.includeNewLine = includeNewLine;
/*  98 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setEscapeNL(String escapeNL) {
/* 102 */       this.escapeNL = escapeNL;
/* 103 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 110 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
/*     */ 
/*     */   
/*     */   private final Facility facility;
/*     */   
/*     */   private final boolean includeNewLine;
/*     */   
/*     */   private final String escapeNewLine;
/*     */   
/* 125 */   private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   private final String localHostname = NetUtils.getLocalHostname();
/*     */   
/*     */   protected SyslogLayout(Facility facility, boolean includeNL, String escapeNL, Charset charset) {
/* 133 */     super(charset);
/* 134 */     this.facility = facility;
/* 135 */     this.includeNewLine = includeNL;
/* 136 */     this.escapeNewLine = (escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL);
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
/* 147 */     StringBuilder buf = getStringBuilder();
/*     */     
/* 149 */     buf.append('<');
/* 150 */     buf.append(Priority.getPriority(this.facility, event.getLevel()));
/* 151 */     buf.append('>');
/* 152 */     addDate(event.getTimeMillis(), buf);
/* 153 */     buf.append(' ');
/* 154 */     buf.append(this.localHostname);
/* 155 */     buf.append(' ');
/*     */     
/* 157 */     String message = event.getMessage().getFormattedMessage();
/* 158 */     if (null != this.escapeNewLine) {
/* 159 */       message = NEWLINE_PATTERN.matcher(message).replaceAll(this.escapeNewLine);
/*     */     }
/* 161 */     buf.append(message);
/*     */     
/* 163 */     if (this.includeNewLine) {
/* 164 */       buf.append('\n');
/*     */     }
/* 166 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private synchronized void addDate(long timestamp, StringBuilder buf) {
/* 170 */     int index = buf.length() + 4;
/* 171 */     buf.append(this.dateFormat.format(new Date(timestamp)));
/*     */     
/* 173 */     if (buf.charAt(index) == '0') {
/* 174 */       buf.setCharAt(index, ' ');
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
/*     */   public Map<String, String> getContentFormat() {
/* 192 */     Map<String, String> result = new HashMap<>();
/* 193 */     result.put("structured", "false");
/* 194 */     result.put("formatType", "logfilepatternreceiver");
/* 195 */     result.put("dateFormat", this.dateFormat.toPattern());
/* 196 */     result.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
/* 197 */     return result;
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
/*     */   @Deprecated
/*     */   public static SyslogLayout createLayout(Facility facility, boolean includeNewLine, String escapeNL, Charset charset) {
/* 213 */     return new SyslogLayout(facility, includeNewLine, escapeNL, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Facility getFacility() {
/* 222 */     return this.facility;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\SyslogLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */