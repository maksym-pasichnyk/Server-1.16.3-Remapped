/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.layout.LoggerFields;
/*     */ import org.apache.logging.log4j.core.layout.Rfc5424Layout;
/*     */ import org.apache.logging.log4j.core.layout.SyslogLayout;
/*     */ import org.apache.logging.log4j.core.net.AbstractSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.net.SocketOptions;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Syslog", category = "Core", elementType = "appender", printObject = true)
/*     */ public class SyslogAppender
/*     */   extends SocketAppender
/*     */ {
/*     */   protected static final String RFC5424 = "RFC5424";
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends SocketAppender.AbstractBuilder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SocketAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("facility")
/*  52 */     private Facility facility = Facility.LOCAL0;
/*     */     
/*     */     @PluginBuilderAttribute("id")
/*     */     private String id;
/*     */     
/*     */     @PluginBuilderAttribute("enterpriseNumber")
/*  58 */     private int enterpriseNumber = 18060;
/*     */     
/*     */     @PluginBuilderAttribute("includeMdc")
/*     */     private boolean includeMdc = true;
/*     */     
/*     */     @PluginBuilderAttribute("mdcId")
/*     */     private String mdcId;
/*     */     
/*     */     @PluginBuilderAttribute("mdcPrefix")
/*     */     private String mdcPrefix;
/*     */     
/*     */     @PluginBuilderAttribute("eventPrefix")
/*     */     private String eventPrefix;
/*     */     
/*     */     @PluginBuilderAttribute("newLine")
/*     */     private boolean newLine;
/*     */     
/*     */     @PluginBuilderAttribute("newLineEscape")
/*     */     private String escapeNL;
/*     */     
/*     */     @PluginBuilderAttribute("appName")
/*     */     private String appName;
/*     */     
/*     */     @PluginBuilderAttribute("messageId")
/*     */     private String msgId;
/*     */     
/*     */     @PluginBuilderAttribute("mdcExcludes")
/*     */     private String excludes;
/*     */     
/*     */     @PluginBuilderAttribute("mdcIncludes")
/*     */     private String includes;
/*     */     
/*     */     @PluginBuilderAttribute("mdcRequired")
/*     */     private String required;
/*     */     
/*     */     @PluginBuilderAttribute("format")
/*     */     private String format;
/*     */     
/*     */     @PluginBuilderAttribute("charset")
/*  97 */     private Charset charsetName = StandardCharsets.UTF_8;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute("exceptionPattern")
/*     */     private String exceptionPattern;
/*     */ 
/*     */     
/*     */     @PluginElement("LoggerFields")
/*     */     private LoggerFields[] loggerFields;
/*     */ 
/*     */     
/*     */     public SyslogAppender build() {
/* 109 */       Protocol protocol = getProtocol();
/* 110 */       SslConfiguration sslConfiguration = getSslConfiguration();
/* 111 */       boolean useTlsMessageFormat = (sslConfiguration != null || protocol == Protocol.SSL);
/* 112 */       Configuration configuration = getConfiguration();
/* 113 */       Layout<? extends Serializable> layout = getLayout();
/* 114 */       if (layout == null) {
/* 115 */         layout = "RFC5424".equalsIgnoreCase(this.format) ? (Layout<? extends Serializable>)Rfc5424Layout.createLayout(this.facility, this.id, this.enterpriseNumber, this.includeMdc, this.mdcId, this.mdcPrefix, this.eventPrefix, this.newLine, this.escapeNL, this.appName, this.msgId, this.excludes, this.includes, this.required, this.exceptionPattern, useTlsMessageFormat, this.loggerFields, configuration) : (Layout<? extends Serializable>)((SyslogLayout.Builder)SyslogLayout.newBuilder().setFacility(this.facility).setIncludeNewLine(this.newLine).setEscapeNL(this.escapeNL).setCharset(this.charsetName)).build();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 129 */       String name = getName();
/* 130 */       if (name == null) {
/* 131 */         SyslogAppender.LOGGER.error("No name provided for SyslogAppender");
/* 132 */         return null;
/*     */       } 
/* 134 */       AbstractSocketManager manager = SocketAppender.createSocketManager(name, protocol, getHost(), getPort(), getConnectTimeoutMillis(), sslConfiguration, getReconnectDelayMillis(), getImmediateFail(), layout, Constants.ENCODER_BYTE_BUFFER_SIZE, (SocketOptions)null);
/*     */ 
/*     */       
/* 137 */       return new SyslogAppender(name, layout, getFilter(), isIgnoreExceptions(), isImmediateFlush(), manager, getAdvertise() ? configuration.getAdvertiser() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Facility getFacility() {
/* 142 */       return this.facility;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 146 */       return this.id;
/*     */     }
/*     */     
/*     */     public int getEnterpriseNumber() {
/* 150 */       return this.enterpriseNumber;
/*     */     }
/*     */     
/*     */     public boolean isIncludeMdc() {
/* 154 */       return this.includeMdc;
/*     */     }
/*     */     
/*     */     public String getMdcId() {
/* 158 */       return this.mdcId;
/*     */     }
/*     */     
/*     */     public String getMdcPrefix() {
/* 162 */       return this.mdcPrefix;
/*     */     }
/*     */     
/*     */     public String getEventPrefix() {
/* 166 */       return this.eventPrefix;
/*     */     }
/*     */     
/*     */     public boolean isNewLine() {
/* 170 */       return this.newLine;
/*     */     }
/*     */     
/*     */     public String getEscapeNL() {
/* 174 */       return this.escapeNL;
/*     */     }
/*     */     
/*     */     public String getAppName() {
/* 178 */       return this.appName;
/*     */     }
/*     */     
/*     */     public String getMsgId() {
/* 182 */       return this.msgId;
/*     */     }
/*     */     
/*     */     public String getExcludes() {
/* 186 */       return this.excludes;
/*     */     }
/*     */     
/*     */     public String getIncludes() {
/* 190 */       return this.includes;
/*     */     }
/*     */     
/*     */     public String getRequired() {
/* 194 */       return this.required;
/*     */     }
/*     */     
/*     */     public String getFormat() {
/* 198 */       return this.format;
/*     */     }
/*     */     
/*     */     public Charset getCharsetName() {
/* 202 */       return this.charsetName;
/*     */     }
/*     */     
/*     */     public String getExceptionPattern() {
/* 206 */       return this.exceptionPattern;
/*     */     }
/*     */     
/*     */     public LoggerFields[] getLoggerFields() {
/* 210 */       return this.loggerFields;
/*     */     }
/*     */     
/*     */     public B setFacility(Facility facility) {
/* 214 */       this.facility = facility;
/* 215 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setId(String id) {
/* 219 */       this.id = id;
/* 220 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setEnterpriseNumber(int enterpriseNumber) {
/* 224 */       this.enterpriseNumber = enterpriseNumber;
/* 225 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setIncludeMdc(boolean includeMdc) {
/* 229 */       this.includeMdc = includeMdc;
/* 230 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMdcId(String mdcId) {
/* 234 */       this.mdcId = mdcId;
/* 235 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMdcPrefix(String mdcPrefix) {
/* 239 */       this.mdcPrefix = mdcPrefix;
/* 240 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setEventPrefix(String eventPrefix) {
/* 244 */       this.eventPrefix = eventPrefix;
/* 245 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setNewLine(boolean newLine) {
/* 249 */       this.newLine = newLine;
/* 250 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setEscapeNL(String escapeNL) {
/* 254 */       this.escapeNL = escapeNL;
/* 255 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAppName(String appName) {
/* 259 */       this.appName = appName;
/* 260 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setMsgId(String msgId) {
/* 264 */       this.msgId = msgId;
/* 265 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setExcludes(String excludes) {
/* 269 */       this.excludes = excludes;
/* 270 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setIncludes(String includes) {
/* 274 */       this.includes = includes;
/* 275 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setRequired(String required) {
/* 279 */       this.required = required;
/* 280 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setFormat(String format) {
/* 284 */       this.format = format;
/* 285 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setCharsetName(Charset charset) {
/* 289 */       this.charsetName = charset;
/* 290 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setExceptionPattern(String exceptionPattern) {
/* 294 */       this.exceptionPattern = exceptionPattern;
/* 295 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setLoggerFields(LoggerFields[] loggerFields) {
/* 299 */       this.loggerFields = loggerFields;
/* 300 */       return (B)asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SyslogAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, AbstractSocketManager manager, Advertiser advertiser) {
/* 309 */     super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <B extends Builder<B>> SyslogAppender createAppender(String host, int port, String protocolStr, SslConfiguration sslConfiguration, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, String name, boolean immediateFlush, boolean ignoreExceptions, Facility facility, String id, int enterpriseNumber, boolean includeMdc, String mdcId, String mdcPrefix, String eventPrefix, boolean newLine, String escapeNL, String appName, String msgId, String excludes, String includes, String required, String format, Filter filter, Configuration configuration, Charset charset, String exceptionPattern, LoggerFields[] loggerFields, boolean advertise) {
/* 389 */     return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder)((Builder<Builder>)((Builder<Builder<Builder>>)((Builder<Builder<Builder<Builder>>>)((Builder<Builder<Builder<Builder<Builder>>>>)((Builder<Builder<Builder<Builder<Builder<Builder>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>>)((Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder<Builder>>>>>>>>>)newSyslogAppenderBuilder().withHost(host)).withPort(port)).withProtocol((Protocol)EnglishEnums.valueOf(Protocol.class, protocolStr))).withSslConfiguration(sslConfiguration)).withConnectTimeoutMillis(connectTimeoutMillis)).withReconnectDelayMillis(reconnectDelayMillis)).withImmediateFail(immediateFail)).withName(appName)).withImmediateFlush(immediateFlush)).withIgnoreExceptions(ignoreExceptions)).withFilter(filter)).setConfiguration(configuration)).withAdvertise(advertise)).setFacility(facility).setId(id).setEnterpriseNumber(enterpriseNumber).setIncludeMdc(includeMdc).setMdcId(mdcId).setMdcPrefix(mdcPrefix).setEventPrefix(eventPrefix).setNewLine(newLine).setAppName(appName).setMsgId(msgId).setExcludes(excludes).setIncludeMdc(includeMdc).setRequired(required).setFormat(format).setCharsetName(charset).setExceptionPattern(exceptionPattern).setLoggerFields(loggerFields).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newSyslogAppenderBuilder() {
/* 427 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\SyslogAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */