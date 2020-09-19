package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionSource {
  Connection getConnection() throws SQLException;
  
  String toString();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jdbc\ConnectionSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */