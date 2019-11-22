package net.runelite.client.plugins.tobstats.rooms.Xarpus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.client.plugins.tobstats.RoomData;
import org.sql2o.Connection;

@Data
@EqualsAndHashCode(callSuper = true)
public class XarpusData extends RoomData
{
    private Integer heal_amount = 0;
    private Integer p1 = null;
    private Integer p2 = null;

    static final String UPDATE_VALUES_QUERY = "insert into Xarpus (id, heal_amount, p1, p2) values (:id, :heal_amount, :p1, :p2)";

    @Override
    public void executeQuery(Connection connection)
    {
        super.executeQuery(connection);
        connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
    }
}