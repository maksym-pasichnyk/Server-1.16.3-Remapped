/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.StringRange;
/*    */ import com.mojang.brigadier.suggestion.Suggestion;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundCommandSuggestionsPacket implements Packet<ClientGamePacketListener> {
/*    */   private int id;
/*    */   private Suggestions suggestions;
/*    */   
/*    */   public ClientboundCommandSuggestionsPacket() {}
/*    */   
/*    */   public ClientboundCommandSuggestionsPacket(int debug1, Suggestions debug2) {
/* 23 */     this.id = debug1;
/* 24 */     this.suggestions = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 29 */     this.id = debug1.readVarInt();
/* 30 */     int debug2 = debug1.readVarInt();
/* 31 */     int debug3 = debug1.readVarInt();
/* 32 */     StringRange debug4 = StringRange.between(debug2, debug2 + debug3);
/*    */     
/* 34 */     int debug5 = debug1.readVarInt();
/* 35 */     List<Suggestion> debug6 = Lists.newArrayListWithCapacity(debug5);
/*    */     
/* 37 */     for (int debug7 = 0; debug7 < debug5; debug7++) {
/* 38 */       String debug8 = debug1.readUtf(32767);
/* 39 */       Component debug9 = debug1.readBoolean() ? debug1.readComponent() : null;
/* 40 */       debug6.add(new Suggestion(debug4, debug8, (Message)debug9));
/*    */     } 
/*    */     
/* 43 */     this.suggestions = new Suggestions(debug4, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 48 */     debug1.writeVarInt(this.id);
/* 49 */     debug1.writeVarInt(this.suggestions.getRange().getStart());
/* 50 */     debug1.writeVarInt(this.suggestions.getRange().getLength());
/* 51 */     debug1.writeVarInt(this.suggestions.getList().size());
/*    */     
/* 53 */     for (Suggestion debug3 : this.suggestions.getList()) {
/* 54 */       debug1.writeUtf(debug3.getText());
/* 55 */       debug1.writeBoolean((debug3.getTooltip() != null));
/* 56 */       if (debug3.getTooltip() != null) {
/* 57 */         debug1.writeComponent(ComponentUtils.fromMessage(debug3.getTooltip()));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 64 */     debug1.handleCommandSuggestions(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandSuggestionsPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */