package org.cs23sw612;

import org.cs23sw612.Interfaces.CacheStorage;

import java.sql.*;

public class SqliteCacheStorage implements CacheStorage {
    private final Connection connection;
    public SqliteCacheStorage(String dbPath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        var stmt = connection.createStatement();
        var resultSet = stmt
                .executeQuery("SELECT COUNT(name) > 0 AS c FROM sqlite_master WHERE type='table' AND name='sulcache'");
        assert resultSet.next();
        var c = resultSet.getBoolean("c");
        resultSet.close();
        if (!c)
            setupTable();
    }

    private void setupTable() throws SQLException {
        var stmt = connection.createStatement();
        stmt.execute("""
                    CREATE TABLE sulcache (
                        id integer PRIMARY KEY,
                        input TEXT NOT NULL,
                        response TEXT NOT NULL,
                        parent_id INTEGER,
                        UNIQUE (input,parent_id),
                        FOREIGN KEY(parent_id) REFERENCES sulcache (id)
                    )
                """);
    }

    @Override
    public CacheStorageRecord LookupCacheEntry(Integer previousInputId, String input) {
        try {
            ResultSet result;
            if (previousInputId == null) {
                var stmt = connection
                        .prepareStatement("SELECT id, response FROM sulcache WHERE input=? AND parent_id IS NULL");
                stmt.setString(1, input);
                result = stmt.executeQuery();
            } else {
                var stmt = connection
                        .prepareStatement("SELECT id, response FROM sulcache WHERE input=? AND parent_id=?");
                stmt.setString(1, input);
                stmt.setInt(2, previousInputId);
                result = stmt.executeQuery();
            }
            if (!result.next()) {
                return null;
            }
            var response = result.getString("response");
            var id = result.getInt("id");
            result.close();
            return new CacheStorageRecord(id, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CacheStorageRecord InsertCacheEntry(Integer previousInputId, String input, String output) {
        try {
            PreparedStatement stmt = null;
            stmt = connection.prepareStatement("INSERT INTO sulcache (input, response, parent_id) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, input);
            stmt.setString(2, output);
            if (previousInputId == null)
                stmt.setNull(3, Types.NULL);
            else
                stmt.setInt(3, previousInputId);
            stmt.execute();

            var rs = stmt.getGeneratedKeys();
            assert rs.next();
            var insertedId = rs.getInt(1);
            rs.close();
            return new CacheStorageRecord(insertedId, output);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
