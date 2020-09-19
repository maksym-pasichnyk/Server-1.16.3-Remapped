/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ @Plugin(name = "Console", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class ConsoleAppender
/*     */   extends AbstractOutputStreamAppender<OutputStreamManager>
/*     */ {
/*     */   public static final String PLUGIN_NAME = "Console";
/*     */   private static final String JANSI_CLASS = "org.fusesource.jansi.WindowsAnsiOutputStream";
/*  57 */   private static ConsoleManagerFactory factory = new ConsoleManagerFactory();
/*  58 */   private static final Target DEFAULT_TARGET = Target.SYSTEM_OUT;
/*  59 */   private static final AtomicInteger COUNT = new AtomicInteger();
/*     */ 
/*     */   
/*     */   private final Target target;
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Target
/*     */   {
/*  68 */     SYSTEM_OUT
/*     */     {
/*     */       public Charset getDefaultCharset() {
/*  71 */         return getCharset("sun.stdout.encoding");
/*     */       }
/*     */     },
/*     */     
/*  75 */     SYSTEM_ERR
/*     */     {
/*     */       public Charset getDefaultCharset() {
/*  78 */         return getCharset("sun.stderr.encoding");
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     protected Charset getCharset(String property) {
/*  85 */       return (new PropertiesUtil(PropertiesUtil.getSystemProperties())).getCharsetProperty(property);
/*     */     }
/*     */     
/*     */     public abstract Charset getDefaultCharset();
/*     */   }
/*     */   
/*     */   private ConsoleAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions, Target target) {
/*  92 */     super(name, layout, filter, ignoreExceptions, true, manager);
/*  93 */     this.target = target;
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
/*     */   @Deprecated
/*     */   public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, Filter filter, String targetStr, String name, String follow, String ignore) {
/*     */     PatternLayout patternLayout;
/* 116 */     if (name == null) {
/* 117 */       LOGGER.error("No name provided for ConsoleAppender");
/* 118 */       return null;
/*     */     } 
/* 120 */     if (layout == null) {
/* 121 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 123 */     boolean isFollow = Boolean.parseBoolean(follow);
/* 124 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 125 */     Target target = (targetStr == null) ? DEFAULT_TARGET : Target.valueOf(targetStr);
/* 126 */     return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, isFollow, false, (Layout<? extends Serializable>)patternLayout), ignoreExceptions, target);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static ConsoleAppender createAppender(Layout<? extends Serializable> layout, Filter filter, Target target, String name, boolean follow, boolean direct, boolean ignoreExceptions) {
/*     */     PatternLayout patternLayout;
/* 155 */     if (name == null) {
/* 156 */       LOGGER.error("No name provided for ConsoleAppender");
/* 157 */       return null;
/*     */     } 
/* 159 */     if (layout == null) {
/* 160 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 162 */     target = (target == null) ? Target.SYSTEM_OUT : target;
/* 163 */     if (follow && direct) {
/* 164 */       LOGGER.error("Cannot use both follow and direct on ConsoleAppender");
/* 165 */       return null;
/*     */     } 
/* 167 */     return new ConsoleAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, follow, direct, (Layout<? extends Serializable>)patternLayout), ignoreExceptions, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConsoleAppender createDefaultAppenderForLayout(Layout<? extends Serializable> layout) {
/* 172 */     return new ConsoleAppender("DefaultConsole-" + COUNT.incrementAndGet(), layout, null, getDefaultManager(DEFAULT_TARGET, false, false, layout), true, DEFAULT_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 178 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<ConsoleAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required
/* 188 */     private ConsoleAppender.Target target = ConsoleAppender.DEFAULT_TARGET;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean follow;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean direct;
/*     */ 
/*     */     
/*     */     public B setTarget(ConsoleAppender.Target aTarget) {
/* 199 */       this.target = aTarget;
/* 200 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setFollow(boolean shouldFollow) {
/* 204 */       this.follow = shouldFollow;
/* 205 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setDirect(boolean shouldDirect) {
/* 209 */       this.direct = shouldDirect;
/* 210 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */     
/*     */     public ConsoleAppender build() {
/* 215 */       if (this.follow && this.direct) {
/* 216 */         throw new IllegalArgumentException("Cannot use both follow and direct on ConsoleAppender '" + getName() + "'");
/*     */       }
/* 218 */       Layout<? extends Serializable> layout = getOrCreateLayout(this.target.getDefaultCharset());
/* 219 */       return new ConsoleAppender(getName(), layout, getFilter(), ConsoleAppender.getManager(this.target, this.follow, this.direct, layout), isIgnoreExceptions(), this.target);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static OutputStreamManager getDefaultManager(Target target, boolean follow, boolean direct, Layout<? extends Serializable> layout) {
/* 226 */     OutputStream os = getOutputStream(follow, direct, target);
/*     */ 
/*     */     
/* 229 */     String managerName = target.name() + '.' + follow + '.' + direct + "-" + COUNT.get();
/* 230 */     return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
/*     */   }
/*     */ 
/*     */   
/*     */   private static OutputStreamManager getManager(Target target, boolean follow, boolean direct, Layout<? extends Serializable> layout) {
/* 235 */     OutputStream os = getOutputStream(follow, direct, target);
/* 236 */     String managerName = target.name() + '.' + follow + '.' + direct;
/* 237 */     return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), factory);
/*     */   }
/*     */   private static OutputStream getOutputStream(boolean follow, boolean direct, Target target) {
/*     */     CloseShieldOutputStream closeShieldOutputStream;
/* 241 */     String enc = Charset.defaultCharset().name();
/*     */ 
/*     */     
/*     */     try {
/* 245 */       OutputStream outputStream = (target == Target.SYSTEM_OUT) ? (direct ? new FileOutputStream(FileDescriptor.out) : (follow ? new PrintStream(new SystemOutStream(), true, enc) : System.out)) : (direct ? new FileOutputStream(FileDescriptor.err) : (follow ? new PrintStream(new SystemErrStream(), true, enc) : System.err));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 251 */       closeShieldOutputStream = new CloseShieldOutputStream(outputStream);
/* 252 */     } catch (UnsupportedEncodingException ex) {
/* 253 */       throw new IllegalStateException("Unsupported default encoding " + enc, ex);
/*     */     } 
/* 255 */     PropertiesUtil propsUtil = PropertiesUtil.getProperties();
/* 256 */     if (!propsUtil.isOsWindows() || propsUtil.getBooleanProperty("log4j.skipJansi") || direct) {
/* 257 */       return (OutputStream)closeShieldOutputStream;
/*     */     }
/*     */     
/*     */     try {
/* 261 */       Class<?> clazz = LoaderUtil.loadClass("org.fusesource.jansi.WindowsAnsiOutputStream");
/* 262 */       Constructor<?> constructor = clazz.getConstructor(new Class[] { OutputStream.class });
/* 263 */       return (OutputStream)new CloseShieldOutputStream((OutputStream)constructor.newInstance(new Object[] { closeShieldOutputStream }));
/* 264 */     } catch (ClassNotFoundException cnfe) {
/* 265 */       LOGGER.debug("Jansi is not installed, cannot find {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
/* 266 */     } catch (NoSuchMethodException nsme) {
/* 267 */       LOGGER.warn("{} is missing the proper constructor", "org.fusesource.jansi.WindowsAnsiOutputStream");
/* 268 */     } catch (Exception ex) {
/* 269 */       LOGGER.warn("Unable to instantiate {}", "org.fusesource.jansi.WindowsAnsiOutputStream");
/*     */     } 
/* 271 */     return (OutputStream)closeShieldOutputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemErrStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 288 */       System.err.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 293 */       System.err.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 298 */       System.err.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/* 303 */       System.err.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemOutStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 321 */       System.out.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 326 */       System.out.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 331 */       System.out.write(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 336 */       System.out.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final OutputStream os;
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
/* 356 */       this.os = os;
/* 357 */       this.name = type;
/* 358 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConsoleManagerFactory
/*     */     implements ManagerFactory<OutputStreamManager, FactoryData>
/*     */   {
/*     */     private ConsoleManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStreamManager createManager(String name, ConsoleAppender.FactoryData data) {
/* 376 */       return new OutputStreamManager(data.os, data.name, data.layout, true);
/*     */     }
/*     */   }
/*     */   
/*     */   public Target getTarget() {
/* 381 */     return this.target;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\ConsoleAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */