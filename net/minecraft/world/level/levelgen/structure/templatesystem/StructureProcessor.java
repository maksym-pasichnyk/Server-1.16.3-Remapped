package net.minecraft.world.level.levelgen.structure.templatesystem;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public abstract class StructureProcessor {
  @Nullable
  public abstract StructureTemplate.StructureBlockInfo processBlock(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockPos paramBlockPos2, StructureTemplate.StructureBlockInfo paramStructureBlockInfo1, StructureTemplate.StructureBlockInfo paramStructureBlockInfo2, StructurePlaceSettings paramStructurePlaceSettings);
  
  protected abstract StructureProcessorType<?> getType();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */