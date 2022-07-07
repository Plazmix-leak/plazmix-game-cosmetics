package net.plazmix.cosmetics.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.plazmix.cosmetics.game.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author JustCodeItBabe
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum KillEffectManager {
    
    SQUID_MISSILE("Ракета из осьминога") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            Squid squid = victim.getWorld().spawn(victim.getLocation().clone(), Squid.class);
            squid.setNoDamageTicks(1000);
            new BukkitRunnable() {
        
                int ticks = 100;
        
                @Override
                public void run() {
                    this.ticks = this.ticks - 5;
                    if (this.ticks <= 0) {
                        Firework f = squid.getWorld().spawn(squid.getLocation(), Firework.class);
                        FireworkMeta meta = f.getFireworkMeta();
                        meta.addEffect(FireworkEffect.builder()
                                .flicker(true)
                                .trail(true)
                                .with(FireworkEffect.Type.BALL)
                                .withColor(Color.BLACK)
                                .withFade(Color.BLACK)
                                .build()
                        );
                        f.setFireworkMeta(meta);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, f::detonate, 1);
                        squid.remove();
                        cancel();
                        return;
                    }
                    squid.setVelocity(new Vector(0D, .3D, 0D));
                    squid.getWorld().playSound(squid.getLocation(), Sound.ITEM_PICKUP, 1F, 1F);
                    if (this.ticks % 10 == 0)
                        UtilParticles.display(Particles.FLAME, squid.getLocation(), 1);
                }
            }.runTaskTimer(plugin, 0, 2);
        }
    }, // 0
    FIREWORK("Фейерверк") {
        @Override
        public  void run(JavaPlugin plugin, Player killer, Player victim) {
            Firework f = Fireworks.launch(victim.getLocation().clone().add(.5D, 0D, .5D));
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, f::detonate, 1);
        }
    }, // 1
    LIGHTNING_STRIKE("Молния") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
        }
    }, // 2
    TNT("Динамит") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            TNTPrimed tnt = victim.getWorld().spawn(victim.getLocation(), TNTPrimed.class);
            tnt.setVelocity(new Vector(0D, .75D, 0D));
            tnt.setFuseTicks(40);
            tnt.setMetadata("KILL_EFFECT_TNT", new FixedMetadataValue(plugin, true));
        }
    }, // 3
    CAT_EXPLOSION("Котик замедленного действия") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            Ocelot ocelot = victim.getWorld().spawn(victim.getLocation().clone().add(0, 1, 0), Ocelot.class);
            Ocelot.Type[] types = {
                    Ocelot.Type.BLACK_CAT, Ocelot.Type.RED_CAT, Ocelot.Type.SIAMESE_CAT
            };
            ocelot.setCatType(types[MathUtil.random(types.length)]);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ocelot.getWorld().createExplosion(ocelot.getLocation(), 0F);
                ocelot.remove();
            }, 40);
        }
    }, // 4
    BODY_EXPLOSION("Взрыв тела") {
        @Override
        public  void run(JavaPlugin plugin, Player killer, Player victim) {
            Location location = victim.getLocation();
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < 20; ++i) {
                Item item = location.getWorld().dropItem(location, MathUtil.random(2) == 0 ? new ItemStack(Material.BONE) : new ItemStack(Material.ROTTEN_FLESH));
                item.setVelocity(new Vector(new Random().nextDouble() - .5D, new Random().nextDouble() / 2, new Random().nextDouble() - .5D));
                item.setPickupDelay(Integer.MAX_VALUE);
                items.add(item);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                items.forEach(Item::remove);
                items.clear();
            }, 60);
        }
    }, // 5
    BUNNY_EXPLOSION("Взрыв из кроликов") {
    
        private final Random r = new Random();
        
        @Override
        public  void run(JavaPlugin plugin, Player killer, Player victim) {
            Location location = victim.getLocation().clone();
            List<Rabbit> rabbits = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Rabbit rabbit = location.getWorld().spawn(location.clone().add(0, 1, 0), Rabbit.class);
                rabbit.setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
                rabbit.setNoDamageTicks(Integer.MAX_VALUE);
                rabbit.setVelocity(new Vector(this.r.nextDouble() - .5D, this.r.nextDouble() / 2, this.r.nextDouble() - .5D));
                rabbits.add(rabbit);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (Rabbit rabbit : rabbits) {
                    UtilParticles.display(Particles.SMOKE_NORMAL, rabbit.getLocation(), 10);
                    rabbit.remove();
                }
                rabbits.clear();
            }, 60);
        }
    }, // 6
    FIRE_SHOES("Огненная поступь") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            new BukkitRunnable() {
        
                int ticks = 0;
        
                @Override
                public void run() {
                    if (this.ticks >= 30) {
                        cancel();
                        return;
                    }
                    UtilParticles.display(Particles.FLAME, killer.getLocation().add(MathsUtils.randomDouble(-.25D, .25D), 1D, MathsUtils.randomDouble(-.25D, .25)), 5);
                    ++this.ticks;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }, // 7
    COOKIE_FOUNTAIN("Фонтан из печанья") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            Location location = victim.getLocation().add(0d, 2d, 0d);
            new BukkitRunnable() {
        
                private final Random r = new Random();
        
                int ticks = 0;
        
                final List<Item> items = new ArrayList<>();
        
                @Override
                public void run() {
                    if (this.ticks >= 25) {
                        this.items.forEach(Entity::remove);
                        cancel();
                        return;
                    }
                    Item item = victim.getWorld().dropItem(location, new ItemStack(Material.COOKIE));
                    item.setPickupDelay(Integer.MAX_VALUE);
                    item.setVelocity(new Vector((this.r.nextDouble() - .5D) * .4D, .4D, (this.r.nextDouble() - .5D) * .4D));
                    item.getWorld().playSound(item.getLocation(), Sound.CHICKEN_EGG_POP, .2F, 1F);
                    this.items.add(item);
                    ++this.ticks;
                }
            }.runTaskTimer(plugin, 0, 2);
        }
    }, // 8
    MAGNOLIA("Цветущая магнолия") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            Location location = victim.getLocation().clone();
            new BukkitRunnable() {
        
                private double ticks = 0;
        
                @Override
                public void run() {
                    if (2.5D - this.ticks < 0) {
                        cancel();
                        return;
                    }
                    for (int i = 0; i < 10; ++i) {
                        UtilParticles.display(Particles.REDSTONE, MathUtil.random(2) == 0 ? 255 : 250, MathUtil.random(2) == 0 ? 145 : 190, MathUtil.random(2) == 0 ? 140 : 200, location.clone().add(MathsUtils.randomDouble(-1D, 1D), 2.5D - this.ticks, MathsUtils.randomDouble(-1D, 1D)));
                        UtilParticles.display(Particles.REDSTONE, MathUtil.random(2) == 0 ? 255 : 250, MathUtil.random(2) == 0 ? 145 : 190, MathUtil.random(2) == 0 ? 140 : 200, location.clone().add(MathsUtils.randomDouble(-1D, 1D), 2.5D - this.ticks, MathsUtils.randomDouble(-1D, 1D)));
                        UtilParticles.display(Particles.REDSTONE, MathUtil.random(2) == 0 ? 255 : 250, MathUtil.random(2) == 0 ? 145 : 190, MathUtil.random(2) == 0 ? 140 : 200, location.clone().add(MathsUtils.randomDouble(-1D, 1D), 2.5D - this.ticks, MathsUtils.randomDouble(-1D, 1D)));
                        UtilParticles.display(Particles.REDSTONE, MathUtil.random(2) == 0 ? 255 : 250, MathUtil.random(2) == 0 ? 145 : 190, MathUtil.random(2) == 0 ? 140 : 200, location.clone().add(MathsUtils.randomDouble(-1D, 1D), 2.5D - this.ticks, MathsUtils.randomDouble(-1D, 1D)));
                        UtilParticles.display(Particles.REDSTONE, MathUtil.random(2) == 0 ? 255 : 250, MathUtil.random(2) == 0 ? 145 : 190, MathUtil.random(2) == 0 ? 140 : 200, location.clone().add(MathsUtils.randomDouble(-1D, 1D), 2.5D - this.ticks, MathsUtils.randomDouble(-1D, 1D)));
                        UtilParticles.display(Particles.REDSTONE, MathUtil.random(2) == 0 ? 255 : 250, MathUtil.random(2) == 0 ? 145 : 190, MathUtil.random(2) == 0 ? 140 : 200, location.clone().add(MathsUtils.randomDouble(-1D, 1D), 2.5D - this.ticks, MathsUtils.randomDouble(-1D, 1D)));
                    }
                    this.ticks += .1D;
                }
            }.runTaskTimer(plugin, 0, 2);
        }
    }, // 10
    HEAD_ROCKET("Обезглавливание") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            ArmorStand stand = victim.getLocation().getWorld().spawn(victim.getLocation().clone().add(0d, -1d, 0d), ArmorStand.class);
            stand.setVisible(false);
            // TODO: вот здесь надо сетать голову игрока с текстуркой на стенд!
            stand.getWorld().playSound(stand.getLocation(), Sound.FIREWORK_LAUNCH, 1F, 1F);
            new BukkitRunnable() {
        
                int ticks = 50;
        
                private Random r = new Random();
        
                @Override
                public void run() {
                    if (this.ticks <= 0) {
                        stand.getWorld().playEffect(new Location(
                                stand.getWorld(),
                                stand.getLocation().getX() + this.r.nextDouble(),
                                stand.getLocation().getY() + 1D,
                                stand.getLocation().getZ() + this.r.nextDouble()
                        ), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                        stand.getWorld().playEffect(new Location(
                                stand.getWorld(),
                                stand.getLocation().getX() + this.r.nextDouble(),
                                stand.getLocation().getY() + 1D,
                                stand.getLocation().getZ() + this.r.nextDouble()
                        ), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                        stand.getWorld().playEffect(new Location(
                                stand.getWorld(),
                                stand.getLocation().getX() + this.r.nextDouble(),
                                stand.getLocation().getY() + 1D,
                                stand.getLocation().getZ() + this.r.nextDouble()
                        ), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                
                        stand.getWorld().playSound(stand.getLocation(), Sound.FIREWORK_BLAST, 1F, 1F);
                
                        stand.remove();
                        cancel();
                        return;
                    }
                    stand.setVelocity(new Vector(0D, .3D, 0D));
                    stand.setHeadPose(new EulerAngle(0D, Math.toRadians(this.ticks * 4), 0));
                    if (this.ticks % 2 == 0)
                        UtilParticles.display(Particles.EXPLOSION_NORMAL, stand.getLocation(), 1);
                    --this.ticks;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }, // 11
    BLOOD("Кровавый взрыв") {
    
        private final Random r = new Random();
        
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            victim.getWorld().playEffect(new Location(
                    victim.getWorld(),
                    victim.getLocation().getX() + this.r.nextDouble(),
                    victim.getLocation().getY() + 1D,
                    victim.getLocation().getZ() + this.r.nextDouble()
            ), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            victim.getWorld().playEffect(new Location(
                    victim.getWorld(),
                    victim.getLocation().getX() + this.r.nextDouble(),
                    victim.getLocation().getY() + 1D,
                    victim.getLocation().getZ() + this.r.nextDouble()
            ), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            victim.getWorld().playEffect(new Location(
                    victim.getWorld(),
                    victim.getLocation().getX() + this.r.nextDouble(),
                    victim.getLocation().getY() + 1D,
                    victim.getLocation().getZ() + this.r.nextDouble()
            ), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }
    }, // 12
    PINATA("Пината") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            Location location = victim.getEyeLocation();
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                Item item = location.getWorld().dropItem(location, ItemFactory.create(Material.INK_SACK, (byte) MathUtil.random(16), UUID.randomUUID().toString()));
                item.setVelocity(new Vector(new Random().nextDouble() - .5D, new Random().nextDouble() / 2, new Random().nextDouble() - .5D));
                item.setPickupDelay(Integer.MAX_VALUE);
                items.add(item);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                items.forEach(Item::remove);
                items.clear();
            }, 60);
        }
    }, // 13
    SHATTERED("Разбитый в вдребезги") {
    
        private final Random r = new Random();
        
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            killer.playSound(killer.getLocation(), Sound.GLASS, 1F, 1F);
            for (int i = 0; i < 2; ++i) {
                victim.getWorld().playEffect(new Location(
                        victim.getWorld(),
                        victim.getLocation().getX() + this.r.nextDouble(),
                        victim.getLocation().getY() + 1D,
                        victim.getLocation().getZ() + this.r.nextDouble()
                ), Effect.STEP_SOUND, Material.ICE);
                victim.getWorld().playEffect(new Location(
                        victim.getWorld(),
                        victim.getLocation().getX() + this.r.nextDouble(),
                        victim.getLocation().getY(),
                        victim.getLocation().getZ() + this.r.nextDouble()
                ), Effect.STEP_SOUND, Material.ICE);
                victim.getWorld().playEffect(new Location(
                        victim.getWorld(),
                        victim.getLocation().getX() + this.r.nextDouble(),
                        victim.getLocation().getY() - 1D,
                        victim.getLocation().getZ() + this.r.nextDouble()
                ), Effect.STEP_SOUND, Material.ICE);
            }
        }
    }, // 14
    IN_LOVE("В любви..") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            new BukkitRunnable() {
        
                int ticks = 0;
        
                final Location location = victim.getLocation().clone();
        
                @Override
                public void run() {
                    if (this.ticks >= 10)
                        cancel();
                    UtilParticles.display(Particles.HEART, .5F, .5F, .5F, this.location.clone().add(0, 1, 0), 3);
                    ++this.ticks;
                }
            }.runTaskTimer(plugin, 0, 4);
        }
    }, // 15
    TORNADO("Торнадо") {
        @Override
        public void run(JavaPlugin plugin, Player killer, Player victim) {
            Location location = new Location(victim.getWorld(), victim.getLocation().getX(), victim.getLocation().getY(), victim.getLocation().getZ());
            new BukkitRunnable() {
        
                int ticks = 4;
        
                @Override
                public void run() {
                    if (this.ticks == 0)
                        cancel();
                    double radius = .005D, y = location.getY();
            
                    for (double i = 0D; i < 50D; i += .1D) {
                
                        double x = radius * Math.sin(i), z = radius * Math.cos(i);
                
                        Location loc;
                        if (this.ticks % 2 == 0) {
                            loc = new Location(location.getWorld(), location.getX() + x, y, location.getZ() + z);
                        } else {
                            loc = new Location(location.getWorld(), location.getX() + z, y, location.getZ() + x);
                        }
                
                        UtilParticles.display(Particles.FIREWORKS_SPARK, loc, 1);
                
                        if (radius < 2.5D)
                            radius += .005D;
                        y += .01D;
                    }
                    UtilParticles.display(Particles.SMOKE_LARGE, location, 1);
                    --this.ticks;
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }; // 16
    
    @Getter
    private final String name;
    
    public abstract void run(JavaPlugin plugin, Player killer, Player victim);
    
}
