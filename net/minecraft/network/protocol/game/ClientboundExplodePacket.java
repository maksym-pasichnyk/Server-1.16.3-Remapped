/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientboundExplodePacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/*    */   private double x;
/*    */   private double y;
/*    */   private double z;
/*    */   private float power;
/*    */   private List<BlockPos> toBlow;
/*    */   private float knockbackX;
/*    */   private float knockbackY;
/*    */   private float knockbackZ;
/*    */   
/*    */   public ClientboundExplodePacket() {}
/*    */   
/*    */   public ClientboundExplodePacket(double debug1, double debug3, double debug5, float debug7, List<BlockPos> debug8, Vec3 debug9) {
/* 28 */     this.x = debug1;
/* 29 */     this.y = debug3;
/* 30 */     this.z = debug5;
/* 31 */     this.power = debug7;
/* 32 */     this.toBlow = Lists.newArrayList(debug8);
/*    */     
/* 34 */     if (debug9 != null) {
/* 35 */       this.knockbackX = (float)debug9.x;
/* 36 */       this.knockbackY = (float)debug9.y;
/* 37 */       this.knockbackZ = (float)debug9.z;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 43 */     this.x = debug1.readFloat();
/* 44 */     this.y = debug1.readFloat();
/* 45 */     this.z = debug1.readFloat();
/* 46 */     this.power = debug1.readFloat();
/* 47 */     int debug2 = debug1.readInt();
/*    */     
/* 49 */     this.toBlow = Lists.newArrayListWithCapacity(debug2);
/*    */     
/* 51 */     int debug3 = Mth.floor(this.x);
/* 52 */     int debug4 = Mth.floor(this.y);
/* 53 */     int debug5 = Mth.floor(this.z);
/* 54 */     for (int debug6 = 0; debug6 < debug2; debug6++) {
/* 55 */       int debug7 = debug1.readByte() + debug3;
/* 56 */       int debug8 = debug1.readByte() + debug4;
/* 57 */       int debug9 = debug1.readByte() + debug5;
/* 58 */       this.toBlow.add(new BlockPos(debug7, debug8, debug9));
/*    */     } 
/*    */     
/* 61 */     this.knockbackX = debug1.readFloat();
/* 62 */     this.knockbackY = debug1.readFloat();
/* 63 */     this.knockbackZ = debug1.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 68 */     debug1.writeFloat((float)this.x);
/* 69 */     debug1.writeFloat((float)this.y);
/* 70 */     debug1.writeFloat((float)this.z);
/* 71 */     debug1.writeFloat(this.power);
/* 72 */     debug1.writeInt(this.toBlow.size());
/*    */     
/* 74 */     int debug2 = Mth.floor(this.x);
/* 75 */     int debug3 = Mth.floor(this.y);
/* 76 */     int debug4 = Mth.floor(this.z);
/* 77 */     for (BlockPos debug6 : this.toBlow) {
/* 78 */       int debug7 = debug6.getX() - debug2;
/* 79 */       int debug8 = debug6.getY() - debug3;
/* 80 */       int debug9 = debug6.getZ() - debug4;
/* 81 */       debug1.writeByte(debug7);
/* 82 */       debug1.writeByte(debug8);
/* 83 */       debug1.writeByte(debug9);
/*    */     } 
/*    */     
/* 86 */     debug1.writeFloat(this.knockbackX);
/* 87 */     debug1.writeFloat(this.knockbackY);
/* 88 */     debug1.writeFloat(this.knockbackZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener debug1) {
/* 93 */     debug1.handleExplosion(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundExplodePacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */