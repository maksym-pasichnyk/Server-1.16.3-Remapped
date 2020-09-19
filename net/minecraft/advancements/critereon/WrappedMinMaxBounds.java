/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrappedMinMaxBounds
/*     */ {
/*  17 */   public static final WrappedMinMaxBounds ANY = new WrappedMinMaxBounds(null, null);
/*     */   
/*  19 */   public static final SimpleCommandExceptionType ERROR_INTS_ONLY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.range.ints"));
/*     */   
/*     */   private final Float min;
/*     */   private final Float max;
/*     */   
/*     */   public WrappedMinMaxBounds(@Nullable Float debug1, @Nullable Float debug2) {
/*  25 */     this.min = debug1;
/*  26 */     this.max = debug2;
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
/*     */   @Nullable
/*     */   public Float getMin() {
/*  73 */     return this.min;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Float getMax() {
/*  78 */     return this.max;
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
/*     */   public static WrappedMinMaxBounds fromReader(StringReader debug0, boolean debug1, Function<Float, Float> debug2) throws CommandSyntaxException {
/*     */     Float debug5;
/* 121 */     if (!debug0.canRead()) {
/* 122 */       throw MinMaxBounds.ERROR_EMPTY.createWithContext(debug0);
/*     */     }
/* 124 */     int debug3 = debug0.getCursor();
/* 125 */     Float debug4 = optionallyFormat(readNumber(debug0, debug1), debug2);
/*     */     
/* 127 */     if (debug0.canRead(2) && debug0.peek() == '.' && debug0.peek(1) == '.')
/* 128 */     { debug0.skip();
/* 129 */       debug0.skip();
/* 130 */       debug5 = optionallyFormat(readNumber(debug0, debug1), debug2);
/* 131 */       if (debug4 == null && debug5 == null) {
/* 132 */         debug0.setCursor(debug3);
/* 133 */         throw MinMaxBounds.ERROR_EMPTY.createWithContext(debug0);
/*     */       }  }
/* 135 */     else { if (!debug1 && debug0.canRead() && debug0.peek() == '.') {
/* 136 */         debug0.setCursor(debug3);
/* 137 */         throw ERROR_INTS_ONLY.createWithContext(debug0);
/*     */       } 
/* 139 */       debug5 = debug4; }
/*     */     
/* 141 */     if (debug4 == null && debug5 == null) {
/* 142 */       debug0.setCursor(debug3);
/* 143 */       throw MinMaxBounds.ERROR_EMPTY.createWithContext(debug0);
/*     */     } 
/* 145 */     return new WrappedMinMaxBounds(debug4, debug5);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Float readNumber(StringReader debug0, boolean debug1) throws CommandSyntaxException {
/* 150 */     int debug2 = debug0.getCursor();
/* 151 */     while (debug0.canRead() && isAllowedNumber(debug0, debug1)) {
/* 152 */       debug0.skip();
/*     */     }
/* 154 */     String debug3 = debug0.getString().substring(debug2, debug0.getCursor());
/* 155 */     if (debug3.isEmpty()) {
/* 156 */       return null;
/*     */     }
/*     */     try {
/* 159 */       return Float.valueOf(Float.parseFloat(debug3));
/* 160 */     } catch (NumberFormatException debug4) {
/* 161 */       if (debug1) {
/* 162 */         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(debug0, debug3);
/*     */       }
/* 164 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(debug0, debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isAllowedNumber(StringReader debug0, boolean debug1) {
/* 170 */     char debug2 = debug0.peek();
/* 171 */     if ((debug2 >= '0' && debug2 <= '9') || debug2 == '-') {
/* 172 */       return true;
/*     */     }
/*     */     
/* 175 */     if (debug1 && debug2 == '.') {
/* 176 */       return (!debug0.canRead(2) || debug0.peek(1) != '.');
/*     */     }
/*     */     
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Float optionallyFormat(@Nullable Float debug0, Function<Float, Float> debug1) {
/* 184 */     return (debug0 == null) ? null : debug1.apply(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\WrappedMinMaxBounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */