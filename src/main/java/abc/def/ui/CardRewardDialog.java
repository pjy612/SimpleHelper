package abc.def.ui;

import abc.def.SimpleHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardRewardDialog extends JDialog {
    final static private String CONTENT_COLOR = "000000";
    private JPanel contentPane;
    private JComboBox allCardComboBox;
    private JButton refreshButton;
    private JButton changeButton;
    private JButton addButton;

    public CardRewardDialog() {
        setTitle("Card Reward");
        setContentPane(contentPane);
        setModal(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        refreshButton.addActionListener(e -> {
            allCardComboBox.removeAllItems();
            SimpleHelper.toChangeCard = null;
            for (AbstractCard card : AbstractDungeon.commonCardPool.group) {
                allCardComboBox.addItem(cardToHtml(card, "999999"));
            }
            for (AbstractCard card : AbstractDungeon.uncommonCardPool.group) {
                allCardComboBox.addItem(cardToHtml(card, "0000CC"));
            }
            for (AbstractCard card : AbstractDungeon.rareCardPool.group) {
                allCardComboBox.addItem(cardToHtml(card, "FF9900"));
            }
            pack();
        });

        changeButton.addActionListener(e -> {
            int index = allCardComboBox.getSelectedIndex();
            if (index >= 0) {
                int[] sizes = new int[]{AbstractDungeon.commonCardPool.size(), AbstractDungeon.uncommonCardPool.size(), AbstractDungeon.rareCardPool.size()};
                if (index < sizes[0]) {
                    SimpleHelper.toChangeCard = AbstractDungeon.commonCardPool.group.get(index).makeCopy();
                } else if (index < sizes[0] + sizes[1]) {
                    SimpleHelper.toChangeCard = AbstractDungeon.uncommonCardPool.group.get(index - sizes[0]).makeCopy();
                } else if (index < sizes[0] + sizes[1] + sizes[2]) {
                    SimpleHelper.toChangeCard = AbstractDungeon.rareCardPool.group.get(index - sizes[0] - sizes[1]).makeCopy();
                }
                if (SimpleHelper.toChangeCard != null) {
                    if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                        SimpleHelper.adjustRewardForSelect(AbstractDungeon.combatRewardScreen.rewards);
                        AbstractDungeon.cardRewardScreen.update();
                    }
                }
            }
        });
        addButton.addActionListener(e -> {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW && AbstractDungeon.isScreenUp)
                return;
            AbstractCard card = null;
            int index = allCardComboBox.getSelectedIndex();
            if (index >= 0) {
                int[] sizes = new int[]{AbstractDungeon.commonCardPool.size(), AbstractDungeon.uncommonCardPool.size(), AbstractDungeon.rareCardPool.size()};
                if (index < sizes[0]) {
                    card = AbstractDungeon.commonCardPool.group.get(index).makeCopy();
                } else if (index < sizes[0] + sizes[1]) {
                    card = AbstractDungeon.uncommonCardPool.group.get(index - sizes[0]).makeCopy();
                } else if (index < sizes[0] + sizes[1] + sizes[2]) {
                    card = AbstractDungeon.rareCardPool.group.get(index - sizes[0] - sizes[1]).makeCopy();
                }
                if (card != null) {
                    if (AbstractDungeon.player != null) {
                        if (card.canUpgrade()) {
                            card.upgrade();
                        }
                        AbstractDungeon.player.masterDeck.addToTop(card);
                    }
                }
            }
        });
    }

    private String cardToHtml(AbstractCard card, String titleColor) {
        return String.format("<html><span style='color:#%s'>%s</span><br/><span style='color:#%s'>%s</span></html>", titleColor, card.name, CONTENT_COLOR, card.rawDescription);
    }

    public static void main(String[] args) {
        CardRewardDialog dialog = new CardRewardDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
