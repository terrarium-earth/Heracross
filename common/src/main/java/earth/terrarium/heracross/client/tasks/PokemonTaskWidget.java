package earth.terrarium.heracross.client.tasks;

import com.cobblemon.mod.common.client.gui.PokemonGuiUtilsKt;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.heracles.api.client.DisplayWidget;
import earth.terrarium.heracles.api.client.WidgetUtils;
import earth.terrarium.heracles.api.tasks.QuestTaskDisplayFormatter;
import earth.terrarium.heracles.api.tasks.client.display.TaskTitleFormatter;
import earth.terrarium.heracles.common.handlers.progress.TaskProgress;
import earth.terrarium.heracross.common.tasks.PokemonTask;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.NumericTag;
import net.minecraft.network.chat.Component;

import java.util.HashSet;

public interface PokemonTaskWidget extends DisplayWidget {

    PokemonTask<?, ?> task();

    TaskProgress<NumericTag> progress();

    @Override
    default void render(GuiGraphics graphics, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        String singularDesc = task().type().id().toLanguageKey("task", "desc.singular");
        String pluralDesc = task().type().id().toLanguageKey("task", "desc.plural");


        WidgetUtils.drawBackground(graphics, x, y, width, getHeight(width));

        Component pokemonName = Optionull.mapOrDefault(task().getSpecies(), Species::getTranslatedName, Component.literal("???"));

        int iconSize = 32;
        if (!task().icon().render(graphics, scissor, x + 5, y + 5, iconSize, iconSize)) {
            if (this.task().getSpecies() != null) {
                try (var ignored = RenderUtils.createScissorBoxStack(scissor, Minecraft.getInstance(), graphics.pose(), x + 5, y + 5, iconSize, iconSize)) {
                    try (var pose = new CloseablePoseStack(graphics.pose())) {
                        pose.translate(x + 5 + iconSize / 2f, y - 15 + iconSize / 2f, 0);
                        PokemonGuiUtilsKt.drawProfilePokemon(
                                this.task().pokemon(),
                                new HashSet<>(),
                                graphics.pose(),
                                Axis.YP.rotationDegrees(-45f),
                                null,
                                partialTicks,
                                20f
                        );
                    }
                }
            }
        }
        graphics.fill(x + iconSize + 9, y + 5, x + iconSize + 10, y + getHeight(width) - 5, 0xFF909090);
        graphics.drawString(
                font,
                task().titleOr(TaskTitleFormatter.create(task())), x + iconSize + 16, y + 6, 0xFFFFFFFF,
                false
        );
        Component desc = this.task().target() > 1 ?
                Component.translatable(pluralDesc, this.task().target(), pokemonName) :
                Component.translatable(singularDesc, pokemonName);
        graphics.drawString(
                font,
                desc, x + iconSize + 16, y + 8 + font.lineHeight, 0xFF808080,
                false
        );
        String progress = QuestTaskDisplayFormatter.create(task(), this.progress());
        graphics.drawString(
                font,
                progress, x + width - 5 - font.width(progress), y + 6, 0xFFFFFFFF,
                false
        );

        int height = getHeight(width);
        WidgetUtils.drawProgressBar(graphics, x + iconSize + 16, y + height - font.lineHeight - 5, x + width - 5, y + height - 6, task(), this.progress());
    }

    @Override
    default int getHeight(int width) {
        return 42;
    }
}
