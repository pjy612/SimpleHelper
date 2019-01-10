package abc.def.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ColorTestDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JTextField titleColorTextField;
    private JTextField contentColorTextField;
    private JButton refreshButton;
    private JTextField fontTextField;
    private ArrayList<String> listData = new ArrayList<>(2);

    public ColorTestDialog() {
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        refreshButton.addActionListener(e -> {
            String titleColor = titleColorTextField.getText();
            String contentColor = contentColorTextField.getText();
            String fontSize = fontTextField.getText();

            if (!titleColor.isEmpty() && !contentColor.isEmpty()) {
                listData.clear();
                listData.add(htmlString(1, titleColor, contentColor));
                listData.add(htmlString(2, titleColor, contentColor));
                list1.setListData(listData.toArray());
                Font font = new Font(list1.getFont().getFontName(), Font.PLAIN, Integer.valueOf(fontSize));
                list1.setFont(font);
                pack();
            }
        });
    }

    private String htmlString(int id, String title, String content) {
        return String.format("<html><span style='color:#%s'>标题%d<span/><br/><span style='color:#%s'>这里是内容%d<span/><html/>", title, id, content, id);
    }

    public static void main(String[] args) {
        ColorTestDialog dialog = new ColorTestDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
