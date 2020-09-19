/*     */ package net.minecraft.util.monitoring.jmx;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MinecraftServerStatistics
/*     */   implements DynamicMBean
/*     */ {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   private final MinecraftServer server;
/*     */   
/*     */   private MinecraftServerStatistics(MinecraftServer debug1) {
/*  34 */     this
/*     */ 
/*     */       
/*  37 */       .attributeDescriptionByName = (Map<String, AttributeDescription>)Stream.<AttributeDescription>of(new AttributeDescription[] { new AttributeDescription("tickTimes", this::getTickTimes, "Historical tick times (ms)", long[].class), new AttributeDescription("averageTickTime", this::getAverageTickTime, "Current average tick time (ms)", long.class) }).collect(Collectors.toMap(debug0 -> debug0.name, Function.identity()));
/*     */ 
/*     */     
/*  40 */     this.server = debug1;
/*     */ 
/*     */ 
/*     */     
/*  44 */     MBeanAttributeInfo[] debug2 = (MBeanAttributeInfo[])this.attributeDescriptionByName.values().stream().map(debug0 -> ((AttributeDescription)debug0).asMBeanAttributeInfo()).toArray(debug0 -> new MBeanAttributeInfo[debug0]);
/*     */     
/*  46 */     this.mBeanInfo = new MBeanInfo(MinecraftServerStatistics.class.getSimpleName(), "metrics for dedicated server", debug2, null, null, new javax.management.MBeanNotificationInfo[0]);
/*     */   }
/*     */   private final MBeanInfo mBeanInfo; private final Map<String, AttributeDescription> attributeDescriptionByName;
/*     */   public static void registerJmxMonitoring(MinecraftServer debug0) {
/*     */     try {
/*  51 */       ManagementFactory.getPlatformMBeanServer().registerMBean(new MinecraftServerStatistics(debug0), new ObjectName("net.minecraft.server:type=Server"));
/*     */ 
/*     */     
/*     */     }
/*  55 */     catch (MalformedObjectNameException|javax.management.InstanceAlreadyExistsException|javax.management.MBeanRegistrationException|javax.management.NotCompliantMBeanException debug1) {
/*  56 */       LOGGER.warn("Failed to initialise server as JMX bean", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getAverageTickTime() {
/*  61 */     return this.server.getAverageTickTime();
/*     */   }
/*     */   
/*     */   private long[] getTickTimes() {
/*  65 */     return this.server.tickTimes;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getAttribute(String debug1) {
/*  71 */     AttributeDescription debug2 = this.attributeDescriptionByName.get(debug1);
/*  72 */     return (debug2 == null) ? null : debug2
/*     */       
/*  74 */       .getter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(Attribute debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList getAttributes(String[] debug1) {
/*  88 */     List<Attribute> debug2 = (List<Attribute>)Arrays.<String>stream(debug1).map(this.attributeDescriptionByName::get).filter(Objects::nonNull).map(debug0 -> new Attribute(debug0.name, debug0.getter.get())).collect(Collectors.toList());
/*  89 */     return new AttributeList(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList setAttributes(AttributeList debug1) {
/*  95 */     return new AttributeList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object invoke(String debug1, Object[] debug2, String[] debug3) {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MBeanInfo getMBeanInfo() {
/* 107 */     return this.mBeanInfo;
/*     */   }
/*     */   
/*     */   static final class AttributeDescription {
/*     */     private final String name;
/*     */     private final Supplier<Object> getter;
/*     */     private final String description;
/*     */     private final Class<?> type;
/*     */     
/*     */     private AttributeDescription(String debug1, Supplier<Object> debug2, String debug3, Class<?> debug4) {
/* 117 */       this.name = debug1;
/* 118 */       this.getter = debug2;
/* 119 */       this.description = debug3;
/* 120 */       this.type = debug4;
/*     */     }
/*     */     
/*     */     private MBeanAttributeInfo asMBeanAttributeInfo() {
/* 124 */       return new MBeanAttributeInfo(this.name, this.type.getSimpleName(), this.description, true, false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\monitoring\jmx\MinecraftServerStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */