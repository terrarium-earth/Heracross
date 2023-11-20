package earth.terrarium.heracross.client;

import com.cobblemon.mod.common.pokemon.Species;
import earth.terrarium.heracles.api.client.settings.Settings;
import earth.terrarium.heracles.api.rewards.client.QuestRewardWidgets;
import earth.terrarium.heracles.api.tasks.QuestTaskDisplayFormatter;
import earth.terrarium.heracles.api.tasks.client.QuestTaskWidgets;
import earth.terrarium.heracles.api.tasks.client.display.TaskTitleFormatter;
import earth.terrarium.heracles.api.tasks.client.display.TaskTitleFormatters;
import earth.terrarium.heracles.common.handlers.progress.TaskProgress;
import earth.terrarium.heracross.client.settings.BefriendPokemonTaskSettings;
import earth.terrarium.heracross.client.settings.PokemonExpRewardSettings;
import earth.terrarium.heracross.client.settings.PokemonTaskSettings;
import earth.terrarium.heracross.client.tasks.BasicPokemonTaskWidget;
import earth.terrarium.heracross.client.tasks.PokemonExpRewardWidget;
import earth.terrarium.heracross.common.rewards.PokemonExpReward;
import earth.terrarium.heracross.common.tasks.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.chat.Component;

public class HeracrossClient {

    private static final Component ERROR = Component.literal("Error: Unknown").withStyle(ChatFormatting.RED);

    public static void init() {
        QuestTaskWidgets.registerSimple(CatchPokemonTask.TYPE, BasicPokemonTaskWidget::new);
        QuestTaskWidgets.registerSimple(DefeatPokemonTask.TYPE, BasicPokemonTaskWidget::new);
        QuestTaskWidgets.registerSimple(LoseToPokemonTask.TYPE, BasicPokemonTaskWidget::new);
        QuestTaskWidgets.registerSimple(EncounterPokemonTask.TYPE, BasicPokemonTaskWidget::new);
        QuestTaskWidgets.registerSimple(EvolvePokemonTask.TYPE, BasicPokemonTaskWidget::new);
        QuestTaskWidgets.registerSimple(BefriendPokemonTask.TYPE, BasicPokemonTaskWidget::new);
        QuestRewardWidgets.register(PokemonExpReward.TYPE, PokemonExpRewardWidget::new);

        TaskTitleFormatter.register(CatchPokemonTask.TYPE, HeracrossClient::getPokemonTaskTitle);
        TaskTitleFormatter.register(DefeatPokemonTask.TYPE, HeracrossClient::getPokemonTaskTitle);
        TaskTitleFormatter.register(LoseToPokemonTask.TYPE, HeracrossClient::getPokemonTaskTitle);
        TaskTitleFormatter.register(EncounterPokemonTask.TYPE, HeracrossClient::getPokemonTaskTitle);
        TaskTitleFormatter.register(EvolvePokemonTask.TYPE, HeracrossClient::getPokemonTaskTitle);
        TaskTitleFormatter.register(BefriendPokemonTask.TYPE, HeracrossClient::getPokemonTaskTitle);

        QuestTaskDisplayFormatter.register(CatchPokemonTask.TYPE, HeracrossClient::getPokemonTaskDisplay);
        QuestTaskDisplayFormatter.register(DefeatPokemonTask.TYPE, HeracrossClient::getPokemonTaskDisplay);
        QuestTaskDisplayFormatter.register(LoseToPokemonTask.TYPE, HeracrossClient::getPokemonTaskDisplay);
        QuestTaskDisplayFormatter.register(EncounterPokemonTask.TYPE, HeracrossClient::getPokemonTaskDisplay);
        QuestTaskDisplayFormatter.register(EvolvePokemonTask.TYPE, HeracrossClient::getPokemonTaskDisplay);
        QuestTaskDisplayFormatter.register(BefriendPokemonTask.TYPE, HeracrossClient::getPokemonTaskDisplay);

        Settings.register(CatchPokemonTask.TYPE, PokemonTaskSettings.CATCH);
        Settings.register(DefeatPokemonTask.TYPE, PokemonTaskSettings.DEFEAT);
        Settings.register(LoseToPokemonTask.TYPE, PokemonTaskSettings.LOSE_TO);
        Settings.register(EncounterPokemonTask.TYPE, PokemonTaskSettings.ENCOUNTER);
        Settings.register(EvolvePokemonTask.TYPE, PokemonTaskSettings.EVOLVE);
        Settings.register(BefriendPokemonTask.TYPE, BefriendPokemonTaskSettings.INSTANCE);
        Settings.register(PokemonExpReward.TYPE, PokemonExpRewardSettings.INSTANCE);
    }

    private static Component getPokemonTaskTitle(PokemonTask<?, ?> task) {
        return Component.translatable(
                TaskTitleFormatters.toTranslationKey(task, true),
                Optionull.mapOrDefault(task.getSpecies(), Species::getTranslatedName, ERROR)
        );
    }

    private static String getPokemonTaskDisplay(TaskProgress<NumericTag> progress, PokemonTask<?, ?> task) {
        return String.format("%d/%d", task.storage().read(progress.progress()), task.target());
    }
}
