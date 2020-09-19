/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class TimerCallbacks<C>
/*    */ {
/* 15 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/* 17 */   public static final TimerCallbacks<MinecraftServer> SERVER_CALLBACKS = (new TimerCallbacks())
/* 18 */     .register(new FunctionCallback.Serializer())
/* 19 */     .register(new FunctionTagCallback.Serializer());
/*    */   
/* 21 */   private final Map<ResourceLocation, TimerCallback.Serializer<C, ?>> idToSerializer = Maps.newHashMap();
/*    */   
/* 23 */   private final Map<Class<?>, TimerCallback.Serializer<C, ?>> classToSerializer = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TimerCallbacks<C> register(TimerCallback.Serializer<C, ?> debug1) {
/* 30 */     this.idToSerializer.put(debug1.getId(), debug1);
/* 31 */     this.classToSerializer.put(debug1.getCls(), debug1);
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   private <T extends TimerCallback<C>> TimerCallback.Serializer<C, T> getSerializer(Class<?> debug1) {
/* 37 */     return (TimerCallback.Serializer<C, T>)this.classToSerializer.get(debug1);
/*    */   }
/*    */   
/*    */   public <T extends TimerCallback<C>> CompoundTag serialize(T debug1) {
/* 41 */     TimerCallback.Serializer<C, T> debug2 = getSerializer(debug1.getClass());
/* 42 */     CompoundTag debug3 = new CompoundTag();
/* 43 */     debug2.serialize(debug3, debug1);
/* 44 */     debug3.putString("Type", debug2.getId().toString());
/* 45 */     return debug3;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public TimerCallback<C> deserialize(CompoundTag debug1) {
/* 50 */     ResourceLocation debug2 = ResourceLocation.tryParse(debug1.getString("Type"));
/* 51 */     TimerCallback.Serializer<C, ?> debug3 = this.idToSerializer.get(debug2);
/* 52 */     if (debug3 == null) {
/* 53 */       LOGGER.error("Failed to deserialize timer callback: " + debug1);
/* 54 */       return null;
/*    */     } 
/*    */     try {
/* 57 */       return (TimerCallback<C>)debug3.deserialize(debug1);
/* 58 */     } catch (Exception debug4) {
/* 59 */       LOGGER.error("Failed to deserialize timer callback: " + debug1, debug4);
/* 60 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\timers\TimerCallbacks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */