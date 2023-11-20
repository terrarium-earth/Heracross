package earth.terrarium.heracross.client.settings;

import earth.terrarium.heracles.api.client.settings.CustomizableQuestElementSettings;
import earth.terrarium.heracles.api.client.settings.SettingInitializer;
import earth.terrarium.heracles.api.client.settings.base.BooleanSetting;
import earth.terrarium.heracles.api.client.settings.base.IntSetting;
import earth.terrarium.heracross.common.rewards.PokemonExpReward;
import earth.terrarium.heracross.common.tasks.BefriendPokemonTask;
import earth.terrarium.heracross.common.tasks.PokemonTask;
import net.minecraft.Optionull;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PokemonExpRewardSettings implements SettingInitializer<PokemonExpReward>, CustomizableQuestElementSettings<PokemonExpReward> {

    public static final PokemonExpRewardSettings INSTANCE = new PokemonExpRewardSettings();

    @Override
    public CreationData create(@Nullable PokemonExpReward object) {
        CreationData settings = CustomizableQuestElementSettings.super.create(object);
        settings.put("amount", IntSetting.ONE, getDefaultAmount(object));
        settings.put("split", BooleanSetting.FALSE, getDefaultSplit(object));
        return settings;
    }

    @Override
    public PokemonExpReward create(String id, PokemonExpReward object, Data data) {
        return create(object, data, (title, icon) -> new PokemonExpReward(
            id,
            title,
            icon,
            data.get("amount", IntSetting.ONE).orElse(getDefaultAmount(object)),
            data.get("split", BooleanSetting.FALSE).orElse(getDefaultSplit(object))
        ));
    }

    private static int getDefaultAmount(@Nullable PokemonExpReward object) {
        return Optionull.mapOrDefault(object, PokemonExpReward::amount, 1);
    }

    private static boolean getDefaultSplit(@Nullable PokemonExpReward object) {
        return Optionull.mapOrDefault(object, PokemonExpReward::split, false);
    }
}
