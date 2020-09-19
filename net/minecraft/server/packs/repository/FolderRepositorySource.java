/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.packs.FilePackResources;
/*    */ import net.minecraft.server.packs.FolderPackResources;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ 
/*    */ public class FolderRepositorySource implements RepositorySource {
/*    */   static {
/* 13 */     RESOURCEPACK_FILTER = (debug0 -> {
/* 14 */         boolean debug1 = (debug0.isFile() && debug0.getName().endsWith(".zip"));
/* 15 */         boolean debug2 = (debug0.isDirectory() && (new File(debug0, "pack.mcmeta")).isFile());
/*    */         
/* 17 */         return (debug1 || debug2);
/*    */       });
/*    */   }
/*    */   
/*    */   private static final FileFilter RESOURCEPACK_FILTER;
/*    */   
/*    */   public FolderRepositorySource(File debug1, PackSource debug2) {
/* 24 */     this.folder = debug1;
/* 25 */     this.packSource = debug2;
/*    */   }
/*    */   private final File folder; private final PackSource packSource;
/*    */   
/*    */   public void loadPacks(Consumer<Pack> debug1, Pack.PackConstructor debug2) {
/* 30 */     if (!this.folder.isDirectory())
/*    */     {
/* 32 */       this.folder.mkdirs();
/*    */     }
/* 34 */     File[] debug3 = this.folder.listFiles(RESOURCEPACK_FILTER);
/* 35 */     if (debug3 == null) {
/*    */       return;
/*    */     }
/* 38 */     for (File debug7 : debug3) {
/* 39 */       String debug8 = "file/" + debug7.getName();
/* 40 */       Pack debug9 = Pack.create(debug8, false, createSupplier(debug7), debug2, Pack.Position.TOP, this.packSource);
/* 41 */       if (debug9 != null) {
/* 42 */         debug1.accept(debug9);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private Supplier<PackResources> createSupplier(File debug1) {
/* 48 */     if (debug1.isDirectory()) {
/* 49 */       return () -> new FolderPackResources(debug0);
/*    */     }
/* 51 */     return () -> new FilePackResources(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\FolderRepositorySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */