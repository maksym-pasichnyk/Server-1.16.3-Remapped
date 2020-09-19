/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public interface PackSource
/*    */ {
/*  8 */   public static final PackSource DEFAULT = passThrough();
/*  9 */   public static final PackSource BUILT_IN = decorating("pack.source.builtin");
/*    */ 
/*    */   
/* 12 */   public static final PackSource WORLD = decorating("pack.source.world");
/* 13 */   public static final PackSource SERVER = decorating("pack.source.server");
/*    */ 
/*    */ 
/*    */   
/*    */   static PackSource passThrough() {
/* 18 */     return debug0 -> debug0;
/*    */   }
/*    */   
/*    */   static PackSource decorating(String debug0) {
/* 22 */     TranslatableComponent translatableComponent = new TranslatableComponent(debug0);
/* 23 */     return debug1 -> new TranslatableComponent("pack.nameAndSource", new Object[] { debug1, debug0 });
/*    */   }
/*    */   
/*    */   Component decorate(Component paramComponent);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\PackSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */