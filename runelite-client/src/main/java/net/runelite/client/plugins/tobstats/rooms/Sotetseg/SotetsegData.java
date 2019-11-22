package net.runelite.client.plugins.tobstats.rooms.Sotetseg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.client.plugins.tobstats.RoomData;
import org.sql2o.Connection;

@Data
@EqualsAndHashCode(callSuper = true)
public class SotetsegData extends RoomData
{
    private Integer p1 = null;
    private Integer p2 = null;

    static final String UPDATE_VALUES_QUERY = "insert into Sotetseg (id, p1, p2) values (:id, :p1, :p2)";

    @Override
    public void executeQuery(Connection connection)
    {
        super.executeQuery(connection);
        connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
    }
}