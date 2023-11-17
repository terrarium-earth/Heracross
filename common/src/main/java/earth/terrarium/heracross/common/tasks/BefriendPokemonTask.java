package earth.terrarium.heracross.common.tasks;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.quests.QuestIcons;
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon;
import earth.terrarium.heracles.api.tasks.QuestTaskType;
import earth.terrarium.heracross.common.Heracross;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.nbt.NumericTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public record BefriendPokemonTask(
    String id, String title, QuestIcon<?> icon, ResourceLocation pokemon, int friendship, int target
) implements PokemonTask<ObjectIntPair<Pokemon>, BefriendPokemonTask> {

    public static final QuestTaskType<BefriendPokemonTask> TYPE = new Type();
    @Override
    public NumericTag test(QuestTaskType<?> type, NumericTag progress, ObjectIntPair<Pokemon> input) {
        Pokemon pokemon = input.first();
        int friendship = input.secondInt();
        if (pokemon.getSpecies().getResourceIdentifier().equals(this.pokemon) && friendship == this.friendship) {
            return storage().add(progress, 1);
        }
        return progress;
    }

    @Override
    public QuestTaskType<BefriendPokemonTask> type() {
        return TYPE;
    }

    private static final class Type implements QuestTaskType<BefriendPokemonTask> {

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Heracross.MOD_ID, "befriend_pokemon");
        }

        @Override
        public Codec<BefriendPokemonTask> codec(String id) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    RecordCodecBuilder.point(id),
                    Codec.STRING.fieldOf("title").orElse("").forGetter(PokemonTask::title),
                    QuestIcons.CODEC.fieldOf("icon").orElse(new ItemQuestIcon(Items.AIR)).forGetter(PokemonTask::icon),
                    ResourceLocation.CODEC.fieldOf("pokemon").forGetter(PokemonTask::pokemon),
                    Codec.INT.fieldOf("friendship").orElse(5).forGetter(PokemonTask::target),
                    Codec.INT.fieldOf("target").orElse(1).forGetter(PokemonTask::target)
            ).apply(instance, BefriendPokemonTask::new));
        }
    }
}
