package net.runelite.client.plugins.tobstats.rooms.Bloat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.client.plugins.tobstats.RoomData;
import org.sql2o.Connection;

@Data
@EqualsAndHashCode(callSuper = true)
public class BloatData extends RoomData
{
    static final String UPDATE_VALUES_QUERY = "insert into Bloat (id) values (:id)";

    @Override
    public void executeQuery(Connection connection)
    {
        super.executeQuery(connection);
        connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
    }
}