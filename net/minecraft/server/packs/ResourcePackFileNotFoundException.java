/*   */ package net.minecraft.server.packs;
/*   */ 
/*   */ import java.io.File;
/*   */ import java.io.FileNotFoundException;
/*   */ 
/*   */ public class ResourcePackFileNotFoundException extends FileNotFoundException {
/*   */   public ResourcePackFileNotFoundException(File debug1, String debug2) {
/* 8 */     super(String.format("'%s' in ResourcePack '%s'", new Object[] { debug2, debug1 }));
/*   */   }
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\ResourcePackFileNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */