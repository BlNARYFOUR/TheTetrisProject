package domain.game;

import java.util.ArrayList;

public class HeroAbility {
    private static boolean switchingControls = false;
    private static int activatedHero;
    private static int timerAbility = 30;
    private String name;

    public HeroAbility(String name) {
        this.name = name;
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

    public static boolean activated(){
        System.out.println("switch " + switchingControls);
        return switchingControls;
    }

    public static int activatedID(){
        return activatedHero;
    }




}
