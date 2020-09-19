/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.concurrent.Immutable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class LockCode
/*    */ {
/* 11 */   public static final LockCode NO_LOCK = new LockCode("");
/*    */   
/*    */   private final String key;
/*    */ 
/*    */   
/*    */   public LockCode(String debug1) {
/* 17 */     this.key = debug1;
/*    */   }
/*    */   
/*    */   public boolean unlocksWith(ItemStack debug1) {
/* 21 */     return (this.key.isEmpty() || (!debug1.isEmpty() && debug1.hasCustomHoverName() && this.key.equals(debug1.getHoverName().getString())));
/*    */   }
/*    */   
/*    */   public void addToTag(CompoundTag debug1) {
/* 25 */     if (!this.key.isEmpty()) {
/* 26 */       debug1.putString("Lock", this.key);
/*    */     }
/*    */   }
/*    */   
/*    */   public static LockCode fromTag(CompoundTag debug0) {
/* 31 */     if (debug0.contains("Lock", 8)) {
/* 32 */       return new LockCode(debug0.getString("Lock"));
/*    */     }
/* 34 */     return NO_LOCK;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\LockCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */