package data.heroesRepository;

import data.MySqlConnection;
import domain.hero.Hero;
import util.HeroException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlHeroesRepository implements HeroesRepository {
    private static final String SQL_ADD_HERO = "insert into heroes (heroName, heroAbility, heroAbilityNegative, cost) " +
            "values (?, ?, ?, ?)";
    private static final String SQL_GET_HERO = "select * from heroes where heroName = ?";
    private static final String SQL_DELETE_HERO = "delete from heroes where heroName = ?";
    private static final String SQL_GET_ALL_HEROES = "select * from heroes";

    @Override
    public void addHero(Hero h) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_ADD_HERO)){

            prep.setString(1, h.getHeroName());
            prep.setString(2, h.getHeroAbility());
            prep.setBoolean(3, h.isHeroAbilityNegative());
            prep.setInt(4, h.getCost());

            prep.executeUpdate();
            System.out.println("Hero has been added.");

        }catch (SQLException ex){
            throw new HeroException("Unable to add hero to DB.", ex);
        }
    }

    @Override
    public Hero getHero(String name) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_HERO)) {
            prep.setString(1, name);

            try (ResultSet rs = prep.executeQuery()){
                if (rs.next()){
                    return createHero(rs);
                }else {
                    return null;
                }
            }
        }catch (SQLException ex){
            throw new HeroException("Can't find the hero.", ex);
        }
    }

    private Hero createHero(ResultSet rs) throws SQLException {
        int ID = rs.getInt("heroID");
        String heroName = rs.getString("heroName");
        String heroAbility = rs.getString("heroAbility");
        Boolean heroAbilityNegative = rs.getBoolean("heroAbilityNegative");
        int cost = rs.getInt("cost");
        return new Hero(ID, heroName, heroAbility, heroAbilityNegative, cost);
    }

    @Override
    public Hero deleteHero(String name) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_DELETE_HERO)){
            prep.setString(1, name);

            prep.executeUpdate();
            System.out.println("Hero has been deleted!");
        }catch (SQLException ex){
            throw new HeroException("Can't delete hero", ex);
        }
        return null;
    }

    @Override
    public List<Hero> getAllHeroes() {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(SQL_GET_ALL_HEROES)){
            try (ResultSet rs = prep.executeQuery()){
                List<Hero> heroes = new ArrayList<>();

                while (rs.next()){
                    heroes.add(createHero(rs));
                }
                return heroes;
            }
        }catch (SQLException ex){
            throw new HeroException("Unable to get logins from DB.", ex);
        }
    }
}
