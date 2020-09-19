/*     */ package org.apache.logging.log4j.core.config.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.ConfiguratonFileWatcher;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.FileWatcher;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*     */   private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*     */   private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*  70 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */   
/*     */   private static final String LOG4J_XSD = "Log4j-config.xsd";
/*  73 */   private final List<Status> status = new ArrayList<>();
/*     */   private Element rootElement;
/*     */   private boolean strict;
/*     */   private String schemaResource;
/*     */   
/*     */   public XmlConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
/*  79 */     super(loggerContext, configSource);
/*  80 */     File configFile = configSource.getFile();
/*  81 */     byte[] buffer = null;
/*     */     try {
/*     */       Document document;
/*  84 */       InputStream configStream = configSource.getInputStream();
/*     */       try {
/*  86 */         buffer = toByteArray(configStream);
/*     */       } finally {
/*  88 */         Closer.closeSilently(configStream);
/*     */       } 
/*  90 */       InputSource source = new InputSource(new ByteArrayInputStream(buffer));
/*  91 */       source.setSystemId(configSource.getLocation());
/*  92 */       DocumentBuilder documentBuilder = newDocumentBuilder(true);
/*     */       
/*     */       try {
/*  95 */         document = documentBuilder.parse(source);
/*  96 */       } catch (Exception e) {
/*     */         
/*  98 */         Throwable throwable = Throwables.getRootCause(e);
/*  99 */         if (throwable instanceof UnsupportedOperationException) {
/* 100 */           LOGGER.warn("The DocumentBuilder {} does not support an operation: {}.Trying again without XInclude...", documentBuilder, e);
/*     */ 
/*     */ 
/*     */           
/* 104 */           document = newDocumentBuilder(false).parse(source);
/*     */         } else {
/* 106 */           throw e;
/*     */         } 
/*     */       } 
/* 109 */       this.rootElement = document.getDocumentElement();
/* 110 */       Map<String, String> attrs = processAttributes(this.rootNode, this.rootElement);
/* 111 */       StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*     */       
/* 113 */       for (Map.Entry<String, String> entry : attrs.entrySet()) {
/* 114 */         String key = entry.getKey();
/* 115 */         String value = getStrSubstitutor().replace(entry.getValue());
/* 116 */         if ("status".equalsIgnoreCase(key)) {
/* 117 */           statusConfig.withStatus(value); continue;
/* 118 */         }  if ("dest".equalsIgnoreCase(key)) {
/* 119 */           statusConfig.withDestination(value); continue;
/* 120 */         }  if ("shutdownHook".equalsIgnoreCase(key)) {
/* 121 */           this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/* 122 */         }  if ("shutdownTimeout".equalsIgnoreCase(key)) {
/* 123 */           this.shutdownTimeoutMillis = Long.parseLong(value); continue;
/* 124 */         }  if ("verbose".equalsIgnoreCase(key)) {
/* 125 */           statusConfig.withVerbosity(value); continue;
/* 126 */         }  if ("packages".equalsIgnoreCase(key)) {
/* 127 */           this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR))); continue;
/* 128 */         }  if ("name".equalsIgnoreCase(key)) {
/* 129 */           setName(value); continue;
/* 130 */         }  if ("strict".equalsIgnoreCase(key)) {
/* 131 */           this.strict = Boolean.parseBoolean(value); continue;
/* 132 */         }  if ("schema".equalsIgnoreCase(key)) {
/* 133 */           this.schemaResource = value; continue;
/* 134 */         }  if ("monitorInterval".equalsIgnoreCase(key)) {
/* 135 */           int intervalSeconds = Integer.parseInt(value);
/* 136 */           if (intervalSeconds > 0) {
/* 137 */             getWatchManager().setIntervalSeconds(intervalSeconds);
/* 138 */             if (configFile != null) {
/* 139 */               ConfiguratonFileWatcher configuratonFileWatcher = new ConfiguratonFileWatcher(this, this.listeners);
/* 140 */               getWatchManager().watchFile(configFile, (FileWatcher)configuratonFileWatcher);
/*     */             } 
/*     */           }  continue;
/* 143 */         }  if ("advertiser".equalsIgnoreCase(key)) {
/* 144 */           createAdvertiser(value, configSource, buffer, "text/xml");
/*     */         }
/*     */       } 
/* 147 */       statusConfig.initialize();
/* 148 */     } catch (SAXException|IOException|ParserConfigurationException e) {
/* 149 */       LOGGER.error("Error parsing " + configSource.getLocation(), e);
/*     */     } 
/* 151 */     if (this.strict && this.schemaResource != null && buffer != null) {
/* 152 */       try (InputStream is = Loader.getResourceAsStream(this.schemaResource, XmlConfiguration.class.getClassLoader())) {
/* 153 */         if (is != null) {
/* 154 */           Source src = new StreamSource(is, "Log4j-config.xsd");
/* 155 */           SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 156 */           Schema schema = null;
/*     */           try {
/* 158 */             schema = factory.newSchema(src);
/* 159 */           } catch (SAXException ex) {
/* 160 */             LOGGER.error("Error parsing Log4j schema", ex);
/*     */           } 
/* 162 */           if (schema != null) {
/* 163 */             Validator validator = schema.newValidator();
/*     */             try {
/* 165 */               validator.validate(new StreamSource(new ByteArrayInputStream(buffer)));
/* 166 */             } catch (IOException ioe) {
/* 167 */               LOGGER.error("Error reading configuration for validation", ioe);
/* 168 */             } catch (SAXException ex) {
/* 169 */               LOGGER.error("Error validating configuration", ex);
/*     */             } 
/*     */           } 
/*     */         } 
/* 173 */       } catch (Exception ex) {
/* 174 */         LOGGER.error("Unable to access schema {}", this.schemaResource, ex);
/*     */       } 
/*     */     }
/*     */     
/* 178 */     if (getName() == null) {
/* 179 */       setName(configSource.getLocation());
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
/*     */   static DocumentBuilder newDocumentBuilder(boolean xIncludeAware) throws ParserConfigurationException {
/* 191 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 192 */     factory.setNamespaceAware(true);
/* 193 */     if (xIncludeAware) {
/* 194 */       enableXInclude(factory);
/*     */     }
/* 196 */     return factory.newDocumentBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void enableXInclude(DocumentBuilderFactory factory) {
/*     */     try {
/* 208 */       factory.setXIncludeAware(true);
/* 209 */     } catch (UnsupportedOperationException e) {
/* 210 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support XInclude: {}", factory, e);
/* 211 */     } catch (AbstractMethodError|NoSuchMethodError err) {
/* 212 */       LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support XInclude: {}", factory, err);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 218 */       factory.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
/* 219 */     } catch (ParserConfigurationException e) {
/* 220 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}]: {}", factory, "http://apache.org/xml/features/xinclude/fixup-base-uris", e);
/*     */     }
/* 222 */     catch (AbstractMethodError err) {
/* 223 */       LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support setFeature: {}", factory, err);
/*     */     } 
/*     */     
/*     */     try {
/* 227 */       factory.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
/* 228 */     } catch (ParserConfigurationException e) {
/* 229 */       LOGGER.warn("The DocumentBuilderFactory [{}] does not support the feature [{}]: {}", factory, "http://apache.org/xml/features/xinclude/fixup-language", e);
/*     */     }
/* 231 */     catch (AbstractMethodError err) {
/* 232 */       LOGGER.warn("The DocumentBuilderFactory [{}] is out of date and does not support setFeature: {}", factory, err);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setup() {
/* 239 */     if (this.rootElement == null) {
/* 240 */       LOGGER.error("No logging configuration");
/*     */       return;
/*     */     } 
/* 243 */     constructHierarchy(this.rootNode, this.rootElement);
/* 244 */     if (this.status.size() > 0) {
/* 245 */       for (Status s : this.status) {
/* 246 */         LOGGER.error("Error processing element {} ({}): {}", s.name, s.element, s.errorType);
/*     */       }
/*     */       return;
/*     */     } 
/* 250 */     this.rootElement = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/*     */     try {
/* 256 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 257 */       if (source == null) {
/* 258 */         return null;
/*     */       }
/* 260 */       XmlConfiguration config = new XmlConfiguration(getLoggerContext(), source);
/* 261 */       return (config.rootElement == null) ? null : (Configuration)config;
/* 262 */     } catch (IOException ex) {
/* 263 */       LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
/*     */       
/* 265 */       return null;
/*     */     } 
/*     */   }
/*     */   private void constructHierarchy(Node node, Element element) {
/* 269 */     processAttributes(node, element);
/* 270 */     StringBuilder buffer = new StringBuilder();
/* 271 */     NodeList list = element.getChildNodes();
/* 272 */     List<Node> children = node.getChildren();
/* 273 */     for (int i = 0; i < list.getLength(); i++) {
/* 274 */       Node w3cNode = list.item(i);
/* 275 */       if (w3cNode instanceof Element) {
/* 276 */         Element child = (Element)w3cNode;
/* 277 */         String name = getType(child);
/* 278 */         PluginType<?> type = this.pluginManager.getPluginType(name);
/* 279 */         Node childNode = new Node(node, name, type);
/* 280 */         constructHierarchy(childNode, child);
/* 281 */         if (type == null) {
/* 282 */           String value = childNode.getValue();
/* 283 */           if (!childNode.hasChildren() && value != null) {
/* 284 */             node.getAttributes().put(name, value);
/*     */           } else {
/* 286 */             this.status.add(new Status(name, element, ErrorType.CLASS_NOT_FOUND));
/*     */           } 
/*     */         } else {
/* 289 */           children.add(childNode);
/*     */         } 
/* 291 */       } else if (w3cNode instanceof Text) {
/* 292 */         Text data = (Text)w3cNode;
/* 293 */         buffer.append(data.getData());
/*     */       } 
/*     */     } 
/*     */     
/* 297 */     String text = buffer.toString().trim();
/* 298 */     if (text.length() > 0 || (!node.hasChildren() && !node.isRoot())) {
/* 299 */       node.setValue(text);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getType(Element element) {
/* 304 */     if (this.strict) {
/* 305 */       NamedNodeMap attrs = element.getAttributes();
/* 306 */       for (int i = 0; i < attrs.getLength(); i++) {
/* 307 */         Node w3cNode = attrs.item(i);
/* 308 */         if (w3cNode instanceof Attr) {
/* 309 */           Attr attr = (Attr)w3cNode;
/* 310 */           if (attr.getName().equalsIgnoreCase("type")) {
/* 311 */             String type = attr.getValue();
/* 312 */             attrs.removeNamedItem(attr.getName());
/* 313 */             return type;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 318 */     return element.getTagName();
/*     */   }
/*     */   
/*     */   private Map<String, String> processAttributes(Node node, Element element) {
/* 322 */     NamedNodeMap attrs = element.getAttributes();
/* 323 */     Map<String, String> attributes = node.getAttributes();
/*     */     
/* 325 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 326 */       Node w3cNode = attrs.item(i);
/* 327 */       if (w3cNode instanceof Attr) {
/* 328 */         Attr attr = (Attr)w3cNode;
/* 329 */         if (!attr.getName().equals("xml:base"))
/*     */         {
/*     */           
/* 332 */           attributes.put(attr.getName(), attr.getValue()); } 
/*     */       } 
/*     */     } 
/* 335 */     return attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 340 */     return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ErrorType
/*     */   {
/* 347 */     CLASS_NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Status
/*     */   {
/*     */     private final Element element;
/*     */     
/*     */     private final String name;
/*     */     private final XmlConfiguration.ErrorType errorType;
/*     */     
/*     */     public Status(String name, Element element, XmlConfiguration.ErrorType errorType) {
/* 359 */       this.name = name;
/* 360 */       this.element = element;
/* 361 */       this.errorType = errorType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 366 */       return "Status [name=" + this.name + ", element=" + this.element + ", errorType=" + this.errorType + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\xml\XmlConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */