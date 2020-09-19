/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public abstract class SavedData {
/* 13 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   private final String id;
/*    */   private boolean dirty;
/*    */   
/*    */   public SavedData(String debug1) {
/* 18 */     this.id = debug1;
/*    */   }
/*    */   
/*    */   public abstract void load(CompoundTag paramCompoundTag);
/*    */   
/*    */   public abstract CompoundTag save(CompoundTag paramCompoundTag);
/*    */   
/*    */   public void setDirty() {
/* 26 */     setDirty(true);
/*    */   }
/*    */   
/*    */   public void setDirty(boolean debug1) {
/* 30 */     this.dirty = debug1;
/*    */   }
/*    */   
/*    */   public boolean isDirty() {
/* 34 */     return this.dirty;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 38 */     return this.id;
/*    */   }
/*    */   
/*    */   public void save(File debug1) {
/* 42 */     if (!isDirty()) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     CompoundTag debug2 = new CompoundTag();
/* 47 */     debug2.put("data", (Tag)save(new CompoundTag()));
/* 48 */     debug2.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
/*    */     
/*    */     try {
/* 51 */       NbtIo.writeCompressed(debug2, debug1);
/* 52 */     } catch (IOException debug3) {
/* 53 */       LOGGER.error("Could not save data {}", this, debug3);
/*    */     } 
/* 55 */     setDirty(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\saveddata\SavedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */