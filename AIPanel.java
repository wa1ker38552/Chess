import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AIPanel extends JPanel {
	JButton easy;
	JButton hard;
	
	public AIPanel() {
		JPanel selectionContainer = new JPanel();
		this.easy = new JButton("Easy AI coded from sratch (very fast)");
		this.hard = new JButton("Hard AI (stockfish, a bit laggy)");
		
		easy.setBackground(Color.GREEN);
		
		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hard.setBackground(null);
				easy.setBackground(Color.GREEN);
			}
		});
		
		hard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				easy.setBackground(null);
				hard.setBackground(Color.GREEN);
			}
		});
		
		this.setLayout(new GridLayout(3, 1));
		selectionContainer.setLayout(new GridLayout(1, 2));
		
		this.add(new JLabel("You may need to play one more turn as black until the AI starts playing."));
		this.add(new JLabel("When playing with stockfish, it's highly recommended that you turn off move highlighting to reduce lag!"));
		selectionContainer.add(easy);
		selectionContainer.add(hard);
		this.add(selectionContainer);
	}
	
	public JButton getEasyButton() {
		return this.easy;
	}
	
	public JButton getHardButton() {
		return this.hard;
	}
}
