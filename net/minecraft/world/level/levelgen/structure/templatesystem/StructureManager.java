/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.FileUtil;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureManager
/*     */ {
/*  36 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private final Map<ResourceLocation, StructureTemplate> structureRepository = Maps.newHashMap();
/*     */   private final DataFixer fixerUpper;
/*     */   private ResourceManager resourceManager;
/*     */   private final Path generatedDir;
/*     */   
/*     */   public StructureManager(ResourceManager debug1, LevelStorageSource.LevelStorageAccess debug2, DataFixer debug3) {
/*  48 */     this.resourceManager = debug1;
/*  49 */     this.fixerUpper = debug3;
/*  50 */     this.generatedDir = debug2.getLevelPath(LevelResource.GENERATED_DIR).normalize();
/*     */   }
/*     */   
/*     */   public StructureTemplate getOrCreate(ResourceLocation debug1) {
/*  54 */     StructureTemplate debug2 = get(debug1);
/*  55 */     if (debug2 == null) {
/*  56 */       debug2 = new StructureTemplate();
/*  57 */       this.structureRepository.put(debug1, debug2);
/*     */     } 
/*  59 */     return debug2;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public StructureTemplate get(ResourceLocation debug1) {
/*  64 */     return this.structureRepository.computeIfAbsent(debug1, debug1 -> {
/*     */           StructureTemplate debug2 = loadFromGenerated(debug1);
/*     */           return (debug2 != null) ? debug2 : loadFromResource(debug1);
/*     */         });
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager debug1) {
/*  71 */     this.resourceManager = debug1;
/*  72 */     this.structureRepository.clear();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private StructureTemplate loadFromResource(ResourceLocation debug1) {
/*  77 */     ResourceLocation debug2 = new ResourceLocation(debug1.getNamespace(), "structures/" + debug1.getPath() + ".nbt");
/*  78 */     try (Resource debug3 = this.resourceManager.getResource(debug2)) {
/*  79 */       return readStructure(debug3.getInputStream());
/*  80 */     } catch (FileNotFoundException debug3) {
/*  81 */       return null;
/*  82 */     } catch (Throwable debug3) {
/*  83 */       LOGGER.error("Couldn't load structure {}: {}", debug1, debug3.toString());
/*  84 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private StructureTemplate loadFromGenerated(ResourceLocation debug1) {
/*  90 */     if (!this.generatedDir.toFile().isDirectory()) {
/*  91 */       return null;
/*     */     }
/*     */     
/*  94 */     Path debug2 = createAndValidatePathToStructure(debug1, ".nbt");
/*     */     
/*  96 */     try (InputStream debug3 = new FileInputStream(debug2.toFile())) {
/*  97 */       return readStructure(debug3);
/*  98 */     } catch (FileNotFoundException debug3) {
/*  99 */       return null;
/* 100 */     } catch (IOException debug3) {
/* 101 */       LOGGER.error("Couldn't load structure from {}", debug2, debug3);
/* 102 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private StructureTemplate readStructure(InputStream debug1) throws IOException {
/* 107 */     CompoundTag debug2 = NbtIo.readCompressed(debug1);
/* 108 */     return readStructure(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureTemplate readStructure(CompoundTag debug1) {
/* 113 */     if (!debug1.contains("DataVersion", 99)) {
/* 114 */       debug1.putInt("DataVersion", 500);
/*     */     }
/*     */     
/* 117 */     StructureTemplate debug2 = new StructureTemplate();
/* 118 */     debug2.load(NbtUtils.update(this.fixerUpper, DataFixTypes.STRUCTURE, debug1, debug1.getInt("DataVersion")));
/* 119 */     return debug2;
/*     */   }
/*     */   
/*     */   public boolean save(ResourceLocation debug1) {
/* 123 */     StructureTemplate debug2 = this.structureRepository.get(debug1);
/* 124 */     if (debug2 == null) {
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     Path debug3 = createAndValidatePathToStructure(debug1, ".nbt");
/*     */     
/* 130 */     Path debug4 = debug3.getParent();
/* 131 */     if (debug4 == null) {
/* 132 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 136 */       Files.createDirectories(Files.exists(debug4, new java.nio.file.LinkOption[0]) ? debug4.toRealPath(new java.nio.file.LinkOption[0]) : debug4, (FileAttribute<?>[])new FileAttribute[0]);
/* 137 */     } catch (IOException iOException) {
/* 138 */       LOGGER.error("Failed to create parent directory: {}", debug4);
/* 139 */       return false;
/*     */     } 
/*     */     
/* 142 */     CompoundTag debug5 = debug2.save(new CompoundTag());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     try (OutputStream debug6 = new FileOutputStream(debug3.toFile())) {
/* 151 */       NbtIo.writeCompressed(debug5, debug6);
/* 152 */     } catch (Throwable debug6) {
/* 153 */       return false;
/*     */     } 
/*     */     
/* 156 */     return true;
/*     */   }
/*     */   
/*     */   public Path createPathToStructure(ResourceLocation debug1, String debug2) {
/*     */     try {
/* 161 */       Path debug3 = this.generatedDir.resolve(debug1.getNamespace());
/* 162 */       Path debug4 = debug3.resolve("structures");
/* 163 */       return FileUtil.createPathToResource(debug4, debug1.getPath(), debug2);
/* 164 */     } catch (InvalidPathException debug3) {
/* 165 */       throw new ResourceLocationException("Invalid resource path: " + debug1, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Path createAndValidatePathToStructure(ResourceLocation debug1, String debug2) {
/* 170 */     if (debug1.getPath().contains("//")) {
/* 171 */       throw new ResourceLocationException("Invalid resource path: " + debug1);
/*     */     }
/*     */     
/* 174 */     Path debug3 = createPathToStructure(debug1, debug2);
/*     */     
/* 176 */     if (!debug3.startsWith(this.generatedDir) || !FileUtil.isPathNormalized(debug3) || !FileUtil.isPathPortable(debug3)) {
/* 177 */       throw new ResourceLocationException("Invalid resource path: " + debug3);
/*     */     }
/*     */     
/* 180 */     return debug3;
/*     */   }
/*     */   
/*     */   public void remove(ResourceLocation debug1) {
/* 184 */     this.structureRepository.remove(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */