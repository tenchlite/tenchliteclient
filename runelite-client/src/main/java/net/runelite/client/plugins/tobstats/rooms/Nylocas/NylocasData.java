package net.runelite.client.plugins.tobstats.rooms.Nylocas;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.runelite.client.plugins.tobstats.RoomData;
import org.sql2o.Connection;

@Data
@EqualsAndHashCode(callSuper = true)
public class NylocasData extends RoomData
{
    private Integer wave31 = null;
    private Integer end_waves = null;
    private Integer boss_spawn = null;

    static final String UPDATE_VALUES_QUERY = "insert into Nylocas (id, wave31, end_waves, boss_spawn) values (:id, :wave31, :end_waves, :boss_spawn)";

    @Override
    public void executeQuery(Connection connection)
    {
        super.executeQuery(connection);
        connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
    }
}