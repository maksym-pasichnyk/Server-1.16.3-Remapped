package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface EntityBlock {
  @Nullable
  BlockEntity newBlockEntity(BlockGetter paramBlockGetter);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EntityBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */