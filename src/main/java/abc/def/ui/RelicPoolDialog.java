package abc.def.ui;

import abc.def.SimpleHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;

public class RelicPoolDialog extends JDialog {
    final static private String TITLE_COLOR = "339933";
    final static private String CONTENT_COLOR = "000000";
    private JPanel contentPane;
    private JRadioButton commonRadioButton;
    private JButton topButton;
    private JButton bottomButton;
    private JRadioButton uncommonRadioButton;
    private JRadioButton rareRadioButton;
    private JRadioButton shopRadioButton;
    private JRadioButton bossRadioButton;
    private JList relicList;
    private JButton selectButton;
    private ArrayList<String> relicPool = null;

    public RelicPoolDialog() {
        setTitle("Relic Pool");
        setContentPane(contentPane);
        setModal(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        ItemListener radioButtonItemListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SimpleHelper.resetRelicPool();
                JRadioButton radioButton = (JRadioButton) e.getItem();
                switch (radioButton.getText()) {
                    case "common":
                        relicPool = AbstractDungeon.commonRelicPool;
                        break;
                    case "uncommon":
                        relicPool = AbstractDungeon.uncommonRelicPool;
                        break;
                    case "rare":
                        relicPool = AbstractDungeon.rareRelicPool;
                        break;
                    case "shop":
                        relicPool = AbstractDungeon.shopRelicPool;
                        break;
                    case "boss":
                        relicPool = AbstractDungeon.bossRelicPool;
                        break;
                    default:
                        relicPool = null;
                }
                refreshList();
            }
        };
        commonRadioButton.addItemListener(radioButtonItemListener);
        uncommonRadioButton.addItemListener(radioButtonItemListener);
        rareRadioButton.addItemListener(radioButtonItemListener);
        shopRadioButton.addItemListener(radioButtonItemListener);
        bossRadioButton.addItemListener(radioButtonItemListener);

        topButton.addActionListener(e -> {
            int index = relicList.getSelectedIndex();
            if (relicPool != null && index > 0) {
                //Collections.swap(relicPool, index, 0);
                String r = relicPool.remove(index);
                relicPool.add(0, r);
                refreshList();
            }
        });

        bottomButton.addActionListener(e -> {
            int index = relicList.getSelectedIndex();
            if (relicPool != null && index >= 0) {
                //Collections.swap(relicPool, index, relicPool.size() - 1);
                String r = relicPool.remove(index);
                relicPool.add(r);
                refreshList();
            }
        });
        selectButton.addActionListener(e -> {
            int index = relicList.getSelectedIndex();
            if (relicPool != null && index >= 0) {
                SimpleHelper.selectRelic = relicPool.get(index);
                if (!AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                    SimpleHelper.adjustRewardForSelect(AbstractDungeon.combatRewardScreen.rewards);
                    AbstractDungeon.combatRewardScreen.positionRewards();
                }
            }
        });
    }

    private String relicToHtml(AbstractRelic relic) {
        return String.format("<html><span style='color:#%s'>%s<span/><br/><span style='color:#%s'>%s<span/><html/>", TITLE_COLOR, relic.name, CONTENT_COLOR, relic.description);
    }

    private void refreshList() {
        if (relicPool != null) {
            ArrayList<String> htmls = new ArrayList<>(relicPool.size());
            for (String id : relicPool) {
                htmls.add(relicToHtml(RelicLibrary.getRelic(id)));
            }
            relicList.setListData(htmls.toArray());
        } else {
            relicList.setListData(new String[]{""});
        }
        pack();
    }

    public static void main(String[] args) {
        RelicPoolDialog dialog = new RelicPoolDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
