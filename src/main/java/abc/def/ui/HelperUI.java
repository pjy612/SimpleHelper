package abc.def.ui;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NeowsLament;

import javax.swing.*;
import java.util.Iterator;

public class HelperUI {
    private JButton cardRewardButton;
    private JButton relicPoolButton;
    public JPanel rootPanel;
    private JButton eliteRelicTierButton;
    private JButton killAllButton;
    private JButton cardManagerbutton;

    private CardRewardDialog cardRewardDialog = null;
    private CardManagerDialog cardManagerDialog = null;
    private RelicPoolDialog relicPoolDialog = null;
    private EliteRelicTier eliteRelicTierDialog = null;

    public HelperUI() {
        super();
        cardRewardDialog = new CardRewardDialog();
        cardRewardDialog.pack();
        cardManagerDialog = new CardManagerDialog();
        cardManagerDialog.pack();
        relicPoolDialog = new RelicPoolDialog();
        relicPoolDialog.pack();
        eliteRelicTierDialog = new EliteRelicTier();
        eliteRelicTierDialog.pack();

        cardRewardButton.addActionListener(e -> {
            cardRewardDialog.setVisible(!cardRewardDialog.isVisible());
        });

        cardManagerbutton.addActionListener(e -> {
            cardManagerDialog.setVisible(!cardManagerDialog.isVisible());
        });

        relicPoolButton.addActionListener(e -> {
            relicPoolDialog.setVisible(!relicPoolDialog.isVisible());
        });

        eliteRelicTierButton.addActionListener(e -> {
            eliteRelicTierDialog.setVisible(!eliteRelicTierDialog.isVisible());
        });

        killAllButton.addActionListener(e -> {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                m.currentHealth = 1;
                m.healthBarUpdatedEvent();
            }
        });
    }

    public void switchDisplay() {
        rootPanel.setVisible(!rootPanel.isVisible());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HelperUI");
        frame.setContentPane(new HelperUI().rootPanel);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
