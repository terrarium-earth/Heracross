package earth.terrarium.heracross.client.tasks;

import com.cobblemon.mod.common.CobblemonItems;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.heracles.api.client.DisplayWidget;
import earth.terrarium.heracles.api.client.WidgetUtils;
import earth.terrarium.heracross.common.rewards.PokemonExpReward;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public record PokemonExpRewardWidget(PokemonExpReward reward) implements DisplayWidget {

    private static final String TITLE_SINGULAR = "reward.heracross.pokemon_exp.title.singular";
    private static final String TITLE_PLURAL = "reward.heracross.pokemon_exp.title.plural";
    private static final String DESC_SINGULAR = "reward.heracross.pokemon_exp.desc.singular";
    private static final String DESC_PLURAL = "reward.heracross.pokemon_exp.desc.plural";

    @Override
    public void render(GuiGraphics graphics, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        WidgetUtils.drawBackground(graphics, x, y, width, getHeight(width));
        int iconSize = 32;
        if (!reward.icon().render(graphics, scissor, x + 5, y + 5, iconSize, iconSize)) {
            WidgetUtils.drawItemIcon(graphics, CobblemonItems.EXP_SHARE.getDefaultInstance(), x + 5, y + 5, iconSize);
        }
        graphics.fill(x + iconSize + 9, y + 5, x + iconSize + 10, y + getHeight(width) - 5, 0xFF909090);
        String title = this.reward.amount() == 1 ? TITLE_SINGULAR : TITLE_PLURAL;
        String desc = this.reward.amount() == 1 ? DESC_SINGULAR : DESC_PLURAL;
        graphics.drawString(
                font,
                reward.titleOr(Component.translatable(title, this.reward.amount())), x + 48, y + 6, 0xFFFFFFFF,
                false
        );
        graphics.drawString(
                font,
                Component.translatable(desc, this.reward.amount()), x + 48, y + 8 + font.lineHeight, 0xFF808080,
                false
        );
    }

    @Override
    public int getHeight(int width) {
        return 42;
    }
}
