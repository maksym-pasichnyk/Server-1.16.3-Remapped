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
/*    */ 
/*    */ public class ServerboundSeenAdvancementsPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private Action action;
/*    */   private ResourceLocation tab;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 33 */     this.action = (Action)debug1.readEnum(Action.class);
/* 34 */     if (this.action == Action.OPENED_TAB) {
/* 35 */       this.tab = debug1.readResourceLocation();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 41 */     debug1.writeEnum(this.action);
/* 42 */     if (this.action == Action.OPENED_TAB) {
/* 43 */       debug1.writeResourceLocation(this.tab);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 49 */     debug1.handleSeenAdvancements(this);
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 53 */     return this.action;
/*    */   }
/*    */   
/*    */   public ResourceLocation getTab() {
/* 57 */     return this.tab;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 61 */     OPENED_TAB,
/* 62 */     CLOSED_SCREEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSeenAdvancementsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */