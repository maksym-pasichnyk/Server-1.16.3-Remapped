/*     */ package net.minecraft.stats;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerStatsCounter extends StatsCounter {
/*  38 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private final File file;
/*  43 */   private final Set<Stat<?>> dirty = Sets.newHashSet();
/*  44 */   private int lastStatRequest = -300;
/*     */   
/*     */   public ServerStatsCounter(MinecraftServer debug1, File debug2) {
/*  47 */     this.server = debug1;
/*  48 */     this.file = debug2;
/*  49 */     if (debug2.isFile()) {
/*     */       try {
/*  51 */         parseLocal(debug1.getFixerUpper(), FileUtils.readFileToString(debug2));
/*  52 */       } catch (IOException debug3) {
/*  53 */         LOGGER.error("Couldn't read statistics file {}", debug2, debug3);
/*  54 */       } catch (JsonParseException debug3) {
/*  55 */         LOGGER.error("Couldn't parse statistics file {}", debug2, debug3);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void save() {
/*     */     try {
/*  62 */       FileUtils.writeStringToFile(this.file, toJson());
/*  63 */     } catch (IOException debug1) {
/*  64 */       LOGGER.error("Couldn't save stats", debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Player debug1, Stat<?> debug2, int debug3) {
/*  70 */     super.setValue(debug1, debug2, debug3);
/*  71 */     this.dirty.add(debug2);
/*     */   }
/*     */   
/*     */   private Set<Stat<?>> getDirty() {
/*  75 */     Set<Stat<?>> debug1 = Sets.newHashSet(this.dirty);
/*  76 */     this.dirty.clear();
/*  77 */     return debug1;
/*     */   }
/*     */   
/*     */   public void parseLocal(DataFixer debug1, String debug2) {
/*  81 */     try (JsonReader debug3 = new JsonReader(new StringReader(debug2))) {
/*  82 */       debug3.setLenient(false);
/*  83 */       JsonElement debug5 = Streams.parse(debug3);
/*     */       
/*  85 */       if (debug5.isJsonNull()) {
/*  86 */         LOGGER.error("Unable to parse Stat data from {}", this.file);
/*     */         
/*     */         return;
/*     */       } 
/*  90 */       CompoundTag debug6 = fromJson(debug5.getAsJsonObject());
/*     */       
/*  92 */       if (!debug6.contains("DataVersion", 99)) {
/*  93 */         debug6.putInt("DataVersion", 1343);
/*     */       }
/*  95 */       debug6 = NbtUtils.update(debug1, DataFixTypes.STATS, debug6, debug6.getInt("DataVersion"));
/*     */       
/*  97 */       if (debug6.contains("stats", 10)) {
/*  98 */         CompoundTag debug7 = debug6.getCompound("stats");
/*  99 */         for (String debug9 : debug7.getAllKeys()) {
/* 100 */           if (debug7.contains(debug9, 10)) {
/* 101 */             Util.ifElse(Registry.STAT_TYPE.getOptional(new ResourceLocation(debug9)), debug3 -> {
/*     */                   CompoundTag debug4 = debug1.getCompound(debug2);
/*     */ 
/*     */                   
/*     */                   for (String debug6 : debug4.getAllKeys()) {
/*     */                     if (debug4.contains(debug6, 99)) {
/*     */                       Util.ifElse(getStat(debug3, debug6), (), ());
/*     */ 
/*     */                       
/*     */                       continue;
/*     */                     } 
/*     */                     
/*     */                     LOGGER.warn("Invalid statistic value in {}: Don't know what {} is for key {}", this.file, debug4.get(debug6), debug6);
/*     */                   } 
/*     */                 }() -> LOGGER.warn("Invalid statistic type in {}: Don't know what {} is", this.file, debug1));
/*     */           }
/*     */         } 
/*     */       } 
/* 119 */     } catch (JsonParseException|IOException debug3) {
/* 120 */       LOGGER.error("Unable to parse Stat data from {}", this.file, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Optional<Stat<T>> getStat(StatType<T> debug1, String debug2) {
/* 125 */     return Optional.<ResourceLocation>ofNullable(ResourceLocation.tryParse(debug2))
/* 126 */       .flatMap(debug1.getRegistry()::getOptional)
/* 127 */       .map(debug1::get);
/*     */   }
/*     */   
/*     */   private static CompoundTag fromJson(JsonObject debug0) {
/* 131 */     CompoundTag debug1 = new CompoundTag();
/* 132 */     for (Map.Entry<String, JsonElement> debug3 : (Iterable<Map.Entry<String, JsonElement>>)debug0.entrySet()) {
/* 133 */       JsonElement debug4 = debug3.getValue();
/* 134 */       if (debug4.isJsonObject()) {
/* 135 */         debug1.put(debug3.getKey(), (Tag)fromJson(debug4.getAsJsonObject())); continue;
/* 136 */       }  if (debug4.isJsonPrimitive()) {
/* 137 */         JsonPrimitive debug5 = debug4.getAsJsonPrimitive();
/* 138 */         if (debug5.isNumber()) {
/* 139 */           debug1.putInt(debug3.getKey(), debug5.getAsInt());
/*     */         }
/*     */       } 
/*     */     } 
/* 143 */     return debug1;
/*     */   }
/*     */   
/*     */   protected String toJson() {
/* 147 */     Map<StatType<?>, JsonObject> debug1 = Maps.newHashMap();
/* 148 */     for (ObjectIterator<Object2IntMap.Entry<Stat<?>>> objectIterator = this.stats.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Stat<?>> entry = objectIterator.next();
/* 149 */       Stat<?> debug4 = (Stat)entry.getKey();
/* 150 */       ((JsonObject)debug1.computeIfAbsent(debug4.getType(), debug0 -> new JsonObject())).addProperty(getKey(debug4).toString(), Integer.valueOf(entry.getIntValue())); }
/*     */ 
/*     */     
/* 153 */     JsonObject debug2 = new JsonObject();
/* 154 */     for (Map.Entry<StatType<?>, JsonObject> debug4 : debug1.entrySet()) {
/* 155 */       debug2.add(Registry.STAT_TYPE.getKey(debug4.getKey()).toString(), (JsonElement)debug4.getValue());
/*     */     }
/*     */     
/* 158 */     JsonObject debug3 = new JsonObject();
/* 159 */     debug3.add("stats", (JsonElement)debug2);
/* 160 */     debug3.addProperty("DataVersion", Integer.valueOf(SharedConstants.getCurrentVersion().getWorldVersion()));
/*     */     
/* 162 */     return debug3.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> ResourceLocation getKey(Stat<T> debug0) {
/* 168 */     return debug0.getType().getRegistry().getKey(debug0.getValue());
/*     */   }
/*     */   
/*     */   public void markAllDirty() {
/* 172 */     this.dirty.addAll((Collection<? extends Stat<?>>)this.stats.keySet());
/*     */   }
/*     */   
/*     */   public void sendStats(ServerPlayer debug1) {
/* 176 */     int debug2 = this.server.getTickCount();
/* 177 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 179 */     if (debug2 - this.lastStatRequest > 300) {
/* 180 */       this.lastStatRequest = debug2;
/*     */       
/* 182 */       for (Stat<?> debug5 : getDirty()) {
/* 183 */         object2IntOpenHashMap.put(debug5, getValue(debug5));
/*     */       }
/*     */     } 
/*     */     
/* 187 */     debug1.connection.send((Packet)new ClientboundAwardStatsPacket((Object2IntMap)object2IntOpenHashMap));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\ServerStatsCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */