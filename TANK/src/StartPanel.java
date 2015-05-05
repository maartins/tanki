import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class StartPanel extends JPanel implements ActionListener{
	
	private JButton startButton = new JButton("Sakt speli");
	
	public StartPanel(){
		this.setBounds(0, 0, 518, 582);
		this.setLayout(null);
		
		startButton.setBounds(5, 5, 300, 30);
		startButton.addActionListener(this);
		this.add(startButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(startButton)){
			
		}
	}
}
