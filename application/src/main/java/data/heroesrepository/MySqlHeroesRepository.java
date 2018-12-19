package data.heroesrepository;

import data.MySqlConnection;
import domain.hero.Hero;
import org.pmw.tinylog.Logger;
import util.HeroException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of HeroesRepository.
 */
public class MySqlHeroesRepository implements HeroesRepository {
    private static final String SQL_ADD_HERO = "insert into heroes (heroName, heroAbility, heroAbilityNegative, cost) "
            + "values (?, ?, ?, ?)";
    private static final String SQL_GET_HERO = "select * from heroes where heroName = ?";
    private static final String SQL_DELETE_HERO = "delete from heroes where heroName = ?";
    private static final String SQL_GET_ALL_HEROES = "select * from heroes";

    @Override
    public void addHero(final Hero h) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_ADD_HERO)) {

            prep.setString(1, h.getHeroName());
            prep.setString(2, h.getHeroAbility());
            prep.setBoolean(3, h.isHeroAbilityNegative());
            prep.setInt(4, h.getCost());

            prep.executeUpdate();
            Logger.info("Hero has been added.");

        } catch (SQLException ex) {
            throw new HeroException("Unable to add hero to DB.", ex);
        }
    }

    @Override
    public Hero getHero(final String name) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_HERO)) {
            prep.setString(1, name);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return createHero(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new HeroException("Can't find the hero.", ex);
        }
    }

    private Hero createHero(final ResultSet rs) throws SQLException {
        final int id = rs.getInt("heroID");
        final String heroName = rs.getString("heroName");
        final String heroAbility = rs.getString("heroAbility");
        final Boolean heroAbilityNegative = rs.getBoolean("heroAbilityNegative");
        final int cost = rs.getInt("cost");
        return new Hero(id, heroName, heroAbility, heroAbilityNegative, cost);
    }

    @Override
    public Hero deleteHero(final String name) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_DELETE_HERO)) {
            prep.setString(1, name);

            prep.executeUpdate();
            Logger.info("Hero has been deleted!");
        } catch (SQLException ex) {
            throw new HeroException("Can't delete hero", ex);
        }
        return null;
    }

    @Override
    public List<Hero> getAllHeroes() {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_ALL_HEROES)) {
            try (ResultSet rs = prep.executeQuery()) {
                final List<Hero> heroes = new ArrayList<>();

                while (rs.next()) {
                    heroes.add(createHero(rs));
                }
                return heroes;
            }
        } catch (SQLException ex) {
            throw new HeroException("Unable to get logins from DB.", ex);
        }
    }
}
