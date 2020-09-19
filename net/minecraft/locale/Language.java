/*    */ package net.minecraft.locale;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Language
/*    */ {
/* 28 */   private static final Logger LOGGER = LogManager.getLogger();
/* 29 */   private static final Gson GSON = new Gson();
/*    */   
/* 31 */   private static final Pattern UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
/*    */   
/* 33 */   private static volatile Language instance = loadDefault();
/*    */   
/*    */   private static Language loadDefault() {
/* 36 */     ImmutableMap.Builder<String, String> debug0 = ImmutableMap.builder();
/* 37 */     BiConsumer<String, String> debug1 = debug0::put;
/* 38 */     try (InputStream debug2 = Language.class.getResourceAsStream("/assets/minecraft/lang/en_us.json")) {
/* 39 */       loadFromJson(debug2, debug1);
/* 40 */     } catch (IOException|com.google.gson.JsonParseException debug2) {
/* 41 */       LOGGER.error("Couldn't read strings from /assets/minecraft/lang/en_us.json", debug2);
/*    */     } 
/*    */     
/* 44 */     final ImmutableMap storage = debug0.build();
/* 45 */     return new Language()
/*    */       {
/*    */         public String getOrDefault(String debug1) {
/* 48 */           return (String)storage.getOrDefault(debug1, debug1);
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean has(String debug1) {
/* 53 */           return storage.containsKey(debug1);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void loadFromJson(InputStream debug0, BiConsumer<String, String> debug1) {
/* 72 */     JsonObject debug2 = (JsonObject)GSON.fromJson(new InputStreamReader(debug0, StandardCharsets.UTF_8), JsonObject.class);
/* 73 */     for (Map.Entry<String, JsonElement> debug4 : (Iterable<Map.Entry<String, JsonElement>>)debug2.entrySet()) {
/* 74 */       String debug5 = UNSUPPORTED_FORMAT_PATTERN.matcher(GsonHelper.convertToString(debug4.getValue(), debug4.getKey())).replaceAll("%$1s");
/* 75 */       debug1.accept(debug4.getKey(), debug5);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Language getInstance() {
/* 80 */     return instance;
/*    */   }
/*    */   
/*    */   public abstract String getOrDefault(String paramString);
/*    */   
/*    */   public abstract boolean has(String paramString);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\locale\Language.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */