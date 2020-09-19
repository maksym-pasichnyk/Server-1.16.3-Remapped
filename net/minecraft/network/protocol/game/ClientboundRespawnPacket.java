/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class ClientboundRespawnPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private DimensionType dimensionType;
/*    */   private ResourceKey<Level> dimension;
/*    */   private long seed;
/*    */   private GameType playerGameType;
/*    */   private GameType previousPlayerGameType;
/*    */   private boolean isDebug;
/*    */   private boolean isFlat;
/*    */   private boolean keepAllPlayerData;
/*    */   
/*    */   public ClientboundRespawnPacket() {}
/*    */   
/*    */   public ClientboundRespawnPacket(DimensionType debug1, ResourceKey<Level> debug2, long debug3, GameType debug5, GameType debug6, boolean debug7, boolean debug8, boolean debug9) {
/* 28 */     this.dimensionType = debug1;
/* 29 */     this.dimension = debug2;
/* 30 */     this.seed = debug3;
/* 31 */     this.playerGameType = debug5;
/* 32 */     this.previousPlayerGameType = debug6;
/* 33 */     this.isDebug = debug7;
/* 34 */     this.isFlat = debug8;
/* 35 */     this.keepAllPlayerData = debug9;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 40 */     debug1.handleRespawn(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 45 */     this.dimensionType = ((Supplier<DimensionType>)debug1.readWithCodec(DimensionType.CODEC)).get();
/* 46 */     this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, debug1.readResourceLocation());
/* 47 */     this.seed = debug1.readLong();
/* 48 */     this.playerGameType = GameType.byId(debug1.readUnsignedByte());
/* 49 */     this.previousPlayerGameType = GameType.byId(debug1.readUnsignedByte());
/* 50 */     this.isDebug = debug1.readBoolean();
/* 51 */     this.isFlat = debug1.readBoolean();
/* 52 */     this.keepAllPlayerData = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 57 */     debug1.writeWithCodec(DimensionType.CODEC, () -> this.dimensionType);
/* 58 */     debug1.writeResourceLocation(this.dimension.location());
/* 59 */     debug1.writeLong(this.seed);
/* 60 */     debug1.writeByte(this.playerGameType.getId());
/* 61 */     debug1.writeByte(this.previousPlayerGameType.getId());
/* 62 */     debug1.writeBoolean(this.isDebug);
/* 63 */     debug1.writeBoolean(this.isFlat);
/* 64 */     debug1.writeBoolean(this.keepAllPlayerData);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRespawnPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */