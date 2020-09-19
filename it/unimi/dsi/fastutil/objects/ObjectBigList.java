package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BigList;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import java.util.Iterator;

public interface ObjectBigList<K> extends BigList<K>, ObjectCollection<K>, Size64, Comparable<BigList<? extends K>> {
  ObjectBigListIterator<K> iterator();
  
  ObjectBigListIterator<K> listIterator();
  
  ObjectBigListIterator<K> listIterator(long paramLong);
  
  ObjectBigList<K> subList(long paramLong1, long paramLong2);
  
  void getElements(long paramLong1, Object[][] paramArrayOfObject, long paramLong2, long paramLong3);
  
  void removeElements(long paramLong1, long paramLong2);
  
  void addElements(long paramLong, K[][] paramArrayOfK);
  
  void addElements(long paramLong1, K[][] paramArrayOfK, long paramLong2, long paramLong3);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */