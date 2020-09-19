/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public class ClientboundChangeDifficultyPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private Difficulty difficulty;
/*    */   private boolean locked;
/*    */   
/*    */   public ClientboundChangeDifficultyPacket() {}
/*    */   
/*    */   public ClientboundChangeDifficultyPacket(Difficulty debug1, boolean debug2) {
/* 17 */     this.difficulty = debug1;
/* 18 */     this.locked = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 23 */     debug1.handleChangeDifficulty(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 28 */     this.difficulty = Difficulty.byId(debug1.readUnsignedByte());
/* 29 */     this.locked = debug1.readBoolean();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 34 */     debug1.writeByte(this.difficulty.getId());
/* 35 */     debug1.writeBoolean(this.locked);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundChangeDifficultyPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */