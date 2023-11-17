package earth.terrarium.heracross.client.tasks;

import earth.terrarium.heracles.common.handlers.progress.TaskProgress;
import earth.terrarium.heracross.common.tasks.PokemonTask;
import net.minecraft.nbt.NumericTag;

public record BasicPokemonTaskWidget(PokemonTask<?, ?> task, TaskProgress<NumericTag> progress) implements PokemonTaskWidget {

}
