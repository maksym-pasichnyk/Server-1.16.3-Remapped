/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementList;
/*    */ import net.minecraft.advancements.TreeNodePosition;
/*    */ import net.minecraft.advancements.critereon.DeserializationContext;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ import net.minecraft.world.level.storage.loot.PredicateManager;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ServerAdvancementManager
/*    */   extends SimpleJsonResourceReloadListener {
/* 27 */   private static final Logger LOGGER = LogManager.getLogger();
/* 28 */   private static final Gson GSON = (new GsonBuilder()).create();
/*    */   
/* 30 */   private AdvancementList advancements = new AdvancementList();
/*    */   private final PredicateManager predicateManager;
/*    */   
/*    */   public ServerAdvancementManager(PredicateManager debug1) {
/* 34 */     super(GSON, "advancements");
/* 35 */     this.predicateManager = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(Map<ResourceLocation, JsonElement> debug1, ResourceManager debug2, ProfilerFiller debug3) {
/* 40 */     Map<ResourceLocation, Advancement.Builder> debug4 = Maps.newHashMap();
/*    */     
/* 42 */     debug1.forEach((debug2, debug3) -> {
/*    */           try {
/*    */             JsonObject debug4 = GsonHelper.convertToJsonObject(debug3, "advancement");
/*    */             Advancement.Builder debug5 = Advancement.Builder.fromJson(debug4, new DeserializationContext(debug2, this.predicateManager));
/*    */             debug1.put(debug2, debug5);
/* 47 */           } catch (JsonParseException|IllegalArgumentException debug4) {
/*    */             LOGGER.error("Parsing error loading custom advancement {}: {}", debug2, debug4.getMessage());
/*    */           } 
/*    */         });
/*    */     
/* 52 */     AdvancementList debug5 = new AdvancementList();
/* 53 */     debug5.add(debug4);
/*    */     
/* 55 */     for (Advancement debug7 : debug5.getRoots()) {
/* 56 */       if (debug7.getDisplay() != null) {
/* 57 */         TreeNodePosition.run(debug7);
/*    */       }
/*    */     } 
/*    */     
/* 61 */     this.advancements = debug5;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Advancement getAdvancement(ResourceLocation debug1) {
/* 66 */     return this.advancements.get(debug1);
/*    */   }
/*    */   
/*    */   public Collection<Advancement> getAllAdvancements() {
/* 70 */     return this.advancements.getAllAdvancements();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\ServerAdvancementManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */