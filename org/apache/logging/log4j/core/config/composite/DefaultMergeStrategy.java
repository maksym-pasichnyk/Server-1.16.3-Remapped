/*     */ package org.apache.logging.log4j.core.config.composite;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.filter.CompositeFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultMergeStrategy
/*     */   implements MergeStrategy
/*     */ {
/*     */   private static final String APPENDERS = "appenders";
/*     */   private static final String PROPERTIES = "properties";
/*     */   private static final String LOGGERS = "loggers";
/*     */   private static final String SCRIPTS = "scripts";
/*     */   private static final String FILTERS = "filters";
/*     */   private static final String STATUS = "status";
/*     */   private static final String NAME = "name";
/*     */   private static final String REF = "ref";
/*     */   
/*     */   public void mergeRootProperties(Node rootNode, AbstractConfiguration configuration) {
/*  72 */     for (Map.Entry<String, String> attribute : (Iterable<Map.Entry<String, String>>)configuration.getRootNode().getAttributes().entrySet()) {
/*  73 */       boolean isFound = false;
/*  74 */       for (Map.Entry<String, String> targetAttribute : (Iterable<Map.Entry<String, String>>)rootNode.getAttributes().entrySet()) {
/*  75 */         if (((String)targetAttribute.getKey()).equalsIgnoreCase(attribute.getKey())) {
/*  76 */           if (((String)attribute.getKey()).equalsIgnoreCase("status")) {
/*  77 */             Level targetLevel = Level.getLevel(((String)targetAttribute.getValue()).toUpperCase());
/*  78 */             Level sourceLevel = Level.getLevel(((String)attribute.getValue()).toUpperCase());
/*  79 */             if (targetLevel != null && sourceLevel != null) {
/*  80 */               if (sourceLevel.isLessSpecificThan(targetLevel)) {
/*  81 */                 targetAttribute.setValue(attribute.getValue());
/*     */               }
/*     */             }
/*  84 */             else if (sourceLevel != null) {
/*  85 */               targetAttribute.setValue(attribute.getValue());
/*     */             }
/*     */           
/*  88 */           } else if (((String)attribute.getKey()).equalsIgnoreCase("monitorInterval")) {
/*  89 */             int sourceInterval = Integer.parseInt(attribute.getValue());
/*  90 */             int targetInterval = Integer.parseInt(targetAttribute.getValue());
/*  91 */             if (targetInterval == 0 || sourceInterval < targetInterval) {
/*  92 */               targetAttribute.setValue(attribute.getValue());
/*     */             }
/*     */           } else {
/*  95 */             targetAttribute.setValue(attribute.getValue());
/*     */           } 
/*     */           
/*  98 */           isFound = true;
/*     */         } 
/*     */       } 
/* 101 */       if (!isFound) {
/* 102 */         rootNode.getAttributes().put(attribute.getKey(), attribute.getValue());
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
/*     */   public void mergConfigurations(Node target, Node source, PluginManager pluginManager) {
/* 116 */     for (Node sourceChildNode : source.getChildren()) {
/* 117 */       boolean isFilter = isFilterNode(sourceChildNode);
/* 118 */       boolean isMerged = false;
/* 119 */       for (Node targetChildNode : target.getChildren()) {
/* 120 */         Map<String, Node> targetLoggers; if (isFilter) {
/* 121 */           if (isFilterNode(targetChildNode)) {
/* 122 */             updateFilterNode(target, targetChildNode, sourceChildNode, pluginManager);
/* 123 */             isMerged = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */           continue;
/*     */         } 
/* 129 */         if (!targetChildNode.getName().equalsIgnoreCase(sourceChildNode.getName())) {
/*     */           continue;
/*     */         }
/*     */         
/* 133 */         switch (targetChildNode.getName().toLowerCase()) {
/*     */           case "properties":
/*     */           case "scripts":
/*     */           case "appenders":
/* 137 */             for (Node node : sourceChildNode.getChildren()) {
/* 138 */               for (Node targetNode : targetChildNode.getChildren()) {
/* 139 */                 if (((String)targetNode.getAttributes().get("name")).equals(node.getAttributes().get("name"))) {
/* 140 */                   targetChildNode.getChildren().remove(targetNode);
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 144 */               targetChildNode.getChildren().add(node);
/*     */             } 
/* 146 */             isMerged = true;
/*     */             continue;
/*     */           
/*     */           case "loggers":
/* 150 */             targetLoggers = new HashMap<>();
/* 151 */             for (Node node : targetChildNode.getChildren()) {
/* 152 */               targetLoggers.put(node.getName(), node);
/*     */             }
/* 154 */             for (Node node : sourceChildNode.getChildren()) {
/* 155 */               Node targetNode = getLoggerNode(targetChildNode, (String)node.getAttributes().get("name"));
/* 156 */               Node loggerNode = new Node(targetChildNode, node.getName(), node.getType());
/* 157 */               if (targetNode != null) {
/* 158 */                 targetNode.getAttributes().putAll(node.getAttributes());
/* 159 */                 for (Node sourceLoggerChild : node.getChildren()) {
/* 160 */                   if (isFilterNode(sourceLoggerChild)) {
/* 161 */                     boolean foundFilter = false;
/* 162 */                     for (Node targetChild : targetNode.getChildren()) {
/* 163 */                       if (isFilterNode(targetChild)) {
/* 164 */                         updateFilterNode(loggerNode, targetChild, sourceLoggerChild, pluginManager);
/*     */                         
/* 166 */                         foundFilter = true;
/*     */                         break;
/*     */                       } 
/*     */                     } 
/* 170 */                     if (!foundFilter) {
/* 171 */                       Node node1 = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
/*     */                       
/* 173 */                       targetNode.getChildren().add(node1);
/*     */                     }  continue;
/*     */                   } 
/* 176 */                   Node childNode = new Node(loggerNode, sourceLoggerChild.getName(), sourceLoggerChild.getType());
/*     */                   
/* 178 */                   childNode.getAttributes().putAll(sourceLoggerChild.getAttributes());
/* 179 */                   childNode.getChildren().addAll(sourceLoggerChild.getChildren());
/* 180 */                   if (childNode.getName().equalsIgnoreCase("AppenderRef")) {
/* 181 */                     for (Node targetChild : targetNode.getChildren()) {
/* 182 */                       if (isSameReference(targetChild, childNode)) {
/* 183 */                         targetNode.getChildren().remove(targetChild);
/*     */                         break;
/*     */                       } 
/*     */                     } 
/*     */                   } else {
/* 188 */                     for (Node targetChild : targetNode.getChildren()) {
/* 189 */                       if (isSameName(targetChild, childNode)) {
/* 190 */                         targetNode.getChildren().remove(targetChild);
/*     */                         
/*     */                         break;
/*     */                       } 
/*     */                     } 
/*     */                   } 
/* 196 */                   targetNode.getChildren().add(childNode);
/*     */                 } 
/*     */                 continue;
/*     */               } 
/* 200 */               loggerNode.getAttributes().putAll(node.getAttributes());
/* 201 */               loggerNode.getChildren().addAll(node.getChildren());
/* 202 */               targetChildNode.getChildren().add(loggerNode);
/*     */             } 
/*     */             
/* 205 */             isMerged = true;
/*     */             continue;
/*     */         } 
/*     */         
/* 209 */         targetChildNode.getChildren().addAll(sourceChildNode.getChildren());
/* 210 */         isMerged = true;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       if (!isMerged) {
/* 217 */         if (sourceChildNode.getName().equalsIgnoreCase("Properties")) {
/* 218 */           target.getChildren().add(0, sourceChildNode); continue;
/*     */         } 
/* 220 */         target.getChildren().add(sourceChildNode);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Node getLoggerNode(Node parentNode, String name) {
/* 227 */     for (Node node : parentNode.getChildren()) {
/* 228 */       String nodeName = (String)node.getAttributes().get("name");
/* 229 */       if (name == null && nodeName == null) {
/* 230 */         return node;
/*     */       }
/* 232 */       if (nodeName != null && nodeName.equals(name)) {
/* 233 */         return node;
/*     */       }
/*     */     } 
/* 236 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateFilterNode(Node target, Node targetChildNode, Node sourceChildNode, PluginManager pluginManager) {
/* 241 */     if (CompositeFilter.class.isAssignableFrom(targetChildNode.getType().getPluginClass())) {
/* 242 */       Node node = new Node(targetChildNode, sourceChildNode.getName(), sourceChildNode.getType());
/* 243 */       node.getChildren().addAll(sourceChildNode.getChildren());
/* 244 */       node.getAttributes().putAll(sourceChildNode.getAttributes());
/* 245 */       targetChildNode.getChildren().add(node);
/*     */     } else {
/* 247 */       PluginType pluginType = pluginManager.getPluginType("filters");
/* 248 */       Node filtersNode = new Node(targetChildNode, "filters", pluginType);
/* 249 */       Node node = new Node(filtersNode, sourceChildNode.getName(), sourceChildNode.getType());
/* 250 */       node.getAttributes().putAll(sourceChildNode.getAttributes());
/* 251 */       List<Node> children = filtersNode.getChildren();
/* 252 */       children.add(targetChildNode);
/* 253 */       children.add(node);
/* 254 */       List<Node> nodes = target.getChildren();
/* 255 */       nodes.remove(targetChildNode);
/* 256 */       nodes.add(filtersNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isFilterNode(Node node) {
/* 261 */     return Filter.class.isAssignableFrom(node.getType().getPluginClass());
/*     */   }
/*     */   
/*     */   private boolean isSameName(Node node1, Node node2) {
/* 265 */     String value = (String)node1.getAttributes().get("name");
/* 266 */     return (value != null && value.toLowerCase().equals(((String)node2.getAttributes().get("name")).toLowerCase()));
/*     */   }
/*     */   
/*     */   private boolean isSameReference(Node node1, Node node2) {
/* 270 */     String value = (String)node1.getAttributes().get("ref");
/* 271 */     return (value != null && value.toLowerCase().equals(((String)node2.getAttributes().get("ref")).toLowerCase()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\composite\DefaultMergeStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */