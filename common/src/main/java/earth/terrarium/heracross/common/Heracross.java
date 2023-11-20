package earth.terrarium.heracross.common;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import earth.terrarium.heracles.api.rewards.QuestRewards;
import earth.terrarium.heracles.api.tasks.QuestTasks;
import earth.terrarium.heracross.common.rewards.PokemonExpReward;
import earth.terrarium.heracross.common.tasks.*;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.function.Consumer;

public class Heracross {
	public static final String MOD_ID = "heracross";

	public static void init() {
		QuestTasks.register(CatchPokemonTask.TYPE);
		QuestTasks.register(DefeatPokemonTask.TYPE);
		QuestTasks.register(LoseToPokemonTask.TYPE);
		QuestTasks.register(EncounterPokemonTask.TYPE);
		QuestTasks.register(EvolvePokemonTask.TYPE);
		QuestTasks.register(BefriendPokemonTask.TYPE);
		QuestRewards.register(PokemonExpReward.TYPE);

		CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, toUnit(HeracrossEvents::onPokemonCaptured));
		CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, toUnit(HeracrossEvents::onBattleVictory));
		CobblemonEvents.BATTLE_FLED.subscribe(Priority.NORMAL, toUnit(HeracrossEvents::onFleeingBattle));
		CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, toUnit(HeracrossEvents::onPokemonEvolved));
		CobblemonEvents.FRIENDSHIP_UPDATED.subscribe(Priority.NORMAL, toUnit(HeracrossEvents::onPokemonFriendshipUpdate));
	}

	private static <T> Function1<T, Unit> toUnit(Consumer<T> function) {
		return t -> {
			function.accept(t);
			return Unit.INSTANCE;
		};
	}
}