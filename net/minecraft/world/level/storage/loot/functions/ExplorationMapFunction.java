/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ExplorationMapFunction extends LootItemConditionalFunction {
/*  29 */   private static final Logger LOGGER = LogManager.getLogger();
/*  30 */   public static final StructureFeature<?> DEFAULT_FEATURE = StructureFeature.BURIED_TREASURE;
/*     */   
/*  32 */   public static final MapDecoration.Type DEFAULT_DECORATION = MapDecoration.Type.MANSION;
/*     */   
/*     */   private final StructureFeature<?> destination;
/*     */   
/*     */   private final MapDecoration.Type mapDecoration;
/*     */   
/*     */   private final byte zoom;
/*     */   
/*     */   private final int searchRadius;
/*     */   private final boolean skipKnownStructures;
/*     */   
/*     */   private ExplorationMapFunction(LootItemCondition[] debug1, StructureFeature<?> debug2, MapDecoration.Type debug3, byte debug4, int debug5, boolean debug6) {
/*  44 */     super(debug1);
/*  45 */     this.destination = debug2;
/*  46 */     this.mapDecoration = debug3;
/*  47 */     this.zoom = debug4;
/*  48 */     this.searchRadius = debug5;
/*  49 */     this.skipKnownStructures = debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType getType() {
/*  54 */     return LootItemFunctions.EXPLORATION_MAP;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<LootContextParam<?>> getReferencedContextParams() {
/*  59 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.ORIGIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/*  64 */     if (debug1.getItem() != Items.MAP) {
/*  65 */       return debug1;
/*     */     }
/*     */     
/*  68 */     Vec3 debug3 = (Vec3)debug2.getParamOrNull(LootContextParams.ORIGIN);
/*  69 */     if (debug3 != null) {
/*  70 */       ServerLevel debug4 = debug2.getLevel();
/*     */       
/*  72 */       BlockPos debug5 = debug4.findNearestMapFeature(this.destination, new BlockPos(debug3), this.searchRadius, this.skipKnownStructures);
/*  73 */       if (debug5 != null) {
/*  74 */         ItemStack debug6 = MapItem.create((Level)debug4, debug5.getX(), debug5.getZ(), this.zoom, true, true);
/*  75 */         MapItem.renderBiomePreviewMap(debug4, debug6);
/*  76 */         MapItemSavedData.addTargetDecoration(debug6, debug5, "+", this.mapDecoration);
/*  77 */         debug6.setHoverName((Component)new TranslatableComponent("filled_map." + this.destination.getFeatureName().toLowerCase(Locale.ROOT)));
/*  78 */         return debug6;
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     return debug1;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  86 */     private StructureFeature<?> destination = ExplorationMapFunction.DEFAULT_FEATURE;
/*  87 */     private MapDecoration.Type mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
/*  88 */     private byte zoom = 2;
/*  89 */     private int searchRadius = 50;
/*     */     
/*     */     private boolean skipKnownStructures = true;
/*     */     
/*     */     protected Builder getThis() {
/*  94 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDestination(StructureFeature<?> debug1) {
/*  98 */       this.destination = debug1;
/*  99 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMapDecoration(MapDecoration.Type debug1) {
/* 103 */       this.mapDecoration = debug1;
/* 104 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setZoom(byte debug1) {
/* 108 */       this.zoom = debug1;
/* 109 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSkipKnownStructures(boolean debug1) {
/* 118 */       this.skipKnownStructures = debug1;
/* 119 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 124 */       return new ExplorationMapFunction(getConditions(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder makeExplorationMap() {
/* 129 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends LootItemConditionalFunction.Serializer<ExplorationMapFunction> {
/*     */     public void serialize(JsonObject debug1, ExplorationMapFunction debug2, JsonSerializationContext debug3) {
/* 135 */       super.serialize(debug1, debug2, debug3);
/*     */       
/* 137 */       if (!debug2.destination.equals(ExplorationMapFunction.DEFAULT_FEATURE)) {
/* 138 */         debug1.add("destination", debug3.serialize(debug2.destination.getFeatureName()));
/*     */       }
/*     */       
/* 141 */       if (debug2.mapDecoration != ExplorationMapFunction.DEFAULT_DECORATION) {
/* 142 */         debug1.add("decoration", debug3.serialize(debug2.mapDecoration.toString().toLowerCase(Locale.ROOT)));
/*     */       }
/*     */       
/* 145 */       if (debug2.zoom != 2) {
/* 146 */         debug1.addProperty("zoom", Byte.valueOf(debug2.zoom));
/*     */       }
/*     */       
/* 149 */       if (debug2.searchRadius != 50) {
/* 150 */         debug1.addProperty("search_radius", Integer.valueOf(debug2.searchRadius));
/*     */       }
/*     */       
/* 153 */       if (debug2.skipKnownStructures != true) {
/* 154 */         debug1.addProperty("skip_existing_chunks", Boolean.valueOf(debug2.skipKnownStructures));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public ExplorationMapFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 160 */       StructureFeature<?> debug4 = readStructure(debug1);
/*     */       
/* 162 */       String debug5 = debug1.has("decoration") ? GsonHelper.getAsString(debug1, "decoration") : "mansion";
/* 163 */       MapDecoration.Type debug6 = ExplorationMapFunction.DEFAULT_DECORATION;
/*     */       try {
/* 165 */         debug6 = MapDecoration.Type.valueOf(debug5.toUpperCase(Locale.ROOT));
/* 166 */       } catch (IllegalArgumentException illegalArgumentException) {
/* 167 */         ExplorationMapFunction.LOGGER.error("Error while parsing loot table decoration entry. Found {}. Defaulting to " + ExplorationMapFunction.DEFAULT_DECORATION, debug5);
/*     */       } 
/*     */       
/* 170 */       byte debug7 = GsonHelper.getAsByte(debug1, "zoom", (byte)2);
/*     */       
/* 172 */       int debug8 = GsonHelper.getAsInt(debug1, "search_radius", 50);
/*     */       
/* 174 */       boolean debug9 = GsonHelper.getAsBoolean(debug1, "skip_existing_chunks", true);
/*     */       
/* 176 */       return new ExplorationMapFunction(debug3, debug4, debug6, debug7, debug8, debug9);
/*     */     }
/*     */     
/*     */     private static StructureFeature<?> readStructure(JsonObject debug0) {
/* 180 */       if (debug0.has("destination")) {
/* 181 */         String debug1 = GsonHelper.getAsString(debug0, "destination");
/* 182 */         StructureFeature<?> debug2 = (StructureFeature)StructureFeature.STRUCTURES_REGISTRY.get(debug1.toLowerCase(Locale.ROOT));
/* 183 */         if (debug2 != null) {
/* 184 */           return debug2;
/*     */         }
/*     */       } 
/* 187 */       return ExplorationMapFunction.DEFAULT_FEATURE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ExplorationMapFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */