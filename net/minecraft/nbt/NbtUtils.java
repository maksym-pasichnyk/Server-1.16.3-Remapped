/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.SerializableUUID;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NbtUtils
/*     */ {
/*  35 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static GameProfile readGameProfile(CompoundTag debug0) {
/*  43 */     String debug1 = null;
/*  44 */     UUID debug2 = null;
/*     */     
/*  46 */     if (debug0.contains("Name", 8)) {
/*  47 */       debug1 = debug0.getString("Name");
/*     */     }
/*  49 */     if (debug0.hasUUID("Id")) {
/*  50 */       debug2 = debug0.getUUID("Id");
/*     */     }
/*     */     
/*     */     try {
/*  54 */       GameProfile debug3 = new GameProfile(debug2, debug1);
/*     */       
/*  56 */       if (debug0.contains("Properties", 10)) {
/*  57 */         CompoundTag debug4 = debug0.getCompound("Properties");
/*     */         
/*  59 */         for (String debug6 : debug4.getAllKeys()) {
/*  60 */           ListTag debug7 = debug4.getList(debug6, 10);
/*  61 */           for (int debug8 = 0; debug8 < debug7.size(); debug8++) {
/*  62 */             CompoundTag debug9 = debug7.getCompound(debug8);
/*  63 */             String debug10 = debug9.getString("Value");
/*     */             
/*  65 */             if (debug9.contains("Signature", 8)) {
/*  66 */               debug3.getProperties().put(debug6, new Property(debug6, debug10, debug9.getString("Signature")));
/*     */             } else {
/*  68 */               debug3.getProperties().put(debug6, new Property(debug6, debug10));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  74 */       return debug3;
/*  75 */     } catch (Throwable throwable) {
/*     */       
/*  77 */       return null;
/*     */     } 
/*     */   }
/*     */   public static CompoundTag writeGameProfile(CompoundTag debug0, GameProfile debug1) {
/*  81 */     if (!StringUtil.isNullOrEmpty(debug1.getName())) {
/*  82 */       debug0.putString("Name", debug1.getName());
/*     */     }
/*  84 */     if (debug1.getId() != null) {
/*  85 */       debug0.putUUID("Id", debug1.getId());
/*     */     }
/*  87 */     if (!debug1.getProperties().isEmpty()) {
/*  88 */       CompoundTag debug2 = new CompoundTag();
/*  89 */       for (String debug4 : debug1.getProperties().keySet()) {
/*  90 */         ListTag debug5 = new ListTag();
/*  91 */         for (Property debug7 : debug1.getProperties().get(debug4)) {
/*  92 */           CompoundTag debug8 = new CompoundTag();
/*  93 */           debug8.putString("Value", debug7.getValue());
/*  94 */           if (debug7.hasSignature()) {
/*  95 */             debug8.putString("Signature", debug7.getSignature());
/*     */           }
/*  97 */           debug5.add(debug8);
/*     */         } 
/*  99 */         debug2.put(debug4, debug5);
/*     */       } 
/* 101 */       debug0.put("Properties", debug2);
/*     */     } 
/*     */     
/* 104 */     return debug0;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static boolean compareNbt(@Nullable Tag debug0, @Nullable Tag debug1, boolean debug2) {
/* 109 */     if (debug0 == debug1) {
/* 110 */       return true;
/*     */     }
/* 112 */     if (debug0 == null) {
/* 113 */       return true;
/*     */     }
/* 115 */     if (debug1 == null) {
/* 116 */       return false;
/*     */     }
/* 118 */     if (!debug0.getClass().equals(debug1.getClass())) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if (debug0 instanceof CompoundTag) {
/* 123 */       CompoundTag debug3 = (CompoundTag)debug0;
/* 124 */       CompoundTag debug4 = (CompoundTag)debug1;
/*     */       
/* 126 */       for (String debug6 : debug3.getAllKeys()) {
/* 127 */         Tag debug7 = debug3.get(debug6);
/* 128 */         if (!compareNbt(debug7, debug4.get(debug6), debug2)) {
/* 129 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 133 */       return true;
/* 134 */     }  if (debug0 instanceof ListTag && debug2) {
/* 135 */       ListTag debug3 = (ListTag)debug0;
/* 136 */       ListTag debug4 = (ListTag)debug1;
/*     */       
/* 138 */       if (debug3.isEmpty()) {
/* 139 */         return debug4.isEmpty();
/*     */       }
/*     */       
/* 142 */       for (int debug5 = 0; debug5 < debug3.size(); debug5++) {
/* 143 */         Tag debug6 = debug3.get(debug5);
/* 144 */         boolean debug7 = false;
/* 145 */         for (int debug8 = 0; debug8 < debug4.size(); debug8++) {
/* 146 */           if (compareNbt(debug6, debug4.get(debug8), debug2)) {
/* 147 */             debug7 = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 151 */         if (!debug7) {
/* 152 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 156 */       return true;
/*     */     } 
/* 158 */     return debug0.equals(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static IntArrayTag createUUID(UUID debug0) {
/* 163 */     return new IntArrayTag(SerializableUUID.uuidToIntArray(debug0));
/*     */   }
/*     */   
/*     */   public static UUID loadUUID(Tag debug0) {
/* 167 */     if (debug0.getType() != IntArrayTag.TYPE) {
/* 168 */       throw new IllegalArgumentException("Expected UUID-Tag to be of type " + IntArrayTag.TYPE.getName() + ", but found " + debug0.getType().getName() + ".");
/*     */     }
/* 170 */     int[] debug1 = ((IntArrayTag)debug0).getAsIntArray();
/* 171 */     if (debug1.length != 4) {
/* 172 */       throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + debug1.length + ".");
/*     */     }
/* 174 */     return SerializableUUID.uuidFromIntArray(debug1);
/*     */   }
/*     */   
/*     */   public static BlockPos readBlockPos(CompoundTag debug0) {
/* 178 */     return new BlockPos(debug0.getInt("X"), debug0.getInt("Y"), debug0.getInt("Z"));
/*     */   }
/*     */   
/*     */   public static CompoundTag writeBlockPos(BlockPos debug0) {
/* 182 */     CompoundTag debug1 = new CompoundTag();
/* 183 */     debug1.putInt("X", debug0.getX());
/* 184 */     debug1.putInt("Y", debug0.getY());
/* 185 */     debug1.putInt("Z", debug0.getZ());
/* 186 */     return debug1;
/*     */   }
/*     */   
/*     */   public static BlockState readBlockState(CompoundTag debug0) {
/* 190 */     if (!debug0.contains("Name", 8)) {
/* 191 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 194 */     Block debug1 = (Block)Registry.BLOCK.get(new ResourceLocation(debug0.getString("Name")));
/* 195 */     BlockState debug2 = debug1.defaultBlockState();
/*     */     
/* 197 */     if (debug0.contains("Properties", 10)) {
/* 198 */       CompoundTag debug3 = debug0.getCompound("Properties");
/*     */       
/* 200 */       StateDefinition<Block, BlockState> debug4 = debug1.getStateDefinition();
/* 201 */       for (String debug6 : debug3.getAllKeys()) {
/* 202 */         Property<?> debug7 = debug4.getProperty(debug6);
/* 203 */         if (debug7 != null) {
/* 204 */           debug2 = setValueHelper(debug2, debug7, debug6, debug3, debug0);
/*     */         }
/*     */       } 
/*     */     } 
/* 208 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <S extends net.minecraft.world.level.block.state.StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S debug0, Property<T> debug1, String debug2, CompoundTag debug3, CompoundTag debug4) {
/* 213 */     Optional<T> debug5 = debug1.getValue(debug3.getString(debug2));
/* 214 */     if (debug5.isPresent()) {
/* 215 */       return (S)debug0.setValue(debug1, (Comparable)debug5.get());
/*     */     }
/*     */     
/* 218 */     LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", debug2, debug3.getString(debug2), debug4.toString());
/* 219 */     return debug0;
/*     */   }
/*     */   
/*     */   public static CompoundTag writeBlockState(BlockState debug0) {
/* 223 */     CompoundTag debug1 = new CompoundTag();
/* 224 */     debug1.putString("Name", Registry.BLOCK.getKey(debug0.getBlock()).toString());
/*     */     
/* 226 */     ImmutableMap<Property<?>, Comparable<?>> debug2 = debug0.getValues();
/* 227 */     if (!debug2.isEmpty()) {
/* 228 */       CompoundTag debug3 = new CompoundTag();
/*     */       
/* 230 */       for (UnmodifiableIterator<Map.Entry<Property<?>, Comparable<?>>> unmodifiableIterator = debug2.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Property<?>, Comparable<?>> debug5 = unmodifiableIterator.next();
/* 231 */         Property<?> debug6 = debug5.getKey();
/* 232 */         debug3.putString(debug6.getName(), getName(debug6, debug5.getValue())); }
/*     */       
/* 234 */       debug1.put("Properties", debug3);
/*     */     } 
/*     */     
/* 237 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T extends Comparable<T>> String getName(Property<T> debug0, Comparable<?> debug1) {
/* 261 */     return debug0.getName(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompoundTag update(DataFixer debug0, DataFixTypes debug1, CompoundTag debug2, int debug3) {
/* 462 */     return update(debug0, debug1, debug2, debug3, SharedConstants.getCurrentVersion().getWorldVersion());
/*     */   }
/*     */   
/*     */   public static CompoundTag update(DataFixer debug0, DataFixTypes debug1, CompoundTag debug2, int debug3, int debug4) {
/* 466 */     return (CompoundTag)debug0.update(debug1.getType(), new Dynamic(NbtOps.INSTANCE, debug2), debug3, debug4).getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\NbtUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */