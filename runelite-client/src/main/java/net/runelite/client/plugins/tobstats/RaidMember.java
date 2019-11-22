package net.runelite.client.plugins.tobstats;

import lombok.Data;
import org.sql2o.Connection;

@Data
public class RaidMember implements DatabaseObject
{
    private String name;
    private int raid_id;

    static final String UPDATE_VALUES_QUERY = "insert into Raid_Member (name, raid_id) values (:name, :raid_id)";

    public RaidMember(String name, int id)
    {
        this.name = name;
        this.raid_id = id;
    }

    @Override
    public void executeQuery(Connection connection)
    {
        connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
    }
}