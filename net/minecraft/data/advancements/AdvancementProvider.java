/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class AdvancementProvider implements DataProvider {
/* 22 */   private static final Logger LOGGER = LogManager.getLogger();
/* 23 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*    */   
/*    */   private final DataGenerator generator;
/* 26 */   private final List<Consumer<Consumer<Advancement>>> tabs = (List<Consumer<Consumer<Advancement>>>)ImmutableList.of(new TheEndAdvancements(), new HusbandryAdvancements(), new AdventureAdvancements(), new NetherAdvancements(), new StoryAdvancements());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AdvancementProvider(DataGenerator debug1) {
/* 35 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) throws IOException {
/* 40 */     Path debug2 = this.generator.getOutputFolder();
/* 41 */     Set<ResourceLocation> debug3 = Sets.newHashSet();
/* 42 */     Consumer<Advancement> debug4 = debug3 -> {
/*    */         if (!debug0.add(debug3.getId())) {
/*    */           throw new IllegalStateException("Duplicate advancement " + debug3.getId());
/*    */         }
/*    */         
/*    */         Path debug4 = createPath(debug1, debug3);
/*    */         try {
/*    */           DataProvider.save(GSON, debug2, (JsonElement)debug3.deconstruct().serializeToJson(), debug4);
/* 50 */         } catch (IOException debug5) {
/*    */           LOGGER.error("Couldn't save advancement {}", debug4, debug5);
/*    */         } 
/*    */       };
/*    */     
/* 55 */     for (Consumer<Consumer<Advancement>> debug6 : this.tabs) {
/* 56 */       debug6.accept(debug4);
/*    */     }
/*    */   }
/*    */   
/*    */   private static Path createPath(Path debug0, Advancement debug1) {
/* 61 */     return debug0.resolve("data/" + debug1.getId().getNamespace() + "/advancements/" + debug1.getId().getPath() + ".json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 66 */     return "Advancements";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\advancements\AdvancementProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */