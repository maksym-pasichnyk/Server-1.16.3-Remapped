/*     */ package org.apache.logging.log4j.core.config.builder.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.Component;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultConfigurationBuilder<T extends BuiltConfiguration>
/*     */   implements ConfigurationBuilder<T>
/*     */ {
/*     */   private static final String INDENT = "  ";
/*  58 */   private static final String EOL = System.lineSeparator();
/*     */   
/*  60 */   private final Component root = new Component();
/*     */   
/*     */   private Component loggers;
/*     */   private Component appenders;
/*     */   private Component filters;
/*     */   private Component properties;
/*     */   private Component customLevels;
/*     */   private Component scripts;
/*     */   private final Class<T> clazz;
/*     */   private ConfigurationSource source;
/*     */   private int monitorInterval;
/*     */   private Level level;
/*     */   private String verbosity;
/*     */   private String destination;
/*     */   private String packages;
/*     */   private String shutdownFlag;
/*     */   private long shutdownTimeoutMillis;
/*     */   private String advertiser;
/*     */   private LoggerContext loggerContext;
/*     */   private String name;
/*     */   
/*     */   public DefaultConfigurationBuilder() {
/*  82 */     this((Class)BuiltConfiguration.class);
/*  83 */     this.root.addAttribute("name", "Built");
/*     */   }
/*     */   
/*     */   public DefaultConfigurationBuilder(Class<T> clazz) {
/*  87 */     if (clazz == null) {
/*  88 */       throw new IllegalArgumentException("A Configuration class must be provided");
/*     */     }
/*  90 */     this.clazz = clazz;
/*  91 */     List<Component> components = this.root.getComponents();
/*  92 */     this.properties = new Component("Properties");
/*  93 */     components.add(this.properties);
/*  94 */     this.scripts = new Component("Scripts");
/*  95 */     components.add(this.scripts);
/*  96 */     this.customLevels = new Component("CustomLevels");
/*  97 */     components.add(this.customLevels);
/*  98 */     this.filters = new Component("Filters");
/*  99 */     components.add(this.filters);
/* 100 */     this.appenders = new Component("Appenders");
/* 101 */     components.add(this.appenders);
/* 102 */     this.loggers = new Component("Loggers");
/* 103 */     components.add(this.loggers);
/*     */   }
/*     */   
/*     */   protected ConfigurationBuilder<T> add(Component parent, ComponentBuilder<?> builder) {
/* 107 */     parent.getComponents().add(builder.build());
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(AppenderComponentBuilder builder) {
/* 113 */     return add(this.appenders, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(CustomLevelComponentBuilder builder) {
/* 118 */     return add(this.customLevels, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(FilterComponentBuilder builder) {
/* 123 */     return add(this.filters, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(ScriptComponentBuilder builder) {
/* 128 */     return add(this.scripts, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(ScriptFileComponentBuilder builder) {
/* 133 */     return add(this.scripts, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(LoggerComponentBuilder builder) {
/* 138 */     return add(this.loggers, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> add(RootLoggerComponentBuilder builder) {
/* 143 */     for (Component c : this.loggers.getComponents()) {
/* 144 */       if (c.getPluginType().equals("root")) {
/* 145 */         throw new ConfigurationException("Root Logger was previously defined");
/*     */       }
/*     */     } 
/* 148 */     return add(this.loggers, (ComponentBuilder<?>)builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> addProperty(String key, String value) {
/* 153 */     this.properties.addComponent((Component)newComponent(key, "Property", value).build());
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public T build() {
/* 159 */     return build(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public T build(boolean initialize) {
/*     */     BuiltConfiguration builtConfiguration;
/*     */     try {
/* 166 */       if (this.source == null) {
/* 167 */         this.source = ConfigurationSource.NULL_SOURCE;
/*     */       }
/* 169 */       Constructor<T> constructor = this.clazz.getConstructor(new Class[] { LoggerContext.class, ConfigurationSource.class, Component.class });
/* 170 */       builtConfiguration = (BuiltConfiguration)constructor.newInstance(new Object[] { this.loggerContext, this.source, this.root });
/* 171 */       builtConfiguration.setMonitorInterval(this.monitorInterval);
/* 172 */       builtConfiguration.getRootNode().getAttributes().putAll(this.root.getAttributes());
/* 173 */       if (this.name != null) {
/* 174 */         builtConfiguration.setName(this.name);
/*     */       }
/* 176 */       if (this.level != null) {
/* 177 */         builtConfiguration.getStatusConfiguration().withStatus(this.level);
/*     */       }
/* 179 */       if (this.verbosity != null) {
/* 180 */         builtConfiguration.getStatusConfiguration().withVerbosity(this.verbosity);
/*     */       }
/* 182 */       if (this.destination != null) {
/* 183 */         builtConfiguration.getStatusConfiguration().withDestination(this.destination);
/*     */       }
/* 185 */       if (this.packages != null) {
/* 186 */         builtConfiguration.setPluginPackages(this.packages);
/*     */       }
/* 188 */       if (this.shutdownFlag != null) {
/* 189 */         builtConfiguration.setShutdownHook(this.shutdownFlag);
/*     */       }
/* 191 */       if (this.shutdownTimeoutMillis > 0L) {
/* 192 */         builtConfiguration.setShutdownTimeoutMillis(this.shutdownTimeoutMillis);
/*     */       }
/* 194 */       if (this.advertiser != null) {
/* 195 */         builtConfiguration.createAdvertiser(this.advertiser, this.source);
/*     */       }
/* 197 */     } catch (Exception ex) {
/* 198 */       throw new IllegalArgumentException("Invalid Configuration class specified", ex);
/*     */     } 
/* 200 */     builtConfiguration.getStatusConfiguration().initialize();
/* 201 */     if (initialize) {
/* 202 */       builtConfiguration.initialize();
/*     */     }
/* 204 */     return (T)builtConfiguration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeXmlConfiguration(OutputStream output) throws IOException {
/*     */     try {
/* 210 */       XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(output);
/* 211 */       writeXmlConfiguration(xmlWriter);
/* 212 */       xmlWriter.close();
/* 213 */     } catch (XMLStreamException e) {
/* 214 */       if (e.getNestedException() instanceof IOException) {
/* 215 */         throw (IOException)e.getNestedException();
/*     */       }
/* 217 */       Throwables.rethrow(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toXmlConfiguration() {
/* 223 */     StringWriter sw = new StringWriter();
/*     */     try {
/* 225 */       XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
/* 226 */       writeXmlConfiguration(xmlWriter);
/* 227 */       xmlWriter.close();
/* 228 */     } catch (XMLStreamException e) {
/* 229 */       Throwables.rethrow(e);
/*     */     } 
/* 231 */     return sw.toString();
/*     */   }
/*     */   
/*     */   private void writeXmlConfiguration(XMLStreamWriter xmlWriter) throws XMLStreamException {
/* 235 */     xmlWriter.writeStartDocument();
/* 236 */     xmlWriter.writeCharacters(EOL);
/*     */     
/* 238 */     xmlWriter.writeStartElement("Configuration");
/* 239 */     if (this.name != null) {
/* 240 */       xmlWriter.writeAttribute("name", this.name);
/*     */     }
/* 242 */     if (this.level != null) {
/* 243 */       xmlWriter.writeAttribute("status", this.level.name());
/*     */     }
/* 245 */     if (this.verbosity != null) {
/* 246 */       xmlWriter.writeAttribute("verbose", this.verbosity);
/*     */     }
/* 248 */     if (this.destination != null) {
/* 249 */       xmlWriter.writeAttribute("dest", this.destination);
/*     */     }
/* 251 */     if (this.packages != null) {
/* 252 */       xmlWriter.writeAttribute("packages", this.packages);
/*     */     }
/* 254 */     if (this.shutdownFlag != null) {
/* 255 */       xmlWriter.writeAttribute("shutdownHook", this.shutdownFlag);
/*     */     }
/* 257 */     if (this.shutdownTimeoutMillis > 0L) {
/* 258 */       xmlWriter.writeAttribute("shutdownTimeout", String.valueOf(this.shutdownTimeoutMillis));
/*     */     }
/* 260 */     if (this.advertiser != null) {
/* 261 */       xmlWriter.writeAttribute("advertiser", this.advertiser);
/*     */     }
/* 263 */     if (this.monitorInterval > 0) {
/* 264 */       xmlWriter.writeAttribute("monitorInterval", String.valueOf(this.monitorInterval));
/*     */     }
/*     */     
/* 267 */     xmlWriter.writeCharacters(EOL);
/*     */     
/* 269 */     writeXmlSection(xmlWriter, this.properties);
/* 270 */     writeXmlSection(xmlWriter, this.scripts);
/* 271 */     writeXmlSection(xmlWriter, this.customLevels);
/* 272 */     if (this.filters.getComponents().size() == 1) {
/* 273 */       writeXmlComponent(xmlWriter, this.filters.getComponents().get(0), 1);
/* 274 */     } else if (this.filters.getComponents().size() > 1) {
/* 275 */       writeXmlSection(xmlWriter, this.filters);
/*     */     } 
/* 277 */     writeXmlSection(xmlWriter, this.appenders);
/* 278 */     writeXmlSection(xmlWriter, this.loggers);
/*     */     
/* 280 */     xmlWriter.writeEndElement();
/* 281 */     xmlWriter.writeCharacters(EOL);
/*     */     
/* 283 */     xmlWriter.writeEndDocument();
/*     */   }
/*     */   
/*     */   private void writeXmlSection(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
/* 287 */     if (!component.getAttributes().isEmpty() || !component.getComponents().isEmpty() || component.getValue() != null) {
/* 288 */       writeXmlComponent(xmlWriter, component, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeXmlComponent(XMLStreamWriter xmlWriter, Component component, int nesting) throws XMLStreamException {
/* 293 */     if (!component.getComponents().isEmpty() || component.getValue() != null) {
/* 294 */       writeXmlIndent(xmlWriter, nesting);
/* 295 */       xmlWriter.writeStartElement(component.getPluginType());
/* 296 */       writeXmlAttributes(xmlWriter, component);
/* 297 */       if (!component.getComponents().isEmpty()) {
/* 298 */         xmlWriter.writeCharacters(EOL);
/*     */       }
/* 300 */       for (Component subComponent : component.getComponents()) {
/* 301 */         writeXmlComponent(xmlWriter, subComponent, nesting + 1);
/*     */       }
/* 303 */       if (component.getValue() != null) {
/* 304 */         xmlWriter.writeCharacters(component.getValue());
/*     */       }
/* 306 */       if (!component.getComponents().isEmpty()) {
/* 307 */         writeXmlIndent(xmlWriter, nesting);
/*     */       }
/* 309 */       xmlWriter.writeEndElement();
/*     */     } else {
/* 311 */       writeXmlIndent(xmlWriter, nesting);
/* 312 */       xmlWriter.writeEmptyElement(component.getPluginType());
/* 313 */       writeXmlAttributes(xmlWriter, component);
/*     */     } 
/* 315 */     xmlWriter.writeCharacters(EOL);
/*     */   }
/*     */   
/*     */   private void writeXmlIndent(XMLStreamWriter xmlWriter, int nesting) throws XMLStreamException {
/* 319 */     for (int i = 0; i < nesting; i++) {
/* 320 */       xmlWriter.writeCharacters("  ");
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeXmlAttributes(XMLStreamWriter xmlWriter, Component component) throws XMLStreamException {
/* 325 */     for (Map.Entry<String, String> attribute : (Iterable<Map.Entry<String, String>>)component.getAttributes().entrySet()) {
/* 326 */       xmlWriter.writeAttribute(attribute.getKey(), attribute.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ScriptComponentBuilder newScript(String name, String language, String text) {
/* 332 */     return new DefaultScriptComponentBuilder((DefaultConfigurationBuilder)this, name, language, text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptFileComponentBuilder newScriptFile(String path) {
/* 338 */     return new DefaultScriptFileComponentBuilder((DefaultConfigurationBuilder)this, path, path);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScriptFileComponentBuilder newScriptFile(String name, String path) {
/* 343 */     return new DefaultScriptFileComponentBuilder((DefaultConfigurationBuilder)this, name, path);
/*     */   }
/*     */ 
/*     */   
/*     */   public AppenderComponentBuilder newAppender(String name, String type) {
/* 348 */     return new DefaultAppenderComponentBuilder((DefaultConfigurationBuilder)this, name, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public AppenderRefComponentBuilder newAppenderRef(String ref) {
/* 353 */     return new DefaultAppenderRefComponentBuilder((DefaultConfigurationBuilder)this, ref);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, Level level) {
/* 358 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), "AsyncLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, Level level, boolean includeLocation) {
/* 363 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), "AsyncLogger", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, String level) {
/* 368 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, "AsyncLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newAsyncLogger(String name, String level, boolean includeLocation) {
/* 373 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, "AsyncLogger");
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(Level level) {
/* 378 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), "AsyncRoot");
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(Level level, boolean includeLocation) {
/* 383 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), "AsyncRoot", includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(String level) {
/* 388 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, "AsyncRoot");
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newAsyncRootLogger(String level, boolean includeLocation) {
/* 393 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, "AsyncRoot", includeLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String type) {
/* 399 */     return new DefaultComponentBuilder<>(this, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type) {
/* 404 */     return new DefaultComponentBuilder<>(this, name, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(String name, String type, String value) {
/* 410 */     return new DefaultComponentBuilder<>(this, name, type, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomLevelComponentBuilder newCustomLevel(String name, int level) {
/* 416 */     return new DefaultCustomLevelComponentBuilder((DefaultConfigurationBuilder)this, name, level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterComponentBuilder newFilter(String type, Filter.Result onMatch, Filter.Result onMisMatch) {
/* 422 */     return new DefaultFilterComponentBuilder((DefaultConfigurationBuilder)this, type, onMatch.name(), onMisMatch.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public FilterComponentBuilder newFilter(String type, String onMatch, String onMisMatch) {
/* 427 */     return new DefaultFilterComponentBuilder((DefaultConfigurationBuilder)this, type, onMatch, onMisMatch);
/*     */   }
/*     */ 
/*     */   
/*     */   public LayoutComponentBuilder newLayout(String type) {
/* 432 */     return new DefaultLayoutComponentBuilder((DefaultConfigurationBuilder)this, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, Level level) {
/* 438 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, Level level, boolean includeLocation) {
/* 443 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level.toString(), includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, String level) {
/* 448 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerComponentBuilder newLogger(String name, String level, boolean includeLocation) {
/* 453 */     return new DefaultLoggerComponentBuilder((DefaultConfigurationBuilder)this, name, level, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(Level level) {
/* 458 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(Level level, boolean includeLocation) {
/* 463 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level.toString(), includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(String level) {
/* 468 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public RootLoggerComponentBuilder newRootLogger(String level, boolean includeLocation) {
/* 473 */     return new DefaultRootLoggerComponentBuilder((DefaultConfigurationBuilder)this, level, includeLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setAdvertiser(String advertiser) {
/* 478 */     this.advertiser = advertiser;
/* 479 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setConfigurationName(String name) {
/* 490 */     this.name = name;
/* 491 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setConfigurationSource(ConfigurationSource configurationSource) {
/* 502 */     this.source = configurationSource;
/* 503 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setMonitorInterval(String intervalSeconds) {
/* 508 */     this.monitorInterval = Integer.parseInt(intervalSeconds);
/* 509 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setPackages(String packages) {
/* 514 */     this.packages = packages;
/* 515 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setShutdownHook(String flag) {
/* 520 */     this.shutdownFlag = flag;
/* 521 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setShutdownTimeout(long timeout, TimeUnit timeUnit) {
/* 526 */     this.shutdownTimeoutMillis = timeUnit.toMillis(timeout);
/* 527 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setStatusLevel(Level level) {
/* 532 */     this.level = level;
/* 533 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setVerbosity(String verbosity) {
/* 538 */     this.verbosity = verbosity;
/* 539 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> setDestination(String destination) {
/* 544 */     this.destination = destination;
/* 545 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoggerContext(LoggerContext loggerContext) {
/* 550 */     this.loggerContext = loggerContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder<T> addRootProperty(String key, String value) {
/* 555 */     this.root.getAttributes().put(key, value);
/* 556 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultConfigurationBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */