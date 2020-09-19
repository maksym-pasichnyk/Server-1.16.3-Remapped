/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidators;
/*     */ import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
/*     */ import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitors;
/*     */ import org.apache.logging.log4j.core.util.Builder;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.core.util.TypeUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginBuilder
/*     */   implements Builder<Object>
/*     */ {
/*  55 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final PluginType<?> pluginType;
/*     */   
/*     */   private final Class<?> clazz;
/*     */   
/*     */   private Configuration configuration;
/*     */   
/*     */   private Node node;
/*     */   
/*     */   private LogEvent event;
/*     */ 
/*     */   
/*     */   public PluginBuilder(PluginType<?> pluginType) {
/*  70 */     this.pluginType = pluginType;
/*  71 */     this.clazz = pluginType.getPluginClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder withConfiguration(Configuration configuration) {
/*  81 */     this.configuration = configuration;
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder withConfigurationNode(Node node) {
/*  92 */     this.node = node;
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginBuilder forLogEvent(LogEvent event) {
/* 103 */     this.event = event;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object build() {
/* 114 */     verify();
/*     */     
/*     */     try {
/* 117 */       LOGGER.debug("Building Plugin[name={}, class={}].", this.pluginType.getElementName(), this.pluginType.getPluginClass().getName());
/*     */       
/* 119 */       Builder<?> builder = createBuilder(this.clazz);
/* 120 */       if (builder != null) {
/* 121 */         injectFields(builder);
/* 122 */         return builder.build();
/*     */       } 
/* 124 */     } catch (Exception e) {
/* 125 */       LOGGER.error("Unable to inject fields into builder class for plugin type {}, element {}.", this.clazz, this.node.getName(), e);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 130 */       Method factory = findFactoryMethod(this.clazz);
/* 131 */       Object[] params = generateParameters(factory);
/* 132 */       return factory.invoke((Object)null, params);
/* 133 */     } catch (Exception e) {
/* 134 */       LOGGER.error("Unable to invoke factory method in class {} for element {}.", this.clazz, this.node.getName(), e);
/*     */       
/* 136 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void verify() {
/* 141 */     Objects.requireNonNull(this.configuration, "No Configuration object was set.");
/* 142 */     Objects.requireNonNull(this.node, "No Node object was set.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Builder<?> createBuilder(Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
/* 147 */     for (Method method : clazz.getDeclaredMethods()) {
/* 148 */       if (method.isAnnotationPresent((Class)PluginBuilderFactory.class) && Modifier.isStatic(method.getModifiers()) && TypeUtil.isAssignable(Builder.class, method.getReturnType())) {
/*     */ 
/*     */         
/* 151 */         ReflectionUtil.makeAccessible(method);
/* 152 */         return (Builder)method.invoke((Object)null, new Object[0]);
/*     */       } 
/*     */     } 
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   private void injectFields(Builder<?> builder) throws IllegalAccessException {
/* 159 */     List<Field> fields = TypeUtil.getAllDeclaredFields(builder.getClass());
/* 160 */     AccessibleObject.setAccessible(fields.<AccessibleObject>toArray((AccessibleObject[])new Field[0]), true);
/* 161 */     StringBuilder log = new StringBuilder();
/* 162 */     boolean invalid = false;
/* 163 */     for (Field field : fields) {
/* 164 */       log.append((log.length() == 0) ? (simpleName(builder) + "(") : ", ");
/* 165 */       Annotation[] annotations = field.getDeclaredAnnotations();
/* 166 */       String[] aliases = extractPluginAliases(annotations);
/* 167 */       for (Annotation a : annotations) {
/* 168 */         if (!(a instanceof PluginAliases)) {
/*     */ 
/*     */           
/* 171 */           PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
/*     */           
/* 173 */           if (visitor != null) {
/* 174 */             Object object = visitor.setAliases(aliases).setAnnotation(a).setConversionType(field.getType()).setStrSubstitutor(this.configuration.getStrSubstitutor()).setMember(field).visit(this.configuration, this.node, this.event, log);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 181 */             if (object != null)
/* 182 */               field.set(builder, object); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 186 */       Collection<ConstraintValidator<?>> validators = ConstraintValidators.findValidators(annotations);
/*     */       
/* 188 */       Object value = field.get(builder);
/* 189 */       for (ConstraintValidator<?> validator : validators) {
/* 190 */         if (!validator.isValid(field.getName(), value)) {
/* 191 */           invalid = true;
/*     */         }
/*     */       } 
/*     */     } 
/* 195 */     log.append((log.length() == 0) ? (builder.getClass().getSimpleName() + "()") : ")");
/* 196 */     LOGGER.debug(log.toString());
/* 197 */     if (invalid) {
/* 198 */       throw new ConfigurationException("Arguments given for element " + this.node.getName() + " are invalid");
/*     */     }
/* 200 */     checkForRemainingAttributes();
/* 201 */     verifyNodeChildrenUsed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String simpleName(Object object) {
/* 208 */     if (object == null) {
/* 209 */       return "null";
/*     */     }
/* 211 */     String cls = object.getClass().getName();
/* 212 */     int index = cls.lastIndexOf('.');
/* 213 */     return (index < 0) ? cls : cls.substring(index + 1);
/*     */   }
/*     */   
/*     */   private static Method findFactoryMethod(Class<?> clazz) {
/* 217 */     for (Method method : clazz.getDeclaredMethods()) {
/* 218 */       if (method.isAnnotationPresent((Class)PluginFactory.class) && Modifier.isStatic(method.getModifiers())) {
/*     */         
/* 220 */         ReflectionUtil.makeAccessible(method);
/* 221 */         return method;
/*     */       } 
/*     */     } 
/* 224 */     throw new IllegalStateException("No factory method found for class " + clazz.getName());
/*     */   }
/*     */   
/*     */   private Object[] generateParameters(Method factory) {
/* 228 */     StringBuilder log = new StringBuilder();
/* 229 */     Class<?>[] types = factory.getParameterTypes();
/* 230 */     Annotation[][] annotations = factory.getParameterAnnotations();
/* 231 */     Object[] args = new Object[annotations.length];
/* 232 */     boolean invalid = false;
/* 233 */     for (int i = 0; i < annotations.length; i++) {
/* 234 */       log.append((log.length() == 0) ? (factory.getName() + "(") : ", ");
/* 235 */       String[] aliases = extractPluginAliases(annotations[i]);
/* 236 */       for (Annotation a : annotations[i]) {
/* 237 */         if (!(a instanceof PluginAliases)) {
/*     */ 
/*     */           
/* 240 */           PluginVisitor<? extends Annotation> visitor = PluginVisitors.findVisitor(a.annotationType());
/*     */           
/* 242 */           if (visitor != null) {
/* 243 */             Object object = visitor.setAliases(aliases).setAnnotation(a).setConversionType(types[i]).setStrSubstitutor(this.configuration.getStrSubstitutor()).setMember(factory).visit(this.configuration, this.node, this.event, log);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 250 */             if (object != null)
/* 251 */               args[i] = object; 
/*     */           } 
/*     */         } 
/*     */       } 
/* 255 */       Collection<ConstraintValidator<?>> validators = ConstraintValidators.findValidators(annotations[i]);
/*     */       
/* 257 */       Object value = args[i];
/* 258 */       String argName = "arg[" + i + "](" + simpleName(value) + ")";
/* 259 */       for (ConstraintValidator<?> validator : validators) {
/* 260 */         if (!validator.isValid(argName, value)) {
/* 261 */           invalid = true;
/*     */         }
/*     */       } 
/*     */     } 
/* 265 */     log.append((log.length() == 0) ? (factory.getName() + "()") : ")");
/* 266 */     checkForRemainingAttributes();
/* 267 */     verifyNodeChildrenUsed();
/* 268 */     LOGGER.debug(log.toString());
/* 269 */     if (invalid) {
/* 270 */       throw new ConfigurationException("Arguments given for element " + this.node.getName() + " are invalid");
/*     */     }
/* 272 */     return args;
/*     */   }
/*     */   
/*     */   private static String[] extractPluginAliases(Annotation... parmTypes) {
/* 276 */     String[] aliases = null;
/* 277 */     for (Annotation a : parmTypes) {
/* 278 */       if (a instanceof PluginAliases) {
/* 279 */         aliases = ((PluginAliases)a).value();
/*     */       }
/*     */     } 
/* 282 */     return aliases;
/*     */   }
/*     */   
/*     */   private void checkForRemainingAttributes() {
/* 286 */     Map<String, String> attrs = this.node.getAttributes();
/* 287 */     if (!attrs.isEmpty()) {
/* 288 */       StringBuilder sb = new StringBuilder();
/* 289 */       for (String key : attrs.keySet()) {
/* 290 */         if (sb.length() == 0) {
/* 291 */           sb.append(this.node.getName());
/* 292 */           sb.append(" contains ");
/* 293 */           if (attrs.size() == 1) {
/* 294 */             sb.append("an invalid element or attribute ");
/*     */           } else {
/* 296 */             sb.append("invalid attributes ");
/*     */           } 
/*     */         } else {
/* 299 */           sb.append(", ");
/*     */         } 
/* 301 */         StringBuilders.appendDqValue(sb, key);
/*     */       } 
/* 303 */       LOGGER.error(sb.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void verifyNodeChildrenUsed() {
/* 308 */     List<Node> children = this.node.getChildren();
/* 309 */     if (!this.pluginType.isDeferChildren() && !children.isEmpty())
/* 310 */       for (Node child : children) {
/* 311 */         String nodeType = this.node.getType().getElementName();
/* 312 */         String start = nodeType.equals(this.node.getName()) ? this.node.getName() : (nodeType + ' ' + this.node.getName());
/* 313 */         LOGGER.error("{} has no parameter that matches element {}", start, child.getName());
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */