package earth.terrarium.heracross.common.tasks;

import com.cobblemon.mod.common.pokemon.Pokemon;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.tasks.QuestTaskType;
import earth.terrarium.heracross.common.Heracross;
import net.minecraft.nbt.NumericTag;
import net.minecraft.resources.ResourceLocation;

public record CatchPokemonTask(
    String id, String title, QuestIcon<?> icon, ResourceLocation pokemon, int target
) implements PokemonTask<Pokemon, CatchPokemonTask> {

    public static final QuestTaskType<CatchPokemonTask> TYPE = new PokemonTask.Type<>(
            new ResourceLocation(Heracross.MOD_ID, "catch_pokemon"),
            CatchPokemonTask::new
    );

    @Override
    public NumericTag test(QuestTaskType<?> type, NumericTag progress, Pokemon input) {
        if (input.getSpecies().getResourceIdentifier().equals(pokemon)) {
            return storage().add(progress, 1);
        }
        return progress;
    }

    @Override
    public QuestTaskType<CatchPokemonTask> type() {
        return TYPE;
    }
}
