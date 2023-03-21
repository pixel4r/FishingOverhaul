package com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.listeners;

import com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.utils.MathUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Random;

public class FishingEvents implements Listener {
    public static FishHook HOOK_ENTITY;
    public static double SUCCESS_RATE = 0;

    public FishingEvents () {

    }

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        Player player = event.getPlayer();
        HOOK_ENTITY = event.getHook();

        player.sendMessage("SUCCESS RATE: " + SUCCESS_RATE);

        switch (event.getState()) {
            case REEL_IN -> {
                player.sendMessage("REEL IN!");
                if (SUCCESS_RATE < 100) {
                    event.setCancelled(true);

                    if (SUCCESS_RATE < 30) return;
                    double rate_modifier = MathUtils.randDouble(-5, 8);
                    SUCCESS_RATE += rate_modifier;
                    if (rate_modifier < 0) HOOK_ENTITY.setVelocity(player.getFacing().getDirection().multiply(0.2));
                    if (rate_modifier > 0) HOOK_ENTITY.setVelocity(player.getFacing().getDirection().multiply(-0.2));

                } else {
                    SUCCESS_RATE = 0;
                    HOOK_ENTITY.eject();
                }
            }
            case FISHING -> {
                player.sendMessage("FISHING!");
            }
            case BITE -> {
                player.sendMessage("BITE!");
                SUCCESS_RATE += MathUtils.randDouble(30, 35);;

                if (HOOK_ENTITY.getPassengers().size() > 0) return;
                Entity entity = HOOK_ENTITY.getLocation().getWorld().spawnEntity(HOOK_ENTITY.getLocation(), randomCaughtableEntity());
                HOOK_ENTITY.addPassenger(entity);
            }
            case CAUGHT_FISH -> {
                player.sendMessage("CAUGHT_FISH!");
                event.setCancelled(true);
            }
            case FAILED_ATTEMPT -> {
                player.sendMessage("FAILED_ATTEMPT");
                SUCCESS_RATE -= MathUtils.randDouble(20, 25);;
            }
        }
    }

    private static EntityType randomCaughtableEntity() {
        EntityType[] entities = {
                EntityType.TROPICAL_FISH,
                EntityType.SALMON,
                EntityType.COD,
                EntityType.PUFFERFISH,
        };

        int rnd = new Random().nextInt(entities.length);
        return entities[rnd];
    }

}
