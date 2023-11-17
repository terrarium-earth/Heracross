package earth.terrarium.heracross.client.settings;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import earth.terrarium.heracles.api.client.settings.CustomizableQuestElementSettings;
import earth.terrarium.heracles.api.client.settings.SettingInitializer;
import earth.terrarium.heracles.api.client.settings.base.AutocompleteTextSetting;
import earth.terrarium.heracles.api.client.settings.base.IntSetting;
import earth.terrarium.heracross.common.tasks.*;
import net.minecraft.Optionull;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public record PokemonTaskSettings<T extends PokemonTask<?, T>>(PokemonTask.Factory<T> factory) implements SettingInitializer<T>, CustomizableQuestElementSettings<T> {

    public static final PokemonTaskSettings<DefeatPokemonTask> DEFEAT = new PokemonTaskSettings<>(DefeatPokemonTask::new);
    public static final PokemonTaskSettings<CatchPokemonTask> CATCH = new PokemonTaskSettings<>(CatchPokemonTask::new);
    public static final PokemonTaskSettings<LoseToPokemonTask> LOSE_TO = new PokemonTaskSettings<>(LoseToPokemonTask::new);
    public static final PokemonTaskSettings<EncounterPokemonTask> ENCOUNTER = new PokemonTaskSettings<>(EncounterPokemonTask::new);
    public static final PokemonTaskSettings<EvolvePokemonTask> EVOLVE = new PokemonTaskSettings<>(EvolvePokemonTask::new);

    public static final AutocompleteTextSetting<ResourceLocation> ALL_SPECIES = new AutocompleteTextSetting<>(
            () -> PokemonSpecies.INSTANCE.getSpecies()
                    .stream()
                    .map(Species::getResourceIdentifier)
                    .toList(),
            (text, item) -> item.toString().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) && !item.toString().equalsIgnoreCase(text),
            r -> Optionull.mapOrDefault(r, ResourceLocation::toString, "")
    );

    @Override
    public CreationData create(@Nullable T object) {
        CreationData settings = CustomizableQuestElementSettings.super.create(object);
        settings.put("pokemon", ALL_SPECIES, getDefaultSpecies(object));
        settings.put("target", IntSetting.ONE, getDefaultCount(object));
        return settings;
    }

    @Override
    public T create(String id, T object, Data data) {
        return create(object, data, (title, icon) -> factory.create(
            id,
            title,
            icon,
            data.get("pokemon", ALL_SPECIES).orElse(getDefaultSpecies(object)),
            data.get("target", IntSetting.ONE).orElse(getDefaultCount(object))
        ));
    }


    private ResourceLocation getDefaultSpecies(T object) {
        return Optionull.mapOrElse(
                object, PokemonTask::pokemon,
                () -> new ResourceLocation("cobblemon", "charizard")
        );
    }

    private int getDefaultCount(T object) {
        return Optionull.mapOrDefault(object, PokemonTask::target, 1);
    }
}
