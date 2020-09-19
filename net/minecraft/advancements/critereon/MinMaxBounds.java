/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public abstract class MinMaxBounds<T extends Number> {
/*     */   public static class Ints extends MinMaxBounds<Integer> {
/*  21 */     public static final Ints ANY = new Ints(null, null); private final Long minSq;
/*     */     
/*     */     private static Ints create(StringReader debug0, @Nullable Integer debug1, @Nullable Integer debug2) throws CommandSyntaxException {
/*  24 */       if (debug1 != null && debug2 != null && debug1.intValue() > debug2.intValue()) {
/*  25 */         throw ERROR_SWAPPED.createWithContext(debug0);
/*     */       }
/*     */       
/*  28 */       return new Ints(debug1, debug2);
/*     */     }
/*     */     private final Long maxSq;
/*     */     @Nullable
/*     */     private static Long squareOpt(@Nullable Integer debug0) {
/*  33 */       return (debug0 == null) ? null : Long.valueOf(debug0.longValue() * debug0.longValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Ints(@Nullable Integer debug1, @Nullable Integer debug2) {
/*  40 */       super(debug1, debug2);
/*  41 */       this.minSq = squareOpt(debug1);
/*  42 */       this.maxSq = squareOpt(debug2);
/*     */     }
/*     */     
/*     */     public static Ints exactly(int debug0) {
/*  46 */       return new Ints(Integer.valueOf(debug0), Integer.valueOf(debug0));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Ints atLeast(int debug0) {
/*  54 */       return new Ints(Integer.valueOf(debug0), null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(int debug1) {
/*  62 */       if (this.min != null && this.min.intValue() > debug1) {
/*  63 */         return false;
/*     */       }
/*  65 */       if (this.max != null && this.max.intValue() < debug1) {
/*  66 */         return false;
/*     */       }
/*  68 */       return true;
/*     */     }
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
/*     */     public static Ints fromJson(@Nullable JsonElement debug0) {
/*  82 */       return (Ints)fromJson(debug0, ANY, GsonHelper::convertToInt, Ints::new);
/*     */     }
/*     */     
/*     */     public static Ints fromReader(StringReader debug0) throws CommandSyntaxException {
/*  86 */       return fromReader(debug0, debug0 -> debug0);
/*     */     }
/*     */     
/*     */     public static Ints fromReader(StringReader debug0, Function<Integer, Integer> debug1) throws CommandSyntaxException {
/*  90 */       return (Ints)fromReader(debug0, Ints::create, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Floats extends MinMaxBounds<Float> {
/*  95 */     public static final Floats ANY = new Floats(null, null); private final Double minSq; private final Double maxSq;
/*     */     
/*     */     private static Floats create(StringReader debug0, @Nullable Float debug1, @Nullable Float debug2) throws CommandSyntaxException {
/*  98 */       if (debug1 != null && debug2 != null && debug1.floatValue() > debug2.floatValue()) {
/*  99 */         throw ERROR_SWAPPED.createWithContext(debug0);
/*     */       }
/*     */       
/* 102 */       return new Floats(debug1, debug2);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static Double squareOpt(@Nullable Float debug0) {
/* 107 */       return (debug0 == null) ? null : Double.valueOf(debug0.doubleValue() * debug0.doubleValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Floats(@Nullable Float debug1, @Nullable Float debug2) {
/* 114 */       super(debug1, debug2);
/* 115 */       this.minSq = squareOpt(debug1);
/* 116 */       this.maxSq = squareOpt(debug2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Floats atLeast(float debug0) {
/* 128 */       return new Floats(Float.valueOf(debug0), null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(float debug1) {
/* 136 */       if (this.min != null && this.min.floatValue() > debug1) {
/* 137 */         return false;
/*     */       }
/* 139 */       if (this.max != null && this.max.floatValue() < debug1) {
/* 140 */         return false;
/*     */       }
/* 142 */       return true;
/*     */     }
/*     */     
/*     */     public boolean matchesSqr(double debug1) {
/* 146 */       if (this.minSq != null && this.minSq.doubleValue() > debug1) {
/* 147 */         return false;
/*     */       }
/* 149 */       if (this.maxSq != null && this.maxSq.doubleValue() < debug1) {
/* 150 */         return false;
/*     */       }
/* 152 */       return true;
/*     */     }
/*     */     
/*     */     public static Floats fromJson(@Nullable JsonElement debug0) {
/* 156 */       return (Floats)fromJson(debug0, ANY, GsonHelper::convertToFloat, Floats::new);
/*     */     }
/*     */     
/*     */     public static Floats fromReader(StringReader debug0) throws CommandSyntaxException {
/* 160 */       return fromReader(debug0, debug0 -> debug0);
/*     */     }
/*     */     
/*     */     public static Floats fromReader(StringReader debug0, Function<Float, Float> debug1) throws CommandSyntaxException {
/* 164 */       return (Floats)fromReader(debug0, Floats::create, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat, debug1);
/*     */     }
/*     */   }
/*     */   
/* 168 */   public static final SimpleCommandExceptionType ERROR_EMPTY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.range.empty"));
/* 169 */   public static final SimpleCommandExceptionType ERROR_SWAPPED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.range.swapped"));
/*     */   
/*     */   protected final T min;
/*     */   protected final T max;
/*     */   
/*     */   protected MinMaxBounds(@Nullable T debug1, @Nullable T debug2) {
/* 175 */     this.min = debug1;
/* 176 */     this.max = debug2;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public T getMin() {
/* 181 */     return this.min;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public T getMax() {
/* 186 */     return this.max;
/*     */   }
/*     */   
/*     */   public boolean isAny() {
/* 190 */     return (this.min == null && this.max == null);
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 194 */     if (isAny()) {
/* 195 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 198 */     if (this.min != null && this.min.equals(this.max)) {
/* 199 */       return (JsonElement)new JsonPrimitive((Number)this.min);
/*     */     }
/*     */     
/* 202 */     JsonObject debug1 = new JsonObject();
/* 203 */     if (this.min != null) {
/* 204 */       debug1.addProperty("min", (Number)this.min);
/*     */     }
/* 206 */     if (this.max != null) {
/* 207 */       debug1.addProperty("max", (Number)this.max);
/*     */     }
/* 209 */     return (JsonElement)debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <T extends Number, R extends MinMaxBounds<T>> R fromJson(@Nullable JsonElement debug0, R debug1, BiFunction<JsonElement, String, T> debug2, BoundsFactory<T, R> debug3) {
/* 218 */     if (debug0 == null || debug0.isJsonNull()) {
/* 219 */       return debug1;
/*     */     }
/*     */     
/* 222 */     if (GsonHelper.isNumberValue(debug0)) {
/* 223 */       Number number = (Number)debug2.apply(debug0, "value");
/* 224 */       return debug3.create((T)number, (T)number);
/*     */     } 
/* 226 */     JsonObject debug4 = GsonHelper.convertToJsonObject(debug0, "value");
/* 227 */     Number number1 = debug4.has("min") ? (Number)debug2.apply(debug4.get("min"), "min") : null;
/* 228 */     Number number2 = debug4.has("max") ? (Number)debug2.apply(debug4.get("max"), "max") : null;
/* 229 */     return debug3.create((T)number1, (T)number2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <T extends Number, R extends MinMaxBounds<T>> R fromReader(StringReader debug0, BoundsFromReaderFactory<T, R> debug1, Function<String, T> debug2, Supplier<DynamicCommandExceptionType> debug3, Function<T, T> debug4) throws CommandSyntaxException {
/* 239 */     if (!debug0.canRead()) {
/* 240 */       throw ERROR_EMPTY.createWithContext(debug0);
/*     */     }
/*     */     
/* 243 */     int debug5 = debug0.getCursor();
/*     */     
/*     */     try {
/* 246 */       Number number2, number1 = optionallyFormat(readNumber(debug0, debug2, debug3), debug4);
/*     */       
/* 248 */       if (debug0.canRead(2) && debug0.peek() == '.' && debug0.peek(1) == '.') {
/* 249 */         debug0.skip();
/* 250 */         debug0.skip();
/* 251 */         number2 = optionallyFormat(readNumber(debug0, debug2, debug3), debug4);
/* 252 */         if (number1 == null && number2 == null) {
/* 253 */           throw ERROR_EMPTY.createWithContext(debug0);
/*     */         }
/*     */       } else {
/* 256 */         number2 = number1;
/*     */       } 
/*     */       
/* 259 */       if (number1 == null && number2 == null) {
/* 260 */         throw ERROR_EMPTY.createWithContext(debug0);
/*     */       }
/* 262 */       return debug1.create(debug0, (T)number1, (T)number2);
/* 263 */     } catch (CommandSyntaxException debug6) {
/* 264 */       debug0.setCursor(debug5);
/* 265 */       throw new CommandSyntaxException(debug6.getType(), debug6.getRawMessage(), debug6.getInput(), debug5);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static <T extends Number> T readNumber(StringReader debug0, Function<String, T> debug1, Supplier<DynamicCommandExceptionType> debug2) throws CommandSyntaxException {
/* 271 */     int debug3 = debug0.getCursor();
/* 272 */     while (debug0.canRead() && isAllowedInputChat(debug0)) {
/* 273 */       debug0.skip();
/*     */     }
/* 275 */     String debug4 = debug0.getString().substring(debug3, debug0.getCursor());
/* 276 */     if (debug4.isEmpty()) {
/* 277 */       return null;
/*     */     }
/*     */     try {
/* 280 */       return debug1.apply(debug4);
/* 281 */     } catch (NumberFormatException debug5) {
/* 282 */       throw ((DynamicCommandExceptionType)debug2.get()).createWithContext(debug0, debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAllowedInputChat(StringReader debug0) {
/* 287 */     char debug1 = debug0.peek();
/* 288 */     if ((debug1 >= '0' && debug1 <= '9') || debug1 == '-') {
/* 289 */       return true;
/*     */     }
/*     */     
/* 292 */     if (debug1 == '.') {
/* 293 */       return (!debug0.canRead(2) || debug0.peek(1) != '.');
/*     */     }
/*     */     
/* 296 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static <T> T optionallyFormat(@Nullable T debug0, Function<T, T> debug1) {
/* 301 */     return (debug0 == null) ? null : debug1.apply(debug0);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BoundsFactory<T extends Number, R extends MinMaxBounds<T>> {
/*     */     R create(@Nullable T param1T1, @Nullable T param1T2);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BoundsFromReaderFactory<T extends Number, R extends MinMaxBounds<T>> {
/*     */     R create(StringReader param1StringReader, @Nullable T param1T1, @Nullable T param1T2) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\MinMaxBounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */