package earth.terrarium.heracross.common.tasks;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.heracles.api.CustomizableQuestElement;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.quests.QuestIcons;
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon;
import earth.terrarium.heracles.api.tasks.QuestTask;
import earth.terrarium.heracles.api.tasks.QuestTaskType;
import earth.terrarium.heracles.api.tasks.storage.defaults.IntegerTaskStorage;
import net.minecraft.nbt.NumericTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public interface PokemonTask<I, T extends QuestTask<I, NumericTag, T>> extends QuestTask<I, NumericTag, T>, CustomizableQuestElement {

    ResourceLocation pokemon();

    int target();

    default Species getSpecies() {
        return PokemonSpecies.INSTANCE.getByIdentifier(pokemon());
    }

    @Override
    default float getProgress(NumericTag progress) {
        return storage().readInt(progress) / (float) target();
    }

    @Override
    default IntegerTaskStorage storage() {
        return IntegerTaskStorage.INSTANCE;
    }

    record Type<T extends PokemonTask<?, T>>(ResourceLocation id, Factory<T> factory) implements QuestTaskType<T> {

        @Override
        public Codec<T> codec(String id) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    RecordCodecBuilder.point(id),
                    Codec.STRING.fieldOf("title").orElse("").forGetter(PokemonTask::title),
                    QuestIcons.CODEC.fieldOf("icon").orElse(new ItemQuestIcon(Items.AIR)).forGetter(PokemonTask::icon),
                    ResourceLocation.CODEC.fieldOf("pokemon").forGetter(PokemonTask::pokemon),
                    Codec.INT.fieldOf("target").orElse(1).forGetter(PokemonTask::target)
            ).apply(instance, factory::create));
        }
    }

    interface Factory<T extends PokemonTask<?, T>> {

        T create(String id, String title, QuestIcon<?> icon, ResourceLocation pokemon, int target);
    }
}
