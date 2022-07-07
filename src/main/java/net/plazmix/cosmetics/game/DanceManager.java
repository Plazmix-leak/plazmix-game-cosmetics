package net.plazmix.cosmetics.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.plazmix.cosmetics.game.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftBat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWither;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author JustCodeItBabe
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public enum DanceManager {
    
    FIREWORKS("Фейерверки") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }
                    Fireworks.launch(player.getLocation().clone().add(0, 1, 0));
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }, // 0
    ANVIL_RAIN("Дождь из наковален") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            for (int i = 0; i < 100; ++i) {
                int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!player.isOnline())
                        return;
                    Location location = player.getLocation().add(MathsUtils.randomDouble(-50, 50), 40, MathsUtils.randomDouble(-50, 50));
                    if (location.getBlock().getType() == Material.AIR) {
                        Location loc = location.add(MathsUtils.randomDouble(5, 1), MathsUtils.randomDouble(8, 1), MathsUtils.randomDouble(5, 1));
                        for (int j = 0; j < 100; ++j) {
                            if (location.getBlock().getWorld().getHighestBlockAt(location).getType() == Material.ANVIL)
                                loc.getBlock().setType(Material.AIR);
                            if (loc.getBlock().getType() == Material.AIR) {
                                loc.getBlock().setType(Material.ANVIL);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> location.getWorld().getHighestBlockAt(location).setType(Material.AIR), 260);
                            }
                        }
                    }
                }, 10, 30);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 260);
            }
        }
    }, // 2
    COLD_SNAP("Покровитель льда") {
        @Override
        public void run(JavaPlugin plugin, Player p) {
            int[] radius = new int[]{0};
            Location location = p.getLocation();
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (!p.isOnline())
                    return;
                List<Block> blocks = BlockUtils.getBlocksInRadius(location, radius[0], false);
                blocks.forEach(block -> {
                    if (block.getType() != Material.AIR)
                        block.setType(Material.ICE);
                });
                radius[0] = radius[0] + 3;
            }, 0, 15);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 260);
        }
    }, // 3
    HORSE_RIDER("Наездник") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
                horse.setPassenger(player);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                Entity vehicle = player.getVehicle();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, vehicle::remove, 220);
            }, 5);
        }
    }, // 4
    SLIME_PLAYER("Слаймстер") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            player.addPotionEffect(new SimplePotionEffect(PotionEffectType.JUMP, 15, 20));
        }
    }, // 5
    DISCO("Танцор диско") {
        @Override
        public  void run(JavaPlugin plugin, Player player) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                Location location = player.getLocation().clone().add(0, -1, 0);
                double y = location.getY();
                for (double x = location.getX() - 6; x < location.getX() + 6; ++x) {
                    for (double z = location.getZ() - 6; z < location.getZ() + 6; ++z) {
                        Location loc = new Location(location.getWorld(), (int) x, (int) y, (int) z);
                        if (loc.getBlock().getType() != Material.AIR) {
                            loc.getBlock().setType(Material.STAINED_GLASS);
                            loc.getBlock().setData((byte) MathUtil.random(16));
                        }
                    }
                }
            }, 5, 5);
        }
    }, // 7
    TNT("Победный взрыв") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }
                    TNTPrimed primed = player.getLocation().getWorld().spawn(player.getLocation().add(0D, .25D, 0D), TNTPrimed.class);
                    primed.setVelocity(new Vector((new Random().nextDouble() - .5D) * .7D, 1.25D, (new Random().nextDouble() - .5D) * .7D));
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }, // 8
    NIGHT_SHIFT("Смена времени") {
        @Override
        public  void run(JavaPlugin plugin, Player player) {
            new BukkitRunnable() {
        
                int ticks = 0;
        
                @Override
                public void run() {
                    if (++this.ticks == 300) {
                        cancel();
                        return;
                    }
                    player.getWorld().setTime(player.getWorld().getTime() + 150);
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }, // 9
    RAINING_PIGS("Падающие с неба свинки") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            player.getInventory().setItemInHand(new ItemStack(Material.CARROT_STICK));
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (!player.isOnline())
                    return;
                for (int i = 0; i < 15; ++i) {
                    Location loc = player.getLocation().add(MathsUtils.randomDouble(-64, 64), 35, MathsUtils.randomDouble(-64, 64));
                    if (loc.getWorld().getHighestBlockAt(loc).getType() != Material.AIR)
                        return;
                    Pig pig = loc.getWorld().spawn(loc, Pig.class);
                    pig.setNoDamageTicks(Integer.MAX_VALUE);
                    pig.setSaddle(true);
                    pig.getVelocity().setX(0).setZ(0);
                }
            }, 0, 10);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 260);
        }
    }, // 10
    RAINBOW_RAIN("Радужный дождь") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }
                    Location location = player.getLocation().clone().add(.5D, 5D, .5D);
                    for (int i = 0; i < 20; ++i) {
                        FallingBlock block = player.getWorld().spawnFallingBlock(location, Material.WOOL, (byte) MathUtil.random(16));
                        block.setDropItem(false);
                        block.setVelocity(new Vector(MathsUtils.randomDouble(-.5D, .5D), new Random().nextDouble() + .5D, MathsUtils.randomDouble(-.5D, .5D)));
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1F, 1F);
                    }
                }
            }.runTaskTimer(plugin, 5, 10);
        }
    }, // 11
    RAINBOW_DOLLY("Овечка Долли") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            JListener listener = new JListener(plugin) {
                @EventHandler(priority = EventPriority.MONITOR)
                public void onDamage(EntityDamageEvent e) {
                    Entity entity = e.getEntity();
                    if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK || entity.hasMetadata("HIT"))
                        return;
                    if (entity instanceof Sheep) {
                        entity.setMetadata("HIT", new FixedMetadataValue(plugin, true));
                        Sheep sheep = player.getWorld().spawn(entity.getLocation(), Sheep.class);
                        sheep.setColor(DyeColor.values()[MathUtil.random(15)]);
                        UtilParticles.display(Particles.EXPLOSION_LARGE, sheep.getLocation(), 1);
                        sheep.setVelocity(new Vector((new Random().nextDouble() - .5D) * .7D, .75D, (new Random().nextDouble() - .5D) * .7D));
                    }
                }
            };
            Sheep sheep = player.getWorld().spawn(player.getLocation(), Sheep.class);
            sheep.setColor(DyeColor.values()[MathUtil.random(15)]);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, listener::unregister, 260);
        }
    }, // 12
    STORM("Гнев небес") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            player.getWorld().setTime(13000);
            player.getWorld().setStorm(true);
            player.getWorld().setThundering(true);
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (!player.isOnline())
                    return;
                Location location = player.getLocation().clone().add(
                        ThreadLocalRandom.current().nextInt(-64, 64),
                        0,
                        ThreadLocalRandom.current().nextInt(-64, 64)
                );
                while (location.add(0, -2, 0).getBlock().getType() == Material.AIR)
                    location = player.getLocation().clone().add(
                            ThreadLocalRandom.current().nextInt(-64, 64),
                            0,
                            ThreadLocalRandom.current().nextInt(-64, 64)
                    );
                location.getWorld().strikeLightningEffect(location);
                Location loc = location.clone();
                loc.setY(player.getWorld().getHighestBlockYAt(location));
                Skeleton entity = player.getWorld().spawn(loc, Skeleton.class);
                entity.setSkeletonType(Skeleton.SkeletonType.NORMAL);
            }, 0, 5);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Bukkit.getScheduler().cancelTask(task), 260);
        }
    }, // 13
    INSANE_BLOCKS("Безумные блоки") {
        @Override
        public  void run(JavaPlugin plugin, Player player) {
            new BukkitRunnable() {
        
                int ticks = 20;
        
                @Override
                public void run() {
                    --this.ticks;
                    Location location = player.getLocation();
                    for (Block block : BlockUtils.getBlocksInRadius(location.clone().add(0D, -1D, 0D), (10 - this.ticks), true)) {
                        if (MathUtil.random(100) < 30) {
                            if (block.getType() != Material.AIR) {
                                if (block.getRelative(BlockFace.UP).getType() == Material.AIR) {
                                    FallingBlock f = location.getWorld().spawnFallingBlock(
                                            block.getLocation().clone().add(0D, 1.1D, 0D),
                                            block.getType(),
                                            block.getData()
                                    );
                                    f.setVelocity(new Vector(0D, .3D, 0D));
                                    f.setDropItem(false);
                                    f.setHurtEntities(false);
                                    Bat bat = location.getWorld().spawn(block.getLocation().clone().add(0d, 1d, 0d), Bat.class);
                                    ((CraftBat) bat).getHandle().b(true);
                                    bat.addPotionEffect(new SimplePotionEffect(PotionEffectType.INVISIBILITY, 1, Integer.MAX_VALUE));
                                    bat.setPassenger(f);
                                }
                            }
                        }
                    }
                    if (this.ticks <= 0)
                        cancel();
                }
            }.runTaskTimer(plugin, 0, 2);
        }
    }, // 14
    TOY_STICK("Палка-крушитель") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            player.playSound(player.getLocation(), Sound.ENDERMAN_DEATH, 1F, .5F);
            player.getInventory().setItemInHand(new ItemStack(Material.STICK));
            JListener listener = new JListener(plugin) {
                @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
                public void onClick(PlayerInteractEvent e) {
                    if (e.getItem().hasItemMeta() && e.getItem().getType() == Material.STICK) {
                        Block block = e.getClickedBlock();
                        if (block == null)
                            return;
                        player.setVelocity(new Vector(0D, 3D, -1.75D));
                        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);
                        List<Block> blocks = BlockUtils.getBlocksInRadius(block.getLocation(), 5, false);
                        blocks.forEach(b -> {
                            if (b.getType() != Material.AIR)
                                boom(b);
                        });
                    }
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, listener::unregister, 260);
        }
    
        void boom(Block block) {
            Entity entity;
            if (block == null)
                return;
            if (block.getType() == Material.TNT) {
                entity = block.getWorld().spawn(block.getLocation().add(0D, 1D, 0D), TNTPrimed.class);
            } else {
                entity = block.getWorld().spawnFallingBlock(block.getLocation().add(0D, 1D, 0D), block.getType(), block.getData());
                ((FallingBlock) entity).setDropItem(false);
            }
            block.setType(Material.AIR);
        
            float x = -1F + (float) (Math.random() * 3D);
            float y = .5F;
            float z = -.3F + (float) (Math.random() * 1.6D);
            
            entity.setVelocity(new Vector(x, y, z));
        }
    }, // 15
    DARE_DEVIL("Демонический скакун") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                JListener listener = new JListener(plugin) {
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void onDamage(EntityDamageEvent e) {
                        Entity entity = e.getEntity();
                        if (entity instanceof Horse) {
                            e.setCancelled(true);
                        }
                    }
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void PlayerMoveEvent(PlayerMoveEvent e) {
                        Player p = e.getPlayer();
                        if (p.getVehicle() == null)
                            return;
                        Entity entity = p.getVehicle();
                        if (entity.getType() == EntityType.HORSE) {
                            Vector v = p.getLocation().getDirection();
                            entity.setVelocity(v.multiply(.65D));
                            ((CraftEntity) entity).getHandle().setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                        }
                    }
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
                        Player p = e.getPlayer();
                        Entity vehicle = p.getVehicle();
                        if (p.isSneaking()) {
                            if (vehicle instanceof Horse)
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> vehicle.setPassenger(p), 1);
                            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                                if (vehicle instanceof Horse)
                                    vehicle.remove();
                            } else if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                                if (vehicle instanceof Horse)
                                    vehicle.remove();
                            }
                        }
                    }
            
                };
                Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
                horse.setVariant(Horse.Variant.SKELETON_HORSE);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.setPassenger(player);
                player.playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);
                Entity vehicle = player.getVehicle();
                int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!player.isOnline())
                        return;
                    UtilParticles.display(Particles.FLAME, .4F, .2F, .4F, vehicle.getLocation().clone().add(0, 1, 0), 5);
                    Set<Block> blocks = LocationUtil.getBlocksInRadius(vehicle.getLocation(), 3D).keySet();
                    blocks.removeIf(block -> block.getType() == Material.AIR);
                    if (blocks.isEmpty())
                        return;
                    Iterator<Block> iterator = blocks.iterator();
                    while (iterator.hasNext()) {
                        Block block = iterator.next();
                        if (block.isLiquid()) {
                            iterator.remove();
                            continue;
                        }
                        if (block.getRelative(BlockFace.UP).isLiquid())
                            iterator.remove();
                    }
                    blocks.forEach(b -> {
                        if (b.getType() != Material.AIR) {
                            b.setType(Material.AIR);
                        }
                    });
                    for (int i = 0; i < 3; ++i)
                        UtilParticles.display(Particles.EXPLOSION_LARGE, vehicle.getLocation().clone().add(MathsUtils.randomDouble(-1, 1), MathsUtils.randomDouble(-2, 2), MathsUtils.randomDouble(-1, 1)), 5);
                    player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, .85F, 1F);
                }, 5, 2);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    vehicle.remove();
                    Bukkit.getScheduler().cancelTask(task);
                    listener.unregister();
                }, 260);
            }, 5);
        }
    }, // 16
    CHICKEN_APOCALYPSES("Куриный апокалипсис") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            new BukkitRunnable() {
        
                int ticks = 0;
        
                @Override
                public void run() {
                    for (int i = 0; i < 15; ++i) {
                        Location location = player.getLocation().clone().add(
                                ThreadLocalRandom.current().nextInt(-32, 32),
                                0,
                                ThreadLocalRandom.current().nextInt(-32, 32)
                        );
                        location.setY(player.getWorld().getHighestBlockYAt(location) + 3);
                        Chicken chicken = player.getWorld().spawn(location, Chicken.class);
                        chicken.setCustomNameVisible(true);
                        chicken.setCustomName("Курица войны");
                        Projectile egg = chicken.getWorld().spawn(chicken.getLocation().clone().add(0D, 1D, 0D), Egg.class);
                        egg.setShooter(chicken);
                        egg.setVelocity(new Vector(0D, 2D, 0D));
                    }
                    
                    if (++this.ticks >= 42)
                        cancel();
                }
            }.runTaskTimer(plugin, 0, 5);
        }
    }, // 17
    DRAGON_RIDER("Дракон") {
        @Override
        public  void run(JavaPlugin plugin, Player player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                JListener listener = new JListener(plugin) {
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void onDamage(EntityDamageEvent e) {
                        Entity entity = e.getEntity();
                        if (entity instanceof EnderDragon) {
                            e.setCancelled(true);
                        }
                    }
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void PlayerMoveEvent(PlayerMoveEvent e) {
                        Player p = e.getPlayer();
                        if (p.getVehicle() == null)
                            return;
                        Entity entity = p.getVehicle();
                        if (entity.getType() == EntityType.ENDER_DRAGON) {
                            Vector v = p.getLocation().getDirection();
                            entity.setVelocity(v.multiply(.5D));
                            ((CraftEntity) entity).getHandle().setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), p.getLocation().getYaw() - 180, p.getLocation().getPitch());
                        }
                    }
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
                        Player p = e.getPlayer();
                        Entity vehicle = p.getVehicle();
                        if (p.isSneaking()) {
                            if (vehicle instanceof EnderDragon)
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> vehicle.setPassenger(p), 1);
                            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                                if (vehicle instanceof EnderDragon)
                                    vehicle.remove();
                            } else if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                                if (vehicle instanceof EnderDragon)
                                    vehicle.remove();
                            }
                        }
                    }
            
                    @EventHandler
                    public void onClick(PlayerInteractEvent e) {
                        Player p = e.getPlayer();
                        if (p.getVehicle() instanceof EnderDragon) {
                            Fireball f = p.getWorld().spawn(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize().multiply(2)), Fireball.class);
                            f.setShooter(p);
                            f.setYield(16F);
                            f.setIsIncendiary(true);
                            f.setVelocity(p.getEyeLocation().clone().getDirection().normalize().multiply(3));
                        }
                    }
            
                };
                EnderDragon dragon = player.getWorld().spawn(player.getLocation(), EnderDragon.class);
                dragon.setPassenger(player);
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 5F, 1F);
                Entity vehicle = player.getVehicle();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    vehicle.remove();
                    listener.unregister();
                }, 260);
            }, 5);
        }
    }, // 18
    WITHER_RIDER("Иссушитель") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                JListener listener = new JListener(plugin) {
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void onDamage(EntityDamageEvent e) {
                        Entity entity = e.getEntity();
                        if (entity instanceof Wither) {
                            e.setCancelled(true);
                        }
                    }
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void PlayerMoveEvent(PlayerMoveEvent e) {
                        Player p = e.getPlayer();
                        if (p.getVehicle() == null)
                            return;
                        Entity entity = p.getVehicle();
                        if (entity.getType() == EntityType.WITHER) {
                            Vector v = p.getLocation().getDirection();
                            entity.setVelocity(v.multiply(.65D));
                            ((CraftEntity) entity).getHandle().setPositionRotation(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                        }
                    }
            
                    @EventHandler(priority = EventPriority.MONITOR)
                    public void PlayerTeleportEvent(PlayerTeleportEvent e) {
                        Player p = e.getPlayer();
                        Entity vehicle = p.getVehicle();
                        if (p.isSneaking()) {
                            if (vehicle instanceof Wither)
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> vehicle.setPassenger(p), 1);
                            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                                if (vehicle instanceof Wither)
                                    vehicle.remove();
                            } else if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                                if (vehicle instanceof Wither)
                                    vehicle.remove();
                            }
                        }
                    }
            
                    @EventHandler
                    public void onClick(PlayerInteractEvent e) {
                        Player p = e.getPlayer();
                        if (p.getVehicle() instanceof Wither) {
                            WitherSkull skull = p.getWorld().spawn(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize().multiply(2)), WitherSkull.class);
                            skull.setShooter(p);
                            skull.setCharged(true);
                            skull.setYield(16F);
                            skull.setIsIncendiary(false);
                            skull.setVelocity(p.getEyeLocation().clone().getDirection().normalize().multiply(3));
                        }
                    }
            
                };
                Wither wither = player.getWorld().spawn(player.getLocation(), Wither.class);
                ((CraftWither) wither).getHandle().b(true);
                wither.setPassenger(player);
                player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 10F, 1F);
                Entity vehicle = player.getVehicle();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    vehicle.remove();
                    listener.unregister();
                }, 260);
            }, 5);
        }
    }, // 19
    CAT_APOCALYPSES("Тверк котиков") {
        @Override
        public void run(JavaPlugin plugin, Player player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (int i = 0; i < 55; ++i) {
                    Location loc = player.getLocation().clone().add(
                            ThreadLocalRandom.current().nextInt(-32, 32),
                            0,
                            ThreadLocalRandom.current().nextInt(-32, 32)
                    );
                    while (loc.add(0, -2, 0).getBlock().getType() == Material.AIR)
                        loc = player.getLocation().clone().add(
                                ThreadLocalRandom.current().nextInt(-50, 50),
                                0,
                                ThreadLocalRandom.current().nextInt(-50, 50)
                        );
                    loc.setY(player.getWorld().getHighestBlockYAt(loc) + 2);
                    Ocelot ocelot = player.getWorld().spawn(loc, Ocelot.class);
                    ocelot.setNoDamageTicks(Integer.MAX_VALUE);
                    ocelot.setCatType(Ocelot.Type.values()[MathUtil.random(Ocelot.Type.values().length)]);
                    int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                        ocelot.setSitting(true);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> ocelot.setSitting(false), 7);
                    }, 5, 17);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        ocelot.remove();
                        Bukkit.getScheduler().cancelTask(task);
                    }, 260);
                }
                for (int i = 0; i < 55; ++i) {
                    Location loc = player.getLocation().clone().add(
                            ThreadLocalRandom.current().nextInt(-50, 50),
                            0,
                            ThreadLocalRandom.current().nextInt(-50, 50)
                    );
                    loc.setY(player.getWorld().getHighestBlockYAt(loc) + 2);
                    Ocelot ocelot = player.getWorld().spawn(loc, Ocelot.class);
                    ocelot.setNoDamageTicks(Integer.MAX_VALUE);
                    ocelot.setCatType(Ocelot.Type.values()[MathUtil.random(Ocelot.Type.values().length)]);
                    int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                        ocelot.setSitting(true);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> ocelot.setSitting(false), 7);
                    }, 5, 17);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        ocelot.remove();
                        Bukkit.getScheduler().cancelTask(task);
                    }, 260);
                }
            }, 5);
        }
    }; // 20
    
    @Getter
    private final String name;
    
    public abstract void run(JavaPlugin plugin, Player player);
    
}
