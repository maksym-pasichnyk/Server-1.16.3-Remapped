/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.commands.arguments.item.ItemInput;
/*    */ import net.minecraft.commands.arguments.item.ItemParser;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemParticleOption implements ParticleOptions {
/*    */   public static Codec<ItemParticleOption> codec(ParticleType<ItemParticleOption> debug0) {
/* 14 */     return ItemStack.CODEC.xmap(debug1 -> new ItemParticleOption(debug0, debug1), debug0 -> debug0.itemStack);
/*    */   }
/*    */   
/* 17 */   public static final ParticleOptions.Deserializer<ItemParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<ItemParticleOption>()
/*    */     {
/*    */       public ItemParticleOption fromCommand(ParticleType<ItemParticleOption> debug1, StringReader debug2) throws CommandSyntaxException {
/* 20 */         debug2.expect(' ');
/* 21 */         ItemParser debug3 = (new ItemParser(debug2, false)).parse();
/* 22 */         ItemStack debug4 = (new ItemInput(debug3.getItem(), debug3.getNbt())).createItemStack(1, false);
/* 23 */         return new ItemParticleOption(debug1, debug4);
/*    */       }
/*    */ 
/*    */       
/*    */       public ItemParticleOption fromNetwork(ParticleType<ItemParticleOption> debug1, FriendlyByteBuf debug2) {
/* 28 */         return new ItemParticleOption(debug1, debug2.readItem());
/*    */       }
/*    */     };
/*    */   
/*    */   private final ParticleType<ItemParticleOption> type;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public ItemParticleOption(ParticleType<ItemParticleOption> debug1, ItemStack debug2) {
/* 36 */     this.type = debug1;
/* 37 */     this.itemStack = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeToNetwork(FriendlyByteBuf debug1) {
/* 42 */     debug1.writeItem(this.itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public String writeToString() {
/* 47 */     return Registry.PARTICLE_TYPE.getKey(getType()) + " " + (new ItemInput(this.itemStack.getItem(), this.itemStack.getTag())).serialize();
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<ItemParticleOption> getType() {
/* 52 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\particles\ItemParticleOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */