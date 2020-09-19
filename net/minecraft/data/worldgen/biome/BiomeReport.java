/*    */ package net.minecraft.data.worldgen.biome;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.data.BuiltinRegistries;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class BiomeReport implements DataProvider {
/* 26 */   private static final Logger LOGGER = LogManager.getLogger();
/* 27 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*    */   
/*    */   private final DataGenerator generator;
/*    */   
/*    */   public BiomeReport(DataGenerator debug1) {
/* 32 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) {
/* 37 */     Path debug2 = this.generator.getOutputFolder();
/*    */     
/* 39 */     for (Map.Entry<ResourceKey<Biome>, Biome> debug4 : (Iterable<Map.Entry<ResourceKey<Biome>, Biome>>)BuiltinRegistries.BIOME.entrySet()) {
/* 40 */       Path debug5 = createPath(debug2, ((ResourceKey)debug4.getKey()).location());
/* 41 */       Biome debug6 = debug4.getValue();
/* 42 */       Function<Supplier<Biome>, DataResult<JsonElement>> debug7 = JsonOps.INSTANCE.withEncoder((Encoder)Biome.CODEC);
/*    */       try {
/* 44 */         Optional<JsonElement> debug8 = ((DataResult)debug7.apply(() -> debug0)).result();
/* 45 */         if (debug8.isPresent()) {
/* 46 */           DataProvider.save(GSON, debug1, debug8.get(), debug5); continue;
/*    */         } 
/* 48 */         LOGGER.error("Couldn't serialize biome {}", debug5);
/*    */       }
/* 50 */       catch (IOException debug8) {
/* 51 */         LOGGER.error("Couldn't save biome {}", debug5, debug8);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Path createPath(Path debug0, ResourceLocation debug1) {
/* 57 */     return debug0.resolve("reports/biomes/" + debug1.getPath() + ".json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 62 */     return "Biomes";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\biome\BiomeReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */