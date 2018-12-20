package domain.game;

import java.util.ArrayList;

public class HeroAbility {
    private static boolean switchingControls = false;
    private static int activatedHero;
    private static boolean heroAbilityIsActivated = false;
    private static String heroAttack;
    private static int timerAbility = 30;
    private String name;

    public HeroAbility(){}

    public HeroAbility(String name) {
        this.name = name;
    }

    public static String getHeroAttack() {
        return heroAttack;
    }

    public static void setHeroAttack(String heroAttack) {
        HeroAbility.heroAttack = heroAttack;
    }

    public static boolean isHeroAbilityIsActivated() {
        return heroAbilityIsActivated;
    }

    public static void setHeroAbilityIsActivated(boolean heroAbilityIsActivated) {
        HeroAbility.heroAbilityIsActivated = heroAbilityIsActivated;
    }

    public String getName() {
        return name;
    }

    public static void setSwitchingControls(boolean switchingControls) {
        HeroAbility.switchingControls = switchingControls;
    }

    public static boolean isSwitchingControls() {
        return switchingControls;
    }

    public static void setActivatedHero(int activatedHero) {
        HeroAbility.activatedHero = activatedHero;
    }

    public static int getTimerAbility() {
        return timerAbility;
    }


    public static int activatedID(){
        return activatedHero;
    }




}
