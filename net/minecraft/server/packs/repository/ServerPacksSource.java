/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.VanillaPackResources;
/*    */ 
/*    */ 
/*    */ public class ServerPacksSource
/*    */   implements RepositorySource
/*    */ {
/* 11 */   private final VanillaPackResources vanillaPack = new VanillaPackResources(new String[] { "minecraft" });
/*    */ 
/*    */   
/*    */   public void loadPacks(Consumer<Pack> debug1, Pack.PackConstructor debug2) {
/* 15 */     Pack debug3 = Pack.create("vanilla", false, () -> this.vanillaPack, debug2, Pack.Position.BOTTOM, PackSource.BUILT_IN);
/* 16 */     if (debug3 != null)
/* 17 */       debug1.accept(debug3); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\ServerPacksSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */