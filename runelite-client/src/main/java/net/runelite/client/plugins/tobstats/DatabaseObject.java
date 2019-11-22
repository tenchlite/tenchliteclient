package net.runelite.client.plugins.tobstats;

import org.sql2o.Connection;

public interface DatabaseObject
{
    void executeQuery(Connection connection);
}