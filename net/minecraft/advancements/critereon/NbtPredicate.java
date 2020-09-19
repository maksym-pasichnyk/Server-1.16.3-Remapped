/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class NbtPredicate
/*    */ {
/* 20 */   public static final NbtPredicate ANY = new NbtPredicate(null);
/*    */   
/*    */   @Nullable
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public NbtPredicate(@Nullable CompoundTag debug1) {
/* 26 */     this.tag = debug1;
/*    */   }
/*    */   
/*    */   public boolean matches(ItemStack debug1) {
/* 30 */     if (this == ANY) {
/* 31 */       return true;
/*    */     }
/* 33 */     return matches((Tag)debug1.getTag());
/*    */   }
/*    */   
/*    */   public boolean matches(Entity debug1) {
/* 37 */     if (this == ANY) {
/* 38 */       return true;
/*    */     }
/* 40 */     return matches((Tag)getEntityTagToCompare(debug1));
/*    */   }
/*    */   
/*    */   public boolean matches(@Nullable Tag debug1) {
/* 44 */     if (debug1 == null) {
/* 45 */       return (this == ANY);
/*    */     }
/*    */     
/* 48 */     if (this.tag != null && !NbtUtils.compareNbt((Tag)this.tag, debug1, true)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     return true;
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 56 */     if (this == ANY || this.tag == null) {
/* 57 */       return (JsonElement)JsonNull.INSTANCE;
/*    */     }
/*    */     
/* 60 */     return (JsonElement)new JsonPrimitive(this.tag.toString());
/*    */   }
/*    */   public static NbtPredicate fromJson(@Nullable JsonElement debug0) {
/*    */     CompoundTag debug1;
/* 64 */     if (debug0 == null || debug0.isJsonNull()) {
/* 65 */       return ANY;
/*    */     }
/*    */     
/*    */     try {
/* 69 */       debug1 = TagParser.parseTag(GsonHelper.convertToString(debug0, "nbt"));
/* 70 */     } catch (CommandSyntaxException debug2) {
/* 71 */       throw new JsonSyntaxException("Invalid nbt tag: " + debug2.getMessage());
/*    */     } 
/* 73 */     return new NbtPredicate(debug1);
/*    */   }
/*    */   
/*    */   public static CompoundTag getEntityTagToCompare(Entity debug0) {
/* 77 */     CompoundTag debug1 = debug0.saveWithoutId(new CompoundTag());
/* 78 */     if (debug0 instanceof Player) {
/* 79 */       ItemStack debug2 = ((Player)debug0).inventory.getSelected();
/* 80 */       if (!debug2.isEmpty()) {
/* 81 */         debug1.put("SelectedItem", (Tag)debug2.save(new CompoundTag()));
/*    */       }
/*    */     } 
/* 84 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\NbtPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */