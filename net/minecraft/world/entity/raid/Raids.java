/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raids
/*     */   extends SavedData
/*     */ {
/*  35 */   private final Map<Integer, Raid> raidMap = Maps.newHashMap();
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private int nextAvailableID;
/*     */   private int tick;
/*     */   
/*     */   public Raids(ServerLevel debug1) {
/*  43 */     super(getFileId(debug1.dimensionType()));
/*  44 */     this.level = debug1;
/*  45 */     this.nextAvailableID = 1;
/*  46 */     setDirty();
/*     */   }
/*     */   
/*     */   public Raid get(int debug1) {
/*  50 */     return this.raidMap.get(Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  54 */     this.tick++;
/*  55 */     Iterator<Raid> debug1 = this.raidMap.values().iterator();
/*     */     
/*  57 */     while (debug1.hasNext()) {
/*  58 */       Raid debug2 = debug1.next();
/*  59 */       if (this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
/*  60 */         debug2.stop();
/*     */       }
/*  62 */       if (debug2.isStopped()) {
/*  63 */         debug1.remove();
/*  64 */         setDirty();
/*     */         
/*     */         continue;
/*     */       } 
/*  68 */       debug2.tick();
/*     */     } 
/*     */ 
/*     */     
/*  72 */     if (this.tick % 200 == 0) {
/*  73 */       setDirty();
/*     */     }
/*     */     
/*  76 */     DebugPackets.sendRaids(this.level, this.raidMap.values());
/*     */   }
/*     */   
/*     */   public static boolean canJoinRaid(Raider debug0, Raid debug1) {
/*  80 */     if (debug0 != null && debug1 != null && debug1.getLevel() != null) {
/*  81 */       return (debug0.isAlive() && debug0.canJoinRaid() && debug0.getNoActionTime() <= 2400 && debug0.level.dimensionType() == debug1.getLevel().dimensionType());
/*     */     }
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Raid createOrExtendRaid(ServerPlayer debug1) {
/*     */     BlockPos debug4;
/*  92 */     if (debug1.isSpectator()) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     DimensionType debug2 = debug1.level.dimensionType();
/* 101 */     if (!debug2.hasRaids()) {
/* 102 */       return null;
/*     */     }
/*     */     
/* 105 */     BlockPos debug3 = debug1.blockPosition();
/*     */ 
/*     */ 
/*     */     
/* 109 */     List<PoiRecord> debug5 = (List<PoiRecord>)this.level.getPoiManager().getInRange(PoiType.ALL, debug3, 64, PoiManager.Occupancy.IS_OCCUPIED).collect(Collectors.toList());
/* 110 */     int debug6 = 0;
/* 111 */     Vec3 debug7 = Vec3.ZERO;
/* 112 */     for (PoiRecord poiRecord : debug5) {
/* 113 */       BlockPos debug10 = poiRecord.getPos();
/* 114 */       debug7 = debug7.add(debug10.getX(), debug10.getY(), debug10.getZ());
/* 115 */       debug6++;
/*     */     } 
/*     */     
/* 118 */     if (debug6 > 0) {
/*     */       
/* 120 */       debug7 = debug7.scale(1.0D / debug6);
/* 121 */       debug4 = new BlockPos(debug7);
/*     */     } else {
/*     */       
/* 124 */       debug4 = debug3;
/*     */     } 
/*     */     
/* 127 */     Raid debug8 = getOrCreateRaid(debug1.getLevel(), debug4);
/*     */     
/* 129 */     boolean debug9 = false;
/* 130 */     if (!debug8.isStarted()) {
/* 131 */       if (!this.raidMap.containsKey(Integer.valueOf(debug8.getId()))) {
/* 132 */         this.raidMap.put(Integer.valueOf(debug8.getId()), debug8);
/*     */       }
/* 134 */       debug9 = true;
/*     */     }
/* 136 */     else if (debug8.getBadOmenLevel() < debug8.getMaxBadOmenLevel()) {
/* 137 */       debug9 = true;
/*     */     } else {
/*     */       
/* 140 */       debug1.removeEffect(MobEffects.BAD_OMEN);
/* 141 */       debug1.connection.send((Packet)new ClientboundEntityEventPacket((Entity)debug1, (byte)43));
/*     */     } 
/*     */ 
/*     */     
/* 145 */     if (debug9) {
/* 146 */       debug8.absorbBadOmen((Player)debug1);
/* 147 */       debug1.connection.send((Packet)new ClientboundEntityEventPacket((Entity)debug1, (byte)43));
/*     */       
/* 149 */       if (!debug8.hasFirstWaveSpawned()) {
/* 150 */         debug1.awardStat(Stats.RAID_TRIGGER);
/* 151 */         CriteriaTriggers.BAD_OMEN.trigger(debug1);
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     setDirty();
/*     */     
/* 157 */     return debug8;
/*     */   }
/*     */   
/*     */   private Raid getOrCreateRaid(ServerLevel debug1, BlockPos debug2) {
/* 161 */     Raid debug3 = debug1.getRaidAt(debug2);
/* 162 */     return (debug3 != null) ? debug3 : new Raid(getUniqueId(), debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(CompoundTag debug1) {
/* 167 */     this.nextAvailableID = debug1.getInt("NextAvailableID");
/* 168 */     this.tick = debug1.getInt("Tick");
/*     */     
/* 170 */     ListTag debug2 = debug1.getList("Raids", 10);
/* 171 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 172 */       CompoundTag debug4 = debug2.getCompound(debug3);
/* 173 */       Raid debug5 = new Raid(this.level, debug4);
/* 174 */       this.raidMap.put(Integer.valueOf(debug5.getId()), debug5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 180 */     debug1.putInt("NextAvailableID", this.nextAvailableID);
/* 181 */     debug1.putInt("Tick", this.tick);
/*     */     
/* 183 */     ListTag debug2 = new ListTag();
/* 184 */     for (Raid debug4 : this.raidMap.values()) {
/* 185 */       CompoundTag debug5 = new CompoundTag();
/* 186 */       debug4.save(debug5);
/* 187 */       debug2.add(debug5);
/*     */     } 
/* 189 */     debug1.put("Raids", (Tag)debug2);
/* 190 */     return debug1;
/*     */   }
/*     */   
/*     */   public static String getFileId(DimensionType debug0) {
/* 194 */     return "raids" + debug0.getFileSuffix();
/*     */   }
/*     */   
/*     */   private int getUniqueId() {
/* 198 */     return ++this.nextAvailableID;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Raid getNearbyRaid(BlockPos debug1, int debug2) {
/* 203 */     Raid debug3 = null;
/* 204 */     double debug4 = debug2;
/* 205 */     for (Raid debug7 : this.raidMap.values()) {
/* 206 */       double debug8 = debug7.getCenter().distSqr((Vec3i)debug1);
/* 207 */       if (!debug7.isActive()) {
/*     */         continue;
/*     */       }
/* 210 */       if (debug8 < debug4) {
/* 211 */         debug3 = debug7;
/* 212 */         debug4 = debug8;
/*     */       } 
/*     */     } 
/* 215 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\raid\Raids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */