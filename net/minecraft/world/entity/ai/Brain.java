/*     */ package net.minecraft.world.entity.ai;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.entity.schedule.Schedule;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Brain<E extends LivingEntity>
/*     */ {
/*  49 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final Supplier<Codec<Brain<E>>> codec;
/*     */   
/*     */   public static final class Provider<E extends LivingEntity> {
/*     */     private final Collection<? extends MemoryModuleType<?>> memoryTypes;
/*     */     private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
/*     */     private final Codec<Brain<E>> codec;
/*     */     
/*     */     private Provider(Collection<? extends MemoryModuleType<?>> debug1, Collection<? extends SensorType<? extends Sensor<? super E>>> debug2) {
/*  58 */       this.memoryTypes = debug1;
/*  59 */       this.sensorTypes = debug2;
/*  60 */       this.codec = Brain.codec(debug1, debug2);
/*     */     }
/*     */     
/*     */     public Brain<E> makeBrain(Dynamic<?> debug1) {
/*  64 */       return this.codec.parse(debug1).resultOrPartial(Brain.LOGGER::error).orElseGet(() -> new Brain<>(this.memoryTypes, this.sensorTypes, ImmutableList.of(), ()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> Provider<E> provider(Collection<? extends MemoryModuleType<?>> debug0, Collection<? extends SensorType<? extends Sensor<? super E>>> debug1) {
/*  69 */     return new Provider<>(debug0, debug1);
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> Codec<Brain<E>> codec(final Collection<? extends MemoryModuleType<?>> memoryTypes, final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes) {
/*  73 */     final MutableObject<Codec<Brain<E>>> codecReference = new MutableObject();
/*     */     
/*  75 */     debug2.setValue((new MapCodec<Brain<E>>()
/*     */         {
/*     */           public <T> Stream<T> keys(DynamicOps<T> debug1) {
/*  78 */             return memoryTypes.stream().flatMap(debug0 -> Util.toStream(debug0.getCodec().map(()))).map(debug1 -> debug0.createString(debug1.toString()));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<Brain<E>> decode(DynamicOps<T> debug1, MapLike<T> debug2) {
/*  83 */             MutableObject<DataResult<ImmutableList.Builder<Brain.MemoryValue<?>>>> debug3 = new MutableObject(DataResult.success(ImmutableList.builder()));
/*     */             
/*  85 */             debug2.entries().forEach(debug3 -> {
/*     */                   DataResult<MemoryModuleType<?>> debug4 = Registry.MEMORY_MODULE_TYPE.parse(debug1, debug3.getFirst());
/*     */                   
/*     */                   DataResult<? extends Brain.MemoryValue<?>> debug5 = debug4.flatMap(());
/*     */                   debug2.setValue(((DataResult)debug2.getValue()).apply2(ImmutableList.Builder::add, debug5));
/*     */                 });
/*  91 */             ImmutableList<Brain.MemoryValue<?>> debug4 = ((DataResult)debug3.getValue()).resultOrPartial(Brain.LOGGER::error).map(ImmutableList.Builder::build).orElseGet(ImmutableList::of);
/*  92 */             return DataResult.success(new Brain<>(memoryTypes, sensorTypes, debug4, codecReference::getValue));
/*     */           }
/*     */           
/*     */           private <T, U> DataResult<Brain.MemoryValue<U>> captureRead(MemoryModuleType<U> debug1, DynamicOps<T> debug2, T debug3) {
/*  96 */             return ((DataResult)debug1.getCodec().map(DataResult::success).orElseGet(() -> DataResult.error("No codec for memory: " + debug0)))
/*  97 */               .flatMap(debug2 -> debug2.parse(debug0, debug1))
/*  98 */               .map(debug1 -> new Brain.MemoryValue(debug0, Optional.of(debug1)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> encode(Brain<E> debug1, DynamicOps<T> debug2, RecordBuilder<T> debug3) {
/* 103 */             debug1.memories().forEach(debug2 -> debug2.serialize(debug0, debug1));
/* 104 */             return debug3;
/*     */           }
/* 106 */         }).fieldOf("memories").codec());
/*     */     
/* 108 */     return (Codec<Brain<E>>)debug2.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   private final Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memories = Maps.newHashMap();
/*     */ 
/*     */   
/* 116 */   private final Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> sensors = Maps.newLinkedHashMap();
/*     */ 
/*     */   
/* 119 */   private final Map<Integer, Map<Activity, Set<Behavior<? super E>>>> availableBehaviorsByPriority = Maps.newTreeMap();
/*     */   
/* 121 */   private Schedule schedule = Schedule.EMPTY;
/*     */ 
/*     */   
/* 124 */   private final Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements = Maps.newHashMap();
/*     */   
/* 126 */   private final Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped = Maps.newHashMap();
/*     */ 
/*     */   
/* 129 */   private Set<Activity> coreActivities = Sets.newHashSet();
/*     */ 
/*     */   
/* 132 */   private final Set<Activity> activeActivities = Sets.newHashSet();
/*     */ 
/*     */   
/* 135 */   private Activity defaultActivity = Activity.IDLE;
/*     */   
/* 137 */   private long lastScheduleUpdate = -9999L;
/*     */   
/*     */   public Brain(Collection<? extends MemoryModuleType<?>> debug1, Collection<? extends SensorType<? extends Sensor<? super E>>> debug2, ImmutableList<MemoryValue<?>> debug3, Supplier<Codec<Brain<E>>> debug4) {
/* 140 */     this.codec = debug4;
/* 141 */     for (MemoryModuleType<?> debug6 : debug1) {
/* 142 */       this.memories.put(debug6, Optional.empty());
/*     */     }
/* 144 */     for (SensorType<? extends Sensor<? super E>> debug6 : debug2) {
/* 145 */       this.sensors.put(debug6, debug6.create());
/*     */     }
/*     */     
/* 148 */     for (Sensor<? super E> debug6 : this.sensors.values()) {
/* 149 */       for (MemoryModuleType<?> debug8 : (Iterable<MemoryModuleType<?>>)debug6.requires()) {
/* 150 */         this.memories.put(debug8, Optional.empty());
/*     */       }
/*     */     } 
/*     */     
/* 154 */     for (UnmodifiableIterator<MemoryValue> unmodifiableIterator = debug3.iterator(); unmodifiableIterator.hasNext(); ) { MemoryValue<?> debug6 = unmodifiableIterator.next();
/* 155 */       debug6.setMemoryInternal(this); }
/*     */   
/*     */   }
/*     */   
/*     */   public <T> DataResult<T> serializeStart(DynamicOps<T> debug1) {
/* 160 */     return ((Codec)this.codec.get()).encodeStart(debug1, this);
/*     */   }
/*     */   
/*     */   static final class MemoryValue<U>
/*     */   {
/*     */     private final MemoryModuleType<U> type;
/*     */     private final Optional<? extends ExpirableValue<U>> value;
/*     */     
/*     */     private static <U> MemoryValue<U> createUnchecked(MemoryModuleType<U> debug0, Optional<? extends ExpirableValue<?>> debug1) {
/* 169 */       return new MemoryValue<>(debug0, (Optional)debug1);
/*     */     }
/*     */     
/*     */     private MemoryValue(MemoryModuleType<U> debug1, Optional<? extends ExpirableValue<U>> debug2) {
/* 173 */       this.type = debug1;
/* 174 */       this.value = debug2;
/*     */     }
/*     */     
/*     */     private void setMemoryInternal(Brain<?> debug1) {
/* 178 */       debug1.setMemoryInternal(this.type, this.value);
/*     */     }
/*     */     
/*     */     public <T> void serialize(DynamicOps<T> debug1, RecordBuilder<T> debug2) {
/* 182 */       this.type.getCodec().ifPresent(debug3 -> this.value.ifPresent(()));
/*     */     }
/*     */   }
/*     */   
/*     */   private Stream<MemoryValue<?>> memories() {
/* 187 */     return this.memories.entrySet().stream().map(debug0 -> MemoryValue.createUnchecked((MemoryModuleType)debug0.getKey(), (Optional)debug0.getValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMemoryValue(MemoryModuleType<?> debug1) {
/* 194 */     return checkMemory(debug1, MemoryStatus.VALUE_PRESENT);
/*     */   }
/*     */   
/*     */   public <U> void eraseMemory(MemoryModuleType<U> debug1) {
/* 198 */     setMemory(debug1, Optional.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> void setMemory(MemoryModuleType<U> debug1, @Nullable U debug2) {
/* 206 */     setMemory(debug1, Optional.ofNullable(debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> void setMemoryWithExpiry(MemoryModuleType<U> debug1, U debug2, long debug3) {
/* 215 */     setMemoryInternal(debug1, Optional.of(ExpirableValue.of(debug2, debug3)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> void setMemory(MemoryModuleType<U> debug1, Optional<? extends U> debug2) {
/* 223 */     setMemoryInternal(debug1, debug2.map(ExpirableValue::of));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <U> void setMemoryInternal(MemoryModuleType<U> debug1, Optional<? extends ExpirableValue<?>> debug2) {
/* 232 */     if (this.memories.containsKey(debug1)) {
/* 233 */       if (debug2.isPresent() && isEmptyCollection(((ExpirableValue)debug2.get()).getValue())) {
/* 234 */         eraseMemory(debug1);
/*     */       } else {
/* 236 */         this.memories.put(debug1, debug2);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> Optional<U> getMemory(MemoryModuleType<U> debug1) {
/* 243 */     return ((Optional)this.memories.get(debug1)).map(ExpirableValue::getValue);
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
/*     */   public <U> boolean isMemoryValue(MemoryModuleType<U> debug1, U debug2) {
/* 256 */     if (!hasMemoryValue(debug1)) {
/* 257 */       return false;
/*     */     }
/* 259 */     return getMemory(debug1).filter(debug1 -> debug1.equals(debug0)).isPresent();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkMemory(MemoryModuleType<?> debug1, MemoryStatus debug2) {
/* 264 */     Optional<? extends ExpirableValue<?>> debug3 = this.memories.get(debug1);
/* 265 */     if (debug3 == null) {
/* 266 */       return false;
/*     */     }
/*     */     
/* 269 */     return (debug2 == MemoryStatus.REGISTERED || (debug2 == MemoryStatus.VALUE_PRESENT && debug3
/* 270 */       .isPresent()) || (debug2 == MemoryStatus.VALUE_ABSENT && 
/* 271 */       !debug3.isPresent()));
/*     */   }
/*     */   
/*     */   public Schedule getSchedule() {
/* 275 */     return this.schedule;
/*     */   }
/*     */   
/*     */   public void setSchedule(Schedule debug1) {
/* 279 */     this.schedule = debug1;
/*     */   }
/*     */   
/*     */   public void setCoreActivities(Set<Activity> debug1) {
/* 283 */     this.coreActivities = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<Behavior<? super E>> getRunningBehaviors() {
/* 295 */     ObjectArrayList<Behavior<? super E>> objectArrayList = new ObjectArrayList();
/* 296 */     for (Map<Activity, Set<Behavior<? super E>>> debug3 : this.availableBehaviorsByPriority.values()) {
/* 297 */       for (Set<Behavior<? super E>> debug5 : debug3.values()) {
/* 298 */         for (Behavior<? super E> debug7 : debug5) {
/* 299 */           if (debug7.getStatus() == Behavior.Status.RUNNING) {
/* 300 */             objectArrayList.add(debug7);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 305 */     return (List<Behavior<? super E>>)objectArrayList;
/*     */   }
/*     */   
/*     */   public void useDefaultActivity() {
/* 309 */     setActiveActivity(this.defaultActivity);
/*     */   }
/*     */   
/*     */   public Optional<Activity> getActiveNonCoreActivity() {
/* 313 */     for (Activity debug2 : this.activeActivities) {
/* 314 */       if (!this.coreActivities.contains(debug2)) {
/* 315 */         return Optional.of(debug2);
/*     */       }
/*     */     } 
/* 318 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActiveActivityIfPossible(Activity debug1) {
/* 327 */     if (activityRequirementsAreMet(debug1)) {
/* 328 */       setActiveActivity(debug1);
/*     */     } else {
/* 330 */       useDefaultActivity();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setActiveActivity(Activity debug1) {
/* 335 */     if (isActive(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/* 339 */     eraseMemoriesForOtherActivitesThan(debug1);
/* 340 */     this.activeActivities.clear();
/* 341 */     this.activeActivities.addAll(this.coreActivities);
/* 342 */     this.activeActivities.add(debug1);
/*     */   }
/*     */   
/*     */   private void eraseMemoriesForOtherActivitesThan(Activity debug1) {
/* 346 */     for (Activity debug3 : this.activeActivities) {
/* 347 */       if (debug3 != debug1) {
/* 348 */         Set<MemoryModuleType<?>> debug4 = this.activityMemoriesToEraseWhenStopped.get(debug3);
/* 349 */         if (debug4 != null) {
/* 350 */           for (MemoryModuleType<?> debug6 : debug4) {
/* 351 */             eraseMemory(debug6);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateActivityFromSchedule(long debug1, long debug3) {
/* 363 */     if (debug3 - this.lastScheduleUpdate > 20L) {
/* 364 */       this.lastScheduleUpdate = debug3;
/* 365 */       Activity debug5 = getSchedule().getActivityAt((int)(debug1 % 24000L));
/* 366 */       if (!this.activeActivities.contains(debug5)) {
/* 367 */         setActiveActivityIfPossible(debug5);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActiveActivityToFirstValid(List<Activity> debug1) {
/* 376 */     for (Activity debug3 : debug1) {
/* 377 */       if (activityRequirementsAreMet(debug3)) {
/* 378 */         setActiveActivity(debug3);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDefaultActivity(Activity debug1) {
/* 385 */     this.defaultActivity = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActivity(Activity debug1, int debug2, ImmutableList<? extends Behavior<? super E>> debug3) {
/* 392 */     addActivity(debug1, createPriorityPairs(debug2, debug3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActivityAndRemoveMemoryWhenStopped(Activity debug1, int debug2, ImmutableList<? extends Behavior<? super E>> debug3, MemoryModuleType<?> debug4) {
/* 401 */     ImmutableSet immutableSet1 = ImmutableSet.of(
/* 402 */         Pair.of(debug4, MemoryStatus.VALUE_PRESENT));
/*     */     
/* 404 */     ImmutableSet immutableSet2 = ImmutableSet.of(debug4);
/* 405 */     addActivityAndRemoveMemoriesWhenStopped(debug1, createPriorityPairs(debug2, debug3), (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)immutableSet1, (Set<MemoryModuleType<?>>)immutableSet2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActivity(Activity debug1, ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> debug2) {
/* 412 */     addActivityAndRemoveMemoriesWhenStopped(debug1, debug2, (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)ImmutableSet.of(), Sets.newHashSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActivityWithConditions(Activity debug1, ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> debug2, Set<Pair<MemoryModuleType<?>, MemoryStatus>> debug3) {
/* 420 */     addActivityAndRemoveMemoriesWhenStopped(debug1, debug2, debug3, Sets.newHashSet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addActivityAndRemoveMemoriesWhenStopped(Activity debug1, ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> debug2, Set<Pair<MemoryModuleType<?>, MemoryStatus>> debug3, Set<MemoryModuleType<?>> debug4) {
/* 429 */     this.activityRequirements.put(debug1, debug3);
/* 430 */     if (!debug4.isEmpty()) {
/* 431 */       this.activityMemoriesToEraseWhenStopped.put(debug1, debug4);
/*     */     }
/* 433 */     for (UnmodifiableIterator<Pair<Integer, ? extends Behavior<? super E>>> unmodifiableIterator = debug2.iterator(); unmodifiableIterator.hasNext(); ) { Pair<Integer, ? extends Behavior<? super E>> debug6 = unmodifiableIterator.next();
/* 434 */       ((Set<Object>)((Map<Activity, Set<Object>>)this.availableBehaviorsByPriority
/* 435 */         .computeIfAbsent(debug6.getFirst(), debug0 -> Maps.newHashMap()))
/* 436 */         .computeIfAbsent(debug1, debug0 -> Sets.newLinkedHashSet()))
/* 437 */         .add(debug6.getSecond()); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive(Activity debug1) {
/* 447 */     return this.activeActivities.contains(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Brain<E> copyWithoutBehaviors() {
/* 452 */     Brain<E> debug1 = new Brain(this.memories.keySet(), this.sensors.keySet(), ImmutableList.of(), this.codec);
/* 453 */     for (Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> debug3 : this.memories.entrySet()) {
/* 454 */       MemoryModuleType<?> debug4 = debug3.getKey();
/* 455 */       if (((Optional)debug3.getValue()).isPresent()) {
/* 456 */         debug1.memories.put(debug4, debug3.getValue());
/*     */       }
/*     */     } 
/* 459 */     return debug1;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel debug1, E debug2) {
/* 463 */     forgetOutdatedMemories();
/* 464 */     tickSensors(debug1, debug2);
/* 465 */     startEachNonRunningBehavior(debug1, debug2);
/* 466 */     tickEachRunningBehavior(debug1, debug2);
/*     */   }
/*     */   
/*     */   private void tickSensors(ServerLevel debug1, E debug2) {
/* 470 */     for (Sensor<? super E> debug4 : this.sensors.values()) {
/* 471 */       debug4.tick(debug1, (LivingEntity)debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   private void forgetOutdatedMemories() {
/* 476 */     for (Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> debug2 : this.memories.entrySet()) {
/* 477 */       if (((Optional)debug2.getValue()).isPresent()) {
/* 478 */         ExpirableValue<?> debug3 = ((Optional<ExpirableValue>)debug2.getValue()).get();
/* 479 */         debug3.tick();
/* 480 */         if (debug3.hasExpired()) {
/* 481 */           eraseMemory(debug2.getKey());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopAll(ServerLevel debug1, E debug2) {
/* 488 */     long debug3 = ((LivingEntity)debug2).level.getGameTime();
/* 489 */     for (Behavior<? super E> debug6 : getRunningBehaviors()) {
/* 490 */       debug6.doStop(debug1, (LivingEntity)debug2, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startEachNonRunningBehavior(ServerLevel debug1, E debug2) {
/* 498 */     long debug3 = debug1.getGameTime();
/* 499 */     for (Map<Activity, Set<Behavior<? super E>>> debug6 : this.availableBehaviorsByPriority.values()) {
/* 500 */       for (Map.Entry<Activity, Set<Behavior<? super E>>> debug8 : debug6.entrySet()) {
/* 501 */         Activity debug9 = debug8.getKey();
/* 502 */         if (this.activeActivities.contains(debug9)) {
/* 503 */           Set<Behavior<? super E>> debug10 = debug8.getValue();
/* 504 */           for (Behavior<? super E> debug12 : debug10) {
/* 505 */             if (debug12.getStatus() == Behavior.Status.STOPPED) {
/* 506 */               debug12.tryStart(debug1, (LivingEntity)debug2, debug3);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tickEachRunningBehavior(ServerLevel debug1, E debug2) {
/* 519 */     long debug3 = debug1.getGameTime();
/* 520 */     for (Behavior<? super E> debug6 : getRunningBehaviors()) {
/* 521 */       debug6.tickOrStop(debug1, (LivingEntity)debug2, debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean activityRequirementsAreMet(Activity debug1) {
/* 526 */     if (!this.activityRequirements.containsKey(debug1)) {
/* 527 */       return false;
/*     */     }
/*     */     
/* 530 */     for (Pair<MemoryModuleType<?>, MemoryStatus> debug3 : this.activityRequirements.get(debug1)) {
/* 531 */       MemoryModuleType<?> debug4 = (MemoryModuleType)debug3.getFirst();
/* 532 */       MemoryStatus debug5 = (MemoryStatus)debug3.getSecond();
/* 533 */       if (!checkMemory(debug4, debug5)) {
/* 534 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 538 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isEmptyCollection(Object debug1) {
/* 542 */     return (debug1 instanceof Collection && ((Collection)debug1).isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> createPriorityPairs(int debug1, ImmutableList<? extends Behavior<? super E>> debug2) {
/* 549 */     int debug3 = debug1;
/* 550 */     ImmutableList.Builder<Pair<Integer, ? extends Behavior<? super E>>> debug4 = ImmutableList.builder();
/* 551 */     for (UnmodifiableIterator<Behavior<? super E>> unmodifiableIterator = debug2.iterator(); unmodifiableIterator.hasNext(); ) { Behavior<? super E> debug6 = unmodifiableIterator.next();
/* 552 */       debug4.add(Pair.of(Integer.valueOf(debug3++), debug6)); }
/*     */     
/* 554 */     return debug4.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\Brain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */