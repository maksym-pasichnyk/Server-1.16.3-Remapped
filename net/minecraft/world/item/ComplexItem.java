/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ComplexItem
/*    */   extends Item {
/*    */   public ComplexItem(Item.Properties debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isComplex() {
/* 16 */     return true;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Packet<?> getUpdatePacket(ItemStack debug1, Level debug2, Player debug3) {
/* 21 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ComplexItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */