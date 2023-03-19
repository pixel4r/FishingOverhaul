package com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FishingEvents implements Listener {

    private static FishHook FISHING_HOOK;
    private static boolean IS_CAUGHT_READY = false;
    private static Entity CAUGHT_ENTITY;
    private static FishHook HOOK_ENTITY;
    private static int SUCCESS_RATE = 0;

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        HOOK_ENTITY = event.getHook();
        if (event.getState() == PlayerFishEvent.State.REEL_IN) {
            Player player = event.getPlayer();

            Location location = HOOK_ENTITY.getLocation();

            player.sendMessage("REEL IN!");
            event.getPlayer().sendMessage("SUCCESS RATE: " + SUCCESS_RATE);

            if (IS_CAUGHT_READY) {
                location.getWorld().spawnParticle(Particle.WATER_SPLASH, location, 40);
                player.showTitle(Title.title(Component.text("Ho"), Component.text("SubHo")));

                double distance = player.getLocation().distance(FISHING_HOOK.getLocation());
                player.sendMessage(distance + "");
                if (distance >= 3 && distance <= 25) {
                    HOOK_ENTITY.setVelocity(player.getFacing().getDirection().multiply(0.2));
                }

                if (SUCCESS_RATE < 100) {
                    event.setCancelled(true);
                }

                if (SUCCESS_RATE < 30) {

                }


            }

        }

        if (event.getState() == PlayerFishEvent.State.BITE) {
            event.getPlayer().sendMessage("BITE!");
            IS_CAUGHT_READY = true;
            CAUGHT_ENTITY = event.getCaught();
            event.setCancelled(true);
        }

        if (event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            event.getPlayer().sendMessage("FAIL!");
        }

        if (event.getState() == PlayerFishEvent.State.FISHING) {
            IS_CAUGHT_READY = false;
            FISHING_HOOK = event.getHook();
            event.getPlayer().sendMessage("FISHING!");
            event.getPlayer().sendMessage("FISHING HOOK:" + FISHING_HOOK.getType());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
            IS_CAUGHT_READY = false;
            return;
        }

        if (!IS_CAUGHT_READY) return;

        double rand = Math.random();
        double distance = player.getLocation().distance(FISHING_HOOK.getLocation());

        if (rand <= 0.7) {
            if (SUCCESS_RATE <= 60) {
                SUCCESS_RATE += 2;

                if (distance >= 3 && distance <= 25) {
                    HOOK_ENTITY.setVelocity(player.getFacing().getDirection().multiply(-0.15));
                }
            }
        } else {
            if (SUCCESS_RATE > 30) {
                SUCCESS_RATE -= 3;

                if (distance >= 3 && distance <= 25) {
                    HOOK_ENTITY.setVelocity(player.getFacing().getDirection().multiply(0.15));
                }
            }
        }


    }

//    @EventHandler
//    public void onShoot(ProjectileLaunchEvent event) {
////        if (event.getEntityType() != EntityType.PLAYER) return;
////        if (!(event.getEntity() instanceof FishHook fishHook)) return;
//
//        if (event.getEntity() instanceof FishHook fishHook){
//            fishing_hook = fishHook;
//            event.getEntity().sendMessage(fishHook.getType().translationKey());
//        }
//
//    }

}
