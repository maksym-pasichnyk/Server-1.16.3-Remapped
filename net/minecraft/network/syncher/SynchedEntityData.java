/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class SynchedEntityData
/*     */ {
/*  25 */   private static final Logger LOGGER = LogManager.getLogger();
/*  26 */   private static final Map<Class<? extends Entity>, Integer> ENTITY_ID_POOL = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   private final Entity entity;
/*     */   
/*  31 */   private final Map<Integer, DataItem<?>> itemsById = Maps.newHashMap();
/*  32 */   private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */   
/*     */   private boolean isEmpty = true;
/*     */   private boolean isDirty;
/*     */   
/*     */   public SynchedEntityData(Entity debug1) {
/*  38 */     this.entity = debug1;
/*     */   }
/*     */   public static <T> EntityDataAccessor<T> defineId(Class<? extends Entity> debug0, EntityDataSerializer<T> debug1) {
/*     */     int debug2;
/*  42 */     if (LOGGER.isDebugEnabled()) {
/*     */       try {
/*  44 */         Class<?> clazz = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
/*  45 */         if (!clazz.equals(debug0)) {
/*  46 */           LOGGER.debug("defineId called for: {} from {}", debug0, clazz, new RuntimeException());
/*     */         }
/*  48 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  53 */     if (ENTITY_ID_POOL.containsKey(debug0)) {
/*  54 */       debug2 = ((Integer)ENTITY_ID_POOL.get(debug0)).intValue() + 1;
/*     */     } else {
/*  56 */       int debug3 = 0;
/*  57 */       Class<?> debug4 = debug0;
/*  58 */       while (debug4 != Entity.class) {
/*  59 */         debug4 = debug4.getSuperclass();
/*  60 */         if (ENTITY_ID_POOL.containsKey(debug4)) {
/*  61 */           debug3 = ((Integer)ENTITY_ID_POOL.get(debug4)).intValue() + 1;
/*     */           break;
/*     */         } 
/*     */       } 
/*  65 */       debug2 = debug3;
/*     */     } 
/*  67 */     if (debug2 > 254) {
/*  68 */       throw new IllegalArgumentException("Data value id is too big with " + debug2 + "! (Max is " + 'þ' + ")");
/*     */     }
/*  70 */     ENTITY_ID_POOL.put(debug0, Integer.valueOf(debug2));
/*  71 */     return debug1.createAccessor(debug2);
/*     */   }
/*     */   
/*     */   public <T> void define(EntityDataAccessor<T> debug1, T debug2) {
/*  75 */     int debug3 = debug1.getId();
/*  76 */     if (debug3 > 254) {
/*  77 */       throw new IllegalArgumentException("Data value id is too big with " + debug3 + "! (Max is " + 'þ' + ")");
/*     */     }
/*  79 */     if (this.itemsById.containsKey(Integer.valueOf(debug3))) {
/*  80 */       throw new IllegalArgumentException("Duplicate id value for " + debug3 + "!");
/*     */     }
/*  82 */     if (EntityDataSerializers.getSerializedId(debug1.getSerializer()) < 0) {
/*  83 */       throw new IllegalArgumentException("Unregistered serializer " + debug1.getSerializer() + " for " + debug3 + "!");
/*     */     }
/*     */     
/*  86 */     createDataItem(debug1, debug2);
/*     */   }
/*     */   
/*     */   private <T> void createDataItem(EntityDataAccessor<T> debug1, T debug2) {
/*  90 */     DataItem<T> debug3 = new DataItem<>(debug1, debug2);
/*  91 */     this.lock.writeLock().lock();
/*  92 */     this.itemsById.put(Integer.valueOf(debug1.getId()), debug3);
/*  93 */     this.isEmpty = false;
/*  94 */     this.lock.writeLock().unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> DataItem<T> getItem(EntityDataAccessor<T> debug1) {
/*     */     DataItem<T> debug2;
/* 100 */     this.lock.readLock().lock();
/*     */     
/*     */     try {
/* 103 */       debug2 = (DataItem<T>)this.itemsById.get(Integer.valueOf(debug1.getId()));
/* 104 */     } catch (Throwable debug3) {
/* 105 */       CrashReport debug4 = CrashReport.forThrowable(debug3, "Getting synched entity data");
/* 106 */       CrashReportCategory debug5 = debug4.addCategory("Synched entity data");
/*     */       
/* 108 */       debug5.setDetail("Data ID", debug1);
/* 109 */       throw new ReportedException(debug4);
/*     */     } finally {
/* 111 */       this.lock.readLock().unlock();
/*     */     } 
/* 113 */     return debug2;
/*     */   }
/*     */   
/*     */   public <T> T get(EntityDataAccessor<T> debug1) {
/* 117 */     return getItem(debug1).getValue();
/*     */   }
/*     */   
/*     */   public <T> void set(EntityDataAccessor<T> debug1, T debug2) {
/* 121 */     DataItem<T> debug3 = getItem(debug1);
/*     */ 
/*     */     
/* 124 */     if (ObjectUtils.notEqual(debug2, debug3.getValue())) {
/* 125 */       debug3.setValue(debug2);
/* 126 */       this.entity.onSyncedDataUpdated(debug1);
/* 127 */       debug3.setDirty(true);
/* 128 */       this.isDirty = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isDirty() {
/* 133 */     return this.isDirty;
/*     */   }
/*     */   
/*     */   public static void pack(List<DataItem<?>> debug0, FriendlyByteBuf debug1) throws IOException {
/* 137 */     if (debug0 != null) {
/* 138 */       for (int debug2 = 0, debug3 = debug0.size(); debug2 < debug3; debug2++) {
/* 139 */         writeDataItem(debug1, debug0.get(debug2));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 144 */     debug1.writeByte(255);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public List<DataItem<?>> packDirty() {
/* 149 */     List<DataItem<?>> debug1 = null;
/*     */     
/* 151 */     if (this.isDirty) {
/* 152 */       this.lock.readLock().lock();
/* 153 */       for (DataItem<?> debug3 : this.itemsById.values()) {
/* 154 */         if (debug3.isDirty()) {
/* 155 */           debug3.setDirty(false);
/*     */           
/* 157 */           if (debug1 == null) {
/* 158 */             debug1 = Lists.newArrayList();
/*     */           }
/* 160 */           debug1.add(debug3.copy());
/*     */         } 
/*     */       } 
/* 163 */       this.lock.readLock().unlock();
/*     */     } 
/* 165 */     this.isDirty = false;
/*     */     
/* 167 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public List<DataItem<?>> getAll() {
/* 172 */     List<DataItem<?>> debug1 = null;
/*     */     
/* 174 */     this.lock.readLock().lock();
/* 175 */     for (DataItem<?> debug3 : this.itemsById.values()) {
/* 176 */       if (debug1 == null) {
/* 177 */         debug1 = Lists.newArrayList();
/*     */       }
/* 179 */       debug1.add(debug3.copy());
/*     */     } 
/* 181 */     this.lock.readLock().unlock();
/*     */     
/* 183 */     return debug1;
/*     */   }
/*     */   
/*     */   private static <T> void writeDataItem(FriendlyByteBuf debug0, DataItem<T> debug1) throws IOException {
/* 187 */     EntityDataAccessor<T> debug2 = debug1.getAccessor();
/* 188 */     int debug3 = EntityDataSerializers.getSerializedId(debug2.getSerializer());
/* 189 */     if (debug3 < 0) {
/* 190 */       throw new EncoderException("Unknown serializer type " + debug2.getSerializer());
/*     */     }
/* 192 */     debug0.writeByte(debug2.getId());
/* 193 */     debug0.writeVarInt(debug3);
/* 194 */     debug2.getSerializer().write(debug0, debug1.getValue());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static List<DataItem<?>> unpack(FriendlyByteBuf debug0) throws IOException {
/* 199 */     List<DataItem<?>> debug1 = null;
/*     */     
/*     */     int debug2;
/* 202 */     while ((debug2 = debug0.readUnsignedByte()) != 255) {
/* 203 */       if (debug1 == null) {
/* 204 */         debug1 = Lists.newArrayList();
/*     */       }
/*     */       
/* 207 */       int debug3 = debug0.readVarInt();
/* 208 */       EntityDataSerializer<?> debug4 = EntityDataSerializers.getSerializer(debug3);
/* 209 */       if (debug4 == null) {
/* 210 */         throw new DecoderException("Unknown serializer type " + debug3);
/*     */       }
/*     */       
/* 213 */       debug1.add(genericHelper(debug0, debug2, debug4));
/*     */     } 
/*     */     
/* 216 */     return debug1;
/*     */   }
/*     */   
/*     */   private static <T> DataItem<T> genericHelper(FriendlyByteBuf debug0, int debug1, EntityDataSerializer<T> debug2) {
/* 220 */     return new DataItem<>(debug2.createAccessor(debug1), debug2.read(debug0));
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
/*     */   public boolean isEmpty() {
/* 247 */     return this.isEmpty;
/*     */   }
/*     */   
/*     */   public void clearDirty() {
/* 251 */     this.isDirty = false;
/*     */     
/* 253 */     this.lock.readLock().lock();
/* 254 */     for (DataItem<?> debug2 : this.itemsById.values()) {
/* 255 */       debug2.setDirty(false);
/*     */     }
/* 257 */     this.lock.readLock().unlock();
/*     */   }
/*     */   
/*     */   public static class DataItem<T> {
/*     */     private final EntityDataAccessor<T> accessor;
/*     */     private T value;
/*     */     private boolean dirty;
/*     */     
/*     */     public DataItem(EntityDataAccessor<T> debug1, T debug2) {
/* 266 */       this.accessor = debug1;
/* 267 */       this.value = debug2;
/* 268 */       this.dirty = true;
/*     */     }
/*     */     
/*     */     public EntityDataAccessor<T> getAccessor() {
/* 272 */       return this.accessor;
/*     */     }
/*     */     
/*     */     public void setValue(T debug1) {
/* 276 */       this.value = debug1;
/*     */     }
/*     */     
/*     */     public T getValue() {
/* 280 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean isDirty() {
/* 284 */       return this.dirty;
/*     */     }
/*     */     
/*     */     public void setDirty(boolean debug1) {
/* 288 */       this.dirty = debug1;
/*     */     }
/*     */     
/*     */     public DataItem<T> copy() {
/* 292 */       return new DataItem(this.accessor, this.accessor.getSerializer().copy(this.value));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\syncher\SynchedEntityData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */