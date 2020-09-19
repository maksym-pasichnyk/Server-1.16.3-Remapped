/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public enum PackCompatibility {
/*  8 */   TOO_OLD("old"),
/*  9 */   TOO_NEW("new"),
/* 10 */   COMPATIBLE("compatible");
/*    */   
/*    */   private final Component description;
/*    */   
/*    */   private final Component confirmation;
/*    */   
/*    */   PackCompatibility(String debug3) {
/* 17 */     this.description = (Component)new TranslatableComponent("pack.incompatible." + debug3);
/* 18 */     this.confirmation = (Component)new TranslatableComponent("pack.incompatible.confirm." + debug3);
/*    */   }
/*    */   
/*    */   public boolean isCompatible() {
/* 22 */     return (this == COMPATIBLE);
/*    */   }
/*    */   
/*    */   public static PackCompatibility forFormat(int debug0) {
/* 26 */     if (debug0 < SharedConstants.getCurrentVersion().getPackVersion())
/* 27 */       return TOO_OLD; 
/* 28 */     if (debug0 > SharedConstants.getCurrentVersion().getPackVersion()) {
/* 29 */       return TOO_NEW;
/*    */     }
/* 31 */     return COMPATIBLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\PackCompatibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */