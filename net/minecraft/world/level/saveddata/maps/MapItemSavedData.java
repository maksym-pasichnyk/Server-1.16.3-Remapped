/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class MapItemSavedData
/*     */   extends SavedData {
/*  36 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   public int x;
/*     */   public int z;
/*     */   public ResourceKey<Level> dimension;
/*     */   public boolean trackingPosition;
/*     */   public boolean unlimitedTracking;
/*     */   public byte scale;
/*     */   
/*     */   public class HoldingPlayer {
/*     */     public final Player player;
/*     */     private boolean dirtyData = true;
/*  47 */     private int maxDirtyX = 127; private int minDirtyX; private int minDirtyY;
/*  48 */     private int maxDirtyY = 127;
/*     */     private int tick;
/*     */     public int step;
/*     */     
/*     */     public HoldingPlayer(Player debug2) {
/*  53 */       this.player = debug2;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Packet<?> nextUpdatePacket(ItemStack debug1) {
/*  58 */       if (this.dirtyData) {
/*  59 */         this.dirtyData = false;
/*  60 */         return (Packet<?>)new ClientboundMapItemDataPacket(MapItem.getMapId(debug1), MapItemSavedData.this.scale, MapItemSavedData.this.trackingPosition, MapItemSavedData.this.locked, MapItemSavedData.this.decorations.values(), MapItemSavedData.this.colors, this.minDirtyX, this.minDirtyY, this.maxDirtyX + 1 - this.minDirtyX, this.maxDirtyY + 1 - this.minDirtyY);
/*  61 */       }  if (this.tick++ % 5 == 0) {
/*  62 */         return (Packet<?>)new ClientboundMapItemDataPacket(MapItem.getMapId(debug1), MapItemSavedData.this.scale, MapItemSavedData.this.trackingPosition, MapItemSavedData.this.locked, MapItemSavedData.this.decorations.values(), MapItemSavedData.this.colors, 0, 0, 0, 0);
/*     */       }
/*     */       
/*  65 */       return null;
/*     */     }
/*     */     
/*     */     public void markDirty(int debug1, int debug2) {
/*  69 */       if (this.dirtyData) {
/*  70 */         this.minDirtyX = Math.min(this.minDirtyX, debug1);
/*  71 */         this.minDirtyY = Math.min(this.minDirtyY, debug2);
/*  72 */         this.maxDirtyX = Math.max(this.maxDirtyX, debug1);
/*  73 */         this.maxDirtyY = Math.max(this.maxDirtyY, debug2);
/*     */       } else {
/*  75 */         this.dirtyData = true;
/*  76 */         this.minDirtyX = debug1;
/*  77 */         this.minDirtyY = debug2;
/*  78 */         this.maxDirtyX = debug1;
/*  79 */         this.maxDirtyY = debug2;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public byte[] colors = new byte[16384];
/*     */   public boolean locked;
/*  93 */   public final List<HoldingPlayer> carriedBy = Lists.newArrayList();
/*  94 */   private final Map<Player, HoldingPlayer> carriedByPlayers = Maps.newHashMap();
/*  95 */   private final Map<String, MapBanner> bannerMarkers = Maps.newHashMap();
/*  96 */   public final Map<String, MapDecoration> decorations = Maps.newLinkedHashMap();
/*  97 */   private final Map<String, MapFrame> frameMarkers = Maps.newHashMap();
/*     */   
/*     */   public MapItemSavedData(String debug1) {
/* 100 */     super(debug1);
/*     */   }
/*     */   
/*     */   public void setProperties(int debug1, int debug2, int debug3, boolean debug4, boolean debug5, ResourceKey<Level> debug6) {
/* 104 */     this.scale = (byte)debug3;
/* 105 */     setOrigin(debug1, debug2, this.scale);
/* 106 */     this.dimension = debug6;
/* 107 */     this.trackingPosition = debug4;
/* 108 */     this.unlimitedTracking = debug5;
/*     */     
/* 110 */     setDirty();
/*     */   }
/*     */   
/*     */   public void setOrigin(double debug1, double debug3, int debug5) {
/* 114 */     int debug6 = 128 * (1 << debug5);
/*     */     
/* 116 */     int debug7 = Mth.floor((debug1 + 64.0D) / debug6);
/* 117 */     int debug8 = Mth.floor((debug3 + 64.0D) / debug6);
/*     */     
/* 119 */     this.x = debug7 * debug6 + debug6 / 2 - 64;
/* 120 */     this.z = debug8 * debug6 + debug6 / 2 - 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(CompoundTag debug1) {
/* 125 */     this
/*     */       
/* 127 */       .dimension = (ResourceKey<Level>)DimensionType.parseLegacy(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug1.get("dimension"))).resultOrPartial(LOGGER::error).orElseThrow(() -> new IllegalArgumentException("Invalid map dimension: " + debug0.get("dimension")));
/*     */     
/* 129 */     this.x = debug1.getInt("xCenter");
/* 130 */     this.z = debug1.getInt("zCenter");
/* 131 */     this.scale = (byte)Mth.clamp(debug1.getByte("scale"), 0, 4);
/*     */     
/* 133 */     this.trackingPosition = (!debug1.contains("trackingPosition", 1) || debug1.getBoolean("trackingPosition"));
/* 134 */     this.unlimitedTracking = debug1.getBoolean("unlimitedTracking");
/*     */     
/* 136 */     this.locked = debug1.getBoolean("locked");
/*     */     
/* 138 */     this.colors = debug1.getByteArray("colors");
/* 139 */     if (this.colors.length != 16384) {
/* 140 */       this.colors = new byte[16384];
/*     */     }
/*     */     
/* 143 */     ListTag debug2 = debug1.getList("banners", 10);
/* 144 */     for (int i = 0; i < debug2.size(); i++) {
/* 145 */       MapBanner mapBanner = MapBanner.load(debug2.getCompound(i));
/* 146 */       this.bannerMarkers.put(mapBanner.getId(), mapBanner);
/* 147 */       addDecoration(mapBanner.getDecoration(), null, mapBanner.getId(), mapBanner.getPos().getX(), mapBanner.getPos().getZ(), 180.0D, mapBanner.getName());
/*     */     } 
/*     */     
/* 150 */     ListTag debug3 = debug1.getList("frames", 10);
/* 151 */     for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 152 */       MapFrame debug5 = MapFrame.load(debug3.getCompound(debug4));
/* 153 */       this.frameMarkers.put(debug5.getId(), debug5);
/* 154 */       addDecoration(MapDecoration.Type.FRAME, null, "frame-" + debug5.getEntityId(), debug5.getPos().getX(), debug5.getPos().getZ(), debug5.getRotation(), null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 160 */     ResourceLocation.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.dimension.location()).resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.put("dimension", debug1));
/* 161 */     debug1.putInt("xCenter", this.x);
/* 162 */     debug1.putInt("zCenter", this.z);
/* 163 */     debug1.putByte("scale", this.scale);
/* 164 */     debug1.putByteArray("colors", this.colors);
/* 165 */     debug1.putBoolean("trackingPosition", this.trackingPosition);
/* 166 */     debug1.putBoolean("unlimitedTracking", this.unlimitedTracking);
/* 167 */     debug1.putBoolean("locked", this.locked);
/*     */     
/* 169 */     ListTag debug2 = new ListTag();
/* 170 */     for (MapBanner debug4 : this.bannerMarkers.values()) {
/* 171 */       debug2.add(debug4.save());
/*     */     }
/* 173 */     debug1.put("banners", (Tag)debug2);
/*     */     
/* 175 */     ListTag debug3 = new ListTag();
/* 176 */     for (MapFrame debug5 : this.frameMarkers.values()) {
/* 177 */       debug3.add(debug5.save());
/*     */     }
/* 179 */     debug1.put("frames", (Tag)debug3);
/*     */     
/* 181 */     return debug1;
/*     */   }
/*     */   
/*     */   public void lockData(MapItemSavedData debug1) {
/* 185 */     this.locked = true;
/* 186 */     this.x = debug1.x;
/* 187 */     this.z = debug1.z;
/* 188 */     this.bannerMarkers.putAll(debug1.bannerMarkers);
/* 189 */     this.decorations.putAll(debug1.decorations);
/* 190 */     System.arraycopy(debug1.colors, 0, this.colors, 0, debug1.colors.length);
/* 191 */     setDirty();
/*     */   }
/*     */   
/*     */   public void tickCarriedBy(Player debug1, ItemStack debug2) {
/* 195 */     if (!this.carriedByPlayers.containsKey(debug1)) {
/* 196 */       HoldingPlayer holdingPlayer = new HoldingPlayer(debug1);
/* 197 */       this.carriedByPlayers.put(debug1, holdingPlayer);
/* 198 */       this.carriedBy.add(holdingPlayer);
/*     */     } 
/*     */     
/* 201 */     if (!debug1.inventory.contains(debug2)) {
/* 202 */       this.decorations.remove(debug1.getName().getString());
/*     */     }
/*     */     
/* 205 */     for (int i = 0; i < this.carriedBy.size(); i++) {
/* 206 */       HoldingPlayer debug4 = this.carriedBy.get(i);
/* 207 */       String debug5 = debug4.player.getName().getString();
/*     */       
/* 209 */       if (debug4.player.removed || (!debug4.player.inventory.contains(debug2) && !debug2.isFramed())) {
/* 210 */         this.carriedByPlayers.remove(debug4.player);
/* 211 */         this.carriedBy.remove(debug4);
/* 212 */         this.decorations.remove(debug5);
/* 213 */       } else if (!debug2.isFramed() && debug4.player.level.dimension() == this.dimension && this.trackingPosition) {
/* 214 */         addDecoration(MapDecoration.Type.PLAYER, (LevelAccessor)debug4.player.level, debug5, debug4.player.getX(), debug4.player.getZ(), debug4.player.yRot, null);
/*     */       } 
/*     */     } 
/*     */     
/* 218 */     if (debug2.isFramed() && this.trackingPosition) {
/* 219 */       ItemFrame itemFrame = debug2.getFrame();
/* 220 */       BlockPos debug4 = itemFrame.getPos();
/* 221 */       MapFrame debug5 = this.frameMarkers.get(MapFrame.frameId(debug4));
/*     */ 
/*     */       
/* 224 */       if (debug5 != null && itemFrame.getId() != debug5.getEntityId() && this.frameMarkers.containsKey(debug5.getId())) {
/* 225 */         this.decorations.remove("frame-" + debug5.getEntityId());
/*     */       }
/* 227 */       MapFrame debug6 = new MapFrame(debug4, itemFrame.getDirection().get2DDataValue() * 90, itemFrame.getId());
/* 228 */       addDecoration(MapDecoration.Type.FRAME, (LevelAccessor)debug1.level, "frame-" + itemFrame.getId(), debug4.getX(), debug4.getZ(), (itemFrame.getDirection().get2DDataValue() * 90), null);
/* 229 */       this.frameMarkers.put(debug6.getId(), debug6);
/*     */     } 
/*     */     
/* 232 */     CompoundTag debug3 = debug2.getTag();
/* 233 */     if (debug3 != null && debug3.contains("Decorations", 9)) {
/* 234 */       ListTag debug4 = debug3.getList("Decorations", 10);
/* 235 */       for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/* 236 */         CompoundTag debug6 = debug4.getCompound(debug5);
/* 237 */         if (!this.decorations.containsKey(debug6.getString("id"))) {
/* 238 */           addDecoration(MapDecoration.Type.byIcon(debug6.getByte("type")), (LevelAccessor)debug1.level, debug6.getString("id"), debug6.getDouble("x"), debug6.getDouble("z"), debug6.getDouble("rot"), null);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addTargetDecoration(ItemStack debug0, BlockPos debug1, String debug2, MapDecoration.Type debug3) {
/*     */     ListTag debug4;
/* 247 */     if (debug0.hasTag() && debug0.getTag().contains("Decorations", 9)) {
/* 248 */       debug4 = debug0.getTag().getList("Decorations", 10);
/*     */     } else {
/* 250 */       debug4 = new ListTag();
/* 251 */       debug0.addTagElement("Decorations", (Tag)debug4);
/*     */     } 
/* 253 */     CompoundTag debug5 = new CompoundTag();
/* 254 */     debug5.putByte("type", debug3.getIcon());
/* 255 */     debug5.putString("id", debug2);
/* 256 */     debug5.putDouble("x", debug1.getX());
/* 257 */     debug5.putDouble("z", debug1.getZ());
/* 258 */     debug5.putDouble("rot", 180.0D);
/* 259 */     debug4.add(debug5);
/*     */ 
/*     */     
/* 262 */     if (debug3.hasMapColor()) {
/* 263 */       CompoundTag debug6 = debug0.getOrCreateTagElement("display");
/* 264 */       debug6.putInt("MapColor", debug3.getMapColor());
/*     */     } 
/*     */   }
/*     */   private void addDecoration(MapDecoration.Type debug1, @Nullable LevelAccessor debug2, String debug3, double debug4, double debug6, double debug8, @Nullable Component debug10) {
/*     */     byte debug16;
/* 269 */     int debug11 = 1 << this.scale;
/* 270 */     float debug12 = (float)(debug4 - this.x) / debug11;
/* 271 */     float debug13 = (float)(debug6 - this.z) / debug11;
/* 272 */     byte debug14 = (byte)(int)((debug12 * 2.0F) + 0.5D);
/* 273 */     byte debug15 = (byte)(int)((debug13 * 2.0F) + 0.5D);
/*     */     
/* 275 */     int debug17 = 63;
/*     */     
/* 277 */     if (debug12 >= -63.0F && debug13 >= -63.0F && debug12 <= 63.0F && debug13 <= 63.0F) {
/* 278 */       debug8 += (debug8 < 0.0D) ? -8.0D : 8.0D;
/* 279 */       debug16 = (byte)(int)(debug8 * 16.0D / 360.0D);
/*     */       
/* 281 */       if (this.dimension == Level.NETHER && debug2 != null) {
/* 282 */         int debug18 = (int)(debug2.getLevelData().getDayTime() / 10L);
/* 283 */         debug16 = (byte)(debug18 * debug18 * 34187121 + debug18 * 121 >> 15 & 0xF);
/*     */       } 
/* 285 */     } else if (debug1 == MapDecoration.Type.PLAYER) {
/* 286 */       int debug18 = 320;
/* 287 */       if (Math.abs(debug12) < 320.0F && Math.abs(debug13) < 320.0F) {
/* 288 */         debug1 = MapDecoration.Type.PLAYER_OFF_MAP;
/* 289 */       } else if (this.unlimitedTracking) {
/* 290 */         debug1 = MapDecoration.Type.PLAYER_OFF_LIMITS;
/*     */       } else {
/* 292 */         this.decorations.remove(debug3);
/*     */         return;
/*     */       } 
/* 295 */       debug16 = 0;
/* 296 */       if (debug12 <= -63.0F) {
/* 297 */         debug14 = Byte.MIN_VALUE;
/*     */       }
/* 299 */       if (debug13 <= -63.0F) {
/* 300 */         debug15 = Byte.MIN_VALUE;
/*     */       }
/* 302 */       if (debug12 >= 63.0F) {
/* 303 */         debug14 = Byte.MAX_VALUE;
/*     */       }
/* 305 */       if (debug13 >= 63.0F) {
/* 306 */         debug15 = Byte.MAX_VALUE;
/*     */       }
/*     */     } else {
/* 309 */       this.decorations.remove(debug3);
/*     */       
/*     */       return;
/*     */     } 
/* 313 */     this.decorations.put(debug3, new MapDecoration(debug1, debug14, debug15, debug16, debug10));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Packet<?> getUpdatePacket(ItemStack debug1, BlockGetter debug2, Player debug3) {
/* 318 */     HoldingPlayer debug4 = this.carriedByPlayers.get(debug3);
/*     */     
/* 320 */     if (debug4 == null) {
/* 321 */       return null;
/*     */     }
/*     */     
/* 324 */     return debug4.nextUpdatePacket(debug1);
/*     */   }
/*     */   
/*     */   public void setDirty(int debug1, int debug2) {
/* 328 */     setDirty();
/* 329 */     for (HoldingPlayer debug4 : this.carriedBy) {
/* 330 */       debug4.markDirty(debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public HoldingPlayer getHoldingPlayer(Player debug1) {
/* 335 */     HoldingPlayer debug2 = this.carriedByPlayers.get(debug1);
/*     */     
/* 337 */     if (debug2 == null) {
/* 338 */       debug2 = new HoldingPlayer(debug1);
/* 339 */       this.carriedByPlayers.put(debug1, debug2);
/* 340 */       this.carriedBy.add(debug2);
/*     */     } 
/*     */     
/* 343 */     return debug2;
/*     */   }
/*     */   
/*     */   public void toggleBanner(LevelAccessor debug1, BlockPos debug2) {
/* 347 */     double debug3 = debug2.getX() + 0.5D;
/* 348 */     double debug5 = debug2.getZ() + 0.5D;
/* 349 */     int debug7 = 1 << this.scale;
/* 350 */     double debug8 = (debug3 - this.x) / debug7;
/* 351 */     double debug10 = (debug5 - this.z) / debug7;
/* 352 */     int debug12 = 63;
/* 353 */     boolean debug13 = false;
/* 354 */     if (debug8 >= -63.0D && debug10 >= -63.0D && debug8 <= 63.0D && debug10 <= 63.0D) {
/* 355 */       MapBanner debug14 = MapBanner.fromWorld((BlockGetter)debug1, debug2);
/* 356 */       if (debug14 == null) {
/*     */         return;
/*     */       }
/*     */       
/* 360 */       boolean debug15 = true;
/* 361 */       if (this.bannerMarkers.containsKey(debug14.getId()) && (
/* 362 */         (MapBanner)this.bannerMarkers.get(debug14.getId())).equals(debug14)) {
/* 363 */         this.bannerMarkers.remove(debug14.getId());
/* 364 */         this.decorations.remove(debug14.getId());
/* 365 */         debug15 = false;
/* 366 */         debug13 = true;
/*     */       } 
/*     */ 
/*     */       
/* 370 */       if (debug15) {
/* 371 */         this.bannerMarkers.put(debug14.getId(), debug14);
/* 372 */         addDecoration(debug14.getDecoration(), debug1, debug14.getId(), debug3, debug5, 180.0D, debug14.getName());
/* 373 */         debug13 = true;
/*     */       } 
/*     */       
/* 376 */       if (debug13) {
/* 377 */         setDirty();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void checkBanners(BlockGetter debug1, int debug2, int debug3) {
/* 383 */     for (Iterator<MapBanner> debug4 = this.bannerMarkers.values().iterator(); debug4.hasNext(); ) {
/* 384 */       MapBanner debug5 = debug4.next();
/* 385 */       if (debug5.getPos().getX() == debug2 && debug5.getPos().getZ() == debug3) {
/* 386 */         MapBanner debug6 = MapBanner.fromWorld(debug1, debug5.getPos());
/* 387 */         if (!debug5.equals(debug6)) {
/* 388 */           debug4.remove();
/* 389 */           this.decorations.remove(debug5.getId());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removedFromFrame(BlockPos debug1, int debug2) {
/* 400 */     this.decorations.remove("frame-" + debug2);
/* 401 */     this.frameMarkers.remove(MapFrame.frameId(debug1));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */