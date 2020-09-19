/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class PlayerHeadItem
/*    */   extends StandingAndWallBlockItem
/*    */ {
/*    */   public PlayerHeadItem(Block debug1, Block debug2, Item.Properties debug3) {
/* 17 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack debug1) {
/* 22 */     if (debug1.getItem() == Items.PLAYER_HEAD && debug1.hasTag()) {
/* 23 */       String debug2 = null;
/* 24 */       CompoundTag debug3 = debug1.getTag();
/* 25 */       if (debug3.contains("SkullOwner", 8)) {
/* 26 */         debug2 = debug3.getString("SkullOwner");
/* 27 */       } else if (debug3.contains("SkullOwner", 10)) {
/* 28 */         CompoundTag debug4 = debug3.getCompound("SkullOwner");
/* 29 */         if (debug4.contains("Name", 8)) {
/* 30 */           debug2 = debug4.getString("Name");
/*    */         }
/*    */       } 
/* 33 */       if (debug2 != null) {
/* 34 */         return (Component)new TranslatableComponent(getDescriptionId() + ".named", new Object[] { debug2 });
/*    */       }
/*    */     } 
/* 37 */     return super.getName(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean verifyTagAfterLoad(CompoundTag debug1) {
/* 42 */     super.verifyTagAfterLoad(debug1);
/* 43 */     if (debug1.contains("SkullOwner", 8) && !StringUtils.isBlank(debug1.getString("SkullOwner"))) {
/* 44 */       GameProfile debug2 = new GameProfile(null, debug1.getString("SkullOwner"));
/* 45 */       debug2 = SkullBlockEntity.updateGameprofile(debug2);
/* 46 */       debug1.put("SkullOwner", (Tag)NbtUtils.writeGameProfile(new CompoundTag(), debug2));
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\PlayerHeadItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */