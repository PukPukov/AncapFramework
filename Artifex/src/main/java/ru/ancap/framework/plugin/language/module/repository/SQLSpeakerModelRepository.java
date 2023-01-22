package ru.ancap.framework.plugin.language.module.repository;

import lombok.AllArgsConstructor;
import ru.ancap.framework.api.database.ConnectionProvider;
import ru.ancap.framework.plugin.language.module.model.SpeakerModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class SQLSpeakerModelRepository implements SpeakerModelRepository {

    private final ConnectionProvider connectionProvider;

    @Override
    public SpeakerModel read(String name) {
        String query = "SELECT * FROM Languages\n" +
                       "WHERE name = ?;\n";
        try (PreparedStatement statement = connectionProvider.get().prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                return null;
            }
            return new SpeakerModel(set.getString("name"), set.getString("language_code"));
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void create(SpeakerModel model) {
        String query = "INSERT INTO Languages (name, language_code) VALUES (?, ?);\n";
        this.operate(query, model, 1, 2);
    }

    @Override
    public void update(SpeakerModel model) {
        String query = "UPDATE Languages\n" +
                       "SET language_code = ?\n" +
                       "WHERE name = ?;\n";
        this.operate(query, model, 2, 1);
    }

    private void operate(String query, SpeakerModel model, int nameIndex, int codeIndex) {
        try (PreparedStatement statement = connectionProvider.get().prepareStatement(query)) {
            statement.setString(nameIndex, model.getName());
            statement.setString(codeIndex, model.getLanguageCode());
            statement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }


}
