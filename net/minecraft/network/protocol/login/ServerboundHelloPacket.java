/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundHelloPacket
/*    */   implements Packet<ServerLoginPacketListener>
/*    */ {
/*    */   private GameProfile gameProfile;
/*    */   
/*    */   public ServerboundHelloPacket() {}
/*    */   
/*    */   public ServerboundHelloPacket(GameProfile debug1) {
/* 17 */     this.gameProfile = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 22 */     this.gameProfile = new GameProfile(null, debug1.readUtf(16));
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 27 */     debug1.writeUtf(this.gameProfile.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerLoginPacketListener debug1) {
/* 32 */     debug1.handleHello(this);
/*    */   }
/*    */   
/*    */   public GameProfile getGameProfile() {
/* 36 */     return this.gameProfile;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundHelloPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */