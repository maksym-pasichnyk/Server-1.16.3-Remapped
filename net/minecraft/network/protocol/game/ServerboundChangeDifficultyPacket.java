/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public class ServerboundChangeDifficultyPacket
/*    */   implements Packet<ServerGamePacketListener> {
/*    */   private Difficulty difficulty;
/*    */   
/*    */   public ServerboundChangeDifficultyPacket() {}
/*    */   
/*    */   public ServerboundChangeDifficultyPacket(Difficulty debug1) {
/* 16 */     this.difficulty = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 21 */     debug1.handleChangeDifficulty(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 26 */     this.difficulty = Difficulty.byId(debug1.readUnsignedByte());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 31 */     debug1.writeByte(this.difficulty.getId());
/*    */   }
/*    */   
/*    */   public Difficulty getDifficulty() {
/* 35 */     return this.difficulty;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChangeDifficultyPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */