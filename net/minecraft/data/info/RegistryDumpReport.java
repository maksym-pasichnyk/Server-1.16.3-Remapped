/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.core.DefaultedRegistry;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class RegistryDumpReport
/*    */   implements DataProvider {
/* 18 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*    */   private final DataGenerator generator;
/*    */   
/*    */   public RegistryDumpReport(DataGenerator debug1) {
/* 22 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) throws IOException {
/* 27 */     JsonObject debug2 = new JsonObject();
/*    */     
/* 29 */     Registry.REGISTRY.keySet().forEach(debug1 -> debug0.add(debug1.toString(), dumpRegistry((Registry)Registry.REGISTRY.get(debug1))));
/*    */     
/* 31 */     Path debug3 = this.generator.getOutputFolder().resolve("reports/registries.json");
/* 32 */     DataProvider.save(GSON, debug1, (JsonElement)debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> JsonElement dumpRegistry(Registry<T> debug0) {
/* 37 */     JsonObject debug1 = new JsonObject();
/*    */     
/* 39 */     if (debug0 instanceof DefaultedRegistry) {
/* 40 */       ResourceLocation resourceLocation = ((DefaultedRegistry)debug0).getDefaultKey();
/* 41 */       debug1.addProperty("default", resourceLocation.toString());
/*    */     } 
/*    */     
/* 44 */     int debug2 = Registry.REGISTRY.getId(debug0);
/* 45 */     debug1.addProperty("protocol_id", Integer.valueOf(debug2));
/*    */     
/* 47 */     JsonObject debug3 = new JsonObject();
/* 48 */     for (ResourceLocation debug5 : debug0.keySet()) {
/* 49 */       T debug6 = (T)debug0.get(debug5);
/* 50 */       int debug7 = debug0.getId(debug6);
/*    */       
/* 52 */       JsonObject debug8 = new JsonObject();
/* 53 */       debug8.addProperty("protocol_id", Integer.valueOf(debug7));
/*    */       
/* 55 */       debug3.add(debug5.toString(), (JsonElement)debug8);
/*    */     } 
/* 57 */     debug1.add("entries", (JsonElement)debug3);
/* 58 */     return (JsonElement)debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 63 */     return "Registry Dump";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\info\RegistryDumpReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */