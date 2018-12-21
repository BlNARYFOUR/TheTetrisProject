package domain.game;

/**
 * Hero ability class.
 */
public class HeroAbility {
    private static boolean switchingControls;
    private static int activatedHero;
    private static boolean heroAbilityIsActivated;
    private static String heroAttack;
    private static int timerAbility = 30;
    private String name;

    public HeroAbility() {
        super();
    }

    public HeroAbility(final String name) {
        this.name = name;
    }

    public static String getHeroAttack() {
        return heroAttack;
    }

    public static void setHeroAttack(final String heroAttack) {
        HeroAbility.heroAttack = heroAttack;
    }

    public static boolean isHeroAbilityIsActivated() {
        return heroAbilityIsActivated;
    }

    public static void setHeroAbilityIsActivated(final boolean heroAbilityIsActivated) {
        HeroAbility.heroAbilityIsActivated = heroAbilityIsActivated;
    }

    public String getName() {
        return name;
    }

    public static void setSwitchingControls(final boolean switchingControls) {
        HeroAbility.switchingControls = switchingControls;
    }

    public static boolean isSwitchingControls() {
        return switchingControls;
    }

    public static void setActivatedHero(final int activatedHero) {
        HeroAbility.activatedHero = activatedHero;
    }

    public static int getTimerAbility() {
        return timerAbility;
    }


    public static int activatedID() {
        return activatedHero;
    }




}
