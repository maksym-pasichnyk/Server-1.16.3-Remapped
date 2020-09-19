/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockParticleOption implements ParticleOptions {
/*    */   public static Codec<BlockParticleOption> codec(ParticleType<BlockParticleOption> debug0) {
/* 14 */     return BlockState.CODEC.xmap(debug1 -> new BlockParticleOption(debug0, debug1), debug0 -> debug0.state);
/*    */   }
/*    */   
/* 17 */   public static final ParticleOptions.Deserializer<BlockParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<BlockParticleOption>()
/*    */     {
/*    */       public BlockParticleOption fromCommand(ParticleType<BlockParticleOption> debug1, StringReader debug2) throws CommandSyntaxException {
/* 20 */         debug2.expect(' ');
/* 21 */         return new BlockParticleOption(debug1, (new BlockStateParser(debug2, false)).parse(false).getState());
/*    */       }
/*    */ 
/*    */       
/*    */       public BlockParticleOption fromNetwork(ParticleType<BlockParticleOption> debug1, FriendlyByteBuf debug2) {
/* 26 */         return new BlockParticleOption(debug1, (BlockState)Block.BLOCK_STATE_REGISTRY.byId(debug2.readVarInt()));
/*    */       }
/*    */     };
/*    */   
/*    */   private final ParticleType<BlockParticleOption> type;
/*    */   private final BlockState state;
/*    */   
/*    */   public BlockParticleOption(ParticleType<BlockParticleOption> debug1, BlockState debug2) {
/* 34 */     this.type = debug1;
/* 35 */     this.state = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeToNetwork(FriendlyByteBuf debug1) {
/* 40 */     debug1.writeVarInt(Block.BLOCK_STATE_REGISTRY.getId(this.state));
/*    */   }
/*    */ 
/*    */   
/*    */   public String writeToString() {
/* 45 */     return Registry.PARTICLE_TYPE.getKey(getType()) + " " + BlockStateParser.serialize(this.state);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<BlockParticleOption> getType() {
/* 50 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\particles\BlockParticleOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */