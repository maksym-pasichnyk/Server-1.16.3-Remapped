/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundUseItemOnPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private BlockHitResult blockHit;
/*    */   private InteractionHand hand;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 24 */     this.hand = (InteractionHand)debug1.readEnum(InteractionHand.class);
/* 25 */     this.blockHit = debug1.readBlockHitResult();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 30 */     debug1.writeEnum((Enum)this.hand);
/* 31 */     debug1.writeBlockHitResult(this.blockHit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 36 */     debug1.handleUseItemOn(this);
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 40 */     return this.hand;
/*    */   }
/*    */   
/*    */   public BlockHitResult getHitResult() {
/* 44 */     return this.blockHit;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundUseItemOnPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */