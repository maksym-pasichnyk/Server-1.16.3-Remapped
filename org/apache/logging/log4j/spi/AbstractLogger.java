/*      */ package org.apache.logging.log4j.spi;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.Marker;
/*      */ import org.apache.logging.log4j.MarkerManager;
/*      */ import org.apache.logging.log4j.message.DefaultFlowMessageFactory;
/*      */ import org.apache.logging.log4j.message.EntryMessage;
/*      */ import org.apache.logging.log4j.message.FlowMessageFactory;
/*      */ import org.apache.logging.log4j.message.Message;
/*      */ import org.apache.logging.log4j.message.MessageFactory;
/*      */ import org.apache.logging.log4j.message.MessageFactory2;
/*      */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*      */ import org.apache.logging.log4j.message.ParameterizedMessageFactory;
/*      */ import org.apache.logging.log4j.message.ReusableMessageFactory;
/*      */ import org.apache.logging.log4j.message.SimpleMessage;
/*      */ import org.apache.logging.log4j.message.StringFormattedMessage;
/*      */ import org.apache.logging.log4j.status.StatusLogger;
/*      */ import org.apache.logging.log4j.util.Constants;
/*      */ import org.apache.logging.log4j.util.LambdaUtil;
/*      */ import org.apache.logging.log4j.util.LoaderUtil;
/*      */ import org.apache.logging.log4j.util.MessageSupplier;
/*      */ import org.apache.logging.log4j.util.PropertiesUtil;
/*      */ import org.apache.logging.log4j.util.Strings;
/*      */ import org.apache.logging.log4j.util.Supplier;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractLogger
/*      */   implements ExtendedLogger, Serializable
/*      */ {
/*   52 */   public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   57 */   public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTER").setParents(new Marker[] { FLOW_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   62 */   public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT").setParents(new Marker[] { FLOW_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   67 */   public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   72 */   public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING").setParents(new Marker[] { EXCEPTION_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   77 */   public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING").setParents(new Marker[] { EXCEPTION_MARKER });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   82 */   public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = createClassForProperty("log4j2.messageFactory", ReusableMessageFactory.class, ParameterizedMessageFactory.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   public static final Class<? extends FlowMessageFactory> DEFAULT_FLOW_MESSAGE_FACTORY_CLASS = createFlowClassForProperty("log4j2.flowMessageFactory", DefaultFlowMessageFactory.class);
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = 2L;
/*      */   
/*   94 */   private static final String FQCN = AbstractLogger.class.getName();
/*      */   
/*      */   private static final String THROWING = "Throwing";
/*      */   
/*      */   private static final String CATCHING = "Catching";
/*      */   
/*      */   protected final String name;
/*      */   
/*      */   private final MessageFactory2 messageFactory;
/*      */   private final FlowMessageFactory flowMessageFactory;
/*      */   
/*      */   public AbstractLogger() {
/*  106 */     this.name = getClass().getName();
/*  107 */     this.messageFactory = createDefaultMessageFactory();
/*  108 */     this.flowMessageFactory = createDefaultFlowMessageFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractLogger(String name) {
/*  117 */     this(name, (MessageFactory)createDefaultMessageFactory());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractLogger(String name, MessageFactory messageFactory) {
/*  127 */     this.name = name;
/*  128 */     this.messageFactory = (messageFactory == null) ? createDefaultMessageFactory() : narrow(messageFactory);
/*  129 */     this.flowMessageFactory = createDefaultFlowMessageFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkMessageFactory(ExtendedLogger logger, MessageFactory messageFactory) {
/*  141 */     String name = logger.getName();
/*  142 */     MessageFactory loggerMessageFactory = logger.getMessageFactory();
/*  143 */     if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
/*  144 */       StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", name, loggerMessageFactory, messageFactory);
/*      */ 
/*      */     
/*      */     }
/*  148 */     else if (messageFactory == null && !loggerMessageFactory.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS)) {
/*  149 */       StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", name, loggerMessageFactory, DEFAULT_MESSAGE_FACTORY_CLASS.getName());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void catching(Level level, Throwable t) {
/*  160 */     catching(FQCN, level, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void catching(String fqcn, Level level, Throwable t) {
/*  171 */     if (isEnabled(level, CATCHING_MARKER, (Object)null, (Throwable)null)) {
/*  172 */       logMessageSafely(fqcn, level, CATCHING_MARKER, catchingMsg(t), t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void catching(Throwable t) {
/*  178 */     if (isEnabled(Level.ERROR, CATCHING_MARKER, (Object)null, (Throwable)null)) {
/*  179 */       logMessageSafely(FQCN, Level.ERROR, CATCHING_MARKER, catchingMsg(t), t);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Message catchingMsg(Throwable t) {
/*  184 */     return this.messageFactory.newMessage("Catching");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Class<? extends MessageFactory> createClassForProperty(String property, Class<ReusableMessageFactory> reusableParameterizedMessageFactoryClass, Class<ParameterizedMessageFactory> parameterizedMessageFactoryClass) {
/*      */     try {
/*  191 */       String fallback = Constants.ENABLE_THREADLOCALS ? reusableParameterizedMessageFactoryClass.getName() : parameterizedMessageFactoryClass.getName();
/*      */       
/*  193 */       String clsName = PropertiesUtil.getProperties().getStringProperty(property, fallback);
/*  194 */       return LoaderUtil.loadClass(clsName).asSubclass(MessageFactory.class);
/*  195 */     } catch (Throwable t) {
/*  196 */       return (Class)parameterizedMessageFactoryClass;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Class<? extends FlowMessageFactory> createFlowClassForProperty(String property, Class<DefaultFlowMessageFactory> defaultFlowMessageFactoryClass) {
/*      */     try {
/*  203 */       String clsName = PropertiesUtil.getProperties().getStringProperty(property, defaultFlowMessageFactoryClass.getName());
/*  204 */       return LoaderUtil.loadClass(clsName).asSubclass(FlowMessageFactory.class);
/*  205 */     } catch (Throwable t) {
/*  206 */       return (Class)defaultFlowMessageFactoryClass;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static MessageFactory2 createDefaultMessageFactory() {
/*      */     try {
/*  212 */       MessageFactory result = DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
/*  213 */       return narrow(result);
/*  214 */     } catch (InstantiationException|IllegalAccessException e) {
/*  215 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static MessageFactory2 narrow(MessageFactory result) {
/*  220 */     if (result instanceof MessageFactory2) {
/*  221 */       return (MessageFactory2)result;
/*      */     }
/*  223 */     return new MessageFactory2Adapter(result);
/*      */   }
/*      */   
/*      */   private static FlowMessageFactory createDefaultFlowMessageFactory() {
/*      */     try {
/*  228 */       return DEFAULT_FLOW_MESSAGE_FACTORY_CLASS.newInstance();
/*  229 */     } catch (InstantiationException|IllegalAccessException e) {
/*  230 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, CharSequence message) {
/*  236 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, CharSequence message, Throwable t) {
/*  241 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Message msg) {
/*  246 */     logIfEnabled(FQCN, Level.DEBUG, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Message msg, Throwable t) {
/*  251 */     logIfEnabled(FQCN, Level.DEBUG, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Object message) {
/*  256 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Object message, Throwable t) {
/*  261 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message) {
/*  266 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object... params) {
/*  271 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Throwable t) {
/*  276 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Message msg) {
/*  281 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Message msg, Throwable t) {
/*  286 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(CharSequence message) {
/*  291 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(CharSequence message, Throwable t) {
/*  296 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Object message) {
/*  301 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Object message, Throwable t) {
/*  306 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message) {
/*  311 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object... params) {
/*  316 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Throwable t) {
/*  321 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Supplier<?> msgSupplier) {
/*  326 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Supplier<?> msgSupplier, Throwable t) {
/*  331 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Supplier<?> msgSupplier) {
/*  336 */     logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Supplier<?>... paramSuppliers) {
/*  341 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, Supplier<?> msgSupplier, Throwable t) {
/*  346 */     logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Supplier<?>... paramSuppliers) {
/*  351 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, MessageSupplier msgSupplier) {
/*  356 */     logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, MessageSupplier msgSupplier, Throwable t) {
/*  361 */     logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(MessageSupplier msgSupplier) {
/*  366 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(MessageSupplier msgSupplier, Throwable t) {
/*  371 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0) {
/*  376 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1) {
/*  381 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
/*  386 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/*  392 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  398 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  404 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  411 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  418 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  425 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  432 */     logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0) {
/*  437 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1) {
/*  442 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2) {
/*  447 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
/*  452 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  458 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  464 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  470 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  477 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  484 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  491 */     logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected EntryMessage enter(String fqcn, String format, Supplier<?>... paramSuppliers) {
/*  502 */     EntryMessage entryMsg = null;
/*  503 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  504 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, paramSuppliers)), (Throwable)null);
/*      */     }
/*  506 */     return entryMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected EntryMessage enter(String fqcn, String format, MessageSupplier... paramSuppliers) {
/*  518 */     EntryMessage entryMsg = null;
/*  519 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  520 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, paramSuppliers)), (Throwable)null);
/*      */     }
/*  522 */     return entryMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected EntryMessage enter(String fqcn, String format, Object... params) {
/*  533 */     EntryMessage entryMsg = null;
/*  534 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  535 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, params)), (Throwable)null);
/*      */     }
/*  537 */     return entryMsg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected EntryMessage enter(String fqcn, MessageSupplier msgSupplier) {
/*  548 */     EntryMessage message = null;
/*  549 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  550 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(message = this.flowMessageFactory.newEntryMessage(msgSupplier.get())), (Throwable)null);
/*      */     }
/*      */     
/*  553 */     return message;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected EntryMessage enter(String fqcn, Message message) {
/*  566 */     EntryMessage flowMessage = null;
/*  567 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  568 */       logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(flowMessage = this.flowMessageFactory.newEntryMessage(message)), (Throwable)null);
/*      */     }
/*      */     
/*  571 */     return flowMessage;
/*      */   }
/*      */ 
/*      */   
/*      */   public void entry() {
/*  576 */     entry(FQCN, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void entry(Object... params) {
/*  581 */     entry(FQCN, params);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void entry(String fqcn, Object... params) {
/*  591 */     if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null)) {
/*  592 */       if (params == null) {
/*  593 */         logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)entryMsg((String)null, (Supplier<?>[])null), (Throwable)null);
/*      */       } else {
/*  595 */         logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)entryMsg((String)null, params), (Throwable)null);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected EntryMessage entryMsg(String format, Object... params) {
/*  601 */     int count = (params == null) ? 0 : params.length;
/*  602 */     if (count == 0) {
/*  603 */       if (Strings.isEmpty(format)) {
/*  604 */         return this.flowMessageFactory.newEntryMessage(null);
/*      */       }
/*  606 */       return this.flowMessageFactory.newEntryMessage((Message)new SimpleMessage(format));
/*      */     } 
/*  608 */     if (format != null) {
/*  609 */       return this.flowMessageFactory.newEntryMessage((Message)new ParameterizedMessage(format, params));
/*      */     }
/*  611 */     StringBuilder sb = new StringBuilder();
/*  612 */     sb.append("params(");
/*  613 */     for (int i = 0; i < count; i++) {
/*  614 */       if (i > 0) {
/*  615 */         sb.append(", ");
/*      */       }
/*  617 */       Object parm = params[i];
/*  618 */       sb.append((parm instanceof Message) ? ((Message)parm).getFormattedMessage() : String.valueOf(parm));
/*      */     } 
/*  620 */     sb.append(')');
/*  621 */     return this.flowMessageFactory.newEntryMessage((Message)new SimpleMessage(sb));
/*      */   }
/*      */   
/*      */   protected EntryMessage entryMsg(String format, MessageSupplier... paramSuppliers) {
/*  625 */     int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
/*  626 */     Object[] params = new Object[count];
/*  627 */     for (int i = 0; i < count; i++) {
/*  628 */       params[i] = paramSuppliers[i].get();
/*  629 */       params[i] = (params[i] != null) ? ((Message)params[i]).getFormattedMessage() : null;
/*      */     } 
/*  631 */     return entryMsg(format, params);
/*      */   }
/*      */   
/*      */   protected EntryMessage entryMsg(String format, Supplier<?>... paramSuppliers) {
/*  635 */     int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
/*  636 */     Object[] params = new Object[count];
/*  637 */     for (int i = 0; i < count; i++) {
/*  638 */       params[i] = paramSuppliers[i].get();
/*  639 */       if (params[i] instanceof Message) {
/*  640 */         params[i] = ((Message)params[i]).getFormattedMessage();
/*      */       }
/*      */     } 
/*  643 */     return entryMsg(format, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Message msg) {
/*  648 */     logIfEnabled(FQCN, Level.ERROR, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Message msg, Throwable t) {
/*  653 */     logIfEnabled(FQCN, Level.ERROR, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, CharSequence message) {
/*  658 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, CharSequence message, Throwable t) {
/*  663 */     logIfEnabled(FQCN, Level.ERROR, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Object message) {
/*  668 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Object message, Throwable t) {
/*  673 */     logIfEnabled(FQCN, Level.ERROR, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message) {
/*  678 */     logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object... params) {
/*  683 */     logIfEnabled(FQCN, Level.ERROR, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Throwable t) {
/*  688 */     logIfEnabled(FQCN, Level.ERROR, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Message msg) {
/*  693 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Message msg, Throwable t) {
/*  698 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(CharSequence message) {
/*  703 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(CharSequence message, Throwable t) {
/*  708 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Object message) {
/*  713 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Object message, Throwable t) {
/*  718 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message) {
/*  723 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object... params) {
/*  728 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Throwable t) {
/*  733 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Supplier<?> msgSupplier) {
/*  738 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Supplier<?> msgSupplier, Throwable t) {
/*  743 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Supplier<?> msgSupplier) {
/*  748 */     logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Supplier<?>... paramSuppliers) {
/*  753 */     logIfEnabled(FQCN, Level.ERROR, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, Supplier<?> msgSupplier, Throwable t) {
/*  758 */     logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Supplier<?>... paramSuppliers) {
/*  763 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, MessageSupplier msgSupplier) {
/*  768 */     logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, MessageSupplier msgSupplier, Throwable t) {
/*  773 */     logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(MessageSupplier msgSupplier) {
/*  778 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(MessageSupplier msgSupplier, Throwable t) {
/*  783 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0) {
/*  788 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1) {
/*  793 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
/*  798 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/*  804 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  810 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  816 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  823 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  830 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  837 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  844 */     logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0) {
/*  849 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1) {
/*  854 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2) {
/*  859 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3) {
/*  864 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/*  870 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/*  876 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/*  882 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/*  888 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/*  894 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/*  900 */     logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void exit() {
/*  905 */     exit(FQCN, (Object)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public <R> R exit(R result) {
/*  910 */     return exit(FQCN, result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <R> R exit(String fqcn, R result) {
/*  922 */     logIfEnabled(fqcn, Level.TRACE, EXIT_MARKER, exitMsg((String)null, result), (Throwable)null);
/*  923 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <R> R exit(String fqcn, String format, R result) {
/*  935 */     logIfEnabled(fqcn, Level.TRACE, EXIT_MARKER, exitMsg(format, result), (Throwable)null);
/*  936 */     return result;
/*      */   }
/*      */   
/*      */   protected Message exitMsg(String format, Object result) {
/*  940 */     if (result == null) {
/*  941 */       if (format == null) {
/*  942 */         return this.messageFactory.newMessage("Exit");
/*      */       }
/*  944 */       return this.messageFactory.newMessage("Exit: " + format);
/*      */     } 
/*  946 */     if (format == null) {
/*  947 */       return this.messageFactory.newMessage("Exit with(" + result + ')');
/*      */     }
/*  949 */     return this.messageFactory.newMessage("Exit: " + format, result);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Message msg) {
/*  955 */     logIfEnabled(FQCN, Level.FATAL, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Message msg, Throwable t) {
/*  960 */     logIfEnabled(FQCN, Level.FATAL, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, CharSequence message) {
/*  965 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, CharSequence message, Throwable t) {
/*  970 */     logIfEnabled(FQCN, Level.FATAL, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Object message) {
/*  975 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Object message, Throwable t) {
/*  980 */     logIfEnabled(FQCN, Level.FATAL, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message) {
/*  985 */     logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object... params) {
/*  990 */     logIfEnabled(FQCN, Level.FATAL, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Throwable t) {
/*  995 */     logIfEnabled(FQCN, Level.FATAL, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Message msg) {
/* 1000 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Message msg, Throwable t) {
/* 1005 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(CharSequence message) {
/* 1010 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(CharSequence message, Throwable t) {
/* 1015 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Object message) {
/* 1020 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Object message, Throwable t) {
/* 1025 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message) {
/* 1030 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object... params) {
/* 1035 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Throwable t) {
/* 1040 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Supplier<?> msgSupplier) {
/* 1045 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Supplier<?> msgSupplier, Throwable t) {
/* 1050 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Supplier<?> msgSupplier) {
/* 1055 */     logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1060 */     logIfEnabled(FQCN, Level.FATAL, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 1065 */     logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Supplier<?>... paramSuppliers) {
/* 1070 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, MessageSupplier msgSupplier) {
/* 1075 */     logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 1080 */     logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(MessageSupplier msgSupplier) {
/* 1085 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(MessageSupplier msgSupplier, Throwable t) {
/* 1090 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0) {
/* 1095 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1) {
/* 1100 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1105 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1111 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1117 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1123 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1129 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1135 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1142 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1149 */     logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0) {
/* 1154 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1) {
/* 1159 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2) {
/* 1164 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
/* 1169 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1175 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1181 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1187 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1193 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1199 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1206 */     logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <MF extends MessageFactory> MF getMessageFactory() {
/* 1212 */     return (MF)this.messageFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1217 */     return this.name;
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Message msg) {
/* 1222 */     logIfEnabled(FQCN, Level.INFO, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Message msg, Throwable t) {
/* 1227 */     logIfEnabled(FQCN, Level.INFO, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, CharSequence message) {
/* 1232 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, CharSequence message, Throwable t) {
/* 1237 */     logIfEnabled(FQCN, Level.INFO, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Object message) {
/* 1242 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Object message, Throwable t) {
/* 1247 */     logIfEnabled(FQCN, Level.INFO, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message) {
/* 1252 */     logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object... params) {
/* 1257 */     logIfEnabled(FQCN, Level.INFO, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Throwable t) {
/* 1262 */     logIfEnabled(FQCN, Level.INFO, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Message msg) {
/* 1267 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Message msg, Throwable t) {
/* 1272 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(CharSequence message) {
/* 1277 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(CharSequence message, Throwable t) {
/* 1282 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Object message) {
/* 1287 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Object message, Throwable t) {
/* 1292 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message) {
/* 1297 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object... params) {
/* 1302 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Throwable t) {
/* 1307 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Supplier<?> msgSupplier) {
/* 1312 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Supplier<?> msgSupplier, Throwable t) {
/* 1317 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Supplier<?> msgSupplier) {
/* 1322 */     logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1327 */     logIfEnabled(FQCN, Level.INFO, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 1332 */     logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Supplier<?>... paramSuppliers) {
/* 1337 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, MessageSupplier msgSupplier) {
/* 1342 */     logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 1347 */     logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(MessageSupplier msgSupplier) {
/* 1352 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(MessageSupplier msgSupplier, Throwable t) {
/* 1357 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0) {
/* 1362 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1) {
/* 1367 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1372 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1378 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1384 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1390 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1396 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1402 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1409 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1416 */     logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0) {
/* 1421 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1) {
/* 1426 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2) {
/* 1431 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3) {
/* 1436 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1442 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1448 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1454 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1461 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1468 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1475 */     logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDebugEnabled() {
/* 1480 */     return isEnabled(Level.DEBUG, (Marker)null, (String)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDebugEnabled(Marker marker) {
/* 1485 */     return isEnabled(Level.DEBUG, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnabled(Level level) {
/* 1490 */     return isEnabled(level, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnabled(Level level, Marker marker) {
/* 1495 */     return isEnabled(level, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isErrorEnabled() {
/* 1500 */     return isEnabled(Level.ERROR, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isErrorEnabled(Marker marker) {
/* 1505 */     return isEnabled(Level.ERROR, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFatalEnabled() {
/* 1510 */     return isEnabled(Level.FATAL, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFatalEnabled(Marker marker) {
/* 1515 */     return isEnabled(Level.FATAL, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled() {
/* 1520 */     return isEnabled(Level.INFO, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInfoEnabled(Marker marker) {
/* 1525 */     return isEnabled(Level.INFO, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTraceEnabled() {
/* 1530 */     return isEnabled(Level.TRACE, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTraceEnabled(Marker marker) {
/* 1535 */     return isEnabled(Level.TRACE, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWarnEnabled() {
/* 1540 */     return isEnabled(Level.WARN, (Marker)null, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWarnEnabled(Marker marker) {
/* 1545 */     return isEnabled(Level.WARN, marker, (Object)null, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Message msg) {
/* 1550 */     logIfEnabled(FQCN, level, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Message msg, Throwable t) {
/* 1555 */     logIfEnabled(FQCN, level, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, CharSequence message) {
/* 1560 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, CharSequence message, Throwable t) {
/* 1565 */     if (isEnabled(level, marker, message, t)) {
/* 1566 */       logMessage(FQCN, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Object message) {
/* 1572 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Object message, Throwable t) {
/* 1577 */     if (isEnabled(level, marker, message, t)) {
/* 1578 */       logMessage(FQCN, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message) {
/* 1584 */     logIfEnabled(FQCN, level, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object... params) {
/* 1589 */     logIfEnabled(FQCN, level, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Throwable t) {
/* 1594 */     logIfEnabled(FQCN, level, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Message msg) {
/* 1599 */     logIfEnabled(FQCN, level, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Message msg, Throwable t) {
/* 1604 */     logIfEnabled(FQCN, level, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, CharSequence message) {
/* 1609 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, CharSequence message, Throwable t) {
/* 1614 */     logIfEnabled(FQCN, level, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Object message) {
/* 1619 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Object message, Throwable t) {
/* 1624 */     logIfEnabled(FQCN, level, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message) {
/* 1629 */     logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object... params) {
/* 1634 */     logIfEnabled(FQCN, level, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Throwable t) {
/* 1639 */     logIfEnabled(FQCN, level, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Supplier<?> msgSupplier) {
/* 1644 */     logIfEnabled(FQCN, level, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Supplier<?> msgSupplier, Throwable t) {
/* 1649 */     logIfEnabled(FQCN, level, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Supplier<?> msgSupplier) {
/* 1654 */     logIfEnabled(FQCN, level, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1659 */     logIfEnabled(FQCN, level, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 1664 */     logIfEnabled(FQCN, level, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Supplier<?>... paramSuppliers) {
/* 1669 */     logIfEnabled(FQCN, level, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, MessageSupplier msgSupplier) {
/* 1674 */     logIfEnabled(FQCN, level, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 1679 */     logIfEnabled(FQCN, level, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, MessageSupplier msgSupplier) {
/* 1684 */     logIfEnabled(FQCN, level, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, MessageSupplier msgSupplier, Throwable t) {
/* 1689 */     logIfEnabled(FQCN, level, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0) {
/* 1694 */     logIfEnabled(FQCN, level, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1) {
/* 1699 */     logIfEnabled(FQCN, level, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1705 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1711 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1717 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1723 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1729 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1736 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1743 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1750 */     logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0) {
/* 1755 */     logIfEnabled(FQCN, level, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1) {
/* 1760 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2) {
/* 1765 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1770 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1776 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1782 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1788 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1794 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1800 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1806 */     logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Message msg, Throwable t) {
/* 1812 */     if (isEnabled(level, marker, msg, t)) {
/* 1813 */       logMessageSafely(fqcn, level, marker, msg, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 1820 */     if (isEnabled(level, marker, msgSupplier, t)) {
/* 1821 */       logMessage(fqcn, level, marker, msgSupplier, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable t) {
/* 1828 */     if (isEnabled(level, marker, message, t)) {
/* 1829 */       logMessage(fqcn, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, CharSequence message, Throwable t) {
/* 1836 */     if (isEnabled(level, marker, message, t)) {
/* 1837 */       logMessage(fqcn, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 1844 */     if (isEnabled(level, marker, msgSupplier, t)) {
/* 1845 */       logMessage(fqcn, level, marker, msgSupplier, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
/* 1851 */     if (isEnabled(level, marker, message)) {
/* 1852 */       logMessage(fqcn, level, marker, message);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 1859 */     if (isEnabled(level, marker, message)) {
/* 1860 */       logMessage(fqcn, level, marker, message, paramSuppliers);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
/* 1867 */     if (isEnabled(level, marker, message, params)) {
/* 1868 */       logMessage(fqcn, level, marker, message, params);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) {
/* 1875 */     if (isEnabled(level, marker, message, p0)) {
/* 1876 */       logMessage(fqcn, level, marker, message, p0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
/* 1883 */     if (isEnabled(level, marker, message, p0, p1)) {
/* 1884 */       logMessage(fqcn, level, marker, message, p0, p1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 1891 */     if (isEnabled(level, marker, message, p0, p1, p2)) {
/* 1892 */       logMessage(fqcn, level, marker, message, p0, p1, p2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 1899 */     if (isEnabled(level, marker, message, p0, p1, p2, p3)) {
/* 1900 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 1907 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4)) {
/* 1908 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 1915 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5)) {
/* 1916 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 1924 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6)) {
/* 1925 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 1933 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7)) {
/* 1934 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 1942 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8)) {
/* 1943 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 1951 */     if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)) {
/* 1952 */       logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable t) {
/* 1959 */     if (isEnabled(level, marker, message, t)) {
/* 1960 */       logMessage(fqcn, level, marker, message, t);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, CharSequence message, Throwable t) {
/* 1966 */     logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), t);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, Object message, Throwable t) {
/* 1971 */     logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), t);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 1976 */     Message message = LambdaUtil.get(msgSupplier);
/* 1977 */     logMessageSafely(fqcn, level, marker, message, (t == null && message != null) ? message.getThrowable() : t);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 1982 */     Message message = LambdaUtil.getMessage(msgSupplier, (MessageFactory)this.messageFactory);
/* 1983 */     logMessageSafely(fqcn, level, marker, message, (t == null && message != null) ? message.getThrowable() : t);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Throwable t) {
/* 1988 */     logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), t);
/*      */   }
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message) {
/* 1992 */     Message msg = this.messageFactory.newMessage(message);
/* 1993 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object... params) {
/* 1998 */     Message msg = this.messageFactory.newMessage(message, params);
/* 1999 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0) {
/* 2004 */     Message msg = this.messageFactory.newMessage(message, p0);
/* 2005 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
/* 2010 */     Message msg = this.messageFactory.newMessage(message, p0, p1);
/* 2011 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 2016 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2);
/* 2017 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 2022 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3);
/* 2023 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2028 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4);
/* 2029 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2034 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5);
/* 2035 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2041 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6);
/* 2042 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2048 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
/* 2049 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2055 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/* 2056 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2062 */     Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/* 2063 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void logMessage(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 2068 */     Message msg = this.messageFactory.newMessage(message, LambdaUtil.getAll((Supplier[])paramSuppliers));
/* 2069 */     logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
/*      */   }
/*      */ 
/*      */   
/*      */   public void printf(Level level, Marker marker, String format, Object... params) {
/* 2074 */     if (isEnabled(level, marker, format, params)) {
/* 2075 */       StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
/* 2076 */       logMessageSafely(FQCN, level, marker, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void printf(Level level, String format, Object... params) {
/* 2082 */     if (isEnabled(level, (Marker)null, format, params)) {
/* 2083 */       StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
/* 2084 */       logMessageSafely(FQCN, level, (Marker)null, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void logMessageSafely(String fqcn, Level level, Marker marker, Message msg, Throwable throwable) {
/*      */     try {
/* 2091 */       logMessage(fqcn, level, marker, msg, throwable);
/*      */     } finally {
/*      */       
/* 2094 */       ReusableMessageFactory.release(msg);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Throwable> T throwing(T t) {
/* 2100 */     return throwing(FQCN, Level.ERROR, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Throwable> T throwing(Level level, T t) {
/* 2105 */     return throwing(FQCN, level, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T extends Throwable> T throwing(String fqcn, Level level, T t) {
/* 2118 */     if (isEnabled(level, THROWING_MARKER, (Object)null, (Throwable)null)) {
/* 2119 */       logMessageSafely(fqcn, level, THROWING_MARKER, throwingMsg((Throwable)t), (Throwable)t);
/*      */     }
/* 2121 */     return t;
/*      */   }
/*      */   
/*      */   protected Message throwingMsg(Throwable t) {
/* 2125 */     return this.messageFactory.newMessage("Throwing");
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Message msg) {
/* 2130 */     logIfEnabled(FQCN, Level.TRACE, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Message msg, Throwable t) {
/* 2135 */     logIfEnabled(FQCN, Level.TRACE, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, CharSequence message) {
/* 2140 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, CharSequence message, Throwable t) {
/* 2145 */     logIfEnabled(FQCN, Level.TRACE, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Object message) {
/* 2150 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Object message, Throwable t) {
/* 2155 */     logIfEnabled(FQCN, Level.TRACE, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message) {
/* 2160 */     logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object... params) {
/* 2165 */     logIfEnabled(FQCN, Level.TRACE, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Throwable t) {
/* 2170 */     logIfEnabled(FQCN, Level.TRACE, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Message msg) {
/* 2175 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Message msg, Throwable t) {
/* 2180 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(CharSequence message) {
/* 2185 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(CharSequence message, Throwable t) {
/* 2190 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Object message) {
/* 2195 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Object message, Throwable t) {
/* 2200 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message) {
/* 2205 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object... params) {
/* 2210 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Throwable t) {
/* 2215 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Supplier<?> msgSupplier) {
/* 2220 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Supplier<?> msgSupplier, Throwable t) {
/* 2225 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Supplier<?> msgSupplier) {
/* 2230 */     logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 2235 */     logIfEnabled(FQCN, Level.TRACE, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 2240 */     logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Supplier<?>... paramSuppliers) {
/* 2245 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, MessageSupplier msgSupplier) {
/* 2250 */     logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 2255 */     logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(MessageSupplier msgSupplier) {
/* 2260 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(MessageSupplier msgSupplier, Throwable t) {
/* 2265 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0) {
/* 2270 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1) {
/* 2275 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 2280 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 2286 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2292 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2298 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2304 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2310 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2317 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2324 */     logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0) {
/* 2329 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1) {
/* 2334 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2) {
/* 2339 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
/* 2344 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2350 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2356 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2362 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2368 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2374 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2380 */     logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry() {
/* 2385 */     return enter(FQCN, (String)null, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(String format, Object... params) {
/* 2390 */     return enter(FQCN, format, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(Supplier<?>... paramSuppliers) {
/* 2395 */     return enter(FQCN, (String)null, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(String format, Supplier<?>... paramSuppliers) {
/* 2400 */     return enter(FQCN, format, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntryMessage traceEntry(Message message) {
/* 2405 */     return enter(FQCN, message);
/*      */   }
/*      */ 
/*      */   
/*      */   public void traceExit() {
/* 2410 */     exit(FQCN, (String)null, (Object)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public <R> R traceExit(R result) {
/* 2415 */     return exit(FQCN, (String)null, result);
/*      */   }
/*      */ 
/*      */   
/*      */   public <R> R traceExit(String format, R result) {
/* 2420 */     return exit(FQCN, format, result);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void traceExit(EntryMessage message) {
/* 2426 */     if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, (Message)message, (Throwable)null)) {
/* 2427 */       logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(message), (Throwable)null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <R> R traceExit(EntryMessage message, R result) {
/* 2434 */     if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, (Message)message, (Throwable)null)) {
/* 2435 */       logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(result, message), (Throwable)null);
/*      */     }
/* 2437 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <R> R traceExit(Message message, R result) {
/* 2443 */     if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, message, (Throwable)null)) {
/* 2444 */       logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(result, message), (Throwable)null);
/*      */     }
/* 2446 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Message msg) {
/* 2451 */     logIfEnabled(FQCN, Level.WARN, marker, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Message msg, Throwable t) {
/* 2456 */     logIfEnabled(FQCN, Level.WARN, marker, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, CharSequence message) {
/* 2461 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, CharSequence message, Throwable t) {
/* 2466 */     logIfEnabled(FQCN, Level.WARN, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Object message) {
/* 2471 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Object message, Throwable t) {
/* 2476 */     logIfEnabled(FQCN, Level.WARN, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message) {
/* 2481 */     logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object... params) {
/* 2486 */     logIfEnabled(FQCN, Level.WARN, marker, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Throwable t) {
/* 2491 */     logIfEnabled(FQCN, Level.WARN, marker, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Message msg) {
/* 2496 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msg, (msg != null) ? msg.getThrowable() : null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Message msg, Throwable t) {
/* 2501 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msg, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(CharSequence message) {
/* 2506 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(CharSequence message, Throwable t) {
/* 2511 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Object message) {
/* 2516 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Object message, Throwable t) {
/* 2521 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message) {
/* 2526 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object... params) {
/* 2531 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Throwable t) {
/* 2536 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Supplier<?> msgSupplier) {
/* 2541 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Supplier<?> msgSupplier, Throwable t) {
/* 2546 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Supplier<?> msgSupplier) {
/* 2551 */     logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Supplier<?>... paramSuppliers) {
/* 2556 */     logIfEnabled(FQCN, Level.WARN, marker, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, Supplier<?> msgSupplier, Throwable t) {
/* 2561 */     logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Supplier<?>... paramSuppliers) {
/* 2566 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, paramSuppliers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, MessageSupplier msgSupplier) {
/* 2571 */     logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, MessageSupplier msgSupplier, Throwable t) {
/* 2576 */     logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(MessageSupplier msgSupplier) {
/* 2581 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msgSupplier, (Throwable)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(MessageSupplier msgSupplier, Throwable t) {
/* 2586 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, msgSupplier, t);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0) {
/* 2591 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1) {
/* 2596 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
/* 2601 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 2607 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2613 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2619 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2625 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2631 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2637 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2644 */     logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0) {
/* 2649 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1) {
/* 2654 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2) {
/* 2659 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
/* 2664 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 2670 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 2676 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 2682 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 2688 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 2694 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 2701 */     logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\AbstractLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */