package abc.def.ui;

import abc.def.SimpleHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class EliteRelicTier extends JDialog {
    private JPanel contentPane;
    private JButton changeButton;
    private JRadioButton commonRadioButton;
    private JRadioButton uncommonRadioButton;
    private JRadioButton rareRadioButton;
    private AbstractRelic.RelicTier selectedTier = AbstractRelic.RelicTier.COMMON;

    public EliteRelicTier() {
        setContentPane(contentPane);
        setTitle("Elite Relic Tier");
        setModal(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        ItemListener itemListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getItem();
                switch (radioButton.getText()) {
                    case "common":
                        selectedTier = AbstractRelic.RelicTier.COMMON;
                        break;
                    case "uncommon":
                        selectedTier = AbstractRelic.RelicTier.UNCOMMON;
                        break;
                    case "rare":
                        selectedTier = AbstractRelic.RelicTier.RARE;
                        break;
                }
            }
        };
        commonRadioButton.addItemListener(itemListener);
        uncommonRadioButton.addItemListener(itemListener);
        rareRadioButton.addItemListener(itemListener);

        changeButton.addActionListener(e -> {
            SimpleHelper.nextEliteRelicTier = selectedTier;
        });
    }
}
