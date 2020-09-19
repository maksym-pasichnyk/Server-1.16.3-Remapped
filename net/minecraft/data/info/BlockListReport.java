/*    */ package net.minecraft.data.info;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockListReport implements DataProvider {
/* 22 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*    */   private final DataGenerator generator;
/*    */   
/*    */   public BlockListReport(DataGenerator debug1) {
/* 26 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) throws IOException {
/* 31 */     JsonObject debug2 = new JsonObject();
/*    */     
/* 33 */     for (Block debug4 : Registry.BLOCK) {
/* 34 */       ResourceLocation debug5 = Registry.BLOCK.getKey(debug4);
/* 35 */       JsonObject debug6 = new JsonObject();
/* 36 */       StateDefinition<Block, BlockState> debug7 = debug4.getStateDefinition();
/*    */       
/* 38 */       if (!debug7.getProperties().isEmpty()) {
/* 39 */         JsonObject jsonObject = new JsonObject();
/* 40 */         for (Property<?> debug10 : (Iterable<Property<?>>)debug7.getProperties()) {
/* 41 */           JsonArray debug11 = new JsonArray();
/* 42 */           for (Comparable<?> debug13 : (Iterable<Comparable<?>>)debug10.getPossibleValues()) {
/* 43 */             debug11.add(Util.getPropertyName(debug10, debug13));
/*    */           }
/* 45 */           jsonObject.add(debug10.getName(), (JsonElement)debug11);
/*    */         } 
/*    */         
/* 48 */         debug6.add("properties", (JsonElement)jsonObject);
/*    */       } 
/*    */       
/* 51 */       JsonArray debug8 = new JsonArray();
/* 52 */       for (UnmodifiableIterator<BlockState> unmodifiableIterator = debug7.getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { BlockState debug10 = unmodifiableIterator.next();
/* 53 */         JsonObject debug11 = new JsonObject();
/* 54 */         JsonObject debug12 = new JsonObject();
/* 55 */         for (Property<?> debug14 : (Iterable<Property<?>>)debug7.getProperties()) {
/* 56 */           debug12.addProperty(debug14.getName(), Util.getPropertyName(debug14, debug10.getValue(debug14)));
/*    */         }
/* 58 */         if (debug12.size() > 0) {
/* 59 */           debug11.add("properties", (JsonElement)debug12);
/*    */         }
/* 61 */         debug11.addProperty("id", Integer.valueOf(Block.getId(debug10)));
/* 62 */         if (debug10 == debug4.defaultBlockState()) {
/* 63 */           debug11.addProperty("default", Boolean.valueOf(true));
/*    */         }
/* 65 */         debug8.add((JsonElement)debug11); }
/*    */ 
/*    */       
/* 68 */       debug6.add("states", (JsonElement)debug8);
/* 69 */       debug2.add(debug5.toString(), (JsonElement)debug6);
/*    */     } 
/*    */     
/* 72 */     Path debug3 = this.generator.getOutputFolder().resolve("reports/blocks.json");
/* 73 */     DataProvider.save(GSON, debug1, (JsonElement)debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 78 */     return "Block List";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\info\BlockListReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */