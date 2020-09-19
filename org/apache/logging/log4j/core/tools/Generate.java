/*      */ package org.apache.logging.log4j.core.tools;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
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
/*      */ public final class Generate
/*      */ {
/*      */   static final String PACKAGE_DECLARATION = "package %s;%n%n";
/*      */   static final String FQCN_FIELD = "    private static final String FQCN = %s.class.getName();%n";
/*      */   static final String LEVEL_FIELD = "    private static final Level %s = Level.forName(\"%s\", %d);%n";
/*      */   static final String FACTORY_METHODS = "%n    /**%n     * Returns a custom Logger with the name of the calling class.%n     * %n     * @return The custom Logger for the calling class.%n     */%n    public static CLASSNAME create() {%n        final Logger wrapped = LogManager.getLogger();%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified name of the Class as%n     * the Logger name.%n     * %n     * @param loggerName The Class whose name should be used as the Logger name.%n     *            If null it will default to the calling class.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Class<?> loggerName) {%n        final Logger wrapped = LogManager.getLogger(loggerName);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified name of the Class as%n     * the Logger name.%n     * %n     * @param loggerName The Class whose name should be used as the Logger name.%n     *            If null it will default to the calling class.%n     * @param messageFactory The message factory is used only when creating a%n     *            logger, subsequent use does not change the logger but will log%n     *            a warning if mismatched.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Class<?> loggerName, final MessageFactory messageFactory) {%n        final Logger wrapped = LogManager.getLogger(loggerName, messageFactory);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified class name of the value%n     * as the Logger name.%n     * %n     * @param value The value whose class name should be used as the Logger%n     *            name. If null the name of the calling class will be used as%n     *            the logger name.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Object value) {%n        final Logger wrapped = LogManager.getLogger(value);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified class name of the value%n     * as the Logger name.%n     * %n     * @param value The value whose class name should be used as the Logger%n     *            name. If null the name of the calling class will be used as%n     *            the logger name.%n     * @param messageFactory The message factory is used only when creating a%n     *            logger, subsequent use does not change the logger but will log%n     *            a warning if mismatched.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Object value, final MessageFactory messageFactory) {%n        final Logger wrapped = LogManager.getLogger(value, messageFactory);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger with the specified name.%n     * %n     * @param name The logger name. If null the name of the calling class will%n     *            be used.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final String name) {%n        final Logger wrapped = LogManager.getLogger(name);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger with the specified name.%n     * %n     * @param name The logger name. If null the name of the calling class will%n     *            be used.%n     * @param messageFactory The message factory is used only when creating a%n     *            logger, subsequent use does not change the logger but will log%n     *            a warning if mismatched.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final String name, final MessageFactory messageFactory) {%n        final Logger wrapped = LogManager.getLogger(name, messageFactory);%n        return new CLASSNAME(wrapped);%n    }%n";
/*      */   static final String METHODS = "%n    /**%n     * Logs a message with the specific Marker at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param msg the message string to be logged%n     */%n    public void methodName(final Marker marker, final Message msg) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msg, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with the specific Marker at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param msg the message string to be logged%n     * @param t A Throwable or null.%n     */%n    public void methodName(final Marker marker, final Message msg, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msg, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message object to log.%n     */%n    public void methodName(final Marker marker, final Object message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message CharSequence with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message CharSequence to log.%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final CharSequence message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final Marker marker, final Object message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, t);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the CharSequence to log.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final CharSequence message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message object to log.%n     */%n    public void methodName(final Marker marker, final String message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param params parameters to the message.%n     * @see #getMessageFactory()%n     */%n    public void methodName(final Marker marker, final String message, final Object... params) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, params);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @param p9 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8, final Object p9) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final Marker marker, final String message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, t);%n    }%n%n    /**%n     * Logs the specified Message at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param msg the message string to be logged%n     */%n    public void methodName(final Message msg) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msg, (Throwable) null);%n    }%n%n    /**%n     * Logs the specified Message at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param msg the message string to be logged%n     * @param t A Throwable or null.%n     */%n    public void methodName(final Message msg, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msg, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message object to log.%n     */%n    public void methodName(final Object message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final Object message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, t);%n    }%n%n    /**%n     * Logs a message CharSequence with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message CharSequence to log.%n     * @since Log4j-2.6%n     */%n    public void methodName(final CharSequence message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a CharSequence at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param message the CharSequence to log.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.6%n     */%n    public void methodName(final CharSequence message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message object to log.%n     */%n    public void methodName(final String message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param params parameters to the message.%n     * @see #getMessageFactory()%n     */%n    public void methodName(final String message, final Object... params) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, params);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6, p7);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @param p9 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8, final Object p9) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final String message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, t);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the {@code CUSTOM_LEVEL}level.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Supplier<?> msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) including the stack trace of the {@link Throwable} <code>t</code> passed as parameter.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Supplier<?> msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, t);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level with the specified Marker.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final Supplier<?> msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with parameters which are only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param paramSuppliers An array of functions, which when called, produce the desired log message parameters.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, paramSuppliers);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) with the specified Marker and including the stack trace of the {@link Throwable}%n     * <code>t</code> passed as parameter.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @param t A Throwable or null.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, t);%n    }%n%n    /**%n     * Logs a message with parameters which are only to be constructed if the logging level is%n     * the {@code CUSTOM_LEVEL} level.%n     *%n     * @param message the message to log; the format depends on the message factory.%n     * @param paramSuppliers An array of functions, which when called, produce the desired log message parameters.%n     * @since Log4j-2.4%n     */%n    public void methodName(final String message, final Supplier<?>... paramSuppliers) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, paramSuppliers);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level with the specified Marker. The {@code MessageSupplier} may or may%n     * not use the {@link MessageFactory} to construct the {@code Message}.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final MessageSupplier msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) with the specified Marker and including the stack trace of the {@link Throwable}%n     * <code>t</code> passed as parameter. The {@code MessageSupplier} may or may not use the%n     * {@link MessageFactory} to construct the {@code Message}.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @param t A Throwable or null.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, t);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level. The {@code MessageSupplier} may or may not use the%n     * {@link MessageFactory} to construct the {@code Message}.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @since Log4j-2.4%n     */%n    public void methodName(final MessageSupplier msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) including the stack trace of the {@link Throwable} <code>t</code> passed as parameter.%n     * The {@code MessageSupplier} may or may not use the {@link MessageFactory} to construct the%n     * {@code Message}.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.4%n     */%n    public void methodName(final MessageSupplier msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, t);%n    }%n";
/*      */   
/*      */   enum Type
/*      */   {
/*   54 */     CUSTOM
/*      */     {
/*      */       String imports() {
/*   57 */         return "import java.io.Serializable;%nimport org.apache.logging.log4j.Level;%nimport org.apache.logging.log4j.LogManager;%nimport org.apache.logging.log4j.Logger;%nimport org.apache.logging.log4j.Marker;%nimport org.apache.logging.log4j.message.Message;%nimport org.apache.logging.log4j.message.MessageFactory;%nimport org.apache.logging.log4j.spi.AbstractLogger;%nimport org.apache.logging.log4j.spi.ExtendedLoggerWrapper;%nimport org.apache.logging.log4j.util.MessageSupplier;%nimport org.apache.logging.log4j.util.Supplier;%n%n";
/*      */       }
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
/*      */       String declaration() {
/*   74 */         return "/**%n * Custom Logger interface with convenience methods for%n * %s%n * <p>Compatible with Log4j 2.6 or higher.</p>%n */%npublic final class %s implements Serializable {%n    private static final long serialVersionUID = " + System.nanoTime() + "L;%n" + "    private final ExtendedLoggerWrapper logger;%n" + "%n";
/*      */       }
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
/*      */       String constructor() {
/*   88 */         return "%n    private %s(final Logger logger) {%n        this.logger = new ExtendedLoggerWrapper((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());%n    }%n";
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Class<?> generator() {
/*   98 */         return Generate.CustomLogger.class;
/*      */       }
/*      */     },
/*  101 */     EXTEND
/*      */     {
/*      */       String imports() {
/*  104 */         return "import org.apache.logging.log4j.Level;%nimport org.apache.logging.log4j.LogManager;%nimport org.apache.logging.log4j.Logger;%nimport org.apache.logging.log4j.Marker;%nimport org.apache.logging.log4j.message.Message;%nimport org.apache.logging.log4j.message.MessageFactory;%nimport org.apache.logging.log4j.spi.AbstractLogger;%nimport org.apache.logging.log4j.spi.ExtendedLoggerWrapper;%nimport org.apache.logging.log4j.util.MessageSupplier;%nimport org.apache.logging.log4j.util.Supplier;%n%n";
/*      */       }
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
/*      */       String declaration() {
/*  120 */         return "/**%n * Extended Logger interface with convenience methods for%n * %s%n * <p>Compatible with Log4j 2.6 or higher.</p>%n */%npublic final class %s extends ExtendedLoggerWrapper {%n    private static final long serialVersionUID = " + System.nanoTime() + "L;%n" + "    private final ExtendedLoggerWrapper logger;%n" + "%n";
/*      */       }
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
/*      */       String constructor() {
/*  134 */         return "%n    private %s(final Logger logger) {%n        super((AbstractLogger) logger, logger.getName(), logger.getMessageFactory());%n        this.logger = this;%n    }%n";
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Class<?> generator() {
/*  144 */         return Generate.ExtendedLogger.class;
/*      */       }
/*      */     };
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract String imports();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract String declaration();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract String constructor();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Class<?> generator();
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
/*      */   public static final class CustomLogger
/*      */   {
/*      */     public static void main(String[] args) {
/*  991 */       Generate.generate(args, Generate.Type.CUSTOM);
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class ExtendedLogger
/*      */   {
/*      */     public static void main(String[] args) {
/* 1012 */       Generate.generate(args, Generate.Type.EXTEND);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class LevelInfo
/*      */   {
/*      */     final String name;
/*      */     
/*      */     final int intLevel;
/*      */     
/*      */     LevelInfo(String description) {
/* 1024 */       String[] parts = description.split("=");
/* 1025 */       this.name = parts[0];
/* 1026 */       this.intLevel = Integer.parseInt(parts[1]);
/*      */     }
/*      */     
/*      */     public static List<LevelInfo> parse(List<String> values, Class<?> generator) {
/* 1030 */       List<LevelInfo> result = new ArrayList<>(values.size());
/* 1031 */       for (int i = 0; i < values.size(); i++) {
/*      */         try {
/* 1033 */           result.add(new LevelInfo(values.get(i)));
/* 1034 */         } catch (Exception ex) {
/* 1035 */           System.err.println("Cannot parse custom level '" + (String)values.get(i) + "': " + ex.toString());
/* 1036 */           Generate.usage(System.err, generator);
/* 1037 */           System.exit(-1);
/*      */         } 
/*      */       } 
/* 1040 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static void generate(String[] args, Type type) {
/* 1045 */     generate(args, type, System.out);
/*      */   }
/*      */   
/*      */   static void generate(String[] args, Type type, PrintStream printStream) {
/* 1049 */     if (!validate(args)) {
/* 1050 */       usage(printStream, type.generator());
/* 1051 */       System.exit(-1);
/*      */     } 
/* 1053 */     List<String> values = new ArrayList<>(Arrays.asList(args));
/* 1054 */     String classFQN = values.remove(0);
/* 1055 */     List<LevelInfo> levels = LevelInfo.parse(values, type.generator());
/* 1056 */     printStream.println(generateSource(classFQN, levels, type));
/*      */   }
/*      */   
/*      */   static boolean validate(String[] args) {
/* 1060 */     if (args.length < 2) {
/* 1061 */       return false;
/*      */     }
/* 1063 */     return true;
/*      */   }
/*      */   
/*      */   private static void usage(PrintStream out, Class<?> generator) {
/* 1067 */     out.println("Usage: java " + generator.getName() + " className LEVEL1=intLevel1 [LEVEL2=intLevel2...]");
/* 1068 */     out.println("       Where className is the fully qualified class name of the custom/extended logger");
/* 1069 */     out.println("       to generate, followed by a space-separated list of custom log levels.");
/* 1070 */     out.println("       For each custom log level, specify NAME=intLevel (without spaces).");
/*      */   }
/*      */   
/*      */   static String generateSource(String classNameFQN, List<LevelInfo> levels, Type type) {
/* 1074 */     StringBuilder sb = new StringBuilder(10000 * levels.size());
/* 1075 */     int lastDot = classNameFQN.lastIndexOf('.');
/* 1076 */     String pkg = classNameFQN.substring(0, (lastDot >= 0) ? lastDot : 0);
/* 1077 */     if (!pkg.isEmpty()) {
/* 1078 */       sb.append(String.format("package %s;%n%n", new Object[] { pkg }));
/*      */     }
/* 1080 */     sb.append(String.format(type.imports(), new Object[] { "" }));
/* 1081 */     String className = classNameFQN.substring(classNameFQN.lastIndexOf('.') + 1);
/* 1082 */     String javadocDescr = javadocDescription(levels);
/* 1083 */     sb.append(String.format(type.declaration(), new Object[] { javadocDescr, className }));
/* 1084 */     sb.append(String.format("    private static final String FQCN = %s.class.getName();%n", new Object[] { className }));
/* 1085 */     for (LevelInfo level : levels) {
/* 1086 */       sb.append(String.format("    private static final Level %s = Level.forName(\"%s\", %d);%n", new Object[] { level.name, level.name, Integer.valueOf(level.intLevel) }));
/*      */     } 
/* 1088 */     sb.append(String.format(type.constructor(), new Object[] { className }));
/* 1089 */     sb.append(String.format("%n    /**%n     * Returns a custom Logger with the name of the calling class.%n     * %n     * @return The custom Logger for the calling class.%n     */%n    public static CLASSNAME create() {%n        final Logger wrapped = LogManager.getLogger();%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified name of the Class as%n     * the Logger name.%n     * %n     * @param loggerName The Class whose name should be used as the Logger name.%n     *            If null it will default to the calling class.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Class<?> loggerName) {%n        final Logger wrapped = LogManager.getLogger(loggerName);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified name of the Class as%n     * the Logger name.%n     * %n     * @param loggerName The Class whose name should be used as the Logger name.%n     *            If null it will default to the calling class.%n     * @param messageFactory The message factory is used only when creating a%n     *            logger, subsequent use does not change the logger but will log%n     *            a warning if mismatched.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Class<?> loggerName, final MessageFactory messageFactory) {%n        final Logger wrapped = LogManager.getLogger(loggerName, messageFactory);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified class name of the value%n     * as the Logger name.%n     * %n     * @param value The value whose class name should be used as the Logger%n     *            name. If null the name of the calling class will be used as%n     *            the logger name.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Object value) {%n        final Logger wrapped = LogManager.getLogger(value);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger using the fully qualified class name of the value%n     * as the Logger name.%n     * %n     * @param value The value whose class name should be used as the Logger%n     *            name. If null the name of the calling class will be used as%n     *            the logger name.%n     * @param messageFactory The message factory is used only when creating a%n     *            logger, subsequent use does not change the logger but will log%n     *            a warning if mismatched.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final Object value, final MessageFactory messageFactory) {%n        final Logger wrapped = LogManager.getLogger(value, messageFactory);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger with the specified name.%n     * %n     * @param name The logger name. If null the name of the calling class will%n     *            be used.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final String name) {%n        final Logger wrapped = LogManager.getLogger(name);%n        return new CLASSNAME(wrapped);%n    }%n%n    /**%n     * Returns a custom Logger with the specified name.%n     * %n     * @param name The logger name. If null the name of the calling class will%n     *            be used.%n     * @param messageFactory The message factory is used only when creating a%n     *            logger, subsequent use does not change the logger but will log%n     *            a warning if mismatched.%n     * @return The custom Logger.%n     */%n    public static CLASSNAME create(final String name, final MessageFactory messageFactory) {%n        final Logger wrapped = LogManager.getLogger(name, messageFactory);%n        return new CLASSNAME(wrapped);%n    }%n".replaceAll("CLASSNAME", className), new Object[] { "" }));
/* 1090 */     for (LevelInfo level : levels) {
/* 1091 */       String methodName = camelCase(level.name);
/* 1092 */       String phase1 = "%n    /**%n     * Logs a message with the specific Marker at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param msg the message string to be logged%n     */%n    public void methodName(final Marker marker, final Message msg) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msg, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with the specific Marker at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param msg the message string to be logged%n     * @param t A Throwable or null.%n     */%n    public void methodName(final Marker marker, final Message msg, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msg, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message object to log.%n     */%n    public void methodName(final Marker marker, final Object message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message CharSequence with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message CharSequence to log.%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final CharSequence message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final Marker marker, final Object message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, t);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the CharSequence to log.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final CharSequence message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message object to log.%n     */%n    public void methodName(final Marker marker, final String message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param params parameters to the message.%n     * @see #getMessageFactory()%n     */%n    public void methodName(final Marker marker, final String message, final Object... params) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, params);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @param p9 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final Marker marker, final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8, final Object p9) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param marker the marker data specific to this log statement%n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final Marker marker, final String message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, t);%n    }%n%n    /**%n     * Logs the specified Message at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param msg the message string to be logged%n     */%n    public void methodName(final Message msg) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msg, (Throwable) null);%n    }%n%n    /**%n     * Logs the specified Message at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param msg the message string to be logged%n     * @param t A Throwable or null.%n     */%n    public void methodName(final Message msg, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msg, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message object to log.%n     */%n    public void methodName(final Object message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final Object message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, t);%n    }%n%n    /**%n     * Logs a message CharSequence with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message CharSequence to log.%n     * @since Log4j-2.6%n     */%n    public void methodName(final CharSequence message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a CharSequence at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param message the CharSequence to log.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.6%n     */%n    public void methodName(final CharSequence message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, t);%n    }%n%n    /**%n     * Logs a message object with the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message object to log.%n     */%n    public void methodName(final String message) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param params parameters to the message.%n     * @see #getMessageFactory()%n     */%n    public void methodName(final String message, final Object... params) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, params);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6, p7);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);%n    }%n%n    /**%n     * Logs a message with parameters at the {@code CUSTOM_LEVEL} level.%n     * %n     * @param message the message to log; the format depends on the message factory.%n     * @param p0 parameter to the message.%n     * @param p1 parameter to the message.%n     * @param p2 parameter to the message.%n     * @param p3 parameter to the message.%n     * @param p4 parameter to the message.%n     * @param p5 parameter to the message.%n     * @param p6 parameter to the message.%n     * @param p7 parameter to the message.%n     * @param p8 parameter to the message.%n     * @param p9 parameter to the message.%n     * @see #getMessageFactory()%n     * @since Log4j-2.6%n     */%n    public void methodName(final String message, final Object p0, final Object p1, final Object p2,%n            final Object p3, final Object p4, final Object p5, final Object p6,%n            final Object p7, final Object p8, final Object p9) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);%n    }%n%n    /**%n     * Logs a message at the {@code CUSTOM_LEVEL} level including the stack trace of%n     * the {@link Throwable} {@code t} passed as parameter.%n     * %n     * @param message the message to log.%n     * @param t the exception to log, including its stack trace.%n     */%n    public void methodName(final String message, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, t);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the {@code CUSTOM_LEVEL}level.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Supplier<?> msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) including the stack trace of the {@link Throwable} <code>t</code> passed as parameter.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Supplier<?> msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, t);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level with the specified Marker.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final Supplier<?> msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message with parameters which are only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param message the message to log; the format depends on the message factory.%n     * @param paramSuppliers An array of functions, which when called, produce the desired log message parameters.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, message, paramSuppliers);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) with the specified Marker and including the stack trace of the {@link Throwable}%n     * <code>t</code> passed as parameter.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message;%n     *            the format depends on the message factory.%n     * @param t A Throwable or null.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final Supplier<?> msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, t);%n    }%n%n    /**%n     * Logs a message with parameters which are only to be constructed if the logging level is%n     * the {@code CUSTOM_LEVEL} level.%n     *%n     * @param message the message to log; the format depends on the message factory.%n     * @param paramSuppliers An array of functions, which when called, produce the desired log message parameters.%n     * @since Log4j-2.4%n     */%n    public void methodName(final String message, final Supplier<?>... paramSuppliers) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, message, paramSuppliers);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level with the specified Marker. The {@code MessageSupplier} may or may%n     * not use the {@link MessageFactory} to construct the {@code Message}.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final MessageSupplier msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) with the specified Marker and including the stack trace of the {@link Throwable}%n     * <code>t</code> passed as parameter. The {@code MessageSupplier} may or may not use the%n     * {@link MessageFactory} to construct the {@code Message}.%n     *%n     * @param marker the marker data specific to this log statement%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @param t A Throwable or null.%n     * @since Log4j-2.4%n     */%n    public void methodName(final Marker marker, final MessageSupplier msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, marker, msgSupplier, t);%n    }%n%n    /**%n     * Logs a message which is only to be constructed if the logging level is the%n     * {@code CUSTOM_LEVEL} level. The {@code MessageSupplier} may or may not use the%n     * {@link MessageFactory} to construct the {@code Message}.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @since Log4j-2.4%n     */%n    public void methodName(final MessageSupplier msgSupplier) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, (Throwable) null);%n    }%n%n    /**%n     * Logs a message (only to be constructed if the logging level is the {@code CUSTOM_LEVEL}%n     * level) including the stack trace of the {@link Throwable} <code>t</code> passed as parameter.%n     * The {@code MessageSupplier} may or may not use the {@link MessageFactory} to construct the%n     * {@code Message}.%n     *%n     * @param msgSupplier A function, which when called, produces the desired log message.%n     * @param t the exception to log, including its stack trace.%n     * @since Log4j-2.4%n     */%n    public void methodName(final MessageSupplier msgSupplier, final Throwable t) {%n        logger.logIfEnabled(FQCN, CUSTOM_LEVEL, null, msgSupplier, t);%n    }%n".replaceAll("CUSTOM_LEVEL", level.name);
/* 1093 */       String phase2 = phase1.replaceAll("methodName", methodName);
/* 1094 */       sb.append(String.format(phase2, new Object[] { "" }));
/*      */     } 
/*      */     
/* 1097 */     sb.append('}');
/* 1098 */     sb.append(System.getProperty("line.separator"));
/* 1099 */     return sb.toString();
/*      */   }
/*      */   
/*      */   static String javadocDescription(List<LevelInfo> levels) {
/* 1103 */     if (levels.size() == 1) {
/* 1104 */       return "the " + ((LevelInfo)levels.get(0)).name + " custom log level.";
/*      */     }
/* 1106 */     StringBuilder sb = new StringBuilder(512);
/* 1107 */     sb.append("the ");
/* 1108 */     String sep = "";
/* 1109 */     for (int i = 0; i < levels.size(); i++) {
/* 1110 */       sb.append(sep);
/* 1111 */       sb.append(((LevelInfo)levels.get(i)).name);
/* 1112 */       sep = (i == levels.size() - 2) ? " and " : ", ";
/*      */     } 
/* 1114 */     sb.append(" custom log levels.");
/* 1115 */     return sb.toString();
/*      */   }
/*      */   
/*      */   static String camelCase(String customLevel) {
/* 1119 */     StringBuilder sb = new StringBuilder(customLevel.length());
/* 1120 */     boolean lower = true;
/* 1121 */     for (char ch : customLevel.toCharArray()) {
/* 1122 */       if (ch == '_') {
/* 1123 */         lower = false;
/*      */       } else {
/*      */         
/* 1126 */         sb.append(lower ? Character.toLowerCase(ch) : Character.toUpperCase(ch));
/* 1127 */         lower = true;
/*      */       } 
/* 1129 */     }  return sb.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\tools\Generate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */