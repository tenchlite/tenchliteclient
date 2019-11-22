package net.runelite.client.plugins.tobstats;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

@Singleton
@Slf4j
public class StorageProvider
{
    private static final boolean disconnectedDB = true;

    private static final String DATABASEPATH = RuneLite.RUNELITE_DIR + "/TheatreStats/";
    private static final String DATABASEFILE = "TheatreDB";
    private static final String EXTENSION = ".db";
    private static final String DATABASE = DATABASEPATH + DATABASEFILE + EXTENSION;
    private static boolean firstOpen = true;
    private static final int isolationLevel = 8;

    private static final Sql2o sql2o;

    private static final InputStream RUNSCRIPT;
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    static
    {
        if (disconnectedDB)
        {
            RUNSCRIPT = null;
            sql2o = null;
        }
        else
        {
            RUNSCRIPT = TobstatsPlugin.class.getResourceAsStream("create_database.txt");
            sql2o = new Sql2o("jdbc:sqlite:" + DATABASE, null, null);
        }
    }

    public void emptyDatabase()
    {
        if (disconnectedDB)
        {
            return;
        }
        scheduledThreadPoolExecutor.submit(() ->
        {
            try (Connection connection = sql2o.beginTransaction(isolationLevel))
            {
                connection.createQuery("delete from raid;").executeUpdate();
                connection.createQuery("delete from room;").executeUpdate();
                connection.createQuery("delete from maiden;").executeUpdate();
                connection.createQuery("delete from bloat;").executeUpdate();
                connection.createQuery("delete from nylocas;").executeUpdate();
                connection.createQuery("delete from sotetseg;").executeUpdate();
                connection.createQuery("delete from xarpus;").executeUpdate();
                connection.createQuery("delete from verzik;").executeUpdate();
                connection.createQuery("delete from member;").executeUpdate();
                connection.createQuery("delete from rare_drop;").executeUpdate();
                connection.createQuery("delete from death;").executeUpdate();
                connection.createQuery("delete from raid_member;").executeUpdate();
                connection.commit();
            }
            catch (Exception ex)
            {
                log.warn("Couldn't delete all entries", ex);
            }
        });
    }

    public void initialize()
    {
        if (disconnectedDB)
        {
            return;
        }
        scheduledThreadPoolExecutor.submit(() ->
        {
            if (firstOpen)
            {
                try
                {
                    String queryText = CharStreams.toString(new InputStreamReader(
                            RUNSCRIPT, Charsets.UTF_8));
                    String[] queries = queryText.split(";");

                    try (Connection connection = sql2o.beginTransaction(isolationLevel))
                    {
                        for (String query : queries)
                       {
                            connection.createQuery(query).executeUpdate();
                        }
                        firstOpen = false;
                        connection.commit();
                    }
                }
                catch (IOException io)
                {
                    log.warn("Couldn't execute runscript for database", io);
                }
                catch (Exception ex)
                {
                    log.warn("Setting up database ran into exception", ex);
                }
            }
        });
    }

    public int getRoomRowId()
    {
       return getRowId("room");
   }

    int getRaidRowId()
    {
        return getRowId("raid");
    }

    private int getRowId(String table)
    {
        if (disconnectedDB)
        {
            return -1;
        }
        try (Connection connection = sql2o.beginTransaction(isolationLevel))
        {
            return Optional.ofNullable(connection.createQuery("select max(rowid) from " + table).executeScalar(Integer.class)).orElse(0);
        }
        catch (Exception ex)
        {
            log.warn("Couldn't retrieve " + table + " row id", ex);
        }
        return -1;
    }

    public void asyncExecuteInBatch(DatabaseObject... collection)
    {
        if (disconnectedDB)
        {
            return;
        }
        scheduledThreadPoolExecutor.submit(() ->
        {
            try (Connection connection = sql2o.beginTransaction(isolationLevel))
            {
                for (DatabaseObject databaseObject : collection)
                {
                    databaseObject.executeQuery(connection);
                }
                connection.commit();
            }
            catch (Exception ex)
            {
                log.warn("Couldn't batch update the records", ex);
            }
        });
    }
}