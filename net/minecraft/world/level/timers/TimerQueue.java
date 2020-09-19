/*     */ package net.minecraft.world.level.timers;
/*     */ 
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.primitives.UnsignedLong;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class TimerQueue<T>
/*     */ {
/*  22 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final TimerCallbacks<T> callbacksRegistry;
/*     */   
/*     */   public static class Event<T>
/*     */   {
/*     */     public final long triggerTime;
/*     */     public final UnsignedLong sequentialId;
/*     */     public final String id;
/*     */     public final TimerCallback<T> callback;
/*     */     
/*     */     private Event(long debug1, UnsignedLong debug3, String debug4, TimerCallback<T> debug5) {
/*  34 */       this.triggerTime = debug1;
/*  35 */       this.sequentialId = debug3;
/*  36 */       this.id = debug4;
/*  37 */       this.callback = debug5;
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> Comparator<Event<T>> createComparator() {
/*  42 */     return Comparator.comparingLong(debug0 -> debug0.triggerTime).thenComparing(debug0 -> debug0.sequentialId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   private final Queue<Event<T>> queue = new PriorityQueue<>((Comparator)createComparator());
/*     */   
/*  49 */   private UnsignedLong sequentialId = UnsignedLong.ZERO;
/*     */   
/*  51 */   private final Table<String, Long, Event<T>> events = (Table<String, Long, Event<T>>)HashBasedTable.create();
/*     */   
/*     */   public TimerQueue(TimerCallbacks<T> debug1, Stream<Dynamic<Tag>> debug2) {
/*  54 */     this(debug1);
/*  55 */     this.queue.clear();
/*  56 */     this.events.clear();
/*  57 */     this.sequentialId = UnsignedLong.ZERO;
/*     */     
/*  59 */     debug2.forEach(debug1 -> {
/*     */           if (!(debug1.getValue() instanceof CompoundTag)) {
/*     */             LOGGER.warn("Invalid format of events: {}", debug1);
/*     */             return;
/*     */           } 
/*     */           loadEvent((CompoundTag)debug1.getValue());
/*     */         });
/*     */   }
/*     */   
/*     */   public TimerQueue(TimerCallbacks<T> debug1) {
/*  69 */     this.callbacksRegistry = debug1;
/*     */   }
/*     */   
/*     */   public void tick(T debug1, long debug2) {
/*     */     while (true) {
/*  74 */       Event<T> debug4 = this.queue.peek();
/*  75 */       if (debug4 == null || debug4.triggerTime > debug2) {
/*     */         break;
/*     */       }
/*     */       
/*  79 */       this.queue.remove();
/*  80 */       this.events.remove(debug4.id, Long.valueOf(debug2));
/*     */       
/*  82 */       debug4.callback.handle(debug1, this, debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void schedule(String debug1, long debug2, TimerCallback<T> debug4) {
/*  87 */     if (this.events.contains(debug1, Long.valueOf(debug2))) {
/*     */       return;
/*     */     }
/*  90 */     this.sequentialId = this.sequentialId.plus(UnsignedLong.ONE);
/*  91 */     Event<T> debug5 = new Event<>(debug2, this.sequentialId, debug1, debug4);
/*  92 */     this.events.put(debug1, Long.valueOf(debug2), debug5);
/*  93 */     this.queue.add(debug5);
/*     */   }
/*     */   
/*     */   public int remove(String debug1) {
/*  97 */     Collection<Event<T>> debug2 = this.events.row(debug1).values();
/*  98 */     debug2.forEach(this.queue::remove);
/*  99 */     int debug3 = debug2.size();
/* 100 */     debug2.clear();
/* 101 */     return debug3;
/*     */   }
/*     */   
/*     */   public Set<String> getEventsIds() {
/* 105 */     return Collections.unmodifiableSet(this.events.rowKeySet());
/*     */   }
/*     */   
/*     */   private void loadEvent(CompoundTag debug1) {
/* 109 */     CompoundTag debug2 = debug1.getCompound("Callback");
/* 110 */     TimerCallback<T> debug3 = this.callbacksRegistry.deserialize(debug2);
/* 111 */     if (debug3 != null) {
/* 112 */       String debug4 = debug1.getString("Name");
/* 113 */       long debug5 = debug1.getLong("TriggerTime");
/* 114 */       schedule(debug4, debug5, debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompoundTag storeEvent(Event<T> debug1) {
/* 119 */     CompoundTag debug2 = new CompoundTag();
/* 120 */     debug2.putString("Name", debug1.id);
/* 121 */     debug2.putLong("TriggerTime", debug1.triggerTime);
/* 122 */     debug2.put("Callback", (Tag)this.callbacksRegistry.serialize(debug1.callback));
/* 123 */     return debug2;
/*     */   }
/*     */   
/*     */   public ListTag store() {
/* 127 */     ListTag debug1 = new ListTag();
/* 128 */     this.queue.stream().sorted(createComparator()).map(this::storeEvent).forEach(debug1::add);
/* 129 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\timers\TimerQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */