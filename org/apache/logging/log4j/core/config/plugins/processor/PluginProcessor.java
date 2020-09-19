/*     */ package org.apache.logging.log4j.core.config.plugins.processor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementVisitor;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleElementVisitor7;
/*     */ import javax.tools.Diagnostic;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.StandardLocation;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SupportedAnnotationTypes({"org.apache.logging.log4j.core.config.plugins.*"})
/*     */ public class PluginProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*     */   public static final String PLUGIN_CACHE_FILE = "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat";
/*  61 */   private final PluginCache pluginCache = new PluginCache();
/*     */ 
/*     */   
/*     */   public SourceVersion getSupportedSourceVersion() {
/*  65 */     return SourceVersion.latest();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/*     */     try {
/*  71 */       Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith((Class)Plugin.class);
/*  72 */       if (elements.isEmpty()) {
/*  73 */         return false;
/*     */       }
/*  75 */       collectPlugins(elements);
/*  76 */       writeCacheFile(elements.<Element>toArray(new Element[elements.size()]));
/*  77 */       return true;
/*  78 */     } catch (IOException e) {
/*  79 */       error(e.getMessage());
/*  80 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void error(CharSequence message) {
/*  85 */     this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
/*     */   }
/*     */   
/*     */   private void collectPlugins(Iterable<? extends Element> elements) {
/*  89 */     Elements elementUtils = this.processingEnv.getElementUtils();
/*  90 */     ElementVisitor<PluginEntry, Plugin> pluginVisitor = new PluginElementVisitor(elementUtils);
/*  91 */     ElementVisitor<Collection<PluginEntry>, Plugin> pluginAliasesVisitor = new PluginAliasesElementVisitor(elementUtils);
/*     */     
/*  93 */     for (Element element : elements) {
/*  94 */       Plugin plugin = element.<Plugin>getAnnotation(Plugin.class);
/*  95 */       if (plugin == null) {
/*     */         continue;
/*     */       }
/*  98 */       PluginEntry entry = element.<PluginEntry, Plugin>accept(pluginVisitor, plugin);
/*  99 */       Map<String, PluginEntry> category = this.pluginCache.getCategory(entry.getCategory());
/* 100 */       category.put(entry.getKey(), entry);
/* 101 */       Collection<PluginEntry> entries = element.<Collection<PluginEntry>, Plugin>accept(pluginAliasesVisitor, plugin);
/* 102 */       for (PluginEntry pluginEntry : entries) {
/* 103 */         category.put(pluginEntry.getKey(), pluginEntry);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeCacheFile(Element... elements) throws IOException {
/* 109 */     FileObject fileObject = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat", elements);
/*     */     
/* 111 */     try (OutputStream out = fileObject.openOutputStream()) {
/* 112 */       this.pluginCache.writeCache(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PluginElementVisitor
/*     */     extends SimpleElementVisitor7<PluginEntry, Plugin>
/*     */   {
/*     */     private final Elements elements;
/*     */ 
/*     */     
/*     */     private PluginElementVisitor(Elements elements) {
/* 124 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public PluginEntry visitType(TypeElement e, Plugin plugin) {
/* 129 */       Objects.requireNonNull(plugin, "Plugin annotation is null.");
/* 130 */       PluginEntry entry = new PluginEntry();
/* 131 */       entry.setKey(plugin.name().toLowerCase(Locale.US));
/* 132 */       entry.setClassName(this.elements.getBinaryName(e).toString());
/* 133 */       entry.setName("".equals(plugin.elementType()) ? plugin.name() : plugin.elementType());
/* 134 */       entry.setPrintable(plugin.printObject());
/* 135 */       entry.setDefer(plugin.deferChildren());
/* 136 */       entry.setCategory(plugin.category());
/* 137 */       return entry;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PluginAliasesElementVisitor
/*     */     extends SimpleElementVisitor7<Collection<PluginEntry>, Plugin>
/*     */   {
/*     */     private final Elements elements;
/*     */ 
/*     */     
/*     */     private PluginAliasesElementVisitor(Elements elements) {
/* 149 */       super(Collections.emptyList());
/* 150 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<PluginEntry> visitType(TypeElement e, Plugin plugin) {
/* 155 */       PluginAliases aliases = e.<PluginAliases>getAnnotation(PluginAliases.class);
/* 156 */       if (aliases == null) {
/* 157 */         return this.DEFAULT_VALUE;
/*     */       }
/* 159 */       Collection<PluginEntry> entries = new ArrayList<>((aliases.value()).length);
/* 160 */       for (String alias : aliases.value()) {
/* 161 */         PluginEntry entry = new PluginEntry();
/* 162 */         entry.setKey(alias.toLowerCase(Locale.US));
/* 163 */         entry.setClassName(this.elements.getBinaryName(e).toString());
/* 164 */         entry.setName("".equals(plugin.elementType()) ? alias : plugin.elementType());
/* 165 */         entry.setPrintable(plugin.printObject());
/* 166 */         entry.setDefer(plugin.deferChildren());
/* 167 */         entry.setCategory(plugin.category());
/* 168 */         entries.add(entry);
/*     */       } 
/* 170 */       return entries;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */