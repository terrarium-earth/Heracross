package earth.terrarium.heracross.common;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.api.events.pokemon.FriendshipUpdatedEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent;
import com.cobblemon.mod.common.api.events.pokemon.evolution.EvolutionCompleteEvent;
import earth.terrarium.heracles.common.handlers.progress.QuestProgressHandler;
import earth.terrarium.heracross.common.tasks.*;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class HeracrossEvents {

    public static void onPokemonCaptured(PokemonCapturedEvent event) {
        ServerPlayer player = event.getPlayer();
        QuestProgressHandler.getProgress(player.server, player.getUUID())
                .testAndProgressTaskType(player, event.getPokemon(), CatchPokemonTask.TYPE);
    }

    public static void onBattleVictory(BattleVictoryEvent event) {
        List<ServerPlayer> winners = actorsToPlayers(event.getWinners());
        List<ServerPlayer> losers = actorsToPlayers(event.getLosers());
        if (winners.isEmpty() && losers.isEmpty()) return;

        for (ServerPlayer winner : winners) {
            QuestProgressHandler.getProgress(winner.server, winner.getUUID())
                    .testAndProgressTaskType(winner, event.getLosers(), DefeatPokemonTask.TYPE);
            QuestProgressHandler.getProgress(winner.server, winner.getUUID())
                    .testAndProgressTaskType(winner, event.getLosers(), EncounterPokemonTask.TYPE);
        }

        for (ServerPlayer loser : losers) {
            QuestProgressHandler.getProgress(loser.server, loser.getUUID())
                    .testAndProgressTaskType(loser, event.getWinners(), LoseToPokemonTask.TYPE);
            QuestProgressHandler.getProgress(loser.server, loser.getUUID())
                    .testAndProgressTaskType(loser, event.getWinners(), EncounterPokemonTask.TYPE);
        }
    }

    public static void onFleeingBattle(BattleFledEvent event) {
        ServerPlayer player = event.getPlayer().getEntity();
        List<BattleActor> actors = new ArrayList<>();
        for (BattleActor actor : event.getBattle().getActors()) {
            if (actor.equals(event.getPlayer())) continue;
            actors.add(actor);
        }
        if (actors.isEmpty() || player == null) return;
        QuestProgressHandler.getProgress(player.server, player.getUUID())
                .testAndProgressTaskType(player, actors, EncounterPokemonTask.TYPE);
    }

    public static void onPokemonEvolved(EvolutionCompleteEvent event) {
        ServerPlayer player = event.getPokemon().getOwnerPlayer();
        if (player == null) return;
        QuestProgressHandler.getProgress(player.server, player.getUUID())
                .testAndProgressTaskType(player, event.getPokemon(), EvolvePokemonTask.TYPE);
    }

    public static void onPokemonFriendshipUpdate(FriendshipUpdatedEvent event) {
        ServerPlayer player = event.getPokemon().getOwnerPlayer();
        if (player == null) return;
        QuestProgressHandler.getProgress(player.server, player.getUUID())
                .testAndProgressTaskType(player, ObjectIntPair.of(event.getPokemon(), event.getNewFriendship()), BefriendPokemonTask.TYPE);
    }

    private static List<ServerPlayer> actorsToPlayers(List<BattleActor> actors) {
        return actors.stream()
                .filter(actor -> actor instanceof EntityBackedBattleActor<?>)
                .map(actor -> (EntityBackedBattleActor<?>) actor)
                .map(EntityBackedBattleActor::getEntity)
                .filter(entity -> entity instanceof ServerPlayer)
                .map(entity -> (ServerPlayer) entity)
                .toList();
    }
}
