/*    */ package net.minecraft.server.packs.metadata.pack;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class PackMetadataSectionSerializer implements MetadataSectionSerializer<PackMetadataSection> {
/*    */   public PackMetadataSection fromJson(JsonObject debug1) {
/* 12 */     MutableComponent mutableComponent = Component.Serializer.fromJson(debug1.get("description"));
/* 13 */     if (mutableComponent == null) {
/* 14 */       throw new JsonParseException("Invalid/missing description!");
/*    */     }
/* 16 */     int debug3 = GsonHelper.getAsInt(debug1, "pack_format");
/* 17 */     return new PackMetadataSection((Component)mutableComponent, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMetadataSectionName() {
/* 22 */     return "pack";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\metadata\pack\PackMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */