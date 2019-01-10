package abc.def.ui;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalInt;
import java.util.concurrent.CopyOnWriteArrayList;

public class CardManagerDialog extends JDialog {
    final static private String CONTENT_COLOR = "000000";
    private JPanel contentPane;
    private JButton refreshButton;
    private JButton removeButton;
    private JList cardList;

    public CardManagerDialog() {
        setTitle("Card Manager");
        setContentPane(contentPane);
        setModal(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        refreshButton.addActionListener(e -> {
            refreshList();
        });

        removeButton.addActionListener(e -> {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW && AbstractDungeon.isScreenUp)
                return;
            int[] indexs = cardList.getSelectedIndices();
            if (indexs.length > 0) {
                ArrayList<AbstractCard> tmplist = new ArrayList<>();
                OptionalInt max = Arrays.stream(indexs).max();
                int idx = max.getAsInt();
                for (int index :
                        indexs) {
                    if (index >= 0 && index < AbstractDungeon.player.masterDeck.group.size()) {
                        AbstractCard c = AbstractDungeon.player.masterDeck.group.get(index);
                        if (c != null) {
                            tmplist.add(c);
                        }
                    }
                }
                if (tmplist.size() > 0) {
                    for (AbstractCard c : tmplist) {
                        AbstractDungeon.player.masterDeck.group.remove(c);
                    }
                }

                refreshList();
                if (idx > 0) {
                    if (idx < AbstractDungeon.player.masterDeck.group.size()) {
                        cardList.setSelectedIndex(idx - 1);
                    } else {
                        cardList.setSelectedIndex(AbstractDungeon.player.masterDeck.group.size() - 1);
                    }
                }
            }

        });
    }

    private void refreshList() {
        if (AbstractDungeon.player == null) {
            cardList.setListData(new String[]{""});
            return;
        }
        ArrayList<AbstractCard> cardArrayList = AbstractDungeon.player.masterDeck.group;
        if (cardArrayList != null) {
            ArrayList<String> htmls = new ArrayList<>();
            for (int i = 0; i < cardArrayList.size(); i++) {
                AbstractCard card = cardArrayList.get(i);
                switch (card.rarity) {
                    case BASIC:
                        htmls.add(cardToHtml(i, card, "000000"));
                        break;
                    case SPECIAL:
                        htmls.add(cardToHtml(i, card, "8B008B"));
                        break;
                    case COMMON:
                        htmls.add(cardToHtml(i, card, "999999"));
                        break;
                    case UNCOMMON:
                        htmls.add(cardToHtml(i, card, "0000CC"));
                        break;
                    case RARE:
                        htmls.add(cardToHtml(i, card, "FF9900"));
                        break;
                    case CURSE:
                        htmls.add(cardToHtml(i, card, "A52A2A"));
                        break;
                    default:
                        htmls.add(cardToHtml(i, card, "999999"));
                        break;
                }
            }
            cardList.setListData(htmls.toArray());
        } else {
            cardList.setListData(new String[]{""});
        }
        pack();
    }

    private String cardToHtml(int index, AbstractCard card, String titleColor) {
        String template = String.format("<html><span style='color:#%s'>%d：%s %s</span><br/><span style='color:#%s'>%s</span></html>",
                titleColor, index, card.name, card.timesUpgraded > 0 ? String.valueOf(card.timesUpgraded) : "", CONTENT_COLOR, card.rawDescription);
        return template;
    }

    public static void main(String[] args) {
        CardManagerDialog dialog = new CardManagerDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
