package domain.hero;

import java.util.Objects;

/**
 * Hero class.
 */
public class Hero {
    private int heroID;
    private String heroName;
    private String heroAbility;
    private boolean heroAbilityNegative;
    private int cost;

    public Hero(final int heroID, final String heroName, final String heroAbility,
                final boolean heroAbilityNegative, final int cost) {
        this.heroID = heroID;
        this.heroName = heroName;
        this.heroAbility = heroAbility;
        this.heroAbilityNegative = heroAbilityNegative;
        this.cost = cost;
    }

    public Hero(final String heroName, final String heroAbility, final boolean heroAbilityNegative, final int cost) {
        this(-1, heroName, heroAbility, heroAbilityNegative, cost);
    }

    private int getHeroID() {
        return heroID;
    }

    public void setHeroID(final int heroID) {
        if (this.heroID < 0) {
            this.heroID = heroID;
        }
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(final String heroName) {
        this.heroName = heroName;
    }

    public String getHeroAbility() {
        return heroAbility;
    }

    public void setHeroAbility(final String heroAbility) {
        this.heroAbility = heroAbility;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(final int cost) {
        this.cost = cost;
    }

    public boolean isHeroAbilityNegative() {
        return heroAbilityNegative;
    }

    public void setHeroAbilityNegative(final boolean heroAbilityNegative) {
        this.heroAbilityNegative = heroAbilityNegative;
    }

    @Override
    public String toString() {
        return "Selected " + getHeroID() + " hero: " + getHeroName() + " ability: "
                + getHeroAbility() + " heroAbilityNegative? : " + isHeroAbilityNegative()
                + " cost: " + getCost();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Hero hero = (Hero) o;
        return heroID == hero.heroID
                && Objects.equals(heroName, hero.heroName)
                && Objects.equals(heroAbility, hero.heroAbility);
    }

    @Override
    public int hashCode() {

        return Objects.hash(heroID, heroName, heroAbility);
    }
}
