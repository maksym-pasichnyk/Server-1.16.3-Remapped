/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ 
/*     */ public class LevelDataGeneratorOptionsFix extends DataFix {
/*     */   static {
/*  29 */     MAP = (Map<String, String>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("0", "minecraft:ocean");
/*     */           debug0.put("1", "minecraft:plains");
/*     */           debug0.put("2", "minecraft:desert");
/*     */           debug0.put("3", "minecraft:mountains");
/*     */           debug0.put("4", "minecraft:forest");
/*     */           debug0.put("5", "minecraft:taiga");
/*     */           debug0.put("6", "minecraft:swamp");
/*     */           debug0.put("7", "minecraft:river");
/*     */           debug0.put("8", "minecraft:nether");
/*     */           debug0.put("9", "minecraft:the_end");
/*     */           debug0.put("10", "minecraft:frozen_ocean");
/*     */           debug0.put("11", "minecraft:frozen_river");
/*     */           debug0.put("12", "minecraft:snowy_tundra");
/*     */           debug0.put("13", "minecraft:snowy_mountains");
/*     */           debug0.put("14", "minecraft:mushroom_fields");
/*     */           debug0.put("15", "minecraft:mushroom_field_shore");
/*     */           debug0.put("16", "minecraft:beach");
/*     */           debug0.put("17", "minecraft:desert_hills");
/*     */           debug0.put("18", "minecraft:wooded_hills");
/*     */           debug0.put("19", "minecraft:taiga_hills");
/*     */           debug0.put("20", "minecraft:mountain_edge");
/*     */           debug0.put("21", "minecraft:jungle");
/*     */           debug0.put("22", "minecraft:jungle_hills");
/*     */           debug0.put("23", "minecraft:jungle_edge");
/*     */           debug0.put("24", "minecraft:deep_ocean");
/*     */           debug0.put("25", "minecraft:stone_shore");
/*     */           debug0.put("26", "minecraft:snowy_beach");
/*     */           debug0.put("27", "minecraft:birch_forest");
/*     */           debug0.put("28", "minecraft:birch_forest_hills");
/*     */           debug0.put("29", "minecraft:dark_forest");
/*     */           debug0.put("30", "minecraft:snowy_taiga");
/*     */           debug0.put("31", "minecraft:snowy_taiga_hills");
/*     */           debug0.put("32", "minecraft:giant_tree_taiga");
/*     */           debug0.put("33", "minecraft:giant_tree_taiga_hills");
/*     */           debug0.put("34", "minecraft:wooded_mountains");
/*     */           debug0.put("35", "minecraft:savanna");
/*     */           debug0.put("36", "minecraft:savanna_plateau");
/*     */           debug0.put("37", "minecraft:badlands");
/*     */           debug0.put("38", "minecraft:wooded_badlands_plateau");
/*     */           debug0.put("39", "minecraft:badlands_plateau");
/*     */           debug0.put("40", "minecraft:small_end_islands");
/*     */           debug0.put("41", "minecraft:end_midlands");
/*     */           debug0.put("42", "minecraft:end_highlands");
/*     */           debug0.put("43", "minecraft:end_barrens");
/*     */           debug0.put("44", "minecraft:warm_ocean");
/*     */           debug0.put("45", "minecraft:lukewarm_ocean");
/*     */           debug0.put("46", "minecraft:cold_ocean");
/*     */           debug0.put("47", "minecraft:deep_warm_ocean");
/*     */           debug0.put("48", "minecraft:deep_lukewarm_ocean");
/*     */           debug0.put("49", "minecraft:deep_cold_ocean");
/*     */           debug0.put("50", "minecraft:deep_frozen_ocean");
/*     */           debug0.put("127", "minecraft:the_void");
/*     */           debug0.put("129", "minecraft:sunflower_plains");
/*     */           debug0.put("130", "minecraft:desert_lakes");
/*     */           debug0.put("131", "minecraft:gravelly_mountains");
/*     */           debug0.put("132", "minecraft:flower_forest");
/*     */           debug0.put("133", "minecraft:taiga_mountains");
/*     */           debug0.put("134", "minecraft:swamp_hills");
/*     */           debug0.put("140", "minecraft:ice_spikes");
/*     */           debug0.put("149", "minecraft:modified_jungle");
/*     */           debug0.put("151", "minecraft:modified_jungle_edge");
/*     */           debug0.put("155", "minecraft:tall_birch_forest");
/*     */           debug0.put("156", "minecraft:tall_birch_hills");
/*     */           debug0.put("157", "minecraft:dark_forest_hills");
/*     */           debug0.put("158", "minecraft:snowy_taiga_mountains");
/*     */           debug0.put("160", "minecraft:giant_spruce_taiga");
/*     */           debug0.put("161", "minecraft:giant_spruce_taiga_hills");
/*     */           debug0.put("162", "minecraft:modified_gravelly_mountains");
/*     */           debug0.put("163", "minecraft:shattered_savanna");
/*     */           debug0.put("164", "minecraft:shattered_savanna_plateau");
/*     */           debug0.put("165", "minecraft:eroded_badlands");
/*     */           debug0.put("166", "minecraft:modified_wooded_badlands_plateau");
/*     */           debug0.put("167", "minecraft:modified_badlands_plateau");
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static final Map<String, String> MAP;
/*     */   
/*     */   public LevelDataGeneratorOptionsFix(Schema debug1, boolean debug2) {
/* 110 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 115 */     Type<?> debug1 = getOutputSchema().getType(References.LEVEL);
/* 116 */     return fixTypeEverywhereTyped("LevelDataGeneratorOptionsFix", getInputSchema().getType(References.LEVEL), debug1, debug1 -> (Typed)debug1.write().flatMap(()).map(Pair::getFirst).result().orElseThrow(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> convert(String debug0, DynamicOps<T> debug1) {
/*     */     List<Pair<Integer, String>> debug3;
/* 135 */     Iterator<String> debug2 = Splitter.on(';').split(debug0).iterator();
/*     */ 
/*     */     
/* 138 */     String debug4 = "minecraft:plains";
/* 139 */     Map<String, Map<String, String>> debug5 = Maps.newHashMap();
/*     */     
/* 141 */     if (!debug0.isEmpty() && debug2.hasNext()) {
/* 142 */       debug3 = getLayersInfoFromString(debug2.next());
/*     */       
/* 144 */       if (!debug3.isEmpty()) {
/* 145 */         if (debug2.hasNext()) {
/* 146 */           debug4 = MAP.getOrDefault(debug2.next(), "minecraft:plains");
/*     */         }
/*     */         
/* 149 */         if (debug2.hasNext()) {
/* 150 */           String[] arrayOfString = ((String)debug2.next()).toLowerCase(Locale.ROOT).split(",");
/*     */           
/* 152 */           for (String debug10 : arrayOfString) {
/* 153 */             String[] debug11 = debug10.split("\\(", 2);
/*     */             
/* 155 */             if (!debug11[0].isEmpty()) {
/* 156 */               debug5.put(debug11[0], Maps.newHashMap());
/*     */               
/* 158 */               if (debug11.length > 1 && debug11[1].endsWith(")") && debug11[1].length() > 1) {
/* 159 */                 String[] debug12 = debug11[1].substring(0, debug11[1].length() - 1).split(" ");
/*     */                 
/* 161 */                 for (String debug16 : debug12) {
/* 162 */                   String[] debug17 = debug16.split("=", 2);
/* 163 */                   if (debug17.length == 2) {
/* 164 */                     ((Map<String, String>)debug5.get(debug11[0])).put(debug17[0], debug17[1]);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } else {
/* 171 */           debug5.put("village", Maps.newHashMap());
/*     */         } 
/*     */       } 
/*     */     } else {
/* 175 */       debug3 = Lists.newArrayList();
/* 176 */       debug3.add(Pair.of(Integer.valueOf(1), "minecraft:bedrock"));
/* 177 */       debug3.add(Pair.of(Integer.valueOf(2), "minecraft:dirt"));
/* 178 */       debug3.add(Pair.of(Integer.valueOf(1), "minecraft:grass_block"));
/* 179 */       debug5.put("village", Maps.newHashMap());
/*     */     } 
/*     */     
/* 182 */     T debug6 = (T)debug1.createList(debug3.stream().map(debug1 -> debug0.createMap((Map)ImmutableMap.of(debug0.createString("height"), debug0.createInt(((Integer)debug1.getFirst()).intValue()), debug0.createString("block"), debug0.createString((String)debug1.getSecond())))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     T debug7 = (T)debug1.createMap((Map)debug5.entrySet().stream().map(debug1 -> Pair.of(debug0.createString(((String)debug1.getKey()).toLowerCase(Locale.ROOT)), debug0.createMap((Map)((Map)debug1.getValue()).entrySet().stream().map(()).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 194 */         .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
/*     */     
/* 196 */     return new Dynamic(debug1, debug1.createMap((Map)ImmutableMap.of(debug1
/* 197 */             .createString("layers"), debug6, debug1
/* 198 */             .createString("biome"), debug1.createString(debug4), debug1
/* 199 */             .createString("structures"), debug7)));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Pair<Integer, String> getLayerInfoFromString(String debug0) {
/*     */     int debug2;
/* 205 */     String[] debug1 = debug0.split("\\*", 2);
/*     */ 
/*     */     
/* 208 */     if (debug1.length == 2) {
/*     */       try {
/* 210 */         debug2 = Integer.parseInt(debug1[0]);
/* 211 */       } catch (NumberFormatException numberFormatException) {
/* 212 */         return null;
/*     */       } 
/*     */     } else {
/* 215 */       debug2 = 1;
/*     */     } 
/*     */     
/* 218 */     String debug3 = debug1[debug1.length - 1];
/* 219 */     return Pair.of(Integer.valueOf(debug2), debug3);
/*     */   }
/*     */   
/*     */   private static List<Pair<Integer, String>> getLayersInfoFromString(String debug0) {
/* 223 */     List<Pair<Integer, String>> debug1 = Lists.newArrayList();
/* 224 */     String[] debug2 = debug0.split(",");
/*     */     
/* 226 */     for (String debug6 : debug2) {
/* 227 */       Pair<Integer, String> debug7 = getLayerInfoFromString(debug6);
/* 228 */       if (debug7 == null) {
/* 229 */         return Collections.emptyList();
/*     */       }
/* 231 */       debug1.add(debug7);
/*     */     } 
/*     */     
/* 234 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\LevelDataGeneratorOptionsFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */