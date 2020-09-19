/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class ResourceLocation implements Comparable<ResourceLocation> {
/*  24 */   public static final Codec<ResourceLocation> CODEC = Codec.STRING.comapFlatMap(ResourceLocation::read, ResourceLocation::toString).stable();
/*  25 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.id.invalid"));
/*     */ 
/*     */   
/*     */   protected final String namespace;
/*     */ 
/*     */   
/*     */   protected final String path;
/*     */ 
/*     */   
/*     */   protected ResourceLocation(String[] debug1) {
/*  35 */     this.namespace = StringUtils.isEmpty(debug1[0]) ? "minecraft" : debug1[0];
/*  36 */     this.path = debug1[1];
/*  37 */     if (!isValidNamespace(this.namespace)) {
/*  38 */       throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ':' + this.path);
/*     */     }
/*  40 */     if (!isValidPath(this.path)) {
/*  41 */       throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ':' + this.path);
/*     */     }
/*     */   }
/*     */   
/*     */   public ResourceLocation(String debug1) {
/*  46 */     this(decompose(debug1, ':'));
/*     */   }
/*     */   
/*     */   public ResourceLocation(String debug1, String debug2) {
/*  50 */     this(new String[] { debug1, debug2 });
/*     */   }
/*     */   
/*     */   public static ResourceLocation of(String debug0, char debug1) {
/*  54 */     return new ResourceLocation(decompose(debug0, debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static ResourceLocation tryParse(String debug0) {
/*     */     try {
/*  60 */       return new ResourceLocation(debug0);
/*  61 */     } catch (ResourceLocationException debug1) {
/*  62 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static String[] decompose(String debug0, char debug1) {
/*  67 */     String[] debug2 = { "minecraft", debug0 };
/*  68 */     int debug3 = debug0.indexOf(debug1);
/*  69 */     if (debug3 >= 0) {
/*  70 */       debug2[1] = debug0.substring(debug3 + 1, debug0.length());
/*  71 */       if (debug3 >= 1) {
/*  72 */         debug2[0] = debug0.substring(0, debug3);
/*     */       }
/*     */     } 
/*     */     
/*  76 */     return debug2;
/*     */   }
/*     */   
/*     */   private static DataResult<ResourceLocation> read(String debug0) {
/*     */     try {
/*  81 */       return DataResult.success(new ResourceLocation(debug0));
/*  82 */     } catch (ResourceLocationException debug1) {
/*  83 */       return DataResult.error("Not a valid resource location: " + debug0 + " " + debug1.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getPath() {
/*  88 */     return this.path;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/*  92 */     return this.namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  97 */     return this.namespace + ':' + this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 102 */     if (this == debug1) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     if (debug1 instanceof ResourceLocation) {
/* 107 */       ResourceLocation debug2 = (ResourceLocation)debug1;
/*     */       
/* 109 */       return (this.namespace.equals(debug2.namespace) && this.path.equals(debug2.path));
/*     */     } 
/*     */     
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     return 31 * this.namespace.hashCode() + this.path.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(ResourceLocation debug1) {
/* 123 */     int debug2 = this.path.compareTo(debug1.path);
/* 124 */     if (debug2 == 0) {
/* 125 */       debug2 = this.namespace.compareTo(debug1.namespace);
/*     */     }
/* 127 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation>
/*     */   {
/*     */     public ResourceLocation deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 137 */       return new ResourceLocation(GsonHelper.convertToString(debug1, "location"));
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement serialize(ResourceLocation debug1, Type debug2, JsonSerializationContext debug3) {
/* 142 */       return (JsonElement)new JsonPrimitive(debug1.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public static ResourceLocation read(StringReader debug0) throws CommandSyntaxException {
/* 147 */     int debug1 = debug0.getCursor();
/* 148 */     while (debug0.canRead() && isAllowedInResourceLocation(debug0.peek())) {
/* 149 */       debug0.skip();
/*     */     }
/* 151 */     String debug2 = debug0.getString().substring(debug1, debug0.getCursor());
/*     */     try {
/* 153 */       return new ResourceLocation(debug2);
/* 154 */     } catch (ResourceLocationException debug3) {
/* 155 */       debug0.setCursor(debug1);
/* 156 */       throw ERROR_INVALID.createWithContext(debug0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAllowedInResourceLocation(char debug0) {
/* 161 */     return ((debug0 >= '0' && debug0 <= '9') || (debug0 >= 'a' && debug0 <= 'z') || debug0 == '_' || debug0 == ':' || debug0 == '/' || debug0 == '.' || debug0 == '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidPath(String debug0) {
/* 169 */     for (int debug1 = 0; debug1 < debug0.length(); debug1++) {
/* 170 */       if (!validPathChar(debug0.charAt(debug1))) {
/* 171 */         return false;
/*     */       }
/*     */     } 
/* 174 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isValidNamespace(String debug0) {
/* 178 */     for (int debug1 = 0; debug1 < debug0.length(); debug1++) {
/* 179 */       if (!validNamespaceChar(debug0.charAt(debug1))) {
/* 180 */         return false;
/*     */       }
/*     */     } 
/* 183 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean validPathChar(char debug0) {
/* 187 */     return (debug0 == '_' || debug0 == '-' || (debug0 >= 'a' && debug0 <= 'z') || (debug0 >= '0' && debug0 <= '9') || debug0 == '/' || debug0 == '.');
/*     */   }
/*     */   
/*     */   private static boolean validNamespaceChar(char debug0) {
/* 191 */     return (debug0 == '_' || debug0 == '-' || (debug0 >= 'a' && debug0 <= 'z') || (debug0 >= '0' && debug0 <= '9') || debug0 == '.');
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\ResourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */