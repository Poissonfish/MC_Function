import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/** @see http://stackoverflow.com/a/13919878/230513 */
public class CheckListExample {

    private static final CheckModel model = new CheckModel(5000);
    private static final JTable table = new JTable(model) {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(150, 300);
        }

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            JCheckBox jcb = (JCheckBox) super.prepareRenderer(renderer, row, column);
            jcb.setHorizontalTextPosition(JCheckBox.LEADING);
            jcb.setText(String.valueOf(row));
            return jcb;
        }
    };

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new JFrame("CheckTable");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setLayout(new GridLayout(1, 0));
                f.add(new JScrollPane(table));  //Right panel
                f.add(new DisplayPanel(model)); //Left panel
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    private static class DisplayPanel extends JPanel {

        private DefaultListModel dlm = new DefaultListModel();
        private JList list = new JList(dlm);

        public DisplayPanel(final CheckModel model) {
            super(new GridLayout());
            this.setBorder(BorderFactory.createTitledBorder("Checked"));
            this.add(new JScrollPane(list));
            model.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    dlm.removeAllElements();
                    for (Integer integer : model.checked) {
                        dlm.addElement(integer);
                    }
                }
            });
        }
    }

    private static class CheckModel extends AbstractTableModel {

        private final int rows;
        private List<Boolean> rowList;
        private Set<Integer> checked = new TreeSet<Integer>();

        public CheckModel(int rows) {
            this.rows = rows;
            rowList = new ArrayList<Boolean>(rows);
            for (int i = 0; i < rows; i++) {
                rowList.add(Boolean.FALSE);
            }
        }

        @Override
        public int getRowCount() { //editable
            return rows;
        }

        @Override
        public int getColumnCount() { //editable
            return 1;
        }

        @Override
        public String getColumnName(int col) {
            return "Column " + col;
        }

        @Override
        public Object getValueAt(int row, int col) { //editable
                return rowList.get(row);
        }

        @Override
        public void setValueAt(Object aValue, int row, int col) {
            boolean b = (Boolean) aValue;
            rowList.set(row, b);
            if (b) {
                checked.add(row);
            } else {
                checked.remove(row);
            }
            fireTableRowsUpdated(row, row);
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return getValueAt(0, col).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }
    }
}