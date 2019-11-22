package net.runelite.client.plugins.tobstats;

import lombok.Data;
import org.sql2o.Connection;

@Data
public class RoomData implements DatabaseObject
{
	protected int id;
	protected Integer millis = 0;
	protected int incomplete = 0;

	private static final String UPDATE_VALUES_QUERY = "insert into Room (millis, incomplete) values (:millis, :incomplete)";

	@Override
	public void executeQuery(Connection connection)
	{
		connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
	}
}