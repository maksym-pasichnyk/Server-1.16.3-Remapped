package org.apache.logging.log4j.core.impl;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.message.Message;

public interface LogEventFactory {
  LogEvent createEvent(String paramString1, Marker paramMarker, String paramString2, Level paramLevel, Message paramMessage, List<Property> paramList, Throwable paramThrowable);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\LogEventFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */