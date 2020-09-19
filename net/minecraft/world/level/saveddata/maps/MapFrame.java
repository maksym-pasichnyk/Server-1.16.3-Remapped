/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ public class MapFrame
/*    */ {
/*    */   private final BlockPos pos;
/*    */   
/*    */   public MapFrame(BlockPos debug1, int debug2, int debug3) {
/* 13 */     this.pos = debug1;
/* 14 */     this.rotation = debug2;
/* 15 */     this.entityId = debug3;
/*    */   }
/*    */   private final int rotation; private final int entityId;
/*    */   public static MapFrame load(CompoundTag debug0) {
/* 19 */     BlockPos debug1 = NbtUtils.readBlockPos(debug0.getCompound("Pos"));
/* 20 */     int debug2 = debug0.getInt("Rotation");
/* 21 */     int debug3 = debug0.getInt("EntityId");
/* 22 */     return new MapFrame(debug1, debug2, debug3);
/*    */   }
/*    */   
/*    */   public CompoundTag save() {
/* 26 */     CompoundTag debug1 = new CompoundTag();
/* 27 */     debug1.put("Pos", (Tag)NbtUtils.writeBlockPos(this.pos));
/* 28 */     debug1.putInt("Rotation", this.rotation);
/* 29 */     debug1.putInt("EntityId", this.entityId);
/* 30 */     return debug1;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 34 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int getRotation() {
/* 38 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public int getEntityId() {
/* 42 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 46 */     return frameId(this.pos);
/*    */   }
/*    */   
/*    */   public static String frameId(BlockPos debug0) {
/* 50 */     return "frame-" + debug0.getX() + "," + debug0.getY() + "," + debug0.getZ();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\maps\MapFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */