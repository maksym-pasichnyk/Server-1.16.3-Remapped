/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientboundCustomPayloadPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/* 14 */   public static final ResourceLocation BRAND = new ResourceLocation("brand");
/* 15 */   public static final ResourceLocation DEBUG_PATHFINDING_PACKET = new ResourceLocation("debug/path");
/* 16 */   public static final ResourceLocation DEBUG_NEIGHBORSUPDATE_PACKET = new ResourceLocation("debug/neighbors_update");
/* 17 */   public static final ResourceLocation DEBUG_CAVES_PACKET = new ResourceLocation("debug/caves");
/* 18 */   public static final ResourceLocation DEBUG_STRUCTURES_PACKET = new ResourceLocation("debug/structures");
/* 19 */   public static final ResourceLocation DEBUG_WORLDGENATTEMPT_PACKET = new ResourceLocation("debug/worldgen_attempt");
/* 20 */   public static final ResourceLocation DEBUG_POI_TICKET_COUNT_PACKET = new ResourceLocation("debug/poi_ticket_count");
/* 21 */   public static final ResourceLocation DEBUG_POI_ADDED_PACKET = new ResourceLocation("debug/poi_added");
/* 22 */   public static final ResourceLocation DEBUG_POI_REMOVED_PACKET = new ResourceLocation("debug/poi_removed");
/* 23 */   public static final ResourceLocation DEBUG_VILLAGE_SECTIONS = new ResourceLocation("debug/village_sections");
/* 24 */   public static final ResourceLocation DEBUG_GOAL_SELECTOR = new ResourceLocation("debug/goal_selector");
/* 25 */   public static final ResourceLocation DEBUG_BRAIN = new ResourceLocation("debug/brain");
/* 26 */   public static final ResourceLocation DEBUG_BEE = new ResourceLocation("debug/bee");
/* 27 */   public static final ResourceLocation DEBUG_HIVE = new ResourceLocation("debug/hive");
/* 28 */   public static final ResourceLocation DEBUG_GAME_TEST_ADD_MARKER = new ResourceLocation("debug/game_test_add_marker");
/* 29 */   public static final ResourceLocation DEBUG_GAME_TEST_CLEAR = new ResourceLocation("debug/game_test_clear");
/* 30 */   public static final ResourceLocation DEBUG_RAIDS = new ResourceLocation("debug/raids");
/*    */   
/*    */   private ResourceLocation identifier;
/*    */   
/*    */   private FriendlyByteBuf data;
/*    */   
/*    */   public ClientboundCustomPayloadPacket() {}
/*    */   
/*    */   public ClientboundCustomPayloadPacket(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 39 */     this.identifier = debug1;
/* 40 */     this.data = debug2;
/*    */     
/* 42 */     if (debug2.writerIndex() > 1048576) {
/* 43 */       throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 49 */     this.identifier = debug1.readResourceLocation();
/* 50 */     int debug2 = debug1.readableBytes();
/* 51 */     if (debug2 < 0 || debug2 > 1048576) {
/* 52 */       throw new IOException("Payload may not be larger than 1048576 bytes");
/*    */     }
/* 54 */     this.data = new FriendlyByteBuf(debug1.readBytes(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 59 */     debug1.writeResourceLocation(this.identifier);
/* 60 */     debug1.writeBytes(this.data.copy());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 65 */     debug1.handleCustomPayload(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCustomPayloadPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */