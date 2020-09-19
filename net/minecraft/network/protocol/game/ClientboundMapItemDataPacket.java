/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*    */ 
/*    */ 
/*    */ public class ClientboundMapItemDataPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private int mapId;
/*    */   private byte scale;
/*    */   private boolean trackingPosition;
/*    */   private boolean locked;
/*    */   private MapDecoration[] decorations;
/*    */   private int startX;
/*    */   private int startY;
/*    */   private int width;
/*    */   private int height;
/*    */   private byte[] mapColors;
/*    */   
/*    */   public ClientboundMapItemDataPacket() {}
/*    */   
/*    */   public ClientboundMapItemDataPacket(int debug1, byte debug2, boolean debug3, boolean debug4, Collection<MapDecoration> debug5, byte[] debug6, int debug7, int debug8, int debug9, int debug10) {
/* 28 */     this.mapId = debug1;
/* 29 */     this.scale = debug2;
/* 30 */     this.trackingPosition = debug3;
/* 31 */     this.locked = debug4;
/* 32 */     this.decorations = debug5.<MapDecoration>toArray(new MapDecoration[debug5.size()]);
/* 33 */     this.startX = debug7;
/* 34 */     this.startY = debug8;
/* 35 */     this.width = debug9;
/* 36 */     this.height = debug10;
/*    */     
/* 38 */     this.mapColors = new byte[debug9 * debug10];
/* 39 */     for (int debug11 = 0; debug11 < debug9; debug11++) {
/* 40 */       for (int debug12 = 0; debug12 < debug10; debug12++) {
/* 41 */         this.mapColors[debug11 + debug12 * debug9] = debug6[debug7 + debug11 + (debug8 + debug12) * 128];
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 48 */     this.mapId = debug1.readVarInt();
/* 49 */     this.scale = debug1.readByte();
/* 50 */     this.trackingPosition = debug1.readBoolean();
/* 51 */     this.locked = debug1.readBoolean();
/* 52 */     this.decorations = new MapDecoration[debug1.readVarInt()];
/* 53 */     for (int debug2 = 0; debug2 < this.decorations.length; debug2++) {
/* 54 */       MapDecoration.Type debug3 = (MapDecoration.Type)debug1.readEnum(MapDecoration.Type.class);
/* 55 */       this.decorations[debug2] = new MapDecoration(debug3, debug1.readByte(), debug1.readByte(), (byte)(debug1.readByte() & 0xF), debug1.readBoolean() ? debug1.readComponent() : null);
/*    */     } 
/* 57 */     this.width = debug1.readUnsignedByte();
/* 58 */     if (this.width > 0) {
/* 59 */       this.height = debug1.readUnsignedByte();
/* 60 */       this.startX = debug1.readUnsignedByte();
/* 61 */       this.startY = debug1.readUnsignedByte();
/* 62 */       this.mapColors = debug1.readByteArray();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 68 */     debug1.writeVarInt(this.mapId);
/* 69 */     debug1.writeByte(this.scale);
/* 70 */     debug1.writeBoolean(this.trackingPosition);
/* 71 */     debug1.writeBoolean(this.locked);
/* 72 */     debug1.writeVarInt(this.decorations.length);
/* 73 */     for (MapDecoration debug5 : this.decorations) {
/* 74 */       debug1.writeEnum((Enum)debug5.getType());
/* 75 */       debug1.writeByte(debug5.getX());
/* 76 */       debug1.writeByte(debug5.getY());
/* 77 */       debug1.writeByte(debug5.getRot() & 0xF);
/* 78 */       if (debug5.getName() != null) {
/* 79 */         debug1.writeBoolean(true);
/* 80 */         debug1.writeComponent(debug5.getName());
/*    */       } else {
/* 82 */         debug1.writeBoolean(false);
/*    */       } 
/*    */     } 
/* 85 */     debug1.writeByte(this.width);
/* 86 */     if (this.width > 0) {
/* 87 */       debug1.writeByte(this.height);
/* 88 */       debug1.writeByte(this.startX);
/* 89 */       debug1.writeByte(this.startY);
/* 90 */       debug1.writeByteArray(this.mapColors);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 96 */     debug1.handleMapItemData(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMapItemDataPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */