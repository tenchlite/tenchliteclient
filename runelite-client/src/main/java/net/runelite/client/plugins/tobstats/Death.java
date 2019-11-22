package net.runelite.client.plugins.tobstats;

import lombok.Data;
import org.sql2o.Connection;

@Data
public class Death implements DatabaseObject
{
	protected int id;
	protected Integer room_id;
	protected Integer timestamp;
	protected Integer member_id;

	static final String UPDATE_VALUES_QUERY = "insert into Death (room_id, timestamp, member_id) values (:room_id, :timestamp, :member_id)";

	@Override
	public void executeQuery(Connection connection)
	{
		connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
	}
}