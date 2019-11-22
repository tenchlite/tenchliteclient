package net.runelite.client.plugins.tobstats;

import lombok.Data;
import org.sql2o.Connection;

@Data
public class Drop implements DatabaseObject
{
	protected int id;
	protected Integer raid_id = null;
	protected Integer receiver_id = null;
	protected Integer value = null;

	static final String UPDATE_VALUES_QUERY = "insert into Rare_Drop (raid_id, receiver_id, value) values (:raid_id, :receiver_id, :value)";

	@Override
	public void executeQuery(Connection connection)
	{
		connection.createQuery(UPDATE_VALUES_QUERY).bind(this).executeUpdate();
	}
}