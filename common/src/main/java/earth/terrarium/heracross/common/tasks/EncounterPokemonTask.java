package earth.terrarium.heracross.common.tasks;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.tasks.QuestTaskType;
import earth.terrarium.heracross.common.Heracross;
import net.minecraft.nbt.NumericTag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record EncounterPokemonTask(
    String id, String title, QuestIcon<?> icon, ResourceLocation pokemon, int target
) implements PokemonTask<List<BattleActor>, EncounterPokemonTask> {

    public static final QuestTaskType<EncounterPokemonTask> TYPE = new Type<>(
            new ResourceLocation(Heracross.MOD_ID, "encounter_pokemon"),
            EncounterPokemonTask::new
    );

    @Override
    public NumericTag test(QuestTaskType<?> type, NumericTag progress, List<BattleActor> input) {
        for (BattleActor actor : input) {
            for (BattlePokemon pokemon : actor.getPokemonList()) {
                if (pokemon.getOriginalPokemon().getSpecies().getResourceIdentifier().equals(this.pokemon)) {
                    return storage().add(progress, 1);
                }
            }
        }
        return progress;
    }

    @Override
    public QuestTaskType<EncounterPokemonTask> type() {
        return TYPE;
    }
}
