package Main;

import Exceptions.NotAllFieldsEnteredException;
import Exceptions.RowNotSelectedException;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import game.Game;
import player.Player;
import team.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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

    public UI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(tabbedPane1);
        this.pack();
        this.setSize(650, 600);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
//        if (databaseController.getClubFromDatabase("FC Barcelona") != null) {
//            Team team = databaseController.getClubFromDatabase("FC Barcelona");
//            clubNameText.setText(team.getName());
//            cityText.setText(team.getCity());
//            foundText.setText(String.valueOf(team.getFoundationYear()));
//            clubNameText.setEditable(false);
//            cityText.setEditable(false);
//            foundText.setEditable(false);
//            applyButton.setEnabled(false);
//        }
        applyButton.addActionListener( x -> {
            databaseController.addClubToDatabase(clubNameText.getText(),cityText.getText()
                    ,Integer.parseInt(foundText.getText()));
            databaseController.teamName = clubNameText.getText();
//            clubNameText.setEditable(false);
//            cityText.setEditable(false);
//            foundText.setEditable(false);
//            applyButton.setEnabled(false);
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
        addPlayerButton.addActionListener( x -> {
            try {
                addPlayerAction();
            } catch (NotAllFieldsEnteredException e) {
                JOptionPane.showMessageDialog(this, "Not all fields were entered");
            }

        });

        deletePlayerButton.addActionListener(x -> {
            try {
                deletePlayerAction();
            } catch (RowNotSelectedException e) {
                JOptionPane.showMessageDialog(this, "No row was selected");
            }
        });

        editPlayerButton.addActionListener(x -> {
            try {
                editPlayerAction();
            } catch (NotAllFieldsEnteredException e) {
                JOptionPane.showMessageDialog(this, "Not all fields were entered");
            } catch (RowNotSelectedException e) {
                JOptionPane.showMessageDialog(this, "No row was selected");
            }
        });

        searchPlayersButton.addActionListener(x -> {
            searchForPlayersAction();
        });


        //Managing Games panel

        addGameButton.addActionListener(x -> {
            addGameAction();
        });

        editGameButton.addActionListener(x -> {
            editGameAction();
        });

        deleteGameButton.addActionListener(x -> {
            deleteGameAction();
        });

        searchGameButton.addActionListener(x -> {
            refreshGameTable();
            searchGamesAction();
        });

    }

    private void deleteGameAction() {
        Object dateObj = table2.getValueAt(table2.getSelectedRow(), 1);
        Calendar date = new GregorianCalendar(Integer.parseInt(dateObj.toString().substring(0,4)),
                Integer.parseInt(dateObj.toString().substring(5,7)) -1,
                Integer.parseInt(dateObj.toString().substring(8, 10)),
                Integer.parseInt(dateObj.toString().substring(11,13)),
                Integer.parseInt(dateObj.toString().substring(14, 16)));
        databaseController.deleteGame(date);
        refreshGameTable();

    }

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

    void editPlayerAction() throws NotAllFieldsEnteredException, RowNotSelectedException {
        if (table1.getSelectedRow() == -1)
            throw new RowNotSelectedException();
        JTextField surname = new JTextField();
        JTextField name = new JTextField();
        JTextField number = new JTextField();
        JTextField position = new JTextField();
        JTextField goals = new JTextField();
        JTextField assists = new JTextField();
        number.setEditable(false);
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
        int option = JOptionPane.showConfirmDialog(null, message, "Edit Player", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (surname.getText().equals("") || name.getText().equals("") || position.getText().equals("")
                    || number.getText().equals("") || goals.getText().equals("")
                    || assists.getText().equals("")) {
                throw new NotAllFieldsEnteredException();
            }
            Player player = databaseController.findByNumber(Integer.parseInt(number.getText()));
            player.setTeam(databaseController.getClubFromDatabase(databaseController.teamName));
            player.setFirstName(name.getText());
            player.setSurname(surname.getText());
            player.setPosition(position.getText());
            player.setGoals(Integer.parseInt(goals.getText()));
            player.setAssists(Integer.parseInt(assists.getText()));
            databaseController.addPlayer(player);
            refreshPlayerTable();
        } else {
            System.out.println("Login canceled");
        }
    }

    void deletePlayerAction() throws RowNotSelectedException {
        if (table1.getSelectedRow() == -1)
            throw new RowNotSelectedException();
        System.out.println(table1.getSelectedRow());
        databaseController.deletePlayer(Integer.parseInt(
                table1.getValueAt(table1.getSelectedRow(), 2).toString()));
        refreshPlayerTable();
    }

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

        result.forEach( x -> {
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

    void addGameAction(){
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
            Calendar date = new GregorianCalendar(Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(0,4)),
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(5,7)) - 1,
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(8)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(0,2)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(3)));

            Game game = databaseController.addGame(opponent.getText(), date,
                    Integer.parseInt(yourGoals.getText()),Integer.parseInt(oppGoals.getText()));
            System.out.println(databaseController.teamName);
            refreshGameTable();
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(0,4));
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(5,7));
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(8));
            System.out.println(picker.timePicker.getTimeStringOrEmptyString());
        }
    }
    void editGameAction() {
        DateTimePicker picker = new DateTimePicker();
        JTextField opponent = new JTextField();
        JTextField yourGoals = new JTextField();
        JTextField oppGoals = new JTextField();
        picker.setEnabled(false);
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
        picker.datePicker.setDate(LocalDate.of(Integer.parseInt(data[1].toString().substring(0,4)),
                        Integer.parseInt(data[1].toString().substring(5,7)),
                        Integer.parseInt(data[1].toString().substring(8,10))));
        picker.timePicker.setTime(LocalTime.of(Integer.parseInt(data[1].toString().substring(11, 13)),
                Integer.parseInt(data[1].toString().substring(14, 16))));
        opponent.setText(data[0].toString());
        yourGoals.setText(data[2].toString());
        oppGoals.setText(data[3].toString());

        int option = JOptionPane.showConfirmDialog(null, message, "Edit Game", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (opponent.getText().equals("") || yourGoals.getText().equals("") || oppGoals.getText().equals("")
                    || picker.datePicker.getDateStringOrEmptyString().equals("")
                    || picker.timePicker.getTimeStringOrEmptyString().equals("")) {
                // throw new NotAllFieldsEnteredException();
            }
            //System.out.println(name.getText());
            Calendar date = new GregorianCalendar(Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(0,4)),
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(5,7)) - 1,
                    Integer.parseInt(picker.datePicker.getDateStringOrEmptyString().substring(8)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(0,2)),
                    Integer.parseInt(picker.timePicker.getTimeStringOrEmptyString().substring(3)));

            Game game = databaseController.findByDate(date);
            game.setOpponent(opponent.getText());
            game.setYourGoals(Integer.parseInt(yourGoals.getText()));
            game.setOpponentGoals(Integer.parseInt(oppGoals.getText()));
            databaseController.addGame(game);
            refreshGameTable();
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(0,4));
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(5,7));
            System.out.println(picker.datePicker.getDateStringOrEmptyString().substring(8));
            System.out.println(picker.timePicker.getTimeStringOrEmptyString());
        } else {
            System.out.println("Login canceled");
        }
    }

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

        result.forEach( x -> {
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
