package earth.terrarium.heracross.client.settings;

import earth.terrarium.heracles.api.client.settings.CustomizableQuestElementSettings;
import earth.terrarium.heracles.api.client.settings.SettingInitializer;
import earth.terrarium.heracles.api.client.settings.base.IntSetting;
import earth.terrarium.heracross.common.tasks.BefriendPokemonTask;
import earth.terrarium.heracross.common.tasks.PokemonTask;
import net.minecraft.Optionull;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BefriendPokemonTaskSettings implements SettingInitializer<BefriendPokemonTask>, CustomizableQuestElementSettings<BefriendPokemonTask> {

    public static final BefriendPokemonTaskSettings INSTANCE = new BefriendPokemonTaskSettings();

    @Override
    public CreationData create(@Nullable BefriendPokemonTask object) {
        CreationData settings = CustomizableQuestElementSettings.super.create(object);
        settings.put("pokemon", PokemonTaskSettings.ALL_SPECIES, getDefaultSpecies(object));
        settings.put("target", IntSetting.ONE, getDefaultCount(object));
        settings.put("friendship", IntSetting.ONE, getDefaultFriendship(object));
        return settings;
    }

    @Override
    public BefriendPokemonTask create(String id, BefriendPokemonTask object, Data data) {
        return create(object, data, (title, icon) -> new BefriendPokemonTask(
            id,
            title,
            icon,
            data.get("pokemon", PokemonTaskSettings.ALL_SPECIES).orElse(getDefaultSpecies(object)),
            data.get("friendship", IntSetting.ONE).orElse(getDefaultFriendship(object)),
            data.get("target", IntSetting.ONE).orElse(getDefaultCount(object))
        ));
    }


    private ResourceLocation getDefaultSpecies(BefriendPokemonTask object) {
        return Optionull.mapOrElse(
                object, PokemonTask::pokemon,
                () -> new ResourceLocation("cobblemon", "charizard")
        );
    }

    private int getDefaultFriendship(BefriendPokemonTask object) {
        return Optionull.mapOrDefault(object, BefriendPokemonTask::friendship, 1);
    }

    private int getDefaultCount(BefriendPokemonTask object) {
        return Optionull.mapOrDefault(object, PokemonTask::target, 1);
    }
}
