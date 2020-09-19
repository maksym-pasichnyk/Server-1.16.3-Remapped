/*     */ package org.apache.logging.log4j.core.script;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.Path;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.script.Bindings;
/*     */ import javax.script.Compilable;
/*     */ import javax.script.CompiledScript;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineFactory;
/*     */ import javax.script.ScriptEngineManager;
/*     */ import javax.script.ScriptException;
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.FileWatcher;
/*     */ import org.apache.logging.log4j.core.util.WatchManager;
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
/*     */ public class ScriptManager
/*     */   implements FileWatcher, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2534169384971965196L;
/*     */   private static final String KEY_THREADING = "THREADING";
/*     */   
/*     */   private abstract class AbstractScriptRunner
/*     */     implements ScriptRunner
/*     */   {
/*     */     private static final String KEY_STATUS_LOGGER = "statusLogger";
/*     */     private static final String KEY_CONFIGURATION = "configuration";
/*     */     
/*     */     private AbstractScriptRunner() {}
/*     */     
/*     */     public Bindings createBindings() {
/*  55 */       SimpleBindings bindings = new SimpleBindings();
/*  56 */       bindings.put("configuration", ScriptManager.this.configuration);
/*  57 */       bindings.put("statusLogger", ScriptManager.logger);
/*  58 */       return bindings;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final Logger logger = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Configuration configuration;
/*  68 */   private final ScriptEngineManager manager = new ScriptEngineManager();
/*  69 */   private final ConcurrentMap<String, ScriptRunner> scriptRunners = new ConcurrentHashMap<>();
/*     */   private final String languages;
/*     */   private final WatchManager watchManager;
/*     */   
/*     */   public ScriptManager(Configuration configuration, WatchManager watchManager) {
/*  74 */     this.configuration = configuration;
/*  75 */     this.watchManager = watchManager;
/*  76 */     List<ScriptEngineFactory> factories = this.manager.getEngineFactories();
/*  77 */     if (logger.isDebugEnabled()) {
/*  78 */       StringBuilder sb = new StringBuilder();
/*  79 */       logger.debug("Installed script engines");
/*  80 */       for (ScriptEngineFactory factory : factories) {
/*  81 */         String threading = (String)factory.getParameter("THREADING");
/*  82 */         if (threading == null) {
/*  83 */           threading = "Not Thread Safe";
/*     */         }
/*  85 */         StringBuilder names = new StringBuilder();
/*  86 */         for (String name : factory.getNames()) {
/*  87 */           if (names.length() > 0) {
/*  88 */             names.append(", ");
/*     */           }
/*  90 */           names.append(name);
/*     */         } 
/*  92 */         if (sb.length() > 0) {
/*  93 */           sb.append(", ");
/*     */         }
/*  95 */         sb.append(names);
/*  96 */         boolean compiled = factory.getScriptEngine() instanceof Compilable;
/*  97 */         logger.debug(factory.getEngineName() + " Version: " + factory.getEngineVersion() + ", Language: " + factory.getLanguageName() + ", Threading: " + threading + ", Compile: " + compiled + ", Names: {" + names.toString() + "}");
/*     */       } 
/*     */ 
/*     */       
/* 101 */       this.languages = sb.toString();
/*     */     } else {
/* 103 */       StringBuilder names = new StringBuilder();
/* 104 */       for (ScriptEngineFactory factory : factories) {
/* 105 */         for (String name : factory.getNames()) {
/* 106 */           if (names.length() > 0) {
/* 107 */             names.append(", ");
/*     */           }
/* 109 */           names.append(name);
/*     */         } 
/*     */       } 
/* 112 */       this.languages = names.toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addScript(AbstractScript script) {
/* 117 */     ScriptEngine engine = this.manager.getEngineByName(script.getLanguage());
/* 118 */     if (engine == null) {
/* 119 */       logger.error("No ScriptEngine found for language " + script.getLanguage() + ". Available languages are: " + this.languages);
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     if (engine.getFactory().getParameter("THREADING") == null) {
/* 124 */       this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
/*     */     } else {
/* 126 */       this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
/*     */     } 
/*     */     
/* 129 */     if (script instanceof ScriptFile) {
/* 130 */       ScriptFile scriptFile = (ScriptFile)script;
/* 131 */       Path path = scriptFile.getPath();
/* 132 */       if (scriptFile.isWatched() && path != null) {
/* 133 */         this.watchManager.watchFile(path.toFile(), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Bindings createBindings(AbstractScript script) {
/* 139 */     return getScriptRunner(script).createBindings();
/*     */   }
/*     */   
/*     */   public AbstractScript getScript(String name) {
/* 143 */     ScriptRunner runner = this.scriptRunners.get(name);
/* 144 */     return (runner != null) ? runner.getScript() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fileModified(File file) {
/* 149 */     ScriptRunner runner = this.scriptRunners.get(file.toString());
/* 150 */     if (runner == null) {
/* 151 */       logger.info("{} is not a running script");
/*     */       return;
/*     */     } 
/* 154 */     ScriptEngine engine = runner.getScriptEngine();
/* 155 */     AbstractScript script = runner.getScript();
/* 156 */     if (engine.getFactory().getParameter("THREADING") == null) {
/* 157 */       this.scriptRunners.put(script.getName(), new ThreadLocalScriptRunner(script));
/*     */     } else {
/* 159 */       this.scriptRunners.put(script.getName(), new MainScriptRunner(engine, script));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object execute(String name, final Bindings bindings) {
/* 165 */     final ScriptRunner scriptRunner = this.scriptRunners.get(name);
/* 166 */     if (scriptRunner == null) {
/* 167 */       logger.warn("No script named {} could be found");
/* 168 */       return null;
/*     */     } 
/* 170 */     return AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 173 */             return scriptRunner.execute(bindings);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MainScriptRunner
/*     */     extends AbstractScriptRunner
/*     */   {
/*     */     private final AbstractScript script;
/*     */ 
/*     */     
/*     */     private final CompiledScript compiledScript;
/*     */ 
/*     */     
/*     */     private final ScriptEngine scriptEngine;
/*     */ 
/*     */ 
/*     */     
/*     */     public MainScriptRunner(final ScriptEngine scriptEngine, final AbstractScript script) {
/* 195 */       this.script = script;
/* 196 */       this.scriptEngine = scriptEngine;
/* 197 */       CompiledScript compiled = null;
/* 198 */       if (scriptEngine instanceof Compilable) {
/* 199 */         ScriptManager.logger.debug("Script {} is compilable", script.getName());
/* 200 */         compiled = AccessController.<CompiledScript>doPrivileged(new PrivilegedAction<CompiledScript>()
/*     */             {
/*     */               public CompiledScript run() {
/*     */                 try {
/* 204 */                   return ((Compilable)scriptEngine).compile(script.getScriptText());
/* 205 */                 } catch (Throwable ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 210 */                   ScriptManager.logger.warn("Error compiling script", ex);
/* 211 */                   return null;
/*     */                 } 
/*     */               }
/*     */             });
/*     */       } 
/* 216 */       this.compiledScript = compiled;
/*     */     }
/*     */ 
/*     */     
/*     */     public ScriptEngine getScriptEngine() {
/* 221 */       return this.scriptEngine;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object execute(Bindings bindings) {
/* 226 */       if (this.compiledScript != null) {
/*     */         try {
/* 228 */           return this.compiledScript.eval(bindings);
/* 229 */         } catch (ScriptException ex) {
/* 230 */           ScriptManager.logger.error("Error running script " + this.script.getName(), ex);
/* 231 */           return null;
/*     */         } 
/*     */       }
/*     */       try {
/* 235 */         return this.scriptEngine.eval(this.script.getScriptText(), bindings);
/* 236 */       } catch (ScriptException ex) {
/* 237 */         ScriptManager.logger.error("Error running script " + this.script.getName(), ex);
/* 238 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public AbstractScript getScript() {
/* 244 */       return this.script;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ThreadLocalScriptRunner extends AbstractScriptRunner {
/*     */     private final AbstractScript script;
/*     */     
/* 251 */     private final ThreadLocal<ScriptManager.MainScriptRunner> runners = new ThreadLocal<ScriptManager.MainScriptRunner>() {
/*     */         protected ScriptManager.MainScriptRunner initialValue() {
/* 253 */           ScriptEngine engine = ScriptManager.this.manager.getEngineByName(ScriptManager.ThreadLocalScriptRunner.this.script.getLanguage());
/* 254 */           return new ScriptManager.MainScriptRunner(engine, ScriptManager.ThreadLocalScriptRunner.this.script);
/*     */         }
/*     */       };
/*     */     
/*     */     public ThreadLocalScriptRunner(AbstractScript script) {
/* 259 */       this.script = script;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object execute(Bindings bindings) {
/* 264 */       return ((ScriptManager.MainScriptRunner)this.runners.get()).execute(bindings);
/*     */     }
/*     */ 
/*     */     
/*     */     public AbstractScript getScript() {
/* 269 */       return this.script;
/*     */     }
/*     */     
/*     */     public ScriptEngine getScriptEngine() {
/* 273 */       return ((ScriptManager.MainScriptRunner)this.runners.get()).getScriptEngine();
/*     */     }
/*     */   }
/*     */   
/*     */   private ScriptRunner getScriptRunner(AbstractScript script) {
/* 278 */     return this.scriptRunners.get(script.getName());
/*     */   }
/*     */   
/*     */   private static interface ScriptRunner {
/*     */     Bindings createBindings();
/*     */     
/*     */     Object execute(Bindings param1Bindings);
/*     */     
/*     */     AbstractScript getScript();
/*     */     
/*     */     ScriptEngine getScriptEngine();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\script\ScriptManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */