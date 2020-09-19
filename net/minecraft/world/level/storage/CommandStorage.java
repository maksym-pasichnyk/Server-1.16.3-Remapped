/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ 
/*    */ public class CommandStorage {
/*    */   static class Container
/*    */     extends SavedData {
/* 14 */     private final Map<String, CompoundTag> storage = Maps.newHashMap();
/*    */     
/*    */     public Container(String debug1) {
/* 17 */       super(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public void load(CompoundTag debug1) {
/* 22 */       CompoundTag debug2 = debug1.getCompound("contents");
/* 23 */       for (String debug4 : debug2.getAllKeys()) {
/* 24 */         this.storage.put(debug4, debug2.getCompound(debug4));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public CompoundTag save(CompoundTag debug1) {
/* 30 */       CompoundTag debug2 = new CompoundTag();
/* 31 */       this.storage.forEach((debug1, debug2) -> debug0.put(debug1, (Tag)debug2.copy()));
/* 32 */       debug1.put("contents", (Tag)debug2);
/* 33 */       return debug1;
/*    */     }
/*    */     
/*    */     public CompoundTag get(String debug1) {
/* 37 */       CompoundTag debug2 = this.storage.get(debug1);
/* 38 */       return (debug2 != null) ? debug2 : new CompoundTag();
/*    */     }
/*    */     
/*    */     public void put(String debug1, CompoundTag debug2) {
/* 42 */       if (debug2.isEmpty()) {
/* 43 */         this.storage.remove(debug1);
/*    */       } else {
/* 45 */         this.storage.put(debug1, debug2);
/*    */       } 
/* 47 */       setDirty();
/*    */     }
/*    */     
/*    */     public Stream<ResourceLocation> getKeys(String debug1) {
/* 51 */       return this.storage.keySet().stream().map(debug1 -> new ResourceLocation(debug0, debug1));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 56 */   private final Map<String, Container> namespaces = Maps.newHashMap();
/*    */   private final DimensionDataStorage storage;
/*    */   
/*    */   public CommandStorage(DimensionDataStorage debug1) {
/* 60 */     this.storage = debug1;
/*    */   }
/*    */   
/*    */   private Container newStorage(String debug1, String debug2) {
/* 64 */     Container debug3 = new Container(debug2);
/* 65 */     this.namespaces.put(debug1, debug3);
/* 66 */     return debug3;
/*    */   }
/*    */   
/*    */   public CompoundTag get(ResourceLocation debug1) {
/* 70 */     String debug2 = debug1.getNamespace();
/* 71 */     String debug3 = createId(debug2);
/* 72 */     Container debug4 = this.storage.<Container>get(() -> newStorage(debug1, debug2), debug3);
/* 73 */     return (debug4 != null) ? debug4.get(debug1.getPath()) : new CompoundTag();
/*    */   }
/*    */   
/*    */   public void set(ResourceLocation debug1, CompoundTag debug2) {
/* 77 */     String debug3 = debug1.getNamespace();
/* 78 */     String debug4 = createId(debug3);
/* 79 */     ((Container)this.storage.<Container>computeIfAbsent(() -> newStorage(debug1, debug2), debug4)).put(debug1.getPath(), debug2);
/*    */   }
/*    */   
/*    */   public Stream<ResourceLocation> keys() {
/* 83 */     return this.namespaces.entrySet().stream().flatMap(debug0 -> ((Container)debug0.getValue()).getKeys((String)debug0.getKey()));
/*    */   }
/*    */   
/*    */   private static String createId(String debug0) {
/* 87 */     return "command_storage_" + debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\CommandStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */