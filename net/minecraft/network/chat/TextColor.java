/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public final class TextColor {
/*    */   private static final Map<ChatFormatting, TextColor> LEGACY_FORMAT_TO_COLOR;
/*    */   
/*    */   static {
/* 15 */     LEGACY_FORMAT_TO_COLOR = (Map<ChatFormatting, TextColor>)Stream.<ChatFormatting>of(ChatFormatting.values()).filter(ChatFormatting::isColor).collect(ImmutableMap.toImmutableMap(Function.identity(), debug0 -> new TextColor(debug0.getColor().intValue(), debug0.getName())));
/* 16 */     NAMED_COLORS = (Map<String, TextColor>)LEGACY_FORMAT_TO_COLOR.values().stream().collect(ImmutableMap.toImmutableMap(debug0 -> debug0.name, Function.identity()));
/*    */   }
/*    */   private static final Map<String, TextColor> NAMED_COLORS;
/*    */   private final int value;
/*    */   @Nullable
/*    */   private final String name;
/*    */   
/*    */   private TextColor(int debug1, String debug2) {
/* 24 */     this.value = debug1;
/* 25 */     this.name = debug2;
/*    */   }
/*    */   
/*    */   private TextColor(int debug1) {
/* 29 */     this.value = debug1;
/* 30 */     this.name = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String serialize() {
/* 38 */     if (this.name != null) {
/* 39 */       return this.name;
/*    */     }
/*    */     
/* 42 */     return formatValue();
/*    */   }
/*    */   
/*    */   private String formatValue() {
/* 46 */     return String.format("#%06X", new Object[] { Integer.valueOf(this.value) });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 51 */     if (this == debug1) {
/* 52 */       return true;
/*    */     }
/* 54 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 55 */       return false;
/*    */     }
/* 57 */     TextColor debug2 = (TextColor)debug1;
/* 58 */     return (this.value == debug2.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Objects.hash(new Object[] { Integer.valueOf(this.value), this.name });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return (this.name != null) ? this.name : formatValue();
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static TextColor fromLegacyFormat(ChatFormatting debug0) {
/* 73 */     return LEGACY_FORMAT_TO_COLOR.get(debug0);
/*    */   }
/*    */   
/*    */   public static TextColor fromRgb(int debug0) {
/* 77 */     return new TextColor(debug0);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static TextColor parseColor(String debug0) {
/* 82 */     if (debug0.startsWith("#")) {
/*    */       try {
/* 84 */         int debug1 = Integer.parseInt(debug0.substring(1), 16);
/* 85 */         return fromRgb(debug1);
/* 86 */       } catch (NumberFormatException debug1) {
/* 87 */         return null;
/*    */       } 
/*    */     }
/* 90 */     return NAMED_COLORS.get(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\TextColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */