/*    */ package net.minecraft.network.protocol.status;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.LowerCaseEnumTypeAdapterFactory;
/*    */ 
/*    */ public class ClientboundStatusResponsePacket implements Packet<ClientStatusPacketListener> {
/* 15 */   private static final Gson GSON = (new GsonBuilder())
/* 16 */     .registerTypeAdapter(ServerStatus.Version.class, new ServerStatus.Version.Serializer())
/* 17 */     .registerTypeAdapter(ServerStatus.Players.class, new ServerStatus.Players.Serializer())
/* 18 */     .registerTypeAdapter(ServerStatus.class, new ServerStatus.Serializer())
/* 19 */     .registerTypeHierarchyAdapter(Component.class, new Component.Serializer())
/* 20 */     .registerTypeHierarchyAdapter(Style.class, new Style.Serializer())
/* 21 */     .registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory())
/* 22 */     .create();
/*    */   
/*    */   private ServerStatus status;
/*    */ 
/*    */   
/*    */   public ClientboundStatusResponsePacket() {}
/*    */   
/*    */   public ClientboundStatusResponsePacket(ServerStatus debug1) {
/* 30 */     this.status = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 35 */     this.status = (ServerStatus)GsonHelper.fromJson(GSON, debug1.readUtf(32767), ServerStatus.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 40 */     debug1.writeUtf(GSON.toJson(this.status));
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientStatusPacketListener debug1) {
/* 45 */     debug1.handleStatusResponse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\status\ClientboundStatusResponsePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */