/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetTitlesPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   private Type type;
/*    */   private Component text;
/*    */   private int fadeInTime;
/*    */   private int stayTime;
/*    */   private int fadeOutTime;
/*    */   
/*    */   public ClientboundSetTitlesPacket() {}
/*    */   
/*    */   public ClientboundSetTitlesPacket(Type debug1, Component debug2) {
/* 21 */     this(debug1, debug2, -1, -1, -1);
/*    */   }
/*    */   
/*    */   public ClientboundSetTitlesPacket(int debug1, int debug2, int debug3) {
/* 25 */     this(Type.TIMES, null, debug1, debug2, debug3);
/*    */   }
/*    */   
/*    */   public ClientboundSetTitlesPacket(Type debug1, @Nullable Component debug2, int debug3, int debug4, int debug5) {
/* 29 */     this.type = debug1;
/* 30 */     this.text = debug2;
/* 31 */     this.fadeInTime = debug3;
/* 32 */     this.stayTime = debug4;
/* 33 */     this.fadeOutTime = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 38 */     this.type = (Type)debug1.readEnum(Type.class);
/* 39 */     if (this.type == Type.TITLE || this.type == Type.SUBTITLE || this.type == Type.ACTIONBAR) {
/* 40 */       this.text = debug1.readComponent();
/*    */     }
/* 42 */     if (this.type == Type.TIMES) {
/* 43 */       this.fadeInTime = debug1.readInt();
/* 44 */       this.stayTime = debug1.readInt();
/* 45 */       this.fadeOutTime = debug1.readInt();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 51 */     debug1.writeEnum(this.type);
/* 52 */     if (this.type == Type.TITLE || this.type == Type.SUBTITLE || this.type == Type.ACTIONBAR) {
/* 53 */       debug1.writeComponent(this.text);
/*    */     }
/* 55 */     if (this.type == Type.TIMES) {
/* 56 */       debug1.writeInt(this.fadeInTime);
/* 57 */       debug1.writeInt(this.stayTime);
/* 58 */       debug1.writeInt(this.fadeOutTime);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 64 */     debug1.handleSetTitles(this);
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
/*    */   public enum Type
/*    */   {
/* 88 */     TITLE,
/* 89 */     SUBTITLE,
/* 90 */     ACTIONBAR,
/* 91 */     TIMES,
/* 92 */     CLEAR,
/* 93 */     RESET;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetTitlesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */