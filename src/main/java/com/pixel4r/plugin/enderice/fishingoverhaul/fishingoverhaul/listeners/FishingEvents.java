package com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.listeners;

import com.pixel4r.plugin.enderice.fishingoverhaul.fishingoverhaul.utils.MathUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingEvents implements Listener {
    public static FishHook HOOK_ENTITY;
    public static Entity CAUGHT_ENTITY = null;
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
                    player.getLocation().getWorld().spawnEntity(player.getLocation(), CAUGHT_ENTITY.getType());
                }
            }
            case FISHING -> {
                player.sendMessage("FISHING!");
            }
            case BITE -> {
                player.sendMessage("BITE!");
                CAUGHT_ENTITY = event.getCaught();
                SUCCESS_RATE += 35;
            }
            case CAUGHT_FISH -> {
                player.sendMessage("CAUGHT_FISH!");
                event.setCancelled(true);
            }
            case FAILED_ATTEMPT -> {
                player.sendMessage("FAILED_ATTEMPT");
                CAUGHT_ENTITY = null;
                SUCCESS_RATE -= 25;
            }
        }

        if (CAUGHT_ENTITY != null) player.sendMessage(CAUGHT_ENTITY.getType().translationKey());

    }
}
