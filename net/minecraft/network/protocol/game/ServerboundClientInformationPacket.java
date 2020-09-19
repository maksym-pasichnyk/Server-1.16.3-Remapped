/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.player.ChatVisiblity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerboundClientInformationPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private String language;
/*    */   private int viewDistance;
/*    */   private ChatVisiblity chatVisibility;
/*    */   private boolean chatColors;
/*    */   private int modelCustomisation;
/*    */   private HumanoidArm mainHand;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 34 */     this.language = debug1.readUtf(16);
/* 35 */     this.viewDistance = debug1.readByte();
/*    */     
/* 37 */     this.chatVisibility = (ChatVisiblity)debug1.readEnum(ChatVisiblity.class);
/* 38 */     this.chatColors = debug1.readBoolean();
/*    */     
/* 40 */     this.modelCustomisation = debug1.readUnsignedByte();
/* 41 */     this.mainHand = (HumanoidArm)debug1.readEnum(HumanoidArm.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 46 */     debug1.writeUtf(this.language);
/* 47 */     debug1.writeByte(this.viewDistance);
/* 48 */     debug1.writeEnum((Enum)this.chatVisibility);
/* 49 */     debug1.writeBoolean(this.chatColors);
/* 50 */     debug1.writeByte(this.modelCustomisation);
/* 51 */     debug1.writeEnum((Enum)this.mainHand);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 56 */     debug1.handleClientInformation(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChatVisiblity getChatVisibility() {
/* 68 */     return this.chatVisibility;
/*    */   }
/*    */   
/*    */   public boolean getChatColors() {
/* 72 */     return this.chatColors;
/*    */   }
/*    */   
/*    */   public int getModelCustomisation() {
/* 76 */     return this.modelCustomisation;
/*    */   }
/*    */   
/*    */   public HumanoidArm getMainHand() {
/* 80 */     return this.mainHand;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundClientInformationPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */