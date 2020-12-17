package Main;

import Exceptions.NotAllFieldsEnteredException;
import Exceptions.RowNotSelectedException;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import game.Game;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import player.Player;
import team.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;

/**
 * Class, where is implemented all work with UI elements
 */

public class UI extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel homePagePanel;
    private JTextField foundText;
    private JTextField clubNameText;
    private JTextField cityText;
    private JButton applyButton;
    private JLabel cityLabel;
    private JLabel clubNameLabel;
    private JLabel foundLabel;
    private JPanel squadPanel;
    private JButton addPlayerButton;
    private JTable table1;
    DefaultTableModel defaultTableModel;
    DefaultTableModel defaultTableModelGame;
    private JTable table2;
    private JPanel gamesPanel;
    private JButton deletePlayerButton;
    private JButton editPlayerButton;
    private JButton searchPlayersButton;
    private JButton addGameButton;
    private JButton deleteGameButton;
    private JButton editGameButton;
    private JButton searchGameButton;
    private JButton chooseClubButton;
    private JButton savePlayersButton;
    private JButton saveGamesButton;
    private JButton xmlFileButton;
    private JButton xmlGameButton;
    private static final Logger log = Logger.getLogger(UI.class);

    /**
     * Class constructor
     */
    public UI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(tabbedPane1);
        this.pack();
        this.setSize(650, 600);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        applyButton.setToolTipText("Apply this team");
        chooseClubButton.setToolTipText("Choose existing club");

        applyButton.addActionListener(x -> {
            databaseController.addClubToDatabase(clubNameText.getText(), cityText.getText()
                    , Integer.parseInt(foundText.getText()));
            databaseController.teamName = clubNameText.getText();
        });

        chooseClubButton.addActionListener(x -> {
            List<Team> teams = databaseController.getAllClubs();

            String[] options = new String[teams.size()];
            for (int i = 0; i < teams.size(); i++) {
                options[i] = teams.get(i).getName();
            }
            JComboBox optionBox = new JComboBox(options);
            Object[] message = {
                    optionBox
            };
            int option = JOptionPane.showConfirmDialog(null, message, "Choose club", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                Team team = databaseController.getClubFromDatabase(optionBox.getSelectedItem().toString());
                clubNameText.setText(team.getName());
                cityText.setText(team.getCity());
                foundText.setText(String.valueOf(team.getFoundationYear()));
                databaseController.teamName = team.getName();
            }


        });

        //Managing Squad panel
        addPlayerButton.setToolTipText("Add Player");
        addPlayerButton.addActionListener(x -> {
            try {
                addPlayerAction();
            } catch (NotAllFieldsEnteredException e) {
                log.error("Not all fields entered on adding player", e);
                JOptionPane.showMessageDialog(this, "Not all fields were entered");
            }

        });

        deletePlayerButton.setToolTipText("Delete Player");
        deletePlayerButton.addActionListener(x -> {
            try {
                deletePlayerAction();
            } catch (RowNotSelectedException e) {
                log.error("No row selected on deleting player", e);
                JOptionPane.showMessageDialog(this, "No row was selected");
            }
        });

        editPlayerButton.setToolTipText("Edit selected player");
        editPlayerButton.addActionListener(x -> {
            try {
                editPlayerAction();
            } catch (NotAllFieldsEnteredException e) {
                log.error("Not all fields entered on editing player", e);
                JOptionPane.showMessageDialog(this, "Not all fields were entered");
            } catch (RowNotSelectedException e) {
                log.error("No row selected on editing player", e);
                JOptionPane.showMessageDialog(this, "No row was selected");
            }
        });

        searchPlayersButton.setToolTipText("Search players");
        searchPlayersButton.addActionListener(x -> {
            searchForPlayersAction();
        });

        savePlayersButton.addActionListener(x -> {
            JFileChooser file = new JFileChooser();
            file.setDialogTitle("Save to file");
            file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = file.showSaveDialog(UI.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                System.out.println(file.getSelectedFile());
                try {

                    String fileName = file.getSelectedFile().toString();

                    if (fileName == null) return;
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    for (int i = 0; i < defaultTableModel.getRowCount(); i++)
                        for (int j = 0; j < defaultTableModel.getColumnCount(); j++) {
                            writer.write(defaultTableModel.getValueAt(i, j).toString() + " "); // Записать значение из ячейки
                            writer.write("\n"); // Записать символ перевода каретки
                        }
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Saving completed successfully");
                } catch (IOException e) // Ошибка записи в файл
                {
                    e.printStackTrace();
                }
            }
        });

        xmlFileButton.addActionListener(x -> {
            DocumentBuilder builder;
            Document doc = null;
            try {
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.newDocument();

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Node playerlist = doc.createElement("playerlist");

            doc.appendChild(playerlist);

// Создание дочерних элементов book и присвоение значений атрибутам


            for (int i = 0; i < defaultTableModel.getRowCount(); i++) {

                Element player = doc.createElement("player");

                playerlist.appendChild(player);
                player.setAttribute("surname", defaultTableModel.getValueAt(i, 0).toString());
                player.setAttribute("name", defaultTableModel.getValueAt(i, 1).toString());
                player.setAttribute("number", defaultTableModel.getValueAt(i, 2).toString());
                player.setAttribute("position", defaultTableModel.getValueAt(i, 3).toString());
                player.setAttribute("goals", defaultTableModel.getValueAt(i, 4).toString());
                player.setAttribute("assists", defaultTableModel.getValueAt(i, 5).toString());

            }

            try {

                Transformer trans = TransformerFactory.newInstance().newTransformer();

                java.io.FileWriter fw = new FileWriter("players.xml");
                trans.transform(new DOMSource(doc), new StreamResult(fw));

            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
             print("players.xml", "playerlist/player", "report5.jrxml", "outputfile12.pdf");
        });

        //Managing Games panel

        addGameButton.setToolTipText("Add Game");
        addGameButton.addActionListener(x -> {
            addGameAction();
        });

        editGameButton.setToolTipText("Edit Game");
        editGameButton.addActionListener(x -> {
            editGameAction();
        });

        deleteGameButton.setToolTipText("Delete Game");
        deleteGameButton.addActionListener(x -> {
            deleteGameAction();
        });

        searchGameButton.setToolTipText("Search games");
        searchGameButton.addActionListener(x -> {
            refreshGameTable();
            searchGamesAction();
        });

        saveGamesButton.addActionListener(x -> {
            JFileChooser file = new JFileChooser();
            file.setDialogTitle("Save to file");
            file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = file.showSaveDialog(UI.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                System.out.println(file.getSelectedFile());
                try {

                    String fileName = file.getSelectedFile().toString();

                    if (fileName == null) return;
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                    for (int i = 0; i < defaultTableModelGame.getRowCount(); i++)
                        for (int j = 0; j < defaultTableModelGame.getColumnCount(); j++) {
                            writer.write(defaultTableModelGame.getValueAt(i, j).toString() + " "); // Записать значение из ячейки
                            writer.write("\n"); // Записать символ перевода каретки
                        }
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Saving completed successfully");
                } catch (IOException e) // Ошибка записи в файл
                {
                    e.printStackTrace();
                }
            }
        });

        xmlGameButton.addActionListener(x -> {
            DocumentBuilder builder;
            Document doc = null;
            try {
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.newDocument();

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Node gamelist = doc.createElement("gamelist");

            doc.appendChild(gamelist);

// Создание дочерних элементов book и присвоение значений атрибутам


            for (int i = 0; i < defaultTableModelGame.getRowCount(); i++) {

                Element game = doc.createElement("game");

                gamelist.appendChild(game);
                game.setAttribute("opponent", defaultTableModelGame.getValueAt(i, 0).toString());
                game.setAttribute("date", defaultTableModelGame.getValueAt(i, 1).toString());
                game.setAttribute("yourgoals", defaultTableModelGame.getValueAt(i, 2).toString());
                game.setAttribute("oppgoals", defaultTableModelGame.getValueAt(i, 3).toString());
                game.setAttribute("isplayed", defaultTableModelGame.getValueAt(i, 4).toString());

            }

            try {

                Transformer trans = TransformerFactory.newInstance().newTransformer();

                java.io.FileWriter fw = new FileWriter("games.xml");
                trans.transform(new DOMSource(doc), new StreamResult(fw));

            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //print("players.xml", "playerlist/player", "report5.jrxml", "outputfile12.pdf");
        });
    }

    public static void print(String datasource, String xpath, String template, String resultpath) {

        try {

// Указание источника XML-данных

            JRDataSource ds = new JRXmlDataSource(datasource, xpath);

// Создание отчета на базе шаблона

            JasperReport jasperReport = JasperCompileManager.compileReport(template);

// Заполнение отчета данными

            JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap(), ds);

            JRExporter exporter = null;

            if(resultpath.toLowerCase().endsWith("pdf"))

                exporter = new JRPdfExporter(); // Генерация отчета в формате PDF


// Задание имени файла для выгрузки отчета

            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, resultpath);

// Подключение данных к отчету

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

// Выгрузка отчета в заданном формате

            exporter.exportReport();

        } catch (JRException e) { e.printStackTrace(); }

    }

    /**
     * Method to delete selected game from the database
     */
    private void deleteGameAction() {
        Object dateObj = table2.getValueAt(table2.getSelectedRow(), 1);
        Calendar date = new GregorianCalendar(Integer.parseInt(dateObj.toString().substring(0, 4)),
                Integer.parseInt(dateObj.toString().substring(5, 7)) - 1,
                Integer.parseInt(dateObj.toString().substring(8, 10)),
                Integer.parseInt(dateObj.toString().substring(11, 13)),
                Integer.parseInt(dateObj.toString().substring(14, 16)));
        databaseController.deleteGame(date);
        refreshGameTable();

    }

    /**
     * Method to define additional UI elements
     */
    private void createUIComponents() {
        Icon icon = new ImageIcon("./delete.png");
        addPlayerButton = new JButton(icon);
        addPlayerButton.setSize(30, 30);

        // TODO: place custom component creation code here
        table1 = new JTable();
        refreshPlayerTable();
        table1.setFillsViewportHeight(true);
        table2 = new JTable();
        refreshGameTable();
        table2.setFillsViewportHeight(true);

    }

    /**
     * Method to update player table in Swing
     */
    void refreshPlayerTable() {
        String[] columns = {"Surname", "Name", "Number", "Position", "Goals", "Assists"};
        Object[][] data = databaseController.showPlayers();
        defaultTableModel = new DefaultTableModel(data, columns);
        table1.setModel(defaultTableModel);
        table1.setRowHeight(48);
        table1.getColumnModel().getColumn(0).setPreferredWidth(150);
        table1.getColumnModel().getColumn(1).setPreferredWidth(60);
        table1.getColumnModel().getColumn(2).setPreferredWidth(145);
        table1.getColumnModel().getColumn(3).setPreferredWidth(60);
        table1.getColumnModel().getColumn(4).setPreferredWidth(60);
        table1.getColumnModel().getColumn(5).setPreferredWidth(60);
    }

    /**
     * Method to update game table in Swing
     */
    void refreshGameTable() {
        String[] columns = {"Opponent", "Date", "Your Goals", "Opponent Goals", "Is Played"};
        Object[][] data = databaseController.showGames();
        defaultTableModelGame = new DefaultTableModel(data, columns);
        table2.setModel(defaultTableModelGame);
        table2.setRowHeight(48);
        table2.getColumnModel().getColumn(0).setPreferredWidth(150);
        table2.getColumnModel().getColumn(1).setPreferredWidth(145);
        table2.getColumnModel().getColumn(2).setPreferredWidth(60);
        table2.getColumnModel().getColumn(3).setPreferredWidth(60);
        table2.getColumnModel().getColumn(4).setPreferredWidth(60);
    }

    /**
     * Method to add player to database
     * Is called on clicking Add button
     *
     * @throws NotAllFieldsEnteredException if not all fields were entered in "Add Player" form
     */
    void addPlayerAction() throws NotAllFieldsEnteredException {
        JTextField surname = new JTextField();
        JTextField name = new JTextField();
        JTextField number = new JTextField();
        JTextField position = new JTextField();
        JTextField goals = new JTextField();
        JTextField assists = new JTextField();
        Object[] message = {
                "Surname:", surname,
                "Name:", name,
                "Number", number,
                "Position:", position,
                "Scored goals:", goals,
                "Assists", assists

        };

        int option = JOptionPane.showConfirmDialog(null, message, "New Player", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (surname.getText().equals("") || name.getText().equals("") || position.getText().equals("")
                    || number.getText().equals("") || goals.getText().equals("")
                    || assists.getText().equals("")) {
                throw new NotAllFieldsEnteredException();
            }
            System.out.println(name.getText());
            Player pl = databaseController.addPlayer(surname.getText(), name.getText(),
                    Integer.parseInt(number.getText()), position.getText(),
                    Integer.parseInt(goals.getText()), Integer.parseInt(assists.getText()));
            System.out.println(databaseController.teamName);
            refreshPlayerTable();
        } else {
            System.out.println("Login canceled");
        }

        //JOptionPane.showInputDialog(new dialogAddPlayer());
    }

    /**
     * Method to edit player in database
     * Is called on clicking Edit button
     *
     * @throws NotAllFieldsEnteredException if not all fields were entered in "Edit Player" form
     * @throws RowNotSelectedException      if no row of the table was selected
     */
    void editPlayerAction() throws NotAllFieldsEnteredException, RowNotSelectedException {
        if (table1.getSelectedRow() == -1)
            throw new RowNotSelectedException();
        JTextField surname = new JTextField();
        JTextField name = new JTextField();
        JTextField number = new JTextField();
        JTextField position = new JTextField();
        JTextField goals = new JTextField();
        JTextField assists = new JTextField();
        Object[] message = {
                "Surname:", surname,
                "Name:", name,
                "Number", number,
                "Position:", position,
                "Scored goals:", goals,
                "Assists", assists

        };
        Object[] data = new Object[6];
        for (int i = 0; i < 6; i++) {
            data[i] = table1.getValueAt(table1.getSelectedRow(), i);
        }
        surname.setText(data[0].toString());
        name.setText(data[1].toString());
        number.setText(data[2].toString());
        position.setText(data[3].toString());
        goals.setText(data[4].toString());
        assists.setText(data[5].toString());
        Player player = databaseController.findByNumber(Integer.parseInt(number.getText()));
        int option = JOptionPane.showConfirmDialog(null, message, "Edit Player", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (surname.getText().equals("") || name.getText().equals("") || position.getText().equals("")
                    || number.getText().equals("") || goals.getText().equals("")
                    || assists.getText().equals("")) {
                throw new NotAllFieldsEnteredException();
            }
            player.setTeam(databaseController.getClubFromDatabase(databaseController.teamName));
            player.setFirstName(name.getText());
            player.setNumber(Integer.parseInt(number.getText()));
            player.setSurname(surname.getText());
            player.setPosition(position.getText());
            player.setGoals(Integer.parseInt(goals.getText()));
            player.setAssists(Integer.parseInt(assists.getText()));
            databaseController.updatePlayer(player);
            refreshPlayerTable();
        } else {
            System.out.println("Login canceled");
        }
    }

    /**
     * Method to delete player from database
     * Is called on clicking Delete button
     *
     * @throws RowNotSelectedException if no row of the table was selected
     */
    void deletePlayerAction() throws RowNotSelectedException {
        if (table1.getSelectedRow() == -1)
            throw new RowNotSelectedException();
        System.out.println(table1.getSelectedRow());
        databaseController.deletePlayer(Integer.parseInt(
                table1.getValueAt(table1.getSelectedRow(), 2).toString()));
        refreshPlayerTable();
    }

    /**
     * Method to search for players in database by different params
     * Is called on clicking Search button
     */
    void searchForPlayersAction() {
        JTextField field = new JTextField();
        String[] options = {"Surname", "Name", "Position", "Number", "Goals", "Assists"};
        JComboBox optionBox = new JComboBox(options);
        Object[] message = {
                optionBox, field
        };

        int option = JOptionPane.showConfirmDialog(null, message, "New Player", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            switch (optionBox.getSelectedIndex()) {
                case 0:
                    showSearchResult("Surname", field.getText());
                    break;
                case 1:
                    showSearchResult("Name", field.getText());
                    break;
                case 2:
                    showSearchResult("Position", field.getText());
                    break;
                case 3:
                    showSearchResult("Number", field.getText());
                    break;
                case 4:
                    showSearchResult("Goals", field.getText());
                    break;
                case 5:
                    showSearchResult("Assists", field.getText());
                    break;
            }
        } else {
            System.out.println("Login canceled");
        }
    }

    /**
     * Method to show search results for players
     *
     * @param field name of the field to search by
     * @param input value to be found
     */
    void showSearchResult(String field, String input) {
        List<Player> result = new ArrayList<Player>();
        switch (field) {
            case "Surname":
                result = databaseController.findPlayersBySurname(input);
                break;
            case "Name":
                result = databaseController.findPlayersByName(input);
                break;
            case "Position":
                result = databaseController.findPlayersByPosition(input);
                break;
            case "Number":
                result.add(databaseController.findByNumber(Integer.parseInt(input)));
                break;
            case "Goals":
                result = databaseController.findPlayersByGoals(Integer.parseInt(input));
                break;
            case "Assists":
                result = databaseController.findPlayersByAssists(Integer.parseInt(input));
                break;
        }
        defaultTableModel.setRowCount(0);

        result.forEach(x -> {
            Object[] inp = {x.getSurname(), x.getFirstName(), x.getNumber(), x.getPosition(), x.getGoals(), x.getAssists()};
            defaultTableModel.addRow(inp);

        });
        table1.setModel(defaultTableModel);
        table1.setRowHeight(48);
        table1.getColumnModel().getColumn(0).setPreferredWidth(150);
        table1.getColumnModel().getColumn(1).setPreferredWidth(60);
        table1.getColumnModel().getColumn(2).setPreferredWidth(145);
        table1.getColumnModel().getColumn(3).setPreferredWidth(60);
        table1.getColumnModel().getColumn(4).setPreferredWidth(60);
        table1.getColumnModel().getColumn(5).setPreferredWidth(60);
    }

    void addGameAction() {
        DateTimePicker picker = new DateTimePicker();
        JTextField opponent = new JTextField();
        JTextField yourGoals = new JTextField();
        JTextField oppGoals = new JTextField();
        Object[] message = {
                "Opponent:", opponent,
                "Date and Time:", picker,
                "Your Goals", yourGoals,
                "Opponent Goals:", oppGoals,

        };

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        int option = JOptionPane.showConfirmDialog(null, message, "New Game", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (opponent.getText().equals("") || yourGoals.getText().equals("") || oppGoals.getText().equals("")
                    || picker.datePicker.getDateStringOrEmptyString().equals("")
                    || picker.timePicker.getTimeStringOrEmptyString().equals("")) {
                // throw new NotAllFieldsEnteredException();
            }
            //System.out.println(name.getText());
            Calendar date = new GregorianCalendar(Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(0, 4)),
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(5, 7)) - 1,
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(8)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(0, 2)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(3)));

            Game game = databaseController.addGame(opponent.getText(), date,
                    Integer.parseInt(yourGoals.getText()), Integer.parseInt(oppGoals.getText()));
            System.out.println(databaseController.teamName);
            refreshGameTable();
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(0, 4));
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(5, 7));
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(8));
            System.out.println(picker.timePicker.getTimeStringOrEmptyString());
        }
    }

    void editGameAction() {
        DateTimePicker picker = new DateTimePicker();
        JTextField opponent = new JTextField();
        JTextField yourGoals = new JTextField();
        JTextField oppGoals = new JTextField();
        Object[] message = {
                "Opponent:", opponent,
                "Date and Time:", picker,
                "Your Goals", yourGoals,
                "Opponent Goals:", oppGoals,

        };
        Object[] data = new Object[4];
        for (int i = 0; i < 4; i++) {
            data[i] = table2.getValueAt(table2.getSelectedRow(), i);
        }
        picker.datePicker.setDate(LocalDate.of(Integer.parseInt(data[1].toString().substring(0, 4)),
                Integer.parseInt(data[1].toString().substring(5, 7)),
                Integer.parseInt(data[1].toString().substring(8, 10))));
        picker.timePicker.setTime(LocalTime.of(Integer.parseInt(data[1].toString().substring(11, 13)),
                Integer.parseInt(data[1].toString().substring(14, 16))));
        opponent.setText(data[0].toString());
        yourGoals.setText(data[2].toString());
        oppGoals.setText(data[3].toString());
        Calendar date = new GregorianCalendar(Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(0, 4)),
                Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(5, 7)) - 1,
                Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(8)),
                Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(0, 2)),
                Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(3)));

        Game game = databaseController.findByDate(date);
        int option = JOptionPane.showConfirmDialog(null, message, "Edit Game", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (opponent.getText().equals("") || yourGoals.getText().equals("") || oppGoals.getText().equals("")
                    || picker.datePicker.getDateStringOrEmptyString().equals("")
                    || picker.timePicker.getTimeStringOrEmptyString().equals("")) {
                // throw new NotAllFieldsEnteredException();
            }
            //System.out.println(name.getText());
            Calendar newDate = new GregorianCalendar(Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(0, 4)),
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(5, 7)) - 1,
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(8)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(0, 2)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(3)));
            game.setDateTime(newDate);
            game.setOpponent(opponent.getText());
            game.setYourGoals(Integer.parseInt(yourGoals.getText()));
            game.setOpponentGoals(Integer.parseInt(oppGoals.getText()));
            databaseController.updateGame(game);
            refreshGameTable();
        } else {
            System.out.println("Login canceled");
        }
    }

    /**
     * Method to search for games in database by different params
     * Is called on clicking Search button
     */
    void searchGamesAction() {
        JTextField field = new JTextField();
        String[] options = {"Date", "Opponent", "Your Goals", "Opponent Goals", "Played Games"};
        JComboBox optionBox = new JComboBox(options);
        Object[] message = {
                optionBox, field
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Search Games", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            switch (optionBox.getSelectedIndex()) {
                case 0:
                    gamesShowResult("Date", field.getText());
                    break;
                case 1:
                    gamesShowResult("Opponent", field.getText());
                    break;
                case 2:
                    gamesShowResult("Your Goals", field.getText());
                    break;
                case 3:
                    gamesShowResult("Opponent Goals", field.getText());
                    break;
                case 4:
                    gamesShowResult("Played Games", field.getText());
                    break;
            }
        }
    }

    /**
     * Method to show search results for games
     *
     * @param field name of the field to search by
     * @param input value to be found
     */
    void gamesShowResult(String field, String input) {
        List<Game> result = new ArrayList<Game>();
        switch (field) {
            case "Opponent":
                result = databaseController.findByOpponent(input);
                break;
            case "Date":
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = format.parse(input);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                result.add(databaseController.findByDate(cal));
                break;
            case "Your Goals":
                result = databaseController.findByYourGoals(Integer.parseInt(input));
                break;
            case "Opponent Goals":
                result = databaseController.findByOppGoals(Integer.parseInt(input));
                break;
            case "Played Games":
                result = databaseController.findPlayedGames(Boolean.parseBoolean(input));
                break;
        }
        defaultTableModelGame.setRowCount(0);

        result.forEach(x -> {
            SimpleDateFormat formmat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatted = formmat1.format(x.getDateTime().getTime());
            Object[] inp = {x.getOpponent(), formatted, x.getYourGoals(), x.getOpponentGoals(), x.isPlayed()};
            defaultTableModelGame.addRow(inp);

        });
        table2.setModel(defaultTableModelGame);
        table2.setRowHeight(48);
        table2.getColumnModel().getColumn(0).setPreferredWidth(150);
        table2.getColumnModel().getColumn(1).setPreferredWidth(145);
        table2.getColumnModel().getColumn(2).setPreferredWidth(60);
        table2.getColumnModel().getColumn(3).setPreferredWidth(60);
        table2.getColumnModel().getColumn(4).setPreferredWidth(60);
    }
}
