import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class TextEditor implements DocumentListener {
    public static void main(String[] args) {
        new TextEditor();
    }
    public TextEditor(){
        SwingUtilities.invokeLater(this::createGUI);
    }

    private State state = State.NEW;

    private JLabel bgLabel;
    private JLabel fgLabel;
    private JLabel statusLabel;
    private JLabel sizeLabel;

    protected void createGUI(){
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //USEFUL VARIABLES
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        String mainTitle = "Text Editor - ";
        String tempTitle = "no title";

        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //J_FRAME OPERATIONS
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setTitle(mainTitle + tempTitle);
        frame.setLocationRelativeTo(null);
        frame.setSize(420,420);

        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //J_TEXT_AREA_OPERATIONS
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        JPanel centralPanel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("example",Font.PLAIN,15));
        textArea.getDocument().addDocumentListener(this);
        centralPanel.add(textArea);

        JScrollPane textAreaScroll = new JScrollPane(textArea);
        centralPanel.add(textAreaScroll);

        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //BOTTOM PANEL OPERATIONS
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel colorAndSizePanel = new JPanel();

        statusLabel = new JLabel();
        sizeLabel = new JLabel(" Size");
        bgLabel = new JLabel(" BG");
        fgLabel = new JLabel(" FG");

        colorAndSizePanel.add(fgLabel,BorderLayout.EAST);
        colorAndSizePanel.add(bgLabel,BorderLayout.CENTER);
        colorAndSizePanel.add(sizeLabel,BorderLayout.WEST);

        enumSwitcher(statusLabel);

        bottomPanel.add(statusLabel,BorderLayout.EAST);
        bottomPanel.add(colorAndSizePanel,BorderLayout.WEST);

        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //J_MENU_BAR OPERATIONS
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        JMenuBar menu = new JMenuBar();
        JSeparator saveAsAndExitSeparator = new JSeparator();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));

        JMenu file = new JMenu("File");
        menu.add(file);
        //--------------------------------------------------------------------------------------------------------------
        //open

        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        open.setMnemonic(KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke("control O"));

        open.addActionListener(e -> {
            if (open.getText().equals("Open") && fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    FileReader fileToOpen = new FileReader(fileChooser.getSelectedFile().getAbsoluteFile());
                    BufferedReader buffer = new BufferedReader(fileToOpen);
                    textArea.read(buffer,null);
                    frame.setTitle(mainTitle + fileChooser.getSelectedFile().getAbsolutePath());
                    textArea.getDocument().addDocumentListener(this);
                    buffer.close();
                    textArea.requestFocus();
                    state = State.MODIFIED;
                    enumSwitcher(statusLabel);
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        //save

        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke("control S"));

        save.addActionListener(e -> {
            if ((state == State.NEW) || ((state == State.MODIFIED) && (frame.getTitle().equals(mainTitle + tempTitle)))){
                if (save.getText().equals("Save") && fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    frame.setTitle(mainTitle + fileChooser.getSelectedFile().getAbsolutePath());
                    saveMethod(fileChooser, textArea);
                    state = State.SAVED;
                    enumSwitcher(statusLabel);
                }
            }else {
                saveMethod(fileChooser,textArea);
                state = State.SAVED;
                enumSwitcher(statusLabel);
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        //save_as

        JMenuItem save_as = new JMenuItem("Save as");
        file.add(save_as);
        save_as.setMnemonic(KeyEvent.VK_A);
        save_as.setAccelerator(KeyStroke.getKeyStroke("control D"));

        save_as.addActionListener(e -> {
                    if (save_as.getText().equals("Save as") && fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        saveMethod(fileChooser,textArea);
                    }
                    state = State.SAVED;
                    enumSwitcher(statusLabel);
                });

        //--------------------------------------------------------------------------------------------------------------
        //SEPARATOR IMPLEMENTATION

        saveAsAndExitSeparator.setForeground(Color.RED);
        file.add(saveAsAndExitSeparator);

        //--------------------------------------------------------------------------------------------------------------
        //exit

        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.setMnemonic(KeyEvent.VK_E);
        exit.addActionListener(e -> System.exit(0));
        exit.setAccelerator(KeyStroke.getKeyStroke("control E"));

        //--------------------------------------------------------------------------------------------------------------
        //edit

        JMenu edit = new JMenu("Edit");
        menu.add(edit);

        JMenu addresses = new JMenu("Addresses");

        //--------------------------------------------------------------------------------------------------------------
        //home

        JMenuItem home = new JMenuItem("Home");
        addresses.add(home);
        home.setMnemonic(KeyEvent.VK_H);
        home.setAccelerator(KeyStroke.getKeyStroke("control shift H"));
        home.addActionListener(e -> textArea.insert("Warsaw Aleje Jerozolimskie 150a",textArea.getCaretPosition()));

        //--------------------------------------------------------------------------------------------------------------
        //work

        JMenuItem work = new JMenuItem("Work");
        addresses.add(work);
        work.setMnemonic(KeyEvent.VK_W);
        work.setAccelerator(KeyStroke.getKeyStroke("control shift W"));
        work.addActionListener(e -> textArea.insert("London Baker Street 35",textArea.getCaretPosition()));

        //--------------------------------------------------------------------------------------------------------------
        //school

        JMenuItem school = new JMenuItem("School");
        addresses.add(school);
        school.setMnemonic(KeyEvent.VK_S);
        school.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        school.addActionListener(e -> textArea.insert("Warsaw Koszykowa 86",textArea.getCaretPosition()));

        edit.add(addresses);

        //--------------------------------------------------------------------------------------------------------------
        //options

        JMenu options = new JMenu("Options");
        menu.add(options);

        //--------------------------------------------------------------------------------------------------------------
        //foreground

        ButtonGroup foreGroup = new ButtonGroup();
        JMenu foreground = new JMenu("Foreground");

        options.add(foreground);
        foreground.setMnemonic(KeyEvent.VK_F);
        colorChangerFore(textArea,foreGroup,foreground,"Red",Color.RED);
        colorChangerFore(textArea,foreGroup,foreground,"Blue",Color.BLUE);
        colorChangerFore(textArea,foreGroup,foreground,"Yellow",Color.YELLOW);
        colorChangerFore(textArea,foreGroup,foreground,"Black",Color.BLACK);
        colorChangerFore(textArea,foreGroup,foreground,"Green",Color.GREEN);
        colorChangerFore(textArea,foreGroup,foreground,"Cyan",Color.CYAN);
        colorChangerFore(textArea,foreGroup,foreground,"Magenta",Color.MAGENTA);
        colorChangerFore(textArea,foreGroup,foreground,"Pink",Color.PINK);

        //--------------------------------------------------------------------------------------------------------------
        //background

        ButtonGroup backGroup = new ButtonGroup();
        JMenu background = new JMenu("Background");

        options.add(background);
        background.setMnemonic(KeyEvent.VK_B);

        colorChangerBack(textArea,backGroup,background,"Red",Color.RED);
        colorChangerBack(textArea,backGroup,background,"Blue",Color.BLUE);
        colorChangerBack(textArea,backGroup,background,"Yellow",Color.YELLOW);
        colorChangerBack(textArea,backGroup,background,"Black",Color.BLACK);
        colorChangerBack(textArea,backGroup,background,"Green",Color.GREEN);
        colorChangerBack(textArea,backGroup,background,"Cyan",Color.CYAN);
        colorChangerBack(textArea,backGroup,background,"Magenta",Color.MAGENTA);
        colorChangerBack(textArea,backGroup,background,"Pink",Color.PINK);
        //--------------------------------------------------------------------------------------------------------------
        //font_size

        JMenu font_size = new JMenu("Font size");
        options.add(font_size);
        font_size.setMnemonic(KeyEvent.VK_S);

        for (int i = 6; i <= 24; i+=2) {
            JMenuItem item = new JMenuItem("" + i + " pts");
            item.setFont(new Font("",Font.PLAIN,i));
            item.addActionListener(e -> {
                textArea.setFont(item.getFont());
                sizeLabel.setText(item.getText());
            });
            font_size.add(item);
        }
        frame.setJMenuBar(menu);

        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------
        //END OPERATIONS
        //--------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(centralPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel,BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    //--------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------
    //METHODS
    //--------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------

    private void saveMethod (JFileChooser chooser,JTextArea area){
        try {
            FileWriter fileToWrite = new FileWriter(chooser.getSelectedFile().getAbsoluteFile());
            BufferedWriter buffer = new BufferedWriter(fileToWrite);
            area.write(buffer);
            buffer.close();
            area.requestFocus();
        }catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
    private void colorChangerFore (JTextArea area,ButtonGroup group,JMenu menu,String name, Color color){

        JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);

        item.setForeground(color);

        item.addActionListener(e -> {
            fgLabel.setIcon(new CircleIcon(area.getForeground()));
            area.setForeground(color);
        });
        item.setIcon(new CircleIcon(color));
        group.add(item);
        menu.add(item);
    }
    private void colorChangerBack (JTextArea area,ButtonGroup group,JMenu menu,String name, Color color){

        JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);

        item.setForeground(color);

        item.addActionListener(e -> {
            bgLabel.setIcon(new CircleIcon(area.getBackground()));
            area.setBackground(color);
        });
        item.setIcon(new CircleIcon(color));
        group.add(item);
        menu.add(item);
    }
    private void enumSwitcher(JLabel label){
        switch (state){
            case NEW -> label.setText("New ");
            case SAVED -> label.setText("Saved ");
            case MODIFIED -> label.setText("Modified ");
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    //INTERFACE DOCUMENT LISTENER METHODS
    @Override
    public void insertUpdate(DocumentEvent e) {
        state = State.MODIFIED;
        enumSwitcher(statusLabel);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        state = State.MODIFIED;
        enumSwitcher(statusLabel);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        state = State.MODIFIED;
        enumSwitcher(statusLabel);
    }
}
enum State{
    NEW,
    MODIFIED,
    SAVED
}
class CircleIcon implements Icon {

    Color color;

    public CircleIcon(Color color){
        this.color = color;
    }
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillOval(x,y,getIconWidth(),getIconHeight());
    }

    @Override
    public int getIconWidth() {
        return 10;
    }

    @Override
    public int getIconHeight() {
        return 10;
    }
}
