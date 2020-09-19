/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.spi.StandardLevel;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public final class Level
/*     */   implements Comparable<Level>, Serializable
/*     */ {
/*     */   public static final Level OFF;
/*     */   public static final Level FATAL;
/*     */   public static final Level ERROR;
/*     */   public static final Level WARN;
/*     */   public static final Level INFO;
/*     */   public static final Level DEBUG;
/*     */   public static final Level TRACE;
/*     */   public static final Level ALL;
/*     */   public static final String CATEGORY = "Level";
/*  93 */   private static final ConcurrentMap<String, Level> LEVELS = new ConcurrentHashMap<>();
/*     */   private static final long serialVersionUID = 1581082L;
/*     */   private final String name;
/*     */   
/*     */   static {
/*  98 */     OFF = new Level("OFF", StandardLevel.OFF.intLevel());
/*  99 */     FATAL = new Level("FATAL", StandardLevel.FATAL.intLevel());
/* 100 */     ERROR = new Level("ERROR", StandardLevel.ERROR.intLevel());
/* 101 */     WARN = new Level("WARN", StandardLevel.WARN.intLevel());
/* 102 */     INFO = new Level("INFO", StandardLevel.INFO.intLevel());
/* 103 */     DEBUG = new Level("DEBUG", StandardLevel.DEBUG.intLevel());
/* 104 */     TRACE = new Level("TRACE", StandardLevel.TRACE.intLevel());
/* 105 */     ALL = new Level("ALL", StandardLevel.ALL.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   private final int intLevel;
/*     */   private final StandardLevel standardLevel;
/*     */   
/*     */   private Level(String name, int intLevel) {
/* 113 */     if (Strings.isEmpty(name)) {
/* 114 */       throw new IllegalArgumentException("Illegal null or empty Level name.");
/*     */     }
/* 116 */     if (intLevel < 0) {
/* 117 */       throw new IllegalArgumentException("Illegal Level int less than zero.");
/*     */     }
/* 119 */     this.name = name;
/* 120 */     this.intLevel = intLevel;
/* 121 */     this.standardLevel = StandardLevel.getStandardLevel(intLevel);
/* 122 */     if (LEVELS.putIfAbsent(name, this) != null) {
/* 123 */       throw new IllegalStateException("Level " + name + " has already been defined.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intLevel() {
/* 133 */     return this.intLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardLevel getStandardLevel() {
/* 142 */     return this.standardLevel;
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
/*     */   public boolean isInRange(Level minLevel, Level maxLevel) {
/* 155 */     return (this.intLevel >= minLevel.intLevel && this.intLevel <= maxLevel.intLevel);
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
/*     */   public boolean isLessSpecificThan(Level level) {
/* 171 */     return (this.intLevel >= level.intLevel);
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
/*     */   public boolean isMoreSpecificThan(Level level) {
/* 187 */     return (this.intLevel <= level.intLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level clone() throws CloneNotSupportedException {
/* 194 */     throw new CloneNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Level other) {
/* 200 */     return (this.intLevel < other.intLevel) ? -1 : ((this.intLevel > other.intLevel) ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 205 */     return (other instanceof Level && other == this);
/*     */   }
/*     */   
/*     */   public Class<Level> getDeclaringClass() {
/* 209 */     return Level.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 214 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 223 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 228 */     return this.name;
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
/*     */   public static Level forName(String name, int intValue) {
/* 240 */     Level level = LEVELS.get(name);
/* 241 */     if (level != null) {
/* 242 */       return level;
/*     */     }
/*     */     try {
/* 245 */       return new Level(name, intValue);
/* 246 */     } catch (IllegalStateException ex) {
/*     */       
/* 248 */       return LEVELS.get(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level getLevel(String name) {
/* 259 */     return LEVELS.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String sArg) {
/* 270 */     return toLevel(sArg, DEBUG);
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
/*     */   public static Level toLevel(String name, Level defaultLevel) {
/* 282 */     if (name == null) {
/* 283 */       return defaultLevel;
/*     */     }
/* 285 */     Level level = LEVELS.get(name.toUpperCase(Locale.ENGLISH));
/* 286 */     return (level == null) ? defaultLevel : level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level[] values() {
/* 295 */     Collection<Level> values = LEVELS.values();
/* 296 */     return values.<Level>toArray(new Level[values.size()]);
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
/*     */   public static Level valueOf(String name) {
/* 308 */     Objects.requireNonNull(name, "No level name given.");
/* 309 */     String levelName = name.toUpperCase(Locale.ENGLISH);
/* 310 */     Level level = LEVELS.get(levelName);
/* 311 */     if (level != null) {
/* 312 */       return level;
/*     */     }
/* 314 */     throw new IllegalArgumentException("Unknown level constant [" + levelName + "].");
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
/*     */   public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
/* 331 */     return Enum.valueOf(enumType, name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readResolve() {
/* 336 */     return valueOf(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\Level.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */