package earth.terrarium.heracross.common.tasks;

import com.cobblemon.mod.common.pokemon.Pokemon;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.tasks.QuestTaskType;
import earth.terrarium.heracross.common.Heracross;
import net.minecraft.nbt.NumericTag;
import net.minecraft.resources.ResourceLocation;

public record EvolvePokemonTask(
    String id, String title, QuestIcon<?> icon, ResourceLocation pokemon, int target
) implements PokemonTask<Pokemon, EvolvePokemonTask> {

    public static final QuestTaskType<EvolvePokemonTask> TYPE = new Type<>(
            new ResourceLocation(Heracross.MOD_ID, "evolve_pokemon"),
            EvolvePokemonTask::new
    );

    @Override
    public NumericTag test(QuestTaskType<?> type, NumericTag progress, Pokemon input) {
        if (input.getSpecies().getResourceIdentifier().equals(this.pokemon)) {
            return storage().add(progress, 1);
        }
        return progress;
    }

    @Override
    public QuestTaskType<EvolvePokemonTask> type() {
        return TYPE;
    }
}
