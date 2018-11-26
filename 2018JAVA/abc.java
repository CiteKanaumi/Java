
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class abc extends JFrame {
  private JLabel label;
  
  public abc(){
    this.setSize(300, 300);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 

    label = new JLabel();

  }
  
  public static void main(String[] args) {
    new abc().setVisible(true);
  }
}
