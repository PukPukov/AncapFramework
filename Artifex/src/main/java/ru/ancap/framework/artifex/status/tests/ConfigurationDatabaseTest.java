package ru.ancap.framework.artifex.status.tests;

import org.junit.jupiter.api.Assertions;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.database.nosql.ConfigurationDatabase;
import ru.ancap.framework.database.nosql.exception.DifferentDatatypeException;
import ru.ancap.framework.database.nosql.PathDatabase;
import ru.ancap.framework.status.test.AbstractTest;
import ru.ancap.framework.status.util.TestDomain;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ConfigurationDatabaseTest extends AbstractTest {
    
    public ConfigurationDatabaseTest() {
        super(
            TestDomain.of(Artifex.class, "configuration-database"),
            () -> {
                File temp = new File(Artifex.PLUGIN.getDataFolder(), "temp"+System.currentTimeMillis()+".yml");
                try { temp.createNewFile(); } catch (IOException exception) { throw new RuntimeException(exception); }
                PathDatabase database = ConfigurationDatabase.file(temp);
                
                /* INNER */ 
                
                PathDatabase inner = database.inner("1");
                
                /* KEYS */
                
                inner.write("example1", "value");
                inner.write("example2", "value");
                inner.write("example3", "value");
                Assertions.assertEquals(Set.of("example1", "example2", "example3"), inner.keys());
                
                /* VALUE */
                
                inner.write("value-storage-1", 10D);
                Assertions.assertEquals(10D, inner.readNumber("value-storage-1"));
                
                /* TYPES */
                
                inner.write("value-storage-2", 10L);
                Assertions.assertThrows(DifferentDatatypeException.class, () -> inner.readNumber("value-storage-2"));
                
                /* DELETE */
                
                inner.write("value-storage-3", "value");
                inner.delete("value-storage-3");
                Assertions.assertNull(inner.readString("value-storage-3"));
                
                try { return TestResult.SUCCESS; }
                finally { 
                    database.nullify();
                    database.close();
                    temp.delete();
                }
            }
        );
    }
    
}
