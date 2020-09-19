package net.minecraft.server.level.progress;

import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;

public interface ChunkProgressListener {
  void updateSpawnPos(ChunkPos paramChunkPos);
  
  void onStatusChange(ChunkPos paramChunkPos, @Nullable ChunkStatus paramChunkStatus);
  
  void stop();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\progress\ChunkProgressListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */