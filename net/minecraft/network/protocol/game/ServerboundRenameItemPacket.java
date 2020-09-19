/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundRenameItemPacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private String name;
/*    */   
/*    */   public ServerboundRenameItemPacket() {}
/*    */   
/*    */   public ServerboundRenameItemPacket(String debug1) {
/* 15 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 20 */     this.name = debug1.readUtf(32767);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 25 */     debug1.writeUtf(this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 30 */     debug1.handleRenameItem(this);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundRenameItemPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */