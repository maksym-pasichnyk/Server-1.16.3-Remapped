/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ public interface Packet<T extends net.minecraft.network.PacketListener>
/*    */ {
/*    */   void read(FriendlyByteBuf paramFriendlyByteBuf) throws IOException;
/*    */   
/*    */   void write(FriendlyByteBuf paramFriendlyByteBuf) throws IOException;
/*    */   
/*    */   void handle(T paramT);
/*    */   
/*    */   default boolean isSkippable() {
/* 16 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\Packet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */