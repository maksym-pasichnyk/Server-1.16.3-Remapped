/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface Nameable
/*    */ {
/*    */   Component getName();
/*    */   
/*    */   default boolean hasCustomName() {
/* 11 */     return (getCustomName() != null);
/*    */   }
/*    */   
/*    */   default Component getDisplayName() {
/* 15 */     return getName();
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   default Component getCustomName() {
/* 20 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\Nameable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */