/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientboundLoginPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   private int playerId;
/*     */   private long seed;
/*     */   private boolean hardcore;
/*     */   private GameType gameType;
/*     */   private GameType previousGameType;
/*     */   private Set<ResourceKey<Level>> levels;
/*     */   private RegistryAccess.RegistryHolder registryHolder;
/*     */   private DimensionType dimensionType;
/*     */   private ResourceKey<Level> dimension;
/*     */   private int maxPlayers;
/*     */   private int chunkRadius;
/*     */   private boolean reducedDebugInfo;
/*     */   private boolean showDeathScreen;
/*     */   private boolean isDebug;
/*     */   private boolean isFlat;
/*     */   
/*     */   public ClientboundLoginPacket() {}
/*     */   
/*     */   public ClientboundLoginPacket(int debug1, GameType debug2, GameType debug3, long debug4, boolean debug6, Set<ResourceKey<Level>> debug7, RegistryAccess.RegistryHolder debug8, DimensionType debug9, ResourceKey<Level> debug10, int debug11, int debug12, boolean debug13, boolean debug14, boolean debug15, boolean debug16) {
/*  41 */     this.playerId = debug1;
/*  42 */     this.levels = debug7;
/*  43 */     this.registryHolder = debug8;
/*  44 */     this.dimensionType = debug9;
/*  45 */     this.dimension = debug10;
/*  46 */     this.seed = debug4;
/*  47 */     this.gameType = debug2;
/*  48 */     this.previousGameType = debug3;
/*  49 */     this.maxPlayers = debug11;
/*  50 */     this.hardcore = debug6;
/*  51 */     this.chunkRadius = debug12;
/*  52 */     this.reducedDebugInfo = debug13;
/*  53 */     this.showDeathScreen = debug14;
/*  54 */     this.isDebug = debug15;
/*  55 */     this.isFlat = debug16;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  60 */     this.playerId = debug1.readInt();
/*     */     
/*  62 */     this.hardcore = debug1.readBoolean();
/*  63 */     this.gameType = GameType.byId(debug1.readByte());
/*  64 */     this.previousGameType = GameType.byId(debug1.readByte());
/*     */     
/*  66 */     int debug2 = debug1.readVarInt();
/*  67 */     this.levels = Sets.newHashSet();
/*  68 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  69 */       this.levels.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, debug1.readResourceLocation()));
/*     */     }
/*  71 */     this.registryHolder = (RegistryAccess.RegistryHolder)debug1.readWithCodec(RegistryAccess.RegistryHolder.NETWORK_CODEC);
/*     */     
/*  73 */     this.dimensionType = ((Supplier<DimensionType>)debug1.readWithCodec(DimensionType.CODEC)).get();
/*  74 */     this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, debug1.readResourceLocation());
/*  75 */     this.seed = debug1.readLong();
/*  76 */     this.maxPlayers = debug1.readVarInt();
/*  77 */     this.chunkRadius = debug1.readVarInt();
/*  78 */     this.reducedDebugInfo = debug1.readBoolean();
/*  79 */     this.showDeathScreen = debug1.readBoolean();
/*  80 */     this.isDebug = debug1.readBoolean();
/*  81 */     this.isFlat = debug1.readBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  86 */     debug1.writeInt(this.playerId);
/*  87 */     debug1.writeBoolean(this.hardcore);
/*  88 */     debug1.writeByte(this.gameType.getId());
/*  89 */     debug1.writeByte(this.previousGameType.getId());
/*     */     
/*  91 */     debug1.writeVarInt(this.levels.size());
/*  92 */     for (ResourceKey<Level> debug3 : this.levels) {
/*  93 */       debug1.writeResourceLocation(debug3.location());
/*     */     }
/*  95 */     debug1.writeWithCodec(RegistryAccess.RegistryHolder.NETWORK_CODEC, this.registryHolder);
/*     */     
/*  97 */     debug1.writeWithCodec(DimensionType.CODEC, () -> this.dimensionType);
/*  98 */     debug1.writeResourceLocation(this.dimension.location());
/*  99 */     debug1.writeLong(this.seed);
/* 100 */     debug1.writeVarInt(this.maxPlayers);
/* 101 */     debug1.writeVarInt(this.chunkRadius);
/* 102 */     debug1.writeBoolean(this.reducedDebugInfo);
/* 103 */     debug1.writeBoolean(this.showDeathScreen);
/* 104 */     debug1.writeBoolean(this.isDebug);
/* 105 */     debug1.writeBoolean(this.isFlat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 110 */     debug1.handleLogin(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLoginPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */