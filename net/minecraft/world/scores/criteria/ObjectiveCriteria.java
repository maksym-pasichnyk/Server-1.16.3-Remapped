/*     */ package net.minecraft.world.scores.criteria;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.stats.StatType;
/*     */ 
/*     */ public class ObjectiveCriteria
/*     */ {
/*  14 */   public static final Map<String, ObjectiveCriteria> CRITERIA_BY_NAME = Maps.newHashMap();
/*  15 */   public static final ObjectiveCriteria DUMMY = new ObjectiveCriteria("dummy");
/*  16 */   public static final ObjectiveCriteria TRIGGER = new ObjectiveCriteria("trigger");
/*  17 */   public static final ObjectiveCriteria DEATH_COUNT = new ObjectiveCriteria("deathCount");
/*  18 */   public static final ObjectiveCriteria KILL_COUNT_PLAYERS = new ObjectiveCriteria("playerKillCount");
/*  19 */   public static final ObjectiveCriteria KILL_COUNT_ALL = new ObjectiveCriteria("totalKillCount");
/*  20 */   public static final ObjectiveCriteria HEALTH = new ObjectiveCriteria("health", true, RenderType.HEARTS);
/*  21 */   public static final ObjectiveCriteria FOOD = new ObjectiveCriteria("food", true, RenderType.INTEGER);
/*  22 */   public static final ObjectiveCriteria AIR = new ObjectiveCriteria("air", true, RenderType.INTEGER);
/*  23 */   public static final ObjectiveCriteria ARMOR = new ObjectiveCriteria("armor", true, RenderType.INTEGER);
/*  24 */   public static final ObjectiveCriteria EXPERIENCE = new ObjectiveCriteria("xp", true, RenderType.INTEGER);
/*  25 */   public static final ObjectiveCriteria LEVEL = new ObjectiveCriteria("level", true, RenderType.INTEGER);
/*  26 */   public static final ObjectiveCriteria[] TEAM_KILL = new ObjectiveCriteria[] { new ObjectiveCriteria("teamkill." + ChatFormatting.BLACK
/*  27 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_BLUE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_GREEN
/*  28 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_AQUA.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_RED
/*  29 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_PURPLE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.GOLD
/*  30 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.GRAY.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_GRAY
/*  31 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.BLUE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.GREEN
/*  32 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.AQUA.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.RED
/*  33 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.LIGHT_PURPLE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.YELLOW
/*  34 */         .getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.WHITE.getName()) };
/*     */   
/*  36 */   public static final ObjectiveCriteria[] KILLED_BY_TEAM = new ObjectiveCriteria[] { new ObjectiveCriteria("killedByTeam." + ChatFormatting.BLACK
/*  37 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_BLUE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_GREEN
/*  38 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_AQUA.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_RED
/*  39 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_PURPLE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.GOLD
/*  40 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.GRAY.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_GRAY
/*  41 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.BLUE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.GREEN
/*  42 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.AQUA.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.RED
/*  43 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.LIGHT_PURPLE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.YELLOW
/*  44 */         .getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.WHITE.getName()) };
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean readOnly;
/*     */   private final RenderType renderType;
/*     */   
/*     */   public ObjectiveCriteria(String debug1) {
/*  52 */     this(debug1, false, RenderType.INTEGER);
/*     */   }
/*     */   
/*     */   protected ObjectiveCriteria(String debug1, boolean debug2, RenderType debug3) {
/*  56 */     this.name = debug1;
/*  57 */     this.readOnly = debug2;
/*  58 */     this.renderType = debug3;
/*  59 */     CRITERIA_BY_NAME.put(debug1, this);
/*     */   }
/*     */   
/*     */   public static Optional<ObjectiveCriteria> byName(String debug0) {
/*  63 */     if (CRITERIA_BY_NAME.containsKey(debug0)) {
/*  64 */       return Optional.of(CRITERIA_BY_NAME.get(debug0));
/*     */     }
/*  66 */     int debug1 = debug0.indexOf(':');
/*  67 */     if (debug1 < 0) {
/*  68 */       return Optional.empty();
/*     */     }
/*  70 */     return Registry.STAT_TYPE.getOptional(ResourceLocation.of(debug0.substring(0, debug1), '.'))
/*  71 */       .flatMap(debug2 -> getStat(debug2, ResourceLocation.of(debug0.substring(debug1 + 1), '.')));
/*     */   }
/*     */   
/*     */   private static <T> Optional<ObjectiveCriteria> getStat(StatType<T> debug0, ResourceLocation debug1) {
/*  75 */     return debug0.getRegistry().getOptional(debug1).map(debug0::get);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  79 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() {
/*  83 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public RenderType getDefaultRenderType() {
/*  87 */     return this.renderType;
/*     */   }
/*     */   
/*     */   public enum RenderType {
/*  91 */     INTEGER("integer"),
/*  92 */     HEARTS("hearts");
/*     */     
/*     */     private final String id;
/*     */     private static final Map<String, RenderType> BY_ID;
/*     */     
/*     */     RenderType(String debug3) {
/*  98 */       this.id = debug3;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 102 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 108 */       ImmutableMap.Builder<String, RenderType> debug0 = ImmutableMap.builder();
/* 109 */       for (RenderType debug4 : values()) {
/* 110 */         debug0.put(debug4.id, debug4);
/*     */       }
/* 112 */       BY_ID = (Map<String, RenderType>)debug0.build();
/*     */     }
/*     */     
/*     */     public static RenderType byId(String debug0) {
/* 116 */       return BY_ID.getOrDefault(debug0, INTEGER);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\scores\criteria\ObjectiveCriteria.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */