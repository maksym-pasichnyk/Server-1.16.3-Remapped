/*     */ package org.apache.logging.log4j.core.config.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ import org.apache.logging.log4j.core.util.FileWatcher;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*  51 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*  52 */   private final List<Status> status = new ArrayList<>();
/*     */   private JsonNode root;
/*     */   
/*     */   public JsonConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
/*  56 */     super(loggerContext, configSource);
/*  57 */     File configFile = configSource.getFile();
/*     */     try {
/*     */       byte[] buffer;
/*  60 */       try (InputStream configStream = configSource.getInputStream()) {
/*  61 */         buffer = toByteArray(configStream);
/*     */       } 
/*  63 */       InputStream is = new ByteArrayInputStream(buffer);
/*  64 */       this.root = getObjectMapper().readTree(is);
/*  65 */       if (this.root.size() == 1) {
/*  66 */         for (JsonNode node : this.root) {
/*  67 */           this.root = node;
/*     */         }
/*     */       }
/*  70 */       processAttributes(this.rootNode, this.root);
/*  71 */       StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*     */       
/*  73 */       for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
/*  74 */         String key = entry.getKey();
/*  75 */         String value = getStrSubstitutor().replace(entry.getValue());
/*     */         
/*  77 */         if ("status".equalsIgnoreCase(key)) {
/*  78 */           statusConfig.withStatus(value); continue;
/*  79 */         }  if ("dest".equalsIgnoreCase(key)) {
/*  80 */           statusConfig.withDestination(value); continue;
/*  81 */         }  if ("shutdownHook".equalsIgnoreCase(key)) {
/*  82 */           this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/*  83 */         }  if ("shutdownTimeout".equalsIgnoreCase(key)) {
/*  84 */           this.shutdownTimeoutMillis = Long.parseLong(value); continue;
/*  85 */         }  if ("verbose".equalsIgnoreCase(entry.getKey())) {
/*  86 */           statusConfig.withVerbosity(value); continue;
/*  87 */         }  if ("packages".equalsIgnoreCase(key)) {
/*  88 */           this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR))); continue;
/*  89 */         }  if ("name".equalsIgnoreCase(key)) {
/*  90 */           setName(value); continue;
/*  91 */         }  if ("monitorInterval".equalsIgnoreCase(key)) {
/*  92 */           int intervalSeconds = Integer.parseInt(value);
/*  93 */           if (intervalSeconds > 0) {
/*  94 */             getWatchManager().setIntervalSeconds(intervalSeconds);
/*  95 */             if (configFile != null) {
/*  96 */               ConfiguratonFileWatcher configuratonFileWatcher = new ConfiguratonFileWatcher(this, this.listeners);
/*  97 */               getWatchManager().watchFile(configFile, (FileWatcher)configuratonFileWatcher);
/*     */             } 
/*     */           }  continue;
/* 100 */         }  if ("advertiser".equalsIgnoreCase(key)) {
/* 101 */           createAdvertiser(value, configSource, buffer, "application/json");
/*     */         }
/*     */       } 
/* 104 */       statusConfig.initialize();
/* 105 */       if (getName() == null) {
/* 106 */         setName(configSource.getLocation());
/*     */       }
/* 108 */     } catch (Exception ex) {
/* 109 */       LOGGER.error("Error parsing " + configSource.getLocation(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ObjectMapper getObjectMapper() {
/* 114 */     return (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/* 119 */     Iterator<Map.Entry<String, JsonNode>> iter = this.root.fields();
/* 120 */     List<Node> children = this.rootNode.getChildren();
/* 121 */     while (iter.hasNext()) {
/* 122 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 123 */       JsonNode n = entry.getValue();
/* 124 */       if (n.isObject()) {
/* 125 */         LOGGER.debug("Processing node for object {}", entry.getKey());
/* 126 */         children.add(constructNode(entry.getKey(), this.rootNode, n)); continue;
/* 127 */       }  if (n.isArray()) {
/* 128 */         LOGGER.error("Arrays are not supported at the root configuration.");
/*     */       }
/*     */     } 
/* 131 */     LOGGER.debug("Completed parsing configuration");
/* 132 */     if (this.status.size() > 0) {
/* 133 */       for (Status s : this.status) {
/* 134 */         LOGGER.error("Error processing element {}: {}", s.name, s.errorType);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/*     */     try {
/* 142 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 143 */       if (source == null) {
/* 144 */         return null;
/*     */       }
/* 146 */       return (Configuration)new JsonConfiguration(getLoggerContext(), source);
/* 147 */     } catch (IOException ex) {
/* 148 */       LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
/*     */       
/* 150 */       return null;
/*     */     } 
/*     */   } private Node constructNode(String name, Node parent, JsonNode jsonNode) {
/*     */     String t;
/* 154 */     PluginType<?> type = this.pluginManager.getPluginType(name);
/* 155 */     Node node = new Node(parent, name, type);
/* 156 */     processAttributes(node, jsonNode);
/* 157 */     Iterator<Map.Entry<String, JsonNode>> iter = jsonNode.fields();
/* 158 */     List<Node> children = node.getChildren();
/* 159 */     while (iter.hasNext()) {
/* 160 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 161 */       JsonNode n = entry.getValue();
/* 162 */       if (n.isArray() || n.isObject()) {
/* 163 */         if (type == null) {
/* 164 */           this.status.add(new Status(name, n, ErrorType.CLASS_NOT_FOUND));
/*     */         }
/* 166 */         if (n.isArray()) {
/* 167 */           LOGGER.debug("Processing node for array {}", entry.getKey());
/* 168 */           for (int i = 0; i < n.size(); i++) {
/* 169 */             String pluginType = getType(n.get(i), entry.getKey());
/* 170 */             PluginType<?> entryType = this.pluginManager.getPluginType(pluginType);
/* 171 */             Node item = new Node(node, entry.getKey(), entryType);
/* 172 */             processAttributes(item, n.get(i));
/* 173 */             if (pluginType.equals(entry.getKey())) {
/* 174 */               LOGGER.debug("Processing {}[{}]", entry.getKey(), Integer.valueOf(i));
/*     */             } else {
/* 176 */               LOGGER.debug("Processing {} {}[{}]", pluginType, entry.getKey(), Integer.valueOf(i));
/*     */             } 
/* 178 */             Iterator<Map.Entry<String, JsonNode>> itemIter = n.get(i).fields();
/* 179 */             List<Node> itemChildren = item.getChildren();
/* 180 */             while (itemIter.hasNext()) {
/* 181 */               Map.Entry<String, JsonNode> itemEntry = itemIter.next();
/* 182 */               if (((JsonNode)itemEntry.getValue()).isObject()) {
/* 183 */                 LOGGER.debug("Processing node for object {}", itemEntry.getKey());
/* 184 */                 itemChildren.add(constructNode(itemEntry.getKey(), item, itemEntry.getValue())); continue;
/* 185 */               }  if (((JsonNode)itemEntry.getValue()).isArray()) {
/* 186 */                 JsonNode array = itemEntry.getValue();
/* 187 */                 String entryName = itemEntry.getKey();
/* 188 */                 LOGGER.debug("Processing array for object {}", entryName);
/* 189 */                 for (int j = 0; j < array.size(); j++) {
/* 190 */                   itemChildren.add(constructNode(entryName, item, array.get(j)));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 195 */             children.add(item);
/*     */           }  continue;
/*     */         } 
/* 198 */         LOGGER.debug("Processing node for object {}", entry.getKey());
/* 199 */         children.add(constructNode(entry.getKey(), node, n));
/*     */         continue;
/*     */       } 
/* 202 */       LOGGER.debug("Node {} is of type {}", entry.getKey(), n.getNodeType());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 207 */     if (type == null) {
/* 208 */       t = "null";
/*     */     } else {
/* 210 */       t = type.getElementName() + ':' + type.getPluginClass();
/*     */     } 
/*     */     
/* 213 */     String p = (node.getParent() == null) ? "null" : ((node.getParent().getName() == null) ? "root" : node.getParent().getName());
/*     */     
/* 215 */     LOGGER.debug("Returning {} with parent {} of type {}", node.getName(), p, t);
/* 216 */     return node;
/*     */   }
/*     */   
/*     */   private String getType(JsonNode node, String name) {
/* 220 */     Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
/* 221 */     while (iter.hasNext()) {
/* 222 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 223 */       if (((String)entry.getKey()).equalsIgnoreCase("type")) {
/* 224 */         JsonNode n = entry.getValue();
/* 225 */         if (n.isValueNode()) {
/* 226 */           return n.asText();
/*     */         }
/*     */       } 
/*     */     } 
/* 230 */     return name;
/*     */   }
/*     */   
/*     */   private void processAttributes(Node parent, JsonNode node) {
/* 234 */     Map<String, String> attrs = parent.getAttributes();
/* 235 */     Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
/* 236 */     while (iter.hasNext()) {
/* 237 */       Map.Entry<String, JsonNode> entry = iter.next();
/* 238 */       if (!((String)entry.getKey()).equalsIgnoreCase("type")) {
/* 239 */         JsonNode n = entry.getValue();
/* 240 */         if (n.isValueNode()) {
/* 241 */           attrs.put(entry.getKey(), n.asText());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 249 */     return getClass().getSimpleName() + "[location=" + getConfigurationSource() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ErrorType
/*     */   {
/* 256 */     CLASS_NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Status
/*     */   {
/*     */     private final JsonNode node;
/*     */     
/*     */     private final String name;
/*     */     private final JsonConfiguration.ErrorType errorType;
/*     */     
/*     */     public Status(String name, JsonNode node, JsonConfiguration.ErrorType errorType) {
/* 268 */       this.name = name;
/* 269 */       this.node = node;
/* 270 */       this.errorType = errorType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 275 */       return "Status [name=" + this.name + ", errorType=" + this.errorType + ", node=" + this.node + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\json\JsonConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */