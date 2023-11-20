package earth.terrarium.heracross.common.rewards;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.experience.SidemodExperienceSource;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.heracles.api.CustomizableQuestElement;
import earth.terrarium.heracles.api.quests.QuestIcon;
import earth.terrarium.heracles.api.quests.QuestIcons;
import earth.terrarium.heracles.api.quests.defaults.ItemQuestIcon;
import earth.terrarium.heracles.api.rewards.QuestReward;
import earth.terrarium.heracles.api.rewards.QuestRewardType;
import earth.terrarium.heracross.common.Heracross;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.stream.Stream;

public record PokemonExpReward(String id, String title, QuestIcon<?> icon, int amount, boolean split) implements QuestReward<PokemonExpReward>, CustomizableQuestElement {

    public static final QuestRewardType<PokemonExpReward> TYPE = new Type();

    @Override
    public Stream<ItemStack> reward(ServerPlayer player) {
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        SidemodExperienceSource source = new SidemodExperienceSource(Heracross.MOD_ID);

        for (Pokemon pokemon : party) {
            pokemon.addExperienceWithPlayer(player, source, this.split ? amount / party.size() : amount);
        }
        return Stream.empty();
    }

    @Override
    public QuestRewardType<PokemonExpReward> type() {
        return TYPE;
    }

    private static class Type implements QuestRewardType<PokemonExpReward> {

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Heracross.MOD_ID, "pokemon_exp");
        }

        @Override
        public Codec<PokemonExpReward> codec(String id) {
            return RecordCodecBuilder.create(instance -> instance.group(
                    RecordCodecBuilder.point(id),
                    Codec.STRING.fieldOf("title").orElse("").forGetter(PokemonExpReward::title),
                    QuestIcons.CODEC.fieldOf("icon").orElse(new ItemQuestIcon(Items.AIR)).forGetter(PokemonExpReward::icon),
                    Codec.INT.fieldOf("amount").orElse(1).forGetter(PokemonExpReward::amount),
                    Codec.BOOL.fieldOf("split").orElse(true).forGetter(PokemonExpReward::split)
            ).apply(instance, PokemonExpReward::new));
        }
    }
}
