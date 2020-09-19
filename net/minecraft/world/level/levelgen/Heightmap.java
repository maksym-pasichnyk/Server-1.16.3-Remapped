/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ public class Heightmap {
/*     */   private static final Predicate<BlockState> NOT_AIR;
/*     */   private static final Predicate<BlockState> MATERIAL_MOTION_BLOCKING;
/*     */   
/*     */   static {
/*  24 */     NOT_AIR = (debug0 -> !debug0.isAir());
/*  25 */     MATERIAL_MOTION_BLOCKING = (debug0 -> debug0.getMaterial().blocksMotion());
/*     */   }
/*     */   
/*  28 */   public enum Usage { WORLDGEN,
/*  29 */     LIVE_WORLD,
/*  30 */     CLIENT; }
/*     */ 
/*     */   
/*     */   public enum Types
/*     */     implements StringRepresentable {
/*  35 */     WORLD_SURFACE_WG("WORLD_SURFACE_WG", Heightmap.Usage.WORLDGEN, (String)Heightmap.NOT_AIR),
/*  36 */     WORLD_SURFACE("WORLD_SURFACE", Heightmap.Usage.CLIENT, (String)Heightmap.NOT_AIR),
/*  37 */     OCEAN_FLOOR_WG("OCEAN_FLOOR_WG", Heightmap.Usage.WORLDGEN, (String)Heightmap.MATERIAL_MOTION_BLOCKING),
/*  38 */     OCEAN_FLOOR("OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, (String)Heightmap.MATERIAL_MOTION_BLOCKING), MOTION_BLOCKING("OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, (String)Heightmap.MATERIAL_MOTION_BLOCKING), MOTION_BLOCKING_NO_LEAVES("OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, (String)Heightmap.MATERIAL_MOTION_BLOCKING); public static final Codec<Types> CODEC; private final String serializationKey; static {
/*  39 */       MOTION_BLOCKING = new Types("MOTION_BLOCKING", 4, "MOTION_BLOCKING", Heightmap.Usage.CLIENT, debug0 -> (debug0.getMaterial().blocksMotion() || !debug0.getFluidState().isEmpty()));
/*  40 */       MOTION_BLOCKING_NO_LEAVES = new Types("MOTION_BLOCKING_NO_LEAVES", 5, "MOTION_BLOCKING_NO_LEAVES", Heightmap.Usage.LIVE_WORLD, debug0 -> ((debug0.getMaterial().blocksMotion() || !debug0.getFluidState().isEmpty()) && !(debug0.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)));
/*     */     }
/*     */     private final Heightmap.Usage usage; private final Predicate<BlockState> isOpaque; private static final Map<String, Types> REVERSE_LOOKUP;
/*     */     static {
/*  44 */       CODEC = StringRepresentable.fromEnum(Types::values, Types::getFromKey);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  50 */       REVERSE_LOOKUP = (Map<String, Types>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */             for (Types debug4 : values())
/*     */               debug0.put(debug4.serializationKey, debug4); 
/*     */           });
/*     */     }
/*     */     
/*     */     Types(String debug3, Heightmap.Usage debug4, Predicate<BlockState> debug5) {
/*  57 */       this.serializationKey = debug3;
/*  58 */       this.usage = debug4;
/*  59 */       this.isOpaque = debug5;
/*     */     }
/*     */     
/*     */     public String getSerializationKey() {
/*  63 */       return this.serializationKey;
/*     */     }
/*     */     
/*     */     public boolean sendToClient() {
/*  67 */       return (this.usage == Heightmap.Usage.CLIENT);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public static Types getFromKey(String debug0) {
/*  76 */       return REVERSE_LOOKUP.get(debug0);
/*     */     }
/*     */     
/*     */     public Predicate<BlockState> isOpaque() {
/*  80 */       return this.isOpaque;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  85 */       return this.serializationKey;
/*     */     }
/*     */   }
/*     */   
/*  89 */   private final BitStorage data = new BitStorage(9, 256);
/*     */   private final Predicate<BlockState> isOpaque;
/*     */   private final ChunkAccess chunk;
/*     */   
/*     */   public Heightmap(ChunkAccess debug1, Types debug2) {
/*  94 */     this.isOpaque = debug2.isOpaque();
/*  95 */     this.chunk = debug1;
/*     */   }
/*     */   
/*     */   public static void primeHeightmaps(ChunkAccess debug0, Set<Types> debug1) {
/*  99 */     int debug2 = debug1.size();
/* 100 */     ObjectArrayList objectArrayList = new ObjectArrayList(debug2);
/* 101 */     ObjectListIterator<Heightmap> debug4 = objectArrayList.iterator();
/*     */     
/* 103 */     int debug5 = debug0.getHighestSectionPosition() + 16;
/* 104 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/* 105 */     for (int debug7 = 0; debug7 < 16; debug7++) {
/* 106 */       for (int debug8 = 0; debug8 < 16; debug8++) {
/* 107 */         for (Types debug10 : debug1) {
/* 108 */           objectArrayList.add(debug0.getOrCreateHeightmapUnprimed(debug10));
/*     */         }
/*     */         
/* 111 */         for (int debug9 = debug5 - 1; debug9 >= 0; debug9--) {
/* 112 */           debug6.set(debug7, debug9, debug8);
/* 113 */           BlockState debug10 = debug0.getBlockState((BlockPos)debug6);
/* 114 */           if (!debug10.is(Blocks.AIR)) {
/*     */ 
/*     */             
/* 117 */             while (debug4.hasNext()) {
/* 118 */               Heightmap debug11 = (Heightmap)debug4.next();
/* 119 */               if (debug11.isOpaque.test(debug10)) {
/* 120 */                 debug11.setHeight(debug7, debug8, debug9 + 1);
/* 121 */                 debug4.remove();
/*     */               } 
/*     */             } 
/* 124 */             if (objectArrayList.isEmpty()) {
/*     */               break;
/*     */             }
/* 127 */             debug4.back(debug2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public boolean update(int debug1, int debug2, int debug3, BlockState debug4) {
/* 134 */     int debug5 = getFirstAvailable(debug1, debug3);
/* 135 */     if (debug2 <= debug5 - 2)
/*     */     {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     if (this.isOpaque.test(debug4)) {
/*     */       
/* 142 */       if (debug2 >= debug5) {
/* 143 */         setHeight(debug1, debug3, debug2 + 1);
/* 144 */         return true;
/*     */       }
/*     */     
/*     */     }
/* 148 */     else if (debug5 - 1 == debug2) {
/* 149 */       BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/* 150 */       for (int debug7 = debug2 - 1; debug7 >= 0; debug7--) {
/* 151 */         debug6.set(debug1, debug7, debug3);
/* 152 */         if (this.isOpaque.test(this.chunk.getBlockState((BlockPos)debug6))) {
/* 153 */           setHeight(debug1, debug3, debug7 + 1);
/* 154 */           return true;
/*     */         } 
/*     */       } 
/* 157 */       setHeight(debug1, debug3, 0);
/* 158 */       return true;
/*     */     } 
/*     */     
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   public int getFirstAvailable(int debug1, int debug2) {
/* 165 */     return getFirstAvailable(getIndex(debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getFirstAvailable(int debug1) {
/* 173 */     return this.data.get(debug1);
/*     */   }
/*     */   
/*     */   private void setHeight(int debug1, int debug2, int debug3) {
/* 177 */     this.data.set(getIndex(debug1, debug2), debug3);
/*     */   }
/*     */   
/*     */   public void setRawData(long[] debug1) {
/* 181 */     System.arraycopy(debug1, 0, this.data.getRaw(), 0, debug1.length);
/*     */   }
/*     */   
/*     */   public long[] getRawData() {
/* 185 */     return this.data.getRaw();
/*     */   }
/*     */   
/*     */   private static int getIndex(int debug0, int debug1) {
/* 189 */     return debug0 + debug1 * 16;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\Heightmap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */