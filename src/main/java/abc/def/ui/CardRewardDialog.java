package abc.def.ui;

import abc.def.SimpleHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import javax.swing.*;
import java.util.concurrent.locks.Lock;

public class CardRewardDialog extends JDialog {
    final static private String CONTENT_COLOR = "000000";
    private JPanel contentPane;
    private JComboBox allCardComboBox;
    private JButton refreshButton;
    private JButton changeButton;
    private JButton addButton;
    private JLabel tip;

    public CardRewardDialog() {
        setTitle("Card Reward");
        setContentPane(contentPane);
        setModal(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);
        tip.setVisible(false);
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
                SimpleHelper.queue.offer(() -> {
                    int[] sizes = new int[]{AbstractDungeon.commonCardPool.size(), AbstractDungeon.uncommonCardPool.size(), AbstractDungeon.rareCardPool.size()};
                    AbstractCard card = null;
                    if (index < sizes[0]) {
                        card = AbstractDungeon.commonCardPool.group.get(index);
                    } else if (index < sizes[0] + sizes[1]) {
                        card = AbstractDungeon.uncommonCardPool.group.get(index - sizes[0]);
                    } else if (index < sizes[0] + sizes[1] + sizes[2]) {
                        card = AbstractDungeon.rareCardPool.group.get(index - sizes[0] - sizes[1]);
                    }
                    if (card != null) {
                        SimpleHelper.toChangeCard = CardLibrary.getCopy(card.cardID, 100, 100);
                        if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                            SimpleHelper.adjustRewardForSelect(AbstractDungeon.combatRewardScreen.rewards);
                            AbstractDungeon.cardRewardScreen.update();
                        }
                    }
                });
                //有消息入队后激活轮询线程
                synchronized (Lock.class)
                {
                    Lock.class.notify();
                }
            }
        });
        addButton.addActionListener(e -> {
//            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW && AbstractDungeon.isScreenUp) {
//                return;
//            }
            int index = allCardComboBox.getSelectedIndex();
            if (index >= 0) {
                SimpleHelper.queue.offer(() -> {
                    AbstractCard card = null;
                    int[] sizes = new int[]{AbstractDungeon.commonCardPool.size(), AbstractDungeon.uncommonCardPool.size(), AbstractDungeon.rareCardPool.size()};
                    if (index < sizes[0]) {
                        card = AbstractDungeon.commonCardPool.group.get(index);
                    } else if (index < sizes[0] + sizes[1]) {
                        card = AbstractDungeon.uncommonCardPool.group.get(index - sizes[0]);
                    } else if (index < sizes[0] + sizes[1] + sizes[2]) {
                        card = AbstractDungeon.rareCardPool.group.get(index - sizes[0] - sizes[1]);
                    }
                    if (card != null) {
                        if (AbstractDungeon.player != null) {
                            AbstractCard finalCard = card;
                            AbstractCard finalCard1 = CardLibrary.getCopy(finalCard.cardID, 100, 100);
                            if (finalCard1.canUpgrade()) {
                                finalCard1.upgrade();
                            }
                            AbstractDungeon.player.masterDeck.addToTop(finalCard1);
                        }
                    }
                });
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
