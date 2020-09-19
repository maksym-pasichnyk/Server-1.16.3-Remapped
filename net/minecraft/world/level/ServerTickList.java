/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerChunkCache;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerTickList<T>
/*     */   implements TickList<T>
/*     */ {
/*     */   protected final Predicate<T> ignore;
/*     */   private final Function<T, ResourceLocation> toId;
/*  36 */   private final Set<TickNextTickData<T>> tickNextTickSet = Sets.newHashSet();
/*  37 */   private final TreeSet<TickNextTickData<T>> tickNextTickList = Sets.newTreeSet(TickNextTickData.createTimeComparator());
/*     */   
/*     */   private final ServerLevel level;
/*  40 */   private final Queue<TickNextTickData<T>> currentlyTicking = Queues.newArrayDeque();
/*  41 */   private final List<TickNextTickData<T>> alreadyTicked = Lists.newArrayList();
/*     */   private final Consumer<TickNextTickData<T>> ticker;
/*     */   
/*     */   public ServerTickList(ServerLevel debug1, Predicate<T> debug2, Function<T, ResourceLocation> debug3, Consumer<TickNextTickData<T>> debug4) {
/*  45 */     this.ignore = debug2;
/*  46 */     this.toId = debug3;
/*  47 */     this.level = debug1;
/*  48 */     this.ticker = debug4;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  52 */     int debug1 = this.tickNextTickList.size();
/*  53 */     if (debug1 != this.tickNextTickSet.size()) {
/*  54 */       throw new IllegalStateException("TickNextTick list out of synch");
/*     */     }
/*  56 */     if (debug1 > 65536) {
/*  57 */       debug1 = 65536;
/*     */     }
/*     */     
/*  60 */     ServerChunkCache debug2 = this.level.getChunkSource();
/*  61 */     Iterator<TickNextTickData<T>> debug3 = this.tickNextTickList.iterator();
/*  62 */     this.level.getProfiler().push("cleaning");
/*  63 */     while (debug1 > 0 && debug3.hasNext()) {
/*  64 */       TickNextTickData<T> tickNextTickData = debug3.next();
/*  65 */       if (tickNextTickData.triggerTick > this.level.getGameTime()) {
/*     */         break;
/*     */       }
/*     */       
/*  69 */       if (!debug2.isTickingChunk(tickNextTickData.pos)) {
/*     */         continue;
/*     */       }
/*     */       
/*  73 */       debug3.remove();
/*  74 */       this.tickNextTickSet.remove(tickNextTickData);
/*  75 */       this.currentlyTicking.add(tickNextTickData);
/*  76 */       debug1--;
/*     */     } 
/*  78 */     this.level.getProfiler().popPush("ticking");
/*     */     TickNextTickData<T> debug4;
/*  80 */     while ((debug4 = this.currentlyTicking.poll()) != null) {
/*  81 */       if (debug2.isTickingChunk(debug4.pos)) {
/*     */         try {
/*  83 */           this.alreadyTicked.add(debug4);
/*  84 */           this.ticker.accept(debug4);
/*  85 */         } catch (Throwable debug5) {
/*  86 */           CrashReport debug6 = CrashReport.forThrowable(debug5, "Exception while ticking");
/*  87 */           CrashReportCategory debug7 = debug6.addCategory("Block being ticked");
/*  88 */           CrashReportCategory.populateBlockDetails(debug7, debug4.pos, null);
/*  89 */           throw new ReportedException(debug6);
/*     */         }  continue;
/*     */       } 
/*  92 */       scheduleTick(debug4.pos, debug4.getType(), 0);
/*     */     } 
/*     */     
/*  95 */     this.level.getProfiler().pop();
/*     */     
/*  97 */     this.alreadyTicked.clear();
/*  98 */     this.currentlyTicking.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean willTickThisTick(BlockPos debug1, T debug2) {
/* 104 */     return this.currentlyTicking.contains(new TickNextTickData<>(debug1, debug2));
/*     */   }
/*     */   
/*     */   public List<TickNextTickData<T>> fetchTicksInChunk(ChunkPos debug1, boolean debug2, boolean debug3) {
/* 108 */     int debug4 = (debug1.x << 4) - 2;
/* 109 */     int debug5 = debug4 + 16 + 2;
/* 110 */     int debug6 = (debug1.z << 4) - 2;
/* 111 */     int debug7 = debug6 + 16 + 2;
/*     */     
/* 113 */     return fetchTicksInArea(new BoundingBox(debug4, 0, debug6, debug5, 256, debug7), debug2, debug3);
/*     */   }
/*     */   
/*     */   public List<TickNextTickData<T>> fetchTicksInArea(BoundingBox debug1, boolean debug2, boolean debug3) {
/* 117 */     List<TickNextTickData<T>> debug4 = fetchTicksInArea((List<TickNextTickData<T>>)null, this.tickNextTickList, debug1, debug2);
/* 118 */     if (debug2 && debug4 != null) {
/* 119 */       this.tickNextTickSet.removeAll(debug4);
/*     */     }
/* 121 */     debug4 = fetchTicksInArea(debug4, this.currentlyTicking, debug1, debug2);
/* 122 */     if (!debug3) {
/* 123 */       debug4 = fetchTicksInArea(debug4, this.alreadyTicked, debug1, debug2);
/*     */     }
/* 125 */     return (debug4 == null) ? Collections.<TickNextTickData<T>>emptyList() : debug4;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private List<TickNextTickData<T>> fetchTicksInArea(@Nullable List<TickNextTickData<T>> debug1, Collection<TickNextTickData<T>> debug2, BoundingBox debug3, boolean debug4) {
/* 130 */     Iterator<TickNextTickData<T>> debug5 = debug2.iterator();
/* 131 */     while (debug5.hasNext()) {
/* 132 */       TickNextTickData<T> debug6 = debug5.next();
/* 133 */       BlockPos debug7 = debug6.pos;
/* 134 */       if (debug7.getX() >= debug3.x0 && debug7.getX() < debug3.x1 && debug7.getZ() >= debug3.z0 && debug7.getZ() < debug3.z1) {
/* 135 */         if (debug4) {
/* 136 */           debug5.remove();
/*     */         }
/* 138 */         if (debug1 == null) {
/* 139 */           debug1 = Lists.newArrayList();
/*     */         }
/* 141 */         debug1.add(debug6);
/*     */       } 
/*     */     } 
/* 144 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void copy(BoundingBox debug1, BlockPos debug2) {
/* 149 */     List<TickNextTickData<T>> debug3 = fetchTicksInArea(debug1, false, false);
/* 150 */     for (TickNextTickData<T> debug5 : debug3) {
/* 151 */       if (debug1.isInside((Vec3i)debug5.pos)) {
/* 152 */         BlockPos debug6 = debug5.pos.offset((Vec3i)debug2);
/* 153 */         T debug7 = debug5.getType();
/* 154 */         addTickData(new TickNextTickData<>(debug6, debug7, debug5.triggerTick, debug5.priority));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListTag save(ChunkPos debug1) {
/* 161 */     List<TickNextTickData<T>> debug2 = fetchTicksInChunk(debug1, false, true);
/* 162 */     return saveTickList(this.toId, debug2, this.level.getGameTime());
/*     */   }
/*     */   
/*     */   private static <T> ListTag saveTickList(Function<T, ResourceLocation> debug0, Iterable<TickNextTickData<T>> debug1, long debug2) {
/* 166 */     ListTag debug4 = new ListTag();
/* 167 */     for (TickNextTickData<T> debug6 : debug1) {
/* 168 */       CompoundTag debug7 = new CompoundTag();
/* 169 */       debug7.putString("i", ((ResourceLocation)debug0.apply(debug6.getType())).toString());
/* 170 */       debug7.putInt("x", debug6.pos.getX());
/* 171 */       debug7.putInt("y", debug6.pos.getY());
/* 172 */       debug7.putInt("z", debug6.pos.getZ());
/* 173 */       debug7.putInt("t", (int)(debug6.triggerTick - debug2));
/* 174 */       debug7.putInt("p", debug6.priority.getValue());
/*     */       
/* 176 */       debug4.add(debug7);
/*     */     } 
/*     */     
/* 179 */     return debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasScheduledTick(BlockPos debug1, T debug2) {
/* 188 */     return this.tickNextTickSet.contains(new TickNextTickData<>(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleTick(BlockPos debug1, T debug2, int debug3, TickPriority debug4) {
/* 193 */     if (!this.ignore.test(debug2)) {
/* 194 */       addTickData(new TickNextTickData<>(debug1, debug2, debug3 + this.level.getGameTime(), debug4));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addTickData(TickNextTickData<T> debug1) {
/* 199 */     if (!this.tickNextTickSet.contains(debug1)) {
/* 200 */       this.tickNextTickSet.add(debug1);
/* 201 */       this.tickNextTickList.add(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 207 */     return this.tickNextTickSet.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ServerTickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */