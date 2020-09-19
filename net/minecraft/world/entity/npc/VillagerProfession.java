/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class VillagerProfession
/*    */ {
/* 17 */   public static final VillagerProfession NONE = register("none", PoiType.UNEMPLOYED, null);
/* 18 */   public static final VillagerProfession ARMORER = register("armorer", PoiType.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER);
/* 19 */   public static final VillagerProfession BUTCHER = register("butcher", PoiType.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER);
/* 20 */   public static final VillagerProfession CARTOGRAPHER = register("cartographer", PoiType.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
/* 21 */   public static final VillagerProfession CLERIC = register("cleric", PoiType.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC);
/* 22 */   public static final VillagerProfession FARMER = register("farmer", PoiType.FARMER, ImmutableSet.of(Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.BONE_MEAL), ImmutableSet.of(Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER);
/* 23 */   public static final VillagerProfession FISHERMAN = register("fisherman", PoiType.FISHERMAN, SoundEvents.VILLAGER_WORK_FISHERMAN);
/* 24 */   public static final VillagerProfession FLETCHER = register("fletcher", PoiType.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER);
/* 25 */   public static final VillagerProfession LEATHERWORKER = register("leatherworker", PoiType.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER);
/* 26 */   public static final VillagerProfession LIBRARIAN = register("librarian", PoiType.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN);
/* 27 */   public static final VillagerProfession MASON = register("mason", PoiType.MASON, SoundEvents.VILLAGER_WORK_MASON);
/* 28 */   public static final VillagerProfession NITWIT = register("nitwit", PoiType.NITWIT, null);
/* 29 */   public static final VillagerProfession SHEPHERD = register("shepherd", PoiType.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD);
/* 30 */   public static final VillagerProfession TOOLSMITH = register("toolsmith", PoiType.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH);
/* 31 */   public static final VillagerProfession WEAPONSMITH = register("weaponsmith", PoiType.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH);
/*    */   
/*    */   private final String name;
/*    */   private final PoiType jobPoiType;
/*    */   private final ImmutableSet<Item> requestedItems;
/*    */   private final ImmutableSet<Block> secondaryPoi;
/*    */   @Nullable
/*    */   private final SoundEvent workSound;
/*    */   
/*    */   private VillagerProfession(String debug1, PoiType debug2, ImmutableSet<Item> debug3, ImmutableSet<Block> debug4, @Nullable SoundEvent debug5) {
/* 41 */     this.name = debug1;
/* 42 */     this.jobPoiType = debug2;
/* 43 */     this.requestedItems = debug3;
/* 44 */     this.secondaryPoi = debug4;
/* 45 */     this.workSound = debug5;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PoiType getJobPoiType() {
/* 53 */     return this.jobPoiType;
/*    */   }
/*    */   
/*    */   public ImmutableSet<Item> getRequestedItems() {
/* 57 */     return this.requestedItems;
/*    */   }
/*    */   
/*    */   public ImmutableSet<Block> getSecondaryPoi() {
/* 61 */     return this.secondaryPoi;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public SoundEvent getWorkSound() {
/* 66 */     return this.workSound;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return this.name;
/*    */   }
/*    */   
/*    */   static VillagerProfession register(String debug0, PoiType debug1, @Nullable SoundEvent debug2) {
/* 75 */     return register(debug0, debug1, ImmutableSet.of(), ImmutableSet.of(), debug2);
/*    */   }
/*    */   
/*    */   static VillagerProfession register(String debug0, PoiType debug1, ImmutableSet<Item> debug2, ImmutableSet<Block> debug3, @Nullable SoundEvent debug4) {
/* 79 */     return (VillagerProfession)Registry.register((Registry)Registry.VILLAGER_PROFESSION, new ResourceLocation(debug0), new VillagerProfession(debug0, debug1, debug2, debug3, debug4));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\VillagerProfession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */