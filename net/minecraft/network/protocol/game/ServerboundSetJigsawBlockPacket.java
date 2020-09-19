/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
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
/*    */ public class ServerboundSetJigsawBlockPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private ResourceLocation name;
/*    */   private ResourceLocation target;
/*    */   private ResourceLocation pool;
/*    */   private String finalState;
/*    */   private JigsawBlockEntity.JointType joint;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 33 */     this.pos = debug1.readBlockPos();
/* 34 */     this.name = debug1.readResourceLocation();
/* 35 */     this.target = debug1.readResourceLocation();
/* 36 */     this.pool = debug1.readResourceLocation();
/* 37 */     this.finalState = debug1.readUtf(32767);
/* 38 */     this.joint = JigsawBlockEntity.JointType.byName(debug1.readUtf(32767)).orElse(JigsawBlockEntity.JointType.ALIGNED);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 43 */     debug1.writeBlockPos(this.pos);
/* 44 */     debug1.writeResourceLocation(this.name);
/* 45 */     debug1.writeResourceLocation(this.target);
/* 46 */     debug1.writeResourceLocation(this.pool);
/* 47 */     debug1.writeUtf(this.finalState);
/* 48 */     debug1.writeUtf(this.joint.getSerializedName());
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 53 */     debug1.handleSetJigsawBlock(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 57 */     return this.pos;
/*    */   }
/*    */   
/*    */   public ResourceLocation getName() {
/* 61 */     return this.name;
/*    */   }
/*    */   
/*    */   public ResourceLocation getTarget() {
/* 65 */     return this.target;
/*    */   }
/*    */   
/*    */   public ResourceLocation getPool() {
/* 69 */     return this.pool;
/*    */   }
/*    */   
/*    */   public String getFinalState() {
/* 73 */     return this.finalState;
/*    */   }
/*    */   
/*    */   public JigsawBlockEntity.JointType getJoint() {
/* 77 */     return this.joint;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetJigsawBlockPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */