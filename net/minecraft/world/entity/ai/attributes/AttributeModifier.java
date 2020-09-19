/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AttributeModifier {
/*     */   private final double amount;
/*     */   private final Operation operation;
/*  15 */   private static final Logger LOGGER = LogManager.getLogger(); private final Supplier<String> nameGetter;
/*     */   private final UUID id;
/*     */   
/*  18 */   public enum Operation { ADDITION(0),
/*  19 */     MULTIPLY_BASE(1),
/*  20 */     MULTIPLY_TOTAL(2);
/*     */     
/*  22 */     private static final Operation[] OPERATIONS = new Operation[] { ADDITION, MULTIPLY_BASE, MULTIPLY_TOTAL }; private final int value;
/*     */     static {
/*     */     
/*     */     }
/*     */     Operation(int debug3) {
/*  27 */       this.value = debug3;
/*     */     }
/*     */     
/*     */     public int toValue() {
/*  31 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Operation fromValue(int debug0) {
/*  35 */       if (debug0 < 0 || debug0 >= OPERATIONS.length) {
/*  36 */         throw new IllegalArgumentException("No operation with value " + debug0);
/*     */       }
/*     */       
/*  39 */       return OPERATIONS[debug0];
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeModifier(String debug1, double debug2, Operation debug4) {
/*  49 */     this(Mth.createInsecureUUID((Random)ThreadLocalRandom.current()), () -> debug0, debug2, debug4);
/*     */   }
/*     */   
/*     */   public AttributeModifier(UUID debug1, String debug2, double debug3, Operation debug5) {
/*  53 */     this(debug1, () -> debug0, debug3, debug5);
/*     */   }
/*     */   
/*     */   public AttributeModifier(UUID debug1, Supplier<String> debug2, double debug3, Operation debug5) {
/*  57 */     this.id = debug1;
/*  58 */     this.nameGetter = debug2;
/*  59 */     this.amount = debug3;
/*  60 */     this.operation = debug5;
/*     */   }
/*     */   
/*     */   public UUID getId() {
/*  64 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  68 */     return this.nameGetter.get();
/*     */   }
/*     */   
/*     */   public Operation getOperation() {
/*  72 */     return this.operation;
/*     */   }
/*     */   
/*     */   public double getAmount() {
/*  76 */     return this.amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  81 */     if (this == debug1) {
/*  82 */       return true;
/*     */     }
/*  84 */     if (debug1 == null || getClass() != debug1.getClass()) {
/*  85 */       return false;
/*     */     }
/*     */     
/*  88 */     AttributeModifier debug2 = (AttributeModifier)debug1;
/*     */     
/*  90 */     return Objects.equals(this.id, debug2.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + (String)this.nameGetter
/*     */ 
/*     */       
/* 103 */       .get() + '\'' + ", id=" + this.id + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag save() {
/* 109 */     CompoundTag debug1 = new CompoundTag();
/* 110 */     debug1.putString("Name", getName());
/* 111 */     debug1.putDouble("Amount", this.amount);
/* 112 */     debug1.putInt("Operation", this.operation.toValue());
/* 113 */     debug1.putUUID("UUID", this.id);
/* 114 */     return debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static AttributeModifier load(CompoundTag debug0) {
/*     */     try {
/* 120 */       UUID debug1 = debug0.getUUID("UUID");
/* 121 */       Operation debug2 = Operation.fromValue(debug0.getInt("Operation"));
/* 122 */       return new AttributeModifier(debug1, debug0.getString("Name"), debug0.getDouble("Amount"), debug2);
/* 123 */     } catch (Exception debug1) {
/* 124 */       LOGGER.warn("Unable to create attribute: {}", debug1.getMessage());
/* 125 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */