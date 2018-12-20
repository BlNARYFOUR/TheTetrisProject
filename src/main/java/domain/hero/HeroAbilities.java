package domain.hero;

import domain.game.Player;

import java.util.List;

public class HeroAbilities {

    private final List<Player> players;
    private int timer = 30;

    public HeroAbilities(List<Player> players) {
        this.players = players;
    }



    public void activatedHeroAbility(final Player executor, String key){
        players.forEach(player -> {
            if (!player.equals(executor)){
                String hero = player.getUser().getHeroName();

                switch (hero){
                    case "pikachu":
                        player.mixedControls(key);
                        startTimer(player);
                        break;
                    case "donkeykong":
                        player.donkeyKongControls(key);
                        break;
                    default :
                        break;
                }

            }
        });
    }

    public void startTimer(Player player) {
        System.out.println(timer);

        if (timer == 0){
            timer = 30;
            players.remove(player);
        }
        timer--;
    }

    public void countBlock(int amountBlocks) {
        if (amountBlocks == 2){

        }
    }

    public void removePlayerFromAbility(Player player) {
        players.remove(player);
    }
}
