package gg.uhc.uhc.modules.health;

import com.google.common.collect.ImmutableList;
import gg.uhc.uhc.modules.DisableableModule;
import gg.uhc.uhc.modules.WorldMatcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class HealthRegenerationModule extends DisableableModule implements Listener {

    protected static final String GAME_RULE = "naturalRegeneration";
    protected static final String ICON_NAME = "Health Regeneration";

    protected static final short ENABLED_DATA = new Potion(PotionType.REGEN).toDamageValue();
    protected static final short DISABLED_DATA = new Potion(PotionType.WATER).toDamageValue();

    protected WorldMatcher worlds;

    public HealthRegenerationModule() {
        this.iconName = ICON_NAME;
        this.icon.setType(Material.POTION);
        this.icon.setWeight(-10);
    }

    @Override
    public void initialize(ConfigurationSection section) throws InvalidConfigurationException {
        worlds = new WorldMatcher(section, ImmutableList.of("world to not touch on enable/disable"), false);

        super.initialize(section);
    }

    @Override
    protected boolean isEnabledByDefault() {
        return false;
    }

    @Override
    protected void rerender() {
        super.rerender();

        if (isEnabled()) {
            icon.setDurability(ENABLED_DATA);
            icon.setLore("Natural health regeneration is enabled");
        } else {
            icon.setDurability(DISABLED_DATA);
            icon.setLore("Natural health regeneration is disabled");
        }
    }

    @Override
    public void onEnable() {
        for (World world : Bukkit.getWorlds()) {
            if (worlds.worldMatches(world)) {
                world.setGameRuleValue(GAME_RULE, "true");
            }
        }
    }

    @Override
    public void onDisable() {
        for (World world : Bukkit.getWorlds()) {
            if (worlds.worldMatches(world)) {
                world.setGameRuleValue(GAME_RULE, "false");
            }
        }
    }

    @EventHandler
    public void on(WorldLoadEvent event) {
        event.getWorld().setGameRuleValue(GAME_RULE, isEnabled() ? "true" : "false");
    }
}
