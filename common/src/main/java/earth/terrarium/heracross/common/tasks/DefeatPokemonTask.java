package earth.terrarium.heracross.common.tasks;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.tasks.QuestTaskType;
import earth.terrarium.heracross.common.Heracross;
import net.minecraft.nbt.NumericTag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record DefeatPokemonTask(
    String id, String title, QuestIcon<?> icon, ResourceLocation pokemon, int target
) implements PokemonTask<List<BattleActor>, DefeatPokemonTask> {

    public static final QuestTaskType<DefeatPokemonTask> TYPE = new PokemonTask.Type<>(
            new ResourceLocation(Heracross.MOD_ID, "defeat_pokemon"),
            DefeatPokemonTask::new
    );

    @Override
    public NumericTag test(QuestTaskType<?> type, NumericTag progress, List<BattleActor> input) {
        int defeated = 0;
        for (BattleActor actor : input) {
            for (BattlePokemon pokemon : actor.getPokemonList()) {
                if (pokemon.getOriginalPokemon().getSpecies().getResourceIdentifier().equals(this.pokemon)) {
                    defeated++;
                }
            }
        }
        return storage().add(progress, defeated);
    }

    @Override
    public QuestTaskType<DefeatPokemonTask> type() {
        return TYPE;
    }
}
