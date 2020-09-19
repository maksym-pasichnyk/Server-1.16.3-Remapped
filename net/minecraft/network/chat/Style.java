/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public class Style
/*     */ {
/*  21 */   public static final Style EMPTY = new Style(null, null, null, null, null, null, null, null, null, null);
/*     */   
/*  23 */   public static final ResourceLocation DEFAULT_FONT = new ResourceLocation("minecraft", "default");
/*     */   
/*     */   @Nullable
/*     */   private final TextColor color;
/*     */   @Nullable
/*     */   private final Boolean bold;
/*     */   @Nullable
/*     */   private final Boolean italic;
/*     */   @Nullable
/*     */   private final Boolean underlined;
/*     */   @Nullable
/*     */   private final Boolean strikethrough;
/*     */   @Nullable
/*     */   private final Boolean obfuscated;
/*     */   @Nullable
/*     */   private final ClickEvent clickEvent;
/*     */   @Nullable
/*     */   private final HoverEvent hoverEvent;
/*     */   @Nullable
/*     */   private final String insertion;
/*     */   @Nullable
/*     */   private final ResourceLocation font;
/*     */   
/*     */   private Style(@Nullable TextColor debug1, @Nullable Boolean debug2, @Nullable Boolean debug3, @Nullable Boolean debug4, @Nullable Boolean debug5, @Nullable Boolean debug6, @Nullable ClickEvent debug7, @Nullable HoverEvent debug8, @Nullable String debug9, @Nullable ResourceLocation debug10) {
/*  47 */     this.color = debug1;
/*  48 */     this.bold = debug2;
/*  49 */     this.italic = debug3;
/*  50 */     this.underlined = debug4;
/*  51 */     this.strikethrough = debug5;
/*  52 */     this.obfuscated = debug6;
/*  53 */     this.clickEvent = debug7;
/*  54 */     this.hoverEvent = debug8;
/*  55 */     this.insertion = debug9;
/*  56 */     this.font = debug10;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public TextColor getColor() {
/*  61 */     return this.color;
/*     */   }
/*     */   
/*     */   public boolean isBold() {
/*  65 */     return (this.bold == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isItalic() {
/*  69 */     return (this.italic == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isStrikethrough() {
/*  73 */     return (this.strikethrough == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isUnderlined() {
/*  77 */     return (this.underlined == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isObfuscated() {
/*  81 */     return (this.obfuscated == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  85 */     return (this == EMPTY);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ClickEvent getClickEvent() {
/*  90 */     return this.clickEvent;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public HoverEvent getHoverEvent() {
/*  95 */     return this.hoverEvent;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getInsertion() {
/* 100 */     return this.insertion;
/*     */   }
/*     */   
/*     */   public ResourceLocation getFont() {
/* 104 */     return (this.font != null) ? this.font : DEFAULT_FONT;
/*     */   }
/*     */   
/*     */   public Style withColor(@Nullable TextColor debug1) {
/* 108 */     return new Style(debug1, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style withColor(@Nullable ChatFormatting debug1) {
/* 112 */     return withColor((debug1 != null) ? TextColor.fromLegacyFormat(debug1) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style withBold(@Nullable Boolean debug1) {
/* 120 */     return new Style(this.color, debug1, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style withItalic(@Nullable Boolean debug1) {
/* 124 */     return new Style(this.color, this.bold, debug1, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
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
/*     */   public Style withClickEvent(@Nullable ClickEvent debug1) {
/* 140 */     return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, debug1, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style withHoverEvent(@Nullable HoverEvent debug1) {
/* 144 */     return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, debug1, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style withInsertion(@Nullable String debug1) {
/* 148 */     return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, debug1, this.font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style applyFormat(ChatFormatting debug1) {
/* 156 */     TextColor debug2 = this.color;
/* 157 */     Boolean debug3 = this.bold;
/* 158 */     Boolean debug4 = this.italic;
/* 159 */     Boolean debug5 = this.strikethrough;
/* 160 */     Boolean debug6 = this.underlined;
/* 161 */     Boolean debug7 = this.obfuscated;
/*     */     
/* 163 */     switch (debug1)
/*     */     { case OBFUSCATED:
/* 165 */         debug7 = Boolean.valueOf(true);
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
/* 185 */         return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);case BOLD: debug3 = Boolean.valueOf(true); return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);case STRIKETHROUGH: debug5 = Boolean.valueOf(true); return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);case UNDERLINE: debug6 = Boolean.valueOf(true); return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);case ITALIC: debug4 = Boolean.valueOf(true); return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);case RESET: return EMPTY; }  debug2 = TextColor.fromLegacyFormat(debug1); return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);
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
/*     */   public Style applyFormats(ChatFormatting... debug1) {
/* 228 */     TextColor debug2 = this.color;
/* 229 */     Boolean debug3 = this.bold;
/* 230 */     Boolean debug4 = this.italic;
/* 231 */     Boolean debug5 = this.strikethrough;
/* 232 */     Boolean debug6 = this.underlined;
/* 233 */     Boolean debug7 = this.obfuscated;
/*     */     
/* 235 */     for (ChatFormatting debug11 : debug1) {
/* 236 */       switch (debug11) {
/*     */         case OBFUSCATED:
/* 238 */           debug7 = Boolean.valueOf(true);
/*     */           break;
/*     */         case BOLD:
/* 241 */           debug3 = Boolean.valueOf(true);
/*     */           break;
/*     */         case STRIKETHROUGH:
/* 244 */           debug5 = Boolean.valueOf(true);
/*     */           break;
/*     */         case UNDERLINE:
/* 247 */           debug6 = Boolean.valueOf(true);
/*     */           break;
/*     */         case ITALIC:
/* 250 */           debug4 = Boolean.valueOf(true);
/*     */           break;
/*     */         case RESET:
/* 253 */           return EMPTY;
/*     */         default:
/* 255 */           debug2 = TextColor.fromLegacyFormat(debug11);
/*     */           break;
/*     */       } 
/*     */     } 
/* 259 */     return new Style(debug2, debug3, debug4, debug6, debug5, debug7, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyTo(Style debug1) {
/* 263 */     if (this == EMPTY) {
/* 264 */       return debug1;
/*     */     }
/*     */     
/* 267 */     if (debug1 == EMPTY) {
/* 268 */       return this;
/*     */     }
/*     */     
/* 271 */     return new Style((this.color != null) ? this.color : debug1.color, (this.bold != null) ? this.bold : debug1.bold, (this.italic != null) ? this.italic : debug1.italic, (this.underlined != null) ? this.underlined : debug1.underlined, (this.strikethrough != null) ? this.strikethrough : debug1.strikethrough, (this.obfuscated != null) ? this.obfuscated : debug1.obfuscated, (this.clickEvent != null) ? this.clickEvent : debug1.clickEvent, (this.hoverEvent != null) ? this.hoverEvent : debug1.hoverEvent, (this.insertion != null) ? this.insertion : debug1.insertion, (this.font != null) ? this.font : debug1.font);
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
/*     */   public String toString() {
/* 287 */     return "Style{ color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", clickEvent=" + 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       getClickEvent() + ", hoverEvent=" + 
/* 295 */       getHoverEvent() + ", insertion=" + 
/* 296 */       getInsertion() + ", font=" + 
/* 297 */       getFont() + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 303 */     if (this == debug1) {
/* 304 */       return true;
/*     */     }
/* 306 */     if (debug1 instanceof Style) {
/* 307 */       Style debug2 = (Style)debug1;
/*     */       
/* 309 */       return (isBold() == debug2.isBold() && 
/* 310 */         Objects.equals(getColor(), debug2.getColor()) && 
/* 311 */         isItalic() == debug2.isItalic() && 
/* 312 */         isObfuscated() == debug2.isObfuscated() && 
/* 313 */         isStrikethrough() == debug2.isStrikethrough() && 
/* 314 */         isUnderlined() == debug2.isUnderlined() && 
/* 315 */         Objects.equals(getClickEvent(), debug2.getClickEvent()) && 
/* 316 */         Objects.equals(getHoverEvent(), debug2.getHoverEvent()) && 
/* 317 */         Objects.equals(getInsertion(), debug2.getInsertion()) && 
/* 318 */         Objects.equals(getFont(), debug2.getFont()));
/*     */     } 
/*     */ 
/*     */     
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 327 */     return Objects.hash(new Object[] { this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion });
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<Style>, JsonSerializer<Style> {
/*     */     @Nullable
/*     */     public Style deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 334 */       if (debug1.isJsonObject()) {
/* 335 */         JsonObject debug4 = debug1.getAsJsonObject();
/* 336 */         if (debug4 == null) {
/* 337 */           return null;
/*     */         }
/*     */         
/* 340 */         Boolean debug5 = getOptionalFlag(debug4, "bold");
/* 341 */         Boolean debug6 = getOptionalFlag(debug4, "italic");
/* 342 */         Boolean debug7 = getOptionalFlag(debug4, "underlined");
/* 343 */         Boolean debug8 = getOptionalFlag(debug4, "strikethrough");
/* 344 */         Boolean debug9 = getOptionalFlag(debug4, "obfuscated");
/* 345 */         TextColor debug10 = getTextColor(debug4);
/* 346 */         String debug11 = getInsertion(debug4);
/* 347 */         ClickEvent debug12 = getClickEvent(debug4);
/* 348 */         HoverEvent debug13 = getHoverEvent(debug4);
/* 349 */         ResourceLocation debug14 = getFont(debug4);
/*     */         
/* 351 */         return new Style(debug10, debug5, debug6, debug7, debug8, debug9, debug12, debug13, debug11, debug14);
/*     */       } 
/*     */       
/* 354 */       return null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static ResourceLocation getFont(JsonObject debug0) {
/* 359 */       if (debug0.has("font")) {
/* 360 */         String debug1 = GsonHelper.getAsString(debug0, "font");
/*     */         try {
/* 362 */           return new ResourceLocation(debug1);
/* 363 */         } catch (ResourceLocationException debug2) {
/* 364 */           throw new JsonSyntaxException("Invalid font name: " + debug1);
/*     */         } 
/*     */       } 
/* 367 */       return null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static HoverEvent getHoverEvent(JsonObject debug0) {
/* 372 */       if (debug0.has("hoverEvent")) {
/* 373 */         JsonObject debug1 = GsonHelper.getAsJsonObject(debug0, "hoverEvent");
/* 374 */         HoverEvent debug2 = HoverEvent.deserialize(debug1);
/* 375 */         if (debug2 != null && debug2.getAction().isAllowedFromServer()) {
/* 376 */           return debug2;
/*     */         }
/*     */       } 
/* 379 */       return null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static ClickEvent getClickEvent(JsonObject debug0) {
/* 384 */       if (debug0.has("clickEvent")) {
/* 385 */         JsonObject debug1 = GsonHelper.getAsJsonObject(debug0, "clickEvent");
/* 386 */         String debug2 = GsonHelper.getAsString(debug1, "action", null);
/* 387 */         ClickEvent.Action debug3 = (debug2 == null) ? null : ClickEvent.Action.getByName(debug2);
/*     */         
/* 389 */         String debug4 = GsonHelper.getAsString(debug1, "value", null);
/*     */         
/* 391 */         if (debug3 != null && debug4 != null && debug3.isAllowedFromServer()) {
/* 392 */           return new ClickEvent(debug3, debug4);
/*     */         }
/*     */       } 
/* 395 */       return null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static String getInsertion(JsonObject debug0) {
/* 400 */       return GsonHelper.getAsString(debug0, "insertion", null);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static TextColor getTextColor(JsonObject debug0) {
/* 405 */       if (debug0.has("color")) {
/* 406 */         String debug1 = GsonHelper.getAsString(debug0, "color");
/* 407 */         return TextColor.parseColor(debug1);
/*     */       } 
/* 409 */       return null;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static Boolean getOptionalFlag(JsonObject debug0, String debug1) {
/* 414 */       if (debug0.has(debug1)) {
/* 415 */         return Boolean.valueOf(debug0.get(debug1).getAsBoolean());
/*     */       }
/*     */       
/* 418 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public JsonElement serialize(Style debug1, Type debug2, JsonSerializationContext debug3) {
/* 424 */       if (debug1.isEmpty()) {
/* 425 */         return null;
/*     */       }
/* 427 */       JsonObject debug4 = new JsonObject();
/*     */       
/* 429 */       if (debug1.bold != null) {
/* 430 */         debug4.addProperty("bold", debug1.bold);
/*     */       }
/* 432 */       if (debug1.italic != null) {
/* 433 */         debug4.addProperty("italic", debug1.italic);
/*     */       }
/* 435 */       if (debug1.underlined != null) {
/* 436 */         debug4.addProperty("underlined", debug1.underlined);
/*     */       }
/* 438 */       if (debug1.strikethrough != null)
/*     */       {
/* 440 */         debug4.addProperty("strikethrough", debug1.strikethrough);
/*     */       }
/* 442 */       if (debug1.obfuscated != null) {
/* 443 */         debug4.addProperty("obfuscated", debug1.obfuscated);
/*     */       }
/* 445 */       if (debug1.color != null) {
/* 446 */         debug4.addProperty("color", debug1.color.serialize());
/*     */       }
/* 448 */       if (debug1.insertion != null) {
/* 449 */         debug4.add("insertion", debug3.serialize(debug1.insertion));
/*     */       }
/*     */       
/* 452 */       if (debug1.clickEvent != null) {
/* 453 */         JsonObject debug5 = new JsonObject();
/* 454 */         debug5.addProperty("action", debug1.clickEvent.getAction().getName());
/* 455 */         debug5.addProperty("value", debug1.clickEvent.getValue());
/* 456 */         debug4.add("clickEvent", (JsonElement)debug5);
/*     */       } 
/*     */       
/* 459 */       if (debug1.hoverEvent != null) {
/* 460 */         debug4.add("hoverEvent", (JsonElement)debug1.hoverEvent.serialize());
/*     */       }
/*     */       
/* 463 */       if (debug1.font != null) {
/* 464 */         debug4.addProperty("font", debug1.font.toString());
/*     */       }
/*     */       
/* 467 */       return (JsonElement)debug4;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\Style.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */