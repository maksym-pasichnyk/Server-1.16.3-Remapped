/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ 
/*     */ public class ClientboundLevelParticlesPacket
/*     */   implements Packet<ClientGamePacketListener> {
/*     */   private double x;
/*     */   private double y;
/*     */   private double z;
/*     */   private float xDist;
/*     */   private float yDist;
/*     */   private float zDist;
/*     */   private float maxSpeed;
/*     */   private int count;
/*     */   private boolean overrideLimiter;
/*     */   private ParticleOptions particle;
/*     */   
/*     */   public ClientboundLevelParticlesPacket() {}
/*     */   
/*     */   public <T extends ParticleOptions> ClientboundLevelParticlesPacket(T debug1, boolean debug2, double debug3, double debug5, double debug7, float debug9, float debug10, float debug11, float debug12, int debug13) {
/*  29 */     this.particle = (ParticleOptions)debug1;
/*  30 */     this.overrideLimiter = debug2;
/*  31 */     this.x = debug3;
/*  32 */     this.y = debug5;
/*  33 */     this.z = debug7;
/*  34 */     this.xDist = debug9;
/*  35 */     this.yDist = debug10;
/*  36 */     this.zDist = debug11;
/*  37 */     this.maxSpeed = debug12;
/*  38 */     this.count = debug13;
/*     */   }
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*     */     SimpleParticleType simpleParticleType;
/*  43 */     ParticleType<?> debug2 = (ParticleType)Registry.PARTICLE_TYPE.byId(debug1.readInt());
/*  44 */     if (debug2 == null) {
/*  45 */       simpleParticleType = ParticleTypes.BARRIER;
/*     */     }
/*  47 */     this.overrideLimiter = debug1.readBoolean();
/*  48 */     this.x = debug1.readDouble();
/*  49 */     this.y = debug1.readDouble();
/*  50 */     this.z = debug1.readDouble();
/*  51 */     this.xDist = debug1.readFloat();
/*  52 */     this.yDist = debug1.readFloat();
/*  53 */     this.zDist = debug1.readFloat();
/*  54 */     this.maxSpeed = debug1.readFloat();
/*  55 */     this.count = debug1.readInt();
/*  56 */     this.particle = readParticle(debug1, (ParticleType<ParticleOptions>)simpleParticleType);
/*     */   }
/*     */   
/*     */   private <T extends ParticleOptions> T readParticle(FriendlyByteBuf debug1, ParticleType<T> debug2) {
/*  60 */     return (T)debug2.getDeserializer().fromNetwork(debug2, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  65 */     debug1.writeInt(Registry.PARTICLE_TYPE.getId(this.particle.getType()));
/*  66 */     debug1.writeBoolean(this.overrideLimiter);
/*  67 */     debug1.writeDouble(this.x);
/*  68 */     debug1.writeDouble(this.y);
/*  69 */     debug1.writeDouble(this.z);
/*  70 */     debug1.writeFloat(this.xDist);
/*  71 */     debug1.writeFloat(this.yDist);
/*  72 */     debug1.writeFloat(this.zDist);
/*  73 */     debug1.writeFloat(this.maxSpeed);
/*  74 */     debug1.writeInt(this.count);
/*  75 */     this.particle.writeToNetwork(debug1);
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
/*     */   public void handle(ClientGamePacketListener debug1) {
/* 120 */     debug1.handleParticleEvent(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLevelParticlesPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */