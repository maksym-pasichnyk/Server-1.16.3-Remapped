/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundGameEventPacket
/*    */   implements Packet<ClientGamePacketListener> {
/*    */   public static class Type {
/* 13 */     private static final Int2ObjectMap<Type> TYPES = (Int2ObjectMap<Type>)new Int2ObjectOpenHashMap();
/*    */     
/*    */     private final int id;
/*    */     
/*    */     public Type(int debug1) {
/* 18 */       this.id = debug1;
/* 19 */       TYPES.put(debug1, this);
/*    */     }
/*    */   }
/*    */   
/* 23 */   public static final Type NO_RESPAWN_BLOCK_AVAILABLE = new Type(0);
/* 24 */   public static final Type START_RAINING = new Type(1);
/* 25 */   public static final Type STOP_RAINING = new Type(2);
/* 26 */   public static final Type CHANGE_GAME_MODE = new Type(3);
/* 27 */   public static final Type WIN_GAME = new Type(4);
/* 28 */   public static final Type DEMO_EVENT = new Type(5);
/* 29 */   public static final Type ARROW_HIT_PLAYER = new Type(6);
/* 30 */   public static final Type RAIN_LEVEL_CHANGE = new Type(7);
/* 31 */   public static final Type THUNDER_LEVEL_CHANGE = new Type(8);
/* 32 */   public static final Type PUFFER_FISH_STING = new Type(9);
/* 33 */   public static final Type GUARDIAN_ELDER_EFFECT = new Type(10);
/* 34 */   public static final Type IMMEDIATE_RESPAWN = new Type(11);
/*    */ 
/*    */   
/*    */   private Type event;
/*    */ 
/*    */   
/*    */   private float param;
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundGameEventPacket() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientboundGameEventPacket(Type debug1, float debug2) {
/* 49 */     this.event = debug1;
/* 50 */     this.param = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 55 */     this.event = (Type)Type.TYPES.get(debug1.readUnsignedByte());
/* 56 */     this.param = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 61 */     debug1.writeByte(this.event.id);
/* 62 */     debug1.writeFloat(this.param);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 67 */     debug1.handleGameEvent(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundGameEventPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */