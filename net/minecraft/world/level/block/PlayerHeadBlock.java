/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class PlayerHeadBlock
/*    */   extends SkullBlock {
/*    */   protected PlayerHeadBlock(BlockBehaviour.Properties debug1) {
/* 20 */     super(SkullBlock.Types.PLAYER, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/* 25 */     super.setPlacedBy(debug1, debug2, debug3, debug4, debug5);
/*    */     
/* 27 */     BlockEntity debug6 = debug1.getBlockEntity(debug2);
/*    */     
/* 29 */     if (debug6 instanceof SkullBlockEntity) {
/* 30 */       SkullBlockEntity debug7 = (SkullBlockEntity)debug6;
/* 31 */       GameProfile debug8 = null;
/* 32 */       if (debug5.hasTag()) {
/* 33 */         CompoundTag debug9 = debug5.getTag();
/*    */ 
/*    */         
/* 36 */         if (debug9.contains("SkullOwner", 10)) {
/* 37 */           debug8 = NbtUtils.readGameProfile(debug9.getCompound("SkullOwner"));
/* 38 */         } else if (debug9.contains("SkullOwner", 8) && !StringUtils.isBlank(debug9.getString("SkullOwner"))) {
/* 39 */           debug8 = new GameProfile(null, debug9.getString("SkullOwner"));
/*    */         } 
/*    */       } 
/*    */       
/* 43 */       debug7.setOwner(debug8);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PlayerHeadBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */