package net.plazmix.cosmetics.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author JustCodeItBabe
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum DeathSoundManager {
    
    ENDERMAN("Эндермен") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_DEATH, 1F, 1F);
        }
    },
    PIG("Свинка") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.PIG_DEATH, 1F, 1F);
        }
    },
    FIREBALL("Фаербол") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1F, 1F);
        }
    },
    DRY_BONES("Иссохшие кости") {
        @Override
        public  void run(Player player) {
            player.playSound(player.getLocation(), Sound.SKELETON_DEATH, 1F, 1F);
        }
    },
    DING("Динь") {
        @Override
        public  void run(Player player) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
        }
    },
    SPLASH("Всплеск") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.SWIM, 1F, 1F);
        }
    },
    BAT("Летучая мышь") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.BAT_DEATH, 1F, 1F);
        }
    },
    PLOP("Бултых") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
        }
    },
    BLAZE("Блейз") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 1F, 1F);
        }
    },
    SPIDER("Паук") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.SPIDER_DEATH, 1F, 1F);
        }
    },
    FRAGILE("Разбитый") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.GLASS, 1F, 1F);
        }
    },
    DISINTEGRATED("Поломка") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1F, 1F);
        }
    },
    WITHER("Иссушитель") {
        @Override
        public void run(Player p) {
            p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
        }
    },
    BAZINGA("Бу-у") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.WITHER_IDLE, 1F, 1F);
        }
    },
    GRUMPY_VILLAGER("Ворчливый житель") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, 1F, 1F);
        }
    },
    SAD_PUPPY("Грустный песик") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.WOLF_DEATH, .95F, 0.65F);
        }
    },
    MONSTER_BURP("Отрыжка") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.BURP, 1F, .65F);
        }
    },
    AWW("Оу-у") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.WOLF_WHINE, 1F, 1F);
        }
    },
    SPLOOSH("Плюх") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.SPLASH, 1F, 1F);
        }
    },
    GHAST("Гаст") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.GHAST_DEATH, 1F, 1F);
        }
    },
    HORSE("Лошадь") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.HORSE_ZOMBIE_DEATH, 1F, 1F);
        }
    },
    BIG_BABY("Ужасающий крик") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 1F, 1F);
        }
    },
    MEOW( "Мяу!") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.CAT_MEOW, 1F, 1F);
        }
    },
    SCURRY("Жуткая беготня") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.SPIDER_WALK, 1F, 1F);
        }
    },
    HOWL("Вопль") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.WOLF_HOWL, 1F, 1F);
        }
    },
    FIREWORK("Фейерверк") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1F, 1F);
        }
    },
    DRAGON_ROAR("Рычание дракона") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 0.75F);
        }
    },
    MOO("Му-у!") {
        @Override
        public void run(Player player) {
            player.playSound(player.getLocation(), Sound.COW_IDLE, 1F, 0.75F);
        }
    };
    
    @Getter
    private final String name;
    
    public abstract void run(Player player);
    
}
