/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class Settings<T extends Settings<T>> {
/*     */   public class MutableValue<V>
/*     */     implements Supplier<V> {
/*     */     private final String key;
/*     */     private final V value;
/*     */     private final Function<V, String> serializer;
/*     */     
/*     */     private MutableValue(String debug2, V debug3, Function<V, String> debug4) {
/*  28 */       this.key = debug2;
/*  29 */       this.value = debug3;
/*  30 */       this.serializer = debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() {
/*  35 */       return this.value;
/*     */     }
/*     */     
/*     */     public T update(RegistryAccess debug1, V debug2) {
/*  39 */       Properties debug3 = Settings.this.cloneProperties();
/*  40 */       debug3.put(this.key, this.serializer.apply(debug2));
/*  41 */       return Settings.this.reload(debug1, debug3);
/*     */     }
/*     */   }
/*     */   
/*  45 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Properties properties;
/*     */   
/*     */   public Settings(Properties debug1) {
/*  50 */     this.properties = debug1;
/*     */   }
/*     */   
/*     */   public static Properties loadFromFile(Path debug0) {
/*  54 */     Properties debug1 = new Properties();
/*  55 */     try (InputStream debug2 = Files.newInputStream(debug0, new java.nio.file.OpenOption[0])) {
/*  56 */       debug1.load(debug2);
/*  57 */     } catch (IOException debug2) {
/*  58 */       LOGGER.error("Failed to load properties from file: " + debug0);
/*     */     } 
/*  60 */     return debug1;
/*     */   }
/*     */   
/*     */   public void store(Path debug1) {
/*  64 */     try (OutputStream debug2 = Files.newOutputStream(debug1, new java.nio.file.OpenOption[0])) {
/*  65 */       this.properties.store(debug2, "Minecraft server properties");
/*  66 */     } catch (IOException debug2) {
/*  67 */       LOGGER.error("Failed to store properties to file: " + debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <V extends Number> Function<String, V> wrapNumberDeserializer(Function<String, V> debug0) {
/*  72 */     return debug1 -> {
/*     */         try {
/*     */           return debug0.apply(debug1);
/*  75 */         } catch (NumberFormatException debug2) {
/*     */           return null;
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   protected static <V> Function<String, V> dispatchNumberOrString(IntFunction<V> debug0, Function<String, V> debug1) {
/*  82 */     return debug2 -> {
/*     */         try {
/*     */           return debug0.apply(Integer.parseInt(debug2));
/*  85 */         } catch (NumberFormatException debug3) {
/*     */           return debug1.apply(debug2);
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getStringRaw(String debug1) {
/*  93 */     return (String)this.properties.get(debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected <V> V getLegacy(String debug1, Function<String, V> debug2) {
/*  98 */     String debug3 = getStringRaw(debug1);
/*  99 */     if (debug3 == null) {
/* 100 */       return null;
/*     */     }
/* 102 */     this.properties.remove(debug1);
/* 103 */     return debug2.apply(debug3);
/*     */   }
/*     */   
/*     */   protected <V> V get(String debug1, Function<String, V> debug2, Function<V, String> debug3, V debug4) {
/* 107 */     String debug5 = getStringRaw(debug1);
/* 108 */     V debug6 = (V)MoreObjects.firstNonNull((debug5 != null) ? debug2.apply(debug5) : null, debug4);
/* 109 */     this.properties.put(debug1, debug3.apply(debug6));
/* 110 */     return debug6;
/*     */   }
/*     */   
/*     */   protected <V> MutableValue<V> getMutable(String debug1, Function<String, V> debug2, Function<V, String> debug3, V debug4) {
/* 114 */     String debug5 = getStringRaw(debug1);
/* 115 */     V debug6 = (V)MoreObjects.firstNonNull((debug5 != null) ? debug2.apply(debug5) : null, debug4);
/* 116 */     this.properties.put(debug1, debug3.apply(debug6));
/* 117 */     return new MutableValue<>(debug1, debug6, debug3);
/*     */   }
/*     */   
/*     */   protected <V> V get(String debug1, Function<String, V> debug2, UnaryOperator<V> debug3, Function<V, String> debug4, V debug5) {
/* 121 */     return get(debug1, debug2 -> { V debug3 = debug0.apply(debug2); return (debug3 != null) ? debug1.apply(debug3) : null; }debug4, debug5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <V> V get(String debug1, Function<String, V> debug2, V debug3) {
/* 128 */     return get(debug1, debug2, Objects::toString, debug3);
/*     */   }
/*     */   
/*     */   protected <V> MutableValue<V> getMutable(String debug1, Function<String, V> debug2, V debug3) {
/* 132 */     return getMutable(debug1, debug2, Objects::toString, debug3);
/*     */   }
/*     */   
/*     */   protected String get(String debug1, String debug2) {
/* 136 */     return get(debug1, Function.identity(), Function.identity(), debug2);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected String getLegacyString(String debug1) {
/* 141 */     return getLegacy(debug1, Function.identity());
/*     */   }
/*     */   
/*     */   protected int get(String debug1, int debug2) {
/* 145 */     return ((Integer)get(debug1, wrapNumberDeserializer(Integer::parseInt), Integer.valueOf(debug2))).intValue();
/*     */   }
/*     */   
/*     */   protected MutableValue<Integer> getMutable(String debug1, int debug2) {
/* 149 */     return getMutable(debug1, wrapNumberDeserializer(Integer::parseInt), Integer.valueOf(debug2));
/*     */   }
/*     */   
/*     */   protected int get(String debug1, UnaryOperator<Integer> debug2, int debug3) {
/* 153 */     return ((Integer)get(debug1, wrapNumberDeserializer(Integer::parseInt), debug2, Objects::toString, Integer.valueOf(debug3))).intValue();
/*     */   }
/*     */   
/*     */   protected long get(String debug1, long debug2) {
/* 157 */     return ((Long)get(debug1, wrapNumberDeserializer(Long::parseLong), Long.valueOf(debug2))).longValue();
/*     */   }
/*     */   
/*     */   protected boolean get(String debug1, boolean debug2) {
/* 161 */     return ((Boolean)get(debug1, Boolean::valueOf, Boolean.valueOf(debug2))).booleanValue();
/*     */   }
/*     */   
/*     */   protected MutableValue<Boolean> getMutable(String debug1, boolean debug2) {
/* 165 */     return getMutable(debug1, Boolean::valueOf, Boolean.valueOf(debug2));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Boolean getLegacyBoolean(String debug1) {
/* 170 */     return getLegacy(debug1, Boolean::valueOf);
/*     */   }
/*     */   
/*     */   protected Properties cloneProperties() {
/* 174 */     Properties debug1 = new Properties();
/* 175 */     debug1.putAll(this.properties);
/* 176 */     return debug1;
/*     */   }
/*     */   
/*     */   protected abstract T reload(RegistryAccess paramRegistryAccess, Properties paramProperties);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\dedicated\Settings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */