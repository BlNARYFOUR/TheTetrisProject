package data.heroesRepository;

import domain.hero.Hero;

import java.util.List;

/**
 * Contract for HeroesRepository.
 */
public interface HeroesRepository {
    void addHero(Hero h);
    Hero getHero(String name);
    Hero deleteHero(String name);
    List<Hero> getAllHeroes();
}
