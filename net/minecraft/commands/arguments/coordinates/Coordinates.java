/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface Coordinates {
/*    */   Vec3 getPosition(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   Vec2 getRotation(CommandSourceStack paramCommandSourceStack);
/*    */   
/*    */   default BlockPos getBlockPos(CommandSourceStack debug1) {
/* 14 */     return new BlockPos(getPosition(debug1));
/*    */   }
/*    */   
/*    */   boolean isXRelative();
/*    */   
/*    */   boolean isYRelative();
/*    */   
/*    */   boolean isZRelative();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\coordinates\Coordinates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */