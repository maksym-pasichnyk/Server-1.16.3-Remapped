/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class MobEffectInstance
/*     */   implements Comparable<MobEffectInstance>
/*     */ {
/*  14 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final MobEffect effect;
/*     */   
/*     */   private int duration;
/*     */   private int amplifier;
/*     */   private boolean splash;
/*     */   private boolean ambient;
/*     */   private boolean visible;
/*     */   private boolean showIcon;
/*     */   @Nullable
/*     */   private MobEffectInstance hiddenEffect;
/*     */   
/*     */   public MobEffectInstance(MobEffect debug1) {
/*  28 */     this(debug1, 0, 0);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffect debug1, int debug2) {
/*  32 */     this(debug1, debug2, 0);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffect debug1, int debug2, int debug3) {
/*  36 */     this(debug1, debug2, debug3, false, true);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffect debug1, int debug2, int debug3, boolean debug4, boolean debug5) {
/*  40 */     this(debug1, debug2, debug3, debug4, debug5, debug5);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffect debug1, int debug2, int debug3, boolean debug4, boolean debug5, boolean debug6) {
/*  44 */     this(debug1, debug2, debug3, debug4, debug5, debug6, null);
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffect debug1, int debug2, int debug3, boolean debug4, boolean debug5, boolean debug6, @Nullable MobEffectInstance debug7) {
/*  48 */     this.effect = debug1;
/*  49 */     this.duration = debug2;
/*  50 */     this.amplifier = debug3;
/*  51 */     this.ambient = debug4;
/*  52 */     this.visible = debug5;
/*  53 */     this.showIcon = debug6;
/*  54 */     this.hiddenEffect = debug7;
/*     */   }
/*     */   
/*     */   public MobEffectInstance(MobEffectInstance debug1) {
/*  58 */     this.effect = debug1.effect;
/*  59 */     setDetailsFrom(debug1);
/*     */   }
/*     */   
/*     */   void setDetailsFrom(MobEffectInstance debug1) {
/*  63 */     this.duration = debug1.duration;
/*  64 */     this.amplifier = debug1.amplifier;
/*  65 */     this.ambient = debug1.ambient;
/*  66 */     this.visible = debug1.visible;
/*  67 */     this.showIcon = debug1.showIcon;
/*     */   }
/*     */   
/*     */   public boolean update(MobEffectInstance debug1) {
/*  71 */     if (this.effect != debug1.effect) {
/*  72 */       LOGGER.warn("This method should only be called for matching effects!");
/*     */     }
/*  74 */     boolean debug2 = false;
/*  75 */     if (debug1.amplifier > this.amplifier) {
/*  76 */       if (debug1.duration < this.duration) {
/*  77 */         MobEffectInstance debug3 = this.hiddenEffect;
/*  78 */         this.hiddenEffect = new MobEffectInstance(this);
/*  79 */         this.hiddenEffect.hiddenEffect = debug3;
/*     */       } 
/*  81 */       this.amplifier = debug1.amplifier;
/*  82 */       this.duration = debug1.duration;
/*  83 */       debug2 = true;
/*  84 */     } else if (debug1.duration > this.duration) {
/*  85 */       if (debug1.amplifier == this.amplifier) {
/*  86 */         this.duration = debug1.duration;
/*  87 */         debug2 = true;
/*     */       }
/*  89 */       else if (this.hiddenEffect == null) {
/*  90 */         this.hiddenEffect = new MobEffectInstance(debug1);
/*     */       } else {
/*  92 */         this.hiddenEffect.update(debug1);
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     if ((!debug1.ambient && this.ambient) || debug2) {
/*  97 */       this.ambient = debug1.ambient;
/*  98 */       debug2 = true;
/*     */     } 
/* 100 */     if (debug1.visible != this.visible) {
/* 101 */       this.visible = debug1.visible;
/* 102 */       debug2 = true;
/*     */     } 
/* 104 */     if (debug1.showIcon != this.showIcon) {
/* 105 */       this.showIcon = debug1.showIcon;
/* 106 */       debug2 = true;
/*     */     } 
/* 108 */     return debug2;
/*     */   }
/*     */   
/*     */   public MobEffect getEffect() {
/* 112 */     return this.effect;
/*     */   }
/*     */   
/*     */   public int getDuration() {
/* 116 */     return this.duration;
/*     */   }
/*     */   
/*     */   public int getAmplifier() {
/* 120 */     return this.amplifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAmbient() {
/* 128 */     return this.ambient;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 132 */     return this.visible;
/*     */   }
/*     */   
/*     */   public boolean showIcon() {
/* 136 */     return this.showIcon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tick(LivingEntity debug1, Runnable debug2) {
/* 146 */     if (this.duration > 0) {
/* 147 */       if (this.effect.isDurationEffectTick(this.duration, this.amplifier)) {
/* 148 */         applyEffect(debug1);
/*     */       }
/* 150 */       tickDownDuration();
/* 151 */       if (this.duration == 0 && this.hiddenEffect != null) {
/* 152 */         setDetailsFrom(this.hiddenEffect);
/* 153 */         this.hiddenEffect = this.hiddenEffect.hiddenEffect;
/* 154 */         debug2.run();
/*     */       } 
/*     */     } 
/* 157 */     return (this.duration > 0);
/*     */   }
/*     */   
/*     */   private int tickDownDuration() {
/* 161 */     if (this.hiddenEffect != null) {
/* 162 */       this.hiddenEffect.tickDownDuration();
/*     */     }
/* 164 */     return --this.duration;
/*     */   }
/*     */   
/*     */   public void applyEffect(LivingEntity debug1) {
/* 168 */     if (this.duration > 0) {
/* 169 */       this.effect.applyEffectTick(debug1, this.amplifier);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 174 */     return this.effect.getDescriptionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     String debug1;
/* 180 */     if (this.amplifier > 0) {
/* 181 */       debug1 = getDescriptionId() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration;
/*     */     } else {
/* 183 */       debug1 = getDescriptionId() + ", Duration: " + this.duration;
/*     */     } 
/* 185 */     if (this.splash) {
/* 186 */       debug1 = debug1 + ", Splash: true";
/*     */     }
/* 188 */     if (!this.visible) {
/* 189 */       debug1 = debug1 + ", Particles: false";
/*     */     }
/* 191 */     if (!this.showIcon) {
/* 192 */       debug1 = debug1 + ", Show Icon: false";
/*     */     }
/*     */     
/* 195 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 200 */     if (this == debug1) {
/* 201 */       return true;
/*     */     }
/*     */     
/* 204 */     if (debug1 instanceof MobEffectInstance) {
/* 205 */       MobEffectInstance debug2 = (MobEffectInstance)debug1;
/*     */       
/* 207 */       return (this.duration == debug2.duration && this.amplifier == debug2.amplifier && this.splash == debug2.splash && this.ambient == debug2.ambient && this.effect.equals(debug2.effect));
/*     */     } 
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 214 */     int debug1 = this.effect.hashCode();
/* 215 */     debug1 = 31 * debug1 + this.duration;
/* 216 */     debug1 = 31 * debug1 + this.amplifier;
/* 217 */     debug1 = 31 * debug1 + (this.splash ? 1 : 0);
/* 218 */     debug1 = 31 * debug1 + (this.ambient ? 1 : 0);
/* 219 */     return debug1;
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 223 */     debug1.putByte("Id", (byte)MobEffect.getId(getEffect()));
/* 224 */     writeDetailsTo(debug1);
/* 225 */     return debug1;
/*     */   }
/*     */   
/*     */   private void writeDetailsTo(CompoundTag debug1) {
/* 229 */     debug1.putByte("Amplifier", (byte)getAmplifier());
/* 230 */     debug1.putInt("Duration", getDuration());
/* 231 */     debug1.putBoolean("Ambient", isAmbient());
/* 232 */     debug1.putBoolean("ShowParticles", isVisible());
/* 233 */     debug1.putBoolean("ShowIcon", showIcon());
/* 234 */     if (this.hiddenEffect != null) {
/* 235 */       CompoundTag debug2 = new CompoundTag();
/* 236 */       this.hiddenEffect.save(debug2);
/* 237 */       debug1.put("HiddenEffect", (Tag)debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static MobEffectInstance load(CompoundTag debug0) {
/* 242 */     int debug1 = debug0.getByte("Id");
/* 243 */     MobEffect debug2 = MobEffect.byId(debug1);
/* 244 */     if (debug2 == null) {
/* 245 */       return null;
/*     */     }
/* 247 */     return loadSpecifiedEffect(debug2, debug0);
/*     */   }
/*     */   
/*     */   private static MobEffectInstance loadSpecifiedEffect(MobEffect debug0, CompoundTag debug1) {
/* 251 */     int debug2 = debug1.getByte("Amplifier");
/* 252 */     int debug3 = debug1.getInt("Duration");
/* 253 */     boolean debug4 = debug1.getBoolean("Ambient");
/* 254 */     boolean debug5 = true;
/* 255 */     if (debug1.contains("ShowParticles", 1)) {
/* 256 */       debug5 = debug1.getBoolean("ShowParticles");
/*     */     }
/* 258 */     boolean debug6 = debug5;
/* 259 */     if (debug1.contains("ShowIcon", 1)) {
/* 260 */       debug6 = debug1.getBoolean("ShowIcon");
/*     */     }
/* 262 */     MobEffectInstance debug7 = null;
/* 263 */     if (debug1.contains("HiddenEffect", 10)) {
/* 264 */       debug7 = loadSpecifiedEffect(debug0, debug1.getCompound("HiddenEffect"));
/*     */     }
/* 266 */     return new MobEffectInstance(debug0, debug3, (debug2 < 0) ? 0 : debug2, debug4, debug5, debug6, debug7);
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
/*     */   public int compareTo(MobEffectInstance debug1) {
/* 279 */     int debug2 = 32147;
/* 280 */     if ((getDuration() > 32147 && debug1.getDuration() > 32147) || (isAmbient() && debug1.isAmbient()))
/*     */     {
/* 282 */       return ComparisonChain.start()
/* 283 */         .compare(Boolean.valueOf(isAmbient()), Boolean.valueOf(debug1.isAmbient()))
/* 284 */         .compare(getEffect().getColor(), debug1.getEffect().getColor())
/* 285 */         .result();
/*     */     }
/* 287 */     return ComparisonChain.start()
/* 288 */       .compare(Boolean.valueOf(isAmbient()), Boolean.valueOf(debug1.isAmbient()))
/* 289 */       .compare(getDuration(), debug1.getDuration())
/* 290 */       .compare(getEffect().getColor(), debug1.getEffect().getColor())
/* 291 */       .result();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\MobEffectInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */