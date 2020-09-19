/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "IfFileName", category = "Core", printObject = true)
/*     */ public final class IfFileName
/*     */   implements PathCondition
/*     */ {
/*  47 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final PathMatcher pathMatcher;
/*     */ 
/*     */   
/*     */   private final String syntaxAndPattern;
/*     */ 
/*     */   
/*     */   private final PathCondition[] nestedConditions;
/*     */ 
/*     */ 
/*     */   
/*     */   private IfFileName(String glob, String regex, PathCondition[] nestedConditions) {
/*  61 */     if (regex == null && glob == null) {
/*  62 */       throw new IllegalArgumentException("Specify either a path glob or a regular expression. Both cannot be null.");
/*     */     }
/*     */     
/*  65 */     this.syntaxAndPattern = createSyntaxAndPatternString(glob, regex);
/*  66 */     this.pathMatcher = FileSystems.getDefault().getPathMatcher(this.syntaxAndPattern);
/*  67 */     this.nestedConditions = (nestedConditions == null) ? new PathCondition[0] : Arrays.<PathCondition>copyOf(nestedConditions, nestedConditions.length);
/*     */   }
/*     */ 
/*     */   
/*     */   static String createSyntaxAndPatternString(String glob, String regex) {
/*  72 */     if (glob != null) {
/*  73 */       return glob.startsWith("glob:") ? glob : ("glob:" + glob);
/*     */     }
/*  75 */     return regex.startsWith("regex:") ? regex : ("regex:" + regex);
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
/*     */   public String getSyntaxAndPattern() {
/*  87 */     return this.syntaxAndPattern;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  91 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
/* 102 */     boolean result = this.pathMatcher.matches(relativePath);
/*     */     
/* 104 */     String match = result ? "matches" : "does not match";
/* 105 */     String accept = result ? "ACCEPTED" : "REJECTED";
/* 106 */     LOGGER.trace("IfFileName {}: '{}' {} relative path '{}'", accept, this.syntaxAndPattern, match, relativePath);
/* 107 */     if (result) {
/* 108 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/* 110 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/* 120 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   @PluginFactory
/*     */   public static IfFileName createNameCondition(@PluginAttribute("glob") String glob, @PluginAttribute("regex") String regex, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 142 */     return new IfFileName(glob, regex, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 148 */     return "IfFileName(" + this.syntaxAndPattern + nested + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfFileName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */