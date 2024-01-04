import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class BuscaMinas extends JFrame {
  private JButton[][] board;
  private int[][] filledBoard;
  private int minas;
  private int contadorMinas;
  private Dimension buttonSize;
  private JButton restartGame;
  private JLabel counterBombs;

  private int puntuacionBanderas;
  private ImageIcon flagIcon;
  private Image flagImage;
  private ImageIcon scaledFlagIcon;

  public BuscaMinas() {
    this.buttonSize = new Dimension(25, 25);
    this.flagIcon = new ImageIcon("./img/flag.png");
    this.flagImage = flagIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
    this.scaledFlagIcon = new ImageIcon(flagImage);
    this.minas = ((int)(Math.random() * 30) + 10) ;
    this.contadorMinas = minas;
    this.filledBoard = new int[9][9];
    this.board = new JButton[9][9];
    this.puntuacionBanderas = 0;

    initcomponents();
    crearTableroLleno();
  }

  public void initcomponents() {
    setTitle("Busca Minas");
    setSize(300, 400);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    Image icon = Toolkit.getDefaultToolkit().getImage("./img/bomba.png");
    icon = icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    setIconImage(icon);


    JPanel panel = new JPanel();
    GridBagLayout container = new GridBagLayout();
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    panel.setLayout(container);
    GridBagConstraints constraints = new GridBagConstraints();

    this.counterBombs = new JLabel("0" + contadorMinas + "");
    this.counterBombs.setBorder(new EmptyBorder(5, 10, 5, 10));
    this.counterBombs.setFont(new Font("Arial", Font.BOLD, 29));
    this.counterBombs.setForeground(Color.RED);
    this.counterBombs.setOpaque(true);
    this.counterBombs.setBackground(Color.BLACK);
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.gridwidth = 3;
    constraints.gridheight = 1;
    constraints.weightx = 1;
    constraints.weighty = 1;
    panel.add(this.counterBombs, constraints);

    this.restartGame = new JButton();
    this.restartGame.setBorder(new EmptyBorder(5, 10, 5, 10));
    ImageIcon restartIcon = new ImageIcon("./img/ok.png");
    ImageIcon restartIconResize = new ImageIcon(restartIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
    this.restartGame.setFocusPainted(false);
    this.restartGame.setIcon(restartIconResize);
    constraints.gridx = 3;
    constraints.gridy = 0;
    constraints.gridwidth = 3;
    constraints.gridheight = 1;
    constraints.weightx = 1;
    constraints.weighty = 1;
    MouseListener restartListener = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
          System.out.println("*****************");
          System.out.println("Reiniciar juego");
          reiniciarJuego();
        }
      }
    };
    this.restartGame.addMouseListener(restartListener);
    panel.add(this.restartGame, constraints);

    JLabel counterTime = new JLabel("000");
    counterTime.setBorder(new EmptyBorder(5, 10, 5, 10));
    counterTime.setFont(new Font("Arial", Font.BOLD, 29));
    counterTime.setForeground(Color.RED);
    counterTime.setOpaque(true);
    counterTime.setBackground(Color.BLACK);
    constraints.gridx = 6;
    constraints.gridy = 0;
    constraints.gridwidth = 3;
    constraints.gridheight = 1;
    constraints.weightx = 1;
    constraints.weighty = 1;
    panel.add(counterTime, constraints);

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        final int row = i;
        final int column = j;
        board[i][j] = new JButton();
        board[i][j].setPreferredSize(buttonSize);
        constraints.gridx = j;
        constraints.gridy = i + 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        board[i][j].setFocusPainted(false);
        panel.add(board[i][j], constraints);
        MouseListener mouseListener = new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
              System.out.println("Click en posicion " + row + " " + column);
              descubrirCelda(row, column);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
              putFlag(row, column);
            }
          }
        };
        board[i][j].addMouseListener(mouseListener);
      }
    }

    add(panel);
    revalidate();
    setVisible(true);
    repaint();
  }

  public void crearTableroLleno() {
    int contador = 0;
    while (contador < this.minas) {
      int row = (int)(Math.random() * (this.filledBoard.length));
      int column = (int)(Math.random() * ((this.filledBoard).length));
      if (this.filledBoard[row][column] == 0) {
        this.filledBoard[row][column] = 9;
        contador++;
      }
    }
    completarTablero();
    for (int i = 0; i < this.filledBoard.length; i++) {
      for (int j = 0; j < (this.filledBoard).length; j++){
        System.out.print("[" + this.filledBoard[i][j] + "]");
      }
      System.out.println();
    }
  }

  public void completarTablero() {
    int limiteFilas = this.filledBoard.length;
    int limiteColumnas = (this.filledBoard).length;
    for (int i = 0; i < limiteFilas; i++) {
      for (int j = 0; j < limiteColumnas; j++) {
        if (this.filledBoard[i][j] == 0) {
          int contador = 0;
          if (i - 1 >= 0 && j - 1 >= 0 && this.filledBoard[i - 1][j - 1] == 9)
            contador++;
          if (i - 1 >= 0 && this.filledBoard[i - 1][j] == 9)
            contador++;
          if (i - 1 >= 0 && j + 1 < limiteColumnas && this.filledBoard[i - 1][j + 1] == 9)
            contador++;
          if (j - 1 >= 0 && this.filledBoard[i][j - 1] == 9)
            contador++;
          if (j + 1 < limiteColumnas && this.filledBoard[i][j + 1] == 9)
            contador++;
          if (i + 1 < limiteFilas && j - 1 >= 0 && this.filledBoard[i + 1][j - 1] == 9)
            contador++;
          if (i + 1 < limiteFilas && this.filledBoard[i + 1][j] == 9)
            contador++;
          if (i + 1 < limiteFilas && j + 1 < limiteColumnas && this.filledBoard[i + 1][j + 1] == 9)
            contador++;
          this.filledBoard[i][j] = contador;
        }
      }
    }
  }

  public void descubrirCelda(int row, int column) {
    if (this.filledBoard[row][column] == 0) {
      if (this.board[row][column].getIcon() != null) {
        System.out.println("Posicion ocupada por un icono espacio");
        return;
      }
      ImageIcon blankSpace = new ImageIcon("./img/espacio.png");
      Image resizedBlankSpace = blankSpace.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
      ImageIcon scaledBlankSpace = new ImageIcon(resizedBlankSpace);
      this.board[row][column].setIcon(scaledBlankSpace);
      this.board[row][column].setDisabledIcon(scaledBlankSpace);
      this.board[row][column].setEnabled(false);
      destaparCasillasAlrededor(row, column);
      verificarVictoria();
    }

    if (this.filledBoard[row][column] == 9) {
      if (this.board[row][column].getIcon() != null) {
        System.out.println("Posicion ocupada por un icono bomba");
        return;
      }
      destaparTablero();

      ImageIcon blankSpace = new ImageIcon("./img/bomba.png");
      Image resizedBlankSpace = blankSpace.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
      ImageIcon scaledBlankSpace = new ImageIcon(resizedBlankSpace);

      this.board[row][column].setIcon(scaledBlankSpace);
      this.board[row][column].setDisabledIcon(scaledBlankSpace);
      this.board[row][column].setEnabled(false);

      ImageIcon restartIcon = new ImageIcon("./img/death.png");
      ImageIcon restartIconResize = new ImageIcon(restartIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
      restartGame.setIcon(restartIconResize);

    }

    if (this.filledBoard[row][column] >= 1 && this.filledBoard[row][column] <= 8) {
      if (this.board[row][column].getIcon() != null) {
        System.out.println("Posicion ocupada por un icono");
        return;
      }
      ImageIcon numberIcon = new ImageIcon("./img/" + this.filledBoard[row][column] + ".png");
      Image resizedNumberIcon = numberIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
      ImageIcon scaledNumberIcon = new ImageIcon(resizedNumberIcon);
      this.board[row][column].setIcon(scaledNumberIcon);
      this.board[row][column].setDisabledIcon(scaledNumberIcon);
      this.board[row][column].setEnabled(false);
      verificarVictoria();
    }
  }

  public void putFlag(int row, int column) {
    if (board[row][column].isEnabled()) {
      if (contadorMinas <= 0) {
        System.out.println("No se pueden poner mas banderas");
      } else if ((board[row][column].getIcon() == null)) {
        System.out.println("**************");
        System.out.println("Bandera puesta en posicion " + row + " " + column);

        board[row][column].setIcon(scaledFlagIcon);
        board[row][column].setDisabledIcon(scaledFlagIcon);
        board[row][column].setEnabled(false);


        contadorMinas--;
        System.out.println("contadorMinas: " + contadorMinas);
        counterBombs.setText((contadorMinas < 10 ? "00" : "0") + contadorMinas + "");
        if (filledBoard[row][column] == 9) {
          puntuacionBanderas++;
        }
        verificarVictoria();
      } else {
        System.out.println("Posicion ocupada por un icono");
      }
    } else if ((board[row][column].getIcon().equals(scaledFlagIcon))) {
      System.out.println("**************");
      System.out.println("Bandera quitada en posicion " + row + " " + column);

      board[row][column].setEnabled(true);
      board[row][column].setIcon(null);


      contadorMinas++;
      System.out.println("contadorMinas: " + contadorMinas);
      counterBombs.setText((contadorMinas < 10 ? "00" : "0") + contadorMinas + "");
    }

  }

  public void reiniciarJuego() {
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < (this.board[0]).length; j++) {
        this.filledBoard[i][j] = 0;
        this.board[i][j].setEnabled(true);
        this.board[i][j].setIcon(null);
        this.board[i][j].setBackground(null);
      }
    }
    this.minas = ((int)(Math.random() * 30) + 10);
    this.contadorMinas = this.minas;
    this.counterBombs.setText((this.contadorMinas < 10 ? "00" : "0") + this.contadorMinas + "");
    ImageIcon restartIcon = new ImageIcon("./img/ok.png");
    ImageIcon restartIconResize = new ImageIcon(restartIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
    this.restartGame.setFocusPainted(false);
    this.restartGame.setIcon(restartIconResize);
    this.puntuacionBanderas = 0;
    crearTableroLleno();
  }

  public void destaparTablero() {
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < this.board.length; j++) {
        int row = i;
        int column = j;
        if (this.filledBoard[row][column] == 0) {
          ImageIcon blankSpace = new ImageIcon("./img/espacio.png");
          Image resizedBlankSpace = blankSpace.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
          ImageIcon scaledBlankSpace = new ImageIcon(resizedBlankSpace);
          this.board[row][column].setIcon(scaledBlankSpace);
          this.board[row][column].setDisabledIcon(scaledBlankSpace);
          this.board[row][column].setEnabled(false);
        }

        if (this.filledBoard[row][column] == 9) {
          if (this.board[row][column].getIcon() != null) {
            this.puntuacionBanderas++;
            ImageIcon discoveredMine = new ImageIcon("./img/nice.png");
            Image resizedDiscoveredMine = discoveredMine.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            ImageIcon scaledDiscoveredMine = new ImageIcon(resizedDiscoveredMine);
            this.board[row][column].setIcon(scaledDiscoveredMine);
            this.board[row][column].setDisabledIcon(scaledDiscoveredMine);
            this.board[row][column].setEnabled(false);
            this.board[row][column].setBackground(Color.GREEN);
            continue;
          }
          ImageIcon bomb = new ImageIcon("./img/bomba.png");
          Image resizedBomb = bomb.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
          ImageIcon scaledBomb = new ImageIcon(resizedBomb);
          this.board[row][column].setIcon(scaledBomb);
          this.board[row][column].setDisabledIcon(scaledBomb);
          this.board[row][column].setEnabled(false);
          this.board[row][column].setBackground(Color.RED);
        }

        if (this.filledBoard[row][column] >= 1 && this.filledBoard[row][column] <= 8) {
          ImageIcon numberIcon = new ImageIcon("./img/" + this.filledBoard[row][column] + ".png");
          Image resizedNumberIcon = numberIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
          ImageIcon scaledNumberIcon = new ImageIcon(resizedNumberIcon);
          this.board[row][column].setIcon(scaledNumberIcon);
          this.board[row][column].setDisabledIcon(scaledNumberIcon);
          this.board[row][column].setEnabled(false);
        }
      }
    }
    //agregar alerta con cantidad de minas descubiertas
    //JOptionPane.showMessageDialog(null, "KABOOM");
  }

  public void destaparCasillasAlrededor(int row, int column) {
    int limiteFilas = this.filledBoard.length;
    int limiteColumnas = this.filledBoard[0].length;

    for (int i = row - 1; i <= row + 1; i++) {
      for (int j = column - 1; j <= column + 1; j++) {
        if (i >= 0 && i < limiteFilas && j >= 0 && j < limiteColumnas && !(i == row && j == column)) {
          if (this.filledBoard[i][j] == 0 && this.board[i][j].isEnabled()) {
            ImageIcon blankSpace = new ImageIcon("./img/espacio.png");
            Image resizedBlankSpace = blankSpace.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            ImageIcon scaledBlankSpace = new ImageIcon(resizedBlankSpace);
            this.board[i][j].setIcon(scaledBlankSpace);
            this.board[i][j].setDisabledIcon(scaledBlankSpace);
            this.board[i][j].setEnabled(false);
            destaparCasillasAlrededor(i, j);
          } else if (this.filledBoard[i][j] >= 1 && this.filledBoard[i][j] <= 8 && this.board[i][j].isEnabled()) {
            ImageIcon numberIcon = new ImageIcon("./img/" + this.filledBoard[i][j] + ".png");
            Image resizedNumberIcon = numberIcon.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            ImageIcon scaledNumberIcon = new ImageIcon(resizedNumberIcon);
            this.board[i][j].setIcon(scaledNumberIcon);
            this.board[i][j].setDisabledIcon(scaledNumberIcon);
            this.board[i][j].setEnabled(false);
          }
        }
      }
    }
  }

  public void verificarVictoria() {
    boolean todasDescubiertas = true;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (filledBoard[i][j] != 9 && board[i][j].isEnabled()) {
          todasDescubiertas = false;
          break;
        }
      }
    }
    String mensaje = "";
    System.out.println("contador: "+ this.contadorMinas);
    System.out.println("Banderas: "+this.puntuacionBanderas);
    int puntuacion = this.puntuacionBanderas;
    puntuacion *= 4;

    if(todasDescubiertas){
      ImageIcon winnerIcon = new ImageIcon("./img/winner.png");
      ImageIcon winnerResize = new ImageIcon(winnerIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
      this.restartGame.setIcon(winnerResize);
      destaparTablero();
      mensaje = "¡Has ganado el juego!\n";
    }

    mensaje += "Minas descubiertas: "+ this.puntuacionBanderas + "\n";
    mensaje += "Minas por descubrir: "+ this.contadorMinas + "\n";
    mensaje += "Puntuación: " + puntuacion + "\n";
    mensaje += "¿Deseas reiniciar el juego?";

    int opcion = 0;
    if (todasDescubiertas) {
      opcion = JOptionPane.showOptionDialog(this, mensaje, "¡Victoria!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
      
      if (opcion == JOptionPane.YES_OPTION) {
        reiniciarJuego();
      }
    } 
  }
  /*  */
}