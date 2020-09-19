/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
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
/*    */ public class ServerboundSetCommandBlockPacket
/*    */   implements Packet<ServerGamePacketListener>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private String command;
/*    */   private boolean trackOutput;
/*    */   private boolean conditional;
/*    */   private boolean automatic;
/*    */   private CommandBlockEntity.Mode mode;
/*    */   
/*    */   public void read(FriendlyByteBuf debug1) throws IOException {
/* 36 */     this.pos = debug1.readBlockPos();
/* 37 */     this.command = debug1.readUtf(32767);
/* 38 */     this.mode = (CommandBlockEntity.Mode)debug1.readEnum(CommandBlockEntity.Mode.class);
/* 39 */     int debug2 = debug1.readByte();
/* 40 */     this.trackOutput = ((debug2 & 0x1) != 0);
/* 41 */     this.conditional = ((debug2 & 0x2) != 0);
/* 42 */     this.automatic = ((debug2 & 0x4) != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(FriendlyByteBuf debug1) throws IOException {
/* 47 */     debug1.writeBlockPos(this.pos);
/* 48 */     debug1.writeUtf(this.command);
/* 49 */     debug1.writeEnum((Enum)this.mode);
/* 50 */     int debug2 = 0;
/* 51 */     if (this.trackOutput) {
/* 52 */       debug2 |= 0x1;
/*    */     }
/* 54 */     if (this.conditional) {
/* 55 */       debug2 |= 0x2;
/*    */     }
/* 57 */     if (this.automatic) {
/* 58 */       debug2 |= 0x4;
/*    */     }
/* 60 */     debug1.writeByte(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener debug1) {
/* 65 */     debug1.handleSetCommandBlock(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 69 */     return this.pos;
/*    */   }
/*    */   
/*    */   public String getCommand() {
/* 73 */     return this.command;
/*    */   }
/*    */   
/*    */   public boolean isTrackOutput() {
/* 77 */     return this.trackOutput;
/*    */   }
/*    */   
/*    */   public boolean isConditional() {
/* 81 */     return this.conditional;
/*    */   }
/*    */   
/*    */   public boolean isAutomatic() {
/* 85 */     return this.automatic;
/*    */   }
/*    */   
/*    */   public CommandBlockEntity.Mode getMode() {
/* 89 */     return this.mode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCommandBlockPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */