/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.script.Bindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
/*     */ import org.apache.logging.log4j.core.script.ScriptManager;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Routing", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RoutingAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   public static final String STATIC_VARIABLES_KEY = "staticVariables";
/*     */   private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
/*     */   private final Routes routes;
/*     */   private Route defaultRoute;
/*     */   private final Configuration configuration;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RoutingAppender>
/*     */   {
/*     */     @PluginElement("Script")
/*     */     private AbstractScript defaultRouteScript;
/*     */     @PluginElement("Routes")
/*     */     private Routes routes;
/*     */     @PluginElement("RewritePolicy")
/*     */     private RewritePolicy rewritePolicy;
/*     */     @PluginElement("PurgePolicy")
/*     */     private PurgePolicy purgePolicy;
/*     */     
/*     */     public RoutingAppender build() {
/*  76 */       String name = getName();
/*  77 */       if (name == null) {
/*  78 */         RoutingAppender.LOGGER.error("No name defined for this RoutingAppender");
/*  79 */         return null;
/*     */       } 
/*  81 */       if (this.routes == null) {
/*  82 */         RoutingAppender.LOGGER.error("No routes defined for RoutingAppender {}", name);
/*  83 */         return null;
/*     */       } 
/*  85 */       return new RoutingAppender(name, getFilter(), isIgnoreExceptions(), this.routes, this.rewritePolicy, getConfiguration(), this.purgePolicy, this.defaultRouteScript);
/*     */     }
/*     */ 
/*     */     
/*     */     public Routes getRoutes() {
/*  90 */       return this.routes;
/*     */     }
/*     */     
/*     */     public AbstractScript getDefaultRouteScript() {
/*  94 */       return this.defaultRouteScript;
/*     */     }
/*     */     
/*     */     public RewritePolicy getRewritePolicy() {
/*  98 */       return this.rewritePolicy;
/*     */     }
/*     */     
/*     */     public PurgePolicy getPurgePolicy() {
/* 102 */       return this.purgePolicy;
/*     */     }
/*     */     
/*     */     public B withRoutes(Routes routes) {
/* 106 */       this.routes = routes;
/* 107 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withDefaultRouteScript(AbstractScript defaultRouteScript) {
/* 111 */       this.defaultRouteScript = defaultRouteScript;
/* 112 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withRewritePolicy(RewritePolicy rewritePolicy) {
/* 116 */       this.rewritePolicy = rewritePolicy;
/* 117 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public void withPurgePolicy(PurgePolicy purgePolicy) {
/* 121 */       this.purgePolicy = purgePolicy;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 128 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   private final ConcurrentMap<String, AppenderControl> appenders = new ConcurrentHashMap<>();
/*     */   private final RewritePolicy rewritePolicy;
/*     */   private final PurgePolicy purgePolicy;
/*     */   private final AbstractScript defaultRouteScript;
/* 140 */   private final ConcurrentMap<Object, Object> scriptStaticVariables = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private RoutingAppender(String name, Filter filter, boolean ignoreExceptions, Routes routes, RewritePolicy rewritePolicy, Configuration configuration, PurgePolicy purgePolicy, AbstractScript defaultRouteScript) {
/* 145 */     super(name, filter, null, ignoreExceptions);
/* 146 */     this.routes = routes;
/* 147 */     this.configuration = configuration;
/* 148 */     this.rewritePolicy = rewritePolicy;
/* 149 */     this.purgePolicy = purgePolicy;
/* 150 */     if (this.purgePolicy != null) {
/* 151 */       this.purgePolicy.initialize(this);
/*     */     }
/* 153 */     this.defaultRouteScript = defaultRouteScript;
/* 154 */     Route defRoute = null;
/* 155 */     for (Route route : routes.getRoutes()) {
/* 156 */       if (route.getKey() == null) {
/* 157 */         if (defRoute == null) {
/* 158 */           defRoute = route;
/*     */         } else {
/* 160 */           error("Multiple default routes. Route " + route.toString() + " will be ignored");
/*     */         } 
/*     */       }
/*     */     } 
/* 164 */     this.defaultRoute = defRoute;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 169 */     if (this.defaultRouteScript != null) {
/* 170 */       if (this.configuration == null) {
/* 171 */         error("No Configuration defined for RoutingAppender; required for Script element.");
/*     */       } else {
/* 173 */         ScriptManager scriptManager = this.configuration.getScriptManager();
/* 174 */         scriptManager.addScript(this.defaultRouteScript);
/* 175 */         Bindings bindings = scriptManager.createBindings(this.defaultRouteScript);
/* 176 */         bindings.put("staticVariables", this.scriptStaticVariables);
/* 177 */         Object object = scriptManager.execute(this.defaultRouteScript.getName(), bindings);
/* 178 */         Route route = this.routes.getRoute(Objects.toString(object, null));
/* 179 */         if (route != null) {
/* 180 */           this.defaultRoute = route;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 185 */     for (Route route : this.routes.getRoutes()) {
/* 186 */       if (route.getAppenderRef() != null) {
/* 187 */         Appender appender = this.configuration.getAppender(route.getAppenderRef());
/* 188 */         if (appender != null) {
/* 189 */           String key = (route == this.defaultRoute) ? "ROUTING_APPENDER_DEFAULT" : route.getKey();
/* 190 */           this.appenders.put(key, new AppenderControl(appender, null, null));
/*     */         } else {
/* 192 */           error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
/*     */         } 
/*     */       } 
/*     */     } 
/* 196 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 201 */     setStopping();
/* 202 */     stop(timeout, timeUnit, false);
/* 203 */     Map<String, Appender> map = this.configuration.getAppenders();
/* 204 */     for (Map.Entry<String, AppenderControl> entry : this.appenders.entrySet()) {
/* 205 */       Appender appender = ((AppenderControl)entry.getValue()).getAppender();
/* 206 */       if (!map.containsKey(appender.getName())) {
/* 207 */         if (appender instanceof LifeCycle2) {
/* 208 */           ((LifeCycle2)appender).stop(timeout, timeUnit); continue;
/*     */         } 
/* 210 */         appender.stop();
/*     */       } 
/*     */     } 
/*     */     
/* 214 */     setStopped();
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 220 */     if (this.rewritePolicy != null) {
/* 221 */       event = this.rewritePolicy.rewrite(event);
/*     */     }
/* 223 */     String pattern = this.routes.getPattern(event, this.scriptStaticVariables);
/* 224 */     String key = (pattern != null) ? this.configuration.getStrSubstitutor().replace(event, pattern) : this.defaultRoute.getKey();
/* 225 */     AppenderControl control = getControl(key, event);
/* 226 */     if (control != null) {
/* 227 */       control.callAppender(event);
/*     */     }
/*     */     
/* 230 */     if (this.purgePolicy != null) {
/* 231 */       this.purgePolicy.update(key, event);
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized AppenderControl getControl(String key, LogEvent event) {
/* 236 */     AppenderControl control = this.appenders.get(key);
/* 237 */     if (control != null) {
/* 238 */       return control;
/*     */     }
/* 240 */     Route route = null;
/* 241 */     for (Route r : this.routes.getRoutes()) {
/* 242 */       if (r.getAppenderRef() == null && key.equals(r.getKey())) {
/* 243 */         route = r;
/*     */         break;
/*     */       } 
/*     */     } 
/* 247 */     if (route == null) {
/* 248 */       route = this.defaultRoute;
/* 249 */       control = this.appenders.get("ROUTING_APPENDER_DEFAULT");
/* 250 */       if (control != null) {
/* 251 */         return control;
/*     */       }
/*     */     } 
/* 254 */     if (route != null) {
/* 255 */       Appender app = createAppender(route, event);
/* 256 */       if (app == null) {
/* 257 */         return null;
/*     */       }
/* 259 */       control = new AppenderControl(app, null, null);
/* 260 */       this.appenders.put(key, control);
/*     */     } 
/*     */     
/* 263 */     return control;
/*     */   }
/*     */   
/*     */   private Appender createAppender(Route route, LogEvent event) {
/* 267 */     Node routeNode = route.getNode();
/* 268 */     for (Node node : routeNode.getChildren()) {
/* 269 */       if (node.getType().getElementName().equals("appender")) {
/* 270 */         Node appNode = new Node(node);
/* 271 */         this.configuration.createConfiguration(appNode, event);
/* 272 */         if (appNode.getObject() instanceof Appender) {
/* 273 */           Appender app = (Appender)appNode.getObject();
/* 274 */           app.start();
/* 275 */           return app;
/*     */         } 
/* 277 */         error("Unable to create Appender of type " + node.getName());
/* 278 */         return null;
/*     */       } 
/*     */     } 
/* 281 */     error("No Appender was configured for route " + route.getKey());
/* 282 */     return null;
/*     */   }
/*     */   
/*     */   public Map<String, AppenderControl> getAppenders() {
/* 286 */     return Collections.unmodifiableMap(this.appenders);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteAppender(String key) {
/* 295 */     LOGGER.debug("Deleting route with " + key + " key ");
/* 296 */     AppenderControl control = this.appenders.remove(key);
/* 297 */     if (null != control) {
/* 298 */       LOGGER.debug("Stopping route with " + key + " key");
/* 299 */       control.getAppender().stop();
/*     */     } else {
/* 301 */       LOGGER.debug("Route with " + key + " key already deleted");
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
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static RoutingAppender createAppender(String name, String ignore, Routes routes, Configuration config, RewritePolicy rewritePolicy, PurgePolicy purgePolicy, Filter filter) {
/* 327 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 328 */     if (name == null) {
/* 329 */       LOGGER.error("No name provided for RoutingAppender");
/* 330 */       return null;
/*     */     } 
/* 332 */     if (routes == null) {
/* 333 */       LOGGER.error("No routes defined for RoutingAppender");
/* 334 */       return null;
/*     */     } 
/* 336 */     return new RoutingAppender(name, filter, ignoreExceptions, routes, rewritePolicy, config, purgePolicy, null);
/*     */   }
/*     */   
/*     */   public Route getDefaultRoute() {
/* 340 */     return this.defaultRoute;
/*     */   }
/*     */   
/*     */   public AbstractScript getDefaultRouteScript() {
/* 344 */     return this.defaultRouteScript;
/*     */   }
/*     */   
/*     */   public PurgePolicy getPurgePolicy() {
/* 348 */     return this.purgePolicy;
/*     */   }
/*     */   
/*     */   public RewritePolicy getRewritePolicy() {
/* 352 */     return this.rewritePolicy;
/*     */   }
/*     */   
/*     */   public Routes getRoutes() {
/* 356 */     return this.routes;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() {
/* 360 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public ConcurrentMap<Object, Object> getScriptStaticVariables() {
/* 364 */     return this.scriptStaticVariables;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\routing\RoutingAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */