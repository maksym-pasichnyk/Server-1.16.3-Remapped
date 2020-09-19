/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Node
/*     */ {
/*     */   public static final String CATEGORY = "Core";
/*     */   private final Node parent;
/*     */   private final String name;
/*     */   private String value;
/*     */   private final PluginType<?> type;
/*  43 */   private final Map<String, String> attributes = new HashMap<>();
/*  44 */   private final List<Node> children = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object object;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node(Node parent, String name, PluginType<?> type) {
/*  57 */     this.parent = parent;
/*  58 */     this.name = name;
/*  59 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Node() {
/*  63 */     this.parent = null;
/*  64 */     this.name = null;
/*  65 */     this.type = null;
/*     */   }
/*     */   
/*     */   public Node(Node node) {
/*  69 */     this.parent = node.parent;
/*  70 */     this.name = node.name;
/*  71 */     this.type = node.type;
/*  72 */     this.attributes.putAll(node.getAttributes());
/*  73 */     this.value = node.getValue();
/*  74 */     for (Node child : node.getChildren()) {
/*  75 */       this.children.add(new Node(child));
/*     */     }
/*  77 */     this.object = node.object;
/*     */   }
/*     */   
/*     */   public Map<String, String> getAttributes() {
/*  81 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public List<Node> getChildren() {
/*  85 */     return this.children;
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/*  89 */     return !this.children.isEmpty();
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  93 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/*  97 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Node getParent() {
/* 101 */     return this.parent;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 105 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isRoot() {
/* 109 */     return (this.parent == null);
/*     */   }
/*     */   
/*     */   public void setObject(Object obj) {
/* 113 */     this.object = obj;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObject() {
/* 118 */     return (T)this.object;
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
/*     */   public <T> T getObject(Class<T> clazz) {
/* 130 */     return clazz.cast(this.object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInstanceOf(Class<?> clazz) {
/* 141 */     return clazz.isInstance(this.object);
/*     */   }
/*     */   
/*     */   public PluginType<?> getType() {
/* 145 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     if (this.object == null) {
/* 151 */       return "null";
/*     */     }
/* 153 */     return this.type.isObjectPrintable() ? this.object.toString() : (this.type.getPluginClass().getName() + " with name " + this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\Node.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */